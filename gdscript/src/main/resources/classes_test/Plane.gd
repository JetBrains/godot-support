#brief Plane in hessian form.
#desc Plane represents a normalized plane equation. Basically, "normal" is the normal of the plane (a,b,c normalized), and "d" is the distance from the origin to the plane (in the direction of "normal"). "Over" or "Above" the plane is considered the side of the plane towards where the normal is pointing.
class_name Plane

#desc A plane that extends in the Y and Z axes (normal vector points +X).
const PLANE_YZ = Plane(1, 0, 0, 0);

#desc A plane that extends in the X and Z axes (normal vector points +Y).
const PLANE_XZ = Plane(0, 1, 0, 0);

#desc A plane that extends in the X and Y axes (normal vector points +Z).
const PLANE_XY = Plane(0, 0, 1, 0);


#desc The distance from the origin to the plane, in the direction of [member normal]. This value is typically non-negative.
#desc In the scalar equation of the plane [code]ax + by + cz = d[/code], this is [code]d[/code], while the [code](a, b, c)[/code] coordinates are represented by the [member normal] property.
var d: float;

#desc The normal of the plane, which must be normalized.
#desc In the scalar equation of the plane [code]ax + by + cz = d[/code], this is the vector [code](a, b, c)[/code], where [code]d[/code] is the [member d] property.
var normal: Vector3;

#desc The X component of the plane's [member normal] vector.
var x: float;

#desc The Y component of the plane's [member normal] vector.
var y: float;

#desc The Z component of the plane's [member normal] vector.
var z: float;


#desc Constructs a default-initialized [Plane] with all components set to [code]0[/code].
func Plane() -> Plane:
	pass;

#desc Constructs a [Plane] as a copy of the given [Plane].
func Plane() -> Plane:
	pass;

#desc Creates a plane from the four parameters. The three components of the resulting plane's [member normal] are [param a], [param b] and [param c], and the plane has a distance of [param d] from the origin.
func Plane(a: float, b: float, c: float, d: float) -> Plane:
	pass;

#desc Creates a plane from the normal vector. The plane will intersect the origin.
func Plane() -> Plane:
	pass;

#desc Creates a plane from the normal vector and the plane's distance from the origin.
func Plane(normal: Vector3, d: float) -> Plane:
	pass;

#desc Creates a plane from the normal vector and a point on the plane.
func Plane(normal: Vector3, point: Vector3) -> Plane:
	pass;

#desc Creates a plane from the three points, given in clockwise order.
func Plane(point1: Vector3, point2: Vector3, point3: Vector3) -> Plane:
	pass;


#desc Returns the center of the plane.
func center() -> Vector3:
	pass;

#desc Returns the shortest distance from the plane to the position [param point]. If the point is above the plane, the distance will be positive. If below, the distance will be negative.
func distance_to() -> float:
	pass;

#desc Returns [code]true[/code] if [param point] is inside the plane. Comparison uses a custom minimum [param tolerance] threshold.
func has_point(point: Vector3, tolerance: float) -> bool:
	pass;

#desc Returns the intersection point of the three planes [param b], [param c] and this plane. If no intersection is found, [code]null[/code] is returned.
func intersect_3(b: Plane, c: Plane) -> Variant:
	pass;

#desc Returns the intersection point of a ray consisting of the position [param from] and the direction normal [param dir] with this plane. If no intersection is found, [code]null[/code] is returned.
func intersects_ray(from: Vector3, dir: Vector3) -> Variant:
	pass;

#desc Returns the intersection point of a segment from position [param from] to position [param to] with this plane. If no intersection is found, [code]null[/code] is returned.
func intersects_segment(from: Vector3, to: Vector3) -> Variant:
	pass;

#desc Returns [code]true[/code] if this plane and [param to_plane] are approximately equal, by running [method @GlobalScope.is_equal_approx] on each component.
func is_equal_approx() -> bool:
	pass;

#desc Returns [code]true[/code] if [param point] is located above the plane.
func is_point_over() -> bool:
	pass;

#desc Returns a copy of the plane, normalized.
func normalized() -> Plane:
	pass;

#desc Returns the orthogonal projection of [param point] into a point in the plane.
func project() -> Vector3:
	pass;


