extends Material
class_name ShaderMaterial


var shader: Shader setget set_shader, get_shader;

func get_shader_param(param: StringName) -> Variant:
    pass;

func property_can_revert(name: String) -> bool:
    pass;

func property_get_revert(name: String) -> Variant:
    pass;

func set_shader_param(param: StringName, value: Variant) -> void:
    pass;

