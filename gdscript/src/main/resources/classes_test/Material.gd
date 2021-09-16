extends Resource
class_name Material
const RENDER_PRIORITY_MAX = 127;
const RENDER_PRIORITY_MIN = -128;

var next_pass: Material;
var render_priority: int;

func inspect_native_shader_code() -> void:
    pass;
