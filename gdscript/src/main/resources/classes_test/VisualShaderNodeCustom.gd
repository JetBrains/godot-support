extends VisualShaderNode
#brief Virtual class to define custom [VisualShaderNode]s for use in the Visual Shader Editor.
#desc By inheriting this class you can create a custom [VisualShader] script addon which will be automatically added to the Visual Shader Editor. The [VisualShaderNode]'s behavior is defined by overriding the provided virtual methods.
#desc In order for the node to be registered as an editor addon, you must use the [code]@tool[/code] annotation and provide a [code]class_name[/code] for your custom script. For example:
#desc [codeblock]
#desc @tool
#desc extends VisualShaderNodeCustom
#desc class_name VisualShaderNodeNoise
#desc [/codeblock]
class_name VisualShaderNodeCustom




#desc Override this method to define the path to the associated custom node in the Visual Shader Editor's members dialog. The path may look like [code]"MyGame/MyFunctions/Noise"[/code].
#desc Defining this method is [b]optional[/b]. If not overridden, the node will be filed under the "Addons" category.
func _get_category() -> String:
	pass;

#desc Override this method to define the actual shader code of the associated custom node. The shader code should be returned as a string, which can have multiple lines (the [code]"""[/code] multiline string construct can be used for convenience).
#desc The [param input_vars] and [param output_vars] arrays contain the string names of the various input and output variables, as defined by [code]_get_input_*[/code] and [code]_get_output_*[/code] virtual methods in this class.
#desc The output ports can be assigned values in the shader code. For example, [code]return output_vars[0] + " = " + input_vars[0] + ";"[/code].
#desc You can customize the generated code based on the shader [param mode] (see [enum Shader.Mode]) and/or [param type] (see [enum VisualShader.Type]).
#desc Defining this method is [b]required[/b].
func _get_code(input_vars: String[], output_vars: String[], mode: int, type: int) -> String:
	pass;

#desc Override this method to define the description of the associated custom node in the Visual Shader Editor's members dialog.
#desc Defining this method is [b]optional[/b].
func _get_description() -> String:
	pass;

#desc Override this method to add a shader code to the beginning of each shader function (once). The shader code should be returned as a string, which can have multiple lines (the [code]"""[/code] multiline string construct can be used for convenience).
#desc If there are multiple custom nodes of different types which use this feature the order of each insertion is undefined.
#desc You can customize the generated code based on the shader [param mode] (see [enum Shader.Mode]) and/or [param type] (see [enum VisualShader.Type]).
#desc Defining this method is [b]optional[/b].
func _get_func_code(mode: int, type: int) -> String:
	pass;

#desc Override this method to add shader code on top of the global shader, to define your own standard library of reusable methods, varyings, constants, uniforms, etc. The shader code should be returned as a string, which can have multiple lines (the [code]"""[/code] multiline string construct can be used for convenience).
#desc Be careful with this functionality as it can cause name conflicts with other custom nodes, so be sure to give the defined entities unique names.
#desc You can customize the generated code based on the shader [param mode] (see [enum Shader.Mode]).
#desc Defining this method is [b]optional[/b].
func _get_global_code(mode: int) -> String:
	pass;

#desc Override this method to define the number of input ports of the associated custom node.
#desc Defining this method is [b]required[/b]. If not overridden, the node has no input ports.
func _get_input_port_count() -> int:
	pass;

#desc Override this method to define the names of input ports of the associated custom node. The names are used both for the input slots in the editor and as identifiers in the shader code, and are passed in the [code]input_vars[/code] array in [method _get_code].
#desc Defining this method is [b]optional[/b], but recommended. If not overridden, input ports are named as [code]"in" + str(port)[/code].
func _get_input_port_name(port: int) -> String:
	pass;

#desc Override this method to define the returned type of each input port of the associated custom node (see [enum VisualShaderNode.PortType] for possible types).
#desc Defining this method is [b]optional[/b], but recommended. If not overridden, input ports will return the [constant VisualShaderNode.PORT_TYPE_SCALAR] type.
func _get_input_port_type(port: int) -> int:
	pass;

#desc Override this method to define the name of the associated custom node in the Visual Shader Editor's members dialog and graph.
#desc Defining this method is [b]optional[/b], but recommended. If not overridden, the node will be named as "Unnamed".
func _get_name() -> String:
	pass;

#desc Override this method to define the number of output ports of the associated custom node.
#desc Defining this method is [b]required[/b]. If not overridden, the node has no output ports.
func _get_output_port_count() -> int:
	pass;

#desc Override this method to define the names of output ports of the associated custom node. The names are used both for the output slots in the editor and as identifiers in the shader code, and are passed in the [code]output_vars[/code] array in [method _get_code].
#desc Defining this method is [b]optional[/b], but recommended. If not overridden, output ports are named as [code]"out" + str(port)[/code].
func _get_output_port_name(port: int) -> String:
	pass;

#desc Override this method to define the returned type of each output port of the associated custom node (see [enum VisualShaderNode.PortType] for possible types).
#desc Defining this method is [b]optional[/b], but recommended. If not overridden, output ports will return the [constant VisualShaderNode.PORT_TYPE_SCALAR] type.
func _get_output_port_type(port: int) -> int:
	pass;

#desc Override this method to define the return icon of the associated custom node in the Visual Shader Editor's members dialog.
#desc Defining this method is [b]optional[/b]. If not overridden, no return icon is shown.
func _get_return_icon_type() -> int:
	pass;

#desc Override this method to prevent the node to be visible in the member dialog for the certain [param mode] (see [enum Shader.Mode]) and/or [param type] (see [enum VisualShader.Type]).
#desc Defining this method is [b]optional[/b]. If not overridden, it's [code]true[/code].
func _is_available(mode: int, type: int) -> bool:
	pass;

#desc Override this method to enable high-end mark in the Visual Shader Editor's members dialog.
#desc Defining this method is [b]optional[/b]. If not overridden, it's [code]false[/code].
func _is_highend() -> bool:
	pass;


