extends Node3D
class_name AudioStreamPlayer3D
const ATTENUATION_INVERSE_DISTANCE = 0;
const ATTENUATION_INVERSE_SQUARE_DISTANCE = 1;
const ATTENUATION_LOGARITHMIC = 2;
const ATTENUATION_DISABLED = 3;
const OUT_OF_RANGE_MIX = 0;
const OUT_OF_RANGE_PAUSE = 1;
const DOPPLER_TRACKING_DISABLED = 0;
const DOPPLER_TRACKING_IDLE_STEP = 1;
const DOPPLER_TRACKING_PHYSICS_STEP = 2;

var area_mask: int;
var attenuation_filter_cutoff_hz: float;
var attenuation_filter_db: float;
var attenuation_model: int;
var autoplay: bool;
var bus: StringName;
var doppler_tracking: int;
var emission_angle_degrees: float;
var emission_angle_enabled: bool;
var emission_angle_filter_attenuation_db: float;
var max_db: float;
var max_distance: float;
var out_of_range_mode: int;
var pitch_scale: float;
var playing: bool;
var stream: AudioStream;
var stream_paused: bool;
var unit_db: float;
var unit_size: float;

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
