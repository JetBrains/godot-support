extends GeometryInstance3D
class_name CPUParticles3D

const DRAW_ORDER_INDEX = 0;
const DRAW_ORDER_LIFETIME = 1;
const DRAW_ORDER_VIEW_DEPTH = 2;
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

var amount: int setget set_amount, get_amount;
var angle: float setget set_param, get_param;
var angle_curve: Curve setget set_param_curve, get_param_curve;
var angle_random: float setget set_param_randomness, get_param_randomness;
var angular_velocity: float setget set_param, get_param;
var angular_velocity_curve: Curve setget set_param_curve, get_param_curve;
var angular_velocity_random: float setget set_param_randomness, get_param_randomness;
var anim_offset: float setget set_param, get_param;
var anim_offset_curve: Curve setget set_param_curve, get_param_curve;
var anim_offset_random: float setget set_param_randomness, get_param_randomness;
var anim_speed: float setget set_param, get_param;
var anim_speed_curve: Curve setget set_param_curve, get_param_curve;
var anim_speed_random: float setget set_param_randomness, get_param_randomness;
var color: Color setget set_color, get_color;
var color_ramp: Gradient setget set_color_ramp, get_color_ramp;
var damping: float setget set_param, get_param;
var damping_curve: Curve setget set_param_curve, get_param_curve;
var damping_random: float setget set_param_randomness, get_param_randomness;
var direction: Vector3 setget set_direction, get_direction;
var draw_order: int setget set_draw_order, get_draw_order;
var emission_box_extents: Vector3 setget set_emission_box_extents, get_emission_box_extents;
var emission_colors: PackedColorArray setget set_emission_colors, get_emission_colors;
var emission_normals: PackedVector3Array setget set_emission_normals, get_emission_normals;
var emission_points: PackedVector3Array setget set_emission_points, get_emission_points;
var emission_shape: int setget set_emission_shape, get_emission_shape;
var emission_sphere_radius: float setget set_emission_sphere_radius, get_emission_sphere_radius;
var emitting: bool setget set_emitting, is_emitting;
var explosiveness: float setget set_explosiveness_ratio, get_explosiveness_ratio;
var fixed_fps: int setget set_fixed_fps, get_fixed_fps;
var flatness: float setget set_flatness, get_flatness;
var fract_delta: bool setget set_fractional_delta, get_fractional_delta;
var gravity: Vector3 setget set_gravity, get_gravity;
var hue_variation: float setget set_param, get_param;
var hue_variation_curve: Curve setget set_param_curve, get_param_curve;
var hue_variation_random: float setget set_param_randomness, get_param_randomness;
var initial_velocity: float setget set_param, get_param;
var initial_velocity_random: float setget set_param_randomness, get_param_randomness;
var lifetime: float setget set_lifetime, get_lifetime;
var lifetime_randomness: float setget set_lifetime_randomness, get_lifetime_randomness;
var linear_accel: float setget set_param, get_param;
var linear_accel_curve: Curve setget set_param_curve, get_param_curve;
var linear_accel_random: float setget set_param_randomness, get_param_randomness;
var local_coords: bool setget set_use_local_coordinates, get_use_local_coordinates;
var mesh: Mesh setget set_mesh, get_mesh;
var one_shot: bool setget set_one_shot, get_one_shot;
var orbit_velocity: float setget set_param, get_param;
var orbit_velocity_curve: Curve setget set_param_curve, get_param_curve;
var orbit_velocity_random: float setget set_param_randomness, get_param_randomness;
var particle_flag_align_y: bool setget set_particle_flag, get_particle_flag;
var particle_flag_disable_z: bool setget set_particle_flag, get_particle_flag;
var particle_flag_rotate_y: bool setget set_particle_flag, get_particle_flag;
var preprocess: float setget set_pre_process_time, get_pre_process_time;
var radial_accel: float setget set_param, get_param;
var radial_accel_curve: Curve setget set_param_curve, get_param_curve;
var radial_accel_random: float setget set_param_randomness, get_param_randomness;
var randomness: float setget set_randomness_ratio, get_randomness_ratio;
var scale_amount: float setget set_param, get_param;
var scale_amount_curve: Curve setget set_param_curve, get_param_curve;
var scale_amount_random: float setget set_param_randomness, get_param_randomness;
var speed_scale: float setget set_speed_scale, get_speed_scale;
var spread: float setget set_spread, get_spread;
var tangential_accel: float setget set_param, get_param;
var tangential_accel_curve: Curve setget set_param_curve, get_param_curve;
var tangential_accel_random: float setget set_param_randomness, get_param_randomness;

func convert_from_particles(particles: Node) -> void:
    pass;

func get_param(param: int) -> float:
    pass;

func get_param_curve(param: int) -> Curve:
    pass;

func get_param_randomness(param: int) -> float:
    pass;

func get_particle_flag(particle_flag: int) -> bool:
    pass;

func restart() -> void:
    pass;

func set_param(param: int, value: float) -> void:
    pass;

func set_param_curve(param: int, curve: Curve) -> void:
    pass;

func set_param_randomness(param: int, randomness: float) -> void:
    pass;

func set_particle_flag(particle_flag: int, enable: bool) -> void:
    pass;

