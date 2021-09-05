extends Object
class_name RenderingServer

const NO_INDEX_ARRAY = -1;
const ARRAY_WEIGHTS_SIZE = 4;
const CANVAS_ITEM_Z_MIN = -4096;
const CANVAS_ITEM_Z_MAX = 4096;
const MAX_GLOW_LEVELS = 7;
const MAX_CURSORS = 8;
const MAX_2D_DIRECTIONAL_LIGHTS = 8;
const TEXTURE_LAYERED_2D_ARRAY = 0;
const TEXTURE_LAYERED_CUBEMAP = 1;
const TEXTURE_LAYERED_CUBEMAP_ARRAY = 2;
const CUBEMAP_LAYER_LEFT = 0;
const CUBEMAP_LAYER_RIGHT = 1;
const CUBEMAP_LAYER_BOTTOM = 2;
const CUBEMAP_LAYER_TOP = 3;
const CUBEMAP_LAYER_FRONT = 4;
const CUBEMAP_LAYER_BACK = 5;
const SHADER_SPATIAL = 0;
const SHADER_CANVAS_ITEM = 1;
const SHADER_PARTICLES = 2;
const SHADER_SKY = 3;
const SHADER_MAX = 4;
const MATERIAL_RENDER_PRIORITY_MIN = -128;
const MATERIAL_RENDER_PRIORITY_MAX = 127;
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
const ARRAY_CUSTOM_COUNT = 4;
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
const ARRAY_FORMAT_CUSTOM_BITS = 3;
const ARRAY_FORMAT_CUSTOM0_SHIFT = 13;
const ARRAY_FORMAT_CUSTOM1_SHIFT = 16;
const ARRAY_FORMAT_CUSTOM2_SHIFT = 19;
const ARRAY_FORMAT_CUSTOM3_SHIFT = 22;
const ARRAY_FORMAT_CUSTOM_MASK = 7;
const ARRAY_COMPRESS_FLAGS_BASE = 25;
const ARRAY_FLAG_USE_2D_VERTICES = 33554432;
const ARRAY_FLAG_USE_DYNAMIC_UPDATE = 67108864;
const ARRAY_FLAG_USE_8_BONE_WEIGHTS = 134217728;
const PRIMITIVE_POINTS = 0;
const PRIMITIVE_LINES = 1;
const PRIMITIVE_LINE_STRIP = 2;
const PRIMITIVE_TRIANGLES = 3;
const PRIMITIVE_TRIANGLE_STRIP = 4;
const PRIMITIVE_MAX = 5;
const BLEND_SHAPE_MODE_NORMALIZED = 0;
const BLEND_SHAPE_MODE_RELATIVE = 1;
const MULTIMESH_TRANSFORM_2D = 0;
const MULTIMESH_TRANSFORM_3D = 1;
const LIGHT_DIRECTIONAL = 0;
const LIGHT_OMNI = 1;
const LIGHT_SPOT = 2;
const LIGHT_PARAM_ENERGY = 0;
const LIGHT_PARAM_INDIRECT_ENERGY = 1;
const LIGHT_PARAM_SPECULAR = 2;
const LIGHT_PARAM_RANGE = 3;
const LIGHT_PARAM_SIZE = 4;
const LIGHT_PARAM_ATTENUATION = 5;
const LIGHT_PARAM_SPOT_ANGLE = 6;
const LIGHT_PARAM_SPOT_ATTENUATION = 7;
const LIGHT_PARAM_SHADOW_MAX_DISTANCE = 8;
const LIGHT_PARAM_SHADOW_SPLIT_1_OFFSET = 9;
const LIGHT_PARAM_SHADOW_SPLIT_2_OFFSET = 10;
const LIGHT_PARAM_SHADOW_SPLIT_3_OFFSET = 11;
const LIGHT_PARAM_SHADOW_FADE_START = 12;
const LIGHT_PARAM_SHADOW_NORMAL_BIAS = 13;
const LIGHT_PARAM_SHADOW_BIAS = 14;
const LIGHT_PARAM_SHADOW_PANCAKE_SIZE = 15;
const LIGHT_PARAM_SHADOW_BLUR = 16;
const LIGHT_PARAM_SHADOW_VOLUMETRIC_FOG_FADE = 17;
const LIGHT_PARAM_TRANSMITTANCE_BIAS = 18;
const LIGHT_PARAM_MAX = 19;
const LIGHT_BAKE_DISABLED = 0;
const LIGHT_BAKE_DYNAMIC = 1;
const LIGHT_BAKE_STATIC = 2;
const LIGHT_OMNI_SHADOW_DUAL_PARABOLOID = 0;
const LIGHT_OMNI_SHADOW_CUBE = 1;
const LIGHT_DIRECTIONAL_SHADOW_ORTHOGONAL = 0;
const LIGHT_DIRECTIONAL_SHADOW_PARALLEL_2_SPLITS = 1;
const LIGHT_DIRECTIONAL_SHADOW_PARALLEL_4_SPLITS = 2;
const SHADOW_QUALITY_HARD = 0;
const SHADOW_QUALITY_SOFT_LOW = 1;
const SHADOW_QUALITY_SOFT_MEDIUM = 2;
const SHADOW_QUALITY_SOFT_HIGH = 3;
const SHADOW_QUALITY_SOFT_ULTRA = 4;
const SHADOW_QUALITY_MAX = 5;
const REFLECTION_PROBE_UPDATE_ONCE = 0;
const REFLECTION_PROBE_UPDATE_ALWAYS = 1;
const REFLECTION_PROBE_AMBIENT_DISABLED = 0;
const REFLECTION_PROBE_AMBIENT_ENVIRONMENT = 1;
const REFLECTION_PROBE_AMBIENT_COLOR = 2;
const DECAL_TEXTURE_ALBEDO = 0;
const DECAL_TEXTURE_NORMAL = 1;
const DECAL_TEXTURE_ORM = 2;
const DECAL_TEXTURE_EMISSION = 3;
const DECAL_TEXTURE_MAX = 4;
const VOXEL_GI_QUALITY_LOW = 0;
const VOXEL_GI_QUALITY_HIGH = 1;
const PARTICLES_MODE_2D = 0;
const PARTICLES_MODE_3D = 1;
const PARTICLES_TRANSFORM_ALIGN_DISABLED = 0;
const PARTICLES_TRANSFORM_ALIGN_Z_BILLBOARD = 1;
const PARTICLES_TRANSFORM_ALIGN_Y_TO_VELOCITY = 2;
const PARTICLES_TRANSFORM_ALIGN_Z_BILLBOARD_Y_TO_VELOCITY = 3;
const PARTICLES_EMIT_FLAG_POSITION = 1;
const PARTICLES_EMIT_FLAG_ROTATION_SCALE = 2;
const PARTICLES_EMIT_FLAG_VELOCITY = 4;
const PARTICLES_EMIT_FLAG_COLOR = 8;
const PARTICLES_EMIT_FLAG_CUSTOM = 16;
const PARTICLES_DRAW_ORDER_INDEX = 0;
const PARTICLES_DRAW_ORDER_LIFETIME = 1;
const PARTICLES_DRAW_ORDER_REVERSE_LIFETIME = 2;
const PARTICLES_DRAW_ORDER_VIEW_DEPTH = 3;
const PARTICLES_COLLISION_TYPE_SPHERE_ATTRACT = 0;
const PARTICLES_COLLISION_TYPE_BOX_ATTRACT = 1;
const PARTICLES_COLLISION_TYPE_VECTOR_FIELD_ATTRACT = 2;
const PARTICLES_COLLISION_TYPE_SPHERE_COLLIDE = 3;
const PARTICLES_COLLISION_TYPE_BOX_COLLIDE = 4;
const PARTICLES_COLLISION_TYPE_SDF_COLLIDE = 5;
const PARTICLES_COLLISION_TYPE_HEIGHTFIELD_COLLIDE = 6;
const PARTICLES_COLLISION_HEIGHTFIELD_RESOLUTION_256 = 0;
const PARTICLES_COLLISION_HEIGHTFIELD_RESOLUTION_512 = 1;
const PARTICLES_COLLISION_HEIGHTFIELD_RESOLUTION_1024 = 2;
const PARTICLES_COLLISION_HEIGHTFIELD_RESOLUTION_2048 = 3;
const PARTICLES_COLLISION_HEIGHTFIELD_RESOLUTION_4096 = 4;
const PARTICLES_COLLISION_HEIGHTFIELD_RESOLUTION_8192 = 5;
const PARTICLES_COLLISION_HEIGHTFIELD_RESOLUTION_MAX = 6;
const VIEWPORT_UPDATE_DISABLED = 0;
const VIEWPORT_UPDATE_ONCE = 1;
const VIEWPORT_UPDATE_WHEN_VISIBLE = 2;
const VIEWPORT_UPDATE_WHEN_PARENT_VISIBLE = 3;
const VIEWPORT_UPDATE_ALWAYS = 4;
const VIEWPORT_CLEAR_ALWAYS = 0;
const VIEWPORT_CLEAR_NEVER = 1;
const VIEWPORT_CLEAR_ONLY_NEXT_FRAME = 2;
const VIEWPORT_SDF_OVERSIZE_100_PERCENT = 0;
const VIEWPORT_SDF_OVERSIZE_120_PERCENT = 1;
const VIEWPORT_SDF_OVERSIZE_150_PERCENT = 2;
const VIEWPORT_SDF_OVERSIZE_200_PERCENT = 3;
const VIEWPORT_SDF_OVERSIZE_MAX = 4;
const VIEWPORT_SDF_SCALE_100_PERCENT = 0;
const VIEWPORT_SDF_SCALE_50_PERCENT = 1;
const VIEWPORT_SDF_SCALE_25_PERCENT = 2;
const VIEWPORT_SDF_SCALE_MAX = 3;
const VIEWPORT_MSAA_DISABLED = 0;
const VIEWPORT_MSAA_2X = 1;
const VIEWPORT_MSAA_4X = 2;
const VIEWPORT_MSAA_8X = 3;
const VIEWPORT_MSAA_16X = 4;
const VIEWPORT_MSAA_MAX = 5;
const VIEWPORT_SCREEN_SPACE_AA_DISABLED = 0;
const VIEWPORT_SCREEN_SPACE_AA_FXAA = 1;
const VIEWPORT_SCREEN_SPACE_AA_MAX = 2;
const VIEWPORT_OCCLUSION_BUILD_QUALITY_LOW = 0;
const VIEWPORT_OCCLUSION_BUILD_QUALITY_MEDIUM = 1;
const VIEWPORT_OCCLUSION_BUILD_QUALITY_HIGH = 2;
const VIEWPORT_RENDER_INFO_OBJECTS_IN_FRAME = 0;
const VIEWPORT_RENDER_INFO_PRIMITIVES_IN_FRAME = 1;
const VIEWPORT_RENDER_INFO_DRAW_CALLS_IN_FRAME = 2;
const VIEWPORT_RENDER_INFO_MAX = 3;
const VIEWPORT_RENDER_INFO_TYPE_VISIBLE = 0;
const VIEWPORT_RENDER_INFO_TYPE_SHADOW = 1;
const VIEWPORT_RENDER_INFO_TYPE_MAX = 2;
const VIEWPORT_DEBUG_DRAW_DISABLED = 0;
const VIEWPORT_DEBUG_DRAW_UNSHADED = 1;
const VIEWPORT_DEBUG_DRAW_LIGHTING = 2;
const VIEWPORT_DEBUG_DRAW_OVERDRAW = 3;
const VIEWPORT_DEBUG_DRAW_WIREFRAME = 4;
const VIEWPORT_DEBUG_DRAW_NORMAL_BUFFER = 5;
const VIEWPORT_DEBUG_DRAW_VOXEL_GI_ALBEDO = 6;
const VIEWPORT_DEBUG_DRAW_VOXEL_GI_LIGHTING = 7;
const VIEWPORT_DEBUG_DRAW_VOXEL_GI_EMISSION = 8;
const VIEWPORT_DEBUG_DRAW_SHADOW_ATLAS = 9;
const VIEWPORT_DEBUG_DRAW_DIRECTIONAL_SHADOW_ATLAS = 10;
const VIEWPORT_DEBUG_DRAW_SCENE_LUMINANCE = 11;
const VIEWPORT_DEBUG_DRAW_SSAO = 12;
const VIEWPORT_DEBUG_DRAW_PSSM_SPLITS = 13;
const VIEWPORT_DEBUG_DRAW_DECAL_ATLAS = 14;
const VIEWPORT_DEBUG_DRAW_SDFGI = 15;
const VIEWPORT_DEBUG_DRAW_SDFGI_PROBES = 16;
const VIEWPORT_DEBUG_DRAW_GI_BUFFER = 17;
const VIEWPORT_DEBUG_DRAW_DISABLE_LOD = 18;
const VIEWPORT_DEBUG_DRAW_CLUSTER_OMNI_LIGHTS = 19;
const VIEWPORT_DEBUG_DRAW_CLUSTER_SPOT_LIGHTS = 20;
const VIEWPORT_DEBUG_DRAW_CLUSTER_DECALS = 21;
const VIEWPORT_DEBUG_DRAW_CLUSTER_REFLECTION_PROBES = 22;
const VIEWPORT_DEBUG_DRAW_OCCLUDERS = 23;
const SKY_MODE_AUTOMATIC = 0;
const SKY_MODE_QUALITY = 1;
const SKY_MODE_INCREMENTAL = 2;
const SKY_MODE_REALTIME = 3;
const ENV_BG_CLEAR_COLOR = 0;
const ENV_BG_COLOR = 1;
const ENV_BG_SKY = 2;
const ENV_BG_CANVAS = 3;
const ENV_BG_KEEP = 4;
const ENV_BG_CAMERA_FEED = 5;
const ENV_BG_MAX = 6;
const ENV_AMBIENT_SOURCE_BG = 0;
const ENV_AMBIENT_SOURCE_DISABLED = 1;
const ENV_AMBIENT_SOURCE_COLOR = 2;
const ENV_AMBIENT_SOURCE_SKY = 3;
const ENV_REFLECTION_SOURCE_BG = 0;
const ENV_REFLECTION_SOURCE_DISABLED = 1;
const ENV_REFLECTION_SOURCE_SKY = 2;
const ENV_GLOW_BLEND_MODE_ADDITIVE = 0;
const ENV_GLOW_BLEND_MODE_SCREEN = 1;
const ENV_GLOW_BLEND_MODE_SOFTLIGHT = 2;
const ENV_GLOW_BLEND_MODE_REPLACE = 3;
const ENV_GLOW_BLEND_MODE_MIX = 4;
const ENV_TONE_MAPPER_LINEAR = 0;
const ENV_TONE_MAPPER_REINHARD = 1;
const ENV_TONE_MAPPER_FILMIC = 2;
const ENV_TONE_MAPPER_ACES = 3;
const ENV_SSR_ROUGNESS_QUALITY_DISABLED = 0;
const ENV_SSR_ROUGNESS_QUALITY_LOW = 1;
const ENV_SSR_ROUGNESS_QUALITY_MEDIUM = 2;
const ENV_SSR_ROUGNESS_QUALITY_HIGH = 3;
const ENV_SSAO_QUALITY_VERY_LOW = 0;
const ENV_SSAO_QUALITY_LOW = 1;
const ENV_SSAO_QUALITY_MEDIUM = 2;
const ENV_SSAO_QUALITY_HIGH = 3;
const ENV_SSAO_QUALITY_ULTRA = 4;
const ENV_SDFGI_CASCADES_4 = 0;
const ENV_SDFGI_CASCADES_6 = 1;
const ENV_SDFGI_CASCADES_8 = 2;
const ENV_SDFGI_Y_SCALE_DISABLED = 0;
const ENV_SDFGI_Y_SCALE_75_PERCENT = 1;
const ENV_SDFGI_Y_SCALE_50_PERCENT = 2;
const ENV_SDFGI_RAY_COUNT_4 = 0;
const ENV_SDFGI_RAY_COUNT_8 = 1;
const ENV_SDFGI_RAY_COUNT_16 = 2;
const ENV_SDFGI_RAY_COUNT_32 = 3;
const ENV_SDFGI_RAY_COUNT_64 = 4;
const ENV_SDFGI_RAY_COUNT_96 = 5;
const ENV_SDFGI_RAY_COUNT_128 = 6;
const ENV_SDFGI_RAY_COUNT_MAX = 7;
const ENV_SDFGI_CONVERGE_IN_5_FRAMES = 0;
const ENV_SDFGI_CONVERGE_IN_10_FRAMES = 1;
const ENV_SDFGI_CONVERGE_IN_15_FRAMES = 2;
const ENV_SDFGI_CONVERGE_IN_20_FRAMES = 3;
const ENV_SDFGI_CONVERGE_IN_25_FRAMES = 4;
const ENV_SDFGI_CONVERGE_IN_30_FRAMES = 5;
const ENV_SDFGI_CONVERGE_MAX = 6;
const ENV_SDFGI_UPDATE_LIGHT_IN_1_FRAME = 0;
const ENV_SDFGI_UPDATE_LIGHT_IN_2_FRAMES = 1;
const ENV_SDFGI_UPDATE_LIGHT_IN_4_FRAMES = 2;
const ENV_SDFGI_UPDATE_LIGHT_IN_8_FRAMES = 3;
const ENV_SDFGI_UPDATE_LIGHT_IN_16_FRAMES = 4;
const ENV_SDFGI_UPDATE_LIGHT_MAX = 5;
const SUB_SURFACE_SCATTERING_QUALITY_DISABLED = 0;
const SUB_SURFACE_SCATTERING_QUALITY_LOW = 1;
const SUB_SURFACE_SCATTERING_QUALITY_MEDIUM = 2;
const SUB_SURFACE_SCATTERING_QUALITY_HIGH = 3;
const DOF_BOKEH_BOX = 0;
const DOF_BOKEH_HEXAGON = 1;
const DOF_BOKEH_CIRCLE = 2;
const DOF_BLUR_QUALITY_VERY_LOW = 0;
const DOF_BLUR_QUALITY_LOW = 1;
const DOF_BLUR_QUALITY_MEDIUM = 2;
const DOF_BLUR_QUALITY_HIGH = 3;
const INSTANCE_NONE = 0;
const INSTANCE_MESH = 1;
const INSTANCE_MULTIMESH = 2;
const INSTANCE_PARTICLES = 3;
const INSTANCE_PARTICLES_COLLISION = 4;
const INSTANCE_LIGHT = 5;
const INSTANCE_REFLECTION_PROBE = 6;
const INSTANCE_DECAL = 7;
const INSTANCE_VOXEL_GI = 8;
const INSTANCE_LIGHTMAP = 9;
const INSTANCE_OCCLUDER = 10;
const INSTANCE_VISIBLITY_NOTIFIER = 11;
const INSTANCE_MAX = 12;
const INSTANCE_GEOMETRY_MASK = 14;
const INSTANCE_FLAG_USE_BAKED_LIGHT = 0;
const INSTANCE_FLAG_USE_DYNAMIC_GI = 1;
const INSTANCE_FLAG_DRAW_NEXT_FRAME_IF_VISIBLE = 2;
const INSTANCE_FLAG_IGNORE_OCCLUSION_CULLING = 3;
const INSTANCE_FLAG_MAX = 4;
const SHADOW_CASTING_SETTING_OFF = 0;
const SHADOW_CASTING_SETTING_ON = 1;
const SHADOW_CASTING_SETTING_DOUBLE_SIDED = 2;
const SHADOW_CASTING_SETTING_SHADOWS_ONLY = 3;
const BAKE_CHANNEL_ALBEDO_ALPHA = 0;
const BAKE_CHANNEL_NORMAL = 1;
const BAKE_CHANNEL_ORM = 2;
const BAKE_CHANNEL_EMISSION = 3;
const CANVAS_TEXTURE_CHANNEL_DIFFUSE = 0;
const CANVAS_TEXTURE_CHANNEL_NORMAL = 1;
const CANVAS_TEXTURE_CHANNEL_SPECULAR = 2;
const NINE_PATCH_STRETCH = 0;
const NINE_PATCH_TILE = 1;
const NINE_PATCH_TILE_FIT = 2;
const CANVAS_ITEM_TEXTURE_FILTER_DEFAULT = 0;
const CANVAS_ITEM_TEXTURE_FILTER_NEAREST = 1;
const CANVAS_ITEM_TEXTURE_FILTER_LINEAR = 2;
const CANVAS_ITEM_TEXTURE_FILTER_NEAREST_WITH_MIPMAPS = 3;
const CANVAS_ITEM_TEXTURE_FILTER_LINEAR_WITH_MIPMAPS = 4;
const CANVAS_ITEM_TEXTURE_FILTER_NEAREST_WITH_MIPMAPS_ANISOTROPIC = 5;
const CANVAS_ITEM_TEXTURE_FILTER_LINEAR_WITH_MIPMAPS_ANISOTROPIC = 6;
const CANVAS_ITEM_TEXTURE_FILTER_MAX = 7;
const CANVAS_ITEM_TEXTURE_REPEAT_DEFAULT = 0;
const CANVAS_ITEM_TEXTURE_REPEAT_DISABLED = 1;
const CANVAS_ITEM_TEXTURE_REPEAT_ENABLED = 2;
const CANVAS_ITEM_TEXTURE_REPEAT_MIRROR = 3;
const CANVAS_ITEM_TEXTURE_REPEAT_MAX = 4;
const CANVAS_GROUP_MODE_DISABLED = 0;
const CANVAS_GROUP_MODE_OPAQUE = 1;
const CANVAS_GROUP_MODE_TRANSPARENT = 2;
const CANVAS_LIGHT_MODE_POINT = 0;
const CANVAS_LIGHT_MODE_DIRECTIONAL = 1;
const CANVAS_LIGHT_BLEND_MODE_ADD = 0;
const CANVAS_LIGHT_BLEND_MODE_SUB = 1;
const CANVAS_LIGHT_BLEND_MODE_MIX = 2;
const CANVAS_LIGHT_FILTER_NONE = 0;
const CANVAS_LIGHT_FILTER_PCF5 = 1;
const CANVAS_LIGHT_FILTER_PCF13 = 2;
const CANVAS_LIGHT_FILTER_MAX = 3;
const CANVAS_OCCLUDER_POLYGON_CULL_DISABLED = 0;
const CANVAS_OCCLUDER_POLYGON_CULL_CLOCKWISE = 1;
const CANVAS_OCCLUDER_POLYGON_CULL_COUNTER_CLOCKWISE = 2;
const GLOBAL_VAR_TYPE_BOOL = 0;
const GLOBAL_VAR_TYPE_BVEC2 = 1;
const GLOBAL_VAR_TYPE_BVEC3 = 2;
const GLOBAL_VAR_TYPE_BVEC4 = 3;
const GLOBAL_VAR_TYPE_INT = 4;
const GLOBAL_VAR_TYPE_IVEC2 = 5;
const GLOBAL_VAR_TYPE_IVEC3 = 6;
const GLOBAL_VAR_TYPE_IVEC4 = 7;
const GLOBAL_VAR_TYPE_RECT2I = 8;
const GLOBAL_VAR_TYPE_UINT = 9;
const GLOBAL_VAR_TYPE_UVEC2 = 10;
const GLOBAL_VAR_TYPE_UVEC3 = 11;
const GLOBAL_VAR_TYPE_UVEC4 = 12;
const GLOBAL_VAR_TYPE_FLOAT = 13;
const GLOBAL_VAR_TYPE_VEC2 = 14;
const GLOBAL_VAR_TYPE_VEC3 = 15;
const GLOBAL_VAR_TYPE_VEC4 = 16;
const GLOBAL_VAR_TYPE_COLOR = 17;
const GLOBAL_VAR_TYPE_RECT2 = 18;
const GLOBAL_VAR_TYPE_MAT2 = 19;
const GLOBAL_VAR_TYPE_MAT3 = 20;
const GLOBAL_VAR_TYPE_MAT4 = 21;
const GLOBAL_VAR_TYPE_TRANSFORM_2D = 22;
const GLOBAL_VAR_TYPE_TRANSFORM = 23;
const GLOBAL_VAR_TYPE_SAMPLER2D = 24;
const GLOBAL_VAR_TYPE_SAMPLER2DARRAY = 25;
const GLOBAL_VAR_TYPE_SAMPLER3D = 26;
const GLOBAL_VAR_TYPE_SAMPLERCUBE = 27;
const GLOBAL_VAR_TYPE_MAX = 28;
const RENDERING_INFO_TOTAL_OBJECTS_IN_FRAME = 0;
const RENDERING_INFO_TOTAL_PRIMITIVES_IN_FRAME = 1;
const RENDERING_INFO_TOTAL_DRAW_CALLS_IN_FRAME = 2;
const RENDERING_INFO_TEXTURE_MEM_USED = 3;
const RENDERING_INFO_BUFFER_MEM_USED = 4;
const RENDERING_INFO_VIDEO_MEM_USED = 5;
const FEATURE_SHADERS = 0;
const FEATURE_MULTITHREADED = 1;

