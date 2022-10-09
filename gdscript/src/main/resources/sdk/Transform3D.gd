#brief 3D transformation (3×4 matrix).
#desc 3×4 matrix (3 rows, 4 columns) used for 3D linear transformations. It can represent transformations such as translation, rotation, or scaling. It consists of a [member basis] (first 3 columns) and a [Vector3] for the [member origin] (last column).
#desc For more information, read the "Matrices and transforms" documentation article.
class_name Transform3D

#desc [Transform3D] with no translation, rotation or scaling applied. When applied to other data structures, [constant IDENTITY] performs no transformation.
const IDENTITY = Transform3D(1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0);

#desc [Transform3D] with mirroring applied perpendicular to the YZ plane.
const FLIP_X = Transform3D(-1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0);

#desc [Transform3D] with mirroring applied perpendicular to the XZ plane.
const FLIP_Y = Transform3D(1, 0, 0, 0, -1, 0, 0, 0, 1, 0, 0, 0);

#desc [Transform3D] with mirroring applied perpendicular to the XY plane.
const FLIP_Z = Transform3D(1, 0, 0, 0, 1, 0, 0, 0, -1, 0, 0, 0);


#desc The basis is a matrix containing 3 [Vector3] as its columns: X axis, Y axis, and Z axis. These vectors can be interpreted as the basis vectors of local coordinate system traveling with the object.
var basis: Basis;

#desc The translation offset of the transform (column 3, the fourth column). Equivalent to array index [code]3[/code].
var origin: Vector3;


#desc Constructs a default-initialized [Transform3D] set to [constant IDENTITY].
func Transform3D() -> Transform3D:
	pass;

#desc Constructs a [Transform3D] as a copy of the given [Transform3D].
func Transform3D(from: Transform3D) -> Transform3D:
	pass;

#desc Constructs a Transform3D from a [Basis] and [Vector3].
func Transform3D(basis: Basis, origin: Vector3) -> Transform3D:
	pass;

func Transform3D(from: Projection) -> Transform3D:
	pass;

#desc Constructs a Transform3D from four [Vector3] values (matrix columns). Each axis corresponds to local basis vectors (some of which may be scaled).
func Transform3D(x_axis: Vector3, y_axis: Vector3, z_axis: Vector3, origin: Vector3) -> Transform3D:
	pass;


#desc Returns the inverse of the transform, under the assumption that the transformation is composed of rotation, scaling and translation.
func affine_inverse() -> Transform3D:
	pass;

#desc Returns a transform interpolated between this transform and another by a given [param weight] (on the range of 0.0 to 1.0).
func interpolate_with(xform: Transform3D, weight: float) -> Transform3D:
	pass;

#desc Returns the inverse of the transform, under the assumption that the transformation is composed of rotation and translation (no scaling, use [method affine_inverse] for transforms with scaling).
func inverse() -> Transform3D:
	pass;

#desc Returns [code]true[/code] if this transform and [code]transform[/code] are approximately equal, by calling [code]is_equal_approx[/code] on each component.
func is_equal_approx(xform: Transform3D) -> bool:
	pass;

#desc Returns a copy of the transform rotated such that the forward axis (-Z) points towards the [param target] position.
#desc The up axis (+Y) points as close to the [param up] vector as possible while staying perpendicular to the forward axis. The resulting transform is orthonormalized. The existing rotation, scale, and skew information from the original transform is discarded. The [param target] and [param up] vectors cannot be zero, cannot be parallel to each other, and are defined in global/parent space.
func looking_at(target: Vector3, up: Vector3) -> Transform3D:
	pass;

#desc Returns the transform with the basis orthogonal (90 degrees), and normalized axis vectors (scale of 1 or -1).
func orthonormalized() -> Transform3D:
	pass;

#desc Returns a copy of the transform rotated around the given [param axis] by the given [param angle] (in radians).
#desc The [param axis] must be a normalized vector.
#desc This method is an optimized version of multiplying the given transform [code]X[/code]
#desc with a corresponding rotation transform [code]R[/code] from the left, i.e., [code]R * X[/code].
#desc This can be seen as transforming with respect to the global/parent frame.
func rotated(axis: Vector3, angle: float) -> Transform3D:
	pass;

#desc Returns a copy of the transform rotated around the given [param axis] by the given [param angle] (in radians).
#desc The [param axis] must be a normalized vector.
#desc This method is an optimized version of multiplying the given transform [code]X[/code]
#desc with a corresponding rotation transform [code]R[/code] from the right, i.e., [code]X * R[/code].
#desc This can be seen as transforming with respect to the local frame.
func rotated_local(axis: Vector3, angle: float) -> Transform3D:
	pass;

#desc Returns a copy of the transform scaled by the given [param scale] factor.
#desc This method is an optimized version of multiplying the given transform [code]X[/code]
#desc with a corresponding scaling transform [code]S[/code] from the left, i.e., [code]S * X[/code].
#desc This can be seen as transforming with respect to the global/parent frame.
func scaled(scale: Vector3) -> Transform3D:
	pass;

#desc Returns a copy of the transform scaled by the given [param scale] factor.
#desc This method is an optimized version of multiplying the given transform [code]X[/code]
#desc with a corresponding scaling transform [code]S[/code] from the right, i.e., [code]X * S[/code].
#desc This can be seen as transforming with respect to the local frame.
func scaled_local(scale: Vector3) -> Transform3D:
	pass;

#desc Returns a copy of the transform translated by the given [param offset].
#desc This method is an optimized version of multiplying the given transform [code]X[/code]
#desc with a corresponding translation transform [code]T[/code] from the left, i.e., [code]T * X[/code].
#desc This can be seen as transforming with respect to the global/parent frame.
func translated(offset: Vector3) -> Transform3D:
	pass;

#desc Returns a copy of the transform translated by the given [param offset].
#desc This method is an optimized version of multiplying the given transform [code]X[/code]
#desc with a corresponding translation transform [code]T[/code] from the right, i.e., [code]X * T[/code].
#desc This can be seen as transforming with respect to the local frame.
func translated_local(offset: Vector3) -> Transform3D:
	pass;


