#brief Vector used for 4D math using integer coordinates.
#desc 4-element structure that can be used to represent 4D grid coordinates or sets of integers.
#desc It uses integer coordinates. See [Vector4] for its floating-point counterpart.
class_name Vector4i

#desc Enumerated value for the X axis. Returned by [method max_axis_index] and [method min_axis_index].
const AXIS_X = 0;

#desc Enumerated value for the Y axis. Returned by [method max_axis_index] and [method min_axis_index].
const AXIS_Y = 1;

#desc Enumerated value for the Z axis. Returned by [method max_axis_index] and [method min_axis_index].
const AXIS_Z = 2;

#desc Enumerated value for the W axis. Returned by [method max_axis_index] and [method min_axis_index].
const AXIS_W = 3;

#desc Zero vector, a vector with all components set to [code]0[/code].
const ZERO = Vector4i(0, 0, 0, 0);

#desc One vector, a vector with all components set to [code]1[/code].
const ONE = Vector4i(1, 1, 1, 1);


#desc The vector's W component. Also accessible by using the index position [code][3][/code].
var w: int;

#desc The vector's X component. Also accessible by using the index position [code][0][/code].
var x: int;

#desc The vector's Y component. Also accessible by using the index position [code][1][/code].
var y: int;

#desc The vector's Z component. Also accessible by using the index position [code][2][/code].
var z: int;


#desc Constructs a default-initialized [Vector4i] with all components set to [code]0[/code].
func Vector4i() -> Vector4i:
	pass;

#desc Constructs a [Vector4i] as a copy of the given [Vector4i].
func Vector4i(from: Vector4i) -> Vector4i:
	pass;

#desc Constructs a new [Vector4i] from the given [Vector4].
func Vector4i(from: Vector4) -> Vector4i:
	pass;

#desc Returns a [Vector4i] with the given components.
func Vector4i(x: int, y: int, z: int, w: int) -> Vector4i:
	pass;


#desc Returns a new vector with all components in absolute values (i.e. positive).
func abs() -> Vector4i:
	pass;

#desc Returns a new vector with all components clamped between the components of [param min] and [param max], by running [method @GlobalScope.clamp] on each component.
func clamp(min: Vector4i, max: Vector4i) -> Vector4i:
	pass;

#desc Returns the length (magnitude) of this vector.
func length() -> float:
	pass;

#desc Returns the squared length (squared magnitude) of this vector. This method runs faster than [method length].
func length_squared() -> int:
	pass;

#desc Returns the axis of the vector's highest value. See [code]AXIS_*[/code] constants. If all components are equal, this method returns [constant AXIS_X].
func max_axis_index() -> int:
	pass;

#desc Returns the axis of the vector's lowest value. See [code]AXIS_*[/code] constants. If all components are equal, this method returns [constant AXIS_W].
func min_axis_index() -> int:
	pass;

#desc Returns a new vector with each component set to one or negative one, depending on the signs of the components, or zero if the component is zero, by calling [method @GlobalScope.sign] on each component.
func sign() -> Vector4i:
	pass;