var render_loop_enabled: bool setget set_render_loop_enabled, is_render_loop_enabled;

func bake_render_uv2(base: RID, material_overrides: Array, image_size: Vector2i) -> Image[]:
    pass;

func camera_create() -> RID:
    pass;

func camera_effects_create() -> RID:
    pass;

func camera_effects_set_custom_exposure(camera_effects: RID, enable: bool, exposure: float) -> void:
    pass;

func camera_effects_set_dof_blur(camera_effects: RID, far_enable: bool, far_distance: float, far_transition: float, near_enable: bool, near_distance: float, near_transition: float, amount: float) -> void:
    pass;

func camera_effects_set_dof_blur_bokeh_shape(shape: int) -> void:
    pass;

func camera_effects_set_dof_blur_quality(quality: int, use_jitter: bool) -> void:
    pass;

func camera_set_camera_effects(camera: RID, effects: RID) -> void:
    pass;

func camera_set_cull_mask(camera: RID, layers: int) -> void:
    pass;

func camera_set_environment(camera: RID, env: RID) -> void:
    pass;

func camera_set_frustum(camera: RID, size: float, offset: Vector2, z_near: float, z_far: float) -> void:
    pass;

func camera_set_orthogonal(camera: RID, size: float, z_near: float, z_far: float) -> void:
    pass;

