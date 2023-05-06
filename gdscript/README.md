# GdScript

Language plugin for JetBrains IDEs (Intellij, Rider and all the other ones) of GdScript2

# Installation

Please follow installation instructions

1. Download the plugin from Marketplace
   - There are currently 2 plugins, use the one from "IceExplosive"
2. Restart the IDE, due to unknown reasons, some parts like Highlighting is not working right after installation
3. Download an SDK from Language settings
4. Within Settings -> Languages & Frameworks -> GdScript add extracted folder into GdScript SDK field
   - SDK is rebuilt daily from current Godot master so after new Godot release, download new SDK as well

## Plugin settings:
- In order to dedent on backspace instead of deleting a line, you can change editor's settings under:  
Editor -> General -> Smart Keys -> UnIndent on Backspace
- By default, few of annotators are off due dynamics of Godot and GdScript, you can change it in settings, but then
it's required to be thorough when specifying types
- Warning that variable is not typed is disabled by default, but I recommend to opt-in (types can be added via alt+enter)  
- Completion settings: Editor -> General -> Code Completion -> Parameter Info
  - Try turning on: `Show parameter name hints on completion` which invokes hints after auto-completion

## Known limitations

- IDE must be opened from Godot's root folder, otherwise resource paths are incorrect [#issue](https://gitlab.com/IceExplosive/gdscript/-/issues/97)
- Attached scripts are not parsed -> if you create a method and attach it to Node3D f.e. autocompletion won't find it
unless you add class_name and specify that given object is that class (will be supported later on, but can't promise when)
- get_node(), get_parent() and so on atm do not parse actual Node, but only as a generic Node type (will be supported later on)
- get_window() (and maybe few other methods) return different class based on context (SubViewport, Window, ...),
plugin specify it as base Viewport class, so to get completion/check for inherited ones available you have to manually specify the type
- Dynamic nodes and such added at runtime cannot be predicted and thus no autocompletion

## List of features

Video preview (version 0.7): https://youtu.be/hjtzJW25GMI  
[Screenshots](./documentation/features.md)  

- Auto-completion
  - Inheritance & ClassName
  - Annotations
  - func overrides
  - Resources (`$Path/Node` && `$"%Name"`)
  - Inputs, Groups, Meta fields, user resources
- Refactoring
- Go to declaration / usages
- File templates taken from Godot's source
- Hides _prefix as private fields (optional based on Language settings)
- Built-in [documentation](./documentation/auto-documentation.md) (Ctrl+Q)
- Line markers
  - Resource usages  
  - Signals  
  - Super method
  - Color picker
  - Run current scene
- Inlay hints  
- Param hints (Ctrl+P)  
- Run configuration - start game from Editor
- Formatter
- is/has conditioned type for validations (ignore checks following get_node)

## Actions
### Quick fixes
- Add/change return Type
- Generate get_set methods
- Remove annotation
- Change class_name to match filename
- Remove getter & setter
- Too many arguments / change function type

## Trait like system
Via alt+insert sync trait code snippets
TODO create preview and explanation
