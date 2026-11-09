#pragma once

#include "godot_cpp/classes/button.hpp"
#include "godot_cpp/classes/control.hpp"
#include "godot_cpp/classes/editor_plugin.hpp"
#include "godot_cpp/classes/wrapped.hpp"

#include <array>
#include <functional>
#include <utility>

using namespace godot;

class GodotRdExtension : public EditorPlugin {
	GDCLASS(GodotRdExtension, EditorPlugin)
	static constexpr std::array<const char *, 4> editable_extensions{ "tres", "res", "gdshader", "gdshaderinc" };
	// The instance of an editor plugin is created and owned by the editor, so it is published here for the
	// classes which are not plugins themselves.
	static GodotRdExtension *singleton;

	std::function<void(Node *)> new_node_opened;
	mutable std::function<void()> debug_scene_stopped_callback;
	// The variables from Rider for the game process of the current debug launch.
	Dictionary launch_environment;
	// The values that the launch variables replaced. A key without a previous value is not in the dictionary.
	// The members are mutable, because the editor calls the _run_scene() override through a const method.
	mutable Dictionary replaced_environment;
	mutable bool launch_environment_applied = false;
	mutable bool debug_scene_launched = false;

	bool _external_editor_turned_on() const;
	void _apply_launch_environment() const;
	void restore_launch_environment() const;

	void _scene_saved(const String &);
	void report_debug_scene_stopped() const;

public:
	void set_new_node_opened_callback(std::function<void(Node *)> callback);

	static GodotRdExtension *get_singleton() { return singleton; }

	void _enter_tree() override;
	void _exit_tree() override;
	void _process(double) override;
	PackedStringArray _run_scene(const String &p_scene, const PackedStringArray &p_args) const override;

	void open_in_godot(const String &);
	void scene_saved(const String &);
	bool is_scene(const String &extension) const;
	int get_launch_instance_count() const;

	bool can_debug_current_scene() const;
	bool play_current_scene_with_environment(const Dictionary &environment, std::function<void()> on_stopped);
	bool is_debug_scene_playing() const;
	void stop_playing_scene();
	void clear_debug_scene_stopped_callback();

protected:
	static void _bind_methods();
};
