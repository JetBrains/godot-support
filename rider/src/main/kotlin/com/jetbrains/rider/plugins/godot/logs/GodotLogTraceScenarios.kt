package com.jetbrains.rider.plugins.godot.logs

import com.jetbrains.rd.platform.diagnostics.LogTraceScenario

/**
 * Trace scenarios of the Godot support. Turn one on from `Help | Diagnostic Tools | Trace Scenarios`,
 * or start the IDE with `-Drd.forced.trace.scenarios=Godot`.
 *
 * A category is a logger name prefix, so each entry covers every logger below it.
 * [LogTraceScenario] adds the `#` form and the plain form of each category, so the frontend
 * `thisLogger()` calls and the backend loggers both switch to TRACE.
 */
@Suppress("unused")
class GodotLogTraceScenarios {

    /**
     * Every namespace of the plugins under `dotnet/Plugins/godot-support`.
     *
     * The GDScript plugin keeps its code in the root packages `gdscript`, `tscn`, `config`,
     * `project` and `common`. The last three are generic names, so they can also switch on a
     * logger of another plugin that uses the same prefix.
     *
     * `GdProjectService`, `GdScriptBundle`, `GdScriptIcons`, `GdScriptToolWindowManagerProjectActivity`
     * and `PluginConstants` sit in the default package. No category can cover them.
     */
    object Godot : LogTraceScenario(
        // frontend - the Rider plugin
        "#com.jetbrains.rider.plugins.godot",
        // frontend - the shared plugin
        "#com.jetbrains.rider.godot.community",
        // frontend - the GDScript plugin
        "#gdscript",
        "#tscn",
        "#config",
        "#project",
        "#common",
        // backend - the ReSharper plugin and the debugger worker below it
        "JetBrains.ReSharper.Plugins.Godot",
    )
}
