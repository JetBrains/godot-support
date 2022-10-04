#brief Image datatype.
#desc Native image datatype. Contains image data which can be converted to an [ImageTexture] and provides commonly used [i]image processing[/i] methods. The maximum width and height for an [Image] are [constant MAX_WIDTH] and [constant MAX_HEIGHT].
#desc An [Image] cannot be assigned to a [code]texture[/code] property of an object directly (such as [Sprite2D]), and has to be converted manually to an [ImageTexture] first.
#desc [b]Note:[/b] The maximum image size is 16384×16384 pixels due to graphics hardware limitations. Larger images may fail to import.
class_name Image

#desc The maximal width allowed for [Image] resources.
const MAX_WIDTH = 16777216;

#desc The maximal height allowed for [Image] resources.
const MAX_HEIGHT = 16777216;

#desc Texture format with a single 8-bit depth representing luminance.
const FORMAT_L8 = 0;

#desc OpenGL texture format with two values, luminance and alpha each stored with 8 bits.
const FORMAT_LA8 = 1;

#desc OpenGL texture format [code]RED[/code] with a single component and a bitdepth of 8.
const FORMAT_R8 = 2;

#desc OpenGL texture format [code]RG[/code] with two components and a bitdepth of 8 for each.
const FORMAT_RG8 = 3;

#desc OpenGL texture format [code]RGB[/code] with three components, each with a bitdepth of 8.
#desc [b]Note:[/b] When creating an [ImageTexture], an sRGB to linear color space conversion is performed.
const FORMAT_RGB8 = 4;

#desc OpenGL texture format [code]RGBA[/code] with four components, each with a bitdepth of 8.
#desc [b]Note:[/b] When creating an [ImageTexture], an sRGB to linear color space conversion is performed.
const FORMAT_RGBA8 = 5;

#desc OpenGL texture format [code]RGBA[/code] with four components, each with a bitdepth of 4.
const FORMAT_RGBA4444 = 6;

const FORMAT_RGB565 = 7;

#desc OpenGL texture format [code]GL_R32F[/code] where there's one component, a 32-bit floating-point value.
const FORMAT_RF = 8;

#desc OpenGL texture format [code]GL_RG32F[/code] where there are two components, each a 32-bit floating-point values.
const FORMAT_RGF = 9;

#desc OpenGL texture format [code]GL_RGB32F[/code] where there are three components, each a 32-bit floating-point values.
const FORMAT_RGBF = 10;

#desc OpenGL texture format [code]GL_RGBA32F[/code] where there are four components, each a 32-bit floating-point values.
const FORMAT_RGBAF = 11;

#desc OpenGL texture format [code]GL_R32F[/code] where there's one component, a 16-bit "half-precision" floating-point value.
const FORMAT_RH = 12;

#desc OpenGL texture format [code]GL_RG32F[/code] where there are two components, each a 16-bit "half-precision" floating-point value.
const FORMAT_RGH = 13;

#desc OpenGL texture format [code]GL_RGB32F[/code] where there are three components, each a 16-bit "half-precision" floating-point value.
const FORMAT_RGBH = 14;

#desc OpenGL texture format [code]GL_RGBA32F[/code] where there are four components, each a 16-bit "half-precision" floating-point value.
const FORMAT_RGBAH = 15;

#desc A special OpenGL texture format where the three color components have 9 bits of precision and all three share a single 5-bit exponent.
const FORMAT_RGBE9995 = 16;

#desc The [url=https://en.wikipedia.org/wiki/S3_Texture_Compression]S3TC[/url] texture format that uses Block Compression 1, and is the smallest variation of S3TC, only providing 1 bit of alpha and color data being premultiplied with alpha.
#desc [b]Note:[/b] When creating an [ImageTexture], an sRGB to linear color space conversion is performed.
const FORMAT_DXT1 = 17;

