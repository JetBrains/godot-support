extends Control
class_name EditorDebuggerPlugin


func has_capture(name: StringName) -> bool:
    pass;
func is_breaked() -> bool:
    pass;
func is_debuggable() -> bool:
    pass;
func is_session_active() -> bool:
    pass;
func register_message_capture(name: StringName, callable: Callable) -> void:
    pass;
func send_message(message: String, data: Array) -> void:
    pass;
func unregister_message_capture(name: StringName) -> void:
    pass;
