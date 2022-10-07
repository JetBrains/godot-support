# GdScript

## Work queue
### Missing language syntax
- Match case
- Lambdas
- Signals
- Inner class
- Parent method call fe.: ._init()
- Annotation parameters

### Lower Priority
- Full feature Formatter
- Linkable Documentation
- Check Parameters in function call
- expr type match
- Resource exists
- Double enter -> force dedent
- Create getter/setter via Alt+Insert
- Rename file -> rename also class_name
- Multi-line string bugs
- Line marker (signals, ...)
- Don't auto-complete private functions/fields (leading underscore) -> allow it based on settings
- Flow templates
- Enum checks
- Proper Run/Debug config

## List of features
### Auto-completion
- Inheritance & ClassName
- Annotations
- func overrides
- Resources (`$Path/Node` && `$"%Name"`)

### Documentation (Ctrl+Q)
- Currently only simplified plain-text
- ❌ Links and visualization like Java doc

### Other
- Inlay hints  
 ![](./screens/inlay.png)
- Param hints (Ctrl+P)  
![](./screens/param-hint.png)
- Run configuration - starts the game from Godot exe  
![](./screens/run-config.png)

### Formatter
- Very simplified version - requires major work  
- ❌ Optional semicolons (add/remove based on settings)

## Actions
### Quick fixes
#### Alt+Enter
- add/change return Type
- remove annotation
- change class_name to match filename
