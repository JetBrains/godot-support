class_name Array

## A built-in data structure that holds a sequence of elements.
##
## An array data structure that can contain a sequence of elements of any [Variant] type. Elements are accessed by a numerical index starting at [code]0[/code]. Negative indices are used to count from the back ([code]-1[/code] is the last element, [code]-2[/code] is the second to last, etc.).
## [codeblocks]
## [gdscript]
## var array = ["First", 2, 3, "Last"]
## print(array[0])  # Prints "First"
## print(array[2])  # Prints 3
## print(array[-1]) # Prints "Last"
## array[1] = "Second"
## print(array[1])  # Prints "Second"
## print(array[-3]) # Prints "Second"
## [/gdscript]
## [csharp]
## Godot.Collections.Array array = ["First", 2, 3, "Last"];
## GD.Print(array[0]); // Prints "First"
## GD.Print(array[2]); // Prints 3
## GD.Print(array[^1]); // Prints "Last"
## array[1] = "Second";
## GD.Print(array[1]); // Prints "Second"
## GD.Print(array[^3]); // Prints "Second"
## [/csharp]
## [/codeblocks]
## [b]Note:[/b] Arrays are always passed by [b]reference[/b]. To get a copy of an array that can be modified independently of the original array, use [method duplicate].
## [b]Note:[/b] Erasing elements while iterating over arrays is [b]not[/b] supported and will result in unpredictable behavior.
## [b]Differences between packed arrays, typed arrays, and untyped arrays:[/b] Packed arrays are generally faster to iterate on and modify compared to a typed array of the same type (e.g. [PackedInt64Array] versus [code]Array[int][/code]). Also, packed arrays consume less memory. As a downside, packed arrays are less flexible as they don't offer as many convenience methods such as [method Array.map]. Typed arrays are in turn faster to iterate on and modify than untyped arrays.




## Constructs an empty [Array].
func Array() -> Array:
	pass;

## Creates a typed array from the [param base] array. A typed array can only contain elements of the given type, or that inherit from the given class, as described by this constructor's parameters:
## - [param type] is the built-in [Variant] type, as one the [enum Variant.Type] constants.
## - [param class_name] is the built-in class name (see [method Object.get_class]).
## - [param script] is the associated script. It must be a [Script] instance or [code]null[/code].
## If [param type] is not [constant TYPE_OBJECT], [param class_name] must be an empty [StringName] and [param script] must be [code]null[/code].
## [codeblock]
## class_name Sword
## extends Node
## class Stats:
## pass
## func _ready():
## var a = Array([], TYPE_INT, "", null)               # Array[int]
## var b = Array([], TYPE_OBJECT, "Node", null)        # Array[Node]
## var c = Array([], TYPE_OBJECT, "Node", Sword)       # Array[Sword]
## var d = Array([], TYPE_OBJECT, "RefCounted", Stats) # Array[Stats]
## [/codeblock]
## The [param base] array's elements are converted when necessary. If this is not possible or [param base] is already typed, this constructor fails and returns an empty [Array].
## In GDScript, this constructor is usually not necessary, as it is possible to create a typed array through static typing:
## [codeblock]
## var numbers: Array[float] = []
## var children: Array[Node] = [$Node, $Sprite2D, $RigidBody3D]
## var integers: Array[int] = [0.2, 4.5, -2.0]
## print(integers) # Prints [0, 4, -2]
## [/codeblock]
func Array(base: Array, type: int, class_name: StringName, script: Variant) -> Array:
	pass;

## Returns the same array as [param from]. If you need a copy of the array, use [method duplicate].
func Array(from: Array) -> Array:
	pass;

## Constructs an array from a [PackedByteArray].
func Array(from: PackedByteArray) -> Array:
	pass;

## Constructs an array from a [PackedColorArray].
func Array(from: PackedColorArray) -> Array:
	pass;

## Constructs an array from a [PackedFloat32Array].
func Array(from: PackedFloat32Array) -> Array:
	pass;

## Constructs an array from a [PackedFloat64Array].
func Array(from: PackedFloat64Array) -> Array:
	pass;

## Constructs an array from a [PackedInt32Array].
func Array(from: PackedInt32Array) -> Array:
	pass;

## Constructs an array from a [PackedInt64Array].
func Array(from: PackedInt64Array) -> Array:
	pass;

## Constructs an array from a [PackedStringArray].
func Array(from: PackedStringArray) -> Array:
	pass;

