#brief Vector used for 2D math using floating point coordinates.
#desc 2-element structure that can be used to represent positions in 2D space or any other pair of numeric values.
#desc It uses floating-point coordinates. See [Vector2i] for its integer counterpart.
#desc [b]Note:[/b] In a boolean context, a Vector2 will evaluate to [code]false[/code] if it's equal to [code]Vector2(0, 0)[/code]. Otherwise, a Vector2 will always evaluate to [code]true[/code].
class_name Vector2

#desc Enumerated value for the X axis. Returned by [method max_axis_index] and [method min_axis_index].
const AXIS_X = 0;

#desc Enumerated value for the Y axis. Returned by [method max_axis_index] and [method min_axis_index].
const AXIS_Y = 1;

#desc Zero vector, a vector with all components set to [code]0[/code].
const ZERO = Vector2(0, 0);

#desc One vector, a vector with all components set to [code]1[/code].
const ONE = Vector2(1, 1);

#desc Infinity vector, a vector with all components set to [constant @GDScript.INF].
const INF = Vector2(inf, inf);

#desc Left unit vector. Represents the direction of left.
const LEFT = Vector2(-1, 0);

#desc Right unit vector. Represents the direction of right.
const RIGHT = Vector2(1, 0);

#desc Up unit vector. Y is down in 2D, so this vector points -Y.
const UP = Vector2(0, -1);

#desc Down unit vector. Y is down in 2D, so this vector points +Y.
const DOWN = Vector2(0, 1);


#desc The vector's X component. Also accessible by using the index position [code][0][/code].
var x: float;

#desc The vector's Y component. Also accessible by using the index position [code][1][/code].
var y: float;


#desc Constructs a default-initialized [Vector2] with all components set to [code]0[/code].
func Vector2() -> Vector2:
	pass;

#desc Constructs a [Vector2] as a copy of the given [Vector2].
func Vector2() -> Vector2:
	pass;

#desc Constructs a new [Vector2] from [Vector2i].
func Vector2() -> Vector2:
	pass;

#desc Constructs a new [Vector2] from the given [param x] and [param y].
func Vector2(x: float, y: float) -> Vector2:
	pass;


#desc Returns a new vector with all components in absolute values (i.e. positive).
func abs() -> Vector2:
	pass;

#desc Returns this vector's angle with respect to the positive X axis, or [code](1, 0)[/code] vector, in radians.
#desc For example, [code]Vector2.RIGHT.angle()[/code] will return zero, [code]Vector2.DOWN.angle()[/code] will return [code]PI / 2[/code] (a quarter turn, or 90 degrees), and [code]Vector2(1, -1).angle()[/code] will return [code]-PI / 4[/code] (a negative eighth turn, or -45 degrees).
#desc [url=https://raw.githubusercontent.com/godotengine/godot-docs/master/img/vector2_angle.png]Illustration of the returned angle.[/url]
#desc Equivalent to the result of [method @GlobalScope.atan2] when called with the vector's [member y] and [member x] as parameters: [code]atan2(y, x)[/code].
func angle() -> float:
	pass;

#desc Returns the angle to the given vector, in radians.
#desc [url=https://raw.githubusercontent.com/godotengine/godot-docs/master/img/vector2_angle_to.png]Illustration of the returned angle.[/url]
func angle_to() -> float:
	pass;

#desc Returns the angle between the line connecting the two points and the X axis, in radians.
#desc [code]a.angle_to_point(b)[/code] is equivalent of doing [code](b - a).angle()[/code].
#desc [url=https://raw.githubusercontent.com/godotengine/godot-docs/master/img/vector2_angle_to_point.png]Illustration of the returned angle.[/url]
func angle_to_point() -> float:
	pass;

#desc Returns the aspect ratio of this vector, the ratio of [member x] to [member y].
func aspect() -> float:
	pass;

