extends Node
class_name Timer

const TIMER_PROCESS_PHYSICS = 0;
const TIMER_PROCESS_IDLE = 1;

var autostart: bool setget set_autostart, has_autostart;
var one_shot: bool setget set_one_shot, is_one_shot;
var paused: bool setget set_paused, is_paused;
var process_callback: int setget set_timer_process_callback, get_timer_process_callback;
var time_left: float setget , get_time_left;
var wait_time: float setget set_wait_time, get_wait_time;

func is_stopped() -> bool:
    pass;

func start(time_sec: float) -> void:
    pass;

func stop() -> void:
    pass;

