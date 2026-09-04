---
name: godot-trace
description: Trace a running Rider to debug Godot or GDScript behaviour that fails only in the IDE.
---

# Trace a run of Rider for Godot

Use this loop when a feature of `dotnet/Plugins/godot-support` fails only in a running IDE, for
example a refactoring that reverts its own change.

## 1. Add the trace records

Log with `thisLogger().trace { ... }`. For a top level function keep one `private val LOG =
fileLogger()` in the file. Both put the record under the namespace of the file, and the `Godot`
trace scenario covers every namespace of the plugin.

`gdscript.utils.PsiTraceUtil` describes a PSI element or a string in one short line. Use it instead
of `element.text`, which can be a whole file.

Log every early exit, not only the success path. A resolve walk that returns an empty list is the
usual cause of a feature that does nothing, and a silent `return` tells you nothing.

## 2. Turn the scenario on

`com.jetbrains.rider.plugins.godot.logs.GodotLogTraceScenarios` declares the `Godot` scenario. The
Rider `plugin.xml` registers it under `rd.platform.traceScenarioHolder`. One switch raises the
frontend and the backend to TRACE.

Turn it on in one of two ways:

- In the running IDE: `Help | Diagnostic Tools | Choose Trace Scenarios`, then select `Godot`. The
  choice survives a restart.
- At start: add `-Drd.forced.trace.scenarios=Godot` to the VM options of the run configuration.

A category is a logger name prefix, so `gdscript` covers `gdscript.search.GdOwnReferencesSearcher`.
`GdProjectService` and the other classes in the default package of the GDScript plugin get no
category. Move such a class into a package before you trace it.

## 3. Run and reproduce

Start the `Rider` run configuration and reproduce the case once. Keep the run short. A trace
scenario writes a lot, and a long session buries the interesting records.

## 4. Read the log

```
out/dev-data/rider/system/log/idea.log          # the frontend
out/dev-data/rider/system/log/backend.*.log     # the backend, one file per run
```

Take the newest `backend.*.log` by the timestamp in its name. Filter `idea.log` by the class name of
the code you instrumented.
