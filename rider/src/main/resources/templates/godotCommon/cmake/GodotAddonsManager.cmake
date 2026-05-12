# GodotAddonsManager.cmake — Godot addon manager
#
# Included by CMakeLists.txt when present. Delete this file to disable the addon manager.
#
# Downloads addons during CMake configuration on developer machines. Addons are not automatically enabled.
#
# Add more addons below with: godot_addon(NAME "name" URL "https://...")
#
# CMake option (added automatically):
#   INSTALL_GODOT_ADDONS  ON by default; set to OFF to skip all downloads.
#   Auto-set to OFF when the environment variable "CI" is defined.
#   CI is enabled on github (https://docs.github.com/en/actions/reference/workflows-and-actions/variables)
#
# To disable downloads in CI:
#   cmake -B build -DINSTALL_GODOT_ADDONS=OFF
# or rely on the CI env-var auto-detection.
#
# Download tracking:
#   A stamp file (.godot_addon_stamp) is written inside each addon's directory
#   (addons/<name>/.godot_addon_stamp) after a successful download.
#
#   If you commit an addon to VCS together with its stamp file, the manager will not re-download it.

if(DEFINED ENV{CI})
    set(_godot_addons_default OFF)
else()
    set(_godot_addons_default ON)
endif()
option(INSTALL_GODOT_ADDONS
    "Download and install Godot addons for local development (auto-disabled on CI)"
    ${_godot_addons_default})

# Tracks which addon directories are managed (used by CMakeLists.txt for source filtering)
set(GODOT_MANAGED_ADDONS "")

function(godot_addon)
    cmake_parse_arguments(ADDON "" "NAME;URL" "" ${ARGN})
    if(NOT ADDON_NAME OR NOT ADDON_URL)
        message(FATAL_ERROR "godot_addon() requires NAME and URL arguments")
    endif()

    # Always register for PROJECT_SOURCES filtering, even when downloads are disabled
    list(APPEND GODOT_MANAGED_ADDONS "${ADDON_NAME}")
    set(GODOT_MANAGED_ADDONS "${GODOT_MANAGED_ADDONS}" PARENT_SCOPE)

    if(NOT INSTALL_GODOT_ADDONS)
        return()
    endif()

    set(_stamp "${CMAKE_CURRENT_SOURCE_DIR}/addons/${ADDON_NAME}/.godot_addon_stamp")
    if(EXISTS "${_stamp}")
        return()
    endif()

    set(_zip "${CMAKE_CURRENT_BINARY_DIR}/.godot_addon_${ADDON_NAME}.zip")
    set(_tmp "${CMAKE_CURRENT_BINARY_DIR}/.godot_addon_${ADDON_NAME}_tmp")

    message(STATUS "Godot addon: installing '${ADDON_NAME}' from '${ADDON_URL}'...")
    file(DOWNLOAD "${ADDON_URL}" "${_zip}" SHOW_PROGRESS STATUS _dl_status)
    list(GET _dl_status 0 _dl_err)
    if(_dl_err)
        list(GET _dl_status 1 _dl_msg)
        message(WARNING "Godot addon: could not download '${ADDON_NAME}': ${_dl_msg}. Skipping.")
        return()
    endif()

    file(MAKE_DIRECTORY "${_tmp}")
    execute_process(
        COMMAND ${CMAKE_COMMAND} -E tar xf "${_zip}"
        WORKING_DIRECTORY "${_tmp}"
        RESULT_VARIABLE _extract_err
    )
    if(_extract_err)
        message(WARNING "Godot addon: could not extract '${ADDON_NAME}' (error: ${_extract_err}). Skipping.")
        file(REMOVE_RECURSE "${_tmp}" "${_zip}")
        return()
    endif()

    # Support zip layouts: addons/ at root, or addons/ one level deep (e.g. wrapper-dir/addons/)
    file(GLOB _items "${_tmp}/addons/*")
    if(NOT _items)
        file(GLOB _items "${_tmp}/*/addons/*")
    endif()
    if(NOT _items)
        message(WARNING "Godot addon: no 'addons/' directory found in archive for '${ADDON_NAME}'. Skipping.")
        file(REMOVE_RECURSE "${_tmp}" "${_zip}")
        return()
    endif()

    foreach(_item IN LISTS _items)
        file(COPY "${_item}" DESTINATION "${CMAKE_CURRENT_SOURCE_DIR}/addons")
    endforeach()
    file(REMOVE_RECURSE "${_tmp}" "${_zip}")

    file(TOUCH "${_stamp}")
    message(STATUS "Godot addon: '${ADDON_NAME}' installed.")
endfunction()

# --- Configured addons ---

godot_addon(
    NAME "rider-plugin"
    URL  "https://github.com/JetBrains/godot-support/releases/latest/download/rider-plugin-addons.zip"
)
