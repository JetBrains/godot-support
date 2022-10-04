#brief Handle for a [Resource]'s unique ID.
#desc The RID type is used to access the unique integer ID of a resource. They are opaque, which means they do not grant access to the associated resource by themselves. They are used by and with the low-level Server classes such as [RenderingServer].
class_name RID



#desc Constructs an empty [RID] with the invalid ID [code]0[/code].
func RID() -> RID:
	pass;

#desc Constructs a [RID] as a copy of the given [RID].
func RID() -> RID:
	pass;


#desc Returns the ID of the referenced resource.
func get_id() -> int:
	pass;

#desc Returns [code]true[/code] if [RID] is valid.
func is_valid() -> bool:
	pass;


