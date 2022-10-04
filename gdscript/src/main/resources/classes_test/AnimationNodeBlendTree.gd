#brief [AnimationTree] node resource that contains many blend type nodes.
#desc This node may contain a sub-tree of any other blend type nodes, such as [AnimationNodeTransition], [AnimationNodeBlend2], [AnimationNodeBlend3], [AnimationNodeOneShot], etc. This is one of the most commonly used roots.
#desc An [AnimationNodeOutput] node named [code]output[/code] is created by default.
class_name AnimationNodeBlendTree

#desc The connection was successful.
const CONNECTION_OK = 0;

#desc The input node is [code]null[/code].
const CONNECTION_ERROR_NO_INPUT = 1;

#desc The specified input port is out of range.
const CONNECTION_ERROR_NO_INPUT_INDEX = 2;

#desc The output node is [code]null[/code].
const CONNECTION_ERROR_NO_OUTPUT = 3;

#desc Input and output nodes are the same.
const CONNECTION_ERROR_SAME_NODE = 4;

#desc The specified connection already exists.
const CONNECTION_ERROR_CONNECTION_EXISTS = 5;


#desc The global offset of all sub-nodes.
var graph_offset: Vector2;



#desc Adds an [AnimationNode] at the given [param position]. The [param name] is used to identify the created sub-node later.
func add_node(name: StringName, node: AnimationNode, position: Vector2) -> void:
	pass;

#desc Connects the output of an [AnimationNode] as input for another [AnimationNode], at the input port specified by [param input_index].
func connect_node(input_node: StringName, input_index: int, output_node: StringName) -> void:
	pass;

#desc Disconnects the node connected to the specified input.
func disconnect_node(input_node: StringName, input_index: int) -> void:
	pass;

#desc Returns the sub-node with the specified [param name].
func get_node() -> AnimationNode:
	pass;

#desc Returns the position of the sub-node with the specified [param name].
func get_node_position() -> Vector2:
	pass;

#desc Returns [code]true[/code] if a sub-node with specified [param name] exists.
func has_node() -> bool:
	pass;

#desc Removes a sub-node.
func remove_node() -> void:
	pass;

#desc Changes the name of a sub-node.
func rename_node(name: StringName, new_name: StringName) -> void:
	pass;

#desc Modifies the position of a sub-node.
func set_node_position(name: StringName, position: Vector2) -> void:
	pass;


