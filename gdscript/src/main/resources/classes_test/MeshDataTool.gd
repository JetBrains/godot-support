#brief Helper tool to access and edit [Mesh] data.
#desc MeshDataTool provides access to individual vertices in a [Mesh]. It allows users to read and edit vertex data of meshes. It also creates an array of faces and edges.
#desc To use MeshDataTool, load a mesh with [method create_from_surface]. When you are finished editing the data commit the data to a mesh with [method commit_to_surface].
#desc Below is an example of how MeshDataTool may be used.
#desc [codeblocks]
#desc [gdscript]
#desc var mesh = ArrayMesh.new()
#desc mesh.add_surface_from_arrays(Mesh.PRIMITIVE_TRIANGLES, BoxMesh.new().get_mesh_arrays())
#desc var mdt = MeshDataTool.new()
#desc mdt.create_from_surface(mesh, 0)
#desc for i in range(mdt.get_vertex_count()):
#desc var vertex = mdt.get_vertex(i)
#desc # In this example we extend the mesh by one unit, which results in separated faces as it is flat shaded.
#desc vertex += mdt.get_vertex_normal(i)
#desc # Save your change.
#desc mdt.set_vertex(i, vertex)
#desc mesh.surface_remove(0)
#desc mdt.commit_to_surface(mesh)
#desc var mi = MeshInstance.new()
#desc mi.mesh = mesh
#desc add_child(mi)
#desc [/gdscript]
#desc [csharp]
#desc var mesh = new ArrayMesh();
#desc mesh.AddSurfaceFromArrays(Mesh.PrimitiveType.Triangles, new BoxMesh().GetMeshArrays());
#desc var mdt = new MeshDataTool();
#desc mdt.CreateFromSurface(mesh, 0);
#desc for (var i = 0; i < mdt.GetVertexCount(); i++)
#desc {
#desc Vector3 vertex = mdt.GetVertex(i);
#desc // In this example we extend the mesh by one unit, which results in separated faces as it is flat shaded.
#desc vertex += mdt.GetVertexNormal(i);
#desc // Save your change.
#desc mdt.SetVertex(i, vertex);
#desc }
#desc mesh.SurfaceRemove(0);
#desc mdt.CommitToSurface(mesh);
#desc var mi = new MeshInstance();
#desc mi.Mesh = mesh;
#desc AddChild(mi);
#desc [/csharp]
#desc [/codeblocks]
#desc See also [ArrayMesh], [ImmediateMesh] and [SurfaceTool] for procedural geometry generation.
#desc [b]Note:[/b] Godot uses clockwise [url=https://learnopengl.com/Advanced-OpenGL/Face-culling]winding order[/url] for front faces of triangle primitive modes.
class_name MeshDataTool




#desc Clears all data currently in MeshDataTool.
func clear() -> void:
	pass;

#desc Adds a new surface to specified [Mesh] with edited data.
func commit_to_surface(mesh: ArrayMesh) -> int:
	pass;

#desc Uses specified surface of given [Mesh] to populate data for MeshDataTool.
#desc Requires [Mesh] with primitive type [constant Mesh.PRIMITIVE_TRIANGLES].
func create_from_surface(mesh: ArrayMesh, surface: int) -> int:
	pass;

#desc Returns the number of edges in this [Mesh].
func get_edge_count() -> int:
	pass;

#desc Returns array of faces that touch given edge.
func get_edge_faces(idx: int) -> PackedInt32Array:
	pass;

#desc Returns meta information assigned to given edge.
func get_edge_meta(idx: int) -> Variant:
	pass;

#desc Returns index of specified vertex connected to given edge.
#desc Vertex argument can only be 0 or 1 because edges are comprised of two vertices.
func get_edge_vertex(idx: int, vertex: int) -> int:
	pass;

#desc Returns the number of faces in this [Mesh].
func get_face_count() -> int:
	pass;

#desc Returns specified edge associated with given face.
#desc Edge argument must be either 0, 1, or 2 because a face only has three edges.
func get_face_edge(idx: int, edge: int) -> int:
	pass;

