extends Resource
#brief Resource for environment nodes (like [WorldEnvironment]) that define multiple rendering options.
#desc Resource for environment nodes (like [WorldEnvironment]) that define multiple environment operations (such as background [Sky] or [Color], ambient light, fog, depth-of-field...). These parameters affect the final render of the scene. The order of these operations is:
#desc - Depth of Field Blur
#desc - Glow
#desc - Tonemap (Auto Exposure)
#desc - Adjustments
class_name Environment

#desc Clears the background using the clear color defined in [member ProjectSettings.rendering/environment/defaults/default_clear_color].
const BG_CLEAR_COLOR = 0;

#desc Clears the background using a custom clear color.
const BG_COLOR = 1;

#desc Displays a user-defined sky in the background.
const BG_SKY = 2;

#desc Displays a [CanvasLayer] in the background.
const BG_CANVAS = 3;

#desc Keeps on screen every pixel drawn in the background. This is the fastest background mode, but it can only be safely used in fully-interior scenes (no visible sky or sky reflections). If enabled in a scene where the background is visible, "ghost trail" artifacts will be visible when moving the camera.
const BG_KEEP = 4;

#desc Displays a camera feed in the background.
const BG_CAMERA_FEED = 5;

#desc Represents the size of the [enum BGMode] enum.
const BG_MAX = 6;

#desc Gather ambient light from whichever source is specified as the background.
const AMBIENT_SOURCE_BG = 0;

#desc Disable ambient light. This provides a slight performance boost over [constant AMBIENT_SOURCE_SKY].
const AMBIENT_SOURCE_DISABLED = 1;

#desc Specify a specific [Color] for ambient light. This provides a slight performance boost over [constant AMBIENT_SOURCE_SKY].
const AMBIENT_SOURCE_COLOR = 2;

#desc Gather ambient light from the [Sky] regardless of what the background is.
const AMBIENT_SOURCE_SKY = 3;

#desc Use the background for reflections.
const REFLECTION_SOURCE_BG = 0;

#desc Disable reflections. This provides a slight performance boost over other options.
const REFLECTION_SOURCE_DISABLED = 1;

#desc Use the [Sky] for reflections regardless of what the background is.
const REFLECTION_SOURCE_SKY = 2;

#desc Linear tonemapper operator. Reads the linear data and passes it on unmodified. This can cause bright lighting to look blown out, with noticeable clipping in the output colors.
const TONE_MAPPER_LINEAR = 0;

#desc Reinhardt tonemapper operator. Performs a variation on rendered pixels' colors by this formula: [code]color = color / (1 + color)[/code]. This avoids clipping bright highlights, but the resulting image can look a bit dull.
const TONE_MAPPER_REINHARDT = 1;

#desc Filmic tonemapper operator. This avoids clipping bright highlights, with a resulting image that usually looks more vivid than [constant TONE_MAPPER_REINHARDT].
const TONE_MAPPER_FILMIC = 2;

#desc Use the Academy Color Encoding System tonemapper. ACES is slightly more expensive than other options, but it handles bright lighting in a more realistic fashion by desaturating it as it becomes brighter. ACES typically has a more contrasted output compared to [constant TONE_MAPPER_REINHARDT] and [constant TONE_MAPPER_FILMIC].
#desc [b]Note:[/b] This tonemapping operator is called "ACES Fitted" in Godot 3.x.
const TONE_MAPPER_ACES = 3;

#desc Additive glow blending mode. Mostly used for particles, glows (bloom), lens flare, bright sources.
const GLOW_BLEND_MODE_ADDITIVE = 0;

#desc Screen glow blending mode. Increases brightness, used frequently with bloom.
const GLOW_BLEND_MODE_SCREEN = 1;

#desc Soft light glow blending mode. Modifies contrast, exposes shadows and highlights (vivid bloom).
const GLOW_BLEND_MODE_SOFTLIGHT = 2;

#desc Replace glow blending mode. Replaces all pixels' color by the glow value. This can be used to simulate a full-screen blur effect by tweaking the glow parameters to match the original image's brightness.
const GLOW_BLEND_MODE_REPLACE = 3;

