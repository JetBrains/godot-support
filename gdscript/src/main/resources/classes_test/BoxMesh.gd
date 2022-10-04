#brief Generate an axis-aligned box [PrimitiveMesh].
#desc Generate an axis-aligned box [PrimitiveMesh].
#desc The box's UV layout is arranged in a 3×2 layout that allows texturing each face individually. To apply the same texture on all faces, change the material's UV property to [code]Vector3(3, 2, 1)[/code].
#desc [b]Note:[/b] When using a large textured [BoxMesh] (e.g. as a floor), you may stumble upon UV jittering issues depending on the camera angle. To solve this, increase [member subdivide_depth], [member subdivide_height] and [member subdivide_width] until you no longer notice UV jittering.
class_name BoxMesh


#desc The box's width, height and depth.
var size: Vector3;

#desc Number of extra edge loops inserted along the Z axis.
var subdivide_depth: int;

#desc Number of extra edge loops inserted along the Y axis.
var subdivide_height: int;

#desc Number of extra edge loops inserted along the X axis.
var subdivide_width: int;




