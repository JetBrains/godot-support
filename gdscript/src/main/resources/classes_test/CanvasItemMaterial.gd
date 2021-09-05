extends Material
class_name CanvasItemMaterial

const BLEND_MODE_MIX = 0;
const BLEND_MODE_ADD = 1;
const BLEND_MODE_SUB = 2;
const BLEND_MODE_MUL = 3;
const BLEND_MODE_PREMULT_ALPHA = 4;
const LIGHT_MODE_NORMAL = 0;
const LIGHT_MODE_UNSHADED = 1;
const LIGHT_MODE_LIGHT_ONLY = 2;

var blend_mode: int setget set_blend_mode, get_blend_mode;
var light_mode: int setget set_light_mode, get_light_mode;
var particles_anim_h_frames: int setget set_particles_anim_h_frames, get_particles_anim_h_frames;
var particles_anim_loop: bool setget set_particles_anim_loop, get_particles_anim_loop;
var particles_anim_v_frames: int setget set_particles_anim_v_frames, get_particles_anim_v_frames;
var particles_animation: bool setget set_particles_animation, get_particles_animation;

