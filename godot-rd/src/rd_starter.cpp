#include "rd_starter.h"

#include "filesystem_menu.h"
#include "godot_cpp/classes/center_container.hpp"
#include "godot_cpp/classes/editor_interface.hpp"
#include "godot_cpp/core/memory.hpp"
#include "godot_cpp/variant/utility_functions.hpp"


RdStarter::RdStarter() {
	callable_mp(this, &RdStarter::startup).call_deferred();
}

RdStarter::~RdStarter() {
	shutdown();
}

// The editor frees us before the extension is unloaded, so this only makes the teardown
// happen while everything is still in the tree. The destructor takes care of the rest.
void RdStarter::_exit_tree() {
	shutdown();
}

void RdStarter::startup() {
	if (server.is_valid()) {
		return;
	}
	if (!is_inside_tree() || EditorInterface::get_singleton() == nullptr) {
		// Godot can create instances that aren't in the main editor. 
		// We do not want to run our logic in these cases.
		return;
	}
	UtilityFunctions::print_verbose("[RIDER RD] starting up");
	server.instantiate();
	server->set_client_connected_callback([this] { on_client_connected(); });
	server->set_client_disconnected_callback([this] { on_client_disconnected(); });

	connect("scene_changed", callable_mp(*server, &GodotRdServer::on_scene_changed));
	connect("scene_saved", callable_mp(*server, &GodotRdServer::on_scene_saved));
	connect("scene_closed", callable_mp(*server, &GodotRdServer::on_scene_closed));

	menu.instantiate([this](const String &path) { server->open_in_rider(path); });

	server->start();
}

void RdStarter::shutdown() {
	if (server.is_null() && menu.is_null()) {
		return;
	}
	UtilityFunctions::print_verbose("[RIDER RD] shutting down");
	if (server.is_valid()) {
		server->set_client_connected_callback(std::function<void()>{});
		server->set_client_disconnected_callback(std::function<void()>{});
		disconnect("scene_changed", callable_mp(*server, &GodotRdServer::on_scene_changed));
		disconnect("scene_saved", callable_mp(*server, &GodotRdServer::on_scene_saved));
		disconnect("scene_closed", callable_mp(*server, &GodotRdServer::on_scene_closed));
		server->stop();
		menu->reset_callback();
		server.unref();
	}

	if (menu.is_valid() && is_menu_showing.is_set()) {
		remove_context_menu_plugin(menu);
		is_menu_showing.set_to(false);
	}
	menu.unref();
}

void RdStarter::on_client_connected() {
	if (!is_menu_showing.is_set()) {
		add_context_menu_plugin(EditorContextMenuPlugin::CONTEXT_SLOT_FILESYSTEM, menu);
		is_menu_showing.set();
	}
	if (auto *root = EditorInterface::get_singleton()->get_edited_scene_root(); root != nullptr) {
		server->on_scene_changed(root);
	} else {
		server->on_scene_closed("");
	}
	UtilityFunctions::print_verbose("[RIDER RD] client connected");
}

void RdStarter::on_client_disconnected() {
	if (is_menu_showing.is_set()) {
		remove_context_menu_plugin(menu);
		is_menu_showing.set_to(false);
	}
	UtilityFunctions::print_verbose("[RIDER RD] client disconnected");
}

void RdStarter::_bind_methods() {
}
