#include "external_editor_toggle_plugin.h"

using namespace godot;

void RiderEditorPlugin::_bind_methods() {
	ClassDB::bind_method(D_METHOD("_on_toggled", "pressed"), &RiderEditorPlugin::_on_toggled);
}

void RiderEditorPlugin::_enter_tree() {
	// Only available in editor
	const EditorInterface *editor_interface = EditorInterface::get_singleton();
	if (!editor_interface) {
		UtilityFunctions::print("RiderEditorPlugin: EditorInterface is null (not running in editor)?");
		return;
	}

	editor_settings = editor_interface->get_editor_settings();
	if (editor_settings.is_null()) {
		UtilityFunctions::printerr("RiderEditorPlugin: EditorSettings is null");
		return;
	}

	check_button = memnew(CheckButton);
	const bool current = editor_settings->get_setting(setting_key);
	check_button->set_pressed(current);
	check_button->set_text("Use Rider");
	check_button->set_tooltip_text("Shortcut for setting all the settings recommended for Rider");

	check_button->connect("toggled", Callable(this, "_on_toggled"));

	add_control_to_container(CONTAINER_TOOLBAR, check_button);
}

void RiderEditorPlugin::_exit_tree() {
	if (check_button) {
		remove_control_from_container(CONTAINER_TOOLBAR, check_button);
		memdelete(check_button);
		check_button = nullptr;
	}
	editor_settings.unref();
}

void RiderEditorPlugin::_on_toggled(bool p_pressed) {
	if (editor_settings.is_valid()) {
		editor_settings->set_setting(setting_key, p_pressed);
	}
}
