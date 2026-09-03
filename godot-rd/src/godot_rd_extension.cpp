#include "godot_rd_extension.h"

#include "utils.h"
#include "godot_cpp/classes/config_file.hpp"
#include "godot_cpp/classes/editor_interface.hpp"
#include "godot_cpp/classes/editor_settings.hpp"
#include "godot_cpp/classes/file_system_dock.hpp"
#include "godot_cpp/classes/project_settings.hpp"
#include "godot_cpp/classes/resource_loader.hpp"
#include "godot_cpp/classes/script.hpp"


bool GodotRdExtension::is_scene(const String &extension) const {
	return extension == "tscn" || extension == "scn";
}

bool GodotRdExtension::_external_editor_turned_on() const {
	auto settings = EditorInterface::get_singleton()->get_editor_settings();
	return settings->get_setting("text_editor/external/use_external_editor");
}

void GodotRdExtension::open_in_godot(const String &path) {
	UtilityFunctions::print_verbose("[RIDER RD] open in godot ran with: " + path);
	const String resource_path = ProjectSettings::get_singleton()->localize_path(path);
	auto extension = path.get_extension();
	auto *editor = EditorInterface::get_singleton();
	auto focus_guard = Utils::ScopeGuard([] { Utils::focus_godot(); });
	if (is_scene(extension)) {
		editor->open_scene_from_path(resource_path);
		return;
	}
	auto focus_in_dock = [editor, resource_path] {
		// Note this is safe even when the file system dock is closed.
		auto *dock = editor->get_file_system_dock();
		dock->navigate_to_path(resource_path);
		dock->make_visible();
	};
	auto *resource_loader = ResourceLoader::get_singleton();
	if (!resource_loader->exists(resource_path)) {
		focus_in_dock();
		return;
	}
	if (extension == "gd") {
		// If the user has set Rider as external editor, then opening the
		// script with edit_script just pings back to rider.
		if (_external_editor_turned_on()) {
			focus_in_dock();
			return;
		}
		Ref<Script> script = resource_loader->load(resource_path);
		editor->edit_script(script);
		return;
	}
	// Godot does not have good support for cs files
	// it opens them pretty much as text files.
	if (extension == "cs") {
		focus_in_dock();
		return;
	}
	Ref<Resource> resource = resource_loader->load(resource_path);
	for (const auto editable_ext : editable_extensions) {
		if (extension == editable_ext) {
			editor->edit_resource(resource.ptr());
			return;
		}
	}
	focus_in_dock();
}

void GodotRdExtension::set_new_node_opened_callback(std::function<void(Node *)> callback) {
	new_node_opened = std::move(callback);
}

void GodotRdExtension::_scene_saved(const String &path) {
	if (auto *root = EditorInterface::get_singleton()->get_edited_scene_root(); root != nullptr) {
		if (String(root->get_scene_file_path()) == path && new_node_opened) {
			new_node_opened(root);
		}
	}
}

void GodotRdExtension::scene_saved(const String &path) {
	callable_mp(this, &GodotRdExtension::_scene_saved).call_deferred(path);
}

void GodotRdExtension::_bind_methods() {
}
