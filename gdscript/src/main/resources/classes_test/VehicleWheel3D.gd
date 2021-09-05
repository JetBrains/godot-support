extends Node3D
class_name VehicleWheel3D


var brake: float setget set_brake, get_brake;
var damping_compression: float setget set_damping_compression, get_damping_compression;
var damping_relaxation: float setget set_damping_relaxation, get_damping_relaxation;
var engine_force: float setget set_engine_force, get_engine_force;
var steering: float setget set_steering, get_steering;
var suspension_max_force: float setget set_suspension_max_force, get_suspension_max_force;
var suspension_stiffness: float setget set_suspension_stiffness, get_suspension_stiffness;
var suspension_travel: float setget set_suspension_travel, get_suspension_travel;
var use_as_steering: bool setget set_use_as_steering, is_used_as_steering;
var use_as_traction: bool setget set_use_as_traction, is_used_as_traction;
var wheel_friction_slip: float setget set_friction_slip, get_friction_slip;
var wheel_radius: float setget set_radius, get_radius;
var wheel_rest_length: float setget set_suspension_rest_length, get_suspension_rest_length;
var wheel_roll_influence: float setget set_roll_influence, get_roll_influence;

func get_rpm() -> float:
    pass;

func get_skidinfo() -> float:
    pass;

func is_in_contact() -> bool:
    pass;