## Constructs an array from a [PackedVector2Array].
func Array(from: PackedVector2Array) -> Array:
	pass;

## Constructs an array from a [PackedVector3Array].
func Array(from: PackedVector3Array) -> Array:
	pass;

## Constructs an array from a [PackedVector4Array].
func Array(from: PackedVector4Array) -> Array:
	pass;


## Calls the given [Callable] on each element in the array and returns [code]true[/code] if the [Callable] returns [code]true[/code] for [i]all[/i] elements in the array. If the [Callable] returns [code]false[/code] for one array element or more, this method returns [code]false[/code].
## The [param method] should take one [Variant] parameter (the current array element) and return a [bool].
## [codeblocks]
## [gdscript]
## func greater_than_5(number):
## return number > 5
## func _ready():
## print([6, 10, 6].all(greater_than_5)) # Prints true (3/3 elements evaluate to true).
## print([4, 10, 4].all(greater_than_5)) # Prints false (1/3 elements evaluate to true).
## print([4, 4, 4].all(greater_than_5))  # Prints false (0/3 elements evaluate to true).
## print([].all(greater_than_5))         # Prints true (0/0 elements evaluate to true).
## # Same as the first line above, but using a lambda function.
## print([6, 10, 6].all(func(element): return element > 5)) # Prints true
## [/gdscript]
## [csharp]
## private static bool GreaterThan5(int number)
## {
## return number > 5;
## }
## public override void _Ready()
## {
## // Prints True (3/3 elements evaluate to true).
## GD.Print(new Godot.Collections.Array>int< { 6, 10, 6 }.All(GreaterThan5));
## // Prints False (1/3 elements evaluate to true).
## GD.Print(new Godot.Collections.Array>int< { 4, 10, 4 }.All(GreaterThan5));
## // Prints False (0/3 elements evaluate to true).
## GD.Print(new Godot.Collections.Array>int< { 4, 4, 4 }.All(GreaterThan5));
## // Prints True (0/0 elements evaluate to true).
## GD.Print(new Godot.Collections.Array>int< { }.All(GreaterThan5));
## // Same as the first line above, but using a lambda function.
## GD.Print(new Godot.Collections.Array>int< { 6, 10, 6 }.All(element => element > 5)); // Prints True
## }
## [/csharp]
## [/codeblocks]
## See also [method any], [method filter], [method map] and [method reduce].
## [b]Note:[/b] Unlike relying on the size of an array returned by [method filter], this method will return as early as possible to improve performance (especially with large arrays).
## [b]Note:[/b] For an empty array, this method [url=https://en.wikipedia.org/wiki/Vacuous_truth]always[/url] returns [code]true[/code].
func all(method: Callable) -> bool:
	pass;

## Calls the given [Callable] on each element in the array and returns [code]true[/code] if the [Callable] returns [code]true[/code] for [i]one or more[/i] elements in the array. If the [Callable] returns [code]false[/code] for all elements in the array, this method returns [code]false[/code].
## The [param method] should take one [Variant] parameter (the current array element) and return a [bool].
## [codeblock]
## func greater_than_5(number):
## return number > 5
## func _ready():
## print([6, 10, 6].any(greater_than_5)) # Prints true (3 elements evaluate to true).
## print([4, 10, 4].any(greater_than_5)) # Prints true (1 elements evaluate to true).
## print([4, 4, 4].any(greater_than_5))  # Prints false (0 elements evaluate to true).
## print([].any(greater_than_5))         # Prints false (0 elements evaluate to true).
## # Same as the first line above, but using a lambda function.
## print([6, 10, 6].any(func(number): return number > 5)) # Prints true
## [/codeblock]
## See also [method all], [method filter], [method map] and [method reduce].
## [b]Note:[/b] Unlike relying on the size of an array returned by [method filter], this method will return as early as possible to improve performance (especially with large arrays).
## [b]Note:[/b] For an empty array, this method always returns [code]false[/code].
func any(method: Callable) -> bool:
	pass;

## Appends [param value] at the end of the array (alias of [method push_back]).
func append(value: Variant) -> void:
	pass;

## Appends another [param array] at the end of this array.
## [codeblock]
## var numbers = [1, 2, 3]
## var extra = [4, 5, 6]
## numbers.append_array(extra)
## print(numbers) # Prints [1, 2, 3, 4, 5, 6]
## [/codeblock]
func append_array(array: Array) -> void:
	pass;

