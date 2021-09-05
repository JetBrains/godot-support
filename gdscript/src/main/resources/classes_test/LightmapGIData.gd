extends Resource
class_name LightmapGIData


var light_texture: TextureLayered setget set_light_texture, get_light_texture;

func add_user(path: NodePath, uv_scale: Rect2, slice_index: int, sub_instance: int) -> void:
    pass;

func clear_users() -> void:
    pass;

func get_user_count() -> int:
    pass;

func get_user_path(user_idx: int) -> NodePath:
    pass;

func is_using_spherical_harmonics() -> bool:
    pass;

func set_uses_spherical_harmonics(uses_spherical_harmonics: bool) -> void:
    pass;

