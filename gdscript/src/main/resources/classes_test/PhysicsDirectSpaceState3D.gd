extends Object
class_name PhysicsDirectSpaceState3D


func cast_motion(shape: PhysicsShapeQueryParameters3D, motion: Vector3) -> Array:
    pass;
func collide_shape(shape: PhysicsShapeQueryParameters3D, max_results: int) -> Array:
    pass;
func get_rest_info(shape: PhysicsShapeQueryParameters3D) -> Dictionary:
    pass;
func intersect_ray(from: Vector3, to: Vector3, exclude: Array, collision_mask: int, collide_with_bodies: bool, collide_with_areas: bool) -> Dictionary:
    pass;
func intersect_shape(shape: PhysicsShapeQueryParameters3D, max_results: int) -> Array:
    pass;
