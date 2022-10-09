extends Resource
#brief Sprite frame library for AnimatedSprite2D and AnimatedSprite3D.
#desc Sprite frame library for an [AnimatedSprite2D] or [AnimatedSprite3D] node. Contains frames and animation data for playback.
#desc [b]Note:[/b] You can associate a set of normal or specular maps by creating additional [SpriteFrames] resources with a [code]_normal[/code] or [code]_specular[/code] suffix. For example, having 3 [SpriteFrames] resources [code]run[/code], [code]run_normal[/code], and [code]run_specular[/code] will make it so the [code]run[/code] animation uses normal and specular maps.
class_name SpriteFrames




#desc Adds a new animation to the library.
func add_animation(anim: StringName) -> void:
	pass;

#desc Adds a frame to the given animation.
func add_frame(anim: StringName, frame: Texture2D, at_position: int) -> void:
	pass;

#desc Removes all frames from the given animation.
func clear(anim: StringName) -> void:
	pass;

#desc Removes all animations. A "default" animation will be created.
func clear_all() -> void:
	pass;

#desc Returns [code]true[/code] if the given animation is configured to loop when it finishes playing. Otherwise, returns [code]false[/code].
func get_animation_loop(anim: StringName) -> bool:
	pass;

#desc Returns an array containing the names associated to each animation. Values are placed in alphabetical order.
func get_animation_names() -> PackedStringArray:
	pass;

#desc The animation's speed in frames per second.
func get_animation_speed(anim: StringName) -> float:
	pass;

#desc Returns the animation's selected frame.
func get_frame(anim: StringName, idx: int) -> Texture2D:
	pass;

#desc Returns the number of frames in the animation.
func get_frame_count(anim: StringName) -> int:
	pass;

#desc If [code]true[/code], the named animation exists.
func has_animation(anim: StringName) -> bool:
	pass;

#desc Removes the given animation.
func remove_animation(anim: StringName) -> void:
	pass;

#desc Removes the animation's selected frame.
func remove_frame(anim: StringName, idx: int) -> void:
	pass;

#desc Changes the animation's name to [param newname].
func rename_animation(anim: StringName, newname: StringName) -> void:
	pass;

#desc If [code]true[/code], the animation will loop.
func set_animation_loop(anim: StringName, loop: bool) -> void:
	pass;

#desc The animation's speed in frames per second.
func set_animation_speed(anim: StringName, speed: float) -> void:
	pass;

#desc Sets the texture of the given frame.
func set_frame(anim: StringName, idx: int, txt: Texture2D) -> void:
	pass;


