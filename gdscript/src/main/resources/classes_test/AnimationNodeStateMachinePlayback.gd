extends Resource
class_name AnimationNodeStateMachinePlayback


var resource_local_to_scene: bool setget set_local_to_scene, is_local_to_scene;

func get_current_length() -> float:
    pass;

func get_current_node() -> StringName:
    pass;

func get_current_play_position() -> float:
    pass;

func get_travel_path() -> PackedStringArray:
    pass;

func is_playing() -> bool:
    pass;

func start(node: StringName) -> void:
    pass;

func stop() -> void:
    pass;

func travel(to_node: StringName) -> void:
    pass;