func camera_set_perspective(camera: RID, fovy_degrees: float, z_near: float, z_far: float) -> void:
    pass;

func camera_set_transform(camera: RID, transform: Transform3D) -> void:
    pass;

func camera_set_use_vertical_aspect(camera: RID, enable: bool) -> void:
    pass;

func canvas_create() -> RID:
    pass;

func canvas_item_add_circle(item: RID, pos: Vector2, radius: float, color: Color) -> void:
    pass;

func canvas_item_add_clip_ignore(item: RID, ignore: bool) -> void:
    pass;

func canvas_item_add_line(item: RID, from: Vector2, to: Vector2, color: Color, width: float) -> void:
    pass;

func canvas_item_add_mesh(item: RID, mesh: RID, transform: Transform2D, modulate: Color, texture: RID) -> void:
    pass;

func canvas_item_add_multimesh(item: RID, mesh: RID, texture: RID) -> void:
    pass;

func canvas_item_add_nine_patch(item: RID, rect: Rect2, source: Rect2, texture: RID, topleft: Vector2, bottomright: Vector2, x_axis_mode: int, y_axis_mode: int, draw_center: bool, modulate: Color) -> void:
    pass;

func canvas_item_add_particles(item: RID, particles: RID, texture: RID) -> void:
    pass;

