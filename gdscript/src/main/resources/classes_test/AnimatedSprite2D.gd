extends Node2D
class_name AnimatedSprite2D


var animation: String setget set_animation, get_animation;
var centered: bool setget set_centered, is_centered;
var flip_h: bool setget set_flip_h, is_flipped_h;
var flip_v: bool setget set_flip_v, is_flipped_v;
var frame: int setget set_frame, get_frame;
var frames: SpriteFrames setget set_sprite_frames, get_sprite_frames;
var offset: Vector2 setget set_offset, get_offset;
var playing: bool setget _set_playing, _is_playing;
var speed_scale: float setget set_speed_scale, get_speed_scale;

func is_playing() -> bool:
    pass;

func play(anim: StringName, backwards: bool) -> void:
    pass;

func stop() -> void:
    pass;

