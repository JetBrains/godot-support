#brief Casts light in a 2D environment.
#desc Casts light in a 2D environment. Light is defined by a (usually grayscale) texture, a color, an energy value, a mode (see constants), and various other parameters (range and shadows-related).
#desc [b]Note:[/b] Light2D can also be used as a mask.
class_name Light2D

#desc No filter applies to the shadow map. See [member shadow_filter].
const SHADOW_FILTER_NONE = 0;

#desc Percentage closer filtering (5 samples) applies to the shadow map. See [member shadow_filter].
const SHADOW_FILTER_PCF5 = 1;

#desc Percentage closer filtering (13 samples) applies to the shadow map. See [member shadow_filter].
const SHADOW_FILTER_PCF13 = 2;

#desc Adds the value of pixels corresponding to the Light2D to the values of pixels under it. This is the common behavior of a light.
const BLEND_MODE_ADD = 0;

#desc Subtracts the value of pixels corresponding to the Light2D to the values of pixels under it, resulting in inversed light effect.
const BLEND_MODE_SUB = 1;

#desc Mix the value of pixels corresponding to the Light2D to the values of pixels under it by linear interpolation.
const BLEND_MODE_MIX = 2;


#desc The Light2D's blend mode. See [enum BlendMode] constants for values.
var blend_mode: int;

#desc The Light2D's [Color].
var color: Color;

#desc If [code]true[/code], Light2D will only appear when editing the scene.
var editor_only: bool;

#desc If [code]true[/code], Light2D will emit light.
var enabled: bool;

#desc The Light2D's energy value. The larger the value, the stronger the light.
var energy: float;

#desc The layer mask. Only objects with a matching mask will be affected by the Light2D.
var range_item_cull_mask: int;

#desc Maximum layer value of objects that are affected by the Light2D.
var range_layer_max: int;

#desc Minimum layer value of objects that are affected by the Light2D.
var range_layer_min: int;

#desc Maximum [code]z[/code] value of objects that are affected by the Light2D.
var range_z_max: int;

#desc Minimum [code]z[/code] value of objects that are affected by the Light2D.
var range_z_min: int;

#desc [Color] of shadows cast by the Light2D.
var shadow_color: Color;

#desc If [code]true[/code], the Light2D will cast shadows.
var shadow_enabled: bool;

#desc Shadow filter type. See [enum ShadowFilter] for possible values.
var shadow_filter: int;

#desc Smoothing value for shadows.
var shadow_filter_smooth: float;

#desc The shadow mask. Used with [LightOccluder2D] to cast shadows. Only occluders with a matching light mask will cast shadows.
var shadow_item_cull_mask: int;



func get_height() -> float:
	pass;

func set_height(height: float) -> void:
	pass;


