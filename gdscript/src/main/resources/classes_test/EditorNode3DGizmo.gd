extends Node3DGizmo
class_name EditorNode3DGizmo



func _commit_handle(index: int, restore: Variant, cancel: bool) -> void:
    pass;

func _get_handle_name(index: int) -> String:
    pass;

func _get_handle_value(index: int) -> Variant:
    pass;

func _is_handle_highlighted(index: int) -> bool:
    pass;

func _redraw() -> void:
    pass;

func _set_handle(index: int, camera: Camera3D, point: Vector2) -> void:
    pass;

func add_collision_segments(segments: PackedVector3Array) -> void:
    pass;

func add_collision_triangles(triangles: TriangleMesh) -> void:
    pass;

func add_handles(handles: PackedVector3Array, material: Material, billboard: bool, secondary: bool) -> void:
    pass;

func add_lines(lines: PackedVector3Array, material: Material, billboard: bool, modulate: Color) -> void:
    pass;

func add_mesh(mesh: ArrayMesh, billboard: bool, skeleton: SkinReference, material: Material) -> void:
    pass;

func add_unscaled_billboard(material: Material, default_scale: float, modulate: Color) -> void:
    pass;

func clear() -> void:
    pass;

func get_plugin() -> EditorNode3DGizmoPlugin:
    pass;

func get_spatial_node() -> Node3D:
    pass;

func set_hidden(hidden: bool) -> void:
    pass;

func set_spatial_node(node: Node) -> void:
    pass;

