ClassName unique
Extends correct
Method unique (+completion removal)














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
7. annotator
8
10
11
12
13
14
15
16
18
19

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