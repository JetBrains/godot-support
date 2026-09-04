Development guidelines for the Godot support under `dotnet/Plugins/godot-support`.

The GDScript plugin has its own guide in `gdscript/AGENTS.md`. Read it for the build, the test and
the lexer commands.

## 1. Layout

| Folder | Bazel target | Namespace |
| --- | --- | --- |
| `rider/` | `//dotnet/Plugins:rider-plugins-godot` | `com.jetbrains.rider.plugins.godot` |
| `community/` | `//dotnet/Plugins:rider-plugins-godot-community` | `com.jetbrains.rider.godot.community` |
| `gdscript/` | `//dotnet/Plugins:rider-plugins-godot-gdscript` | `gdscript`, `tscn`, `config`, `project`, `common` |
| `resharper/` | a .NET project | `JetBrains.ReSharper.Plugins.Godot` |
| `debugger/` | a .NET project | `JetBrains.ReSharper.Plugins.Godot.Rider.Debugger` |

The `.iml` files sit in `dotnet/Plugins/`, not in this folder. To compile only:

```
./bazel.cmd build //dotnet/Plugins:rider-plugins-godot //dotnet/Plugins:rider-plugins-godot-gdscript
```

## 2. Trace a run of Rider

Use this loop when a feature fails only in a running IDE, for example a refactoring that reverts
its own change.

### 2.1 Add the trace records

Log with `thisLogger().trace { ... }`. For a top level function keep one `private val LOG =
fileLogger()` in the file. Both put the record under the namespace of the file, and the `Godot`
trace scenario covers every namespace of the table above.

`gdscript.utils.PsiTraceUtil` describes a PSI element or a string in one short line. Use it instead
of `element.text`, which can be a whole file.

Log every early exit, not only the success path. A resolve walk that returns an empty list is the
usual cause of a feature that does nothing, and a silent `return` tells you nothing.

### 2.2 Turn the scenario on

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

### 2.3 Run and reproduce

Start the `Rider` run configuration and reproduce the case once. Keep the run short. A trace
scenario writes a lot, and a long session buries the interesting records.

### 2.4 Read the log

```
out/dev-data/rider/system/log/idea.log          # the frontend
out/dev-data/rider/system/log/backend.*.log     # the backend, one file per run
```

Take the newest `backend.*.log` by the timestamp in its name. Filter `idea.log` by the class name of
the code you instrumented.

## 3. The two rename mechanisms

The platform has two rename mechanisms, and the PolySymbols migration puts GDScript on the newer
one. Find out which mechanism runs before you debug a rename.

| | Target | Collects a usage with | Writes with |
| --- | --- | --- | --- |
| PSI rename | a `PsiElement` | `ReferencesSearch` | `PsiReference.handleElementRename` |
| symbol rename | a `RenameTarget` | `RenameUsageSearcher` | a `FileUpdater` of the searcher |

`gdscript.polySymbols.psi.GdPsiPolySymbol` implements `PolySymbolDeclaredInPsi`, and that interface
extends `RenameTarget`. So each GDScript declaration is its own rename target, and the platform
`PolySymbolRenameHandlerVeto` keeps the classic PSI rename away from it. The editor offers one
renamer, the symbol rename.

The platform serves the symbol rename with `PolySymbolRenameUsageSearcher`, which accepts a target
that is a `PolySymbol`. The plugin registers no searcher of its own and needs none. The PSI rename
still runs from other entry points, for example `CodeInsightTestFixture.renameElementAtCaret`, so
both mechanisms must reach the scene files.

## 4. The rename of a GDScript declaration in a scene file

A `.tscn` or `.tres` file names a GDScript method, signal, property or class as plain text. Both
rename mechanisms start from the own references of the scene element, and then they part.

`tscn.psi.impl.TscnNamedElementImpl.getOwnReferences` reports those own references. A `[connection]`
value, a `script_class` value, a `.tres` data line key and the `"method"` key of an animation track
each have a resolve walk. Every walk logs, so the trace shows which one returns nothing.

The symbol rename needs no more than that. `PolySymbolUsageQueries` word-searches the name of the
symbol and keeps each `PolySymbolReference` that resolves to it, and its own `FileUpdater` writes
the new name.

The PSI rename needs two more links of the plugin:

1. `gdscript.search.GdOwnReferencesSearcher` bridges an own reference into a classic
   `ReferencesSearch` result. It word-searches the name and keeps a candidate whose own reference
   resolves to the search target.
2. `tscn.psi.manipulator.TscnElementManipulator` replaces the text under the range through
   `PsiReferenceBase.handleElementRename`, and keeps the quotes and the `&` around it.

### 4.1 The in-place template needs a resilient pointer

The editor runs the symbol rename through the live template of
`com.intellij.refactoring.rename.inplace.inplaceRename`. The template deletes the text of every
usage in the file, the declaration included, puts the old text back, and only then dereferences the
target pointer and starts the real rename. A plain smart pointer to a name identifier stays dead
after the delete, so the pointer answers `null`, `performRename` returns without a word, and the
name reverts.

`gdscript.polySymbols.psi.gdPsiSymbolPointer` solves this. It keeps the original range and searches
by it when the smart pointer is dead. Every `GdPsi*Symbol` that a name identifier backs builds its
pointer with it. The two file-backed symbols, `GdPsiResourceClassSymbol` and `GdPsiAutoloadSymbol`,
keep a plain smart pointer, because no template deletes a file.

`com.jetbrains.godot.tscn.refactoring.rename.ConnectionMethodInplaceRenamingTest` drives the whole
template path. The other tests in that package call the rename target directly, so they pass even
when the template path fails. Add an in-place test for a new rename case.
