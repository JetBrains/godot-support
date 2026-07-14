extends Object
class_name Input


#enum MouseMode
enum {
    MOUSE_MODE_VISIBLE = 0,
    MOUSE_MODE_HIDDEN = 1,
    MOUSE_MODE_CAPTURED = 2,
    MOUSE_MODE_CONFINED = 3,
    MOUSE_MODE_CONFINED_HIDDEN = 4,
    MOUSE_MODE_MAX = 5,
}
#enum CursorShape
enum {
    CURSOR_ARROW = 0,
    CURSOR_IBEAM = 1,
    CURSOR_POINTING_HAND = 2,
    CURSOR_CROSS = 3,
    CURSOR_WAIT = 4,
    CURSOR_BUSY = 5,
    CURSOR_DRAG = 6,
    CURSOR_CAN_DROP = 7,
    CURSOR_FORBIDDEN = 8,
    CURSOR_VSIZE = 9,
    CURSOR_HSIZE = 10,
    CURSOR_BDIAGSIZE = 11,
    CURSOR_FDIAGSIZE = 12,
    CURSOR_MOVE = 13,
    CURSOR_VSPLIT = 14,
    CURSOR_HSPLIT = 15,
    CURSOR_HELP = 16,
}
## If [code]true[/code], sends mouse input events when tapping or swiping on the touchscreen. See also [member ProjectSettings.input_devices/pointing/emulate_mouse_from_touch].
var emulate_mouse_from_touch: bool:
	get = is_emulating_mouse_from_touch, set = set_emulate_mouse_from_touch

## If [code]true[/code], sends touch input events when clicking or dragging the mouse. See also [member ProjectSettings.input_devices/pointing/emulate_touch_from_mouse].
var emulate_touch_from_mouse: bool:
	get = is_emulating_touch_from_mouse, set = set_emulate_touch_from_mouse

## Controls the mouse mode.
var mouse_mode: int:
	get = get_mouse_mode, set = set_mouse_mode

## If [code]true[/code], similar input events sent by the operating system are accumulated. When input accumulation is enabled, all input events generated during a frame will be merged and emitted when the frame is done rendering. Therefore, this limits the number of input method calls per second to the rendering FPS.
## Input accumulation can be disabled to get slightly more precise/reactive input at the cost of increased CPU usage. In applications where drawing freehand lines is required, input accumulation should generally be disabled while the user is drawing the line to get results that closely follow the actual input.
## [b]Note:[/b] Input accumulation is [i]enabled[/i] by default.
var use_accumulated_input: bool:
	get = is_using_accumulated_input, set = set_use_accumulated_input



## This will simulate pressing the specified action.
## The strength can be used for non-boolean actions, it's ranged between 0 and 1 representing the intensity of the given action.
## [b]Note:[/b] This method will not cause any [method Node._input] calls. It is intended to be used with [method is_action_pressed] and [method is_action_just_pressed]. If you want to simulate [code]_input[/code], use [method parse_input_event] instead.
func action_press(action: StringName, strength: float = 1.0) -> void:
	pass;

## If the specified action is already pressed, this will release it.
func action_release(action: StringName) -> void:
	pass;

## Adds a new mapping entry (in SDL2 format) to the mapping database. Optionally update already connected devices.
func add_joy_mapping(mapping: String, update_existing: bool = false) -> void:
	pass;

## Sends all input events which are in the current buffer to the game loop. These events may have been buffered as a result of accumulated input ([member use_accumulated_input]) or agile input flushing ([member ProjectSettings.input_devices/buffering/agile_event_flushing]).
## The engine will already do this itself at key execution points (at least once per frame). However, this can be useful in advanced cases where you want precise control over the timing of event handling.
func flush_buffered_events() -> void:
	pass;

