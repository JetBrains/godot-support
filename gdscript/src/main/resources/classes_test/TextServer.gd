#brief Interface for the fonts and complex text layouts.
#desc [TextServer] is the API backend for managing fonts, and rendering complex text.
class_name TextServer

#desc Font glyphs are rasterized as 1-bit bitmaps.
const FONT_ANTIALIASING_NONE = 0;

#desc Font glyphs are rasterized as 8-bit grayscale anti-aliased bitmaps.
const FONT_ANTIALIASING_GRAY = 1;

#desc Font glyphs are rasterized for LCD screens.
#desc LCD sub-pixel layout is determined by the value of [code]gui/theme/lcd_subpixel_layout[/code] project settings.
#desc LCD sub-pixel anti-aliasing mode is suitable only for rendering horizontal, unscaled text in 2D.
const FONT_ANTIALIASING_LCD = 2;

#desc Unknown or unsupported sub-pixel layout, LCD sub-pixel anti-aliasing is disabled.
const FONT_LCD_SUBPIXEL_LAYOUT_NONE = 0;

#desc Horizontal RGB sub-pixel layout.
const FONT_LCD_SUBPIXEL_LAYOUT_HRGB = 1;

#desc Horizontal BGR sub-pixel layout.
const FONT_LCD_SUBPIXEL_LAYOUT_HBGR = 2;

#desc Vertical RGB sub-pixel layout.
const FONT_LCD_SUBPIXEL_LAYOUT_VRGB = 3;

#desc Vertical BGR sub-pixel layout.
const FONT_LCD_SUBPIXEL_LAYOUT_VBGR = 4;

const FONT_LCD_SUBPIXEL_LAYOUT_MAX = 5;

#desc Text direction is determined based on contents and current locale.
const DIRECTION_AUTO = 0;

#desc Text is written from left to right.
const DIRECTION_LTR = 1;

#desc Text is written from right to left.
const DIRECTION_RTL = 2;

#desc Text is written horizontally.
const ORIENTATION_HORIZONTAL = 0;

#desc Left to right text is written vertically from top to bottom.
#desc Right to left text is written vertically from bottom to top.
const ORIENTATION_VERTICAL = 1;

#desc Do not justify text.
const JUSTIFICATION_NONE = 0;

#desc Justify text by adding and removing kashidas.
const JUSTIFICATION_KASHIDA = 1;

#desc Justify text by changing width of the spaces between the words.
const JUSTIFICATION_WORD_BOUND = 2;

#desc Remove trailing and leading spaces from the justified text.
const JUSTIFICATION_TRIM_EDGE_SPACES = 4;

#desc Only apply justification to the part of the text after the last tab.
const JUSTIFICATION_AFTER_LAST_TAB = 8;

#desc Apply justification to the trimmed line with ellipsis.
const JUSTIFICATION_CONSTRAIN_ELLIPSIS = 16;

#desc Autowrap is disabled.
const AUTOWRAP_OFF = 0;

#desc Wraps the text inside the node's bounding rectangle by allowing to break lines at arbitrary positions, which is useful when very limited space is available.
const AUTOWRAP_ARBITRARY = 1;

#desc Wraps the text inside the node's bounding rectangle by soft-breaking between words.
const AUTOWRAP_WORD = 2;

#desc Behaves similarly to [constant AUTOWRAP_WORD], but force-breaks a word if that single word does not fit in one line.
const AUTOWRAP_WORD_SMART = 3;

#desc Do not break the line.
const BREAK_NONE = 0;

#desc Break the line at the line mandatory break characters (e.g. [code]"\n"[/code]).
const BREAK_MANDATORY = 1;

#desc Break the line between the words.
const BREAK_WORD_BOUND = 2;

#desc Break the line between any unconnected graphemes.
const BREAK_GRAPHEME_BOUND = 4;

#desc Should be used only in conjunction with [constant BREAK_WORD_BOUND], break the line between any unconnected graphemes, if it's impossible to break it between the words.
const BREAK_ADAPTIVE = 8;

#desc Remove edge spaces from the broken line segments.
const BREAK_TRIM_EDGE_SPACES = 16;

#desc Trims text before the shaping. e.g, increasing [member Label.visible_characters] or [member RichTextLabel.visible_characters] value is visually identical to typing the text.
const VC_CHARS_BEFORE_SHAPING = 0;

#desc Displays glyphs that are mapped to the first [member Label.visible_characters] or [member RichTextLabel.visible_characters] characters from the beginning of the text.
const VC_CHARS_AFTER_SHAPING = 1;

#desc Displays [member Label.visible_ratio] or [member RichTextLabel.visible_ratio] glyphs, starting from the left or from the right, depending on [member Control.layout_direction] value.
const VC_GLYPHS_AUTO = 2;

#desc Displays [member Label.visible_ratio] or [member RichTextLabel.visible_ratio] glyphs, starting from the left.
const VC_GLYPHS_LTR = 3;

#desc Displays [member Label.visible_ratio] or [member RichTextLabel.visible_ratio] glyphs, starting from the right.
const VC_GLYPHS_RTL = 4;

#desc No text trimming is performed.
const OVERRUN_NO_TRIMMING = 0;

#desc Trims the text per character.
const OVERRUN_TRIM_CHAR = 1;

#desc Trims the text per word.
const OVERRUN_TRIM_WORD = 2;

#desc Trims the text per character and adds an ellipsis to indicate that parts are hidden.
const OVERRUN_TRIM_ELLIPSIS = 3;

#desc Trims the text per word and adds an ellipsis to indicate that parts are hidden.
const OVERRUN_TRIM_WORD_ELLIPSIS = 4;

#desc No trimming is performed.
const OVERRUN_NO_TRIM = 0;

#desc Trims the text when it exceeds the given width.
const OVERRUN_TRIM = 1;

#desc Trims the text per word instead of per grapheme.
const OVERRUN_TRIM_WORD_ONLY = 2;

#desc Determines whether an ellipsis should be added at the end of the text.
const OVERRUN_ADD_ELLIPSIS = 4;

