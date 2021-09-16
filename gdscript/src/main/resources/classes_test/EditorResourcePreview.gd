extends Node
class_name EditorResourcePreview


func add_preview_generator(generator: EditorResourcePreviewGenerator) -> void:
    pass;
func check_for_invalidation(path: String) -> void:
    pass;
func queue_edited_resource_preview(resource: Resource, receiver: Object, receiver_func: StringName, userdata: Variant) -> void:
    pass;
func queue_resource_preview(path: String, receiver: Object, receiver_func: StringName, userdata: Variant) -> void:
    pass;
func remove_preview_generator(generator: EditorResourcePreviewGenerator) -> void:
    pass;
