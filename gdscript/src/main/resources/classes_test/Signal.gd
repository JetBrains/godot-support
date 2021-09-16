class_name Signal


func Signal() -> Signal:
    pass;
func Signal(from: Signal) -> Signal:
    pass;
func Signal(object: Object, signal: StringName) -> Signal:
    pass;
func connect(callable: Callable, binds: Array, flags: int) -> int:
    pass;
func disconnect(callable: Callable) -> void:
    pass;
func emit() -> void:
    pass;
func get_connections() -> Array:
    pass;
func get_name() -> StringName:
    pass;
func get_object() -> Object:
    pass;
func get_object_id() -> int:
    pass;
func is_connected(callable: Callable) -> bool:
    pass;
func is_null() -> bool:
    pass;