#desc Determines whether the ellipsis at the end of the text is enforced and may not be hidden.
const OVERRUN_ENFORCE_ELLIPSIS = 8;

const OVERRUN_JUSTIFICATION_AWARE = 16;

#desc Grapheme is supported by the font, and can be drawn.
const GRAPHEME_IS_VALID = 1;

#desc Grapheme is part of right-to-left or bottom-to-top run.
const GRAPHEME_IS_RTL = 2;

#desc Grapheme is not part of source text, it was added by justification process.
const GRAPHEME_IS_VIRTUAL = 4;

#desc Grapheme is whitespace.
const GRAPHEME_IS_SPACE = 8;

#desc Grapheme is mandatory break point (e.g. [code]"\n"[/code]).
const GRAPHEME_IS_BREAK_HARD = 16;

#desc Grapheme is optional break point (e.g. space).
const GRAPHEME_IS_BREAK_SOFT = 32;

#desc Grapheme is the tabulation character.
const GRAPHEME_IS_TAB = 64;

#desc Grapheme is kashida.
const GRAPHEME_IS_ELONGATION = 128;

#desc Grapheme is punctuation character.
const GRAPHEME_IS_PUNCTUATION = 256;

#desc Grapheme is underscore character.
const GRAPHEME_IS_UNDERSCORE = 512;

#desc Grapheme is connected to the previous grapheme. Breaking line before this grapheme is not safe.
const GRAPHEME_IS_CONNECTED = 1024;

#desc It is safe to insert a U+0640 before this grapheme for elongation.
const GRAPHEME_IS_SAFE_TO_INSERT_TATWEEL = 2048;

#desc Disables font hinting (smoother but less crisp).
const HINTING_NONE = 0;

#desc Use the light font hinting mode.
const HINTING_LIGHT = 1;

#desc Use the default font hinting mode (crisper but less smooth).
#desc [b]Note:[/b] This hinting mode changes both horizontal and vertical glyph metrics. If applied to monospace font, some glyphs might have different width.
const HINTING_NORMAL = 2;

#desc Glyph horizontal position is rounded to the whole pixel size, each glyph is rasterized once.
const SUBPIXEL_POSITIONING_DISABLED = 0;

#desc Glyph horizontal position is rounded based on font size.
#desc - To one quarter of the pixel size if font size is smaller or equal to [constant SUBPIXEL_POSITIONING_ONE_QUARTER_MAX_SIZE].
#desc - To one half of the pixel size if font size is smaller or equal to [constant SUBPIXEL_POSITIONING_ONE_HALF_MAX_SIZE].
#desc - To the whole pixel size for larger fonts.
const SUBPIXEL_POSITIONING_AUTO = 1;

#desc Glyph horizontal position is rounded to one half of the pixel size, each glyph is rasterized up to two times.
const SUBPIXEL_POSITIONING_ONE_HALF = 2;

#desc Glyph horizontal position is rounded to one quarter of the pixel size, each glyph is rasterized up to four times.
const SUBPIXEL_POSITIONING_ONE_QUARTER = 3;

#desc Maximum font size which will use one half of the pixel subpixel positioning in [constant SUBPIXEL_POSITIONING_AUTO] mode.
const SUBPIXEL_POSITIONING_ONE_HALF_MAX_SIZE = 20;

#desc Maximum font size which will use one quarter of the pixel subpixel positioning in [constant SUBPIXEL_POSITIONING_AUTO] mode.
const SUBPIXEL_POSITIONING_ONE_QUARTER_MAX_SIZE = 16;

#desc TextServer supports simple text layouts.
const FEATURE_SIMPLE_LAYOUT = 1;

#desc TextServer supports bidirectional text layouts.
const FEATURE_BIDI_LAYOUT = 2;

#desc TextServer supports vertical layouts.
const FEATURE_VERTICAL_LAYOUT = 4;

#desc TextServer supports complex text shaping.
const FEATURE_SHAPING = 8;

#desc TextServer supports justification using kashidas.
const FEATURE_KASHIDA_JUSTIFICATION = 16;

#desc TextServer supports complex line/word breaking rules (e.g. dictionary based).
const FEATURE_BREAK_ITERATORS = 32;

#desc TextServer supports loading bitmap fonts.
const FEATURE_FONT_BITMAP = 64;

#desc TextServer supports loading dynamic (TrueType, OpeType, etc.) fonts.
const FEATURE_FONT_DYNAMIC = 128;

#desc TextServer supports multichannel signed distance field dynamic font rendering.
const FEATURE_FONT_MSDF = 256;

#desc TextServer supports loading system fonts.
const FEATURE_FONT_SYSTEM = 512;

#desc TextServer supports variable fonts.
const FEATURE_FONT_VARIABLE = 1024;

#desc TextServer supports locale dependent and context sensitive case conversion.
const FEATURE_CONTEXT_SENSITIVE_CASE_CONVERSION = 2048;

#desc TextServer require external data file for some features, see [method load_support_data].
const FEATURE_USE_SUPPORT_DATA = 4096;

#desc TextServer supports UAX #31 identifier validation, see [method is_valid_identifier].
const FEATURE_UNICODE_IDENTIFIERS = 8192;

#desc TextServer supports [url=https://unicode.org/reports/tr36/]Unicode Technical Report #36[/url] and [url=https://unicode.org/reports/tr39/]Unicode Technical Standard #39[/url] based spoof detection features.
const FEATURE_UNICODE_SECURITY = 16384;

#desc Contour point is on the curve.
const CONTOUR_CURVE_TAG_ON = 1;

#desc Contour point isn't on the curve, but serves as a control point for a conic (quadratic) Bézier arc.
const CONTOUR_CURVE_TAG_OFF_CONIC = 0;

#desc Contour point isn't on the curve, but serves as a control point for a cubic Bézier arc.
const CONTOUR_CURVE_TAG_OFF_CUBIC = 2;

#desc Spacing for each glyph.
const SPACING_GLYPH = 0;

#desc Spacing for the space character.
const SPACING_SPACE = 1;