#desc The [url=https://en.wikipedia.org/wiki/S3_Texture_Compression]S3TC[/url] texture format that uses Block Compression 2, and color data is interpreted as not having been premultiplied by alpha. Well suited for images with sharp alpha transitions between translucent and opaque areas.
#desc [b]Note:[/b] When creating an [ImageTexture], an sRGB to linear color space conversion is performed.
const FORMAT_DXT3 = 18;

#desc The [url=https://en.wikipedia.org/wiki/S3_Texture_Compression]S3TC[/url] texture format also known as Block Compression 3 or BC3 that contains 64 bits of alpha channel data followed by 64 bits of DXT1-encoded color data. Color data is not premultiplied by alpha, same as DXT3. DXT5 generally produces superior results for transparent gradients compared to DXT3.
#desc [b]Note:[/b] When creating an [ImageTexture], an sRGB to linear color space conversion is performed.
const FORMAT_DXT5 = 19;

#desc Texture format that uses [url=https://www.khronos.org/opengl/wiki/Red_Green_Texture_Compression]Red Green Texture Compression[/url], normalizing the red channel data using the same compression algorithm that DXT5 uses for the alpha channel.
const FORMAT_RGTC_R = 20;

#desc Texture format that uses [url=https://www.khronos.org/opengl/wiki/Red_Green_Texture_Compression]Red Green Texture Compression[/url], normalizing the red and green channel data using the same compression algorithm that DXT5 uses for the alpha channel.
const FORMAT_RGTC_RG = 21;

#desc Texture format that uses [url=https://www.khronos.org/opengl/wiki/BPTC_Texture_Compression]BPTC[/url] compression with unsigned normalized RGBA components.
#desc [b]Note:[/b] When creating an [ImageTexture], an sRGB to linear color space conversion is performed.
const FORMAT_BPTC_RGBA = 22;

#desc Texture format that uses [url=https://www.khronos.org/opengl/wiki/BPTC_Texture_Compression]BPTC[/url] compression with signed floating-point RGB components.
const FORMAT_BPTC_RGBF = 23;

#desc Texture format that uses [url=https://www.khronos.org/opengl/wiki/BPTC_Texture_Compression]BPTC[/url] compression with unsigned floating-point RGB components.
const FORMAT_BPTC_RGBFU = 24;

#desc [url=https://en.wikipedia.org/wiki/Ericsson_Texture_Compression#ETC1]Ericsson Texture Compression format 1[/url], also referred to as "ETC1", and is part of the OpenGL ES graphics standard. This format cannot store an alpha channel.
const FORMAT_ETC = 25;

#desc [url=https://en.wikipedia.org/wiki/Ericsson_Texture_Compression#ETC2_and_EAC]Ericsson Texture Compression format 2[/url] ([code]R11_EAC[/code] variant), which provides one channel of unsigned data.
const FORMAT_ETC2_R11 = 26;

#desc [url=https://en.wikipedia.org/wiki/Ericsson_Texture_Compression#ETC2_and_EAC]Ericsson Texture Compression format 2[/url] ([code]SIGNED_R11_EAC[/code] variant), which provides one channel of signed data.
const FORMAT_ETC2_R11S = 27;

#desc [url=https://en.wikipedia.org/wiki/Ericsson_Texture_Compression#ETC2_and_EAC]Ericsson Texture Compression format 2[/url] ([code]RG11_EAC[/code] variant), which provides two channels of unsigned data.
const FORMAT_ETC2_RG11 = 28;

#desc [url=https://en.wikipedia.org/wiki/Ericsson_Texture_Compression#ETC2_and_EAC]Ericsson Texture Compression format 2[/url] ([code]SIGNED_RG11_EAC[/code] variant), which provides two channels of signed data.
const FORMAT_ETC2_RG11S = 29;

#desc [url=https://en.wikipedia.org/wiki/Ericsson_Texture_Compression#ETC2_and_EAC]Ericsson Texture Compression format 2[/url] ([code]RGB8[/code] variant), which is a follow-up of ETC1 and compresses RGB888 data.
#desc [b]Note:[/b] When creating an [ImageTexture], an sRGB to linear color space conversion is performed.
const FORMAT_ETC2_RGB8 = 30;

