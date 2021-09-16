class_name Quaternion
const IDENTITY = Quaternion(0, 0, 0, 1);

var w: float;
var x: float;
var y: float;
var z: float;

func Quaternion() -> Quaternion:
    pass;
func Quaternion(from: Quaternion) -> Quaternion:
    pass;
func Quaternion(arc_from: Vector3, arc_to: Vector3) -> Quaternion:
    pass;
func Quaternion(axis: Vector3, angle: float) -> Quaternion:
    pass;
func Quaternion(euler: Vector3) -> Quaternion:
    pass;
func Quaternion(from: Basis) -> Quaternion:
    pass;
func Quaternion(x: float, y: float, z: float, w: float) -> Quaternion:
    pass;
func angle_to(to: Quaternion) -> float:
    pass;
func cubic_slerp(b: Quaternion, pre_a: Quaternion, post_b: Quaternion, weight: float) -> Quaternion:
    pass;
func dot(with: Quaternion) -> float:
    pass;
func get_euler() -> Vector3:
    pass;
func inverse() -> Quaternion:
    pass;
func is_equal_approx(to: Quaternion) -> bool:
    pass;
func is_normalized() -> bool:
    pass;
func length() -> float:
    pass;
func length_squared() -> float:
    pass;
func normalized() -> Quaternion:
    pass;
func slerp(to: Quaternion, weight: float) -> Quaternion:
    pass;
func slerpni(to: Quaternion, weight: float) -> Quaternion:
    pass;
