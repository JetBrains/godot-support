extends Light3D
class_name DirectionalLight3D

const SHADOW_ORTHOGONAL = 0;
const SHADOW_PARALLEL_2_SPLITS = 1;
const SHADOW_PARALLEL_4_SPLITS = 2;

var directional_shadow_blend_splits: bool setget set_blend_splits, is_blend_splits_enabled;
var directional_shadow_fade_start: float setget set_param, get_param;
var directional_shadow_max_distance: float setget set_param, get_param;
var directional_shadow_mode: int setget set_shadow_mode, get_shadow_mode;
var directional_shadow_pancake_size: float setget set_param, get_param;
var directional_shadow_split_1: float setget set_param, get_param;
var directional_shadow_split_2: float setget set_param, get_param;
var directional_shadow_split_3: float setget set_param, get_param;
var shadow_bias: float setget set_param, get_param;
var shadow_normal_bias: float setget set_param, get_param;
var use_in_sky_only: bool setget set_sky_only, is_sky_only;

