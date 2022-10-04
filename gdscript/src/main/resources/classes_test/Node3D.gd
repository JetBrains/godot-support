extends Node
#brief Most basic 3D game object, parent of all 3D-related nodes.
#desc Most basic 3D game object, with a 3D [Transform3D] and visibility settings. All other 3D game objects inherit from Node3D. Use [Node3D] as a parent node to move, scale, rotate and show/hide children in a 3D project.
#desc Affine operations (rotate, scale, translate) happen in parent's local coordinate system, unless the [Node3D] object is set as top-level. Affine operations in this coordinate system correspond to direct affine operations on the [Node3D]'s transform. The word local below refers to this coordinate system. The coordinate system that is attached to the [Node3D] object itself is referred to as object-local coordinate system.
#desc [b]Note:[/b] Unless otherwise specified, all methods that have angle parameters must have angles specified as [i]radians[/i]. To convert degrees to radians, use [method @GlobalScope.deg_to_rad].
class_name Node3D

#desc Node3D nodes receives this notification when their global transform changes. This means that either the current or a parent node changed its transform.
#desc In order for [constant NOTIFICATION_TRANSFORM_CHANGED] to work, users first need to ask for it, with [method set_notify_transform]. The notification is also sent if the node is in the editor context and it has at least one valid gizmo.
const NOTIFICATION_TRANSFORM_CHANGED = 2000;

#desc Node3D nodes receives this notification when they are registered to new [World3D] resource.
const NOTIFICATION_ENTER_WORLD = 41;

#desc Node3D nodes receives this notification when they are unregistered from current [World3D] resource.
const NOTIFICATION_EXIT_WORLD = 42;

#desc Node3D nodes receives this notification when their visibility changes.
const NOTIFICATION_VISIBILITY_CHANGED = 43;

#desc Node3D nodes receives this notification when their local transform changes. This is not received when the transform of a parent node is changed.
#desc In order for [constant NOTIFICATION_LOCAL_TRANSFORM_CHANGED] to work, users first need to ask for it, with [method set_notify_local_transform].
const NOTIFICATION_LOCAL_TRANSFORM_CHANGED = 44;

const ROTATION_EDIT_MODE_EULER = 0;

const ROTATION_EDIT_MODE_QUATERNION = 1;

const ROTATION_EDIT_MODE_BASIS = 2;

const ROTATION_ORDER_XYZ = 0;

const ROTATION_ORDER_XZY = 1;

const ROTATION_ORDER_YXZ = 2;

const ROTATION_ORDER_YZX = 3;

const ROTATION_ORDER_ZXY = 4;

const ROTATION_ORDER_ZYX = 5;


#desc Direct access to the 3x3 basis of the [Transform3D] property.
var basis: Basis;

#desc Global position of this node. This is equivalent to [code]global_transform.origin[/code].
var global_position: Vector3;

#desc Rotation part of the global transformation in radians, specified in terms of YXZ-Euler angles in the format (X angle, Y angle, Z angle).
#desc [b]Note:[/b] In the mathematical sense, rotation is a matrix and not a vector. The three Euler angles, which are the three independent parameters of the Euler-angle parametrization of the rotation matrix, are stored in a [Vector3] data structure not because the rotation is a vector, but only because [Vector3] exists as a convenient data-structure to store 3 floating-point numbers. Therefore, applying affine operations on the rotation "vector" is not meaningful.
var global_rotation: Vector3;

#desc World3D space (global) [Transform3D] of this node.
var global_transform: Transform3D;

#desc Local position or translation of this node relative to the parent. This is equivalent to [code]transform.origin[/code].
var position: Vector3;

#desc Access to the node rotation as a [Quaternion]. This property is ideal for tweening complex rotations.
var quaternion: Quaternion;

