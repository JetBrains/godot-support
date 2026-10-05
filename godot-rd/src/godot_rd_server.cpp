#include "godot_rd_server.h"

#include "utils.h"
#include "std/to_string.h"
#include "godot_cpp/classes/dir_access.hpp"
#include "godot_cpp/classes/editor_interface.hpp"
#include "godot_cpp/classes/editor_paths.hpp"
#include "godot_cpp/variant/utility_functions.hpp"
#include "spdlog/spdlog.h"


void GodotRdServer::start() noexcept {
	stop();
	stopping.set_to(false);
	if (auto extension = get_extension(); extension != nullptr) {
		extension->set_new_node_opened_callback([this](Node *node) { on_scene_changed(node); });
		if (!extension->is_connected("scene_changed", callable_mp(this, &GodotRdServer::on_scene_changed))) {
			extension->connect("scene_changed", callable_mp(this, &GodotRdServer::on_scene_changed));
			extension->connect("scene_closed", callable_mp(this, &GodotRdServer::on_scene_closed));
		}
	}
	// The scheduler registers a logger under its name, and the spdlog registry is a global which
	// outlives the extension being unloaded, so registering the same name again would throw.
	spdlog::drop(SERVER_NAME);
	std::unique_ptr<RdSession> tmp_session = RdSession::create_new_session();
	if (!tmp_session) {
		return;
	}
	auto port = tmp_session->get_port();
	tmp_session->set_connection_callback([port, this](const bool &is_connected) {
		if (stopping.is_set()) {
			return;
		}
		if (is_connected) {
			UtilityFunctions::print_verbose(
					String("[RIDER RD] Client connected to port: ") +
					String::num_int64(port));
		} else {
			UtilityFunctions::print_verbose(
					String("[RIDER RD] Client disconnected from port: ") +
					String::num_int64(port));
		}
		callable_mp(this, &GodotRdServer::_notify_client_state).call_deferred(is_connected);
	});
	tmp_session->bind_model(model, [this](JetBrains::GodotPlugin::FrontendGodotModel &model, Lifetime lifetime) {
		model.get_openInGodot().advise(lifetime, [this](const std::wstring &path) {
			callable_mp(this, &GodotRdServer::open_in_godot).call_deferred(Utils::to_godot_string(path));
		});
		model.get_playCurrentSceneForDebug().set(
				[this](Lifetime, const std::wstring &game_argument) -> RdTask<bool> {
					UtilityFunctions::print_verbose("[RIDER RD] play current scene for debug requested");
					RdTask<bool> task;
					// Protect against session changes before run_on_godot_thread runs.
					size_t starting_id;
					{
						std::lock_guard m(session_mutex);
						if (!session) {
							task.set(false);
							return task;
						}
						starting_id = session->get_id();
					}
					run_on_godot_thread([this, task, starting_id, argument = Utils::to_godot_string(game_argument)] {
						const bool playing = play_current_scene_for_debug(argument);
						// we don't need a lock here, since this runs on Godot thread.
						if (session && session->get_id() == starting_id) {
							session->queue([task, playing] { task.set(playing); });
						} else {
							session->queue([task] { task.set(false); });
							UtilityFunctions::print_verbose(
									"[RIDER RD] session gone or has changed before the play result could be sent");
						}
					});
					return task;
				});
		model.get_stopPlayingScene().advise(lifetime, [this](const Void &) {
			UtilityFunctions::print_verbose("[RIDER RD] stop playing scene requested");
			callable_mp(this, &GodotRdServer::stop_playing_scene).call_deferred();
		});
	});
	if (!write_connection_info(tmp_session->get_port())) {
		return;
	}
	std::lock_guard m(session_mutex);
	tmp_session.swap(session);
}

void GodotRdServer::set_client_connected_callback(std::function<void()> callback) {
	client_connected = std::move(callback);
}

void GodotRdServer::set_client_disconnected_callback(std::function<void()> callback) {
	client_disconnected = std::move(callback);
}

void GodotRdServer::_notify_client_state(bool is_connected) {
	if (is_connected) {
		if (client_connected) {
			client_connected();
		}
	} else if (client_disconnected) {
		client_disconnected();
	}
}

