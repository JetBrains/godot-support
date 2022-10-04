#brief Quaternion.
#desc A unit quaternion used for representing 3D rotations. Quaternions need to be normalized to be used for rotation.
#desc It is similar to Basis, which implements matrix representation of rotations, and can be parametrized using both an axis-angle pair or Euler angles. Basis stores rotation, scale, and shearing, while Quaternion only stores rotation.
#desc Due to its compactness and the way it is stored in memory, certain operations (obtaining axis-angle and performing SLERP, in particular) are more efficient and robust against floating-point errors.
class_name Quaternion

#desc The identity quaternion, representing no rotation. Equivalent to an identity [Basis] matrix. If a vector is transformed by an identity quaternion, it will not change.
const IDENTITY = Quaternion(0, 0, 0, 1);


#desc W component of the quaternion (real part).
#desc Quaternion components should usually not be manipulated directly.
var w: float;

#desc X component of the quaternion (imaginary [code]i[/code] axis part).
#desc Quaternion components should usually not be manipulated directly.
var x: float;

#desc Y component of the quaternion (imaginary [code]j[/code] axis part).
#desc Quaternion components should usually not be manipulated directly.
var y: float;

#desc Z component of the quaternion (imaginary [code]k[/code] axis part).
#desc Quaternion components should usually not be manipulated directly.
var z: float;


#desc Constructs a default-initialized quaternion with all components set to [code]0[/code].
func Quaternion() -> Quaternion:
	pass;

#desc Constructs a [Quaternion] as a copy of the given [Quaternion].
func Quaternion() -> Quaternion:
	pass;

func Quaternion(arc_from: Vector3, arc_to: Vector3) -> Quaternion:
	pass;

#desc Constructs a quaternion that will rotate around the given axis by the specified angle. The axis must be a normalized vector.
func Quaternion(axis: Vector3, angle: float) -> Quaternion:
	pass;

func Quaternion() -> Quaternion:
	pass;

#desc Constructs a quaternion from the given [Basis].
func Quaternion() -> Quaternion:
	pass;

#desc Constructs a quaternion defined by the given values.
func Quaternion(x: float, y: float, z: float, w: float) -> Quaternion:
	pass;


#desc Returns the angle between this quaternion and [param to]. This is the magnitude of the angle you would need to rotate by to get from one to the other.
#desc [b]Note:[/b] The magnitude of the floating-point error for this method is abnormally high, so methods such as [code]is_zero_approx[/code] will not work reliably.
func angle_to() -> float:
	pass;

#desc Returns the dot product of two quaternions.
func dot() -> float:
	pass;

func exp() -> Quaternion:
	pass;

func get_angle() -> float:
	pass;

func get_axis() -> Vector3:
	pass;

#desc Returns Euler angles (in the YXZ convention: when decomposing, first Z, then X, and Y last) corresponding to the rotation represented by the unit quaternion. Returned vector contains the rotation angles in the format (X angle, Y angle, Z angle).
func get_euler() -> Vector3:
	pass;

#desc Returns the inverse of the quaternion.
func inverse() -> Quaternion:
	pass;

#desc Returns [code]true[/code] if this quaternion and [param to] are approximately equal, by running [method @GlobalScope.is_equal_approx] on each component.
func is_equal_approx() -> bool:
	pass;

#desc Returns whether the quaternion is normalized or not.
func is_normalized() -> bool:
	pass;

#desc Returns the length of the quaternion.
func length() -> float:
	pass;

#desc Returns the length of the quaternion, squared.
func length_squared() -> float:
	pass;

func log() -> Quaternion:
	pass;

#desc Returns a copy of the quaternion, normalized to unit length.
func normalized() -> Quaternion:
	pass;

#desc Returns the result of the spherical linear interpolation between this quaternion and [param to] by amount [param weight].
#desc [b]Note:[/b] Both quaternions must be normalized.
func slerp(to: Quaternion, weight: float) -> Quaternion:
	pass;

#desc Returns the result of the spherical linear interpolation between this quaternion and [param to] by amount [param weight], but without checking if the rotation path is not bigger than 90 degrees.
func slerpni(to: Quaternion, weight: float) -> Quaternion:
	pass;

#desc Performs a spherical cubic interpolation between quaternions [param pre_a], this vector, [param b], and [param post_b], by the given amount [param weight].
func spherical_cubic_interpolate(b: Quaternion, pre_a: Quaternion, post_b: Quaternion, weight: float) -> Quaternion:
	pass;

#desc Performs a spherical cubic interpolation between quaternions [param pre_a], this vector, [param b], and [param post_b], by the given amount [param weight].
#desc It can perform smoother interpolation than [code]spherical_cubic_interpolate()[/code] by the time values.
func spherical_cubic_interpolate_in_time(b: Quaternion, pre_a: Quaternion, post_b: Quaternion, weight: float, b_t: float, pre_a_t: float, post_b_t: float) -> Quaternion:
	pass;


