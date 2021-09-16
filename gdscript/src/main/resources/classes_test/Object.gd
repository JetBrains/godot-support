class_name Object
const NOTIFICATION_POSTINITIALIZE = 0;
const NOTIFICATION_PREDELETE = 1;
const CONNECT_DEFERRED = 1;
const CONNECT_PERSIST = 2;
const CONNECT_ONESHOT = 4;
const CONNECT_REFERENCE_COUNTED = 8;


func _get(property: StringName) -> Variant:
    pass;
func _get_property_list() -> Array:
    pass;
func _init() -> void:
    pass;
func _notification(what: int) -> void:
    pass;
func _set(property: StringName, value: Variant) -> bool:
    pass;
func _to_string() -> String:
    pass;
func add_user_signal(signal: String, arguments: Array) -> void:
    pass;
func call(method: StringName) -> Variant:
    pass;
func call_deferred(method: StringName) -> void:
    pass;
func callv(method: StringName, arg_array: Array) -> Variant:
    pass;
func can_translate_messages() -> bool:
    pass;
func connect(signal: StringName, callable: Callable, binds: Array, flags: int) -> int:
    pass;
func disconnect(signal: StringName, callable: Callable) -> void:
    pass;
func emit_signal(signal: StringName) -> void:
    pass;
func free() -> void:
    pass;
func get(property: String) -> Variant:
    pass;
func get_class() -> String:
    pass;
func get_incoming_connections() -> Array:
    pass;
func get_indexed(property: NodePath) -> Variant:
    pass;
func get_instance_id() -> int:
    pass;
func get_meta(name: String) -> Variant:
    pass;
func get_meta_list() -> PackedStringArray:
    pass;
func get_method_list() -> Array:
    pass;
func get_property_list() -> Array:
    pass;
func get_script() -> Variant:
    pass;
func get_signal_connection_list(signal: String) -> Array:
    pass;
func get_signal_list() -> Array:
    pass;
func has_meta(name: String) -> bool:
    pass;
func has_method(method: StringName) -> bool:
    pass;
func has_signal(signal: StringName) -> bool:
    pass;
func has_user_signal(signal: StringName) -> bool:
    pass;
func is_blocking_signals() -> bool:
    pass;
func is_class(class: String) -> bool:
    pass;
func is_connected(signal: StringName, callable: Callable) -> bool:
    pass;
func is_queued_for_deletion() -> bool:
    pass;
func notification(what: int, reversed: bool) -> void:
    pass;
func notify_property_list_changed() -> void:
    pass;
func remove_meta(name: String) -> void:
    pass;
func set(property: String, value: Variant) -> void:
    pass;
func set_block_signals(enable: bool) -> void:
    pass;
func set_deferred(property: StringName, value: Variant) -> void:
    pass;
func set_indexed(property: NodePath, value: Variant) -> void:
    pass;
func set_message_translation(enable: bool) -> void:
    pass;
func set_meta(name: String, value: Variant) -> void:
    pass;
func set_script(script: Variant) -> void:
    pass;
func to_string() -> String:
    pass;
func tr(message: StringName, context: StringName) -> String:
    pass;
func tr_n(message: StringName, plural_message: StringName, n: int, context: StringName) -> String:
    pass;
