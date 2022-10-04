#brief Helper node to calculate generic geometry operations in 2D space.
#desc Geometry2D provides users with a set of helper functions to create geometric shapes, compute intersections between shapes, and process various other geometric operations.
class_name Geometry2D

#desc Create regions where either subject or clip polygons (or both) are filled.
const OPERATION_UNION = 0;

#desc Create regions where subject polygons are filled except where clip polygons are filled.
const OPERATION_DIFFERENCE = 1;

#desc Create regions where both subject and clip polygons are filled.
const OPERATION_INTERSECTION = 2;

#desc Create regions where either subject or clip polygons are filled but not where both are filled.
const OPERATION_XOR = 3;

#desc Squaring is applied uniformally at all convex edge joins at [code]1 * delta[/code].
const JOIN_SQUARE = 0;

#desc While flattened paths can never perfectly trace an arc, they are approximated by a series of arc chords.
const JOIN_ROUND = 1;

#desc There's a necessary limit to mitered joins since offsetting edges that join at very acute angles will produce excessively long and narrow "spikes". For any given edge join, when miter offsetting would exceed that maximum distance, "square" joining is applied.
const JOIN_MITER = 2;

#desc Endpoints are joined using the [enum PolyJoinType] value and the path filled as a polygon.
const END_POLYGON = 0;

#desc Endpoints are joined using the [enum PolyJoinType] value and the path filled as a polyline.
const END_JOINED = 1;

#desc Endpoints are squared off with no extension.
const END_BUTT = 2;

#desc Endpoints are squared off and extended by [code]delta[/code] units.
const END_SQUARE = 3;

#desc Endpoints are rounded off and extended by [code]delta[/code] units.
const END_ROUND = 4;




#desc Clips [param polygon_a] against [param polygon_b] and returns an array of clipped polygons. This performs [constant OPERATION_DIFFERENCE] between polygons. Returns an empty array if [param polygon_b] completely overlaps [param polygon_a].
#desc If [param polygon_b] is enclosed by [param polygon_a], returns an outer polygon (boundary) and inner polygon (hole) which could be distinguished by calling [method is_polygon_clockwise].
func clip_polygons(polygon_a: PackedVector2Array, polygon_b: PackedVector2Array) -> PackedVector2Array[]:
	pass;

#desc Clips [param polyline] against [param polygon] and returns an array of clipped polylines. This performs [constant OPERATION_DIFFERENCE] between the polyline and the polygon. This operation can be thought of as cutting a line with a closed shape.
func clip_polyline_with_polygon(polyline: PackedVector2Array, polygon: PackedVector2Array) -> PackedVector2Array[]:
	pass;

#desc Given an array of [Vector2]s, returns the convex hull as a list of points in counterclockwise order. The last point is the same as the first one.
func convex_hull() -> PackedVector2Array:
	pass;

#desc Decomposes the [param polygon] into multiple convex hulls and returns an array of [PackedVector2Array].
func decompose_polygon_in_convex() -> PackedVector2Array[]:
	pass;

#desc Mutually excludes common area defined by intersection of [param polygon_a] and [param polygon_b] (see [method intersect_polygons]) and returns an array of excluded polygons. This performs [constant OPERATION_XOR] between polygons. In other words, returns all but common area between polygons.
#desc The operation may result in an outer polygon (boundary) and inner polygon (hole) produced which could be distinguished by calling [method is_polygon_clockwise].
func exclude_polygons(polygon_a: PackedVector2Array, polygon_b: PackedVector2Array) -> PackedVector2Array[]:
	pass;

#desc Returns the 2D point on the 2D segment ([param s1], [param s2]) that is closest to [param point]. The returned point will always be inside the specified segment.
func get_closest_point_to_segment(point: Vector2, s1: Vector2, s2: Vector2) -> Vector2:
	pass;

#desc Returns the 2D point on the 2D line defined by ([param s1], [param s2]) that is closest to [param point]. The returned point can be inside the segment ([param s1], [param s2]) or outside of it, i.e. somewhere on the line extending from the segment.
func get_closest_point_to_segment_uncapped(point: Vector2, s1: Vector2, s2: Vector2) -> Vector2:
	pass;