void GodotRdServer::on_scene_changed(Node *node) {
	if (!session) {
		return;
	}
	if (node == nullptr) {
		on_scene_closed("");
	} else {
		auto node_path = node->get_scene_file_path();
		session->queue([this,node_path] {
			UtilityFunctions::print_verbose(String("[RIDER RD] sending node path: ") + node_path);
			model.get_currentSceneChange().fire(rd::to_wstring(std::string(node_path.utf8().get_data())));
		});
	}
}

void GodotRdServer::on_scene_closed(const String &) {
	if (!session) {
		return;
	}
	session->queue([this] {
		model.get_currentSceneChange().fire(std::wstring());
	});
}


void GodotRdServer::run_on_godot_thread(std::function<void()> task) {
	{
		std::lock_guard guard(godot_thread_tasks_mutex);
		godot_thread_tasks.push_back(std::move(task));
	}
	callable_mp(this, &GodotRdServer::_run_godot_thread_tasks).call_deferred();
}

void GodotRdServer::_run_godot_thread_tasks() {
	std::vector<std::function<void()>> tasks;
	{
		std::lock_guard guard(godot_thread_tasks_mutex);
		tasks.swap(godot_thread_tasks);
	}
	for (const auto &task : tasks) {
		task();
	}
}

GodotRdExtension *GodotRdServer::get_extension() {
	auto *extension = GodotRdExtension::get_singleton();
	if (extension == nullptr) {
		ERR_PRINT("[RIDER RD] internal error, the editor plugin of the extension is not in the tree");
	}
	return extension;
}

bool GodotRdServer::play_current_scene_for_debug(const String &game_argument) {
	auto *extension = get_extension();
	return extension != nullptr && extension->play_current_scene_for_debug(game_argument);
}

void GodotRdServer::stop_playing_scene() {
	if (auto *extension = get_extension(); extension != nullptr) {
		extension->stop_playing_scene();
	}
}

void GodotRdServer::open_in_godot(const String &path) {
	if (auto *extension = get_extension(); extension != nullptr) {
		extension->open_in_godot(path);
	}
}

String GodotRdServer::get_port_file_path() const {
	auto settings_dir = EditorInterface::get_singleton()->get_editor_paths()->get_project_settings_dir();
	return settings_dir.path_join(Utils::to_godot_string(model.portFilename.data()));
}

bool GodotRdServer::write_connection_info(uint16_t port) {
	// The model hash lets Rider verify that both sides were generated from the same
	// protocol model, so it can refuse to connect instead of failing at runtime.
	// This can happen if a user has multiple versions of Rider installed and points
	// the editor plugin into one version and opens the project in another.
	String contents = Utils::to_godot_string(model.portKey.data()) + "=" + String::num_int64(port) + "\n" +
			Utils::to_godot_string(model.modelHashKey.data()) + "=" + String::num_int64(model.serializationHash) + "\n";
	return writer.write_port_info(get_port_file_path(), contents);
}

void GodotRdServer::stop() noexcept {
	stopping.set();
	if (auto extension = get_extension(); extension != nullptr) {
		get_extension()->set_new_node_opened_callback(std::function<void(Node *)>());
		if (extension->is_connected("scene_changed", callable_mp(this, &GodotRdServer::on_scene_changed))) {
			extension->disconnect("scene_changed", callable_mp(this, &GodotRdServer::on_scene_changed));
			extension->disconnect("scene_closed", callable_mp(this, &GodotRdServer::on_scene_closed));
		}
	}
	if (writer.owns_port_lock()) {
		DirAccess::remove_absolute(get_port_file_path());
	}
	std::unique_ptr<RdSession> old_session;
	{
		std::lock_guard m(session_mutex);
		// We cannot destroy the session while holding the lock
		// since session cleanup calls .flush() that waits for all the current calls to finish.
		// And if some call waits on the mutex, you get a deadlock.
		old_session.swap(session);
	}
	old_session.reset();
	std::lock_guard guard(godot_thread_tasks_mutex);
	godot_thread_tasks.clear();
}

void GodotRdServer::open_in_rider(const String &path) {
	if (!session) {
		ERR_PRINT(
				"[RIDER RD] Open in rider called before establishing a connection, is Rider running with the project opened?");
		return;
	}
	session->queue([this,path] {
		model.get_openInRider().fire(rd::to_wstring(std::string(path.utf8().get_data())));
	});
}


void GodotRdServer::_bind_methods() {
}