#desc Rotation part of the local transformation in radians, specified in terms of Euler angles. The angles construct a rotaton in the order specified by the [member rotation_order] property.
#desc [b]Note:[/b] In the mathematical sense, rotation is a matrix and not a vector. The three Euler angles, which are the three independent parameters of the Euler-angle parametrization of the rotation matrix, are stored in a [Vector3] data structure not because the rotation is a vector, but only because [Vector3] exists as a convenient data-structure to store 3 floating-point numbers. Therefore, applying affine operations on the rotation "vector" is not meaningful.
var rotation: Vector3;

#desc Specify how rotation (and scale) will be presented in the editor.
var rotation_edit_mode: int;

#desc Specify the axis rotation order of the [member rotation] property. The final orientation is constructed by rotating the Euler angles in the order specified by this property.
var rotation_order: int;

#desc Scale part of the local transformation.
#desc [b]Note:[/b] Mixed negative scales in 3D are not decomposable from the transformation matrix. Due to the way scale is represented with transformation matrices in Godot, the scale values will either be all positive or all negative.
var scale: Vector3;

#desc If [code]true[/code], the node will not inherit its transformations from its parent. Node transformations are only in global space.
var top_level: bool;

#desc Local space [Transform3D] of this node, with respect to the parent node.
var transform: Transform3D;

#desc Defines the visibility range parent for this node and its subtree. The visibility parent must be a GeometryInstance3D. Any visual instance will only be visible if the visibility parent (and all of its visibility ancestors) is hidden by being closer to the camera than its own [member GeometryInstance3D.visibility_range_begin]. Nodes hidden via the [member Node3D.visible] property are essentially removed from the visibility dependency tree, so dependent instances will not take the hidden node or its ancestors into account.
var visibility_parent: NodePath;

#desc If [code]true[/code], this node is drawn. The node is only visible if all of its antecedents are visible as well (in other words, [method is_visible_in_tree] must return [code]true[/code]).
var visible: bool;



#desc Attach a gizmo to this [code]Node3D[/code].
func add_gizmo(gizmo: Node3DGizmo) -> void:
	pass;

#desc Clear all gizmos attached to this [code]Node3D[/code].
func clear_gizmos() -> void:
	pass;

#desc Clears subgizmo selection for this node in the editor. Useful when subgizmo IDs become invalid after a property change.
func clear_subgizmo_selection() -> void:
	pass;

#desc Forces the transform to update. Transform changes in physics are not instant for performance reasons. Transforms are accumulated and then set. Use this if you need an up-to-date transform when doing physics operations.
func force_update_transform() -> void:
	pass;

#desc Returns all the gizmos attached to this [code]Node3D[/code].
func get_gizmos() -> Node3DGizmo[]:
	pass;

#desc Returns the parent [Node3D], or an empty [Object] if no parent exists or parent is not of type [Node3D].
func get_parent_node_3d() -> Node3D:
	pass;

#desc Returns the current [World3D] resource this [Node3D] node is registered to.
func get_world_3d() -> World3D:
	pass;

#desc Rotates the global (world) transformation around axis, a unit [Vector3], by specified angle in radians. The rotation axis is in global coordinate system.
func global_rotate(axis: Vector3, angle: float) -> void:
	pass;

#desc Scales the global (world) transformation by the given [Vector3] scale factors.
func global_scale(scale: Vector3) -> void:
	pass;

#desc Moves the global (world) transformation by [Vector3] offset. The offset is in global coordinate system.
func global_translate(offset: Vector3) -> void:
	pass;

#desc Disables rendering of this node. Changes [member visible] to [code]false[/code].
func hide() -> void:
	pass;

#desc Returns whether node notifies about its local transformation changes. [Node3D] will not propagate this by default.
func is_local_transform_notification_enabled() -> bool:
	pass;

#desc Returns whether this node uses a scale of [code](1, 1, 1)[/code] or its local transformation scale.
func is_scale_disabled() -> bool:
	pass;

#desc Returns whether the node notifies about its global and local transformation changes. [Node3D] will not propagate this by default.
func is_transform_notification_enabled() -> bool:
	pass;

#desc Returns [code]true[/code] if the node is present in the [SceneTree], its [member visible] property is [code]true[/code] and all its antecedents are also visible. If any antecedent is hidden, this node will not be visible in the scene tree.
func is_visible_in_tree() -> bool:
	pass;

