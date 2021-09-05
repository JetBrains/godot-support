extends MeshInstance3D
class_name SoftBody3D

const DISABLE_MODE_REMOVE = 0;
const DISABLE_MODE_KEEP_ACTIVE = 1;

var collision_layer: int setget set_collision_layer, get_collision_layer;
var collision_mask: int setget set_collision_mask, get_collision_mask;
var damping_coefficient: float setget set_damping_coefficient, get_damping_coefficient;
var disable_mode: int setget set_disable_mode, get_disable_mode;
var drag_coefficient: float setget set_drag_coefficient, get_drag_coefficient;
var linear_stiffness: float setget set_linear_stiffness, get_linear_stiffness;
var parent_collision_ignore: NodePath setget set_parent_collision_ignore, get_parent_collision_ignore;
var pressure_coefficient: float setget set_pressure_coefficient, get_pressure_coefficient;
var ray_pickable: bool setget set_ray_pickable, is_ray_pickable;
var simulation_precision: int setget set_simulation_precision, get_simulation_precision;
var total_mass: float setget set_total_mass, get_total_mass;

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

