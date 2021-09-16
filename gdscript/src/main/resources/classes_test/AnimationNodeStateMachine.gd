extends AnimationRootNode
class_name AnimationNodeStateMachine


func add_node(name: StringName, node: AnimationNode, position: Vector2) -> void:
    pass;
func add_transition(from: StringName, to: StringName, transition: AnimationNodeStateMachineTransition) -> void:
    pass;
func get_end_node() -> String:
    pass;
func get_graph_offset() -> Vector2:
    pass;
func get_node(name: StringName) -> AnimationNode:
    pass;
func get_node_name(node: AnimationNode) -> StringName:
    pass;
func get_node_position(name: StringName) -> Vector2:
    pass;
func get_start_node() -> String:
    pass;
func get_transition(idx: int) -> AnimationNodeStateMachineTransition:
    pass;
func get_transition_count() -> int:
    pass;
func get_transition_from(idx: int) -> StringName:
    pass;
func get_transition_to(idx: int) -> StringName:
    pass;
func has_node(name: StringName) -> bool:
    pass;
func has_transition(from: StringName, to: StringName) -> bool:
    pass;
func remove_node(name: StringName) -> void:
    pass;
func remove_transition(from: StringName, to: StringName) -> void:
    pass;
func remove_transition_by_index(idx: int) -> void:
    pass;
func rename_node(name: StringName, new_name: StringName) -> void:
    pass;
func replace_node(name: StringName, node: AnimationNode) -> void:
    pass;
func set_end_node(name: StringName) -> void:
    pass;
func set_graph_offset(offset: Vector2) -> void:
    pass;
func set_node_position(name: StringName, position: Vector2) -> void:
    pass;
func set_start_node(name: StringName) -> void:
    pass;
