#brief [Material] used with a [FogVolume] to draw things with the volumetric fog effect.
#desc A [Material] resource that can be used by [FogVolume]s to draw volumetric effects.
#desc If you need more advanced effects, use a custom [url=$DOCS_URL/tutorials/shaders/shader_reference/fog_shader.html]fog shader[/url].
class_name FogMaterial


#desc The single-scattering [Color] of the [FogVolume]. Internally, [member albedo] is converted into single-scattering, which is additively blended with other [FogVolume]s and the [member Environment.volumetric_fog_albedo].
var albedo: Color;

#desc The density of the [FogVolume]. Denser objects are more opaque, but may suffer from under-sampling artifacts that look like stripes. Negative values can be used to subtract fog from other [FogVolume]s or global volumetric fog.
var density: float;

#desc The 3D texture that is used to scale the [member density] of the [FogVolume]. This can be used to vary fog density within the [FogVolume] with any kind of static pattern. For animated effects, consider using a custom [url=$DOCS_URL/tutorials/shaders/shader_reference/fog_shader.html]fog shader[/url].
var density_texture: Texture3D;

#desc The hardness of the edges of the [FogVolume]. A higher value will result in softer edges, while a lower value will result in harder edges.
var edge_fade: float;

#desc The [Color] of the light emitted by the [FogVolume]. Emitted light will not cast light or shadows on other objects, but can be useful for modulating the [Color] of the [FogVolume] independently from light sources.
var emission: Color;

#desc The rate by which the height-based fog decreases in density as height increases in world space. A high falloff will result in a sharp transition, while a low falloff will result in a smoother transition. A value of [code]0.0[/code] results in uniform-density fog. The height threshold is determined by the height of the associated [FogVolume].
var height_falloff: float;




