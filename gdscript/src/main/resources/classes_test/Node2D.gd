extends CanvasItem
class_name Node2D

var global_position: Vector2;
var global_rotation: float;
var global_scale: Vector2;
var global_transform: Transform2D;
var position: Vector2;
var rotation: float;
var scale: Vector2;
var skew: float;
var transform: Transform2D;
var y_sort_enabled: bool;
var z_as_relative: bool;
var z_index: int;

func apply_scale(ratio: Vector2) -> void:
    pass;
func get_angle_to(point: Vector2) -> float:
    pass;
func get_relative_transform_to_parent(parent: Node) -> Transform2D:
    pass;
func global_translate(offset: Vector2) -> void:
    pass;
func look_at(point: Vector2) -> void:
    pass;
func move_local_x(delta: float, scaled: bool) -> void:
    pass;
func move_local_y(delta: float, scaled: bool) -> void:
    pass;
func rotate(radians: float) -> void:
    pass;
func to_global(local_point: Vector2) -> Vector2:
    pass;
func to_local(global_point: Vector2) -> Vector2:
    pass;
func translate(offset: Vector2) -> void:
    pass;
