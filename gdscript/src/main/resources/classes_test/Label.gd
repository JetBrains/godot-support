#brief Displays plain text in a line or wrapped inside a rectangle. For formatted text, use [RichTextLabel].
#desc Label displays plain text on the screen. It gives you control over the horizontal and vertical alignment and can wrap the text inside the node's bounding rectangle. It doesn't support bold, italics, or other formatting. For that, use [RichTextLabel] instead.
#desc [b]Note:[/b] Contrarily to most other [Control]s, Label's [member Control.mouse_filter] defaults to [constant Control.MOUSE_FILTER_IGNORE] (i.e. it doesn't react to mouse input events). This implies that a label won't display any configured [member Control.tooltip_text], unless you change its mouse filter.
class_name Label


#desc If set to something other than [constant TextServer.AUTOWRAP_OFF], the text gets wrapped inside the node's bounding rectangle. If you resize the node, it will change its height automatically to show all the text. To see how each mode behaves, see [enum TextServer.AutowrapMode].
var autowrap_mode: int;

#desc If [code]true[/code], the Label only shows the text that fits inside its bounding rectangle and will clip text horizontally.
var clip_text: bool;

#desc Controls the text's horizontal alignment. Supports left, center, right, and fill, or justify. Set it to one of the [enum HorizontalAlignment] constants.
var horizontal_alignment: int;

var label_settings: LabelSettings;

#desc Language code used for line-breaking and text shaping algorithms, if left empty current locale is used instead.
var language: String;

#desc The node ignores the first [code]lines_skipped[/code] lines before it starts to display text.
var lines_skipped: int;

#desc Limits the lines of text the node shows on screen.
var max_lines_visible: int;

var mouse_filter: int;

var size_flags_vertical: int;

#desc Set BiDi algorithm override for the structured text.
var structured_text_bidi_override: int;

#desc Set additional options for BiDi override.
var structured_text_bidi_override_options: Array;

#desc The text to display on screen.
var text: String;

#desc Base text writing direction.
var text_direction: int;

#desc Sets the clipping behavior when the text exceeds the node's bounding rectangle. See [enum TextServer.OverrunBehavior] for a description of all modes.
var text_overrun_behavior: int;

#desc If [code]true[/code], all the text displays as UPPERCASE.
var uppercase: bool;

#desc Controls the text's vertical alignment. Supports top, center, bottom, and fill. Set it to one of the [enum VerticalAlignment] constants.
var vertical_alignment: int;

#desc The number of characters to display. If set to [code]-1[/code], all characters are displayed. This can be useful when animating the text appearing in a dialog box.
#desc [b]Note:[/b] Setting this property updates [member visible_ratio] accordingly.
var visible_characters: int;

#desc Sets the clipping behavior when [member visible_characters] or [member visible_ratio] is set. See [enum TextServer.VisibleCharactersBehavior] for more info.
var visible_characters_behavior: int;

#desc The fraction of characters to display, relative to the total number of characters (see [method get_total_character_count]). If set to [code]1.0[/code], all characters are displayed. If set to [code]0.5[/code], only half of the characters will be displayed. This can be useful when animating the text appearing in a dialog box.
#desc [b]Note:[/b] Setting this property updates [member visible_characters] accordingly.
var visible_ratio: float;



#desc Returns the number of lines of text the Label has.
func get_line_count() -> int:
	pass;

#desc Returns the height of the line [param line].
#desc If [param line] is set to [code]-1[/code], returns the biggest line height.
#desc If there are no lines, returns font size in pixels.
func get_line_height(line: int) -> int:
	pass;

#desc Returns the total number of printable characters in the text (excluding spaces and newlines).
func get_total_character_count() -> int:
	pass;

#desc Returns the number of lines shown. Useful if the [Label]'s height cannot currently display all lines.
func get_visible_line_count() -> int:
	pass;


