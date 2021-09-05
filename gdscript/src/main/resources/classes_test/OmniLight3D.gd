extends Light3D
class_name OmniLight3D

const SHADOW_DUAL_PARABOLOID = 0;
const SHADOW_CUBE = 1;

var omni_attenuation: float setget set_param, get_param;
var omni_range: float setget set_param, get_param;
var omni_shadow_mode: int setget set_shadow_mode, get_shadow_mode;

