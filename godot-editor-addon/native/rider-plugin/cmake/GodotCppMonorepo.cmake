# Locates, prepares and adds the godot-cpp subdirectory.
# Must be include()d from native/rider-plugin/CMakeLists.txt after GODOTCPP_TARGET is set.

# The godot-cpp submodule exists only in the SDK scenario. In the monorepo it is absent, and the godot-cpp
# prepared by godot-rd (a sibling folder, only present in the monorepo) is used instead.
# Sets <out_var> to ON in the monorepo and to OFF in the SDK scenario.
function(godot_detect_monorepo out_var)
    # godot-support has its own .git only in the SDK scenario
    if(EXISTS "${CMAKE_CURRENT_FUNCTION_LIST_DIR}/../../../../.git")
        set(${out_var} OFF PARENT_SCOPE)
    else()
        set(${out_var} ON PARENT_SCOPE)
    endif()
endfunction()

function(monorepo_init)
    set(GODOTCPP_API_VERSION "4.6") # temporary, consider using the one from the godot-rd
    set(GODOT_RD_GODOTCPP_DIR "${CMAKE_CURRENT_FUNCTION_LIST_DIR}/../../../../godot-rd/thirdParty/godot-cpp")
    if(NOT EXISTS "${GODOT_RD_GODOTCPP_DIR}/src")
        message(FATAL_ERROR "godot-cpp is not prepared by godot-rd yet (${GODOT_RD_GODOTCPP_DIR}): configure godot-rd first")
    endif()
    get_filename_component(GODOT_RD_GODOTCPP_DIR "${GODOT_RD_GODOTCPP_DIR}" ABSOLUTE)
    add_subdirectory("${GODOT_RD_GODOTCPP_DIR}" godot-cpp SYSTEM)
endfunction()
