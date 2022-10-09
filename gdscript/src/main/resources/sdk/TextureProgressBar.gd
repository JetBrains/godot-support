extends Range
#brief Texture-based progress bar. Useful for loading screens and life or stamina bars.
#desc TextureProgressBar works like [ProgressBar], but uses up to 3 textures instead of Godot's [Theme] resource. It can be used to create horizontal, vertical and radial progress bars.
class_name TextureProgressBar

#desc The [member texture_progress] fills from left to right.
const FILL_LEFT_TO_RIGHT = 0;

#desc The [member texture_progress] fills from right to left.
const FILL_RIGHT_TO_LEFT = 1;

#desc The [member texture_progress] fills from top to bottom.
const FILL_TOP_TO_BOTTOM = 2;

#desc The [member texture_progress] fills from bottom to top.
const FILL_BOTTOM_TO_TOP = 3;

#desc Turns the node into a radial bar. The [member texture_progress] fills clockwise. See [member radial_center_offset], [member radial_initial_angle] and [member radial_fill_degrees] to control the way the bar fills up.
const FILL_CLOCKWISE = 4;

#desc Turns the node into a radial bar. The [member texture_progress] fills counterclockwise. See [member radial_center_offset], [member radial_initial_angle] and [member radial_fill_degrees] to control the way the bar fills up.
const FILL_COUNTER_CLOCKWISE = 5;

#desc The [member texture_progress] fills from the center, expanding both towards the left and the right.
const FILL_BILINEAR_LEFT_AND_RIGHT = 6;

#desc The [member texture_progress] fills from the center, expanding both towards the top and the bottom.
const FILL_BILINEAR_TOP_AND_BOTTOM = 7;

#desc Turns the node into a radial bar. The [member texture_progress] fills radially from the center, expanding both clockwise and counterclockwise. See [member radial_center_offset], [member radial_initial_angle] and [member radial_fill_degrees] to control the way the bar fills up.
const FILL_CLOCKWISE_AND_COUNTER_CLOCKWISE = 8;


#desc The fill direction. See [enum FillMode] for possible values.
var fill_mode: int;

#desc If [code]true[/code], Godot treats the bar's textures like in [NinePatchRect]. Use the [code]stretch_margin_*[/code] properties like [member stretch_margin_bottom] to set up the nine patch's 3×3 grid. When using a radial [member fill_mode], this setting will enable stretching.
var nine_patch_stretch: bool;

#desc Offsets [member texture_progress] if [member fill_mode] is [constant FILL_CLOCKWISE] or [constant FILL_COUNTER_CLOCKWISE].
var radial_center_offset: Vector2;

#desc Upper limit for the fill of [member texture_progress] if [member fill_mode] is [constant FILL_CLOCKWISE] or [constant FILL_COUNTER_CLOCKWISE]. When the node's [code]value[/code] is equal to its [code]max_value[/code], the texture fills up to this angle.
#desc See [member Range.value], [member Range.max_value].
var radial_fill_degrees: float;

#desc Starting angle for the fill of [member texture_progress] if [member fill_mode] is [constant FILL_CLOCKWISE] or [constant FILL_COUNTER_CLOCKWISE]. When the node's [code]value[/code] is equal to its [code]min_value[/code], the texture doesn't show up at all. When the [code]value[/code] increases, the texture fills and tends towards [member radial_fill_degrees].
var radial_initial_angle: float;

#desc The height of the 9-patch's bottom row. A margin of 16 means the 9-slice's bottom corners and side will have a height of 16 pixels. You can set all 4 margin values individually to create panels with non-uniform borders.
var stretch_margin_bottom: int;

#desc The width of the 9-patch's left column.
var stretch_margin_left: int;

#desc The width of the 9-patch's right column.
var stretch_margin_right: int;

#desc The height of the 9-patch's top row.
var stretch_margin_top: int;

#desc [Texture2D] that draws over the progress bar. Use it to add highlights or an upper-frame that hides part of [member texture_progress].
var texture_over: Texture2D;

#desc [Texture2D] that clips based on the node's [code]value[/code] and [member fill_mode]. As [code]value[/code] increased, the texture fills up. It shows entirely when [code]value[/code] reaches [code]max_value[/code]. It doesn't show at all if [code]value[/code] is equal to [code]min_value[/code].
#desc The [code]value[/code] property comes from [Range]. See [member Range.value], [member Range.min_value], [member Range.max_value].
var texture_progress: Texture2D;

#desc The offset of [member texture_progress]. Useful for [member texture_over] and [member texture_under] with fancy borders, to avoid transparent margins in your progress texture.
var texture_progress_offset: Vector2;

#desc [Texture2D] that draws under the progress bar. The bar's background.
var texture_under: Texture2D;

#desc Multiplies the color of the bar's [code]texture_over[/code] texture. The effect is similar to [member CanvasItem.modulate], except it only affects this specific texture instead of the entire node.
var tint_over: Color;

#desc Multiplies the color of the bar's [code]texture_progress[/code] texture.
var tint_progress: Color;

#desc Multiplies the color of the bar's [code]texture_under[/code] texture.
var tint_under: Color;



func get_stretch_margin(margin: int) -> int:
	pass;

func set_stretch_margin(margin: int, value: int) -> void:
	pass;


