extends Mesh
class_name ArrayMesh


var blend_shape_mode: int setget set_blend_shape_mode, get_blend_shape_mode;
var custom_aabb: AABB setget set_custom_aabb, get_custom_aabb;
var shadow_mesh: ArrayMesh setget set_shadow_mesh, get_shadow_mesh;

func add_blend_shape(name: StringName) -> void:
    pass;

func add_surface_from_arrays(primitive: int, arrays: Array, blend_shapes: Array, lods: Dictionary, compress_flags: int) -> void:
    pass;

func clear_blend_shapes() -> void:
    pass;

func clear_surfaces() -> void:
    pass;

func get_blend_shape_count() -> int:
    pass;

func get_blend_shape_name(index: int) -> StringName:
    pass;

func lightmap_unwrap(transform: Transform3D, texel_size: float) -> int:
    pass;

func regen_normal_maps() -> void:
    pass;

func set_blend_shape_name(index: int, name: StringName) -> void:
    pass;

func surface_find_by_name(name: String) -> int:
    pass;

func surface_get_array_index_len(surf_idx: int) -> int:
    pass;

func surface_get_array_len(surf_idx: int) -> int:
    pass;

func surface_get_format(surf_idx: int) -> int:
    pass;

func surface_get_name(surf_idx: int) -> String:
    pass;

func surface_get_primitive_type(surf_idx: int) -> int:
    pass;

func surface_set_name(surf_idx: int, name: String) -> void:
    pass;

func surface_update_attribute_region(surf_idx: int, offset: int, data: PackedByteArray) -> void:
    pass;

func surface_update_skin_region(surf_idx: int, offset: int, data: PackedByteArray) -> void:
    pass;

func surface_update_vertex_region(surf_idx: int, offset: int, data: PackedByteArray) -> void:
    pass;

