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


sdk -> lepší impl

8 line marker
12 folding
13 go to
14 structure
15 formatter
16 code style
18 quick fix
19 doc

Completions:
- fn names (parent, own, privacy)
- variable
- constant (PI, TAU, ...)
- public/private settings
- method calls
- method params
- if else /...  flow hints
- flow templates

parent method call:
```
func loso():
    .loso()
```

recovery

#Done:

- syntax highlights + settings
- commenter

Completions:
- extends <className>
- parent function names