#desc [url=https://en.wikipedia.org/wiki/Ericsson_Texture_Compression#ETC2_and_EAC]Ericsson Texture Compression format 2[/url] ([code]RGBA8[/code]variant), which compresses RGBA8888 data with full alpha support.
#desc [b]Note:[/b] When creating an [ImageTexture], an sRGB to linear color space conversion is performed.
const FORMAT_ETC2_RGBA8 = 31;

#desc [url=https://en.wikipedia.org/wiki/Ericsson_Texture_Compression#ETC2_and_EAC]Ericsson Texture Compression format 2[/url] ([code]RGB8_PUNCHTHROUGH_ALPHA1[/code] variant), which compresses RGBA data to make alpha either fully transparent or fully opaque.
#desc [b]Note:[/b] When creating an [ImageTexture], an sRGB to linear color space conversion is performed.
const FORMAT_ETC2_RGB8A1 = 32;

const FORMAT_ETC2_RA_AS_RG = 33;

const FORMAT_DXT5_RA_AS_RG = 34;

#desc Represents the size of the [enum Format] enum.
const FORMAT_MAX = 35;

#desc Performs nearest-neighbor interpolation. If the image is resized, it will be pixelated.
const INTERPOLATE_NEAREST = 0;

#desc Performs bilinear interpolation. If the image is resized, it will be blurry. This mode is faster than [constant INTERPOLATE_CUBIC], but it results in lower quality.
const INTERPOLATE_BILINEAR = 1;

#desc Performs cubic interpolation. If the image is resized, it will be blurry. This mode often gives better results compared to [constant INTERPOLATE_BILINEAR], at the cost of being slower.
const INTERPOLATE_CUBIC = 2;

#desc Performs bilinear separately on the two most-suited mipmap levels, then linearly interpolates between them.
#desc It's slower than [constant INTERPOLATE_BILINEAR], but produces higher-quality results with far fewer aliasing artifacts.
#desc If the image does not have mipmaps, they will be generated and used internally, but no mipmaps will be generated on the resulting image.
#desc [b]Note:[/b] If you intend to scale multiple copies of the original image, it's better to call [method generate_mipmaps]] on it in advance, to avoid wasting processing power in generating them again and again.
#desc On the other hand, if the image already has mipmaps, they will be used, and a new set will be generated for the resulting image.
const INTERPOLATE_TRILINEAR = 3;

#desc Performs Lanczos interpolation. This is the slowest image resizing mode, but it typically gives the best results, especially when downscalng images.
const INTERPOLATE_LANCZOS = 4;

#desc Image does not have alpha.
const ALPHA_NONE = 0;

#desc Image stores alpha in a single bit.
const ALPHA_BIT = 1;

#desc Image uses alpha.
const ALPHA_BLEND = 2;

#desc Use S3TC compression.
const COMPRESS_S3TC = 0;

#desc Use ETC compression.
const COMPRESS_ETC = 1;

#desc Use ETC2 compression.
const COMPRESS_ETC2 = 2;

#desc Use BPTC compression.
const COMPRESS_BPTC = 3;

const USED_CHANNELS_L = 0;

const USED_CHANNELS_LA = 1;

const USED_CHANNELS_R = 2;

const USED_CHANNELS_RG = 3;

const USED_CHANNELS_RGB = 4;

const USED_CHANNELS_RGBA = 5;

#desc Source texture (before compression) is a regular texture. Default for all textures.
const COMPRESS_SOURCE_GENERIC = 0;

#desc Source texture (before compression) is in sRGB space.
const COMPRESS_SOURCE_SRGB = 1;

#desc Source texture (before compression) is a normal texture (e.g. it can be compressed into two channels).
const COMPRESS_SOURCE_NORMAL = 2;


#desc Holds all the image's color data in a given format. See [enum Format] constants.
var data: Dictionary;



