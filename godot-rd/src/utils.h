#pragma once
#include "godot_cpp/classes/display_server.hpp"
#include "godot_cpp/variant/string.hpp"

#include <functional>
#include <string>


namespace Utils {
using namespace godot;
String to_godot_string(const std::wstring &str);

void focus_godot();

bool is_csharp_project();

// Never ever use this with something that can throw
// since if an exception is being propagated and another exception
// is thrown Godot would be terminated. Always add try catches inside you callable.
class ScopeGuard {
	std::function<void()> callable;

public:
	explicit ScopeGuard(std::function<void()> callable) :
		callable(std::move(callable)) {
	}

	ScopeGuard(const ScopeGuard &) = delete;

	~ScopeGuard() {
		if (callable) {
			callable();
		}
	}
};
}
