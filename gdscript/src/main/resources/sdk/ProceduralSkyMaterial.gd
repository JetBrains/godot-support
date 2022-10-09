extends Material
#brief A [Material] used with [Sky] to generate a background based on user input parameters.
#desc ProceduralSkyMaterial provides a way to create an effective background quickly by defining procedural parameters for the sun, the sky and the ground. The sky and ground are very similar, they are defined by a color at the horizon, another color, and finally an easing curve to interpolate between these two colors. Similarly, the sun is described by a position in the sky, a color, and an easing curve. However, the sun also defines a minimum and maximum angle, these two values define at what distance the easing curve begins and ends from the sun, and thus end up defining the size of the sun in the sky.
#desc The [ProceduralSkyMaterial] uses a lightweight shader to draw the sky and is thus suited for real time updates. When you do not need a quick sky that is not realistic, this is a good option. If you need a more realistic option, try using [PhysicalSkyMaterial] instead.
#desc The [ProceduralSkyMaterial] supports up to 4 suns. Each sun takes its color, energy, and direction from the corresponding [DirectionalLight3D] in the scene.
class_name ProceduralSkyMaterial


#desc Color of the ground at the bottom. Blends with [member ground_horizon_color].
var ground_bottom_color: Color;

#desc How quickly the [member ground_horizon_color] fades into the [member ground_bottom_color].
var ground_curve: float;

#desc Multiplier for ground color. A higher value will make the ground brighter.
var ground_energy_multiplier: float;

#desc Color of the ground at the horizon. Blends with [member ground_bottom_color].
var ground_horizon_color: Color;

#desc The sky cover texture to use. This texture must use an equirectangular projection (similar to [PanoramaSkyMaterial]). The texture's colors will be [i]added[/i] to the existing sky color, and will be multiplied by [member sky_energy_multiplier] and [member sky_cover_modulate]. This is mainly suited to displaying stars at night, but it can also be used to display clouds at day or night (with a non-physically-accurate look).
var sky_cover: Texture2D;

#desc The tint to apply to the [member sky_cover] texture. This can be used to change the sky cover's colors or opacity independently of the sky energy, which is useful for day/night or weather transitions. Only effective if a texture is defined in [member sky_cover].
var sky_cover_modulate: Color;

#desc How quickly the [member sky_horizon_color] fades into the [member sky_top_color].
var sky_curve: float;

#desc Multiplier for sky color. A higher value will make the sky brighter.
var sky_energy_multiplier: float;

#desc Color of the sky at the horizon. Blends with [member sky_top_color].
var sky_horizon_color: Color;

#desc Color of the sky at the top. Blends with [member sky_horizon_color].
var sky_top_color: Color;

#desc Distance from center of sun where it fades out completely.
var sun_angle_max: float;

#desc How quickly the sun fades away between the edge of the sun disk and [member sun_angle_max].
var sun_curve: float;

#desc If [code]true[/code], enables debanding. Debanding adds a small amount of noise which helps reduce banding that appears from the smooth changes in color in the sky.
var use_debanding: bool;




