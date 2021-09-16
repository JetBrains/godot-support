extends MeshInstance3D
class_name SoftBody3D
const DISABLE_MODE_REMOVE = 0;
const DISABLE_MODE_KEEP_ACTIVE = 1;

var collision_layer: int;
var collision_mask: int;
var damping_coefficient: float;
var disable_mode: int;
var drag_coefficient: float;
var linear_stiffness: float;
var parent_collision_ignore: NodePath;
var pressure_coefficient: float;
var ray_pickable: bool;
var simulation_precision: int;
var total_mass: float;

func add_collision_exception_with(body: Node) -> void:
    pass;
func get_collision_exceptions() -> Array:
    pass;
func get_collision_layer_bit(bit: int) -> bool:
    pass;
func get_collision_mask_bit(bit: int) -> bool:
    pass;
func get_physics_rid() -> RID:
    pass;
func remove_collision_exception_with(body: Node) -> void:
    pass;
func set_collision_layer_bit(bit: int, value: bool) -> void:
    pass;
func set_collision_mask_bit(bit: int, value: bool) -> void:
    pass;
