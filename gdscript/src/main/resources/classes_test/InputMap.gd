extends Object
class_name InputMap



func action_add_event(action: StringName, event: InputEvent) -> void:
    pass;

func action_erase_event(action: StringName, event: InputEvent) -> void:
    pass;

func action_erase_events(action: StringName) -> void:
    pass;

func action_get_deadzone(action: StringName) -> float:
    pass;

func action_get_events(action: StringName) -> Array:
    pass;

func action_has_event(action: StringName, event: InputEvent) -> bool:
    pass;

func action_set_deadzone(action: StringName, deadzone: float) -> void:
    pass;

func add_action(action: StringName, deadzone: float) -> void:
    pass;

func erase_action(action: StringName) -> void:
    pass;

func event_is_action(event: InputEvent, action: StringName, exact_match: bool) -> bool:
    pass;

func get_actions() -> Array:
    pass;

func has_action(action: StringName) -> bool:
    pass;

func load_from_project_settings() -> void:
    pass;