func adjust_bcs(brightness: float, contrast: float, saturation: float) -> void:
	pass;

#desc Alpha-blends [param src_rect] from [param src] image to this image at coordinates [param dst], clipped accordingly to both image bounds. This image and [param src] image [b]must[/b] have the same format. [param src_rect] with not positive size is treated as empty.
func blend_rect(src: Image, src_rect: Rect2i, dst: Vector2i) -> void:
	pass;

#desc Alpha-blends [param src_rect] from [param src] image to this image using [param mask] image at coordinates [param dst], clipped accordingly to both image bounds. Alpha channels are required for both [param src] and [param mask]. [param dst] pixels and [param src] pixels will blend if the corresponding mask pixel's alpha value is not 0. This image and [param src] image [b]must[/b] have the same format. [param src] image and [param mask] image [b]must[/b] have the same size (width and height) but they can have different formats. [param src_rect] with not positive size is treated as empty.
func blend_rect_mask(src: Image, mask: Image, src_rect: Rect2i, dst: Vector2i) -> void:
	pass;

#desc Copies [param src_rect] from [param src] image to this image at coordinates [param dst], clipped accordingly to both image bounds. This image and [param src] image [b]must[/b] have the same format. [param src_rect] with not positive size is treated as empty.
func blit_rect(src: Image, src_rect: Rect2i, dst: Vector2i) -> void:
	pass;

#desc Blits [param src_rect] area from [param src] image to this image at the coordinates given by [param dst], clipped accordingly to both image bounds. [param src] pixel is copied onto [param dst] if the corresponding [param mask] pixel's alpha value is not 0. This image and [param src] image [b]must[/b] have the same format. [param src] image and [param mask] image [b]must[/b] have the same size (width and height) but they can have different formats. [param src_rect] with not positive size is treated as empty.
func blit_rect_mask(src: Image, mask: Image, src_rect: Rect2i, dst: Vector2i) -> void:
	pass;

#desc Converts a bump map to a normal map. A bump map provides a height offset per-pixel, while a normal map provides a normal direction per pixel.
func bump_map_to_normal_map(bump_scale: float) -> void:
	pass;

#desc Removes the image's mipmaps.
func clear_mipmaps() -> void:
	pass;

#desc Compresses the image to use less memory. Can not directly access pixel data while the image is compressed. Returns error if the chosen compression mode is not available. See [enum CompressMode] and [enum CompressSource] constants.
func compress(mode: int, source: int, lossy_quality: float) -> int:
	pass;

func compress_from_channels(mode: int, channels: int, lossy_quality: float) -> int:
	pass;

#desc Compute image metrics on the current image and the compared image.
#desc The dictionary contains [code]max[/code], [code]mean[/code], [code]mean_squared[/code], [code]root_mean_squared[/code] and [code]peak_snr[/code].
func compute_image_metrics(compared_image: Image, use_luma: bool) -> Dictionary:
	pass;

#desc Converts the image's format. See [enum Format] constants.
func convert(format: int) -> void:
	pass;

#desc Copies [param src] image to this image.
func copy_from(src: Image) -> void:
	pass;

#desc Creates an empty image of given size and format. See [enum Format] constants. If [param use_mipmaps] is [code]true[/code] then generate mipmaps for this image. See the [method generate_mipmaps].
func create(width: int, height: int, use_mipmaps: bool, format: int) -> void:
	pass;

#desc Creates a new image of given size and format. See [enum Format] constants. Fills the image with the given raw data. If [param use_mipmaps] is [code]true[/code] then loads mipmaps for this image from [param data]. See [method generate_mipmaps].
func create_from_data(width: int, height: int, use_mipmaps: bool, format: int, data: PackedByteArray) -> void:
	pass;

#desc Crops the image to the given [param width] and [param height]. If the specified size is larger than the current size, the extra area is filled with black pixels.
func crop(width: int, height: int) -> void:
	pass;

