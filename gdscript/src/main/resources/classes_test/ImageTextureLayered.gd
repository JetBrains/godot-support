class_name ImageTextureLayered




#desc Creates an [ImageTextureLayered] from an array of [Image]s. The first image decides the width, height, image format and mipmapping setting. The other images must have the same width, height, image format and mipmapping setting.
#desc Each [Image] represents one [code]layer[/code].
func create_from_images() -> int:
	pass;

#desc Replaces the existing [Image] data at the given [code]layer[/code] with this new image.
#desc The given [Image] must have the same width, height, image format and mipmapping setting (a [code]bool[/code] value) as the rest of the referenced images.
#desc If the image format is unsupported, it will be decompressed and converted to a similar and supported [enum Image.Format].
#desc The update is immediate: synced with the draw.
func update_layer(image: Image, layer: int) -> void:
	pass;