## Returns the acceleration in m/s² of the device's accelerometer sensor, if the device has one. Otherwise, the method returns [constant Vector3.ZERO].
## Note this method returns an empty [Vector3] when running from the editor even when your device has an accelerometer. You must export your project to a supported device to read values from the accelerometer.
## [b]Note:[/b] This method only works on Android and iOS. On other platforms, it always returns [constant Vector3.ZERO].
## [b]Note:[/b] For Android, [member ProjectSettings.input_devices/sensors/enable_accelerometer] must be enabled.
func get_accelerometer() -> Vector3:
	pass;

## Returns a value between 0 and 1 representing the raw intensity of the given action, ignoring the action's deadzone. In most cases, you should use [method get_action_strength] instead.
## If [param exact_match] is [code]false[/code], it ignores additional input modifiers for [InputEventKey] and [InputEventMouseButton] events, and the direction for [InputEventJoypadMotion] events.
func get_action_raw_strength(action: StringName, exact_match: bool = false) -> float:
	pass;

## Returns a value between 0 and 1 representing the intensity of the given action. In a joypad, for example, the further away the axis (analog sticks or L2, R2 triggers) is from the dead zone, the closer the value will be to 1. If the action is mapped to a control that has no axis such as the keyboard, the value returned will be 0 or 1.
## If [param exact_match] is [code]false[/code], it ignores additional input modifiers for [InputEventKey] and [InputEventMouseButton] events, and the direction for [InputEventJoypadMotion] events.
func get_action_strength(action: StringName, exact_match: bool = false) -> float:
	pass;

## Get axis input by specifying two actions, one negative and one positive.
## This is a shorthand for writing [code]Input.get_action_strength("positive_action") - Input.get_action_strength("negative_action")[/code].
func get_axis(negative_action: StringName, positive_action: StringName) -> float:
	pass;

## Returns an [Array] containing the device IDs of all currently connected joypads.
func get_connected_joypads() -> Array[int]:
	pass;

## Returns the currently assigned cursor shape.
func get_current_cursor_shape() -> int:
	pass;

## Returns the gravity in m/s² of the device's accelerometer sensor, if the device has one. Otherwise, the method returns [constant Vector3.ZERO].
## [b]Note:[/b] This method only works on Android and iOS. On other platforms, it always returns [constant Vector3.ZERO].
## [b]Note:[/b] For Android, [member ProjectSettings.input_devices/sensors/enable_gravity] must be enabled.
func get_gravity() -> Vector3:
	pass;

## Returns the rotation rate in rad/s around a device's X, Y, and Z axes of the gyroscope sensor, if the device has one. Otherwise, the method returns [constant Vector3.ZERO].
## [b]Note:[/b] This method only works on Android and iOS. On other platforms, it always returns [constant Vector3.ZERO].
## [b]Note:[/b] For Android, [member ProjectSettings.input_devices/sensors/enable_gyroscope] must be enabled.
func get_gyroscope() -> Vector3:
	pass;

## Returns the current value of the joypad axis at index [param axis].
func get_joy_axis(device: int, axis: int) -> float:
	pass;

## Returns an SDL2-compatible device GUID on platforms that use gamepad remapping, e.g. [code]030000004c050000c405000000010000[/code]. Returns an empty string if it cannot be found. Godot uses the [url=https://github.com/gabomdq/SDL_GameControllerDB]SDL2 game controller database[/url] to determine gamepad names and mappings based on this GUID.
## On Windows, all XInput joypad GUIDs will be overridden by Godot to [code]__XINPUT_DEVICE__[/code], because their mappings are the same.
func get_joy_guid(device: int) -> String:
	pass;

## Returns a dictionary with extra platform-specific information about the device, e.g. the raw gamepad name from the OS or the Steam Input index.
## On Windows, the dictionary contains the following fields:
## [code]xinput_index[/code]: The index of the controller in the XInput system. Undefined for DirectInput devices.
## [code]vendor_id[/code]: The USB vendor ID of the device.
## [code]product_id[/code]: The USB product ID of the device.
## On Linux:
## [code]raw_name[/code]: The name of the controller as it came from the OS, before getting renamed by the godot controller database.
## [code]vendor_id[/code]: The USB vendor ID of the device.
## [code]product_id[/code]: The USB product ID of the device.
## [code]steam_input_index[/code]: The Steam Input gamepad index, if the device is not a Steam Input device this key won't be present.
## [b]Note:[/b] The returned dictionary is always empty on Web, iOS, Android, and macOS.
func get_joy_info(device: int) -> Dictionary:
	pass;

