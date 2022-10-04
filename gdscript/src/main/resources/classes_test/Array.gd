#brief A generic array datatype.
#desc A generic array that can contain several elements of any type, accessible by a numerical index starting at 0. Negative indices can be used to count from the back, like in Python (-1 is the last element, -2 is the second to last, etc.).
#desc [b]Example:[/b]
#desc [codeblocks]
#desc [gdscript]
#desc var array = ["One", 2, 3, "Four"]
#desc print(array[0]) # One.
#desc print(array[2]) # 3.
#desc print(array[-1]) # Four.
#desc array[2] = "Three"
#desc print(array[-2]) # Three.
#desc [/gdscript]
#desc [csharp]
#desc var array = new Godot.Collections.Array{"One", 2, 3, "Four"};
#desc GD.Print(array[0]); // One.
#desc GD.Print(array[2]); // 3.
#desc GD.Print(array[array.Count - 1]); // Four.
#desc array[2] = "Three";
#desc GD.Print(array[array.Count - 2]); // Three.
#desc [/csharp]
#desc [/codeblocks]
#desc Arrays can be concatenated using the [code]+[/code] operator:
#desc [codeblocks]
#desc [gdscript]
#desc var array1 = ["One", 2]
#desc var array2 = [3, "Four"]
#desc print(array1 + array2) # ["One", 2, 3, "Four"]
#desc [/gdscript]
#desc [csharp]
#desc // Array concatenation is not possible with C# arrays, but is with Godot.Collections.Array.
#desc var array1 = new Godot.Collections.Array{"One", 2};
#desc var array2 = new Godot.Collections.Array{3, "Four"};
#desc GD.Print(array1 + array2); // Prints [One, 2, 3, Four]
#desc [/csharp]
#desc [/codeblocks]
#desc [b]Note:[/b] Concatenating with the [code]+=[/code] operator will create a new array, which has a cost. If you want to append another array to an existing array, [method append_array] is more efficient.
#desc [b]Note:[/b] Arrays are always passed by reference. To get a copy of an array that can be modified independently of the original array, use [method duplicate].
#desc [b]Note:[/b] When declaring an array with [code]const[/code], the array itself can still be mutated by defining the values at individual indices or pushing/removing elements. Using [code]const[/code] will only prevent assigning the constant with another value after it was initialized.
class_name Array



#desc Constructs an empty [Array].
func Array() -> Array:
	pass;

func Array(base: Array, type: int, class_name: StringName, script: Variant) -> Array:
	pass;

#desc Constructs an [Array] as a copy of the given [Array].
func Array() -> Array:
	pass;

#desc Constructs an array from a [PackedByteArray].
func Array() -> Array:
	pass;

#desc Constructs an array from a [PackedColorArray].
func Array() -> Array:
	pass;

#desc Constructs an array from a [PackedFloat32Array].
func Array() -> Array:
	pass;

#desc Constructs an array from a [PackedFloat64Array].
func Array() -> Array:
	pass;

#desc Constructs an array from a [PackedInt32Array].
func Array() -> Array:
	pass;

#desc Constructs an array from a [PackedInt64Array].
func Array() -> Array:
	pass;

#desc Constructs an array from a [PackedStringArray].
func Array() -> Array:
	pass;

#desc Constructs an array from a [PackedVector2Array].
func Array() -> Array:
	pass;

#desc Constructs an array from a [PackedVector3Array].
func Array() -> Array:
	pass;


#desc Calls the provided [Callable] on each element in the array and returns [code]true[/code] if the [Callable] returns [code]true[/code] for [i]all[/i] elements in the array. If the [Callable] returns [code]false[/code] for one array element or more, this method returns [code]false[/code].
#desc The callable's method should take one [Variant] parameter (the current array element) and return a boolean value.
#desc [codeblock]
#desc func _ready():
#desc print([6, 10, 6].all(greater_than_5))  # Prints True (3/3 elements evaluate to `true`).
#desc print([4, 10, 4].all(greater_than_5))  # Prints False (1/3 elements evaluate to `true`).
#desc print([4, 4, 4].all(greater_than_5))  # Prints False (0/3 elements evaluate to `true`).
#desc print([].all(greater_than_5))  # Prints True (0/0 elements evaluate to `true`).
#desc 
#desc print([6, 10, 6].all(func(number): return number > 5))  # Prints True. Same as the first line above, but using lambda function.
#desc 
#desc func greater_than_5(number):
#desc return number > 5
#desc [/codeblock]
#desc See also [method any], [method filter], [method map] and [method reduce].
#desc [b]Note:[/b] Unlike relying on the size of an array returned by [method filter], this method will return as early as possible to improve performance (especially with large arrays).
#desc [b]Note:[/b] For an empty array, this method [url=https://en.wikipedia.org/wiki/Vacuous_truth]always[/url] returns [code]true[/code].
func all() -> bool:
	pass;