#desc Spacing at the top of the line.
const SPACING_TOP = 2;

#desc Spacing at the bottom of the line.
const SPACING_BOTTOM = 3;

const SPACING_MAX = 4;

#desc Font is bold.
const FONT_BOLD = 1;

#desc Font is italic or oblique.
const FONT_ITALIC = 2;

#desc Font have fixed-width characters.
const FONT_FIXED_WIDTH = 4;

#desc Use default behavior. Same as [code]STRUCTURED_TEXT_NONE[/code] unless specified otherwise in the control description.
const STRUCTURED_TEXT_DEFAULT = 0;

#desc BiDi override for URI.
const STRUCTURED_TEXT_URI = 1;

#desc BiDi override for file path.
const STRUCTURED_TEXT_FILE = 2;

#desc BiDi override for email.
const STRUCTURED_TEXT_EMAIL = 3;

#desc BiDi override for lists.
#desc Structured text options: list separator [code]String[/code].
const STRUCTURED_TEXT_LIST = 4;

#desc Use default Unicode BiDi algorithm.
const STRUCTURED_TEXT_NONE = 5;

#desc User defined structured text BiDi override function.
const STRUCTURED_TEXT_CUSTOM = 6;




#desc Creates new, empty font cache entry resource. To free the resulting resourec, use [method free_rid] method.
func create_font() -> RID:
	pass;

#desc Creates new buffer for complex text layout, with the given [param direction] and [param orientation]. To free the resulting buffer, use [method free_rid] method.
#desc [b]Note:[/b] Direction is ignored if server does not support [constant FEATURE_BIDI_LAYOUT] feature (supported by [TextServerAdvanced]).
#desc [b]Note:[/b] Orientation is ignored if server does not support [constant FEATURE_VERTICAL_LAYOUT] feature (supported by [TextServerAdvanced]).
func create_shaped_text(direction: int, orientation: int) -> RID:
	pass;

#desc Draws box displaying character hexadecimal code. Used for replacing missing characters.
func draw_hex_code_box(canvas: RID, size: int, pos: Vector2, index: int, color: Color) -> void:
	pass;

#desc Removes all rendered glyphs information from the cache entry.
#desc [b]Note:[/b] This function will not remove textures associated with the glyphs, use [method font_remove_texture] to remove them manually.
func font_clear_glyphs(font_rid: RID, size: Vector2i) -> void:
	pass;

#desc Removes all kerning overrides.
func font_clear_kerning_map(font_rid: RID, size: int) -> void:
	pass;

#desc Removes all font sizes from the cache entry.
func font_clear_size_cache(font_rid: RID) -> void:
	pass;

#desc Removes all textures from font cache entry.
#desc [b]Note:[/b] This function will not remove glyphs associated with the texture, use [method font_remove_glyph] to remove them manually.
func font_clear_textures(font_rid: RID, size: Vector2i) -> void:
	pass;

#desc Draws single glyph into a canvas item at the position, using [param font_rid] at the size [param size].
#desc [b]Note:[/b] Glyph index is specific to the font, use glyphs indices returned by [method shaped_text_get_glyphs] or [method font_get_glyph_index].
#desc [b]Note:[/b] If there are pending glyphs to render, calling this function might trigger the texture cache update.
func font_draw_glyph(font_rid: RID, canvas: RID, size: int, pos: Vector2, index: int, color: Color) -> void:
	pass;

#desc Draws single glyph outline of size [param outline_size] into a canvas item at the position, using [param font_rid] at the size [param size].
#desc [b]Note:[/b] Glyph index is specific to the font, use glyphs indices returned by [method shaped_text_get_glyphs] or [method font_get_glyph_index].
#desc [b]Note:[/b] If there are pending glyphs to render, calling this function might trigger the texture cache update.
func font_draw_glyph_outline(font_rid: RID, canvas: RID, size: int, outline_size: int, pos: Vector2, index: int, color: Color) -> void:
	pass;

#desc Returns font anti-aliasing mode.
func font_get_antialiasing(font_rid: RID) -> int:
	pass;

#desc Returns the font ascent (number of pixels above the baseline).
func font_get_ascent(font_rid: RID, size: int) -> float:
	pass;

#desc Returns the font descent (number of pixels below the baseline).
func font_get_descent(font_rid: RID, size: int) -> float:
	pass;

#desc Returns font embolden strength.
func font_get_embolden(font_rid: RID) -> float:
	pass;

#desc Returns number of faces in the TrueType / OpenType collection.
func font_get_face_count(font_rid: RID) -> int:
	pass;

#desc Recturns an active face index in the TrueType / OpenType collection.
func font_get_face_index(font_rid: RID) -> int:
	pass;

#desc Returns bitmap font fixed size.
func font_get_fixed_size(font_rid: RID) -> int:
	pass;

#desc Returns [code]true[/code] if font texture mipmap generation is enabled.
func font_get_generate_mipmaps(font_rid: RID) -> bool:
	pass;

#desc Returns the font oversampling factor, shared by all fonts in the TextServer.
func font_get_global_oversampling() -> float:
	pass;

#desc Returns glyph advance (offset of the next glyph).
#desc [b]Note:[/b] Advance for glyphs outlines is the same as the base glyph advance and is not saved.
func font_get_glyph_advance(font_rid: RID, size: int, glyph: int) -> Vector2:
	pass;

#desc Returns outline contours of the glyph as a [code]Dictionary[/code] with the following contents:
#desc [code]points[/code]         - [PackedVector3Array], containing outline points. [code]x[/code] and [code]y[/code] are point coordinates. [code]z[/code] is the type of the point, using the [enum ContourPointTag] values.
#desc [code]contours[/code]       - [PackedInt32Array], containing indices the end points of each contour.
#desc [code]orientation[/code]    - [bool], contour orientation. If [code]true[/code], clockwise contours must be filled.
func font_get_glyph_contours(font: RID, size: int, index: int) -> Dictionary:
	pass;

#desc Returns the glyph index of a [param char], optionally modified by the [param variation_selector].
func font_get_glyph_index(font_rid: RID, size: int, char: int, variation_selector: int) -> int:
	pass;

