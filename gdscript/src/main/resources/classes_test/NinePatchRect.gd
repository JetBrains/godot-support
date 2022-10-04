#brief Scalable texture-based frame that tiles the texture's centers and sides, but keeps the corners' original size. Perfect for panels and dialog boxes.
#desc Also known as 9-slice panels, NinePatchRect produces clean panels of any size, based on a small texture. To do so, it splits the texture in a 3×3 grid. When you scale the node, it tiles the texture's sides horizontally or vertically, the center on both axes but it doesn't scale or tile the corners.
class_name NinePatchRect

#desc Stretches the center texture across the NinePatchRect. This may cause the texture to be distorted.
const AXIS_STRETCH_MODE_STRETCH = 0;

#desc Repeats the center texture across the NinePatchRect. This won't cause any visible distortion. The texture must be seamless for this to work without displaying artifacts between edges.
#desc [b]Note:[/b] Only supported when using the Vulkan renderer. When using the OpenGL renderer, this will behave like [constant AXIS_STRETCH_MODE_STRETCH].
const AXIS_STRETCH_MODE_TILE = 1;

#desc Repeats the center texture across the NinePatchRect, but will also stretch the texture to make sure each tile is visible in full. This may cause the texture to be distorted, but less than [constant AXIS_STRETCH_MODE_STRETCH]. The texture must be seamless for this to work without displaying artifacts between edges.
#desc [b]Note:[/b] Only supported when using the Vulkan renderer. When using the OpenGL renderer, this will behave like [constant AXIS_STRETCH_MODE_STRETCH].
const AXIS_STRETCH_MODE_TILE_FIT = 2;


#desc The stretch mode to use for horizontal stretching/tiling. See [enum NinePatchRect.AxisStretchMode] for possible values.
var axis_stretch_horizontal: int;

#desc The stretch mode to use for vertical stretching/tiling. See [enum NinePatchRect.AxisStretchMode] for possible values.
var axis_stretch_vertical: int;

#desc If [code]true[/code], draw the panel's center. Else, only draw the 9-slice's borders.
var draw_center: bool;

var mouse_filter: int;

#desc The height of the 9-slice's bottom row. A margin of 16 means the 9-slice's bottom corners and side will have a height of 16 pixels. You can set all 4 margin values individually to create panels with non-uniform borders.
var patch_margin_bottom: int;

#desc The width of the 9-slice's left column. A margin of 16 means the 9-slice's left corners and side will have a width of 16 pixels. You can set all 4 margin values individually to create panels with non-uniform borders.
var patch_margin_left: int;

#desc The width of the 9-slice's right column. A margin of 16 means the 9-slice's right corners and side will have a width of 16 pixels. You can set all 4 margin values individually to create panels with non-uniform borders.
var patch_margin_right: int;

#desc The height of the 9-slice's top row. A margin of 16 means the 9-slice's top corners and side will have a height of 16 pixels. You can set all 4 margin values individually to create panels with non-uniform borders.
var patch_margin_top: int;

#desc Rectangular region of the texture to sample from. If you're working with an atlas, use this property to define the area the 9-slice should use. All other properties are relative to this one. If the rect is empty, NinePatchRect will use the whole texture.
var region_rect: Rect2;

#desc The node's texture resource.
var texture: Texture2D;



#desc Returns the size of the margin on the specified [enum Side].
func get_patch_margin(margin: int) -> int:
	pass;

#desc Sets the size of the margin on the specified [enum Side] to [param value] pixels.
func set_patch_margin(margin: int, value: int) -> void:
	pass;


