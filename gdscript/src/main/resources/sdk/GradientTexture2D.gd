extends Texture2D
#brief Gradient-filled 2D texture.
#desc The texture uses a [Gradient] to fill the texture data in 2D space. The gradient is filled according to the specified [member fill] and [member repeat] types using colors obtained from the gradient. The texture does not necessarily represent an exact copy of the gradient, but instead an interpolation of samples obtained from the gradient at fixed steps (see [member width] and [member height]). See also [GradientTexture1D], [CurveTexture] and [CurveXYZTexture].
class_name GradientTexture2D

#desc The colors are linearly interpolated in a straight line.
const FILL_LINEAR = 0;

#desc The colors are linearly interpolated in a circular pattern.
const FILL_RADIAL = 1;

#desc The gradient fill is restricted to the range defined by [member fill_from] to [member fill_to] offsets.
const REPEAT_NONE = 0;

#desc The texture is filled starting from [member fill_from] to [member fill_to] offsets, repeating the same pattern in both directions.
const REPEAT = 1;

#desc The texture is filled starting from [member fill_from] to [member fill_to] offsets, mirroring the pattern in both directions.
const REPEAT_MIRROR = 2;


#desc The gradient fill type, one of the [enum Fill] values. The texture is filled by interpolating colors starting from [member fill_from] to [member fill_to] offsets.
var fill: int;

#desc The initial offset used to fill the texture specified in UV coordinates.
var fill_from: Vector2;

#desc The final offset used to fill the texture specified in UV coordinates.
var fill_to: Vector2;

#desc The [Gradient] used to fill the texture.
var gradient: Gradient;

#desc The number of vertical color samples that will be obtained from the [Gradient], which also represents the texture's height.
var height: int;

#desc The gradient repeat type, one of the [enum Repeat] values. The texture is filled starting from [member fill_from] to [member fill_to] offsets by default, but the gradient fill can be repeated to cover the entire texture.
var repeat: int;

#desc If [code]true[/code], the generated texture will support high dynamic range ([constant Image.FORMAT_RGBAF] format). This allows for glow effects to work if [member Environment.glow_enabled] is [code]true[/code]. If [code]false[/code], the generated texture will use low dynamic range; overbright colors will be clamped ([constant Image.FORMAT_RGBA8] format).
var use_hdr: bool;

#desc The number of horizontal color samples that will be obtained from the [Gradient], which also represents the texture's width.
var width: int;




