extends Node
class_name CanvasItem
const NOTIFICATION_TRANSFORM_CHANGED = 2000;
const NOTIFICATION_DRAW = 30;
const NOTIFICATION_VISIBILITY_CHANGED = 31;
const NOTIFICATION_ENTER_CANVAS = 32;
const NOTIFICATION_EXIT_CANVAS = 33;
const TEXTURE_FILTER_PARENT_NODE = 0;
const TEXTURE_FILTER_NEAREST = 1;
const TEXTURE_FILTER_LINEAR = 2;
const TEXTURE_FILTER_NEAREST_WITH_MIPMAPS = 3;
const TEXTURE_FILTER_LINEAR_WITH_MIPMAPS = 4;
const TEXTURE_FILTER_NEAREST_WITH_MIPMAPS_ANISOTROPIC = 5;
const TEXTURE_FILTER_LINEAR_WITH_MIPMAPS_ANISOTROPIC = 6;
const TEXTURE_FILTER_MAX = 7;
const TEXTURE_REPEAT_PARENT_NODE = 0;
const TEXTURE_REPEAT_DISABLED = 1;
const TEXTURE_REPEAT_ENABLED = 2;
const TEXTURE_REPEAT_MIRROR = 3;
const TEXTURE_REPEAT_MAX = 4;

var clip_children: bool;
var light_mask: int;
var material: Material;
var modulate: Color;
var self_modulate: Color;
var show_behind_parent: bool;
var show_on_top: bool;
var texture_filter: int;
var texture_repeat: int;
var top_level: bool;
var use_parent_material: bool;
var visible: bool;

func _draw() -> void:
    pass;
func draw_animation_slice(animation_length: float, slice_begin: float, slice_end: float, offset: float) -> void:
    pass;
func draw_arc(center: Vector2, radius: float, start_angle: float, end_angle: float, point_count: int, color: Color, width: float, antialiased: bool) -> void:
    pass;
func draw_char(font: Font, pos: Vector2, char: String, next: String, size: int, modulate: Color, outline_size: int, outline_modulate: Color) -> float:
    pass;
func draw_circle(position: Vector2, radius: float, color: Color) -> void:
    pass;
func draw_colored_polygon(points: PackedVector2Array, color: Color, uvs: PackedVector2Array, texture: Texture2D) -> void:
    pass;
func draw_end_animation() -> void:
    pass;
func draw_line(from: Vector2, to: Vector2, color: Color, width: float) -> void:
    pass;
func draw_mesh(mesh: Mesh, texture: Texture2D, transform: Transform2D, modulate: Color) -> void:
    pass;
func draw_multiline(points: PackedVector2Array, color: Color, width: float) -> void:
    pass;
func draw_multiline_colors(points: PackedVector2Array, colors: PackedColorArray, width: float) -> void:
    pass;
func draw_multiline_string(font: Font, pos: Vector2, text: String, align: int, width: float, max_lines: int, size: int, modulate: Color, outline_size: int, outline_modulate: Color, flags: int) -> void:
    pass;
func draw_multimesh(multimesh: MultiMesh, texture: Texture2D) -> void:
    pass;
func draw_polygon(points: PackedVector2Array, colors: PackedColorArray, uvs: PackedVector2Array, texture: Texture2D) -> void:
    pass;
func draw_polyline(points: PackedVector2Array, color: Color, width: float, antialiased: bool) -> void:
    pass;
func draw_polyline_colors(points: PackedVector2Array, colors: PackedColorArray, width: float, antialiased: bool) -> void:
    pass;
func draw_primitive(points: PackedVector2Array, colors: PackedColorArray, uvs: PackedVector2Array, texture: Texture2D, width: float) -> void:
    pass;
func draw_rect(rect: Rect2, color: Color, filled: bool, width: float) -> void:
    pass;
func draw_set_transform(position: Vector2, rotation: float, scale: Vector2) -> void:
    pass;
func draw_set_transform_matrix(xform: Transform2D) -> void:
    pass;
func draw_string(font: Font, pos: Vector2, text: String, align: int, width: float, size: int, modulate: Color, outline_size: int, outline_modulate: Color, flags: int) -> void:
    pass;
func draw_style_box(style_box: StyleBox, rect: Rect2) -> void:
    pass;
func draw_texture(texture: Texture2D, position: Vector2, modulate: Color) -> void:
    pass;
func draw_texture_rect(texture: Texture2D, rect: Rect2, tile: bool, modulate: Color, transpose: bool) -> void:
    pass;
func draw_texture_rect_region(texture: Texture2D, rect: Rect2, src_rect: Rect2, modulate: Color, transpose: bool, clip_uv: bool) -> void:
    pass;
func force_update_transform() -> void:
    pass;
func get_canvas() -> RID:
    pass;
func get_canvas_item() -> RID:
    pass;
func get_canvas_transform() -> Transform2D:
    pass;
func get_global_mouse_position() -> Vector2:
    pass;
func get_global_transform() -> Transform2D:
    pass;
func get_global_transform_with_canvas() -> Transform2D:
    pass;
func get_local_mouse_position() -> Vector2:
    pass;
func get_transform() -> Transform2D:
    pass;
func get_viewport_rect() -> Rect2:
    pass;
func get_viewport_transform() -> Transform2D:
    pass;
func get_world_2d() -> World2D:
    pass;
func hide() -> void:
    pass;
func is_local_transform_notification_enabled() -> bool:
    pass;
func is_transform_notification_enabled() -> bool:
    pass;
func is_visible_in_tree() -> bool:
    pass;
func make_canvas_position_local(screen_point: Vector2) -> Vector2:
    pass;
func make_input_local(event: InputEvent) -> InputEvent:
    pass;
func set_notify_local_transform(enable: bool) -> void:
    pass;
func set_notify_transform(enable: bool) -> void:
    pass;
func show() -> void:
    pass;
func update() -> void:
    pass;
