#include "rd_starter.h"

#include "filesystem_menu.h"
#include "rider_toolbar_item.h"
#include "godot_cpp/classes/center_container.hpp"
#include "godot_cpp/classes/display_server.hpp"
#include "godot_cpp/classes/engine.hpp"
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
	if (!Engine::get_singleton()->has_singleton("DisplayServer")) {
		return;
	}
	DisplayServer *display = DisplayServer::get_singleton();
	if (display == nullptr || display->get_name() == "headless") {
		return;
	}
	UtilityFunctions::print_verbose("[RIDER RD] starting up");
	server.instantiate();
	server->set_client_connected_callback([this] { on_client_connected(); });
	server->set_client_disconnected_callback([this] { on_client_disconnected(); });

	menu.instantiate([this](const String &path) { server->open_in_rider(path); });
	toolbar_item.instantiate(
			[this] { server->request_debug_current_scene(); },
			[this] { server->stop_playing_scene(); },
			[] {
				auto *extension = GodotRdExtension::get_singleton();
				return extension != nullptr && extension->is_debug_scene_playing();
			});
	toolbar_item->attach();

	server->start();
}

void RdStarter::shutdown() {
	if (server.is_null() && menu.is_null() && toolbar_item.is_null()) {
		return;
	}
	UtilityFunctions::print_verbose("[RIDER RD] shutting down");
	if (server.is_valid()) {
		server->stop();
		menu->reset_callback();
		if (toolbar_item.is_valid()) {
			toolbar_item->reset_callback();
		}
		server.unref();
	}

	if (toolbar_item.is_valid()) {
		toolbar_item->detach();
		toolbar_item.unref();
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
	if (toolbar_item.is_valid()) {
		toolbar_item->set_enabled(true);
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
	if (toolbar_item.is_valid()) {
		toolbar_item->set_enabled(false);
	}
	UtilityFunctions::print_verbose("[RIDER RD] client disconnected");
}

void RdStarter::_bind_methods() {
}
