#brief A custom shader program with a visual editor.
#desc This class allows you to define a custom shader program that can be used for various materials to render objects.
#desc The visual shader editor creates the shader.
class_name VisualShader

#desc A vertex shader, operating on vertices.
const TYPE_VERTEX = 0;

#desc A fragment shader, operating on fragments (pixels).
const TYPE_FRAGMENT = 1;

#desc A shader for light calculations.
const TYPE_LIGHT = 2;

const TYPE_START = 3;

const TYPE_PROCESS = 4;

const TYPE_COLLIDE = 5;

const TYPE_START_CUSTOM = 6;

const TYPE_PROCESS_CUSTOM = 7;

const TYPE_SKY = 8;

#desc A compute shader that runs for each froxel of the volumetric fog map.
const TYPE_FOG = 9;

#desc Represents the size of the [enum Type] enum.
const TYPE_MAX = 10;

const VARYING_MODE_VERTEX_TO_FRAG_LIGHT = 0;

const VARYING_MODE_FRAG_TO_LIGHT = 1;

const VARYING_MODE_MAX = 2;

const VARYING_TYPE_FLOAT = 0;

const VARYING_TYPE_INT = 1;

const VARYING_TYPE_VECTOR_2D = 2;

const VARYING_TYPE_VECTOR_3D = 3;

const VARYING_TYPE_VECTOR_4D = 4;

const VARYING_TYPE_BOOLEAN = 5;

const VARYING_TYPE_TRANSFORM = 6;

const VARYING_TYPE_MAX = 7;

const NODE_ID_INVALID = -1;

const NODE_ID_OUTPUT = 0;


#desc The offset vector of the whole graph.
var graph_offset: Vector2;



#desc Adds the specified [param node] to the shader.
func add_node(type: int, node: VisualShaderNode, position: Vector2, id: int) -> void:
	pass;

func add_varying(name: String, mode: int, type: int) -> void:
	pass;

#desc Returns [code]true[/code] if the specified nodes and ports can be connected together.
func can_connect_nodes(type: int, from_node: int, from_port: int, to_node: int, to_port: int) -> bool:
	pass;

#desc Connects the specified nodes and ports.
func connect_nodes(type: int, from_node: int, from_port: int, to_node: int, to_port: int) -> int:
	pass;

#desc Connects the specified nodes and ports, even if they can't be connected. Such connection is invalid and will not function properly.
func connect_nodes_forced(type: int, from_node: int, from_port: int, to_node: int, to_port: int) -> void:
	pass;

#desc Connects the specified nodes and ports.
func disconnect_nodes(type: int, from_node: int, from_port: int, to_node: int, to_port: int) -> void:
	pass;

#desc Returns the shader node instance with specified [param type] and [param id].
func get_node(type: int, id: int) -> VisualShaderNode:
	pass;

#desc Returns the list of connected nodes with the specified type.
func get_node_connections() -> Dictionary[]:
	pass;

#desc Returns the list of all nodes in the shader with the specified type.
func get_node_list() -> PackedInt32Array:
	pass;

#desc Returns the position of the specified node within the shader graph.
func get_node_position(type: int, id: int) -> Vector2:
	pass;

func get_valid_node_id() -> int:
	pass;

func has_varying() -> bool:
	pass;

#desc Returns [code]true[/code] if the specified node and port connection exist.
func is_node_connection(type: int, from_node: int, from_port: int, to_node: int, to_port: int) -> bool:
	pass;

#desc Removes the specified node from the shader.
func remove_node(type: int, id: int) -> void:
	pass;

func remove_varying() -> void:
	pass;

#desc Replaces the specified node with a node of new class type.
func replace_node(type: int, id: int, new_class: StringName) -> void:
	pass;

#desc Sets the mode of this shader.
func set_mode() -> void:
	pass;

#desc Sets the position of the specified node.
func set_node_position(type: int, id: int, position: Vector2) -> void:
	pass;


