cmake_minimum_required(VERSION 3.25)

project(godot-rd
        VERSION 1.0
        LANGUAGES CXX
)

set(CMAKE_POSITION_INDEPENDENT_CODE ON)

include(FetchContent)

# --- Dependencies ---------------------------------------------------------

# Bazel is the authoritative build, so the dependencies are declared once as `http_archive`s in
# godot-rd.MODULE.bazel and this build only mirrors them.
function(godot_rd_dependency repo url_variable sha256_variable)
    set(MODULE_BAZEL_FILE "${CMAKE_CURRENT_LIST_DIR}/godot-rd.MODULE.bazel")
    file(READ "${MODULE_BAZEL_FILE}" MODULE_BAZEL)
    if (NOT MODULE_BAZEL MATCHES "name = \"${repo}\"[^)]*\\)")
        message(FATAL_ERROR "no http_archive named '${repo}' in ${MODULE_BAZEL_FILE}")
    endif ()
    set(ARCHIVE "${CMAKE_MATCH_0}")
    if (NOT ARCHIVE MATCHES "url = \"([^\"]+)\"")
        message(FATAL_ERROR "no url of '${repo}' in ${MODULE_BAZEL_FILE}")
    endif ()
    set(${url_variable} "${CMAKE_MATCH_1}" PARENT_SCOPE)
    if (NOT ARCHIVE MATCHES "sha256 = \"([^\"]+)\"")
        message(FATAL_ERROR "no sha256 of '${repo}' in ${MODULE_BAZEL_FILE}")
    endif ()
    set(${sha256_variable} "${CMAKE_MATCH_1}" PARENT_SCOPE)
endfunction()

godot_rd_dependency(godot_cpp GCPP_URL GCPP_SHA)
godot_rd_dependency(rd_cpp RD_URL RD_SHA)

# `SYSTEM` keeps the warnings of the two dependencies out of the build of this extension.
fetchcontent_declare(godot_cpp
        URL ${GCPP_URL}
        URL_HASH SHA256=${GCPP_SHA}
        SOURCE_DIR ${CMAKE_CURRENT_LIST_DIR}/thirdParty/godot-cpp
        SYSTEM
)
fetchcontent_declare(rd_cpp
        URL ${RD_URL}
        URL_HASH SHA256=${RD_SHA}
        SOURCE_DIR ${CMAKE_CURRENT_LIST_DIR}/thirdParty/rd-cpp
        SOURCE_SUBDIR rd-cpp
        SYSTEM
)

# Keep the Godot API version shared with Bazel and the GDExtension descriptor;
# godot_cpp_gen_main.py reads the very same assignment.
set(GODOT_VERSION_FILE "${CMAKE_CURRENT_LIST_DIR}/godot_version.bzl")
file(READ "${GODOT_VERSION_FILE}" GODOT_VERSION_BZL)
if (NOT GODOT_VERSION_BZL MATCHES "GODOT_VERSION = \"([0-9]+\\.[0-9]+)\"")
    message(FATAL_ERROR "no GODOT_VERSION assignment in ${GODOT_VERSION_FILE}")
endif ()
set(GODOT_VERSION "${CMAKE_MATCH_1}")

# The options of both dependencies have to be forced before they are configured below.
set(GODOTCPP_API_VERSION "${GODOT_VERSION}" CACHE STRING "Godot GDExtension API version" FORCE)
set(GODOTCPP_DISABLE_EXCEPTIONS OFF CACHE BOOL "RD needs exceptions" FORCE)

# This GDExtension is declared `reloadable = true` in godot-rd.gdextension, so Godot dlopen()/
# dlclose()s the library at runtime (hot reload / dynamic load) instead of only loading it once at
# editor startup. By default godot-cpp statically links libgcc/libstdc++ (GODOTCPP_USE_STATIC_CPP=ON)
# for portability, but a statically-embedded libstdc++ CANNOT survive dlopen/dlclose reload cycles.
set(GODOTCPP_USE_HOT_RELOAD ON CACHE BOOL "Reloadable extension: link libstdc++ dynamically" FORCE)

set(ENABLE_TESTS_OPTION OFF CACHE BOOL "Global enable test" FORCE)
set(RD_STATIC ON CACHE BOOL "Compile RD as static library" FORCE)
set(USE_ADDRESS_SANITIZER OFF CACHE BOOL "Do not instrument RD inside the Godot extension" FORCE)

fetchcontent_makeavailable(godot_cpp rd_cpp)

