class_name Vector3

const AXIS_X = 0;
const AXIS_Y = 1;
const AXIS_Z = 2;
const ZERO = Vector3(0, 0, 0);
const ONE = Vector3(1, 1, 1);
const INF = Vector3(inf, inf, inf);
const LEFT = Vector3(-1, 0, 0);
const RIGHT = Vector3(1, 0, 0);
const UP = Vector3(0, 1, 0);
const DOWN = Vector3(0, -1, 0);
const FORWARD = Vector3(0, 0, -1);
const BACK = Vector3(0, 0, 1);

var x: float setget , ;
var y: float setget , ;
var z: float setget , ;

func Vector3() -> Vector3:
    pass;

func Vector3(from: Vector3) -> Vector3:
    pass;

func Vector3(from: Vector3i) -> Vector3:
    pass;

func Vector3(x: float, y: float, z: float) -> Vector3:
    pass;

func abs() -> Vector3:
    pass;

func angle_to(to: Vector3) -> float:
    pass;

func bounce(n: Vector3) -> Vector3:
    pass;

func ceil() -> Vector3:
    pass;

func clamp(min: Vector3, max: Vector3) -> Vector3:
    pass;

func cross(with: Vector3) -> Vector3:
    pass;

func cubic_interpolate(b: Vector3, pre_a: Vector3, post_b: Vector3, weight: float) -> Vector3:
    pass;

func direction_to(b: Vector3) -> Vector3:
    pass;

func distance_squared_to(b: Vector3) -> float:
    pass;

func distance_to(b: Vector3) -> float:
    pass;

func dot(with: Vector3) -> float:
    pass;

func floor() -> Vector3:
    pass;

func inverse() -> Vector3:
    pass;

func is_equal_approx(to: Vector3) -> bool:
    pass;

func is_normalized() -> bool:
    pass;

func length() -> float:
    pass;

func length_squared() -> float:
    pass;

func lerp(to: Vector3, weight: float) -> Vector3:
    pass;

func limit_length(length: float) -> Vector3:
    pass;

func max_axis() -> int:
    pass;

func min_axis() -> int:
    pass;

func move_toward(to: Vector3, delta: float) -> Vector3:
    pass;

func normalized() -> Vector3:
    pass;

func operator !=(right: Vector3) -> bool:
    pass;

func operator *(right: Vector3) -> Vector3:
    pass;

func operator *(right: Basis) -> Vector3:
    pass;

func operator *(right: Quaternion) -> Vector3:
    pass;

func operator *(right: Transform3D) -> Vector3:
    pass;

func operator *(right: float) -> Vector3:
    pass;

func operator *(right: int) -> Vector3:
    pass;

func operator +(right: Vector3) -> Vector3:
    pass;

func operator -(right: Vector3) -> Vector3:
    pass;

func operator /(right: Vector3) -> Vector3:
    pass;

func operator /(right: float) -> Vector3:
    pass;

func operator /(right: int) -> Vector3:
    pass;

func operator <(right: Vector3) -> bool:
    pass;

func operator <=(right: Vector3) -> bool:
    pass;

func operator ==(right: Vector3) -> bool:
    pass;

func operator >(right: Vector3) -> bool:
    pass;

func operator >=(right: Vector3) -> bool:
    pass;

func operator [](index: int) -> float:
    pass;

func operator unary+() -> Vector3:
    pass;

func operator unary-() -> Vector3:
    pass;

func outer(with: Vector3) -> Basis:
    pass;

func posmod(mod: float) -> Vector3:
    pass;

func posmodv(modv: Vector3) -> Vector3:
    pass;

func project(b: Vector3) -> Vector3:
    pass;

func reflect(n: Vector3) -> Vector3:
    pass;

func rotated(by_axis: Vector3, phi: float) -> Vector3:
    pass;

func round() -> Vector3:
    pass;

func sign() -> Vector3:
    pass;

func signed_angle_to(to: Vector3, axis: Vector3) -> float:
    pass;

func slerp(to: Vector3, weight: float) -> Vector3:
    pass;

func slide(n: Vector3) -> Vector3:
    pass;

func snapped(step: Vector3) -> Vector3:
    pass;

func to_diagonal_matrix() -> Basis:
    pass;

