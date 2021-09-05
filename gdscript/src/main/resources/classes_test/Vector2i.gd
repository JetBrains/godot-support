class_name Vector2i

const AXIS_X = 0;
const AXIS_Y = 1;
const ZERO = Vector2i(0, 0);
const ONE = Vector2i(1, 1);
const LEFT = Vector2i(-1, 0);
const RIGHT = Vector2i(1, 0);
const UP = Vector2i(0, -1);
const DOWN = Vector2i(0, 1);

var x: int setget , ;
var y: int setget , ;

func Vector2i() -> Vector2i:
    pass;

func Vector2i(from: Vector2i) -> Vector2i:
    pass;

func Vector2i(from: Vector2) -> Vector2i:
    pass;

func Vector2i(x: int, y: int) -> Vector2i:
    pass;

func abs() -> Vector2i:
    pass;

func aspect() -> float:
    pass;

func clamp(min: Vector2i, max: Vector2i) -> Vector2i:
    pass;

func operator !=(right: Vector2i) -> bool:
    pass;

func operator %(right: Vector2i) -> Vector2i:
    pass;

func operator %(right: int) -> Vector2i:
    pass;

func operator *(right: Vector2i) -> Vector2i:
    pass;

func operator *(right: float) -> Vector2i:
    pass;

func operator *(right: int) -> Vector2i:
    pass;

func operator +(right: Vector2i) -> Vector2i:
    pass;

func operator -(right: Vector2i) -> Vector2i:
    pass;

func operator /(right: Vector2i) -> Vector2i:
    pass;

func operator /(right: float) -> Vector2i:
    pass;

func operator /(right: int) -> Vector2i:
    pass;

func operator <(right: Vector2i) -> bool:
    pass;

func operator <=(right: Vector2i) -> bool:
    pass;

func operator ==(right: Vector2i) -> bool:
    pass;

func operator >(right: Vector2i) -> bool:
    pass;

func operator >=(right: Vector2i) -> bool:
    pass;

func operator [](index: int) -> int:
    pass;

func operator unary+() -> Vector2i:
    pass;

func operator unary-() -> Vector2i:
    pass;

func sign() -> Vector2i:
    pass;

