extends Material
#brief Default 3D rendering material.
#desc This provides a default material with a wide variety of rendering features and properties without the need to write shader code. See the tutorial below for details.
class_name BaseMaterial3D

#desc Texture specifying per-pixel color.
const TEXTURE_ALBEDO = 0;

#desc Texture specifying per-pixel metallic value.
const TEXTURE_METALLIC = 1;

#desc Texture specifying per-pixel roughness value.
const TEXTURE_ROUGHNESS = 2;

#desc Texture specifying per-pixel emission color.
const TEXTURE_EMISSION = 3;

#desc Texture specifying per-pixel normal vector.
const TEXTURE_NORMAL = 4;

#desc Texture specifying per-pixel rim value.
const TEXTURE_RIM = 5;

#desc Texture specifying per-pixel clearcoat value.
const TEXTURE_CLEARCOAT = 6;

#desc Texture specifying per-pixel flowmap direction for use with [member anisotropy].
const TEXTURE_FLOWMAP = 7;

#desc Texture specifying per-pixel ambient occlusion value.
const TEXTURE_AMBIENT_OCCLUSION = 8;

#desc Texture specifying per-pixel height.
const TEXTURE_HEIGHTMAP = 9;

#desc Texture specifying per-pixel subsurface scattering.
const TEXTURE_SUBSURFACE_SCATTERING = 10;

#desc Texture specifying per-pixel transmittance for subsurface scattering.
const TEXTURE_SUBSURFACE_TRANSMITTANCE = 11;

#desc Texture specifying per-pixel backlight color.
const TEXTURE_BACKLIGHT = 12;

#desc Texture specifying per-pixel refraction strength.
const TEXTURE_REFRACTION = 13;

#desc Texture specifying per-pixel detail mask blending value.
const TEXTURE_DETAIL_MASK = 14;

#desc Texture specifying per-pixel detail color.
const TEXTURE_DETAIL_ALBEDO = 15;

#desc Texture specifying per-pixel detail normal.
const TEXTURE_DETAIL_NORMAL = 16;

#desc Texture holding ambient occlusion, roughness, and metallic.
const TEXTURE_ORM = 17;

#desc Represents the size of the [enum TextureParam] enum.
const TEXTURE_MAX = 18;

#desc The texture filter reads from the nearest pixel only. The simplest and fastest method of filtering, but the texture will look pixelized.
const TEXTURE_FILTER_NEAREST = 0;

#desc The texture filter blends between the nearest 4 pixels. Use this when you want to avoid a pixelated style, but do not want mipmaps.
const TEXTURE_FILTER_LINEAR = 1;

#desc The texture filter reads from the nearest pixel in the nearest mipmap. The fastest way to read from textures with mipmaps.
const TEXTURE_FILTER_NEAREST_WITH_MIPMAPS = 2;

#desc The texture filter blends between the nearest 4 pixels and between the nearest 2 mipmaps. Use this for most cases as mipmaps are important to smooth out pixels that are far from the camera.
const TEXTURE_FILTER_LINEAR_WITH_MIPMAPS = 3;

#desc The texture filter reads from the nearest pixel, but selects a mipmap based on the angle between the surface and the camera view. This reduces artifacts on surfaces that are almost in line with the camera. The anisotropic filtering level can be changed by adjusting [member ProjectSettings.rendering/textures/default_filters/anisotropic_filtering_level].
const TEXTURE_FILTER_NEAREST_WITH_MIPMAPS_ANISOTROPIC = 4;

#desc The texture filter blends between the nearest 4 pixels and selects a mipmap based on the angle between the surface and the camera view. This reduces artifacts on surfaces that are almost in line with the camera. This is the slowest of the filtering options, but results in the highest quality texturing. The anisotropic filtering level can be changed by adjusting [member ProjectSettings.rendering/textures/default_filters/anisotropic_filtering_level].
const TEXTURE_FILTER_LINEAR_WITH_MIPMAPS_ANISOTROPIC = 5;

#desc Represents the size of the [enum TextureFilter] enum.
const TEXTURE_FILTER_MAX = 6;

#desc Use [code]UV[/code] with the detail texture.
const DETAIL_UV_1 = 0;

#desc Use [code]UV2[/code] with the detail texture.
const DETAIL_UV_2 = 1;

#desc The material will not use transparency.
const TRANSPARENCY_DISABLED = 0;

#desc The material will use the texture's alpha values for transparency.
const TRANSPARENCY_ALPHA = 1;

#desc The material will cut off all values below a threshold, the rest will remain opaque.
const TRANSPARENCY_ALPHA_SCISSOR = 2;

#desc The material will cut off all values below a spatially-deterministic threshold, the rest will remain opaque.
const TRANSPARENCY_ALPHA_HASH = 3;

#desc The material will use the texture's alpha value for transparency, but will still be rendered in the pre-pass.
const TRANSPARENCY_ALPHA_DEPTH_PRE_PASS = 4;

#desc Represents the size of the [enum Transparency] enum.
const TRANSPARENCY_MAX = 5;

#desc The object will not receive shadows.
const SHADING_MODE_UNSHADED = 0;

#desc The object will be shaded per pixel. Useful for realistic shading effect.
const SHADING_MODE_PER_PIXEL = 1;

#desc The object will be shaded per vertex. Useful when you want cheaper shaders and do not care about visual quality.
const SHADING_MODE_PER_VERTEX = 2;