func canvas_item_add_polygon(item: RID, points: PackedVector2Array, colors: PackedColorArray, uvs: PackedVector2Array, texture: RID) -> void:
    pass;

func canvas_item_add_polyline(item: RID, points: PackedVector2Array, colors: PackedColorArray, width: float, antialiased: bool) -> void:
    pass;

func canvas_item_add_primitive(item: RID, points: PackedVector2Array, colors: PackedColorArray, uvs: PackedVector2Array, texture: RID, width: float) -> void:
    pass;

func canvas_item_add_rect(item: RID, rect: Rect2, color: Color) -> void:
    pass;

func canvas_item_add_set_transform(item: RID, transform: Transform2D) -> void:
    pass;

func canvas_item_add_texture_rect(item: RID, rect: Rect2, texture: RID, tile: bool, modulate: Color, transpose: bool) -> void:
    pass;

func canvas_item_add_texture_rect_region(item: RID, rect: Rect2, texture: RID, src_rect: Rect2, modulate: Color, transpose: bool, clip_uv: bool) -> void:
    pass;

func canvas_item_add_triangle_array(item: RID, indices: PackedInt32Array, points: PackedVector2Array, colors: PackedColorArray, uvs: PackedVector2Array, bones: PackedInt32Array, weights: PackedFloat32Array, texture: RID, count: int) -> void:
    pass;

func canvas_item_clear(item: RID) -> void:
    pass;

func canvas_item_create() -> RID:
    pass;

func canvas_item_set_canvas_group_mode(item: RID, mode: int, clear_margin: float, fit_empty: bool, fit_margin: float, blur_mipmaps: bool) -> void:
    pass;

func canvas_item_set_clip(item: RID, clip: bool) -> void:
    pass;

func canvas_item_set_copy_to_backbuffer(item: RID, enabled: bool, rect: Rect2) -> void:
    pass;

func canvas_item_set_custom_rect(item: RID, use_custom_rect: bool, rect: Rect2) -> void:
    pass;

func canvas_item_set_default_texture_filter(item: RID, filter: int) -> void:
    pass;

func canvas_item_set_default_texture_repeat(item: RID, repeat: int) -> void:
    pass;

func canvas_item_set_distance_field_mode(item: RID, enabled: bool) -> void:
    pass;

func canvas_item_set_draw_behind_parent(item: RID, enabled: bool) -> void:
    pass;

func canvas_item_set_draw_index(item: RID, index: int) -> void:
    pass;

func canvas_item_set_light_mask(item: RID, mask: int) -> void:
    pass;

func canvas_item_set_material(item: RID, material: RID) -> void:
    pass;

func canvas_item_set_modulate(item: RID, color: Color) -> void:
    pass;

func canvas_item_set_parent(item: RID, parent: RID) -> void:
    pass;

func canvas_item_set_self_modulate(item: RID, color: Color) -> void:
    pass;

func canvas_item_set_sort_children_by_y(item: RID, enabled: bool) -> void:
    pass;

func canvas_item_set_transform(item: RID, transform: Transform2D) -> void:
    pass;

func canvas_item_set_use_parent_material(item: RID, enabled: bool) -> void:
    pass;

func canvas_item_set_visibility_notifier(item: RID, enable: bool, area: Rect2, enter_callable: Callable, exit_callable: Callable) -> void:
    pass;

func canvas_item_set_visible(item: RID, visible: bool) -> void:
    pass;

func canvas_item_set_z_as_relative_to_parent(item: RID, enabled: bool) -> void:
    pass;

func canvas_item_set_z_index(item: RID, z_index: int) -> void:
    pass;

func canvas_light_attach_to_canvas(light: RID, canvas: RID) -> void:
    pass;

func canvas_light_create() -> RID:
    pass;

func canvas_light_occluder_attach_to_canvas(occluder: RID, canvas: RID) -> void:
    pass;

func canvas_light_occluder_create() -> RID:
    pass;

func canvas_light_occluder_set_as_sdf_collision(occluder: RID, enable: bool) -> void:
    pass;

func canvas_light_occluder_set_enabled(occluder: RID, enabled: bool) -> void:
    pass;

func canvas_light_occluder_set_light_mask(occluder: RID, mask: int) -> void:
    pass;

func canvas_light_occluder_set_polygon(occluder: RID, polygon: RID) -> void:
    pass;

func canvas_light_occluder_set_transform(occluder: RID, transform: Transform2D) -> void:
    pass;

func canvas_light_set_color(light: RID, color: Color) -> void:
    pass;

func canvas_light_set_enabled(light: RID, enabled: bool) -> void:
    pass;

func canvas_light_set_energy(light: RID, energy: float) -> void:
    pass;

func canvas_light_set_height(light: RID, height: float) -> void:
    pass;

