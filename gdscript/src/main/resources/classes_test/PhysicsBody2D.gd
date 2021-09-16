extends CollisionObject2D
class_name PhysicsBody2D

var input_pickable: bool;

func add_collision_exception_with(body: Node) -> void:
    pass;
func get_collision_exceptions() -> Array[PhysicsBody2D]:
    pass;
func move_and_collide(rel_vec: Vector2, infinite_inertia: bool, exclude_raycast_shapes: bool, test_only: bool, safe_margin: float) -> KinematicCollision2D:
    pass;
func remove_collision_exception_with(body: Node) -> void:
    pass;
func test_move(from: Transform2D, rel_vec: Vector2, infinite_inertia: bool, exclude_raycast_shapes: bool, collision: KinematicCollision2D, safe_margin: float) -> bool:
    pass;
