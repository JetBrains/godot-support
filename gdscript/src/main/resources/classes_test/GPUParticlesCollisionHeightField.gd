extends GPUParticlesCollision3D
class_name GPUParticlesCollisionHeightField

const RESOLUTION_256 = 0;
const RESOLUTION_512 = 1;
const RESOLUTION_1024 = 2;
const RESOLUTION_2048 = 3;
const RESOLUTION_4096 = 4;
const RESOLUTION_8192 = 5;
const RESOLUTION_MAX = 6;
const UPDATE_MODE_WHEN_MOVED = 0;
const UPDATE_MODE_ALWAYS = 1;

var extents: Vector3 setget set_extents, get_extents;
var follow_camera_enabled: bool setget set_follow_camera_mode, is_follow_camera_mode_enabled;
var follow_camera_push_ratio: float setget set_follow_camera_push_ratio, get_follow_camera_push_ratio;
var resolution: int setget set_resolution, get_resolution;
var update_mode: int setget set_update_mode, get_update_mode;

