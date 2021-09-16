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

var amount: int;
var collision_base_size: float;
var draw_order: int;
var draw_pass_1: Mesh;
var draw_pass_2: Mesh;
var draw_pass_3: Mesh;
var draw_pass_4: Mesh;
var draw_passes: int;
var draw_skin: Skin;
var emitting: bool;
var explosiveness: float;
var fixed_fps: int;
var fract_delta: bool;
var interpolate: bool;
var lifetime: float;
var local_coords: bool;
var one_shot: bool;
var preprocess: float;
var process_material: Material;
var randomness: float;
var speed_scale: float;
var sub_emitter: NodePath;
var trail_enabled: bool;
var trail_length_secs: float;
var transform_align: int;
var visibility_aabb: AABB;

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
