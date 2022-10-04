#brief Font loaded from a system font.
#brief [b]Note:[/b] This class is implemented on iOS, Linux, macOS and Windows, on other platforms it will fallback to default theme font.
#desc [SystemFont] loads a font from a system font with the first matching name from [member font_names].
#desc It will attempt to match font style, but it's not guaranteed.
#desc The returned font might be part of a font collection or be a variable font with OpenType "weight" and/or "italic" features set.
#desc You can create [FontVariation] of the system font for fine control over its features.
class_name SystemFont


#desc Font anti-aliasing mode.
var antialiasing: int;

#desc Array of fallback [Font]s.
var fallbacks: Font[];

#desc Array of font family names to search, first matching font found is used.
var font_names: PackedStringArray;

#desc Font style flags, see [enum TextServer.FontStyle].
var font_style: int;

#desc If set to [code]true[/code], auto-hinting is supported and preferred over font built-in hinting.
var force_autohinter: bool;

#desc If set to [code]true[/code], generate mipmaps for the font textures.
var generate_mipmaps: bool;

#desc Font hinting mode.
var hinting: int;

#desc If set to [code]true[/code], glyphs of all sizes are rendered using single multichannel signed distance field generated from the dynamic font vector data.
var multichannel_signed_distance_field: bool;

#desc Font oversampling factor, if set to [code]0.0[/code] global oversampling factor is used instead.
var oversampling: float;

#desc Font glyph sub-pixel positioning mode. Subpixel positioning provides shaper text and better kerning for smaller font sizes, at the cost of memory usage and font rasterization speed. Use [constant TextServer.SUBPIXEL_POSITIONING_AUTO] to automatically enable it based on the font size.
var subpixel_positioning: int;




