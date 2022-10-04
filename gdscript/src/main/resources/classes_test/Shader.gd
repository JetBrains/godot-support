#brief A custom shader program.
#desc This class allows you to define a custom shader program that can be used by a [ShaderMaterial]. Shaders allow you to write your own custom behavior for rendering objects or updating particle information. For a detailed explanation and usage, please see the tutorials linked below.
class_name Shader

#desc Mode used to draw all 3D objects.
const MODE_SPATIAL = 0;

#desc Mode used to draw all 2D objects.
const MODE_CANVAS_ITEM = 1;

#desc Mode used to calculate particle information on a per-particle basis. Not used for drawing.
const MODE_PARTICLES = 2;

#desc Mode used for drawing skies. Only works with shaders attached to [Sky] objects.
const MODE_SKY = 3;

#desc Mode used for setting the color and density of volumetric fog effect.
const MODE_FOG = 4;


#desc Returns the shader's code as the user has written it, not the full generated code used internally.
var code: String;



#desc Returns the texture that is set as default for the specified parameter.
#desc [b]Note:[/b] [param name] must match the name of the uniform in the code exactly.
#desc [b]Note:[/b] If the sampler array is used use [param index] to access the specified texture.
func get_default_texture_parameter(name: StringName, index: int) -> Texture2D:
	pass;

#desc Returns the shader mode for the shader, either [constant MODE_CANVAS_ITEM], [constant MODE_SPATIAL] or [constant MODE_PARTICLES].
func get_mode() -> int:
	pass;

#desc Returns [code]true[/code] if the shader has this param defined as a uniform in its code.
#desc [b]Note:[/b] [param name] must match the name of the uniform in the code exactly.
func has_parameter() -> bool:
	pass;

#desc Sets the default texture to be used with a texture uniform. The default is used if a texture is not set in the [ShaderMaterial].
#desc [b]Note:[/b] [param name] must match the name of the uniform in the code exactly.
#desc [b]Note:[/b] If the sampler array is used use [param index] to access the specified texture.
func set_default_texture_parameter(name: StringName, texture: Texture2D, index: int) -> void:
	pass;


