#brief Generic input event.
#desc Base class of all sort of input event. See [method Node._input].
class_name InputEvent


#desc The event's device ID.
#desc [b]Note:[/b] This device ID will always be [code]-1[/code] for emulated mouse input from a touchscreen. This can be used to distinguish emulated mouse input from physical mouse input.
var device: int;



#desc Returns [code]true[/code] if the given input event and this input event can be added together (only for events of type [InputEventMouseMotion]).
#desc The given input event's position, global position and speed will be copied. The resulting [code]relative[/code] is a sum of both events. Both events' modifiers have to be identical.
func accumulate(with_event: InputEvent) -> bool:
	pass;

#desc Returns a [String] representation of the event.
func as_text() -> String:
	pass;

#desc Returns a value between 0.0 and 1.0 depending on the given actions' state. Useful for getting the value of events of type [InputEventJoypadMotion].
#desc If [param exact_match] is [code]false[/code], it ignores additional input modifiers for [InputEventKey] and [InputEventMouseButton] events, and the direction for [InputEventJoypadMotion] events.
func get_action_strength(action: StringName, exact_match: bool) -> float:
	pass;

#desc Returns [code]true[/code] if this input event matches a pre-defined action of any type.
#desc If [param exact_match] is [code]false[/code], it ignores additional input modifiers for [InputEventKey] and [InputEventMouseButton] events, and the direction for [InputEventJoypadMotion] events.
func is_action(action: StringName, exact_match: bool) -> bool:
	pass;

#desc Returns [code]true[/code] if the given action is being pressed (and is not an echo event for [InputEventKey] events, unless [param allow_echo] is [code]true[/code]). Not relevant for events of type [InputEventMouseMotion] or [InputEventScreenDrag].
#desc If [param exact_match] is [code]false[/code], it ignores additional input modifiers for [InputEventKey] and [InputEventMouseButton] events, and the direction for [InputEventJoypadMotion] events.
#desc [b]Note:[/b] Due to keyboard ghosting, [method is_action_pressed] may return [code]false[/code] even if one of the action's keys is pressed. See [url=$DOCS_URL/tutorials/inputs/input_examples.html#keyboard-events]Input examples[/url] in the documentation for more information.
func is_action_pressed(action: StringName, allow_echo: bool, exact_match: bool) -> bool:
	pass;

#desc Returns [code]true[/code] if the given action is released (i.e. not pressed). Not relevant for events of type [InputEventMouseMotion] or [InputEventScreenDrag].
#desc If [param exact_match] is [code]false[/code], it ignores additional input modifiers for [InputEventKey] and [InputEventMouseButton] events, and the direction for [InputEventJoypadMotion] events.
func is_action_released(action: StringName, exact_match: bool) -> bool:
	pass;

#desc Returns [code]true[/code] if this input event's type is one that can be assigned to an input action.
func is_action_type() -> bool:
	pass;

#desc Returns [code]true[/code] if this input event is an echo event (only for events of type [InputEventKey]).
func is_echo() -> bool:
	pass;

#desc Returns [code]true[/code] if the specified [param event] matches this event. Only valid for action events i.e key ([InputEventKey]), button ([InputEventMouseButton] or [InputEventJoypadButton]), axis [InputEventJoypadMotion] or action ([InputEventAction]) events.
#desc If [param exact_match] is [code]false[/code], it ignores additional input modifiers for [InputEventKey] and [InputEventMouseButton] events, and the direction for [InputEventJoypadMotion] events.
func is_match(event: InputEvent, exact_match: bool) -> bool:
	pass;

#desc Returns [code]true[/code] if this input event is pressed. Not relevant for events of type [InputEventMouseMotion] or [InputEventScreenDrag].
#desc [b]Note:[/b] Due to keyboard ghosting, [method is_pressed] may return [code]false[/code] even if one of the action's keys is pressed. See [url=$DOCS_URL/tutorials/inputs/input_examples.html#keyboard-events]Input examples[/url] in the documentation for more information.
func is_pressed() -> bool:
	pass;

#desc Returns a copy of the given input event which has been offset by [param local_ofs] and transformed by [param xform]. Relevant for events of type [InputEventMouseButton], [InputEventMouseMotion], [InputEventScreenTouch], [InputEventScreenDrag], [InputEventMagnifyGesture] and [InputEventPanGesture].
func xformed_by(xform: Transform2D, local_ofs: Vector2) -> InputEvent:
	pass;


