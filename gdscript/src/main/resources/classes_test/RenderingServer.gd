extends Object
#brief Server for anything visible.
#desc Server for anything visible. The rendering server is the API backend for everything visible. The whole scene system mounts on it to display.
#desc The rendering server is completely opaque, the internals are entirely implementation specific and cannot be accessed.
#desc The rendering server can be used to bypass the scene system entirely.
#desc Resources are created using the [code]*_create[/code] functions.
#desc All objects are drawn to a viewport. You can use the [Viewport] attached to the [SceneTree] or you can create one yourself with [method viewport_create]. When using a custom scenario or canvas, the scenario or canvas needs to be attached to the viewport using [method viewport_set_scenario] or [method viewport_attach_canvas].
#desc In 3D, all visual objects must be associated with a scenario. The scenario is a visual representation of the world. If accessing the rendering server from a running game, the scenario can be accessed from the scene tree from any [Node3D] node with [method Node3D.get_world_3d]. Otherwise, a scenario can be created with [method scenario_create].
#desc Similarly, in 2D, a canvas is needed to draw all canvas items.
#desc In 3D, all visible objects are comprised of a resource and an instance. A resource can be a mesh, a particle system, a light, or any other 3D object. In order to be visible resources must be attached to an instance using [method instance_set_base]. The instance must also be attached to the scenario using [method instance_set_scenario] in order to be visible.
#desc In 2D, all visible objects are some form of canvas item. In order to be visible, a canvas item needs to be the child of a canvas attached to a viewport, or it needs to be the child of another canvas item that is eventually attached to the canvas.
class_name RenderingServer

#desc Marks an error that shows that the index array is empty.
const NO_INDEX_ARRAY = -1;

#desc Number of weights/bones per vertex.
const ARRAY_WEIGHTS_SIZE = 4;

#desc The minimum Z-layer for canvas items.
const CANVAS_ITEM_Z_MIN = -4096;

#desc The maximum Z-layer for canvas items.
const CANVAS_ITEM_Z_MAX = 4096;

#desc Max number of glow levels that can be used with glow post-process effect.
const MAX_GLOW_LEVELS = 7;

#desc Unused enum in Godot 3.x.
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

#desc Shader is a 3D shader.
const SHADER_SPATIAL = 0;

#desc Shader is a 2D shader.
const SHADER_CANVAS_ITEM = 1;

#desc Shader is a particle shader.
const SHADER_PARTICLES = 2;

#desc Shader is a sky shader.
const SHADER_SKY = 3;

#desc Shader is a fog shader.
const SHADER_FOG = 4;

#desc Represents the size of the [enum ShaderMode] enum.
const SHADER_MAX = 5;

#desc The minimum renderpriority of all materials.
const MATERIAL_RENDER_PRIORITY_MIN = -128;

#desc The maximum renderpriority of all materials.
const MATERIAL_RENDER_PRIORITY_MAX = 127;

#desc Array is a vertex array.
const ARRAY_VERTEX = 0;

#desc Array is a normal array.
const ARRAY_NORMAL = 1;

#desc Array is a tangent array.
const ARRAY_TANGENT = 2;

#desc Array is a color array.
const ARRAY_COLOR = 3;

#desc Array is an UV coordinates array.
const ARRAY_TEX_UV = 4;

#desc Array is an UV coordinates array for the second UV coordinates.
const ARRAY_TEX_UV2 = 5;

const ARRAY_CUSTOM0 = 6;

const ARRAY_CUSTOM1 = 7;

const ARRAY_CUSTOM2 = 8;

const ARRAY_CUSTOM3 = 9;

#desc Array contains bone information.
const ARRAY_BONES = 10;

#desc Array is weight information.
const ARRAY_WEIGHTS = 11;

#desc Array is index array.
const ARRAY_INDEX = 12;

#desc Represents the size of the [enum ArrayType] enum.
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

#desc Flag used to mark a vertex array.
const ARRAY_FORMAT_VERTEX = 1;

#desc Flag used to mark a normal array.
const ARRAY_FORMAT_NORMAL = 2;

#desc Flag used to mark a tangent array.
const ARRAY_FORMAT_TANGENT = 4;

#desc Flag used to mark a color array.
const ARRAY_FORMAT_COLOR = 8;

#desc Flag used to mark an UV coordinates array.
const ARRAY_FORMAT_TEX_UV = 16;

#desc Flag used to mark an UV coordinates array for the second UV coordinates.
const ARRAY_FORMAT_TEX_UV2 = 32;

const ARRAY_FORMAT_CUSTOM0 = 64;

const ARRAY_FORMAT_CUSTOM1 = 128;

const ARRAY_FORMAT_CUSTOM2 = 256;

const ARRAY_FORMAT_CUSTOM3 = 512;

#desc Flag used to mark a bone information array.
const ARRAY_FORMAT_BONES = 1024;

#desc Flag used to mark a weights array.
const ARRAY_FORMAT_WEIGHTS = 2048;

#desc Flag used to mark an index array.
const ARRAY_FORMAT_INDEX = 4096;

const ARRAY_FORMAT_BLEND_SHAPE_MASK = 7;

const ARRAY_FORMAT_CUSTOM_BASE = 13;

const ARRAY_FORMAT_CUSTOM_BITS = 3;

const ARRAY_FORMAT_CUSTOM0_SHIFT = 13;

const ARRAY_FORMAT_CUSTOM1_SHIFT = 16;

const ARRAY_FORMAT_CUSTOM2_SHIFT = 19;

const ARRAY_FORMAT_CUSTOM3_SHIFT = 22;

const ARRAY_FORMAT_CUSTOM_MASK = 7;

const ARRAY_COMPRESS_FLAGS_BASE = 25;

#desc Flag used to mark that the array contains 2D vertices.
const ARRAY_FLAG_USE_2D_VERTICES = 33554432;

const ARRAY_FLAG_USE_DYNAMIC_UPDATE = 67108864;

const ARRAY_FLAG_USE_8_BONE_WEIGHTS = 134217728;

#desc Primitive to draw consists of points.
const PRIMITIVE_POINTS = 0;

#desc Primitive to draw consists of lines.
const PRIMITIVE_LINES = 1;

#desc Primitive to draw consists of a line strip from start to end.
const PRIMITIVE_LINE_STRIP = 2;

#desc Primitive to draw consists of triangles.
const PRIMITIVE_TRIANGLES = 3;

#desc Primitive to draw consists of a triangle strip (the last 3 vertices are always combined to make a triangle).
const PRIMITIVE_TRIANGLE_STRIP = 4;

#desc Represents the size of the [enum PrimitiveType] enum.
const PRIMITIVE_MAX = 5;

#desc Blend shapes are normalized.
const BLEND_SHAPE_MODE_NORMALIZED = 0;

#desc Blend shapes are relative to base weight.
const BLEND_SHAPE_MODE_RELATIVE = 1;

#desc Use [Transform2D] to store MultiMesh transform.
const MULTIMESH_TRANSFORM_2D = 0;

#desc Use [Transform3D] to store MultiMesh transform.
const MULTIMESH_TRANSFORM_3D = 1;

#desc Nearest-neighbor filter for light projectors (use for pixel art light projectors). No mipmaps are used for rendering, which means light projectors at a distance will look sharp but grainy. This has roughly the same performance cost as using mipmaps.
const LIGHT_PROJECTOR_FILTER_NEAREST = 0;

#desc Linear filter for light projectors (use for non-pixel art light projectors). No mipmaps are used for rendering, which means light projectors at a distance will look smooth but blurry. This has roughly the same performance cost as using mipmaps.
const LIGHT_PROJECTOR_FILTER_LINEAR = 1;

#desc Nearest-neighbor filter for light projectors (use for pixel art light projectors). Isotropic mipmaps are used for rendering, which means light projectors at a distance will look smooth but blurry. This has roughly the same performance cost as not using mipmaps.
const LIGHT_PROJECTOR_FILTER_NEAREST_MIPMAPS = 2;

#desc Linear filter for light projectors (use for non-pixel art light projectors). Isotropic mipmaps are used for rendering, which means light projectors at a distance will look smooth but blurry. This has roughly the same performance cost as not using mipmaps.
const LIGHT_PROJECTOR_FILTER_LINEAR_MIPMAPS = 3;

#desc Nearest-neighbor filter for light projectors (use for pixel art light projectors). Anisotropic mipmaps are used for rendering, which means light projectors at a distance will look smooth and sharp when viewed from oblique angles. This looks better compared to isotropic mipmaps, but is slower. The level of anisotropic filtering is defined by [member ProjectSettings.rendering/textures/default_filters/anisotropic_filtering_level].
const LIGHT_PROJECTOR_FILTER_NEAREST_MIPMAPS_ANISOTROPIC = 4;

#desc Linear filter for light projectors (use for non-pixel art light projectors). Anisotropic mipmaps are used for rendering, which means light projectors at a distance will look smooth and sharp when viewed from oblique angles. This looks better compared to isotropic mipmaps, but is slower. The level of anisotropic filtering is defined by [member ProjectSettings.rendering/textures/default_filters/anisotropic_filtering_level].
const LIGHT_PROJECTOR_FILTER_LINEAR_MIPMAPS_ANISOTROPIC = 5;

#desc Is a directional (sun) light.
const LIGHT_DIRECTIONAL = 0;

#desc Is an omni light.
const LIGHT_OMNI = 1;

#desc Is a spot light.
const LIGHT_SPOT = 2;

#desc The light's energy multiplier.
const LIGHT_PARAM_ENERGY = 0;

#desc The light's indirect energy multiplier (final indirect energy is [constant LIGHT_PARAM_ENERGY] * [constant LIGHT_PARAM_INDIRECT_ENERGY]).
const LIGHT_PARAM_INDIRECT_ENERGY = 1;

#desc The light's volumetric fog energy multiplier (final volumetric fog energy is [constant LIGHT_PARAM_ENERGY] * [constant LIGHT_PARAM_VOLUMETRIC_FOG_ENERGY]).
const LIGHT_PARAM_VOLUMETRIC_FOG_ENERGY = 2;

#desc The light's influence on specularity.
const LIGHT_PARAM_SPECULAR = 3;

#desc The light's range.
const LIGHT_PARAM_RANGE = 4;

#desc The size of the light when using spot light or omni light. The angular size of the light when using directional light.
const LIGHT_PARAM_SIZE = 5;

#desc The light's attenuation.
const LIGHT_PARAM_ATTENUATION = 6;

#desc The spotlight's angle.
const LIGHT_PARAM_SPOT_ANGLE = 7;

#desc The spotlight's attenuation.
const LIGHT_PARAM_SPOT_ATTENUATION = 8;

#desc Max distance that shadows will be rendered.
const LIGHT_PARAM_SHADOW_MAX_DISTANCE = 9;

#desc Proportion of shadow atlas occupied by the first split.
const LIGHT_PARAM_SHADOW_SPLIT_1_OFFSET = 10;

#desc Proportion of shadow atlas occupied by the second split.
const LIGHT_PARAM_SHADOW_SPLIT_2_OFFSET = 11;

#desc Proportion of shadow atlas occupied by the third split. The fourth split occupies the rest.
const LIGHT_PARAM_SHADOW_SPLIT_3_OFFSET = 12;

#desc Proportion of shadow max distance where the shadow will start to fade out.
const LIGHT_PARAM_SHADOW_FADE_START = 13;

#desc Normal bias used to offset shadow lookup by object normal. Can be used to fix self-shadowing artifacts.
const LIGHT_PARAM_SHADOW_NORMAL_BIAS = 14;

#desc Bias the shadow lookup to fix self-shadowing artifacts.
const LIGHT_PARAM_SHADOW_BIAS = 15;

#desc Sets the size of the directional shadow pancake. The pancake offsets the start of the shadow's camera frustum to provide a higher effective depth resolution for the shadow. However, a high pancake size can cause artifacts in the shadows of large objects that are close to the edge of the frustum. Reducing the pancake size can help. Setting the size to [code]0[/code] turns off the pancaking effect.
const LIGHT_PARAM_SHADOW_PANCAKE_SIZE = 16;

#desc The light's shadow opacity. Values lower than [code]1.0[/code] make the light appear through shadows. This can be used to fake global illumination at a low performance cost.
const LIGHT_PARAM_SHADOW_OPACITY = 17;

#desc Blurs the edges of the shadow. Can be used to hide pixel artifacts in low resolution shadow maps. A high value can make shadows appear grainy and can cause other unwanted artifacts. Try to keep as near default as possible.
const LIGHT_PARAM_SHADOW_BLUR = 18;

const LIGHT_PARAM_TRANSMITTANCE_BIAS = 19;

#desc Represents the size of the [enum LightParam] enum.
const LIGHT_PARAM_MAX = 21;

const LIGHT_BAKE_DISABLED = 0;

const LIGHT_BAKE_STATIC = 1;

const LIGHT_BAKE_DYNAMIC = 2;

#desc Use a dual paraboloid shadow map for omni lights.
const LIGHT_OMNI_SHADOW_DUAL_PARABOLOID = 0;

#desc Use a cubemap shadow map for omni lights. Slower but better quality than dual paraboloid.
const LIGHT_OMNI_SHADOW_CUBE = 1;

#desc Use orthogonal shadow projection for directional light.
const LIGHT_DIRECTIONAL_SHADOW_ORTHOGONAL = 0;

#desc Use 2 splits for shadow projection when using directional light.
const LIGHT_DIRECTIONAL_SHADOW_PARALLEL_2_SPLITS = 1;

#desc Use 4 splits for shadow projection when using directional light.
const LIGHT_DIRECTIONAL_SHADOW_PARALLEL_4_SPLITS = 2;

#desc Use DirectionalLight3D in both sky rendering and scene lighting.
const LIGHT_DIRECTIONAL_SKY_MODE_LIGHT_AND_SKY = 0;

#desc Only use DirectionalLight3D in scene lighting.
const LIGHT_DIRECTIONAL_SKY_MODE_LIGHT_ONLY = 1;

