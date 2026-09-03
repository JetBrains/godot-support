#Constants for builds based on Rider Minimum specs.
GODOT_PLATFORMS = {
    "linux-x86_64": ("linux", "x86_64", "so"),
    "linux-arm64": ("linux", "arm64", "so"),
    "windows-x86_64": ("windows", "x86_64", "dll"),
    "windows-arm64": ("windows", "arm64", "dll"),
    "macos-x86_64": ("macos", "x86_64", "dylib"),
    "macos-arm64": ("macos", "arm64", "dylib"),
}

# The Godot OS names of GODOT_PLATFORMS, deduplicated, used as the addon directory names.
GODOT_OS_DIRS = {platform[0]: None for platform in GODOT_PLATFORMS.values()}.keys()

UNSUPPORTED_PLATFORM = "the godot-rd GDExtension is not built for this platform, see GODOT_PLATFORMS in //dotnet/Plugins/godot-support/godot-rd:godot_rd_platforms.bzl"

# Lowest version of supported GLIBC -> Anything newer required will fail the build.
GLIBC_FLOOR = "@llvm//constraints/libc:gnu.2.29"

# MacOS targetted version.
MACOS_DEPLOYMENT_TARGET = "13.0"

# NTDDI_WIN10_RS5 is Windows 10 1809. The PE format has no place to express a build number floor,
# so this is an API gate only: the Windows SDK headers hide everything introduced after 1809, which
# turns the use of a newer API into a compile error.
WINDOWS_10_1809_COPTS = [
    "/D_WIN32_WINNT=0x0A00",
    "/DWINVER=0x0A00",
    "/DNTDDI_VERSION=0x0A000006",
]

# The Windows targets compile with clang-cl, which reads the GCC style `-Wall` of the default
# compile flags of the toolchain as the cl.exe spelling `/Wall` and maps that to `-Weverything`
# and everything here means a lot of bogus warnings/errors.
WINDOWS_CLANG_CL_WARNING_COPTS = [
    "-Wno-everything",
    "/W3",
]

HIDDEN_VISIBILITY_COPTS = select({
    "@platforms//os:windows": [],
    "//conditions:default": [
        "-fvisibility=hidden",
        "-fvisibility-inlines-hidden",
    ],
})

def godot_shared_lib_name(platform_id):
    """The file name the `godot-rd.gdextension` descriptor expects for the given platform id."""
    return "godot-rd.%s.%s.%s" % GODOT_PLATFORMS[platform_id]

def godot_os(platform_id):
    """The Godot platform name of the given platform id, used as the addon directory name."""
    return GODOT_PLATFORMS[platform_id][0]
