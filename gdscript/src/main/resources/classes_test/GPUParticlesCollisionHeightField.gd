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

var extents: Vector3;
var follow_camera_enabled: bool;
var follow_camera_push_ratio: float;
var resolution: int;
var update_mode: int;

