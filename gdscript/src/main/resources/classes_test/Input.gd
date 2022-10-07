extends Object
#brief A singleton that deals with inputs.
#desc A singleton that deals with inputs. This includes key presses, mouse buttons and movement, joypads, and input actions. Actions and their events can be set in the [b]Input Map[/b] tab in the [b]Project > Project Settings[/b], or with the [InputMap] class.
class_name Input

#desc Makes the mouse cursor visible if it is hidden.
const MOUSE_MODE_VISIBLE = 0;

#desc Makes the mouse cursor hidden if it is visible.
const MOUSE_MODE_HIDDEN = 1;

#desc Captures the mouse. The mouse will be hidden and its position locked at the center of the screen.
#desc [b]Note:[/b] If you want to process the mouse's movement in this mode, you need to use [member InputEventMouseMotion.relative].
const MOUSE_MODE_CAPTURED = 2;

#desc Confines the mouse cursor to the game window, and make it visible.
const MOUSE_MODE_CONFINED = 3;

#desc Confines the mouse cursor to the game window, and make it hidden.
const MOUSE_MODE_CONFINED_HIDDEN = 4;

#desc Arrow cursor. Standard, default pointing cursor.
const CURSOR_ARROW = 0;

#desc I-beam cursor. Usually used to show where the text cursor will appear when the mouse is clicked.
const CURSOR_IBEAM = 1;

#desc Pointing hand cursor. Usually used to indicate the pointer is over a link or other interactable item.
const CURSOR_POINTING_HAND = 2;

#desc Cross cursor. Typically appears over regions in which a drawing operation can be performed or for selections.
const CURSOR_CROSS = 3;

#desc Wait cursor. Indicates that the application is busy performing an operation. This cursor shape denotes that the application isn't usable during the operation (e.g. something is blocking its main thread).
const CURSOR_WAIT = 4;

#desc Busy cursor. Indicates that the application is busy performing an operation. This cursor shape denotes that the application is still usable during the operation.
const CURSOR_BUSY = 5;

#desc Drag cursor. Usually displayed when dragging something.
#desc [b]Note:[/b] Windows lacks a dragging cursor, so [constant CURSOR_DRAG] is the same as [constant CURSOR_MOVE] for this platform.
const CURSOR_DRAG = 6;

#desc Can drop cursor. Usually displayed when dragging something to indicate that it can be dropped at the current position.
const CURSOR_CAN_DROP = 7;

#desc Forbidden cursor. Indicates that the current action is forbidden (for example, when dragging something) or that the control at a position is disabled.
const CURSOR_FORBIDDEN = 8;

#desc Vertical resize mouse cursor. A double-headed vertical arrow. It tells the user they can resize the window or the panel vertically.
const CURSOR_VSIZE = 9;

#desc Horizontal resize mouse cursor. A double-headed horizontal arrow. It tells the user they can resize the window or the panel horizontally.
const CURSOR_HSIZE = 10;

#desc Window resize mouse cursor. The cursor is a double-headed arrow that goes from the bottom left to the top right. It tells the user they can resize the window or the panel both horizontally and vertically.
const CURSOR_BDIAGSIZE = 11;

#desc Window resize mouse cursor. The cursor is a double-headed arrow that goes from the top left to the bottom right, the opposite of [constant CURSOR_BDIAGSIZE]. It tells the user they can resize the window or the panel both horizontally and vertically.
const CURSOR_FDIAGSIZE = 12;

#desc Move cursor. Indicates that something can be moved.
const CURSOR_MOVE = 13;

#desc Vertical split mouse cursor. On Windows, it's the same as [constant CURSOR_VSIZE].
const CURSOR_VSPLIT = 14;

#desc Horizontal split mouse cursor. On Windows, it's the same as [constant CURSOR_HSIZE].
const CURSOR_HSPLIT = 15;

#desc Help cursor. Usually a question mark.
const CURSOR_HELP = 16;


#desc Controls the mouse mode. See [enum MouseMode] for more information.
var mouse_mode: int;

