extends Resource
class_name SpriteFrames

var frames: Array;

func add_animation(anim: StringName) -> void:
    pass;
func add_frame(anim: StringName, frame: Texture2D, at_position: int) -> void:
    pass;
func clear(anim: StringName) -> void:
    pass;
func clear_all() -> void:
    pass;
func get_animation_loop(anim: StringName) -> bool:
    pass;
func get_animation_names() -> PackedStringArray:
    pass;
func get_animation_speed(anim: StringName) -> float:
    pass;
func get_frame(anim: StringName, idx: int) -> Texture2D:
    pass;
func get_frame_count(anim: StringName) -> int:
    pass;
func has_animation(anim: StringName) -> bool:
    pass;
func remove_animation(anim: StringName) -> void:
    pass;
func remove_frame(anim: StringName, idx: int) -> void:
    pass;
func rename_animation(anim: StringName, newname: StringName) -> void:
    pass;
func set_animation_loop(anim: StringName, loop: bool) -> void:
    pass;
func set_animation_speed(anim: StringName, speed: float) -> void:
    pass;
func set_frame(anim: StringName, idx: int, txt: Texture2D) -> void:
    pass;
