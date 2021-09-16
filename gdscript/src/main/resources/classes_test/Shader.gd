extends Resource
class_name Shader
const MODE_SPATIAL = 0;
const MODE_CANVAS_ITEM = 1;
const MODE_PARTICLES = 2;
const MODE_SKY = 3;

var code: String;

func get_default_texture_param(param: StringName) -> Texture2D:
    pass;
func get_mode() -> int:
    pass;
func has_param(name: StringName) -> bool:
    pass;
func set_default_texture_param(param: StringName, texture: Texture2D) -> void:
    pass;
