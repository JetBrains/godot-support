extends RefCounted
class_name EditorResourcePreviewGenerator


func _can_generate_small_preview() -> bool:
    pass;
func _generate(from: Resource, size: Vector2) -> Texture2D:
    pass;
func _generate_from_path(path: String, size: Vector2) -> Texture2D:
    pass;
func _generate_small_preview_automatically() -> bool:
    pass;
func _handles(type: String) -> bool:
    pass;