#desc Represents the size of the [enum ShadingMode] enum.
const SHADING_MODE_MAX = 3;

#desc Constant for setting [member emission_enabled].
const FEATURE_EMISSION = 0;

#desc Constant for setting [member normal_enabled].
const FEATURE_NORMAL_MAPPING = 1;

#desc Constant for setting [member rim_enabled].
const FEATURE_RIM = 2;

#desc Constant for setting [member clearcoat_enabled].
const FEATURE_CLEARCOAT = 3;

#desc Constant for setting [member anisotropy_enabled].
const FEATURE_ANISOTROPY = 4;

#desc Constant for setting [member ao_enabled].
const FEATURE_AMBIENT_OCCLUSION = 5;

#desc Constant for setting [member heightmap_enabled].
const FEATURE_HEIGHT_MAPPING = 6;

#desc Constant for setting [member subsurf_scatter_enabled].
const FEATURE_SUBSURFACE_SCATTERING = 7;

#desc Constant for setting [member subsurf_scatter_transmittance_enabled].
const FEATURE_SUBSURFACE_TRANSMITTANCE = 8;

#desc Constant for setting [member backlight_enabled].
const FEATURE_BACKLIGHT = 9;

#desc Constant for setting [member refraction_enabled].
const FEATURE_REFRACTION = 10;

#desc Constant for setting [member detail_enabled].
const FEATURE_DETAIL = 11;

#desc Represents the size of the [enum Feature] enum.
const FEATURE_MAX = 12;

#desc Default blend mode. The color of the object is blended over the background based on the object's alpha value.
const BLEND_MODE_MIX = 0;

#desc The color of the object is added to the background.
const BLEND_MODE_ADD = 1;

#desc The color of the object is subtracted from the background.
const BLEND_MODE_SUB = 2;

#desc The color of the object is multiplied by the background.
const BLEND_MODE_MUL = 3;

#desc Disables Alpha AntiAliasing for the material.
const ALPHA_ANTIALIASING_OFF = 0;

#desc Enables AlphaToCoverage. Alpha values in the material are passed to the AntiAliasing sample mask.
const ALPHA_ANTIALIASING_ALPHA_TO_COVERAGE = 1;

#desc Enables AlphaToCoverage and forces all non-zero alpha values to [code]1[/code]. Alpha values in the material are passed to the AntiAliasing sample mask.
const ALPHA_ANTIALIASING_ALPHA_TO_COVERAGE_AND_TO_ONE = 2;

#desc Default depth draw mode. Depth is drawn only for opaque objects.
const DEPTH_DRAW_OPAQUE_ONLY = 0;

#desc Depth draw is calculated for both opaque and transparent objects.
const DEPTH_DRAW_ALWAYS = 1;

#desc No depth draw.
const DEPTH_DRAW_DISABLED = 2;

#desc Default cull mode. The back of the object is culled when not visible. Back face triangles will be culled when facing the camera. This results in only the front side of triangles being drawn. For closed-surface meshes this means that only the exterior of the mesh will be visible.
const CULL_BACK = 0;

#desc Front face triangles will be culled when facing the camera. This results in only the back side of triangles being drawn. For closed-surface meshes this means that the interior of the mesh will be drawn instead of the exterior.
const CULL_FRONT = 1;

#desc No culling is performed.
const CULL_DISABLED = 2;

#desc Disables the depth test, so this object is drawn on top of all others. However, objects drawn after it in the draw order may cover it.
const FLAG_DISABLE_DEPTH_TEST = 0;

#desc Set [code]ALBEDO[/code] to the per-vertex color specified in the mesh.
const FLAG_ALBEDO_FROM_VERTEX_COLOR = 1;

#desc Vertex colors are considered to be stored in sRGB color space and are converted to linear color space during rendering. See also [member vertex_color_is_srgb].
#desc [b]Note:[/b] Only effective when using the Vulkan Clustered or Vulkan Mobile backends.
const FLAG_SRGB_VERTEX_COLOR = 2;

#desc Uses point size to alter the size of primitive points. Also changes the albedo texture lookup to use [code]POINT_COORD[/code] instead of [code]UV[/code].
const FLAG_USE_POINT_SIZE = 3;

#desc Object is scaled by depth so that it always appears the same size on screen.
const FLAG_FIXED_SIZE = 4;

#desc Shader will keep the scale set for the mesh. Otherwise the scale is lost when billboarding. Only applies when [member billboard_mode] is [constant BILLBOARD_ENABLED].
const FLAG_BILLBOARD_KEEP_SCALE = 5;

#desc Use triplanar texture lookup for all texture lookups that would normally use [code]UV[/code].
const FLAG_UV1_USE_TRIPLANAR = 6;

#desc Use triplanar texture lookup for all texture lookups that would normally use [code]UV2[/code].
const FLAG_UV2_USE_TRIPLANAR = 7;

#desc Use triplanar texture lookup for all texture lookups that would normally use [code]UV[/code].
const FLAG_UV1_USE_WORLD_TRIPLANAR = 8;

#desc Use triplanar texture lookup for all texture lookups that would normally use [code]UV2[/code].
const FLAG_UV2_USE_WORLD_TRIPLANAR = 9;

#desc Use [code]UV2[/code] coordinates to look up from the [member ao_texture].
const FLAG_AO_ON_UV2 = 10;

#desc Use [code]UV2[/code] coordinates to look up from the [member emission_texture].
const FLAG_EMISSION_ON_UV2 = 11;