#desc Only use DirectionalLight3D in sky rendering.
const LIGHT_DIRECTIONAL_SKY_MODE_SKY_ONLY = 2;

#desc Lowest shadow filtering quality (fastest). Soft shadows are not available with this quality setting, which means the [member Light3D.shadow_blur] property is ignored if [member Light3D.light_size] and [member Light3D.light_angular_distance] is [code]0.0[/code].
#desc [b]Note:[/b] The variable shadow blur performed by [member Light3D.light_size] and [member Light3D.light_angular_distance] is still effective when using hard shadow filtering. In this case, [member Light3D.shadow_blur] [i]is[/i] taken into account. However, the results will not be blurred, instead the blur amount is treated as a maximum radius for the penumbra.
const SHADOW_QUALITY_HARD = 0;

#desc Very low shadow filtering quality (faster). When using this quality setting, [member Light3D.shadow_blur] is automatically multiplied by 0.75× to avoid introducing too much noise. This division only applies to lights whose [member Light3D.light_size] or [member Light3D.light_angular_distance] is [code]0.0[/code]).
const SHADOW_QUALITY_SOFT_VERY_LOW = 1;

#desc Low shadow filtering quality (fast).
const SHADOW_QUALITY_SOFT_LOW = 2;

#desc Medium low shadow filtering quality (average).
const SHADOW_QUALITY_SOFT_MEDIUM = 3;

#desc High low shadow filtering quality (slow). When using this quality setting, [member Light3D.shadow_blur] is automatically multiplied by 1.5× to better make use of the high sample count. This increased blur also improves the stability of dynamic object shadows. This multiplier only applies to lights whose [member Light3D.light_size] or [member Light3D.light_angular_distance] is [code]0.0[/code]).
const SHADOW_QUALITY_SOFT_HIGH = 4;

#desc Highest low shadow filtering quality (slowest). When using this quality setting, [member Light3D.shadow_blur] is automatically multiplied by 2× to better make use of the high sample count. This increased blur also improves the stability of dynamic object shadows. This multiplier only applies to lights whose [member Light3D.light_size] or [member Light3D.light_angular_distance] is [code]0.0[/code]).
const SHADOW_QUALITY_SOFT_ULTRA = 5;

const SHADOW_QUALITY_MAX = 6;

#desc Reflection probe will update reflections once and then stop.
const REFLECTION_PROBE_UPDATE_ONCE = 0;

#desc Reflection probe will update each frame. This mode is necessary to capture moving objects.
const REFLECTION_PROBE_UPDATE_ALWAYS = 1;

const REFLECTION_PROBE_AMBIENT_DISABLED = 0;

const REFLECTION_PROBE_AMBIENT_ENVIRONMENT = 1;

const REFLECTION_PROBE_AMBIENT_COLOR = 2;

const DECAL_TEXTURE_ALBEDO = 0;

const DECAL_TEXTURE_NORMAL = 1;

const DECAL_TEXTURE_ORM = 2;

const DECAL_TEXTURE_EMISSION = 3;

const DECAL_TEXTURE_MAX = 4;

#desc Nearest-neighbor filter for decals (use for pixel art decals). No mipmaps are used for rendering, which means decals at a distance will look sharp but grainy. This has roughly the same performance cost as using mipmaps.
const DECAL_FILTER_NEAREST = 0;

#desc Linear filter for decals (use for non-pixel art decals). No mipmaps are used for rendering, which means decals at a distance will look smooth but blurry. This has roughly the same performance cost as using mipmaps.
const DECAL_FILTER_LINEAR = 1;

#desc Nearest-neighbor filter for decals (use for pixel art decals). Isotropic mipmaps are used for rendering, which means decals at a distance will look smooth but blurry. This has roughly the same performance cost as not using mipmaps.
const DECAL_FILTER_NEAREST_MIPMAPS = 2;

#desc Linear filter for decals (use for non-pixel art decals). Isotropic mipmaps are used for rendering, which means decals at a distance will look smooth but blurry. This has roughly the same performance cost as not using mipmaps.
const DECAL_FILTER_LINEAR_MIPMAPS = 3;

#desc Nearest-neighbor filter for decals (use for pixel art decals). Anisotropic mipmaps are used for rendering, which means decals at a distance will look smooth and sharp when viewed from oblique angles. This looks better compared to isotropic mipmaps, but is slower. The level of anisotropic filtering is defined by [member ProjectSettings.rendering/textures/default_filters/anisotropic_filtering_level].
const DECAL_FILTER_NEAREST_MIPMAPS_ANISOTROPIC = 4;

#desc Linear filter for decals (use for non-pixel art decals). Anisotropic mipmaps are used for rendering, which means decals at a distance will look smooth and sharp when viewed from oblique angles. This looks better compared to isotropic mipmaps, but is slower. The level of anisotropic filtering is defined by [member ProjectSettings.rendering/textures/default_filters/anisotropic_filtering_level].
const DECAL_FILTER_LINEAR_MIPMAPS_ANISOTROPIC = 5;

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

#desc Draw particles in the order that they appear in the particles array.
const PARTICLES_DRAW_ORDER_INDEX = 0;

#desc Sort particles based on their lifetime.
const PARTICLES_DRAW_ORDER_LIFETIME = 1;

const PARTICLES_DRAW_ORDER_REVERSE_LIFETIME = 2;

#desc Sort particles based on their distance to the camera.
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

#desc [FogVolume] will be shaped like an ellipsoid (stretched sphere).
const FOG_VOLUME_SHAPE_ELLIPSOID = 0;

#desc [FogVolume] will be shaped like a cone pointing upwards (in local coordinates). The cone's angle is set automatically to fill the extents. The cone will be adjusted to fit within the extents. Rotate the [FogVolume] node to reorient the cone. Non-uniform scaling via extents is not supported (scale the [FogVolume] node instead).
const FOG_VOLUME_SHAPE_CONE = 1;

#desc [FogVolume] will be shaped like an upright cylinder (in local coordinates). Rotate the [FogVolume] node to reorient the cylinder. The cylinder will be adjusted to fit within the extents. Non-uniform scaling via extents is not supported (scale the [FogVolume] node instead).
const FOG_VOLUME_SHAPE_CYLINDER = 2;

#desc [FogVolume] will be shaped like a box.
const FOG_VOLUME_SHAPE_BOX = 3;

#desc [FogVolume] will have no shape, will cover the whole world and will not be culled.
const FOG_VOLUME_SHAPE_WORLD = 4;

const FOG_VOLUME_SHAPE_MAX = 5;

#desc Use bilinear scaling for the viewport's 3D buffer. The amount of scaling can be set using [member Viewport.scaling_3d_scale]. Values less then [code]1.0[/code] will result in undersampling while values greater than [code]1.0[/code] will result in supersampling. A value of [code]1.0[/code] disables scaling.
const VIEWPORT_SCALING_3D_MODE_BILINEAR = 0;

#desc Use AMD FidelityFX Super Resolution 1.0 upscaling for the viewport's 3D buffer. The amount of scaling can be set using [member Viewport.scaling_3d_scale]. Values less then [code]1.0[/code] will be result in the viewport being upscaled using FSR. Values greater than [code]1.0[/code] are not supported and bilinear downsampling will be used instead. A value of [code]1.0[/code] disables scaling.
const VIEWPORT_SCALING_3D_MODE_FSR = 1;

const VIEWPORT_SCALING_3D_MODE_MAX = 2;

#desc Do not update the viewport.
const VIEWPORT_UPDATE_DISABLED = 0;

#desc Update the viewport once then set to disabled.
const VIEWPORT_UPDATE_ONCE = 1;

#desc Update the viewport whenever it is visible.
const VIEWPORT_UPDATE_WHEN_VISIBLE = 2;

const VIEWPORT_UPDATE_WHEN_PARENT_VISIBLE = 3;

#desc Always update the viewport.
const VIEWPORT_UPDATE_ALWAYS = 4;

#desc The viewport is always cleared before drawing.
const VIEWPORT_CLEAR_ALWAYS = 0;

#desc The viewport is never cleared before drawing.
const VIEWPORT_CLEAR_NEVER = 1;

#desc The viewport is cleared once, then the clear mode is set to [constant VIEWPORT_CLEAR_NEVER].
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

#desc Multisample antialiasing for 3D is disabled. This is the default value, and also the fastest setting.
const VIEWPORT_MSAA_DISABLED = 0;

#desc Multisample antialiasing uses 2 samples per pixel for 3D. This has a moderate impact on performance.
const VIEWPORT_MSAA_2X = 1;

#desc Multisample antialiasing uses 4 samples per pixel for 3D. This has a high impact on performance.
const VIEWPORT_MSAA_4X = 2;

#desc Multisample antialiasing uses 8 samples per pixel for 3D. This has a very high impact on performance. Likely unsupported on low-end and older hardware.
const VIEWPORT_MSAA_8X = 3;

const VIEWPORT_MSAA_MAX = 4;

const VIEWPORT_SCREEN_SPACE_AA_DISABLED = 0;

const VIEWPORT_SCREEN_SPACE_AA_FXAA = 1;

const VIEWPORT_SCREEN_SPACE_AA_MAX = 2;

const VIEWPORT_OCCLUSION_BUILD_QUALITY_LOW = 0;

const VIEWPORT_OCCLUSION_BUILD_QUALITY_MEDIUM = 1;

const VIEWPORT_OCCLUSION_BUILD_QUALITY_HIGH = 2;

#desc Number of objects drawn in a single frame.
const VIEWPORT_RENDER_INFO_OBJECTS_IN_FRAME = 0;

#desc Number of vertices drawn in a single frame.
const VIEWPORT_RENDER_INFO_PRIMITIVES_IN_FRAME = 1;

#desc Number of draw calls during this frame.
const VIEWPORT_RENDER_INFO_DRAW_CALLS_IN_FRAME = 2;

#desc Represents the size of the [enum ViewportRenderInfo] enum.
const VIEWPORT_RENDER_INFO_MAX = 3;

const VIEWPORT_RENDER_INFO_TYPE_VISIBLE = 0;

const VIEWPORT_RENDER_INFO_TYPE_SHADOW = 1;

const VIEWPORT_RENDER_INFO_TYPE_MAX = 2;

#desc Debug draw is disabled. Default setting.
const VIEWPORT_DEBUG_DRAW_DISABLED = 0;

#desc Objects are displayed without light information.
const VIEWPORT_DEBUG_DRAW_UNSHADED = 1;

#desc Objects are displayed with only light information.
const VIEWPORT_DEBUG_DRAW_LIGHTING = 2;

#desc Objects are displayed semi-transparent with additive blending so you can see where they are drawing over top of one another. A higher overdraw (represented by brighter colors) means you are wasting performance on drawing pixels that are being hidden behind others.
#desc [b]Note:[/b] When using this debug draw mode, custom shaders will be ignored. This means vertex displacement won't be visible anymore.
const VIEWPORT_DEBUG_DRAW_OVERDRAW = 3;

#desc Debug draw draws objects in wireframe.
const VIEWPORT_DEBUG_DRAW_WIREFRAME = 4;

#desc Normal buffer is drawn instead of regular scene so you can see the per-pixel normals that will be used by post-processing effects.
const VIEWPORT_DEBUG_DRAW_NORMAL_BUFFER = 5;

#desc Objects are displayed with only the albedo value from [VoxelGI]s.
const VIEWPORT_DEBUG_DRAW_VOXEL_GI_ALBEDO = 6;

#desc Objects are displayed with only the lighting value from [VoxelGI]s.
const VIEWPORT_DEBUG_DRAW_VOXEL_GI_LIGHTING = 7;

#desc Objects are displayed with only the emission color from [VoxelGI]s.
const VIEWPORT_DEBUG_DRAW_VOXEL_GI_EMISSION = 8;

#desc Draws the shadow atlas that stores shadows from [OmniLight3D]s and [SpotLight3D]s in the upper left quadrant of the [Viewport].
const VIEWPORT_DEBUG_DRAW_SHADOW_ATLAS = 9;

#desc Draws the shadow atlas that stores shadows from [DirectionalLight3D]s in the upper left quadrant of the [Viewport].
const VIEWPORT_DEBUG_DRAW_DIRECTIONAL_SHADOW_ATLAS = 10;

const VIEWPORT_DEBUG_DRAW_SCENE_LUMINANCE = 11;

#desc Draws the screen space ambient occlusion texture instead of the scene so that you can clearly see how it is affecting objects. In order for this display mode to work, you must have [member Environment.ssao_enabled] set in your [WorldEnvironment].
const VIEWPORT_DEBUG_DRAW_SSAO = 12;

#desc Draws the screen space indirect lighting texture instead of the scene so that you can clearly see how it is affecting objects. In order for this display mode to work, you must have [member Environment.ssil_enabled] set in your [WorldEnvironment].
const VIEWPORT_DEBUG_DRAW_SSIL = 13;

#desc Colors each PSSM split for the [DirectionalLight3D]s in the scene a different color so you can see where the splits are. In order they will be colored red, green, blue, yellow.
const VIEWPORT_DEBUG_DRAW_PSSM_SPLITS = 14;

const VIEWPORT_DEBUG_DRAW_DECAL_ATLAS = 15;

const VIEWPORT_DEBUG_DRAW_SDFGI = 16;

const VIEWPORT_DEBUG_DRAW_SDFGI_PROBES = 17;

const VIEWPORT_DEBUG_DRAW_GI_BUFFER = 18;

const VIEWPORT_DEBUG_DRAW_DISABLE_LOD = 19;

const VIEWPORT_DEBUG_DRAW_CLUSTER_OMNI_LIGHTS = 20;

const VIEWPORT_DEBUG_DRAW_CLUSTER_SPOT_LIGHTS = 21;

const VIEWPORT_DEBUG_DRAW_CLUSTER_DECALS = 22;

const VIEWPORT_DEBUG_DRAW_CLUSTER_REFLECTION_PROBES = 23;

const VIEWPORT_DEBUG_DRAW_OCCLUDERS = 24;

