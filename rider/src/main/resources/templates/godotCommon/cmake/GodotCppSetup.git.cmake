# godot-cpp git submodule setup for GDExtension
# Included by CMakeLists.txt when present. Delete this file to remove GDExtension support.
# Runs before native/${EXTENSION_NAME} is added as a subdirectory.

find_program(GIT git REQUIRED)

# Initialize git repository at the project root if not already present
if(NOT EXISTS "${CMAKE_SOURCE_DIR}/.git")
    message(NOTICE "Initializing git repository...")
    execute_process(
        COMMAND ${GIT} init
        WORKING_DIRECTORY "${CMAKE_SOURCE_DIR}"
        COMMAND_ERROR_IS_FATAL ANY
    )
    execute_process(
        COMMAND ${GIT} add .
        WORKING_DIRECTORY "${CMAKE_SOURCE_DIR}"
        COMMAND_ERROR_IS_FATAL ANY
    )
endif()

set(_godot_cpp_dir "${CMAKE_SOURCE_DIR}/native/${EXTENSION_NAME}/godot-cpp")

if(NOT EXISTS "${_godot_cpp_dir}/.git")
    # Recover from any interrupted previous attempt
    if(EXISTS "${_godot_cpp_dir}")
        message(NOTICE "Cleaning up incomplete godot-cpp directory...")
        execute_process(
            COMMAND ${GIT} submodule deinit -f -- native/${EXTENSION_NAME}/godot-cpp
            WORKING_DIRECTORY "${CMAKE_SOURCE_DIR}"
            ERROR_QUIET OUTPUT_QUIET
        )
        file(REMOVE_RECURSE "${CMAKE_SOURCE_DIR}/.git/modules/native/${EXTENSION_NAME}/godot-cpp")
        file(REMOVE_RECURSE "${_godot_cpp_dir}")
        execute_process(
            COMMAND ${GIT} config --file "${CMAKE_SOURCE_DIR}/.gitmodules"
                --remove-section "submodule.native/${EXTENSION_NAME}/godot-cpp"
            ERROR_QUIET OUTPUT_QUIET
        )
        execute_process(
            COMMAND ${GIT} rm --cached -f native/${EXTENSION_NAME}/godot-cpp
            WORKING_DIRECTORY "${CMAKE_SOURCE_DIR}"
            ERROR_QUIET OUTPUT_QUIET
        )
    endif()

    message(NOTICE "Adding and initializing the godot-cpp submodule...")
    execute_process(
        COMMAND ${GIT} submodule add -b master https://github.com/godotengine/godot-cpp
        WORKING_DIRECTORY "${CMAKE_SOURCE_DIR}/native/${EXTENSION_NAME}"
        COMMAND_ERROR_IS_FATAL ANY
    )
endif()

add_subdirectory("${_godot_cpp_dir}" "${CMAKE_BINARY_DIR}/native/${EXTENSION_NAME}/godot-cpp" SYSTEM)
set(godot-cpp_SOURCE_DIR "${_godot_cpp_dir}")
set(godot-cpp_BINARY_DIR "${CMAKE_BINARY_DIR}/native/${EXTENSION_NAME}/godot-cpp")