#desc Forces the shader to convert albedo from sRGB space to linear space. See also [member albedo_texture_force_srgb].
const FLAG_ALBEDO_TEXTURE_FORCE_SRGB = 12;

#desc Disables receiving shadows from other objects.
const FLAG_DONT_RECEIVE_SHADOWS = 13;

#desc Disables receiving ambient light.
const FLAG_DISABLE_AMBIENT_LIGHT = 14;

#desc Enables the shadow to opacity feature.
const FLAG_USE_SHADOW_TO_OPACITY = 15;

#desc Enables the texture to repeat when UV coordinates are outside the 0-1 range. If using one of the linear filtering modes, this can result in artifacts at the edges of a texture when the sampler filters across the edges of the texture.
const FLAG_USE_TEXTURE_REPEAT = 16;

#desc Invert values read from a depth texture to convert them to height values (heightmap).
const FLAG_INVERT_HEIGHTMAP = 17;

#desc Enables the skin mode for subsurface scattering which is used to improve the look of subsurface scattering when used for human skin.
const FLAG_SUBSURFACE_MODE_SKIN = 18;

const FLAG_PARTICLE_TRAILS_MODE = 19;

#desc Enables multichannel signed distance field rendering shader.
const FLAG_ALBEDO_TEXTURE_MSDF = 20;

#desc Represents the size of the [enum Flags] enum.
const FLAG_MAX = 21;

#desc Default diffuse scattering algorithm.
const DIFFUSE_BURLEY = 0;

#desc Diffuse scattering ignores roughness.
const DIFFUSE_LAMBERT = 1;

#desc Extends Lambert to cover more than 90 degrees when roughness increases.
const DIFFUSE_LAMBERT_WRAP = 2;

#desc Uses a hard cut for lighting, with smoothing affected by roughness.
const DIFFUSE_TOON = 3;

#desc Default specular blob.
const SPECULAR_SCHLICK_GGX = 0;

#desc Toon blob which changes size based on roughness.
const SPECULAR_TOON = 1;

#desc No specular blob.
const SPECULAR_DISABLED = 2;

#desc Billboard mode is disabled.
const BILLBOARD_DISABLED = 0;

#desc The object's Z axis will always face the camera.
const BILLBOARD_ENABLED = 1;

#desc The object's X axis will always face the camera.
const BILLBOARD_FIXED_Y = 2;

#desc Used for particle systems when assigned to [GPUParticles3D] and [CPUParticles3D] nodes. Enables [code]particles_anim_*[/code] properties.
#desc The [member ParticleProcessMaterial.anim_speed_min] or [member CPUParticles3D.anim_speed_min] should also be set to a value bigger than zero for the animation to play.
const BILLBOARD_PARTICLES = 3;

#desc Used to read from the red channel of a texture.
const TEXTURE_CHANNEL_RED = 0;

#desc Used to read from the green channel of a texture.
const TEXTURE_CHANNEL_GREEN = 1;

#desc Used to read from the blue channel of a texture.
const TEXTURE_CHANNEL_BLUE = 2;

#desc Used to read from the alpha channel of a texture.
const TEXTURE_CHANNEL_ALPHA = 3;

#desc Used to read from the linear (non-perceptual) average of the red, green and blue channels of a texture.
const TEXTURE_CHANNEL_GRAYSCALE = 4;

#desc Adds the emission color to the color from the emission texture.
const EMISSION_OP_ADD = 0;

#desc Multiplies the emission color by the color from the emission texture.
const EMISSION_OP_MULTIPLY = 1;

#desc Do not use distance fade.
const DISTANCE_FADE_DISABLED = 0;

#desc Smoothly fades the object out based on each pixel's distance from the camera using the alpha channel.
const DISTANCE_FADE_PIXEL_ALPHA = 1;

#desc Smoothly fades the object out based on each pixel's distance from the camera using a dither approach. Dithering discards pixels based on a set pattern to smoothly fade without enabling transparency. On certain hardware this can be faster than [constant DISTANCE_FADE_PIXEL_ALPHA].
const DISTANCE_FADE_PIXEL_DITHER = 2;

#desc Smoothly fades the object out based on the object's distance from the camera using a dither approach. Dithering discards pixels based on a set pattern to smoothly fade without enabling transparency. On certain hardware this can be faster than [constant DISTANCE_FADE_PIXEL_ALPHA].
const DISTANCE_FADE_OBJECT_DITHER = 3;


#desc The material's base color.
#desc [b]Note:[/b] If [member detail_enabled] is [code]true[/code] and a [member detail_albedo] texture is specified, [member albedo_color] will [i]not[/i] modulate the detail texture. This can be used to color partial areas of a material by not specifying an albedo texture and using a transparent [member detail_albedo] texture instead.
var albedo_color: Color;

#desc Texture to multiply by [member albedo_color]. Used for basic texturing of objects.
#desc If the texture appears unexpectedly too dark or too bright, check [member albedo_texture_force_srgb].
var albedo_texture: Texture2D;

#desc If [code]true[/code], forces a conversion of the [member albedo_texture] from sRGB color space to linear color space. See also [member vertex_color_is_srgb].
#desc This should only be enabled when needed (typically when using a [ViewportTexture] as [member albedo_texture]). If [member albedo_texture_force_srgb] is [code]true[/code] when it shouldn't be, the texture will appear to be too dark. If [member albedo_texture_force_srgb] is [code]false[/code] when it shouldn't be, the texture will appear to be too bright.
var albedo_texture_force_srgb: bool;

