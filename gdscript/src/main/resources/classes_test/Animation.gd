extends Resource
class_name Animation
const TYPE_VALUE = 0;
const TYPE_TRANSFORM3D = 1;
const TYPE_METHOD = 2;
const TYPE_BEZIER = 3;
const TYPE_AUDIO = 4;
const TYPE_ANIMATION = 5;
const INTERPOLATION_NEAREST = 0;
const INTERPOLATION_LINEAR = 1;
const INTERPOLATION_CUBIC = 2;
const UPDATE_CONTINUOUS = 0;
const UPDATE_DISCRETE = 1;
const UPDATE_TRIGGER = 2;
const UPDATE_CAPTURE = 3;

var length: float;
var loop: bool;
var step: float;

func add_track(type: int, at_position: int) -> int:
    pass;
func animation_track_get_key_animation(track_idx: int, key_idx: int) -> StringName:
    pass;
func animation_track_insert_key(track_idx: int, time: float, animation: StringName) -> int:
    pass;
func animation_track_set_key_animation(track_idx: int, key_idx: int, animation: StringName) -> void:
    pass;
func audio_track_get_key_end_offset(track_idx: int, key_idx: int) -> float:
    pass;
func audio_track_get_key_start_offset(track_idx: int, key_idx: int) -> float:
    pass;
func audio_track_get_key_stream(track_idx: int, key_idx: int) -> Resource:
    pass;
func audio_track_insert_key(track_idx: int, time: float, stream: Resource, start_offset: float, end_offset: float) -> int:
    pass;
func audio_track_set_key_end_offset(track_idx: int, key_idx: int, offset: float) -> void:
    pass;
func audio_track_set_key_start_offset(track_idx: int, key_idx: int, offset: float) -> void:
    pass;
func audio_track_set_key_stream(track_idx: int, key_idx: int, stream: Resource) -> void:
    pass;
func bezier_track_get_key_in_handle(track_idx: int, key_idx: int) -> Vector2:
    pass;
func bezier_track_get_key_out_handle(track_idx: int, key_idx: int) -> Vector2:
    pass;
func bezier_track_get_key_value(track_idx: int, key_idx: int) -> float:
    pass;
func bezier_track_insert_key(track_idx: int, time: float, value: float, in_handle: Vector2, out_handle: Vector2) -> int:
    pass;
func bezier_track_interpolate(track_idx: int, time: float) -> float:
    pass;
func bezier_track_set_key_in_handle(track_idx: int, key_idx: int, in_handle: Vector2) -> void:
    pass;
func bezier_track_set_key_out_handle(track_idx: int, key_idx: int, out_handle: Vector2) -> void:
    pass;
func bezier_track_set_key_value(track_idx: int, key_idx: int, value: float) -> void:
    pass;
func clear() -> void:
    pass;
func copy_track(track_idx: int, to_animation: Animation) -> void:
    pass;
func find_track(path: NodePath) -> int:
    pass;
func get_track_count() -> int:
    pass;
func method_track_get_key_indices(track_idx: int, time_sec: float, delta: float) -> PackedInt32Array:
    pass;
func method_track_get_name(track_idx: int, key_idx: int) -> StringName:
    pass;
func method_track_get_params(track_idx: int, key_idx: int) -> Array:
    pass;
func remove_track(track_idx: int) -> void:
    pass;
func track_find_key(track_idx: int, time: float, exact: bool) -> int:
    pass;
func track_get_interpolation_loop_wrap(track_idx: int) -> bool:
    pass;
func track_get_interpolation_type(track_idx: int) -> int:
    pass;
func track_get_key_count(track_idx: int) -> int:
    pass;
func track_get_key_time(track_idx: int, key_idx: int) -> float:
    pass;
func track_get_key_transition(track_idx: int, key_idx: int) -> float:
    pass;
func track_get_key_value(track_idx: int, key_idx: int) -> Variant:
    pass;
func track_get_path(track_idx: int) -> NodePath:
    pass;
func track_get_type(track_idx: int) -> int:
    pass;
func track_insert_key(track_idx: int, time: float, key: Variant, transition: float) -> void:
    pass;
func track_is_enabled(track_idx: int) -> bool:
    pass;
func track_is_imported(track_idx: int) -> bool:
    pass;
func track_move_down(track_idx: int) -> void:
    pass;
func track_move_to(track_idx: int, to_idx: int) -> void:
    pass;
func track_move_up(track_idx: int) -> void:
    pass;
func track_remove_key(track_idx: int, key_idx: int) -> void:
    pass;
func track_remove_key_at_time(track_idx: int, time: float) -> void:
    pass;
func track_set_enabled(track_idx: int, enabled: bool) -> void:
    pass;
func track_set_imported(track_idx: int, imported: bool) -> void:
    pass;
func track_set_interpolation_loop_wrap(track_idx: int, interpolation: bool) -> void:
    pass;
func track_set_interpolation_type(track_idx: int, interpolation: int) -> void:
    pass;
func track_set_key_time(track_idx: int, key_idx: int, time: float) -> void:
    pass;
func track_set_key_transition(track_idx: int, key_idx: int, transition: float) -> void:
    pass;
func track_set_key_value(track_idx: int, key: int, value: Variant) -> void:
    pass;
func track_set_path(track_idx: int, path: NodePath) -> void:
    pass;
func track_swap(track_idx: int, with_idx: int) -> void:
    pass;
func transform_track_insert_key(track_idx: int, time: float, location: Vector3, rotation: Quaternion, scale: Vector3) -> int:
    pass;
func transform_track_interpolate(track_idx: int, time_sec: float) -> Array:
    pass;
func value_track_get_key_indices(track_idx: int, time_sec: float, delta: float) -> PackedInt32Array:
    pass;
func value_track_get_update_mode(track_idx: int) -> int:
    pass;
func value_track_interpolate(track_idx: int, time_sec: float) -> Variant:
    pass;
func value_track_set_update_mode(track_idx: int, mode: int) -> void:
    pass;
