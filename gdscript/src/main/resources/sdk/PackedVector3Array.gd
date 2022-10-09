#brief A packed array of [Vector3]s.
#desc An array specifically designed to hold [Vector3]. Packs data tightly, so it saves memory for large array sizes.
class_name PackedVector3Array



#desc Constructs an empty [PackedVector3Array].
func PackedVector3Array() -> PackedVector3Array:
	pass;

#desc Constructs a [PackedVector3Array] as a copy of the given [PackedVector3Array].
func PackedVector3Array(from: PackedVector3Array) -> PackedVector3Array:
	pass;

#desc Constructs a new [PackedVector3Array]. Optionally, you can pass in a generic [Array] that will be converted.
func PackedVector3Array(from: Array) -> PackedVector3Array:
	pass;


#desc Appends an element at the end of the array (alias of [method push_back]).
func append(value: Vector3) -> bool:
	pass;

#desc Appends a [PackedVector3Array] at the end of this array.
func append_array(array: PackedVector3Array) -> void:
	pass;

#desc Finds the index of an existing value (or the insertion index that maintains sorting order, if the value is not yet present in the array) using binary search. Optionally, a [param before] specifier can be passed. If [code]false[/code], the returned index comes after all existing entries of the value in the array.
#desc [b]Note:[/b] Calling [method bsearch] on an unsorted array results in unexpected behavior.
func bsearch(value: Vector3, before: bool) -> int:
	pass;

#desc Clears the array. This is equivalent to using [method resize] with a size of [code]0[/code].
func clear() -> void:
	pass;

#desc Returns the number of times an element is in the array.
func count(value: Vector3) -> int:
	pass;

#desc Creates a copy of the array, and returns it.
func duplicate() -> PackedVector3Array:
	pass;

#desc Assigns the given value to all elements in the array. This can typically be used together with [method resize] to create an array with a given size and initialized elements.
func fill(value: Vector3) -> void:
	pass;

#desc Searches the array for a value and returns its index or [code]-1[/code] if not found. Optionally, the initial search index can be passed.
func find(value: Vector3, from: int) -> int:
	pass;

#desc Returns [code]true[/code] if the array contains [param value].
func has(value: Vector3) -> bool:
	pass;

#desc Inserts a new element at a given position in the array. The position must be valid, or at the end of the array ([code]idx == size()[/code]).
func insert(at_index: int, value: Vector3) -> int:
	pass;

#desc Returns [code]true[/code] if the array is empty.
func is_empty() -> bool:
	pass;

#desc Inserts a [Vector3] at the end.
func push_back(value: Vector3) -> bool:
	pass;

#desc Removes an element from the array by index.
func remove_at(index: int) -> void:
	pass;

#desc Sets the size of the array. If the array is grown, reserves elements at the end of the array. If the array is shrunk, truncates the array to the new size.
func resize(new_size: int) -> int:
	pass;

#desc Reverses the order of the elements in the array.
func reverse() -> void:
	pass;

#desc Searches the array in reverse order. Optionally, a start search index can be passed. If negative, the start index is considered relative to the end of the array.
func rfind(value: Vector3, from: int) -> int:
	pass;

#desc Changes the [Vector3] at the given index.
func set(index: int, value: Vector3) -> void:
	pass;

#desc Returns the number of elements in the array.
func size() -> int:
	pass;

#desc Returns the slice of the [PackedVector3Array], from [param begin] (inclusive) to [param end] (exclusive), as a new [PackedVector3Array].
#desc The absolute value of [param begin] and [param end] will be clamped to the array size, so the default value for [param end] makes it slice to the size of the array by default (i.e. [code]arr.slice(1)[/code] is a shorthand for [code]arr.slice(1, arr.size())[/code]).
#desc If either [param begin] or [param end] are negative, they will be relative to the end of the array (i.e. [code]arr.slice(0, -2)[/code] is a shorthand for [code]arr.slice(0, arr.size() - 2)[/code]).
func slice(begin: int, end: int) -> PackedVector3Array:
	pass;

#desc Sorts the elements of the array in ascending order.
func sort() -> void:
	pass;

#desc Returns a [PackedByteArray] with each vector encoded as bytes.
func to_byte_array() -> PackedByteArray:
	pass;


