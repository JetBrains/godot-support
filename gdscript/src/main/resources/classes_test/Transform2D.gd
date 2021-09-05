class_name Transform2D

const IDENTITY = Transform2D(1, 0, 0, 1, 0, 0);
const FLIP_X = Transform2D(-1, 0, 0, 1, 0, 0);
const FLIP_Y = Transform2D(1, 0, 0, -1, 0, 0);

var origin: Vector2 setget , ;
var x: Vector2 setget , ;
var y: Vector2 setget , ;

func Transform2D() -> Transform2D:
    pass;

func Transform2D(from: Transform2D) -> Transform2D:
    pass;

func Transform2D(rotation: float, position: Vector2) -> Transform2D:
    pass;

func Transform2D(x_axis: Vector2, y_axis: Vector2, origin: Vector2) -> Transform2D:
    pass;

func affine_inverse() -> Transform2D:
    pass;

func basis_xform(v: Vector2) -> Vector2:
    pass;

func basis_xform_inv(v: Vector2) -> Vector2:
    pass;

func get_origin() -> Vector2:
    pass;

func get_rotation() -> float:
    pass;

func get_scale() -> Vector2:
    pass;

func interpolate_with(xform: Transform2D, weight: float) -> Transform2D:
    pass;

func inverse() -> Transform2D:
    pass;

func is_equal_approx(xform: Transform2D) -> bool:
    pass;

func looking_at(target: Vector2) -> Transform2D:
    pass;

func operator !=(right: Transform2D) -> bool:
    pass;

func operator *(right: Vector2) -> Vector2:
    pass;

func operator *(right: Rect2) -> Rect2:
    pass;

func operator *(right: Transform2D) -> Transform2D:
    pass;

func operator *(right: PackedVector2Array) -> PackedVector2Array:
    pass;

func operator *(right: float) -> Transform2D:
    pass;

func operator *(right: int) -> Transform2D:
    pass;

func operator ==(right: Transform2D) -> bool:
    pass;

func operator [](index: int) -> Vector2:
    pass;

func orthonormalized() -> Transform2D:
    pass;

func rotated(phi: float) -> Transform2D:
    pass;

func scaled(scale: Vector2) -> Transform2D:
    pass;

func set_rotation(rotation: float) -> void:
    pass;

func translated(offset: Vector2) -> Transform2D:
    pass;

