#brief Provides a base class for different kinds of light nodes.
#desc Light3D is the [i]abstract[/i] base class for light nodes. As it can't be instantiated, it shouldn't be used directly. Other types of light nodes inherit from it. Light3D contains the common variables and parameters used for lighting.
class_name Light3D

#desc Constant for accessing [member light_energy].
const PARAM_ENERGY = 0;

#desc Constant for accessing [member light_indirect_energy].
const PARAM_INDIRECT_ENERGY = 1;

#desc Constant for accessing [member light_volumetric_fog_energy].
const PARAM_VOLUMETRIC_FOG_ENERGY = 2;

#desc Constant for accessing [member light_specular].
const PARAM_SPECULAR = 3;

#desc Constant for accessing [member OmniLight3D.omni_range] or [member SpotLight3D.spot_range].
const PARAM_RANGE = 4;

#desc Constant for accessing [member light_size].
const PARAM_SIZE = 5;

#desc Constant for accessing [member OmniLight3D.omni_attenuation] or [member SpotLight3D.spot_attenuation].
const PARAM_ATTENUATION = 6;

#desc Constant for accessing [member SpotLight3D.spot_angle].
const PARAM_SPOT_ANGLE = 7;

#desc Constant for accessing [member SpotLight3D.spot_angle_attenuation].
const PARAM_SPOT_ATTENUATION = 8;

#desc Constant for accessing [member DirectionalLight3D.directional_shadow_max_distance].
const PARAM_SHADOW_MAX_DISTANCE = 9;

#desc Constant for accessing [member DirectionalLight3D.directional_shadow_split_1].
const PARAM_SHADOW_SPLIT_1_OFFSET = 10;

#desc Constant for accessing [member DirectionalLight3D.directional_shadow_split_2].
const PARAM_SHADOW_SPLIT_2_OFFSET = 11;

#desc Constant for accessing [member DirectionalLight3D.directional_shadow_split_3].
const PARAM_SHADOW_SPLIT_3_OFFSET = 12;

#desc Constant for accessing [member DirectionalLight3D.directional_shadow_fade_start].
const PARAM_SHADOW_FADE_START = 13;

#desc Constant for accessing [member shadow_normal_bias].
const PARAM_SHADOW_NORMAL_BIAS = 14;

#desc Constant for accessing [member shadow_bias].
const PARAM_SHADOW_BIAS = 15;

#desc Constant for accessing [member DirectionalLight3D.directional_shadow_pancake_size].
const PARAM_SHADOW_PANCAKE_SIZE = 16;

#desc Constant for accessing [member shadow_opacity].
const PARAM_SHADOW_OPACITY = 17;

#desc Constant for accessing [member shadow_blur].
const PARAM_SHADOW_BLUR = 18;

#desc Constant for accessing [member shadow_transmittance_bias].
const PARAM_TRANSMITTANCE_BIAS = 19;

#desc Constant for accessing [member light_intensity_lumens] and [member light_intensity_lux]. Only used when [member ProjectSettings.rendering/lights_and_shadows/use_physical_light_units] is [code]true[/code].
const PARAM_INTENSITY = 20;

#desc Represents the size of the [enum Param] enum.
const PARAM_MAX = 21;

#desc Light is ignored when baking. This is the fastest mode, but the light will be taken into account when baking global illumination. This mode should generally be used for dynamic lights that change quickly, as the effect of global illumination is less noticeable on those lights.
#desc [b]Note:[/b] Hiding a light does [i]not[/i] affect baking [LightmapGI]. Hiding a light will still affect baking [VoxelGI] and SDFGI (see [member Environment.sdfgi_enabled).
const BAKE_DISABLED = 0;

#desc Light is taken into account in static baking ([VoxelGI], [LightmapGI], SDFGI ([member Environment.sdfgi_enabled])). The light can be moved around or modified, but its global illumination will not update in real-time. This is suitable for subtle changes (such as flickering torches), but generally not large changes such as toggling a light on and off.
const BAKE_STATIC = 1;

