#brief Axis-Aligned Bounding Box.
#desc [AABB] consists of a position, a size, and several utility functions. It is typically used for fast overlap tests.
#desc It uses floating-point coordinates. The 2D counterpart to [AABB] is [Rect2].
#desc Negative values for [member size] are not supported and will not work for most methods. Use [method abs] to get an AABB with a positive size.
#desc [b]Note:[/b] Unlike [Rect2], [AABB] does not have a variant that uses integer coordinates.
class_name AABB


#desc Ending corner. This is calculated as [code]position + size[/code]. Setting this value will change the size.
var end: Vector3;

#desc Beginning corner. Typically has values lower than [member end].
var position: Vector3;

#desc Size from [member position] to [member end]. Typically, all components are positive.
#desc If the size is negative, you can use [method abs] to fix it.
var size: Vector3;


#desc Constructs a default-initialized [AABB] with default (zero) values of [member position] and [member size].
func AABB() -> AABB:
	pass;

#desc Constructs an [AABB] as a copy of the given [AABB].
func AABB(from: AABB) -> AABB:
	pass;

#desc Constructs an [AABB] from a position and size.
func AABB(position: Vector3, size: Vector3) -> AABB:
	pass;


#desc Returns an AABB with equivalent position and size, modified so that the most-negative corner is the origin and the size is positive.
func abs() -> AABB:
	pass;

#desc Returns [code]true[/code] if this [AABB] completely encloses another one.
func encloses(with: AABB) -> bool:
	pass;

#desc Returns a copy of this [AABB] expanded to include a given point.
#desc [b]Example:[/b]
#desc [codeblocks]
#desc [gdscript]
#desc # position (-3, 2, 0), size (1, 1, 1)
#desc var box = AABB(Vector3(-3, 2, 0), Vector3(1, 1, 1))
#desc # position (-3, -1, 0), size (3, 4, 2), so we fit both the original AABB and Vector3(0, -1, 2)
#desc var box2 = box.expand(Vector3(0, -1, 2))
#desc [/gdscript]
#desc [csharp]
#desc // position (-3, 2, 0), size (1, 1, 1)
#desc var box = new AABB(new Vector3(-3, 2, 0), new Vector3(1, 1, 1));
#desc // position (-3, -1, 0), size (3, 4, 2), so we fit both the original AABB and Vector3(0, -1, 2)
#desc var box2 = box.Expand(new Vector3(0, -1, 2));
#desc [/csharp]
#desc [/codeblocks]
func expand(to_point: Vector3) -> AABB:
	pass;

#desc Returns the center of the [AABB], which is equal to [member position] + ([member size] / 2).
func get_center() -> Vector3:
	pass;

#desc Gets the position of the 8 endpoints of the [AABB] in space.
func get_endpoint(idx: int) -> Vector3:
	pass;

#desc Returns the normalized longest axis of the [AABB].
func get_longest_axis() -> Vector3:
	pass;

#desc Returns the index of the longest axis of the [AABB] (according to [Vector3]'s [code]AXIS_*[/code] constants).
func get_longest_axis_index() -> int:
	pass;

#desc Returns the scalar length of the longest axis of the [AABB].
func get_longest_axis_size() -> float:
	pass;

#desc Returns the normalized shortest axis of the [AABB].
func get_shortest_axis() -> Vector3:
	pass;

#desc Returns the index of the shortest axis of the [AABB] (according to [Vector3]::AXIS* enum).
func get_shortest_axis_index() -> int:
	pass;

#desc Returns the scalar length of the shortest axis of the [AABB].
func get_shortest_axis_size() -> float:
	pass;

#desc Returns the support point in a given direction. This is useful for collision detection algorithms.
func get_support(dir: Vector3) -> Vector3:
	pass;

#desc Returns the volume of the [AABB].
func get_volume() -> float:
	pass;

#desc Returns a copy of the [AABB] grown a given number of units towards all the sides.
func grow(by: float) -> AABB:
	pass;

#desc Returns [code]true[/code] if the [AABB] contains a point. Points on the faces of the AABB are considered included, though float-point precision errors may impact the accuracy of such checks.
#desc [b]Note:[/b] This method is not reliable for [AABB] with a [i]negative size[/i]. Use [method abs] to get a positive sized equivalent [AABB] to check for contained points.
func has_point(point: Vector3) -> bool:
	pass;

#desc Returns [code]true[/code] if the [AABB] has a surface or a length, and [code]false[/code] if the [AABB] is empty (all components of [member size] are zero or negative).
func has_surface() -> bool:
	pass;

#desc Returns [code]true[/code] if the [AABB] has a volume, and [code]false[/code] if the [AABB] is flat, empty, or has a negative [member size].
func has_volume() -> bool:
	pass;

#desc Returns the intersection between two [AABB]. An empty AABB (size [code](0, 0, 0)[/code]) is returned on failure.
func intersection(with: AABB) -> AABB:
	pass;

#desc Returns [code]true[/code] if the [AABB] overlaps with another.
func intersects(with: AABB) -> bool:
	pass;

#desc Returns [code]true[/code] if the [AABB] is on both sides of a plane.
func intersects_plane(plane: Plane) -> bool:
	pass;

func intersects_ray(from: Vector3, dir: Vector3) -> Variant:
	pass;

#desc Returns [code]true[/code] if the [AABB] intersects the line segment between [param from] and [param to].
func intersects_segment(from: Vector3, to: Vector3) -> Variant:
	pass;

#desc Returns [code]true[/code] if this [AABB] and [param aabb] are approximately equal, by calling [method @GlobalScope.is_equal_approx] on each component.
func is_equal_approx(aabb: AABB) -> bool:
	pass;

#desc Returns a larger [AABB] that contains both this [AABB] and [param with].
func merge(with: AABB) -> AABB:
	pass;


