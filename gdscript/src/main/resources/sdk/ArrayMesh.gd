extends Mesh
#brief [Mesh] type that provides utility for constructing a surface from arrays.
#desc The [ArrayMesh] is used to construct a [Mesh] by specifying the attributes as arrays.
#desc The most basic example is the creation of a single triangle:
#desc [codeblocks]
#desc [gdscript]
#desc var vertices = PackedVector3Array()
#desc vertices.push_back(Vector3(0, 1, 0))
#desc vertices.push_back(Vector3(1, 0, 0))
#desc vertices.push_back(Vector3(0, 0, 1))
#desc 
#desc # Initialize the ArrayMesh.
#desc var arr_mesh = ArrayMesh.new()
#desc var arrays = []
#desc arrays.resize(Mesh.ARRAY_MAX)
#desc arrays[Mesh.ARRAY_VERTEX] = vertices
#desc 
#desc # Create the Mesh.
#desc arr_mesh.add_surface_from_arrays(Mesh.PRIMITIVE_TRIANGLES, arrays)
#desc var m = MeshInstance3D.new()
#desc m.mesh = arr_mesh
#desc [/gdscript]
#desc [csharp]
#desc var vertices = new Godot.Collections.Array<Vector3>();
#desc vertices.Add(new Vector3(0, 1, 0));
#desc vertices.Add(new Vector3(1, 0, 0));
#desc vertices.Add(new Vector3(0, 0, 1));
#desc 
#desc // Initialize the ArrayMesh.
#desc var arrMesh = new ArrayMesh();
#desc var arrays = new Godot.Collections.Array();
#desc arrays.Resize((int)Mesh.ArrayType.Max);
#desc arrays[(int)Mesh.ArrayType.Vertex] = vertices;
#desc 
#desc // Create the Mesh.
#desc arrMesh.AddSurfaceFromArrays(Mesh.PrimitiveType.Triangles, arrays);
#desc var m = new MeshInstance();
#desc m.Mesh = arrMesh;
#desc [/csharp]
#desc [/codeblocks]
#desc The [MeshInstance3D] is ready to be added to the [SceneTree] to be shown.
#desc See also [ImmediateMesh], [MeshDataTool] and [SurfaceTool] for procedural geometry generation.
#desc [b]Note:[/b] Godot uses clockwise [url=https://learnopengl.com/Advanced-OpenGL/Face-culling]winding order[/url] for front faces of triangle primitive modes.
class_name ArrayMesh


#desc Sets the blend shape mode to one of [enum Mesh.BlendShapeMode].
var blend_shape_mode: int;

#desc Overrides the [AABB] with one defined by user for use with frustum culling. Especially useful to avoid unexpected culling when using a shader to offset vertices.
var custom_aabb: AABB;

var shadow_mesh: ArrayMesh;



#desc Adds name for a blend shape that will be added with [method add_surface_from_arrays]. Must be called before surface is added.
func add_blend_shape(name: StringName) -> void:
	pass;

#desc Creates a new surface.
#desc Surfaces are created to be rendered using a [param primitive], which may be any of the types defined in [enum Mesh.PrimitiveType]. (As a note, when using indices, it is recommended to only use points, lines, or triangles.) [method Mesh.get_surface_count] will become the [code]surf_idx[/code] for this new surface.
#desc The [param arrays] argument is an array of arrays. See [enum Mesh.ArrayType] for the values used in this array. For example, [code]arrays[0][/code] is the array of vertices. That first vertex sub-array is always required; the others are optional. Adding an index array puts this function into "index mode" where the vertex and other arrays become the sources of data and the index array defines the vertex order. All sub-arrays must have the same length as the vertex array (or be an exact multiple of the vertex array's length, when multiple elements of a sub-array correspond to a single vertex) or be empty, except for [constant Mesh.ARRAY_INDEX] if it is used.
func add_surface_from_arrays(primitive: int, arrays: Array, blend_shapes: Array[], lods: Dictionary, compress_flags: int) -> void:
	pass;

#desc Removes all blend shapes from this [ArrayMesh].
func clear_blend_shapes() -> void:
	pass;

#desc Removes all surfaces from this [ArrayMesh].
func clear_surfaces() -> void:
	pass;

#desc Returns the number of blend shapes that the [ArrayMesh] holds.
func get_blend_shape_count() -> int:
	pass;

#desc Returns the name of the blend shape at this index.
func get_blend_shape_name(index: int) -> StringName:
	pass;

#desc Will perform a UV unwrap on the [ArrayMesh] to prepare the mesh for lightmapping.
func lightmap_unwrap(transform: Transform3D, texel_size: float) -> int:
	pass;

#desc Will regenerate normal maps for the [ArrayMesh].
func regen_normal_maps() -> void:
	pass;

#desc Sets the name of the blend shape at this index.
func set_blend_shape_name(index: int, name: StringName) -> void:
	pass;

#desc Returns the index of the first surface with this name held within this [ArrayMesh]. If none are found, -1 is returned.
func surface_find_by_name(name: String) -> int:
	pass;

#desc Returns the length in indices of the index array in the requested surface (see [method add_surface_from_arrays]).
func surface_get_array_index_len(surf_idx: int) -> int:
	pass;

#desc Returns the length in vertices of the vertex array in the requested surface (see [method add_surface_from_arrays]).
func surface_get_array_len(surf_idx: int) -> int:
	pass;

#desc Returns the format mask of the requested surface (see [method add_surface_from_arrays]).
func surface_get_format(surf_idx: int) -> int:
	pass;

#desc Gets the name assigned to this surface.
func surface_get_name(surf_idx: int) -> String:
	pass;

#desc Returns the primitive type of the requested surface (see [method add_surface_from_arrays]).
func surface_get_primitive_type(surf_idx: int) -> int:
	pass;

#desc Sets a name for a given surface.
func surface_set_name(surf_idx: int, name: String) -> void:
	pass;

func surface_update_attribute_region(surf_idx: int, offset: int, data: PackedByteArray) -> void:
	pass;

func surface_update_skin_region(surf_idx: int, offset: int, data: PackedByteArray) -> void:
	pass;

func surface_update_vertex_region(surf_idx: int, offset: int, data: PackedByteArray) -> void:
	pass;


