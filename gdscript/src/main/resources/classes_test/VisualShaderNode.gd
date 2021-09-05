extends Resource
class_name VisualShaderNode

const PORT_TYPE_SCALAR = 0;
const PORT_TYPE_SCALAR_INT = 1;
const PORT_TYPE_VECTOR = 2;
const PORT_TYPE_BOOLEAN = 3;
const PORT_TYPE_TRANSFORM = 4;
const PORT_TYPE_SAMPLER = 5;
const PORT_TYPE_MAX = 6;

var output_port_for_preview: int setget set_output_port_for_preview, get_output_port_for_preview;

func clear_default_input_values() -> void:
    pass;

func get_default_input_values() -> Array:
    pass;

func get_input_port_default_value(port: int) -> Variant:
    pass;

func remove_input_port_default_value(port: int) -> void:
    pass;

func set_default_input_values(values: Array) -> void:
    pass;

func set_input_port_default_value(port: int, value: Variant) -> void:
    pass;

