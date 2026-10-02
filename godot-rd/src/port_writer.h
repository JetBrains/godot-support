#pragma once
#include "godot_cpp/variant/string.hpp"

#include <optional>

using namespace godot;

/**
 * Provides an abstraction over OS level file locks in order to check if this specific Godot instance
 * can access the port file (managed to lock the file) without messing up the connection for another instance.
 */
class PortWriter {
	static constexpr const char *LOCK_FILE_PATH = "res://.godot/editor/rider_connection.lock";
#ifdef _WIN32
	std::optional<void *> port_handle = std::nullopt;
#else
	std::optional<int> port_fd = std::nullopt;
#endif
	bool acquire_port_lock();

public:
	bool owns_port_lock();
	bool write_port_info(const String &path, const String &contents);

	~PortWriter();
};
