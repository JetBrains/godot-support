extends Resource
class_name RDShaderBytecode

var bytecode_compute: PackedByteArray;
var bytecode_fragment: PackedByteArray;
var bytecode_tesselation_control: PackedByteArray;
var bytecode_tesselation_evaluation: PackedByteArray;
var bytecode_vertex: PackedByteArray;
var compile_error_compute: String;
var compile_error_fragment: String;
var compile_error_tesselation_control: String;
var compile_error_tesselation_evaluation: String;
var compile_error_vertex: String;

func get_stage_bytecode(stage: int) -> PackedByteArray:
    pass;
func get_stage_compile_error(stage: int) -> String:
    pass;
func set_stage_bytecode(stage: int, bytecode: PackedByteArray) -> void:
    pass;
func set_stage_compile_error(stage: int, compile_error: String) -> void:
    pass;