#desc Decompresses the image if it is VRAM compressed in a supported format. Returns [constant OK] if the format is supported, otherwise [constant ERR_UNAVAILABLE].
#desc [b]Note:[/b] The following formats can be decompressed: DXT, RGTC, BPTC. The formats ETC1 and ETC2 are not supported.
func decompress() -> int:
	pass;

#desc Returns [constant ALPHA_BLEND] if the image has data for alpha values. Returns [constant ALPHA_BIT] if all the alpha values are stored in a single bit. Returns [constant ALPHA_NONE] if no data for alpha values is found.
func detect_alpha() -> int:
	pass;

func detect_used_channels(source: int) -> int:
	pass;

#desc Fills the image with [param color].
func fill(color: Color) -> void:
	pass;

#desc Fills [param rect] with [param color].
func fill_rect(rect: Rect2i, color: Color) -> void:
	pass;

#desc Blends low-alpha pixels with nearby pixels.
func fix_alpha_edges() -> void:
	pass;

#desc Flips the image horizontally.
func flip_x() -> void:
	pass;

#desc Flips the image vertically.
func flip_y() -> void:
	pass;

#desc Generates mipmaps for the image. Mipmaps are precalculated lower-resolution copies of the image that are automatically used if the image needs to be scaled down when rendered. They help improve image quality and performance when rendering. This method returns an error if the image is compressed, in a custom format, or if the image's width/height is [code]0[/code].
#desc [b]Note:[/b] Mipmap generation is done on the CPU, is single-threaded and is [i]always[/i] done on the main thread. This means generating mipmaps will result in noticeable stuttering during gameplay, even if [method generate_mipmaps] is called from a [Thread].
func generate_mipmaps(renormalize: bool) -> int:
	pass;

#desc Returns a copy of the image's raw data.
func get_data() -> PackedByteArray:
	pass;

#desc Returns the image's format. See [enum Format] constants.
func get_format() -> int:
	pass;

#desc Returns the image's height.
func get_height() -> int:
	pass;

#desc Returns the offset where the image's mipmap with index [param mipmap] is stored in the [code]data[/code] dictionary.
func get_mipmap_offset(mipmap: int) -> int:
	pass;

#desc Returns the color of the pixel at [code](x, y)[/code].
#desc This is the same as [method get_pixelv], but with two integer arguments instead of a [Vector2i] argument.
func get_pixel(x: int, y: int) -> Color:
	pass;

#desc Returns the color of the pixel at [param point].
#desc This is the same as [method get_pixel], but with a [Vector2i] argument instead of two integer arguments.
func get_pixelv(point: Vector2i) -> Color:
	pass;

#desc Returns a new image that is a copy of the image's area specified with [param rect].
func get_rect(rect: Rect2i) -> Image:
	pass;

#desc Returns the image's size (width and height).
func get_size() -> Vector2i:
	pass;

#desc Returns a [Rect2i] enclosing the visible portion of the image, considering each pixel with a non-zero alpha channel as visible.
func get_used_rect() -> Rect2i:
	pass;

#desc Returns the image's width.
func get_width() -> int:
	pass;

#desc Returns [code]true[/code] if the image has generated mipmaps.
func has_mipmaps() -> bool:
	pass;

#desc Returns [code]true[/code] if the image is compressed.
func is_compressed() -> bool:
	pass;

#desc Returns [code]true[/code] if the image has no data.
func is_empty() -> bool:
	pass;

#desc Returns [code]true[/code] if all the image's pixels have an alpha value of 0. Returns [code]false[/code] if any pixel has an alpha value higher than 0.
func is_invisible() -> bool:
	pass;

#desc Loads an image from file [param path]. See [url=$DOCS_URL/tutorials/assets_pipeline/importing_images.html#supported-image-formats]Supported image formats[/url] for a list of supported image formats and limitations.
#desc [b]Warning:[/b] This method should only be used in the editor or in cases when you need to load external images at run-time, such as images located at the [code]user://[/code] directory, and may not work in exported projects.
#desc See also [ImageTexture] description for usage examples.
func load(path: String) -> int:
	pass;

