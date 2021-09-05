extends Window
class_name AcceptDialog


var dialog_autowrap: bool setget set_autowrap, has_autowrap;
var dialog_hide_on_ok: bool setget set_hide_on_ok, get_hide_on_ok;
var dialog_text: String setget set_text, get_text;
var exclusive: bool setget set_exclusive, is_exclusive;
var title: String setget set_title, get_title;
var transient: bool setget set_transient, is_transient;
var visible: bool setget set_visible, is_visible;
var wrap_controls: bool setget set_wrap_controls, is_wrapping_controls;

func add_button(text: String, right: bool, action: String) -> Button:
    pass;

func add_cancel_button(name: String) -> Button:
    pass;

func get_label() -> Label:
    pass;

func get_ok_button() -> Button:
    pass;

func register_text_enter(line_edit: Control) -> void:
    pass;

func remove_button(button: Control) -> void:
    pass;

