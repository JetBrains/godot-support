extends RefCounted
#brief Collision data for [method PhysicsBody2D.move_and_collide] collisions.
#desc Contains collision data for [method PhysicsBody2D.move_and_collide] collisions. When a [PhysicsBody2D] is moved using [method PhysicsBody2D.move_and_collide], it stops if it detects a collision with another body. If a collision is detected, a [KinematicCollision2D] object is returned.
#desc This object contains information about the collision, including the colliding object, the remaining motion, and the collision position. This information can be used to calculate a collision response.
class_name KinematicCollision2D




#desc Returns the collision angle according to [param up_direction], which is [constant Vector2.UP] by default. This value is always positive.
func get_angle(up_direction: Vector2) -> float:
	pass;

#desc Returns the colliding body's attached [Object].
func get_collider() -> Object:
	pass;

#desc Returns the unique instance ID of the colliding body's attached [Object]. See [method Object.get_instance_id].
func get_collider_id() -> int:
	pass;

#desc Returns the colliding body's [RID] used by the [PhysicsServer2D].
func get_collider_rid() -> RID:
	pass;

#desc Returns the colliding body's shape.
func get_collider_shape() -> Object:
	pass;

#desc Returns the colliding body's shape index. See [CollisionObject2D].
func get_collider_shape_index() -> int:
	pass;

#desc Returns the colliding body's velocity.
func get_collider_velocity() -> Vector2:
	pass;

#desc Returns the colliding body's length of overlap along the collision normal.
func get_depth() -> float:
	pass;

#desc Returns the moving object's colliding shape.
func get_local_shape() -> Object:
	pass;

#desc Returns the colliding body's shape's normal at the point of collision.
func get_normal() -> Vector2:
	pass;

#desc Returns the point of collision in global coordinates.
func get_position() -> Vector2:
	pass;

#desc Returns the moving object's remaining movement vector.
func get_remainder() -> Vector2:
	pass;

#desc Returns the moving object's travel before collision.
func get_travel() -> Vector2:
	pass;


