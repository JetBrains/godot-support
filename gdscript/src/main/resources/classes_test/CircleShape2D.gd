extends Shape2D
#brief Circular shape resource for 2D physics.
#desc 2D circular shape to be added as a [i]direct[/i] child of a [PhysicsBody2D] or [Area2D] using a [CollisionShape2D] node. This shape is useful for modeling balls or small characters and its collision detection with everything else is very fast.
#desc [b]Performance:[/b] Being a primitive collision shape, [CircleShape2D] is the fastest collision shape to check collisions against, as it only requires a distance check with the shape's origin.
class_name CircleShape2D


#desc The circle's radius.
var radius: float;




