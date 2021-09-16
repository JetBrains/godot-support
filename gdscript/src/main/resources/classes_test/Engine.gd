extends Object
class_name Engine

var editor_hint: bool;
var iterations_per_second: int;
var physics_jitter_fix: float;
var print_error_messages: bool;
var target_fps: int;
var time_scale: float;

func get_author_info() -> Dictionary:
    pass;
func get_copyright_info() -> Array:
    pass;
func get_donor_info() -> Dictionary:
    pass;
func get_frames_drawn() -> int:
    pass;
func get_frames_per_second() -> float:
    pass;
func get_license_info() -> Dictionary:
    pass;
func get_license_text() -> String:
    pass;
func get_main_loop() -> MainLoop:
    pass;
func get_physics_frames() -> int:
    pass;
func get_physics_interpolation_fraction() -> float:
    pass;
func get_process_frames() -> int:
    pass;
func get_singleton(name: String) -> Object:
    pass;
func get_version_info() -> Dictionary:
    pass;
func has_singleton(name: String) -> bool:
    pass;
func is_in_physics_frame() -> bool:
    pass;
