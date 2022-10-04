#brief 3×3 matrix datatype.
#desc 3×3 matrix used for 3D rotation and scale. Almost always used as an orthogonal basis for a [Transform3D].
#desc Contains 3 vector fields X, Y and Z as its columns, which are typically interpreted as the local basis vectors of a transformation. For such use, it is composed of a scaling and a rotation matrix, in that order (M = R.S).
#desc Can also be accessed as array of 3D vectors. These vectors are normally orthogonal to each other, but are not necessarily normalized (due to scaling).
#desc For more information, read the "Matrices and transforms" documentation article.
class_name Basis

const EULER_ORDER_XYZ = 0;

const EULER_ORDER_XZY = 1;

const EULER_ORDER_YXZ = 2;

const EULER_ORDER_YZX = 3;

const EULER_ORDER_ZXY = 4;

const EULER_ORDER_ZYX = 5;

#desc The identity basis, with no rotation or scaling applied.
#desc This is identical to calling [code]Basis()[/code] without any parameters. This constant can be used to make your code clearer, and for consistency with C#.
const IDENTITY = Basis(1, 0, 0, 0, 1, 0, 0, 0, 1);

#desc The basis that will flip something along the X axis when used in a transformation.
const FLIP_X = Basis(-1, 0, 0, 0, 1, 0, 0, 0, 1);

#desc The basis that will flip something along the Y axis when used in a transformation.
const FLIP_Y = Basis(1, 0, 0, 0, -1, 0, 0, 0, 1);

#desc The basis that will flip something along the Z axis when used in a transformation.
const FLIP_Z = Basis(1, 0, 0, 0, 1, 0, 0, 0, -1);


#desc The basis matrix's X vector (column 0). Equivalent to array index [code]0[/code].
var x: Vector3;

#desc The basis matrix's Y vector (column 1). Equivalent to array index [code]1[/code].
var y: Vector3;

#desc The basis matrix's Z vector (column 2). Equivalent to array index [code]2[/code].
var z: Vector3;


#desc Constructs a default-initialized [Basis] set to [constant IDENTITY].
func Basis() -> Basis:
	pass;

#desc Constructs a [Basis] as a copy of the given [Basis].
func Basis() -> Basis:
	pass;

#desc Constructs a pure rotation basis matrix, rotated around the given [param axis] by [param angle] (in radians). The axis must be a normalized vector.
func Basis(axis: Vector3, angle: float) -> Basis:
	pass;

#desc Constructs a pure rotation basis matrix from the given quaternion.
func Basis() -> Basis:
	pass;

#desc Constructs a basis matrix from 3 axis vectors (matrix columns).
func Basis(x_axis: Vector3, y_axis: Vector3, z_axis: Vector3) -> Basis:
	pass;


#desc Returns the determinant of the basis matrix. If the basis is uniformly scaled, its determinant is the square of the scale.
#desc A negative determinant means the basis has a negative scale. A zero determinant means the basis isn't invertible, and is usually considered invalid.
func determinant() -> float:
	pass;

static func from_euler(euler: Vector3, order: int) -> Basis:
	pass;

#desc Constructs a pure scale basis matrix with no rotation or shearing. The scale values are set as the diagonal of the matrix, and the other parts of the matrix are zero.
static func from_scale() -> Basis:
	pass;

#desc Returns the basis's rotation in the form of Euler angles. The Euler order depends on the [param order] parameter, by default it uses the YXZ convention: when decomposing, first Z, then X, and Y last. The returned vector contains the rotation angles in the format (X angle, Y angle, Z angle).
#desc Consider using the [method get_rotation_quaternion] method instead, which returns a [Quaternion] quaternion instead of Euler angles.
func get_euler() -> Vector3:
	pass;

#desc Returns the basis's rotation in the form of a quaternion. See [method get_euler] if you need Euler angles, but keep in mind quaternions should generally be preferred to Euler angles.
func get_rotation_quaternion() -> Quaternion:
	pass;

#desc Assuming that the matrix is the combination of a rotation and scaling, return the absolute value of scaling factors along each axis.
func get_scale() -> Vector3:
	pass;

#desc Returns the inverse of the matrix.
func inverse() -> Basis:
	pass;

#desc Returns [code]true[/code] if this basis and [param b] are approximately equal, by calling [code]is_equal_approx[/code] on each component.
func is_equal_approx() -> bool:
	pass;

#desc Creates a Basis with a rotation such that the forward axis (-Z) points towards the [param target] position.
#desc The up axis (+Y) points as close to the [param up] vector as possible while staying perpendicular to the forward axis. The resulting Basis is orthonormalized. The [param target] and [param up] vectors cannot be zero, and cannot be parallel to each other.
static func looking_at(target: Vector3, up: Vector3) -> Basis:
	pass;

#desc Returns the orthonormalized version of the matrix (useful to call from time to time to avoid rounding error for orthogonal matrices). This performs a Gram-Schmidt orthonormalization on the basis of the matrix.
func orthonormalized() -> Basis:
	pass;

#desc Introduce an additional rotation around the given axis by [param angle] (in radians). The axis must be a normalized vector.
func rotated(axis: Vector3, angle: float) -> Basis:
	pass;

#desc Introduce an additional scaling specified by the given 3D scaling factor.
func scaled() -> Basis:
	pass;

#desc Assuming that the matrix is a proper rotation matrix, slerp performs a spherical-linear interpolation with another rotation matrix.
func slerp(to: Basis, weight: float) -> Basis:
	pass;

#desc Transposed dot product with the X axis of the matrix.
func tdotx() -> float:
	pass;

#desc Transposed dot product with the Y axis of the matrix.
func tdoty() -> float:
	pass;

#desc Transposed dot product with the Z axis of the matrix.
func tdotz() -> float:
	pass;

#desc Returns the transposed version of the matrix.
func transposed() -> Basis:
	pass;


