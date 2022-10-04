#brief Describes a Bézier curve in 2D space.
#desc This class describes a Bézier curve in 2D space. It is mainly used to give a shape to a [Path2D], but can be manually sampled for other purposes.
#desc It keeps a cache of precalculated points along the curve, to speed up further calculations.
class_name Curve2D


#desc The distance in pixels between two adjacent cached points. Changing it forces the cache to be recomputed the next time the [method get_baked_points] or [method get_baked_length] function is called. The smaller the distance, the more points in the cache and the more memory it will consume, so use with care.
var bake_interval: float;

#desc The number of points describing the curve.
var point_count: int;



#desc Adds a point with the specified [param position] relative to the curve's own position, with control points [param in] and [param out]. Appends the new point at the end of the point list.
#desc If [param index] is given, the new point is inserted before the existing point identified by index [param index]. Every existing point starting from [param index] is shifted further down the list of points. The index must be greater than or equal to [code]0[/code] and must not exceed the number of existing points in the line. See [member point_count].
func add_point(position: Vector2, in: Vector2, out: Vector2, index: int) -> void:
	pass;

#desc Removes all points from the curve.
func clear_points() -> void:
	pass;

#desc Returns the total length of the curve, based on the cached points. Given enough density (see [member bake_interval]), it should be approximate enough.
func get_baked_length() -> float:
	pass;

#desc Returns the cache of points as a [PackedVector2Array].
func get_baked_points() -> PackedVector2Array:
	pass;

#desc Returns the closest offset to [param to_point]. This offset is meant to be used in [method sample_baked].
#desc [param to_point] must be in this curve's local space.
func get_closest_offset(to_point: Vector2) -> float:
	pass;

#desc Returns the closest baked point (in curve's local space) to [param to_point].
#desc [param to_point] must be in this curve's local space.
func get_closest_point(to_point: Vector2) -> Vector2:
	pass;

#desc Returns the position of the control point leading to the vertex [param idx]. The returned position is relative to the vertex [param idx]. If the index is out of bounds, the function sends an error to the console, and returns [code](0, 0)[/code].
func get_point_in(idx: int) -> Vector2:
	pass;

#desc Returns the position of the control point leading out of the vertex [param idx]. The returned position is relative to the vertex [param idx]. If the index is out of bounds, the function sends an error to the console, and returns [code](0, 0)[/code].
func get_point_out(idx: int) -> Vector2:
	pass;

#desc Returns the position of the vertex [param idx]. If the index is out of bounds, the function sends an error to the console, and returns [code](0, 0)[/code].
func get_point_position(idx: int) -> Vector2:
	pass;

#desc Deletes the point [code]idx[/code] from the curve. Sends an error to the console if [code]idx[/code] is out of bounds.
func remove_point(idx: int) -> void:
	pass;

#desc Returns the position between the vertex [param idx] and the vertex [code]idx + 1[/code], where [param t] controls if the point is the first vertex ([code]t = 0.0[/code]), the last vertex ([code]t = 1.0[/code]), or in between. Values of [param t] outside the range ([code]0.0 >= t <=1[/code]) give strange, but predictable results.
#desc If [param idx] is out of bounds it is truncated to the first or last vertex, and [param t] is ignored. If the curve has no points, the function sends an error to the console, and returns [code](0, 0)[/code].
func sample(idx: int, t: float) -> Vector2:
	pass;

#desc Returns a point within the curve at position [param offset], where [param offset] is measured as a pixel distance along the curve.
#desc To do that, it finds the two cached points where the [param offset] lies between, then interpolates the values. This interpolation is cubic if [param cubic] is set to [code]true[/code], or linear if set to [code]false[/code].
#desc Cubic interpolation tends to follow the curves better, but linear is faster (and often, precise enough).
func sample_baked(offset: float, cubic: bool) -> Vector2:
	pass;

#desc Returns the position at the vertex [param fofs]. It calls [method sample] using the integer part of [param fofs] as [code]idx[/code], and its fractional part as [code]t[/code].
func samplef(fofs: float) -> Vector2:
	pass;

#desc Sets the position of the control point leading to the vertex [param idx]. If the index is out of bounds, the function sends an error to the console. The position is relative to the vertex.
func set_point_in(idx: int, position: Vector2) -> void:
	pass;

#desc Sets the position of the control point leading out of the vertex [param idx]. If the index is out of bounds, the function sends an error to the console. The position is relative to the vertex.
func set_point_out(idx: int, position: Vector2) -> void:
	pass;

#desc Sets the position for the vertex [param idx]. If the index is out of bounds, the function sends an error to the console.
func set_point_position(idx: int, position: Vector2) -> void:
	pass;

#desc Returns a list of points along the curve, with a curvature controlled point density. That is, the curvier parts will have more points than the straighter parts.
#desc This approximation makes straight segments between each point, then subdivides those segments until the resulting shape is similar enough.
#desc [param max_stages] controls how many subdivisions a curve segment may face before it is considered approximate enough. Each subdivision splits the segment in half, so the default 5 stages may mean up to 32 subdivisions per curve segment. Increase with care!
#desc [param tolerance_degrees] controls how many degrees the midpoint of a segment may deviate from the real curve, before the segment has to be subdivided.
func tessellate(max_stages: int, tolerance_degrees: float) -> PackedVector2Array:
	pass;


