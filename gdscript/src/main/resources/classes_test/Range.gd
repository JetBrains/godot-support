extends Control
class_name Range

var allow_greater: bool;
var allow_lesser: bool;
var exp_edit: bool;
var max_value: float;
var min_value: float;
var page: float;
var ratio: float;
var rounded: bool;
var step: float;
var value: float;

func share(with: Node) -> void:
    pass;
func unshare() -> void:
    pass;
