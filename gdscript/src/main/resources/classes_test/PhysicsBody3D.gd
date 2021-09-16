extends CollisionObject3D
class_name PhysicsBody3D

var axis_lock_angular_x: bool;
var axis_lock_angular_y: bool;
var axis_lock_angular_z: bool;
var axis_lock_linear_x: bool;
var axis_lock_linear_y: bool;
var axis_lock_linear_z: bool;

func add_collision_exception_with(body: Node) -> void:
    pass;
func get_axis_lock(axis: int) -> bool:
    pass;
func get_collision_exceptions() -> Array[PhysicsBody3D]:
    pass;
func move_and_collide(rel_vec: Vector3, infinite_inertia: bool, exclude_raycast_shapes: bool, test_only: bool, safe_margin: float) -> KinematicCollision3D:
    pass;
func remove_collision_exception_with(body: Node) -> void:
    pass;
func set_axis_lock(axis: int, lock: bool) -> void:
    pass;
func test_move(from: Transform3D, rel_vec: Vector3, infinite_inertia: bool, exclude_raycast_shapes: bool, collision: KinematicCollision3D, safe_margin: float) -> bool:
    pass;
