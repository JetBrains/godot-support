#brief Base resource for [AnimationTree] nodes.
#desc Base resource for [AnimationTree] nodes. In general, it's not used directly, but you can create custom ones with custom blending formulas.
#desc Inherit this when creating nodes mainly for use in [AnimationNodeBlendTree], otherwise [AnimationRootNode] should be used instead.
class_name AnimationNode

#desc Do not use filtering.
const FILTER_IGNORE = 0;

#desc Paths matching the filter will be allowed to pass.
const FILTER_PASS = 1;

#desc Paths matching the filter will be discarded.
const FILTER_STOP = 2;

#desc Paths matching the filter will be blended (by the blend value).
const FILTER_BLEND = 3;


#desc If [code]true[/code], filtering is enabled.
var filter_enabled: bool;



#desc When inheriting from [AnimationRootNode], implement this virtual method to override the text caption for this node.
virtual const func _get_caption() -> String:
	pass;

#desc When inheriting from [AnimationRootNode], implement this virtual method to return a child node by its [param name].
virtual const func _get_child_by_name() -> AnimationNode:
	pass;

#desc When inheriting from [AnimationRootNode], implement this virtual method to return all children nodes in order as a [code]name: node[/code] dictionary.
virtual const func _get_child_nodes() -> Dictionary:
	pass;

#desc When inheriting from [AnimationRootNode], implement this virtual method to return the default value of a [param parameter]. Parameters are custom local memory used for your nodes, given a resource can be reused in multiple trees.
virtual const func _get_parameter_default_value() -> Variant:
	pass;

#desc When inheriting from [AnimationRootNode], implement this virtual method to return a list of the properties on this node. Parameters are custom local memory used for your nodes, given a resource can be reused in multiple trees. Format is similar to [method Object.get_property_list].
virtual const func _get_parameter_list() -> Array:
	pass;

#desc When inheriting from [AnimationRootNode], implement this virtual method to return whether the blend tree editor should display filter editing on this node.
virtual const func _has_filter() -> bool:
	pass;

#desc When inheriting from [AnimationRootNode], implement this virtual method to run some code when this node is processed. The [param time] parameter is a relative delta, unless [param seek] is [code]true[/code], in which case it is absolute.
#desc Here, call the [method blend_input], [method blend_node] or [method blend_animation] functions. You can also use [method get_parameter] and [method set_parameter] to modify local memory.
#desc This function should return the time left for the current animation to finish (if unsure, pass the value from the main blend being called).
virtual const func _process(time: float, seek: bool, seek_root: bool) -> float:
	pass;

#desc Adds an input to the node. This is only useful for nodes created for use in an [AnimationNodeBlendTree].
func add_input() -> void:
	pass;

#desc Blend an animation by [param blend] amount (name must be valid in the linked [AnimationPlayer]). A [param time] and [param delta] may be passed, as well as whether [param seeked] happened.
func blend_animation(animation: StringName, time: float, delta: float, seeked: bool, seek_root: bool, blend: float, pingponged: int) -> void:
	pass;

#desc Blend an input. This is only useful for nodes created for an [AnimationNodeBlendTree]. The [param time] parameter is a relative delta, unless [param seek] is [code]true[/code], in which case it is absolute. A filter mode may be optionally passed (see [enum FilterAction] for options).
func blend_input(input_index: int, time: float, seek: bool, seek_root: bool, blend: float, filter: int, sync: bool) -> float:
	pass;

#desc Blend another animation node (in case this node contains children animation nodes). This function is only useful if you inherit from [AnimationRootNode] instead, else editors will not display your node for addition.
func blend_node(name: StringName, node: AnimationNode, time: float, seek: bool, seek_root: bool, blend: float, filter: int, sync: bool) -> float:
	pass;

#desc Amount of inputs in this node, only useful for nodes that go into [AnimationNodeBlendTree].
func get_input_count() -> int:
	pass;

#desc Gets the name of an input by index.
func get_input_name() -> String:
	pass;

#desc Gets the value of a parameter. Parameters are custom local memory used for your nodes, given a resource can be reused in multiple trees.
func get_parameter() -> Variant:
	pass;

#desc Returns whether the given path is filtered.
func is_path_filtered() -> bool:
	pass;

#desc Removes an input, call this only when inactive.
func remove_input() -> void:
	pass;

#desc Adds or removes a path for the filter.
func set_filter_path(path: NodePath, enable: bool) -> void:
	pass;

#desc Sets a custom parameter. These are used as local memory, because resources can be reused across the tree or scenes.
func set_parameter(name: StringName, value: Variant) -> void:
	pass;


