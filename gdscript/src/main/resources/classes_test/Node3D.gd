extends Node
class_name Node3D

const NOTIFICATION_TRANSFORM_CHANGED = 2000;
const NOTIFICATION_ENTER_WORLD = 41;
const NOTIFICATION_EXIT_WORLD = 42;
const NOTIFICATION_VISIBILITY_CHANGED = 43;

var gizmo: Node3DGizmo setget set_gizmo, get_gizmo;
var global_transform: Transform3D setget set_global_transform, get_global_transform;
var position: Vector3 setget set_position, get_position;
var rotation: Vector3 setget set_rotation, get_rotation;
var scale: Vector3 setget set_scale, get_scale;
var top_level: bool setget set_as_top_level, is_set_as_top_level;
var transform: Transform3D setget set_transform, get_transform;
var visibility_parent: NodePath setget set_visibility_parent, get_visibility_parent;
var visible: bool setget set_visible, is_visible;

func force_update_transform() -> void:
    pass;

func get_parent_node_3d() -> Node3D:
    pass;

func get_world_3d() -> World3D:
    pass;

func global_rotate(axis: Vector3, angle: float) -> void:
    pass;

func global_scale(scale: Vector3) -> void:
    pass;

func global_translate(offset: Vector3) -> void:
    pass;

func hide() -> void:
    pass;

func is_local_transform_notification_enabled() -> bool:
    pass;

func is_scale_disabled() -> bool:
    pass;

func is_transform_notification_enabled() -> bool:
    pass;

func is_visible_in_tree() -> bool:
    pass;

func look_at(target: Vector3, up: Vector3) -> void:
    pass;

func look_at_from_position(position: Vector3, target: Vector3, up: Vector3) -> void:
    pass;

func orthonormalize() -> void:
    pass;

func rotate(axis: Vector3, angle: float) -> void:
    pass;

func rotate_object_local(axis: Vector3, angle: float) -> void:
    pass;

func rotate_x(angle: float) -> void:
    pass;

func rotate_y(angle: float) -> void:
    pass;

func rotate_z(angle: float) -> void:
    pass;

func scale_object_local(scale: Vector3) -> void:
    pass;

func set_disable_scale(disable: bool) -> void:
    pass;

func set_identity() -> void:
    pass;

func set_ignore_transform_notification(enabled: bool) -> void:
    pass;

func set_notify_local_transform(enable: bool) -> void:
    pass;

func set_notify_transform(enable: bool) -> void:
    pass;

func show() -> void:
    pass;

func to_global(local_point: Vector3) -> Vector3:
    pass;

func to_local(global_point: Vector3) -> Vector3:
    pass;

func translate(offset: Vector3) -> void:
    pass;

func translate_object_local(offset: Vector3) -> void:
    pass;

func update_gizmo() -> void:
    pass;