func canvas_light_set_item_cull_mask(light: RID, mask: int) -> void:
    pass;

func canvas_light_set_item_shadow_cull_mask(light: RID, mask: int) -> void:
    pass;

func canvas_light_set_layer_range(light: RID, min_layer: int, max_layer: int) -> void:
    pass;

func canvas_light_set_mode(light: RID, mode: int) -> void:
    pass;

func canvas_light_set_shadow_color(light: RID, color: Color) -> void:
    pass;

func canvas_light_set_shadow_enabled(light: RID, enabled: bool) -> void:
    pass;

func canvas_light_set_shadow_filter(light: RID, filter: int) -> void:
    pass;

func canvas_light_set_shadow_smooth(light: RID, smooth: float) -> void:
    pass;

func canvas_light_set_texture(light: RID, texture: RID) -> void:
    pass;

func canvas_light_set_texture_offset(light: RID, offset: Vector2) -> void:
    pass;

func canvas_light_set_texture_scale(light: RID, scale: float) -> void:
    pass;

func canvas_light_set_transform(light: RID, transform: Transform2D) -> void:
    pass;

func canvas_light_set_z_range(light: RID, min_z: int, max_z: int) -> void:
    pass;

func canvas_occluder_polygon_create() -> RID:
    pass;

func canvas_occluder_polygon_set_cull_mode(occluder_polygon: RID, mode: int) -> void:
    pass;

func canvas_occluder_polygon_set_shape(occluder_polygon: RID, shape: PackedVector2Array, closed: bool) -> void:
    pass;

func canvas_set_disable_scale(disable: bool) -> void:
    pass;

func canvas_set_item_mirroring(canvas: RID, item: RID, mirroring: Vector2) -> void:
    pass;

func canvas_set_modulate(canvas: RID, color: Color) -> void:
    pass;

func canvas_set_shadow_texture_size(size: int) -> void:
    pass;

func canvas_texture_create() -> RID:
    pass;

func canvas_texture_set_channel(canvas_texture: RID, channel: int, texture: RID) -> void:
    pass;

func canvas_texture_set_shading_parameters(canvas_texture: RID, base_color: Color, shininess: float) -> void:
    pass;

func canvas_texture_set_texture_filter(canvas_texture: RID, filter: int) -> void:
    pass;

func canvas_texture_set_texture_repeat(canvas_texture: RID, repeat: int) -> void:
    pass;

func create_local_rendering_device() -> RenderingDevice:
    pass;

func decal_create() -> RID:
    pass;

func decal_set_albedo_mix(decal: RID, albedo_mix: float) -> void:
    pass;

func decal_set_cull_mask(decal: RID, mask: int) -> void:
    pass;

func decal_set_distance_fade(decal: RID, enabled: bool, begin: float, length: float) -> void:
    pass;

func decal_set_emission_energy(decal: RID, energy: float) -> void:
    pass;

func decal_set_extents(decal: RID, extents: Vector3) -> void:
    pass;

func decal_set_fade(decal: RID, above: float, below: float) -> void:
    pass;

func decal_set_modulate(decal: RID, color: Color) -> void:
    pass;

func decal_set_normal_fade(decal: RID, fade: float) -> void:
    pass;

func decal_set_texture(decal: RID, type: int, texture: RID) -> void:
    pass;

func directional_light_create() -> RID:
    pass;

func directional_shadow_atlas_set_size(size: int, is_16bits: bool) -> void:
    pass;

func directional_shadow_quality_set(quality: int) -> void:
    pass;

func environment_bake_panorama(environment: RID, bake_irradiance: bool, size: Vector2i) -> Image:
    pass;

func environment_create() -> RID:
    pass;

func environment_glow_set_use_bicubic_upscale(enable: bool) -> void:
    pass;

func environment_glow_set_use_high_quality(enable: bool) -> void:
    pass;

func environment_set_adjustment(env: RID, enable: bool, brightness: float, contrast: float, saturation: float, use_1d_color_correction: bool, color_correction: RID) -> void:
    pass;

func environment_set_ambient_light(env: RID, color: Color, ambient: int, energy: float, sky_contibution: float, reflection_source: int, ao_color: Color) -> void:
    pass;

func environment_set_background(env: RID, bg: int) -> void:
    pass;

func environment_set_bg_color(env: RID, color: Color) -> void:
    pass;

func environment_set_bg_energy(env: RID, energy: float) -> void:
    pass;

func environment_set_canvas_max_layer(env: RID, max_layer: int) -> void:
    pass;

func environment_set_fog(env: RID, enable: bool, light_color: Color, light_energy: float, sun_scatter: float, density: float, height: float, height_density: float, aerial_perspective: float) -> void:
    pass;

func environment_set_glow(env: RID, enable: bool, levels: PackedFloat32Array, intensity: float, strength: float, mix: float, bloom_threshold: float, blend_mode: int, hdr_bleed_threshold: float, hdr_bleed_scale: float, hdr_luminance_cap: float) -> void:
    pass;

func environment_set_sdfgi(env: RID, enable: bool, cascades: int, min_cell_size: float, y_scale: int, use_occlusion: bool, bounce_feedback: float, read_sky: bool, energy: float, normal_bias: float, probe_bias: float) -> void:
    pass;

func environment_set_sdfgi_frames_to_converge(frames: int) -> void:
    pass;

func environment_set_sdfgi_frames_to_update_light(frames: int) -> void:
    pass;

func environment_set_sdfgi_ray_count(ray_count: int) -> void:
    pass;

func environment_set_sky(env: RID, sky: RID) -> void:
    pass;

func environment_set_sky_custom_fov(env: RID, scale: float) -> void:
    pass;

func environment_set_sky_orientation(env: RID, orientation: Basis) -> void:
    pass;

func environment_set_ssao(env: RID, enable: bool, radius: float, intensity: float, power: float, detail: float, horizon: float, sharpness: float, light_affect: float, ao_channel_affect: float) -> void:
    pass;

func environment_set_ssao_quality(quality: int, half_size: bool, adaptive_target: float, blur_passes: int, fadeout_from: float, fadeout_to: float) -> void:
    pass;

func environment_set_ssr(env: RID, enable: bool, max_steps: int, fade_in: float, fade_out: float, depth_tolerance: float) -> void:
    pass;

func environment_set_ssr_roughness_quality(quality: int) -> void:
    pass;

func environment_set_tonemap(env: RID, tone_mapper: int, exposure: float, white: float, auto_exposure: bool, min_luminance: float, max_luminance: float, auto_exp_speed: float, auto_exp_grey: float) -> void:
    pass;

func environment_set_volumetric_fog(env: RID, enable: bool, density: float, light: Color, light_energy: float, length: float, p_detail_spread: float, gi_inject: float, temporal_reprojection: bool, temporal_reprojection_amount: float) -> void:
    pass;

func environment_set_volumetric_fog_filter_active(active: bool) -> void:
    pass;

func environment_set_volumetric_fog_volume_size(size: int, depth: int) -> void:
    pass;

func force_draw(swap_buffers: bool, frame_step: float) -> void:
    pass;

func force_sync() -> void:
    pass;

func free_rid(rid: RID) -> void:
    pass;

func get_frame_setup_time_cpu() -> float:
    pass;

func get_rendering_info(info: int) -> int:
    pass;

func get_test_cube() -> RID:
    pass;

func get_test_texture() -> RID:
    pass;

func get_video_adapter_name() -> String:
    pass;

func get_video_adapter_vendor() -> String:
    pass;

func get_white_texture() -> RID:
    pass;

func global_variable_add(name: StringName, type: int, default_value: Variant) -> void:
    pass;

func global_variable_get(name: StringName) -> Variant:
    pass;

func global_variable_get_list() -> PackedStringArray:
    pass;

func global_variable_get_type(name: StringName) -> int:
    pass;

func global_variable_remove(name: StringName) -> void:
    pass;

func global_variable_set(name: StringName, value: Variant) -> void:
    pass;

func global_variable_set_override(name: StringName, value: Variant) -> void:
    pass;

func has_changed() -> bool:
    pass;

func has_feature(feature: int) -> bool:
    pass;

func has_os_feature(feature: String) -> bool:
    pass;

