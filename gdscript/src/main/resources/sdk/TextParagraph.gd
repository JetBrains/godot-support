extends RefCounted
#brief Holds a paragraph of text.
#desc Abstraction over [TextServer] for handling paragraph of text.
class_name TextParagraph


#desc Paragraph horizontal alignment.
var alignment: int;

#desc Line breaking rules. For more info see [TextServer].
var break_flags: int;

#desc Custom punctuation character list, used for word breaking. If set to empty string, server defaults are used.
var custom_punctuation: String;

#desc Text writing direction.
var direction: int;

#desc Line alignment rules. For more info see [TextServer].
var justification_flags: int;

#desc Limits the lines of text shown.
var max_lines_visible: int;

#desc Text orientation.
var orientation: int;

#desc If set to [code]true[/code] text will display control characters.
var preserve_control: bool;

#desc If set to [code]true[/code] text will display invalid characters.
var preserve_invalid: bool;

#desc Sets the clipping behavior when the text exceeds the paragraph's set width. See [enum TextServer.OverrunBehavior] for a description of all modes.
var text_overrun_behavior: int;

#desc Paragraph width.
var width: float;



#desc Adds inline object to the text buffer, [param key] must be unique. In the text, object is represented as [param length] object replacement characters.
func add_object(key: Variant, size: Vector2, inline_align: int, length: int) -> bool:
	pass;

#desc Adds text span and font to draw it.
func add_string(text: String, font: Font, font_size: int, language: String, meta: Variant) -> bool:
	pass;

#desc Clears text paragraph (removes text and inline objects).
func clear() -> void:
	pass;

#desc Removes dropcap.
func clear_dropcap() -> void:
	pass;

#desc Draw all lines of the text and drop cap into a canvas item at a given position, with [param color]. [param pos] specifies the top left corner of the bounding box.
func draw(canvas: RID, pos: Vector2, color: Color, dc_color: Color) -> void:
	pass;

#desc Draw drop cap into a canvas item at a given position, with [param color]. [param pos] specifies the top left corner of the bounding box.
func draw_dropcap(canvas: RID, pos: Vector2, color: Color) -> void:
	pass;

#desc Draw drop cap outline into a canvas item at a given position, with [param color]. [param pos] specifies the top left corner of the bounding box.
func draw_dropcap_outline(canvas: RID, pos: Vector2, outline_size: int, color: Color) -> void:
	pass;

#desc Draw single line of text into a canvas item at a given position, with [param color]. [param pos] specifies the top left corner of the bounding box.
func draw_line(canvas: RID, pos: Vector2, line: int, color: Color) -> void:
	pass;

#desc Draw outline of the single line of text into a canvas item at a given position, with [param color]. [param pos] specifies the top left corner of the bounding box.
func draw_line_outline(canvas: RID, pos: Vector2, line: int, outline_size: int, color: Color) -> void:
	pass;

#desc Draw outlines of all lines of the text and drop cap into a canvas item at a given position, with [param color]. [param pos] specifies the top left corner of the bounding box.
func draw_outline(canvas: RID, pos: Vector2, outline_size: int, color: Color, dc_color: Color) -> void:
	pass;

#desc Returns number of lines used by dropcap.
func get_dropcap_lines() -> int:
	pass;

#desc Returns drop cap text buffer RID.
func get_dropcap_rid() -> RID:
	pass;

#desc Returns drop cap bounding box size.
func get_dropcap_size() -> Vector2:
	pass;

#desc Returns the text line ascent (number of pixels above the baseline for horizontal layout or to the left of baseline for vertical).
func get_line_ascent(line: int) -> float:
	pass;

#desc Returns number of lines in the paragraph.
func get_line_count() -> int:
	pass;

#desc Returns the text line descent (number of pixels below the baseline for horizontal layout or to the right of baseline for vertical).
func get_line_descent(line: int) -> float:
	pass;

#desc Returns bounding rectangle of the inline object.
func get_line_object_rect(line: int, key: Variant) -> Rect2:
	pass;

#desc Returns array of inline objects in the line.
func get_line_objects(line: int) -> Array:
	pass;

#desc Returns character range of the line.
func get_line_range(line: int) -> Vector2i:
	pass;

#desc Returns TextServer line buffer RID.
func get_line_rid(line: int) -> RID:
	pass;

#desc Returns size of the bounding box of the line of text.
func get_line_size(line: int) -> Vector2:
	pass;

#desc Returns pixel offset of the underline below the baseline.
func get_line_underline_position(line: int) -> float:
	pass;

#desc Returns thickness of the underline.
func get_line_underline_thickness(line: int) -> float:
	pass;

#desc Returns width (for horizontal layout) or height (for vertical) of the line of text.
func get_line_width(line: int) -> float:
	pass;

#desc Returns the size of the bounding box of the paragraph, without line breaks.
func get_non_wrapped_size() -> Vector2:
	pass;

#desc Returns TextServer full string buffer RID.
func get_rid() -> RID:
	pass;

#desc Returns the size of the bounding box of the paragraph.
func get_size() -> Vector2:
	pass;

#desc Returns caret character offset at the specified coordinates. This function always returns a valid position.
func hit_test(coords: Vector2) -> int:
	pass;

#desc Sets new size and alignment of embedded object.
func resize_object(key: Variant, size: Vector2, inline_align: int) -> bool:
	pass;

#desc Overrides BiDi for the structured text.
#desc Override ranges should cover full source text without overlaps. BiDi algorithm will be used on each range separately.
func set_bidi_override(override: Array) -> void:
	pass;

#desc Sets drop cap, overrides previously set drop cap. Drop cap (dropped capital) is a decorative element at the beginning of a paragraph that is larger than the rest of the text.
func set_dropcap(text: String, font: Font, font_size: int, dropcap_margins: Rect2, language: String) -> bool:
	pass;

#desc Aligns paragraph to the given tab-stops.
func tab_align(tab_stops: PackedFloat32Array) -> void:
	pass;


