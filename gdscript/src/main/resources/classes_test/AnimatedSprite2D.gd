extends Node2D
class_name AnimatedSprite2D

var animation: StringName;
var centered: bool;
var flip_h: bool;
var flip_v: bool;
var frame: int;
var frames: SpriteFrames;
var offset: Vector2;
var playing: bool;
var speed_scale: float;

func is_playing() -> bool:
    pass;
func play(anim: StringName, backwards: bool) -> void:
    pass;
func stop() -> void:
    pass;
