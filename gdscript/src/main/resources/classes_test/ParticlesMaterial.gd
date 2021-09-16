extends Material
class_name ParticlesMaterial
const PARAM_INITIAL_LINEAR_VELOCITY = 0;
const PARAM_ANGULAR_VELOCITY = 1;
const PARAM_ORBIT_VELOCITY = 2;
const PARAM_LINEAR_ACCEL = 3;
const PARAM_RADIAL_ACCEL = 4;
const PARAM_TANGENTIAL_ACCEL = 5;
const PARAM_DAMPING = 6;
const PARAM_ANGLE = 7;
const PARAM_SCALE = 8;
const PARAM_HUE_VARIATION = 9;
const PARAM_ANIM_SPEED = 10;
const PARAM_ANIM_OFFSET = 11;
const PARAM_MAX = 12;
const PARTICLE_FLAG_ALIGN_Y_TO_VELOCITY = 0;
const PARTICLE_FLAG_ROTATE_Y = 1;
const PARTICLE_FLAG_DISABLE_Z = 2;
const PARTICLE_FLAG_MAX = 3;
const EMISSION_SHAPE_POINT = 0;
const EMISSION_SHAPE_SPHERE = 1;
const EMISSION_SHAPE_BOX = 2;
const EMISSION_SHAPE_POINTS = 3;
const EMISSION_SHAPE_DIRECTED_POINTS = 4;
const EMISSION_SHAPE_MAX = 5;
const SUB_EMITTER_DISABLED = 0;
const SUB_EMITTER_CONSTANT = 1;
const SUB_EMITTER_AT_END = 2;
const SUB_EMITTER_AT_COLLISION = 3;
const SUB_EMITTER_MAX = 4;

var angle: float;
var angle_curve: Texture2D;
var angle_random: float;
var angular_velocity: float;
var angular_velocity_curve: Texture2D;
var angular_velocity_random: float;
var anim_offset: float;
var anim_offset_curve: Texture2D;
var anim_offset_random: float;
var anim_speed: float;
var anim_speed_curve: Texture2D;
var anim_speed_random: float;
var attractor_interaction_enabled: bool;
var collision_bounce: float;
var collision_enabled: bool;
var collision_friction: float;
var collision_use_scale: bool;
var color: Color;
var color_ramp: Texture2D;
var damping: float;
var damping_curve: Texture2D;
var damping_random: float;
var direction: Vector3;
var emission_box_extents: Vector3;
var emission_color_texture: Texture2D;
var emission_normal_texture: Texture2D;
var emission_point_count: int;
var emission_point_texture: Texture2D;
var emission_shape: int;
var emission_sphere_radius: float;
var flatness: float;
var gravity: Vector3;
var hue_variation: float;
var hue_variation_curve: Texture2D;
var hue_variation_random: float;
var initial_velocity: float;
var initial_velocity_random: float;
var lifetime_randomness: float;
var linear_accel: float;
var linear_accel_curve: Texture2D;
var linear_accel_random: float;
var orbit_velocity: float;
var orbit_velocity_curve: Texture2D;
var orbit_velocity_random: float;
var particle_flag_align_y: bool;
var particle_flag_disable_z: bool;
var particle_flag_rotate_y: bool;
var radial_accel: float;
var radial_accel_curve: Texture2D;
var radial_accel_random: float;
var scale: float;
var scale_curve: Texture2D;
var scale_random: float;
var spread: float;
var sub_emitter_amount_at_end: int;
var sub_emitter_frequency: float;
var sub_emitter_keep_velocity: bool;
var sub_emitter_mode: int;
var tangential_accel: float;
var tangential_accel_curve: Texture2D;
var tangential_accel_random: float;

func get_param(param: int) -> float:
    pass;
func get_param_randomness(param: int) -> float:
    pass;
func get_param_texture(param: int) -> Texture2D:
    pass;
func get_particle_flag(particle_flag: int) -> bool:
    pass;
func set_param(param: int, value: float) -> void:
    pass;
func set_param_randomness(param: int, randomness: float) -> void:
    pass;
func set_param_texture(param: int, texture: Texture2D) -> void:
    pass;
func set_particle_flag(particle_flag: int, enable: bool) -> void:
    pass;
