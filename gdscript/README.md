# GdScript

!!! IMPORTANT !!!  
This plugin is still under development and serious bugs are bubbling up. I very much appreciate all tests and do please report any bugs you find, but keep in mind it's not production ready yet.  
!!! IMPORTANT !!!  

# Installation guide
1) Download GdScript plugin from marketplace (from IceExplosive)  
![](./screens/install/01.png)  
2) Download SDK zip from this repository and extract it somewhere (current version 0.x matches Godot's beta x)  
3) GoTo File -> Project Structure settings  
4) Select SDK tabs and create new GdScript SDK  
![](./screens/install/02.png)  
5) Select extracted folder  
   - *Ignore the error about cannot read SDK version*  
![](./screens/install/03.png)  
6) Add again the same folder as a Classpath  
![](./screens/install/04.png)  
7) Under Project tab select created GdScript SDK  
![](./screens/install/05.png)

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