const VIEWPORT_DEBUG_DRAW_MOTION_VECTORS = 25;

#desc VRS is disabled.
const VIEWPORT_VRS_DISABLED = 0;

#desc VRS uses a texture. Note, for stereoscopic use a texture atlas with a texture for each view.
const VIEWPORT_VRS_TEXTURE = 1;

#desc VRS texture is supplied by the primary [XRInterface].
const VIEWPORT_VRS_XR = 2;

#desc Represents the size of the [enum ViewportVRSMode] enum.
const VIEWPORT_VRS_MAX = 3;

const SKY_MODE_AUTOMATIC = 0;

#desc Uses high quality importance sampling to process the radiance map. In general, this results in much higher quality than [constant Sky.PROCESS_MODE_REALTIME] but takes much longer to generate. This should not be used if you plan on changing the sky at runtime. If you are finding that the reflection is not blurry enough and is showing sparkles or fireflies, try increasing [member ProjectSettings.rendering/reflections/sky_reflections/ggx_samples].
const SKY_MODE_QUALITY = 1;

const SKY_MODE_INCREMENTAL = 2;

#desc Uses the fast filtering algorithm to process the radiance map. In general this results in lower quality, but substantially faster run times.
#desc [b]Note:[/b] The fast filtering algorithm is limited to 256x256 cubemaps, so [member Sky.radiance_size] must be set to [constant Sky.RADIANCE_SIZE_256].
const SKY_MODE_REALTIME = 3;

#desc Use the clear color as background.
const ENV_BG_CLEAR_COLOR = 0;

#desc Use a specified color as the background.
const ENV_BG_COLOR = 1;

#desc Use a sky resource for the background.
const ENV_BG_SKY = 2;

#desc Use a specified canvas layer as the background. This can be useful for instantiating a 2D scene in a 3D world.
const ENV_BG_CANVAS = 3;

#desc Do not clear the background, use whatever was rendered last frame as the background.
const ENV_BG_KEEP = 4;

#desc Displays a camera feed in the background.
const ENV_BG_CAMERA_FEED = 5;

#desc Represents the size of the [enum EnvironmentBG] enum.
const ENV_BG_MAX = 6;

#desc Gather ambient light from whichever source is specified as the background.
const ENV_AMBIENT_SOURCE_BG = 0;

#desc Disable ambient light.
const ENV_AMBIENT_SOURCE_DISABLED = 1;

#desc Specify a specific [Color] for ambient light.
const ENV_AMBIENT_SOURCE_COLOR = 2;

#desc Gather ambient light from the [Sky] regardless of what the background is.
const ENV_AMBIENT_SOURCE_SKY = 3;

#desc Use the background for reflections.
const ENV_REFLECTION_SOURCE_BG = 0;

#desc Disable reflections.
const ENV_REFLECTION_SOURCE_DISABLED = 1;

#desc Use the [Sky] for reflections regardless of what the background is.
const ENV_REFLECTION_SOURCE_SKY = 2;

#desc Additive glow blending mode. Mostly used for particles, glows (bloom), lens flare, bright sources.
const ENV_GLOW_BLEND_MODE_ADDITIVE = 0;

#desc Screen glow blending mode. Increases brightness, used frequently with bloom.
const ENV_GLOW_BLEND_MODE_SCREEN = 1;

#desc Soft light glow blending mode. Modifies contrast, exposes shadows and highlights (vivid bloom).
const ENV_GLOW_BLEND_MODE_SOFTLIGHT = 2;

#desc Replace glow blending mode. Replaces all pixels' color by the glow value. This can be used to simulate a full-screen blur effect by tweaking the glow parameters to match the original image's brightness.
const ENV_GLOW_BLEND_MODE_REPLACE = 3;

#desc Mixes the glow with the underlying color to avoid increasing brightness as much while still maintaining a glow effect.
const ENV_GLOW_BLEND_MODE_MIX = 4;

#desc Output color as they came in. This can cause bright lighting to look blown out, with noticeable clipping in the output colors.
const ENV_TONE_MAPPER_LINEAR = 0;

#desc Use the Reinhard tonemapper. Performs a variation on rendered pixels' colors by this formula: [code]color = color / (1 + color)[/code]. This avoids clipping bright highlights, but the resulting image can look a bit dull.
const ENV_TONE_MAPPER_REINHARD = 1;

#desc Use the filmic tonemapper. This avoids clipping bright highlights, with a resulting image that usually looks more vivid than [constant ENV_TONE_MAPPER_REINHARD].
const ENV_TONE_MAPPER_FILMIC = 2;

#desc Use the Academy Color Encoding System tonemapper. ACES is slightly more expensive than other options, but it handles bright lighting in a more realistic fashion by desaturating it as it becomes brighter. ACES typically has a more contrasted output compared to [constant ENV_TONE_MAPPER_REINHARD] and [constant ENV_TONE_MAPPER_FILMIC].
#desc [b]Note:[/b] This tonemapping operator is called "ACES Fitted" in Godot 3.x.
const ENV_TONE_MAPPER_ACES = 3;

#desc Lowest quality of roughness filter for screen-space reflections. Rough materials will not have blurrier screen-space reflections compared to smooth (non-rough) materials. This is the fastest option.
const ENV_SSR_ROUGHNESS_QUALITY_DISABLED = 0;

#desc Low quality of roughness filter for screen-space reflections.
const ENV_SSR_ROUGHNESS_QUALITY_LOW = 1;

#desc Medium quality of roughness filter for screen-space reflections.
const ENV_SSR_ROUGHNESS_QUALITY_MEDIUM = 2;

#desc High quality of roughness filter for screen-space reflections. This is the slowest option.
const ENV_SSR_ROUGHNESS_QUALITY_HIGH = 3;

#desc Lowest quality of screen-space ambient occlusion.
const ENV_SSAO_QUALITY_VERY_LOW = 0;

#desc Low quality screen-space ambient occlusion.
const ENV_SSAO_QUALITY_LOW = 1;

#desc Medium quality screen-space ambient occlusion.
const ENV_SSAO_QUALITY_MEDIUM = 2;

#desc High quality screen-space ambient occlusion.
const ENV_SSAO_QUALITY_HIGH = 3;

#desc Highest quality screen-space ambient occlusion. Uses the adaptive target setting which can be dynamically adjusted to smoothly balance performance and visual quality.
const ENV_SSAO_QUALITY_ULTRA = 4;

#desc Lowest quality of screen-space indirect lighting.
const ENV_SSIL_QUALITY_VERY_LOW = 0;

#desc Low quality screen-space indirect lighting.
const ENV_SSIL_QUALITY_LOW = 1;

#desc High quality screen-space indirect lighting.
const ENV_SSIL_QUALITY_MEDIUM = 2;

#desc High quality screen-space indirect lighting.
const ENV_SSIL_QUALITY_HIGH = 3;

#desc Highest quality screen-space indirect lighting. Uses the adaptive target setting which can be dynamically adjusted to smoothly balance performance and visual quality.
const ENV_SSIL_QUALITY_ULTRA = 4;

const ENV_SDFGI_Y_SCALE_50_PERCENT = 0;

const ENV_SDFGI_Y_SCALE_75_PERCENT = 1;

const ENV_SDFGI_Y_SCALE_100_PERCENT = 2;

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

#desc Calculate the DOF blur using a box filter. The fastest option, but results in obvious lines in blur pattern.
const DOF_BOKEH_BOX = 0;

#desc Calculates DOF blur using a hexagon shaped filter.
const DOF_BOKEH_HEXAGON = 1;

#desc Calculates DOF blur using a circle shaped filter. Best quality and most realistic, but slowest. Use only for areas where a lot of performance can be dedicated to post-processing (e.g. cutscenes).
const DOF_BOKEH_CIRCLE = 2;

#desc Lowest quality DOF blur. This is the fastest setting, but you may be able to see filtering artifacts.
const DOF_BLUR_QUALITY_VERY_LOW = 0;

#desc Low quality DOF blur.
const DOF_BLUR_QUALITY_LOW = 1;

#desc Medium quality DOF blur.
const DOF_BLUR_QUALITY_MEDIUM = 2;

#desc Highest quality DOF blur. Results in the smoothest looking blur by taking the most samples, but is also significantly slower.
const DOF_BLUR_QUALITY_HIGH = 3;

#desc The instance does not have a type.
const INSTANCE_NONE = 0;

#desc The instance is a mesh.
const INSTANCE_MESH = 1;

#desc The instance is a multimesh.
const INSTANCE_MULTIMESH = 2;

#desc The instance is a particle emitter.
const INSTANCE_PARTICLES = 3;

const INSTANCE_PARTICLES_COLLISION = 4;

#desc The instance is a light.
const INSTANCE_LIGHT = 5;

#desc The instance is a reflection probe.
const INSTANCE_REFLECTION_PROBE = 6;

#desc The instance is a decal.
const INSTANCE_DECAL = 7;

#desc The instance is a VoxelGI.
const INSTANCE_VOXEL_GI = 8;

#desc The instance is a lightmap.
const INSTANCE_LIGHTMAP = 9;

const INSTANCE_OCCLUDER = 10;

const INSTANCE_VISIBLITY_NOTIFIER = 11;

const INSTANCE_FOG_VOLUME = 12;

#desc Represents the size of the [enum InstanceType] enum.
const INSTANCE_MAX = 13;

#desc A combination of the flags of geometry instances (mesh, multimesh, immediate and particles).
const INSTANCE_GEOMETRY_MASK = 14;

#desc Allows the instance to be used in baked lighting.
const INSTANCE_FLAG_USE_BAKED_LIGHT = 0;

#desc Allows the instance to be used with dynamic global illumination.
const INSTANCE_FLAG_USE_DYNAMIC_GI = 1;

#desc When set, manually requests to draw geometry on next frame.
const INSTANCE_FLAG_DRAW_NEXT_FRAME_IF_VISIBLE = 2;

const INSTANCE_FLAG_IGNORE_OCCLUSION_CULLING = 3;

#desc Represents the size of the [enum InstanceFlags] enum.
const INSTANCE_FLAG_MAX = 4;

#desc Disable shadows from this instance.
const SHADOW_CASTING_SETTING_OFF = 0;

#desc Cast shadows from this instance.
const SHADOW_CASTING_SETTING_ON = 1;

#desc Disable backface culling when rendering the shadow of the object. This is slightly slower but may result in more correct shadows.
const SHADOW_CASTING_SETTING_DOUBLE_SIDED = 2;

#desc Only render the shadows from the object. The object itself will not be drawn.
const SHADOW_CASTING_SETTING_SHADOWS_ONLY = 3;

#desc Disable visibility range fading for the given instance.
const VISIBILITY_RANGE_FADE_DISABLED = 0;

#desc Fade-out the given instance when it approaches its visibility range limits.
const VISIBILITY_RANGE_FADE_SELF = 1;

#desc Fade-in the given instance's dependencies when reaching its visibility range limits.
const VISIBILITY_RANGE_FADE_DEPENDENCIES = 2;

const BAKE_CHANNEL_ALBEDO_ALPHA = 0;

const BAKE_CHANNEL_NORMAL = 1;

const BAKE_CHANNEL_ORM = 2;

const BAKE_CHANNEL_EMISSION = 3;

const CANVAS_TEXTURE_CHANNEL_DIFFUSE = 0;

const CANVAS_TEXTURE_CHANNEL_NORMAL = 1;

const CANVAS_TEXTURE_CHANNEL_SPECULAR = 2;

#desc The nine patch gets stretched where needed.
const NINE_PATCH_STRETCH = 0;

#desc The nine patch gets filled with tiles where needed.
const NINE_PATCH_TILE = 1;

#desc The nine patch gets filled with tiles where needed and stretches them a bit if needed.
const NINE_PATCH_TILE_FIT = 2;

#desc Uses the default filter mode for this [Viewport].
const CANVAS_ITEM_TEXTURE_FILTER_DEFAULT = 0;

#desc The texture filter reads from the nearest pixel only. The simplest and fastest method of filtering, but the texture will look pixelized.
const CANVAS_ITEM_TEXTURE_FILTER_NEAREST = 1;

#desc The texture filter blends between the nearest 4 pixels. Use this when you want to avoid a pixelated style, but do not want mipmaps.
const CANVAS_ITEM_TEXTURE_FILTER_LINEAR = 2;

#desc The texture filter reads from the nearest pixel in the nearest mipmap. The fastest way to read from textures with mipmaps.
const CANVAS_ITEM_TEXTURE_FILTER_NEAREST_WITH_MIPMAPS = 3;

#desc The texture filter blends between the nearest 4 pixels and between the nearest 2 mipmaps.
const CANVAS_ITEM_TEXTURE_FILTER_LINEAR_WITH_MIPMAPS = 4;

#desc The texture filter reads from the nearest pixel, but selects a mipmap based on the angle between the surface and the camera view. This reduces artifacts on surfaces that are almost in line with the camera.
const CANVAS_ITEM_TEXTURE_FILTER_NEAREST_WITH_MIPMAPS_ANISOTROPIC = 5;

#desc The texture filter blends between the nearest 4 pixels and selects a mipmap based on the angle between the surface and the camera view. This reduces artifacts on surfaces that are almost in line with the camera. This is the slowest of the filtering options, but results in the highest quality texturing.
const CANVAS_ITEM_TEXTURE_FILTER_LINEAR_WITH_MIPMAPS_ANISOTROPIC = 6;

#desc Max value for [enum CanvasItemTextureFilter] enum.
const CANVAS_ITEM_TEXTURE_FILTER_MAX = 7;

#desc Uses the default repeat mode for this [Viewport].
const CANVAS_ITEM_TEXTURE_REPEAT_DEFAULT = 0;

#desc Disables textures repeating. Instead, when reading UVs outside the 0-1 range, the value will be clamped to the edge of the texture, resulting in a stretched out look at the borders of the texture.
const CANVAS_ITEM_TEXTURE_REPEAT_DISABLED = 1;