#desc Mixes the glow with the underlying color to avoid increasing brightness as much while still maintaining a glow effect.
const GLOW_BLEND_MODE_MIX = 4;

#desc Use 50% scale for SDFGI on the Y (vertical) axis. SDFGI cells will be twice as short as they are wide. This allows providing increased GI detail and reduced light leaking with thin floors and ceilings. This is usually the best choice for scenes that don't feature much verticality.
const SDFGI_Y_SCALE_50_PERCENT = 0;

#desc Use 75% scale for SDFGI on the Y (vertical) axis. This is a balance between the 50% and 100% SDFGI Y scales.
const SDFGI_Y_SCALE_75_PERCENT = 1;

#desc Use 100% scale for SDFGI on the Y (vertical) axis. SDFGI cells will be as tall as they are wide. This is usually the best choice for highly vertical scenes. The downside is that light leaking may become more noticeable with thin floors and ceilings.
const SDFGI_Y_SCALE_100_PERCENT = 2;


#desc The global brightness value of the rendered scene. Effective only if [code]adjustment_enabled[/code] is [code]true[/code].
var adjustment_brightness: float;

#desc The [Texture2D] or [Texture3D] lookup table (LUT) to use for the built-in post-process color grading. Can use a [GradientTexture1D] for a 1-dimensional LUT, or a [Texture3D] for a more complex LUT. Effective only if [code]adjustment_enabled[/code] is [code]true[/code].
var adjustment_color_correction: Texture;

#desc The global contrast value of the rendered scene (default value is 1). Effective only if [code]adjustment_enabled[/code] is [code]true[/code].
var adjustment_contrast: float;

#desc If [code]true[/code], enables the [code]adjustment_*[/code] properties provided by this resource. If [code]false[/code], modifications to the [code]adjustment_*[/code] properties will have no effect on the rendered scene.
var adjustment_enabled: bool;

#desc The global color saturation value of the rendered scene (default value is 1). Effective only if [code]adjustment_enabled[/code] is [code]true[/code].
var adjustment_saturation: float;

#desc The ambient light's [Color]. Only effective if [member ambient_light_sky_contribution] is lower than [code]1.0[/code] (exclusive).
var ambient_light_color: Color;

#desc The ambient light's energy. The higher the value, the stronger the light. Only effective if [member ambient_light_sky_contribution] is lower than [code]1.0[/code] (exclusive).
var ambient_light_energy: float;

#desc Defines the amount of light that the sky brings on the scene. A value of [code]0.0[/code] means that the sky's light emission has no effect on the scene illumination, thus all ambient illumination is provided by the ambient light. On the contrary, a value of [code]1.0[/code] means that [i]all[/i] the light that affects the scene is provided by the sky, thus the ambient light parameter has no effect on the scene.
#desc [b]Note:[/b] [member ambient_light_sky_contribution] is internally clamped between [code]0.0[/code] and [code]1.0[/code] (inclusive).
var ambient_light_sky_contribution: float;

#desc The ambient light source to use for rendering materials and global illumination.
var ambient_light_source: int;

#desc The ID of the camera feed to show in the background.
var background_camera_feed_id: int;

#desc The maximum layer ID to display. Only effective when using the [constant BG_CANVAS] background mode.
var background_canvas_max_layer: int;

#desc The [Color] displayed for clear areas of the scene. Only effective when using the [constant BG_COLOR] background mode.
var background_color: Color;

#desc Multiplier for background energy. Increase to make background brighter, decrease to make background dimmer.
var background_energy_multiplier: float;

#desc Luminance of background measured in nits (candela per square meter). Only used when [member ProjectSettings.rendering/lights_and_shadows/use_physical_light_units] is enabled. The default value is roughly equivalent to the sky at midday.
var background_intensity: float;

#desc The background mode. See [enum BGMode] for possible values.
var background_mode: int;

