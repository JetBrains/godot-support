extends GeometryInstance3D
#brief Displays plain text in a 3D world.
#desc Label3D displays plain text in a 3D world. It gives you control over the horizontal and vertical alignment.
class_name Label3D

#desc If set, lights in the environment affect the label.
const FLAG_SHADED = 0;

#desc If set, text can be seen from the back as well. If not, the text is invisible when looking at it from behind.
const FLAG_DOUBLE_SIDED = 1;

#desc Disables the depth test, so this object is drawn on top of all others. However, objects drawn after it in the draw order may cover it.
const FLAG_DISABLE_DEPTH_TEST = 2;

#desc Label is scaled by depth so that it always appears the same size on screen.
const FLAG_FIXED_SIZE = 3;

#desc Represents the size of the [enum DrawFlags] enum.
const FLAG_MAX = 4;

#desc This mode performs standard alpha blending. It can display translucent areas, but transparency sorting issues may be visible when multiple transparent materials are overlapping. [member GeometryInstance3D.cast_shadow] has no effect when this transparency mode is used; the [Label3D] will never cast shadows.
const ALPHA_CUT_DISABLED = 0;

#desc This mode only allows fully transparent or fully opaque pixels. Harsh edges will be visible unless some form of screen-space antialiasing is enabled (see [member ProjectSettings.rendering/anti_aliasing/quality/screen_space_aa]). This mode is also known as [i]alpha testing[/i] or [i]1-bit transparency[/i].
#desc [b]Note:[/b] This mode might have issues with anti-aliased fonts and outlines, try adjusting [member alpha_scissor_threshold] or using MSDF font.
#desc [b]Note:[/b] When using text with overlapping glyphs (e.g., cursive scripts), this mode might have transparency sorting issues between the main text and the outline.
const ALPHA_CUT_DISCARD = 1;

#desc This mode draws fully opaque pixels in the depth prepass. This is slower than [constant ALPHA_CUT_DISABLED] or [constant ALPHA_CUT_DISCARD], but it allows displaying translucent areas and smooth edges while using proper sorting.
#desc [b]Note:[/b] When using text with overlapping glyphs (e.g., cursive scripts), this mode might have transparency sorting issues between the main text and the outline.
const ALPHA_CUT_OPAQUE_PREPASS = 2;


#desc The alpha cutting mode to use for the sprite. See [enum AlphaCutMode] for possible values.
var alpha_cut: int;

#desc Threshold at which the alpha scissor will discard values.
var alpha_scissor_threshold: float;

#desc If set to something other than [constant TextServer.AUTOWRAP_OFF], the text gets wrapped inside the node's bounding rectangle. If you resize the node, it will change its height automatically to show all the text. To see how each mode behaves, see [enum TextServer.AutowrapMode].
var autowrap_mode: int;

#desc The billboard mode to use for the label. See [enum BaseMaterial3D.BillboardMode] for possible values.
var billboard: int;

#desc If [code]true[/code], text can be seen from the back as well, if [code]false[/code], it is invisible when looking at it from behind.
var double_sided: bool;

#desc If [code]true[/code], the label is rendered at the same size regardless of distance.
var fixed_size: bool;

#desc Font configuration used to display text.
var font: Font;

#desc Font size of the [Label3D]'s text. To make the font look more detailed when up close, increase [member font_size] while decreasing [member pixel_size] at the same time.
#desc Higher font sizes require more time to render new characters, which can cause stuttering during gameplay.
var font_size: int;

#desc Controls the text's horizontal alignment. Supports left, center, right, and fill, or justify. Set it to one of the [enum HorizontalAlignment] constants.
var horizontal_alignment: int;

#desc Language code used for line-breaking and text shaping algorithms, if left empty current locale is used instead.
var language: String;

#desc Vertical space between lines in multiline [Label3D].
var line_spacing: float;

#desc Text [Color] of the [Label3D].
var modulate: Color;

#desc If [code]true[/code], depth testing is disabled and the object will be drawn in render order.
var no_depth_test: bool;

#desc The text drawing offset (in pixels).
var offset: Vector2;

#desc The tint of text outline.
var outline_modulate: Color;

#desc Sets the render priority for the text outline. Higher priority objects will be sorted in front of lower priority objects.
#desc [b]Note:[/b] This only applies if [member alpha_cut] is set to [constant ALPHA_CUT_DISABLED] (default value).
#desc [b]Note:[/b] This only applies to sorting of transparent objects. This will not impact how transparent objects are sorted relative to opaque objects. This is because opaque objects are not sorted, while transparent objects are sorted from back to front (subject to priority).
var outline_render_priority: int;

#desc Text outline size.
var outline_size: int;

#desc The size of one pixel's width on the label to scale it in 3D. To make the font look more detailed when up close, increase [member font_size] while decreasing [member pixel_size] at the same time.
var pixel_size: float;

#desc Sets the render priority for the text. Higher priority objects will be sorted in front of lower priority objects.
#desc [b]Note:[/b] This only applies if [member alpha_cut] is set to [constant ALPHA_CUT_DISABLED] (default value).
#desc [b]Note:[/b] This only applies to sorting of transparent objects. This will not impact how transparent objects are sorted relative to opaque objects. This is because opaque objects are not sorted, while transparent objects are sorted from back to front (subject to priority).
var render_priority: int;

#desc If [code]true[/code], the [Light3D] in the [Environment] has effects on the label.
var shaded: bool;

#desc Set BiDi algorithm override for the structured text.
var structured_text_bidi_override: int;

#desc Set additional options for BiDi override.
var structured_text_bidi_override_options: Array;

#desc The text to display on screen.
var text: String;

#desc Base text writing direction.
var text_direction: int;

#desc Filter flags for the texture. See [enum BaseMaterial3D.TextureFilter] for options.
var texture_filter: int;

#desc If [code]true[/code], all the text displays as UPPERCASE.
var uppercase: bool;

#desc Controls the text's vertical alignment. Supports top, center, bottom. Set it to one of the [enum VerticalAlignment] constants.
var vertical_alignment: int;

#desc Text width (in pixels), used for autowrap and fill alignment.
var width: float;



#desc Returns a [TriangleMesh] with the label's vertices following its current configuration (such as its [member pixel_size]).
func generate_triangle_mesh() -> TriangleMesh:
	pass;

#desc Returns the value of the specified flag.
func get_draw_flag(flag: int) -> bool:
	pass;

#desc If [code]true[/code], the specified flag will be enabled. See [enum Label3D.DrawFlags] for a list of flags.
func set_draw_flag(flag: int, enabled: bool) -> void:
	pass;


