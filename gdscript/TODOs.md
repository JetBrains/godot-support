resources: onready check

oddělit static metody

TopLevel: innerClass
syntax: @"Node/Label"

quote matcher -> multiline string  """ """

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
barvy definice/volání/parentVolání
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
structure view - filters for variable/const/methods? ... separate groups somehow

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
// TODO tady chybí enum check -> chtělo by to rozšířit an ten parentaly... :/
unique name
unique value

Match, ...

Formatter

Run - reuse context not working
Debug