// Common, platform-agnostic pieces of RiderPathLocator reworked for GDExtension

#include "rider_path_locator.h"

#include <algorithm>
#include <cctype>
#include <filesystem>
#include <fstream>
#include <regex>
#include <sstream>
#include <cstdlib>
#include <climits>

namespace fs = std::filesystem;

// --- Version -----------------------------------------------------------------

static int to_int(const std::string &s) {
    if (s.empty()) return -1;
    char *end = nullptr;
    errno = 0;
    long v = std::strtol(s.c_str(), &end, 10);
    if (end == s.c_str() || *end != '\0') return -1; // not a pure integer
    if (errno == ERANGE || v < INT_MIN || v > INT_MAX) return -1; // out of range
    return static_cast<int>(v);
}

void Version::operator=(const std::string &version_str) {
    parts.clear();
    std::stringstream ss(version_str);
    std::string item;
    while (std::getline(ss, item, '.')) {
        parts.push_back(to_int(item));
    }
}

bool Version::operator<(const Version &rhs) const {
    size_t l = parts.size();
    size_t r = rhs.parts.size();
    size_t n = std::min(l, r);
    for (size_t i = 0; i < n; ++i) {
        if (parts[i] < rhs.parts[i]) return true;
        if (parts[i] > rhs.parts[i]) return false;
    }
    return l < r;
}

// --- Helpers -----------------------------------------------------------------

static std::string read_file_to_string(const std::string &path) {
    std::ifstream f(path);
    if (!f.is_open()) return {};
    std::stringstream buffer;
    buffer << f.rdbuf();
    return buffer.str();
}

static std::string trim(const std::string &s) {
    auto b = s.begin();
    while (b != s.end() && std::isspace(static_cast<unsigned char>(*b))) ++b;
    auto e = s.end();
    do { if (e == s.begin()) break; --e; } while (std::isspace(static_cast<unsigned char>(*e)));
    if (b > e) return {};
    return std::string(b, e + 1);
}

bool RiderPathLocator::directory_exists_and_non_empty(const std::string &path) {
    std::error_code ec;
    if (!fs::exists(path, ec) || !fs::is_directory(path, ec)) return false;
    auto it = fs::directory_iterator(path, ec);
    return ec ? false : it != fs::end(it);
}

// Extract install_location from toolbox .settings.json using a simple regex
static std::string extract_install_location_from_settings_json(const std::string &toolbox_path) {
    const std::string settings = (fs::path(toolbox_path) / ".settings.json").string();
    const std::string json = read_file_to_string(settings);
    if (json.empty()) return {};
    // Standard string literal with escaped backslashes for regex tokens
    std::regex re("\"install_location\"\\s*:\\s*\"([^\"]*)\"");
    std::smatch m;
    if (std::regex_search(json, m, re) && m.size() > 1) {
        return m[1].str();
    }
    return {};
}

// Find nearest .history.json upwards
std::string RiderPathLocator::get_history_json_path(const std::string &rider_path) {
    fs::path dir = fs::absolute(fs::path(rider_path)).parent_path();
    for (int i = 0; i < 10; ++i) {
        fs::path candidate = dir / ".history.json";
        if (fs::exists(candidate)) return candidate.string();
        if (!dir.has_parent_path()) break;
        dir = dir.parent_path();
    }
    return {};
}

Version RiderPathLocator::get_last_build_version(const std::string &history_json_path) {
    const std::string json = read_file_to_string(history_json_path);
    if (json.empty()) return {};
    // very small/robust extractor for the last history item build number
    // We search for "build" : "<digits>"
    std::regex re("\\\"build\\\"\\s*:\\s*\\\"([0-9.]+)\\\"");
    std::smatch m;
    Version v;
    std::string::const_iterator search_start(json.cbegin());
    while (std::regex_search(search_start, json.cend(), m, re)) {
        v = Version(m[1].str());
        search_start = m.suffix().first; // keep last match
    }
    return v;
}