func instance_attach_object_instance_id(instance: RID, id: int) -> void:
    pass;

func instance_attach_skeleton(instance: RID, skeleton: RID) -> void:
    pass;

func instance_create() -> RID:
    pass;

func instance_create2(base: RID, scenario: RID) -> RID:
    pass;

func instance_geometry_get_shader_parameter(instance: RID, parameter: StringName) -> Variant:
    pass;

func instance_geometry_get_shader_parameter_default_value(instance: RID, parameter: StringName) -> Variant:
    pass;

func instance_geometry_get_shader_parameter_list(instance: RID) -> Array:
    pass;

func instance_geometry_set_cast_shadows_setting(instance: RID, shadow_casting_setting: int) -> void:
    pass;

func instance_geometry_set_flag(instance: RID, flag: int, enabled: bool) -> void:
    pass;

func instance_geometry_set_lightmap(instance: RID, lightmap: RID, lightmap_uv_scale: Rect2, lightmap_slice: int) -> void:
    pass;

func instance_geometry_set_lod_bias(instance: RID, lod_bias: float) -> void:
    pass;

func instance_geometry_set_material_override(instance: RID, material: RID) -> void:
    pass;

func instance_geometry_set_shader_parameter(instance: RID, parameter: StringName, value: Variant) -> void:
    pass;

func instance_geometry_set_visibility_range(instance: RID, min: float, max: float, min_margin: float, max_margin: float) -> void:
    pass;

func instance_set_base(instance: RID, base: RID) -> void:
    pass;

func instance_set_blend_shape_weight(instance: RID, shape: int, weight: float) -> void:
    pass;

func instance_set_custom_aabb(instance: RID, aabb: AABB) -> void:
    pass;

func instance_set_extra_visibility_margin(instance: RID, margin: float) -> void:
    pass;

func instance_set_layer_mask(instance: RID, mask: int) -> void:
    pass;

func instance_set_scenario(instance: RID, scenario: RID) -> void:
    pass;

func instance_set_surface_override_material(instance: RID, surface: int, material: RID) -> void:
    pass;

func instance_set_transform(instance: RID, transform: Transform3D) -> void:
    pass;

func instance_set_visibility_parent(instance: RID, parent: RID) -> void:
    pass;

func instance_set_visible(instance: RID, visible: bool) -> void:
    pass;

func instances_cull_aabb(aabb: AABB, scenario: RID) -> Array:
    pass;

func instances_cull_convex(convex: Array, scenario: RID) -> Array:
    pass;

func instances_cull_ray(from: Vector3, to: Vector3, scenario: RID) -> Array:
    pass;

func light_directional_set_blend_splits(light: RID, enable: bool) -> void:
    pass;

func light_directional_set_shadow_mode(light: RID, mode: int) -> void:
    pass;

func light_directional_set_sky_only(light: RID, enable: bool) -> void:
    pass;

func light_omni_set_shadow_mode(light: RID, mode: int) -> void:
    pass;

func light_set_bake_mode(light: RID, bake_mode: int) -> void:
    pass;

func light_set_color(light: RID, color: Color) -> void:
    pass;

func light_set_cull_mask(light: RID, mask: int) -> void:
    pass;

func light_set_max_sdfgi_cascade(light: RID, cascade: int) -> void:
    pass;

func light_set_negative(light: RID, enable: bool) -> void:
    pass;

func light_set_param(light: RID, param: int, value: float) -> void:
    pass;

func light_set_projector(light: RID, texture: RID) -> void:
    pass;

func light_set_reverse_cull_face_mode(light: RID, enabled: bool) -> void:
    pass;

func light_set_shadow(light: RID, enabled: bool) -> void:
    pass;

func light_set_shadow_color(light: RID, color: Color) -> void:
    pass;

func lightmap_create() -> RID:
    pass;

func lightmap_get_probe_capture_bsp_tree(lightmap: RID) -> PackedInt32Array:
    pass;

func lightmap_get_probe_capture_points(lightmap: RID) -> PackedVector3Array:
    pass;

func lightmap_get_probe_capture_sh(lightmap: RID) -> PackedColorArray:
    pass;

func lightmap_get_probe_capture_tetrahedra(lightmap: RID) -> PackedInt32Array:
    pass;

func lightmap_set_probe_bounds(lightmap: RID, bounds: AABB) -> void:
    pass;

func lightmap_set_probe_capture_data(lightmap: RID, points: PackedVector3Array, point_sh: PackedColorArray, tetrahedra: PackedInt32Array, bsp_tree: PackedInt32Array) -> void:
    pass;

func lightmap_set_probe_capture_update_speed(speed: float) -> void:
    pass;

func lightmap_set_probe_interior(lightmap: RID, interior: bool) -> void:
    pass;

func lightmap_set_textures(lightmap: RID, light: RID, uses_sh: bool) -> void:
    pass;

func make_sphere_mesh(latitudes: int, longitudes: int, radius: float) -> RID:
    pass;

func material_create() -> RID:
    pass;

func material_get_param(material: RID, parameter: StringName) -> Variant:
    pass;

func material_set_next_pass(material: RID, next_material: RID) -> void:
    pass;

func material_set_param(material: RID, parameter: StringName, value: Variant) -> void:
    pass;

func material_set_render_priority(material: RID, priority: int) -> void:
    pass;

func material_set_shader(shader_material: RID, shader: RID) -> void:
    pass;

func mesh_add_surface(mesh: RID, surface: Dictionary) -> void:
    pass;

func mesh_add_surface_from_arrays(mesh: RID, primitive: int, arrays: Array, blend_shapes: Array, lods: Dictionary, compress_format: int) -> void:
    pass;

func mesh_clear(mesh: RID) -> void:
    pass;

func mesh_create() -> RID:
    pass;

func mesh_create_from_surfaces(surfaces: Dictionary[], blend_shape_count: int) -> RID:
    pass;

func mesh_get_blend_shape_count(mesh: RID) -> int:
    pass;

func mesh_get_blend_shape_mode(mesh: RID) -> int:
    pass;

func mesh_get_custom_aabb(mesh: RID) -> AABB:
    pass;

func mesh_get_surface(mesh: RID, surface: int) -> Dictionary:
    pass;

func mesh_get_surface_count(mesh: RID) -> int:
    pass;

func mesh_set_blend_shape_mode(mesh: RID, mode: int) -> void:
    pass;

func mesh_set_custom_aabb(mesh: RID, aabb: AABB) -> void:
    pass;

func mesh_set_shadow_mesh(mesh: RID, shadow_mesh: RID) -> void:
    pass;

func mesh_surface_get_arrays(mesh: RID, surface: int) -> Array:
    pass;

func mesh_surface_get_blend_shape_arrays(mesh: RID, surface: int) -> Array:
    pass;

func mesh_surface_get_format_attribute_stride(format: int, vertex_count: int) -> int:
    pass;

func mesh_surface_get_format_offset(format: int, vertex_count: int, array_index: int) -> int:
    pass;

func mesh_surface_get_format_skin_stride(format: int, vertex_count: int) -> int:
    pass;

func mesh_surface_get_format_vertex_stride(format: int, vertex_count: int) -> int:
    pass;

func mesh_surface_get_material(mesh: RID, surface: int) -> RID:
    pass;

func mesh_surface_set_material(mesh: RID, surface: int, material: RID) -> void:
    pass;

func mesh_surface_update_attribute_region(mesh: RID, surface: int, offset: int, data: PackedByteArray) -> void:
    pass;

func mesh_surface_update_skin_region(mesh: RID, surface: int, offset: int, data: PackedByteArray) -> void:
    pass;

func mesh_surface_update_vertex_region(mesh: RID, surface: int, offset: int, data: PackedByteArray) -> void:
    pass;

func multimesh_allocate_data(multimesh: RID, instances: int, transform_format: int, color_format: bool, custom_data_format: bool) -> void:
    pass;

func multimesh_create() -> RID:
    pass;

func multimesh_get_aabb(multimesh: RID) -> AABB:
    pass;

func multimesh_get_buffer(multimesh: RID) -> PackedFloat32Array:
    pass;

func multimesh_get_instance_count(multimesh: RID) -> int:
    pass;

