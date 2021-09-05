extends Resource
class_name RDShaderBytecode


var bytecode_compute: PackedByteArray setget set_stage_bytecode, get_stage_bytecode;
var bytecode_fragment: PackedByteArray setget set_stage_bytecode, get_stage_bytecode;
var bytecode_tesselation_control: PackedByteArray setget set_stage_bytecode, get_stage_bytecode;
var bytecode_tesselation_evaluation: PackedByteArray setget set_stage_bytecode, get_stage_bytecode;
var bytecode_vertex: PackedByteArray setget set_stage_bytecode, get_stage_bytecode;
var compile_error_compute: String setget set_stage_compile_error, get_stage_compile_error;
var compile_error_fragment: String setget set_stage_compile_error, get_stage_compile_error;
var compile_error_tesselation_control: String setget set_stage_compile_error, get_stage_compile_error;
var compile_error_tesselation_evaluation: String setget set_stage_compile_error, get_stage_compile_error;
var compile_error_vertex: String setget set_stage_compile_error, get_stage_compile_error;

func get_stage_bytecode(stage: int) -> PackedByteArray:
    pass;

func get_stage_compile_error(stage: int) -> String:
    pass;

func set_stage_bytecode(stage: int, bytecode: PackedByteArray) -> void:
    pass;

func set_stage_compile_error(stage: int, compile_error: String) -> void:
    pass;

