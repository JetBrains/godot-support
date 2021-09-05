extends Control
class_name Container

const NOTIFICATION_SORT_CHILDREN = 50;

var mouse_filter: int setget set_mouse_filter, get_mouse_filter;

func fit_child_in_rect(child: Control, rect: Rect2) -> void:
    pass;

func queue_sort() -> void:
    pass;

