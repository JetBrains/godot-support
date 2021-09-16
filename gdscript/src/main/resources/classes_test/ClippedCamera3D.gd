extends Camera3D
class_name ClippedCamera3D
const CLIP_PROCESS_PHYSICS = 0;
const CLIP_PROCESS_IDLE = 1;

var clip_to_areas: bool;
var clip_to_bodies: bool;
var collision_mask: int;
var margin: float;
var process_callback: int;

func add_exception(node: Object) -> void:
    pass;
func add_exception_rid(rid: RID) -> void:
    pass;
func clear_exceptions() -> void:
    pass;
func get_clip_offset() -> float:
    pass;
func get_collision_mask_bit(bit: int) -> bool:
    pass;
func remove_exception(node: Object) -> void:
    pass;
func remove_exception_rid(rid: RID) -> void:
    pass;
func set_collision_mask_bit(bit: int, value: bool) -> void:
    pass;