## Assigns elements of another [param array] into the array. Resizes the array to match [param array]. Performs type conversions if the array is typed.
func assign(array: Array) -> void:
	pass;

## Returns the last element of the array. If the array is empty, fails and returns [code]null[/code]. See also [method front].
## [b]Note:[/b] Unlike with the [code][][/code] operator ([code]array[-1][/code]), an error is generated without stopping project execution.
func back() -> Variant:
	pass;

## Returns the index of [param value] in the sorted array. If it cannot be found, returns where [param value] should be inserted to keep the array sorted. The algorithm used is [url=https://en.wikipedia.org/wiki/Binary_search_algorithm]binary search[/url].
## If [param before] is [code]true[/code] (as by default), the returned index comes before all existing elements equal to [param value] in the array.
## [codeblock]
## var numbers = [2, 4, 8, 10]
## var idx = numbers.bsearch(7)
## numbers.insert(idx, 7)
## print(numbers) # Prints [2, 4, 7, 8, 10]
## var fruits = ["Apple", "Lemon", "Lemon", "Orange"]
## print(fruits.bsearch("Lemon", true))  # Prints 1, points at the first "Lemon".
## print(fruits.bsearch("Lemon", false)) # Prints 3, points at "Orange".
## [/codeblock]
## [b]Note:[/b] Calling [method bsearch] on an [i]unsorted[/i] array will result in unexpected behavior. Use [method sort] before calling this method.
func bsearch(value: Variant, before: bool = true) -> int:
	pass;

## Returns the index of [param value] in the sorted array. If it cannot be found, returns where [param value] should be inserted to keep the array sorted (using [param func] for the comparisons). The algorithm used is [url=https://en.wikipedia.org/wiki/Binary_search_algorithm]binary search[/url].
## Similar to [method sort_custom], [param func] is called as many times as necessary, receiving one array element and [param value] as arguments. The function should return [code]true[/code] if the array element should be [i]behind[/i] [param value], otherwise it should return [code]false[/code].
## If [param before] is [code]true[/code] (as by default), the returned index comes before all existing elements equal to [param value] in the array.
## [codeblock]
## func sort_by_amount(a, b):
## if a[1] < b[1]:
## return true
## return false
## func _ready():
## var my_items = [["Tomato", 2], ["Kiwi", 5], ["Rice", 9]]
## var apple = ["Apple", 5]
## # "Apple" is inserted before "Kiwi".
## my_items.insert(my_items.bsearch_custom(apple, sort_by_amount, true), apple)
## var banana = ["Banana", 5]
## # "Banana" is inserted after "Kiwi".
## my_items.insert(my_items.bsearch_custom(banana, sort_by_amount, false), banana)
## # Prints [["Tomato", 2], ["Apple", 5], ["Kiwi", 5], ["Banana", 5], ["Rice", 9]]
## print(my_items)
## [/codeblock]
## [b]Note:[/b] Calling [method bsearch_custom] on an [i]unsorted[/i] array will result in unexpected behavior. Use [method sort_custom] with [param func] before calling this method.
func bsearch_custom(value: Variant, func: Callable, before: bool = true) -> int:
	pass;

## Removes all elements from the array. This is equivalent to using [method resize] with a size of [code]0[/code].
func clear() -> void:
	pass;

## Returns the number of times an element is in the array.
## To count how many elements in an array satisfy a condition, see [method reduce].
func count(value: Variant) -> int:
	pass;

## Returns a new copy of the array.
## By default, a [b]shallow[/b] copy is returned: all nested [Array] and [Dictionary] elements are shared with the original array. Modifying them in one array will also affect them in the other.[br]If [param deep] is [code]true[/code], a [b]deep[/b] copy is returned: all nested arrays and dictionaries are also duplicated (recursively).
func duplicate(deep: bool = false) -> Array:
	pass;

## Finds and removes the first occurrence of [param value] from the array. If [param value] does not exist in the array, nothing happens. To remove an element by index, use [method remove_at] instead.
## [b]Note:[/b] This method shifts every element's index after the removed [param value] back, which may have a noticeable performance cost, especially on larger arrays.
## [b]Note:[/b] Erasing elements while iterating over arrays is [b]not[/b] supported and will result in unpredictable behavior.
func erase(value: Variant) -> void:
	pass;