func multimesh_get_mesh(multimesh: RID) -> RID:
    pass;

func multimesh_get_visible_instances(multimesh: RID) -> int:
    pass;

func multimesh_instance_get_color(multimesh: RID, index: int) -> Color:
    pass;

func multimesh_instance_get_custom_data(multimesh: RID, index: int) -> Color:
    pass;

func multimesh_instance_get_transform(multimesh: RID, index: int) -> Transform3D:
    pass;

func multimesh_instance_get_transform_2d(multimesh: RID, index: int) -> Transform2D:
    pass;

func multimesh_instance_set_color(multimesh: RID, index: int, color: Color) -> void:
    pass;

func multimesh_instance_set_custom_data(multimesh: RID, index: int, custom_data: Color) -> void:
    pass;

func multimesh_instance_set_transform(multimesh: RID, index: int, transform: Transform3D) -> void:
    pass;

func multimesh_instance_set_transform_2d(multimesh: RID, index: int, transform: Transform2D) -> void:
    pass;

func multimesh_set_buffer(multimesh: RID, buffer: PackedFloat32Array) -> void:
    pass;

func multimesh_set_mesh(multimesh: RID, mesh: RID) -> void:
    pass;

func multimesh_set_visible_instances(multimesh: RID, visible: int) -> void:
    pass;

func occluder_create() -> RID:
    pass;

func occluder_set_mesh(occluder: RID, vertices: PackedVector3Array, indices: PackedInt32Array) -> void:
    pass;

func omni_light_create() -> RID:
    pass;

func particles_collision_create() -> RID:
    pass;

func particles_collision_height_field_update(particles_collision: RID) -> void:
    pass;

func particles_collision_set_attractor_attenuation(particles_collision: RID, curve: float) -> void:
    pass;

func particles_collision_set_attractor_directionality(particles_collision: RID, amount: float) -> void:
    pass;

func particles_collision_set_attractor_strength(particles_collision: RID, setrngth: float) -> void:
    pass;

func particles_collision_set_box_extents(particles_collision: RID, extents: Vector3) -> void:
    pass;

func particles_collision_set_collision_type(particles_collision: RID, type: int) -> void:
    pass;

func particles_collision_set_cull_mask(particles_collision: RID, mask: int) -> void:
    pass;

func particles_collision_set_field_texture(particles_collision: RID, texture: RID) -> void:
    pass;

func particles_collision_set_height_field_resolution(particles_collision: RID, resolution: int) -> void:
    pass;

func particles_collision_set_sphere_radius(particles_collision: RID, radius: float) -> void:
    pass;

func particles_create() -> RID:
    pass;

func particles_emit(particles: RID, transform: Transform3D, velocity: Vector3, color: Color, custom: Color, emit_flags: int) -> void:
    pass;

func particles_get_current_aabb(particles: RID) -> AABB:
    pass;

func particles_get_emitting(particles: RID) -> bool:
    pass;

func particles_is_inactive(particles: RID) -> bool:
    pass;

func particles_request_process(particles: RID) -> void:
    pass;

func particles_restart(particles: RID) -> void:
    pass;

func particles_set_amount(particles: RID, amount: int) -> void:
    pass;

func particles_set_collision_base_size(particles: RID, size: float) -> void:
    pass;

func particles_set_custom_aabb(particles: RID, aabb: AABB) -> void:
    pass;

func particles_set_draw_order(particles: RID, order: int) -> void:
    pass;

func particles_set_draw_pass_mesh(particles: RID, pass: int, mesh: RID) -> void:
    pass;

func particles_set_draw_passes(particles: RID, count: int) -> void:
    pass;

func particles_set_emission_transform(particles: RID, transform: Transform3D) -> void:
    pass;

func particles_set_emitting(particles: RID, emitting: bool) -> void:
    pass;

func particles_set_explosiveness_ratio(particles: RID, ratio: float) -> void:
    pass;

func particles_set_fixed_fps(particles: RID, fps: int) -> void:
    pass;

func particles_set_fractional_delta(particles: RID, enable: bool) -> void:
    pass;

func particles_set_interpolate(particles: RID, enable: bool) -> void:
    pass;

func particles_set_lifetime(particles: RID, lifetime: float) -> void:
    pass;

func particles_set_mode(particles: RID, mode: int) -> void:
    pass;

func particles_set_one_shot(particles: RID, one_shot: bool) -> void:
    pass;

func particles_set_pre_process_time(particles: RID, time: float) -> void:
    pass;

func particles_set_process_material(particles: RID, material: RID) -> void:
    pass;

func particles_set_randomness_ratio(particles: RID, ratio: float) -> void:
    pass;

func particles_set_speed_scale(particles: RID, scale: float) -> void:
    pass;

func particles_set_subemitter(particles: RID, subemitter_particles: RID) -> void:
    pass;

func particles_set_trail_bind_poses(particles: RID, bind_poses: Transform3D[]) -> void:
    pass;

func particles_set_trails(particles: RID, enable: bool, length_sec: float) -> void:
    pass;

func particles_set_transform_align(particles: RID, align: int) -> void:
    pass;

func particles_set_use_local_coordinates(particles: RID, enable: bool) -> void:
    pass;

func reflection_probe_create() -> RID:
    pass;

func reflection_probe_set_ambient_color(probe: RID, color: Color) -> void:
    pass;

func reflection_probe_set_ambient_energy(probe: RID, energy: float) -> void:
    pass;

func reflection_probe_set_ambient_mode(probe: RID, mode: int) -> void:
    pass;

func reflection_probe_set_as_interior(probe: RID, enable: bool) -> void:
    pass;

func reflection_probe_set_cull_mask(probe: RID, layers: int) -> void:
    pass;

func reflection_probe_set_enable_box_projection(probe: RID, enable: bool) -> void:
    pass;

func reflection_probe_set_enable_shadows(probe: RID, enable: bool) -> void:
    pass;

func reflection_probe_set_extents(probe: RID, extents: Vector3) -> void:
    pass;

func reflection_probe_set_intensity(probe: RID, intensity: float) -> void:
    pass;

func reflection_probe_set_lod_threshold(probe: RID, pixels: float) -> void:
    pass;

func reflection_probe_set_max_distance(probe: RID, distance: float) -> void:
    pass;

func reflection_probe_set_origin_offset(probe: RID, offset: Vector3) -> void:
    pass;

func reflection_probe_set_resolution(probe: RID, resolution: int) -> void:
    pass;

func reflection_probe_set_update_mode(probe: RID, mode: int) -> void:
    pass;

func request_frame_drawn_callback(where: Object, method: StringName, userdata: Variant) -> void:
    pass;

func scenario_create() -> RID:
    pass;

func scenario_set_camera_effects(scenario: RID, effects: RID) -> void:
    pass;

func scenario_set_environment(scenario: RID, environment: RID) -> void:
    pass;

func scenario_set_fallback_environment(scenario: RID, environment: RID) -> void:
    pass;

func screen_space_roughness_limiter_set_active(enable: bool, amount: float, limit: float) -> void:
    pass;

func set_boot_image(image: Image, color: Color, scale: bool, use_filter: bool) -> void:
    pass;

func set_debug_generate_wireframes(generate: bool) -> void:
    pass;

func set_default_clear_color(color: Color) -> void:
    pass;

func shader_create() -> RID:
    pass;

func shader_get_code(shader: RID) -> String:
    pass;

func shader_get_default_texture_param(shader: RID, param: StringName) -> RID:
    pass;

func shader_get_param_default(shader: RID, param: StringName) -> Variant:
    pass;

func shader_get_param_list(shader: RID) -> Dictionary[]:
    pass;

func shader_set_default_texture_param(shader: RID, param: StringName, texture: RID) -> void:
    pass;

func shadows_quality_set(quality: int) -> void:
    pass;

func skeleton_allocate_data(skeleton: RID, bones: int, is_2d_skeleton: bool) -> void:
    pass;

func skeleton_bone_get_transform(skeleton: RID, bone: int) -> Transform3D:
    pass;

func skeleton_bone_get_transform_2d(skeleton: RID, bone: int) -> Transform2D:
    pass;

func skeleton_bone_set_transform(skeleton: RID, bone: int, transform: Transform3D) -> void:
    pass;

