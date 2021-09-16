extends Node3D
class_name XRController3D

var controller_id: int;
var rumble: float;

func get_controller_name() -> String:
    pass;
func get_is_active() -> bool:
    pass;
func get_joystick_axis(axis: int) -> float:
    pass;
func get_joystick_id() -> int:
    pass;
func get_mesh() -> Mesh:
    pass;
func get_tracker_hand() -> int:
    pass;
func is_button_pressed(button: int) -> bool:
    pass;
