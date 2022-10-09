extends Node3D
#brief Node that represents collision shape data in 3D space.
#desc Editor facility for creating and editing collision shapes in 3D space. Set the [member shape] property to configure the shape. [b]IMPORTANT[/b]: this is an Editor-only helper to create shapes, use [method CollisionObject3D.shape_owner_get_shape] to get the actual shape.
#desc You can use this node to represent all sorts of collision shapes, for example, add this to an [Area3D] to give it a detection shape, or add it to a [PhysicsBody3D] to create a solid object.
class_name CollisionShape3D


#desc A disabled collision shape has no effect in the world.
var disabled: bool;

#desc The actual shape owned by this collision shape.
var shape: Shape3D;



#desc Sets the collision shape's shape to the addition of all its convexed [MeshInstance3D] siblings geometry.
func make_convex_from_siblings() -> void:
	pass;

#desc If this method exists within a script it will be called whenever the shape resource has been modified.
func resource_changed(resource: Resource) -> void:
	pass;


