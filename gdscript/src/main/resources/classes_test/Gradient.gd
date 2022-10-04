#brief A color interpolator resource which can be used to generate colors between user-defined color points.
#desc Given a set of colors, this resource will interpolate them in order. This means that if you have color 1, color 2 and color 3, the gradient will interpolate from color 1 to color 2 and from color 2 to color 3. The gradient will initially have 2 colors (black and white), one (black) at gradient lower offset 0 and the other (white) at the gradient higher offset 1.
#desc See also [Curve] which supports more complex easing methods, but does not support colors.
class_name Gradient

#desc Linear interpolation.
const GRADIENT_INTERPOLATE_LINEAR = 0;

#desc Constant interpolation, color changes abruptly at each point and stays uniform between. This might cause visible aliasing when used for a gradient texture in some cases.
const GRADIENT_INTERPOLATE_CONSTANT = 1;

#desc Cubic interpolation.
const GRADIENT_INTERPOLATE_CUBIC = 2;


#desc Gradient's colors returned as a [PackedColorArray].
var colors: PackedColorArray;

#desc Defines how the colors between points of the gradient are interpolated. See [enum InterpolationMode] for available modes.
var interpolation_mode: int;

#desc Gradient's offsets returned as a [PackedFloat32Array].
var offsets: PackedFloat32Array;



#desc Adds the specified color to the end of the gradient, with the specified offset.
func add_point(offset: float, color: Color) -> void:
	pass;

#desc Returns the color of the gradient color at index [param point].
func get_color(point: int) -> Color:
	pass;

#desc Returns the offset of the gradient color at index [param point].
func get_offset(point: int) -> float:
	pass;

#desc Returns the number of colors in the gradient.
func get_point_count() -> int:
	pass;

#desc Removes the color at the index [param point].
func remove_point(point: int) -> void:
	pass;

#desc Reverses/mirrors the gradient.
func reverse() -> void:
	pass;

#desc Returns the interpolated color specified by [code]offset[/code].
func sample(offset: float) -> Color:
	pass;

#desc Sets the color of the gradient color at index [param point].
func set_color(point: int, color: Color) -> void:
	pass;

#desc Sets the offset for the gradient color at index [param point].
func set_offset(point: int, offset: float) -> void:
	pass;


