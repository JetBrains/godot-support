#brief Font source data and prerendered glyph cache, imported from dynamic or bitmap font.
#desc [FontFile] contains a set of glyphs to represent Unicode characters imported from a font file, as well as a cache of rasterized glyphs, and a set of fallback [Font]s to use.
#desc Use [FontVariation] to access specific OpenType variation of the font, create simulated bold / slanted version, and draw lines of text.
#desc For more complex text processing, use [FontVariation] in conjunction with [TextLine] or [TextParagraph].
#desc Supported font formats:
#desc - Dynamic font importer: TrueType (.ttf), TrueType collection (.ttc), OpenType (.otf), OpenType collection (.otc), WOFF (.woff), WOFF2 (.woff2), Type 1 (.pfb, .pfm).
#desc - Bitmap font importer: AngelCode BMFont (.fnt, .font), text and binary (version 3) format variants.
#desc - Monospace image font importer: All supported image formats.
#desc [b]Note:[/b] A character is a symbol that represents an item (letter, digit etc.) in an abstract way.
#desc [b]Note:[/b] A glyph is a bitmap or shape used to draw a one or more characters in a context-dependent manner. Glyph indices are bound to the specific font data source.
#desc [b]Note:[/b] If a none of the font data sources contain glyphs for a character used in a string, the character in question will be replaced with a box displaying its hexadecimal code.
#desc 
#desc [codeblocks]
#desc [gdscript]
#desc var f = load("res://BarlowCondensed-Bold.ttf")
#desc $"Label".set("custom_fonts/font", f)
#desc $"Label".set("custom_fonts/font_size", 64)
#desc [/gdscript]
#desc [csharp]
#desc var f = ResourceLoader.Load<FontFile>("res://BarlowCondensed-Bold.ttf");
#desc GetNode("Label").Set("custom_fonts/font", f);
#desc GetNode("Label").Set("custom_font_sizes/font_size", 64);
#desc [/csharp]
#desc [/codeblocks]
class_name FontFile


#desc Font anti-aliasing mode.
var antialiasing: int;

#desc Contents of the dynamic font source file.
var data: PackedByteArray;

#desc Array of fallback [Font]s.
var fallbacks: Font[];

#desc Font size, used only for the bitmap fonts.
var fixed_size: int;

#desc Font family name.
var font_name: String;

#desc Font style flags, see [enum TextServer.FontStyle].
var font_style: int;

#desc If set to [code]true[/code], auto-hinting is supported and preferred over font built-in hinting. Used by dynamic fonts only.
var force_autohinter: bool;

#desc If set to [code]true[/code], generate mipmaps for the font textures.
var generate_mipmaps: bool;

#desc Font hinting mode. Used by dynamic fonts only.
var hinting: int;

#desc The width of the range around the shape between the minimum and maximum representable signed distance.
var msdf_pixel_range: int;

#desc Source font size used to generate MSDF textures.
var msdf_size: int;

#desc If set to [code]true[/code], glyphs of all sizes are rendered using single multichannel signed distance field generated from the dynamic font vector data.
var multichannel_signed_distance_field: bool;

#desc Font OpenType feature set override.
var opentype_feature_overrides: Dictionary;

#desc Font oversampling factor, if set to [code]0.0[/code] global oversampling factor is used instead. Used by dynamic fonts only.
var oversampling: float;

#desc Font style name.
var style_name: String;

#desc Font glyph sub-pixel positioning mode. Subpixel positioning provides shaper text and better kerning for smaller font sizes, at the cost of memory usage and font rasterization speed. Use [constant TextServer.SUBPIXEL_POSITIONING_AUTO] to automatically enable it based on the font size.
var subpixel_positioning: int;



#desc Removes all font cache entries.
func clear_cache() -> void:
	pass;

#desc Removes all rendered glyphs information from the cache entry.
#desc [b]Note:[/b] This function will not remove textures associated with the glyphs, use [method remove_texture] to remove them manually.
func clear_glyphs(cache_index: int, size: Vector2i) -> void:
	pass;

#desc Removes all kerning overrides.
func clear_kerning_map(cache_index: int, size: int) -> void:
	pass;

#desc Removes all font sizes from the cache entry
func clear_size_cache(cache_index: int) -> void:
	pass;

#desc Removes all textures from font cache entry.
#desc [b]Note:[/b] This function will not remove glyphs associated with the texture, use [method remove_glyph] to remove them manually.
func clear_textures(cache_index: int, size: Vector2i) -> void:
	pass;

