extends Texture2D
class_name AnimatedTexture

const MAX_FRAMES = 256;

var current_frame: int setget set_current_frame, get_current_frame;
var fps: float setget set_fps, get_fps;
var frames: int setget set_frames, get_frames;
var oneshot: bool setget set_oneshot, get_oneshot;
var pause: bool setget set_pause, get_pause;

func get_frame_delay(frame: int) -> float:
    pass;

func get_frame_texture(frame: int) -> Texture2D:
    pass;

func set_frame_delay(frame: int, delay: float) -> void:
    pass;

func set_frame_texture(frame: int, texture: Texture2D) -> void:
    pass;

