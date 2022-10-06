resources: onready check

const func ... qualifiers ?? Je to i v xml

automatizace refreshe

nefunguje kontrola rekurze / metod definovaných po tom, kde se volají ?? možná ano - chybka mohla být v kanclu bez indexu

documentace:
Vector2.AXIS_X má klíč [method #name#]

TopLevel: innerClass
syntax: @"Node/Label"

quote matcher -> multiline string  """ """

resource $"...  zda existuje

attribute_ex -> reference completion

method decl unique validation + params validation (parent)
method: parent call ._init()

getter/setter:
GenerateGetterHandler - GenerateGetterSetterHandlerBase

validace: attribute_ex
construcotr annotator -> args parent validace

file rename:
https://intellij-support.jetbrains.com/hc/en-us/community/posts/206760415-Renaming-files-in-IDE

extends inner class extends "losos.gd".inner
set,get validation - param, return type
var a: Node = 4; // chybí kontrola typu hodnoty
projít dostupné annotatce -> něco má extra options !!!



lambda
```
func _ready():
    var my_lambda = func(x):
    print(x)
    my_lambda.call("hello")
```
```
func _ready():
    button_down.connect(func(): print("button was pressed"))
```

Static methods
```
var x = Color.html_is_valid("00ffff") # true
```

TODO

Known issues
annotation insert

params - pokud je param další callExpr, tak to už na jméně napovídá ten vnitřní  
kontrala refIdAnnotator -> že metoda je metoda  

# String format:
signal - argumenty
signal - napovídat jména + kontrolovat params

sdk -> lepší impl

8 line marker (něco jako signals z godotu?)
13 go to
19 doc

Completions:
- fn names privacy?
- public/private settings
- method params
- if else /...  flow hints
- flow templates

```
func _init(e=null, m=null).(e):
    # Do something with 'e'.
```


Array.gd
methody typu constructor Array(from: ...)


Enum
// TODO tady chybí enum check -> chtělo by to rozšířit na ten parentaly... :/
unique name
unique value

Formatter

Run - reuse context not working
Debug

optional ";" ... hledej "// +;" kde to bylo použito
