#brief 2D transformation (2×3 matrix).
#desc 2×3 matrix (2 rows, 3 columns) used for 2D linear transformations. It can represent transformations such as translation, rotation, or scaling. It consists of three [Vector2] values: [member x], [member y], and the [member origin].
#desc For more information, read the "Matrices and transforms" documentation article.
class_name Transform2D

#desc The identity [Transform2D] with no translation, rotation or scaling applied. When applied to other data structures, [constant IDENTITY] performs no transformation.
const IDENTITY = Transform2D(1, 0, 0, 1, 0, 0);

#desc The [Transform2D] that will flip something along the X axis.
const FLIP_X = Transform2D(-1, 0, 0, 1, 0, 0);

#desc The [Transform2D] that will flip something along the Y axis.
const FLIP_Y = Transform2D(1, 0, 0, -1, 0, 0);


#desc The origin vector (column 2, the third column). Equivalent to array index [code]2[/code]. The origin vector represents translation.
var origin: Vector2;

#desc The basis matrix's X vector (column 0). Equivalent to array index [code]0[/code].
var x: Vector2;

#desc The basis matrix's Y vector (column 1). Equivalent to array index [code]1[/code].
var y: Vector2;


#desc Constructs a default-initialized [Transform2D] set to [constant IDENTITY].
func Transform2D() -> Transform2D:
	pass;

#desc Constructs a [Transform2D] as a copy of the given [Transform2D].
func Transform2D(from: Transform2D) -> Transform2D:
	pass;

#desc Constructs the transform from a given angle (in radians) and position.
func Transform2D(rotation: float, position: Vector2) -> Transform2D:
	pass;

#desc Constructs the transform from a given angle (in radians), scale, skew (in radians) and position.
func Transform2D(rotation: float, scale: Vector2, skew: float, position: Vector2) -> Transform2D:
	pass;

#desc Constructs the transform from 3 [Vector2] values representing [member x], [member y], and the [member origin] (the three column vectors).
func Transform2D(x_axis: Vector2, y_axis: Vector2, origin: Vector2) -> Transform2D:
	pass;


#desc Returns the inverse of the transform, under the assumption that the transformation is composed of rotation, scaling and translation.
func affine_inverse() -> Transform2D:
	pass;

#desc Returns a vector transformed (multiplied) by the basis matrix.
#desc This method does not account for translation (the origin vector).
func basis_xform(v: Vector2) -> Vector2:
	pass;

#desc Returns a vector transformed (multiplied) by the inverse basis matrix.
#desc This method does not account for translation (the origin vector).
func basis_xform_inv(v: Vector2) -> Vector2:
	pass;

#desc Returns the transform's origin (translation).
func get_origin() -> Vector2:
	pass;

#desc Returns the transform's rotation (in radians).
func get_rotation() -> float:
	pass;

#desc Returns the scale.
func get_scale() -> Vector2:
	pass;

#desc Returns the transform's skew (in radians).
func get_skew() -> float:
	pass;

#desc Returns a transform interpolated between this transform and another by a given [param weight] (on the range of 0.0 to 1.0).
func interpolate_with(xform: Transform2D, weight: float) -> Transform2D:
	pass;

#desc Returns the inverse of the transform, under the assumption that the transformation is composed of rotation and translation (no scaling, use [method affine_inverse] for transforms with scaling).
func inverse() -> Transform2D:
	pass;

#desc Returns [code]true[/code] if this transform and [code]transform[/code] are approximately equal, by calling [code]is_equal_approx[/code] on each component.
func is_equal_approx(xform: Transform2D) -> bool:
	pass;

#desc Returns a copy of the transform rotated such that it's rotation on the X-axis points towards the [param target] position.
#desc Operations take place in global space.
func looking_at(target: Vector2) -> Transform2D:
	pass;

#desc Returns the transform with the basis orthogonal (90 degrees), and normalized axis vectors (scale of 1 or -1).
func orthonormalized() -> Transform2D:
	pass;

#desc Returns a copy of the transform rotated by the given [param angle] (in radians).
#desc This method is an optimized version of multiplying the given transform [code]X[/code]
#desc with a corresponding rotation transform [code]R[/code] from the left, i.e., [code]R * X[/code].
#desc This can be seen as transforming with respect to the global/parent frame.
func rotated(angle: float) -> Transform2D:
	pass;

#desc Returns a copy of the transform rotated by the given [param angle] (in radians).
#desc This method is an optimized version of multiplying the given transform [code]X[/code]
#desc with a corresponding rotation transform [code]R[/code] from the right, i.e., [code]X * R[/code].
#desc This can be seen as transforming with respect to the local frame.
func rotated_local(angle: float) -> Transform2D:
	pass;

#desc Returns a copy of the transform scaled by the given [param scale] factor.
#desc This method is an optimized version of multiplying the given transform [code]X[/code]
#desc with a corresponding scaling transform [code]S[/code] from the left, i.e., [code]S * X[/code].
#desc This can be seen as transforming with respect to the global/parent frame.
func scaled(scale: Vector2) -> Transform2D:
	pass;

#desc Returns a copy of the transform scaled by the given [param scale] factor.
#desc This method is an optimized version of multiplying the given transform [code]X[/code]
#desc with a corresponding scaling transform [code]S[/code] from the right, i.e., [code]X * S[/code].
#desc This can be seen as transforming with respect to the local frame.
func scaled_local(scale: Vector2) -> Transform2D:
	pass;

#desc Sets the transform's rotation (in radians).
func set_rotation(rotation: float) -> void:
	pass;

#desc Sets the transform's scale.
#desc [b]Note:[/b] Negative X scales in 2D are not decomposable from the transformation matrix. Due to the way scale is represented with transformation matrices in Godot, negative scales on the X axis will be changed to negative scales on the Y axis and a rotation of 180 degrees when decomposed.
func set_scale(scale: Vector2) -> void:
	pass;

#desc Sets the transform's skew (in radians).
func set_skew(skew: float) -> void:
	pass;

#desc Returns a copy of the transform translated by the given [param offset].
#desc This method is an optimized version of multiplying the given transform [code]X[/code]
#desc with a corresponding translation transform [code]T[/code] from the left, i.e., [code]T * X[/code].
#desc This can be seen as transforming with respect to the global/parent frame.
func translated(offset: Vector2) -> Transform2D:
	pass;

#desc Returns a copy of the transform translated by the given [param offset].
#desc This method is an optimized version of multiplying the given transform [code]X[/code]
#desc with a corresponding translation transform [code]T[/code] from the right, i.e., [code]X * T[/code].
#desc This can be seen as transforming with respect to the local frame.
func translated_local(offset: Vector2) -> Transform2D:
	pass;


