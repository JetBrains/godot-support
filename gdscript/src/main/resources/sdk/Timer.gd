extends Node
#brief A countdown timer.
#desc Counts down a specified interval and emits a signal on reaching 0. Can be set to repeat or "one-shot" mode.
#desc [b]Note:[/b] To create a one-shot timer without instantiating a node, use [method SceneTree.create_timer].
class_name Timer

#desc Update the timer during the physics step at each frame (fixed framerate processing).
const TIMER_PROCESS_PHYSICS = 0;

#desc Update the timer during the idle time at each frame.
const TIMER_PROCESS_IDLE = 1;


#desc If [code]true[/code], the timer will automatically start when entering the scene tree.
#desc [b]Note:[/b] This property is automatically set to [code]false[/code] after the timer enters the scene tree and starts.
var autostart: bool;

#desc If [code]true[/code], the timer will stop when reaching 0. If [code]false[/code], it will restart.
var one_shot: bool;

#desc If [code]true[/code], the timer is paused and will not process until it is unpaused again, even if [method start] is called.
var paused: bool;

#desc Processing callback. See [enum TimerProcessCallback].
var process_callback: int;

#desc The timer's remaining time in seconds. Returns 0 if the timer is inactive.
#desc [b]Note:[/b] You cannot set this value. To change the timer's remaining time, use [method start].
var time_left: float;

#desc The wait time in seconds.
#desc [b]Note:[/b] Timers can only emit once per rendered frame at most (or once per physics frame if [member process_callback] is [constant TIMER_PROCESS_PHYSICS]). This means very low wait times (lower than 0.05 seconds) will behave in significantly different ways depending on the rendered framerate. For very low wait times, it is recommended to use a process loop in a script instead of using a Timer node.
var wait_time: float;



#desc Returns [code]true[/code] if the timer is stopped.
func is_stopped() -> bool:
	pass;

#desc Starts the timer. Sets [member wait_time] to [param time_sec] if [code]time_sec > 0[/code]. This also resets the remaining time to [member wait_time].
#desc [b]Note:[/b] This method will not resume a paused timer. See [member paused].
func start(time_sec: float) -> void:
	pass;

#desc Stops the timer.
func stop() -> void:
	pass;


