extends AnimationRootNode
#brief Blends linearly between two of any number of [AnimationNode] of any type placed on a virtual axis.
#desc A resource to add to an [AnimationNodeBlendTree].
#desc This is a virtual axis on which you can add any type of [AnimationNode] using [method add_blend_point].
#desc Outputs the linear blend of the two [AnimationNode]s closest to the node's current value.
#desc You can set the extents of the axis using the [member min_space] and [member max_space].
class_name AnimationNodeBlendSpace1D


#desc The blend space's axis's upper limit for the points' position. See [method add_blend_point].
var max_space: float;

#desc The blend space's axis's lower limit for the points' position. See [method add_blend_point].
var min_space: float;

#desc Position increment to snap to when moving a point on the axis.
var snap: float;

#desc If [code]false[/code], the blended animations' frame are stopped when the blend value is [code]0[/code].
#desc If [code]true[/code], forcing the blended animations to advance frame.
var sync: bool;

#desc Label of the virtual axis of the blend space.
var value_label: String;



#desc Adds a new point that represents a [param node] on the virtual axis at a given position set by [param pos]. You can insert it at a specific index using the [param at_index] argument. If you use the default value for [param at_index], the point is inserted at the end of the blend points array.
func add_blend_point(node: AnimationRootNode, pos: float, at_index: int) -> void:
	pass;

#desc Returns the number of points on the blend axis.
func get_blend_point_count() -> int:
	pass;

#desc Returns the [AnimationNode] referenced by the point at index [param point].
func get_blend_point_node(point: int) -> AnimationRootNode:
	pass;

#desc Returns the position of the point at index [param point].
func get_blend_point_position(point: int) -> float:
	pass;

#desc Removes the point at index [param point] from the blend axis.
func remove_blend_point(point: int) -> void:
	pass;

#desc Changes the [AnimationNode] referenced by the point at index [param point].
func set_blend_point_node(point: int, node: AnimationRootNode) -> void:
	pass;

#desc Updates the position of the point at index [param point] on the blend axis.
func set_blend_point_position(point: int, pos: float) -> void:
	pass;