void RiderPathLocator::parse_product_info_json(InstallInfo &info, const std::string &product_info_json_path) {
    const std::string json = read_file_to_string(product_info_json_path);
    if (json.empty()) return;
    std::regex build_re("\\\"buildNumber\\\"\\s*:\\s*\\\"([0-9.]+)\\\"");
    std::smatch m;
    if (std::regex_search(json, m, build_re) && m.size() > 1) {
        info.version = Version(m[1].str());
        if (info.version.major() >= 221) {
            info.support = InstallInfo::SupportUproject::Release;
            return;
        }
    }

    // Fallback: look for SupportUproject or SupportUprojectState in customProperties
    std::regex state_re("\\\"(SupportUproject|SupportUprojectState)\\\".*?\\\"value\\\"\\s*:\\n?\\t?\\s*\\\"(Beta|Release)\\\"");
    if (std::regex_search(json, m, state_re) && m.size() > 2) {
        std::string val = m[2].str();
        if (val == "Beta") info.support = InstallInfo::SupportUproject::Beta;
        if (val == "Release") info.support = InstallInfo::SupportUproject::Release;
    }
}

std::vector<InstallInfo> RiderPathLocator::get_install_infos(const std::string &toolbox_rider_root_path,
                                                             const std::string &pattern,
                                                             InstallInfo::InstallType type) {
    std::vector<InstallInfo> result;
    if (!directory_exists_and_non_empty(toolbox_rider_root_path)) return result;

    std::regex rx;
    // Convert very small subset of glob to regex: '*' -> ".*"
    std::string rx_str = std::regex_replace(pattern, std::regex("[.]"), "[.]");
    rx_str = std::regex_replace(rx_str, std::regex("\\*"), ".*");
    rx = std::regex(rx_str);

    for (auto const &p : fs::recursive_directory_iterator(toolbox_rider_root_path)) {
        if (!p.is_regular_file()) continue;
        const std::string filename = p.path().filename().string();
        if (!std::regex_match(filename, rx)) continue;

        auto info = get_install_info_from_rider_path(p.path().string(), type);
        if (!info.has_value()) continue;
        Version last = get_last_build_version(get_history_json_path(p.path().string()));
        if (last.initialized() && !(info->version == last)) continue;
        result.push_back(*info);
    }
    return result;
}

std::vector<InstallInfo> RiderPathLocator::get_install_infos_from_toolbox(const std::string &toolbox_path,
                                                                          const std::string &pattern) {
    if (!directory_exists_and_non_empty(toolbox_path)) return {};

    const std::string install_location = extract_install_location_from_settings_json(toolbox_path);
    if (!install_location.empty()) {
        // V1 custom location
        auto r = get_install_infos((fs::path(install_location) / "apps").string(), pattern, InstallInfo::InstallType::Toolbox);
        if (!r.empty()) return r;
        // V2 custom location
        return get_install_infos(install_location, pattern, InstallInfo::InstallType::Toolbox);
    }

    // V1 default location
    auto r = get_install_infos((fs::path(toolbox_path) / "apps").string(), pattern, InstallInfo::InstallType::Toolbox);
    if (!r.empty()) return r;

    // V2 default location
    return get_install_infos(get_default_ide_install_location_for_toolbox_v2(), pattern, InstallInfo::InstallType::Toolbox);
}

// Resource file not available in Godot context; keep as empty but leave hook for future.
std::vector<InstallInfo> RiderPathLocator::get_install_infos_from_resource_file() {
    return {};
}

bool InstallInfoLess::operator()(const InstallInfo &a, const InstallInfo &b) const {
    if (a.version < b.version) return true;
    if (b.version < a.version) return false;
    // normalize path separators
    auto norm = [](std::string s) {
        std::replace(s.begin(), s.end(), '\\', '/');
        return s;
    };
    return norm(a.path) < norm(b.path);
}