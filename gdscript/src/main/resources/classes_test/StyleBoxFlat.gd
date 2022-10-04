#brief Customizable [StyleBox] with a given set of parameters (no texture required).
#desc This [StyleBox] can be used to achieve all kinds of looks without the need of a texture. The following properties are customizable:
#desc - Color
#desc - Border width (individual width for each border)
#desc - Rounded corners (individual radius for each corner)
#desc - Shadow (with blur and offset)
#desc Setting corner radius to high values is allowed. As soon as corners overlap, the stylebox will switch to a relative system. Example:
#desc [codeblock]
#desc height = 30
#desc corner_radius_top_left = 50
#desc corner_radius_bottom_left = 100
#desc [/codeblock]
#desc The relative system now would take the 1:2 ratio of the two left corners to calculate the actual corner width. Both corners added will [b]never[/b] be more than the height. Result:
#desc [codeblock]
#desc corner_radius_top_left: 10
#desc corner_radius_bottom_left: 20
#desc [/codeblock]
class_name StyleBoxFlat


#desc Antialiasing draws a small ring around the edges, which fades to transparency. As a result, edges look much smoother. This is only noticeable when using rounded corners or [member skew].
#desc [b]Note:[/b] When using beveled corners with 45-degree angles ([member corner_detail] = 1), it is recommended to set [member anti_aliasing] to [code]false[/code] to ensure crisp visuals and avoid possible visual glitches.
var anti_aliasing: bool;

#desc This changes the size of the faded ring. Higher values can be used to achieve a "blurry" effect.
var anti_aliasing_size: float;

#desc The background color of the stylebox.
var bg_color: Color;

#desc If [code]true[/code], the border will fade into the background color.
var border_blend: bool;

#desc Sets the color of the border.
var border_color: Color;

#desc Border width for the bottom border.
var border_width_bottom: int;

#desc Border width for the left border.
var border_width_left: int;

#desc Border width for the right border.
var border_width_right: int;

#desc Border width for the top border.
var border_width_top: int;

#desc This sets the number of vertices used for each corner. Higher values result in rounder corners but take more processing power to compute. When choosing a value, you should take the corner radius ([method set_corner_radius_all]) into account.
#desc For corner radii less than 10, [code]4[/code] or [code]5[/code] should be enough. For corner radii less than 30, values between [code]8[/code] and [code]12[/code] should be enough.
#desc A corner detail of [code]1[/code] will result in chamfered corners instead of rounded corners, which is useful for some artistic effects.
var corner_detail: int;

#desc The bottom-left corner's radius. If [code]0[/code], the corner is not rounded.
var corner_radius_bottom_left: int;

#desc The bottom-right corner's radius. If [code]0[/code], the corner is not rounded.
var corner_radius_bottom_right: int;

#desc The top-left corner's radius. If [code]0[/code], the corner is not rounded.
var corner_radius_top_left: int;

#desc The top-right corner's radius. If [code]0[/code], the corner is not rounded.
var corner_radius_top_right: int;

#desc Toggles drawing of the inner part of the stylebox.
var draw_center: bool;

#desc Expands the stylebox outside of the control rect on the bottom edge. Useful in combination with [member border_width_bottom] to draw a border outside the control rect.
#desc [b]Note:[/b] Unlike [member StyleBox.content_margin_bottom], [member expand_margin_bottom] does [i]not[/i] affect the size of the clickable area for [Control]s. This can negatively impact usability if used wrong, as the user may try to click an area of the StyleBox that cannot actually receive clicks.
var expand_margin_bottom: float;

#desc Expands the stylebox outside of the control rect on the left edge. Useful in combination with [member border_width_left] to draw a border outside the control rect.
#desc [b]Note:[/b] Unlike [member StyleBox.content_margin_left], [member expand_margin_left] does [i]not[/i] affect the size of the clickable area for [Control]s. This can negatively impact usability if used wrong, as the user may try to click an area of the StyleBox that cannot actually receive clicks.
var expand_margin_left: float;

#desc Expands the stylebox outside of the control rect on the right edge. Useful in combination with [member border_width_right] to draw a border outside the control rect.
#desc [b]Note:[/b] Unlike [member StyleBox.content_margin_right], [member expand_margin_right] does [i]not[/i] affect the size of the clickable area for [Control]s. This can negatively impact usability if used wrong, as the user may try to click an area of the StyleBox that cannot actually receive clicks.
var expand_margin_right: float;

#desc Expands the stylebox outside of the control rect on the top edge. Useful in combination with [member border_width_top] to draw a border outside the control rect.
#desc [b]Note:[/b] Unlike [member StyleBox.content_margin_top], [member expand_margin_top] does [i]not[/i] affect the size of the clickable area for [Control]s. This can negatively impact usability if used wrong, as the user may try to click an area of the StyleBox that cannot actually receive clicks.
var expand_margin_top: float;

#desc The color of the shadow. This has no effect if [member shadow_size] is lower than 1.
var shadow_color: Color;

#desc The shadow offset in pixels. Adjusts the position of the shadow relatively to the stylebox.
var shadow_offset: Vector2;

#desc The shadow size in pixels.
var shadow_size: int;

#desc If set to a non-zero value on either axis, [member skew] distorts the StyleBox horizontally and/or vertically. This can be used for "futuristic"-style UIs. Positive values skew the StyleBox towards the right (X axis) and upwards (Y axis), while negative values skew the StyleBox towards the left (X axis) and downwards (Y axis).
#desc [b]Note:[/b] To ensure text does not touch the StyleBox's edges, consider increasing the [StyleBox]'s content margin (see [member StyleBox.content_margin_bottom]). It is preferable to increase the content margin instead of the expand margin (see [member expand_margin_bottom]), as increasing the expand margin does not increase the size of the clickable area for [Control]s.
var skew: Vector2;



#desc Returns the specified [enum Side]'s border width.
func get_border_width(margin: int) -> int:
	pass;

#desc Returns the smallest border width out of all four borders.
func get_border_width_min() -> int:
	pass;

#desc Returns the given [param corner]'s radius. See [enum Corner] for possible values.
func get_corner_radius(corner: int) -> int:
	pass;

#desc Returns the size of the specified [enum Side]'s expand margin.
func get_expand_margin(margin: int) -> float:
	pass;

#desc Sets the specified [enum Side]'s border width to [param width] pixels.
func set_border_width(margin: int, width: int) -> void:
	pass;

#desc Sets the border width to [param width] pixels for all sides.
func set_border_width_all(width: int) -> void:
	pass;

#desc Sets the corner radius to [param radius] pixels for the given [param corner]. See [enum Corner] for possible values.
func set_corner_radius(corner: int, radius: int) -> void:
	pass;

#desc Sets the corner radius to [param radius] pixels for all corners.
func set_corner_radius_all(radius: int) -> void:
	pass;

#desc Sets the expand margin to [param size] pixels for the specified [enum Side].
func set_expand_margin(margin: int, size: float) -> void:
	pass;

#desc Sets the expand margin to [param size] pixels for all margins.
func set_expand_margin_all(size: float) -> void:
	pass;


