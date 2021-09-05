extends RefCounted
class_name XRPositionalTracker

const TRACKER_HAND_UNKNOWN = 0;
const TRACKER_HAND_LEFT = 1;
const TRACKER_HAND_RIGHT = 2;

var rumble: float setget set_rumble, get_rumble;

func get_joy_id() -> int:
    pass;

func get_mesh() -> Mesh:
    pass;

func get_orientation() -> Basis:
    pass;

func get_position() -> Vector3:
    pass;

func get_tracker_hand() -> int:
    pass;

func get_tracker_id() -> int:
    pass;

func get_tracker_name() -> StringName:
    pass;

func get_tracker_type() -> int:
    pass;

func get_transform(adjust_by_reference_frame: bool) -> Transform3D:
    pass;

func is_tracking_orientation() -> bool:
    pass;

func is_tracking_position() -> bool:
    pass;

