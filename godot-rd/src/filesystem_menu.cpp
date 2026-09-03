#include "filesystem_menu.h"

#include "godot_cpp/classes/project_settings.hpp"


void FilesystemMenu::_open_in_rider(const Array &selected_paths) {
	if (!open_in_rider_callback) {
		ERR_PRINT("[RIDER RD] internal error: filesystem menu item called without setting callback");
		return;
	}
	for (int i = 0; i < selected_paths.size(); ++i) {
		const String resource_path = selected_paths[i];
		const String absolute_path = ProjectSettings::get_singleton()->globalize_path(resource_path);
		open_in_rider_callback(absolute_path);
	}
}

void FilesystemMenu::_popup_menu(const PackedStringArray &p_paths) {
	if (p_paths.is_empty()) {
		return;
	}
	if (rider_icon.is_null()) {
		Ref<Image> image;
		image.instantiate();

		const Error error =
				image->load_svg_from_string(String::utf8(RIDER_ICON_SVG), 1.0);

		if (error != OK) {
			UtilityFunctions::print_verbose(
					"[RIDER RD] Failed to decode the embedded SVG icon: " + UtilityFunctions::error_string(error)
					);
			add_context_menu_item(OPEN_IN_RIDER, callable_mp(this, &FilesystemMenu::_open_in_rider));
			return;
		}
		rider_icon = ImageTexture::create_from_image(image);
	}
	add_context_menu_item(OPEN_IN_RIDER, callable_mp(this, &FilesystemMenu::_open_in_rider), rider_icon);
}

void FilesystemMenu::_bind_methods() {
}
