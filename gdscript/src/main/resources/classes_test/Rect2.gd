class_name Rect2

var end: Vector2;
var position: Vector2;
var size: Vector2;

func Rect2() -> Rect2:
    pass;
func Rect2(from: Rect2) -> Rect2:
    pass;
func Rect2(from: Rect2i) -> Rect2:
    pass;
func Rect2(position: Vector2, size: Vector2) -> Rect2:
    pass;
func Rect2(x: float, y: float, width: float, height: float) -> Rect2:
    pass;
func abs() -> Rect2:
    pass;
func encloses(b: Rect2) -> bool:
    pass;
func expand(to: Vector2) -> Rect2:
    pass;
func get_area() -> float:
    pass;
func grow(amount: float) -> Rect2:
    pass;
func grow_individual(left: float, top: float, right: float, bottom: float) -> Rect2:
    pass;
func grow_side(side: int, amount: float) -> Rect2:
    pass;
func has_no_area() -> bool:
    pass;
func has_point(point: Vector2) -> bool:
    pass;
func intersection(b: Rect2) -> Rect2:
    pass;
func intersects(b: Rect2, include_borders: bool) -> bool:
    pass;
func is_equal_approx(rect: Rect2) -> bool:
    pass;
func merge(b: Rect2) -> Rect2:
    pass;
