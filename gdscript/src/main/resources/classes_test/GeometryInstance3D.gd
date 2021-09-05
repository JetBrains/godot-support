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

var cast_shadow: int setget set_cast_shadows_setting, get_cast_shadows_setting;
var extra_cull_margin: float setget set_extra_cull_margin, get_extra_cull_margin;
var gi_lightmap_scale: int setget set_lightmap_scale, get_lightmap_scale;
var gi_mode: int setget set_gi_mode, get_gi_mode;
var ignore_occlusion_culling: bool setget set_ignore_occlusion_culling, is_ignoring_occlusion_culling;
var lod_bias: float setget set_lod_bias, get_lod_bias;
var material_override: Material setget set_material_override, get_material_override;
var visibility_range_begin: float setget set_visibility_range_begin, get_visibility_range_begin;
var visibility_range_begin_margin: float setget set_visibility_range_begin_margin, get_visibility_range_begin_margin;
var visibility_range_end: float setget set_visibility_range_end, get_visibility_range_end;
var visibility_range_end_margin: float setget set_visibility_range_end_margin, get_visibility_range_end_margin;

func get_shader_instance_uniform(uniform: StringName) -> Variant:
    pass;

func set_custom_aabb(aabb: AABB) -> void:
    pass;

func set_shader_instance_uniform(uniform: StringName, value: Variant) -> void:
    pass;

