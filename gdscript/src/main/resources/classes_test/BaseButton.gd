#brief Base class for different kinds of buttons.
#desc BaseButton is the abstract base class for buttons, so it shouldn't be used directly (it doesn't display anything). Other types of buttons inherit from it.
class_name BaseButton

#desc The normal state (i.e. not pressed, not hovered, not toggled and enabled) of buttons.
const DRAW_NORMAL = 0;

#desc The state of buttons are pressed.
const DRAW_PRESSED = 1;

#desc The state of buttons are hovered.
const DRAW_HOVER = 2;

#desc The state of buttons are disabled.
const DRAW_DISABLED = 3;

#desc The state of buttons are both hovered and pressed.
const DRAW_HOVER_PRESSED = 4;

#desc Require just a press to consider the button clicked.
const ACTION_MODE_BUTTON_PRESS = 0;

#desc Require a press and a subsequent release before considering the button clicked.
const ACTION_MODE_BUTTON_RELEASE = 1;


#desc Determines when the button is considered clicked, one of the [enum ActionMode] constants.
var action_mode: int;

#desc The [ButtonGroup] associated with the button. Not to be confused with node groups.
var button_group: ButtonGroup;

#desc Binary mask to choose which mouse buttons this button will respond to.
#desc To allow both left-click and right-click, use [code]MOUSE_BUTTON_MASK_LEFT | MOUSE_BUTTON_MASK_RIGHT[/code].
var button_mask: int;

#desc If [code]true[/code], the button's state is pressed. Means the button is pressed down or toggled (if [member toggle_mode] is active). Only works if [member toggle_mode] is [code]true[/code].
#desc [b]Note:[/b] Setting [member button_pressed] will result in [signal toggled] to be emitted. If you want to change the pressed state without emitting that signal, use [method set_pressed_no_signal].
var button_pressed: bool;

#desc If [code]true[/code], the button is in disabled state and can't be clicked or toggled.
var disabled: bool;

#desc If [code]true[/code], the button stays pressed when moving the cursor outside the button while pressing it.
#desc [b]Note:[/b] This property only affects the button's visual appearance. Signals will be emitted at the same moment regardless of this property's value.
var keep_pressed_outside: bool;

#desc [Shortcut] associated to the button.
var shortcut: Shortcut;

#desc The [Node] which must be a parent of the focused GUI [Control] for the shortcut to be activated. If [code]null[/code], the shortcut can be activated when any control is focused (a global shortcut). This allows shortcuts to be accepted only when the user has a certain area of the GUI focused.
var shortcut_context: Node;

#desc If [code]true[/code], the button will add information about its shortcut in the tooltip.
var shortcut_in_tooltip: bool;

#desc If [code]true[/code], the button is in toggle mode. Makes the button flip state between pressed and unpressed each time its area is clicked.
var toggle_mode: bool;



#desc Called when the button is pressed. If you need to know the button's pressed state (and [member toggle_mode] is active), use [method _toggled] instead.
virtual func _pressed() -> void:
	pass;

#desc Called when the button is toggled (only if [member toggle_mode] is active).
virtual func _toggled() -> void:
	pass;

#desc Returns the visual state used to draw the button. This is useful mainly when implementing your own draw code by either overriding _draw() or connecting to "draw" signal. The visual state of the button is defined by the [enum DrawMode] enum.
func get_draw_mode() -> int:
	pass;

#desc Returns [code]true[/code] if the mouse has entered the button and has not left it yet.
func is_hovered() -> bool:
	pass;

#desc Changes the [member button_pressed] state of the button, without emitting [signal toggled]. Use when you just want to change the state of the button without sending the pressed event (e.g. when initializing scene). Only works if [member toggle_mode] is [code]true[/code].
#desc [b]Note:[/b] This method doesn't unpress other buttons in [member button_group].
func set_pressed_no_signal() -> void:
	pass;


