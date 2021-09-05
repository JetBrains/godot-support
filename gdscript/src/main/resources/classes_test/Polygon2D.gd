extends Node2D
class_name Polygon2D


var antialiased: bool setget set_antialiased, get_antialiased;
var bones: Array setget _set_bones, _get_bones;
var color: Color setget set_color, get_color;
var internal_vertex_count: int setget set_internal_vertex_count, get_internal_vertex_count;
var invert_border: float setget set_invert_border, get_invert_border;
var invert_enable: bool setget set_invert, get_invert;
var offset: Vector2 setget set_offset, get_offset;
var polygon: PackedVector2Array setget set_polygon, get_polygon;
var polygons: Array setget set_polygons, get_polygons;
var skeleton: NodePath setget set_skeleton, get_skeleton;
var texture: Texture2D setget set_texture, get_texture;
var texture_offset: Vector2 setget set_texture_offset, get_texture_offset;
var texture_rotation: float setget set_texture_rotation, get_texture_rotation;
var texture_scale: Vector2 setget set_texture_scale, get_texture_scale;
var uv: PackedVector2Array setget set_uv, get_uv;
var vertex_colors: PackedColorArray setget set_vertex_colors, get_vertex_colors;

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

