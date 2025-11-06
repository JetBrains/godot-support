[![official JetBrains project](https://jb.gg/badges/official-flat-square.svg)](https://github.com/JetBrains#jetbrains-on-github)

# JetBrains Godot Development Tools

This repository contains a collection of plugins that enhance Godot game development in JetBrains IDEs:

1. **Godot Support Plugin** - Basic GDScript, enhanced C# support for Godot in Rider
2. **GdScript Plugin** - Enhanced GDScript support
3. **Community Plugin** - Technical bridge between the two

## JetBrains Rider

Rider is JetBrains' cross platform IDE, based on ReSharper and the IntelliJ Platform. It can be used on Windows, Mac and Linux providing rich code navigation, inspections and refactorings.

## Godot Support Plugin for Rider

**Key components:**
- Located in the `rider`, `resharper`, and `debugger` folders
- Focused on C# development for Godot
- However provides LSP and debugging for GDScript

[Dedicated readme](godot-support/features.md)

## GdScript Plugin for JetBrains IDEs

**Key components:**
- Located in the `gdscript` folder
- Supports Godot 4.0 and higher
- Focused on GdScript development

[Dedicated readme](gdscript/README.md)

## Community Plugin

The "Community" plugin serves as a technical bridge between the Godot Support and GdScript plugins. It provides common interfaces and extension points that both plugins can use to communicate with each other.

**Key components:**
- Located in the `community` folder
- Provides extension points between the other plugins
- Referenced by both the Godot Support and GdScript plugins

## Installation

### JetBrains Rider
- All the three plugins for Godot developments are bundled in Rider.

### Other JetBrains IDEs
- The GdScript Plugin can be build targeting any JetBrains IDE SDK. _To be documented._

## License

- **Godot Support Plugin** and **Community Plugin** are licensed under the [Apache License 2.0](LICENSE)
- **GdScript Plugin** is licensed under the [MIT License](gdscript/LICENSE)

## Contributing

This project welcomes contributions and suggestions.
Please refer to our [Guidelines](CONTRIBUTING.md) for contributing.
