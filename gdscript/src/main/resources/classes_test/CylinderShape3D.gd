extends Shape3D
#brief Cylinder shape for 3D collisions.
#desc Cylinder shape for collisions. Like [CapsuleShape3D], but without hemispheres at the cylinder's ends.
#desc [b]Performance:[/b] Being a primitive collision shape, [CylinderShape3D] is fast to check collisions against (though not as fast as [SphereShape3D]). [CylinderShape3D] is also more demanding compared to [CapsuleShape3D].
class_name CylinderShape3D


#desc The cylinder's height.
var height: float;

#desc The cylinder's radius.
var radius: float;




