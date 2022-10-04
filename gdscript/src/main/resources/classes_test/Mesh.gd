extends Resource
#brief A [Resource] that contains vertex array-based geometry.
#desc Mesh is a type of [Resource] that contains vertex array-based geometry, divided in [i]surfaces[/i]. Each surface contains a completely separate array and a material used to draw it. Design wise, a mesh with multiple surfaces is preferred to a single surface, because objects created in 3D editing software commonly contain multiple materials.
class_name Mesh

#desc Render array as points (one vertex equals one point).
const PRIMITIVE_POINTS = 0;

#desc Render array as lines (every two vertices a line is created).
const PRIMITIVE_LINES = 1;

#desc Render array as line strip.
const PRIMITIVE_LINE_STRIP = 2;

#desc Render array as triangles (every three vertices a triangle is created).
const PRIMITIVE_TRIANGLES = 3;

#desc Render array as triangle strips.
const PRIMITIVE_TRIANGLE_STRIP = 4;

#desc [PackedVector3Array], [PackedVector2Array], or [Array] of vertex positions.
const ARRAY_VERTEX = 0;

#desc [PackedVector3Array] of vertex normals.
const ARRAY_NORMAL = 1;

#desc [PackedFloat32Array] of vertex tangents. Each element in groups of 4 floats, first 3 floats determine the tangent, and the last the binormal direction as -1 or 1.
const ARRAY_TANGENT = 2;

#desc [PackedColorArray] of vertex colors.
const ARRAY_COLOR = 3;

#desc [PackedVector2Array] for UV coordinates.
const ARRAY_TEX_UV = 4;

#desc [PackedVector2Array] for second UV coordinates.
const ARRAY_TEX_UV2 = 5;

#desc Contains custom color channel 0. [PackedByteArray] if [code](format >> [constant ARRAY_FORMAT_CUSTOM0_SHIFT]) & [constant ARRAY_FORMAT_CUSTOM_MASK])[/code] is [constant ARRAY_CUSTOM_RGBA8_UNORM], [constant ARRAY_CUSTOM_RGBA8_UNORM], [constant ARRAY_CUSTOM_RG_HALF] or [constant ARRAY_CUSTOM_RGBA_HALF]. [PackedFloat32Array] otherwise.
const ARRAY_CUSTOM0 = 6;

#desc Contains custom color channel 1. [PackedByteArray] if [code](format >> [constant ARRAY_FORMAT_CUSTOM1_SHIFT]) & [constant ARRAY_FORMAT_CUSTOM_MASK])[/code] is [constant ARRAY_CUSTOM_RGBA8_UNORM], [constant ARRAY_CUSTOM_RGBA8_UNORM], [constant ARRAY_CUSTOM_RG_HALF] or [constant ARRAY_CUSTOM_RGBA_HALF]. [PackedFloat32Array] otherwise.
const ARRAY_CUSTOM1 = 7;

#desc Contains custom color channel 2. [PackedByteArray] if [code](format >> [constant ARRAY_FORMAT_CUSTOM2_SHIFT]) & [constant ARRAY_FORMAT_CUSTOM_MASK])[/code] is [constant ARRAY_CUSTOM_RGBA8_UNORM], [constant ARRAY_CUSTOM_RGBA8_UNORM], [constant ARRAY_CUSTOM_RG_HALF] or [constant ARRAY_CUSTOM_RGBA_HALF]. [PackedFloat32Array] otherwise.
const ARRAY_CUSTOM2 = 8;

#desc Contains custom color channel 3. [PackedByteArray] if [code](format >> [constant ARRAY_FORMAT_CUSTOM3_SHIFT]) & [constant ARRAY_FORMAT_CUSTOM_MASK])[/code] is [constant ARRAY_CUSTOM_RGBA8_UNORM], [constant ARRAY_CUSTOM_RGBA8_UNORM], [constant ARRAY_CUSTOM_RG_HALF] or [constant ARRAY_CUSTOM_RGBA_HALF]. [PackedFloat32Array] otherwise.
const ARRAY_CUSTOM3 = 9;

#desc [PackedFloat32Array] or [PackedInt32Array] of bone indices. Each element is a group of 4 numbers.
const ARRAY_BONES = 10;

#desc [PackedFloat32Array] of bone weights. Each element in groups of 4 floats.
const ARRAY_WEIGHTS = 11;