#desc Returns list of rendered glyphs in the cache entry.
func font_get_glyph_list(font_rid: RID, size: Vector2i) -> PackedInt32Array:
	pass;

#desc Returns glyph offset from the baseline.
func font_get_glyph_offset(font_rid: RID, size: Vector2i, glyph: int) -> Vector2:
	pass;

#desc Returns size of the glyph.
func font_get_glyph_size(font_rid: RID, size: Vector2i, glyph: int) -> Vector2:
	pass;

#desc Returns index of the cache texture containing the glyph.
func font_get_glyph_texture_idx(font_rid: RID, size: Vector2i, glyph: int) -> int:
	pass;

#desc Returns resource id of the cache texture containing the glyph.
#desc [b]Note:[/b] If there are pending glyphs to render, calling this function might trigger the texture cache update.
func font_get_glyph_texture_rid(font_rid: RID, size: Vector2i, glyph: int) -> RID:
	pass;

#desc Returns size of the cache texture containing the glyph.
#desc [b]Note:[/b] If there are pending glyphs to render, calling this function might trigger the texture cache update.
func font_get_glyph_texture_size(font_rid: RID, size: Vector2i, glyph: int) -> Vector2:
	pass;

#desc Returns rectangle in the cache texture containing the glyph.
func font_get_glyph_uv_rect(font_rid: RID, size: Vector2i, glyph: int) -> Rect2:
	pass;

#desc Returns the font hinting mode. Used by dynamic fonts only.
func font_get_hinting(font_rid: RID) -> int:
	pass;

#desc Returns kerning for the pair of glyphs.
func font_get_kerning(font_rid: RID, size: int, glyph_pair: Vector2i) -> Vector2:
	pass;

#desc Returns list of the kerning overrides.
func font_get_kerning_list(font_rid: RID, size: int) -> Vector2i[]:
	pass;

#desc Returns [code]true[/code] if support override is enabled for the [param language].
func font_get_language_support_override(font_rid: RID, language: String) -> bool:
	pass;

#desc Returns list of language support overrides.
func font_get_language_support_overrides(font_rid: RID) -> PackedStringArray:
	pass;

#desc Returns the width of the range around the shape between the minimum and maximum representable signed distance.
func font_get_msdf_pixel_range(font_rid: RID) -> int:
	pass;

#desc Returns source font size used to generate MSDF textures.
func font_get_msdf_size(font_rid: RID) -> int:
	pass;

#desc Returns font family name.
func font_get_name(font_rid: RID) -> String:
	pass;

#desc Returns font OpenType feature set override.
func font_get_opentype_feature_overrides(font_rid: RID) -> Dictionary:
	pass;

#desc Returns font oversampling factor, if set to [code]0.0[/code] global oversampling factor is used instead. Used by dynamic fonts only.
func font_get_oversampling(font_rid: RID) -> float:
	pass;

#desc Returns scaling factor of the color bitmap font.
func font_get_scale(font_rid: RID, size: int) -> float:
	pass;

#desc Returns [code]true[/code] if support override is enabled for the [param script].
func font_get_script_support_override(font_rid: RID, script: String) -> bool:
	pass;

#desc Returns list of script support overrides.
func font_get_script_support_overrides(font_rid: RID) -> PackedStringArray:
	pass;

#desc Returns list of the font sizes in the cache. Each size is [code]Vector2i[/code] with font size and outline size.
func font_get_size_cache_list(font_rid: RID) -> Vector2i[]:
	pass;

#desc Returns font style flags, see [enum FontStyle].
func font_get_style(font_rid: RID) -> int:
	pass;

#desc Returns font style name.
func font_get_style_name(font_rid: RID) -> String:
	pass;

#desc Returns font sub-pixel glyph positioning mode.
func font_get_subpixel_positioning(font_rid: RID) -> int:
	pass;

#desc Returns a string containing all the characters available in the font.
func font_get_supported_chars(font_rid: RID) -> String:
	pass;

#desc Returns number of textures used by font cache entry.
func font_get_texture_count(font_rid: RID, size: Vector2i) -> int:
	pass;

#desc Returns font cache texture image data.
func font_get_texture_image(font_rid: RID, size: Vector2i, texture_index: int) -> Image:
	pass;

#desc Returns array containing the first free pixel in the each column of texture. Should be the same size as texture width or empty.
func font_get_texture_offsets(font_rid: RID, size: Vector2i, texture_index: int) -> PackedInt32Array:
	pass;

#desc Returns 2D transform applied to the font outlines.
func font_get_transform(font_rid: RID) -> Transform2D:
	pass;

#desc Returns pixel offset of the underline below the baseline.
func font_get_underline_position(font_rid: RID, size: int) -> float:
	pass;

#desc Returns thickness of the underline in pixels.
func font_get_underline_thickness(font_rid: RID, size: int) -> float:
	pass;

#desc Returns variation coordinates for the specified font cache entry. See [method font_supported_variation_list] for more info.
func font_get_variation_coordinates(font_rid: RID) -> Dictionary:
	pass;

#desc Returns [code]true[/code] if a Unicode [param char] is available in the font.
func font_has_char(font_rid: RID, char: int) -> bool:
	pass;

#desc Returns [code]true[/code] if auto-hinting is supported and preferred over font built-in hinting. Used by dynamic fonts only.
func font_is_force_autohinter(font_rid: RID) -> bool:
	pass;

#desc Returns [code]true[/code], if font supports given language ([url=https://en.wikipedia.org/wiki/ISO_639-1]ISO 639[/url] code).
func font_is_language_supported(font_rid: RID, language: String) -> bool:
	pass;

#desc Returns [code]true[/code] if glyphs of all sizes are rendered using single multichannel signed distance field generated from the dynamic font vector data.
func font_is_multichannel_signed_distance_field(font_rid: RID) -> bool:
	pass;

#desc Returns [code]true[/code], if font supports given script (ISO 15924 code).
func font_is_script_supported(font_rid: RID, script: String) -> bool:
	pass;

