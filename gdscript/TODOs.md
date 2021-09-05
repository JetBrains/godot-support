resources: onready check

TopLevel: enumDecl, innerClass
syntax: @"Node/Label"

obarvení stringu """ """ při změně -> out of bounds
quote matcher -> multiline

smart enter -> odsazení -> možná spíš índent ve formatteru

attribute_ex -> reference completion

method decl validation + params validation (parent)
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


2.0 syntax
typed array
- var my_array: Array[int] = [1, 2, 3]
- var inferred_array := [1, 2, 3] # This is Array[int].



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
endofstmt -> nutnost prázdý řádek/; na konci souboru
structure view - filters for variable/const/methods? ... separate groups somehow

# String format:
var format_string = "We're waiting for %s."

// Using the '%' operator, the placeholder is replaced with the desired value

var actual_string = format_string % "Godot"




sdk -> lepší impl

8 line marker
13 go to
15 formatter
16 code style
19 doc

Completions:
- fn names privacy?
- variable
- public/private settings
- method params
- if else /...  flow hints
- flow templates

parent method call:
```
func loso():
    .loso()
```
