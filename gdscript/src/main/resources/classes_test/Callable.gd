class_name Callable


func Callable() -> Callable:
    pass;
func Callable(from: Callable) -> Callable:
    pass;
func Callable(object: Object, method: StringName) -> Callable:
    pass;
func bind() -> Callable:
    pass;
func call() -> Variant:
    pass;
func call_deferred() -> void:
    pass;
func get_method() -> StringName:
    pass;
func get_object() -> Object:
    pass;
func get_object_id() -> int:
    pass;
func hash() -> int:
    pass;
func is_custom() -> bool:
    pass;
func is_null() -> bool:
    pass;
func is_standard() -> bool:
    pass;
func is_valid() -> bool:
    pass;
func rpc() -> void:
    pass;
func rpc_id(peer_id: int) -> void:
    pass;
func unbind(argcount: int) -> Callable:
    pass;
