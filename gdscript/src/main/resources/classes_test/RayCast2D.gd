#brief Query the closest object intersecting a ray.
#desc A RayCast represents a line from its origin to its destination position, [member target_position]. It is used to query the 2D space in order to find the closest object along the path of the ray.
#desc RayCast2D can ignore some objects by adding them to the exception list via [method add_exception], by setting proper filtering with collision layers, or by filtering object types with type masks.
#desc RayCast2D can be configured to report collisions with [Area2D]s ([member collide_with_areas]) and/or [PhysicsBody2D]s ([member collide_with_bodies]).
#desc Only enabled raycasts will be able to query the space and report collisions.
#desc RayCast2D calculates intersection every physics frame (see [Node]), and the result is cached so it can be used later until the next frame. If multiple queries are required between physics frames (or during the same frame) use [method force_raycast_update] after adjusting the raycast.
class_name RayCast2D


#desc If [code]true[/code], collision with [Area2D]s will be reported.
var collide_with_areas: bool;

#desc If [code]true[/code], collision with [PhysicsBody2D]s will be reported.
var collide_with_bodies: bool;

#desc The ray's collision mask. Only objects in at least one collision layer enabled in the mask will be detected. See [url=$DOCS_URL/tutorials/physics/physics_introduction.html#collision-layers-and-masks]Collision layers and masks[/url] in the documentation for more information.
var collision_mask: int;

#desc If [code]true[/code], collisions will be reported.
var enabled: bool;

#desc If [code]true[/code], the parent node will be excluded from collision detection.
var exclude_parent: bool;

#desc If [code]true[/code], the ray will detect a hit when starting inside shapes. In this case the collision normal will be [code]Vector2(0, 0)[/code]. Does not affect concave polygon shapes.
var hit_from_inside: bool;

#desc The ray's destination point, relative to the RayCast's [code]position[/code].
var target_position: Vector2;



#desc Adds a collision exception so the ray does not report collisions with the specified [CollisionObject2D] node.
func add_exception() -> void:
	pass;

#desc Adds a collision exception so the ray does not report collisions with the specified [RID].
func add_exception_rid() -> void:
	pass;

#desc Removes all collision exceptions for this ray.
func clear_exceptions() -> void:
	pass;

#desc Updates the collision information for the ray. Use this method to update the collision information immediately instead of waiting for the next [code]_physics_process[/code] call, for example if the ray or its parent has changed state.
#desc [b]Note:[/b] [member enabled] does not need to be [code]true[/code] for this to work.
func force_raycast_update() -> void:
	pass;

#desc Returns the first object that the ray intersects, or [code]null[/code] if no object is intersecting the ray (i.e. [method is_colliding] returns [code]false[/code]).
func get_collider() -> Object:
	pass;

#desc Returns the [RID] of the first object that the ray intersects, or an empty [RID] if no object is intersecting the ray (i.e. [method is_colliding] returns [code]false[/code]).
func get_collider_rid() -> RID:
	pass;

#desc Returns the shape ID of the first object that the ray intersects, or [code]0[/code] if no object is intersecting the ray (i.e. [method is_colliding] returns [code]false[/code]).
func get_collider_shape() -> int:
	pass;

#desc Returns whether or not the specified layer of the [member collision_mask] is enabled, given a [param layer_number] between 1 and 32.
func get_collision_mask_value() -> bool:
	pass;

#desc Returns the normal of the intersecting object's shape at the collision point, or [code]Vector2(0, 0)[/code] if the ray starts inside the shape and [member hit_from_inside] is [code]true[/code].
func get_collision_normal() -> Vector2:
	pass;

#desc Returns the collision point at which the ray intersects the closest object.
#desc [b]Note:[/b] This point is in the [b]global[/b] coordinate system.
func get_collision_point() -> Vector2:
	pass;

#desc Returns whether any object is intersecting with the ray's vector (considering the vector length).
func is_colliding() -> bool:
	pass;

#desc Removes a collision exception so the ray does report collisions with the specified [CollisionObject2D] node.
func remove_exception() -> void:
	pass;

#desc Removes a collision exception so the ray does report collisions with the specified [RID].
func remove_exception_rid() -> void:
	pass;

#desc Based on [param value], enables or disables the specified layer in the [member collision_mask], given a [param layer_number] between 1 and 32.
func set_collision_mask_value(layer_number: int, value: bool) -> void:
	pass;


