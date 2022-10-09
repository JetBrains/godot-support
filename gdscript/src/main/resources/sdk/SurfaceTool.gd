extends RefCounted
#brief Helper tool to create geometry.
#desc The [SurfaceTool] is used to construct a [Mesh] by specifying vertex attributes individually. It can be used to construct a [Mesh] from a script. All properties except indices need to be added before calling [method add_vertex]. For example, to add vertex colors and UVs:
#desc [codeblocks]
#desc [gdscript]
#desc var st = SurfaceTool.new()
#desc st.begin(Mesh.PRIMITIVE_TRIANGLES)
#desc st.set_color(Color(1, 0, 0))
#desc st.set_uv(Vector2(0, 0))
#desc st.add_vertex(Vector3(0, 0, 0))
#desc [/gdscript]
#desc [csharp]
#desc var st = new SurfaceTool();
#desc st.Begin(Mesh.PrimitiveType.Triangles);
#desc st.SetColor(new Color(1, 0, 0));
#desc st.SetUv(new Vector2(0, 0));
#desc st.AddVertex(new Vector3(0, 0, 0));
#desc [/csharp]
#desc [/codeblocks]
#desc The above [SurfaceTool] now contains one vertex of a triangle which has a UV coordinate and a specified [Color]. If another vertex were added without calling [method set_uv] or [method set_color], then the last values would be used.
#desc Vertex attributes must be passed [b]before[/b] calling [method add_vertex]. Failure to do so will result in an error when committing the vertex information to a mesh.
#desc Additionally, the attributes used before the first vertex is added determine the format of the mesh. For example, if you only add UVs to the first vertex, you cannot add color to any of the subsequent vertices.
#desc See also [ArrayMesh], [ImmediateMesh] and [MeshDataTool] for procedural geometry generation.
#desc [b]Note:[/b] Godot uses clockwise [url=https://learnopengl.com/Advanced-OpenGL/Face-culling]winding order[/url] for front faces of triangle primitive modes.
class_name SurfaceTool

#desc Limits range of data passed to `set_custom` to unsigned normalized 0 to 1 stored in 8 bits per channel. See [constant Mesh.ARRAY_CUSTOM_RGBA8_UNORM].
const CUSTOM_RGBA8_UNORM = 0;

#desc Limits range of data passed to `set_custom` to signed normalized -1 to 1 stored in 8 bits per channel. See [constant Mesh.ARRAY_CUSTOM_RGBA8_SNORM].
const CUSTOM_RGBA8_SNORM = 1;

#desc Stores data passed to `set_custom` as half precision floats, and uses only red and green color channels. See [constant Mesh.ARRAY_CUSTOM_RG_HALF].
const CUSTOM_RG_HALF = 2;

#desc Stores data passed to `set_custom` as half precision floats and uses all color channels. See [constant Mesh.ARRAY_CUSTOM_RGBA_HALF].
const CUSTOM_RGBA_HALF = 3;

#desc Stores data passed to `set_custom` as full precision floats, and uses only red color channel. See [constant Mesh.ARRAY_CUSTOM_R_FLOAT].
const CUSTOM_R_FLOAT = 4;

#desc Stores data passed to `set_custom` as full precision floats, and uses only red and green color channels. See [constant Mesh.ARRAY_CUSTOM_RG_FLOAT].
const CUSTOM_RG_FLOAT = 5;

#desc Stores data passed to `set_custom` as full precision floats, and uses only red, green and blue color channels. See [constant Mesh.ARRAY_CUSTOM_RGB_FLOAT].
const CUSTOM_RGB_FLOAT = 6;

#desc Stores data passed to `set_custom` as full precision floats, and uses all color channels. See [constant Mesh.ARRAY_CUSTOM_RGBA_FLOAT].
const CUSTOM_RGBA_FLOAT = 7;

#desc Used to indicate a disabled custom channel.
const CUSTOM_MAX = 8;

#desc Each individual vertex can be influenced by only 4 bone weights.
const SKIN_4_WEIGHTS = 0;

#desc Each individual vertex can be influenced by up to 8 bone weights.
const SKIN_8_WEIGHTS = 1;




#desc Adds a vertex to index array if you are using indexed vertices. Does not need to be called before adding vertices.
func add_index(index: int) -> void:
	pass;

#desc Inserts a triangle fan made of array data into [Mesh] being constructed.
#desc Requires the primitive type be set to [constant Mesh.PRIMITIVE_TRIANGLES].
func add_triangle_fan(vertices: PackedVector3Array, uvs: PackedVector2Array, colors: PackedColorArray, uv2s: PackedVector2Array, normals: PackedVector3Array, tangents: Array) -> void:
	pass;

#desc Specifies the position of current vertex. Should be called after specifying other vertex properties (e.g. Color, UV).
func add_vertex(vertex: Vector3) -> void:
	pass;

#desc Append vertices from a given [Mesh] surface onto the current vertex array with specified [Transform3D].
func append_from(existing: Mesh, surface: int, transform: Transform3D) -> void:
	pass;

#desc Called before adding any vertices. Takes the primitive type as an argument (e.g. [constant Mesh.PRIMITIVE_TRIANGLES]).
func begin(primitive: int) -> void:
	pass;

#desc Clear all information passed into the surface tool so far.
func clear() -> void:
	pass;

#desc Returns a constructed [ArrayMesh] from current information passed in. If an existing [ArrayMesh] is passed in as an argument, will add an extra surface to the existing [ArrayMesh].
#desc [b]FIXME:[/b] Document possible values for [param flags], it changed in 4.0. Likely some combinations of [enum Mesh.ArrayFormat].
func commit(existing: ArrayMesh, flags: int) -> ArrayMesh:
	pass;