#desc Calls the provided [Callable] on each element in the array and returns [code]true[/code] if the [Callable] returns [code]true[/code] for [i]one or more[/i] elements in the array. If the [Callable] returns [code]false[/code] for all elements in the array, this method returns [code]false[/code].
#desc The callable's method should take one [Variant] parameter (the current array element) and return a boolean value.
#desc [codeblock]
#desc func _ready():
#desc print([6, 10, 6].any(greater_than_5))  # Prints True (3 elements evaluate to `true`).
#desc print([4, 10, 4].any(greater_than_5))  # Prints True (1 elements evaluate to `true`).
#desc print([4, 4, 4].any(greater_than_5))  # Prints False (0 elements evaluate to `true`).
#desc print([].any(greater_than_5))  # Prints False (0 elements evaluate to `true`).
#desc 
#desc print([6, 10, 6].any(func(number): return number > 5))  # Prints True. Same as the first line above, but using lambda function.
#desc 
#desc func greater_than_5(number):
#desc return number > 5
#desc [/codeblock]
#desc See also [method all], [method filter], [method map] and [method reduce].
#desc [b]Note:[/b] Unlike relying on the size of an array returned by [method filter], this method will return as early as possible to improve performance (especially with large arrays).
#desc [b]Note:[/b] For an empty array, this method always returns [code]false[/code].
func any() -> bool:
	pass;

#desc Appends an element at the end of the array (alias of [method push_back]).
func append() -> void:
	pass;

#desc Appends another array at the end of this array.
#desc [codeblock]
#desc var array1 = [1, 2, 3]
#desc var array2 = [4, 5, 6]
#desc array1.append_array(array2)
#desc print(array1) # Prints [1, 2, 3, 4, 5, 6].
#desc [/codeblock]
func append_array() -> void:
	pass;

#desc Returns the last element of the array. Prints an error and returns [code]null[/code] if the array is empty.
#desc [b]Note:[/b] Calling this function is not the same as writing [code]array[-1][/code]. If the array is empty, accessing by index will pause project execution when running from the editor.
func back() -> Variant:
	pass;

#desc Finds the index of an existing value (or the insertion index that maintains sorting order, if the value is not yet present in the array) using binary search. Optionally, a [param before] specifier can be passed. If [code]false[/code], the returned index comes after all existing entries of the value in the array.
#desc [b]Note:[/b] Calling [method bsearch] on an unsorted array results in unexpected behavior.
func bsearch(value: Variant, before: bool) -> int:
	pass;

#desc Finds the index of an existing value (or the insertion index that maintains sorting order, if the value is not yet present in the array) using binary search and a custom comparison method. Optionally, a [param before] specifier can be passed. If [code]false[/code], the returned index comes after all existing entries of the value in the array. The custom method receives two arguments (an element from the array and the value searched for) and must return [code]true[/code] if the first argument is less than the second, and return [code]false[/code] otherwise.
#desc [b]Note:[/b] Calling [method bsearch_custom] on an unsorted array results in unexpected behavior.
func bsearch_custom(value: Variant, func: Callable, before: bool) -> int:
	pass;

#desc Clears the array. This is equivalent to using [method resize] with a size of [code]0[/code].
func clear() -> void:
	pass;

#desc Returns the number of times an element is in the array.
func count() -> int:
	pass;

#desc Returns a copy of the array.
#desc If [param deep] is [code]true[/code], a deep copy is performed: all nested arrays and dictionaries are duplicated and will not be shared with the original array. If [code]false[/code], a shallow copy is made and references to the original nested arrays and dictionaries are kept, so that modifying a sub-array or dictionary in the copy will also impact those referenced in the source array.
func duplicate() -> Array:
	pass;

