Development guidelines for the Godot support under `dotnet/Plugins/godot-support`.

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

## 2. Read next

| Question | Document |
| --- | --- |
| the build, the test and the lexer commands of the GDScript plugin | `gdscript/AGENTS.md` |
| a feature fails only in a running IDE | the `godot-trace` skill |
| how a rename reaches a `.tscn` or a `.tres` file | the KDoc of `gdscript.search.GdOwnReferencesSearcher` and of `tscn.psi.impl.TscnNamedElementImpl.getOwnReferences` |
| the in-place rename reverts the new name | the KDoc of `gdscript.polySymbols.psi.gdPsiSymbolPointer` |
| which rename mechanism runs on a GDScript declaration | the KDoc of `gdscript.polySymbols.psi.GdPsiPolySymbol` |
| the PolySymbols rename in general | the `symbols-api` and `poly-symbols` skills |
