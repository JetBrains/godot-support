extends Object
class_name XRServer
const TRACKER_CONTROLLER = 1;
const TRACKER_BASESTATION = 2;
const TRACKER_ANCHOR = 4;
const TRACKER_ANY_KNOWN = 127;
const TRACKER_UNKNOWN = 128;
const TRACKER_ANY = 255;
const RESET_FULL_ROTATION = 0;
const RESET_BUT_KEEP_TILT = 1;
const DONT_RESET_ROTATION = 2;

var primary_interface: XRInterface;
var world_scale: float;

func add_interface(interface: XRInterface) -> void:
    pass;
func add_tracker(tracker: XRPositionalTracker) -> void:
    pass;
func center_on_hmd(rotation_mode: int, keep_height: bool) -> void:
    pass;
func clear_primary_interface_if(interface: XRInterface) -> void:
    pass;
func find_interface(name: String) -> XRInterface:
    pass;
func get_hmd_transform() -> Transform3D:
    pass;
func get_interface(idx: int) -> XRInterface:
    pass;
func get_interface_count() -> int:
    pass;
func get_interfaces() -> Array:
    pass;
func get_last_commit_usec() -> int:
    pass;
func get_last_frame_usec() -> int:
    pass;
func get_last_process_usec() -> int:
    pass;
func get_reference_frame() -> Transform3D:
    pass;
func get_tracker(idx: int) -> XRPositionalTracker:
    pass;
func get_tracker_count() -> int:
    pass;
func remove_interface(interface: XRInterface) -> void:
    pass;
func remove_tracker(tracker: XRPositionalTracker) -> void:
    pass;