## Returns the name of the joypad at the specified device index, e.g. [code]PS4 Controller[/code]. Godot uses the [url=https://github.com/gabomdq/SDL_GameControllerDB]SDL2 game controller database[/url] to determine gamepad names.
func get_joy_name(device: int) -> String:
	pass;

## Returns the duration of the current vibration effect in seconds.
func get_joy_vibration_duration(device: int) -> float:
	pass;

## Returns the strength of the joypad vibration: x is the strength of the weak motor, and y is the strength of the strong motor.
func get_joy_vibration_strength(device: int) -> Vector2:
	pass;

## Returns the last mouse velocity in screen coordinates. To provide a precise and jitter-free velocity, mouse velocity is only calculated every 0.1s. Therefore, mouse velocity will lag mouse movements.
func get_last_mouse_screen_velocity() -> Vector2:
	pass;

## Returns the last mouse velocity. To provide a precise and jitter-free velocity, mouse velocity is only calculated every 0.1s. Therefore, mouse velocity will lag mouse movements.
func get_last_mouse_velocity() -> Vector2:
	pass;

## Returns the magnetic field strength in micro-Tesla for all axes of the device's magnetometer sensor, if the device has one. Otherwise, the method returns [constant Vector3.ZERO].
## [b]Note:[/b] This method only works on Android and iOS. On other platforms, it always returns [constant Vector3.ZERO].
## [b]Note:[/b] For Android, [member ProjectSettings.input_devices/sensors/enable_magnetometer] must be enabled.
func get_magnetometer() -> Vector3:
	pass;

## Returns mouse buttons as a bitmask. If multiple mouse buttons are pressed at the same time, the bits are added together. Equivalent to [method DisplayServer.mouse_get_button_state].
func get_mouse_button_mask() -> int:
	pass;

## Gets an input vector by specifying four actions for the positive and negative X and Y axes.
## This method is useful when getting vector input, such as from a joystick, directional pad, arrows, or WASD. The vector has its length limited to 1 and has a circular deadzone, which is useful for using vector input as movement.
## By default, the deadzone is automatically calculated from the average of the action deadzones. However, you can override the deadzone to be whatever you want (on the range of 0 to 1).
func get_vector(negative_x: StringName, positive_x: StringName, negative_y: StringName, positive_y: StringName, deadzone: float = -1.0) -> Vector2:
	pass;

## Returns [code]true[/code] when the user has [i]started[/i] pressing the action event in the current frame or physics tick. It will only return [code]true[/code] on the frame or tick that the user pressed down the button.
## This is useful for code that needs to run only once when an action is pressed, instead of every frame while it's pressed.
## If [param exact_match] is [code]false[/code], it ignores additional input modifiers for [InputEventKey] and [InputEventMouseButton] events, and the direction for [InputEventJoypadMotion] events.
## [b]Note:[/b] Returning [code]true[/code] does not imply that the action is [i]still[/i] pressed. An action can be pressed and released again rapidly, and [code]true[/code] will still be returned so as not to miss input.
## [b]Note:[/b] Due to keyboard ghosting, [method is_action_just_pressed] may return [code]false[/code] even if one of the action's keys is pressed. See [url=$DOCS_URL/tutorials/inputs/input_examples.html#keyboard-events]Input examples[/url] in the documentation for more information.
## [b]Note:[/b] During input handling (e.g. [method Node._input]), use [method InputEvent.is_action_pressed] instead to query the action state of the current event.
func is_action_just_pressed(action: StringName, exact_match: bool = false) -> bool:
	pass;