#desc Light is taken into account in dynamic baking ([VoxelGI] and SDFGI ([member Environment.sdfgi_enabled]) only). The light can be moved around or modified with global illumination updating in real-time. The light's global illumination appearance will be slightly different compared to [constant BAKE_STATIC]. This has a greater performance cost compared to [constant BAKE_STATIC].
const BAKE_DYNAMIC = 2;


#desc The distance from the camera at which the light begins to fade away (in 3D units).
#desc [b]Note:[/b] Only effective for [OmniLight3D] and [SpotLight3D].
var distance_fade_begin: float;

#desc If [code]true[/code], the light will smoothly fade away when far from the active [Camera3D] starting at [member distance_fade_begin]. This acts as a form of level of detail (LOD). The light will fade out over [member distance_fade_begin] + [member distance_fade_length], after which it will be culled and not sent to the shader at all. Use this to reduce the number of active lights in a scene and thus improve performance.
#desc [b]Note:[/b] Only effective for [OmniLight3D] and [SpotLight3D].
var distance_fade_enabled: bool;

#desc Distance over which the light and its shadow fades. The light's energy and shadow's opacity is progressively reduced over this distance and is completely invisible at the end.
#desc [b]Note:[/b] Only effective for [OmniLight3D] and [SpotLight3D].
var distance_fade_length: float;

#desc The distance from the camera at which the light's shadow cuts off (in 3D units). Set this to a value lower than [member distance_fade_begin] + [member distance_fade_length] to further improve performance, as shadow rendering is often more expensive than light rendering itself.
#desc [b]Note:[/b] Only effective for [OmniLight3D] and [SpotLight3D], and only when [member shadow_enabled] is [code]true[/code].
var distance_fade_shadow: float;

#desc If [code]true[/code], the light only appears in the editor and will not be visible at runtime.
var editor_only: bool;

#desc The light's angular size in degrees. Increasing this will make shadows softer at greater distances. Only available for [DirectionalLight3D]s. For reference, the Sun from the Earth is approximately [code]0.5[/code].
var light_angular_distance: float;

#desc The light's bake mode. This will affect the global illumination techniques that have an effect on the light's rendering. See [enum BakeMode].
#desc [b]Note:[/b] Meshes' global illumination mode will also affect the global illumination rendering. See [member GeometryInstance3D.gi_mode].
var light_bake_mode: int;

#desc The light's color. An [i]overbright[/i] color can be used to achieve a result equivalent to increasing the light's [member light_energy].
var light_color: Color;

#desc The light will affect objects in the selected layers.
var light_cull_mask: int;

#desc The light's strength multiplier (this is not a physical unit). For [OmniLight3D] and [SpotLight3D], changing this value will only change the light color's intensity, not the light's radius.
var light_energy: float;

#desc Secondary multiplier used with indirect light (light bounces). Used with [VoxelGI] and SDFGI (see [member Environment.sdfgi_enabled]).
#desc [b]Note:[/b] This property is ignored if [member light_energy] is equal to [code]0.0[/code], as the light won't be present at all in the GI shader.
var light_indirect_energy: float;

#desc Used by positional lights ([OmniLight3D] and [SpotLight3D]) when [member ProjectSettings.rendering/lights_and_shadows/use_physical_light_units] is [code]true[/code]. Sets the intensity of the light source measured in Lumens. Lumens are a measure of luminous flux, which is the total amount of visible light emitted by a light source per unit of time.
#desc For [SpotLight3D]s, we assume that the area outside the visible cone is surrounded by a perfect light absorbing material. Accordingly, the apparent brightness of the cone area does not change as the cone increases and decreases in size.
#desc A typical household lightbulb can range from around 600 lumens to 1,200 lumens, a candle is about 13 lumens, while a streetlight can be approximately 60,000 lumens.
var light_intensity_lumens: float;

#desc Used by [DirectionalLight3D]s when [member ProjectSettings.rendering/lights_and_shadows/use_physical_light_units] is [code]true[/code]. Sets the intensity of the light source measured in Lux. Lux is a measure pf luminous flux per unit area, it is equal to one lumen per square metre. Lux is the measure of how much light hits a surface at a given time.
#desc On a clear sunny day a surface in direct sunlight may be approximately 100,000 lux, a typical room in a home may be approximately 50 lux, while the moonlit ground may be approximately 0.1 lux.
var light_intensity_lux: float;

