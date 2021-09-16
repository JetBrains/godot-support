class_name Transform3D
const IDENTITY = Transform3D(1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0);
const FLIP_X = Transform3D(-1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0);
const FLIP_Y = Transform3D(1, 0, 0, 0, -1, 0, 0, 0, 1, 0, 0, 0);
const FLIP_Z = Transform3D(1, 0, 0, 0, 1, 0, 0, 0, -1, 0, 0, 0);

var basis: Basis;
var origin: Vector3;

func Transform3D() -> Transform3D:
    pass;
func Transform3D(from: Transform3D) -> Transform3D:
    pass;
func Transform3D(basis: Basis, origin: Vector3) -> Transform3D:
    pass;
func Transform3D(x_axis: Vector3, y_axis: Vector3, z_axis: Vector3, origin: Vector3) -> Transform3D:
    pass;
func affine_inverse() -> Transform3D:
    pass;
func interpolate_with(xform: Transform3D, weight: float) -> Transform3D:
    pass;
func inverse() -> Transform3D:
    pass;
func is_equal_approx(xform: Transform3D) -> bool:
    pass;
func looking_at(target: Vector3, up: Vector3) -> Transform3D:
    pass;
func orthonormalized() -> Transform3D:
    pass;
func rotated(axis: Vector3, phi: float) -> Transform3D:
    pass;
func scaled(scale: Vector3) -> Transform3D:
    pass;
func translated(offset: Vector3) -> Transform3D:
    pass;
