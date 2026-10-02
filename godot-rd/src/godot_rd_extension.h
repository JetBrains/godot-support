#pragma once

#include "godot_cpp/classes/button.hpp"
#include "godot_cpp/classes/control.hpp"
#include "godot_cpp/classes/editor_plugin.hpp"
#include "godot_cpp/classes/wrapped.hpp"

#include <array>
#include <functional>

using namespace godot;

class GodotRdExtension : public EditorPlugin {
	GDCLASS(GodotRdExtension, EditorPlugin)
	static constexpr std::array<const char *, 4> editable_extensions{ "tres", "res", "gdshader", "gdshaderinc" };
	// The instance of an editor plugin is created and owned by the editor, so it is published here for the
	// classes which are not plugins themselves.
	static GodotRdExtension *singleton;
	// This is used to store our arguments that 
	// are then read in _run_scene. The issue is that
	// _run_scene consumes it but is const -> hence this being mutable to allow for clearing.
	mutable String pending_launch_argument;

	std::function<void(Node *)> new_node_opened;
	bool _external_editor_turned_on() const;

	void _scene_saved(const String &);

public:
	void set_new_node_opened_callback(std::function<void(Node *)> callback);

	static GodotRdExtension *get_singleton() { return singleton; }

	void _enter_tree() override;
	void _exit_tree() override;

	// Inserts arguments for CoreCLR diagnostic port for debugging C# game inside Godot.
	PackedStringArray _run_scene(const String &scene, const PackedStringArray &args) const override;

	void open_in_godot(const String &);
	void scene_saved(const String &);
	bool is_scene(const String &extension) const;
	int get_launch_instance_count() const;

	bool play_current_scene_for_debug(const String &game_argument);
	void stop_playing_scene();

protected:
	static void _bind_methods();
};
