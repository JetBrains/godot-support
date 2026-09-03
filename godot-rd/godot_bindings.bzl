"""Generates the C++ bindings for a pinned Godot GDExtension API."""

def _godot_bindings_impl(ctx):
    output_dir = ctx.bin_dir.path
    if ctx.label.workspace_root:
        output_dir += "/" + ctx.label.workspace_root
    if ctx.label.package:
        output_dir += "/" + ctx.label.package

    ctx.actions.run(
        arguments = [
            "--api-filepath",
            ctx.file.api_file.path,
            "--interface-filepath",
            ctx.file.interface_file.path,
            "--use-template-get-node",
            "True",
            "--bits",
            "64",
            "--precision",
            "single",
            "--output-dir",
            output_dir,
        ],
        executable = ctx.executable.tool,
        inputs = [
            ctx.file.api_file,
            ctx.file.interface_file,
        ],
        mnemonic = "GodotBindings",
        outputs = ctx.outputs.outs,
        progress_message = "Generating godot-cpp bindings",
        tools = [ctx.attr.tool[DefaultInfo].files_to_run],
    )

    return DefaultInfo(files = depset(ctx.outputs.outs))

godot_bindings = rule(
    implementation = _godot_bindings_impl,
    doc = "Generates the godot-cpp bindings and exposes all generated files to a C++ target.",
    attrs = {
        "api_file": attr.label(
            allow_single_file = [".json"],
            mandatory = True,
            doc = "The pinned Godot GDExtension API description.",
        ),
        "interface_file": attr.label(
            allow_single_file = [".json"],
            mandatory = True,
            doc = "The GDExtension interface description.",
        ),
        "outs": attr.output_list(
            mandatory = True,
            doc = "The headers and C++ sources produced by the binding generator.",
        ),
        "tool": attr.label(
            cfg = "exec",
            executable = True,
            mandatory = True,
            doc = "The executable binding generator.",
        ),
    },
)
