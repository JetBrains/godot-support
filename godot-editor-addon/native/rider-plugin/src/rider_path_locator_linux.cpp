// Linux-specific parts of RiderPathLocator for GDExtension builds

#include "rider_path_locator.h"

#if defined(__linux__)

#include <godot_cpp/classes/dir_access.hpp>
#include <godot_cpp/classes/file_access.hpp>
#include <godot_cpp/classes/os.hpp>
#include <godot_cpp/variant/string.hpp>

#include <cstdio>
#include <sstream>
#include <thread>

using namespace godot;

// -- conversion helpers -------------------------------------------------------

static String gstr(const std::string &s) { return String(s.c_str()); }
static std::string stdstr(const String &s) { return std::string(s.utf8().get_data()); }

// -- RiderPathLocator ---------------------------------------------------------

std::string RiderPathLocator::get_default_ide_install_location_for_toolbox_v2() {
	// Same as V1 on Linux
	return {};
}

std::optional<InstallInfo> RiderPathLocator::get_install_info_from_rider_path(
		const std::string &path) {
	String gpath = gstr(path);
	if (!FileAccess::file_exists(gpath))
		return std::nullopt;

	// Expect .../bin/rider.sh — derive installation root by going two levels up
	const String bin_dir = gpath.get_base_dir();
	if (bin_dir.get_file() != "bin")
		return std::nullopt;
	const String rider_dir = bin_dir.get_base_dir();

	if (!directory_exists_and_non_empty(stdstr(rider_dir.path_join("plugins"))))
		return std::nullopt;

	InstallInfo info;
	info.path = path;
	String product_info = rider_dir.path_join("product-info.json");
	if (FileAccess::file_exists(product_info)) {
		parse_product_info_json(info, stdstr(product_info));
	}
	if (!info.version.initialized()) {
		// Try to parse folder name as version
		info.version = stdstr(rider_dir.get_file());
	}
	return info;
}

// -- static helpers -----------------------------------------------------------

static String get_home() {
	return OS::get_singleton()->get_environment("HOME");
}

static std::vector<InstallInfo> get_manually_installed_riders() {
	std::vector<InstallInfo> result;

	std::vector lookup_paths = {
		get_home(),
		String("/opt"),
		String("/usr/local/bin"),
	};

	for (const auto &base : lookup_paths)
	{
		if (!DirAccess::dir_exists_absolute(base))
			continue;
		PackedStringArray dirs = DirAccess::get_directories_at(base);
		for (int i = 0; i < dirs.size(); ++i) {
			String dir_name = dirs[i];
			if (!dir_name.match("*Rider*"))
				continue;
			String full = base.path_join(dir_name).path_join("bin").path_join("rider.sh");
			auto info = RiderPathLocator::get_install_info_from_rider_path(
					stdstr(full));
			if (info.has_value())
				result.push_back(*info);
		}
	}

	// Snap
	String snap_path("/snap/rider/current/bin/rider.sh");
	auto snap_info = RiderPathLocator::get_install_info_from_rider_path(
			stdstr(snap_path));
	if (snap_info.has_value())
		result.push_back(*snap_info);

	return result;
}

static String get_toolbox_path() {
	return get_home().path_join(".local").path_join("share").path_join("JetBrains").path_join("Toolbox");
}

std::vector<std::string> RiderPathLocator::run_system_search() {
	std::vector<std::string> result;
	std::this_thread::sleep_for(std::chrono::seconds(10));
	FILE *pipe = popen("locate -e bin/rider.sh", "r");
	if (!pipe) return result;
	char buffer[4096];
	std::string out;
	while (fgets(buffer, sizeof(buffer), pipe)) out += buffer;
	pclose(pipe);
	std::istringstream ss(out);
	std::string line;
	while (std::getline(ss, line)) {
		while (!line.empty() && (line.back() == '\n' || line.back() == '\r' || line.back() == ' '))
			line.pop_back();
		if (!line.empty())
			result.push_back(line);
	}
	return result;
}

static std::vector<InstallInfo> get_installed_riders_from_locate_results(
		const std::vector<std::string> &paths) {
	std::vector<InstallInfo> result;
	for (const auto &line : paths) {
		if (line.find("snapd") != std::string::npos ||
			line.find(".local") != std::string::npos ||
			line.find("/opt") != std::string::npos)
			continue;
		auto info = RiderPathLocator::get_install_info_from_rider_path(line);
		if (info.has_value())
			result.push_back(*info);
	}
	return result;
}

std::set<InstallInfo, InstallInfoLess> RiderPathLocator::collect_all_paths(
		const std::vector<std::string> &system_search_results)
{
	std::set<InstallInfo, InstallInfoLess> s;
	for (auto &i : get_installed_riders_from_locate_results(system_search_results))
		s.insert(i);
	for (auto &i : get_manually_installed_riders())
		s.insert(i);
	for (auto &i : get_install_infos_from_toolbox(stdstr(get_toolbox_path()), "[Rr]ider.sh"))
		s.insert(i);
	return s;
}

#endif // __linux__