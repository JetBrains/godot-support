class_name ScriptLanguageExtension

const LOOKUP_RESULT_SCRIPT_LOCATION = 0;

const LOOKUP_RESULT_CLASS = 1;

const LOOKUP_RESULT_CLASS_CONSTANT = 2;

const LOOKUP_RESULT_CLASS_PROPERTY = 3;

const LOOKUP_RESULT_CLASS_METHOD = 4;

const LOOKUP_RESULT_CLASS_SIGNAL = 5;

const LOOKUP_RESULT_CLASS_ENUM = 6;

const LOOKUP_RESULT_CLASS_TBD_GLOBALSCOPE = 7;

const LOOKUP_RESULT_CLASS_ANNOTATION = 8;

const LOOKUP_RESULT_MAX = 9;

#desc The option is local to the location of the code completion query - e.g. a local variable.
const LOCATION_LOCAL = 0;

#desc The option is from the containing class or a parent class, relative to the location of the code completion query. Perform a bitwise OR with the class depth (e.g. 0 for the local class, 1 for the parent, 2 for the grandparent, etc) to store the depth of an option in a the class or a parent class.
const LOCATION_PARENT_MASK = 256;

#desc The option is from user code which is not local and not in a derived class (e.g. Autoload Singletons).
const LOCATION_OTHER_USER_CODE = 512;

#desc The option is from other engine code, not covered by the other enum constants - e.g. built-in classes.
const LOCATION_OTHER = 1024;

const CODE_COMPLETION_KIND_CLASS = 0;

const CODE_COMPLETION_KIND_FUNCTION = 1;

const CODE_COMPLETION_KIND_SIGNAL = 2;

const CODE_COMPLETION_KIND_VARIABLE = 3;

const CODE_COMPLETION_KIND_MEMBER = 4;

const CODE_COMPLETION_KIND_ENUM = 5;

const CODE_COMPLETION_KIND_CONSTANT = 6;

const CODE_COMPLETION_KIND_NODE_PATH = 7;

const CODE_COMPLETION_KIND_FILE_PATH = 8;

const CODE_COMPLETION_KIND_PLAIN_TEXT = 9;

const CODE_COMPLETION_KIND_MAX = 10;




virtual func _add_global_constant(name: StringName, value: Variant) -> void:
	pass;

virtual func _add_named_global_constant(name: StringName, value: Variant) -> void:
	pass;

virtual func _alloc_instance_binding_data(object: Object) -> void*:
	pass;

virtual const func _auto_indent_code(code: String, from_line: int, to_line: int) -> String:
	pass;

virtual const func _can_inherit_from_file() -> bool:
	pass;

virtual const func _complete_code(code: String, path: String, owner: Object) -> Dictionary:
	pass;

virtual const func _create_script() -> Object:
	pass;

virtual func _debug_get_current_stack_info() -> Dictionary[]:
	pass;

virtual const func _debug_get_error() -> String:
	pass;

virtual func _debug_get_globals(max_subitems: int, max_depth: int) -> Dictionary:
	pass;

virtual const func _debug_get_stack_level_count() -> int:
	pass;

virtual const func _debug_get_stack_level_function(level: int) -> String:
	pass;

virtual func _debug_get_stack_level_instance(level: int) -> void*:
	pass;

virtual const func _debug_get_stack_level_line(level: int) -> int:
	pass;

virtual func _debug_get_stack_level_locals(level: int, max_subitems: int, max_depth: int) -> Dictionary:
	pass;

virtual func _debug_get_stack_level_members(level: int, max_subitems: int, max_depth: int) -> Dictionary:
	pass;

virtual func _debug_parse_stack_level_expression(level: int, expression: String, max_subitems: int, max_depth: int) -> String:
	pass;

virtual func _execute_file(path: String) -> int:
	pass;

virtual const func _find_function(class_name: String, function_name: String) -> int:
	pass;

virtual func _finish() -> void:
	pass;

virtual func _frame() -> void:
	pass;

virtual func _free_instance_binding_data(data: void*) -> void:
	pass;

virtual const func _get_built_in_templates(object: StringName) -> Dictionary[]:
	pass;

virtual const func _get_comment_delimiters() -> PackedStringArray:
	pass;

virtual const func _get_extension() -> String:
	pass;

virtual const func _get_global_class_name(path: String) -> Dictionary:
	pass;

virtual const func _get_name() -> String:
	pass;

virtual const func _get_public_annotations() -> Dictionary[]:
	pass;

virtual const func _get_public_constants() -> Dictionary:
	pass;

virtual const func _get_public_functions() -> Dictionary[]:
	pass;

virtual const func _get_recognized_extensions() -> PackedStringArray:
	pass;

virtual const func _get_reserved_words() -> PackedStringArray:
	pass;

virtual const func _get_string_delimiters() -> PackedStringArray:
	pass;

virtual const func _get_type() -> String:
	pass;

virtual const func _handles_global_class_type(type: String) -> bool:
	pass;

virtual const func _has_named_classes() -> bool:
	pass;

virtual func _init() -> void:
	pass;

virtual const func _is_control_flow_keyword(keyword: String) -> bool:
	pass;

virtual func _is_using_templates() -> bool:
	pass;

virtual const func _lookup_code(code: String, symbol: String, path: String, owner: Object) -> Dictionary:
	pass;

virtual const func _make_function(class_name: String, function_name: String, function_args: PackedStringArray) -> String:
	pass;

virtual const func _make_template(template: String, class_name: String, base_class_name: String) -> Script:
	pass;

virtual func _open_in_external_editor(script: Script, line: int, column: int) -> int:
	pass;

virtual func _overrides_external_editor() -> bool:
	pass;

virtual func _profiling_get_accumulated_data(info_array: ScriptLanguageExtensionProfilingInfo*, info_max: int) -> int:
	pass;

virtual func _profiling_get_frame_data(info_array: ScriptLanguageExtensionProfilingInfo*, info_max: int) -> int:
	pass;

virtual func _profiling_start() -> void:
	pass;

virtual func _profiling_stop() -> void:
	pass;

virtual func _refcount_decremented_instance_binding(object: Object) -> bool:
	pass;

virtual func _refcount_incremented_instance_binding(object: Object) -> void:
	pass;

virtual func _reload_all_scripts() -> void:
	pass;

virtual func _reload_tool_script(script: Script, soft_reload: bool) -> void:
	pass;

virtual func _remove_named_global_constant(name: StringName) -> void:
	pass;

virtual const func _supports_builtin_mode() -> bool:
	pass;

virtual const func _supports_documentation() -> bool:
	pass;

virtual func _thread_enter() -> void:
	pass;

virtual func _thread_exit() -> void:
	pass;

virtual const func _validate(script: String, path: String, validate_functions: bool, validate_errors: bool, validate_warnings: bool, validate_safe_lines: bool) -> Dictionary:
	pass;

virtual const func _validate_path(path: String) -> String:
	pass;


