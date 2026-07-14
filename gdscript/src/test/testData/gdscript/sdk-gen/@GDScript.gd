class_name _GDScript

## Built-in GDScript constants, functions, and annotations.
##
## A list of utility functions and annotations accessible from any script written in GDScript.
## For the list of global functions and constants that can be accessed in any scripting language, see [@GlobalScope].
##
## @tutorial(GDScript exports): https://docs.godotengine.org/en/stable/tutorials/scripting/gdscript/gdscript_exports.html


## Constant that represents how many times the diameter of a circle fits around its perimeter. This is equivalent to [code]TAU / 2[/code], or 180 degrees in rotations.
const PI = 3.14159265358979;

## The circle constant, the circumference of the unit circle in radians. This is equivalent to [code]PI * 2[/code], or 360 degrees in rotations.
const TAU = 6.28318530717959;

## Positive floating-point infinity. This is the result of floating-point division when the divisor is [code]0.0[/code]. For negative infinity, use [code]-INF[/code]. Dividing by [code]-0.0[/code] will result in negative infinity if the numerator is positive, so dividing by [code]0.0[/code] is not the same as dividing by [code]-0.0[/code] (despite [code]0.0 == -0.0[/code] returning [code]true[/code]).
## [b]Warning:[/b] Numeric infinity is only a concept with floating-point numbers, and has no equivalent for integers. Dividing an integer number by [code]0[/code] will not result in [constant INF] and will result in a run-time error instead.
const INF = inf;

## "Not a Number", an invalid floating-point value. It is returned by some invalid operations, such as dividing floating-point [code]0.0[/code] by [code]0.0[/code].
## [constant NAN] has special properties, including that [code]!=[/code] always returns [code]true[/code], while other comparison operators always return [code]false[/code]. This is true even when comparing with itself ([code]NAN == NAN[/code] returns [code]false[/code] and [code]NAN != NAN[/code] returns [code]true[/code]). Due to this, you must use [method @GlobalScope.is_nan] to check whether a number is equal to [constant NAN].
## [b]Warning:[/b] "Not a Number" is only a concept with floating-point numbers, and has no equivalent for integers. Dividing an integer [code]0[/code] by [code]0[/code] will not result in [constant NAN] and will result in a run-time error instead.
const NAN = nan;




## Returns a [Color] constructed from red ([param r8]), green ([param g8]), blue ([param b8]), and optionally alpha ([param a8]) integer channels, each divided by [code]255.0[/code] for their final value. Using [method Color8] instead of the standard [Color] constructor is useful when you need to match exact color values in an [Image].
## [codeblock]
## var red = Color8(255, 0, 0)             # Same as Color(1, 0, 0).
## var dark_blue = Color8(0, 0, 51)        # Same as Color(0, 0, 0.2).
## var my_color = Color8(306, 255, 0, 102) # Same as Color(1.2, 1, 0, 0.4).
## [/codeblock]
## [b]Note:[/b] Due to the lower precision of [method Color8] compared to the standard [Color] constructor, a color created with [method Color8] will generally not be equal to the same color created with the standard [Color] constructor. Use [method Color.is_equal_approx] for comparisons to avoid issues with floating-point precision error.
func Color8(r8: int, g8: int, b8: int, a8: int = 255) -> Color:
	pass;

## Asserts that the [param condition] is [code]true[/code]. If the [param condition] is [code]false[/code], an error is generated. When running from the editor, the running project will also be paused until you resume it. This can be used as a stronger form of [method @GlobalScope.push_error] for reporting errors to project developers or add-on users.
## An optional [param message] can be shown in addition to the generic "Assertion failed" message. You can use this to provide additional details about why the assertion failed.
## [b]Warning:[/b] For performance reasons, the code inside [method assert] is only executed in debug builds or when running the project from the editor. Don't include code that has side effects in an [method assert] call. Otherwise, the project will behave differently when exported in release mode.
## [codeblock]
## # Imagine we always want speed to be between 0 and 20.
## var speed = -10
## assert(speed < 20) # True, the program will continue.
## assert(speed >= 0) # False, the program will stop.
## assert(speed >= 0 and speed < 20) # You can also combine the two conditional statements in one check.
## assert(speed < 20, "the speed limit is 20") # Show a message.
## [/codeblock]
## [b]Note:[/b] [method assert] is a keyword, not a function. So you cannot access it as a [Callable] or use it inside expressions.
func assert(condition: bool, message: String = "") -> void:
	pass;

## Returns a single character (as a [String]) of the given Unicode code point (which is compatible with ASCII code).
## [codeblock]
## var upper = char(65)      # upper is "A"
## var lower = char(65 + 32) # lower is "a"
## var euro = char(8364)     # euro is "€"
## [/codeblock]
func char(char: int) -> String:
	pass;

