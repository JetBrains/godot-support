# GdScript

!!! IMPORTANT !!!  
This plugin is still under development and serious bugs are bubbling up. I very much appreciate all tests and do please report any bugs you find, but keep in mind it's not production ready yet.  
!!! IMPORTANT !!!  

# Installation guide

![Instalation](https://youtu.be/5utE9E0D5ZE)

## Extra tips:
- Dedent with Backspace
```
func losos():
    if (true):
        pass
        |
```
When you hit Backspace at cursor (below pass), IDEa will remove whole line and move you to
previous line, like this:
```
func losos():
    if (true):
        pass|
```
In order to dedent on backspace, you can change editor's settings under:  
Editor -> General -> Smart Keys -> Unindent on Backspace  
![](./screens/unindent.png)  
Hitting Backspace will then just move cursor to the left like this:
```
func losos():
    if (true):
        pass
    |
```

## List of features
### Auto-completion
- Inheritance & ClassName
- Annotations
- func overrides
- Resources (`$Path/Node` && `$"%Name"`)
- hides _prefix as private fields (is optional based on Language settings)

### Line markers
- Resource usages  
![](./screens/resource-marker.png)
- Signal  
![](./screens/signal-marker.png)

### Documentation (Ctrl+Q)

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
- generate get_set methods
- remove annotation
- change class_name to match filename
- remove getter & setter