#desc If set above [code]0.0[/code] (exclusive), blends between the fog's color and the color of the background [Sky]. This has a small performance cost when set above [code]0.0[/code]. Must have [member background_mode] set to [constant BG_SKY].
#desc This is useful to simulate [url=https://en.wikipedia.org/wiki/Aerial_perspective]aerial perspective[/url] in large scenes with low density fog. However, it is not very useful for high-density fog, as the sky will shine through. When set to [code]1.0[/code], the fog color comes completely from the [Sky]. If set to [code]0.0[/code], aerial perspective is disabled.
var fog_aerial_perspective: float;

#desc The [i]exponential[/i] fog density to use. Higher values result in a more dense fog. Fog rendering is exponential as in real life.
var fog_density: float;

#desc If [code]true[/code], fog effects are enabled.
var fog_enabled: bool;

#desc The height at which the height fog effect begins.
var fog_height: float;

#desc The density used to increase fog as height decreases. To make fog increase as height increases, use a negative value.
var fog_height_density: float;

#desc The fog's color.
var fog_light_color: Color;

#desc The fog's brightness. Higher values result in brighter fog.
var fog_light_energy: float;

#desc The factor to use when affecting the sky with non-volumetric fog. [code]1.0[/code] means that fog can fully obscure the sky. Lower values reduce the impact of fog on sky rendering, with [code]0.0[/code] not affecting sky rendering at all.
#desc [b]Note:[/b] [member fog_sky_affect] has no visual effect if [member fog_aerial_perspective] is [code]1.0[/code].
var fog_sky_affect: float;

#desc If set above [code]0.0[/code], renders the scene's directional light(s) in the fog color depending on the view angle. This can be used to give the impression that the sun is "piercing" through the fog.
var fog_sun_scatter: float;

#desc The glow blending mode.
var glow_blend_mode: int;

#desc The bloom's intensity. If set to a value higher than [code]0[/code], this will make glow visible in areas darker than the [member glow_hdr_threshold].
var glow_bloom: float;

#desc If [code]true[/code], the glow effect is enabled.
var glow_enabled: bool;

#desc The higher threshold of the HDR glow. Areas brighter than this threshold will be clamped for the purposes of the glow effect.
var glow_hdr_luminance_cap: float;

#desc The bleed scale of the HDR glow.
var glow_hdr_scale: float;

#desc The lower threshold of the HDR glow. When using the OpenGL renderer (which doesn't support HDR), this needs to be below [code]1.0[/code] for glow to be visible. A value of [code]0.9[/code] works well in this case.
var glow_hdr_threshold: float;

#desc The overall brightness multiplier of the glow effect. When using the OpenGL renderer, this should be increased to [code]1.5[/code] to compensate for the lack of HDR rendering.
var glow_intensity: float;

#desc The intensity of the 1st level of glow. This is the most "local" level (least blurry).
var glow_levels/1: float;

#desc The intensity of the 2nd level of glow.
var glow_levels/2: float;

#desc The intensity of the 3rd level of glow.
var glow_levels/3: float;

#desc The intensity of the 4th level of glow.
var glow_levels/4: float;

#desc The intensity of the 5th level of glow.
var glow_levels/5: float;

#desc The intensity of the 6th level of glow.
var glow_levels/6: float;

#desc The intensity of the 7th level of glow. This is the most "global" level (blurriest).
var glow_levels/7: float;

#desc The texture that should be used as a glow map to [i]multiply[/i] the resulting glow color according to [member glow_map_strength]. This can be used to create a "lens dirt" effect. The texture's RGB color channels are used for modulation, but the alpha channel is ignored.
#desc [b]Note:[/b] The texture will be stretched to fit the screen. Therefore, it's recommended to use a texture with an aspect ratio that matches your project's base aspect ratio (typically 16:9).
var glow_map: Texture;

#desc How strong of an impact the [member glow_map] should have on the overall glow effect. A strength of [code]0.0[/code] means the glow map has no effect on the overall glow effect. A strength of [code]1.0[/code] means the glow has a full effect on the overall glow effect (and can turn off glow entirely in specific areas of the screen if the glow map has black areas).
var glow_map_strength: float;