#desc Loads an image from the binary contents of a BMP file.
#desc [b]Note:[/b] Godot's BMP module doesn't support 16-bit per pixel images. Only 1-bit, 4-bit, 8-bit, 24-bit, and 32-bit per pixel images are supported.
func load_bmp_from_buffer(buffer: PackedByteArray) -> int:
	pass;

#desc Creates a new [Image] and loads data from the specified file.
static func load_from_file(path: String) -> Image:
	pass;

#desc Loads an image from the binary contents of a JPEG file.
func load_jpg_from_buffer(buffer: PackedByteArray) -> int:
	pass;

#desc Loads an image from the binary contents of a PNG file.
func load_png_from_buffer(buffer: PackedByteArray) -> int:
	pass;

#desc Loads an image from the binary contents of a TGA file.
func load_tga_from_buffer(buffer: PackedByteArray) -> int:
	pass;

#desc Loads an image from the binary contents of a WebP file.
func load_webp_from_buffer(buffer: PackedByteArray) -> int:
	pass;

#desc Converts the image's data to represent coordinates on a 3D plane. This is used when the image represents a normal map. A normal map can add lots of detail to a 3D surface without increasing the polygon count.
func normal_map_to_xy() -> void:
	pass;

#desc Multiplies color values with alpha values. Resulting color values for a pixel are [code](color * alpha)/256[/code].
func premultiply_alpha() -> void:
	pass;

#desc Resizes the image to the given [param width] and [param height]. New pixels are calculated using the [param interpolation] mode defined via [enum Interpolation] constants.
func resize(width: int, height: int, interpolation: int) -> void:
	pass;

#desc Resizes the image to the nearest power of 2 for the width and height. If [param square] is [code]true[/code] then set width and height to be the same. New pixels are calculated using the [param interpolation] mode defined via [enum Interpolation] constants.
func resize_to_po2(square: bool, interpolation: int) -> void:
	pass;

#desc Converts a standard RGBE (Red Green Blue Exponent) image to an sRGB image.
func rgbe_to_srgb() -> Image:
	pass;

#desc Rotates the image by [code]180[/code] degrees. The width and height of the image must be greater than [code]1[/code].
func rotate_180() -> void:
	pass;

#desc Rotates the image in the specified [param direction] by [code]90[/code] degrees. The width and height of the image must be greater than [code]1[/code]. If the width and height are not equal, the image will be resized.
func rotate_90(direction: int) -> void:
	pass;

#desc Saves the image as an EXR file to [param path]. If [param grayscale] is [code]true[/code] and the image has only one channel, it will be saved explicitly as monochrome rather than one red channel. This function will return [constant ERR_UNAVAILABLE] if Godot was compiled without the TinyEXR module.
#desc [b]Note:[/b] The TinyEXR module is disabled in non-editor builds, which means [method save_exr] will return [constant ERR_UNAVAILABLE] when it is called from an exported project.
func save_exr(path: String, grayscale: bool) -> int:
	pass;

#desc Saves the image as an EXR file to a byte array. If [param grayscale] is [code]true[/code] and the image has only one channel, it will be saved explicitly as monochrome rather than one red channel. This function will return an empty byte array if Godot was compiled without the TinyEXR module.
#desc [b]Note:[/b] The TinyEXR module is disabled in non-editor builds, which means [method save_exr] will return an empty byte array when it is called from an exported project.
func save_exr_to_buffer(grayscale: bool) -> PackedByteArray:
	pass;

#desc Saves the image as a JPEG file to [param path] with the specified [param quality] between [code]0.01[/code] and [code]1.0[/code] (inclusive). Higher [param quality] values result in better-looking output at the cost of larger file sizes. Recommended [param quality] values are between [code]0.75[/code] and [code]0.90[/code]. Even at quality [code]1.00[/code], JPEG compression remains lossy.
#desc [b]Note:[/b] JPEG does not save an alpha channel. If the [Image] contains an alpha channel, the image will still be saved, but the resulting JPEG file won't contain the alpha channel.
func save_jpg(path: String, quality: float) -> int:
	pass;