#desc Given the two 2D segments ([param p1], [param q1]) and ([param p2], [param q2]), finds those two points on the two segments that are closest to each other. Returns a [PackedVector2Array] that contains this point on ([param p1], [param q1]) as well the accompanying point on ([param p2], [param q2]).
func get_closest_points_between_segments(p1: Vector2, q1: Vector2, p2: Vector2, q2: Vector2) -> PackedVector2Array:
	pass;

#desc Intersects [param polygon_a] with [param polygon_b] and returns an array of intersected polygons. This performs [constant OPERATION_INTERSECTION] between polygons. In other words, returns common area shared by polygons. Returns an empty array if no intersection occurs.
#desc The operation may result in an outer polygon (boundary) and inner polygon (hole) produced which could be distinguished by calling [method is_polygon_clockwise].
func intersect_polygons(polygon_a: PackedVector2Array, polygon_b: PackedVector2Array) -> PackedVector2Array[]:
	pass;

#desc Intersects [param polyline] with [param polygon] and returns an array of intersected polylines. This performs [constant OPERATION_INTERSECTION] between the polyline and the polygon. This operation can be thought of as chopping a line with a closed shape.
func intersect_polyline_with_polygon(polyline: PackedVector2Array, polygon: PackedVector2Array) -> PackedVector2Array[]:
	pass;

#desc Returns [code]true[/code] if [param point] is inside the circle or if it's located exactly [i]on[/i] the circle's boundary, otherwise returns [code]false[/code].
func is_point_in_circle(point: Vector2, circle_position: Vector2, circle_radius: float) -> bool:
	pass;

#desc Returns [code]true[/code] if [param point] is inside [param polygon] or if it's located exactly [i]on[/i] polygon's boundary, otherwise returns [code]false[/code].
func is_point_in_polygon(point: Vector2, polygon: PackedVector2Array) -> bool:
	pass;

#desc Returns [code]true[/code] if [param polygon]'s vertices are ordered in clockwise order, otherwise returns [code]false[/code].
func is_polygon_clockwise() -> bool:
	pass;

#desc Checks if the two lines ([param from_a], [param dir_a]) and ([param from_b], [param dir_b]) intersect. If yes, return the point of intersection as [Vector2]. If no intersection takes place, returns [code]null[/code].
#desc [b]Note:[/b] The lines are specified using direction vectors, not end points.
func line_intersects_line(from_a: Vector2, dir_a: Vector2, from_b: Vector2, dir_b: Vector2) -> Variant:
	pass;

#desc Given an array of [Vector2]s representing tiles, builds an atlas. The returned dictionary has two keys: [code]points[/code] is a [PackedVector2Array] that specifies the positions of each tile, [code]size[/code] contains the overall size of the whole atlas as [Vector2i].
func make_atlas() -> Dictionary:
	pass;

#desc Merges (combines) [param polygon_a] and [param polygon_b] and returns an array of merged polygons. This performs [constant OPERATION_UNION] between polygons.
#desc The operation may result in an outer polygon (boundary) and multiple inner polygons (holes) produced which could be distinguished by calling [method is_polygon_clockwise].
func merge_polygons(polygon_a: PackedVector2Array, polygon_b: PackedVector2Array) -> PackedVector2Array[]:
	pass;

