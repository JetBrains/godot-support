#pragma once
#include "godot_cpp/classes/editor_context_menu_plugin.hpp"
#include "godot_cpp/classes/image_texture.hpp"

#include <functional>
#include <optional>

using namespace godot;

class FilesystemMenu : public EditorContextMenuPlugin {
	GDCLASS(FilesystemMenu, EditorContextMenuPlugin)
	std::function<void(const String &)> open_in_rider_callback;
	Ref<ImageTexture> rider_icon;

	// TODO: internationalize string?
	static constexpr auto OPEN_IN_RIDER = "Open in Rider";

	void _open_in_rider(const Array &selected_paths);

public:
	// This is necesary since Godot class register requires an empty arg ctor
	FilesystemMenu() {
	}

	FilesystemMenu(std::function<void(const String &)> callback) :
		open_in_rider_callback(callback) {
	}

	void _popup_menu(const PackedStringArray &p_paths) override;

	void reset_callback() {
		this->open_in_rider_callback = std::function<void(const String &)>();
	}

protected:
	static void _bind_methods();
};