#desc Removes the first occurrence of a value from the array. If the value does not exist in the array, nothing happens. To remove an element by index, use [method remove_at] instead.
#desc [b]Note:[/b] This method acts in-place and doesn't return a value.
#desc [b]Note:[/b] On large arrays, this method will be slower if the removed element is close to the beginning of the array (index 0). This is because all elements placed after the removed element have to be reindexed.
func erase() -> void:
	pass;

#desc Assigns the given value to all elements in the array. This can typically be used together with [method resize] to create an array with a given size and initialized elements:
#desc [codeblocks]
#desc [gdscript]
#desc var array = []
#desc array.resize(10)
#desc array.fill(0) # Initialize the 10 elements to 0.
#desc [/gdscript]
#desc [csharp]
#desc var array = new Godot.Collections.Array{};
#desc array.Resize(10);
#desc array.Fill(0); // Initialize the 10 elements to 0.
#desc [/csharp]
#desc [/codeblocks]
func fill() -> void:
	pass;

#desc Calls the provided [Callable] on each element in the array and returns a new array with the elements for which the method returned [code]true[/code].
#desc The callable's method should take one [Variant] parameter (the current array element) and return a boolean value.
#desc [codeblock]
#desc func _ready():
#desc print([1, 2, 3].filter(remove_1)) # Prints [2, 3].
#desc print([1, 2, 3].filter(func(number): return number != 1)) # Same as above, but using lambda function.
#desc 
#desc func remove_1(number):
#desc return number != 1
#desc [/codeblock]
#desc See also [method any], [method all], [method map] and [method reduce].
func filter() -> Array:
	pass;

#desc Searches the array for a value and returns its index or [code]-1[/code] if not found. Optionally, the initial search index can be passed.
func find(what: Variant, from: int) -> int:
	pass;

#desc Searches the array in reverse order for a value and returns its index or [code]-1[/code] if not found.
func find_last() -> int:
	pass;

#desc Returns the first element of the array. Prints an error and returns [code]null[/code] if the array is empty.
#desc [b]Note:[/b] Calling this function is not the same as writing [code]array[0][/code]. If the array is empty, accessing by index will pause project execution when running from the editor.
func front() -> Variant:
	pass;

func get_typed_builtin() -> int:
	pass;

func get_typed_class_name() -> StringName:
	pass;

func get_typed_script() -> Variant:
	pass;

#desc Returns [code]true[/code] if the array contains the given value.
#desc [codeblocks]
#desc [gdscript]
#desc print(["inside", 7].has("inside")) # True
#desc print(["inside", 7].has("outside")) # False
#desc print(["inside", 7].has(7)) # True
#desc print(["inside", 7].has("7")) # False
#desc [/gdscript]
#desc [csharp]
#desc var arr = new Godot.Collections.Array{"inside", 7};
#desc // has is renamed to Contains
#desc GD.Print(arr.Contains("inside")); // True
#desc GD.Print(arr.Contains("outside")); // False
#desc GD.Print(arr.Contains(7)); // True
#desc GD.Print(arr.Contains("7")); // False
#desc [/csharp]
#desc [/codeblocks]
#desc 
#desc [b]Note:[/b] This is equivalent to using the [code]in[/code] operator as follows:
#desc [codeblocks]
#desc [gdscript]
#desc # Will evaluate to `true`.
#desc if 2 in [2, 4, 6, 8]:
#desc print("Contains!")
#desc [/gdscript]
#desc [csharp]
#desc // As there is no "in" keyword in C#, you have to use Contains
#desc var array = new Godot.Collections.Array{2, 4, 6, 8};
#desc if (array.Contains(2))
#desc {
#desc GD.Print("Contains!");
#desc }
#desc [/csharp]
#desc [/codeblocks]
func has() -> bool:
	pass;

#desc Returns a hashed 32-bit integer value representing the array and its contents.
#desc [b]Note:[/b] [Array]s with equal content will always produce identical hash values. However, the reverse is not true. Returning identical hash values does [i]not[/i] imply the arrays are equal, because different arrays can have identical hash values due to hash collisions.
func hash() -> int:
	pass;

