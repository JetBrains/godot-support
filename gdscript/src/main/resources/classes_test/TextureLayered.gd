#brief Base class for texture types which contain the data of multiple [Image]s. Each image is of the same size and format.
#desc Base class for [Texture2DArray], [Cubemap] and [CubemapArray]. Cannot be used directly, but contains all the functions necessary for accessing the derived resource types.
#desc Data is set on a per-layer basis. For [Texture2DArray]s, the layer specifies the array layer.
#desc All images need to have the same width, height and number of mipmap levels.
#desc A [TextureLayered] can be loaded with [code]method ResourceFormatLoader.load[/code].
#desc To create such a texture file yourself, re-import your image files using the Godot Editor import presets.
#desc Internally, Godot maps these files to their respective counterparts in the target rendering driver (GLES3, Vulkan).
class_name TextureLayered

const LAYERED_TYPE_2D_ARRAY = 0;

const LAYERED_TYPE_CUBEMAP = 1;

const LAYERED_TYPE_CUBEMAP_ARRAY = 2;




virtual const func _get_format() -> int:
	pass;

virtual const func _get_height() -> int:
	pass;

virtual const func _get_layer_data() -> Image:
	pass;

virtual const func _get_layered_type() -> int:
	pass;

virtual const func _get_layers() -> int:
	pass;

virtual const func _get_width() -> int:
	pass;

virtual const func _has_mipmaps() -> bool:
	pass;

#desc Returns the current format being used by this texture. See [enum Image.Format] for details.
func get_format() -> int:
	pass;

#desc Returns the height of the texture. Height is typically represented by the Y-axis.
func get_height() -> int:
	pass;

#desc Returns an [Image] resource with the data from specified [param layer].
func get_layer_data() -> Image:
	pass;

func get_layered_type() -> int:
	pass;

#desc Returns the number of referenced [Image]s.
func get_layers() -> int:
	pass;

#desc Returns the width of the texture. Width is typically represented by the X-axis.
func get_width() -> int:
	pass;

#desc Returns [code]true[/code] if the layers have generated mipmaps.
func has_mipmaps() -> bool:
	pass;