## Converts [param what] to [param type] in the best way possible. The [param type] uses the [enum Variant.Type] values.
## [codeblock]
## var a = [4, 2.5, 1.2]
## print(a is Array) # Prints true
## var b = convert(a, TYPE_PACKED_BYTE_ARRAY)
## print(b)          # Prints [4, 2, 1]
## print(b is Array) # Prints false
## [/codeblock]
func convert(what: Variant, type: int) -> Variant:
	pass;

## Converts a [param dictionary] (created with [method inst_to_dict]) back to an Object instance. Can be useful for deserializing.
func dict_to_inst(dictionary: Dictionary) -> Object:
	pass;

## Returns an array of dictionaries representing the current call stack. See also [method print_stack].
## [codeblock]
## func _ready():
## foo()
## func foo():
## bar()
## func bar():
## print(get_stack())
## [/codeblock]
## Starting from [code]_ready()[/code], [code]bar()[/code] would print:
## [codeblock lang=text]
## [{function:bar, line:12, source:res://script.gd}, {function:foo, line:9, source:res://script.gd}, {function:_ready, line:6, source:res://script.gd}]
## [/codeblock]
## [b]Note:[/b] This function only works if the running instance is connected to a debugging server (i.e. an editor instance). [method get_stack] will not work in projects exported in release mode, or in projects exported in debug mode if not connected to a debugging server.
## [b]Note:[/b] Calling this function from a [Thread] is not supported. Doing so will return an empty array.
func get_stack() -> Array:
	pass;

## Returns the passed [param instance] converted to a [Dictionary]. Can be useful for serializing.
## [codeblock]
## var foo = "bar"
## func _ready():
## var d = inst_to_dict(self)
## print(d.keys())
## print(d.values())
## [/codeblock]
## Prints out:
## [codeblock lang=text]
## [@subpath, @path, foo]
## [, res://test.gd, bar]
## [/codeblock]
## [b]Note:[/b] This function can only be used to serialize objects with an attached [GDScript] stored in a separate file. Objects without an attached script, with a script written in another language, or with a built-in script are not supported.
## [b]Note:[/b] This function is not recursive, which means that nested objects will not be represented as dictionaries. Also, properties passed by reference ([Object], [Dictionary], [Array], and packed arrays) are copied by reference, not duplicated.
func inst_to_dict(instance: Object) -> Dictionary:
	pass;

## Returns [code]true[/code] if [param value] is an instance of [param type]. The [param type] value must be one of the following:
## - A constant from the [enum Variant.Type] enumeration, for example [constant TYPE_INT].
## - An [Object]-derived class which exists in [ClassDB], for example [Node].
## - A [Script] (you can use any class, including inner one).
## Unlike the right operand of the [code]is[/code] operator, [param type] can be a non-constant value. The [code]is[/code] operator supports more features (such as typed arrays). Use the operator instead of this method if you do not need to check the type dynamically.
## [b]Examples:[/b]
## [codeblock]
## print(is_instance_of(a, TYPE_INT))
## print(is_instance_of(a, Node))
## print(is_instance_of(a, MyClass))
## print(is_instance_of(a, MyClass.InnerClass))
## [/codeblock]
## [b]Note:[/b] If [param value] and/or [param type] are freed objects (see [method @GlobalScope.is_instance_valid]), or [param type] is not one of the above options, this method will raise a runtime error.
## See also [method @GlobalScope.typeof], [method type_exists], [method Array.is_same_typed] (and other [Array] methods).
func is_instance_of(value: Variant, type: Variant) -> bool:
	pass;

## Returns the length of the given Variant [param var]. The length can be the character count of a [String] or [StringName], the element count of any array type, or the size of a [Dictionary]. For every other Variant type, a run-time error is generated and execution is stopped.
## [codeblock]
## var a = [1, 2, 3, 4]
## len(a) # Returns 4
## var b = "Hello!"
## len(b) # Returns 6
## [/codeblock]
func len(variable: Variant) -> int:
	pass;

## Returns a [Resource] from the filesystem located at the absolute [param path]. Unless it's already referenced elsewhere (such as in another script or in the scene), the resource is loaded from disk on function call, which might cause a slight delay, especially when loading large scenes. To avoid unnecessary delays when loading something multiple times, either store the resource in a variable or use [method preload]. This method is equivalent of using [method ResourceLoader.load] with [constant ResourceLoader.CACHE_MODE_REUSE].
## [b]Note:[/b] Resource paths can be obtained by right-clicking on a resource in the FileSystem dock and choosing "Copy Path", or by dragging the file from the FileSystem dock into the current script.
## [codeblock]
## # Load a scene called "main" located in the root of the project directory and cache it in a variable.
## var main = load("res://main.tscn") # main will contain a PackedScene resource.
## [/codeblock]
## [b]Important:[/b] Relative paths are [i]not[/i] relative to the script calling this method, instead it is prefixed with [code]"res://"[/code]. Loading from relative paths might not work as expected.
## This function is a simplified version of [method ResourceLoader.load], which can be used for more advanced scenarios.
## [b]Note:[/b] Files have to be imported into the engine first to load them using this function. If you want to load [Image]s at run-time, you may use [method Image.load]. If you want to import audio files, you can use the snippet described in [member AudioStreamMP3.data].
## [b]Note:[/b] If [member ProjectSettings.editor/export/convert_text_resources_to_binary] is [code]true[/code], [method @GDScript.load] will not be able to read converted files in an exported project. If you rely on run-time loading of files present within the PCK, set [member ProjectSettings.editor/export/convert_text_resources_to_binary] to [code]false[/code].
func load(path: String) -> Resource:
	pass;

