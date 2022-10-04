#brief Node that represents collision shape data in 2D space.
#desc Editor facility for creating and editing collision shapes in 2D space. Set the [member shape] property to configure the shape. [b]IMPORTANT[/b]: this is an Editor-only helper to create shapes, use [method CollisionObject2D.shape_owner_get_shape] to get the actual shape.
#desc You can use this node to represent all sorts of collision shapes, for example, add this to an [Area2D] to give it a detection shape, or add it to a [PhysicsBody2D] to create a solid object.
class_name CollisionShape2D


#desc The collision shape debug color.
#desc [b]Note:[/b] The default value is [member ProjectSettings.debug/shapes/collision/shape_color]. The [code]Color(0, 0, 0, 1)[/code] value documented here is a placeholder, and not the actual default debug color.
var debug_color: Color;

#desc A disabled collision shape has no effect in the world. This property should be changed with [method Object.set_deferred].
var disabled: bool;

#desc Sets whether this collision shape should only detect collision on one side (top or bottom).
#desc [b]Note:[/b] This property has no effect if this [CollisionShape2D] is a child of an [Area2D] node.
var one_way_collision: bool;

#desc The margin used for one-way collision (in pixels). Higher values will make the shape thicker, and work better for colliders that enter the shape at a high velocity.
var one_way_collision_margin: float;

#desc The actual shape owned by this collision shape.
var shape: Shape2D;




