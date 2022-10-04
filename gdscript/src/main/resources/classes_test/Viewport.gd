#brief Base class for viewports.
#desc A Viewport creates a different view into the screen, or a sub-view inside another viewport. Children 2D Nodes will display on it, and children Camera3D 3D nodes will render on it too.
#desc Optionally, a viewport can have its own 2D or 3D world, so they don't share what they draw with other viewports.
#desc Viewports can also choose to be audio listeners, so they generate positional audio depending on a 2D or 3D camera child of it.
#desc Also, viewports can be assigned to different screens in case the devices have multiple screens.
#desc Finally, viewports can also behave as render targets, in which case they will not be visible unless the associated texture is used to draw.
class_name Viewport

#desc This quadrant will not be used.
const SHADOW_ATLAS_QUADRANT_SUBDIV_DISABLED = 0;

#desc This quadrant will only be used by one shadow map.
const SHADOW_ATLAS_QUADRANT_SUBDIV_1 = 1;

#desc This quadrant will be split in 4 and used by up to 4 shadow maps.
const SHADOW_ATLAS_QUADRANT_SUBDIV_4 = 2;

#desc This quadrant will be split 16 ways and used by up to 16 shadow maps.
const SHADOW_ATLAS_QUADRANT_SUBDIV_16 = 3;

#desc This quadrant will be split 64 ways and used by up to 64 shadow maps.
const SHADOW_ATLAS_QUADRANT_SUBDIV_64 = 4;

#desc This quadrant will be split 256 ways and used by up to 256 shadow maps. Unless the [member positional_shadow_atlas_size] is very high, the shadows in this quadrant will be very low resolution.
const SHADOW_ATLAS_QUADRANT_SUBDIV_256 = 5;

#desc This quadrant will be split 1024 ways and used by up to 1024 shadow maps. Unless the [member positional_shadow_atlas_size] is very high, the shadows in this quadrant will be very low resolution.
const SHADOW_ATLAS_QUADRANT_SUBDIV_1024 = 6;

#desc Represents the size of the [enum PositionalShadowAtlasQuadrantSubdiv] enum.
const SHADOW_ATLAS_QUADRANT_SUBDIV_MAX = 7;

#desc Use bilinear scaling for the viewport's 3D buffer. The amount of scaling can be set using [member scaling_3d_scale]. Values less then [code]1.0[/code] will result in undersampling while values greater than [code]1.0[/code] will result in supersampling. A value of [code]1.0[/code] disables scaling.
const SCALING_3D_MODE_BILINEAR = 0;

#desc Use AMD FidelityFX Super Resolution 1.0 upscaling for the viewport's 3D buffer. The amount of scaling can be set using [member scaling_3d_scale]. Values less then [code]1.0[/code] will be result in the viewport being upscaled using FSR. Values greater than [code]1.0[/code] are not supported and bilinear downsampling will be used instead. A value of [code]1.0[/code] disables scaling.
const SCALING_3D_MODE_FSR = 1;

#desc Represents the size of the [enum Scaling3DMode] enum.
const SCALING_3D_MODE_MAX = 2;

#desc Multisample antialiasing mode disabled. This is the default value, and is also the fastest setting.
const MSAA_DISABLED = 0;

#desc Use 2× Multisample Antialiasing. This has a moderate performance cost. It helps reduce aliasing noticeably, but 4× MSAA still looks substantially better.
const MSAA_2X = 1;

#desc Use 4× Multisample Antialiasing. This has a significant performance cost, and is generally a good compromise between performance and quality.
const MSAA_4X = 2;

#desc Use 8× Multisample Antialiasing. This has a very high performance cost. The difference between 4× and 8× MSAA may not always be visible in real gameplay conditions. Likely unsupported on low-end and older hardware.
const MSAA_8X = 3;

#desc Represents the size of the [enum MSAA] enum.
const MSAA_MAX = 4;