#desc When using the [constant GLOW_BLEND_MODE_MIX] [member glow_blend_mode], this controls how much the source image is blended with the glow layer. A value of [code]0.0[/code] makes the glow rendering invisible, while a value of [code]1.0[/code] is equivalent to [constant GLOW_BLEND_MODE_REPLACE].
var glow_mix: float;

#desc If [code]true[/code], glow levels will be normalized so that summed together their intensities equal [code]1.0[/code].
var glow_normalized: bool;

#desc The strength of the glow effect. This applies as the glow is blurred across the screen and increases the distance and intensity of the blur. When using the OpenGL renderer, this should be increased to 1.3 to compensate for the lack of HDR rendering.
var glow_strength: float;

#desc The reflected (specular) light source.
var reflected_light_source: int;

#desc The energy multiplier applied to light every time it bounces from a surface when using SDFGI. Values greater than [code]0.0[/code] will simulate multiple bounces, resulting in a more realistic appearance. Increasing [member sdfgi_bounce_feedback] generally has no performance impact. See also [member sdfgi_energy].
#desc [b]Note:[/b] Values greater than [code]0.5[/code] can cause infinite feedback loops and should be avoided in scenes with bright materials.
#desc [b]Note:[/b] If [member sdfgi_bounce_feedback] is [code]0.0[/code], indirect lighting will not be represented in reflections as light will only bounce one time.
var sdfgi_bounce_feedback: float;

#desc [b]Note:[/b] This property is linked to [member sdfgi_min_cell_size] and [member sdfgi_max_distance]. Changing its value will automatically change those properties as well.
var sdfgi_cascade0_distance: float;

#desc The number of cascades to use for SDFGI (between 1 and 8). A higher number of cascades allows displaying SDFGI further away while preserving detail up close, at the cost of performance. When using SDFGI on small-scale levels, [member sdfgi_cascades] can often be decreased between [code]1[/code] and [code]4[/code] to improve performance.
var sdfgi_cascades: int;

#desc If [code]true[/code], enables signed distance field global illumination for meshes that have their [member GeometryInstance3D.gi_mode] set to [constant GeometryInstance3D.GI_MODE_STATIC]. SDFGI is a real-time global illumination technique that works well with procedurally generated and user-built levels, including in situations where geometry is created during gameplay. The signed distance field is automatically generated around the camera as it moves. Dynamic lights are supported, but dynamic occluders and emissive surfaces are not.
#desc [b]Performance:[/b] SDFGI is relatively demanding on the GPU and is not suited to low-end hardware such as integrated graphics (consider [LightmapGI] instead). To improve SDFGI performance, enable [member ProjectSettings.rendering/global_illumination/gi/use_half_resolution] in the Project Settings.
#desc [b]Note:[/b] Meshes should have sufficiently thick walls to avoid light leaks (avoid one-sided walls). For interior levels, enclose your level geometry in a sufficiently large box and bridge the loops to close the mesh.
var sdfgi_enabled: bool;

#desc The energy multiplier to use for SDFGI. Higher values will result in brighter indirect lighting and reflections. See also [member sdfgi_bounce_feedback].
var sdfgi_energy: float;

#desc The maximum distance at which SDFGI is visible. Beyond this distance, environment lighting or other sources of GI such as [ReflectionProbe] will be used as a fallback.
#desc [b]Note:[/b] This property is linked to [member sdfgi_min_cell_size] and [member sdfgi_cascade0_distance]. Changing its value will automatically change those properties as well.
var sdfgi_max_distance: float;

#desc The cell size to use for the closest SDFGI cascade (in 3D units). Lower values allow SDFGI to be more precise up close, at the cost of making SDFGI updates more demanding. This can cause stuttering when the camera moves fast. Higher values allow SDFGI to cover more ground, while also reducing the performance impact of SDFGI updates.
#desc [b]Note:[/b] This property is linked to [member sdfgi_max_distance] and [member sdfgi_cascade0_distance]. Changing its value will automatically change those properties as well.
var sdfgi_min_cell_size: float;

