#include "utils.h"

using namespace godot;
String Utils::to_godot_string(const std::wstring &str) {
	return String(str.c_str());
}

void Utils::focus_godot() {
	DisplayServer *display = DisplayServer::get_singleton();

	if (display != nullptr) {
		display->window_move_to_foreground(DisplayServer::MAIN_WINDOW_ID);
	}
}
