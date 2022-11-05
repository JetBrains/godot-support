res line marker
signal line marker


## Work queue
### Top priority

### Middle Priority
- master/puppet keywords
- Annotation parameters - checks and hints
- Signals
    - parameter hint
    - parameter check
- Check Parameters in function call
- Await
    - check if it is correctly implemented
    - check if awaiting an async task
    - check if async task is awaited
- expr type match
- Double enter -> force dedent
- Create getter/setter via Alt+Insert
- Run configuration
    - tady může být více scriptů (potřeba ověřit ID)
- Enum checks
- Flow templates
- Icon download with class refresh

### Low Priority
- Parent method call fe.: ._init() checks
- Rename file -> rename also class_name
- Linkable Documentation
- type of var binding in match pattern
- solo string expects endStmt
- Debug - does it Godot even allow? ...


const func ... qualifiers ?? Je to i v xml

documentace:
Vector2.AXIS_X má klíč [method #name#]

TopLevel: innerClass
syntax: @"Node/Label"

resource $"...  zda existuje

method decl unique validation + params validation (parent)
method: parent call ._init()

getter/setter:
GenerateGetterHandler - GenerateGetterSetterHandlerBase

validace: attribute_ex
construcotr annotator -> args parent validace

file rename:
https://intellij-support.jetbrains.com/hc/en-us/community/posts/206760415-Renaming-files-in-IDE

set,get validation - param, return type
var a: Node = 4; // chybí kontrola typu hodnoty
projít dostupné annotatce -> něco má extra options

Static methods
```
var x = Color.html_is_valid("00ffff") # true
```

kontrala refIdAnnotator -> že metoda je metoda  

# String format:
signal - argumenty
signal - napovídat jména + kontrolovat params

sdk -> lepší impl

Completions:
- method params
- if else /...  flow hints

```
func _init(e=null, m=null).(e):
    # Do something with 'e'.
```

Enum
// TODO tady chybí enum check -> chtělo by to rozšířit na ten parentaly... :/
unique name
unique value

abs() je v global (ty nejsou) ale i ve Vector 
-> více funkcí v jednom scope -> pořešit hinty, annotatároy

optional ";" ... hledej "// +;" kde to bylo použito
