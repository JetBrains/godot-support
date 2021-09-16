extends Node
class_name AudioStreamPlayer
const MIX_TARGET_STEREO = 0;
const MIX_TARGET_SURROUND = 1;
const MIX_TARGET_CENTER = 2;

var autoplay: bool;
var bus: StringName;
var mix_target: int;
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