#desc Returns the point at the given [param t] on the [url=https://en.wikipedia.org/wiki/B%C3%A9zier_curve]Bezier curve[/url] defined by this vector and the given [param control_1], [param control_2], and [param end] points.
func bezier_interpolate(control_1: Vector2, control_2: Vector2, end: Vector2, t: float) -> Vector2:
	pass;

#desc Returns the vector "bounced off" from a plane defined by the given normal.
func bounce() -> Vector2:
	pass;

#desc Returns a new vector with all components rounded up (towards positive infinity).
func ceil() -> Vector2:
	pass;

#desc Returns a new vector with all components clamped between the components of [param min] and [param max], by running [method @GlobalScope.clamp] on each component.
func clamp(min: Vector2, max: Vector2) -> Vector2:
	pass;

#desc Returns the 2D analog of the cross product for this vector and [param with].
#desc This is the signed area of the parallelogram formed by the two vectors. If the second vector is clockwise from the first vector, then the cross product is the positive area. If counter-clockwise, the cross product is the negative area.
#desc [b]Note:[/b] Cross product is not defined in 2D mathematically. This method embeds the 2D vectors in the XY plane of 3D space and uses their cross product's Z component as the analog.
func cross() -> float:
	pass;

#desc Cubically interpolates between this vector and [param b] using [param pre_a] and [param post_b] as handles, and returns the result at position [param weight]. [param weight] is on the range of 0.0 to 1.0, representing the amount of interpolation.
func cubic_interpolate(b: Vector2, pre_a: Vector2, post_b: Vector2, weight: float) -> Vector2:
	pass;

#desc Cubically interpolates between this vector and [param b] using [param pre_a] and [param post_b] as handles, and returns the result at position [param weight]. [param weight] is on the range of 0.0 to 1.0, representing the amount of interpolation.
#desc It can perform smoother interpolation than [code]cubic_interpolate()[/code] by the time values.
func cubic_interpolate_in_time(b: Vector2, pre_a: Vector2, post_b: Vector2, weight: float, b_t: float, pre_a_t: float, post_b_t: float) -> Vector2:
	pass;

#desc Returns the normalized vector pointing from this vector to [param to]. This is equivalent to using [code](b - a).normalized()[/code].
func direction_to() -> Vector2:
	pass;

#desc Returns the squared distance between this vector and [param to].
#desc This method runs faster than [method distance_to], so prefer it if you need to compare vectors or need the squared distance for some formula.
func distance_squared_to() -> float:
	pass;

#desc Returns the distance between this vector and [param to].
func distance_to() -> float:
	pass;

#desc Returns the dot product of this vector and [param with]. This can be used to compare the angle between two vectors. For example, this can be used to determine whether an enemy is facing the player.
#desc The dot product will be [code]0[/code] for a straight angle (90 degrees), greater than 0 for angles narrower than 90 degrees and lower than 0 for angles wider than 90 degrees.
#desc When using unit (normalized) vectors, the result will always be between [code]-1.0[/code] (180 degree angle) when the vectors are facing opposite directions, and [code]1.0[/code] (0 degree angle) when the vectors are aligned.
#desc [b]Note:[/b] [code]a.dot(b)[/code] is equivalent to [code]b.dot(a)[/code].
func dot() -> float:
	pass;

#desc Returns a new vector with all components rounded down (towards negative infinity).
func floor() -> Vector2:
	pass;

#desc Creates a unit [Vector2] rotated to the given [param angle] in radians. This is equivalent to doing [code]Vector2(cos(angle), sin(angle))[/code] or [code]Vector2.RIGHT.rotated(angle)[/code].
#desc [codeblock]
#desc print(Vector2.from_angle(0)) # Prints (1, 0).
#desc print(Vector2(1, 0).angle()) # Prints 0, which is the angle used above.
#desc print(Vector2.from_angle(PI / 2)) # Prints (0, 1).
#desc [/codeblock]
static func from_angle() -> Vector2:
	pass;

