[![official JetBrains project](https://jb.gg/badges/official-flat-square.svg)](https://github.com/JetBrains#jetbrains-on-github)

# JetBrains Godot Development Tools

This repository contains a collection of plugins that enhance Godot game development in JetBrains IDEs:

1. **Godot.NET** - Advanced C# support for Godot.NET in Rider
2. **Godot** - GDScript support
3. **Shared** - Technical bridge between the two

## JetBrains Rider

Rider is JetBrains' cross platform IDE, based on ReSharper and the IntelliJ Platform. It can be used on Windows, Mac and Linux providing rich code navigation, inspections and refactorings.

## Godot.NET plugin for Rider

**Key components:**
- Located in the `rider`, `resharper`, and `debugger` folders
- Focused on C# development for Godot

[Dedicated readme](godot-support/features.md)

## Godot plugin for JetBrains IDEs

**Key components:**
- Located in the `gdscript` folder
- Supports Godot 4.0 and higher
- Focused on GDScript development for Godot

[Dedicated readme](gdscript/README.md)

## Shared plugin

The Shared plugin serves as a technical bridge between the Godot and Godot.NET plugins. It provides common interfaces and extension points that both plugins can use to communicate with each other.

**Key components:**
- Located in the `community` folder
- Provides extension points between the other plugins
- Referenced by both the Godot and Godot.NET plugins

## Installation

### JetBrains Rider
- All the three plugins for Godot developments are bundled in Rider.

### Other JetBrains IDEs
- The Godot plugin can be build targeting any JetBrains IDE SDK. _To be documented._

## License

- **Godot.NET** and **Godot Shared** plugins are licensed under the [Apache License 2.0](LICENSE)
- **Godot** plugin is licensed under the [MIT License](gdscript/LICENSE)

## Contributing

This project welcomes contributions and suggestions.
Please refer to our [Guidelines](CONTRIBUTING.md) for contributing.
