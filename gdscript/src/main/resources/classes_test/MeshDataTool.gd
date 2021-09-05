extends RefCounted
class_name MeshDataTool



func clear() -> void:
    pass;

func commit_to_surface(mesh: ArrayMesh) -> int:
    pass;

func create_from_surface(mesh: ArrayMesh, surface: int) -> int:
    pass;

func get_edge_count() -> int:
    pass;

func get_edge_faces(idx: int) -> PackedInt32Array:
    pass;

func get_edge_meta(idx: int) -> Variant:
    pass;

func get_edge_vertex(idx: int, vertex: int) -> int:
    pass;

func get_face_count() -> int:
    pass;

func get_face_edge(idx: int, edge: int) -> int:
    pass;

func get_face_meta(idx: int) -> Variant:
    pass;

func get_face_normal(idx: int) -> Vector3:
    pass;

func get_face_vertex(idx: int, vertex: int) -> int:
    pass;

func get_format() -> int:
    pass;

func get_material() -> Material:
    pass;

func get_vertex(idx: int) -> Vector3:
    pass;

func get_vertex_bones(idx: int) -> PackedInt32Array:
    pass;

func get_vertex_color(idx: int) -> Color:
    pass;

func get_vertex_count() -> int:
    pass;

func get_vertex_edges(idx: int) -> PackedInt32Array:
    pass;

func get_vertex_faces(idx: int) -> PackedInt32Array:
    pass;

func get_vertex_meta(idx: int) -> Variant:
    pass;

func get_vertex_normal(idx: int) -> Vector3:
    pass;

func get_vertex_tangent(idx: int) -> Plane:
    pass;

func get_vertex_uv(idx: int) -> Vector2:
    pass;

func get_vertex_uv2(idx: int) -> Vector2:
    pass;

func get_vertex_weights(idx: int) -> PackedFloat32Array:
    pass;

func set_edge_meta(idx: int, meta: Variant) -> void:
    pass;

func set_face_meta(idx: int, meta: Variant) -> void:
    pass;

func set_material(material: Material) -> void:
    pass;

func set_vertex(idx: int, vertex: Vector3) -> void:
    pass;

func set_vertex_bones(idx: int, bones: PackedInt32Array) -> void:
    pass;

func set_vertex_color(idx: int, color: Color) -> void:
    pass;

func set_vertex_meta(idx: int, meta: Variant) -> void:
    pass;

func set_vertex_normal(idx: int, normal: Vector3) -> void:
    pass;

func set_vertex_tangent(idx: int, tangent: Plane) -> void:
    pass;

func set_vertex_uv(idx: int, uv: Vector2) -> void:
    pass;

func set_vertex_uv2(idx: int, uv2: Vector2) -> void:
    pass;

func set_vertex_weights(idx: int, weights: PackedFloat32Array) -> void:
    pass;