#desc If [code]true[/code], similar input events sent by the operating system are accumulated. When input accumulation is enabled, all input events generated during a frame will be merged and emitted when the frame is done rendering. Therefore, this limits the number of input method calls per second to the rendering FPS.
#desc Input accumulation can be disabled to get slightly more precise/reactive input at the cost of increased CPU usage. In applications where drawing freehand lines is required, input accumulation should generally be disabled while the user is drawing the line to get results that closely follow the actual input.
#desc [b]Note:[/b] Input accumulation is [i]enabled[/i] by default.
var use_accumulated_input: bool;



#desc This will simulate pressing the specified action.
#desc The strength can be used for non-boolean actions, it's ranged between 0 and 1 representing the intensity of the given action.
#desc [b]Note:[/b] This method will not cause any [method Node._input] calls. It is intended to be used with [method is_action_pressed] and [method is_action_just_pressed]. If you want to simulate [code]_input[/code], use [method parse_input_event] instead.
func action_press(action: StringName, strength: float) -> void:
	pass;

#desc If the specified action is already pressed, this will release it.
func action_release(action: StringName) -> void:
	pass;

#desc Adds a new mapping entry (in SDL2 format) to the mapping database. Optionally update already connected devices.
func add_joy_mapping(mapping: String, update_existing: bool) -> void:
	pass;

#desc Sends all input events which are in the current buffer to the game loop. These events may have been buffered as a result of accumulated input ([member use_accumulated_input]) or agile input flushing ([member ProjectSettings.input_devices/buffering/agile_event_flushing]).
#desc The engine will already do this itself at key execution points (at least once per frame). However, this can be useful in advanced cases where you want precise control over the timing of event handling.
func flush_buffered_events() -> void:
	pass;

#desc Returns the acceleration in m/s² of the device's accelerometer sensor, if the device has one. Otherwise, the method returns [constant Vector3.ZERO].
#desc Note this method returns an empty [Vector3] when running from the editor even when your device has an accelerometer. You must export your project to a supported device to read values from the accelerometer.
#desc [b]Note:[/b] This method only works on iOS, Android, and UWP. On other platforms, it always returns [constant Vector3.ZERO].
func get_accelerometer() -> Vector3:
	pass;

#desc Returns a value between 0 and 1 representing the raw intensity of the given action, ignoring the action's deadzone. In most cases, you should use [method get_action_strength] instead.
#desc If [param exact_match] is [code]false[/code], it ignores additional input modifiers for [InputEventKey] and [InputEventMouseButton] events, and the direction for [InputEventJoypadMotion] events.
func get_action_raw_strength(action: StringName, exact_match: bool) -> float:
	pass;

#desc Returns a value between 0 and 1 representing the intensity of the given action. In a joypad, for example, the further away the axis (analog sticks or L2, R2 triggers) is from the dead zone, the closer the value will be to 1. If the action is mapped to a control that has no axis as the keyboard, the value returned will be 0 or 1.
#desc If [param exact_match] is [code]false[/code], it ignores additional input modifiers for [InputEventKey] and [InputEventMouseButton] events, and the direction for [InputEventJoypadMotion] events.
func get_action_strength(action: StringName, exact_match: bool) -> float:
	pass;

#desc Get axis input by specifying two actions, one negative and one positive.
#desc This is a shorthand for writing [code]Input.get_action_strength("positive_action") - Input.get_action_strength("negative_action")[/code].
func get_axis(negative_action: StringName, positive_action: StringName) -> float:
	pass;

#desc Returns an [Array] containing the device IDs of all currently connected joypads.
func get_connected_joypads() -> Array[int]:
	pass;

#desc Returns the currently assigned cursor shape (see [enum CursorShape]).
func get_current_cursor_shape() -> int:
	pass;

#desc Returns the gravity in m/s² of the device's accelerometer sensor, if the device has one. Otherwise, the method returns [constant Vector3.ZERO].
#desc [b]Note:[/b] This method only works on Android and iOS. On other platforms, it always returns [constant Vector3.ZERO].
func get_gravity() -> Vector3:
	pass;

