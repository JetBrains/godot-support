#brief A mathematic curve.
#desc A curve that can be saved and re-used for other objects. By default, it ranges between [code]0[/code] and [code]1[/code] on the Y axis and positions points relative to the [code]0.5[/code] Y position.
#desc See also [Gradient] which is designed for color interpolation. See also [Curve2D] and [Curve3D].
class_name Curve

#desc The tangent on this side of the point is user-defined.
const TANGENT_FREE = 0;

#desc The curve calculates the tangent on this side of the point as the slope halfway towards the adjacent point.
const TANGENT_LINEAR = 1;

#desc The total number of available tangent modes.
const TANGENT_MODE_COUNT = 2;


#desc The number of points to include in the baked (i.e. cached) curve data.
var bake_resolution: int;

#desc The maximum value the curve can reach.
var max_value: float;

#desc The minimum value the curve can reach.
var min_value: float;

#desc The number of points describing the curve.
var point_count: int;



#desc Adds a point to the curve. For each side, if the [code]*_mode[/code] is [constant TANGENT_LINEAR], the [code]*_tangent[/code] angle (in degrees) uses the slope of the curve halfway to the adjacent point. Allows custom assignments to the [code]*_tangent[/code] angle if [code]*_mode[/code] is set to [constant TANGENT_FREE].
func add_point(position: Vector2, left_tangent: float, right_tangent: float, left_mode: int, right_mode: int) -> int:
	pass;

#desc Recomputes the baked cache of points for the curve.
func bake() -> void:
	pass;

#desc Removes points that are closer than [code]CMP_EPSILON[/code] (0.00001) units to their neighbor on the curve.
func clean_dupes() -> void:
	pass;

#desc Removes all points from the curve.
func clear_points() -> void:
	pass;

#desc Returns the left [enum TangentMode] for the point at [param index].
func get_point_left_mode() -> int:
	pass;

#desc Returns the left tangent angle (in degrees) for the point at [param index].
func get_point_left_tangent() -> float:
	pass;

#desc Returns the curve coordinates for the point at [param index].
func get_point_position() -> Vector2:
	pass;

#desc Returns the right [enum TangentMode] for the point at [param index].
func get_point_right_mode() -> int:
	pass;

#desc Returns the right tangent angle (in degrees) for the point at [param index].
func get_point_right_tangent() -> float:
	pass;

#desc Removes the point at [code]index[/code] from the curve.
func remove_point() -> void:
	pass;

#desc Returns the Y value for the point that would exist at the X position [param offset] along the curve.
func sample() -> float:
	pass;

#desc Returns the Y value for the point that would exist at the X position [param offset] along the curve using the baked cache. Bakes the curve's points if not already baked.
func sample_baked() -> float:
	pass;

#desc Sets the left [enum TangentMode] for the point at [param index] to [param mode].
func set_point_left_mode(index: int, mode: int) -> void:
	pass;

#desc Sets the left tangent angle for the point at [param index] to [param tangent].
func set_point_left_tangent(index: int, tangent: float) -> void:
	pass;

#desc Sets the offset from [code]0.5[/code].
func set_point_offset(index: int, offset: float) -> int:
	pass;

#desc Sets the right [enum TangentMode] for the point at [param index] to [param mode].
func set_point_right_mode(index: int, mode: int) -> void:
	pass;

#desc Sets the right tangent angle for the point at [param index] to [param tangent].
func set_point_right_tangent(index: int, tangent: float) -> void:
	pass;

#desc Assigns the vertical position [param y] to the point at [param index].
func set_point_value(index: int, y: float) -> void:
	pass;


