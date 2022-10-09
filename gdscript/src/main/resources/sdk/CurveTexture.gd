extends Texture2D
#brief A texture that shows a curve.
#desc Renders a given [Curve] provided to it. Simplifies the task of drawing curves and/or saving them as image files.
#desc If you need to store up to 3 curves within a single texture, use [CurveXYZTexture] instead. See also [GradientTexture1D] and [GradientTexture2D].
class_name CurveTexture

#desc Store the curve equally across the red, green and blue channels. This uses more video memory, but is more compatible with shaders that only read the green and blue values.
const TEXTURE_MODE_RGB = 0;

#desc Store the curve only in the red channel. This saves video memory, but some custom shaders may not be able to work with this.
const TEXTURE_MODE_RED = 1;


#desc The [Curve] that is rendered onto the texture.
var curve: Curve;

#desc The format the texture should be generated with. When passing a CurveTexture as a input to a [Shader], this may need to be adjusted.
var texture_mode: int;

#desc The width of the texture (in pixels). Higher values make it possible to represent high-frequency data better (such as sudden direction changes), at the cost of increased generation time and memory usage.
var width: int;




