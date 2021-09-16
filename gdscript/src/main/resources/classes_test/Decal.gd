extends VisualInstance3D
class_name Decal
const TEXTURE_ALBEDO = 0;
const TEXTURE_NORMAL = 1;
const TEXTURE_ORM = 2;
const TEXTURE_EMISSION = 3;
const TEXTURE_MAX = 4;

var albedo_mix: float;
var cull_mask: int;
var distance_fade_begin: float;
var distance_fade_enabled: bool;
var distance_fade_length: float;
var emission_energy: float;
var extents: Vector3;
var lower_fade: float;
var modulate: Color;
var normal_fade: float;
var texture_albedo: Texture2D;
var texture_emission: Texture2D;
var texture_normal: Texture2D;
var texture_orm: Texture2D;
var upper_fade: float;

func get_texture(type: int) -> Texture2D:
    pass;
func set_texture(type: int, texture: Texture2D) -> void:
    pass;
