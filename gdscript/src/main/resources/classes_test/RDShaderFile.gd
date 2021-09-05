extends Resource
class_name RDShaderFile


var base_error: String setget set_base_error, get_base_error;

func get_bytecode(version: StringName) -> RDShaderBytecode:
    pass;

func get_version_list() -> PackedStringArray:
    pass;

func set_bytecode(bytecode: RDShaderBytecode, version: StringName) -> void:
    pass;

