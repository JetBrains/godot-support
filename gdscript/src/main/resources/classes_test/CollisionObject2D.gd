extends Node2D
class_name CollisionObject2D

const DISABLE_MODE_REMOVE = 0;
const DISABLE_MODE_MAKE_STATIC = 1;
const DISABLE_MODE_KEEP_ACTIVE = 2;

var collision_layer: int setget set_collision_layer, get_collision_layer;
var collision_mask: int setget set_collision_mask, get_collision_mask;
var disable_mode: int setget set_disable_mode, get_disable_mode;
var input_pickable: bool setget set_pickable, is_pickable;

func _input_event(viewport: Object, event: InputEvent, shape_idx: int) -> void:
    pass;

func create_shape_owner(owner: Object) -> int:
    pass;

func get_collision_layer_bit(bit: int) -> bool:
    pass;

func get_collision_mask_bit(bit: int) -> bool:
    pass;

func get_rid() -> RID:
    pass;

func get_shape_owner_one_way_collision_margin(owner_id: int) -> float:
    pass;

func get_shape_owners() -> Array:
    pass;

func is_shape_owner_disabled(owner_id: int) -> bool:
    pass;

func is_shape_owner_one_way_collision_enabled(owner_id: int) -> bool:
    pass;

func remove_shape_owner(owner_id: int) -> void:
    pass;

func set_collision_layer_bit(bit: int, value: bool) -> void:
    pass;

func set_collision_mask_bit(bit: int, value: bool) -> void:
    pass;

func shape_find_owner(shape_index: int) -> int:
    pass;

func shape_owner_add_shape(owner_id: int, shape: Shape2D) -> void:
    pass;

func shape_owner_clear_shapes(owner_id: int) -> void:
    pass;

func shape_owner_get_owner(owner_id: int) -> Object:
    pass;

func shape_owner_get_shape(owner_id: int, shape_id: int) -> Shape2D:
    pass;

func shape_owner_get_shape_count(owner_id: int) -> int:
    pass;

func shape_owner_get_shape_index(owner_id: int, shape_id: int) -> int:
    pass;

func shape_owner_get_transform(owner_id: int) -> Transform2D:
    pass;

func shape_owner_remove_shape(owner_id: int, shape_id: int) -> void:
    pass;

func shape_owner_set_disabled(owner_id: int, disabled: bool) -> void:
    pass;

func shape_owner_set_one_way_collision(owner_id: int, enable: bool) -> void:
    pass;

func shape_owner_set_one_way_collision_margin(owner_id: int, margin: float) -> void:
    pass;

func shape_owner_set_transform(owner_id: int, transform: Transform2D) -> void:
    pass;

