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

var cull_mask: int setget set_cull_mask, get_cull_mask;
var current: bool setget set_current, is_current;
var doppler_tracking: int setget set_doppler_tracking, get_doppler_tracking;
var effects: CameraEffects setget set_effects, get_effects;
var environment: Environment setget set_environment, get_environment;
var far: float setget set_far, get_far;
var fov: float setget set_fov, get_fov;
var frustum_offset: Vector2 setget set_frustum_offset, get_frustum_offset;
var h_offset: float setget set_h_offset, get_h_offset;
var keep_aspect: int setget set_keep_aspect_mode, get_keep_aspect_mode;
var near: float setget set_near, get_near;
var projection: int setget set_projection, get_projection;
var size: float setget set_size, get_size;
var v_offset: float setget set_v_offset, get_v_offset;

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

