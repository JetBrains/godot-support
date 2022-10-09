extends InputEventWithModifiers
#brief Input event type for keyboard events.
#desc Stores key presses on the keyboard. Supports key presses, key releases and [member echo] events.
class_name InputEventKey


#desc If [code]true[/code], the key was already pressed before this event. It means the user is holding the key down.
var echo: bool;

#desc The key keycode, which corresponds to one of the [enum Key] constants. Represent key in the current keyboard layout.
#desc To get a human-readable representation of the [InputEventKey], use [code]OS.get_keycode_string(event.keycode)[/code] where [code]event[/code] is the [InputEventKey].
var keycode: int;

#desc Key physical keycode, which corresponds to one of the [enum Key] constants. Represent the physical location of a key on the 101/102-key US QWERTY keyboard.
#desc To get a human-readable representation of the [InputEventKey], use [code]OS.get_keycode_string(event.keycode)[/code] where [code]event[/code] is the [InputEventKey].
var physical_keycode: int;

#desc If [code]true[/code], the key's state is pressed. If [code]false[/code], the key's state is released.
var pressed: bool;

#desc The key Unicode identifier (when relevant). Unicode identifiers for the composite characters and complex scripts may not be available unless IME input mode is active. See [method Window.set_ime_active] for more information.
var unicode: int;



#desc Returns the keycode combined with modifier keys such as [kbd]Shift[/kbd] or [kbd]Alt[/kbd]. See also [InputEventWithModifiers].
#desc To get a human-readable representation of the [InputEventKey] with modifiers, use [code]OS.get_keycode_string(event.get_keycode_with_modifiers())[/code] where [code]event[/code] is the [InputEventKey].
func get_keycode_with_modifiers() -> int:
	pass;

#desc Returns the physical keycode combined with modifier keys such as [kbd]Shift[/kbd] or [kbd]Alt[/kbd]. See also [InputEventWithModifiers].
#desc To get a human-readable representation of the [InputEventKey] with modifiers, use [code]OS.get_keycode_string(event.get_physical_keycode_with_modifiers())[/code] where [code]event[/code] is the [InputEventKey].
func get_physical_keycode_with_modifiers() -> int:
	pass;