#desc Returns the font ascent (number of pixels above the baseline).
func get_cache_ascent(cache_index: int, size: int) -> float:
	pass;

#desc Returns number of the font cache entries.
func get_cache_count() -> int:
	pass;

func get_cache_descent(cache_index: int, size: int) -> float:
	pass;

func get_cache_scale(cache_index: int, size: int) -> float:
	pass;

func get_cache_underline_position(cache_index: int, size: int) -> float:
	pass;

func get_cache_underline_thickness(cache_index: int, size: int) -> float:
	pass;

#desc Returns embolden strength, if is not equal to zero, emboldens the font outlines. Negative values reduce the outline thickness.
func get_embolden(cache_index: int) -> float:
	pass;

#desc Recturns an active face index in the TrueType / OpenType collection.
func get_face_index(cache_index: int) -> int:
	pass;

#desc Returns glyph advance (offset of the next glyph).
#desc [b]Note:[/b] Advance for glyphs outlines is the same as the base glyph advance and is not saved.
func get_glyph_advance(cache_index: int, size: int, glyph: int) -> Vector2:
	pass;

#desc Returns the glyph index of a [param char], optionally modified by the [param variation_selector].
func get_glyph_index(size: int, char: int, variation_selector: int) -> int:
	pass;

#desc Returns list of rendered glyphs in the cache entry.
func get_glyph_list(cache_index: int, size: Vector2i) -> PackedInt32Array:
	pass;

#desc Returns glyph offset from the baseline.
func get_glyph_offset(cache_index: int, size: Vector2i, glyph: int) -> Vector2:
	pass;

#desc Returns glyph size.
func get_glyph_size(cache_index: int, size: Vector2i, glyph: int) -> Vector2:
	pass;

#desc Returns index of the cache texture containing the glyph.
func get_glyph_texture_idx(cache_index: int, size: Vector2i, glyph: int) -> int:
	pass;

#desc Returns rectangle in the cache texture containing the glyph.
func get_glyph_uv_rect(cache_index: int, size: Vector2i, glyph: int) -> Rect2:
	pass;

#desc Returns kerning for the pair of glyphs.
func get_kerning(cache_index: int, size: int, glyph_pair: Vector2i) -> Vector2:
	pass;

#desc Returns list of the kerning overrides.
func get_kerning_list(cache_index: int, size: int) -> Vector2i[]:
	pass;

#desc Returns [code]true[/code] if support override is enabled for the [param language].
func get_language_support_override(language: String) -> bool:
	pass;

#desc Returns list of language support overrides.
func get_language_support_overrides() -> PackedStringArray:
	pass;

#desc Returns [code]true[/code] if support override is enabled for the [param script].
func get_script_support_override(script: String) -> bool:
	pass;

#desc Returns list of script support overrides.
func get_script_support_overrides() -> PackedStringArray:
	pass;

#desc Returns list of the font sizes in the cache. Each size is [code]Vector2i[/code] with font size and outline size.
func get_size_cache_list(cache_index: int) -> Vector2i[]:
	pass;

#desc Returns number of textures used by font cache entry.
func get_texture_count(cache_index: int, size: Vector2i) -> int:
	pass;

#desc Returns a copy of the font cache texture image.
func get_texture_image(cache_index: int, size: Vector2i, texture_index: int) -> Image:
	pass;

#desc Returns a copy of the array containing the first free pixel in the each column of texture. Should be the same size as texture width or empty.
func get_texture_offsets(cache_index: int, size: Vector2i, texture_index: int) -> PackedInt32Array:
	pass;

#desc Returns 2D transform, applied to the font outlines, can be used for slanting, flipping and rotating glyphs.
func get_transform(cache_index: int) -> Transform2D:
	pass;

#desc Returns variation coordinates for the specified font cache entry. See [method Font.get_supported_variation_list] for more info.
func get_variation_coordinates(cache_index: int) -> Dictionary:
	pass;

#desc Loads an AngelCode BMFont (.fnt, .font) bitmap font from file [param path].
#desc [b]Warning:[/b] This method should only be used in the editor or in cases when you need to load external fonts at run-time, such as fonts located at the [code]user://[/code] directory.
func load_bitmap_font(path: String) -> int:
	pass;

#desc Loads a TrueType (.ttf), OpenType (.otf), WOFF (.woff), WOFF2 (.woff2) or Type 1 (.pfb, .pfm) dynamic font from file [param path].
#desc [b]Warning:[/b] This method should only be used in the editor or in cases when you need to load external fonts at run-time, such as fonts located at the [code]user://[/code] directory.
func load_dynamic_font(path: String) -> int:
	pass;

