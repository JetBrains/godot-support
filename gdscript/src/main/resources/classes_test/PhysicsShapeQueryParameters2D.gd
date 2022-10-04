extends RefCounted
#brief Parameters to be sent to a 2D shape physics query.
#desc This class contains the shape and other parameters for [PhysicsDirectSpaceState2D] intersection/collision queries.
class_name PhysicsShapeQueryParameters2D


#desc If [code]true[/code], the query will take [Area2D]s into account.
var collide_with_areas: bool;

#desc If [code]true[/code], the query will take [PhysicsBody2D]s into account.
var collide_with_bodies: bool;

#desc The physics layers the query will detect (as a bitmask). By default, all collision layers are detected. See [url=$DOCS_URL/tutorials/physics/physics_introduction.html#collision-layers-and-masks]Collision layers and masks[/url] in the documentation for more information.
var collision_mask: int;

#desc The list of objects or object [RID]s that will be excluded from collisions.
var exclude: RID[];

#desc The collision margin for the shape.
var margin: float;

#desc The motion of the shape being queried for.
var motion: Vector2;

#desc The [Shape2D] that will be used for collision/intersection queries. This stores the actual reference which avoids the shape to be released while being used for queries, so always prefer using this over [member shape_rid].
var shape: Resource;

#desc The queried shape's [RID] that will be used for collision/intersection queries. Use this over [member shape] if you want to optimize for performance using the Servers API:
#desc [codeblocks]
#desc [gdscript]
#desc var shape_rid = PhysicsServer2D.circle_shape_create()
#desc var radius = 64
#desc PhysicsServer2D.shape_set_data(shape_rid, radius)
#desc 
#desc var params = PhysicsShapeQueryParameters2D.new()
#desc params.shape_rid = shape_rid
#desc 
#desc # Execute physics queries here...
#desc 
#desc # Release the shape when done with physics queries.
#desc PhysicsServer2D.free_rid(shape_rid)
#desc [/gdscript]
#desc [csharp]
#desc RID shapeRid = PhysicsServer2D.CircleShapeCreate();
#desc int radius = 64;
#desc PhysicsServer2D.ShapeSetData(shapeRid, radius);
#desc 
#desc var params = new PhysicsShapeQueryParameters2D();
#desc params.ShapeRid = shapeRid;
#desc 
#desc // Execute physics queries here...
#desc 
#desc // Release the shape when done with physics queries.
#desc PhysicsServer2D.FreeRid(shapeRid);
#desc [/csharp]
#desc [/codeblocks]
var shape_rid: RID;

#desc The queried shape's transform matrix.
var transform: Transform2D;




