extends SpriteBase3D
class_name AnimatedSprite3D


var animation: String setget set_animation, get_animation;
var frame: int setget set_frame, get_frame;
var frames: SpriteFrames setget set_sprite_frames, get_sprite_frames;
var playing: bool setget _set_playing, _is_playing;

func is_playing() -> bool:
    pass;

func play(anim: StringName) -> void:
    pass;

func stop() -> void:
    pass;