#desc Returns the rotation rate in rad/s around a device's X, Y, and Z axes of the gyroscope sensor, if the device has one. Otherwise, the method returns [constant Vector3.ZERO].
#desc [b]Note:[/b] This method only works on Android and iOS. On other platforms, it always returns [constant Vector3.ZERO].
func get_gyroscope() -> Vector3:
	pass;

#desc Returns the current value of the joypad axis at given index (see [enum JoyAxis]).
func get_joy_axis(device: int, axis: int) -> float:
	pass;

#desc Returns a SDL2-compatible device GUID on platforms that use gamepad remapping. Returns [code]"Default Gamepad"[/code] otherwise.
func get_joy_guid(device: int) -> String:
	pass;

#desc Returns the name of the joypad at the specified device index.
func get_joy_name(device: int) -> String:
	pass;

#desc Returns the duration of the current vibration effect in seconds.
func get_joy_vibration_duration(device: int) -> float:
	pass;

#desc Returns the strength of the joypad vibration: x is the strength of the weak motor, and y is the strength of the strong motor.
func get_joy_vibration_strength(device: int) -> Vector2:
	pass;

#desc Returns the last mouse velocity. To provide a precise and jitter-free velocity, mouse velocity is only calculated every 0.1s. Therefore, mouse velocity will lag mouse movements.
func get_last_mouse_velocity() -> Vector2:
	pass;

#desc Returns the magnetic field strength in micro-Tesla for all axes of the device's magnetometer sensor, if the device has one. Otherwise, the method returns [constant Vector3.ZERO].
#desc [b]Note:[/b] This method only works on Android, iOS and UWP. On other platforms, it always returns [constant Vector3.ZERO].
func get_magnetometer() -> Vector3:
	pass;

#desc Returns mouse buttons as a bitmask. If multiple mouse buttons are pressed at the same time, the bits are added together.
func get_mouse_button_mask() -> int:
	pass;

#desc Gets an input vector by specifying four actions for the positive and negative X and Y axes.
#desc This method is useful when getting vector input, such as from a joystick, directional pad, arrows, or WASD. The vector has its length limited to 1 and has a circular deadzone, which is useful for using vector input as movement.
#desc By default, the deadzone is automatically calculated from the average of the action deadzones. However, you can override the deadzone to be whatever you want (on the range of 0 to 1).
func get_vector(negative_x: StringName, positive_x: StringName, negative_y: StringName, positive_y: StringName, deadzone: float) -> Vector2:
	pass;

#desc Returns [code]true[/code] when the user starts pressing the action event, meaning it's [code]true[/code] only on the frame that the user pressed down the button.
#desc This is useful for code that needs to run only once when an action is pressed, instead of every frame while it's pressed.
#desc If [param exact_match] is [code]false[/code], it ignores additional input modifiers for [InputEventKey] and [InputEventMouseButton] events, and the direction for [InputEventJoypadMotion] events.
#desc [b]Note:[/b] Due to keyboard ghosting, [method is_action_just_pressed] may return [code]false[/code] even if one of the action's keys is pressed. See [url=$DOCS_URL/tutorials/inputs/input_examples.html#keyboard-events]Input examples[/url] in the documentation for more information.
func is_action_just_pressed(action: StringName, exact_match: bool) -> bool:
	pass;

#desc Returns [code]true[/code] when the user stops pressing the action event, meaning it's [code]true[/code] only on the frame that the user released the button.
#desc If [param exact_match] is [code]false[/code], it ignores additional input modifiers for [InputEventKey] and [InputEventMouseButton] events, and the direction for [InputEventJoypadMotion] events.
func is_action_just_released(action: StringName, exact_match: bool) -> bool:
	pass;

