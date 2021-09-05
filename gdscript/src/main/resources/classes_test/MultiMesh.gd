extends Resource
class_name MultiMesh

const TRANSFORM_2D = 0;
const TRANSFORM_3D = 1;

var buffer: PackedFloat32Array setget set_buffer, get_buffer;
var color_array: PackedColorArray setget _set_color_array, _get_color_array;
var custom_data_array: PackedColorArray setget _set_custom_data_array, _get_custom_data_array;
var instance_count: int setget set_instance_count, get_instance_count;
var mesh: Mesh setget set_mesh, get_mesh;
var transform_2d_array: PackedVector2Array setget _set_transform_2d_array, _get_transform_2d_array;
var transform_array: PackedVector3Array setget _set_transform_array, _get_transform_array;
var transform_format: int setget set_transform_format, get_transform_format;
var use_colors: bool setget set_use_colors, is_using_colors;
var use_custom_data: bool setget set_use_custom_data, is_using_custom_data;
var visible_instance_count: int setget set_visible_instance_count, get_visible_instance_count;

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

