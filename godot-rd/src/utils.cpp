#ifdef _WIN32
#ifndef NOMINMAX
#define NOMINMAX
#endif
#include <windows.h>
#endif

#include "utils.h"

using namespace godot;

String Utils::to_godot_string(const std::wstring &str) {
	return String(str.c_str());
}

void Utils::focus_godot() {
	DisplayServer *display = DisplayServer::get_singleton();
	if (display == nullptr) {
		return;
	}

	// Windows doesn't bring up godot window from minimized state with just the window_move_to_foreground.
#ifdef _WIN32
	HWND hwnd = reinterpret_cast<HWND>(display->window_get_native_handle(
			DisplayServer::WINDOW_HANDLE, DisplayServer::MAIN_WINDOW_ID));
	if (hwnd != nullptr && IsIconic(hwnd)) {
		ShowWindow(hwnd, SW_RESTORE);
	}
#endif
	display->window_move_to_foreground(DisplayServer::MAIN_WINDOW_ID);
}
