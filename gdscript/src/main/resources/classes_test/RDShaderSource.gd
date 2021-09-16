extends RefCounted
class_name RDShaderSource

var language: int;
var source_compute: String;
var source_fragment: String;
var source_tesselation_control: String;
var source_tesselation_evaluation: String;
var source_vertex: String;

func get_stage_source(stage: int) -> String:
    pass;
func set_stage_source(stage: int, source: String) -> void:
    pass;
