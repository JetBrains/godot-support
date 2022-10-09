extends RefCounted
#brief Holds an [Object], but does not contribute to the reference count if the object is a reference.
#desc A weakref can hold a [RefCounted], without contributing to the reference counter. A weakref can be created from an [Object] using [method @GlobalScope.weakref]. If this object is not a reference, weakref still works, however, it does not have any effect on the object. Weakrefs are useful in cases where multiple classes have variables that refer to each other. Without weakrefs, using these classes could lead to memory leaks, since both references keep each other from being released. Making part of the variables a weakref can prevent this cyclic dependency, and allows the references to be released.
class_name WeakRef




#desc Returns the [Object] this weakref is referring to. Returns [code]null[/code] if that object no longer exists.
func get_ref() -> Variant:
	pass;


