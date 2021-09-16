extends Object
class_name EngineDebugger


func has_capture(name: StringName) -> bool:
    pass;
func has_profiler(name: StringName) -> bool:
    pass;
func is_active() -> bool:
    pass;
func is_profiling(name: StringName) -> bool:
    pass;
func profiler_add_frame_data(name: StringName, data: Array) -> void:
    pass;
func profiler_enable(name: StringName, enable: bool, arguments: Array) -> void:
    pass;
func register_message_capture(name: StringName, callable: Callable) -> void:
    pass;
func register_profiler(name: StringName, toggle: Callable, add: Callable, tick: Callable) -> void:
    pass;
func send_message(message: String, data: Array) -> void:
    pass;
func unregister_message_capture(name: StringName) -> void:
    pass;
func unregister_profiler(name: StringName) -> void:
    pass;
