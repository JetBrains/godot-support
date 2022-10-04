#brief Capsule shape resource for 2D physics.
#desc 2D capsule shape to be added as a [i]direct[/i] child of a [PhysicsBody2D] or [Area2D] using a [CollisionShape2D] node. In 2D, a capsule is a rectangle shape with half-circles at both ends.
#desc [b]Performance:[/b] Being a primitive collision shape, [CapsuleShape2D] is fast to check collisions against (though not as fast as [CircleShape2D]).
class_name CapsuleShape2D


#desc The capsule's height.
var height: float;

#desc The capsule's radius.
var radius: float;




