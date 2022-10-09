extends AnimationRootNode
#brief State machine for control of animations.
#desc Contains multiple nodes representing animation states, connected in a graph. Node transitions can be configured to happen automatically or via code, using a shortest-path algorithm. Retrieve the [AnimationNodeStateMachinePlayback] object from the [AnimationTree] node to control it programmatically.
#desc [b]Example:[/b]
#desc [codeblocks]
#desc [gdscript]
#desc var state_machine = $AnimationTree.get("parameters/playback")
#desc state_machine.travel("some_state")
#desc [/gdscript]
#desc [csharp]
#desc var stateMachine = GetNode<AnimationTree>("AnimationTree").Get("parameters/playback") as AnimationNodeStateMachinePlayback;
#desc stateMachine.Travel("some_state");
#desc [/csharp]
#desc [/codeblocks]
class_name AnimationNodeStateMachine




#desc Adds a new node to the graph. The [param position] is used for display in the editor.
func add_node(name: StringName, node: AnimationNode, position: Vector2) -> void:
	pass;

#desc Adds a transition between the given nodes.
func add_transition(from: StringName, to: StringName, transition: AnimationNodeStateMachineTransition) -> void:
	pass;

#desc Returns the draw offset of the graph. Used for display in the editor.
func get_graph_offset() -> Vector2:
	pass;

#desc Returns the animation node with the given name.
func get_node(name: StringName) -> AnimationNode:
	pass;

#desc Returns the given animation node's name.
func get_node_name(node: AnimationNode) -> StringName:
	pass;

#desc Returns the given node's coordinates. Used for display in the editor.
func get_node_position(name: StringName) -> Vector2:
	pass;

#desc Returns the given transition.
func get_transition(idx: int) -> AnimationNodeStateMachineTransition:
	pass;

#desc Returns the number of connections in the graph.
func get_transition_count() -> int:
	pass;

#desc Returns the given transition's start node.
func get_transition_from(idx: int) -> StringName:
	pass;

#desc Returns the given transition's end node.
func get_transition_to(idx: int) -> StringName:
	pass;

#desc Returns [code]true[/code] if the graph contains the given node.
func has_node(name: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if there is a transition between the given nodes.
func has_transition(from: StringName, to: StringName) -> bool:
	pass;

#desc Deletes the given node from the graph.
func remove_node(name: StringName) -> void:
	pass;

#desc Deletes the transition between the two specified nodes.
func remove_transition(from: StringName, to: StringName) -> void:
	pass;

#desc Deletes the given transition by index.
func remove_transition_by_index(idx: int) -> void:
	pass;

#desc Renames the given node.
func rename_node(name: StringName, new_name: StringName) -> void:
	pass;

func replace_node(name: StringName, node: AnimationNode) -> void:
	pass;

#desc Sets the draw offset of the graph. Used for display in the editor.
func set_graph_offset(offset: Vector2) -> void:
	pass;

#desc Sets the node's coordinates. Used for display in the editor.
func set_node_position(name: StringName, position: Vector2) -> void:
	pass;