# godot-cpp puts its `gen/include` on the interface include path of godot::cpp but only creates the
# directory while building, whereas CMake rejects a non-existent include directory at generate time.
file(MAKE_DIRECTORY "${godot_cpp_BINARY_DIR}/gen/include")

# godot-cpp requires cpp17, RD uses different libraries on version less than cpp17
# this results in an ABI mismatch without this flag. A global CMAKE_CXX_STANDARD would not help:
# rd-cpp sets its own to 14 in its own scope.
set(RD_TARGETS rd_core_cpp rd_framework_cpp rd_framework_cpp_util)
set_target_properties(${RD_TARGETS} PROPERTIES CXX_STANDARD 17 CXX_STANDARD_REQUIRED ON)

# rd-cpp appends -D_GLIBCXX_DEBUG to the debug flags of its own scope, which changes the layout of
# the libstdc++ containers; this extension is built without it.
if (CMAKE_CXX_COMPILER_ID MATCHES "GNU|Clang")
    foreach (RD_TARGET IN LISTS RD_TARGETS ITEMS spdlog)
        target_compile_options(${RD_TARGET} PRIVATE -U_GLIBCXX_DEBUG)
    endforeach ()
endif ()

# --- The GDExtension ------------------------------------------------------

file(GLOB_RECURSE RD_MODEL_SOURCES CONFIGURE_DEPENDS
        "${CMAKE_CURRENT_LIST_DIR}/rd_models/*.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/rd_models/*.h"
)

file(GLOB RD_EXTENSION_SOURCES CONFIGURE_DEPENDS
        "${CMAKE_CURRENT_LIST_DIR}/src/*.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/src/*.h"
)

add_library(${PROJECT_NAME} SHARED
        ${RD_EXTENSION_SOURCES}
        ${RD_MODEL_SOURCES}
)

target_include_directories(${PROJECT_NAME} PRIVATE rd_models)
target_compile_features(${PROJECT_NAME} PRIVATE cxx_std_17)

if (NOT MSVC)
    target_compile_options(${PROJECT_NAME} PRIVATE -Wall -Wextra -pedantic)
    set_source_files_properties(${RD_MODEL_SOURCES} PROPERTIES COMPILE_OPTIONS "-w")
endif ()

# The RD static archives (rd_framework_cpp / rd_core_cpp / rd_framework_cpp_util) have mutual
# (circular) references between their object files. A single-pass GNU linker scans each archive only
# once, so a fixed link order can leave symbols unresolved.
target_link_libraries(${PROJECT_NAME} PRIVATE godot::cpp)
if (CMAKE_CXX_LINK_GROUP_USING_RESCAN_SUPPORTED)
    target_link_libraries(${PROJECT_NAME} PRIVATE
            "$<LINK_GROUP:RESCAN,rd_framework_cpp,rd_core_cpp,rd_framework_cpp_util>")
else ()
    target_link_libraries(${PROJECT_NAME} PRIVATE
            "$<LINK_LIBRARY:WHOLE_ARCHIVE,rd_framework_cpp,rd_core_cpp,rd_framework_cpp_util>")
endif ()

# --- Delivery -------------------------------------------------------------

get_target_property(GODOTCPP_PLATFORM godot::cpp GODOTCPP_PLATFORM)
get_target_property(GODOTCPP_ARCH godot::cpp GODOTCPP_ARCH)
get_target_property(GODOTCPP_PRECISION godot::cpp GODOTCPP_PRECISION)

set(GODOT_RD_OUTPUT_NAME "${PROJECT_NAME}.${GODOTCPP_PLATFORM}.${GODOTCPP_ARCH}")
if (GODOTCPP_PRECISION STREQUAL "double")
    string(APPEND GODOT_RD_OUTPUT_NAME ".double")
endif ()

set_target_properties(${PROJECT_NAME}
        PROPERTIES
        PREFIX ""
        OUTPUT_NAME "${GODOT_RD_OUTPUT_NAME}"
)

set(GODOT_PROJECT_BINARY_DIR "${PROJECT_SOURCE_DIR}/project/addons/godot-rd/bin/${GODOTCPP_PLATFORM}")

add_custom_command(TARGET ${PROJECT_NAME} POST_BUILD
        COMMAND ${CMAKE_COMMAND} -E make_directory "${GODOT_PROJECT_BINARY_DIR}"
        COMMAND ${CMAKE_COMMAND} -E copy_if_different "$<TARGET_FILE:${PROJECT_NAME}>" "${GODOT_PROJECT_BINARY_DIR}/$<TARGET_FILE_NAME:${PROJECT_NAME}>"
)