#desc Inserts a new element at a given position in the array. The position must be valid, or at the end of the array ([code]pos == size()[/code]).
#desc [b]Note:[/b] This method acts in-place and doesn't return a value.
#desc [b]Note:[/b] On large arrays, this method will be slower if the inserted element is close to the beginning of the array (index 0). This is because all elements placed after the newly inserted element have to be reindexed.
func insert(position: int, value: Variant) -> int:
	pass;

#desc Returns [code]true[/code] if the array is empty.
func is_empty() -> bool:
	pass;

func is_read_only() -> bool:
	pass;

func is_typed() -> bool:
	pass;

#desc Calls the provided [Callable] for each element in the array and returns a new array filled with values returned by the method.
#desc The callable's method should take one [Variant] parameter (the current array element) and can return any [Variant].
#desc [codeblock]
#desc func _ready():
#desc print([1, 2, 3].map(negate)) # Prints [-1, -2, -3].
#desc print([1, 2, 3].map(func(number): return -number)) # Same as above, but using lambda function.
#desc 
#desc func negate(number):
#desc return -number
#desc [/codeblock]
#desc See also [method filter], [method reduce], [method any] and [method all].
func map() -> Array:
	pass;

#desc Returns the maximum value contained in the array if all elements are of comparable types. If the elements can't be compared, [code]null[/code] is returned.
func max() -> Variant:
	pass;

#desc Returns the minimum value contained in the array if all elements are of comparable types. If the elements can't be compared, [code]null[/code] is returned.
func min() -> Variant:
	pass;

#desc Removes and returns the element of the array at index [param position]. If negative, [param position] is considered relative to the end of the array. Leaves the array untouched and returns [code]null[/code] if the array is empty or if it's accessed out of bounds. An error message is printed when the array is accessed out of bounds, but not when the array is empty.
#desc [b]Note:[/b] On large arrays, this method can be slower than [method pop_back] as it will reindex the array's elements that are located after the removed element. The larger the array and the lower the index of the removed element, the slower [method pop_at] will be.
func pop_at() -> Variant:
	pass;

#desc Removes and returns the last element of the array. Returns [code]null[/code] if the array is empty, without printing an error message. See also [method pop_front].
func pop_back() -> Variant:
	pass;

#desc Removes and returns the first element of the array. Returns [code]null[/code] if the array is empty, without printing an error message. See also [method pop_back].
#desc [b]Note:[/b] On large arrays, this method is much slower than [method pop_back] as it will reindex all the array's elements every time it's called. The larger the array, the slower [method pop_front] will be.
func pop_front() -> Variant:
	pass;

#desc Appends an element at the end of the array. See also [method push_front].
func push_back() -> void:
	pass;

#desc Adds an element at the beginning of the array. See also [method push_back].
#desc [b]Note:[/b] On large arrays, this method is much slower than [method push_back] as it will reindex all the array's elements every time it's called. The larger the array, the slower [method push_front] will be.
func push_front() -> void:
	pass;

#desc Calls the provided [Callable] for each element in array and accumulates the result in [param accum].
#desc The callable's method takes two arguments: the current value of [param accum] and the current array element. If [param accum] is [code]null[/code] (default value), the iteration will start from the second element, with the first one used as initial value of [param accum].
#desc [codeblock]
#desc func _ready():
#desc print([1, 2, 3].reduce(sum, 10)) # Prints 16.
#desc print([1, 2, 3].reduce(func(accum, number): return accum + number, 10)) # Same as above, but using lambda function.
#desc 
#desc func sum(accum, number):
#desc return accum + number
#desc [/codeblock]
#desc See also [method map], [method filter], [method any] and [method all].
func reduce(method: Callable, accum: Variant) -> Variant:
	pass;

#desc Removes an element from the array by index. If the index does not exist in the array, nothing happens. To remove an element by searching for its value, use [method erase] instead.
#desc [b]Note:[/b] This method acts in-place and doesn't return a value.
#desc [b]Note:[/b] On large arrays, this method will be slower if the removed element is close to the beginning of the array (index 0). This is because all elements placed after the removed element have to be reindexed.
func remove_at() -> void:
	pass;

