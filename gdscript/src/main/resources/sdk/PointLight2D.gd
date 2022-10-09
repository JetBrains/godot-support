extends Light2D
class_name PointLight2D


#desc The height of the light. Used with 2D normal mapping. The units are in pixels, e.g. if the height is 100, then it will illuminate an object 100 pixels away at a 45° angle to the plane.
var height: float;

#desc The offset of the light's [member texture].
var offset: Vector2;

#desc [Texture2D] used for the light's appearance.
var texture: Texture2D;

#desc The [member texture]'s scale factor.
var texture_scale: float;




