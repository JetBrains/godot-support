extends Script
class_name ScriptExtension




func _can_instantiate() -> bool:
	pass;

func _editor_can_reload_from_file() -> bool:
	pass;

func _get_base_script() -> Script:
	pass;

func _get_constants() -> Dictionary:
	pass;

func _get_documentation() -> Dictionary[]:
	pass;

func _get_instance_base_type() -> StringName:
	pass;

func _get_language() -> ScriptLanguage:
	pass;

func _get_member_line(member: StringName) -> int:
	pass;

func _get_members() -> StringName[]:
	pass;

func _get_method_info(method: StringName) -> Dictionary:
	pass;

func _get_property_default_value(property: StringName) -> Variant:
	pass;

func _get_rpc_config() -> Variant:
	pass;

func _get_script_method_list() -> Dictionary[]:
	pass;

func _get_script_property_list() -> Dictionary[]:
	pass;

func _get_script_signal_list() -> Dictionary[]:
	pass;

func _get_source_code() -> String:
	pass;

func _has_method(method: StringName) -> bool:
	pass;

func _has_property_default_value(property: StringName) -> bool:
	pass;

func _has_script_signal(signal: StringName) -> bool:
	pass;

func _has_source_code() -> bool:
	pass;

func _inherits_script(script: Script) -> bool:
	pass;

func _instance_create(for_object: Object) -> void*:
	pass;

func _instance_has(object: Object) -> bool:
	pass;

func _is_placeholder_fallback_enabled() -> bool:
	pass;

func _is_tool() -> bool:
	pass;

func _is_valid() -> bool:
	pass;

func _placeholder_erased(placeholder: void*) -> void:
	pass;

func _placeholder_instance_create(for_object: Object) -> void*:
	pass;

func _reload(keep_state: bool) -> int:
	pass;

func _set_source_code(code: String) -> void:
	pass;

func _update_exports() -> void:
	pass;


