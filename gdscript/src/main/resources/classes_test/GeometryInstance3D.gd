extends VisualInstance3D
class_name GeometryInstance3D
const SHADOW_CASTING_SETTING_OFF = 0;
const SHADOW_CASTING_SETTING_ON = 1;
const SHADOW_CASTING_SETTING_DOUBLE_SIDED = 2;
const SHADOW_CASTING_SETTING_SHADOWS_ONLY = 3;
const GI_MODE_DISABLED = 0;
const GI_MODE_BAKED = 1;
const GI_MODE_DYNAMIC = 2;
const LIGHTMAP_SCALE_1X = 0;
const LIGHTMAP_SCALE_2X = 1;
const LIGHTMAP_SCALE_4X = 2;
const LIGHTMAP_SCALE_8X = 3;
const LIGHTMAP_SCALE_MAX = 4;

var cast_shadow: int;
var extra_cull_margin: float;
var gi_lightmap_scale: int;
var gi_mode: int;
var ignore_occlusion_culling: bool;
var lod_bias: float;
var material_override: Material;
var visibility_range_begin: float;
var visibility_range_begin_margin: float;
var visibility_range_end: float;
var visibility_range_end_margin: float;

func get_shader_instance_uniform(uniform: StringName) -> Variant:
    pass;
func set_custom_aabb(aabb: AABB) -> void:
    pass;
func set_shader_instance_uniform(uniform: StringName, value: Variant) -> void:
    pass;