#desc Enables the texture to repeat when UV coordinates are outside the 0-1 range. If using one of the linear filtering modes, this can result in artifacts at the edges of a texture when the sampler filters across the edges of the texture.
const CANVAS_ITEM_TEXTURE_REPEAT_ENABLED = 2;

#desc Flip the texture when repeating so that the edge lines up instead of abruptly changing.
const CANVAS_ITEM_TEXTURE_REPEAT_MIRROR = 3;

#desc Max value for [enum CanvasItemTextureRepeat] enum.
const CANVAS_ITEM_TEXTURE_REPEAT_MAX = 4;

const CANVAS_GROUP_MODE_DISABLED = 0;

const CANVAS_GROUP_MODE_OPAQUE = 1;

const CANVAS_GROUP_MODE_TRANSPARENT = 2;

const CANVAS_LIGHT_MODE_POINT = 0;

const CANVAS_LIGHT_MODE_DIRECTIONAL = 1;

#desc Adds light color additive to the canvas.
const CANVAS_LIGHT_BLEND_MODE_ADD = 0;

#desc Adds light color subtractive to the canvas.
const CANVAS_LIGHT_BLEND_MODE_SUB = 1;

#desc The light adds color depending on transparency.
const CANVAS_LIGHT_BLEND_MODE_MIX = 2;

#desc Do not apply a filter to canvas light shadows.
const CANVAS_LIGHT_FILTER_NONE = 0;

#desc Use PCF5 filtering to filter canvas light shadows.
const CANVAS_LIGHT_FILTER_PCF5 = 1;

#desc Use PCF13 filtering to filter canvas light shadows.
const CANVAS_LIGHT_FILTER_PCF13 = 2;

#desc Max value of the [enum CanvasLightShadowFilter] enum.
const CANVAS_LIGHT_FILTER_MAX = 3;

#desc Culling of the canvas occluder is disabled.
const CANVAS_OCCLUDER_POLYGON_CULL_DISABLED = 0;

#desc Culling of the canvas occluder is clockwise.
const CANVAS_OCCLUDER_POLYGON_CULL_CLOCKWISE = 1;

#desc Culling of the canvas occluder is counterclockwise.
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

#desc Hardware supports shaders. This enum is currently unused in Godot 3.x.
const FEATURE_SHADERS = 0;

#desc Hardware supports multithreading. This enum is currently unused in Godot 3.x.
const FEATURE_MULTITHREADED = 1;


#desc If [code]false[/code], disables rendering completely, but the engine logic is still being processed. You can call [method force_draw] to draw a frame even with rendering disabled.
var render_loop_enabled: bool;



func bake_render_uv2(base: RID, material_overrides: RID[], image_size: Vector2i) -> Image[]:
	pass;

#desc Creates a camera attributes object and adds it to the RenderingServer. It can be accessed with the RID that is returned. This RID will be used in all [code]camera_attributes_[/code] RenderingServer functions.
#desc Once finished with your RID, you will want to free the RID using the RenderingServer's [method free_rid] static method.
func camera_attributes_create() -> RID:
	pass;

func camera_attributes_set_auto_exposure(camera_attributes: RID, enable: bool, min_sensitivity: float, max_sensitivity: float, speed: float, scale: float) -> void:
	pass;

func camera_attributes_set_dof_blur(camera_attributes: RID, far_enable: bool, far_distance: float, far_transition: float, near_enable: bool, near_distance: float, near_transition: float, amount: float) -> void:
	pass;

func camera_attributes_set_dof_blur_bokeh_shape(shape: int) -> void:
	pass;

func camera_attributes_set_dof_blur_quality(quality: int, use_jitter: bool) -> void:
	pass;

#desc Sets the exposure values that will be used by the renderers. The normalization amount is used to bake a given Exposure Value (EV) into rendering calculations to reduce the dynamic range of the scene.
#desc The normalization factor can be calculated from exposure value (EV100) as follows:
#desc [codeblock]
#desc func get_exposure_normalization(float ev100):
#desc return 1.0 / (pow(2.0, ev100) * 1.2)
#desc [/codeblock]
#desc The exposure value can be calculated from aperture (in f-stops), shutter speed (in seconds), and sensitivity (in ISO) as follows:
#desc [codeblock]
#desc func get_exposure(float aperture, float shutter_speed, float sensitivity):
#desc return log2((aperture * aperture) / shutterSpeed * (100.0 / sensitivity))
#desc [/codeblock]
func camera_attributes_set_exposure(camera_attributes: RID, multiplier: float, normalization: float) -> void:
	pass;

#desc Creates a camera and adds it to the RenderingServer. It can be accessed with the RID that is returned. This RID will be used in all [code]camera_*[/code] RenderingServer functions.
#desc Once finished with your RID, you will want to free the RID using the RenderingServer's [method free_rid] static method.
func camera_create() -> RID:
	pass;

func camera_set_camera_attributes(camera: RID, effects: RID) -> void:
	pass;

#desc Sets the cull mask associated with this camera. The cull mask describes which 3D layers are rendered by this camera. Equivalent to [member Camera3D.cull_mask].
func camera_set_cull_mask(camera: RID, layers: int) -> void:
	pass;

#desc Sets the environment used by this camera. Equivalent to [member Camera3D.environment].
func camera_set_environment(camera: RID, env: RID) -> void:
	pass;

#desc Sets camera to use frustum projection. This mode allows adjusting the [param offset] argument to create "tilted frustum" effects.
func camera_set_frustum(camera: RID, size: float, offset: Vector2, z_near: float, z_far: float) -> void:
	pass;

#desc Sets camera to use orthogonal projection, also known as orthographic projection. Objects remain the same size on the screen no matter how far away they are.
func camera_set_orthogonal(camera: RID, size: float, z_near: float, z_far: float) -> void:
	pass;

#desc Sets camera to use perspective projection. Objects on the screen becomes smaller when they are far away.
func camera_set_perspective(camera: RID, fovy_degrees: float, z_near: float, z_far: float) -> void:
	pass;

#desc Sets [Transform3D] of camera.
func camera_set_transform(camera: RID, transform: Transform3D) -> void:
	pass;

#desc If [code]true[/code], preserves the horizontal aspect ratio which is equivalent to [constant Camera3D.KEEP_WIDTH]. If [code]false[/code], preserves the vertical aspect ratio which is equivalent to [constant Camera3D.KEEP_HEIGHT].
func camera_set_use_vertical_aspect(camera: RID, enable: bool) -> void:
	pass;

#desc Creates a canvas and returns the assigned [RID]. It can be accessed with the RID that is returned. This RID will be used in all [code]canvas_*[/code] RenderingServer functions.
#desc Once finished with your RID, you will want to free the RID using the RenderingServer's [method free_rid] static method.
func canvas_create() -> RID:
	pass;

#desc Subsequent drawing commands will be ignored unless they fall within the specified animation slice. This is a faster way to implement animations that loop on background rather than redrawing constantly.
func canvas_item_add_animation_slice(item: RID, animation_length: float, slice_begin: float, slice_end: float, offset: float) -> void:
	pass;

func canvas_item_add_circle(item: RID, pos: Vector2, radius: float, color: Color) -> void:
	pass;

func canvas_item_add_clip_ignore(item: RID, ignore: bool) -> void:
	pass;

func canvas_item_add_lcd_texture_rect_region(item: RID, rect: Rect2, texture: RID, src_rect: Rect2, modulate: Color) -> void:
	pass;

func canvas_item_add_line(item: RID, from: Vector2, to: Vector2, color: Color, width: float, antialiased: bool) -> void:
	pass;

func canvas_item_add_mesh(item: RID, mesh: RID, transform: Transform2D, modulate: Color, texture: RID) -> void:
	pass;

func canvas_item_add_msdf_texture_rect_region(item: RID, rect: Rect2, texture: RID, src_rect: Rect2, modulate: Color, outline_size: int, px_range: float) -> void:
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

#desc Clears the [CanvasItem] and removes all commands in it.
func canvas_item_clear(item: RID) -> void:
	pass;

func canvas_item_create() -> RID:
	pass;

func canvas_item_set_canvas_group_mode(item: RID, mode: int, clear_margin: float, fit_empty: bool, fit_margin: float, blur_mipmaps: bool) -> void:
	pass;

func canvas_item_set_clip(item: RID, clip: bool) -> void:
	pass;

#desc Sets the [CanvasItem] to copy a rect to the backbuffer.
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

#desc Sets the index for the [CanvasItem].
func canvas_item_set_draw_index(item: RID, index: int) -> void:
	pass;

func canvas_item_set_light_mask(item: RID, mask: int) -> void:
	pass;

#desc Sets a new material to the [CanvasItem].
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

#desc Sets if the [CanvasItem] uses its parent's material.
func canvas_item_set_use_parent_material(item: RID, enabled: bool) -> void:
	pass;

func canvas_item_set_visibility_notifier(item: RID, enable: bool, area: Rect2, enter_callable: Callable, exit_callable: Callable) -> void:
	pass;

func canvas_item_set_visible(item: RID, visible: bool) -> void:
	pass;

#desc If this is enabled, the Z index of the parent will be added to the children's Z index.
func canvas_item_set_z_as_relative_to_parent(item: RID, enabled: bool) -> void:
	pass;

#desc Sets the [CanvasItem]'s Z index, i.e. its draw order (lower indexes are drawn first).
func canvas_item_set_z_index(item: RID, z_index: int) -> void:
	pass;

#desc Attaches the canvas light to the canvas. Removes it from its previous canvas.
func canvas_light_attach_to_canvas(light: RID, canvas: RID) -> void:
	pass;

#desc Creates a canvas light and adds it to the RenderingServer. It can be accessed with the RID that is returned. This RID will be used in all [code]canvas_light_*[/code] RenderingServer functions.
#desc Once finished with your RID, you will want to free the RID using the RenderingServer's [method free_rid] static method.
func canvas_light_create() -> RID:
	pass;

#desc Attaches a light occluder to the canvas. Removes it from its previous canvas.
func canvas_light_occluder_attach_to_canvas(occluder: RID, canvas: RID) -> void:
	pass;

#desc Creates a light occluder and adds it to the RenderingServer. It can be accessed with the RID that is returned. This RID will be used in all [code]canvas_light_ocluder_*[/code] RenderingServer functions.
#desc Once finished with your RID, you will want to free the RID using the RenderingServer's [method free_rid] static method.
func canvas_light_occluder_create() -> RID:
	pass;

func canvas_light_occluder_set_as_sdf_collision(occluder: RID, enable: bool) -> void:
	pass;

#desc Enables or disables light occluder.
func canvas_light_occluder_set_enabled(occluder: RID, enabled: bool) -> void:
	pass;

#desc The light mask. See [LightOccluder2D] for more information on light masks.
func canvas_light_occluder_set_light_mask(occluder: RID, mask: int) -> void:
	pass;

#desc Sets a light occluder's polygon.
func canvas_light_occluder_set_polygon(occluder: RID, polygon: RID) -> void:
	pass;

#desc Sets a light occluder's [Transform2D].
func canvas_light_occluder_set_transform(occluder: RID, transform: Transform2D) -> void:
	pass;

#desc Sets the color for a light.
func canvas_light_set_color(light: RID, color: Color) -> void:
	pass;

#desc Enables or disables a canvas light.
func canvas_light_set_enabled(light: RID, enabled: bool) -> void:
	pass;

#desc Sets a canvas light's energy.
func canvas_light_set_energy(light: RID, energy: float) -> void:
	pass;

#desc Sets a canvas light's height.
func canvas_light_set_height(light: RID, height: float) -> void:
	pass;

#desc The light mask. See [LightOccluder2D] for more information on light masks.
func canvas_light_set_item_cull_mask(light: RID, mask: int) -> void:
	pass;

#desc The binary mask used to determine which layers this canvas light's shadows affects. See [LightOccluder2D] for more information on light masks.
func canvas_light_set_item_shadow_cull_mask(light: RID, mask: int) -> void:
	pass;

#desc The layer range that gets rendered with this light.
func canvas_light_set_layer_range(light: RID, min_layer: int, max_layer: int) -> void:
	pass;

#desc The mode of the light, see [enum CanvasLightMode] constants.
func canvas_light_set_mode(light: RID, mode: int) -> void:
	pass;

#desc Sets the color of the canvas light's shadow.
func canvas_light_set_shadow_color(light: RID, color: Color) -> void:
	pass;

#desc Enables or disables the canvas light's shadow.
func canvas_light_set_shadow_enabled(light: RID, enabled: bool) -> void:
	pass;

#desc Sets the canvas light's shadow's filter, see [enum CanvasLightShadowFilter] constants.
func canvas_light_set_shadow_filter(light: RID, filter: int) -> void:
	pass;

#desc Smoothens the shadow. The lower, the smoother.
func canvas_light_set_shadow_smooth(light: RID, smooth: float) -> void:
	pass;

#desc Sets the texture to be used by a [PointLight2D]. Equivalent to [member PointLight2D.texture].
func canvas_light_set_texture(light: RID, texture: RID) -> void:
	pass;

#desc Sets the offset of a [PointLight2D]'s texture. Equivalent to [member PointLight2D.offset].
func canvas_light_set_texture_offset(light: RID, offset: Vector2) -> void:
	pass;

#desc Sets the scale factor of a [PointLight2D]'s texture. Equivalent to [member PointLight2D.texture_scale].
func canvas_light_set_texture_scale(light: RID, scale: float) -> void:
	pass;

#desc Sets the canvas light's [Transform2D].
func canvas_light_set_transform(light: RID, transform: Transform2D) -> void:
	pass;

#desc Sets the Z range of objects that will be affected by this light. Equivalent to [member Light2D.range_z_min] and [member Light2D.range_z_max].
func canvas_light_set_z_range(light: RID, min_z: int, max_z: int) -> void:
	pass;

#desc Creates a new light occluder polygon and adds it to the RenderingServer. It can be accessed with the RID that is returned. This RID will be used in all [code]canvas_occluder_polygon_*[/code] RenderingServer functions.
#desc Once finished with your RID, you will want to free the RID using the RenderingServer's [method free_rid] static method.
func canvas_occluder_polygon_create() -> RID:
	pass;

