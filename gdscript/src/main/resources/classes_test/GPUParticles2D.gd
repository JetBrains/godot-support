extends Node2D
class_name GPUParticles2D

const DRAW_ORDER_INDEX = 0;
const DRAW_ORDER_LIFETIME = 1;
const DRAW_ORDER_REVERSE_LIFETIME = 2;

var amount: int setget set_amount, get_amount;
var collision_base_size: float setget set_collision_base_size, get_collision_base_size;
var draw_order: int setget set_draw_order, get_draw_order;
var emitting: bool setget set_emitting, is_emitting;
var explosiveness: float setget set_explosiveness_ratio, get_explosiveness_ratio;
var fixed_fps: int setget set_fixed_fps, get_fixed_fps;
var fract_delta: bool setget set_fractional_delta, get_fractional_delta;
var lifetime: float setget set_lifetime, get_lifetime;
var local_coords: bool setget set_use_local_coordinates, get_use_local_coordinates;
var one_shot: bool setget set_one_shot, get_one_shot;
var preprocess: float setget set_pre_process_time, get_pre_process_time;
var process_material: Material setget set_process_material, get_process_material;
var randomness: float setget set_randomness_ratio, get_randomness_ratio;
var speed_scale: float setget set_speed_scale, get_speed_scale;
var texture: Texture2D setget set_texture, get_texture;
var trail_enabled: bool setget set_trail_enabled, is_trail_enabled;
var trail_length_secs: float setget set_trail_length, get_trail_length;
var trail_section_subdivisions: int setget set_trail_section_subdivisions, get_trail_section_subdivisions;
var trail_sections: int setget set_trail_sections, get_trail_sections;
var visibility_rect: Rect2 setget set_visibility_rect, get_visibility_rect;

func capture_rect() -> Rect2:
    pass;

func restart() -> void:
    pass;

