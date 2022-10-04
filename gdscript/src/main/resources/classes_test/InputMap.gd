#brief Singleton that manages [InputEventAction].
#desc Manages all [InputEventAction] which can be created/modified from the project settings menu [b]Project > Project Settings > Input Map[/b] or in code with [method add_action] and [method action_add_event]. See [method Node._input].
class_name InputMap




#desc Adds an [InputEvent] to an action. This [InputEvent] will trigger the action.
func action_add_event(action: StringName, event: InputEvent) -> void:
	pass;

#desc Removes an [InputEvent] from an action.
func action_erase_event(action: StringName, event: InputEvent) -> void:
	pass;

#desc Removes all events from an action.
func action_erase_events(action: StringName) -> void:
	pass;

#desc Returns a deadzone value for the action.
func action_get_deadzone(action: StringName) -> float:
	pass;

#desc Returns an array of [InputEvent]s associated with a given action.
#desc [b]Note:[/b] When used in the editor (e.g. a tool script or [EditorPlugin]), this method will return events for the editor action. If you want to access your project's input binds from the editor, read the [code]input/*[/code] settings from [ProjectSettings].
func action_get_events(action: StringName) -> InputEvent[]:
	pass;

#desc Returns [code]true[/code] if the action has the given [InputEvent] associated with it.
func action_has_event(action: StringName, event: InputEvent) -> bool:
	pass;

#desc Sets a deadzone value for the action.
func action_set_deadzone(action: StringName, deadzone: float) -> void:
	pass;

#desc Adds an empty action to the [InputMap] with a configurable [param deadzone].
#desc An [InputEvent] can then be added to this action with [method action_add_event].
func add_action(action: StringName, deadzone: float) -> void:
	pass;

#desc Removes an action from the [InputMap].
func erase_action(action: StringName) -> void:
	pass;

#desc Returns [code]true[/code] if the given event is part of an existing action. This method ignores keyboard modifiers if the given [InputEvent] is not pressed (for proper release detection). See [method action_has_event] if you don't want this behavior.
#desc If [param exact_match] is [code]false[/code], it ignores additional input modifiers for [InputEventKey] and [InputEventMouseButton] events, and the direction for [InputEventJoypadMotion] events.
func event_is_action(event: InputEvent, action: StringName, exact_match: bool) -> bool:
	pass;

#desc Returns an array of all actions in the [InputMap].
func get_actions() -> StringName[]:
	pass;

#desc Returns [code]true[/code] if the [InputMap] has a registered action with the given name.
func has_action(action: StringName) -> bool:
	pass;

#desc Clears all [InputEventAction] in the [InputMap] and load it anew from [ProjectSettings].
func load_from_project_settings() -> void:
	pass;