#desc Returns [code]true[/code] if you are pressing the action event. Note that if an action has multiple buttons assigned and more than one of them is pressed, releasing one button will release the action, even if some other button assigned to this action is still pressed.
#desc If [param exact_match] is [code]false[/code], it ignores additional input modifiers for [InputEventKey] and [InputEventMouseButton] events, and the direction for [InputEventJoypadMotion] events.
#desc [b]Note:[/b] Due to keyboard ghosting, [method is_action_pressed] may return [code]false[/code] even if one of the action's keys is pressed. See [url=$DOCS_URL/tutorials/inputs/input_examples.html#keyboard-events]Input examples[/url] in the documentation for more information.
func is_action_pressed(action: StringName, exact_match: bool) -> bool:
	pass;

#desc Returns [code]true[/code] if any action, key, joypad button, or mouse button is being pressed. This will also return [code]true[/code] if any action is simulated via code by calling [method action_press].
func is_anything_pressed() -> bool:
	pass;

#desc Returns [code]true[/code] if you are pressing the joypad button (see [enum JoyButton]).
func is_joy_button_pressed(device: int, button: int) -> bool:
	pass;

#desc Returns [code]true[/code] if the system knows the specified device. This means that it sets all button and axis indices. Unknown joypads are not expected to match these constants, but you can still retrieve events from them.
func is_joy_known(device: int) -> bool:
	pass;

#desc Returns [code]true[/code] if you are pressing the key in the current keyboard layout. You can pass a [enum Key] constant.
#desc [method is_key_pressed] is only recommended over [method is_physical_key_pressed] in non-game applications. This ensures that shortcut keys behave as expected depending on the user's keyboard layout, as keyboard shortcuts are generally dependent on the keyboard layout in non-game applications. If in doubt, use [method is_physical_key_pressed].
#desc [b]Note:[/b] Due to keyboard ghosting, [method is_key_pressed] may return [code]false[/code] even if one of the action's keys is pressed. See [url=$DOCS_URL/tutorials/inputs/input_examples.html#keyboard-events]Input examples[/url] in the documentation for more information.
func is_key_pressed(keycode: int) -> bool:
	pass;

#desc Returns [code]true[/code] if you are pressing the mouse button specified with [enum MouseButton].
func is_mouse_button_pressed(button: int) -> bool:
	pass;

#desc Returns [code]true[/code] if you are pressing the key in the physical location on the 101/102-key US QWERTY keyboard. You can pass a [enum Key] constant.
#desc [method is_physical_key_pressed] is recommended over [method is_key_pressed] for in-game actions, as it will make [kbd]W[/kbd]/[kbd]A[/kbd]/[kbd]S[/kbd]/[kbd]D[/kbd] layouts work regardless of the user's keyboard layout. [method is_physical_key_pressed] will also ensure that the top row number keys work on any keyboard layout. If in doubt, use [method is_physical_key_pressed].
#desc [b]Note:[/b] Due to keyboard ghosting, [method is_physical_key_pressed] may return [code]false[/code] even if one of the action's keys is pressed. See [url=$DOCS_URL/tutorials/inputs/input_examples.html#keyboard-events]Input examples[/url] in the documentation for more information.
func is_physical_key_pressed(keycode: int) -> bool:
	pass;

#desc Feeds an [InputEvent] to the game. Can be used to artificially trigger input events from code. Also generates [method Node._input] calls.
#desc Example:
#desc [codeblocks]
#desc [gdscript]
#desc var cancel_event = InputEventAction.new()
#desc cancel_event.action = "ui_cancel"
#desc cancel_event.pressed = true
#desc Input.parse_input_event(cancel_event)
#desc [/gdscript]
#desc [csharp]
#desc var cancelEvent = new InputEventAction();
#desc cancelEvent.Action = "ui_cancel";
#desc cancelEvent.Pressed = true;
#desc Input.ParseInputEvent(cancelEvent);
#desc [/csharp]
#desc [/codeblocks]
func parse_input_event(event: InputEvent) -> void:
	pass;

#desc Removes all mappings from the internal database that match the given GUID.
func remove_joy_mapping(guid: String) -> void:
	pass;

