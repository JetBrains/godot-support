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

var editor_only: bool;
var light_angular_distance: float;
var light_bake_mode: int;
var light_color: Color;
var light_cull_mask: int;
var light_energy: float;
var light_indirect_energy: float;
var light_negative: bool;
var light_projector: Texture2D;
var light_size: float;
var light_specular: float;
var shadow_bias: float;
var shadow_blur: float;
var shadow_color: Color;
var shadow_enabled: bool;
var shadow_fog_fade: float;
var shadow_normal_bias: float;
var shadow_reverse_cull_face: bool;
var shadow_transmittance_bias: float;

func get_param(param: int) -> float:
    pass;
func set_param(param: int, value: float) -> void:
    pass;
