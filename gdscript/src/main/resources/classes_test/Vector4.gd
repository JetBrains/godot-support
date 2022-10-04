#brief Vector used for 4D math using floating point coordinates.
#desc 4-element structure that can be used to represent any quadruplet of numeric values.
#desc It uses floating-point coordinates. See [Vector4i] for its integer counterpart.
#desc [b]Note:[/b] In a boolean context, a Vector4 will evaluate to [code]false[/code] if it's equal to [code]Vector4(0, 0, 0, 0)[/code]. Otherwise, a Vector4 will always evaluate to [code]true[/code].
class_name Vector4

#desc Enumerated value for the X axis. Returned by [method max_axis_index] and [method min_axis_index].
const AXIS_X = 0;

#desc Enumerated value for the Y axis. Returned by [method max_axis_index] and [method min_axis_index].
const AXIS_Y = 1;

#desc Enumerated value for the Z axis. Returned by [method max_axis_index] and [method min_axis_index].
const AXIS_Z = 2;

#desc Enumerated value for the W axis. Returned by [method max_axis_index] and [method min_axis_index].
const AXIS_W = 3;

#desc Zero vector, a vector with all components set to [code]0[/code].
const ZERO = Vector4(0, 0, 0, 0);

#desc One vector, a vector with all components set to [code]1[/code].
const ONE = Vector4(1, 1, 1, 1);

#desc Infinity vector, a vector with all components set to [constant @GDScript.INF].
const INF = Vector4(inf, inf, inf, inf);


#desc The vector's W component. Also accessible by using the index position [code][3][/code].
var w: float;

#desc The vector's X component. Also accessible by using the index position [code][0][/code].
var x: float;

#desc The vector's Y component. Also accessible by using the index position [code][1][/code].
var y: float;

#desc The vector's Z component. Also accessible by using the index position [code][2][/code].
var z: float;


#desc Constructs a default-initialized [Vector4] with all components set to [code]0[/code].
func Vector4() -> Vector4:
	pass;

#desc Constructs a [Vector4] as a copy of the given [Vector4].
func Vector4() -> Vector4:
	pass;

#desc Constructs a new [Vector4] from the given [Vector4i].
func Vector4() -> Vector4:
	pass;

#desc Returns a [Vector4] with the given components.
func Vector4(x: float, y: float, z: float, w: float) -> Vector4:
	pass;


#desc Returns a new vector with all components in absolute values (i.e. positive).
func abs() -> Vector4:
	pass;

#desc Returns a new vector with all components rounded up (towards positive infinity).
func ceil() -> Vector4:
	pass;

#desc Returns a new vector with all components clamped between the components of [param min] and [param max], by running [method @GlobalScope.clamp] on each component.
func clamp(min: Vector4, max: Vector4) -> Vector4:
	pass;

#desc Performs a cubic interpolation between this vector and [param b] using [param pre_a] and [param post_b] as handles, and returns the result at position [param weight]. [param weight] is on the range of 0.0 to 1.0, representing the amount of interpolation.
func cubic_interpolate(b: Vector4, pre_a: Vector4, post_b: Vector4, weight: float) -> Vector4:
	pass;

#desc Performs a cubic interpolation between this vector and [param b] using [param pre_a] and [param post_b] as handles, and returns the result at position [param weight]. [param weight] is on the range of 0.0 to 1.0, representing the amount of interpolation.
#desc It can perform smoother interpolation than [code]cubic_interpolate()[/code] by the time values.
func cubic_interpolate_in_time(b: Vector4, pre_a: Vector4, post_b: Vector4, weight: float, b_t: float, pre_a_t: float, post_b_t: float) -> Vector4:
	pass;

#desc Returns the normalized vector pointing from this vector to [param to]. This is equivalent to using [code](b - a).normalized()[/code].
func direction_to() -> Vector4:
	pass;

#desc Returns the squared distance between this vector and [param to].
#desc This method runs faster than [method distance_to], so prefer it if you need to compare vectors or need the squared distance for some formula.
func distance_squared_to() -> float:
	pass;

#desc Returns the distance between this vector and [param to].
func distance_to() -> float:
	pass;

#desc Returns the dot product of this vector and [param with].
func dot() -> float:
	pass;

#desc Returns a new vector with all components rounded down (towards negative infinity).
func floor() -> Vector4:
	pass;

#desc Returns the inverse of the vector. This is the same as [code]Vector4(1.0 / v.x, 1.0 / v.y, 1.0 / v.z, 1.0 / v.w)[/code].
func inverse() -> Vector4:
	pass;

#desc Returns [code]true[/code] if this vector and [param with] are approximately equal, by running [method @GlobalScope.is_equal_approx] on each component.
func is_equal_approx() -> bool:
	pass;

#desc Returns [code]true[/code] if the vector is normalized, i.e. its length is equal to 1.
func is_normalized() -> bool:
	pass;

#desc Returns [code]true[/code] if this vector's values are approximately zero, by running [method @GlobalScope.is_zero_approx] on each component.
#desc This method is faster than using [method is_equal_approx] with one value as a zero vector.
func is_zero_approx() -> bool:
	pass;

#desc Returns the length (magnitude) of this vector.
func length() -> float:
	pass;

#desc Returns the squared length (squared magnitude) of this vector. This method runs faster than [method length].
func length_squared() -> float:
	pass;

#desc Returns the result of the linear interpolation between this vector and [param to] by amount [param weight]. [param weight] is on the range of [code]0.0[/code] to [code]1.0[/code], representing the amount of interpolation.
func lerp(to: Vector4, weight: float) -> Vector4:
	pass;

#desc Returns the axis of the vector's highest value. See [code]AXIS_*[/code] constants. If all components are equal, this method returns [constant AXIS_X].
func max_axis_index() -> int:
	pass;

#desc Returns the axis of the vector's lowest value. See [code]AXIS_*[/code] constants. If all components are equal, this method returns [constant AXIS_W].
func min_axis_index() -> int:
	pass;

#desc Returns the vector scaled to unit length. Equivalent to [code]v / v.length()[/code].
func normalized() -> Vector4:
	pass;

#desc Returns a vector composed of the [method @GlobalScope.fposmod] of this vector's components and [param mod].
func posmod() -> Vector4:
	pass;

#desc Returns a vector composed of the [method @GlobalScope.fposmod] of this vector's components and [param modv]'s components.
func posmodv() -> Vector4:
	pass;

#desc Returns a new vector with all components rounded to the nearest integer, with halfway cases rounded away from zero.
func round() -> Vector4:
	pass;

#desc Returns a new vector with each component set to one or negative one, depending on the signs of the components, or zero if the component is zero, by calling [method @GlobalScope.sign] on each component.
func sign() -> Vector4:
	pass;

#desc Returns this vector with each component snapped to the nearest multiple of [param step]. This can also be used to round to an arbitrary number of decimals.
func snapped() -> Vector4:
	pass;


