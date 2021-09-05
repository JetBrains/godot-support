extends Object
class_name Geometry3D



func build_box_planes(extents: Vector3) -> Array:
    pass;

func build_capsule_planes(radius: float, height: float, sides: int, lats: int, axis: int) -> Array:
    pass;

func build_cylinder_planes(radius: float, height: float, sides: int, axis: int) -> Array:
    pass;

func clip_polygon(points: PackedVector3Array, plane: Plane) -> PackedVector3Array:
    pass;

func get_closest_point_to_segment(point: Vector3, s1: Vector3, s2: Vector3) -> Vector3:
    pass;

func get_closest_point_to_segment_uncapped(point: Vector3, s1: Vector3, s2: Vector3) -> Vector3:
    pass;

func get_closest_points_between_segments(p1: Vector3, p2: Vector3, q1: Vector3, q2: Vector3) -> PackedVector3Array:
    pass;

func ray_intersects_triangle(from: Vector3, dir: Vector3, a: Vector3, b: Vector3, c: Vector3) -> Variant:
    pass;

func segment_intersects_convex(from: Vector3, to: Vector3, planes: Array) -> PackedVector3Array:
    pass;

func segment_intersects_cylinder(from: Vector3, to: Vector3, height: float, radius: float) -> PackedVector3Array:
    pass;

func segment_intersects_sphere(from: Vector3, to: Vector3, sphere_position: Vector3, sphere_radius: float) -> PackedVector3Array:
    pass;

func segment_intersects_triangle(from: Vector3, to: Vector3, a: Vector3, b: Vector3, c: Vector3) -> Variant:
    pass;

