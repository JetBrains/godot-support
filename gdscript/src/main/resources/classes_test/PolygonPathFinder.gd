extends Resource
class_name PolygonPathFinder



func find_path(from: Vector2, to: Vector2) -> PackedVector2Array:
    pass;

func get_bounds() -> Rect2:
    pass;

func get_closest_point(point: Vector2) -> Vector2:
    pass;

func get_intersections(from: Vector2, to: Vector2) -> PackedVector2Array:
    pass;

func get_point_penalty(idx: int) -> float:
    pass;

func is_point_inside(point: Vector2) -> bool:
    pass;

func set_point_penalty(idx: int, penalty: float) -> void:
    pass;

func setup(points: PackedVector2Array, connections: PackedInt32Array) -> void:
    pass;