#desc Returns [code]true[/code] if this vector and [code]v[/code] are approximately equal, by running [method @GlobalScope.is_equal_approx] on each component.
func is_equal_approx() -> bool:
	pass;

#desc Returns [code]true[/code] if the vector is normalized, [code]false[/code] otherwise.
func is_normalized() -> bool:
	pass;

#desc Returns [code]true[/code] if this vector's values are approximately zero, by running [method @GlobalScope.is_zero_approx] on each component.
#desc This method is faster than using [method is_equal_approx] with one value as a zero vector.
func is_zero_approx() -> bool:
	pass;

#desc Returns the length (magnitude) of this vector.
func length() -> float:
	pass;

#desc Returns the squared length (squared magnitude) of this vector.
#desc This method runs faster than [method length], so prefer it if you need to compare vectors or need the squared distance for some formula.
func length_squared() -> float:
	pass;

#desc Returns the result of the linear interpolation between this vector and [param to] by amount [param weight]. [param weight] is on the range of 0.0 to 1.0, representing the amount of interpolation.
func lerp(to: Vector2, weight: float) -> Vector2:
	pass;

#desc Returns the vector with a maximum length by limiting its length to [param length].
func limit_length() -> Vector2:
	pass;

#desc Returns the axis of the vector's highest value. See [code]AXIS_*[/code] constants. If all components are equal, this method returns [constant AXIS_X].
func max_axis_index() -> int:
	pass;

#desc Returns the axis of the vector's lowest value. See [code]AXIS_*[/code] constants. If all components are equal, this method returns [constant AXIS_Y].
func min_axis_index() -> int:
	pass;

#desc Returns a new vector moved toward [param to] by the fixed [param delta] amount. Will not go past the final value.
func move_toward(to: Vector2, delta: float) -> Vector2:
	pass;

#desc Returns the vector scaled to unit length. Equivalent to [code]v / v.length()[/code].
func normalized() -> Vector2:
	pass;

#desc Returns a perpendicular vector rotated 90 degrees counter-clockwise compared to the original, with the same length.
func orthogonal() -> Vector2:
	pass;

#desc Returns a vector composed of the [method @GlobalScope.fposmod] of this vector's components and [param mod].
func posmod() -> Vector2:
	pass;

#desc Returns a vector composed of the [method @GlobalScope.fposmod] of this vector's components and [param modv]'s components.
func posmodv() -> Vector2:
	pass;

#desc Returns this vector projected onto the vector [code]b[/code].
func project() -> Vector2:
	pass;

#desc Returns the vector reflected (i.e. mirrored, or symmetric) over a line defined by the given direction vector [param n].
func reflect() -> Vector2:
	pass;

#desc Returns the vector rotated by [param angle] (in radians). See also [method @GlobalScope.deg_to_rad].
func rotated() -> Vector2:
	pass;

#desc Returns a new vector with all components rounded to the nearest integer, with halfway cases rounded away from zero.
func round() -> Vector2:
	pass;

#desc Returns a new vector with each component set to one or negative one, depending on the signs of the components, or zero if the component is zero, by calling [method @GlobalScope.sign] on each component.
func sign() -> Vector2:
	pass;

#desc Returns the result of spherical linear interpolation between this vector and [param to], by amount [param weight]. [param weight] is on the range of 0.0 to 1.0, representing the amount of interpolation.
#desc This method also handles interpolating the lengths if the input vectors have different lengths. For the special case of one or both input vectors having zero length, this method behaves like [method lerp].
func slerp(to: Vector2, weight: float) -> Vector2:
	pass;

#desc Returns this vector slid along a plane defined by the given normal.
func slide() -> Vector2:
	pass;

#desc Returns this vector with each component snapped to the nearest multiple of [param step]. This can also be used to round to an arbitrary number of decimals.
func snapped() -> Vector2:
	pass;


