extends Node3D
class_name CollisionObject3D

const DISABLE_MODE_REMOVE = 0;
const DISABLE_MODE_MAKE_STATIC = 1;
const DISABLE_MODE_KEEP_ACTIVE = 2;

var collision_layer: int setget set_collision_layer, get_collision_layer;
var collision_mask: int setget set_collision_mask, get_collision_mask;
var disable_mode: int setget set_disable_mode, get_disable_mode;
var input_capture_on_drag: bool setget set_capture_input_on_drag, get_capture_input_on_drag;
var input_ray_pickable: bool setget set_ray_pickable, is_ray_pickable;

func _input_event(camera: Object, event: InputEvent, position: Vector3, normal: Vector3, shape_idx: int) -> void:
    pass;

func create_shape_owner(owner: Object) -> int:
    pass;

func get_collision_layer_bit(bit: int) -> bool:
    pass;

func get_collision_mask_bit(bit: int) -> bool:
    pass;

func get_rid() -> RID:
    pass;

func get_shape_owners() -> Array:
    pass;

func is_shape_owner_disabled(owner_id: int) -> bool:
    pass;

func remove_shape_owner(owner_id: int) -> void:
    pass;

func set_collision_layer_bit(bit: int, value: bool) -> void:
    pass;

func set_collision_mask_bit(bit: int, value: bool) -> void:
    pass;

func shape_find_owner(shape_index: int) -> int:
    pass;

func shape_owner_add_shape(owner_id: int, shape: Shape3D) -> void:
    pass;

func shape_owner_clear_shapes(owner_id: int) -> void:
    pass;

func shape_owner_get_owner(owner_id: int) -> Object:
    pass;

func shape_owner_get_shape(owner_id: int, shape_id: int) -> Shape3D:
    pass;

func shape_owner_get_shape_count(owner_id: int) -> int:
    pass;

func shape_owner_get_shape_index(owner_id: int, shape_id: int) -> int:
    pass;

func shape_owner_get_transform(owner_id: int) -> Transform3D:
    pass;

func shape_owner_remove_shape(owner_id: int, shape_id: int) -> void:
    pass;

func shape_owner_set_disabled(owner_id: int, disabled: bool) -> void:
    pass;

func shape_owner_set_transform(owner_id: int, transform: Transform3D) -> void:
    pass;

