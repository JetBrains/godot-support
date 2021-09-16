extends Object
class_name Geometry2D
const OPERATION_UNION = 0;
const OPERATION_DIFFERENCE = 1;
const OPERATION_INTERSECTION = 2;
const OPERATION_XOR = 3;
const JOIN_SQUARE = 0;
const JOIN_ROUND = 1;
const JOIN_MITER = 2;
const END_POLYGON = 0;
const END_JOINED = 1;
const END_BUTT = 2;
const END_SQUARE = 3;
const END_ROUND = 4;


func clip_polygons(polygon_a: PackedVector2Array, polygon_b: PackedVector2Array) -> Array:
    pass;
func clip_polyline_with_polygon(polyline: PackedVector2Array, polygon: PackedVector2Array) -> Array:
    pass;
func convex_hull(points: PackedVector2Array) -> PackedVector2Array:
    pass;
func exclude_polygons(polygon_a: PackedVector2Array, polygon_b: PackedVector2Array) -> Array:
    pass;
func get_closest_point_to_segment(point: Vector2, s1: Vector2, s2: Vector2) -> Vector2:
    pass;
func get_closest_point_to_segment_uncapped(point: Vector2, s1: Vector2, s2: Vector2) -> Vector2:
    pass;
func get_closest_points_between_segments(p1: Vector2, q1: Vector2, p2: Vector2, q2: Vector2) -> PackedVector2Array:
    pass;
func intersect_polygons(polygon_a: PackedVector2Array, polygon_b: PackedVector2Array) -> Array:
    pass;
func intersect_polyline_with_polygon(polyline: PackedVector2Array, polygon: PackedVector2Array) -> Array:
    pass;
func is_point_in_circle(point: Vector2, circle_position: Vector2, circle_radius: float) -> bool:
    pass;
func is_point_in_polygon(point: Vector2, polygon: PackedVector2Array) -> bool:
    pass;
func is_polygon_clockwise(polygon: PackedVector2Array) -> bool:
    pass;
func line_intersects_line(from_a: Vector2, dir_a: Vector2, from_b: Vector2, dir_b: Vector2) -> Variant:
    pass;
func make_atlas(sizes: PackedVector2Array) -> Dictionary:
    pass;
func merge_polygons(polygon_a: PackedVector2Array, polygon_b: PackedVector2Array) -> Array:
    pass;
func offset_polygon(polygon: PackedVector2Array, delta: float, join_type: int) -> Array:
    pass;
func offset_polyline(polyline: PackedVector2Array, delta: float, join_type: int, end_type: int) -> Array:
    pass;
func point_is_inside_triangle(point: Vector2, a: Vector2, b: Vector2, c: Vector2) -> bool:
    pass;
func segment_intersects_segment(from_a: Vector2, to_a: Vector2, from_b: Vector2, to_b: Vector2) -> Variant:
    pass;
func triangulate_delaunay(points: PackedVector2Array) -> PackedInt32Array:
    pass;
func triangulate_polygon(polygon: PackedVector2Array) -> PackedInt32Array:
    pass;
