extends Resource
class_name RDShaderFile


var base_error: String;



func get_spirv(version: StringName) -> RDShaderSPIRV:
	pass;

func get_version_list() -> PackedStringArray:
	pass;

func set_bytecode(bytecode: RDShaderSPIRV, version: StringName) -> void:
	pass;


