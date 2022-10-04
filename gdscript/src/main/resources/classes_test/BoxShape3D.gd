extends Shape3D
#brief Box shape resource for 3D collisions.
#desc 3D box shape to be added as a [i]direct[/i] child of a [PhysicsBody3D] or [Area3D] using a [CollisionShape3D] node.
#desc [b]Performance:[/b] Being a primitive collision shape, [BoxShape3D] is fast to check collisions against (though not as fast as [SphereShape3D]).
class_name BoxShape3D


#desc The box's width, height and depth.
var size: Vector3;




