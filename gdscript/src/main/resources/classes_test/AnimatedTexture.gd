extends Texture2D
class_name AnimatedTexture
const MAX_FRAMES = 256;

var current_frame: int;
var fps: float;
var frames: int;
var oneshot: bool;
var pause: bool;

func get_frame_delay(frame: int) -> float:
    pass;
func get_frame_texture(frame: int) -> Texture2D:
    pass;
func set_frame_delay(frame: int, delay: float) -> void:
    pass;
func set_frame_texture(frame: int, texture: Texture2D) -> void:
    pass;
