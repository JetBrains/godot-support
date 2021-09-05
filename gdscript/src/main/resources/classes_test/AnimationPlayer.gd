extends Node
class_name AnimationPlayer

const ANIMATION_PROCESS_PHYSICS = 0;
const ANIMATION_PROCESS_IDLE = 1;
const ANIMATION_PROCESS_MANUAL = 2;
const ANIMATION_METHOD_CALL_DEFERRED = 0;
const ANIMATION_METHOD_CALL_IMMEDIATE = 1;

var assigned_animation: String setget set_assigned_animation, get_assigned_animation;
var autoplay: String setget set_autoplay, get_autoplay;
var current_animation: String setget set_current_animation, get_current_animation;
var current_animation_length: float setget , get_current_animation_length;
var current_animation_position: float setget , get_current_animation_position;
var method_call_mode: int setget set_method_call_mode, get_method_call_mode;
var playback_active: bool setget set_active, is_active;
var playback_default_blend_time: float setget set_default_blend_time, get_default_blend_time;
var playback_process_mode: int setget set_process_callback, get_process_callback;
var playback_speed: float setget set_speed_scale, get_speed_scale;
var reset_on_save: bool setget set_reset_on_save_enabled, is_reset_on_save_enabled;
var root_node: NodePath setget set_root, get_root;

func add_animation(name: StringName, animation: Animation) -> int:
    pass;

func advance(delta: float) -> void:
    pass;

func animation_get_next(anim_from: StringName) -> StringName:
    pass;

func animation_set_next(anim_from: StringName, anim_to: StringName) -> void:
    pass;

func clear_caches() -> void:
    pass;

func clear_queue() -> void:
    pass;

func find_animation(animation: Animation) -> StringName:
    pass;

func get_animation(name: StringName) -> Animation:
    pass;

func get_animation_list() -> PackedStringArray:
    pass;

func get_blend_time(anim_from: StringName, anim_to: StringName) -> float:
    pass;

func get_playing_speed() -> float:
    pass;

func get_queue() -> PackedStringArray:
    pass;

func has_animation(name: StringName) -> bool:
    pass;

func is_playing() -> bool:
    pass;

func play(name: StringName, custom_blend: float, custom_speed: float, from_end: bool) -> void:
    pass;

func play_backwards(name: StringName, custom_blend: float) -> void:
    pass;

func queue(name: StringName) -> void:
    pass;

func remove_animation(name: StringName) -> void:
    pass;

func rename_animation(name: StringName, newname: StringName) -> void:
    pass;

func seek(seconds: float, update: bool) -> void:
    pass;

func set_blend_time(anim_from: StringName, anim_to: StringName, sec: float) -> void:
    pass;

func stop(reset: bool) -> void:
    pass;

