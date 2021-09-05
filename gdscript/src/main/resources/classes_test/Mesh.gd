extends Resource
class_name Mesh

const PRIMITIVE_POINTS = 0;
const PRIMITIVE_LINES = 1;
const PRIMITIVE_LINE_STRIP = 2;
const PRIMITIVE_TRIANGLES = 3;
const PRIMITIVE_TRIANGLE_STRIP = 4;
const ARRAY_VERTEX = 0;
const ARRAY_NORMAL = 1;
const ARRAY_TANGENT = 2;
const ARRAY_COLOR = 3;
const ARRAY_TEX_UV = 4;
const ARRAY_TEX_UV2 = 5;
const ARRAY_CUSTOM0 = 6;
const ARRAY_CUSTOM1 = 7;
const ARRAY_CUSTOM2 = 8;
const ARRAY_CUSTOM3 = 9;
const ARRAY_BONES = 10;
const ARRAY_WEIGHTS = 11;
const ARRAY_INDEX = 12;
const ARRAY_MAX = 13;
const ARRAY_CUSTOM_RGBA8_UNORM = 0;
const ARRAY_CUSTOM_RGBA8_SNORM = 1;
const ARRAY_CUSTOM_RG_HALF = 2;
const ARRAY_CUSTOM_RGBA_HALF = 3;
const ARRAY_CUSTOM_R_FLOAT = 4;
const ARRAY_CUSTOM_RG_FLOAT = 5;
const ARRAY_CUSTOM_RGB_FLOAT = 6;
const ARRAY_CUSTOM_RGBA_FLOAT = 7;
const ARRAY_CUSTOM_MAX = 8;
const ARRAY_FORMAT_VERTEX = 1;
const ARRAY_FORMAT_NORMAL = 2;
const ARRAY_FORMAT_TANGENT = 4;
const ARRAY_FORMAT_COLOR = 8;
const ARRAY_FORMAT_TEX_UV = 16;
const ARRAY_FORMAT_TEX_UV2 = 32;
const ARRAY_FORMAT_CUSTOM0 = 64;
const ARRAY_FORMAT_CUSTOM1 = 128;
const ARRAY_FORMAT_CUSTOM2 = 256;
const ARRAY_FORMAT_CUSTOM3 = 512;
const ARRAY_FORMAT_BONES = 1024;
const ARRAY_FORMAT_WEIGHTS = 2048;
const ARRAY_FORMAT_INDEX = 4096;
const ARRAY_FORMAT_BLEND_SHAPE_MASK = 2147475463;
const ARRAY_FORMAT_CUSTOM_BASE = 13;
const ARRAY_FORMAT_CUSTOM0_SHIFT = 13;
const ARRAY_FORMAT_CUSTOM1_SHIFT = 16;
const ARRAY_FORMAT_CUSTOM2_SHIFT = 19;
const ARRAY_FORMAT_CUSTOM3_SHIFT = 22;
const ARRAY_FORMAT_CUSTOM_MASK = 7;
const ARRAY_COMPRESS_FLAGS_BASE = 25;
const ARRAY_FLAG_USE_2D_VERTICES = 33554432;
const ARRAY_FLAG_USE_DYNAMIC_UPDATE = 67108864;
const ARRAY_FLAG_USE_8_BONE_WEIGHTS = 134217728;
const BLEND_SHAPE_MODE_NORMALIZED = 0;
const BLEND_SHAPE_MODE_RELATIVE = 1;

var lightmap_size_hint: Vector2i setget set_lightmap_size_hint, get_lightmap_size_hint;

func create_convex_shape(clean: bool, simplify: bool) -> Shape3D:
    pass;

func create_outline(margin: float) -> Mesh:
    pass;

func create_trimesh_shape() -> Shape3D:
    pass;

func generate_triangle_mesh() -> TriangleMesh:
    pass;

func get_aabb() -> AABB:
    pass;

func get_faces() -> PackedVector3Array:
    pass;

func get_surface_count() -> int:
    pass;

func surface_get_arrays(surf_idx: int) -> Array:
    pass;

func surface_get_blend_shape_arrays(surf_idx: int) -> Array:
    pass;

func surface_get_material(surf_idx: int) -> Material:
    pass;

func surface_set_material(surf_idx: int, material: Material) -> void:
    pass;

