#brief Container for [Animation] resources.
#desc An animation library stores a set of animations accessible through [StringName] keys, for use with [AnimationPlayer] nodes.
class_name AnimationLibrary


var _data: Dictionary;



#desc Adds the [param animation] to the library, accessible by the key [param name].
func add_animation(name: StringName, animation: Animation) -> int:
	pass;

#desc Returns the [Animation] with the key [param name]. If the animation does not exist, [code]null[/code] is returned and an error is logged.
func get_animation(name: StringName) -> Animation:
	pass;

#desc Returns the keys for the [Animation]s stored in the library.
func get_animation_list() -> StringName[]:
	pass;

#desc Returns [code]true[/code] if the library stores an [Animation] with [param name] as the key.
func has_animation(name: StringName) -> bool:
	pass;

#desc Removes the [Animation] with the key [param name].
func remove_animation(name: StringName) -> void:
	pass;

#desc Changes the key of the [Animation] associated with the key [param name] to [param newname].
func rename_animation(name: StringName, newname: StringName) -> void:
	pass;