#desc Do not perform any antialiasing in the full screen post-process.
const SCREEN_SPACE_AA_DISABLED = 0;

#desc Use fast approximate antialiasing. FXAA is a popular screen-space antialiasing method, which is fast but will make the image look blurry, especially at lower resolutions. It can still work relatively well at large resolutions such as 1440p and 4K.
const SCREEN_SPACE_AA_FXAA = 1;

#desc Represents the size of the [enum ScreenSpaceAA] enum.
const SCREEN_SPACE_AA_MAX = 2;

#desc Amount of objects in frame.
const RENDER_INFO_OBJECTS_IN_FRAME = 0;

#desc Amount of vertices in frame.
const RENDER_INFO_PRIMITIVES_IN_FRAME = 1;

#desc Amount of draw calls in frame.
const RENDER_INFO_DRAW_CALLS_IN_FRAME = 2;

#desc Represents the size of the [enum RenderInfo] enum.
const RENDER_INFO_MAX = 3;

const RENDER_INFO_TYPE_VISIBLE = 0;

const RENDER_INFO_TYPE_SHADOW = 1;

const RENDER_INFO_TYPE_MAX = 2;

#desc Objects are displayed normally.
const DEBUG_DRAW_DISABLED = 0;

#desc Objects are displayed without light information.
const DEBUG_DRAW_UNSHADED = 1;

const DEBUG_DRAW_LIGHTING = 2;

#desc Objects are displayed semi-transparent with additive blending so you can see where they are drawing over top of one another. A higher overdraw means you are wasting performance on drawing pixels that are being hidden behind others.
const DEBUG_DRAW_OVERDRAW = 3;

#desc Objects are displayed in wireframe style.
const DEBUG_DRAW_WIREFRAME = 4;

const DEBUG_DRAW_NORMAL_BUFFER = 5;

#desc Objects are displayed with only the albedo value from [VoxelGI]s.
const DEBUG_DRAW_VOXEL_GI_ALBEDO = 6;

#desc Objects are displayed with only the lighting value from [VoxelGI]s.
const DEBUG_DRAW_VOXEL_GI_LIGHTING = 7;

#desc Objects are displayed with only the emission color from [VoxelGI]s.
const DEBUG_DRAW_VOXEL_GI_EMISSION = 8;

#desc Draws the shadow atlas that stores shadows from [OmniLight3D]s and [SpotLight3D]s in the upper left quadrant of the [Viewport].
const DEBUG_DRAW_SHADOW_ATLAS = 9;

#desc Draws the shadow atlas that stores shadows from [DirectionalLight3D]s in the upper left quadrant of the [Viewport].
const DEBUG_DRAW_DIRECTIONAL_SHADOW_ATLAS = 10;

const DEBUG_DRAW_SCENE_LUMINANCE = 11;

#desc Draws the screen-space ambient occlusion texture instead of the scene so that you can clearly see how it is affecting objects. In order for this display mode to work, you must have [member Environment.ssao_enabled] set in your [WorldEnvironment].
const DEBUG_DRAW_SSAO = 12;

#desc Draws the screen-space indirect lighting texture instead of the scene so that you can clearly see how it is affecting objects. In order for this display mode to work, you must have [member Environment.ssil_enabled] set in your [WorldEnvironment].
const DEBUG_DRAW_SSIL = 13;

#desc Colors each PSSM split for the [DirectionalLight3D]s in the scene a different color so you can see where the splits are. In order, they will be colored red, green, blue, and yellow.
const DEBUG_DRAW_PSSM_SPLITS = 14;

#desc Draws the decal atlas used by [Decal]s and light projector textures in the upper left quadrant of the [Viewport].
const DEBUG_DRAW_DECAL_ATLAS = 15;

const DEBUG_DRAW_SDFGI = 16;

const DEBUG_DRAW_SDFGI_PROBES = 17;

const DEBUG_DRAW_GI_BUFFER = 18;

const DEBUG_DRAW_DISABLE_LOD = 19;

