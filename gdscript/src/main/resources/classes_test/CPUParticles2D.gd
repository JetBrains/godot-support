extends Node2D
class_name CPUParticles2D
const DRAW_ORDER_INDEX = 0;
const DRAW_ORDER_LIFETIME = 1;
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
const EMISSION_SHAPE_RECTANGLE = 2;
const EMISSION_SHAPE_POINTS = 3;
const EMISSION_SHAPE_DIRECTED_POINTS = 4;
const EMISSION_SHAPE_MAX = 5;

var amount: int;
var angle: float;
var angle_curve: Curve;
var angle_random: float;
var angular_velocity: float;
var angular_velocity_curve: Curve;
var angular_velocity_random: float;
var anim_offset: float;
var anim_offset_curve: Curve;
var anim_offset_random: float;
var anim_speed: float;
var anim_speed_curve: Curve;
var anim_speed_random: float;
var color: Color;
var color_ramp: Gradient;
var damping: float;
var damping_curve: Curve;
var damping_random: float;
var direction: Vector2;
var draw_order: int;
var emission_colors: PackedColorArray;
var emission_normals: PackedVector2Array;
var emission_points: PackedVector2Array;
var emission_rect_extents: Vector2;
var emission_shape: int;
var emission_sphere_radius: float;
var emitting: bool;
var explosiveness: float;
var fixed_fps: int;
var fract_delta: bool;
var gravity: Vector2;
var hue_variation: float;
var hue_variation_curve: Curve;
var hue_variation_random: float;
var initial_velocity: float;
var initial_velocity_random: float;
var lifetime: float;
var lifetime_randomness: float;
var linear_accel: float;
var linear_accel_curve: Curve;
var linear_accel_random: float;
var local_coords: bool;
var one_shot: bool;
var orbit_velocity: float;
var orbit_velocity_curve: Curve;
var orbit_velocity_random: float;
var particle_flag_align_y: bool;
var preprocess: float;
var radial_accel: float;
var radial_accel_curve: Curve;
var radial_accel_random: float;
var randomness: float;
var scale_amount: float;
var scale_amount_curve: Curve;
var scale_amount_random: float;
var speed_scale: float;
var spread: float;
var tangential_accel: float;
var tangential_accel_curve: Curve;
var tangential_accel_random: float;
var texture: Texture2D;

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
