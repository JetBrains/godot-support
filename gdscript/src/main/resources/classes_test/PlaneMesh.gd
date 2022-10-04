#brief Class representing a planar [PrimitiveMesh].
#desc Class representing a planar [PrimitiveMesh]. This flat mesh does not have a thickness. By default, this mesh is aligned on the X and Z axes; this default rotation isn't suited for use with billboarded materials. For billboarded materials, change [member orientation] to [constant FACE_Z].
#desc [b]Note:[/b] When using a large textured [PlaneMesh] (e.g. as a floor), you may stumble upon UV jittering issues depending on the camera angle. To solve this, increase [member subdivide_depth] and [member subdivide_width] until you no longer notice UV jittering.
class_name PlaneMesh

#desc [PlaneMesh] will face the positive X-axis.
const FACE_X = 0;

#desc [PlaneMesh] will face the positive Y-axis. This matches the behavior of the [PlaneMesh] in Godot 3.x.
const FACE_Y = 1;

#desc [PlaneMesh] will face the positive Z-axis. This matches the behavior of the QuadMesh in Godot 3.x.
const FACE_Z = 2;


#desc Offset of the generated plane. Useful for particles.
var center_offset: Vector3;

#desc Direction that the [PlaneMesh] is facing. See [enum Orientation] for options.
var orientation: int;

#desc Size of the generated plane.
var size: Vector2;

#desc Number of subdivision along the Z axis.
var subdivide_depth: int;

#desc Number of subdivision along the X axis.
var subdivide_width: int;




