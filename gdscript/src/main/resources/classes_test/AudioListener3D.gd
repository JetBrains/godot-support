extends Node3D
#brief Overrides the location sounds are heard from.
#desc Once added to the scene tree and enabled using [method make_current], this node will override the location sounds are heard from. This can be used to listen from a location different from the [Camera3D].
class_name AudioListener3D




#desc Disables the listener to use the current camera's listener instead.
func clear_current() -> void:
	pass;

#desc Returns the listener's global orthonormalized [Transform3D].
func get_listener_transform() -> Transform3D:
	pass;

#desc Returns [code]true[/code] if the listener was made current using [method make_current], [code]false[/code] otherwise.
#desc [b]Note:[/b] There may be more than one AudioListener3D marked as "current" in the scene tree, but only the one that was made current last will be used.
func is_current() -> bool:
	pass;

#desc Enables the listener. This will override the current camera's listener.
func make_current() -> void:
	pass;


