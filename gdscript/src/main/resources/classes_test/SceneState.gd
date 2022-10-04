extends RefCounted
#brief A script interface to a scene file's data.
#desc Maintains a list of resources, nodes, exported, and overridden properties, and built-in scripts associated with a scene.
#desc This class cannot be instantiated directly, it is retrieved for a given scene as the result of [method PackedScene.get_state].
class_name SceneState

#desc If passed to [method PackedScene.instantiate], blocks edits to the scene state.
const GEN_EDIT_STATE_DISABLED = 0;

#desc If passed to [method PackedScene.instantiate], provides inherited scene resources to the local scene.
#desc [b]Note:[/b] Only available in editor builds.
const GEN_EDIT_STATE_INSTANCE = 1;

#desc If passed to [method PackedScene.instantiate], provides local scene resources to the local scene. Only the main scene should receive the main edit state.
#desc [b]Note:[/b] Only available in editor builds.
const GEN_EDIT_STATE_MAIN = 2;

#desc If passed to [method PackedScene.instantiate], it's similar to [constant GEN_EDIT_STATE_MAIN], but for the case where the scene is being instantiated to be the base of another one.
#desc [b]Note:[/b] Only available in editor builds.
const GEN_EDIT_STATE_MAIN_INHERITED = 3;




#desc Returns the list of bound parameters for the signal at [param idx].
func get_connection_binds(idx: int) -> Array:
	pass;

#desc Returns the number of signal connections in the scene.
#desc The [code]idx[/code] argument used to query connection metadata in other [code]get_connection_*[/code] methods in the interval [code][0, get_connection_count() - 1][/code].
func get_connection_count() -> int:
	pass;

#desc Returns the connection flags for the signal at [param idx]. See [enum Object.ConnectFlags] constants.
func get_connection_flags(idx: int) -> int:
	pass;

#desc Returns the method connected to the signal at [param idx].
func get_connection_method(idx: int) -> StringName:
	pass;

#desc Returns the name of the signal at [param idx].
func get_connection_signal(idx: int) -> StringName:
	pass;

#desc Returns the path to the node that owns the signal at [param idx], relative to the root node.
func get_connection_source(idx: int) -> NodePath:
	pass;

#desc Returns the path to the node that owns the method connected to the signal at [param idx], relative to the root node.
func get_connection_target(idx: int) -> NodePath:
	pass;

#desc Returns the number of unbound parameters for the signal at [param idx].
func get_connection_unbinds(idx: int) -> int:
	pass;

#desc Returns the number of nodes in the scene.
#desc The [code]idx[/code] argument used to query node data in other [code]get_node_*[/code] methods in the interval [code][0, get_node_count() - 1][/code].
func get_node_count() -> int:
	pass;

#desc Returns the list of group names associated with the node at [param idx].
func get_node_groups(idx: int) -> PackedStringArray:
	pass;

#desc Returns the node's index, which is its position relative to its siblings. This is only relevant and saved in scenes for cases where new nodes are added to an instantiated or inherited scene among siblings from the base scene. Despite the name, this index is not related to the [param idx] argument used here and in other methods.
func get_node_index(idx: int) -> int:
	pass;

#desc Returns a [PackedScene] for the node at [param idx] (i.e. the whole branch starting at this node, with its child nodes and resources), or [code]null[/code] if the node is not an instance.
func get_node_instance(idx: int) -> PackedScene:
	pass;

#desc Returns the path to the represented scene file if the node at [param idx] is an [InstancePlaceholder].
func get_node_instance_placeholder(idx: int) -> String:
	pass;

#desc Returns the name of the node at [param idx].
func get_node_name(idx: int) -> StringName:
	pass;

#desc Returns the path to the owner of the node at [param idx], relative to the root node.
func get_node_owner_path(idx: int) -> NodePath:
	pass;

#desc Returns the path to the node at [param idx].
#desc If [param for_parent] is [code]true[/code], returns the path of the [param idx] node's parent instead.
func get_node_path(idx: int, for_parent: bool) -> NodePath:
	pass;

#desc Returns the number of exported or overridden properties for the node at [param idx].
#desc The [code]prop_idx[/code] argument used to query node property data in other [code]get_node_property_*[/code] methods in the interval [code][0, get_node_property_count() - 1][/code].
func get_node_property_count(idx: int) -> int:
	pass;

#desc Returns the name of the property at [param prop_idx] for the node at [param idx].
func get_node_property_name(idx: int, prop_idx: int) -> StringName:
	pass;

#desc Returns the value of the property at [param prop_idx] for the node at [param idx].
func get_node_property_value(idx: int, prop_idx: int) -> Variant:
	pass;

#desc Returns the type of the node at [param idx].
func get_node_type(idx: int) -> StringName:
	pass;

#desc Returns [code]true[/code] if the node at [param idx] is an [InstancePlaceholder].
func is_node_instance_placeholder(idx: int) -> bool:
	pass;


