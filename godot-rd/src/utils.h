#pragma once
#include "godot_cpp/classes/display_server.hpp"
#include "godot_cpp/variant/string.hpp"

#include <functional>
#include <string>


namespace Utils {
using namespace godot;
String to_godot_string(const std::wstring &str);

std::wstring to_wstring(const String &str);

void set_spdlog_to_trace();

void focus_godot();

class ScopeGuard {
	std::function<void()> callable;

public:
	explicit ScopeGuard(std::function<void()> callable) :
		callable(callable) {
	}

	ScopeGuard(const ScopeGuard &) = delete;

	~ScopeGuard() {
		if (callable) {
			callable();
		}
	}
};
}
