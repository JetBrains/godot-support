extends Node3D
class_name VehicleWheel3D

var brake: float;
var damping_compression: float;
var damping_relaxation: float;
var engine_force: float;
var steering: float;
var suspension_max_force: float;
var suspension_stiffness: float;
var suspension_travel: float;
var use_as_steering: bool;
var use_as_traction: bool;
var wheel_friction_slip: float;
var wheel_radius: float;
var wheel_rest_length: float;
var wheel_roll_influence: float;

func get_rpm() -> float:
    pass;
func get_skidinfo() -> float:
    pass;
func is_in_contact() -> bool:
    pass;
