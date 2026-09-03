#pragma once

#include "godot_cpp/classes/button.hpp"
#include "godot_cpp/classes/control.hpp"
#include "godot_cpp/classes/wrapped.hpp"

#include <array>
#include <functional>

using namespace godot;

class GodotRdExtension : public RefCounted {
	GDCLASS(GodotRdExtension, RefCounted)
	static constexpr std::array<const char *, 4> editable_extensions{ "tres", "res", "gdshader", "gdshaderinc" };
	std::function<void(Node *)> new_node_opened;
	bool _external_editor_turned_on() const;

	void _scene_saved(const String &);

public:
	void set_new_node_opened_callback(std::function<void(Node *)> callback);

	void open_in_godot(const String &);
	void scene_saved(const String &);
	bool is_scene(const String &extension) const;

protected:
	static void _bind_methods();
};
