extends Object
class_name Input

const MOUSE_MODE_VISIBLE = 0;
const MOUSE_MODE_HIDDEN = 1;
const MOUSE_MODE_CAPTURED = 2;
const MOUSE_MODE_CONFINED = 3;
const MOUSE_MODE_CONFINED_HIDDEN = 4;
const CURSOR_ARROW = 0;
const CURSOR_IBEAM = 1;
const CURSOR_POINTING_HAND = 2;
const CURSOR_CROSS = 3;
const CURSOR_WAIT = 4;
const CURSOR_BUSY = 5;
const CURSOR_DRAG = 6;
const CURSOR_CAN_DROP = 7;
const CURSOR_FORBIDDEN = 8;
const CURSOR_VSIZE = 9;
const CURSOR_HSIZE = 10;
const CURSOR_BDIAGSIZE = 11;
const CURSOR_FDIAGSIZE = 12;
const CURSOR_MOVE = 13;
const CURSOR_VSPLIT = 14;
const CURSOR_HSPLIT = 15;
const CURSOR_HELP = 16;


func action_press(action: StringName, strength: float) -> void:
    pass;

func action_release(action: StringName) -> void:
    pass;

func add_joy_mapping(mapping: String, update_existing: bool) -> void:
    pass;

func get_accelerometer() -> Vector3:
    pass;

func get_action_raw_strength(action: StringName, exact_match: bool) -> float:
    pass;

func get_action_strength(action: StringName, exact_match: bool) -> float:
    pass;

func get_axis(negative_action: StringName, positive_action: StringName) -> float:
    pass;

func get_connected_joypads() -> Array:
    pass;

func get_current_cursor_shape() -> int:
    pass;

func get_gravity() -> Vector3:
    pass;

func get_gyroscope() -> Vector3:
    pass;

func get_joy_axis(device: int, axis: int) -> float:
    pass;

func get_joy_guid(device: int) -> String:
    pass;

func get_joy_name(device: int) -> String:
    pass;

func get_joy_vibration_duration(device: int) -> float:
    pass;

func get_joy_vibration_strength(device: int) -> Vector2:
    pass;

func get_last_mouse_speed() -> Vector2:
    pass;

func get_magnetometer() -> Vector3:
    pass;

func get_mouse_button_mask() -> int:
    pass;

func get_mouse_mode() -> int:
    pass;

func get_vector(negative_x: StringName, positive_x: StringName, negative_y: StringName, positive_y: StringName, deadzone: float) -> Vector2:
    pass;

func is_action_just_pressed(action: StringName, exact_match: bool) -> bool:
    pass;

func is_action_just_released(action: StringName, exact_match: bool) -> bool:
    pass;

func is_action_pressed(action: StringName, exact_match: bool) -> bool:
    pass;

func is_joy_button_pressed(device: int, button: int) -> bool:
    pass;

func is_joy_known(device: int) -> bool:
    pass;

func is_key_pressed(keycode: int) -> bool:
    pass;

func is_mouse_button_pressed(button: int) -> bool:
    pass;

func joy_connection_changed(device: int, connected: bool, name: String, guid: String) -> void:
    pass;

func parse_input_event(event: InputEvent) -> void:
    pass;

func remove_joy_mapping(guid: String) -> void:
    pass;

func set_custom_mouse_cursor(image: Resource, shape: int, hotspot: Vector2) -> void:
    pass;

func set_default_cursor_shape(shape: int) -> void:
    pass;

func set_mouse_mode(mode: int) -> void:
    pass;

func set_use_accumulated_input(enable: bool) -> void:
    pass;

func start_joy_vibration(device: int, weak_magnitude: float, strong_magnitude: float, duration: float) -> void:
    pass;

func stop_joy_vibration(device: int) -> void:
    pass;

func vibrate_handheld(duration_ms: int) -> void:
    pass;

func warp_mouse_position(to: Vector2) -> void:
    pass;

