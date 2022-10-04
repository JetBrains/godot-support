class_name PhysicsDirectSpaceState3DExtension




virtual func _cast_motion(shape_rid: RID, transform: Transform3D, motion: Vector3, margin: float, collision_mask: int, collide_with_bodies: bool, collide_with_areas: bool, closest_safe: float*, closest_unsafe: float*, info: PhysicsServer3DExtensionShapeRestInfo*) -> bool:
	pass;

virtual func _collide_shape(shape_rid: RID, transform: Transform3D, motion: Vector3, margin: float, collision_mask: int, collide_with_bodies: bool, collide_with_areas: bool, results: void*, max_results: int, result_count: int32_t*) -> bool:
	pass;

virtual const func _get_closest_point_to_object_volume(object: RID, point: Vector3) -> Vector3:
	pass;

virtual func _intersect_point(position: Vector3, collision_mask: int, collide_with_bodies: bool, collide_with_areas: bool, results: PhysicsServer3DExtensionShapeResult*, max_results: int) -> int:
	pass;

virtual func _intersect_ray(from: Vector3, to: Vector3, collision_mask: int, collide_with_bodies: bool, collide_with_areas: bool, hit_from_inside: bool, hit_back_faces: bool, result: PhysicsServer3DExtensionRayResult*) -> bool:
	pass;

virtual func _intersect_shape(shape_rid: RID, transform: Transform3D, motion: Vector3, margin: float, collision_mask: int, collide_with_bodies: bool, collide_with_areas: bool, result_count: PhysicsServer3DExtensionShapeResult*, max_results: int) -> int:
	pass;

virtual func _rest_info(shape_rid: RID, transform: Transform3D, motion: Vector3, margin: float, collision_mask: int, collide_with_bodies: bool, collide_with_areas: bool, rest_info: PhysicsServer3DExtensionShapeRestInfo*) -> bool:
	pass;


