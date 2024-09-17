[![official JetBrains project](https://jb.gg/badges/official-flat-square.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
[![Build Status](https://teamcity.jetbrains.com/app/rest/builds/buildType:(id:NetProductsExtensions_GodotSupport_Build)/statusIcon.svg?guest=1)](https://teamcity.jetbrains.com/viewType.html?buildTypeId=NetProductsExtensions_GodotSupport_Build&guest=1)
 
# Godot Support for Rider

The "Godot Support" plugin adds specific functionality for the [Godot game engine](https://godotengine.org/) to [Rider](https://www.jetbrains.com/rider/).

Rider is JetBrains' cross platform .NET IDE, based on ReSharper and the IntelliJ Platform. It can be used on Windows, Mac and Linux providing rich code navigation, inspections and refactorings.

# Features

| Feature                                                                | GDScript | C#  | Details                                                                                                                                                                                                                  |
|------------------------------------------------------------------------|----------|-----|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Godot 3.x                                                              | ✔        | ✔   |                                                                                                                                                                                                                          |
| Godot 4.x                                                              | ✔        | ✔   | design ready for future updates                                                                                                                                                                                          |
| Syntax Highlighting                                                    | ✔        | *   | GDScript, scenes, shaders, & resources.                                                                                                                                                                                  |
| Godot GDScript LSP                                                     | ✔        | n/a | LSP from current editor or from headless editor                                                                                                                                                                          |
| Run Configurations                                                     | ✔        | ✔   | Run/Debug, launch editor, and context menu on a scene file allows running game directly to that scene                                                                                                                    |
| [gdUnit4Net](https://github.com/MikeSchulze/gdUnit4Net) test framework | n/a      | ✔   | Write and run unit tests in C#                                                                                                                                                                                           |
| Input Action and NodePath completions                                  | ✔        | ✔   | completion in string literals and [more](https://github.com/JetBrains/godot-support/pull/102)                                                                                                                            |
| Debugger                                                               | ✔        | ✔   | for C# adds a “Current Scene” item to the Variables and Watches tab and a Children node to every Godot.Node item in the Variables and Watches tab. .NET debugging occurs in .NET runtime with access to scene variables. |
| Godot warnings and quick fixes                                         | ✔        | ✔   | Godot specific warnings and quick fixes, integrated with Roslyn provided ones.                                                                                                                                           |
| External Annotations for Godot Attributes                              | n/a      | ✔   | C# Warning suppression for known Godot idioms                                                                                                                                                                            |
| and More...                                                            |          |     |                                                                                                                                                                                                                          | 

\* C# features provided by [JetBrains Rider](https://jetbrains.com/rider)

# Recommendations

Add `.idea` to [.gitignore](https://github.com/van800/godot-demo-projects/pull/2/files#diff-a084b794bc0759e7a6b77810e01874f2R22) 
Include `tscn` in your project as demonstrated [here](https://github.com/van800/godot-demo-projects/pull/2/files#diff-d6ab4c56e3f79be158a2dbd5b9ae8eb8R7)

# Contributing

This project welcomes contributions and suggestions.
Please refer to our [Guidelines](CONTRIBUTING.md) for contributing.
