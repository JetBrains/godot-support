extends Node2D
class_name AudioStreamPlayer2D

var area_mask: int;
var attenuation: float;
var autoplay: bool;
var bus: StringName;
var max_distance: float;
var pitch_scale: float;
var playing: bool;
var stream: AudioStream;
var stream_paused: bool;
var volume_db: float;

func get_playback_position() -> float:
    pass;
func get_stream_playback() -> AudioStreamPlayback:
    pass;
func play(from_position: float) -> void:
    pass;
func seek(to_position: float) -> void:
    pass;
func stop() -> void:
    pass;