#desc Sets an occluder polygons cull mode. See [enum CanvasOccluderPolygonCullMode] constants.
func canvas_occluder_polygon_set_cull_mode(occluder_polygon: RID, mode: int) -> void:
	pass;

#desc Sets the shape of the occluder polygon.
func canvas_occluder_polygon_set_shape(occluder_polygon: RID, shape: PackedVector2Array, closed: bool) -> void:
	pass;

func canvas_set_disable_scale(disable: bool) -> void:
	pass;

#desc A copy of the canvas item will be drawn with a local offset of the mirroring [Vector2].
func canvas_set_item_mirroring(canvas: RID, item: RID, mirroring: Vector2) -> void:
	pass;

#desc Modulates all colors in the given canvas.
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

func decals_set_filter(filter: int) -> void:
	pass;

#desc Creates a directional light and adds it to the RenderingServer. It can be accessed with the RID that is returned. This RID can be used in most [code]light_*[/code] RenderingServer functions.
#desc Once finished with your RID, you will want to free the RID using the RenderingServer's [method free_rid] static method.
#desc To place in a scene, attach this directional light to an instance using [method instance_set_base] using the returned RID.
func directional_light_create() -> RID:
	pass;

func directional_shadow_atlas_set_size(size: int, is_16bits: bool) -> void:
	pass;

func directional_soft_shadow_filter_set_quality(quality: int) -> void:
	pass;

func environment_bake_panorama(environment: RID, bake_irradiance: bool, size: Vector2i) -> Image:
	pass;

#desc Creates an environment and adds it to the RenderingServer. It can be accessed with the RID that is returned. This RID will be used in all [code]environment_*[/code] RenderingServer functions.
#desc Once finished with your RID, you will want to free the RID using the RenderingServer's [method free_rid] static method.
func environment_create() -> RID:
	pass;

func environment_glow_set_use_bicubic_upscale(enable: bool) -> void:
	pass;

func environment_glow_set_use_high_quality(enable: bool) -> void:
	pass;

#desc Sets the values to be used with the "Adjustment" post-process effect. See [Environment] for more details.
func environment_set_adjustment(env: RID, enable: bool, brightness: float, contrast: float, saturation: float, use_1d_color_correction: bool, color_correction: RID) -> void:
	pass;

func environment_set_ambient_light(env: RID, color: Color, ambient: int, energy: float, sky_contibution: float, reflection_source: int) -> void:
	pass;

#desc Sets the [i]BGMode[/i] of the environment. Equivalent to [member Environment.background_mode].
func environment_set_background(env: RID, bg: int) -> void:
	pass;

#desc Color displayed for clear areas of the scene (if using Custom color or Color+Sky background modes).
func environment_set_bg_color(env: RID, color: Color) -> void:
	pass;

#desc Sets the intensity of the background color.
func environment_set_bg_energy(env: RID, multiplier: float, exposure_value: float) -> void:
	pass;

#desc Sets the maximum layer to use if using Canvas background mode.
func environment_set_canvas_max_layer(env: RID, max_layer: int) -> void:
	pass;

func environment_set_fog(env: RID, enable: bool, light_color: Color, light_energy: float, sun_scatter: float, density: float, height: float, height_density: float, aerial_perspective: float, sky_affect: float) -> void:
	pass;

func environment_set_glow(env: RID, enable: bool, levels: PackedFloat32Array, intensity: float, strength: float, mix: float, bloom_threshold: float, blend_mode: int, hdr_bleed_threshold: float, hdr_bleed_scale: float, hdr_luminance_cap: float, glow_map_strength: float, glow_map: RID) -> void:
	pass;

func environment_set_sdfgi(env: RID, enable: bool, cascades: int, min_cell_size: float, y_scale: int, use_occlusion: bool, bounce_feedback: float, read_sky: bool, energy: float, normal_bias: float, probe_bias: float) -> void:
	pass;

func environment_set_sdfgi_frames_to_converge(frames: int) -> void:
	pass;

func environment_set_sdfgi_frames_to_update_light(frames: int) -> void:
	pass;

func environment_set_sdfgi_ray_count(ray_count: int) -> void:
	pass;

#desc Sets the [Sky] to be used as the environment's background when using [i]BGMode[/i] sky. Equivalent to [member Environment.sky].
func environment_set_sky(env: RID, sky: RID) -> void:
	pass;

#desc Sets a custom field of view for the background [Sky]. Equivalent to [member Environment.sky_custom_fov].
func environment_set_sky_custom_fov(env: RID, scale: float) -> void:
	pass;

#desc Sets the rotation of the background [Sky] expressed as a [Basis]. Equivalent to [member Environment.sky_rotation], where the rotation vector is used to construct the [Basis].
func environment_set_sky_orientation(env: RID, orientation: Basis) -> void:
	pass;

#desc Sets the variables to be used with the screen-space ambient occlusion (SSAO) post-process effect. See [Environment] for more details.
func environment_set_ssao(env: RID, enable: bool, radius: float, intensity: float, power: float, detail: float, horizon: float, sharpness: float, light_affect: float, ao_channel_affect: float) -> void:
	pass;

#desc Sets the quality level of the screen-space ambient occlusion (SSAO) post-process effect. See [Environment] for more details.
func environment_set_ssao_quality(quality: int, half_size: bool, adaptive_target: float, blur_passes: int, fadeout_from: float, fadeout_to: float) -> void:
	pass;

#desc Sets the quality level of the screen-space indirect lighting (SSIL) post-process effect. See [Environment] for more details.
func environment_set_ssil_quality(quality: int, half_size: bool, adaptive_target: float, blur_passes: int, fadeout_from: float, fadeout_to: float) -> void:
	pass;

#desc Sets the variables to be used with the "screen space reflections" post-process effect. See [Environment] for more details.
func environment_set_ssr(env: RID, enable: bool, max_steps: int, fade_in: float, fade_out: float, depth_tolerance: float) -> void:
	pass;

func environment_set_ssr_roughness_quality(quality: int) -> void:
	pass;

#desc Sets the variables to be used with the "tonemap" post-process effect. See [Environment] for more details.
func environment_set_tonemap(env: RID, tone_mapper: int, exposure: float, white: float) -> void:
	pass;

func environment_set_volumetric_fog(env: RID, enable: bool, density: float, albedo: Color, emission: Color, emission_energy: float, anisotropy: float, length: float, p_detail_spread: float, gi_inject: float, temporal_reprojection: bool, temporal_reprojection_amount: float, ambient_inject: float, sky_affect: float) -> void:
	pass;

#desc Enables filtering of the volumetric fog scattering buffer. This results in much smoother volumes with very few under-sampling artifacts.
func environment_set_volumetric_fog_filter_active(active: bool) -> void:
	pass;

#desc Sets the resolution of the volumetric fog's froxel buffer. [param size] is modified by the screen's aspect ratio and then used to set the width and height of the buffer. While [param depth] is directly used to set the depth of the buffer.
func environment_set_volumetric_fog_volume_size(size: int, depth: int) -> void:
	pass;

#desc Creates a new fog volume and allocates an RID.
func fog_volume_create() -> RID:
	pass;

#desc Sets the size of the fog volume when shape is [constant RenderingServer.FOG_VOLUME_SHAPE_ELLIPSOID], [constant RenderingServer.FOG_VOLUME_SHAPE_CONE], [constant RenderingServer.FOG_VOLUME_SHAPE_CYLINDER] or [constant RenderingServer.FOG_VOLUME_SHAPE_BOX].
func fog_volume_set_extents(fog_volume: RID, extents: Vector3) -> void:
	pass;

#desc Sets the [Material] of the fog volume. Can be either a [FogMaterial] or a custom [ShaderMaterial].
func fog_volume_set_material(fog_volume: RID, material: RID) -> void:
	pass;

#desc Sets the shape of the fog volume to either [constant RenderingServer.FOG_VOLUME_SHAPE_ELLIPSOID], [constant RenderingServer.FOG_VOLUME_SHAPE_CONE], [constant RenderingServer.FOG_VOLUME_SHAPE_CYLINDER], [constant RenderingServer.FOG_VOLUME_SHAPE_BOX] or [constant RenderingServer.FOG_VOLUME_SHAPE_WORLD].
func fog_volume_set_shape(fog_volume: RID, shape: int) -> void:
	pass;

func force_draw(swap_buffers: bool, frame_step: float) -> void:
	pass;

func force_sync() -> void:
	pass;

#desc Tries to free an object in the RenderingServer.
func free_rid(rid: RID) -> void:
	pass;

func get_frame_setup_time_cpu() -> float:
	pass;

func get_rendering_device() -> RenderingDevice:
	pass;

func get_rendering_info(info: int) -> int:
	pass;

#desc Returns the parameters of a shader.
func get_shader_parameter_list(shader: RID) -> Dictionary[]:
	pass;

#desc Returns the id of the test cube. Creates one if none exists.
func get_test_cube() -> RID:
	pass;

#desc Returns the id of the test texture. Creates one if none exists.
func get_test_texture() -> RID:
	pass;

#desc Returns the version of the graphics video adapter [i]currently in use[/i] (e.g. "1.2.189" for Vulkan, "3.3.0 NVIDIA 510.60.02" for OpenGL). This version may be different from the actual latest version supported by the hardware, as Godot may not always request the latest version.
#desc [b]Note:[/b] When running a headless or server binary, this function returns an empty string.
func get_video_adapter_api_version() -> String:
	pass;

#desc Returns the name of the video adapter (e.g. "GeForce GTX 1080/PCIe/SSE2").
#desc [b]Note:[/b] When running a headless or server binary, this function returns an empty string.
func get_video_adapter_name() -> String:
	pass;

#desc Returns the type of the video adapter. Since dedicated graphics cards from a given generation will [i]usually[/i] be significantly faster than integrated graphics made in the same generation, the device type can be used as a basis for automatic graphics settings adjustment. However, this is not always true, so make sure to provide users with a way to manually override graphics settings.
#desc [b]Note:[/b] When using the OpenGL backend or when running in headless mode, this function always returns [constant RenderingDevice.DEVICE_TYPE_OTHER].
func get_video_adapter_type() -> int:
	pass;

#desc Returns the vendor of the video adapter (e.g. "NVIDIA Corporation").
#desc [b]Note:[/b] When running a headless or server binary, this function returns an empty string.
func get_video_adapter_vendor() -> String:
	pass;

#desc Returns the id of a white texture. Creates one if none exists.
func get_white_texture() -> RID:
	pass;

#desc If [param half_resolution] is [code]true[/code], renders [VoxelGI] and SDFGI ([member Environment.sdfgi_enabled]) buffers at halved resolution (e.g. 960×540 when the viewport size is 1920×1080). This improves performance significantly when VoxelGI or SDFGI is enabled, at the cost of artifacts that may be visible on polygon edges. The loss in quality becomes less noticeable as the viewport resolution increases. [LightmapGI] rendering is not affected by this setting. See also [member ProjectSettings.rendering/global_illumination/gi/use_half_resolution].
func gi_set_use_half_resolution(half_resolution: bool) -> void:
	pass;

func global_shader_parameter_add(name: StringName, type: int, default_value: Variant) -> void:
	pass;

func global_shader_parameter_get(name: StringName) -> Variant:
	pass;

func global_shader_parameter_get_list() -> PackedStringArray:
	pass;

func global_shader_parameter_get_type(name: StringName) -> int:
	pass;

func global_shader_parameter_remove(name: StringName) -> void:
	pass;

func global_shader_parameter_set(name: StringName, value: Variant) -> void:
	pass;

func global_shader_parameter_set_override(name: StringName, value: Variant) -> void:
	pass;

#desc Returns [code]true[/code] if changes have been made to the RenderingServer's data. [method force_draw] is usually called if this happens.
func has_changed() -> bool:
	pass;

#desc Not yet implemented. Always returns [code]false[/code].
func has_feature(feature: int) -> bool:
	pass;

#desc Returns [code]true[/code] if the OS supports a certain [param feature]. Features might be [code]s3tc[/code], [code]etc[/code], and [code]etc2[/code].
func has_os_feature(feature: String) -> bool:
	pass;

#desc Attaches a unique Object ID to instance. Object ID must be attached to instance for proper culling with [method instances_cull_aabb], [method instances_cull_convex], and [method instances_cull_ray].
func instance_attach_object_instance_id(instance: RID, id: int) -> void:
	pass;

#desc Attaches a skeleton to an instance. Removes the previous skeleton from the instance.
func instance_attach_skeleton(instance: RID, skeleton: RID) -> void:
	pass;

#desc Creates a visual instance and adds it to the RenderingServer. It can be accessed with the RID that is returned. This RID will be used in all [code]instance_*[/code] RenderingServer functions.
#desc Once finished with your RID, you will want to free the RID using the RenderingServer's [method free_rid] static method.
#desc An instance is a way of placing a 3D object in the scenario. Objects like particles, meshes, and reflection probes need to be associated with an instance to be visible in the scenario using [method instance_set_base].
func instance_create() -> RID:
	pass;

#desc Creates a visual instance, adds it to the RenderingServer, and sets both base and scenario. It can be accessed with the RID that is returned. This RID will be used in all [code]instance_*[/code] RenderingServer functions.
#desc Once finished with your RID, you will want to free the RID using the RenderingServer's [method free_rid] static method.
func instance_create2(base: RID, scenario: RID) -> RID:
	pass;

func instance_geometry_get_shader_parameter(instance: RID, parameter: StringName) -> Variant:
	pass;

func instance_geometry_get_shader_parameter_default_value(instance: RID, parameter: StringName) -> Variant:
	pass;

func instance_geometry_get_shader_parameter_list(instance: RID) -> Dictionary[]:
	pass;

#desc Sets the shadow casting setting to one of [enum ShadowCastingSetting]. Equivalent to [member GeometryInstance3D.cast_shadow].
func instance_geometry_set_cast_shadows_setting(instance: RID, shadow_casting_setting: int) -> void:
	pass;