## Returns a [Resource] from the filesystem located at [param path]. During run-time, the resource is loaded when the script is being parsed. This function effectively acts as a reference to that resource. Note that this function requires [param path] to be a constant [String]. If you want to load a resource from a dynamic/variable path, use [method load].
## [b]Note:[/b] Resource paths can be obtained by right-clicking on a resource in the Assets Panel and choosing "Copy Path", or by dragging the file from the FileSystem dock into the current script.
## [codeblock]
## # Create instance of a scene.
## var diamond = preload("res://diamond.tscn").instantiate()
## [/codeblock]
## [b]Note:[/b] [method preload] is a keyword, not a function. So you cannot access it as a [Callable].
func preload(path: String) -> Resource:
	pass;

## Like [method @GlobalScope.print], but includes the current stack frame when running with the debugger turned on.
## The output in the console may look like the following:
## [codeblock lang=text]
## Test print
## At: res://test.gd:15:_process()
## [/codeblock]
## [b]Note:[/b] Calling this function from a [Thread] is not supported. Doing so will instead print the thread ID.
vararg func print_debug() -> void:
	pass;

## Prints a stack trace at the current code location. See also [method get_stack].
## The output in the console may look like the following:
## [codeblock lang=text]
## Frame 0 - res://test.gd:16 in function '_process'
## [/codeblock]
## [b]Note:[/b] This function only works if the running instance is connected to a debugging server (i.e. an editor instance). [method print_stack] will not work in projects exported in release mode, or in projects exported in debug mode if not connected to a debugging server.
## [b]Note:[/b] Calling this function from a [Thread] is not supported. Doing so will instead print the thread ID.
func print_stack() -> void:
	pass;

## Returns an array with the given range. [method range] can be called in three ways:
## [code]range(n: int)[/code]: Starts from 0, increases by steps of 1, and stops [i]before[/i] [code]n[/code]. The argument [code]n[/code] is [b]exclusive[/b].
## [code]range(b: int, n: int)[/code]: Starts from [code]b[/code], increases by steps of 1, and stops [i]before[/i] [code]n[/code]. The arguments [code]b[/code] and [code]n[/code] are [b]inclusive[/b] and [b]exclusive[/b], respectively.
## [code]range(b: int, n: int, s: int)[/code]: Starts from [code]b[/code], increases/decreases by steps of [code]s[/code], and stops [i]before[/i] [code]n[/code]. The arguments [code]b[/code] and [code]n[/code] are [b]inclusive[/b] and [b]exclusive[/b], respectively. The argument [code]s[/code] [b]can[/b] be negative, but not [code]0[/code]. If [code]s[/code] is [code]0[/code], an error message is printed.
## [method range] converts all arguments to [int] before processing.
## [b]Note:[/b] Returns an empty array if no value meets the value constraint (e.g. [code]range(2, 5, -1)[/code] or [code]range(5, 5, 1)[/code]).
## [b]Examples:[/b]
## [codeblock]
## print(range(4))        # Prints [0, 1, 2, 3]
## print(range(2, 5))     # Prints [2, 3, 4]
## print(range(0, 6, 2))  # Prints [0, 2, 4]
## print(range(4, 1, -1)) # Prints [4, 3, 2]
## [/codeblock]
## To iterate over an [Array] backwards, use:
## [codeblock]
## var array = [3, 6, 9]
## for i in range(array.size() - 1, -1, -1):
## print(array[i])
## [/codeblock]
## Output:
## [codeblock lang=text]
## 9
## 6
## 3
## [/codeblock]
## To iterate over [float], convert them in the loop.
## [codeblock]
## for i in range (3, 0, -1):
## print(i / 10.0)
## [/codeblock]
## Output:
## [codeblock lang=text]
## 0.3
## 0.2
## 0.1
## [/codeblock]
vararg func range() -> Array:
	pass;

## Returns [code]true[/code] if the given [Object]-derived class exists in [ClassDB]. Note that [Variant] data types are not registered in [ClassDB].
## [codeblock]
## type_exists("Sprite2D") # Returns true
## type_exists("NonExistentClass") # Returns false
## [/codeblock]
func type_exists(type: StringName) -> bool:
	pass;


