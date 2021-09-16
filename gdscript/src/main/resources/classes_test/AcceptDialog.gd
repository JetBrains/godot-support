extends Window
class_name AcceptDialog

var dialog_autowrap: bool;
var dialog_hide_on_ok: bool;
var dialog_text: String;
var exclusive: bool;
var title: String;
var transient: bool;
var visible: bool;
var wrap_controls: bool;

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