#desc Removes specified font cache entry.
func remove_cache(cache_index: int) -> void:
	pass;

#desc Removes specified rendered glyph information from the cache entry.
#desc [b]Note:[/b] This function will not remove textures associated with the glyphs, use [method remove_texture] to remove them manually.
func remove_glyph(cache_index: int, size: Vector2i, glyph: int) -> void:
	pass;

#desc Removes kerning override for the pair of glyphs.
func remove_kerning(cache_index: int, size: int, glyph_pair: Vector2i) -> void:
	pass;

#desc Remove language support override.
func remove_language_support_override(language: String) -> void:
	pass;

#desc Removes script support override.
func remove_script_support_override(script: String) -> void:
	pass;

#desc Removes specified font size from the cache entry.
func remove_size_cache(cache_index: int, size: Vector2i) -> void:
	pass;

#desc Removes specified texture from the cache entry.
#desc [b]Note:[/b] This function will not remove glyphs associated with the texture. Remove them manually using [method remove_glyph].
func remove_texture(cache_index: int, size: Vector2i, texture_index: int) -> void:
	pass;

#desc Renders specified glyph to the font cache texture.
func render_glyph(cache_index: int, size: Vector2i, index: int) -> void:
	pass;

#desc Renders the range of characters to the font cache texture.
func render_range(cache_index: int, size: Vector2i, start: int, end: int) -> void:
	pass;

func set_cache_ascent(cache_index: int, size: int, ascent: float) -> void:
	pass;

func set_cache_descent(cache_index: int, size: int, descent: float) -> void:
	pass;

func set_cache_scale(cache_index: int, size: int, scale: float) -> void:
	pass;

func set_cache_underline_position(cache_index: int, size: int, underline_position: float) -> void:
	pass;

func set_cache_underline_thickness(cache_index: int, size: int, underline_thickness: float) -> void:
	pass;

#desc Sets embolden strength, if is not equal to zero, emboldens the font outlines. Negative values reduce the outline thickness.
func set_embolden(cache_index: int, strength: float) -> void:
	pass;

#desc Sets an active face index in the TrueType / OpenType collection.
func set_face_index(cache_index: int, face_index: int) -> void:
	pass;

#desc Sets glyph advance (offset of the next glyph).
#desc [b]Note:[/b] Advance for glyphs outlines is the same as the base glyph advance and is not saved.
func set_glyph_advance(cache_index: int, size: int, glyph: int, advance: Vector2) -> void:
	pass;

#desc Sets glyph offset from the baseline.
func set_glyph_offset(cache_index: int, size: Vector2i, glyph: int, offset: Vector2) -> void:
	pass;

#desc Sets glyph size.
func set_glyph_size(cache_index: int, size: Vector2i, glyph: int, gl_size: Vector2) -> void:
	pass;

#desc Sets index of the cache texture containing the glyph.
func set_glyph_texture_idx(cache_index: int, size: Vector2i, glyph: int, texture_idx: int) -> void:
	pass;

#desc Sets rectangle in the cache texture containing the glyph.
func set_glyph_uv_rect(cache_index: int, size: Vector2i, glyph: int, uv_rect: Rect2) -> void:
	pass;

#desc Sets kerning for the pair of glyphs.
func set_kerning(cache_index: int, size: int, glyph_pair: Vector2i, kerning: Vector2) -> void:
	pass;

#desc Adds override for [method Font.is_language_supported].
func set_language_support_override(language: String, supported: bool) -> void:
	pass;

#desc Adds override for [method Font.is_script_supported].
func set_script_support_override(script: String, supported: bool) -> void:
	pass;

#desc Sets font cache texture image.
func set_texture_image(cache_index: int, size: Vector2i, texture_index: int, image: Image) -> void:
	pass;

#desc Sets array containing the first free pixel in the each column of texture. Should be the same size as texture width or empty (for the fonts without dynamic glyph generation support).
func set_texture_offsets(cache_index: int, size: Vector2i, texture_index: int, offset: PackedInt32Array) -> void:
	pass;

#desc Sets 2D transform, applied to the font outlines, can be used for slanting, flipping and rotating glyphs.
func set_transform(cache_index: int, transform: Transform2D) -> void:
	pass;

#desc Sets variation coordinates for the specified font cache entry. See [method Font.get_supported_variation_list] for more info.
func set_variation_coordinates(cache_index: int, variation_coordinates: Dictionary) -> void:
	pass;


