extends Control
class_name Range


var allow_greater: bool setget set_allow_greater, is_greater_allowed;
var allow_lesser: bool setget set_allow_lesser, is_lesser_allowed;
var exp_edit: bool setget set_exp_ratio, is_ratio_exp;
var max_value: float setget set_max, get_max;
var min_value: float setget set_min, get_min;
var page: float setget set_page, get_page;
var ratio: float setget set_as_ratio, get_as_ratio;
var rounded: bool setget set_use_rounded_values, is_using_rounded_values;
var step: float setget set_step, get_step;
var value: float setget set_value, get_value;

func share(with: Node) -> void:
    pass;

func unshare() -> void:
    pass;

