#brief Texture-based nine-patch [StyleBox].
#desc Texture-based nine-patch [StyleBox], in a way similar to [NinePatchRect]. This stylebox performs a 3×3 scaling of a texture, where only the center cell is fully stretched. This makes it possible to design bordered styles regardless of the stylebox's size.
class_name StyleBoxTexture

#desc Stretch the stylebox's texture. This results in visible distortion unless the texture size matches the stylebox's size perfectly.
const AXIS_STRETCH_MODE_STRETCH = 0;

#desc Repeats the stylebox's texture to match the stylebox's size according to the nine-patch system.
const AXIS_STRETCH_MODE_TILE = 1;

#desc Repeats the stylebox's texture to match the stylebox's size according to the nine-patch system. Unlike [constant AXIS_STRETCH_MODE_TILE], the texture may be slightly stretched to make the nine-patch texture tile seamlessly.
const AXIS_STRETCH_MODE_TILE_FIT = 2;


#desc Controls how the stylebox's texture will be stretched or tiled horizontally. See [enum AxisStretchMode] for possible values.
var axis_stretch_horizontal: int;

#desc Controls how the stylebox's texture will be stretched or tiled vertically. See [enum AxisStretchMode] for possible values.
var axis_stretch_vertical: int;

#desc If [code]true[/code], the nine-patch texture's center tile will be drawn.
var draw_center: bool;

#desc Expands the bottom margin of this style box when drawing, causing it to be drawn larger than requested.
var expand_margin_bottom: float;

#desc Expands the left margin of this style box when drawing, causing it to be drawn larger than requested.
var expand_margin_left: float;

#desc Expands the right margin of this style box when drawing, causing it to be drawn larger than requested.
var expand_margin_right: float;

#desc Expands the top margin of this style box when drawing, causing it to be drawn larger than requested.
var expand_margin_top: float;

#desc Increases the bottom margin of the 3×3 texture box.
#desc A higher value means more of the source texture is considered to be part of the bottom border of the 3×3 box.
#desc This is also the value used as fallback for [member StyleBox.content_margin_bottom] if it is negative.
var margin_bottom: float;

#desc Increases the left margin of the 3×3 texture box.
#desc A higher value means more of the source texture is considered to be part of the left border of the 3×3 box.
#desc This is also the value used as fallback for [member StyleBox.content_margin_left] if it is negative.
var margin_left: float;

#desc Increases the right margin of the 3×3 texture box.
#desc A higher value means more of the source texture is considered to be part of the right border of the 3×3 box.
#desc This is also the value used as fallback for [member StyleBox.content_margin_right] if it is negative.
var margin_right: float;

#desc Increases the top margin of the 3×3 texture box.
#desc A higher value means more of the source texture is considered to be part of the top border of the 3×3 box.
#desc This is also the value used as fallback for [member StyleBox.content_margin_top] if it is negative.
var margin_top: float;

#desc Modulates the color of the texture when this style box is drawn.
var modulate_color: Color;

#desc Species a sub-region of the texture to use.
#desc This is equivalent to first wrapping the texture in an [AtlasTexture] with the same region.
var region_rect: Rect2;

#desc The texture to use when drawing this style box.
var texture: Texture2D;



#desc Returns the expand margin size of the specified [enum Side].
func get_expand_margin_size() -> float:
	pass;

#desc Returns the margin size of the specified [enum Side].
func get_margin_size() -> float:
	pass;

#desc Sets the expand margin to [param size] pixels for all margins.
func set_expand_margin_all() -> void:
	pass;

#desc Sets the expand margin to [param size] pixels for the specified [enum Side].
func set_expand_margin_size(margin: int, size: float) -> void:
	pass;

#desc Sets the margin to [param size] pixels for the specified [enum Side].
func set_margin_size(margin: int, size: float) -> void:
	pass;

#desc Sets the margin to [param size] pixels for all sides.
func set_margin_size_all() -> void:
	pass;