#desc Removes specified rendered glyph information from the cache entry.
#desc [b]Note:[/b] This function will not remove textures associated with the glyphs, use [method font_remove_texture] to remove them manually.
func font_remove_glyph(font_rid: RID, size: Vector2i, glyph: int) -> void:
	pass;

#desc Removes kerning override for the pair of glyphs.
func font_remove_kerning(font_rid: RID, size: int, glyph_pair: Vector2i) -> void:
	pass;

#desc Remove language support override.
func font_remove_language_support_override(font_rid: RID, language: String) -> void:
	pass;

#desc Removes script support override.
func font_remove_script_support_override(font_rid: RID, script: String) -> void:
	pass;

#desc Removes specified font size from the cache entry.
func font_remove_size_cache(font_rid: RID, size: Vector2i) -> void:
	pass;

#desc Removes specified texture from the cache entry.
#desc [b]Note:[/b] This function will not remove glyphs associated with the texture, remove them manually, using [method font_remove_glyph].
func font_remove_texture(font_rid: RID, size: Vector2i, texture_index: int) -> void:
	pass;

#desc Renders specified glyph to the font cache texture.
func font_render_glyph(font_rid: RID, size: Vector2i, index: int) -> void:
	pass;

#desc Renders the range of characters to the font cache texture.
func font_render_range(font_rid: RID, size: Vector2i, start: int, end: int) -> void:
	pass;

#desc Sets font anti-aliasing mode.
func font_set_antialiasing(font_rid: RID, antialiasing: int) -> void:
	pass;

#desc Sets the font ascent (number of pixels above the baseline).
func font_set_ascent(font_rid: RID, size: int, ascent: float) -> void:
	pass;

#desc Sets font source data, e.g contents of the dynamic font source file.
func font_set_data(font_rid: RID, data: PackedByteArray) -> void:
	pass;

#desc Sets the font descent (number of pixels below the baseline).
func font_set_descent(font_rid: RID, size: int, descent: float) -> void:
	pass;

#desc Sets font embolden strength. If [param strength] is not equal to zero, emboldens the font outlines. Negative values reduce the outline thickness.
func font_set_embolden(font_rid: RID, strength: float) -> void:
	pass;

#desc Sets an active face index in the TrueType / OpenType collection.
func font_set_face_index(font_rid: RID, face_index: int) -> void:
	pass;

#desc Sets bitmap font fixed size. If set to value greater than zero, same cache entry will be used for all font sizes.
func font_set_fixed_size(font_rid: RID, fixed_size: int) -> void:
	pass;

#desc If set to [code]true[/code] auto-hinting is preferred over font built-in hinting.
func font_set_force_autohinter(font_rid: RID, force_autohinter: bool) -> void:
	pass;

#desc If set to [code]true[/code] font texture mipmap generation is enabled.
func font_set_generate_mipmaps(font_rid: RID, generate_mipmaps: bool) -> void:
	pass;

#desc Sets oversampling factor, shared by all font in the TextServer.
#desc [b]Note:[/b] This value can be automatically changed by display server.
func font_set_global_oversampling(oversampling: float) -> void:
	pass;

#desc Sets glyph advance (offset of the next glyph).
#desc [b]Note:[/b] Advance for glyphs outlines is the same as the base glyph advance and is not saved.
func font_set_glyph_advance(font_rid: RID, size: int, glyph: int, advance: Vector2) -> void:
	pass;

#desc Sets glyph offset from the baseline.
func font_set_glyph_offset(font_rid: RID, size: Vector2i, glyph: int, offset: Vector2) -> void:
	pass;

#desc Sets size of the glyph.
func font_set_glyph_size(font_rid: RID, size: Vector2i, glyph: int, gl_size: Vector2) -> void:
	pass;

#desc Sets index of the cache texture containing the glyph.
func font_set_glyph_texture_idx(font_rid: RID, size: Vector2i, glyph: int, texture_idx: int) -> void:
	pass;

#desc Sets rectangle in the cache texture containing the glyph.
func font_set_glyph_uv_rect(font_rid: RID, size: Vector2i, glyph: int, uv_rect: Rect2) -> void:
	pass;

#desc Sets font hinting mode. Used by dynamic fonts only.
func font_set_hinting(font_rid: RID, hinting: int) -> void:
	pass;

#desc Sets kerning for the pair of glyphs.
func font_set_kerning(font_rid: RID, size: int, glyph_pair: Vector2i, kerning: Vector2) -> void:
	pass;

#desc Adds override for [method font_is_language_supported].
func font_set_language_support_override(font_rid: RID, language: String, supported: bool) -> void:
	pass;

#desc Sets the width of the range around the shape between the minimum and maximum representable signed distance.
func font_set_msdf_pixel_range(font_rid: RID, msdf_pixel_range: int) -> void:
	pass;

#desc Sets source font size used to generate MSDF textures.
func font_set_msdf_size(font_rid: RID, msdf_size: int) -> void:
	pass;

#desc If set to [code]true[/code], glyphs of all sizes are rendered using single multichannel signed distance field generated from the dynamic font vector data. MSDF rendering allows displaying the font at any scaling factor without blurriness, and without incurring a CPU cost when the font size changes (since the font no longer needs to be rasterized on the CPU). As a downside, font hinting is not available with MSDF. The lack of font hinting may result in less crisp and less readable fonts at small sizes.
#desc [b]Note:[/b] MSDF font rendering does not render glyphs with overlapping shapes correctly. Overlapping shapes are not valid per the OpenType standard, but are still commonly found in many font files, especially those converted by Google Fonts. To avoid issues with overlapping glyphs, consider downloading the font file directly from the type foundry instead of relying on Google Fonts.
func font_set_multichannel_signed_distance_field(font_rid: RID, msdf: bool) -> void:
	pass;

#desc Sets the font family name.
func font_set_name(font_rid: RID, name: String) -> void:
	pass;

#desc Sets font OpenType feature set override.
func font_set_opentype_feature_overrides(font_rid: RID, overrides: Dictionary) -> void:
	pass;

