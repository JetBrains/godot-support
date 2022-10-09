extends RefCounted
#brief Parameters to be sent to a 3D ray physics query.
#desc This class contains the ray position and other parameters to be used for [method PhysicsDirectSpaceState3D.intersect_ray].
class_name PhysicsRayQueryParameters3D


#desc If [code]true[/code], the query will take [Area3D]s into account.
var collide_with_areas: bool;

#desc If [code]true[/code], the query will take [PhysicsBody3D]s into account.
var collide_with_bodies: bool;

#desc The physics layers the query will detect (as a bitmask). By default, all collision layers are detected. See [url=$DOCS_URL/tutorials/physics/physics_introduction.html#collision-layers-and-masks]Collision layers and masks[/url] in the documentation for more information.
var collision_mask: int;

#desc The list of objects or object [RID]s that will be excluded from collisions.
var exclude: Array;

#desc The starting point of the ray being queried for, in global coordinates.
var from: Vector3;

#desc If [code]true[/code], the query will hit back faces with concave polygon shapes with back face enabled or heightmap shapes.
var hit_back_faces: bool;

#desc If [code]true[/code], the query will detect a hit when starting inside shapes. In this case the collision normal will be [code]Vector3(0, 0, 0)[/code]. Does not affect concave polygon shapes or heightmap shapes.
var hit_from_inside: bool;

#desc The ending point of the ray being queried for, in global coordinates.
var to: Vector3;



#desc Returns a new, pre-configured [PhysicsRayQueryParameters3D] object. Use it to quickly create query parameters using the most common options.
#desc [codeblock]
#desc var query = PhysicsRayQueryParameters3D.create(position, position + Vector3(0, -10, 0))
#desc var collision = get_world_3d().direct_space_state.intersect_ray(query)
#desc [/codeblock]
static func create(from: Vector3, to: Vector3, collision_mask: int, exclude: Array) -> PhysicsRayQueryParameters3D:
	pass;