#desc Resizes the array to contain a different number of elements. If the array size is smaller, elements are cleared, if bigger, new elements are [code]null[/code].
func resize() -> int:
	pass;

#desc Reverses the order of the elements in the array.
func reverse() -> void:
	pass;

#desc Searches the array in reverse order. Optionally, a start search index can be passed. If negative, the start index is considered relative to the end of the array.
func rfind(what: Variant, from: int) -> int:
	pass;

func set_read_only() -> void:
	pass;

func set_typed(type: int, class_name: StringName, script: Variant) -> void:
	pass;

#desc Shuffles the array such that the items will have a random order. This method uses the global random number generator common to methods such as [method @GlobalScope.randi]. Call [method @GlobalScope.randomize] to ensure that a new seed will be used each time if you want non-reproducible shuffling.
func shuffle() -> void:
	pass;

#desc Returns the number of elements in the array.
func size() -> int:
	pass;

#desc Returns the slice of the [Array], from [param begin] (inclusive) to [param end] (exclusive), as a new [Array].
#desc The absolute value of [param begin] and [param end] will be clamped to the array size, so the default value for [param end] makes it slice to the size of the array by default (i.e. [code]arr.slice(1)[/code] is a shorthand for [code]arr.slice(1, arr.size())[/code]).
#desc If either [param begin] or [param end] are negative, they will be relative to the end of the array (i.e. [code]arr.slice(0, -2)[/code] is a shorthand for [code]arr.slice(0, arr.size() - 2)[/code]).
#desc If specified, [param step] is the relative index between source elements. It can be negative, then [param begin] must be higher than [param end]. For example, [code][0, 1, 2, 3, 4, 5].slice(5, 1, -2)[/code] returns [code][5, 3][/code]).
#desc If [param deep] is true, each element will be copied by value rather than by reference.
func slice(begin: int, end: int, step: int, deep: bool) -> Array:
	pass;

#desc Sorts the array.
#desc [b]Note:[/b] Strings are sorted in alphabetical order (as opposed to natural order). This may lead to unexpected behavior when sorting an array of strings ending with a sequence of numbers. Consider the following example:
#desc [codeblocks]
#desc [gdscript]
#desc var strings = ["string1", "string2", "string10", "string11"]
#desc strings.sort()
#desc print(strings) # Prints [string1, string10, string11, string2]
#desc [/gdscript]
#desc [csharp]
#desc // There is no sort support for Godot.Collections.Array
#desc [/csharp]
#desc [/codeblocks]
#desc To perform natural order sorting, you can use [method sort_custom] with [method String.naturalnocasecmp_to] as follows:
#desc [codeblock]
#desc var strings = ["string1", "string2", "string10", "string11"]
#desc strings.sort_custom(func(a, b): return a.naturalnocasecmp_to(b) < 0)
#desc print(strings) # Prints [string1, string2, string10, string11]
#desc [/codeblock]
func sort() -> void:
	pass;

#desc Sorts the array using a custom method. The custom method receives two arguments (a pair of elements from the array) and must return either [code]true[/code] or [code]false[/code]. For two elements [code]a[/code] and [code]b[/code], if the given method returns [code]true[/code], element [code]b[/code] will be after element [code]a[/code] in the array.
#desc [b]Note:[/b] You cannot randomize the return value as the heapsort algorithm expects a deterministic result. Doing so will result in unexpected behavior.
#desc [codeblocks]
#desc [gdscript]
#desc func sort_ascending(a, b):
#desc if a[0] < b[0]:
#desc return true
#desc return false
#desc 
#desc func _ready():
#desc var my_items = [[5, "Potato"], [9, "Rice"], [4, "Tomato"]]
#desc my_items.sort_custom(sort_ascending)
#desc print(my_items) # Prints [[4, Tomato], [5, Potato], [9, Rice]].
#desc 
#desc # Descending, lambda version.
#desc my_items.sort_custom(func(a, b): return a[0] > b[0])
#desc print(my_items) # Prints [[9, Rice], [5, Potato], [4, Tomato]].
#desc [/gdscript]
#desc [csharp]
#desc // There is no custom sort support for Godot.Collections.Array
#desc [/csharp]
#desc [/codeblocks]
func sort_custom() -> void:
	pass;

func typed_assign() -> bool:
	pass;


