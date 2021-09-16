extends Node3D
class_name Camera3D
const PROJECTION_PERSPECTIVE = 0;
const PROJECTION_ORTHOGONAL = 1;
const PROJECTION_FRUSTUM = 2;
const KEEP_WIDTH = 0;
const KEEP_HEIGHT = 1;
const DOPPLER_TRACKING_DISABLED = 0;
const DOPPLER_TRACKING_IDLE_STEP = 1;
const DOPPLER_TRACKING_PHYSICS_STEP = 2;

var cull_mask: int;
var current: bool;
var doppler_tracking: int;
var effects: CameraEffects;
var environment: Environment;
var far: float;
var fov: float;
var frustum_offset: Vector2;
var h_offset: float;
var keep_aspect: int;
var near: float;
var projection: int;
var size: float;
var v_offset: float;

func clear_current(enable_next: bool) -> void:
    pass;
func get_camera_rid() -> RID:
    pass;
func get_camera_transform() -> Transform3D:
    pass;
func get_cull_mask_bit(layer: int) -> bool:
    pass;
func get_frustum() -> Array:
    pass;
func is_position_behind(world_point: Vector3) -> bool:
    pass;
func is_position_in_frustum(world_point: Vector3) -> bool:
    pass;
func make_current() -> void:
    pass;
func project_local_ray_normal(screen_point: Vector2) -> Vector3:
    pass;
func project_position(screen_point: Vector2, z_depth: float) -> Vector3:
    pass;
func project_ray_normal(screen_point: Vector2) -> Vector3:
    pass;
func project_ray_origin(screen_point: Vector2) -> Vector3:
    pass;
func set_cull_mask_bit(layer: int, enable: bool) -> void:
    pass;
func set_frustum(size: float, offset: Vector2, z_near: float, z_far: float) -> void:
    pass;
func set_orthogonal(size: float, z_near: float, z_far: float) -> void:
    pass;
func set_perspective(fov: float, z_near: float, z_far: float) -> void:
    pass;
func unproject_position(world_point: Vector3) -> Vector2:
    pass;
