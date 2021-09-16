extends Resource
class_name EditorNode3DGizmoPlugin


func _can_be_hidden() -> bool:
    pass;
func _commit_handle(gizmo: EditorNode3DGizmo, index: int, restore: Variant, cancel: bool) -> void:
    pass;
func _create_gizmo(spatial: Node3D) -> EditorNode3DGizmo:
    pass;
func _get_gizmo_name() -> String:
    pass;
func _get_handle_name(gizmo: EditorNode3DGizmo, index: int) -> String:
    pass;
func _get_handle_value(gizmo: EditorNode3DGizmo, index: int) -> Variant:
    pass;
func _get_priority() -> int:
    pass;
func _has_gizmo(spatial: Node3D) -> bool:
    pass;
func _is_handle_highlighted(gizmo: EditorNode3DGizmo, index: int) -> bool:
    pass;
func _is_selectable_when_hidden() -> bool:
    pass;
func _redraw(gizmo: EditorNode3DGizmo) -> void:
    pass;
func _set_handle(gizmo: EditorNode3DGizmo, index: int, camera: Camera3D, point: Vector2) -> void:
    pass;
func add_material(name: String, material: StandardMaterial3D) -> void:
    pass;
func create_handle_material(name: String, billboard: bool, texture: Texture2D) -> void:
    pass;
func create_icon_material(name: String, texture: Texture2D, on_top: bool, color: Color) -> void:
    pass;
func create_material(name: String, color: Color, billboard: bool, on_top: bool, use_vertex_color: bool) -> void:
    pass;
func get_material(name: String, gizmo: EditorNode3DGizmo) -> StandardMaterial3D:
    pass;
