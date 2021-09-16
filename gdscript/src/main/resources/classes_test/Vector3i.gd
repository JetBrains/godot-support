class_name Vector3i
const AXIS_X = 0;
const AXIS_Y = 1;
const AXIS_Z = 2;
const ZERO = Vector3i(0, 0, 0);
const ONE = Vector3i(1, 1, 1);
const LEFT = Vector3i(-1, 0, 0);
const RIGHT = Vector3i(1, 0, 0);
const UP = Vector3i(0, 1, 0);
const DOWN = Vector3i(0, -1, 0);
const FORWARD = Vector3i(0, 0, -1);
const BACK = Vector3i(0, 0, 1);

var x: int;
var y: int;
var z: int;

func Vector3i() -> Vector3i:
    pass;
func Vector3i(from: Vector3i) -> Vector3i:
    pass;
func Vector3i(from: Vector3) -> Vector3i:
    pass;
func Vector3i(x: int, y: int, z: int) -> Vector3i:
    pass;
func abs() -> Vector3i:
    pass;
func clamp(min: Vector3i, max: Vector3i) -> Vector3i:
    pass;
func max_axis() -> int:
    pass;
func min_axis() -> int:
    pass;
func sign() -> Vector3i:
    pass;