#desc If [code]true[/code], the light's effect is reversed, darkening areas and casting bright shadows.
var light_negative: bool;

#desc [Texture2D] projected by light. [member shadow_enabled] must be on for the projector to work. Light projectors make the light appear as if it is shining through a colored but transparent object, almost like light shining through stained-glass.
#desc [b]Note:[/b] Unlike [BaseMaterial3D] whose filter mode can be adjusted on a per-material basis, the filter mode for light projector textures is set globally with [member ProjectSettings.rendering/textures/light_projectors/filter].
var light_projector: Texture2D;

#desc The size of the light in Godot units. Only available for [OmniLight3D]s and [SpotLight3D]s. Increasing this value will make the light fade out slower and shadows appear blurrier. This can be used to simulate area lights to an extent.
var light_size: float;

#desc The intensity of the specular blob in objects affected by the light. At [code]0[/code], the light becomes a pure diffuse light. When not baking emission, this can be used to avoid unrealistic reflections when placing lights above an emissive surface.
var light_specular: float;

#desc Sets the color temperature of the light source, measured in Kelvin. This is used to calculate a correlated color temperature which tints the [member light_color].
#desc The sun on a cloudy day is approximately 6500 Kelvin, on a clear day it is between 5500 to 6000 Kelvin, and on a clear day at sunrise or sunset it ranges to around 1850 Kelvin.
var light_temperature: float;

#desc Secondary multiplier multiplied with [member light_energy] then used with the [Environment]'s volumetric fog (if enabled). If set to [code]0.0[/code], computing volumetric fog will be skipped for this light, which can improve performance for large amounts of lights when volumetric fog is enabled.
#desc [b]Note:[/b] To prevent short-lived dynamic light effects from poorly interacting with volumetric fog, lights used in those effects should have [member light_volumetric_fog_energy] set to [code]0.0[/code] unless [member Environment.volumetric_fog_temporal_reprojection_enabled] is disabled (or unless the reprojection amount is significantly lowered).
var light_volumetric_fog_energy: float;

#desc Used to adjust shadow appearance. Too small a value results in self-shadowing ("shadow acne"), while too large a value causes shadows to separate from casters ("peter-panning"). Adjust as needed.
var shadow_bias: float;

#desc Blurs the edges of the shadow. Can be used to hide pixel artifacts in low-resolution shadow maps. A high value can impact performance, make shadows appear grainy and can cause other unwanted artifacts. Try to keep as near default as possible.
var shadow_blur: float;

#desc If [code]true[/code], the light will cast real-time shadows. This has a significant performance cost. Only enable shadow rendering when it makes a noticeable difference in the scene's appearance, and consider using [member distance_fade_enabled] to hide the light when far away from the [Camera3D].
var shadow_enabled: bool;

#desc Offsets the lookup into the shadow map by the object's normal. This can be used to reduce self-shadowing artifacts without using [member shadow_bias]. In practice, this value should be tweaked along with [member shadow_bias] to reduce artifacts as much as possible.
var shadow_normal_bias: float;

#desc The opacity to use when rendering the light's shadow map. Values lower than [code]1.0[/code] make the light appear through shadows. This can be used to fake global illumination at a low performance cost.
var shadow_opacity: float;

#desc If [code]true[/code], reverses the backface culling of the mesh. This can be useful when you have a flat mesh that has a light behind it. If you need to cast a shadow on both sides of the mesh, set the mesh to use double-sided shadows with [constant GeometryInstance3D.SHADOW_CASTING_SETTING_DOUBLE_SIDED].
var shadow_reverse_cull_face: bool;

var shadow_transmittance_bias: float;



#desc Returns the [Color] of an idealized blackbody at the given [member light_temperature]. This value is calculated internally based on the [member light_temperature]. This [Color] is multiplied by [member light_color] before being sent to the [RenderingServer].
func get_correlated_color() -> Color:
	pass;

#desc Returns the value of the specified [enum Light3D.Param] parameter.
func get_param(param: int) -> float:
	pass;

#desc Sets the value of the specified [enum Light3D.Param] parameter.
func set_param(param: int, value: float) -> void:
	pass;


