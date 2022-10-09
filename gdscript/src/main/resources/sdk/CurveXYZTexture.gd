extends Texture2D
#brief A texture that shows 3 different curves (stored on the red, green and blue color channels).
#desc Renders 3 given [Curve]s provided to it, on the red, green and blue channels respectively. Compared to using separate [CurveTexture]s, this further simplifies the task of drawing curves and/or saving them as image files.
#desc If you only need to store one curve within a single texture, use [CurveTexture] instead. See also [GradientTexture1D] and [GradientTexture2D].
class_name CurveXYZTexture


#desc The [Curve] that is rendered onto the texture's red channel.
var curve_x: Curve;

#desc The [Curve] that is rendered onto the texture's green channel.
var curve_y: Curve;

#desc The [Curve] that is rendered onto the texture's blue channel.
var curve_z: Curve;

#desc The width of the texture (in pixels). Higher values make it possible to represent high-frequency data better (such as sudden direction changes), at the cost of increased generation time and memory usage.
var width: int;




