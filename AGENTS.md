Development guidelines for the Godot support under `dotnet/Plugins/godot-support`.

## 1. Layout

| Folder | Bazel target | Namespace |
| --- | --- | --- |
| `rider/` | `//dotnet/Plugins:rider-plugins-godot` | `com.jetbrains.rider.plugins.godot` |
| `community/` | `//dotnet/Plugins:rider-plugins-godot-community` | `com.jetbrains.rider.godot.community` |
| `gdscript/` | `//dotnet/Plugins:rider-plugins-godot-gdscript` | `gdscript`, `tscn`, `config`, `project`, `common` |
| `resharper/` | a .NET project | `JetBrains.ReSharper.Plugins.Godot` |
| `debugger/` | a .NET project | `JetBrains.ReSharper.Plugins.Godot.Rider.Debugger` |
| `godot-rd/` | `//dotnet/Plugins/godot-support/godot-rd:compile_all` | a C++ GDExtension, the RD protocol bridge between Godot and Rider (see `godot-rd/AGENTS.md`) |
| `godot-editor-addon/` | none | a Godot project with the editor addon and its native code |
| `test-shared/` | `//dotnet/Plugins:rider-plugins-godot-test-shared` | the shared test fixtures (module `intellij.rider.plugins.godot.test.shared`) |
| `knowledge/` | none | the reference documents for agents (see section 4) |


## 2. Bazel vs Gradle

The main scenario for working with `godot-support` is the monorepo - the case when the root of the repository contains `dotnet/Plugins/godot-support`.
The `.iml` files sit in `dotnet/Plugins/` - `intellij.rider.plugins.godot*.iml`.

The secondary scenario is when the root of the workspace directly contains folders `rider`, `gdscript`.
Only in this case Gradle is the build/test tool to be used.

## 3. Read next

| Question | Document |
| --- | --- |
| the build, the test and the lexer commands of the GDScript plugin | `gdscript/AGENTS.md` |
| a feature fails only in a running IDE | the `godot-trace` skill |
| how a rename reaches a `.tscn` or a `.tres` file | the KDoc of `gdscript.search.GdOwnReferencesSearcher` and of `tscn.psi.impl.TscnNamedElementImpl.getOwnReferences` |
| the in-place rename reverts the new name | the KDoc of `gdscript.polySymbols.psi.gdPsiSymbolPointer` |
| which rename mechanism runs on a GDScript declaration | the KDoc of `gdscript.polySymbols.psi.GdPsiPolySymbol` |
| the PolySymbols rename in general | the `symbols-api` and `poly-symbols` skills |
| knowledge about Godot, GDScript, and `.tscn` | `godot-docs` (see section 3) |
| the Godot engine sources | `godot` (see section 3) |

## 4. Sources of truth

Two repositories serve as a source of truth for the engine behavior:

- [godot-docs](https://github.com/godotengine/godot-docs): contains useful knowledge about all of Godot, including GDScript and `.tscn`. It is available online or locally in `../godot-docs`, side by side with this repository root.
- [godot](https://github.com/godotengine/godot): contains the engine sources. It is available online or locally in `../godot`, side by side with this repository root.

## 5. References summary

- [TSCN and GDScript](knowledge/tscn_gdscript_references.md): Detailed inventory of all reference types between `.tscn` text scenes and GDScript. See its "Known Gaps" section for the reference types that do not yet resolve as a `PsiReference`.
