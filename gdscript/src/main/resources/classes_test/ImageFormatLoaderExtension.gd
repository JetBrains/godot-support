#brief Base class for creating [ImageFormatLoader] extensions (adding support for extra image formats).
#desc The engine supports multiple image formats out of the box (PNG, SVG, JPEG, WebP to name a few), but you can choose to implement support for additional image formats by extending this class.
#desc Be sure to respect the documented return types and values. You should create an instance of it, and call [method add_format_loader] to register that loader during the initialization phase.
class_name ImageFormatLoaderExtension




#desc Returns the list of file extensions for this image format. Files with the given extensions will be treated as image file and loaded using this class.
virtual const func _get_recognized_extensions() -> PackedStringArray:
	pass;

#desc Loads the content of [param fileaccess] into the provided [param image].
virtual func _load_image(image: Image, fileaccess: FileAccess, flags: int, scale: float) -> int:
	pass;

#desc Add this format loader to the engine, allowing it to recognize the file extensions returned by [method _get_recognized_extensions].
func add_format_loader() -> void:
	pass;

#desc Remove this format loader from the engine.
func remove_format_loader() -> void:
	pass;


