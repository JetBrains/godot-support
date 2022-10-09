extends AnimationRootNode
#brief Blends linearly between three [AnimationNode] of any type placed in a 2D space.
#desc A resource to add to an [AnimationNodeBlendTree].
#desc This node allows you to blend linearly between three animations using a [Vector2] weight.
#desc You can add vertices to the blend space with [method add_blend_point] and automatically triangulate it by setting [member auto_triangles] to [code]true[/code]. Otherwise, use [method add_triangle] and [method remove_triangle] to create up the blend space by hand.
class_name AnimationNodeBlendSpace2D

#desc The interpolation between animations is linear.
const BLEND_MODE_INTERPOLATED = 0;

#desc The blend space plays the animation of the node the blending position is closest to. Useful for frame-by-frame 2D animations.
const BLEND_MODE_DISCRETE = 1;

#desc Similar to [constant BLEND_MODE_DISCRETE], but starts the new animation at the last animation's playback position.
const BLEND_MODE_DISCRETE_CARRY = 2;


#desc If [code]true[/code], the blend space is triangulated automatically. The mesh updates every time you add or remove points with [method add_blend_point] and [method remove_blend_point].
var auto_triangles: bool;

#desc Controls the interpolation between animations. See [enum BlendMode] constants.
var blend_mode: int;

#desc The blend space's X and Y axes' upper limit for the points' position. See [method add_blend_point].
var max_space: Vector2;

#desc The blend space's X and Y axes' lower limit for the points' position. See [method add_blend_point].
var min_space: Vector2;

#desc Position increment to snap to when moving a point.
var snap: Vector2;

#desc If [code]false[/code], the blended animations' frame are stopped when the blend value is [code]0[/code].
#desc If [code]true[/code], forcing the blended animations to advance frame.
var sync: bool;

#desc Name of the blend space's X axis.
var x_label: String;

#desc Name of the blend space's Y axis.
var y_label: String;



#desc Adds a new point that represents a [param node] at the position set by [param pos]. You can insert it at a specific index using the [param at_index] argument. If you use the default value for [param at_index], the point is inserted at the end of the blend points array.
func add_blend_point(node: AnimationRootNode, pos: Vector2, at_index: int) -> void:
	pass;

#desc Creates a new triangle using three points [param x], [param y], and [param z]. Triangles can overlap. You can insert the triangle at a specific index using the [param at_index] argument. If you use the default value for [param at_index], the point is inserted at the end of the blend points array.
func add_triangle(x: int, y: int, z: int, at_index: int) -> void:
	pass;

#desc Returns the number of points in the blend space.
func get_blend_point_count() -> int:
	pass;

#desc Returns the [AnimationRootNode] referenced by the point at index [param point].
func get_blend_point_node(point: int) -> AnimationRootNode:
	pass;

#desc Returns the position of the point at index [param point].
func get_blend_point_position(point: int) -> Vector2:
	pass;

#desc Returns the number of triangles in the blend space.
func get_triangle_count() -> int:
	pass;

#desc Returns the position of the point at index [param point] in the triangle of index [param triangle].
func get_triangle_point(triangle: int, point: int) -> int:
	pass;

#desc Removes the point at index [param point] from the blend space.
func remove_blend_point(point: int) -> void:
	pass;

#desc Removes the triangle at index [param triangle] from the blend space.
func remove_triangle(triangle: int) -> void:
	pass;

#desc Changes the [AnimationNode] referenced by the point at index [param point].
func set_blend_point_node(point: int, node: AnimationRootNode) -> void:
	pass;

#desc Updates the position of the point at index [param point] on the blend axis.
func set_blend_point_position(point: int, pos: Vector2) -> void:
	pass;


