extends RefCounted
class_name SurfaceTool
const CUSTOM_RGBA8_UNORM = 0;
const CUSTOM_RGBA8_SNORM = 1;
const CUSTOM_RG_HALF = 2;
const CUSTOM_RGBA_HALF = 3;
const CUSTOM_R_FLOAT = 4;
const CUSTOM_RG_FLOAT = 5;
const CUSTOM_RGB_FLOAT = 6;
const CUSTOM_RGBA_FLOAT = 7;
const CUSTOM_MAX = 8;
const SKIN_4_WEIGHTS = 0;
const SKIN_8_WEIGHTS = 1;


func add_index(index: int) -> void:
    pass;
func add_triangle_fan(vertices: PackedVector3Array, uvs: PackedVector2Array, colors: PackedColorArray, uv2s: PackedVector2Array, normals: PackedVector3Array, tangents: Array) -> void:
    pass;
func add_vertex(vertex: Vector3) -> void:
    pass;
func append_from(existing: Mesh, surface: int, transform: Transform3D) -> void:
    pass;
func begin(primitive: int) -> void:
    pass;
func clear() -> void:
    pass;
func commit(existing: ArrayMesh, flags: int) -> ArrayMesh:
    pass;
func commit_to_arrays() -> Array:
    pass;
func create_from(existing: Mesh, surface: int) -> void:
    pass;
func create_from_blend_shape(existing: Mesh, surface: int, blend_shape: String) -> void:
    pass;
func deindex() -> void:
    pass;
func generate_lod(nd_threshold: float, target_index_count: int) -> PackedInt32Array:
    pass;
func generate_normals(flip: bool) -> void:
    pass;
func generate_tangents() -> void:
    pass;
func get_custom_format(index: int) -> int:
    pass;
func get_max_axis_length() -> float:
    pass;
func get_primitive() -> int:
    pass;
func get_skin_weight_count() -> int:
    pass;
func index() -> void:
    pass;
func optimize_indices_for_cache() -> void:
    pass;
func set_bones(bones: PackedInt32Array) -> void:
    pass;
func set_color(color: Color) -> void:
    pass;
func set_custom(index: int, custom: Color) -> void:
    pass;
func set_custom_format(index: int, format: int) -> void:
    pass;
func set_material(material: Material) -> void:
    pass;
func set_normal(normal: Vector3) -> void:
    pass;
func set_skin_weight_count(count: int) -> void:
    pass;
func set_smooth_group(index: int) -> void:
    pass;
func set_tangent(tangent: Plane) -> void:
    pass;
func set_uv(uv: Vector2) -> void:
    pass;
func set_uv2(uv2: Vector2) -> void:
    pass;
func set_weights(weights: PackedFloat32Array) -> void:
    pass;
