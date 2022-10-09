extends RefCounted
#brief Parameters to be sent to a 3D point physics query.
#desc This class contains the position and other parameters to be used for [method PhysicsDirectSpaceState3D.intersect_point].
class_name PhysicsPointQueryParameters3D


#desc If [code]true[/code], the query will take [Area3D]s into account.
var collide_with_areas: bool;

#desc If [code]true[/code], the query will take [PhysicsBody3D]s into account.
var collide_with_bodies: bool;

#desc The physics layers the query will detect (as a bitmask). By default, all collision layers are detected. See [url=$DOCS_URL/tutorials/physics/physics_introduction.html#collision-layers-and-masks]Collision layers and masks[/url] in the documentation for more information.
var collision_mask: int;

#desc The list of objects or object [RID]s that will be excluded from collisions.
var exclude: Array;

#desc The position being queried for, in global coordinates.
var position: Vector3;




