extends CanvasLayer
class_name ParallaxBackground


var layer: int setget set_layer, get_layer;
var scroll_base_offset: Vector2 setget set_scroll_base_offset, get_scroll_base_offset;
var scroll_base_scale: Vector2 setget set_scroll_base_scale, get_scroll_base_scale;
var scroll_ignore_camera_zoom: bool setget set_ignore_camera_zoom, is_ignore_camera_zoom;
var scroll_limit_begin: Vector2 setget set_limit_begin, get_limit_begin;
var scroll_limit_end: Vector2 setget set_limit_end, get_limit_end;
var scroll_offset: Vector2 setget set_scroll_offset, get_scroll_offset;