#desc Saves the image as a JPEG file to a byte array with the specified [param quality] between [code]0.01[/code] and [code]1.0[/code] (inclusive). Higher [param quality] values result in better-looking output at the cost of larger byte array sizes (and therefore memory usage). Recommended [param quality] values are between [code]0.75[/code] and [code]0.90[/code]. Even at quality [code]1.00[/code], JPEG compression remains lossy.
#desc [b]Note:[/b] JPEG does not save an alpha channel. If the [Image] contains an alpha channel, the image will still be saved, but the resulting byte array won't contain the alpha channel.
func save_jpg_to_buffer(quality: float) -> PackedByteArray:
	pass;

#desc Saves the image as a PNG file to the file at [param path].
func save_png(path: String) -> int:
	pass;

#desc Saves the image as a PNG file to a byte array.
func save_png_to_buffer() -> PackedByteArray:
	pass;

#desc Saves the image as a WebP (Web Picture) file to the file at [param path]. By default it will save lossless. If [param lossy] is true, the image will be saved lossy, using the [param quality] setting between 0.0 and 1.0 (inclusive).
func save_webp(path: String, lossy: bool, quality: float) -> int:
	pass;

#desc Saves the image as a WebP (Web Picture) file to a byte array. By default it will save lossless. If [param lossy] is true, the image will be saved lossy, using the [param quality] setting between 0.0 and 1.0 (inclusive).
func save_webp_to_buffer(lossy: bool, quality: float) -> PackedByteArray:
	pass;

#desc Sets the [Color] of the pixel at [code](x, y)[/code] to [param color]. Example:
#desc [codeblocks]
#desc [gdscript]
#desc var img_width = 10
#desc var img_height = 5
#desc var img = Image.new()
#desc img.create(img_width, img_height, false, Image.FORMAT_RGBA8)
#desc 
#desc img.set_pixel(1, 2, Color.red) # Sets the color at (1, 2) to red.
#desc [/gdscript]
#desc [csharp]
#desc int imgWidth = 10;
#desc int imgHeight = 5;
#desc var img = new Image();
#desc img.Create(imgWidth, imgHeight, false, Image.Format.Rgba8);
#desc 
#desc img.SetPixel(1, 2, Colors.Red); // Sets the color at (1, 2) to red.
#desc [/csharp]
#desc [/codeblocks]
#desc This is the same as [method set_pixelv], but with a two integer arguments instead of a [Vector2i] argument.
func set_pixel(x: int, y: int, color: Color) -> void:
	pass;

#desc Sets the [Color] of the pixel at [param point] to [param color]. Example:
#desc [codeblocks]
#desc [gdscript]
#desc var img_width = 10
#desc var img_height = 5
#desc var img = Image.new()
#desc img.create(img_width, img_height, false, Image.FORMAT_RGBA8)
#desc 
#desc img.set_pixelv(Vector2i(1, 2), Color.red) # Sets the color at (1, 2) to red.
#desc [/gdscript]
#desc [csharp]
#desc int imgWidth = 10;
#desc int imgHeight = 5;
#desc var img = new Image();
#desc img.Create(imgWidth, imgHeight, false, Image.Format.Rgba8);
#desc 
#desc img.SetPixelv(new Vector2i(1, 2), Colors.Red); // Sets the color at (1, 2) to red.
#desc [/csharp]
#desc [/codeblocks]
#desc This is the same as [method set_pixel], but with a [Vector2i] argument instead of two integer arguments.
func set_pixelv(point: Vector2i, color: Color) -> void:
	pass;

#desc Shrinks the image by a factor of 2.
func shrink_x2() -> void:
	pass;

#desc Converts the raw data from the sRGB colorspace to a linear scale.
func srgb_to_linear() -> void:
	pass;


