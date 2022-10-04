#brief Variation of the [Font].
#desc OpenType variations, simulated bold / slant, and additional font settings like OpenType features and extra spacing.
#desc 
#desc To use simulated bold font variant:
#desc [codeblocks]
#desc [gdscript]
#desc var fv = FontVariation.new()
#desc fv.set_base_font(load("res://BarlowCondensed-Regular.ttf"))
#desc fv.set_variation_embolden(1.2);
#desc $"Label".set("custom_fonts/font", fv)
#desc $"Label".set("custom_fonts/font_size", 64)
#desc [/gdscript]
#desc [csharp]
#desc var fv = new FontVariation();
#desc fv.SetBaseFont(ResourceLoader.Load<FontFile>("res://BarlowCondensed-Regular.ttf"));
#desc fv.SetVariationEmbolden(1.2);
#desc GetNode("Label").Set("custom_fonts/font", fv);
#desc GetNode("Label").Set("custom_font_sizes/font_size", 64);
#desc [/csharp]
#desc [/codeblocks]
class_name FontVariation


#desc Base font used to create a variation. If not set, default [Theme] font is used.
var base_font: Font;

#desc Array of fallback [Font]s. If not set [member base_font] fallback are ussed.
var fallbacks: Font[];

#desc A set of OpenType feature tags. More info: [url=https://docs.microsoft.com/en-us/typography/opentype/spec/featuretags]OpenType feature tags[/url].
var opentype_features: Dictionary;

#desc Extra spacing at the bottom of the line in pixels.
var spacing_bottom: int;

#desc Extra spacing between graphical glyphs
var spacing_glyph: int;

#desc Extra width of the space glyphs.
var spacing_space: int;

#desc Extra spacing at the top of the line in pixels.
var spacing_top: int;

#desc If is not equal to zero, emboldens the font outlines. Negative values reduce the outline thickness.
#desc [b]Note:[/b] Emboldened fonts might have self-intersecting outlines, which will prevent MSDF fonts and [TextMesh] from working correctly.
var variation_embolden: float;

#desc Active face index in the TrueType / OpenType collection file.
var variation_face_index: int;

#desc Font OpenType variation coordinates. More info: [url=https://docs.microsoft.com/en-us/typography/opentype/spec/dvaraxisreg]OpenType variation tags[/url].
var variation_opentype: Dictionary;

#desc 2D transform, applied to the font outlines, can be used for slanting, flipping and rotating glyphs.
#desc For example, to simulate italic typeface by slanting, apply the following transform [code]Transform2D(1.0, slant, 0.0, 1.0, 0.0, 0.0)[/code].
var variation_transform: Transform2D;



#desc Sets the spacing for [code]type[/code] (see [enum TextServer.SpacingType]) to [param value] in pixels (not relative to the font size).
func set_spacing(spacing: int, value: int) -> void:
	pass;


