extends Resource
class_name MultiMesh
const TRANSFORM_2D = 0;
const TRANSFORM_3D = 1;

var buffer: PackedFloat32Array;
var color_array: PackedColorArray;
var custom_data_array: PackedColorArray;
var instance_count: int;
var mesh: Mesh;
var transform_2d_array: PackedVector2Array;
var transform_array: PackedVector3Array;
var transform_format: int;
var use_colors: bool;
var use_custom_data: bool;
var visible_instance_count: int;

func get_aabb() -> AABB:
    pass;
func get_instance_color(instance: int) -> Color:
    pass;
func get_instance_custom_data(instance: int) -> Color:
    pass;
func get_instance_transform(instance: int) -> Transform3D:
    pass;
func get_instance_transform_2d(instance: int) -> Transform2D:
    pass;
func set_instance_color(instance: int, color: Color) -> void:
    pass;
func set_instance_custom_data(instance: int, custom_data: Color) -> void:
    pass;
func set_instance_transform(instance: int, transform: Transform3D) -> void:
    pass;
func set_instance_transform_2d(instance: int, transform: Transform2D) -> void:
    pass;