#desc Rotates the node so that the local forward axis (-Z) points toward the [param target] position.
#desc The local up axis (+Y) points as close to the [param up] vector as possible while staying perpendicular to the local forward axis. The resulting transform is orthogonal, and the scale is preserved. Non-uniform scaling may not work correctly.
#desc The [param target] position cannot be the same as the node's position, the [param up] vector cannot be zero, and the direction from the node's position to the [param target] vector cannot be parallel to the [param up] vector.
#desc Operations take place in global space, which means that the node must be in the scene tree.
func look_at(target: Vector3, up: Vector3) -> void:
	pass;

#desc Moves the node to the specified [param position], and then rotates the node to point toward the [param target] as per [method look_at]. Operations take place in global space.
func look_at_from_position(position: Vector3, target: Vector3, up: Vector3) -> void:
	pass;

#desc Resets this node's transformations (like scale, skew and taper) preserving its rotation and translation by performing Gram-Schmidt orthonormalization on this node's [Transform3D].
func orthonormalize() -> void:
	pass;

#desc Rotates the local transformation around axis, a unit [Vector3], by specified angle in radians.
func rotate(axis: Vector3, angle: float) -> void:
	pass;

#desc Rotates the local transformation around axis, a unit [Vector3], by specified angle in radians. The rotation axis is in object-local coordinate system.
func rotate_object_local(axis: Vector3, angle: float) -> void:
	pass;

#desc Rotates the local transformation around the X axis by angle in radians.
func rotate_x(angle: float) -> void:
	pass;

#desc Rotates the local transformation around the Y axis by angle in radians.
func rotate_y(angle: float) -> void:
	pass;

#desc Rotates the local transformation around the Z axis by angle in radians.
func rotate_z(angle: float) -> void:
	pass;

#desc Scales the local transformation by given 3D scale factors in object-local coordinate system.
func scale_object_local(scale: Vector3) -> void:
	pass;

#desc Sets whether the node uses a scale of [code](1, 1, 1)[/code] or its local transformation scale. Changes to the local transformation scale are preserved.
func set_disable_scale(disable: bool) -> void:
	pass;

#desc Reset all transformations for this node (sets its [Transform3D] to the identity matrix).
func set_identity() -> void:
	pass;

#desc Sets whether the node ignores notification that its transformation (global or local) changed.
func set_ignore_transform_notification(enabled: bool) -> void:
	pass;

#desc Sets whether the node notifies about its local transformation changes. [Node3D] will not propagate this by default.
func set_notify_local_transform(enable: bool) -> void:
	pass;

#desc Sets whether the node notifies about its global and local transformation changes. [Node3D] will not propagate this by default, unless it is in the editor context and it has a valid gizmo.
func set_notify_transform(enable: bool) -> void:
	pass;

#desc Set subgizmo selection for this node in the editor.
func set_subgizmo_selection(gizmo: Node3DGizmo, id: int, transform: Transform3D) -> void:
	pass;

#desc Enables rendering of this node. Changes [member visible] to [code]true[/code].
func show() -> void:
	pass;

#desc Transforms [param local_point] from this node's local space to world space.
func to_global(local_point: Vector3) -> Vector3:
	pass;

#desc Transforms [param global_point] from world space to this node's local space.
func to_local(global_point: Vector3) -> Vector3:
	pass;

#desc Changes the node's position by the given offset [Vector3].
#desc Note that the translation [param offset] is affected by the node's scale, so if scaled by e.g. [code](10, 1, 1)[/code], a translation by an offset of [code](2, 0, 0)[/code] would actually add 20 ([code]2 * 10[/code]) to the X coordinate.
func translate(offset: Vector3) -> void:
	pass;

#desc Changes the node's position by the given offset [Vector3] in local space.
func translate_object_local(offset: Vector3) -> void:
	pass;

#desc Updates all the [Node3DGizmo]s attached to this node.
func update_gizmos() -> void:
	pass;


