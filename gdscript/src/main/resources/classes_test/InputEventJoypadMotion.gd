#brief Input event type for gamepad joysticks and other motions. For buttons, see [code]InputEventJoypadButton[/code].
#desc Stores information about joystick motions. One [InputEventJoypadMotion] represents one axis at a time.
class_name InputEventJoypadMotion


#desc Axis identifier. Use one of the [enum JoyAxis] axis constants.
var axis: int;

#desc Current position of the joystick on the given axis. The value ranges from [code]-1.0[/code] to [code]1.0[/code]. A value of [code]0[/code] means the axis is in its resting position.
var axis_value: float;