const DEBUG_DRAW_CLUSTER_OMNI_LIGHTS = 20;

const DEBUG_DRAW_CLUSTER_SPOT_LIGHTS = 21;

const DEBUG_DRAW_CLUSTER_DECALS = 22;

const DEBUG_DRAW_CLUSTER_REFLECTION_PROBES = 23;

const DEBUG_DRAW_OCCLUDERS = 24;

const DEBUG_DRAW_MOTION_VECTORS = 25;

#desc The texture filter reads from the nearest pixel only. The simplest and fastest method of filtering, but the texture will look pixelized.
const DEFAULT_CANVAS_ITEM_TEXTURE_FILTER_NEAREST = 0;

#desc The texture filter blends between the nearest 4 pixels. Use this when you want to avoid a pixelated style, but do not want mipmaps.
const DEFAULT_CANVAS_ITEM_TEXTURE_FILTER_LINEAR = 1;

#desc The texture filter reads from the nearest pixel in the nearest mipmap. The fastest way to read from textures with mipmaps.
const DEFAULT_CANVAS_ITEM_TEXTURE_FILTER_LINEAR_WITH_MIPMAPS = 2;

#desc The texture filter blends between the nearest 4 pixels and between the nearest 2 mipmaps.
const DEFAULT_CANVAS_ITEM_TEXTURE_FILTER_NEAREST_WITH_MIPMAPS = 3;

#desc Max value for [enum DefaultCanvasItemTextureFilter] enum.
const DEFAULT_CANVAS_ITEM_TEXTURE_FILTER_MAX = 4;

#desc Disables textures repeating. Instead, when reading UVs outside the 0-1 range, the value will be clamped to the edge of the texture, resulting in a stretched out look at the borders of the texture.
const DEFAULT_CANVAS_ITEM_TEXTURE_REPEAT_DISABLED = 0;

#desc Enables the texture to repeat when UV coordinates are outside the 0-1 range. If using one of the linear filtering modes, this can result in artifacts at the edges of a texture when the sampler filters across the edges of the texture.
const DEFAULT_CANVAS_ITEM_TEXTURE_REPEAT_ENABLED = 1;

#desc Flip the texture when repeating so that the edge lines up instead of abruptly changing.
const DEFAULT_CANVAS_ITEM_TEXTURE_REPEAT_MIRROR = 2;

#desc Max value for [enum DefaultCanvasItemTextureRepeat] enum.
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

#desc VRS is disabled.
const VRS_DISABLED = 0;

#desc VRS uses a texture. Note, for stereoscopic use a texture atlas with a texture for each view.
const VRS_TEXTURE = 1;

#desc VRS texture is supplied by the primary [XRInterface].
const VRS_XR = 2;

#desc Represents the size of the [enum VRSMode] enum.
const VRS_MAX = 3;


#desc If [code]true[/code], the viewport will process 2D audio streams.
var audio_listener_enable_2d: bool;

#desc If [code]true[/code], the viewport will process 3D audio streams.
var audio_listener_enable_3d: bool;

#desc Sets the default filter mode used by [CanvasItem]s in this Viewport. See [enum DefaultCanvasItemTextureFilter] for options.
var canvas_item_default_texture_filter: int;

#desc Sets the default repeat mode used by [CanvasItem]s in this Viewport. See [enum DefaultCanvasItemTextureRepeat] for options.
var canvas_item_default_texture_repeat: int;

#desc The canvas transform of the viewport, useful for changing the on-screen positions of all child [CanvasItem]s. This is relative to the global canvas transform of the viewport.
var canvas_transform: Transform2D;

#desc The overlay mode for test rendered geometry in debug purposes.
var debug_draw: int;

#desc Disable 3D rendering (but keep 2D rendering).
var disable_3d: bool;

