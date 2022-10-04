#brief Base class for nodes in a visual shader graph.
#desc Visual shader graphs consist of various nodes. Each node in the graph is a separate object and they are represented as a rectangular boxes with title and a set of properties. Each node has also connection ports that allow to connect it to another nodes and control the flow of the shader.
class_name VisualShaderNode

#desc Floating-point scalar. Translated to [code]float[/code] type in shader code.
const PORT_TYPE_SCALAR = 0;

#desc Integer scalar. Translated to [code]int[/code] type in shader code.
const PORT_TYPE_SCALAR_INT = 1;

#desc 2D vector of floating-point values. Translated to [code]vec2[/code] type in shader code.
const PORT_TYPE_VECTOR_2D = 2;

#desc 3D vector of floating-point values. Translated to [code]vec3[/code] type in shader code.
const PORT_TYPE_VECTOR_3D = 3;

#desc 4D vector of floating-point values. Translated to [code]vec4[/code] type in shader code.
const PORT_TYPE_VECTOR_4D = 4;

#desc Boolean type. Translated to [code]bool[/code] type in shader code.
const PORT_TYPE_BOOLEAN = 5;

#desc Transform type. Translated to [code]mat4[/code] type in shader code.
const PORT_TYPE_TRANSFORM = 6;

#desc Sampler type. Translated to reference of sampler uniform in shader code. Can only be used for input ports in non-uniform nodes.
const PORT_TYPE_SAMPLER = 7;

#desc Represents the size of the [enum PortType] enum.
const PORT_TYPE_MAX = 8;


#desc Sets the output port index which will be showed for preview. If set to [code]-1[/code] no port will be open for preview.
var output_port_for_preview: int;



#desc Clears the default input ports value.
func clear_default_input_values() -> void:
	pass;

#desc Returns an [Array] containing default values for all of the input ports of the node in the form [code][index0, value0, index1, value1, ...][/code].
func get_default_input_values() -> Array:
	pass;

#desc Returns the default value of the input [param port].
func get_input_port_default_value() -> Variant:
	pass;

#desc Removes the default value of the input [param port].
func remove_input_port_default_value() -> void:
	pass;

#desc Sets the default input ports values using an [Array] of the form [code][index0, value0, index1, value1, ...][/code]. For example: [code][0, Vector3(0, 0, 0), 1, Vector3(0, 0, 0)][/code].
func set_default_input_values() -> void:
	pass;

#desc Sets the default [param value] for the selected input [param port].
func set_input_port_default_value(port: int, value: Variant, prev_value: Variant) -> void:
	pass;


