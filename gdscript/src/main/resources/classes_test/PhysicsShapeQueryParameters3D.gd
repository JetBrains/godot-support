#brief Parameters to be sent to a 3D shape physics query.
#desc This class contains the shape and other parameters for [PhysicsDirectSpaceState3D] intersection/collision queries.
class_name PhysicsShapeQueryParameters3D


#desc If [code]true[/code], the query will take [Area3D]s into account.
var collide_with_areas: bool;

#desc If [code]true[/code], the query will take [PhysicsBody3D]s into account.
var collide_with_bodies: bool;

#desc The physics layers the query will detect (as a bitmask). By default, all collision layers are detected. See [url=$DOCS_URL/tutorials/physics/physics_introduction.html#collision-layers-and-masks]Collision layers and masks[/url] in the documentation for more information.
var collision_mask: int;

#desc The list of objects or object [RID]s that will be excluded from collisions.
var exclude: Array;

#desc The collision margin for the shape.
var margin: float;

#desc The motion of the shape being queried for.
var motion: Vector3;

#desc The [Shape3D] that will be used for collision/intersection queries. This stores the actual reference which avoids the shape to be released while being used for queries, so always prefer using this over [member shape_rid].
var shape: Resource;

#desc The queried shape's [RID] that will be used for collision/intersection queries. Use this over [member shape] if you want to optimize for performance using the Servers API:
#desc [codeblocks]
#desc [gdscript]
#desc var shape_rid = PhysicsServer3D.shape_create(PhysicsServer3D.SHAPE_SPHERE)
#desc var radius = 2.0
#desc PhysicsServer3D.shape_set_data(shape_rid, radius)
#desc 
#desc var params = PhysicsShapeQueryParameters3D.new()
#desc params.shape_rid = shape_rid
#desc 
#desc # Execute physics queries here...
#desc 
#desc # Release the shape when done with physics queries.
#desc PhysicsServer3D.free_rid(shape_rid)
#desc [/gdscript]
#desc [csharp]
#desc RID shapeRid = PhysicsServer3D.ShapeCreate(PhysicsServer3D.ShapeType.Sphere);
#desc float radius = 2.0f;
#desc PhysicsServer3D.ShapeSetData(shapeRid, radius);
#desc 
#desc var params = new PhysicsShapeQueryParameters3D();
#desc params.ShapeRid = shapeRid;
#desc 
#desc // Execute physics queries here...
#desc 
#desc // Release the shape when done with physics queries.
#desc PhysicsServer3D.FreeRid(shapeRid);
#desc [/csharp]
#desc [/codeblocks]
var shape_rid: RID;

#desc The queried shape's transform matrix.
var transform: Transform3D;




