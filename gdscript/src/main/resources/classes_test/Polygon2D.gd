extends Node2D
class_name Polygon2D

var antialiased: bool;
var bones: Array;
var color: Color;
var internal_vertex_count: int;
var invert_border: float;
var invert_enable: bool;
var offset: Vector2;
var polygon: PackedVector2Array;
var polygons: Array;
var skeleton: NodePath;
var texture: Texture2D;
var texture_offset: Vector2;
var texture_rotation: float;
var texture_scale: Vector2;
var uv: PackedVector2Array;
var vertex_colors: PackedColorArray;

func add_bone(path: NodePath, weights: PackedFloat32Array) -> void:
    pass;
func clear_bones() -> void:
    pass;
func erase_bone(index: int) -> void:
    pass;
func get_bone_count() -> int:
    pass;
func get_bone_path(index: int) -> NodePath:
    pass;
func get_bone_weights(index: int) -> PackedFloat32Array:
    pass;
func set_bone_path(index: int, path: NodePath) -> void:
    pass;
func set_bone_weights(index: int, weights: PackedFloat32Array) -> void:
    pass;
