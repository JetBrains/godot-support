// macOS-specific parts of RiderPathLocator for GDExtension builds

#include "rider_path_locator.h"

#if defined(__APPLE__)

#include <filesystem>
#include <regex>
#include <string>
#include <vector>
#include <cstdlib>
#include <cstdio>
// Needed for std::stringstream used with std::getline below
#include <sstream>

namespace fs = std::filesystem;

std::optional<InstallInfo> RiderPathLocator::get_install_info_from_rider_path(const std::string &path_to_rider_app,
                                                                              InstallInfo::InstallType type) {
    // Expecting path to Rider.app
    if (!directory_exists_and_non_empty(path_to_rider_app)) return std::nullopt;

    const fs::path app(path_to_rider_app);
    const fs::path plugins = app / "Contents" / "plugins";
    if (!directory_exists_and_non_empty(plugins.string())) return std::nullopt;

    InstallInfo info;
    info.path = (app / "Contents" / "MacOS" / "rider").string();
    info.type = type;
    const fs::path product_info = app / "Contents" / "Resources" / "product-info.json";
    if (fs::exists(product_info)) {
        parse_product_info_json(info, product_info.string());
    }
    return info;
}

static std::vector<InstallInfo> get_manually_installed_riders() {
    std::vector<InstallInfo> result;
    const fs::path apps("/Applications");
    if (!fs::exists(apps)) return result;
    std::regex rx("Rider.*\\.app");
    for (auto const &p : fs::directory_iterator(apps)) {
        if (!p.is_directory()) continue;
        const std::string name = p.path().filename().string();
        if (!std::regex_match(name, rx)) continue;
        auto info = RiderPathLocator::get_install_info_from_rider_path(p.path().string(), InstallInfo::InstallType::Installed);
        if (info.has_value()) result.push_back(*info);
    }
    return result;
}

static std::string get_toolbox_path() {
    const char *home = std::getenv("HOME");
    std::string base = home ? home : "";
    return (fs::path(base) / "Library" / "Application Support" / "JetBrains" / "Toolbox").string();
}

std::string RiderPathLocator::get_default_ide_install_location_for_toolbox_v2() {
    const char *home = std::getenv("HOME");
    std::string base = home ? home : "";
    return (fs::path(base) / "Applications").string();
}

// Use `mdfind` to locate Rider apps (best-effort, optional)
static std::vector<InstallInfo> get_installed_riders_with_mdfind() {
    std::vector<InstallInfo> result;
    FILE *pipe = popen("/usr/bin/mdfind \"kMDItemKind == Application\"", "r");
    if (!pipe) return result;
    char buffer[4096];
    std::string out;
    while (fgets(buffer, sizeof(buffer), pipe)) out += buffer;
    pclose(pipe);
    std::stringstream ss(out);
    std::string line;
    while (std::getline(ss, line)) {
        if (line.find("Rider") == std::string::npos) continue;
        auto info = RiderPathLocator::get_install_info_from_rider_path(line, InstallInfo::InstallType::Installed);
        if (info.has_value()) result.push_back(*info);
    }
    return result;
}

std::set<InstallInfo, InstallInfoLess> RiderPathLocator::collect_all_paths() {
    std::set<InstallInfo, InstallInfoLess> s;
    for (auto &i : get_installed_riders_with_mdfind()) s.insert(i);
    for (auto &i : get_manually_installed_riders()) s.insert(i);
    for (auto &i : get_install_infos_from_toolbox(get_toolbox_path(), "Rider*.app")) s.insert(i);
    for (auto &i : get_install_infos_from_resource_file()) s.insert(i);
    return s;
}

#endif // __APPLE__