## Returns [code]true[/code] when the user [i]stops[/i] pressing the action event in the current frame or physics tick. It will only return [code]true[/code] on the frame or tick that the user releases the button.
## [b]Note:[/b] Returning [code]true[/code] does not imply that the action is [i]still[/i] not pressed. An action can be released and pressed again rapidly, and [code]true[/code] will still be returned so as not to miss input.
## If [param exact_match] is [code]false[/code], it ignores additional input modifiers for [InputEventKey] and [InputEventMouseButton] events, and the direction for [InputEventJoypadMotion] events.
## [b]Note:[/b] During input handling (e.g. [method Node._input]), use [method InputEvent.is_action_released] instead to query the action state of the current event.
func is_action_just_released(action: StringName, exact_match: bool = false) -> bool:
	pass;

## Returns [code]true[/code] if you are pressing the action event.
## If [param exact_match] is [code]false[/code], it ignores additional input modifiers for [InputEventKey] and [InputEventMouseButton] events, and the direction for [InputEventJoypadMotion] events.
## [b]Note:[/b] Due to keyboard ghosting, [method is_action_pressed] may return [code]false[/code] even if one of the action's keys is pressed. See [url=$DOCS_URL/tutorials/inputs/input_examples.html#keyboard-events]Input examples[/url] in the documentation for more information.
func is_action_pressed(action: StringName, exact_match: bool = false) -> bool:
	pass;

## Returns [code]true[/code] if any action, key, joypad button, or mouse button is being pressed. This will also return [code]true[/code] if any action is simulated via code by calling [method action_press].
func is_anything_pressed() -> bool:
	pass;

## Returns [code]true[/code] if you are pressing the joypad button at index [param button].
func is_joy_button_pressed(device: int, button: int) -> bool:
	pass;

## Returns [code]true[/code] if the system knows the specified device. This means that it sets all button and axis indices. Unknown joypads are not expected to match these constants, but you can still retrieve events from them.
func is_joy_known(device: int) -> bool:
	pass;

## Returns [code]true[/code] if you are pressing the key with the [param keycode] printed on it. You can pass a [enum Key] constant or any Unicode character code.
func is_key_label_pressed(keycode: int) -> bool:
	pass;

## Returns [code]true[/code] if you are pressing the Latin key in the current keyboard layout. You can pass a [enum Key] constant.
## [method is_key_pressed] is only recommended over [method is_physical_key_pressed] in non-game applications. This ensures that shortcut keys behave as expected depending on the user's keyboard layout, as keyboard shortcuts are generally dependent on the keyboard layout in non-game applications. If in doubt, use [method is_physical_key_pressed].
## [b]Note:[/b] Due to keyboard ghosting, [method is_key_pressed] may return [code]false[/code] even if one of the action's keys is pressed. See [url=$DOCS_URL/tutorials/inputs/input_examples.html#keyboard-events]Input examples[/url] in the documentation for more information.
func is_key_pressed(keycode: int) -> bool:
	pass;

## Returns [code]true[/code] if you are pressing the mouse button specified with [enum MouseButton].
func is_mouse_button_pressed(button: int) -> bool:
	pass;

## Returns [code]true[/code] if you are pressing the key in the physical location on the 101/102-key US QWERTY keyboard. You can pass a [enum Key] constant.
## [method is_physical_key_pressed] is recommended over [method is_key_pressed] for in-game actions, as it will make [kbd]W[/kbd]/[kbd]A[/kbd]/[kbd]S[/kbd]/[kbd]D[/kbd] layouts work regardless of the user's keyboard layout. [method is_physical_key_pressed] will also ensure that the top row number keys work on any keyboard layout. If in doubt, use [method is_physical_key_pressed].
## [b]Note:[/b] Due to keyboard ghosting, [method is_physical_key_pressed] may return [code]false[/code] even if one of the action's keys is pressed. See [url=$DOCS_URL/tutorials/inputs/input_examples.html#keyboard-events]Input examples[/url] in the documentation for more information.
func is_physical_key_pressed(keycode: int) -> bool:
	pass;