## Assigns the given [param value] to all elements in the array.
## This method can often be combined with [method resize] to create an array with a given size and initialized elements:
## [codeblocks]
## [gdscript]
## var array = []
## array.resize(5)
## array.fill(2)
## print(array) # Prints [2, 2, 2, 2, 2]
## [/gdscript]
## [csharp]
## Godot.Collections.Array array = [];
## array.Resize(5);
## array.Fill(2);
## GD.Print(array); // Prints [2, 2, 2, 2, 2]
## [/csharp]
## [/codeblocks]
## [b]Note:[/b] If [param value] is a [Variant] passed by reference ([Object]-derived, [Array], [Dictionary], etc.), the array will be filled with references to the same [param value], which are not duplicates.
func fill(value: Variant) -> void:
	pass;

## Calls the given [Callable] on each element in the array and returns a new, filtered [Array].
## The [param method] receives one of the array elements as an argument, and should return [code]true[/code] to add the element to the filtered array, or [code]false[/code] to exclude it.
## [codeblock]
## func is_even(number):
## return number % 2 == 0
## func _ready():
## print([1, 4, 5, 8].filter(is_even)) # Prints [4, 8]
## # Same as above, but using a lambda function.
## print([1, 4, 5, 8].filter(func(number): return number % 2 == 0))
## [/codeblock]
## See also [method any], [method all], [method map] and [method reduce].
func filter(method: Callable) -> Array:
	pass;

## Returns the index of the [b]first[/b] occurrence of [param what] in this array, or [code]-1[/code] if there are none. The search's start can be specified with [param from], continuing to the end of the array.
## [b]Note:[/b] If you just want to know whether the array contains [param what], use [method has] ([code]Contains[/code] in C#). In GDScript, you may also use the [code]in[/code] operator.
## [b]Note:[/b] For performance reasons, the search is affected by [param what]'s [enum Variant.Type]. For example, [code]7[/code] ([int]) and [code]7.0[/code] ([float]) are not considered equal for this method.
func find(what: Variant, from: int = 0) -> int:
	pass;

## Returns the index of the [b]first[/b] element in the array that causes [param method] to return [code]true[/code], or [code]-1[/code] if there are none. The search's start can be specified with [param from], continuing to the end of the array.
## [param method] is a callable that takes an element of the array, and returns a [bool].
## [b]Note:[/b] If you just want to know whether the array contains [i]anything[/i] that satisfies [param method], use [method any].
## [codeblocks]
## [gdscript]
## func is_even(number):
## return number % 2 == 0
## func _ready():
## print([1, 3, 4, 7].find_custom(is_even.bind())) # Prints 2
## [/gdscript]
## [/codeblocks]
func find_custom(method: Callable, from: int = 0) -> int:
	pass;

## Returns the first element of the array. If the array is empty, fails and returns [code]null[/code]. See also [method back].
## [b]Note:[/b] Unlike with the [code][][/code] operator ([code]array[0][/code]), an error is generated without stopping project execution.
func front() -> Variant:
	pass;

## Returns the element at the given [param index] in the array. This is the same as using the [code][][/code] operator ([code]array[index][/code]).
func get(index: int) -> Variant:
	pass;

## Returns the built-in [Variant] type of the typed array as a [enum Variant.Type] constant. If the array is not typed, returns [constant TYPE_NIL]. See also [method is_typed].
func get_typed_builtin() -> int:
	pass;

## Returns the [b]built-in[/b] class name of the typed array, if the built-in [Variant] type [constant TYPE_OBJECT]. Otherwise, returns an empty [StringName]. See also [method is_typed] and [method Object.get_class].
func get_typed_class_name() -> StringName:
	pass;

## Returns the [Script] instance associated with this typed array, or [code]null[/code] if it does not exist. See also [method is_typed].
func get_typed_script() -> Variant:
	pass;

## Returns [code]true[/code] if the array contains the given [param value].
## [codeblocks]
## [gdscript]
## print(["inside", 7].has("inside"))  # Prints true
## print(["inside", 7].has("outside")) # Prints false
## print(["inside", 7].has(7))         # Prints true
## print(["inside", 7].has("7"))       # Prints false
## [/gdscript]
## [csharp]
## Godot.Collections.Array arr = ["inside", 7];
## // By C# convention, this method is renamed to `Contains`.
## GD.Print(arr.Contains("inside"));  // Prints True
## GD.Print(arr.Contains("outside")); // Prints False
## GD.Print(arr.Contains(7));         // Prints True
## GD.Print(arr.Contains("7"));       // Prints False
## [/csharp]
## [/codeblocks]
## In GDScript, this is equivalent to the [code]in[/code] operator:
## [codeblock]
## if 4 in [2, 4, 6, 8]:
## print("4 is here!") # Will be printed.
## [/codeblock]
## [b]Note:[/b] For performance reasons, the search is affected by the [param value]'s [enum Variant.Type]. For example, [code]7[/code] ([int]) and [code]7.0[/code] ([float]) are not considered equal for this method.
func has(value: Variant) -> bool:
	pass;

