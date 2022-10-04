#brief Base class for reference-counted objects.
#desc Base class for any object that keeps a reference count. [Resource] and many other helper objects inherit this class.
#desc Unlike other [Object] types, [RefCounted]s keep an internal reference counter so that they are automatically released when no longer in use, and only then. [RefCounted]s therefore do not need to be freed manually with [method Object.free].
#desc In the vast majority of use cases, instantiating and using [RefCounted]-derived types is all you need to do. The methods provided in this class are only for advanced users, and can cause issues if misused.
#desc [b]Note:[/b] In C#, reference-counted objects will not be freed instantly after they are no longer in use. Instead, garbage collection will run periodically and will free reference-counted objects that are no longer in use. This means that unused ones will linger on for a while before being removed.
class_name RefCounted




#desc Returns the current reference count.
func get_reference_count() -> int:
	pass;

#desc Initializes the internal reference counter. Use this only if you really know what you are doing.
#desc Returns whether the initialization was successful.
func init_ref() -> bool:
	pass;

#desc Increments the internal reference counter. Use this only if you really know what you are doing.
#desc Returns [code]true[/code] if the increment was successful, [code]false[/code] otherwise.
func reference() -> bool:
	pass;

#desc Decrements the internal reference counter. Use this only if you really know what you are doing.
#desc Returns [code]true[/code] if the decrement was successful, [code]false[/code] otherwise.
func unreference() -> bool:
	pass;