#desc Enables multichannel signed distance field rendering shader. Use [member msdf_pixel_range] and [member msdf_outline_size] to configure MSDF parameters.
var albedo_texture_msdf: bool;

#desc Threshold at which antialiasing will be applied on the alpha channel.
var alpha_antialiasing_edge: float;

#desc The type of alpha antialiasing to apply. See [enum AlphaAntiAliasing].
var alpha_antialiasing_mode: int;

#desc The hashing scale for Alpha Hash. Recommended values between [code]0[/code] and [code]2[/code].
var alpha_hash_scale: float;

#desc Threshold at which the alpha scissor will discard values. Higher values will result in more pixels being discarded. If the material becomes too opaque at a distance, try increasing [member alpha_scissor_threshold]. If the material disappears at a distance, try decreasing [member alpha_scissor_threshold].
var alpha_scissor_threshold: float;

#desc The strength of the anisotropy effect. This is multiplied by [member anisotropy_flowmap]'s alpha channel if a texture is defined there and the texture contains an alpha channel.
var anisotropy: float;

#desc If [code]true[/code], anisotropy is enabled. Anisotropy changes the shape of the specular blob and aligns it to tangent space. This is useful for brushed aluminium and hair reflections.
#desc [b]Note:[/b] Mesh tangents are needed for anisotropy to work. If the mesh does not contain tangents, the anisotropy effect will appear broken.
#desc [b]Note:[/b] Material anisotropy should not to be confused with anisotropic texture filtering, which can be enabled by setting [member texture_filter] to [constant TEXTURE_FILTER_LINEAR_WITH_MIPMAPS_ANISOTROPIC].
var anisotropy_enabled: bool;

#desc Texture that offsets the tangent map for anisotropy calculations and optionally controls the anisotropy effect (if an alpha channel is present). The flowmap texture is expected to be a derivative map, with the red channel representing distortion on the X axis and green channel representing distortion on the Y axis. Values below 0.5 will result in negative distortion, whereas values above 0.5 will result in positive distortion.
#desc If present, the texture's alpha channel will be used to multiply the strength of the [member anisotropy] effect. Fully opaque pixels will keep the anisotropy effect's original strength while fully transparent pixels will disable the anisotropy effect entirely. The flowmap texture's blue channel is ignored.
var anisotropy_flowmap: Texture2D;

#desc If [code]true[/code], ambient occlusion is enabled. Ambient occlusion darkens areas based on the [member ao_texture].
var ao_enabled: bool;

#desc Amount that ambient occlusion affects lighting from lights. If [code]0[/code], ambient occlusion only affects ambient light. If [code]1[/code], ambient occlusion affects lights just as much as it affects ambient light. This can be used to impact the strength of the ambient occlusion effect, but typically looks unrealistic.
var ao_light_affect: float;

#desc If [code]true[/code], use [code]UV2[/code] coordinates to look up from the [member ao_texture].
var ao_on_uv2: bool;

#desc Texture that defines the amount of ambient occlusion for a given point on the object.
var ao_texture: Texture2D;

#desc Specifies the channel of the [member ao_texture] in which the ambient occlusion information is stored. This is useful when you store the information for multiple effects in a single texture. For example if you stored metallic in the red channel, roughness in the blue, and ambient occlusion in the green you could reduce the number of textures you use.
var ao_texture_channel: int;

#desc The color used by the backlight effect. Represents the light passing through an object.
var backlight: Color;

#desc If [code]true[/code], the backlight effect is enabled.
var backlight_enabled: bool;

#desc Texture used to control the backlight effect per-pixel. Added to [member backlight].
var backlight_texture: Texture2D;

#desc If [code]true[/code], the shader will keep the scale set for the mesh. Otherwise, the scale is lost when billboarding. Only applies when [member billboard_mode] is [constant BILLBOARD_ENABLED].
var billboard_keep_scale: bool;

#desc Controls how the object faces the camera. See [enum BillboardMode].
#desc [b]Note:[/b] Billboard mode is not suitable for VR because the left-right vector of the camera is not horizontal when the screen is attached to your head instead of on the table. See [url=https://github.com/godotengine/godot/issues/41567]GitHub issue #41567[/url] for details.
var billboard_mode: int;

#desc The material's blend mode.
#desc [b]Note:[/b] Values other than [code]Mix[/code] force the object into the transparent pipeline. See [enum BlendMode].
var blend_mode: int;

#desc Sets the strength of the clearcoat effect. Setting to [code]0[/code] looks the same as disabling the clearcoat effect.
var clearcoat: float;

#desc If [code]true[/code], clearcoat rendering is enabled. Adds a secondary transparent pass to the lighting calculation resulting in an added specular blob. This makes materials appear as if they have a clear layer on them that can be either glossy or rough.
#desc [b]Note:[/b] Clearcoat rendering is not visible if the material's [member shading_mode] is [constant SHADING_MODE_UNSHADED].
var clearcoat_enabled: bool;

#desc Sets the roughness of the clearcoat pass. A higher value results in a rougher clearcoat while a lower value results in a smoother clearcoat.
var clearcoat_roughness: float;

#desc Texture that defines the strength of the clearcoat effect and the glossiness of the clearcoat. Strength is specified in the red channel while glossiness is specified in the green channel.
var clearcoat_texture: Texture2D;

