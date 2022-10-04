extends Shape3D
#brief Capsule shape resource for 3D collisions.
#desc 3D capsule shape to be added as a [i]direct[/i] child of a [PhysicsBody3D] or [Area3D] using a [CollisionShape3D] node. In 3D, a capsule is a cylinder shape with hemispheres at both ends.
#desc [b]Performance:[/b] Being a primitive collision shape, [CapsuleShape3D] is fast to check collisions against (though not as fast as [SphereShape3D]). [CapsuleShape3D] is cheaper to check collisions against compared to [CylinderShape3D].
class_name CapsuleShape3D


#desc The capsule's height.
var height: float;

#desc The capsule's radius.
var radius: float;




