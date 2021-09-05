extends GPUParticlesCollision3D
class_name GPUParticlesCollisionSDF

const RESOLUTION_16 = 0;
const RESOLUTION_32 = 1;
const RESOLUTION_64 = 2;
const RESOLUTION_128 = 3;
const RESOLUTION_256 = 4;
const RESOLUTION_512 = 5;
const RESOLUTION_MAX = 6;

var extents: Vector3 setget set_extents, get_extents;
var resolution: int setget set_resolution, get_resolution;
var texture: Texture3D setget set_texture, get_texture;
var thickness: float setget set_thickness, get_thickness;