#desc The normal bias to use for SDFGI probes. Increasing this value can reduce visible streaking artifacts on sloped surfaces, at the cost of increased light leaking.
var sdfgi_normal_bias: float;

#desc The constant bias to use for SDFGI probes. Increasing this value can reduce visible streaking artifacts on sloped surfaces, at the cost of increased light leaking.
var sdfgi_probe_bias: float;

#desc If [code]true[/code], SDFGI takes the environment lighting into account. This should be set to [code]false[/code] for interior scenes.
var sdfgi_read_sky_light: bool;

#desc If [code]true[/code], SDFGI uses an occlusion detection approach to reduce light leaking. Occlusion may however introduce dark blotches in certain spots, which may be undesired in mostly outdoor scenes. [member sdfgi_use_occlusion] has a performance impact and should only be enabled when needed.
var sdfgi_use_occlusion: bool;

#desc The Y scale to use for SDFGI cells. Lower values will result in SDFGI cells being packed together more closely on the Y axis. This is used to balance between quality and covering a lot of vertical ground. [member sdfgi_y_scale] should be set depending on how vertical your scene is (and how fast your camera may move on the Y axis).
var sdfgi_y_scale: int;

#desc The [Sky] resource used for this [Environment].
var sky: Sky;

#desc If set to a value greater than [code]0.0[/code], overrides the field of view to use for sky rendering. If set to [code]0.0[/code], the same FOV as the current [Camera3D] is used for sky rendering.
var sky_custom_fov: float;

#desc The rotation to use for sky rendering.
var sky_rotation: Vector3;

#desc The screen-space ambient occlusion intensity on materials that have an AO texture defined. Values higher than [code]0[/code] will make the SSAO effect visible in areas darkened by AO textures.
var ssao_ao_channel_affect: float;

#desc Sets the strength of the additional level of detail for the screen-space ambient occlusion effect. A high value makes the detail pass more prominent, but it may contribute to aliasing in your final image.
var ssao_detail: float;

#desc If [code]true[/code], the screen-space ambient occlusion effect is enabled. This darkens objects' corners and cavities to simulate ambient light not reaching the entire object as in real life. This works well for small, dynamic objects, but baked lighting or ambient occlusion textures will do a better job at displaying ambient occlusion on large static objects. Godot uses a form of SSAO called Adaptive Screen Space Ambient Occlusion which is itself a form of Horizon Based Ambient Occlusion.
var ssao_enabled: bool;

#desc The threshold for considering whether a given point on a surface is occluded or not represented as an angle from the horizon mapped into the [code]0.0-1.0[/code] range. A value of [code]1.0[/code] results in no occlusion.
var ssao_horizon: float;

#desc The primary screen-space ambient occlusion intensity. Acts as a multiplier for the screen-space ambient occlusion effect. A higher value results in darker occlusion.
var ssao_intensity: float;

#desc The screen-space ambient occlusion intensity in direct light. In real life, ambient occlusion only applies to indirect light, which means its effects can't be seen in direct light. Values higher than [code]0[/code] will make the SSAO effect visible in direct light.
var ssao_light_affect: float;

#desc The distribution of occlusion. A higher value results in darker occlusion, similar to [member ssao_intensity], but with a sharper falloff.
var ssao_power: float;

#desc The distance at which objects can occlude each other when calculating screen-space ambient occlusion. Higher values will result in occlusion over a greater distance at the cost of performance and quality.
var ssao_radius: float;

#desc The amount that the screen-space ambient occlusion effect is allowed to blur over the edges of objects. Setting too high will result in aliasing around the edges of objects. Setting too low will make object edges appear blurry.
var ssao_sharpness: float;

#desc If [code]true[/code], the screen-space indirect lighting effect is enabled. Screen space indirect lighting is a form of indirect lighting that allows diffuse light to bounce between nearby objects. Screen-space indirect lighting works very similarly to screen-space ambient occlusion, in that it only affects a limited range. It is intended to be used along with a form of proper global illumination like SDFGI or [VoxelGI]. Screen-space indirect lighting is not affected by individual light's [member Light3D.light_indirect_energy].
var ssil_enabled: bool;

