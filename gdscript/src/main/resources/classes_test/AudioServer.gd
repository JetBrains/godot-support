extends Object
class_name AudioServer

const SPEAKER_MODE_STEREO = 0;
const SPEAKER_SURROUND_31 = 1;
const SPEAKER_SURROUND_51 = 2;
const SPEAKER_SURROUND_71 = 3;

var bus_count: int setget set_bus_count, get_bus_count;
var device: String setget set_device, get_device;
var global_rate_scale: float setget set_global_rate_scale, get_global_rate_scale;

func add_bus(at_position: int) -> void:
    pass;

func add_bus_effect(bus_idx: int, effect: AudioEffect, at_position: int) -> void:
    pass;

func capture_get_device() -> String:
    pass;

func capture_get_device_list() -> Array:
    pass;

func capture_set_device(name: String) -> void:
    pass;

func generate_bus_layout() -> AudioBusLayout:
    pass;

func get_bus_channels(bus_idx: int) -> int:
    pass;

func get_bus_effect(bus_idx: int, effect_idx: int) -> AudioEffect:
    pass;

func get_bus_effect_count(bus_idx: int) -> int:
    pass;

func get_bus_effect_instance(bus_idx: int, effect_idx: int, channel: int) -> AudioEffectInstance:
    pass;

func get_bus_index(bus_name: StringName) -> int:
    pass;

func get_bus_name(bus_idx: int) -> String:
    pass;

func get_bus_peak_volume_left_db(bus_idx: int, channel: int) -> float:
    pass;

func get_bus_peak_volume_right_db(bus_idx: int, channel: int) -> float:
    pass;

func get_bus_send(bus_idx: int) -> StringName:
    pass;

func get_bus_volume_db(bus_idx: int) -> float:
    pass;

func get_device_list() -> Array:
    pass;

func get_mix_rate() -> float:
    pass;

func get_output_latency() -> float:
    pass;

func get_speaker_mode() -> int:
    pass;

func get_time_since_last_mix() -> float:
    pass;

func get_time_to_next_mix() -> float:
    pass;

func is_bus_bypassing_effects(bus_idx: int) -> bool:
    pass;

func is_bus_effect_enabled(bus_idx: int, effect_idx: int) -> bool:
    pass;

func is_bus_mute(bus_idx: int) -> bool:
    pass;

func is_bus_solo(bus_idx: int) -> bool:
    pass;

func lock() -> void:
    pass;

func move_bus(index: int, to_index: int) -> void:
    pass;

func remove_bus(index: int) -> void:
    pass;

func remove_bus_effect(bus_idx: int, effect_idx: int) -> void:
    pass;

func set_bus_bypass_effects(bus_idx: int, enable: bool) -> void:
    pass;

func set_bus_effect_enabled(bus_idx: int, effect_idx: int, enabled: bool) -> void:
    pass;

func set_bus_layout(bus_layout: AudioBusLayout) -> void:
    pass;

func set_bus_mute(bus_idx: int, enable: bool) -> void:
    pass;

func set_bus_name(bus_idx: int, name: String) -> void:
    pass;

func set_bus_send(bus_idx: int, send: StringName) -> void:
    pass;

func set_bus_solo(bus_idx: int, enable: bool) -> void:
    pass;

func set_bus_volume_db(bus_idx: int, volume_db: float) -> void:
    pass;

func swap_bus_effects(bus_idx: int, effect_idx: int, by_effect_idx: int) -> void:
    pass;

func unlock() -> void:
    pass;