#desc Sets font oversampling factor, if set to [code]0.0[/code] global oversampling factor is used instead. Used by dynamic fonts only.
func font_set_oversampling(font_rid: RID, oversampling: float) -> void:
	pass;

#desc Sets scaling factor of the color bitmap font.
func font_set_scale(font_rid: RID, size: int, scale: float) -> void:
	pass;

#desc Adds override for [method font_is_script_supported].
func font_set_script_support_override(font_rid: RID, script: String, supported: bool) -> void:
	pass;

#desc Sets the font style flags, see [enum FontStyle].
func font_set_style(font_rid: RID, style: int) -> void:
	pass;

#desc Sets the font style name.
func font_set_style_name(font_rid: RID, name: String) -> void:
	pass;

#desc Sets font sub-pixel glyph positioning mode.
func font_set_subpixel_positioning(font_rid: RID, subpixel_positioning: int) -> void:
	pass;

#desc Sets font cache texture image data.
func font_set_texture_image(font_rid: RID, size: Vector2i, texture_index: int, image: Image) -> void:
	pass;

#desc Sets array containing the first free pixel in the each column of texture. Should be the same size as texture width or empty.
func font_set_texture_offsets(font_rid: RID, size: Vector2i, texture_index: int, offset: PackedInt32Array) -> void:
	pass;

#desc Sets 2D transform, applied to the font outlines, can be used for slanting, flipping and rotating glyphs.
#desc For example, to simulate italic typeface by slanting, apply the following transform [code]Transform2D(1.0, slant, 0.0, 1.0, 0.0, 0.0)[/code].
func font_set_transform(font_rid: RID, transform: Transform2D) -> void:
	pass;

#desc Sets pixel offset of the underline below the baseline.
func font_set_underline_position(font_rid: RID, size: int, underline_position: float) -> void:
	pass;

#desc Sets thickness of the underline in pixels.
func font_set_underline_thickness(font_rid: RID, size: int, underline_thickness: float) -> void:
	pass;

#desc Sets variation coordinates for the specified font cache entry. See [method font_supported_variation_list] for more info.
func font_set_variation_coordinates(font_rid: RID, variation_coordinates: Dictionary) -> void:
	pass;

#desc Returns the dictionary of the supported OpenType features.
func font_supported_feature_list(font_rid: RID) -> Dictionary:
	pass;

#desc Returns the dictionary of the supported OpenType variation coordinates.
func font_supported_variation_list(font_rid: RID) -> Dictionary:
	pass;

#desc Converts a number from the Western Arabic (0..9) to the numeral systems used in [param language].
func format_number(number: String, language: String) -> String:
	pass;

#desc Frees an object created by this [TextServer].
func free_rid(rid: RID) -> void:
	pass;

#desc Returns text server features, see [enum Feature].
func get_features() -> int:
	pass;

#desc Returns size of the replacement character (box with character hexadecimal code that is drawn in place of invalid characters).
func get_hex_code_box_size(size: int, index: int) -> Vector2:
	pass;

#desc Returns the name of the server interface.
func get_name() -> String:
	pass;

#desc Returns default TextServer database (e.g. ICU break iterators and dictionaries) filename.
func get_support_data_filename() -> String:
	pass;

#desc Returns TextServer database (e.g. ICU break iterators and dictionaries) description.
func get_support_data_info() -> String:
	pass;

#desc Returns [code]true[/code] if [param rid] is valid resource owned by this text server.
func has(rid: RID) -> bool:
	pass;

#desc Returns [code]true[/code] if the server supports a feature.
func has_feature(feature: int) -> bool:
	pass;

#desc Returns index of the first string in [param dict] which is visually confusable with the [param string], or [code]-1[/code] if none is found.
#desc [b]Note:[/b] This method doesn't detect invisible characters, for spoof detection use it in combination with [method spoof_check].
#desc [b]Note:[/b] Always returns [code]-1[/code] if the server does not support the [constant FEATURE_UNICODE_SECURITY] feature.
func is_confusable(string: String, dict: PackedStringArray) -> int:
	pass;

#desc Returns [code]true[/code] if locale is right-to-left.
func is_locale_right_to_left(locale: String) -> bool:
	pass;

#desc Returns [code]true[/code] is [param string] is a valid identifier.
#desc If the text server supports the [constant FEATURE_UNICODE_IDENTIFIERS] feature, a valid identifier must:
#desc - Conform to normalization form C.
#desc - Begin with a Unicode character of class XID_Start or [code]"_"[/code].
#desc - May contain Unicode characters of class XID_Continue in the other positions.
#desc - Use UAX #31 recommended scripts only (mixed scripts are allowed).
#desc If the [constant FEATURE_UNICODE_IDENTIFIERS] feature is not supported, a valid identifier must:
#desc - Begin with a Unicode character of class XID_Start or [code]"_"[/code].
#desc - May contain Unicode characters of class XID_Continue in the other positions.
func is_valid_identifier(string: String) -> bool:
	pass;

#desc Loads optional TextServer database (e.g. ICU break iterators and dictionaries).
#desc [b]Note:[/b] This function should be called before any other TextServer functions used, otherwise it won't have any effect.
func load_support_data(filename: String) -> bool:
	pass;

#desc Converts readable feature, variation, script or language name to OpenType tag.
func name_to_tag(name: String) -> int:
	pass;

#desc Converts [param number] from the numeral systems used in [param language] to Western Arabic (0..9).
func parse_number(number: String, language: String) -> String:
	pass;

#desc Default implementation of the BiDi algorithm override function. See [enum StructuredTextParser] for more info.
func parse_structured_text(parser_type: int, args: Array, text: String) -> Vector2i[]:
	pass;

#desc Returns percent sign used in the [param language].
func percent_sign(language: String) -> String:
	pass;

#desc Saves optional TextServer database (e.g. ICU break iterators and dictionaries) to the file.
#desc [b]Note:[/b] This function is used by during project export, to include TextServer database.
func save_support_data(filename: String) -> bool:
	pass;

