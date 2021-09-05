extends Resource
class_name Material

const RENDER_PRIORITY_MAX = 127;
const RENDER_PRIORITY_MIN = -128;

var next_pass: Material setget set_next_pass, get_next_pass;
var render_priority: int setget set_render_priority, get_render_priority;

func inspect_native_shader_code() -> void:
    pass;

