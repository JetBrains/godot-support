class_name Vector2

# Constructors

func _init():
	pass

func _init(from: Vector2):
	pass

func _init(from: Vector2i):
	pass

func _init(x: float, y: float):
	pass


# Constants

const AXIS_X = 0

const AXIS_Y = 1

const ZERO = Vector2(0, 0)

const ONE = Vector2(1, 1)

const INF = Vector2(inf, inf)

const LEFT = Vector2(-1, 0)

const RIGHT = Vector2(1, 0)

const UP = Vector2(0, -1)

const DOWN = Vector2(0, 1)


# Enums

enum Axis {
	AXIS_X = 0,
	AXIS_Y = 1,
}


# Properties

var x: float

var y: float


# Methods

func abs() -> Vector2:
	pass

func angle() -> float:
	pass

func angle_to(to: Vector2) -> float:
	pass

func angle_to_point(to: Vector2) -> float:
	pass

func aspect() -> float:
	pass

func bezier_derivative(control_1: Vector2, control_2: Vector2, end: Vector2, t: float) -> Vector2:
	pass

func bezier_interpolate(control_1: Vector2, control_2: Vector2, end: Vector2, t: float) -> Vector2:
	pass

func bounce(n: Vector2) -> Vector2:
	pass

func ceil() -> Vector2:
	pass

func clamp(min: Vector2, max: Vector2) -> Vector2:
	pass

func clampf(min: float, max: float) -> Vector2:
	pass

func cross(with: Vector2) -> float:
	pass

func cubic_interpolate(b: Vector2, pre_a: Vector2, post_b: Vector2, weight: float) -> Vector2:
	pass

func cubic_interpolate_in_time(b: Vector2, pre_a: Vector2, post_b: Vector2, weight: float, b_t: float, pre_a_t: float, post_b_t: float) -> Vector2:
	pass

func direction_to(to: Vector2) -> Vector2:
	pass

func distance_squared_to(to: Vector2) -> float:
	pass

func distance_to(to: Vector2) -> float:
	pass

func dot(with: Vector2) -> float:
	pass

func floor() -> Vector2:
	pass

static func from_angle(angle: float) -> Vector2:
	pass

func is_equal_approx(to: Vector2) -> bool:
	pass

func is_finite() -> bool:
	pass

func is_normalized() -> bool:
	pass

func is_zero_approx() -> bool:
	pass

func length() -> float:
	pass

func length_squared() -> float:
	pass

func lerp(to: Vector2, weight: float) -> Vector2:
	pass

func limit_length(length: float) -> Vector2:
	pass

func max(with: Vector2) -> Vector2:
	pass

func max_axis_index() -> int:
	pass

func maxf(with: float) -> Vector2:
	pass

func min(with: Vector2) -> Vector2:
	pass

func min_axis_index() -> int:
	pass

func minf(with: float) -> Vector2:
	pass

func move_toward(to: Vector2, delta: float) -> Vector2:
	pass

func normalized() -> Vector2:
	pass

func orthogonal() -> Vector2:
	pass

func posmod(mod: float) -> Vector2:
	pass

func posmodv(modv: Vector2) -> Vector2:
	pass

func project(b: Vector2) -> Vector2:
	pass

func reflect(line: Vector2) -> Vector2:
	pass

func rotated(angle: float) -> Vector2:
	pass

func round() -> Vector2:
	pass

func sign() -> Vector2:
	pass

func slerp(to: Vector2, weight: float) -> Vector2:
	pass

func slide(n: Vector2) -> Vector2:
	pass

func snapped(step: Vector2) -> Vector2:
	pass

func snappedf(step: float) -> Vector2:
	pass

