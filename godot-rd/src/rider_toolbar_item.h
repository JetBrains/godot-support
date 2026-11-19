#pragma once
#include "godot_cpp/classes/image_texture.hpp"
#include "godot_cpp/classes/node.hpp"
#include "godot_cpp/classes/popup_menu.hpp"
#include "godot_cpp/classes/ref_counted.hpp"
#include "godot_cpp/classes/texture2d.hpp"

#include <functional>
#include <utility>

using namespace godot;

/**
 * Adds a debug or stop item to the `RiderToolbarMenu` popup in C# projects.
 * Does nothing if the Rider addon is not enabled.
 */
class RiderToolbarItem : public RefCounted {
	GDCLASS(RiderToolbarItem, RefCounted)
	// High IDs keep the item clear of the item IDs of the addon.
	static constexpr int ITEM_ID = 1000;
	static constexpr int SEPARATOR_ID = 1001;
	static constexpr auto TOOLBAR_MENU_CLASS = "RiderToolbarMenu";
	static constexpr auto DEBUG_CURRENT_SCENE = "Debug Current Scene with Rider (C#)";
	static constexpr auto DEBUG_CURRENT_SCENE_TOOLTIP = "Starts the current scene with the Rider C# debugger.";
	static constexpr auto STOP_DEBUGGING = "Stop Rider C# Debugging";
	static constexpr auto STOP_DEBUGGING_TOOLTIP = "Stops the current scene that runs with the Rider C# debugger.";
	// On macOS, a window change while the popup window closes can crash AppKit, so the request waits for the popup to close.
	static constexpr double PRESS_DELAY_SEC = 0.3;

	std::function<void()> pressed_callback;
	std::function<void()> stop_callback;
	std::function<bool()> is_debugging_callback;
	ObjectID popup_id;
	Ref<ImageTexture> debug_icon;
	bool enabled = false;
	bool attached = false;
	bool showing_stop_action = false;
	bool pressed_stop_action = false;

	PopupMenu *get_popup() const;
	void add_item(Node *node);
	void update_item();
	void remove_item();
	static void remove_popup_items(PopupMenu *popup);
	static bool is_toolbar_menu(Node *node);
	static Node *find_toolbar_menu(Node *node);

	void _on_node_added(Node *node);
	void _on_about_to_popup();
	void _on_id_pressed(int64_t id);
	void _on_press_delay_timeout();

public:
	// This is necessary since Godot class register requires an empty arg ctor
	RiderToolbarItem() {
	}

	RiderToolbarItem(std::function<void()> debug_callback, std::function<void()> stop_callback,
			std::function<bool()> is_debugging_callback) :
		pressed_callback(std::move(debug_callback)),
		stop_callback(std::move(stop_callback)),
		is_debugging_callback(std::move(is_debugging_callback)) {
	}

	void attach();
	void detach();
	void set_enabled(bool value);

	void reset_callback() {
		pressed_callback = std::function<void()>();
		stop_callback = std::function<void()>();
		is_debugging_callback = std::function<bool()>();
	}

protected:
	static void _bind_methods();
};
