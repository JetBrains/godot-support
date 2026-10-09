#include "godot_rd_extension.h"

#include "utils.h"
#include "godot_cpp/classes/config_file.hpp"
#include "godot_cpp/classes/editor_interface.hpp"
#include "godot_cpp/classes/editor_settings.hpp"
#include "godot_cpp/classes/file_system_dock.hpp"
#include "godot_cpp/classes/project_settings.hpp"
#include "godot_cpp/classes/resource_loader.hpp"
#include "godot_cpp/classes/script.hpp"


GodotRdExtension *GodotRdExtension::singleton = nullptr;

void GodotRdExtension::_enter_tree() {
	singleton = this;
	// Saves are also required for tracking the current scene, since if you just create a new node and then save,
	// the happy path is scene_changed(nullptr) -> scene_saved("<path>"). Deferred, so that the editor is done
	// saving by the time the edited scene is looked at.
	connect("scene_saved", callable_mp(this, &GodotRdExtension::_scene_saved), CONNECT_DEFERRED);
}

void GodotRdExtension::_exit_tree() {
	disconnect("scene_saved", callable_mp(this, &GodotRdExtension::_scene_saved));
	pending_launch_argument = String();
	if (singleton == this) {
		singleton = nullptr;
	}
}

bool GodotRdExtension::is_scene(const String &extension) const {
	return extension == "tscn" || extension == "scn";
}

int GodotRdExtension::get_launch_instance_count() const {
	const auto settings = EditorInterface::get_singleton()->get_editor_settings();
	const bool multiple = settings->get_project_metadata(
			"debug_options", "multiple_instances_enabled", false);
	return multiple
			? static_cast<int>(settings->get_project_metadata("debug_options", "run_instance_count", 1))
			: 1;
}


bool GodotRdExtension::play_current_scene_for_debug(const String &game_argument) {
	auto *editor = EditorInterface::get_singleton();
	if (editor->is_playing_scene()) {
		editor->stop_playing_scene();
	}
	// Godot can be set to launch multiple instances at once, this would make it so one gets attached,
	// and the others stay hanging.
	if (get_launch_instance_count() > 1) {
		ERR_PRINT("[RIDER RD] Cannot debug in editor with multiple instances. Please disable 'Enable Multiple Instances'");
		return false;
	}
	pending_launch_argument = game_argument;
	editor->play_current_scene();
	const bool playing = editor->is_playing_scene();
	if (!playing) {
		UtilityFunctions::print_verbose("[RIDER RD] the editor did not play the current scene");
		pending_launch_argument = String();
	}
	return playing;
}

void GodotRdExtension::stop_playing_scene() {
	auto *editor = EditorInterface::get_singleton();
	if (editor->is_playing_scene()) {
		editor->stop_playing_scene();
	}
}

PackedStringArray GodotRdExtension::_run_scene(const String &, const PackedStringArray &args) const {
	PackedStringArray result = args;
	if (!pending_launch_argument.is_empty()) {
		UtilityFunctions::print_verbose("[RIDER RD] launching the game with: " + pending_launch_argument);
		result.push_back(pending_launch_argument);
		pending_launch_argument = String();
	}
	return result;
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
	auto focus_in_dock = [editor, resource_path] {
		// Note this is safe even when the file system dock is closed.
		auto *dock = editor->get_file_system_dock();
		dock->navigate_to_path(resource_path);
		dock->make_visible();
	};
	// Always reveal the file in the dock, regardless of how it ends up being opened.
	auto focus_guard = Utils::ScopeGuard([&focus_in_dock] {
		focus_in_dock();
		Utils::focus_godot();
	});
	
	if (is_scene(extension)) {
		editor->open_scene_from_path(resource_path);
		return;
	}
	
	auto *resource_loader = ResourceLoader::get_singleton();
	if (!resource_loader->exists(resource_path)) {
		return;
	}
	if (extension == "gd") {
		// If the user has set Rider as external editor, then opening the
		// script with edit_script just pings back to rider.
		if (_external_editor_turned_on()) {
			return;
		}
		Ref<Script> script = resource_loader->load(resource_path);
		// edit_script only opens the script in the script editor, it does not switch the main screen to it.
		editor->set_main_screen_editor("Script");
		editor->edit_script(script);
		return;
	}
	// Godot does not have good support for cs files
	// it opens them pretty much as text files.
	if (extension == "cs") {
		return;
	}
	Ref<Resource> resource = resource_loader->load(resource_path);
	for (const auto editable_ext : editable_extensions) {
		if (extension == editable_ext) {
			editor->edit_resource(resource.ptr());
			return;
		}
	}
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
