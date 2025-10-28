// Linux-specific parts of RiderPathLocator for GDExtension builds

#include "rider_path_locator.h"

#if defined(__linux__)

#include <filesystem>
#include <regex>
#include <string>
#include <vector>
#include <cstdlib>
#include <cstdio>

namespace fs = std::filesystem;

std::string RiderPathLocator::get_default_ide_install_location_for_toolbox_v2() {
    // Same as V1 on Linux
    return {};
}

std::optional<InstallInfo> RiderPathLocator::get_install_info_from_rider_path(const std::string &path,
                                                                              InstallInfo::InstallType type) {
    if (!fs::exists(path)) return std::nullopt;

    // Expect .../(bin)/rider.sh
    std::regex re("(.*)(?:\\\\|/)bin");
    std::smatch m;
    if (!std::regex_search(path, m, re) || m.size() < 2) return std::nullopt;
    const fs::path rider_dir = m[1].str();

    const fs::path plugins = rider_dir / "plugins";
    if (!directory_exists_and_non_empty(plugins.string())) return std::nullopt;

    InstallInfo info;
    info.path = path;
    info.type = type;
    const fs::path product_info = rider_dir / "product-info.json";
    if (fs::exists(product_info)) parse_product_info_json(info, product_info.string());
    if (!info.version.initialized()) {
        // Try to parse folder name as version
        info.version = rider_dir.filename().string();
        if (info.version.major() >= 221) info.support = InstallInfo::SupportUproject::Release;
    }
    return info;
}

static std::string get_home() {
    const char *h = std::getenv("HOME");
    return h ? h : std::string();
}

static std::vector<InstallInfo> get_manually_installed_riders() {
    std::vector<InstallInfo> result;

    std::vector<fs::path> lookup_paths = {
        fs::path(get_home()),
        fs::path("/opt"),
        fs::path("/usr") / "local" / "bin"
    };

    std::regex dir_rx(".*Rider.*");
    for (const auto &base : lookup_paths) {
        if (!fs::exists(base)) continue;
        for (auto const &p : fs::directory_iterator(base)) {
            if (!p.is_directory()) continue;
            if (!std::regex_match(p.path().filename().string(), dir_rx)) continue;
            fs::path full = p.path() / "bin" / "rider.sh";
            auto info = RiderPathLocator::get_install_info_from_rider_path(full.string(), InstallInfo::InstallType::Installed);
            if (info.has_value()) result.push_back(*info);
        }
    }

    // Snap
    {
        fs::path p = "/snap/rider/current/bin/rider.sh";
        auto info = RiderPathLocator::get_install_info_from_rider_path(p.string(), InstallInfo::InstallType::Installed);
        if (info.has_value()) result.push_back(*info);
    }

    return result;
}

static std::string get_toolbox_path() {
    fs::path local = fs::path(get_home()) / ".local" / "share";
    return (local / "JetBrains" / "Toolbox").string();
}

static std::vector<InstallInfo> get_installed_riders_with_locate() {
    std::vector<InstallInfo> result;
    FILE *pipe = popen("/usr/bin/locate -e bin/rider.sh", "r");
    if (!pipe) return result;
    char buffer[4096];
    std::string out;
    while (fgets(buffer, sizeof(buffer), pipe)) out += buffer;
    pclose(pipe);

    std::stringstream ss(out);
    std::string line;
    while (std::getline(ss, line)) {
        if (line.find("snapd") != std::string::npos || line.find(".local") != std::string::npos || line.find("/opt") != std::string::npos) {
            continue;
        }
        auto info = RiderPathLocator::get_install_info_from_rider_path(line, InstallInfo::InstallType::Installed);
        if (info.has_value()) result.push_back(*info);
    }
    return result;
}

std::set<InstallInfo, InstallInfoLess> RiderPathLocator::collect_all_paths() {
    std::set<InstallInfo, InstallInfoLess> s;
    for (auto &i : get_installed_riders_with_locate()) s.insert(i);
    for (auto &i : get_manually_installed_riders()) s.insert(i);
    for (auto &i : get_install_infos_from_toolbox(get_toolbox_path(), "[Rr]ider.sh")) s.insert(i);
    for (auto &i : get_install_infos_from_resource_file()) s.insert(i);
    return s;
}

#endif // __linux__
