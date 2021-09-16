extends Node
class_name Viewport
const SHADOW_ATLAS_QUADRANT_SUBDIV_DISABLED = 0;
const SHADOW_ATLAS_QUADRANT_SUBDIV_1 = 1;
const SHADOW_ATLAS_QUADRANT_SUBDIV_4 = 2;
const SHADOW_ATLAS_QUADRANT_SUBDIV_16 = 3;
const SHADOW_ATLAS_QUADRANT_SUBDIV_64 = 4;
const SHADOW_ATLAS_QUADRANT_SUBDIV_256 = 5;
const SHADOW_ATLAS_QUADRANT_SUBDIV_1024 = 6;
const SHADOW_ATLAS_QUADRANT_SUBDIV_MAX = 7;
const MSAA_DISABLED = 0;
const MSAA_2X = 1;
const MSAA_4X = 2;
const MSAA_8X = 3;
const MSAA_16X = 4;
const MSAA_MAX = 5;
const SCREEN_SPACE_AA_DISABLED = 0;
const SCREEN_SPACE_AA_FXAA = 1;
const SCREEN_SPACE_AA_MAX = 2;
const RENDER_INFO_OBJECTS_IN_FRAME = 0;
const RENDER_INFO_PRIMITIVES_IN_FRAME = 1;
const RENDER_INFO_DRAW_CALLS_IN_FRAME = 2;
const RENDER_INFO_MAX = 3;
const RENDER_INFO_TYPE_VISIBLE = 0;
const RENDER_INFO_TYPE_SHADOW = 1;
const RENDER_INFO_TYPE_MAX = 2;
const DEBUG_DRAW_DISABLED = 0;
const DEBUG_DRAW_UNSHADED = 1;
const DEBUG_DRAW_LIGHTING = 2;
const DEBUG_DRAW_OVERDRAW = 3;
const DEBUG_DRAW_WIREFRAME = 4;
const DEBUG_DRAW_NORMAL_BUFFER = 5;
const DEBUG_DRAW_VOXEL_GI_ALBEDO = 6;
const DEBUG_DRAW_VOXEL_GI_LIGHTING = 7;
const DEBUG_DRAW_VOXEL_GI_EMISSION = 8;
const DEBUG_DRAW_SHADOW_ATLAS = 9;
const DEBUG_DRAW_DIRECTIONAL_SHADOW_ATLAS = 10;
const DEBUG_DRAW_SCENE_LUMINANCE = 11;
const DEBUG_DRAW_SSAO = 12;
const DEBUG_DRAW_PSSM_SPLITS = 13;
const DEBUG_DRAW_DECAL_ATLAS = 14;
const DEBUG_DRAW_SDFGI = 15;
const DEBUG_DRAW_SDFGI_PROBES = 16;
const DEBUG_DRAW_GI_BUFFER = 17;
const DEBUG_DRAW_DISABLE_LOD = 18;
const DEBUG_DRAW_CLUSTER_OMNI_LIGHTS = 19;
const DEBUG_DRAW_CLUSTER_SPOT_LIGHTS = 20;
const DEBUG_DRAW_CLUSTER_DECALS = 21;
const DEBUG_DRAW_CLUSTER_REFLECTION_PROBES = 22;
const DEBUG_DRAW_OCCLUDERS = 23;
const DEFAULT_CANVAS_ITEM_TEXTURE_FILTER_NEAREST = 0;
const DEFAULT_CANVAS_ITEM_TEXTURE_FILTER_LINEAR = 1;
const DEFAULT_CANVAS_ITEM_TEXTURE_FILTER_LINEAR_WITH_MIPMAPS = 2;
const DEFAULT_CANVAS_ITEM_TEXTURE_FILTER_NEAREST_WITH_MIPMAPS = 3;
const DEFAULT_CANVAS_ITEM_TEXTURE_FILTER_MAX = 4;
const DEFAULT_CANVAS_ITEM_TEXTURE_REPEAT_DISABLED = 0;
const DEFAULT_CANVAS_ITEM_TEXTURE_REPEAT_ENABLED = 1;
const DEFAULT_CANVAS_ITEM_TEXTURE_REPEAT_MIRROR = 2;
const DEFAULT_CANVAS_ITEM_TEXTURE_REPEAT_MAX = 3;
const SDF_OVERSIZE_100_PERCENT = 0;
const SDF_OVERSIZE_120_PERCENT = 1;
const SDF_OVERSIZE_150_PERCENT = 2;
const SDF_OVERSIZE_200_PERCENT = 3;
const SDF_OVERSIZE_MAX = 4;
const SDF_SCALE_100_PERCENT = 0;
const SDF_SCALE_50_PERCENT = 1;
const SDF_SCALE_25_PERCENT = 2;
const SDF_SCALE_MAX = 3;

var audio_listener_enable_2d: bool;
var audio_listener_enable_3d: bool;
var canvas_item_default_texture_filter: int;
var canvas_item_default_texture_repeat: int;
var canvas_transform: Transform2D;
var debug_draw: int;
var disable_3d: bool;
var global_canvas_transform: Transform2D;
var gui_disable_input: bool;
var gui_embed_subwindows: bool;
var gui_snap_controls_to_pixels: bool;
var handle_input_locally: bool;
var lod_threshold: float;
var msaa: int;
var own_world_3d: bool;
var physics_object_picking: bool;
var screen_space_aa: int;
var sdf_oversize: int;
var sdf_scale: int;
var shadow_atlas_16_bits: bool;
var shadow_atlas_quad_0: int;
var shadow_atlas_quad_1: int;
var shadow_atlas_quad_2: int;
var shadow_atlas_quad_3: int;
var shadow_atlas_size: int;
var snap_2d_transforms_to_pixel: bool;
var snap_2d_vertices_to_pixel: bool;
var transparent_bg: bool;
var use_debanding: bool;
var use_occlusion_culling: bool;
var use_xr: bool;
var world_2d: World2D;
var world_3d: World3D;

func find_world_2d() -> World2D:
    pass;
func find_world_3d() -> World3D:
    pass;
func get_camera() -> Camera3D:
    pass;
func get_final_transform() -> Transform2D:
    pass;
func get_mouse_position() -> Vector2:
    pass;
func get_render_info(type: int, info: int) -> int:
    pass;
func get_shadow_atlas_quadrant_subdiv(quadrant: int) -> int:
    pass;
func get_texture() -> ViewportTexture:
    pass;
func get_viewport_rid() -> RID:
    pass;
func get_visible_rect() -> Rect2:
    pass;
func gui_get_drag_data() -> Variant:
    pass;
func gui_is_dragging() -> bool:
    pass;
func input(event: InputEvent, in_local_coords: bool) -> void:
    pass;
func input_text(text: String) -> void:
    pass;
func is_embedding_subwindows() -> bool:
    pass;
func is_input_handled() -> bool:
    pass;
func set_input_as_handled() -> void:
    pass;
func set_shadow_atlas_quadrant_subdiv(quadrant: int, subdiv: int) -> void:
    pass;
func unhandled_input(event: InputEvent, in_local_coords: bool) -> void:
    pass;
func warp_mouse(to_position: Vector2) -> void:
    pass;
