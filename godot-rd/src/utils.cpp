#include "utils.h"

#include "spdlog/spdlog.h"
using namespace godot;
String Utils::to_godot_string(const std::wstring &str) {
	return String(str.c_str());
}

std::wstring Utils::to_wstring(const String &str) {
	auto wide_string = str.wide_string();
	return std::wstring(wide_string, wide_string.length());
}

void Utils::set_spdlog_to_trace() {
	spdlog::set_level(spdlog::level::trace);
	spdlog::flush_on(spdlog::level::trace);
}

void Utils::focus_godot() {
	DisplayServer *display = DisplayServer::get_singleton();

	if (display != nullptr) {
		display->window_move_to_foreground(DisplayServer::MAIN_WINDOW_ID);
	}
}
