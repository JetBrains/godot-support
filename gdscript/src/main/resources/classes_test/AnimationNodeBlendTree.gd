extends AnimationRootNode
class_name AnimationNodeBlendTree
const CONNECTION_OK = 0;
const CONNECTION_ERROR_NO_INPUT = 1;
const CONNECTION_ERROR_NO_INPUT_INDEX = 2;
const CONNECTION_ERROR_NO_OUTPUT = 3;
const CONNECTION_ERROR_SAME_NODE = 4;
const CONNECTION_ERROR_CONNECTION_EXISTS = 5;

var graph_offset: Vector2;

func add_node(name: StringName, node: AnimationNode, position: Vector2) -> void:
    pass;
func connect_node(input_node: StringName, input_index: int, output_node: StringName) -> void:
    pass;
func disconnect_node(input_node: StringName, input_index: int) -> void:
    pass;
func get_node(name: StringName) -> AnimationNode:
    pass;
func get_node_position(name: StringName) -> Vector2:
    pass;
func has_node(name: StringName) -> bool:
    pass;
func remove_node(name: StringName) -> void:
    pass;
func rename_node(name: StringName, new_name: StringName) -> void:
    pass;
func set_node_position(name: StringName, position: Vector2) -> void:
    pass;