#desc Sets the flag for a given [enum InstanceFlags]. See [enum InstanceFlags] for more details.
func instance_geometry_set_flag(instance: RID, flag: int, enabled: bool) -> void:
	pass;

func instance_geometry_set_lightmap(instance: RID, lightmap: RID, lightmap_uv_scale: Rect2, lightmap_slice: int) -> void:
	pass;

func instance_geometry_set_lod_bias(instance: RID, lod_bias: float) -> void:
	pass;

#desc Sets a material that will be rendered for all surfaces on top of active materials for the mesh associated with this instance. Equivalent to [member GeometryInstance3D.material_overlay].
func instance_geometry_set_material_overlay(instance: RID, material: RID) -> void:
	pass;

#desc Sets a material that will override the material for all surfaces on the mesh associated with this instance. Equivalent to [member GeometryInstance3D.material_override].
func instance_geometry_set_material_override(instance: RID, material: RID) -> void:
	pass;

func instance_geometry_set_shader_parameter(instance: RID, parameter: StringName, value: Variant) -> void:
	pass;

#desc Sets the transparency for the given geometry instance. Equivalent to [member GeometryInstance3D.transparency].
#desc A transparency of [code]0.0[/code] is fully opaque, while [code]1.0[/code] is fully transparent. Values greater than [code]0.0[/code] (exclusive) will force the geometry's materials to go through the transparent pipeline, which is slower to render and can exhibit rendering issues due to incorrect transparency sorting. However, unlike using a transparent material, setting [param transparency] to a value greater than [code]0.0[/code] (exclusive) will [i]not[/i] disable shadow rendering.
#desc In spatial shaders, [code]1.0 - transparency[/code] is set as the default value of the [code]ALPHA[/code] built-in.
#desc [b]Note:[/b] [param transparency] is clamped between [code]0.0[/code] and [code]1.0[/code], so this property cannot be used to make transparent materials more opaque than they originally are.
func instance_geometry_set_transparency(instance: RID, transparency: float) -> void:
	pass;

#desc Sets the visibility range values for the given geometry instance. Equivalent to [member GeometryInstance3D.visibility_range_begin] and related properties.
func instance_geometry_set_visibility_range(instance: RID, min: float, max: float, min_margin: float, max_margin: float, fade_mode: int) -> void:
	pass;

#desc Sets the base of the instance. A base can be any of the 3D objects that are created in the RenderingServer that can be displayed. For example, any of the light types, mesh, multimesh, immediate geometry, particle system, reflection probe, lightmap, and the GI probe are all types that can be set as the base of an instance in order to be displayed in the scenario.
func instance_set_base(instance: RID, base: RID) -> void:
	pass;

#desc Sets the weight for a given blend shape associated with this instance.
func instance_set_blend_shape_weight(instance: RID, shape: int, weight: float) -> void:
	pass;

#desc Sets a custom AABB to use when culling objects from the view frustum. Equivalent to [method GeometryInstance3D.set_custom_aabb].
func instance_set_custom_aabb(instance: RID, aabb: AABB) -> void:
	pass;

#desc Sets a margin to increase the size of the AABB when culling objects from the view frustum. This allows you to avoid culling objects that fall outside the view frustum. Equivalent to [member GeometryInstance3D.extra_cull_margin].
func instance_set_extra_visibility_margin(instance: RID, margin: float) -> void:
	pass;

func instance_set_ignore_culling(instance: RID, enabled: bool) -> void:
	pass;

#desc Sets the render layers that this instance will be drawn to. Equivalent to [member VisualInstance3D.layers].
func instance_set_layer_mask(instance: RID, mask: int) -> void:
	pass;

#desc Sets the scenario that the instance is in. The scenario is the 3D world that the objects will be displayed in.
func instance_set_scenario(instance: RID, scenario: RID) -> void:
	pass;

#desc Sets the override material of a specific surface. Equivalent to [method MeshInstance3D.set_surface_override_material].
func instance_set_surface_override_material(instance: RID, surface: int, material: RID) -> void:
	pass;

#desc Sets the world space transform of the instance. Equivalent to [member Node3D.transform].
func instance_set_transform(instance: RID, transform: Transform3D) -> void:
	pass;

#desc Sets the visibility parent for the given instance. Equivalent to [member Node3D.visibility_parent].
func instance_set_visibility_parent(instance: RID, parent: RID) -> void:
	pass;

#desc Sets whether an instance is drawn or not. Equivalent to [member Node3D.visible].
func instance_set_visible(instance: RID, visible: bool) -> void:
	pass;

#desc Returns an array of object IDs intersecting with the provided AABB. Only visual 3D nodes are considered, such as [MeshInstance3D] or [DirectionalLight3D]. Use [method @GlobalScope.instance_from_id] to obtain the actual nodes. A scenario RID must be provided, which is available in the [World3D] you want to query. This forces an update for all resources queued to update.
#desc [b]Warning:[/b] This function is primarily intended for editor usage. For in-game use cases, prefer physics collision.
func instances_cull_aabb(aabb: AABB, scenario: RID) -> PackedInt64Array:
	pass;

#desc Returns an array of object IDs intersecting with the provided convex shape. Only visual 3D nodes are considered, such as [MeshInstance3D] or [DirectionalLight3D]. Use [method @GlobalScope.instance_from_id] to obtain the actual nodes. A scenario RID must be provided, which is available in the [World3D] you want to query. This forces an update for all resources queued to update.
#desc [b]Warning:[/b] This function is primarily intended for editor usage. For in-game use cases, prefer physics collision.
func instances_cull_convex(convex: Plane[], scenario: RID) -> PackedInt64Array:
	pass;

#desc Returns an array of object IDs intersecting with the provided 3D ray. Only visual 3D nodes are considered, such as [MeshInstance3D] or [DirectionalLight3D]. Use [method @GlobalScope.instance_from_id] to obtain the actual nodes. A scenario RID must be provided, which is available in the [World3D] you want to query. This forces an update for all resources queued to update.
#desc [b]Warning:[/b] This function is primarily intended for editor usage. For in-game use cases, prefer physics collision.
func instances_cull_ray(from: Vector3, to: Vector3, scenario: RID) -> PackedInt64Array:
	pass;

#desc If [code]true[/code], this directional light will blend between shadow map splits resulting in a smoother transition between them. Equivalent to [member DirectionalLight3D.directional_shadow_blend_splits].
func light_directional_set_blend_splits(light: RID, enable: bool) -> void:
	pass;

#desc Sets the shadow mode for this directional light. Equivalent to [member DirectionalLight3D.directional_shadow_mode]. See [enum LightDirectionalShadowMode] for options.
func light_directional_set_shadow_mode(light: RID, mode: int) -> void:
	pass;

#desc If [code]true[/code], this light will not be used for anything except sky shaders. Use this for lights that impact your sky shader that you may want to hide from affecting the rest of the scene. For example, you may want to enable this when the sun in your sky shader falls below the horizon.
func light_directional_set_sky_mode(light: RID, mode: int) -> void:
	pass;

#desc Sets whether to use a dual paraboloid or a cubemap for the shadow map. Dual paraboloid is faster but may suffer from artifacts. Equivalent to [member OmniLight3D.omni_shadow_mode].
func light_omni_set_shadow_mode(light: RID, mode: int) -> void:
	pass;

func light_projectors_set_filter(filter: int) -> void:
	pass;

func light_set_bake_mode(light: RID, bake_mode: int) -> void:
	pass;

#desc Sets the color of the light. Equivalent to [member Light3D.light_color].
func light_set_color(light: RID, color: Color) -> void:
	pass;

#desc Sets the cull mask for this Light3D. Lights only affect objects in the selected layers. Equivalent to [member Light3D.light_cull_mask].
func light_set_cull_mask(light: RID, mask: int) -> void:
	pass;

#desc Sets the distance fade for this Light3D. This acts as a form of level of detail (LOD) and can be used to improve performance. Equivalent to [member Light3D.distance_fade_enabled], [member Light3D.distance_fade_begin], [member Light3D.distance_fade_shadow], and [member Light3D.distance_fade_length].
func light_set_distance_fade(decal: RID, enabled: bool, begin: float, shadow: float, length: float) -> void:
	pass;

func light_set_max_sdfgi_cascade(light: RID, cascade: int) -> void:
	pass;

#desc If [code]true[/code], light will subtract light instead of adding light. Equivalent to [member Light3D.light_negative].
func light_set_negative(light: RID, enable: bool) -> void:
	pass;

#desc Sets the specified light parameter. See [enum LightParam] for options. Equivalent to [method Light3D.set_param].
func light_set_param(light: RID, param: int, value: float) -> void:
	pass;

#desc Not implemented in Godot 3.x.
func light_set_projector(light: RID, texture: RID) -> void:
	pass;

#desc If [code]true[/code], reverses the backface culling of the mesh. This can be useful when you have a flat mesh that has a light behind it. If you need to cast a shadow on both sides of the mesh, set the mesh to use double-sided shadows with [method instance_geometry_set_cast_shadows_setting]. Equivalent to [member Light3D.shadow_reverse_cull_face].
func light_set_reverse_cull_face_mode(light: RID, enabled: bool) -> void:
	pass;

#desc If [code]true[/code], light will cast shadows. Equivalent to [member Light3D.shadow_enabled].
func light_set_shadow(light: RID, enabled: bool) -> void:
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

#desc Used to inform the renderer what exposure normalization value was used while baking the lightmap. This value will be used and modulated at run time to ensure that the lightmap maintains a consistent level of exposure even if the scene-wide exposure normalization is changed at run time. For more information see [method camera_attributes_set_exposure].
func lightmap_set_baked_exposure_normalization(lightmap: RID, baked_exposure: float) -> void:
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

#desc Returns a mesh of a sphere with the given number of horizontal and vertical subdivisions.
func make_sphere_mesh(latitudes: int, longitudes: int, radius: float) -> RID:
	pass;

#desc Creates an empty material and adds it to the RenderingServer. It can be accessed with the RID that is returned. This RID will be used in all [code]material_*[/code] RenderingServer functions.
#desc Once finished with your RID, you will want to free the RID using the RenderingServer's [method free_rid] static method.
func material_create() -> RID:
	pass;

#desc Returns the value of a certain material's parameter.
func material_get_param(material: RID, parameter: StringName) -> Variant:
	pass;

#desc Sets an object's next material.
func material_set_next_pass(material: RID, next_material: RID) -> void:
	pass;

#desc Sets a material's parameter.
func material_set_param(material: RID, parameter: StringName, value: Variant) -> void:
	pass;

#desc Sets a material's render priority.
func material_set_render_priority(material: RID, priority: int) -> void:
	pass;

#desc Sets a shader material's shader.
func material_set_shader(shader_material: RID, shader: RID) -> void:
	pass;

func mesh_add_surface(mesh: RID, surface: Dictionary) -> void:
	pass;

func mesh_add_surface_from_arrays(mesh: RID, primitive: int, arrays: Array, blend_shapes: Array, lods: Dictionary, compress_format: int) -> void:
	pass;

#desc Removes all surfaces from a mesh.
func mesh_clear(mesh: RID) -> void:
	pass;

#desc Creates a new mesh and adds it to the RenderingServer. It can be accessed with the RID that is returned. This RID will be used in all [code]mesh_*[/code] RenderingServer functions.
#desc Once finished with your RID, you will want to free the RID using the RenderingServer's [method free_rid] static method.
#desc To place in a scene, attach this mesh to an instance using [method instance_set_base] using the returned RID.
func mesh_create() -> RID:
	pass;

func mesh_create_from_surfaces(surfaces: Dictionary[], blend_shape_count: int) -> RID:
	pass;

#desc Returns a mesh's blend shape count.
func mesh_get_blend_shape_count(mesh: RID) -> int:
	pass;

#desc Returns a mesh's blend shape mode.
func mesh_get_blend_shape_mode(mesh: RID) -> int:
	pass;

#desc Returns a mesh's custom aabb.
func mesh_get_custom_aabb(mesh: RID) -> AABB:
	pass;

func mesh_get_surface(mesh: RID, surface: int) -> Dictionary:
	pass;

#desc Returns a mesh's number of surfaces.
func mesh_get_surface_count(mesh: RID) -> int:
	pass;

#desc Sets a mesh's blend shape mode.
func mesh_set_blend_shape_mode(mesh: RID, mode: int) -> void:
	pass;

#desc Sets a mesh's custom aabb.
func mesh_set_custom_aabb(mesh: RID, aabb: AABB) -> void:
	pass;

func mesh_set_shadow_mesh(mesh: RID, shadow_mesh: RID) -> void:
	pass;

#desc Returns a mesh's surface's buffer arrays.
func mesh_surface_get_arrays(mesh: RID, surface: int) -> Array:
	pass;

#desc Returns a mesh's surface's arrays for blend shapes.
func mesh_surface_get_blend_shape_arrays(mesh: RID, surface: int) -> Array[]:
	pass;

func mesh_surface_get_format_attribute_stride(format: int, vertex_count: int) -> int:
	pass;

func mesh_surface_get_format_offset(format: int, vertex_count: int, array_index: int) -> int:
	pass;

func mesh_surface_get_format_skin_stride(format: int, vertex_count: int) -> int:
	pass;

func mesh_surface_get_format_vertex_stride(format: int, vertex_count: int) -> int:
	pass;

#desc Returns a mesh's surface's material.
func mesh_surface_get_material(mesh: RID, surface: int) -> RID:
	pass;

#desc Sets a mesh's surface's material.
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

#desc Creates a new multimesh on the RenderingServer and returns an [RID] handle. This RID will be used in all [code]multimesh_*[/code] RenderingServer functions.
#desc Once finished with your RID, you will want to free the RID using the RenderingServer's [method free_rid] static method.
#desc To place in a scene, attach this multimesh to an instance using [method instance_set_base] using the returned RID.
func multimesh_create() -> RID:
	pass;