## Returns a hashed 32-bit integer value representing the array and its contents.
## [b]Note:[/b] Arrays with equal hash values are [i]not[/i] guaranteed to be the same, as a result of hash collisions. On the countrary, arrays with different hash values are guaranteed to be different.
func hash() -> int:
	pass;

## Inserts a new element ([param value]) at a given index ([param position]) in the array. [param position] should be between [code]0[/code] and the array's [method size].
## Returns [constant OK] on success, or one of the other [enum Error] constants if this method fails.
## [b]Note:[/b] Every element's index after [param position] needs to be shifted forward, which may have a noticeable performance cost, especially on larger arrays.
func insert(position: int, value: Variant) -> int:
	pass;

## Returns [code]true[/code] if the array is empty ([code][][/code]). See also [method size].
func is_empty() -> bool:
	pass;

## Returns [code]true[/code] if the array is read-only. See [method make_read_only].
## In GDScript, arrays are automatically read-only if declared with the [code]const[/code] keyword.
func is_read_only() -> bool:
	pass;

## Returns [code]true[/code] if this array is typed the same as the given [param array]. See also [method is_typed].
func is_same_typed(array: Array) -> bool:
	pass;

## Returns [code]true[/code] if the array is typed. Typed arrays can only contain elements of a specific type, as defined by the typed array constructor. The methods of a typed array are still expected to return a generic [Variant].
## In GDScript, it is possible to define a typed array with static typing:
## [codeblock]
## var numbers: Array[float] = [0.2, 4.2, -2.0]
## print(numbers.is_typed()) # Prints true
## [/codeblock]
func is_typed() -> bool:
	pass;

## Makes the array read-only. The array's elements cannot be overridden with different values, and their order cannot change. Does not apply to nested elements, such as dictionaries.
## In GDScript, arrays are automatically read-only if declared with the [code]const[/code] keyword.
func make_read_only() -> void:
	pass;

## Calls the given [Callable] for each element in the array and returns a new array filled with values returned by the [param method].
## The [param method] should take one [Variant] parameter (the current array element) and can return any [Variant].
## [codeblock]
## func double(number):
## return number * 2
## func _ready():
## print([1, 2, 3].map(double)) # Prints [2, 4, 6]
## # Same as above, but using a lambda function.
## print([1, 2, 3].map(func(element): return element * 2))
## [/codeblock]
## See also [method filter], [method reduce], [method any] and [method all].
func map(method: Callable) -> Array:
	pass;

## Returns the maximum value contained in the array, if all elements can be compared. Otherwise, returns [code]null[/code]. See also [method min].
## To find the maximum value using a custom comparator, you can use [method reduce].
func max() -> Variant:
	pass;

## Returns the minimum value contained in the array, if all elements can be compared. Otherwise, returns [code]null[/code]. See also [method max].
func min() -> Variant:
	pass;

## Returns a random element from the array. Generates an error and returns [code]null[/code] if the array is empty.
## [codeblocks]
## [gdscript]
## # May print 1, 2, 3.25, or "Hi".
## print([1, 2, 3.25, "Hi"].pick_random())
## [/gdscript]
## [csharp]
## Godot.Collections.Array array = [1, 2, 3.25f, "Hi"];
## GD.Print(array.PickRandom()); // May print 1, 2, 3.25, or "Hi".
## [/csharp]
## [/codeblocks]
## [b]Note:[/b] Like many similar functions in the engine (such as [method @GlobalScope.randi] or [method shuffle]), this method uses a common, global random seed. To get a predictable outcome from this method, see [method @GlobalScope.seed].
func pick_random() -> Variant:
	pass;

## Removes and returns the element of the array at index [param position]. If negative, [param position] is considered relative to the end of the array. Returns [code]null[/code] if the array is empty. If [param position] is out of bounds, an error message is also generated.
## [b]Note:[/b] This method shifts every element's index after [param position] back, which may have a noticeable performance cost, especially on larger arrays.
func pop_at(position: int) -> Variant:
	pass;

