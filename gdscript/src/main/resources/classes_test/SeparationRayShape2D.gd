extends Shape2D
#brief Separation ray shape resource for 2D physics.
#desc 2D separation ray shape to be added as a [i]direct[/i] child of a [PhysicsBody2D] or [Area2D] using a [CollisionShape2D] node. A ray is not really a collision body; instead, it tries to separate itself from whatever is touching its far endpoint. It's often useful for characters.
#desc [b]Performance:[/b] Being a primitive collision shape, [SeparationRayShape2D] is fast to check collisions against.
class_name SeparationRayShape2D


#desc The ray's length.
var length: float;

#desc If [code]false[/code] (default), the shape always separates and returns a normal along its own direction.
#desc If [code]true[/code], the shape can return the correct normal and separate in any direction, allowing sliding motion on slopes.
var slide_on_slope: bool;




