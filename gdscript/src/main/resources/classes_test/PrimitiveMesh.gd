extends Mesh
#brief Base class for all primitive meshes. Handles applying a [Material] to a primitive mesh.
#desc Base class for all primitive meshes. Handles applying a [Material] to a primitive mesh. Examples include [BoxMesh], [CapsuleMesh], [CylinderMesh], [PlaneMesh], [PrismMesh], and [SphereMesh].
class_name PrimitiveMesh


#desc Overrides the [AABB] with one defined by user for use with frustum culling. Especially useful to avoid unexpected culling when using a shader to offset vertices.
var custom_aabb: AABB;

#desc If set, the order of the vertices in each triangle are reversed resulting in the backside of the mesh being drawn.
#desc This gives the same result as using [constant BaseMaterial3D.CULL_FRONT] in [member BaseMaterial3D.cull_mode].
var flip_faces: bool;

#desc The current [Material] of the primitive mesh.
var material: Material;



func _create_mesh_array() -> Array:
	pass;

#desc Returns mesh arrays used to constitute surface of [Mesh]. The result can be passed to [method ArrayMesh.add_surface_from_arrays] to create a new surface. For example:
#desc [codeblocks]
#desc [gdscript]
#desc var c = CylinderMesh.new()
#desc var arr_mesh = ArrayMesh.new()
#desc arr_mesh.add_surface_from_arrays(Mesh.PRIMITIVE_TRIANGLES, c.get_mesh_arrays())
#desc [/gdscript]
#desc [csharp]
#desc var c = new CylinderMesh();
#desc var arrMesh = new ArrayMesh();
#desc arrMesh.AddSurfaceFromArrays(Mesh.PrimitiveType.Triangles, c.GetMeshArrays());
#desc [/csharp]
#desc [/codeblocks]
func get_mesh_arrays() -> Array:
	pass;