## Feeds an [InputEvent] to the game. Can be used to artificially trigger input events from code. Also generates [method Node._input] calls.
## [codeblocks]
## [gdscript]
## var cancel_event = InputEventAction.new()
## cancel_event.action = "ui_cancel"
## cancel_event.pressed = true
## Input.parse_input_event(cancel_event)
## [/gdscript]
## [csharp]
## var cancelEvent = new InputEventAction();
## cancelEvent.Action = "ui_cancel";
## cancelEvent.Pressed = true;
## Input.ParseInputEvent(cancelEvent);
## [/csharp]
## [/codeblocks]
## [b]Note:[/b] Calling this function has no influence on the operating system. So for example sending an [InputEventMouseMotion] will not move the OS mouse cursor to the specified position (use [method warp_mouse] instead) and sending [kbd]Alt/Cmd + Tab[/kbd] as [InputEventKey] won't toggle between active windows.
func parse_input_event(event: InputEvent) -> void:
	pass;

## Removes all mappings from the internal database that match the given GUID. All currently connected joypads that use this GUID will become unmapped.
## On Android, Godot will map to an internal fallback mapping.
func remove_joy_mapping(guid: String) -> void:
	pass;

## Sets the acceleration value of the accelerometer sensor. Can be used for debugging on devices without a hardware sensor, for example in an editor on a PC.
## [b]Note:[/b] This value can be immediately overwritten by the hardware sensor value on Android and iOS.
func set_accelerometer(value: Vector3) -> void:
	pass;

## Sets a custom mouse cursor image, which is only visible inside the game window, for the given mouse [param shape]. The hotspot can also be specified. Passing [code]null[/code] to the image parameter resets to the system cursor.
## [param image] can be either [Texture2D] or [Image] and its size must be lower than or equal to 256×256. To avoid rendering issues, sizes lower than or equal to 128×128 are recommended.
## [param hotspot] must be within [param image]'s size.
## [b]Note:[/b] [AnimatedTexture]s aren't supported as custom mouse cursors. If using an [AnimatedTexture], only the first frame will be displayed.
## [b]Note:[/b] The [b]Lossless[/b], [b]Lossy[/b] or [b]Uncompressed[/b] compression modes are recommended. The [b]Video RAM[/b] compression mode can be used, but it will be decompressed on the CPU, which means loading times are slowed down and no memory is saved compared to lossless modes.
## [b]Note:[/b] On the web platform, the maximum allowed cursor image size is 128×128. Cursor images larger than 32×32 will also only be displayed if the mouse cursor image is entirely located within the page for [url=https://chromestatus.com/feature/5825971391299584]security reasons[/url].
func set_custom_mouse_cursor(image: Resource, shape: int = 0, hotspot: Vector2 = Vector2(0, 0)) -> void:
	pass;

## Sets the default cursor shape to be used in the viewport instead of [constant CURSOR_ARROW].
## [b]Note:[/b] If you want to change the default cursor shape for [Control]'s nodes, use [member Control.mouse_default_cursor_shape] instead.
## [b]Note:[/b] This method generates an [InputEventMouseMotion] to update cursor immediately.
func set_default_cursor_shape(shape: int = 0) -> void:
	pass;

## Sets the gravity value of the accelerometer sensor. Can be used for debugging on devices without a hardware sensor, for example in an editor on a PC.
## [b]Note:[/b] This value can be immediately overwritten by the hardware sensor value on Android and iOS.
func set_gravity(value: Vector3) -> void:
	pass;

## Sets the value of the rotation rate of the gyroscope sensor. Can be used for debugging on devices without a hardware sensor, for example in an editor on a PC.
## [b]Note:[/b] This value can be immediately overwritten by the hardware sensor value on Android and iOS.
func set_gyroscope(value: Vector3) -> void:
	pass;