#desc Returns number of text spans added using [method shaped_text_add_string] or [method shaped_text_add_object].
func shaped_get_span_count(shaped: RID) -> int:
	pass;

#desc Returns text span metadata.
func shaped_get_span_meta(shaped: RID, index: int) -> Variant:
	pass;

#desc Changes text span font, font size and OpenType features, without changing the text.
func shaped_set_span_update_font(shaped: RID, index: int, fonts: RID[], size: int, opentype_features: Dictionary) -> void:
	pass;

#desc Adds inline object to the text buffer, [param key] must be unique. In the text, object is represented as [param length] object replacement characters.
func shaped_text_add_object(shaped: RID, key: Variant, size: Vector2, inline_align: int, length: int) -> bool:
	pass;

#desc Adds text span and font to draw it to the text buffer.
func shaped_text_add_string(shaped: RID, text: String, fonts: RID[], size: int, opentype_features: Dictionary, language: String, meta: Variant) -> bool:
	pass;

#desc Clears text buffer (removes text and inline objects).
func shaped_text_clear(rid: RID) -> void:
	pass;

#desc Draw shaped text into a canvas item at a given position, with [param color]. [param pos] specifies the leftmost point of the baseline (for horizontal layout) or topmost point of the baseline (for vertical layout).
func shaped_text_draw(shaped: RID, canvas: RID, pos: Vector2, clip_l: float, clip_r: float, color: Color) -> void:
	pass;

#desc Draw the outline of the shaped text into a canvas item at a given position, with [param color]. [param pos] specifies the leftmost point of the baseline (for horizontal layout) or topmost point of the baseline (for vertical layout).
func shaped_text_draw_outline(shaped: RID, canvas: RID, pos: Vector2, clip_l: float, clip_r: float, outline_size: int, color: Color) -> void:
	pass;

#desc Adjusts text with to fit to specified width, returns new text width.
func shaped_text_fit_to_width(shaped: RID, width: float, jst_flags: int) -> float:
	pass;

#desc Returns the text ascent (number of pixels above the baseline for horizontal layout or to the left of baseline for vertical).
#desc [b]Note:[/b] Overall ascent can be higher than font ascent, if some glyphs are displaced from the baseline.
func shaped_text_get_ascent(shaped: RID) -> float:
	pass;

#desc Returns shapes of the carets corresponding to the character offset [param position] in the text. Returned caret shape is 1 pixel wide rectangle.
func shaped_text_get_carets(shaped: RID, position: int) -> Dictionary:
	pass;

#desc Returns custom punctuation character list, used for word breaking. If set to empty string, server defaults are used.
func shaped_text_get_custom_punctuation(shaped: RID) -> String:
	pass;

#desc Returns the text descent (number of pixels below the baseline for horizontal layout or to the right of baseline for vertical).
#desc [b]Note:[/b] Overall descent can be higher than font descent, if some glyphs are displaced from the baseline.
func shaped_text_get_descent(shaped: RID) -> float:
	pass;

#desc Returns direction of the text.
func shaped_text_get_direction(shaped: RID) -> int:
	pass;

#desc Returns dominant direction of in the range of text.
func shaped_text_get_dominant_direction_in_range(shaped: RID, start: int, end: int) -> int:
	pass;

#desc Returns number of glyphs in the ellipsis.
func shaped_text_get_ellipsis_glyph_count(shaped: RID) -> int:
	pass;

#desc Returns array of the glyphs in the ellipsis.
func shaped_text_get_ellipsis_glyphs(shaped: RID) -> Dictionary[]:
	pass;

#desc Returns position of the ellipsis.
func shaped_text_get_ellipsis_pos(shaped: RID) -> int:
	pass;

#desc Returns number of glyphs in the buffer.
func shaped_text_get_glyph_count(shaped: RID) -> int:
	pass;

#desc Returns an array of glyphs in the visual order.
func shaped_text_get_glyphs(shaped: RID) -> Dictionary[]:
	pass;

#desc Returns composite character's bounds as offsets from the start of the line.
func shaped_text_get_grapheme_bounds(shaped: RID, pos: int) -> Vector2:
	pass;

#desc Returns direction of the text, inferred by the BiDi algorithm.
func shaped_text_get_inferred_direction(shaped: RID) -> int:
	pass;

#desc Breaks text to the lines and returns character ranges for each line.
func shaped_text_get_line_breaks(shaped: RID, width: float, start: int, break_flags: int) -> PackedInt32Array:
	pass;

#desc Breaks text to the lines and columns. Returns character ranges for each segment.
func shaped_text_get_line_breaks_adv(shaped: RID, width: PackedFloat32Array, start: int, once: bool, break_flags: int) -> PackedInt32Array:
	pass;

#desc Returns bounding rectangle of the inline object.
func shaped_text_get_object_rect(shaped: RID, key: Variant) -> Rect2:
	pass;

#desc Returns array of inline objects.
func shaped_text_get_objects(shaped: RID) -> Array:
	pass;

#desc Returns text orientation.
func shaped_text_get_orientation(shaped: RID) -> int:
	pass;

#desc Returns the parent buffer from which the substring originates.
func shaped_text_get_parent(shaped: RID) -> RID:
	pass;

#desc Returns [code]true[/code] if text buffer is configured to display control characters.
func shaped_text_get_preserve_control(shaped: RID) -> bool:
	pass;

#desc Returns [code]true[/code] if text buffer is configured to display hexadecimal codes in place of invalid characters.
#desc [b]Note:[/b] If set to [code]false[/code], nothing is displayed in place of invalid characters.
func shaped_text_get_preserve_invalid(shaped: RID) -> bool:
	pass;

#desc Returns substring buffer character range in the parent buffer.
func shaped_text_get_range(shaped: RID) -> Vector2i:
	pass;

#desc Returns selection rectangles for the specified character range.
func shaped_text_get_selection(shaped: RID, start: int, end: int) -> PackedVector2Array:
	pass;

#desc Returns size of the text.
func shaped_text_get_size(shaped: RID) -> Vector2:
	pass;

