extends Node2D
class_name GPUParticles2D
const DRAW_ORDER_INDEX = 0;
const DRAW_ORDER_LIFETIME = 1;
const DRAW_ORDER_REVERSE_LIFETIME = 2;

var amount: int;
var collision_base_size: float;
var draw_order: int;
var emitting: bool;
var explosiveness: float;
var fixed_fps: int;
var fract_delta: bool;
var lifetime: float;
var local_coords: bool;
var one_shot: bool;
var preprocess: float;
var process_material: Material;
var randomness: float;
var speed_scale: float;
var texture: Texture2D;
var trail_enabled: bool;
var trail_length_secs: float;
var trail_section_subdivisions: int;
var trail_sections: int;
var visibility_rect: Rect2;

func capture_rect() -> Rect2:
    pass;
func restart() -> void:
    pass;
