extends Light3D
class_name DirectionalLight3D
const SHADOW_ORTHOGONAL = 0;
const SHADOW_PARALLEL_2_SPLITS = 1;
const SHADOW_PARALLEL_4_SPLITS = 2;

var directional_shadow_blend_splits: bool;
var directional_shadow_fade_start: float;
var directional_shadow_max_distance: float;
var directional_shadow_mode: int;
var directional_shadow_pancake_size: float;
var directional_shadow_split_1: float;
var directional_shadow_split_2: float;
var directional_shadow_split_3: float;
var shadow_bias: float;
var shadow_normal_bias: float;
var use_in_sky_only: bool;

