extends VisualInstance3D
class_name Light3D

const PARAM_ENERGY = 0;
const PARAM_INDIRECT_ENERGY = 1;
const PARAM_SPECULAR = 2;
const PARAM_RANGE = 3;
const PARAM_SIZE = 4;
const PARAM_ATTENUATION = 5;
const PARAM_SPOT_ANGLE = 6;
const PARAM_SPOT_ATTENUATION = 7;
const PARAM_SHADOW_MAX_DISTANCE = 8;
const PARAM_SHADOW_SPLIT_1_OFFSET = 9;
const PARAM_SHADOW_SPLIT_2_OFFSET = 10;
const PARAM_SHADOW_SPLIT_3_OFFSET = 11;
const PARAM_SHADOW_FADE_START = 12;
const PARAM_SHADOW_NORMAL_BIAS = 13;
const PARAM_SHADOW_BIAS = 14;
const PARAM_SHADOW_PANCAKE_SIZE = 15;
const PARAM_SHADOW_BLUR = 16;
const PARAM_SHADOW_VOLUMETRIC_FOG_FADE = 17;
const PARAM_TRANSMITTANCE_BIAS = 18;
const PARAM_MAX = 19;
const BAKE_DISABLED = 0;
const BAKE_DYNAMIC = 1;
const BAKE_STATIC = 2;

var editor_only: bool setget set_editor_only, is_editor_only;
var light_angular_distance: float setget set_param, get_param;
var light_bake_mode: int setget set_bake_mode, get_bake_mode;
var light_color: Color setget set_color, get_color;
var light_cull_mask: int setget set_cull_mask, get_cull_mask;
var light_energy: float setget set_param, get_param;
var light_indirect_energy: float setget set_param, get_param;
var light_negative: bool setget set_negative, is_negative;
var light_projector: Texture2D setget set_projector, get_projector;
var light_size: float setget set_param, get_param;
var light_specular: float setget set_param, get_param;
var shadow_bias: float setget set_param, get_param;
var shadow_blur: float setget set_param, get_param;
var shadow_color: Color setget set_shadow_color, get_shadow_color;
var shadow_enabled: bool setget set_shadow, has_shadow;
var shadow_fog_fade: float setget set_param, get_param;
var shadow_normal_bias: float setget set_param, get_param;
var shadow_reverse_cull_face: bool setget set_shadow_reverse_cull_face, get_shadow_reverse_cull_face;
var shadow_transmittance_bias: float setget set_param, get_param;

func get_param(param: int) -> float:
    pass;

func set_param(param: int, value: float) -> void:
    pass;