## Sets the value of the magnetic field of the magnetometer sensor. Can be used for debugging on devices without a hardware sensor, for example in an editor on a PC.
## [b]Note:[/b] This value can be immediately overwritten by the hardware sensor value on Android and iOS.
func set_magnetometer(value: Vector3) -> void:
	pass;

## Queries whether an input device should be ignored or not. Devices can be ignored by setting the environment variable [code]SDL_GAMECONTROLLER_IGNORE_DEVICES[/code]. Read the [url=https://wiki.libsdl.org/SDL2]SDL documentation[/url] for more information.
## [b]Note:[/b] Some 3rd party tools can contribute to the list of ignored devices. For example, [i]SteamInput[/i] creates virtual devices from physical devices for remapping purposes. To avoid handling the same input device twice, the original device is added to the ignore list.
func should_ignore_device(vendor_id: int, product_id: int) -> bool:
	pass;

## Starts to vibrate the joypad. Joypads usually come with two rumble motors, a strong and a weak one. [param weak_magnitude] is the strength of the weak motor (between 0 and 1) and [param strong_magnitude] is the strength of the strong motor (between 0 and 1). [param duration] is the duration of the effect in seconds (a duration of 0 will try to play the vibration indefinitely). The vibration can be stopped early by calling [method stop_joy_vibration].
## [b]Note:[/b] Not every hardware is compatible with long effect durations; it is recommended to restart an effect if it has to be played for more than a few seconds.
## [b]Note:[/b] For macOS, vibration is only supported in macOS 11 and later.
func start_joy_vibration(device: int, weak_magnitude: float, strong_magnitude: float, duration: float = 0) -> void:
	pass;

## Stops the vibration of the joypad started with [method start_joy_vibration].
func stop_joy_vibration(device: int) -> void:
	pass;

## Vibrate the handheld device for the specified duration in milliseconds.
## [param amplitude] is the strength of the vibration, as a value between [code]0.0[/code] and [code]1.0[/code]. If set to [code]-1.0[/code], the default vibration strength of the device is used.
## [b]Note:[/b] This method is implemented on Android, iOS, and Web. It has no effect on other platforms.
## [b]Note:[/b] For Android, [method vibrate_handheld] requires enabling the [code]VIBRATE[/code] permission in the export preset. Otherwise, [method vibrate_handheld] will have no effect.
## [b]Note:[/b] For iOS, specifying the duration is only supported in iOS 13 and later.
## [b]Note:[/b] For Web, the amplitude cannot be changed.
## [b]Note:[/b] Some web browsers such as Safari and Firefox for Android do not support [method vibrate_handheld].
func vibrate_handheld(duration_ms: int = 500, amplitude: float = -1.0) -> void:
	pass;

## Sets the mouse position to the specified vector, provided in pixels and relative to an origin at the upper left corner of the currently focused Window Manager game window.
## Mouse position is clipped to the limits of the screen resolution, or to the limits of the game window if [enum MouseMode] is set to [constant MOUSE_MODE_CONFINED] or [constant MOUSE_MODE_CONFINED_HIDDEN].
## [b]Note:[/b] [method warp_mouse] is only supported on Windows, macOS and Linux. It has no effect on Android, iOS and Web.
func warp_mouse(position: Vector2) -> void:
	pass;


func is_emulating_mouse_from_touch() -> bool:
	return emulate_mouse_from_touch

func set_emulate_mouse_from_touch(value: bool) -> void:
	emulate_mouse_from_touch = value

func is_emulating_touch_from_mouse() -> bool:
	return emulate_touch_from_mouse

func set_emulate_touch_from_mouse(value: bool) -> void:
	emulate_touch_from_mouse = value

func get_mouse_mode() -> int:
	return mouse_mode

func set_mouse_mode(value: int) -> void:
	mouse_mode = value

func is_using_accumulated_input() -> bool:
	return use_accumulated_input

func set_use_accumulated_input(value: bool) -> void:
	use_accumulated_input = value

