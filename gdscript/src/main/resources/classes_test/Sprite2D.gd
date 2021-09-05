extends Node2D
class_name Sprite2D


var centered: bool setget set_centered, is_centered;
var flip_h: bool setget set_flip_h, is_flipped_h;
var flip_v: bool setget set_flip_v, is_flipped_v;
var frame: int setget set_frame, get_frame;
var frame_coords: Vector2i setget set_frame_coords, get_frame_coords;
var hframes: int setget set_hframes, get_hframes;
var offset: Vector2 setget set_offset, get_offset;
var region_enabled: bool setget set_region_enabled, is_region_enabled;
var region_filter_clip_enabled: bool setget set_region_filter_clip_enabled, is_region_filter_clip_enabled;
var region_rect: Rect2 setget set_region_rect, get_region_rect;
var texture: Texture2D setget set_texture, get_texture;
var vframes: int setget set_vframes, get_vframes;

func get_rect() -> Rect2:
    pass;

func is_pixel_opaque(pos: Vector2) -> bool:
    pass;

