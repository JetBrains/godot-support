#include "godot_rd_server.h"

#include "utils.h"
#include "std/to_string.h"
#include "godot_cpp/classes/dir_access.hpp"
#include "godot_cpp/classes/editor_interface.hpp"
#include "godot_cpp/classes/editor_paths.hpp"
#include "godot_cpp/classes/os.hpp"
#include "godot_cpp/variant/utility_functions.hpp"
#include "spdlog/spdlog.h"

#include <utility>
#include <vector>


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
		run_on_godot_thread([this, is_connected] { _notify_client_state(is_connected); });
	});
	const auto godot_editor_pid = OS::get_singleton()->get_process_id();
	tmp_session->bind_model(model, [this, godot_editor_pid](JetBrains::GodotPlugin::FrontendGodotModel &model, Lifetime lifetime) {
		model.get_openInGodot().advise(lifetime, [this](const std::wstring &path) {
			run_on_godot_thread([this, path] { open_in_godot(Utils::to_godot_string(path)); });
		});
		model.get_godotPID().set(godot_editor_pid);
		model.get_stopPlayingScene().advise(lifetime, [this](const Void &) {
			UtilityFunctions::print_verbose("[RIDER RD] stop playing scene requested");
			run_on_godot_thread([this] { stop_playing_scene(); });
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

void GodotRdServer::request_debug_current_scene() {
	auto *extension = get_extension();
	if (extension == nullptr || !extension->can_debug_current_scene()) {
		return;
	}

	std::lock_guard guard(session_mutex);
	if (!session) {
		UtilityFunctions::push_warning("Rider is not connected. Connect Rider and try again.");
		return;
	}
	const size_t starting_id = session->get_id();
	const Lifetime lifetime = session->get_model_lifetime();
	const bool queued = session->queue([this, starting_id, lifetime] {
		{
			std::lock_guard guard(session_mutex);
			if (!session || session->get_id() != starting_id) {
				return;
			}
		}

		auto response_lifetime = std::make_shared<LifetimeDefinition>(lifetime);
		auto task = model.get_prepareDebugSession().start(Void{});
		task.advise(response_lifetime->lifetime,
				[this, starting_id, response_lifetime, task](const auto &result) {
					auto lifetime_guard = Utils::ScopeGuard([response_lifetime] { response_lifetime->terminate(); });
					if (!task.has_value()) {
						return;
					}
					if (!result.is_succeeded()) {
						if (!result.is_canceled()) {
							run_on_godot_thread([] {
								UtilityFunctions::push_warning("Rider could not prepare a debug session.");
							});
						}
						return;
					}

					const auto response = result.unwrap();
					switch (response.get_status()) {
						case JetBrains::GodotPlugin::DebugSessionStatus::Ready: {
							std::vector<std::pair<std::wstring, std::wstring>> environment;
							for (const auto &variable : response.get_environment()) {
								environment.emplace_back(variable->get_name(), variable->get_value());
							}
							const std::wstring session_id = response.get_sessionId();
							run_on_godot_thread([this, starting_id, session_id, environment = std::move(environment)] {
								Dictionary godot_environment;
								for (const auto &[name, value] : environment) {
									godot_environment[Utils::to_godot_string(name)] = Utils::to_godot_string(value);
								}
								{
									std::lock_guard guard(session_mutex);
									if (!session || session->get_id() != starting_id) {
										return;
									}
								}
								auto *extension = get_extension();
								auto on_stopped = [this, starting_id, session_id] {
									report_debug_scene_stopped(starting_id, session_id);
								};
								if (extension != nullptr && extension->play_current_scene_with_environment(godot_environment, std::move(on_stopped))) {
									return;
								}
								report_debug_launch_failed(starting_id, session_id);
							});
							break;
						}
						case JetBrains::GodotPlugin::DebugSessionStatus::Busy:
							run_on_godot_thread([] {
								UtilityFunctions::push_warning(
									"A Rider debug session is already starting or active. Stop it and try again.");
							});
							break;
						case JetBrains::GodotPlugin::DebugSessionStatus::Failed:
							run_on_godot_thread([] {
								UtilityFunctions::push_warning("Rider could not prepare a debug session.");
							});
							break;
					}
				});
	});
	if (!queued) {
		UtilityFunctions::push_warning("Rider could not start a debug request.");
	}
}

void GodotRdServer::report_debug_session_event(
		size_t starting_id,
		const std::wstring &session_id,
		std::function<void(const std::wstring &)> report
) {
	std::lock_guard guard(session_mutex);
	if (!session || session->get_id() != starting_id) {
		return;
	}
	session->queue([this, starting_id, session_id, report = std::move(report)] {
		std::lock_guard guard(session_mutex);
		if (!session || session->get_id() != starting_id) {
			return;
		}
		report(session_id);
	});
}

void GodotRdServer::report_debug_launch_failed(size_t starting_id, const std::wstring &session_id) {
	report_debug_session_event(starting_id, session_id, [this](const std::wstring &id) {
		model.get_debugLaunchFailed().fire(id);
	});
}

void GodotRdServer::report_debug_scene_stopped(size_t starting_id, const std::wstring &session_id) {
	report_debug_session_event(starting_id, session_id, [this](const std::wstring &id) {
		model.get_debugSceneStopped().fire(id);
	});
}

bool GodotRdServer::queue_on_session(std::function<void()> task) {
	std::lock_guard guard(session_mutex);
	return session != nullptr && session->queue(std::move(task));
}

void GodotRdServer::on_scene_changed(Node *node) {
	if (node == nullptr) {
		on_scene_closed("");
		return;
	}
	auto node_path = node->get_scene_file_path();
	queue_on_session([this, node_path] {
		UtilityFunctions::print_verbose(String("[RIDER RD] sending node path: ") + node_path);
		model.get_currentSceneChange().fire(rd::to_wstring(std::string(node_path.utf8().get_data())));
	});
}

void GodotRdServer::on_scene_closed(const String &) {
	queue_on_session([this] {
		model.get_currentSceneChange().fire(std::wstring());
	});
}


void GodotRdServer::run_on_godot_thread(std::function<void()> task) {
	{
		std::lock_guard guard(godot_thread_tasks_mutex);
		godot_thread_tasks.push_back(std::move(task));
	}
	// Godot checks the callable's object ID before dispatch, and stop() clears queued tasks.
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
		extension->clear_debug_scene_stopped_callback();
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
	if (!queue_on_session([this, path] {
		model.get_openInRider().fire(rd::to_wstring(std::string(path.utf8().get_data())));
	})) {
		ERR_PRINT(
				"[RIDER RD] Open in rider called before establishing a connection, is Rider running with the project opened?");
	}
}


void GodotRdServer::_bind_methods() {
}
