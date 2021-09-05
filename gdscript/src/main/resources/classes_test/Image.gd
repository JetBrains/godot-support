extends Resource
class_name Image

const MAX_WIDTH = 16777216;
const MAX_HEIGHT = 16777216;
const FORMAT_L8 = 0;
const FORMAT_LA8 = 1;
const FORMAT_R8 = 2;
const FORMAT_RG8 = 3;
const FORMAT_RGB8 = 4;
const FORMAT_RGBA8 = 5;
const FORMAT_RGBA4444 = 6;
const FORMAT_RGB565 = 7;
const FORMAT_RF = 8;
const FORMAT_RGF = 9;
const FORMAT_RGBF = 10;
const FORMAT_RGBAF = 11;
const FORMAT_RH = 12;
const FORMAT_RGH = 13;
const FORMAT_RGBH = 14;
const FORMAT_RGBAH = 15;
const FORMAT_RGBE9995 = 16;
const FORMAT_DXT1 = 17;
const FORMAT_DXT3 = 18;
const FORMAT_DXT5 = 19;
const FORMAT_RGTC_R = 20;
const FORMAT_RGTC_RG = 21;
const FORMAT_BPTC_RGBA = 22;
const FORMAT_BPTC_RGBF = 23;
const FORMAT_BPTC_RGBFU = 24;
const FORMAT_PVRTC1_2 = 25;
const FORMAT_PVRTC1_2A = 26;
const FORMAT_PVRTC1_4 = 27;
const FORMAT_PVRTC1_4A = 28;
const FORMAT_ETC = 29;
const FORMAT_ETC2_R11 = 30;
const FORMAT_ETC2_R11S = 31;
const FORMAT_ETC2_RG11 = 32;
const FORMAT_ETC2_RG11S = 33;
const FORMAT_ETC2_RGB8 = 34;
const FORMAT_ETC2_RGBA8 = 35;
const FORMAT_ETC2_RGB8A1 = 36;
const FORMAT_ETC2_RA_AS_RG = 37;
const FORMAT_DXT5_RA_AS_RG = 38;
const FORMAT_MAX = 39;
const INTERPOLATE_NEAREST = 0;
const INTERPOLATE_BILINEAR = 1;
const INTERPOLATE_CUBIC = 2;
const INTERPOLATE_TRILINEAR = 3;
const INTERPOLATE_LANCZOS = 4;
const ALPHA_NONE = 0;
const ALPHA_BIT = 1;
const ALPHA_BLEND = 2;
const COMPRESS_S3TC = 0;
const COMPRESS_PVRTC1_4 = 1;
const COMPRESS_ETC = 2;
const COMPRESS_ETC2 = 3;
const COMPRESS_BPTC = 4;
const USED_CHANNELS_L = 0;
const USED_CHANNELS_LA = 1;
const USED_CHANNELS_R = 2;
const USED_CHANNELS_RG = 3;
const USED_CHANNELS_RGB = 4;
const USED_CHANNELS_RGBA = 5;
const COMPRESS_SOURCE_GENERIC = 0;
const COMPRESS_SOURCE_SRGB = 1;
const COMPRESS_SOURCE_NORMAL = 2;

var data: Dictionary setget _set_data, _get_data;

func adjust_bcs(brightness: float, contrast: float, saturation: float) -> void:
    pass;

func blend_rect(src: Image, src_rect: Rect2, dst: Vector2) -> void:
    pass;

func blend_rect_mask(src: Image, mask: Image, src_rect: Rect2, dst: Vector2) -> void:
    pass;

func blit_rect(src: Image, src_rect: Rect2, dst: Vector2) -> void:
    pass;

func blit_rect_mask(src: Image, mask: Image, src_rect: Rect2, dst: Vector2) -> void:
    pass;

func bump_map_to_normal_map(bump_scale: float) -> void:
    pass;

func clear_mipmaps() -> void:
    pass;

func compress(mode: int, source: int, lossy_quality: float) -> int:
    pass;

func compress_from_channels(mode: int, channels: int, lossy_quality: float) -> int:
    pass;

func convert(format: int) -> void:
    pass;

func copy_from(src: Image) -> void:
    pass;

func create(width: int, height: int, use_mipmaps: bool, format: int) -> void:
    pass;

func create_from_data(width: int, height: int, use_mipmaps: bool, format: int, data: PackedByteArray) -> void:
    pass;

func crop(width: int, height: int) -> void:
    pass;

func decompress() -> int:
    pass;

func detect_alpha() -> int:
    pass;

func detect_used_channels(source: int) -> int:
    pass;

func fill(color: Color) -> void:
    pass;

func fix_alpha_edges() -> void:
    pass;

func flip_x() -> void:
    pass;

func flip_y() -> void:
    pass;

func generate_mipmaps(renormalize: bool) -> int:
    pass;

func get_data() -> PackedByteArray:
    pass;

func get_format() -> int:
    pass;

func get_height() -> int:
    pass;

func get_mipmap_offset(mipmap: int) -> int:
    pass;

func get_pixel(x: int, y: int) -> Color:
    pass;

func get_pixelv(point: Vector2i) -> Color:
    pass;

func get_rect(rect: Rect2) -> Image:
    pass;

func get_size() -> Vector2:
    pass;

func get_used_rect() -> Rect2:
    pass;

func get_width() -> int:
    pass;

func has_mipmaps() -> bool:
    pass;

func is_compressed() -> bool:
    pass;

func is_empty() -> bool:
    pass;

func is_invisible() -> bool:
    pass;

func load(path: String) -> int:
    pass;

func load_bmp_from_buffer(buffer: PackedByteArray) -> int:
    pass;

func load_jpg_from_buffer(buffer: PackedByteArray) -> int:
    pass;

func load_png_from_buffer(buffer: PackedByteArray) -> int:
    pass;

func load_tga_from_buffer(buffer: PackedByteArray) -> int:
    pass;

func load_webp_from_buffer(buffer: PackedByteArray) -> int:
    pass;

func normal_map_to_xy() -> void:
    pass;

func premultiply_alpha() -> void:
    pass;

func resize(width: int, height: int, interpolation: int) -> void:
    pass;

func resize_to_po2(square: bool, interpolation: int) -> void:
    pass;

func rgbe_to_srgb() -> Image:
    pass;

func save_exr(path: String, grayscale: bool) -> int:
    pass;

func save_png(path: String) -> int:
    pass;

func save_png_to_buffer() -> PackedByteArray:
    pass;

func set_pixel(x: int, y: int, color: Color) -> void:
    pass;

func set_pixelv(point: Vector2i, color: Color) -> void:
    pass;

func shrink_x2() -> void:
    pass;

func srgb_to_linear() -> void:
    pass;

