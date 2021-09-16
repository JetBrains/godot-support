class_name AABB

var end: Vector3;
var position: Vector3;
var size: Vector3;

func AABB() -> AABB:
    pass;
func AABB(from: AABB) -> AABB:
    pass;
func AABB(position: Vector3, size: Vector3) -> AABB:
    pass;
func abs() -> AABB:
    pass;
func encloses(with: AABB) -> bool:
    pass;
func expand(to_point: Vector3) -> AABB:
    pass;
func get_area() -> float:
    pass;
func get_endpoint(idx: int) -> Vector3:
    pass;
func get_longest_axis() -> Vector3:
    pass;
func get_longest_axis_index() -> int:
    pass;
func get_longest_axis_size() -> float:
    pass;
func get_shortest_axis() -> Vector3:
    pass;
func get_shortest_axis_index() -> int:
    pass;
func get_shortest_axis_size() -> float:
    pass;
func get_support(dir: Vector3) -> Vector3:
    pass;
func grow(by: float) -> AABB:
    pass;
func has_no_area() -> bool:
    pass;
func has_no_surface() -> bool:
    pass;
func has_point(point: Vector3) -> bool:
    pass;
func intersection(with: AABB) -> AABB:
    pass;
func intersects(with: AABB) -> bool:
    pass;
func intersects_plane(plane: Plane) -> bool:
    pass;
func intersects_ray(from: Vector3, dir: Vector3) -> Variant:
    pass;
func intersects_segment(from: Vector3, to: Vector3) -> Variant:
    pass;
func is_equal_approx(aabb: AABB) -> bool:
    pass;
func merge(with: AABB) -> AABB:
    pass;
