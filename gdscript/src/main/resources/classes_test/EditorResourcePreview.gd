extends Node
#brief Helper to generate previews of resources or files.
#desc This object is used to generate previews for resources of files.
#desc [b]Note:[/b] This class shouldn't be instantiated directly. Instead, access the singleton using [method EditorInterface.get_resource_previewer].
class_name EditorResourcePreview




#desc Create an own, custom preview generator.
func add_preview_generator(generator: EditorResourcePreviewGenerator) -> void:
	pass;

#desc Check if the resource changed, if so, it will be invalidated and the corresponding signal emitted.
func check_for_invalidation(path: String) -> void:
	pass;

#desc Queue the [param resource] being edited for preview. Once the preview is ready, the [param receiver]'s [param receiver_func] will be called. The [param receiver_func] must take the following four arguments: [String] path, [Texture2D] preview, [Texture2D] thumbnail_preview, [Variant] userdata. [param userdata] can be anything, and will be returned when [param receiver_func] is called.
#desc [b]Note:[/b] If it was not possible to create the preview the [param receiver_func] will still be called, but the preview will be null.
func queue_edited_resource_preview(resource: Resource, receiver: Object, receiver_func: StringName, userdata: Variant) -> void:
	pass;

#desc Queue a resource file located at [param path] for preview. Once the preview is ready, the [param receiver]'s [param receiver_func] will be called. The [param receiver_func] must take the following four arguments: [String] path, [Texture2D] preview, [Texture2D] thumbnail_preview, [Variant] userdata. [param userdata] can be anything, and will be returned when [param receiver_func] is called.
#desc [b]Note:[/b] If it was not possible to create the preview the [param receiver_func] will still be called, but the preview will be null.
func queue_resource_preview(path: String, receiver: Object, receiver_func: StringName, userdata: Variant) -> void:
	pass;

#desc Removes a custom preview generator.
func remove_preview_generator(generator: EditorResourcePreviewGenerator) -> void:
	pass;


