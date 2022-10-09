extends PrimitiveMesh
#brief Generate an [PrimitiveMesh] from the text.
#desc Generate an [PrimitiveMesh] from the text.
#desc TextMesh can be generated only when using dynamic fonts with vector glyph contours. Bitmap fonts (including bitmap data in the TrueType/OpenType containers, like color emoji fonts) are not supported.
#desc The UV layout is arranged in 4 horizontal strips, top to bottom: 40% of the height for the front face, 40% for the back face, 10% for the outer edges and 10% for the inner edges.
class_name TextMesh


#desc If set to something other than [constant TextServer.AUTOWRAP_OFF], the text gets wrapped inside the node's bounding rectangle. If you resize the node, it will change its height automatically to show all the text. To see how each mode behaves, see [enum TextServer.AutowrapMode].
var autowrap_mode: int;

#desc Step (in pixels) used to approximate Bézier curves.
var curve_step: float;

#desc Depths of the mesh, if set to [code]0.0[/code] only front surface, is generated, and UV layout is changed to use full texture for the front face only.
var depth: float;

#desc Font configuration used to display text.
var font: Font;

#desc Font size of the [TextMesh]'s text.
var font_size: int;

#desc Controls the text's horizontal alignment. Supports left, center, right, and fill, or justify. Set it to one of the [enum HorizontalAlignment] constants.
var horizontal_alignment: int;

#desc Language code used for text shaping algorithms, if left empty current locale is used instead.
var language: String;

#desc Vertical space between lines in multiline [TextMesh].
var line_spacing: float;

#desc The text drawing offset (in pixels).
var offset: Vector2;

#desc The size of one pixel's width on the text to scale it in 3D.
var pixel_size: float;

#desc Set BiDi algorithm override for the structured text.
var structured_text_bidi_override: int;

#desc Set additional options for BiDi override.
var structured_text_bidi_override_options: Array;

#desc The text to generate mesh from.
var text: String;

#desc Base text writing direction.
var text_direction: int;

#desc If [code]true[/code], all the text displays as UPPERCASE.
var uppercase: bool;

#desc Controls the text's vertical alignment. Supports top, center, bottom. Set it to one of the [enum VerticalAlignment] constants.
var vertical_alignment: int;

#desc Text width (in pixels), used for fill alignment.
var width: float;




