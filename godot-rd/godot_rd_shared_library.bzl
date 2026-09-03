"""
Builds the godot-rd shared library for one target platform with the Rider minimum requirements.
"""

load(
    ":godot_rd_platforms.bzl",
    "GODOT_PLATFORMS",
    "MACOS_DEPLOYMENT_TARGET",
    "WINDOWS_10_1809_COPTS",
    "WINDOWS_CLANG_CL_WARNING_COPTS",
    "godot_shared_lib_name",
)

_PLATFORMS_PACKAGE = "//dotnet/Plugins/godot-support/godot-rd/platforms"

# The static MSVC runtime, so the DLLs do not depend on the VC++ redistributable being installed.
_WINDOWS_FEATURES = ["static_link_msvcrt"]

def _enforced_platform_transition_impl(settings, attr):
    return {
        "//command_line_option:platforms": [attr.platform],
        "//command_line_option:features": settings["//command_line_option:features"] + attr.extra_features,
        "//command_line_option:copt": settings["//command_line_option:copt"] + attr.extra_copts,
        # Ignored by the non-Apple toolchains, but a transition has to return every declared output.
        # The hermetic macOS toolchain reads it from the apple fragment and defaults to 14.0.
        "//command_line_option:macos_minimum_os": MACOS_DEPLOYMENT_TARGET,
    }

_enforced_platform_transition = transition(
    implementation = _enforced_platform_transition_impl,
    inputs = [
        "//command_line_option:features",
        "//command_line_option:copt",
    ],
    outputs = [
        "//command_line_option:platforms",
        "//command_line_option:features",
        "//command_line_option:copt",
        "//command_line_option:macos_minimum_os",
    ],
)

def _godot_rd_shared_library_impl(ctx):
    # `cc_shared_library` also reports the import library on Windows, and the descriptor references
    # the shared library by name, so pick it explicitly instead of forwarding everything.
    libraries = [file for file in ctx.files.lib if file.basename == ctx.attr.shared_lib_name]
    if len(libraries) != 1:
        fail("expected exactly one %s among %s, the platform transition did not configure %s" % (
            ctx.attr.shared_lib_name,
            [file.basename for file in ctx.files.lib],
            ctx.attr.platform,
        ))

    return [DefaultInfo(files = depset(libraries))]

godot_rd_shared_library = rule(
    implementation = _godot_rd_shared_library_impl,
    doc = "The godot-rd shared library built for `platform` with the enforcement flags of that platform.",
    attrs = {
        "lib": attr.label(
            mandatory = True,
            cfg = _enforced_platform_transition,
            doc = "The `cc_shared_library` to build in the transitioned configuration.",
        ),
        "platform": attr.label(
            mandatory = True,
            providers = [platform_common.PlatformInfo],
            doc = "The platform to build for.",
        ),
        "shared_lib_name": attr.string(
            mandatory = True,
            doc = "The file name the `godot-rd.gdextension` descriptor expects for this platform.",
        ),
        "extra_features": attr.string_list(
            doc = "Toolchain features to enable for the whole dependency graph.",
        ),
        "extra_copts": attr.string_list(
            doc = "Compiler options to add for the whole dependency graph.",
        ),
    },
)

def _validated_executable_impl(ctx):
    executable = ctx.attr.executable[DefaultInfo]
    validation = ctx.attr.validation[DefaultInfo]
    forwarded_executable = ctx.actions.declare_file(
        "%s_%s" % (ctx.label.name, executable.files_to_run.executable.basename),
    )
    ctx.actions.symlink(
        output = forwarded_executable,
        target_file = executable.files_to_run.executable,
        is_executable = True,
    )
    return [DefaultInfo(
        files = depset([forwarded_executable], transitive = [validation.files]),
        executable = forwarded_executable,
        runfiles = executable.default_runfiles,
    )]

validated_executable = rule(
    implementation = _validated_executable_impl,
    executable = True,
    attrs = {
        "executable": attr.label(
            executable = True,
            mandatory = True,
            cfg = "target",
        ),
        "validation": attr.label(
            allow_files = True,
            mandatory = True,
        ),
    },
)

def godot_rd_platform_libraries(name, lib):
    """Declares one `godot_rd_shared_library` per platform id, named `<name>-<platform id>`.

    All targets use `manual`. An explicit aggregate target can select them without adding the six
    platform builds to a wildcard build.

    Args:
        name: the common prefix of the generated target names.
        lib: the `cc_shared_library` to build for every platform.
    """
    for platform_id in GODOT_PLATFORMS:
        is_windows = platform_id.startswith("windows-")
        godot_rd_shared_library(
            name = "%s-%s" % (name, platform_id),
            lib = lib,
            platform = "%s:%s" % (_PLATFORMS_PACKAGE, platform_id),
            shared_lib_name = godot_shared_lib_name(platform_id),
            extra_features = _WINDOWS_FEATURES if is_windows else [],
            extra_copts = (WINDOWS_10_1809_COPTS + WINDOWS_CLANG_CL_WARNING_COPTS) if is_windows else [],
            tags = ["manual"],
        )