#desc Determines which side of the triangle to cull depending on whether the triangle faces towards or away from the camera. See [enum CullMode].
var cull_mode: int;

#desc Determines when depth rendering takes place. See [enum DepthDrawMode]. See also [member transparency].
var depth_draw_mode: int;

#desc Texture that specifies the color of the detail overlay. [member detail_albedo]'s alpha channel is used as a mask, even when the material is opaque. To use a dedicated texture as a mask, see [member detail_mask].
#desc [b]Note:[/b] [member detail_albedo] is [i]not[/i] modulated by [member albedo_color].
var detail_albedo: Texture2D;

#desc Specifies how the [member detail_albedo] should blend with the current [code]ALBEDO[/code]. See [enum BlendMode] for options.
var detail_blend_mode: int;

#desc If [code]true[/code], enables the detail overlay. Detail is a second texture that gets mixed over the surface of the object based on [member detail_mask] and [member detail_albedo]'s alpha channel. This can be used to add variation to objects, or to blend between two different albedo/normal textures.
var detail_enabled: bool;

#desc Texture used to specify how the detail textures get blended with the base textures. [member detail_mask] can be used together with [member detail_albedo]'s alpha channel (if any).
var detail_mask: Texture2D;

#desc Texture that specifies the per-pixel normal of the detail overlay. The [member detail_normal] texture only uses the red and green channels; the blue and alpha channels are ignored. The normal read from [member detail_normal] is oriented around the surface normal provided by the [Mesh].
#desc [b]Note:[/b] Godot expects the normal map to use X+, Y+, and Z+ coordinates. See [url=http://wiki.polycount.com/wiki/Normal_Map_Technical_Details#Common_Swizzle_Coordinates]this page[/url] for a comparison of normal map coordinates expected by popular engines.
var detail_normal: Texture2D;

#desc Specifies whether to use [code]UV[/code] or [code]UV2[/code] for the detail layer. See [enum DetailUV] for options.
var detail_uv_layer: int;

#desc The algorithm used for diffuse light scattering. See [enum DiffuseMode].
var diffuse_mode: int;

#desc If [code]true[/code], the object receives no ambient light.
var disable_ambient_light: bool;

#desc If [code]true[/code], the object receives no shadow that would otherwise be cast onto it.
var disable_receive_shadows: bool;

#desc Distance at which the object appears fully opaque.
#desc [b]Note:[/b] If [code]distance_fade_max_distance[/code] is less than [code]distance_fade_min_distance[/code], the behavior will be reversed. The object will start to fade away at [code]distance_fade_max_distance[/code] and will fully disappear once it reaches [code]distance_fade_min_distance[/code].
var distance_fade_max_distance: float;

#desc Distance at which the object starts to become visible. If the object is less than this distance away, it will be invisible.
#desc [b]Note:[/b] If [code]distance_fade_min_distance[/code] is greater than [code]distance_fade_max_distance[/code], the behavior will be reversed. The object will start to fade away at [code]distance_fade_max_distance[/code] and will fully disappear once it reaches [code]distance_fade_min_distance[/code].
var distance_fade_min_distance: float;

#desc Specifies which type of fade to use. Can be any of the [enum DistanceFadeMode]s.
var distance_fade_mode: int;

#desc The emitted light's color. See [member emission_enabled].
var emission: Color;

#desc If [code]true[/code], the body emits light. Emitting light makes the object appear brighter. The object can also cast light on other objects if a [VoxelGI], SDFGI, or [LightmapGI] is used and this object is used in baked lighting.
var emission_enabled: bool;

#desc Multiplier for emitted light. See [member emission_enabled].
var emission_energy_multiplier: float;

#desc Luminance of emitted light, measured in nits (candela per square meter). Only available when [member ProjectSettings.rendering/lights_and_shadows/use_physical_light_units] is enabled. The default is roughly equivalent to an indoor lightbulb.
var emission_intensity: float;

#desc Use [code]UV2[/code] to read from the [member emission_texture].
var emission_on_uv2: bool;

#desc Sets how [member emission] interacts with [member emission_texture]. Can either add or multiply. See [enum EmissionOperator] for options.
var emission_operator: int;

#desc Texture that specifies how much surface emits light at a given point.
var emission_texture: Texture2D;

#desc If [code]true[/code], the object is rendered at the same size regardless of distance.
var fixed_size: bool;

#desc If [code]true[/code], enables the vertex grow setting. See [member grow_amount].
var grow: bool;

#desc Grows object vertices in the direction of their normals.
var grow_amount: float;

#desc If [code]true[/code], uses parallax occlusion mapping to represent depth in the material instead of simple offset mapping (see [member heightmap_enabled]). This results in a more convincing depth effect, but is much more expensive on the GPU. Only enable this on materials where it makes a significant visual difference.
var heightmap_deep_parallax: bool;

#desc If [code]true[/code], height mapping is enabled (also called "parallax mapping" or "depth mapping"). See also [member normal_enabled]. Height mapping is a demanding feature on the GPU, so it should only be used on materials where it makes a significant visual difference.
#desc [b]Note:[/b] Height mapping is not supported if triplanar mapping is used on the same material. The value of [member heightmap_enabled] will be ignored if [member uv1_triplanar] is enabled.
var heightmap_enabled: bool;

#desc If [code]true[/code], flips the mesh's binormal vectors when interpreting the height map. If the heightmap effect looks strange when the camera moves (even with a reasonable [member heightmap_scale]), try setting this to [code]true[/code].
var heightmap_flip_binormal: bool;

