extends Shape3D
#brief Separation ray shape resource for 3D physics.
#desc 3D separation ray shape to be added as a [i]direct[/i] child of a [PhysicsBody3D] or [Area3D] using a [CollisionShape3D] node. A ray is not really a collision body; instead, it tries to separate itself from whatever is touching its far endpoint. It's often useful for characters.
#desc [b]Performance:[/b] Being a primitive collision shape, [SeparationRayShape3D] is fast to check collisions against.
class_name SeparationRayShape3D


#desc The ray's length.
var length: float;

#desc If [code]false[/code] (default), the shape always separates and returns a normal along its own direction.
#desc If [code]true[/code], the shape can return the correct normal and separate in any direction, allowing sliding motion on slopes.
var slide_on_slope: bool;




