class_name ScriptExtension




virtual const func _can_instantiate() -> bool:
	pass;

virtual func _editor_can_reload_from_file() -> bool:
	pass;

virtual const func _get_base_script() -> Script:
	pass;

virtual const func _get_constants() -> Dictionary:
	pass;

virtual const func _get_documentation() -> Dictionary[]:
	pass;

virtual const func _get_instance_base_type() -> StringName:
	pass;

virtual const func _get_language() -> ScriptLanguage:
	pass;

virtual const func _get_member_line(member: StringName) -> int:
	pass;

virtual const func _get_members() -> StringName[]:
	pass;

virtual const func _get_method_info(method: StringName) -> Dictionary:
	pass;

virtual const func _get_property_default_value(property: StringName) -> Variant:
	pass;

virtual const func _get_rpc_config() -> Variant:
	pass;

virtual const func _get_script_method_list() -> Dictionary[]:
	pass;

virtual const func _get_script_property_list() -> Dictionary[]:
	pass;

virtual const func _get_script_signal_list() -> Dictionary[]:
	pass;

virtual const func _get_source_code() -> String:
	pass;

virtual const func _has_method(method: StringName) -> bool:
	pass;

virtual const func _has_property_default_value(property: StringName) -> bool:
	pass;

virtual const func _has_script_signal(signal: StringName) -> bool:
	pass;

virtual const func _has_source_code() -> bool:
	pass;

virtual const func _inherits_script(script: Script) -> bool:
	pass;

virtual const func _instance_create(for_object: Object) -> void*:
	pass;

virtual const func _instance_has(object: Object) -> bool:
	pass;

virtual const func _is_placeholder_fallback_enabled() -> bool:
	pass;

virtual const func _is_tool() -> bool:
	pass;

virtual const func _is_valid() -> bool:
	pass;

virtual func _placeholder_erased(placeholder: void*) -> void:
	pass;

virtual const func _placeholder_instance_create(for_object: Object) -> void*:
	pass;

virtual func _reload(keep_state: bool) -> int:
	pass;

virtual func _set_source_code(code: String) -> void:
	pass;

virtual func _update_exports() -> void:
	pass;


