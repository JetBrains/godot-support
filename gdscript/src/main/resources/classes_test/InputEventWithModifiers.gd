#brief Base class for keys events with modifiers.
#desc Contains keys events information with modifiers support like [kbd]Shift[/kbd] or [kbd]Alt[/kbd]. See [method Node._input].
class_name InputEventWithModifiers


#desc State of the [kbd]Alt[/kbd] modifier.
var alt_pressed: bool;

#desc Automatically use [kbd]Meta[/kbd] ([kbd]Command[/kbd]) on macOS and [kbd]Ctrl[/kbd] on other platforms. If [code]true[/code], [member ctrl_pressed] and [member meta_pressed] cannot be set.
var command_or_control_autoremap: bool;

#desc State of the [kbd]Ctrl[/kbd] modifier.
var ctrl_pressed: bool;

#desc State of the [kbd]Meta[/kbd] modifier. On Windows and Linux, this represents the Windows key (sometimes called "meta" or "super" on Linux). On macOS, this represents the Command key.
var meta_pressed: bool;

#desc State of the [kbd]Shift[/kbd] modifier.
var shift_pressed: bool;



#desc On macOS, returns [code]true[/code] if [kbd]Meta[/kbd] ([kbd]Command[/kbd]) is pressed.
#desc On other platforms, returns [code]true[/code] if [kbd]Ctrl[/kbd] is pressed.
func is_command_or_control_pressed() -> bool:
	pass;


