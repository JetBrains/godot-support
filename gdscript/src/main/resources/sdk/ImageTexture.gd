extends Texture2D
#brief A [Texture2D] based on an [Image].
#desc A [Texture2D] based on an [Image]. For an image to be displayed, an [ImageTexture] has to be created from it using the [method create_from_image] method:
#desc [codeblock]
#desc var image = Image.load_from_file("res://icon.svg")
#desc var texture = ImageTexture.create_from_image(image)
#desc $Sprite2D.texture = texture
#desc [/codeblock]
#desc This way, textures can be created at run-time by loading images both from within the editor and externally.
#desc [b]Warning:[/b] Prefer to load imported textures with [method @GDScript.load] over loading them from within the filesystem dynamically with [method Image.load], as it may not work in exported projects:
#desc [codeblock]
#desc var texture = load("res://icon.svg")
#desc $Sprite2D.texture = texture
#desc [/codeblock]
#desc This is because images have to be imported as a [CompressedTexture2D] first to be loaded with [method @GDScript.load]. If you'd still like to load an image file just like any other [Resource], import it as an [Image] resource instead, and then load it normally using the [method @GDScript.load] method.
#desc [b]Note:[/b] The image can be retrieved from an imported texture using the [method Texture2D.get_image] method, which returns a copy of the image:
#desc [codeblock]
#desc var texture = load("res://icon.svg")
#desc var image : Image = texture.get_image()
#desc [/codeblock]
#desc An [ImageTexture] is not meant to be operated from within the editor interface directly, and is mostly useful for rendering images on screen dynamically via code. If you need to generate images procedurally from within the editor, consider saving and importing images as custom texture resources implementing a new [EditorImportPlugin].
#desc [b]Note:[/b] The maximum texture size is 16384×16384 pixels due to graphics hardware limitations.
class_name ImageTexture




#desc Creates a new [ImageTexture] and initializes it by allocating and setting the data from an [Image].
static func create_from_image(image: Image) -> ImageTexture:
	pass;

#desc Returns the format of the texture, one of [enum Image.Format].
func get_format() -> int:
	pass;

#desc Replaces the texture's data with a new [Image]. This will re-allocate new memory for the texture.
#desc If you want to update the image, but don't need to change its parameters (format, size), use [method update] instead for better performance.
func set_image(image: Image) -> void:
	pass;

#desc Resizes the texture to the specified dimensions.
func set_size_override(size: Vector2i) -> void:
	pass;

#desc Replaces the texture's data with a new [Image].
#desc [b]Note:[/b] The texture has to be created using [method create_from_image] or initialized first with the [method set_image] method before it can be updated. The new image dimensions, format, and mipmaps configuration should match the existing texture's image configuration.
#desc Use this method over [method set_image] if you need to update the texture frequently, which is faster than allocating additional memory for a new texture each time.
func update(image: Image) -> void:
	pass;


