#brief [Sky] [Material] used for a physically based sky.
#desc The [PhysicalSkyMaterial] uses the Preetham analytic daylight model to draw a sky based on physical properties. This results in a substantially more realistic sky than the [ProceduralSkyMaterial], but it is slightly slower and less flexible.
#desc The [PhysicalSkyMaterial] only supports one sun. The color, energy, and direction of the sun are taken from the first [DirectionalLight3D] in the scene tree.
#desc As it is based on a daylight model, the sky fades to black as the sunset ends. If you want a full day/night cycle, you will have to add a night sky by converting this to a [ShaderMaterial] and adding a night sky directly into the resulting shader.
class_name PhysicalSkyMaterial


var energy_multiplier: float;

#desc Modulates the [Color] on the bottom half of the sky to represent the ground.
var ground_color: Color;

#desc Controls the strength of mie scattering for the sky. Mie scattering results from light colliding with larger particles (like water). On earth, mie scattering results in a whitish color around the sun and horizon.
var mie_coefficient: float;

#desc Controls the [Color] of the mie scattering effect. While not physically accurate, this allows for the creation of alien-looking planets.
var mie_color: Color;

#desc Controls the direction of the mie scattering. A value of [code]1[/code] means that when light hits a particle it's passing through straight forward. A value of [code]-1[/code] means that all light is scatter backwards.
var mie_eccentricity: float;

#desc [Texture2D] for the night sky. This is added to the sky, so if it is bright enough, it may be visible during the day.
var night_sky: Texture2D;

#desc Controls the strength of the Rayleigh scattering. Rayleigh scattering results from light colliding with small particles. It is responsible for the blue color of the sky.
var rayleigh_coefficient: float;

#desc Controls the [Color] of the Rayleigh scattering. While not physically accurate, this allows for the creation of alien-looking planets. For example, setting this to a red [Color] results in a Mars-looking atmosphere with a corresponding blue sunset.
var rayleigh_color: Color;

#desc Sets the size of the sun disk. Default value is based on Sol's perceived size from Earth.
var sun_disk_scale: float;

#desc Sets the thickness of the atmosphere. High turbidity creates a foggy-looking atmosphere, while a low turbidity results in a clearer atmosphere.
var turbidity: float;

#desc If [code]true[/code], enables debanding. Debanding adds a small amount of noise which helps reduce banding that appears from the smooth changes in color in the sky.
var use_debanding: bool;




