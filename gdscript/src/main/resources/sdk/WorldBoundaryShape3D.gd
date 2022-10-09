extends Shape3D
#brief World boundary (infinite plane) shape resource for 3D physics.
#desc 3D world boundary shape to be added as a [i]direct[/i] child of a [PhysicsBody3D] or [Area3D] using a [CollisionShape3D] node. [WorldBoundaryShape3D] works like an infinite plane and will not allow any physics body to go to the negative side. Note that the [Plane]'s normal matters; anything "below" the plane will collide with it. If the [WorldBoundaryShape3D] is used in a [PhysicsBody3D], it will cause colliding objects placed "below" it to teleport "above" the plane.
#desc [b]Performance:[/b] Being a primitive collision shape, [WorldBoundaryShape3D] is fast to check collisions against.
class_name WorldBoundaryShape3D


#desc The [Plane] used by the [WorldBoundaryShape3D] for collision.
var plane: Plane;




