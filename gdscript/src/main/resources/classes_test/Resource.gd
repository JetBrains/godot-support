extends RefCounted
class_name Resource


var resource_local_to_scene: bool setget set_local_to_scene, is_local_to_scene;
var resource_name: String setget set_name, get_name;
var resource_path: String setget set_path, get_path;

func _setup_local_to_scene() -> void:
    pass;

func duplicate(subresources: bool) -> Resource:
    pass;

func emit_changed() -> void:
    pass;

func get_local_scene() -> Node:
    pass;

func get_rid() -> RID:
    pass;

func setup_local_to_scene() -> void:
    pass;

func take_over_path(path: String) -> void:
    pass;

