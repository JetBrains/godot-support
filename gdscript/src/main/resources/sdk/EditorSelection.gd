extends Object
#brief Manages the SceneTree selection in the editor.
#desc This object manages the SceneTree selection in the editor.
#desc [b]Note:[/b] This class shouldn't be instantiated directly. Instead, access the singleton using [method EditorInterface.get_selection].
class_name EditorSelection




#desc Adds a node to the selection.
#desc [b]Note:[/b] The newly selected node will not be automatically edited in the inspector. If you want to edit a node, use [method EditorInterface.edit_node].
func add_node(node: Node) -> void:
	pass;

#desc Clear the selection.
func clear() -> void:
	pass;

#desc Gets the list of selected nodes.
func get_selected_nodes() -> Array[Node]:
	pass;

#desc Gets the list of selected nodes, optimized for transform operations (i.e. moving them, rotating, etc). This list avoids situations where a node is selected and also child/grandchild.
func get_transformable_selected_nodes() -> Array[Node]:
	pass;

#desc Removes a node from the selection.
func remove_node(node: Node) -> void:
	pass;


