extends RefCounted
#brief Result from a 2D body motion test.
#desc This class contains the motion and collision result from [method PhysicsServer2D.body_test_motion].
class_name PhysicsTestMotionResult2D




#desc Returns the colliding body's attached [Object], if a collision occurred.
func get_collider() -> Object:
	pass;

#desc Returns the unique instance ID of the colliding body's attached [Object], if a collision occurred. See [method Object.get_instance_id].
func get_collider_id() -> int:
	pass;

#desc Returns the colliding body's [RID] used by the [PhysicsServer2D], if a collision occurred.
func get_collider_rid() -> RID:
	pass;

#desc Returns the colliding body's shape index, if a collision occurred. See [CollisionObject2D].
func get_collider_shape() -> int:
	pass;

#desc Returns the colliding body's velocity, if a collision occurred.
func get_collider_velocity() -> Vector2:
	pass;

#desc Returns the length of overlap along the collision normal, if a collision occurred.
func get_collision_depth() -> float:
	pass;

#desc Returns the moving object's colliding shape, if a collision occurred.
func get_collision_local_shape() -> int:
	pass;

#desc Returns the colliding body's shape's normal at the point of collision, if a collision occurred.
func get_collision_normal() -> Vector2:
	pass;

#desc Returns the point of collision in global coordinates, if a collision occurred.
func get_collision_point() -> Vector2:
	pass;

#desc Returns the maximum fraction of the motion that can occur without a collision, between [code]0[/code] and [code]1[/code].
func get_collision_safe_fraction() -> float:
	pass;

#desc Returns the minimum fraction of the motion needed to collide, if a collision occurred, between [code]0[/code] and [code]1[/code].
func get_collision_unsafe_fraction() -> float:
	pass;

#desc Returns the moving object's remaining movement vector.
func get_remainder() -> Vector2:
	pass;

#desc Returns the moving object's travel before collision.
func get_travel() -> Vector2:
	pass;


