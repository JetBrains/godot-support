# godot-cpp FetchContent setup for GDExtension
# Included by CMakeLists.txt when present. Delete this file to remove GDExtension support.
# Fetches godot-cpp into the build directory; no git repository is required.

include(FetchContent)
FetchContent_Declare(
    godot-cpp
    GIT_REPOSITORY https://github.com/godotengine/godot-cpp
    GIT_TAG master
)
FetchContent_MakeAvailable(godot-cpp)
# godot-cpp_SOURCE_DIR and godot-cpp_BINARY_DIR are set by FetchContent
