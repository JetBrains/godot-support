[![official JetBrains project](https://jb.gg/badges/official-flat-square.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
[![Build Status](https://teamcity.jetbrains.com/app/rest/builds/buildType:(id:NetProductsExtensions_GodotSupport_Build)/statusIcon.svg?guest=1)](https://teamcity.jetbrains.com/viewType.html?buildTypeId=NetProductsExtensions_GodotSupport_Build&guest=1)
 
# Godot Support for Rider

The "Godot Support" plugin adds specific functionality for the [Godot game engine](https://godotengine.org/) to [Rider](https://www.jetbrains.com/rider/).

Rider is JetBrains' cross platform .NET IDE, based on ReSharper and the IntelliJ Platform. It can be used on Windows, Mac and Linux providing rich code navigation, inspections and refactorings.

# Features

Autogenerates multiple run configurations:
 - `Player` to Run/Debug or Profile game in one click. [Demo](https://youtu.be/FmaYKONV5NY?t=78)
 - `Editor` to Run/Debug or Profile Godot Editor in one click
 - `Attach` to debug already started player
 - `WATSharp` to start the test-runner. [More](https://www.reddit.com/r/godot/comments/jl547k/official_jetbrains_godot_support_plugin_for_rider/)
 
Context menu on a scene file allows running game directly to that scene.

Fields marked with [Export] attribute would not be marked as not-initialized.
Classes marked with [Tool] attribute would not be marked as unused.
Delegates marked with [Signal] attribute would not be marked as unused.

# Recomendations

Add `.idea` to [.gitignore](https://github.com/van800/godot-demo-projects/pull/2/files#diff-a084b794bc0759e7a6b77810e01874f2R22) 
Including `tscn` in your project as demonstrated [here](https://github.com/van800/godot-demo-projects/pull/2/files#diff-d6ab4c56e3f79be158a2dbd5b9ae8eb8R7)

# Contributing

This project welcomes contributions and suggestions.
Please have a look at our [Guidelines](CONTRIBUTING.md) for contributing.
