extends Node
class_name Timer
const TIMER_PROCESS_PHYSICS = 0;
const TIMER_PROCESS_IDLE = 1;

var autostart: bool;
var one_shot: bool;
var paused: bool;
var process_callback: int;
var time_left: float;
var wait_time: float;

func is_stopped() -> bool:
    pass;
func start(time_sec: float) -> void:
    pass;
func stop() -> void:
    pass;
