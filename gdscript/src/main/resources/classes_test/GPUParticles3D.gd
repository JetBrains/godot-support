extends GeometryInstance3D
class_name GPUParticles3D

const DRAW_ORDER_INDEX = 0;
const DRAW_ORDER_LIFETIME = 1;
const DRAW_ORDER_REVERSE_LIFETIME = 2;
const DRAW_ORDER_VIEW_DEPTH = 3;
const EMIT_FLAG_POSITION = 1;
const EMIT_FLAG_ROTATION_SCALE = 2;
const EMIT_FLAG_VELOCITY = 4;
const EMIT_FLAG_COLOR = 8;
const EMIT_FLAG_CUSTOM = 16;
const MAX_DRAW_PASSES = 4;
const TRANSFORM_ALIGN_DISABLED = 0;
const TRANSFORM_ALIGN_Z_BILLBOARD = 1;
const TRANSFORM_ALIGN_Y_TO_VELOCITY = 2;
const TRANSFORM_ALIGN_Z_BILLBOARD_Y_TO_VELOCITY = 3;

var amount: int setget set_amount, get_amount;
var collision_base_size: float setget set_collision_base_size, get_collision_base_size;
var draw_order: int setget set_draw_order, get_draw_order;
var draw_pass_1: Mesh setget set_draw_pass_mesh, get_draw_pass_mesh;
var draw_pass_2: Mesh setget set_draw_pass_mesh, get_draw_pass_mesh;
var draw_pass_3: Mesh setget set_draw_pass_mesh, get_draw_pass_mesh;
var draw_pass_4: Mesh setget set_draw_pass_mesh, get_draw_pass_mesh;
var draw_passes: int setget set_draw_passes, get_draw_passes;
var draw_skin: Skin setget set_skin, get_skin;
var emitting: bool setget set_emitting, is_emitting;
var explosiveness: float setget set_explosiveness_ratio, get_explosiveness_ratio;
var fixed_fps: int setget set_fixed_fps, get_fixed_fps;
var fract_delta: bool setget set_fractional_delta, get_fractional_delta;
var interpolate: bool setget set_interpolate, get_interpolate;
var lifetime: float setget set_lifetime, get_lifetime;
var local_coords: bool setget set_use_local_coordinates, get_use_local_coordinates;
var one_shot: bool setget set_one_shot, get_one_shot;
var preprocess: float setget set_pre_process_time, get_pre_process_time;
var process_material: Material setget set_process_material, get_process_material;
var randomness: float setget set_randomness_ratio, get_randomness_ratio;
var speed_scale: float setget set_speed_scale, get_speed_scale;
var sub_emitter: NodePath setget set_sub_emitter, get_sub_emitter;
var trail_enabled: bool setget set_trail_enabled, is_trail_enabled;
var trail_length_secs: float setget set_trail_length, get_trail_length;
var transform_align: int setget set_transform_align, get_transform_align;
var visibility_aabb: AABB setget set_visibility_aabb, get_visibility_aabb;

func capture_aabb() -> AABB:
    pass;

func emit_particle(xform: Transform3D, velocity: Vector3, color: Color, custom: Color, flags: int) -> void:
    pass;

func get_draw_pass_mesh(pass: int) -> Mesh:
    pass;

func restart() -> void:
    pass;

func set_draw_pass_mesh(pass: int, mesh: Mesh) -> void:
    pass;