#desc Sets the acceleration value of the accelerometer sensor. Can be used for debugging on devices without a hardware sensor, for example in an editor on a PC.
#desc [b]Note:[/b] This value can be immediately overwritten by the hardware sensor value on Android and iOS.
func set_accelerometer(value: Vector3) -> void:
	pass;

#desc Sets a custom mouse cursor image, which is only visible inside the game window. The hotspot can also be specified. Passing [code]null[/code] to the image parameter resets to the system cursor. See [enum CursorShape] for the list of shapes.
#desc [param image]'s size must be lower than 256×256.
#desc [param hotspot] must be within [param image]'s size.
#desc [b]Note:[/b] [AnimatedTexture]s aren't supported as custom mouse cursors. If using an [AnimatedTexture], only the first frame will be displayed.
#desc [b]Note:[/b] Only images imported with the [b]Lossless[/b], [b]Lossy[/b] or [b]Uncompressed[/b] compression modes are supported. The [b]Video RAM[/b] compression mode can't be used for custom cursors.
func set_custom_mouse_cursor(image: Resource, shape: int, hotspot: Vector2) -> void:
	pass;

#desc Sets the default cursor shape to be used in the viewport instead of [constant CURSOR_ARROW].
#desc [b]Note:[/b] If you want to change the default cursor shape for [Control]'s nodes, use [member Control.mouse_default_cursor_shape] instead.
#desc [b]Note:[/b] This method generates an [InputEventMouseMotion] to update cursor immediately.
func set_default_cursor_shape(shape: int) -> void:
	pass;

#desc Sets the gravity value of the accelerometer sensor. Can be used for debugging on devices without a hardware sensor, for example in an editor on a PC.
#desc [b]Note:[/b] This value can be immediately overwritten by the hardware sensor value on Android and iOS.
func set_gravity(value: Vector3) -> void:
	pass;

#desc Sets the value of the rotation rate of the gyroscope sensor. Can be used for debugging on devices without a hardware sensor, for example in an editor on a PC.
#desc [b]Note:[/b] This value can be immediately overwritten by the hardware sensor value on Android and iOS.
func set_gyroscope(value: Vector3) -> void:
	pass;

#desc Sets the value of the magnetic field of the magnetometer sensor. Can be used for debugging on devices without a hardware sensor, for example in an editor on a PC.
#desc [b]Note:[/b] This value can be immediately overwritten by the hardware sensor value on Android and iOS.
func set_magnetometer(value: Vector3) -> void:
	pass;

#desc Starts to vibrate the joypad. Joypads usually come with two rumble motors, a strong and a weak one. [param weak_magnitude] is the strength of the weak motor (between 0 and 1) and [param strong_magnitude] is the strength of the strong motor (between 0 and 1). [param duration] is the duration of the effect in seconds (a duration of 0 will try to play the vibration indefinitely).
#desc [b]Note:[/b] Not every hardware is compatible with long effect durations; it is recommended to restart an effect if it has to be played for more than a few seconds.
func start_joy_vibration(device: int, weak_magnitude: float, strong_magnitude: float, duration: float) -> void:
	pass;

#desc Stops the vibration of the joypad.
func stop_joy_vibration(device: int) -> void:
	pass;

#desc Vibrate handheld devices.
#desc [b]Note:[/b] This method is implemented on Android, iOS, and Web.
#desc [b]Note:[/b] For Android, it requires enabling the [code]VIBRATE[/code] permission in the export preset.
#desc [b]Note:[/b] For iOS, specifying the duration is supported in iOS 13 and later.
#desc [b]Note:[/b] Some web browsers such as Safari and Firefox for Android do not support this method.
func vibrate_handheld(duration_ms: int) -> void:
	pass;

#desc Sets the mouse position to the specified vector, provided in pixels and relative to an origin at the upper left corner of the currently focused Window Manager game window.
#desc Mouse position is clipped to the limits of the screen resolution, or to the limits of the game window if [enum MouseMode] is set to [code]MOUSE_MODE_CONFINED[/code] or [code]MOUSE_MODE_CONFINED_HIDDEN[/code].
func warp_mouse(position: Vector2) -> void:
	pass;


