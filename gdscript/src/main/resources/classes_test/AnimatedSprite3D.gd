extends SpriteBase3D
class_name AnimatedSprite3D

var animation: StringName;
var frame: int;
var frames: SpriteFrames;
var playing: bool;

func is_playing() -> bool:
    pass;
func play(anim: StringName) -> void:
    pass;
func stop() -> void:
    pass;