#desc Inflates or deflates [param polygon] by [param delta] units (pixels). If [param delta] is positive, makes the polygon grow outward. If [param delta] is negative, shrinks the polygon inward. Returns an array of polygons because inflating/deflating may result in multiple discrete polygons. Returns an empty array if [param delta] is negative and the absolute value of it approximately exceeds the minimum bounding rectangle dimensions of the polygon.
#desc Each polygon's vertices will be rounded as determined by [param join_type], see [enum PolyJoinType].
#desc The operation may result in an outer polygon (boundary) and inner polygon (hole) produced which could be distinguished by calling [method is_polygon_clockwise].
#desc [b]Note:[/b] To translate the polygon's vertices specifically, multiply them to a [Transform2D]:
#desc [codeblocks]
#desc [gdscript]
#desc var polygon = PackedVector2Array([Vector2(0, 0), Vector2(100, 0), Vector2(100, 100), Vector2(0, 100)])
#desc var offset = Vector2(50, 50)
#desc polygon = Transform2D(0, offset) * polygon
#desc print(polygon) # prints [Vector2(50, 50), Vector2(150, 50), Vector2(150, 150), Vector2(50, 150)]
#desc [/gdscript]
#desc [csharp]
#desc var polygon = new Vector2[] { new Vector2(0, 0), new Vector2(100, 0), new Vector2(100, 100), new Vector2(0, 100) };
#desc var offset = new Vector2(50, 50);
#desc // TODO: This code is not valid right now. Ping @aaronfranke about it before Godot 4.0 is out.
#desc //polygon = (Vector2[]) new Transform2D(0, offset).Xform(polygon);
#desc //GD.Print(polygon); // prints [Vector2(50, 50), Vector2(150, 50), Vector2(150, 150), Vector2(50, 150)]
#desc [/csharp]
#desc [/codeblocks]
func offset_polygon(polygon: PackedVector2Array, delta: float, join_type: int) -> PackedVector2Array[]:
	pass;

#desc Inflates or deflates [param polyline] by [param delta] units (pixels), producing polygons. If [param delta] is positive, makes the polyline grow outward. Returns an array of polygons because inflating/deflating may result in multiple discrete polygons. If [param delta] is negative, returns an empty array.
#desc Each polygon's vertices will be rounded as determined by [param join_type], see [enum PolyJoinType].
#desc Each polygon's endpoints will be rounded as determined by [param end_type], see [enum PolyEndType].
#desc The operation may result in an outer polygon (boundary) and inner polygon (hole) produced which could be distinguished by calling [method is_polygon_clockwise].
func offset_polyline(polyline: PackedVector2Array, delta: float, join_type: int, end_type: int) -> PackedVector2Array[]:
	pass;

#desc Returns if [param point] is inside the triangle specified by [param a], [param b] and [param c].
func point_is_inside_triangle(point: Vector2, a: Vector2, b: Vector2, c: Vector2) -> bool:
	pass;

#desc Given the 2D segment ([param segment_from], [param segment_to]), returns the position on the segment (as a number between 0 and 1) at which the segment hits the circle that is located at position [param circle_position] and has radius [param circle_radius]. If the segment does not intersect the circle, -1 is returned (this is also the case if the line extending the segment would intersect the circle, but the segment does not).
func segment_intersects_circle(segment_from: Vector2, segment_to: Vector2, circle_position: Vector2, circle_radius: float) -> float:
	pass;

#desc Checks if the two segments ([param from_a], [param to_a]) and ([param from_b], [param to_b]) intersect. If yes, return the point of intersection as [Vector2]. If no intersection takes place, returns [code]null[/code].
func segment_intersects_segment(from_a: Vector2, to_a: Vector2, from_b: Vector2, to_b: Vector2) -> Variant:
	pass;

#desc Triangulates the area specified by discrete set of [param points] such that no point is inside the circumcircle of any resulting triangle. Returns a [PackedInt32Array] where each triangle consists of three consecutive point indices into [param points] (i.e. the returned array will have [code]n * 3[/code] elements, with [code]n[/code] being the number of found triangles). If the triangulation did not succeed, an empty [PackedInt32Array] is returned.
func triangulate_delaunay() -> PackedInt32Array:
	pass;

#desc Triangulates the polygon specified by the points in [param polygon]. Returns a [PackedInt32Array] where each triangle consists of three consecutive point indices into [param polygon] (i.e. the returned array will have [code]n * 3[/code] elements, with [code]n[/code] being the number of found triangles). Output triangles will always be counter clockwise, and the contour will be flipped if it's clockwise. If the triangulation did not succeed, an empty [PackedInt32Array] is returned.
func triangulate_polygon() -> PackedInt32Array:
	pass;


