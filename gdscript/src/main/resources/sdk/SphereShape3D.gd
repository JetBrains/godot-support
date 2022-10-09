extends Shape3D
#brief Sphere shape resource for 3D collisions.
#desc 3D sphere shape to be added as a [i]direct[/i] child of a [PhysicsBody3D] or [Area3D] using a [CollisionShape3D] node. This shape is useful for modeling sphere-like 3D objects.
#desc [b]Performance:[/b] Being a primitive collision shape, [SphereShape3D] is the fastest collision shape to check collisions against, as it only requires a distance check with the shape's origin.
class_name SphereShape3D


#desc The sphere's radius. The shape's diameter is double the radius.
var radius: float;




