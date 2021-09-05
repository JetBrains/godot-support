class_name Plane

const PLANE_YZ = Plane(1, 0, 0, 0);
const PLANE_XZ = Plane(0, 1, 0, 0);
const PLANE_XY = Plane(0, 0, 1, 0);

var d: float setget , ;
var normal: Vector3 setget , ;
var x: float setget , ;
var y: float setget , ;
var z: float setget , ;

func Plane() -> Plane:
    pass;

func Plane(from: Plane) -> Plane:
    pass;

func Plane(a: float, b: float, c: float, d: float) -> Plane:
    pass;

func Plane(normal: Vector3, d: float) -> Plane:
    pass;

func Plane(point: Vector3, normal: Vector3) -> Plane:
    pass;

func Plane(point1: Vector3, point2: Vector3, point3: Vector3) -> Plane:
    pass;

func center() -> Vector3:
    pass;

func distance_to(point: Vector3) -> float:
    pass;

func has_point(point: Vector3, epsilon: float) -> bool:
    pass;

func intersect_3(b: Plane, c: Plane) -> Variant:
    pass;

func intersects_ray(from: Vector3, dir: Vector3) -> Variant:
    pass;

func intersects_segment(from: Vector3, to: Vector3) -> Variant:
    pass;

func is_equal_approx(to_plane: Plane) -> bool:
    pass;

func is_point_over(plane: Vector3) -> bool:
    pass;

func normalized() -> Plane:
    pass;

func operator !=(right: Plane) -> bool:
    pass;

func operator ==(right: Plane) -> bool:
    pass;

func operator unary+() -> Plane:
    pass;

func operator unary-() -> Plane:
    pass;

func project(point: Vector3) -> Vector3:
    pass;

