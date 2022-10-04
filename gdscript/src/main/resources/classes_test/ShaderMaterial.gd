#brief A material that uses a custom [Shader] program.
#desc A material that uses a custom [Shader] program to render either items to screen or process particles. You can create multiple materials for the same shader but configure different values for the uniforms defined in the shader.
class_name ShaderMaterial


#desc The [Shader] program used to render this material.
var shader: Shader;



#desc Returns the current value set for this material of a uniform in the shader.
func get_shader_parameter() -> Variant:
	pass;

#desc Changes the value set for this material of a uniform in the shader.
#desc [b]Note:[/b] [param param] must match the name of the uniform in the code exactly.
func set_shader_parameter(param: StringName, value: Variant) -> void:
	pass;