#desc Determines how sharp the upscaled image will be when using the FSR upscaling mode. Sharpness halves with every whole number. Values go from 0.0 (sharpest) to 2.0. Values above 2.0 won't make a visible difference.
#desc To control this property on the root viewport, set the [member ProjectSettings.rendering/scaling_3d/fsr_sharpness] project setting.
var fsr_sharpness: float;

#desc The global canvas transform of the viewport. The canvas transform is relative to this.
var global_canvas_transform: Transform2D;

#desc If [code]true[/code], the viewport will not receive input events.
var gui_disable_input: bool;

#desc If [code]true[/code], sub-windows (popups and dialogs) will be embedded inside application window as control-like nodes. If [code]false[/code], they will appear as separate windows handled by the operating system.
var gui_embed_subwindows: bool;

#desc If [code]true[/code], the GUI controls on the viewport will lay pixel perfectly.
var gui_snap_controls_to_pixels: bool;

var handle_input_locally: bool;

#desc The automatic LOD bias to use for meshes rendered within the [Viewport] (this is analogous to [member ReflectionProbe.mesh_lod_threshold]). Higher values will use less detailed versions of meshes that have LOD variations generated. If set to [code]0.0[/code], automatic LOD is disabled. Increase [member mesh_lod_threshold] to improve performance at the cost of geometry detail.
#desc To control this property on the root viewport, set the [member ProjectSettings.rendering/mesh_lod/lod_change/threshold_pixels] project setting.
#desc [b]Note:[/b] [member mesh_lod_threshold] does not affect [GeometryInstance3D] visibility ranges (also known as "manual" LOD or hierarchical LOD).
var mesh_lod_threshold: float;

#desc The multisample anti-aliasing mode for 2D/Canvas rendering. A higher number results in smoother edges at the cost of significantly worse performance. A value of 2 or 4 is best unless targeting very high-end systems. This has no effect on shader-induced aliasing or texture aliasing.
var msaa_2d: int;

#desc The multisample anti-aliasing mode for 3D rendering. A higher number results in smoother edges at the cost of significantly worse performance. A value of 2 or 4 is best unless targeting very high-end systems. See also bilinear scaling 3d [member scaling_3d_mode] for supersampling, which provides higher quality but is much more expensive. This has no effect on shader-induced aliasing or texture aliasing.
var msaa_3d: int;

#desc If [code]true[/code], the viewport will use a unique copy of the [World3D] defined in [member world_3d].
var own_world_3d: bool;

#desc If [code]true[/code], the objects rendered by viewport become subjects of mouse picking process.
var physics_object_picking: bool;

var positional_shadow_atlas_16_bits: bool;

#desc The subdivision amount of the first quadrant on the shadow atlas.
var positional_shadow_atlas_quad_0: int;

#desc The subdivision amount of the second quadrant on the shadow atlas.
var positional_shadow_atlas_quad_1: int;

#desc The subdivision amount of the third quadrant on the shadow atlas.
var positional_shadow_atlas_quad_2: int;

#desc The subdivision amount of the fourth quadrant on the shadow atlas.
var positional_shadow_atlas_quad_3: int;

#desc The shadow atlas' resolution (used for omni and spot lights). The value will be rounded up to the nearest power of 2.
#desc [b]Note:[/b] If this is set to [code]0[/code], no shadows will be visible at all (including directional shadows).
var positional_shadow_atlas_size: int;

#desc Sets scaling 3d mode. Bilinear scaling renders at different resolution to either undersample or supersample the viewport. FidelityFX Super Resolution 1.0, abbreviated to FSR, is an upscaling technology that produces high quality images at fast framerates by using a spatially aware upscaling algorithm. FSR is slightly more expensive than bilinear, but it produces significantly higher image quality. FSR should be used where possible.
#desc To control this property on the root viewport, set the [member ProjectSettings.rendering/scaling_3d/mode] project setting.
var scaling_3d_mode: int;

