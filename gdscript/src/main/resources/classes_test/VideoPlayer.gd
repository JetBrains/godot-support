extends Control
class_name VideoPlayer


var audio_track: int setget set_audio_track, get_audio_track;
var autoplay: bool setget set_autoplay, has_autoplay;
var buffering_msec: int setget set_buffering_msec, get_buffering_msec;
var bus: String setget set_bus, get_bus;
var expand: bool setget set_expand, has_expand;
var paused: bool setget set_paused, is_paused;
var stream: VideoStream setget set_stream, get_stream;
var stream_position: float setget set_stream_position, get_stream_position;
var volume: float setget set_volume, get_volume;
var volume_db: float setget set_volume_db, get_volume_db;

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

