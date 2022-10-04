#brief Collision data for [method PhysicsBody3D.move_and_collide] collisions.
#desc Contains collision data for [method PhysicsBody3D.move_and_collide] collisions. When a [PhysicsBody3D] is moved using [method PhysicsBody3D.move_and_collide], it stops if it detects a collision with another body. If a collision is detected, a [KinematicCollision3D] object is returned.
#desc This object contains information about the collision, including the colliding object, the remaining motion, and the collision position. This information can be used to calculate a collision response.
class_name KinematicCollision3D




#desc Returns the collision angle according to [param up_direction], which is [constant Vector3.UP] by default. This value is always positive.
func get_angle(collision_index: int, up_direction: Vector3) -> float:
	pass;

#desc Returns the colliding body's attached [Object] given a collision index (the deepest collision by default).
func get_collider(collision_index: int) -> Object:
	pass;

#desc Returns the unique instance ID of the colliding body's attached [Object] given a collision index (the deepest collision by default). See [method Object.get_instance_id].
func get_collider_id(collision_index: int) -> int:
	pass;

#desc Returns the colliding body's [RID] used by the [PhysicsServer3D] given a collision index (the deepest collision by default).
func get_collider_rid(collision_index: int) -> RID:
	pass;

#desc Returns the colliding body's shape given a collision index (the deepest collision by default).
func get_collider_shape(collision_index: int) -> Object:
	pass;

#desc Returns the colliding body's shape index given a collision index (the deepest collision by default). See [CollisionObject3D].
func get_collider_shape_index(collision_index: int) -> int:
	pass;

#desc Returns the colliding body's velocity given a collision index (the deepest collision by default).
func get_collider_velocity(collision_index: int) -> Vector3:
	pass;

#desc Returns the number of detected collisions.
func get_collision_count() -> int:
	pass;

#desc Returns the colliding body's length of overlap along the collision normal.
func get_depth() -> float:
	pass;

#desc Returns the moving object's colliding shape given a collision index (the deepest collision by default).
func get_local_shape(collision_index: int) -> Object:
	pass;

#desc Returns the colliding body's shape's normal at the point of collision given a collision index (the deepest collision by default).
func get_normal(collision_index: int) -> Vector3:
	pass;

#desc Returns the point of collision in global coordinates given a collision index (the deepest collision by default).
func get_position(collision_index: int) -> Vector3:
	pass;

#desc Returns the moving object's remaining movement vector.
func get_remainder() -> Vector3:
	pass;

#desc Returns the moving object's travel before collision.
func get_travel() -> Vector3:
	pass;


