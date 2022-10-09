extends CollisionObject3D
#brief Base class for all objects affected by physics in 3D space.
#desc PhysicsBody3D is an abstract base class for implementing a physics body. All *Body types inherit from it.
class_name PhysicsBody3D


#desc Lock the body's rotation in the X axis.
var axis_lock_angular_x: bool;

#desc Lock the body's rotation in the Y axis.
var axis_lock_angular_y: bool;

#desc Lock the body's rotation in the Z axis.
var axis_lock_angular_z: bool;

#desc Lock the body's linear movement in the X axis.
var axis_lock_linear_x: bool;

#desc Lock the body's linear movement in the Y axis.
var axis_lock_linear_y: bool;

#desc Lock the body's linear movement in the Z axis.
var axis_lock_linear_z: bool;



#desc Adds a body to the list of bodies that this body can't collide with.
func add_collision_exception_with(body: Node) -> void:
	pass;

#desc Returns [code]true[/code] if the specified linear or rotational [param axis] is locked.
func get_axis_lock(axis: int) -> bool:
	pass;

#desc Returns an array of nodes that were added as collision exceptions for this body.
func get_collision_exceptions() -> Array[PhysicsBody3D]:
	pass;

#desc Moves the body along the vector [param distance]. In order to be frame rate independent in [method Node._physics_process] or [method Node._process], [param distance] should be computed using [code]delta[/code].
#desc The body will stop if it collides. Returns a [KinematicCollision3D], which contains information about the collision when stopped, or when touching another body along the motion.
#desc If [param test_only] is [code]true[/code], the body does not move but the would-be collision information is given.
#desc [param safe_margin] is the extra margin used for collision recovery (see [member CharacterBody3D.safe_margin] for more details).
#desc If [param recovery_as_collision] is [code]true[/code], any depenetration from the recovery phase is also reported as a collision; this is used e.g. by [CharacterBody3D] for improving floor detection during floor snapping.
#desc [param max_collisions] allows to retrieve more than one collision result.
func move_and_collide(distance: Vector3, test_only: bool, safe_margin: float, recovery_as_collision: bool, max_collisions: int) -> KinematicCollision3D:
	pass;

#desc Removes a body from the list of bodies that this body can't collide with.
func remove_collision_exception_with(body: Node) -> void:
	pass;

#desc Locks or unlocks the specified linear or rotational [param axis] depending on the value of [param lock].
func set_axis_lock(axis: int, lock: bool) -> void:
	pass;

#desc Checks for collisions without moving the body. In order to be frame rate independent in [method Node._physics_process] or [method Node._process], [param distance] should be computed using [code]delta[/code].
#desc Virtually sets the node's position, scale and rotation to that of the given [Transform3D], then tries to move the body along the vector [param distance]. Returns [code]true[/code] if a collision would stop the body from moving along the whole path.
#desc [param collision] is an optional object of type [KinematicCollision3D], which contains additional information about the collision when stopped, or when touching another body along the motion.
#desc [param safe_margin] is the extra margin used for collision recovery (see [member CharacterBody3D.safe_margin] for more details).
#desc If [param recovery_as_collision] is [code]true[/code], any depenetration from the recovery phase is also reported as a collision; this is useful for checking whether the body would [i]touch[/i] any other bodies.
#desc [param max_collisions] allows to retrieve more than one collision result.
func test_move(from: Transform3D, distance: Vector3, collision: KinematicCollision3D, safe_margin: float, recovery_as_collision: bool, max_collisions: int) -> bool:
	pass;


