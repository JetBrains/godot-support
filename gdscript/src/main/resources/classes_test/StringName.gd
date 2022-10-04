#brief An optimized string type for unique names.
#desc [StringName]s are immutable strings designed for general-purpose representation of unique names (also called "string interning"). [StringName] ensures that only one instance of a given name exists (so two [StringName]s with the same value are the same object). Comparing them is much faster than with regular [String]s, because only the pointers are compared, not the whole strings.
#desc You will usually just pass a [String] to methods expecting a [StringName] and it will be automatically converted, but you may occasionally want to construct a [StringName] ahead of time with [StringName] or the literal syntax [code]&"example"[/code].
#desc See also [NodePath], which is a similar concept specifically designed to store pre-parsed node paths.
class_name StringName



#desc Constructs an empty [StringName].
func StringName() -> StringName:
	pass;

#desc Constructs a [StringName] as a copy of the given [StringName].
func StringName(from: StringName) -> StringName:
	pass;

#desc Creates a new [StringName] from the given [String]. [code]StringName("example")[/code] is equivalent to [code]&"example"[/code].
func StringName(from: String) -> StringName:
	pass;


#desc Returns the 32-bit hash value representing the [StringName]'s contents.
func hash() -> int:
	pass;