#desc Returns extra spacing added between glyphs or lines in pixels.
func shaped_text_get_spacing(shaped: RID, spacing: int) -> int:
	pass;

#desc Returns the position of the overrun trim.
func shaped_text_get_trim_pos(shaped: RID) -> int:
	pass;

#desc Returns pixel offset of the underline below the baseline.
func shaped_text_get_underline_position(shaped: RID) -> float:
	pass;

#desc Returns thickness of the underline.
func shaped_text_get_underline_thickness(shaped: RID) -> float:
	pass;

#desc Returns width (for horizontal layout) or height (for vertical) of the text.
func shaped_text_get_width(shaped: RID) -> float:
	pass;

#desc Breaks text into words and returns array of character ranges. Use [param grapheme_flags] to set what characters are used for breaking (see [enum GraphemeFlag]).
func shaped_text_get_word_breaks(shaped: RID, grapheme_flags: int) -> PackedInt32Array:
	pass;

#desc Returns grapheme index at the specified pixel offset at the baseline, or [code]-1[/code] if none is found.
func shaped_text_hit_test_grapheme(shaped: RID, coords: float) -> int:
	pass;

#desc Returns caret character offset at the specified pixel offset at the baseline. This function always returns a valid position.
func shaped_text_hit_test_position(shaped: RID, coords: float) -> int:
	pass;

#desc Returns [code]true[/code] if buffer is successfully shaped.
func shaped_text_is_ready(shaped: RID) -> bool:
	pass;

#desc Returns composite character end position closest to the [param pos].
func shaped_text_next_grapheme_pos(shaped: RID, pos: int) -> int:
	pass;

#desc Trims text if it exceeds the given width.
func shaped_text_overrun_trim_to_width(shaped: RID, width: float, overrun_trim_flags: int) -> void:
	pass;

#desc Returns composite character start position closest to the [param pos].
func shaped_text_prev_grapheme_pos(shaped: RID, pos: int) -> int:
	pass;

#desc Sets new size and alignment of embedded object.
func shaped_text_resize_object(shaped: RID, key: Variant, size: Vector2, inline_align: int) -> bool:
	pass;

#desc Overrides BiDi for the structured text.
#desc Override ranges should cover full source text without overlaps. BiDi algorithm will be used on each range separately.
func shaped_text_set_bidi_override(shaped: RID, override: Array) -> void:
	pass;

#desc Sets custom punctuation character list, used for word breaking. If set to empty string, server defaults are used.
func shaped_text_set_custom_punctuation(shaped: RID, punct: String) -> void:
	pass;

#desc Sets desired text direction. If set to [code]TEXT_DIRECTION_AUTO[/code], direction will be detected based on the buffer contents and current locale.
#desc [b]Note:[/b] Direction is ignored if server does not support [constant FEATURE_BIDI_LAYOUT] feature (supported by [TextServerAdvanced]).
func shaped_text_set_direction(shaped: RID, direction: int) -> void:
	pass;

#desc Sets desired text orientation.
#desc [b]Note:[/b] Orientation is ignored if server does not support [constant FEATURE_VERTICAL_LAYOUT] feature (supported by [TextServerAdvanced]).
func shaped_text_set_orientation(shaped: RID, orientation: int) -> void:
	pass;

#desc If set to [code]true[/code] text buffer will display control characters.
func shaped_text_set_preserve_control(shaped: RID, enabled: bool) -> void:
	pass;

#desc If set to [code]true[/code] text buffer will display invalid characters as hexadecimal codes, otherwise nothing is displayed.
func shaped_text_set_preserve_invalid(shaped: RID, enabled: bool) -> void:
	pass;

#desc Sets extra spacing added between glyphs or lines in pixels.
func shaped_text_set_spacing(shaped: RID, spacing: int, value: int) -> void:
	pass;

#desc Shapes buffer if it's not shaped. Returns [code]true[/code] if the string is shaped successfully.
#desc [b]Note:[/b] It is not necessary to call this function manually, buffer will be shaped automatically as soon as any of its output data is requested.
func shaped_text_shape(shaped: RID) -> bool:
	pass;

#desc Returns text glyphs in the logical order.
func shaped_text_sort_logical(shaped: RID) -> Dictionary[]:
	pass;

#desc Returns text buffer for the substring of the text in the [param shaped] text buffer (including inline objects).
func shaped_text_substr(shaped: RID, start: int, length: int) -> RID:
	pass;

#desc Aligns shaped text to the given tab-stops.
func shaped_text_tab_align(shaped: RID, tab_stops: PackedFloat32Array) -> float:
	pass;

#desc Returns [code]true[/code] if [param string] is likely to be an attempt at confusing the reader.
#desc [b]Note:[/b] Always returns [code]false[/code] if the server does not support the [constant FEATURE_UNICODE_SECURITY] feature.
func spoof_check(string: String) -> bool:
	pass;

#desc Returns array of the word break character offsets.
func string_get_word_breaks(string: String, language: String) -> PackedInt32Array:
	pass;

#desc Returns the string converted to lowercase.
#desc [b]Note:[/b] Casing is locale dependent and context sensitive if server support [constant FEATURE_CONTEXT_SENSITIVE_CASE_CONVERSION] feature (supported by [TextServerAdvanced]).
#desc [b]Note:[/b] The result may be longer or shorter than the original.
func string_to_lower(string: String, language: String) -> String:
	pass;

#desc Returns the string converted to uppercase.
#desc [b]Note:[/b] Casing is locale dependent and context sensitive if server support [constant FEATURE_CONTEXT_SENSITIVE_CASE_CONVERSION] feature (supported by [TextServerAdvanced]).
#desc [b]Note:[/b] The result may be longer or shorter than the original.
func string_to_upper(string: String, language: String) -> String:
	pass;

#desc Strips diacritics from the string.
#desc [b]Note:[/b] The result may be longer or shorter than the original.
func strip_diacritics(string: String) -> String:
	pass;

#desc Converts OpenType tag to readable feature, variation, script or language name.
func tag_to_name(tag: int) -> String:
	pass;


