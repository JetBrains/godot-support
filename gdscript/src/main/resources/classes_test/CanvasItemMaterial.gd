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

var blend_mode: int;
var light_mode: int;
var particles_anim_h_frames: int;
var particles_anim_loop: bool;
var particles_anim_v_frames: int;
var particles_animation: bool;

