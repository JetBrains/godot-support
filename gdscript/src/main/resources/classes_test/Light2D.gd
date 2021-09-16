extends Node2D
class_name Light2D
const SHADOW_FILTER_NONE = 0;
const SHADOW_FILTER_PCF5 = 1;
const SHADOW_FILTER_PCF13 = 2;
const BLEND_MODE_ADD = 0;
const BLEND_MODE_SUB = 1;
const BLEND_MODE_MIX = 2;

var blend_mode: int;
var color: Color;
var editor_only: bool;
var enabled: bool;
var energy: float;
var range_item_cull_mask: int;
var range_layer_max: int;
var range_layer_min: int;
var range_z_max: int;
var range_z_min: int;
var shadow_color: Color;
var shadow_enabled: bool;
var shadow_filter: int;
var shadow_filter_smooth: float;
var shadow_item_cull_mask: int;

func get_height() -> float:
    pass;
func set_height(height: float) -> void:
    pass;