#desc If [code]true[/code], flips the mesh's tangent vectors when interpreting the height map. If the heightmap effect looks strange when the camera moves (even with a reasonable [member heightmap_scale]), try setting this to [code]true[/code].
var heightmap_flip_tangent: bool;

#desc If [code]true[/code], interprets the height map texture as a depth map, with brighter values appearing to be "lower" in altitude compared to darker values.
#desc This can be enabled for compatibility with some materials authored for Godot 3.x. This is not necessary if the Invert import option was used to invert the depth map in Godot 3.x, in which case [member heightmap_flip_texture] should remain [code]false[/code].
var heightmap_flip_texture: bool;

#desc The number of layers to use for parallax occlusion mapping when the camera is up close to the material. Higher values result in a more convincing depth effect, especially in materials that have steep height changes. Higher values have a significant cost on the GPU, so it should only be increased on materials where it makes a significant visual difference.
#desc [b]Note:[/b] Only effective if [member heightmap_deep_parallax] is [code]true[/code].
var heightmap_max_layers: int;

#desc The number of layers to use for parallax occlusion mapping when the camera is far away from the material. Higher values result in a more convincing depth effect, especially in materials that have steep height changes. Higher values have a significant cost on the GPU, so it should only be increased on materials where it makes a significant visual difference.
#desc [b]Note:[/b] Only effective if [member heightmap_deep_parallax] is [code]true[/code].
var heightmap_min_layers: int;

#desc The heightmap scale to use for the parallax effect (see [member heightmap_enabled]). The default value is tuned so that the highest point (value = 255) appears to be 5 cm higher than the lowest point (value = 0). Higher values result in a deeper appearance, but may result in artifacts appearing when looking at the material from oblique angles, especially when the camera moves. Negative values can be used to invert the parallax effect, but this is different from inverting the texture using [member heightmap_flip_texture] as the material will also appear to be "closer" to the camera. In most cases, [member heightmap_scale] should be kept to a positive value.
#desc [b]Note:[/b] If the height map effect looks strange regardless of this value, try adjusting [member heightmap_flip_binormal] and [member heightmap_flip_tangent]. See also [member heightmap_texture] for recommendations on authoring heightmap textures, as the way the heightmap texture is authored affects how [member heightmap_scale] behaves.
var heightmap_scale: float;

#desc The texture to use as a height map. See also [member heightmap_enabled].
#desc For best results, the texture should be normalized (with [member heightmap_scale] reduced to compensate). In [url=https://gimp.org]GIMP[/url], this can be done using [b]Colors > Auto > Equalize[/b]. If the texture only uses a small part of its available range, the parallax effect may look strange, especially when the camera moves.
#desc [b]Note:[/b] To reduce memory usage and improve loading times, you may be able to use a lower-resolution heightmap texture as most heightmaps are only comprised of low-frequency data.
var heightmap_texture: Texture2D;

#desc A high value makes the material appear more like a metal. Non-metals use their albedo as the diffuse color and add diffuse to the specular reflection. With non-metals, the reflection appears on top of the albedo color. Metals use their albedo as a multiplier to the specular reflection and set the diffuse color to black resulting in a tinted reflection. Materials work better when fully metal or fully non-metal, values between [code]0[/code] and [code]1[/code] should only be used for blending between metal and non-metal sections. To alter the amount of reflection use [member roughness].
var metallic: float;

#desc Adjusts the strength of specular reflections. Specular reflections are composed of scene reflections and the specular lobe which is the bright spot that is reflected from light sources. When set to [code]0.0[/code], no specular reflections will be visible. This differs from the [constant SPECULAR_DISABLED] [enum SpecularMode] as [constant SPECULAR_DISABLED] only applies to the specular lobe from the light source.
#desc [b]Note:[/b] Unlike [member metallic], this is not energy-conserving, so it should be left at [code]0.5[/code] in most cases. See also [member roughness].
var metallic_specular: float;

#desc Texture used to specify metallic for an object. This is multiplied by [member metallic].
var metallic_texture: Texture2D;

#desc Specifies the channel of the [member metallic_texture] in which the metallic information is stored. This is useful when you store the information for multiple effects in a single texture. For example if you stored metallic in the red channel, roughness in the blue, and ambient occlusion in the green you could reduce the number of textures you use.
var metallic_texture_channel: int;

#desc The width of the shape outine.
var msdf_outline_size: float;

#desc The width of the range around the shape between the minimum and maximum representable signed distance.
var msdf_pixel_range: float;

#desc If [code]true[/code], depth testing is disabled and the object will be drawn in render order.
var no_depth_test: bool;

#desc If [code]true[/code], normal mapping is enabled.
var normal_enabled: bool;

#desc The strength of the normal map's effect.
var normal_scale: float;

