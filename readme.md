[![official JetBrains project](https://jb.gg/badges/official-flat-square.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
[![Build Status](https://teamcity.jetbrains.com/app/rest/builds/buildType:(id:NetProductsExtensions_GodotSupport_Build)/statusIcon.svg?guest=1)](https://teamcity.jetbrains.com/viewType.html?buildTypeId=NetProductsExtensions_GodotSupport_Build&guest=1)
 
# Godot Support for Rider

The "Godot Support" plugin adds specific functionality for the [Godot game engine](https://godotengine.org/) .NET version (C# support) to [Rider](https://www.jetbrains.com/rider/).

Rider is JetBrains' cross platform .NET IDE, based on ReSharper and the IntelliJ Platform. It can be used on Windows, Mac and Linux providing rich code navigation, inspections and refactorings.

# Features

Bundles syntax highlighting for Godot file types, including GDScript, scenes and resources.

Autogenerates multiple run configurations:
 - `Player` to Run/Debug or Profile game in one click. [Demo](https://youtu.be/FmaYKONV5NY?t=78)
 - `Editor` to Run/Debug or Profile Godot Editor in one click
 - `Attach` to debug already started player
 
Context menu on a scene file allows running game directly to that scene.

Fields marked with [Export] attribute would not be marked as not-initialized.
Classes marked with [Tool] attribute would not be marked as unused.
Delegates marked with [Signal] attribute would not be marked as unused.

Support test framework:

 - [gdUnit4Mono](https://github.com/MikeSchulze/gdUnit4Mono), [Example project](https://github.com/van800/Godot421GdUnitExample)
 - old approach: support running XUnit/NUnit tests inside the game process. [More](https://github.com/JetBrains/godot-support/pull/58)

Supports Input Action and NodePath completion in string literals [More](https://github.com/JetBrains/godot-support/pull/102)

Debugger extension. Adds “Current Scene” item to the Variables and Watches tab. Adds Children node to every Godot.Node item in the Variables and Watches tab

Godot specific warnings and quick fixes. A warning and a quick-fix for a missing parameterless ctor. [Pull request](https://github.com/JetBrains/godot-support/pull/127)

Works with Godot version 3.x and 4.x.

# Recomendations

Add `.idea` to [.gitignore](https://github.com/van800/godot-demo-projects/pull/2/files#diff-a084b794bc0759e7a6b77810e01874f2R22) 
Include `tscn` in your project as demonstrated [here](https://github.com/van800/godot-demo-projects/pull/2/files#diff-d6ab4c56e3f79be158a2dbd5b9ae8eb8R7)

# Contributing

This project welcomes contributions and suggestions.
Please have a look at our [Guidelines](CONTRIBUTING.md) for contributing.
