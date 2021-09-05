extends Range
class_name SpinBox


var align: int setget set_align, get_align;
var editable: bool setget set_editable, is_editable;
var prefix: String setget set_prefix, get_prefix;
var suffix: String setget set_suffix, get_suffix;

func apply() -> void:
    pass;

func get_line_edit() -> LineEdit:
    pass;

