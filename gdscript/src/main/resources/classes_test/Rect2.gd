#brief 2D axis-aligned bounding box using floating point coordinates.
#desc [Rect2] consists of a position, a size, and several utility functions. It is typically used for fast overlap tests.
#desc It uses floating-point coordinates. If you need integer coordinates, use [Rect2i] instead.
#desc The 3D counterpart to [Rect2] is [AABB].
#desc Negative values for [member size] are not supported and will not work for most methods. Use [method abs] to get a Rect2 with a positive size.
class_name Rect2


#desc Ending corner. This is calculated as [code]position + size[/code]. Setting this value will change the size.
var end: Vector2;

#desc Beginning corner. Typically has values lower than [member end].
var position: Vector2;

#desc Size from [member position] to [member end]. Typically, all components are positive.
#desc If the size is negative, you can use [method abs] to fix it.
var size: Vector2;


#desc Constructs a default-initialized [Rect2] with default (zero) values of [member position] and [member size].
func Rect2() -> Rect2:
	pass;

#desc Constructs a [Rect2] as a copy of the given [Rect2].
func Rect2() -> Rect2:
	pass;

#desc Constructs a [Rect2] from a [Rect2i].
func Rect2() -> Rect2:
	pass;

#desc Constructs a [Rect2] by position and size.
func Rect2(position: Vector2, size: Vector2) -> Rect2:
	pass;

#desc Constructs a [Rect2] by x, y, width, and height.
func Rect2(x: float, y: float, width: float, height: float) -> Rect2:
	pass;


#desc Returns a [Rect2] with equivalent position and area, modified so that the top-left corner is the origin and [code]width[/code] and [code]height[/code] are positive.
func abs() -> Rect2:
	pass;

#desc Returns [code]true[/code] if this [Rect2] completely encloses another one.
func encloses() -> bool:
	pass;

#desc Returns a copy of this [Rect2] expanded to include a given point.
#desc [b]Example:[/b]
#desc [codeblocks]
#desc [gdscript]
#desc # position (-3, 2), size (1, 1)
#desc var rect = Rect2(Vector2(-3, 2), Vector2(1, 1))
#desc # position (-3, -1), size (3, 4), so we fit both rect and Vector2(0, -1)
#desc var rect2 = rect.expand(Vector2(0, -1))
#desc [/gdscript]
#desc [csharp]
#desc # position (-3, 2), size (1, 1)
#desc var rect = new Rect2(new Vector2(-3, 2), new Vector2(1, 1));
#desc # position (-3, -1), size (3, 4), so we fit both rect and Vector2(0, -1)
#desc var rect2 = rect.Expand(new Vector2(0, -1));
#desc [/csharp]
#desc [/codeblocks]
func expand() -> Rect2:
	pass;

#desc Returns the area of the [Rect2]. See also [method has_area].
func get_area() -> float:
	pass;

#desc Returns the center of the [Rect2], which is equal to [member position] + ([member size] / 2).
func get_center() -> Vector2:
	pass;

#desc Returns a copy of the [Rect2] grown by the specified [param amount] on all sides.
func grow() -> Rect2:
	pass;

#desc Returns a copy of the [Rect2] grown by the specified amount on each side individually.
func grow_individual(left: float, top: float, right: float, bottom: float) -> Rect2:
	pass;

#desc Returns a copy of the [Rect2] grown by the specified [param amount] on the specified [enum Side].
func grow_side(side: int, amount: float) -> Rect2:
	pass;

#desc Returns [code]true[/code] if the [Rect2] has area, and [code]false[/code] if the [Rect2] is linear, empty, or has a negative [member size]. See also [method get_area].
func has_area() -> bool:
	pass;

#desc Returns [code]true[/code] if the [Rect2] contains a point. By convention, the right and bottom edges of the [Rect2] are considered exclusive, so points on these edges are [b]not[/b] included.
#desc [b]Note:[/b] This method is not reliable for [Rect2] with a [i]negative size[/i]. Use [method abs] to get a positive sized equivalent rectangle to check for contained points.
func has_point() -> bool:
	pass;

#desc Returns the intersection of this [Rect2] and [param b].
#desc If the rectangles do not intersect, an empty [Rect2] is returned.
func intersection() -> Rect2:
	pass;

#desc Returns [code]true[/code] if the [Rect2] overlaps with [code]b[/code] (i.e. they have at least one point in common).
#desc If [param include_borders] is [code]true[/code], they will also be considered overlapping if their borders touch, even without intersection.
func intersects(b: Rect2, include_borders: bool) -> bool:
	pass;

#desc Returns [code]true[/code] if this [Rect2] and [param rect] are approximately equal, by calling [code]is_equal_approx[/code] on each component.
func is_equal_approx() -> bool:
	pass;

#desc Returns a larger [Rect2] that contains this [Rect2] and [param b].
func merge() -> Rect2:
	pass;


