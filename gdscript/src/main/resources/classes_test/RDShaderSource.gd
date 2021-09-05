extends RefCounted
class_name RDShaderSource


var language: int setget set_language, get_language;
var source_compute: String setget set_stage_source, get_stage_source;
var source_fragment: String setget set_stage_source, get_stage_source;
var source_tesselation_control: String setget set_stage_source, get_stage_source;
var source_tesselation_evaluation: String setget set_stage_source, get_stage_source;
var source_vertex: String setget set_stage_source, get_stage_source;

func get_stage_source(stage: int) -> String:
    pass;

func set_stage_source(stage: int, source: String) -> void:
    pass;