#desc Scales the 3D render buffer based on the viewport size uses an image filter specified in [member ProjectSettings.rendering/scaling_3d/mode] to scale the output image to the full viewport size. Values lower than [code]1.0[/code] can be used to speed up 3D rendering at the cost of quality (undersampling). Values greater than [code]1.0[/code] are only valid for bilinear mode and can be used to improve 3D rendering quality at a high performance cost (supersampling). See also [member ProjectSettings.rendering/anti_aliasing/quality/msaa_3d] for multi-sample antialiasing, which is significantly cheaper but only smooths the edges of polygons.
#desc When using FSR upscaling, AMD recommends exposing the following values as preset options to users "Ultra Quality: 0.77", "Quality: 0.67", "Balanced: 0.59", "Performance: 0.5" instead of exposing the entire scale.
#desc To control this property on the root viewport, set the [member ProjectSettings.rendering/scaling_3d/scale] project setting.
var scaling_3d_scale: float;

#desc Sets the screen-space antialiasing method used. Screen-space antialiasing works by selectively blurring edges in a post-process shader. It differs from MSAA which takes multiple coverage samples while rendering objects. Screen-space AA methods are typically faster than MSAA and will smooth out specular aliasing, but tend to make scenes appear blurry.
var screen_space_aa: int;

var sdf_oversize: int;

var sdf_scale: int;

var snap_2d_transforms_to_pixel: bool;

var snap_2d_vertices_to_pixel: bool;

#desc Affects the final texture sharpness by reading from a lower or higher mipmap (also called "texture LOD bias"). Negative values make mipmapped textures sharper but grainier when viewed at a distance, while positive values make mipmapped textures blurrier (even when up close).
#desc Enabling temporal antialiasing ([member use_taa]) will automatically apply a [code]-0.5[/code] offset to this value, while enabling FXAA ([member screen_space_aa]) will automatically apply a [code]-0.25[/code] offset to this value. If both TAA and FXAA are enbled at the same time, an offset of [code]-0.75[/code] is applied to this value.
#desc [b]Note:[/b] If [member scaling_3d_scale] is lower than [code]1.0[/code] (exclusive), [member texture_mipmap_bias] is used to adjust the automatic mipmap bias which is calculated internally based on the scale factor. The formula for this is [code]log2(scaling_3d_scale) + mipmap_bias[/code].
#desc To control this property on the root viewport, set the [member ProjectSettings.rendering/textures/default_filters/texture_mipmap_bias] project setting.
var texture_mipmap_bias: float;

#desc If [code]true[/code], the viewport should render its background as transparent.
var transparent_bg: bool;

var use_debanding: bool;

#desc If [code]true[/code], [OccluderInstance3D] nodes will be usable for occlusion culling in 3D for this viewport. For the root viewport, [member ProjectSettings.rendering/occlusion_culling/use_occlusion_culling] must be set to [code]true[/code] instead.
#desc [b]Note:[/b] Enabling occlusion culling has a cost on the CPU. Only enable occlusion culling if you actually plan to use it, and think whether your scene can actually benefit from occlusion culling. Large, open scenes with few or no objects blocking the view will generally not benefit much from occlusion culling. Large open scenes generally benefit more from mesh LOD and visibility ranges ([member GeometryInstance3D.visibility_range_begin] and [member GeometryInstance3D.visibility_range_end]) compared to occlusion culling.
var use_occlusion_culling: bool;

#desc Enables Temporal Anti-Aliasing for this viewport. TAA works by jittering the camera and accumulating the images of the last rendered frames, motion vector rendering is used to account for camera and object motion.
#desc [b]Note:[/b] The implementation is not complete yet, some visual instances such as particles and skinned meshes may show artifacts.
var use_taa: bool;

#desc If [code]true[/code], the viewport will use the primary XR interface to render XR output. When applicable this can result in a stereoscopic image and the resulting render being output to a headset.
var use_xr: bool;

#desc The Variable Rate Shading (VRS) mode that is used for this viewport. Note, if hardware does not support VRS this property is ignored.
var vrs_mode: int;

