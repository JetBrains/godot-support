extends Object
#brief Class information repository.
#desc Provides access to metadata stored for every available class.
class_name ClassDB




#desc Returns [code]true[/code] if you can instance objects from the specified [param class], [code]false[/code] in other case.
func can_instantiate(class: StringName) -> bool:
	pass;

#desc Returns whether the specified [param class] is available or not.
func class_exists(class: StringName) -> bool:
	pass;

#desc Returns an array with all the keys in [param enum] of [param class] or its ancestry.
func class_get_enum_constants(class: StringName, enum: StringName, no_inheritance: bool) -> PackedStringArray:
	pass;

#desc Returns an array with all the enums of [param class] or its ancestry.
func class_get_enum_list(class: StringName, no_inheritance: bool) -> PackedStringArray:
	pass;

#desc Returns the value of the integer constant [param name] of [param class] or its ancestry. Always returns 0 when the constant could not be found.
func class_get_integer_constant(class: StringName, name: StringName) -> int:
	pass;

#desc Returns which enum the integer constant [param name] of [param class] or its ancestry belongs to.
func class_get_integer_constant_enum(class: StringName, name: StringName, no_inheritance: bool) -> StringName:
	pass;

#desc Returns an array with the names all the integer constants of [param class] or its ancestry.
func class_get_integer_constant_list(class: StringName, no_inheritance: bool) -> PackedStringArray:
	pass;

#desc Returns an array with all the methods of [param class] or its ancestry if [param no_inheritance] is [code]false[/code]. Every element of the array is a [Dictionary] with the following keys: [code]args[/code], [code]default_args[/code], [code]flags[/code], [code]id[/code], [code]name[/code], [code]return: (class_name, hint, hint_string, name, type, usage)[/code].
#desc [b]Note:[/b] In exported release builds the debug info is not available, so the returned dictionaries will contain only method names.
func class_get_method_list(class: StringName, no_inheritance: bool) -> Dictionary[]:
	pass;

#desc Returns the value of [param property] of [param object] or its ancestry.
func class_get_property(object: Object, property: StringName) -> Variant:
	pass;

#desc Returns an array with all the properties of [param class] or its ancestry if [param no_inheritance] is [code]false[/code].
func class_get_property_list(class: StringName, no_inheritance: bool) -> Dictionary[]:
	pass;

#desc Returns the [param signal] data of [param class] or its ancestry. The returned value is a [Dictionary] with the following keys: [code]args[/code], [code]default_args[/code], [code]flags[/code], [code]id[/code], [code]name[/code], [code]return: (class_name, hint, hint_string, name, type, usage)[/code].
func class_get_signal(class: StringName, signal: StringName) -> Dictionary:
	pass;

#desc Returns an array with all the signals of [param class] or its ancestry if [param no_inheritance] is [code]false[/code]. Every element of the array is a [Dictionary] as described in [method class_get_signal].
func class_get_signal_list(class: StringName, no_inheritance: bool) -> Dictionary[]:
	pass;

#desc Returns whether [param class] or its ancestry has an enum called [param name] or not.
func class_has_enum(class: StringName, name: StringName, no_inheritance: bool) -> bool:
	pass;

#desc Returns whether [param class] or its ancestry has an integer constant called [param name] or not.
func class_has_integer_constant(class: StringName, name: StringName) -> bool:
	pass;

#desc Returns whether [param class] (or its ancestry if [param no_inheritance] is [code]false[/code]) has a method called [param method] or not.
func class_has_method(class: StringName, method: StringName, no_inheritance: bool) -> bool:
	pass;

#desc Returns whether [param class] or its ancestry has a signal called [param signal] or not.
func class_has_signal(class: StringName, signal: StringName) -> bool:
	pass;

#desc Sets [param property] value of [param object] to [param value].
func class_set_property(object: Object, property: StringName, value: Variant) -> int:
	pass;

#desc Returns the names of all the classes available.
func get_class_list() -> PackedStringArray:
	pass;

#desc Returns the names of all the classes that directly or indirectly inherit from [param class].
func get_inheriters_from_class(class: StringName) -> PackedStringArray:
	pass;

#desc Returns the parent class of [param class].
func get_parent_class(class: StringName) -> StringName:
	pass;

#desc Creates an instance of [param class].
func instantiate(class: StringName) -> Variant:
	pass;

#desc Returns whether this [param class] is enabled or not.
func is_class_enabled(class: StringName) -> bool:
	pass;

#desc Returns whether [param inherits] is an ancestor of [param class] or not.
func is_parent_class(class: StringName, inherits: StringName) -> bool:
	pass;


