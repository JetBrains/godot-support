extends RefCounted
#brief Parameters to be sent to a 2D ray physics query.
#desc This class contains the ray position and other parameters to be used for [method PhysicsDirectSpaceState2D.intersect_ray].
class_name PhysicsRayQueryParameters2D


#desc If [code]true[/code], the query will take [Area2D]s into account.
var collide_with_areas: bool;

#desc If [code]true[/code], the query will take [PhysicsBody2D]s into account.
var collide_with_bodies: bool;

#desc The physics layers the query will detect (as a bitmask). By default, all collision layers are detected. See [url=$DOCS_URL/tutorials/physics/physics_introduction.html#collision-layers-and-masks]Collision layers and masks[/url] in the documentation for more information.
var collision_mask: int;

#desc The list of objects or object [RID]s that will be excluded from collisions.
var exclude: RID[];

#desc The starting point of the ray being queried for, in global coordinates.
var from: Vector2;

#desc If [code]true[/code], the query will detect a hit when starting inside shapes. In this case the collision normal will be [code]Vector2(0, 0)[/code]. Does not affect concave polygon shapes.
var hit_from_inside: bool;

#desc The ending point of the ray being queried for, in global coordinates.
var to: Vector2;



#desc Returns a new, pre-configured [PhysicsRayQueryParameters2D] object. Use it to quickly create query parameters using the most common options.
#desc [codeblock]
#desc var query = PhysicsRayQueryParameters2D.create(global_position, global_position + Vector2(0, 100))
#desc var collision = get_world_2d().direct_space_state.intersect_ray(query)
#desc [/codeblock]
static func create(from: Vector2, to: Vector2, collision_mask: int, exclude: RID[]) -> PhysicsRayQueryParameters2D:
	pass;


