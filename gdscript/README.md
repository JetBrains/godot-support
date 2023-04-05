# GdScript

Language plugin for JetBrains IDEs (Intellij, Rider and all the other ones) of GdScript2

!!! IMPORTANT !!!  
This plugin is still under development and serious bugs are bubbling up. I very much appreciate all tests and do please report any bugs you find, but keep in mind it's not production ready yet.  
!!! IMPORTANT !!!  

# Installation

![](./video/installation.mp4)

## Recommended settings:
In order to dedent on backspace instead of deletin a line, you can change editor's settings under:  
Editor -> General -> Smart Keys -> Unindent on Backspace

## List of features

Video preview (version 0.7): https://youtu.be/hjtzJW25GMI

- Auto-completion
  - Inheritance & ClassName
  - Annotations
  - func overrides
  - Resources (`$Path/Node` && `$"%Name"`) + inputs, user files
- Refactoring
- Go to declaration / usages
- File templates taken from Godot's source
- Hides _prefix as private fields (optional based on Language settings)
- Built-in documentation (Ctrl+Q)
- Line markers
  - Resource usages  
  - Signals  
- Inlay hints  
- Param hints (Ctrl+P)  
- Run configuration - start game from Editor
- Formatter
- IS conditioned type for validations (ignore checks following get_node)

## Actions
### Quick fixes
#### Alt+Enter
- add/change return Type
- generate get_set methods
- remove annotation
- change class_name to match filename
- remove getter & setter
- too many arguments