#desc Calculates and returns the axis-aligned bounding box that encloses all instances within the multimesh.
func multimesh_get_aabb(multimesh: RID) -> AABB:
	pass;

func multimesh_get_buffer(multimesh: RID) -> PackedFloat32Array:
	pass;

#desc Returns the number of instances allocated for this multimesh.
func multimesh_get_instance_count(multimesh: RID) -> int:
	pass;

#desc Returns the RID of the mesh that will be used in drawing this multimesh.
func multimesh_get_mesh(multimesh: RID) -> RID:
	pass;

#desc Returns the number of visible instances for this multimesh.
func multimesh_get_visible_instances(multimesh: RID) -> int:
	pass;

#desc Returns the color by which the specified instance will be modulated.
func multimesh_instance_get_color(multimesh: RID, index: int) -> Color:
	pass;

#desc Returns the custom data associated with the specified instance.
func multimesh_instance_get_custom_data(multimesh: RID, index: int) -> Color:
	pass;

#desc Returns the [Transform3D] of the specified instance.
func multimesh_instance_get_transform(multimesh: RID, index: int) -> Transform3D:
	pass;

#desc Returns the [Transform2D] of the specified instance. For use when the multimesh is set to use 2D transforms.
func multimesh_instance_get_transform_2d(multimesh: RID, index: int) -> Transform2D:
	pass;

#desc Sets the color by which this instance will be modulated. Equivalent to [method MultiMesh.set_instance_color].
func multimesh_instance_set_color(multimesh: RID, index: int, color: Color) -> void:
	pass;

#desc Sets the custom data for this instance. Custom data is passed as a [Color], but is interpreted as a [code]vec4[/code] in the shader. Equivalent to [method MultiMesh.set_instance_custom_data].
func multimesh_instance_set_custom_data(multimesh: RID, index: int, custom_data: Color) -> void:
	pass;

#desc Sets the [Transform3D] for this instance. Equivalent to [method MultiMesh.set_instance_transform].
func multimesh_instance_set_transform(multimesh: RID, index: int, transform: Transform3D) -> void:
	pass;

#desc Sets the [Transform2D] for this instance. For use when multimesh is used in 2D. Equivalent to [method MultiMesh.set_instance_transform_2d].
func multimesh_instance_set_transform_2d(multimesh: RID, index: int, transform: Transform2D) -> void:
	pass;

func multimesh_set_buffer(multimesh: RID, buffer: PackedFloat32Array) -> void:
	pass;

#desc Sets the mesh to be drawn by the multimesh. Equivalent to [member MultiMesh.mesh].
func multimesh_set_mesh(multimesh: RID, mesh: RID) -> void:
	pass;

#desc Sets the number of instances visible at a given time. If -1, all instances that have been allocated are drawn. Equivalent to [member MultiMesh.visible_instance_count].
func multimesh_set_visible_instances(multimesh: RID, visible: int) -> void:
	pass;

func occluder_create() -> RID:
	pass;

func occluder_set_mesh(occluder: RID, vertices: PackedVector3Array, indices: PackedInt32Array) -> void:
	pass;

#desc Creates a new omni light and adds it to the RenderingServer. It can be accessed with the RID that is returned. This RID can be used in most [code]light_*[/code] RenderingServer functions.
#desc Once finished with your RID, you will want to free the RID using the RenderingServer's [method free_rid] static method.
#desc To place in a scene, attach this omni light to an instance using [method instance_set_base] using the returned RID.
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

#desc Creates a particle system and adds it to the RenderingServer. It can be accessed with the RID that is returned. This RID will be used in all [code]particles_*[/code] RenderingServer functions.
#desc Once finished with your RID, you will want to free the RID using the RenderingServer's [method free_rid] static method.
#desc To place in a scene, attach these particles to an instance using [method instance_set_base] using the returned RID.
func particles_create() -> RID:
	pass;

func particles_emit(particles: RID, transform: Transform3D, velocity: Vector3, color: Color, custom: Color, emit_flags: int) -> void:
	pass;

#desc Calculates and returns the axis-aligned bounding box that contains all the particles. Equivalent to [method GPUParticles3D.capture_aabb].
func particles_get_current_aabb(particles: RID) -> AABB:
	pass;

#desc Returns [code]true[/code] if particles are currently set to emitting.
func particles_get_emitting(particles: RID) -> bool:
	pass;

#desc Returns [code]true[/code] if particles are not emitting and particles are set to inactive.
func particles_is_inactive(particles: RID) -> bool:
	pass;

#desc Add particle system to list of particle systems that need to be updated. Update will take place on the next frame, or on the next call to [method instances_cull_aabb], [method instances_cull_convex], or [method instances_cull_ray].
func particles_request_process(particles: RID) -> void:
	pass;

#desc Reset the particles on the next update. Equivalent to [method GPUParticles3D.restart].
func particles_restart(particles: RID) -> void:
	pass;

#desc Sets the number of particles to be drawn and allocates the memory for them. Equivalent to [member GPUParticles3D.amount].
func particles_set_amount(particles: RID, amount: int) -> void:
	pass;

func particles_set_collision_base_size(particles: RID, size: float) -> void:
	pass;

#desc Sets a custom axis-aligned bounding box for the particle system. Equivalent to [member GPUParticles3D.visibility_aabb].
func particles_set_custom_aabb(particles: RID, aabb: AABB) -> void:
	pass;

#desc Sets the draw order of the particles to one of the named enums from [enum ParticlesDrawOrder]. See [enum ParticlesDrawOrder] for options. Equivalent to [member GPUParticles3D.draw_order].
func particles_set_draw_order(particles: RID, order: int) -> void:
	pass;

#desc Sets the mesh to be used for the specified draw pass. Equivalent to [member GPUParticles3D.draw_pass_1], [member GPUParticles3D.draw_pass_2], [member GPUParticles3D.draw_pass_3], and [member GPUParticles3D.draw_pass_4].
func particles_set_draw_pass_mesh(particles: RID, pass: int, mesh: RID) -> void:
	pass;

#desc Sets the number of draw passes to use. Equivalent to [member GPUParticles3D.draw_passes].
func particles_set_draw_passes(particles: RID, count: int) -> void:
	pass;

#desc Sets the [Transform3D] that will be used by the particles when they first emit.
func particles_set_emission_transform(particles: RID, transform: Transform3D) -> void:
	pass;

#desc If [code]true[/code], particles will emit over time. Setting to false does not reset the particles, but only stops their emission. Equivalent to [member GPUParticles3D.emitting].
func particles_set_emitting(particles: RID, emitting: bool) -> void:
	pass;

#desc Sets the explosiveness ratio. Equivalent to [member GPUParticles3D.explosiveness].
func particles_set_explosiveness_ratio(particles: RID, ratio: float) -> void:
	pass;

#desc Sets the frame rate that the particle system rendering will be fixed to. Equivalent to [member GPUParticles3D.fixed_fps].
func particles_set_fixed_fps(particles: RID, fps: int) -> void:
	pass;

#desc If [code]true[/code], uses fractional delta which smooths the movement of the particles. Equivalent to [member GPUParticles3D.fract_delta].
func particles_set_fractional_delta(particles: RID, enable: bool) -> void:
	pass;

func particles_set_interpolate(particles: RID, enable: bool) -> void:
	pass;

#desc Sets the lifetime of each particle in the system. Equivalent to [member GPUParticles3D.lifetime].
func particles_set_lifetime(particles: RID, lifetime: float) -> void:
	pass;

func particles_set_mode(particles: RID, mode: int) -> void:
	pass;

#desc If [code]true[/code], particles will emit once and then stop. Equivalent to [member GPUParticles3D.one_shot].
func particles_set_one_shot(particles: RID, one_shot: bool) -> void:
	pass;

#desc Sets the preprocess time for the particles' animation. This lets you delay starting an animation until after the particles have begun emitting. Equivalent to [member GPUParticles3D.preprocess].
func particles_set_pre_process_time(particles: RID, time: float) -> void:
	pass;

#desc Sets the material for processing the particles.
#desc [b]Note:[/b] This is not the material used to draw the materials. Equivalent to [member GPUParticles3D.process_material].
func particles_set_process_material(particles: RID, material: RID) -> void:
	pass;

#desc Sets the emission randomness ratio. This randomizes the emission of particles within their phase. Equivalent to [member GPUParticles3D.randomness].
func particles_set_randomness_ratio(particles: RID, ratio: float) -> void:
	pass;

#desc Sets the speed scale of the particle system. Equivalent to [member GPUParticles3D.speed_scale].
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

#desc If [code]true[/code], particles use local coordinates. If [code]false[/code] they use global coordinates. Equivalent to [member GPUParticles3D.local_coords].
func particles_set_use_local_coordinates(particles: RID, enable: bool) -> void:
	pass;

func positional_soft_shadow_filter_set_quality(quality: int) -> void:
	pass;

#desc Creates a reflection probe and adds it to the RenderingServer. It can be accessed with the RID that is returned. This RID will be used in all [code]reflection_probe_*[/code] RenderingServer functions.
#desc Once finished with your RID, you will want to free the RID using the RenderingServer's [method free_rid] static method.
#desc To place in a scene, attach this reflection probe to an instance using [method instance_set_base] using the returned RID.
func reflection_probe_create() -> RID:
	pass;

func reflection_probe_set_ambient_color(probe: RID, color: Color) -> void:
	pass;

func reflection_probe_set_ambient_energy(probe: RID, energy: float) -> void:
	pass;

func reflection_probe_set_ambient_mode(probe: RID, mode: int) -> void:
	pass;

#desc If [code]true[/code], reflections will ignore sky contribution. Equivalent to [member ReflectionProbe.interior].
func reflection_probe_set_as_interior(probe: RID, enable: bool) -> void:
	pass;

#desc Sets the render cull mask for this reflection probe. Only instances with a matching cull mask will be rendered by this probe. Equivalent to [member ReflectionProbe.cull_mask].
func reflection_probe_set_cull_mask(probe: RID, layers: int) -> void:
	pass;

#desc If [code]true[/code], uses box projection. This can make reflections look more correct in certain situations. Equivalent to [member ReflectionProbe.box_projection].
func reflection_probe_set_enable_box_projection(probe: RID, enable: bool) -> void:
	pass;

#desc If [code]true[/code], computes shadows in the reflection probe. This makes the reflection much slower to compute. Equivalent to [member ReflectionProbe.enable_shadows].
func reflection_probe_set_enable_shadows(probe: RID, enable: bool) -> void:
	pass;

#desc Sets the size of the area that the reflection probe will capture. Equivalent to [member ReflectionProbe.extents].
func reflection_probe_set_extents(probe: RID, extents: Vector3) -> void:
	pass;

#desc Sets the intensity of the reflection probe. Intensity modulates the strength of the reflection. Equivalent to [member ReflectionProbe.intensity].
func reflection_probe_set_intensity(probe: RID, intensity: float) -> void:
	pass;

#desc Sets the max distance away from the probe an object can be before it is culled. Equivalent to [member ReflectionProbe.max_distance].
func reflection_probe_set_max_distance(probe: RID, distance: float) -> void:
	pass;

func reflection_probe_set_mesh_lod_threshold(probe: RID, pixels: float) -> void:
	pass;

#desc Sets the origin offset to be used when this reflection probe is in box project mode. Equivalent to [member ReflectionProbe.origin_offset].
func reflection_probe_set_origin_offset(probe: RID, offset: Vector3) -> void:
	pass;

func reflection_probe_set_resolution(probe: RID, resolution: int) -> void:
	pass;

#desc Sets how often the reflection probe updates. Can either be once or every frame. See [enum ReflectionProbeUpdateMode] for options.
func reflection_probe_set_update_mode(probe: RID, mode: int) -> void:
	pass;

#desc Schedules a callback to the given callable after a frame has been drawn.
func request_frame_drawn_callback(callable: Callable) -> void:
	pass;

#desc Creates a scenario and adds it to the RenderingServer. It can be accessed with the RID that is returned. This RID will be used in all [code]scenario_*[/code] RenderingServer functions.
#desc Once finished with your RID, you will want to free the RID using the RenderingServer's [method free_rid] static method.
#desc The scenario is the 3D world that all the visual instances exist in.
func scenario_create() -> RID:
	pass;

func scenario_set_camera_attributes(scenario: RID, effects: RID) -> void:
	pass;

#desc Sets the environment that will be used with this scenario.
func scenario_set_environment(scenario: RID, environment: RID) -> void:
	pass;

#desc Sets the fallback environment to be used by this scenario. The fallback environment is used if no environment is set. Internally, this is used by the editor to provide a default environment.
func scenario_set_fallback_environment(scenario: RID, environment: RID) -> void:
	pass;

func screen_space_roughness_limiter_set_active(enable: bool, amount: float, limit: float) -> void:
	pass;

#desc Sets a boot image. The color defines the background color. If [param scale] is [code]true[/code], the image will be scaled to fit the screen size. If [param use_filter] is [code]true[/code], the image will be scaled with linear interpolation. If [param use_filter] is [code]false[/code], the image will be scaled with nearest-neighbor interpolation.
func set_boot_image(image: Image, color: Color, scale: bool, use_filter: bool) -> void:
	pass;

#desc If [code]true[/code], the engine will generate wireframes for use with the wireframe debug mode.
func set_debug_generate_wireframes(generate: bool) -> void:
	pass;

#desc Sets the default clear color which is used when a specific clear color has not been selected.
func set_default_clear_color(color: Color) -> void:
	pass;

#desc Creates an empty shader and adds it to the RenderingServer. It can be accessed with the RID that is returned. This RID will be used in all [code]shader_*[/code] RenderingServer functions.
#desc Once finished with your RID, you will want to free the RID using the RenderingServer's [method free_rid] static method.
func shader_create() -> RID:
	pass;

#desc Returns a shader's code.
func shader_get_code(shader: RID) -> String:
	pass;

