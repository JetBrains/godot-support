extends RefCounted
class_name Tween

const TWEEN_PROCESS_PHYSICS = 0;
const TWEEN_PROCESS_IDLE = 1;
const TWEEN_PAUSE_BOUND = 0;
const TWEEN_PAUSE_STOP = 1;
const TWEEN_PAUSE_PROCESS = 2;
const TRANS_LINEAR = 0;
const TRANS_SINE = 1;
const TRANS_QUINT = 2;
const TRANS_QUART = 3;
const TRANS_QUAD = 4;
const TRANS_EXPO = 5;
const TRANS_ELASTIC = 6;
const TRANS_CUBIC = 7;
const TRANS_CIRC = 8;
const TRANS_BOUNCE = 9;
const TRANS_BACK = 10;
const EASE_IN = 0;
const EASE_OUT = 1;
const EASE_IN_OUT = 2;
const EASE_OUT_IN = 3;


func bind_node(node: Node) -> Tween:
    pass;

func chain() -> Tween:
    pass;

func custom_step(delta: float) -> bool:
    pass;

func interpolate_value(trans_type: Variant, ease_type: Variant, elapsed_time: float, initial_value: float, delta_value: int, duration: int) -> Variant:
    pass;

func is_running() -> bool:
    pass;

func is_valid() -> bool:
    pass;

func kill() -> void:
    pass;

func parallel() -> Tween:
    pass;

func pause() -> void:
    pass;

func play() -> void:
    pass;

func set_ease(ease: int) -> Tween:
    pass;

func set_loops(loops: int) -> Tween:
    pass;

func set_parallel(parallel: bool) -> Tween:
    pass;

func set_pause_mode(mode: int) -> Tween:
    pass;

func set_process_mode(mode: int) -> Tween:
    pass;

func set_speed_scale(speed: float) -> Tween:
    pass;

func set_trans(trans: int) -> Tween:
    pass;

func stop() -> void:
    pass;

func tween_callback(callback: Callable) -> CallbackTweener:
    pass;

func tween_interval(time: float) -> IntervalTweener:
    pass;

func tween_method(method: Callable, from: float, to: float, duration: float) -> MethodTweener:
    pass;

func tween_property(object: Object, property: NodePath, final_val: Variant, duration: float) -> PropertyTweener:
    pass;

