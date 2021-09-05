extends Resource
class_name Shortcut


var shortcut: InputEvent setget set_shortcut, get_shortcut;

func get_as_text() -> String:
    pass;

func is_shortcut(event: InputEvent) -> bool:
    pass;

func is_valid() -> bool:
    pass;