#desc Returns a default texture from a shader searched by name.
#desc [b]Note:[/b] If the sampler array is used use [param index] to access the specified texture.
func shader_get_default_texture_parameter(shader: RID, name: StringName, index: int) -> RID:
	pass;

func shader_get_parameter_default(shader: RID, name: StringName) -> Variant:
	pass;

func shader_set_code(shader: RID, code: String) -> void:
	pass;

#desc Sets a shader's default texture. Overwrites the texture given by name.
#desc [b]Note:[/b] If the sampler array is used use [param index] to access the specified texture.
func shader_set_default_texture_parameter(shader: RID, name: StringName, texture: RID, index: int) -> void:
	pass;

func shader_set_path_hint(shader: RID, path: String) -> void:
	pass;

func skeleton_allocate_data(skeleton: RID, bones: int, is_2d_skeleton: bool) -> void:
	pass;

#desc Returns the [Transform3D] set for a specific bone of this skeleton.
func skeleton_bone_get_transform(skeleton: RID, bone: int) -> Transform3D:
	pass;

#desc Returns the [Transform2D] set for a specific bone of this skeleton.
func skeleton_bone_get_transform_2d(skeleton: RID, bone: int) -> Transform2D:
	pass;

#desc Sets the [Transform3D] for a specific bone of this skeleton.
func skeleton_bone_set_transform(skeleton: RID, bone: int, transform: Transform3D) -> void:
	pass;

#desc Sets the [Transform2D] for a specific bone of this skeleton.
func skeleton_bone_set_transform_2d(skeleton: RID, bone: int, transform: Transform2D) -> void:
	pass;

#desc Creates a skeleton and adds it to the RenderingServer. It can be accessed with the RID that is returned. This RID will be used in all [code]skeleton_*[/code] RenderingServer functions.
#desc Once finished with your RID, you will want to free the RID using the RenderingServer's [method free_rid] static method.
func skeleton_create() -> RID:
	pass;

#desc Returns the number of bones allocated for this skeleton.
func skeleton_get_bone_count(skeleton: RID) -> int:
	pass;

func skeleton_set_base_transform_2d(skeleton: RID, base_transform: Transform2D) -> void:
	pass;

func sky_bake_panorama(sky: RID, energy: float, bake_irradiance: bool, size: Vector2i) -> Image:
	pass;

#desc Creates an empty sky and adds it to the RenderingServer. It can be accessed with the RID that is returned. This RID will be used in all [code]sky_*[/code] RenderingServer functions.
#desc Once finished with your RID, you will want to free the RID using the RenderingServer's [method free_rid] static method.
func sky_create() -> RID:
	pass;

#desc Sets the material that the sky uses to render the background and reflection maps.
func sky_set_material(sky: RID, material: RID) -> void:
	pass;

func sky_set_mode(sky: RID, mode: int) -> void:
	pass;

func sky_set_radiance_size(sky: RID, radiance_size: int) -> void:
	pass;

#desc Creates a spot light and adds it to the RenderingServer. It can be accessed with the RID that is returned. This RID can be used in most [code]light_*[/code] RenderingServer functions.
#desc Once finished with your RID, you will want to free the RID using the RenderingServer's [method free_rid] static method.
#desc To place in a scene, attach this spot light to an instance using [method instance_set_base] using the returned RID.
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

#desc Sets a viewport's camera.
func viewport_attach_camera(viewport: RID, camera: RID) -> void:
	pass;

#desc Sets a viewport's canvas.
func viewport_attach_canvas(viewport: RID, canvas: RID) -> void:
	pass;

#desc Copies the viewport to a region of the screen specified by [param rect]. If [method viewport_set_render_direct_to_screen] is [code]true[/code], then the viewport does not use a framebuffer and the contents of the viewport are rendered directly to screen. However, note that the root viewport is drawn last, therefore it will draw over the screen. Accordingly, you must set the root viewport to an area that does not cover the area that you have attached this viewport to.
#desc For example, you can set the root viewport to not render at all with the following code:
#desc FIXME: The method seems to be non-existent.
#desc [codeblocks]
#desc [gdscript]
#desc func _ready():
#desc get_viewport().set_attach_to_screen_rect(Rect2())
#desc $Viewport.set_attach_to_screen_rect(Rect2(0, 0, 600, 600))
#desc [/gdscript]
#desc [/codeblocks]
#desc Using this can result in significant optimization, especially on lower-end devices. However, it comes at the cost of having to manage your viewports manually. For further optimization, see [method viewport_set_render_direct_to_screen].
func viewport_attach_to_screen(viewport: RID, rect: Rect2, screen: int) -> void:
	pass;

#desc Creates an empty viewport and adds it to the RenderingServer. It can be accessed with the RID that is returned. This RID will be used in all [code]viewport_*[/code] RenderingServer functions.
#desc Once finished with your RID, you will want to free the RID using the RenderingServer's [method free_rid] static method.
func viewport_create() -> RID:
	pass;

func viewport_get_measured_render_time_cpu(viewport: RID) -> float:
	pass;

func viewport_get_measured_render_time_gpu(viewport: RID) -> float:
	pass;

func viewport_get_render_info(viewport: RID, type: int, info: int) -> int:
	pass;

#desc Returns the viewport's last rendered frame.
func viewport_get_texture(viewport: RID) -> RID:
	pass;

#desc Detaches a viewport from a canvas and vice versa.
func viewport_remove_canvas(viewport: RID, canvas: RID) -> void:
	pass;

#desc If [code]true[/code], sets the viewport active, else sets it inactive.
func viewport_set_active(viewport: RID, active: bool) -> void:
	pass;

#desc Sets the stacking order for a viewport's canvas.
#desc [param layer] is the actual canvas layer, while [param sublayer] specifies the stacking order of the canvas among those in the same layer.
func viewport_set_canvas_stacking(viewport: RID, canvas: RID, layer: int, sublayer: int) -> void:
	pass;

#desc Sets the transformation of a viewport's canvas.
func viewport_set_canvas_transform(viewport: RID, canvas: RID, offset: Transform2D) -> void:
	pass;

#desc Sets the clear mode of a viewport. See [enum ViewportClearMode] for options.
func viewport_set_clear_mode(viewport: RID, clear_mode: int) -> void:
	pass;

#desc Sets the debug draw mode of a viewport. See [enum ViewportDebugDraw] for options.
func viewport_set_debug_draw(viewport: RID, draw: int) -> void:
	pass;

func viewport_set_default_canvas_item_texture_filter(viewport: RID, filter: int) -> void:
	pass;

func viewport_set_default_canvas_item_texture_repeat(viewport: RID, repeat: int) -> void:
	pass;

#desc If [code]true[/code], the viewport's canvas is not rendered.
func viewport_set_disable_2d(viewport: RID, disable: bool) -> void:
	pass;

func viewport_set_disable_3d(viewport: RID, disable: bool) -> void:
	pass;

#desc If [code]true[/code], rendering of a viewport's environment is disabled.
func viewport_set_disable_environment(viewport: RID, disabled: bool) -> void:
	pass;

#desc Determines how sharp the upscaled image will be when using the FSR upscaling mode. Sharpness halves with every whole number. Values go from 0.0 (sharpest) to 2.0. Values above 2.0 won't make a visible difference.
func viewport_set_fsr_sharpness(viewport: RID, sharpness: float) -> void:
	pass;

#desc Sets the viewport's global transformation matrix.
func viewport_set_global_canvas_transform(viewport: RID, transform: Transform2D) -> void:
	pass;

func viewport_set_measure_render_time(viewport: RID, enable: bool) -> void:
	pass;

#desc Sets the multisample anti-aliasing mode for 2D/Canvas. See [enum ViewportMSAA] for options.
func viewport_set_msaa_2d(viewport: RID, msaa: int) -> void:
	pass;

#desc Sets the multisample anti-aliasing mode for 3D. See [enum ViewportMSAA] for options.
func viewport_set_msaa_3d(viewport: RID, msaa: int) -> void:
	pass;

func viewport_set_occlusion_culling_build_quality(quality: int) -> void:
	pass;

func viewport_set_occlusion_rays_per_thread(rays_per_thread: int) -> void:
	pass;

#desc Sets the viewport's parent to another viewport.
func viewport_set_parent_viewport(viewport: RID, parent_viewport: RID) -> void:
	pass;

#desc Sets the shadow atlas quadrant's subdivision.
func viewport_set_positional_shadow_atlas_quadrant_subdivision(viewport: RID, quadrant: int, subdivision: int) -> void:
	pass;

#desc Sets the size of the shadow atlas's images (used for omni and spot lights). The value will be rounded up to the nearest power of 2.
#desc [b]Note:[/b] If this is set to [code]0[/code], no shadows will be visible at all (including directional shadows).
func viewport_set_positional_shadow_atlas_size(viewport: RID, size: int, use_16_bits: bool) -> void:
	pass;

#desc If [code]true[/code], render the contents of the viewport directly to screen. This allows a low-level optimization where you can skip drawing a viewport to the root viewport. While this optimization can result in a significant increase in speed (especially on older devices), it comes at a cost of usability. When this is enabled, you cannot read from the viewport or from the [code]SCREEN_TEXTURE[/code]. You also lose the benefit of certain window settings, such as the various stretch modes. Another consequence to be aware of is that in 2D the rendering happens in window coordinates, so if you have a viewport that is double the size of the window, and you set this, then only the portion that fits within the window will be drawn, no automatic scaling is possible, even if your game scene is significantly larger than the window size.
func viewport_set_render_direct_to_screen(viewport: RID, enabled: bool) -> void:
	pass;

#desc Sets scaling 3d mode. Bilinear scaling renders at different resolution to either undersample or supersample the viewport. FidelityFX Super Resolution 1.0, abbreviated to FSR, is an upscaling technology that produces high quality images at fast framerates by using a spatially aware upscaling algorithm. FSR is slightly more expensive than bilinear, but it produces significantly higher image quality. FSR should be used where possible.
func viewport_set_scaling_3d_mode(viewport: RID, scaling_3d_mode: int) -> void:
	pass;

#desc Scales the 3D render buffer based on the viewport size uses an image filter specified in [enum ViewportScaling3DMode] to scale the output image to the full viewport size. Values lower than [code]1.0[/code] can be used to speed up 3D rendering at the cost of quality (undersampling). Values greater than [code]1.0[/code] are only valid for bilinear mode and can be used to improve 3D rendering quality at a high performance cost (supersampling). See also [enum ViewportMSAA] for multi-sample antialiasing, which is significantly cheaper but only smoothens the edges of polygons.
#desc When using FSR upscaling, AMD recommends exposing the following values as preset options to users "Ultra Quality: 0.77", "Quality: 0.67", "Balanced: 0.59", "Performance: 0.5" instead of exposing the entire scale.
func viewport_set_scaling_3d_scale(viewport: RID, scale: float) -> void:
	pass;

#desc Sets a viewport's scenario.
#desc The scenario contains information about environment information, reflection atlas etc.
func viewport_set_scenario(viewport: RID, scenario: RID) -> void:
	pass;

func viewport_set_screen_space_aa(viewport: RID, mode: int) -> void:
	pass;

func viewport_set_sdf_oversize_and_scale(viewport: RID, oversize: int, scale: int) -> void:
	pass;

#desc Sets the viewport's width and height.
func viewport_set_size(viewport: RID, width: int, height: int) -> void:
	pass;

func viewport_set_snap_2d_transforms_to_pixel(viewport: RID, enabled: bool) -> void:
	pass;

func viewport_set_snap_2d_vertices_to_pixel(viewport: RID, enabled: bool) -> void:
	pass;

#desc Affects the final texture sharpness by reading from a lower or higher mipmap (also called "texture LOD bias"). Negative values make mipmapped textures sharper but grainier when viewed at a distance, while positive values make mipmapped textures blurrier (even when up close). To get sharper textures at a distance without introducing too much graininess, set this between [code]-0.75[/code] and [code]0.0[/code]. Enabling temporal antialiasing ([member ProjectSettings.rendering/anti_aliasing/quality/use_taa]) can help reduce the graininess visible when using negative mipmap bias.
#desc [b]Note:[/b] When the 3D scaling mode is set to FSR 1.0, this value is used to adjust the automatic mipmap bias which is calculated internally based on the scale factor. The formula for this is [code]-log2(1.0 / scale) + mipmap_bias[/code].
func viewport_set_texture_mipmap_bias(viewport: RID, mipmap_bias: float) -> void:
	pass;

#desc If [code]true[/code], the viewport renders its background as transparent.
func viewport_set_transparent_background(viewport: RID, enabled: bool) -> void:
	pass;

#desc Sets when the viewport should be updated. See [enum ViewportUpdateMode] constants for options.
func viewport_set_update_mode(viewport: RID, update_mode: int) -> void:
	pass;

func viewport_set_use_debanding(viewport: RID, enable: bool) -> void:
	pass;

func viewport_set_use_occlusion_culling(viewport: RID, enable: bool) -> void:
	pass;

#desc If [code]true[/code], use Temporal Anti-Aliasing.
func viewport_set_use_taa(viewport: RID, enable: bool) -> void:
	pass;

#desc If [code]true[/code], the viewport uses augmented or virtual reality technologies. See [XRInterface].
func viewport_set_use_xr(viewport: RID, use_xr: bool) -> void:
	pass;

#desc Sets the Variable Rate Shading (VRS) mode for the viewport. Note, if hardware does not support VRS this property is ignored.
func viewport_set_vrs_mode(viewport: RID, mode: int) -> void:
	pass;

#desc Texture to use when the VRS mode is set to [constant RenderingServer.VIEWPORT_VRS_TEXTURE].
func viewport_set_vrs_texture(viewport: RID, texture: RID) -> void:
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

#desc Used to inform the renderer what exposure normalization value was used while baking the voxel gi. This value will be used and modulated at run time to ensure that the voxel gi maintains a consistent level of exposure even if the scene-wide exposure normalization is changed at run time. For more information see [method camera_attributes_set_exposure].
func voxel_gi_set_baked_exposure_normalization(voxel_gi: RID, baked_exposure: float) -> void:
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


