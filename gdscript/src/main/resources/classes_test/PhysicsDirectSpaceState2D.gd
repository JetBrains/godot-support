extends Object
class_name PhysicsDirectSpaceState2D


func cast_motion(shape: PhysicsShapeQueryParameters2D) -> Array:
    pass;
func collide_shape(shape: PhysicsShapeQueryParameters2D, max_results: int) -> Array:
    pass;
func get_rest_info(shape: PhysicsShapeQueryParameters2D) -> Dictionary:
    pass;
func intersect_point(point: Vector2, max_results: int, exclude: Array, collision_layer: int, collide_with_bodies: bool, collide_with_areas: bool) -> Array:
    pass;
func intersect_point_on_canvas(point: Vector2, canvas_instance_id: int, max_results: int, exclude: Array, collision_layer: int, collide_with_bodies: bool, collide_with_areas: bool) -> Array:
    pass;
func intersect_ray(from: Vector2, to: Vector2, exclude: Array, collision_layer: int, collide_with_bodies: bool, collide_with_areas: bool) -> Dictionary:
    pass;
func intersect_shape(shape: PhysicsShapeQueryParameters2D, max_results: int) -> Array:
    pass;