#desc Texture to use when [member vrs_mode] is set to [constant Viewport.VRS_TEXTURE].
var vrs_texture: Texture2D;

#desc The custom [World2D] which can be used as 2D environment source.
var world_2d: World2D;

#desc The custom [World3D] which can be used as 3D environment source.
var world_3d: World3D;



#desc Returns the first valid [World2D] for this viewport, searching the [member world_2d] property of itself and any Viewport ancestor.
func find_world_2d() -> World2D:
	pass;

#desc Returns the first valid [World3D] for this viewport, searching the [member world_3d] property of itself and any Viewport ancestor.
func find_world_3d() -> World3D:
	pass;

#desc Returns the currently active 2D camera. Returns null if there are no active cameras.
func get_camera_2d() -> Camera2D:
	pass;

#desc Returns the currently active 3D camera.
func get_camera_3d() -> Camera3D:
	pass;

#desc Returns the total transform of the viewport.
func get_final_transform() -> Transform2D:
	pass;

#desc Returns the mouse's position in this [Viewport] using the coordinate system of this [Viewport].
func get_mouse_position() -> Vector2:
	pass;

#desc Returns the [enum PositionalShadowAtlasQuadrantSubdiv] of the specified quadrant.
func get_positional_shadow_atlas_quadrant_subdiv(quadrant: int) -> int:
	pass;

func get_render_info(type: int, info: int) -> int:
	pass;

#desc Returns the viewport's texture.
#desc [b]Note:[/b] When trying to store the current texture (e.g. in a file), it might be completely black or outdated if used too early, especially when used in e.g. [method Node._ready]. To make sure the texture you get is correct, you can await [signal RenderingServer.frame_post_draw] signal.
#desc [codeblock]
#desc func _ready():
#desc await RenderingServer.frame_post_draw
#desc $Viewport.get_texture().get_image().save_png("user://Screenshot.png")
#desc [/codeblock]
func get_texture() -> ViewportTexture:
	pass;

#desc Returns the viewport's RID from the [RenderingServer].
func get_viewport_rid() -> RID:
	pass;

#desc Returns the visible rectangle in global screen coordinates.
func get_visible_rect() -> Rect2:
	pass;

#desc Returns the drag data from the GUI, that was previously returned by [method Control._get_drag_data].
func gui_get_drag_data() -> Variant:
	pass;

#desc Returns the [Control] having the focus within this viewport. If no [Control] has the focus, returns null.
func gui_get_focus_owner() -> Control:
	pass;

#desc Returns [code]true[/code] if the drag operation is successful.
func gui_is_drag_successful() -> bool:
	pass;

#desc Returns [code]true[/code] if the viewport is currently performing a drag operation.
#desc Alternative to [constant Node.NOTIFICATION_DRAG_BEGIN] and [constant Node.NOTIFICATION_DRAG_END] when you prefer polling the value.
func gui_is_dragging() -> bool:
	pass;

#desc Removes the focus from the currently focused [Control] within this viewport. If no [Control] has the focus, does nothing.
func gui_release_focus() -> void:
	pass;

func is_input_handled() -> bool:
	pass;

func push_input(event: InputEvent, in_local_coords: bool) -> void:
	pass;

func push_text_input(text: String) -> void:
	pass;

func push_unhandled_input(event: InputEvent, in_local_coords: bool) -> void:
	pass;

#desc Stops the input from propagating further down the [SceneTree].
func set_input_as_handled() -> void:
	pass;

#desc Sets the number of subdivisions to use in the specified quadrant. A higher number of subdivisions allows you to have more shadows in the scene at once, but reduces the quality of the shadows. A good practice is to have quadrants with a varying number of subdivisions and to have as few subdivisions as possible.
func set_positional_shadow_atlas_quadrant_subdiv(quadrant: int, subdiv: int) -> void:
	pass;

#desc Moves the mouse pointer to the specified position in this [Viewport] using the coordinate system of this [Viewport].
func warp_mouse(position: Vector2) -> void:
	pass;


