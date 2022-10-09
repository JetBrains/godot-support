extends InputEvent
#brief Input event for gamepad buttons.
#desc Input event type for gamepad buttons. For gamepad analog sticks and joysticks, see [InputEventJoypadMotion].
class_name InputEventJoypadButton


#desc Button identifier. One of the [enum JoyButton] button constants.
var button_index: int;

#desc If [code]true[/code], the button's state is pressed. If [code]false[/code], the button's state is released.
var pressed: bool;

#desc Represents the pressure the user puts on the button with their finger, if the controller supports it. Ranges from [code]0[/code] to [code]1[/code].
var pressure: float;




