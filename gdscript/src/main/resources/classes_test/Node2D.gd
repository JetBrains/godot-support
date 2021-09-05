extends CanvasItem
class_name Node2D


var global_position: Vector2 setget set_global_position, get_global_position;
var global_rotation: float setget set_global_rotation, get_global_rotation;
var global_scale: Vector2 setget set_global_scale, get_global_scale;
var global_transform: Transform2D setget set_global_transform, get_global_transform;
var position: Vector2 setget set_position, get_position;
var rotation: float setget set_rotation, get_rotation;
var scale: Vector2 setget set_scale, get_scale;
var skew: float setget set_skew, get_skew;
var transform: Transform2D setget set_transform, get_transform;
var y_sort_enabled: bool setget set_y_sort_enabled, is_y_sort_enabled;
var z_as_relative: bool setget set_z_as_relative, is_z_relative;
var z_index: int setget set_z_index, get_z_index;

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