#desc [PackedInt32Array] of integers used as indices referencing vertices, colors, normals, tangents, and textures. All of those arrays must have the same number of elements as the vertex array. No index can be beyond the vertex array size. When this index array is present, it puts the function into "index mode," where the index selects the *i*'th vertex, normal, tangent, color, UV, etc. This means if you want to have different normals or colors along an edge, you have to duplicate the vertices.
#desc For triangles, the index array is interpreted as triples, referring to the vertices of each triangle. For lines, the index array is in pairs indicating the start and end of each line.
const ARRAY_INDEX = 12;

#desc Represents the size of the [enum ArrayType] enum.
const ARRAY_MAX = 13;

#desc Indicates this custom channel contains unsigned normalized byte colors from 0 to 1, encoded as [PackedByteArray].
const ARRAY_CUSTOM_RGBA8_UNORM = 0;

#desc Indicates this custom channel contains signed normalized byte colors from -1 to 1, encoded as [PackedByteArray].
const ARRAY_CUSTOM_RGBA8_SNORM = 1;

#desc Indicates this custom channel contains half precision float colors, encoded as [PackedByteArray]. Only red and green channels are used.
const ARRAY_CUSTOM_RG_HALF = 2;

#desc Indicates this custom channel contains half precision float colors, encoded as [PackedByteArray].
const ARRAY_CUSTOM_RGBA_HALF = 3;

#desc Indicates this custom channel contains full float colors, in a [PackedFloat32Array]. Only the red green channel is used.
const ARRAY_CUSTOM_R_FLOAT = 4;

#desc Indicates this custom channel contains full float colors, in a [PackedFloat32Array]. Only red and green channels are used.
const ARRAY_CUSTOM_RG_FLOAT = 5;

#desc Indicates this custom channel contains full float colors, in a [PackedFloat32Array]. Only red, green and blue channels are used.
const ARRAY_CUSTOM_RGB_FLOAT = 6;

#desc Indicates this custom channel contains full float colors, in a [PackedFloat32Array].
const ARRAY_CUSTOM_RGBA_FLOAT = 7;

#desc Represents the size of the [enum ArrayCustomFormat] enum.
const ARRAY_CUSTOM_MAX = 8;

#desc Mesh array contains vertices. All meshes require a vertex array so this should always be present.
const ARRAY_FORMAT_VERTEX = 1;

#desc Mesh array contains normals.
const ARRAY_FORMAT_NORMAL = 2;

#desc Mesh array contains tangents.
const ARRAY_FORMAT_TANGENT = 4;

#desc Mesh array contains colors.
const ARRAY_FORMAT_COLOR = 8;

#desc Mesh array contains UVs.
const ARRAY_FORMAT_TEX_UV = 16;

#desc Mesh array contains second UV.
const ARRAY_FORMAT_TEX_UV2 = 32;

#desc Mesh array contains custom channel index 0.
const ARRAY_FORMAT_CUSTOM0 = 64;

#desc Mesh array contains custom channel index 1.
const ARRAY_FORMAT_CUSTOM1 = 128;

#desc Mesh array contains custom channel index 2.
const ARRAY_FORMAT_CUSTOM2 = 256;

#desc Mesh array contains custom channel index 3.
const ARRAY_FORMAT_CUSTOM3 = 512;

#desc Mesh array contains bones.
const ARRAY_FORMAT_BONES = 1024;

#desc Mesh array contains bone weights.
const ARRAY_FORMAT_WEIGHTS = 2048;

#desc Mesh array uses indices.
const ARRAY_FORMAT_INDEX = 4096;

#desc Mask of mesh channels permitted in blend shapes.
const ARRAY_FORMAT_BLEND_SHAPE_MASK = 7;

#desc Shift of first custom channel.
const ARRAY_FORMAT_CUSTOM_BASE = 13;

#desc Number of format bits per custom channel. See [enum ArrayCustomFormat].
const ARRAY_FORMAT_CUSTOM_BITS = 3;

#desc Amount to shift [enum ArrayCustomFormat] for custom channel index 0.
const ARRAY_FORMAT_CUSTOM0_SHIFT = 13;

#desc Amount to shift [enum ArrayCustomFormat] for custom channel index 1.
const ARRAY_FORMAT_CUSTOM1_SHIFT = 16;

#desc Amount to shift [enum ArrayCustomFormat] for custom channel index 2.
const ARRAY_FORMAT_CUSTOM2_SHIFT = 19;

#desc Amount to shift [enum ArrayCustomFormat] for custom channel index 3.
const ARRAY_FORMAT_CUSTOM3_SHIFT = 22;

#desc Mask of custom format bits per custom channel. Must be shifted by one of the SHIFT constants. See [enum ArrayCustomFormat].
const ARRAY_FORMAT_CUSTOM_MASK = 7;

