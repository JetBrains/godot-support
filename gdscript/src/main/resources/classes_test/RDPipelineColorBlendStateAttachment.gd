extends RefCounted
class_name RDPipelineColorBlendStateAttachment


var alpha_blend_op: int setget set_alpha_blend_op, get_alpha_blend_op;
var color_blend_op: int setget set_color_blend_op, get_color_blend_op;
var dst_alpha_blend_factor: int setget set_dst_alpha_blend_factor, get_dst_alpha_blend_factor;
var dst_color_blend_factor: int setget set_dst_color_blend_factor, get_dst_color_blend_factor;
var enable_blend: bool setget set_enable_blend, get_enable_blend;
var src_alpha_blend_factor: int setget set_src_alpha_blend_factor, get_src_alpha_blend_factor;
var src_color_blend_factor: int setget set_src_color_blend_factor, get_src_color_blend_factor;
var write_a: bool setget set_write_a, get_write_a;
var write_b: bool setget set_write_b, get_write_b;
var write_g: bool setget set_write_g, get_write_g;
var write_r: bool setget set_write_r, get_write_r;

func set_as_mix() -> void:
    pass;