## Removes and returns the last element of the array. Returns [code]null[/code] if the array is empty, without generating an error. See also [method pop_front].
func pop_back() -> Variant:
	pass;

## Removes and returns the first element of the array. Returns [code]null[/code] if the array is empty, without generating an error. See also [method pop_back].
## [b]Note:[/b] This method shifts every other element's index back, which may have a noticeable performance cost, especially on larger arrays.
func pop_front() -> Variant:
	pass;

## Appends an element at the end of the array. See also [method push_front].
func push_back(value: Variant) -> void:
	pass;

## Adds an element at the beginning of the array. See also [method push_back].
## [b]Note:[/b] This method shifts every other element's index forward, which may have a noticeable performance cost, especially on larger arrays.
func push_front(value: Variant) -> void:
	pass;

## Calls the given [Callable] for each element in array, accumulates the result in [param accum], then returns it.
## The [param method] takes two arguments: the current value of [param accum] and the current array element. If [param accum] is [code]null[/code] (as by default), the iteration will start from the second element, with the first one used as initial value of [param accum].
## [codeblock]
## func sum(accum, number):
## return accum + number
## func _ready():
## print([1, 2, 3].reduce(sum, 0))  # Prints 6
## print([1, 2, 3].reduce(sum, 10)) # Prints 16
## # Same as above, but using a lambda function.
## print([1, 2, 3].reduce(func(accum, number): return accum + number, 10))
## [/codeblock]
## If [method max] is not desirable, this method may also be used to implement a custom comparator:
## [codeblock]
## func _ready():
## var arr = [Vector2i(5, 0), Vector2i(3, 4), Vector2i(1, 2)]
## var longest_vec = arr.reduce(func(max, vec): return vec if is_length_greater(vec, max) else max)
## print(longest_vec) # Prints (3, 4)
## func is_length_greater(a, b):
## return a.length() > b.length()
## [/codeblock]
## This method can also be used to count how many elements in an array satisfy a certain condition, similar to [method count]:
## [codeblock]
## func is_even(number):
## return number % 2 == 0
## func _ready():
## var arr = [1, 2, 3, 4, 5]
## # If the current element is even, increment count, otherwise leave count the same.
## var even_count = arr.reduce(func(count, next): return count + 1 if is_even(next) else count, 0)
## print(even_count) # Prints 2
## [/codeblock]
## See also [method map], [method filter], [method any], and [method all].
func reduce(method: Callable, accum: Variant = null) -> Variant:
	pass;

## Removes the element from the array at the given index ([param position]). If the index is out of bounds, this method fails.
## If you need to return the removed element, use [method pop_at]. To remove an element by value, use [method erase] instead.
## [b]Note:[/b] This method shifts every element's index after [param position] back, which may have a noticeable performance cost, especially on larger arrays.
## [b]Note:[/b] The [param position] cannot be negative. To remove an element relative to the end of the array, use [code]arr.remove_at(arr.size() - (i + 1))[/code]. To remove the last element from the array, use [code]arr.resize(arr.size() - 1)[/code].
func remove_at(position: int) -> void:
	pass;

## Sets the array's number of elements to [param size]. If [param size] is smaller than the array's current size, the elements at the end are removed. If [param size] is greater, new default elements (usually [code]null[/code]) are added, depending on the array's type.
## Returns [constant OK] on success, or one of the other [enum Error] constants if this method fails.
## [b]Note:[/b] Calling this method once and assigning the new values is faster than calling [method append] for every new element.
func resize(size: int) -> int:
	pass;

## Reverses the order of all elements in the array.
func reverse() -> void:
	pass;

## Returns the index of the [b]last[/b] occurrence of [param what] in this array, or [code]-1[/code] if there are none. The search's start can be specified with [param from], continuing to the beginning of the array. This method is the reverse of [method find].
func rfind(what: Variant, from: int = -1) -> int:
	pass;

## Returns the index of the [b]last[/b] element of the array that causes [param method] to return [code]true[/code], or [code]-1[/code] if there are none. The search's start can be specified with [param from], continuing to the beginning of the array. This method is the reverse of [method find_custom].
func rfind_custom(method: Callable, from: int = -1) -> int:
	pass;