#desc Texture used to specify the normal at a given pixel. The [member normal_texture] only uses the red and green channels; the blue and alpha channels are ignored. The normal read from [member normal_texture] is oriented around the surface normal provided by the [Mesh].
#desc [b]Note:[/b] The mesh must have both normals and tangents defined in its vertex data. Otherwise, the normal map won't render correctly and will only appear to darken the whole surface. If creating geometry with [SurfaceTool], you can use [method SurfaceTool.generate_normals] and [method SurfaceTool.generate_tangents] to automatically generate normals and tangents respectively.
#desc [b]Note:[/b] Godot expects the normal map to use X+, Y+, and Z+ coordinates. See [url=http://wiki.polycount.com/wiki/Normal_Map_Technical_Details#Common_Swizzle_Coordinates]this page[/url] for a comparison of normal map coordinates expected by popular engines.
#desc [b]Note:[/b] If [member detail_enabled] is [code]true[/code], the [member detail_albedo] texture is drawn [i]below[/i] the [member normal_texture]. To display a normal map [i]above[/i] the [member detail_albedo] texture, use [member detail_normal] instead.
var normal_texture: Texture2D;

var orm_texture: Texture2D;

#desc The number of horizontal frames in the particle sprite sheet. Only enabled when using [constant BILLBOARD_PARTICLES]. See [member billboard_mode].
var particles_anim_h_frames: int;

#desc If [code]true[/code], particle animations are looped. Only enabled when using [constant BILLBOARD_PARTICLES]. See [member billboard_mode].
var particles_anim_loop: bool;

#desc The number of vertical frames in the particle sprite sheet. Only enabled when using [constant BILLBOARD_PARTICLES]. See [member billboard_mode].
var particles_anim_v_frames: int;

#desc The point size in pixels. See [member use_point_size].
var point_size: float;

#desc Distance over which the fade effect takes place. The larger the distance the longer it takes for an object to fade.
var proximity_fade_distance: float;

#desc If [code]true[/code], the proximity fade effect is enabled. The proximity fade effect fades out each pixel based on its distance to another object.
var proximity_fade_enable: bool;

#desc If [code]true[/code], the refraction effect is enabled. Distorts transparency based on light from behind the object.
var refraction_enabled: bool;

#desc The strength of the refraction effect.
var refraction_scale: float;

#desc Texture that controls the strength of the refraction per-pixel. Multiplied by [member refraction_scale].
var refraction_texture: Texture2D;

#desc Specifies the channel of the [member ao_texture] in which the ambient occlusion information is stored. This is useful when you store the information for multiple effects in a single texture. For example if you stored metallic in the red channel, roughness in the blue, and ambient occlusion in the green you could reduce the number of textures you use.
var refraction_texture_channel: int;

#desc Sets the strength of the rim lighting effect.
var rim: float;

#desc If [code]true[/code], rim effect is enabled. Rim lighting increases the brightness at glancing angles on an object.
#desc [b]Note:[/b] Rim lighting is not visible if the material's [member shading_mode] is [constant SHADING_MODE_UNSHADED].
var rim_enabled: bool;

#desc Texture used to set the strength of the rim lighting effect per-pixel. Multiplied by [member rim].
var rim_texture: Texture2D;

#desc The amount of to blend light and albedo color when rendering rim effect. If [code]0[/code] the light color is used, while [code]1[/code] means albedo color is used. An intermediate value generally works best.
var rim_tint: float;

#desc Surface reflection. A value of [code]0[/code] represents a perfect mirror while a value of [code]1[/code] completely blurs the reflection. See also [member metallic].
var roughness: float;

#desc Texture used to control the roughness per-pixel. Multiplied by [member roughness].
var roughness_texture: Texture2D;

#desc Specifies the channel of the [member ao_texture] in which the ambient occlusion information is stored. This is useful when you store the information for multiple effects in a single texture. For example if you stored metallic in the red channel, roughness in the blue, and ambient occlusion in the green you could reduce the number of textures you use.
var roughness_texture_channel: int;

#desc Sets whether the shading takes place per-pixel or per-vertex. Per-vertex lighting is faster, making it the best choice for mobile applications, however it looks considerably worse than per-pixel.
var shading_mode: int;

#desc If [code]true[/code], enables the "shadow to opacity" render mode where lighting modifies the alpha so shadowed areas are opaque and non-shadowed areas are transparent. Useful for overlaying shadows onto a camera feed in AR.
var shadow_to_opacity: bool;

#desc The method for rendering the specular blob. See [enum SpecularMode].
#desc [b]Note:[/b] Only applies to the specular blob. Does not affect specular reflections from the Sky, SSR, or ReflectionProbes.
var specular_mode: int;

#desc If [code]true[/code], subsurface scattering is enabled. Emulates light that penetrates an object's surface, is scattered, and then emerges.
var subsurf_scatter_enabled: bool;

#desc If [code]true[/code], subsurface scattering will use a special mode optimized for the color and density of human skin.
var subsurf_scatter_skin_mode: bool;

#desc The strength of the subsurface scattering effect.
var subsurf_scatter_strength: float;

#desc Texture used to control the subsurface scattering strength. Stored in the red texture channel. Multiplied by [member subsurf_scatter_strength].
var subsurf_scatter_texture: Texture2D;

var subsurf_scatter_transmittance_boost: float;

var subsurf_scatter_transmittance_color: Color;

var subsurf_scatter_transmittance_depth: float;

var subsurf_scatter_transmittance_enabled: bool;

var subsurf_scatter_transmittance_texture: Texture2D;

#desc Filter flags for the texture. See [enum TextureFilter] for options.
#desc [b]Note:[/b] [member heightmap_texture] is always sampled with linear filtering, even if nearest-neighbor filtering is selected here. This is to ensure the heightmap effect looks as intended. If you need sharper height transitions between pixels, resize the heightmap texture in an image editor with nearest-neighbor filtering.
var texture_filter: int;

