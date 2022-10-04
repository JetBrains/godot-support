#brief Gradient-filled texture.
#desc GradientTexture1D uses a [Gradient] to fill the texture data. The gradient will be filled from left to right using colors obtained from the gradient. This means the texture does not necessarily represent an exact copy of the gradient, but instead an interpolation of samples obtained from the gradient at fixed steps (see [member width]). See also [GradientTexture2D], [CurveTexture] and [CurveXYZTexture].
class_name GradientTexture1D


#desc The [Gradient] that will be used to fill the texture.
var gradient: Gradient;

#desc If [code]true[/code], the generated texture will support high dynamic range ([constant Image.FORMAT_RGBAF] format). This allows for glow effects to work if [member Environment.glow_enabled] is [code]true[/code]. If [code]false[/code], the generated texture will use low dynamic range; overbright colors will be clamped ([constant Image.FORMAT_RGBA8] format).
var use_hdr: bool;

#desc The number of color samples that will be obtained from the [Gradient].
var width: int;




