extends RefCounted
#brief Controls how an individual character will be displayed in a [RichTextEffect].
#desc By setting various properties on this object, you can control how individual characters will be displayed in a [RichTextEffect].
class_name CharFXTransform


#desc The color the character will be drawn with.
var color: Color;

#desc The time elapsed since the [RichTextLabel] was added to the scene tree (in seconds). Time stops when the [RichTextLabel] is paused (see [member Node.process_mode]). Resets when the text in the [RichTextLabel] is changed.
#desc [b]Note:[/b] Time still passes while the [RichTextLabel] is hidden.
var elapsed_time: float;

#desc Contains the arguments passed in the opening BBCode tag. By default, arguments are strings; if their contents match a type such as [bool], [int] or [float], they will be converted automatically. Color codes in the form [code]#rrggbb[/code] or [code]#rgb[/code] will be converted to an opaque [Color]. String arguments may not contain spaces, even if they're quoted. If present, quotes will also be present in the final string.
#desc For example, the opening BBCode tag [code][example foo=hello bar=true baz=42 color=#ffffff][/code] will map to the following [Dictionary]:
#desc [codeblock]
#desc {"foo": "hello", "bar": true, "baz": 42, "color": Color(1, 1, 1, 1)}
#desc [/codeblock]
var env: Dictionary;

#desc Font resource used to render glyph.
var font: RID;

#desc Number of glyphs in the grapheme cluster. This value is set in the first glyph of a cluster. Setting this property won't affect drawing.
var glyph_count: int;

#desc Glyph flags. See [enum TextServer.GraphemeFlag] for more info. Setting this property won't affect drawing.
var glyph_flags: int;

#desc Font specific glyph index.
var glyph_index: int;

#desc The position offset the character will be drawn with (in pixels).
var offset: Vector2;

#desc If [code]true[/code], FX transform is called for outline drawing. Setting this property won't affect drawing.
var outline: bool;

#desc Absolute character range in the string, corresponding to the glyph. Setting this property won't affect drawing.
var range: Vector2i;

#desc If [code]true[/code], the character will be drawn. If [code]false[/code], the character will be hidden. Characters around hidden characters will reflow to take the space of hidden characters. If this is not desired, set their [member color] to [code]Color(1, 1, 1, 0)[/code] instead.
var visible: bool;