#desc Repeat flags for the texture. See [enum TextureFilter] for options.
var texture_repeat: bool;

#desc If [code]true[/code], transparency is enabled on the body. See also [member blend_mode].
var transparency: int;

var use_particle_trails: bool;

#desc If [code]true[/code], render point size can be changed.
#desc [b]Note:[/b] This is only effective for objects whose geometry is point-based rather than triangle-based. See also [member point_size].
var use_point_size: bool;

#desc How much to offset the [code]UV[/code] coordinates. This amount will be added to [code]UV[/code] in the vertex function. This can be used to offset a texture. The Z component is used when [member uv1_triplanar] is enabled, but it is not used anywhere else.
var uv1_offset: Vector3;

#desc How much to scale the [code]UV[/code] coordinates. This is multiplied by [code]UV[/code] in the vertex function. The Z component is used when [member uv1_triplanar] is enabled, but it is not used anywhere else.
var uv1_scale: Vector3;

#desc If [code]true[/code], instead of using [code]UV[/code] textures will use a triplanar texture lookup to determine how to apply textures. Triplanar uses the orientation of the object's surface to blend between texture coordinates. It reads from the source texture 3 times, once for each axis and then blends between the results based on how closely the pixel aligns with each axis. This is often used for natural features to get a realistic blend of materials. Because triplanar texturing requires many more texture reads per-pixel it is much slower than normal UV texturing. Additionally, because it is blending the texture between the three axes, it is unsuitable when you are trying to achieve crisp texturing.
var uv1_triplanar: bool;

#desc A lower number blends the texture more softly while a higher number blends the texture more sharply.
#desc [b]Note:[/b] [member uv1_triplanar_sharpness] is clamped between [code]0.0[/code] and [code]150.0[/code] (inclusive) as values outside that range can look broken depending on the mesh.
var uv1_triplanar_sharpness: float;

#desc If [code]true[/code], triplanar mapping for [code]UV[/code] is calculated in world space rather than object local space. See also [member uv1_triplanar].
var uv1_world_triplanar: bool;

#desc How much to offset the [code]UV2[/code] coordinates. This amount will be added to [code]UV2[/code] in the vertex function. This can be used to offset a texture. The Z component is used when [member uv2_triplanar] is enabled, but it is not used anywhere else.
var uv2_offset: Vector3;

#desc How much to scale the [code]UV2[/code] coordinates. This is multiplied by [code]UV2[/code] in the vertex function. The Z component is used when [member uv2_triplanar] is enabled, but it is not used anywhere else.
var uv2_scale: Vector3;

#desc If [code]true[/code], instead of using [code]UV2[/code] textures will use a triplanar texture lookup to determine how to apply textures. Triplanar uses the orientation of the object's surface to blend between texture coordinates. It reads from the source texture 3 times, once for each axis and then blends between the results based on how closely the pixel aligns with each axis. This is often used for natural features to get a realistic blend of materials. Because triplanar texturing requires many more texture reads per-pixel it is much slower than normal UV texturing. Additionally, because it is blending the texture between the three axes, it is unsuitable when you are trying to achieve crisp texturing.
var uv2_triplanar: bool;

#desc A lower number blends the texture more softly while a higher number blends the texture more sharply.
#desc [b]Note:[/b] [member uv2_triplanar_sharpness] is clamped between [code]0.0[/code] and [code]150.0[/code] (inclusive) as values outside that range can look broken depending on the mesh.
var uv2_triplanar_sharpness: float;

#desc If [code]true[/code], triplanar mapping for [code]UV2[/code] is calculated in world space rather than object local space. See also [member uv2_triplanar].
var uv2_world_triplanar: bool;

#desc If [code]true[/code], vertex colors are considered to be stored in sRGB color space and are converted to linear color space during rendering. If [code]false[/code], vertex colors are considered to be stored in linear color space and are rendered as-is. See also [member albedo_texture_force_srgb].
#desc [b]Note:[/b] Only effective when using the Vulkan Clustered or Vulkan Mobile backends.
var vertex_color_is_srgb: bool;

#desc If [code]true[/code], the vertex color is used as albedo color.
var vertex_color_use_as_albedo: bool;



#desc Returns [code]true[/code], if the specified [enum Feature] is enabled.
func get_feature(feature: int) -> bool:
	pass;

#desc Returns [code]true[/code], if the specified flag is enabled. See [enum Flags] enumerator for options.
func get_flag(flag: int) -> bool:
	pass;

#desc Returns the [Texture2D] associated with the specified [enum TextureParam].
func get_texture(param: int) -> Texture2D:
	pass;

#desc If [code]true[/code], enables the specified [enum Feature]. Many features that are available in [BaseMaterial3D]s need to be enabled before use. This way the cost for using the feature is only incurred when specified. Features can also be enabled by setting the corresponding member to [code]true[/code].
func set_feature(feature: int, enable: bool) -> void:
	pass;

#desc If [code]true[/code], enables the specified flag. Flags are optional behavior that can be turned on and off. Only one flag can be enabled at a time with this function, the flag enumerators cannot be bit-masked together to enable or disable multiple flags at once. Flags can also be enabled by setting the corresponding member to [code]true[/code]. See [enum Flags] enumerator for options.
func set_flag(flag: int, enable: bool) -> void:
	pass;

#desc Sets the texture for the slot specified by [param param]. See [enum TextureParam] for available slots.
func set_texture(param: int, texture: Texture2D) -> void:
	pass;


