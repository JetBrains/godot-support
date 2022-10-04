extends Light3D
#brief Omnidirectional light, such as a light bulb or a candle.
#desc An Omnidirectional light is a type of [Light3D] that emits light in all directions. The light is attenuated by distance and this attenuation can be configured by changing its energy, radius, and attenuation parameters.
class_name OmniLight3D

#desc Shadows are rendered to a dual-paraboloid texture. Faster than [constant SHADOW_CUBE], but lower-quality.
const SHADOW_DUAL_PARABOLOID = 0;

#desc Shadows are rendered to a cubemap. Slower than [constant SHADOW_DUAL_PARABOLOID], but higher-quality.
const SHADOW_CUBE = 1;


#desc The light's attenuation (drop-off) curve. A number of presets are available in the [b]Inspector[/b] by right-clicking the curve.
var omni_attenuation: float;

#desc The light's radius. Note that the effectively lit area may appear to be smaller depending on the [member omni_attenuation] in use. No matter the [member omni_attenuation] in use, the light will never reach anything outside this radius.
var omni_range: float;

#desc See [enum ShadowMode].
var omni_shadow_mode: int;

var shadow_bias: float;




