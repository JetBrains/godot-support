extends VisualShaderNode
#brief Represents the input shader parameter within the visual shader graph.
#desc Gives access to input variables (built-ins) available for the shader. See the shading reference for the list of available built-ins for each shader type (check [code]Tutorials[/code] section for link).
class_name VisualShaderNodeInput


#desc One of the several input constants in lower-case style like: "vertex"([code]VERTEX[/code]) or "point_size"([code]POINT_SIZE[/code]).
var input_name: String;



#desc Returns a translated name of the current constant in the Godot Shader Language. E.g. [code]"ALBEDO"[/code] if the [member input_name] equal to [code]"albedo"[/code].
func get_input_real_name() -> String:
	pass;


