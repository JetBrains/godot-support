extends RefCounted
class_name VelocityTracker3D


var track_physics_step: bool setget set_track_physics_step, is_tracking_physics_step;

func get_tracked_linear_velocity() -> Vector3:
    pass;

func reset(position: Vector3) -> void:
    pass;

func update_position(position: Vector3) -> void:
    pass;

