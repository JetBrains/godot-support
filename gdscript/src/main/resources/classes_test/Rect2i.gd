class_name Rect2i


var end: Vector2i setget , ;
var position: Vector2i setget , ;
var size: Vector2i setget , ;

func Rect2i() -> Rect2i:
    pass;

func Rect2i(from: Rect2i) -> Rect2i:
    pass;

func Rect2i(from: Rect2) -> Rect2i:
    pass;

func Rect2i(position: Vector2i, size: Vector2i) -> Rect2i:
    pass;

func Rect2i(x: int, y: int, width: int, height: int) -> Rect2i:
    pass;

func abs() -> Rect2i:
    pass;

func encloses(b: Rect2i) -> bool:
    pass;

func expand(to: Vector2i) -> Rect2i:
    pass;

func get_area() -> int:
    pass;

func grow(amount: int) -> Rect2i:
    pass;

func grow_individual(left: int, top: int, right: int, bottom: int) -> Rect2i:
    pass;

func grow_side(side: int, amount: int) -> Rect2i:
    pass;

func has_no_area() -> bool:
    pass;

func has_point(point: Vector2i) -> bool:
    pass;

func intersection(b: Rect2i) -> Rect2i:
    pass;

func intersects(b: Rect2i) -> bool:
    pass;

func merge(b: Rect2i) -> Rect2i:
    pass;

func operator !=(right: Rect2i) -> bool:
    pass;

func operator ==(right: Rect2i) -> bool:
    pass;

