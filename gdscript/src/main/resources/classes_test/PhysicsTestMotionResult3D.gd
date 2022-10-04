#brief Result from a 3D body motion test.
#desc This class contains the motion and collision result from [method PhysicsServer3D.body_test_motion].
class_name PhysicsTestMotionResult3D




#desc Returns the colliding body's attached [Object] given a collision index (the deepest collision by default), if a collision occurred.
func get_collider(collision_index: int) -> Object:
	pass;

#desc Returns the unique instance ID of the colliding body's attached [Object] given a collision index (the deepest collision by default), if a collision occurred. See [method Object.get_instance_id].
func get_collider_id(collision_index: int) -> int:
	pass;

#desc Returns the colliding body's [RID] used by the [PhysicsServer3D] given a collision index (the deepest collision by default), if a collision occurred.
func get_collider_rid(collision_index: int) -> RID:
	pass;

#desc Returns the colliding body's shape index given a collision index (the deepest collision by default), if a collision occurred. See [CollisionObject3D].
func get_collider_shape(collision_index: int) -> int:
	pass;

#desc Returns the colliding body's velocity given a collision index (the deepest collision by default), if a collision occurred.
func get_collider_velocity(collision_index: int) -> Vector3:
	pass;

#desc Returns the number of detected collisions.
func get_collision_count() -> int:
	pass;

#desc Returns the length of overlap along the collision normal given a collision index (the deepest collision by default), if a collision occurred.
func get_collision_depth(collision_index: int) -> float:
	pass;

#desc Returns the moving object's colliding shape given a collision index (the deepest collision by default), if a collision occurred.
func get_collision_local_shape(collision_index: int) -> int:
	pass;

#desc Returns the colliding body's shape's normal at the point of collision given a collision index (the deepest collision by default), if a collision occurred.
func get_collision_normal(collision_index: int) -> Vector3:
	pass;

#desc Returns the point of collision in global coordinates given a collision index (the deepest collision by default), if a collision occurred.
func get_collision_point(collision_index: int) -> Vector3:
	pass;

#desc Returns the maximum fraction of the motion that can occur without a collision, between [code]0[/code] and [code]1[/code].
func get_collision_safe_fraction() -> float:
	pass;

#desc Returns the minimum fraction of the motion needed to collide, if a collision occurred, between [code]0[/code] and [code]1[/code].
func get_collision_unsafe_fraction() -> float:
	pass;

#desc Returns the moving object's remaining movement vector.
func get_remainder() -> Vector3:
	pass;

#desc Returns the moving object's travel before collision.
func get_travel() -> Vector3:
	pass;