#desc Commits the data to the same format used by [method ArrayMesh.add_surface_from_arrays]. This way you can further process the mesh data using the [ArrayMesh] API.
func commit_to_arrays() -> Array:
	pass;

#desc Creates a vertex array from an existing [Mesh].
func create_from(existing: Mesh, surface: int) -> void:
	pass;

#desc Creates a vertex array from the specified blend shape of an existing [Mesh]. This can be used to extract a specific pose from a blend shape.
func create_from_blend_shape(existing: Mesh, surface: int, blend_shape: String) -> void:
	pass;

#desc Removes the index array by expanding the vertex array.
func deindex() -> void:
	pass;

#desc Generates a LOD for a given [param nd_threshold] in linear units (square root of quadric error metric), using at most [param target_index_count] indices.
#desc Deprecated. Unused internally and neglects to preserve normals or UVs. Consider using [method ImporterMesh.generate_lods] instead.
func generate_lod(nd_threshold: float, target_index_count: int) -> PackedInt32Array:
	pass;

#desc Generates normals from vertices so you do not have to do it manually. If [param flip] is [code]true[/code], the resulting normals will be inverted. [method generate_normals] should be called [i]after[/i] generating geometry and [i]before[/i] committing the mesh using [method commit] or [method commit_to_arrays]. For correct display of normal-mapped surfaces, you will also have to generate tangents using [method generate_tangents].
#desc [b]Note:[/b] [method generate_normals] only works if the primitive type to be set to [constant Mesh.PRIMITIVE_TRIANGLES].
func generate_normals(flip: bool) -> void:
	pass;

#desc Generates a tangent vector for each vertex. Requires that each vertex have UVs and normals set already (see [method generate_normals]).
func generate_tangents() -> void:
	pass;

#desc Returns the axis-aligned bounding box of the vertex positions.
func get_aabb() -> AABB:
	pass;

#desc Returns the format for custom [param channel_index] (currently up to 4). Returns [constant CUSTOM_MAX] if this custom channel is unused.
func get_custom_format(channel_index: int) -> int:
	pass;

#desc Returns the type of mesh geometry, such as [constant Mesh.PRIMITIVE_TRIANGLES].
func get_primitive_type() -> int:
	pass;

#desc By default, returns [constant SKIN_4_WEIGHTS] to indicate only 4 bone influences per vertex are used.
#desc Returns [constant SKIN_8_WEIGHTS] if up to 8 influences are used.
#desc [b]Note:[/b] This function returns an enum, not the exact number of weights.
func get_skin_weight_count() -> int:
	pass;

#desc Shrinks the vertex array by creating an index array. This can improve performance by avoiding vertex reuse.
func index() -> void:
	pass;

#desc Optimizes triangle sorting for performance. Requires that [method get_primitive_type] is [constant Mesh.PRIMITIVE_TRIANGLES].
func optimize_indices_for_cache() -> void:
	pass;

#desc Specifies an array of bones to use for the [i]next[/i] vertex. [param bones] must contain 4 integers.
func set_bones(bones: PackedInt32Array) -> void:
	pass;

#desc Specifies a [Color] to use for the [i]next[/i] vertex. If every vertex needs to have this information set and you fail to submit it for the first vertex, this information may not be used at all.
#desc [b]Note:[/b] The material must have [member BaseMaterial3D.vertex_color_use_as_albedo] enabled for the vertex color to be visible.
func set_color(color: Color) -> void:
	pass;

#desc Sets the custom value on this vertex for [param channel_index].
#desc [method set_custom_format] must be called first for this [param channel_index]. Formats which are not RGBA will ignore other color channels.
func set_custom(channel_index: int, custom_color: Color) -> void:
	pass;

#desc Sets the color format for this custom [param channel_index]. Use [constant CUSTOM_MAX] to disable.
#desc Must be invoked after [method begin] and should be set before [method commit] or [method commit_to_arrays].
func set_custom_format(channel_index: int, format: int) -> void:
	pass;

#desc Sets [Material] to be used by the [Mesh] you are constructing.
func set_material(material: Material) -> void:
	pass;

#desc Specifies a normal to use for the [i]next[/i] vertex. If every vertex needs to have this information set and you fail to submit it for the first vertex, this information may not be used at all.
func set_normal(normal: Vector3) -> void:
	pass;

#desc Set to [constant SKIN_8_WEIGHTS] to indicate that up to 8 bone influences per vertex may be used.
#desc By default, only 4 bone influences are used ([constant SKIN_4_WEIGHTS])
#desc [b]Note:[/b] This function takes an enum, not the exact number of weights.
func set_skin_weight_count(count: int) -> void:
	pass;

#desc Specifies whether the current vertex (if using only vertex arrays) or current index (if also using index arrays) should use smooth normals for normal calculation.
func set_smooth_group(index: int) -> void:
	pass;

#desc Specifies a tangent to use for the [i]next[/i] vertex. If every vertex needs to have this information set and you fail to submit it for the first vertex, this information may not be used at all.
func set_tangent(tangent: Plane) -> void:
	pass;

#desc Specifies a set of UV coordinates to use for the [i]next[/i] vertex. If every vertex needs to have this information set and you fail to submit it for the first vertex, this information may not be used at all.
func set_uv(uv: Vector2) -> void:
	pass;

#desc Specifies an optional second set of UV coordinates to use for the [i]next[/i] vertex. If every vertex needs to have this information set and you fail to submit it for the first vertex, this information may not be used at all.
func set_uv2(uv2: Vector2) -> void:
	pass;

#desc Specifies weight values to use for the [i]next[/i] vertex. [param weights] must contain 4 values. If every vertex needs to have this information set and you fail to submit it for the first vertex, this information may not be used at all.
func set_weights(weights: PackedFloat32Array) -> void:
	pass;


