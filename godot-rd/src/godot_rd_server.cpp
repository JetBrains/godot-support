#include "godot_rd_server.h"

#include "utils.h"
#include "protocol/Identities.h"
#include "godot_cpp/classes/dir_access.hpp"
#include "godot_cpp/classes/editor_interface.hpp"
#include "godot_cpp/classes/editor_paths.hpp"
#include "godot_cpp/classes/file_access.hpp"
#include "godot_cpp/variant/utility_functions.hpp"
#include "spdlog/spdlog.h"


#include <filesystem>

void GodotRdServer::start() noexcept {
	// Utils::set_spdlog_to_trace();
	stop();
	stopping.set_to(false);
	// The scheduler registers a logger under its name, and the spdlog registry is a global which
	// outlives the extension being unloaded, so registering the same name again would throw.
	spdlog::drop(SERVER_NAME);
	session = RdSession::create_new_session();
	if (!session) {
		return;
	}
	auto port = session->get_port();
	session->set_connection_callback([port, this](const bool &is_connected) {
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
	session->bind_model(model, [this](JetBrains::GodotPlugin::FrontendGodotModel &model, Lifetime lifetime) {
		model.get_openInGodot().advise(lifetime, [this](const std::wstring &path) {
			callable_mp(this, &GodotRdServer::open_in_godot).call_deferred(Utils::to_godot_string(path));
		});
	});
	if (!write_connection_info(session->get_port())) {
		session.reset();
	}
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
			model.get_currentSceneChange().fire(Utils::to_wstring(node_path));
		});
	}
}

void GodotRdServer::on_scene_closed(const String &) {
	if (!session) {
		return;
	}
	session->queue([this] {
		model.get_currentSceneChange().fire(Utils::to_wstring(""));
	});
}

void GodotRdServer::on_scene_saved(const String &path) {
	extension->scene_saved(path);
}

void GodotRdServer::open_in_godot(const String &path) {
	extension->open_in_godot(path);
}

String GodotRdServer::get_port_file_path() const {
	auto settings_dir = EditorInterface::get_singleton()->get_editor_paths()->get_project_settings_dir();
	return settings_dir.path_join(Utils::to_godot_string(model.portFilename.data()));
}

bool GodotRdServer::write_connection_info(uint16_t port) const {
	auto port_file = FileAccess::open(get_port_file_path(), FileAccess::WRITE);
	if (port_file.is_null()) {
		ERR_PRINT(String("[RIDER RD] Failed to write into port file: ") +FileAccess::get_open_error());
		return false;
	}
	// The model hash lets Rider verify that both sides were generated from the same
	// protocol model, so it can refuse to connect instead of failing at runtime.
	// This can happen if a user has multiple versions of Rider installed and points
	// the editor plugin into one version and opens the project in another.
	bool ok = port_file->store_string(
			Utils::to_godot_string(model.portKey.data()) + "=" + String::num_int64(port) + "\n" +
			Utils::to_godot_string(model.modelHashKey.data()) + "=" + String::num_int64(model.serializationHash) + "\n");
	if (!ok) {
		ERR_PRINT(String("[RIDER RD] Failed to write into port file"));
	}
	port_file->close();
	return ok;
}

// This function can be without a mutex,
// since all of this runs on the Godot main thread.
// Meaning checking in functions for !scheduler is sufficient.
void GodotRdServer::stop() noexcept {
	stopping.set();
	DirAccess::remove_absolute(get_port_file_path());
	if (session) {
		session.reset();
	}
}

void GodotRdServer::open_in_rider(const String &path) {
	if (!session) {
		ERR_PRINT(
				"[RIDER RD] Open in rider called before establishing a connection, is Rider running with the project opened?");
		return;
	}
	session->queue([this,path] {
		model.get_openInRider().fire(Utils::to_wstring(path));
	});
}


void GodotRdServer::_bind_methods() {
}
