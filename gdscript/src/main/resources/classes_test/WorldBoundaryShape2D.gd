#brief World boundary (infinite plane) shape resource for 2D physics.
#desc 2D world boundary shape to be added as a [i]direct[/i] child of a [PhysicsBody2D] or [Area2D] using a [CollisionShape2D] node. [WorldBoundaryShape2D] works like an infinite plane and will not allow any physics body to go to the negative side. Note that the [member normal] matters; anything "below" the plane will collide with it. If the [WorldBoundaryShape2D] is used in a [PhysicsBody2D], it will cause colliding objects placed "below" it to teleport "above" the plane.
#desc [b]Performance:[/b] Being a primitive collision shape, [WorldBoundaryShape2D] is fast to check collisions against.
class_name WorldBoundaryShape2D


#desc The line's distance from the origin.
var distance: float;

#desc The line's normal. Defaults to [code]Vector2.UP[/code].
var normal: Vector2;




