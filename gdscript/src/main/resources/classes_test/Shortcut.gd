#brief A shortcut for binding input.
#desc Shortcuts are commonly used for interacting with a [Control] element from an [InputEvent] (also known as hotkeys).
#desc One shortcut can contain multiple [InputEvent]'s, allowing the possibility of triggering one action with multiple different inputs.
class_name Shortcut


#desc The shortcut's [InputEvent] array.
#desc Generally the [InputEvent] used is an [InputEventKey], though it can be any [InputEvent], including an [InputEventAction].
var events: Array;



#desc Returns the shortcut's first valid [InputEvent] as a [String].
func get_as_text() -> String:
	pass;

#desc Returns whether [member events] contains an [InputEvent] which is valid.
func has_valid_event() -> bool:
	pass;

#desc Returns whether any [InputEvent] in [member events] equals [param event].
func matches_event() -> bool:
	pass;


