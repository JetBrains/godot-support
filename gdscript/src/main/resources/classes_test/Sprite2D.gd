extends Node2D
class_name Sprite2D

var centered: bool;
var flip_h: bool;
var flip_v: bool;
var frame: int;
var frame_coords: Vector2i;
var hframes: int;
var offset: Vector2;
var region_enabled: bool;
var region_filter_clip_enabled: bool;
var region_rect: Rect2;
var texture: Texture2D;
var vframes: int;

func get_rect() -> Rect2:
    pass;
func is_pixel_opaque(pos: Vector2) -> bool:
    pass;
