class_name Basis

const IDENTITY = Basis(1, 0, 0, 0, 1, 0, 0, 0, 1);
const FLIP_X = Basis(-1, 0, 0, 0, 1, 0, 0, 0, 1);
const FLIP_Y = Basis(1, 0, 0, 0, -1, 0, 0, 0, 1);
const FLIP_Z = Basis(1, 0, 0, 0, 1, 0, 0, 0, -1);

var x: Vector3 setget , ;
var y: Vector3 setget , ;
var z: Vector3 setget , ;

func Basis() -> Basis:
    pass;

func Basis(from: Basis) -> Basis:
    pass;

func Basis(axis: Vector3, phi: float) -> Basis:
    pass;

func Basis(euler: Vector3) -> Basis:
    pass;

func Basis(from: Quaternion) -> Basis:
    pass;

func Basis(x_axis: Vector3, y_axis: Vector3, z_axis: Vector3) -> Basis:
    pass;

func determinant() -> float:
    pass;

func get_euler() -> Vector3:
    pass;

func get_orthogonal_index() -> int:
    pass;

func get_rotation_quaternion() -> Quaternion:
    pass;

func get_scale() -> Vector3:
    pass;

func inverse() -> Basis:
    pass;

func is_equal_approx(b: Basis) -> bool:
    pass;

func operator !=(right: Basis) -> bool:
    pass;

func operator *(right: Vector3) -> Vector3:
    pass;

func operator *(right: Basis) -> Basis:
    pass;

func operator *(right: float) -> Basis:
    pass;

func operator *(right: int) -> Basis:
    pass;

func operator ==(right: Basis) -> bool:
    pass;

func operator [](index: int) -> Vector3:
    pass;

func orthonormalized() -> Basis:
    pass;

func rotated(axis: Vector3, phi: float) -> Basis:
    pass;

func scaled(scale: Vector3) -> Basis:
    pass;

func slerp(to: Basis, weight: float) -> Basis:
    pass;

func tdotx(with: Vector3) -> float:
    pass;

func tdoty(with: Vector3) -> float:
    pass;

func tdotz(with: Vector3) -> float:
    pass;

func transposed() -> Basis:
    pass;

