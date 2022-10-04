#brief Holds a line of text.
#desc Abstraction over [TextServer] for handling single line of text.
class_name TextLine


var alignment: int;

#desc Text writing direction.
var direction: int;

#desc Line alignment rules. For more info see [TextServer].
var flags: int;

#desc Text orientation.
var orientation: int;

#desc If set to [code]true[/code] text will display control characters.
var preserve_control: bool;

#desc If set to [code]true[/code] text will display invalid characters.
var preserve_invalid: bool;

#desc Sets the clipping behavior when the text exceeds the text line's set width. See [enum TextServer.OverrunBehavior] for a description of all modes.
var text_overrun_behavior: int;

#desc Text line width.
var width: float;



#desc Adds inline object to the text buffer, [param key] must be unique. In the text, object is represented as [param length] object replacement characters.
func add_object(key: Variant, size: Vector2, inline_align: int, length: int) -> bool:
	pass;

#desc Adds text span and font to draw it.
func add_string(text: String, font: Font, font_size: int, language: String, meta: Variant) -> bool:
	pass;

#desc Clears text line (removes text and inline objects).
func clear() -> void:
	pass;

#desc Draw text into a canvas item at a given position, with [param color]. [param pos] specifies the top left corner of the bounding box.
func draw(canvas: RID, pos: Vector2, color: Color) -> void:
	pass;

#desc Draw text into a canvas item at a given position, with [param color]. [param pos] specifies the top left corner of the bounding box.
func draw_outline(canvas: RID, pos: Vector2, outline_size: int, color: Color) -> void:
	pass;

#desc Returns the text ascent (number of pixels above the baseline for horizontal layout or to the left of baseline for vertical).
func get_line_ascent() -> float:
	pass;

#desc Returns the text descent (number of pixels below the baseline for horizontal layout or to the right of baseline for vertical).
func get_line_descent() -> float:
	pass;

#desc Returns pixel offset of the underline below the baseline.
func get_line_underline_position() -> float:
	pass;

#desc Returns thickness of the underline.
func get_line_underline_thickness() -> float:
	pass;

#desc Returns width (for horizontal layout) or height (for vertical) of the text.
func get_line_width() -> float:
	pass;

#desc Returns bounding rectangle of the inline object.
func get_object_rect(key: Variant) -> Rect2:
	pass;

#desc Returns array of inline objects.
func get_objects() -> Array:
	pass;

#desc Returns TextServer buffer RID.
func get_rid() -> RID:
	pass;

#desc Returns size of the bounding box of the text.
func get_size() -> Vector2:
	pass;

#desc Returns caret character offset at the specified pixel offset at the baseline. This function always returns a valid position.
func hit_test(coords: float) -> int:
	pass;

#desc Sets new size and alignment of embedded object.
func resize_object(key: Variant, size: Vector2, inline_align: int) -> bool:
	pass;

#desc Overrides BiDi for the structured text.
#desc Override ranges should cover full source text without overlaps. BiDi algorithm will be used on each range separately.
func set_bidi_override(override: Array) -> void:
	pass;

#desc Aligns text to the given tab-stops.
func tab_align(tab_stops: PackedFloat32Array) -> void:
	pass;


