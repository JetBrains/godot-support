extends VisualShaderNodeResizableBase
#brief Base class for a family of nodes with variable number of input and output ports within the visual shader graph.
#desc Currently, has no direct usage, use the derived classes instead.
class_name VisualShaderNodeGroupBase




#desc Adds an input port with the specified [param type] (see [enum VisualShaderNode.PortType]) and [param name].
func add_input_port(id: int, type: int, name: String) -> void:
	pass;

#desc Adds an output port with the specified [param type] (see [enum VisualShaderNode.PortType]) and [param name].
func add_output_port(id: int, type: int, name: String) -> void:
	pass;

#desc Removes all previously specified input ports.
func clear_input_ports() -> void:
	pass;

#desc Removes all previously specified output ports.
func clear_output_ports() -> void:
	pass;

#desc Returns a free input port ID which can be used in [method add_input_port].
func get_free_input_port_id() -> int:
	pass;

#desc Returns a free output port ID which can be used in [method add_output_port].
func get_free_output_port_id() -> int:
	pass;

#desc Returns the number of input ports in use. Alternative for [method get_free_input_port_id].
func get_input_port_count() -> int:
	pass;

#desc Returns a [String] description of the input ports as a colon-separated list using the format [code]id,type,name;[/code] (see [method add_input_port]).
func get_inputs() -> String:
	pass;

#desc Returns the number of output ports in use. Alternative for [method get_free_output_port_id].
func get_output_port_count() -> int:
	pass;

#desc Returns a [String] description of the output ports as a colon-separated list using the format [code]id,type,name;[/code] (see [method add_output_port]).
func get_outputs() -> String:
	pass;

#desc Returns [code]true[/code] if the specified input port exists.
func has_input_port(id: int) -> bool:
	pass;

#desc Returns [code]true[/code] if the specified output port exists.
func has_output_port(id: int) -> bool:
	pass;

#desc Returns [code]true[/code] if the specified port name does not override an existed port name and is valid within the shader.
func is_valid_port_name(name: String) -> bool:
	pass;

#desc Removes the specified input port.
func remove_input_port(id: int) -> void:
	pass;

#desc Removes the specified output port.
func remove_output_port(id: int) -> void:
	pass;

#desc Renames the specified input port.
func set_input_port_name(id: int, name: String) -> void:
	pass;

#desc Sets the specified input port's type (see [enum VisualShaderNode.PortType]).
func set_input_port_type(id: int, type: int) -> void:
	pass;

#desc Defines all input ports using a [String] formatted as a colon-separated list: [code]id,type,name;[/code] (see [method add_input_port]).
func set_inputs(inputs: String) -> void:
	pass;

#desc Renames the specified output port.
func set_output_port_name(id: int, name: String) -> void:
	pass;

#desc Sets the specified output port's type (see [enum VisualShaderNode.PortType]).
func set_output_port_type(id: int, type: int) -> void:
	pass;

#desc Defines all output ports using a [String] formatted as a colon-separated list: [code]id,type,name;[/code] (see [method add_output_port]).
func set_outputs(outputs: String) -> void:
	pass;


