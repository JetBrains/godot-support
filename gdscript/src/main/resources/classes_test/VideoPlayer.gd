extends Control
class_name VideoPlayer

var audio_track: int;
var autoplay: bool;
var buffering_msec: int;
var bus: StringName;
var expand: bool;
var paused: bool;
var stream: VideoStream;
var stream_position: float;
var volume: float;
var volume_db: float;

func get_stream_name() -> String:
    pass;
func get_video_texture() -> Texture2D:
    pass;
func is_playing() -> bool:
    pass;
func play() -> void:
    pass;
func stop() -> void:
    pass;
