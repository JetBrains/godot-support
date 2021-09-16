class_name Transform2D
const IDENTITY = Transform2D(1, 0, 0, 1, 0, 0);
const FLIP_X = Transform2D(-1, 0, 0, 1, 0, 0);
const FLIP_Y = Transform2D(1, 0, 0, -1, 0, 0);

var origin: Vector2;
var x: Vector2;
var y: Vector2;

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
