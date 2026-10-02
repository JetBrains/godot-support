#include "port_writer.h"

#include <system_error>

#ifdef _WIN32
#include <Windows.h>
#ifdef CONNECT_DEFERRED
    #undef CONNECT_DEFERRED
#endif
#elif defined(__APPLE__) || defined(__linux__)
#include <fcntl.h>
#include <sys/file.h>
#include <unistd.h>
#else
#error Unsupported platform
#endif

#include "godot_cpp/classes/file_access.hpp"
#include "godot_cpp/classes/project_settings.hpp"

bool PortWriter::acquire_port_lock() {
	if (owns_port_lock()) {
		return true;
	}
	String abs_path = ProjectSettings::get_singleton()->globalize_path(LOCK_FILE_PATH);

#ifdef _WIN32
	HANDLE handle = CreateFileW(
			abs_path.wide_string().get_data(),
			GENERIC_READ | GENERIC_WRITE,
			FILE_SHARE_READ | FILE_SHARE_WRITE,
			nullptr, // Non-inheritable handle.
			OPEN_ALWAYS, // Create if missing; never truncate.
			FILE_ATTRIBUTE_NORMAL,
			nullptr);
	auto err_to_string = []() {
		return std::system_category().message(static_cast<int>(GetLastError()));
	};
	if (handle == INVALID_HANDLE_VALUE) {
		UtilityFunctions::print_verbose(String("[RIDER RD] Failed to start communication between Godot and Rider. Failed to open lock file: ") + err_to_string().c_str());
		return false;
	}
	OVERLAPPED offset{};
	if (!LockFileEx(
				handle,
				LOCKFILE_EXCLUSIVE_LOCK | LOCKFILE_FAIL_IMMEDIATELY,
				0,
				1, 0,
				&offset)) {
		UtilityFunctions::print_verbose(String("[RIDER RD] Failed to start communication between Godot and Rider. Failed to lock file: ") + err_to_string().c_str());
		CloseHandle(handle);
		return false;
	}
	port_handle = handle;
#else
	int fd = open(abs_path.utf8().get_data(), O_RDWR | O_CREAT, 0666);
	auto err_to_string = []() -> std::string {
		return std::generic_category().message(errno);
	};
	if (fd == -1) {
		UtilityFunctions::print_verbose(String("[RIDER RD] Failed to start communication between Godot and Rider. Failed to lock file: ") + err_to_string().c_str());
		close(fd);
		return false;
	}
	if (flock(fd, LOCK_EX | LOCK_NB)) {
		UtilityFunctions::print_verbose(String("[RIDER RD] Failed to start communication between Godot and Rider. Failed to lock file, already locked: ") + err_to_string().c_str());
		close(fd);
		return false;
	}
	port_fd = fd;
#endif
	return true;
}

bool PortWriter::owns_port_lock() {
#ifdef _WIN32
	return port_handle.has_value();
#else
	return port_fd.has_value();
#endif
}

bool PortWriter::write_port_info(const String &path, const String &contents) {
	if (!acquire_port_lock()) {
		UtilityFunctions::push_warning("[RIDER RD] Failed to start communication between Godot and Rider. Another Godot instance is already running and connected.");
		return false;
	}
	auto port_file = FileAccess::open(path, FileAccess::WRITE);
	if (port_file.is_null()) {
		ERR_PRINT(String("[RIDER RD] Failed to start communication between Godot and Rider. Failed to open port file: ") + FileAccess::get_open_error());
		return false;
	}
	bool ok = port_file->store_string(contents);
	if (!ok) {
		ERR_PRINT("[RIDER RD] Failed to start communication between Godot and Rider. Failed to write into port file.");
		return false;
	}
	return true;
}

PortWriter::~PortWriter() {
#ifdef _WIN32
	if (port_handle.has_value()) {
		OVERLAPPED offset{};
		UnlockFileEx(*port_handle, 0, 1, 0, &offset);
		CloseHandle(*port_handle);
	}
#else
	if (port_fd.has_value()) {
		flock(*port_fd, LOCK_UN);
		close(port_fd.value());
	}
#endif
}
