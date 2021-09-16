extends Resource
class_name EditorSceneImporterMesh

var _data: Dictionary;

func add_blend_shape(name: String) -> void:
    pass;
func add_surface(primitive: int, arrays: Array, blend_shapes: Array, lods: Dictionary, material: Material, name: String) -> void:
    pass;
func clear() -> void:
    pass;
func get_blend_shape_count() -> int:
    pass;
func get_blend_shape_mode() -> int:
    pass;
func get_blend_shape_name(blend_shape_idx: int) -> String:
    pass;
func get_lightmap_size_hint() -> Vector2i:
    pass;
func get_mesh(arg0: Mesh) -> ArrayMesh:
    pass;
func get_surface_arrays(surface_idx: int) -> Array:
    pass;
func get_surface_blend_shape_arrays(surface_idx: int, blend_shape_idx: int) -> Array:
    pass;
func get_surface_count() -> int:
    pass;
func get_surface_lod_count(surface_idx: int) -> int:
    pass;
func get_surface_lod_indices(surface_idx: int, lod_idx: int) -> PackedInt32Array:
    pass;
func get_surface_lod_size(surface_idx: int, lod_idx: int) -> float:
    pass;
func get_surface_material(surface_idx: int) -> Material:
    pass;
func get_surface_name(surface_idx: int) -> String:
    pass;
func get_surface_primitive_type(surface_idx: int) -> int:
    pass;
func set_blend_shape_mode(mode: int) -> void:
    pass;
func set_lightmap_size_hint(size: Vector2i) -> void:
    pass;
