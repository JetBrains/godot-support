extends AcceptDialog
class_name ConfirmationDialog


var min_size: Vector2i setget set_min_size, get_min_size;
var size: Vector2i setget set_size, get_size;
var title: String setget set_title, get_title;

func get_cancel_button() -> Button:
    pass;

