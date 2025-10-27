#pragma once

#include <godot_cpp/classes/editor_plugin.hpp>
#include <godot_cpp/classes/check_button.hpp>
#include <godot_cpp/classes/editor_interface.hpp>
#include <godot_cpp/classes/editor_settings.hpp>
#include <godot_cpp/core/class_db.hpp>
#include <godot_cpp/variant/utility_functions.hpp>

using namespace godot;

class RiderEditorPlugin : public EditorPlugin {
	GDCLASS(RiderEditorPlugin, EditorPlugin)

private:
	CheckButton *check_button = nullptr;
	Ref<EditorSettings> editor_settings;
	StringName setting_key = StringName("text_editor/external/use_external_editor");

protected:
	static void _bind_methods();

public:
	RiderEditorPlugin() = default;
	~RiderEditorPlugin() override = default;

	void _enter_tree() override;
	void _exit_tree() override;

	void _on_toggled(bool p_pressed);
};
