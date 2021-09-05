extends VisualInstance3D
class_name Decal

const TEXTURE_ALBEDO = 0;
const TEXTURE_NORMAL = 1;
const TEXTURE_ORM = 2;
const TEXTURE_EMISSION = 3;
const TEXTURE_MAX = 4;

var albedo_mix: float setget set_albedo_mix, get_albedo_mix;
var cull_mask: int setget set_cull_mask, get_cull_mask;
var distance_fade_begin: float setget set_distance_fade_begin, get_distance_fade_begin;
var distance_fade_enabled: bool setget set_enable_distance_fade, is_distance_fade_enabled;
var distance_fade_length: float setget set_distance_fade_length, get_distance_fade_length;
var emission_energy: float setget set_emission_energy, get_emission_energy;
var extents: Vector3 setget set_extents, get_extents;
var lower_fade: float setget set_lower_fade, get_lower_fade;
var modulate: Color setget set_modulate, get_modulate;
var normal_fade: float setget set_normal_fade, get_normal_fade;
var texture_albedo: Texture2D setget set_texture, get_texture;
var texture_emission: Texture2D setget set_texture, get_texture;
var texture_normal: Texture2D setget set_texture, get_texture;
var texture_orm: Texture2D setget set_texture, get_texture;
var upper_fade: float setget set_upper_fade, get_upper_fade;

func get_texture(type: int) -> Texture2D:
    pass;

func set_texture(type: int, texture: Texture2D) -> void:
    pass;