## Sets the value of the element at the given [param index] to the given [param value]. This will not change the size of the array, it only changes the value at an index already in the array. This is the same as using the [code][][/code] operator ([code]array[index] = value[/code]).
func set(index: int, value: Variant) -> void:
	pass;

## Shuffles all elements of the array in a random order.
## [b]Note:[/b] Like many similar functions in the engine (such as [method @GlobalScope.randi] or [method pick_random]), this method uses a common, global random seed. To get a predictable outcome from this method, see [method @GlobalScope.seed].
func shuffle() -> void:
	pass;

## Returns the number of elements in the array. Empty arrays ([code][][/code]) always return [code]0[/code]. See also [method is_empty].
func size() -> int:
	pass;

## Returns a new [Array] containing this array's elements, from index [param begin] (inclusive) to [param end] (exclusive), every [param step] elements.
## If either [param begin] or [param end] are negative, their value is relative to the end of the array.
## If [param step] is negative, this method iterates through the array in reverse, returning a slice ordered backwards. For this to work, [param begin] must be greater than [param end].
## If [param deep] is [code]true[/code], all nested [Array] and [Dictionary] elements in the slice are duplicated from the original, recursively. See also [method duplicate]).
## [codeblock]
## var letters = ["A", "B", "C", "D", "E", "F"]
## print(letters.slice(0, 2))  # Prints ["A", "B"]
## print(letters.slice(2, -2)) # Prints ["C", "D"]
## print(letters.slice(-2, 6)) # Prints ["E", "F"]
## print(letters.slice(0, 6, 2))  # Prints ["A", "C", "E"]
## print(letters.slice(4, 1, -1)) # Prints ["E", "D", "C"]
## [/codeblock]
func slice(begin: int, end: int = 2147483647, step: int = 1, deep: bool = false) -> Array:
	pass;

## Sorts the array in ascending order. The final order is dependent on the "less than" ([code]<[/code]) comparison between elements.
## [codeblocks]
## [gdscript]
## var numbers = [10, 5, 2.5, 8]
## numbers.sort()
## print(numbers) # Prints [2.5, 5, 8, 10]
## [/gdscript]
## [csharp]
## Godot.Collections.Array numbers = [10, 5, 2.5, 8];
## numbers.Sort();
## GD.Print(numbers); // Prints [2.5, 5, 8, 10]
## [/csharp]
## [/codeblocks]
## [b]Note:[/b] The sorting algorithm used is not [url=https://en.wikipedia.org/wiki/Sorting_algorithm#Stability]stable[/url]. This means that equivalent elements (such as [code]2[/code] and [code]2.0[/code]) may have their order changed when calling [method sort].
func sort() -> void:
	pass;

## Sorts the array using a custom [Callable].
## [param func] is called as many times as necessary, receiving two array elements as arguments. The function should return [code]true[/code] if the first element should be moved [i]before[/i] the second one, otherwise it should return [code]false[/code].
## [codeblock]
## func sort_ascending(a, b):
## if a[1] < b[1]:
## return true
## return false
## func _ready():
## var my_items = [["Tomato", 5], ["Apple", 9], ["Rice", 4]]
## my_items.sort_custom(sort_ascending)
## print(my_items) # Prints [["Rice", 4], ["Tomato", 5], ["Apple", 9]]
## # Sort descending, using a lambda function.
## my_items.sort_custom(func(a, b): return a[1] > b[1])
## print(my_items) # Prints [["Apple", 9], ["Tomato", 5], ["Rice", 4]]
## [/codeblock]
## It may also be necessary to use this method to sort strings by natural order, with [method String.naturalnocasecmp_to], as in the following example:
## [codeblock]
## var files = ["newfile1", "newfile2", "newfile10", "newfile11"]
## files.sort_custom(func(a, b): return a.naturalnocasecmp_to(b) < 0)
## print(files) # Prints ["newfile1", "newfile2", "newfile10", "newfile11"]
## [/codeblock]
## [b]Note:[/b] In C#, this method is not supported.
## [b]Note:[/b] The sorting algorithm used is not [url=https://en.wikipedia.org/wiki/Sorting_algorithm#Stability]stable[/url]. This means that values considered equal may have their order changed when calling this method.
## [b]Note:[/b] You should not randomize the return value of [param func], as the heapsort algorithm expects a consistent result. Randomizing the return value will result in unexpected behavior.
func sort_custom(func: Callable) -> void:
	pass;