#desc The brightness multiplier for the screen-space indirect lighting effect. A higher value will result in brighter light.
var ssil_intensity: float;

#desc Amount of normal rejection used when calculating screen-space indirect lighting. Normal rejection uses the normal of a given sample point to reject samples that are facing away from the current pixel. Normal rejection is necessary to avoid light leaking when only one side of an object is illuminated. However, normal rejection can be disabled if light leaking is desirable, such as when the scene mostly contains emissive objects that emit light from faces that cannot be seen from the camera.
var ssil_normal_rejection: float;

#desc The distance that bounced lighting can travel when using the screen space indirect lighting effect. A larger value will result in light bouncing further in a scene, but may result in under-sampling artifacts which look like long spikes surrounding light sources.
var ssil_radius: float;

#desc The amount that the screen-space indirect lighting effect is allowed to blur over the edges of objects. Setting too high will result in aliasing around the edges of objects. Setting too low will make object edges appear blurry.
var ssil_sharpness: float;

#desc The depth tolerance for screen-space reflections.
var ssr_depth_tolerance: float;

#desc If [code]true[/code], screen-space reflections are enabled. Screen-space reflections are more accurate than reflections from [VoxelGI]s or [ReflectionProbe]s, but are slower and can't reflect surfaces occluded by others.
var ssr_enabled: bool;

#desc The fade-in distance for screen-space reflections. Affects the area from the reflected material to the screen-space reflection). Only positive values are valid (negative values will be clamped to [code]0.0[/code]).
var ssr_fade_in: float;

#desc The fade-out distance for screen-space reflections. Affects the area from the screen-space reflection to the "global" reflection. Only positive values are valid (negative values will be clamped to [code]0.0[/code]).
var ssr_fade_out: float;

#desc The maximum number of steps for screen-space reflections. Higher values are slower.
var ssr_max_steps: int;

#desc The default exposure used for tonemapping. Higher values result in a brighter image. See also [member tonemap_white].
var tonemap_exposure: float;

#desc The tonemapping mode to use. Tonemapping is the process that "converts" HDR values to be suitable for rendering on a LDR display. (Godot doesn't support rendering on HDR displays yet.)
var tonemap_mode: int;

#desc The white reference value for tonemapping (also called "whitepoint"). Higher values can make highlights look less blown out, and will also slightly darken the whole scene as a result. Only effective if the [member tonemap_mode] isn't set to [constant TONE_MAPPER_LINEAR]. See also [member tonemap_exposure].
var tonemap_white: float;

#desc The [Color] of the volumetric fog when interacting with lights. Mist and fog have an albedo close to [code]Color(1, 1, 1, 1)[/code] while smoke has a darker albedo.
var volumetric_fog_albedo: Color;

#desc Scales the strength of ambient light used in the volumetric fog. A value of [code]0.0[/code] means that ambient light will not impact the volumetric fog. [member volumetric_fog_ambient_inject] has a small performance cost when set above [code]0.0[/code].
#desc [b]Note:[/b] This has no visible effect if [member volumetric_fog_density] is [code]0.0[/code] or if [member volumetric_fog_albedo] is a fully black color.
var volumetric_fog_ambient_inject: float;

#desc The direction of scattered light as it goes through the volumetric fog. A value close to [code]1.0[/code] means almost all light is scattered forward. A value close to [code]0.0[/code] means light is scattered equally in all directions. A value close to [code]-1.0[/code] means light is scattered mostly backward. Fog and mist scatter light slightly forward, while smoke scatters light equally in all directions.
var volumetric_fog_anisotropy: float;

#desc The base [i]exponential[/i] density of the volumetric fog. Set this to the lowest density you want to have globally. [FogVolume]s can be used to add to or subtract from this density in specific areas. Fog rendering is exponential as in real life.
#desc A value of [code]0.0[/code] disables global volumetric fog while allowing [FogVolume]s to display volumetric fog in specific areas.
var volumetric_fog_density: float;