#desc Shift of first compress flag. Compress flags should be passed to [method ArrayMesh.add_surface_from_arrays] and [method SurfaceTool.commit].
const ARRAY_COMPRESS_FLAGS_BASE = 25;

#desc Flag used to mark that the array contains 2D vertices.
const ARRAY_FLAG_USE_2D_VERTICES = 33554432;

#desc Flag indices that the mesh data will use [code]GL_DYNAMIC_DRAW[/code] on GLES. Unused on Vulkan.
const ARRAY_FLAG_USE_DYNAMIC_UPDATE = 67108864;

#desc Flag used to mark that the mesh contains up to 8 bone influences per vertex. This flag indicates that [constant ARRAY_BONES] and [constant ARRAY_WEIGHTS] elements will have double length.
const ARRAY_FLAG_USE_8_BONE_WEIGHTS = 134217728;

#desc Blend shapes are normalized.
const BLEND_SHAPE_MODE_NORMALIZED = 0;

#desc Blend shapes are relative to base weight.
const BLEND_SHAPE_MODE_RELATIVE = 1;


#desc Sets a hint to be used for lightmap resolution.
var lightmap_size_hint: Vector2i;



func _get_aabb() -> AABB:
	pass;

func _get_blend_shape_count() -> int:
	pass;

func _get_blend_shape_name(index: int) -> StringName:
	pass;

func _get_surface_count() -> int:
	pass;

func _set_blend_shape_name(index: int, name: StringName) -> void:
	pass;

func _surface_get_array_index_len(index: int) -> int:
	pass;

func _surface_get_array_len(index: int) -> int:
	pass;

func _surface_get_arrays(index: int) -> Array:
	pass;

func _surface_get_blend_shape_arrays(index: int) -> Array[]:
	pass;

func _surface_get_format(index: int) -> int:
	pass;

func _surface_get_lods(index: int) -> Dictionary:
	pass;

func _surface_get_material(index: int) -> Material:
	pass;

func _surface_get_primitive_type(index: int) -> int:
	pass;

func _surface_set_material(index: int, material: Material) -> void:
	pass;

#desc Calculate a [ConvexPolygonShape3D] from the mesh.
#desc If [param clean] is [code]true[/code] (default), duplicate and interior vertices are removed automatically. You can set it to [code]false[/code] to make the process faster if not needed.
#desc If [param simplify] is [code]true[/code], the geometry can be further simplified to reduce the number of vertices. Disabled by default.
func create_convex_shape(clean: bool, simplify: bool) -> Shape3D:
	pass;

#desc Calculate an outline mesh at a defined offset (margin) from the original mesh.
#desc [b]Note:[/b] This method typically returns the vertices in reverse order (e.g. clockwise to counterclockwise).
func create_outline(margin: float) -> Mesh:
	pass;

#desc Calculate a [ConcavePolygonShape3D] from the mesh.
func create_trimesh_shape() -> Shape3D:
	pass;

#desc Generate a [TriangleMesh] from the mesh. Considers only surfaces using one of these primitive types: [constant PRIMITIVE_TRIANGLES], [constant PRIMITIVE_TRIANGLE_STRIP].
func generate_triangle_mesh() -> TriangleMesh:
	pass;

#desc Returns the smallest [AABB] enclosing this mesh in local space. Not affected by [code]custom_aabb[/code]. See also [method VisualInstance3D.get_transformed_aabb].
#desc [b]Note:[/b] This is only implemented for [ArrayMesh] and [PrimitiveMesh].
func get_aabb() -> AABB:
	pass;

#desc Returns all the vertices that make up the faces of the mesh. Each three vertices represent one triangle.
func get_faces() -> PackedVector3Array:
	pass;

#desc Returns the number of surfaces that the [Mesh] holds.
func get_surface_count() -> int:
	pass;

#desc Returns the arrays for the vertices, normals, uvs, etc. that make up the requested surface (see [method ArrayMesh.add_surface_from_arrays]).
func surface_get_arrays(surf_idx: int) -> Array:
	pass;

#desc Returns the blend shape arrays for the requested surface.
func surface_get_blend_shape_arrays(surf_idx: int) -> Array[]:
	pass;

#desc Returns a [Material] in a given surface. Surface is rendered using this material.
func surface_get_material(surf_idx: int) -> Material:
	pass;

#desc Sets a [Material] for a given surface. Surface will be rendered using this material.
func surface_set_material(surf_idx: int, material: Material) -> void:
	pass;


