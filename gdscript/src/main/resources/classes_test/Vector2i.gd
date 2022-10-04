#brief Vector used for 2D math using integer coordinates.
#desc 2-element structure that can be used to represent positions in 2D space or any other pair of numeric values.
#desc It uses integer coordinates and is therefore preferable to [Vector2] when exact precision is required.
#desc [b]Note:[/b] In a boolean context, a Vector2i will evaluate to [code]false[/code] if it's equal to [code]Vector2i(0, 0)[/code]. Otherwise, a Vector2i will always evaluate to [code]true[/code].
class_name Vector2i

#desc Enumerated value for the X axis. Returned by [method max_axis_index] and [method min_axis_index].
const AXIS_X = 0;

#desc Enumerated value for the Y axis. Returned by [method max_axis_index] and [method min_axis_index].
const AXIS_Y = 1;

#desc Zero vector, a vector with all components set to [code]0[/code].
const ZERO = Vector2i(0, 0);

#desc One vector, a vector with all components set to [code]1[/code].
const ONE = Vector2i(1, 1);

#desc Left unit vector. Represents the direction of left.
const LEFT = Vector2i(-1, 0);

#desc Right unit vector. Represents the direction of right.
const RIGHT = Vector2i(1, 0);

#desc Up unit vector. Y is down in 2D, so this vector points -Y.
const UP = Vector2i(0, -1);

#desc Down unit vector. Y is down in 2D, so this vector points +Y.
const DOWN = Vector2i(0, 1);


#desc The vector's X component. Also accessible by using the index position [code][0][/code].
var x: int;

#desc The vector's Y component. Also accessible by using the index position [code][1][/code].
var y: int;


#desc Constructs a default-initialized [Vector2i] with all components set to [code]0[/code].
func Vector2i() -> Vector2i:
	pass;

#desc Constructs a [Vector2i] as a copy of the given [Vector2i].
func Vector2i() -> Vector2i:
	pass;

#desc Constructs a new [Vector2i] from [Vector2]. The floating point coordinates will be truncated.
func Vector2i() -> Vector2i:
	pass;

#desc Constructs a new [Vector2i] from the given [param x] and [param y].
func Vector2i(x: int, y: int) -> Vector2i:
	pass;


#desc Returns a new vector with all components in absolute values (i.e. positive).
func abs() -> Vector2i:
	pass;

#desc Returns the aspect ratio of this vector, the ratio of [member x] to [member y].
func aspect() -> float:
	pass;

#desc Returns a new vector with all components clamped between the components of [param min] and [param max], by running [method @GlobalScope.clamp] on each component.
func clamp(min: Vector2i, max: Vector2i) -> Vector2i:
	pass;

#desc Returns the length (magnitude) of this vector.
func length() -> float:
	pass;

#desc Returns the squared length (squared magnitude) of this vector.
#desc This method runs faster than [method length], so prefer it if you need to compare vectors or need the squared distance for some formula.
func length_squared() -> int:
	pass;

#desc Returns the axis of the vector's highest value. See [code]AXIS_*[/code] constants. If all components are equal, this method returns [constant AXIS_X].
func max_axis_index() -> int:
	pass;

#desc Returns the axis of the vector's lowest value. See [code]AXIS_*[/code] constants. If all components are equal, this method returns [constant AXIS_Y].
func min_axis_index() -> int:
	pass;

#desc Returns a new vector with each component set to one or negative one, depending on the signs of the components, or zero if the component is zero, by calling [method @GlobalScope.sign] on each component.
func sign() -> Vector2i:
	pass;