func skeleton_bone_set_transform_2d(skeleton: RID, bone: int, transform: Transform2D) -> void:
    pass;

func skeleton_create() -> RID:
    pass;

func skeleton_get_bone_count(skeleton: RID) -> int:
    pass;

func skeleton_set_base_transform_2d(skeleton: RID, base_transform: Transform2D) -> void:
    pass;

func sky_bake_panorama(sky: RID, energy: float, bake_irradiance: bool, size: Vector2i) -> Image:
    pass;

func sky_create() -> RID:
    pass;

func sky_set_material(sky: RID, material: RID) -> void:
    pass;

func sky_set_mode(sky: RID, mode: int) -> void:
    pass;

func sky_set_radiance_size(sky: RID, radiance_size: int) -> void:
    pass;

func spot_light_create() -> RID:
    pass;

func sub_surface_scattering_set_quality(quality: int) -> void:
    pass;

func sub_surface_scattering_set_scale(scale: float, depth_scale: float) -> void:
    pass;

func texture_2d_create(image: Image) -> RID:
    pass;

func texture_2d_get(texture: RID) -> Image:
    pass;

func texture_2d_layer_get(texture: RID, layer: int) -> Image:
    pass;

func texture_2d_layered_create(layers: Image[], layered_type: int) -> RID:
    pass;

func texture_2d_layered_placeholder_create(layered_type: int) -> RID:
    pass;

func texture_2d_placeholder_create() -> RID:
    pass;

func texture_2d_update(texture: RID, image: Image, layer: int) -> void:
    pass;

func texture_3d_create(format: int, width: int, height: int, depth: int, mipmaps: bool, data: Image[]) -> RID:
    pass;

func texture_3d_get(texture: RID) -> Image[]:
    pass;

func texture_3d_placeholder_create() -> RID:
    pass;

func texture_3d_update(texture: RID, data: Image[]) -> void:
    pass;

func texture_get_path(texture: RID) -> String:
    pass;

func texture_proxy_create(base: RID) -> RID:
    pass;

func texture_proxy_update(texture: RID, proxy_to: RID) -> void:
    pass;

func texture_replace(texture: RID, by_texture: RID) -> void:
    pass;

func texture_set_force_redraw_if_visible(texture: RID, enable: bool) -> void:
    pass;

func texture_set_path(texture: RID, path: String) -> void:
    pass;

func texture_set_size_override(texture: RID, width: int, height: int) -> void:
    pass;

func viewport_attach_camera(viewport: RID, camera: RID) -> void:
    pass;

func viewport_attach_canvas(viewport: RID, canvas: RID) -> void:
    pass;

func viewport_attach_to_screen(viewport: RID, rect: Rect2, screen: int) -> void:
    pass;

func viewport_create() -> RID:
    pass;

func viewport_get_measured_render_time_cpu(viewport: RID) -> float:
    pass;

func viewport_get_measured_render_time_gpu(viewport: RID) -> float:
    pass;

func viewport_get_render_info(viewport: RID, type: int, info: int) -> int:
    pass;

func viewport_get_texture(viewport: RID) -> RID:
    pass;

func viewport_remove_canvas(viewport: RID, canvas: RID) -> void:
    pass;

func viewport_set_active(viewport: RID, active: bool) -> void:
    pass;

func viewport_set_canvas_stacking(viewport: RID, canvas: RID, layer: int, sublayer: int) -> void:
    pass;

func viewport_set_canvas_transform(viewport: RID, canvas: RID, offset: Transform2D) -> void:
    pass;

func viewport_set_clear_mode(viewport: RID, clear_mode: int) -> void:
    pass;

func viewport_set_debug_draw(viewport: RID, draw: int) -> void:
    pass;

func viewport_set_default_canvas_item_texture_filter(viewport: RID, filter: int) -> void:
    pass;

func viewport_set_default_canvas_item_texture_repeat(viewport: RID, repeat: int) -> void:
    pass;

func viewport_set_disable_2d(viewport: RID, disable: bool) -> void:
    pass;

func viewport_set_disable_3d(viewport: RID, disable: bool) -> void:
    pass;

func viewport_set_disable_environment(viewport: RID, disabled: bool) -> void:
    pass;

func viewport_set_global_canvas_transform(viewport: RID, transform: Transform2D) -> void:
    pass;

func viewport_set_measure_render_time(viewport: RID, enable: bool) -> void:
    pass;

func viewport_set_msaa(viewport: RID, msaa: int) -> void:
    pass;

func viewport_set_occlusion_culling_build_quality(quality: int) -> void:
    pass;

func viewport_set_occlusion_rays_per_thread(rays_per_thread: int) -> void:
    pass;

func viewport_set_parent_viewport(viewport: RID, parent_viewport: RID) -> void:
    pass;

func viewport_set_render_direct_to_screen(viewport: RID, enabled: bool) -> void:
    pass;

func viewport_set_scenario(viewport: RID, scenario: RID) -> void:
    pass;

func viewport_set_screen_space_aa(viewport: RID, mode: int) -> void:
    pass;

func viewport_set_sdf_oversize_and_scale(viewport: RID, oversize: int, scale: int) -> void:
    pass;

func viewport_set_shadow_atlas_quadrant_subdivision(viewport: RID, quadrant: int, subdivision: int) -> void:
    pass;

func viewport_set_shadow_atlas_size(viewport: RID, size: int, use_16_bits: bool) -> void:
    pass;

func viewport_set_size(viewport: RID, width: int, height: int) -> void:
    pass;

func viewport_set_snap_2d_transforms_to_pixel(viewport: RID, enabled: bool) -> void:
    pass;

func viewport_set_snap_2d_vertices_to_pixel(viewport: RID, enabled: bool) -> void:
    pass;

func viewport_set_transparent_background(viewport: RID, enabled: bool) -> void:
    pass;

func viewport_set_update_mode(viewport: RID, update_mode: int) -> void:
    pass;

func viewport_set_use_debanding(viewport: RID, enable: bool) -> void:
    pass;

func viewport_set_use_occlusion_culling(viewport: RID, enable: bool) -> void:
    pass;

func viewport_set_use_xr(viewport: RID, use_xr: bool) -> void:
    pass;

func visibility_notifier_create() -> RID:
    pass;

func visibility_notifier_set_aabb(notifier: RID, aabb: AABB) -> void:
    pass;

func visibility_notifier_set_callbacks(notifier: RID, enter_callable: Callable, exit_callable: Callable) -> void:
    pass;

func voxel_gi_allocate_data(voxel_gi: RID, to_cell_xform: Transform3D, aabb: AABB, octree_size: Vector3i, octree_cells: PackedByteArray, data_cells: PackedByteArray, distance_field: PackedByteArray, level_counts: PackedInt32Array) -> void:
    pass;

func voxel_gi_create() -> RID:
    pass;

func voxel_gi_get_data_cells(voxel_gi: RID) -> PackedByteArray:
    pass;

func voxel_gi_get_distance_field(voxel_gi: RID) -> PackedByteArray:
    pass;

func voxel_gi_get_level_counts(voxel_gi: RID) -> PackedInt32Array:
    pass;

func voxel_gi_get_octree_cells(voxel_gi: RID) -> PackedByteArray:
    pass;

func voxel_gi_get_octree_size(voxel_gi: RID) -> Vector3i:
    pass;

func voxel_gi_get_to_cell_xform(voxel_gi: RID) -> Transform3D:
    pass;

func voxel_gi_set_bias(voxel_gi: RID, bias: float) -> void:
    pass;

func voxel_gi_set_dynamic_range(voxel_gi: RID, range: float) -> void:
    pass;

func voxel_gi_set_energy(voxel_gi: RID, energy: float) -> void:
    pass;

func voxel_gi_set_interior(voxel_gi: RID, enable: bool) -> void:
    pass;

func voxel_gi_set_normal_bias(voxel_gi: RID, bias: float) -> void:
    pass;

func voxel_gi_set_propagation(voxel_gi: RID, amount: float) -> void:
    pass;

func voxel_gi_set_quality(quality: int) -> void:
    pass;

func voxel_gi_set_use_two_bounces(voxel_gi: RID, enable: bool) -> void:
    pass;

