extends RefCounted
class_name Resource

var resource_local_to_scene: bool;
var resource_name: String;
var resource_path: String;

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
