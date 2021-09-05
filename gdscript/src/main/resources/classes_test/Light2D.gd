extends Node2D
class_name Light2D

const SHADOW_FILTER_NONE = 0;
const SHADOW_FILTER_PCF5 = 1;
const SHADOW_FILTER_PCF13 = 2;
const BLEND_MODE_ADD = 0;
const BLEND_MODE_SUB = 1;
const BLEND_MODE_MIX = 2;

var blend_mode: int setget set_blend_mode, get_blend_mode;
var color: Color setget set_color, get_color;
var editor_only: bool setget set_editor_only, is_editor_only;
var enabled: bool setget set_enabled, is_enabled;
var energy: float setget set_energy, get_energy;
var range_item_cull_mask: int setget set_item_cull_mask, get_item_cull_mask;
var range_layer_max: int setget set_layer_range_max, get_layer_range_max;
var range_layer_min: int setget set_layer_range_min, get_layer_range_min;
var range_z_max: int setget set_z_range_max, get_z_range_max;
var range_z_min: int setget set_z_range_min, get_z_range_min;
var shadow_color: Color setget set_shadow_color, get_shadow_color;
var shadow_enabled: bool setget set_shadow_enabled, is_shadow_enabled;
var shadow_filter: int setget set_shadow_filter, get_shadow_filter;
var shadow_filter_smooth: float setget set_shadow_smooth, get_shadow_smooth;
var shadow_item_cull_mask: int setget set_item_shadow_cull_mask, get_item_shadow_cull_mask;

func get_height() -> float:
    pass;

func set_height(height: float) -> void:
    pass;

