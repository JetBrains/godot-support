#brief 2D axis-aligned bounding box using integer coordinates.
#desc [Rect2i] consists of a position, a size, and several utility functions. It is typically used for fast overlap tests.
#desc It uses integer coordinates. If you need floating-point coordinates, use [Rect2] instead.
#desc Negative values for [member size] are not supported and will not work for most methods. Use [method abs] to get a Rect2i with a positive size.
class_name Rect2i


#desc Ending corner. This is calculated as [code]position + size[/code]. Setting this value will change the size.
var end: Vector2i;

#desc Beginning corner. Typically has values lower than [member end].
var position: Vector2i;

#desc Size from [member position] to [member end]. Typically, all components are positive.
#desc If the size is negative, you can use [method abs] to fix it.
var size: Vector2i;


#desc Constructs a default-initialized [Rect2i] with default (zero) values of [member position] and [member size].
func Rect2i() -> Rect2i:
	pass;

#desc Constructs a [Rect2i] as a copy of the given [Rect2i].
func Rect2i(from: Rect2i) -> Rect2i:
	pass;

#desc Constructs a new [Rect2i] from [Rect2]. The floating point coordinates will be truncated.
func Rect2i(from: Rect2) -> Rect2i:
	pass;

#desc Constructs a [Rect2i] by position and size.
func Rect2i(position: Vector2i, size: Vector2i) -> Rect2i:
	pass;

#desc Constructs a [Rect2i] by x, y, width, and height.
func Rect2i(x: int, y: int, width: int, height: int) -> Rect2i:
	pass;


#desc Returns a [Rect2i] with equivalent position and area, modified so that the top-left corner is the origin and [code]width[/code] and [code]height[/code] are positive.
func abs() -> Rect2i:
	pass;

#desc Returns [code]true[/code] if this [Rect2i] completely encloses another one.
func encloses(b: Rect2i) -> bool:
	pass;

#desc Returns a copy of this [Rect2i] expanded so that the borders align with the given point.
#desc [codeblocks]
#desc [gdscript]
#desc # position (-3, 2), size (1, 1)
#desc var rect = Rect2i(Vector2i(-3, 2), Vector2i(1, 1))
#desc # position (-3, -1), size (3, 4), so we fit both rect and Vector2i(0, -1)
#desc var rect2 = rect.expand(Vector2i(0, -1))
#desc [/gdscript]
#desc [csharp]
#desc # position (-3, 2), size (1, 1)
#desc var rect = new Rect2i(new Vector2i(-3, 2), new Vector2i(1, 1));
#desc # position (-3, -1), size (3, 4), so we fit both rect and Vector2i(0, -1)
#desc var rect2 = rect.Expand(new Vector2i(0, -1));
#desc [/csharp]
#desc [/codeblocks]
func expand(to: Vector2i) -> Rect2i:
	pass;

#desc Returns the area of the [Rect2i]. See also [method has_area].
func get_area() -> int:
	pass;

#desc Returns the center of the [Rect2i], which is equal to [member position] + ([member size] / 2).
#desc If [member size] is an odd number, the returned center value will be rounded towards [member position].
func get_center() -> Vector2i:
	pass;

#desc Returns a copy of the [Rect2i] grown by the specified [param amount] on all sides.
func grow(amount: int) -> Rect2i:
	pass;

#desc Returns a copy of the [Rect2i] grown by the specified amount on each side individually.
func grow_individual(left: int, top: int, right: int, bottom: int) -> Rect2i:
	pass;

#desc Returns a copy of the [Rect2i] grown by the specified [param amount] on the specified [enum Side].
func grow_side(side: int, amount: int) -> Rect2i:
	pass;

#desc Returns [code]true[/code] if the [Rect2i] has area, and [code]false[/code] if the [Rect2i] is linear, empty, or has a negative [member size]. See also [method get_area].
func has_area() -> bool:
	pass;

#desc Returns [code]true[/code] if the [Rect2i] contains a point. By convention, the right and bottom edges of the [Rect2i] are considered exclusive, so points on these edges are [b]not[/b] included.
#desc [b]Note:[/b] This method is not reliable for [Rect2i] with a [i]negative size[/i]. Use [method abs] to get a positive sized equivalent rectangle to check for contained points.
func has_point(point: Vector2i) -> bool:
	pass;

#desc Returns the intersection of this [Rect2i] and [code]b[/code].
#desc If the rectangles do not intersect, an empty [Rect2i] is returned.
func intersection(b: Rect2i) -> Rect2i:
	pass;

#desc Returns [code]true[/code] if the [Rect2i] overlaps with [code]b[/code] (i.e. they have at least one point in common).
func intersects(b: Rect2i) -> bool:
	pass;

#desc Returns a larger [Rect2i] that contains this [Rect2i] and [param b].
func merge(b: Rect2i) -> Rect2i:
	pass;


