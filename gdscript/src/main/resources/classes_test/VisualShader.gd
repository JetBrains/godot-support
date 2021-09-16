extends Shader
class_name VisualShader
const TYPE_VERTEX = 0;
const TYPE_FRAGMENT = 1;
const TYPE_LIGHT = 2;
const TYPE_START = 3;
const TYPE_PROCESS = 4;
const TYPE_COLLIDE = 5;
const TYPE_START_CUSTOM = 6;
const TYPE_PROCESS_CUSTOM = 7;
const TYPE_SKY = 8;
const TYPE_MAX = 9;
const NODE_ID_INVALID = -1;
const NODE_ID_OUTPUT = 0;

var graph_offset: Vector2;
var version: String;

func add_node(type: int, node: VisualShaderNode, position: Vector2, id: int) -> void:
    pass;
func can_connect_nodes(type: int, from_node: int, from_port: int, to_node: int, to_port: int) -> bool:
    pass;
func connect_nodes(type: int, from_node: int, from_port: int, to_node: int, to_port: int) -> int:
    pass;
func connect_nodes_forced(type: int, from_node: int, from_port: int, to_node: int, to_port: int) -> void:
    pass;
func disconnect_nodes(type: int, from_node: int, from_port: int, to_node: int, to_port: int) -> void:
    pass;
func get_node(type: int, id: int) -> VisualShaderNode:
    pass;
func get_node_connections(type: int) -> Array:
    pass;
func get_node_list(type: int) -> PackedInt32Array:
    pass;
func get_node_position(type: int, id: int) -> Vector2:
    pass;
func get_valid_node_id(type: int) -> int:
    pass;
func is_node_connection(type: int, from_node: int, from_port: int, to_node: int, to_port: int) -> bool:
    pass;
func remove_node(type: int, id: int) -> void:
    pass;
func replace_node(type: int, id: int, new_class: StringName) -> void:
    pass;
func set_mode(mode: int) -> void:
    pass;
func set_node_position(type: int, id: int, position: Vector2) -> void:
    pass;