#desc The distribution of size down the length of the froxel buffer. A higher value compresses the froxels closer to the camera and places more detail closer to the camera.
var volumetric_fog_detail_spread: float;

#desc The emitted light from the volumetric fog. Even with emission, volumetric fog will not cast light onto other surfaces. Emission is useful to establish an ambient color. As the volumetric fog effect uses single-scattering only, fog tends to need a little bit of emission to soften the harsh shadows.
var volumetric_fog_emission: Color;

#desc The brightness of the emitted light from the volumetric fog.
var volumetric_fog_emission_energy: float;

#desc Enables the volumetric fog effect. Volumetric fog uses a screen-aligned froxel buffer to calculate accurate volumetric scattering in the short to medium range. Volumetric fog interacts with [FogVolume]s and lights to calculate localized and global fog. Volumetric fog uses a PBR single-scattering model based on extinction, scattering, and emission which it exposes to users as density, albedo, and emission.
#desc [b]Note:[/b] Volumetric fog is only available in the forward plus renderer. It is not available in the mobile renderer or the compatibility renderer.
var volumetric_fog_enabled: bool;

#desc Scales the strength of Global Illumination used in the volumetric fog's albedo color. A value of [code]0.0[/code] means that Global Illumination will not impact the volumetric fog. [member volumetric_fog_gi_inject] has a small performance cost when set above [code]0.0[/code].
#desc [b]Note:[/b] This has no visible effect if [member volumetric_fog_density] is [code]0.0[/code] or if [member volumetric_fog_albedo] is a fully black color.
#desc [b]Note:[/b] Only [VoxelGI] and SDFGI ([member Environment.sdfgi_enabled]) are taken into account when using [member volumetric_fog_gi_inject]. Global illumination from [LightmapGI], [ReflectionProbe] and SSIL (see [member ssil_enabled]) will be ignored by volumetric fog.
var volumetric_fog_gi_inject: float;

#desc The distance over which the volumetric fog is computed. Increase to compute fog over a greater range, decrease to add more detail when a long range is not needed. For best quality fog, keep this as low as possible. See also [member ProjectSettings.rendering/environment/volumetric_fog/volume_depth].
var volumetric_fog_length: float;

#desc The factor to use when affecting the sky with volumetric fog. [code]1.0[/code] means that volumetric fog can fully obscure the sky. Lower values reduce the impact of volumetric fog on sky rendering, with [code]0.0[/code] not affecting sky rendering at all.
#desc [b]Note:[/b] [member volumetric_fog_sky_affect] also affects [FogVolume]s, even if [member volumetric_fog_density] is [code]0.0[/code]. If you notice [FogVolume]s are disappearing when looking towards the sky, set [member volumetric_fog_sky_affect] to [code]1.0[/code].
var volumetric_fog_sky_affect: float;

#desc The amount by which to blend the last frame with the current frame. A higher number results in smoother volumetric fog, but makes "ghosting" much worse. A lower value reduces ghosting but can result in the per-frame temporal jitter becoming visible.
var volumetric_fog_temporal_reprojection_amount: float;

#desc Enables temporal reprojection in the volumetric fog. Temporal reprojection blends the current frame's volumetric fog with the last frame's volumetric fog to smooth out jagged edges. The performance cost is minimal; however, it leads to moving [FogVolume]s and [Light3D]s "ghosting" and leaving a trail behind them. When temporal reprojection is enabled, try to avoid moving [FogVolume]s or [Light3D]s too fast. Short-lived dynamic lighting effects should have [member Light3D.light_volumetric_fog_energy] set to [code]0.0[/code] to avoid ghosting.
var volumetric_fog_temporal_reprojection_enabled: bool;



#desc Returns the intensity of the glow level [param idx].
func get_glow_level(idx: int) -> float:
	pass;

#desc Sets the intensity of the glow level [param idx]. A value above [code]0.0[/code] enables the level. Each level relies on the previous level. This means that enabling higher glow levels will slow down the glow effect rendering, even if previous levels aren't enabled.
func set_glow_level(idx: int, intensity: float) -> void:
	pass;


