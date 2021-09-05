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

var area_mask: int setget set_area_mask, get_area_mask;
var attenuation_filter_cutoff_hz: float setget set_attenuation_filter_cutoff_hz, get_attenuation_filter_cutoff_hz;
var attenuation_filter_db: float setget set_attenuation_filter_db, get_attenuation_filter_db;
var attenuation_model: int setget set_attenuation_model, get_attenuation_model;
var autoplay: bool setget set_autoplay, is_autoplay_enabled;
var bus: String setget set_bus, get_bus;
var doppler_tracking: int setget set_doppler_tracking, get_doppler_tracking;
var emission_angle_degrees: float setget set_emission_angle, get_emission_angle;
var emission_angle_enabled: bool setget set_emission_angle_enabled, is_emission_angle_enabled;
var emission_angle_filter_attenuation_db: float setget set_emission_angle_filter_attenuation_db, get_emission_angle_filter_attenuation_db;
var max_db: float setget set_max_db, get_max_db;
var max_distance: float setget set_max_distance, get_max_distance;
var out_of_range_mode: int setget set_out_of_range_mode, get_out_of_range_mode;
var pitch_scale: float setget set_pitch_scale, get_pitch_scale;
var playing: bool setget _set_playing, is_playing;
var stream: AudioStream setget set_stream, get_stream;
var stream_paused: bool setget set_stream_paused, get_stream_paused;
var unit_db: float setget set_unit_db, get_unit_db;
var unit_size: float setget set_unit_size, get_unit_size;

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