#desc Returns the metadata associated with the given face.
func get_face_meta(idx: int) -> Variant:
	pass;

#desc Calculates and returns the face normal of the given face.
func get_face_normal(idx: int) -> Vector3:
	pass;

#desc Returns the specified vertex of the given face.
#desc Vertex argument must be either 0, 1, or 2 because faces contain three vertices.
func get_face_vertex(idx: int, vertex: int) -> int:
	pass;

#desc Returns the [Mesh]'s format. Format is an integer made up of [Mesh] format flags combined together. For example, a mesh containing both vertices and normals would return a format of [code]3[/code] because [constant Mesh.ARRAY_FORMAT_VERTEX] is [code]1[/code] and [constant Mesh.ARRAY_FORMAT_NORMAL] is [code]2[/code].
#desc See [enum Mesh.ArrayFormat] for a list of format flags.
func get_format() -> int:
	pass;

#desc Returns the material assigned to the [Mesh].
func get_material() -> Material:
	pass;

#desc Returns the vertex at given index.
func get_vertex(idx: int) -> Vector3:
	pass;

#desc Returns the bones of the given vertex.
func get_vertex_bones(idx: int) -> PackedInt32Array:
	pass;

#desc Returns the color of the given vertex.
func get_vertex_color(idx: int) -> Color:
	pass;

#desc Returns the total number of vertices in [Mesh].
func get_vertex_count() -> int:
	pass;

#desc Returns an array of edges that share the given vertex.
func get_vertex_edges(idx: int) -> PackedInt32Array:
	pass;

#desc Returns an array of faces that share the given vertex.
func get_vertex_faces(idx: int) -> PackedInt32Array:
	pass;

#desc Returns the metadata associated with the given vertex.
func get_vertex_meta(idx: int) -> Variant:
	pass;

#desc Returns the normal of the given vertex.
func get_vertex_normal(idx: int) -> Vector3:
	pass;

#desc Returns the tangent of the given vertex.
func get_vertex_tangent(idx: int) -> Plane:
	pass;

#desc Returns the UV of the given vertex.
func get_vertex_uv(idx: int) -> Vector2:
	pass;

#desc Returns the UV2 of the given vertex.
func get_vertex_uv2(idx: int) -> Vector2:
	pass;

#desc Returns bone weights of the given vertex.
func get_vertex_weights(idx: int) -> PackedFloat32Array:
	pass;

#desc Sets the metadata of the given edge.
func set_edge_meta(idx: int, meta: Variant) -> void:
	pass;

#desc Sets the metadata of the given face.
func set_face_meta(idx: int, meta: Variant) -> void:
	pass;

#desc Sets the material to be used by newly-constructed [Mesh].
func set_material(material: Material) -> void:
	pass;

#desc Sets the position of the given vertex.
func set_vertex(idx: int, vertex: Vector3) -> void:
	pass;

#desc Sets the bones of the given vertex.
func set_vertex_bones(idx: int, bones: PackedInt32Array) -> void:
	pass;

#desc Sets the color of the given vertex.
func set_vertex_color(idx: int, color: Color) -> void:
	pass;

#desc Sets the metadata associated with the given vertex.
func set_vertex_meta(idx: int, meta: Variant) -> void:
	pass;

#desc Sets the normal of the given vertex.
func set_vertex_normal(idx: int, normal: Vector3) -> void:
	pass;

#desc Sets the tangent of the given vertex.
func set_vertex_tangent(idx: int, tangent: Plane) -> void:
	pass;

#desc Sets the UV of the given vertex.
func set_vertex_uv(idx: int, uv: Vector2) -> void:
	pass;

#desc Sets the UV2 of the given vertex.
func set_vertex_uv2(idx: int, uv2: Vector2) -> void:
	pass;

#desc Sets the bone weights of the given vertex.
func set_vertex_weights(idx: int, weights: PackedFloat32Array) -> void:
	pass;


