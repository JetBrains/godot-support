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

var angle: float setget set_param, get_param;
var angle_curve: Texture2D setget set_param_texture, get_param_texture;
var angle_random: float setget set_param_randomness, get_param_randomness;
var angular_velocity: float setget set_param, get_param;
var angular_velocity_curve: Texture2D setget set_param_texture, get_param_texture;
var angular_velocity_random: float setget set_param_randomness, get_param_randomness;
var anim_offset: float setget set_param, get_param;
var anim_offset_curve: Texture2D setget set_param_texture, get_param_texture;
var anim_offset_random: float setget set_param_randomness, get_param_randomness;
var anim_speed: float setget set_param, get_param;
var anim_speed_curve: Texture2D setget set_param_texture, get_param_texture;
var anim_speed_random: float setget set_param_randomness, get_param_randomness;
var attractor_interaction_enabled: bool setget set_attractor_interaction_enabled, is_attractor_interaction_enabled;
var collision_bounce: float setget set_collision_bounce, get_collision_bounce;
var collision_enabled: bool setget set_collision_enabled, is_collision_enabled;
var collision_friction: float setget set_collision_friction, get_collision_friction;
var collision_use_scale: bool setget set_collision_use_scale, is_collision_using_scale;
var color: Color setget set_color, get_color;
var color_ramp: Texture2D setget set_color_ramp, get_color_ramp;
var damping: float setget set_param, get_param;
var damping_curve: Texture2D setget set_param_texture, get_param_texture;
var damping_random: float setget set_param_randomness, get_param_randomness;
var direction: Vector3 setget set_direction, get_direction;
var emission_box_extents: Vector3 setget set_emission_box_extents, get_emission_box_extents;
var emission_color_texture: Texture2D setget set_emission_color_texture, get_emission_color_texture;
var emission_normal_texture: Texture2D setget set_emission_normal_texture, get_emission_normal_texture;
var emission_point_count: int setget set_emission_point_count, get_emission_point_count;
var emission_point_texture: Texture2D setget set_emission_point_texture, get_emission_point_texture;
var emission_shape: int setget set_emission_shape, get_emission_shape;
var emission_sphere_radius: float setget set_emission_sphere_radius, get_emission_sphere_radius;
var flatness: float setget set_flatness, get_flatness;
var gravity: Vector3 setget set_gravity, get_gravity;
var hue_variation: float setget set_param, get_param;
var hue_variation_curve: Texture2D setget set_param_texture, get_param_texture;
var hue_variation_random: float setget set_param_randomness, get_param_randomness;
var initial_velocity: float setget set_param, get_param;
var initial_velocity_random: float setget set_param_randomness, get_param_randomness;
var lifetime_randomness: float setget set_lifetime_randomness, get_lifetime_randomness;
var linear_accel: float setget set_param, get_param;
var linear_accel_curve: Texture2D setget set_param_texture, get_param_texture;
var linear_accel_random: float setget set_param_randomness, get_param_randomness;
var orbit_velocity: float setget set_param, get_param;
var orbit_velocity_curve: Texture2D setget set_param_texture, get_param_texture;
var orbit_velocity_random: float setget set_param_randomness, get_param_randomness;
var particle_flag_align_y: bool setget set_particle_flag, get_particle_flag;
var particle_flag_disable_z: bool setget set_particle_flag, get_particle_flag;
var particle_flag_rotate_y: bool setget set_particle_flag, get_particle_flag;
var radial_accel: float setget set_param, get_param;
var radial_accel_curve: Texture2D setget set_param_texture, get_param_texture;
var radial_accel_random: float setget set_param_randomness, get_param_randomness;
var scale: float setget set_param, get_param;
var scale_curve: Texture2D setget set_param_texture, get_param_texture;
var scale_random: float setget set_param_randomness, get_param_randomness;
var spread: float setget set_spread, get_spread;
var sub_emitter_amount_at_end: int setget set_sub_emitter_amount_at_end, get_sub_emitter_amount_at_end;
var sub_emitter_frequency: float setget set_sub_emitter_frequency, get_sub_emitter_frequency;
var sub_emitter_keep_velocity: bool setget set_sub_emitter_keep_velocity, get_sub_emitter_keep_velocity;
var sub_emitter_mode: int setget set_sub_emitter_mode, get_sub_emitter_mode;
var tangential_accel: float setget set_param, get_param;
var tangential_accel_curve: Texture2D setget set_param_texture, get_param_texture;
var tangential_accel_random: float setget set_param_randomness, get_param_randomness;

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

