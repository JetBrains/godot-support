#brief Multiline text editing control.
#desc TextEdit is meant for editing large, multiline text. It also has facilities for editing code, such as syntax highlighting support and multiple levels of undo/redo.
#desc [b]Note:[/b] When holding down [kbd]Alt[/kbd], the vertical scroll wheel will scroll 5 times as fast as it would normally do. This also works in the Godot script editor.
class_name TextEdit

#desc Cuts (copies and clears) the selected text.
const MENU_CUT = 0;

#desc Copies the selected text.
const MENU_COPY = 1;

#desc Pastes the clipboard text over the selected text (or at the cursor's position).
const MENU_PASTE = 2;

#desc Erases the whole [TextEdit] text.
const MENU_CLEAR = 3;

#desc Selects the whole [TextEdit] text.
const MENU_SELECT_ALL = 4;

#desc Undoes the previous action.
const MENU_UNDO = 5;

#desc Redoes the previous action.
const MENU_REDO = 6;

#desc Sets text direction to inherited.
const MENU_DIR_INHERITED = 7;

#desc Sets text direction to automatic.
const MENU_DIR_AUTO = 8;

#desc Sets text direction to left-to-right.
const MENU_DIR_LTR = 9;

#desc Sets text direction to right-to-left.
const MENU_DIR_RTL = 10;

#desc Toggles control character display.
const MENU_DISPLAY_UCC = 11;

#desc Inserts left-to-right mark (LRM) character.
const MENU_INSERT_LRM = 12;

#desc Inserts right-to-left mark (RLM) character.
const MENU_INSERT_RLM = 13;

#desc Inserts start of left-to-right embedding (LRE) character.
const MENU_INSERT_LRE = 14;

#desc Inserts start of right-to-left embedding (RLE) character.
const MENU_INSERT_RLE = 15;

#desc Inserts start of left-to-right override (LRO) character.
const MENU_INSERT_LRO = 16;

#desc Inserts start of right-to-left override (RLO) character.
const MENU_INSERT_RLO = 17;

#desc Inserts pop direction formatting (PDF) character.
const MENU_INSERT_PDF = 18;

#desc Inserts Arabic letter mark (ALM) character.
const MENU_INSERT_ALM = 19;

#desc Inserts left-to-right isolate (LRI) character.
const MENU_INSERT_LRI = 20;

#desc Inserts right-to-left isolate (RLI) character.
const MENU_INSERT_RLI = 21;

#desc Inserts first strong isolate (FSI) character.
const MENU_INSERT_FSI = 22;

#desc Inserts pop direction isolate (PDI) character.
const MENU_INSERT_PDI = 23;

#desc Inserts zero width joiner (ZWJ) character.
const MENU_INSERT_ZWJ = 24;

#desc Inserts zero width non-joiner (ZWNJ) character.
const MENU_INSERT_ZWNJ = 25;

#desc Inserts word joiner (WJ) character.
const MENU_INSERT_WJ = 26;

#desc Inserts soft hyphen (SHY) character.
const MENU_INSERT_SHY = 27;

#desc Represents the size of the [enum MenuItems] enum.
const MENU_MAX = 28;

#desc Match case when searching.
const SEARCH_MATCH_CASE = 1;

#desc Match whole words when searching.
const SEARCH_WHOLE_WORDS = 2;

#desc Search from end to beginning.
const SEARCH_BACKWARDS = 4;

#desc Vertical line caret.
const CARET_TYPE_LINE = 0;

#desc Block caret.
const CARET_TYPE_BLOCK = 1;

#desc Not selecting.
const SELECTION_MODE_NONE = 0;

#desc Select as if [code]shift[/code] is pressed.
const SELECTION_MODE_SHIFT = 1;

#desc Select single characters as if the user single clicked.
const SELECTION_MODE_POINTER = 2;

#desc Select whole words as if the user double clicked.
const SELECTION_MODE_WORD = 3;

#desc Select whole lines as if the user tripped clicked.
const SELECTION_MODE_LINE = 4;

#desc Line wrapping is disabled.
const LINE_WRAPPING_NONE = 0;

#desc Line wrapping occurs at the control boundary, beyond what would normally be visible.
const LINE_WRAPPING_BOUNDARY = 1;

#desc Draw a string.
const GUTTER_TYPE_STRING = 0;

#desc Draw an icon.
const GUTTER_TYPE_ICON = 1;

#desc Custom draw.
const GUTTER_TYPE_CUSTOM = 2;


#desc Sets if the caret should blink.
var caret_blink: bool;

#desc Duration (in seconds) of a caret's blinking cycle.
var caret_blink_interval: float;

#desc Allow moving caret, selecting and removing the individual composite character components.
#desc [b]Note:[/b] [kbd]Backspace[/kbd] is always removing individual composite character components.
var caret_mid_grapheme: bool;

#desc If [code]true[/code], a right-click moves the caret at the mouse position before displaying the context menu.
#desc If [code]false[/code], the context menu disregards mouse location.
var caret_move_on_right_click: bool;

#desc Set the type of caret to draw.
var caret_type: int;

#desc If [code]true[/code], a right-click displays the context menu.
var context_menu_enabled: bool;

#desc If [code]true[/code], the selected text will be deselected when focus is lost.
var deselect_on_focus_loss_enabled: bool;

#desc If [code]true[/code], allow drag and drop of selected text.
var drag_and_drop_selection_enabled: bool;

#desc If [code]true[/code], control characters are displayed.
var draw_control_chars: bool;

#desc If [code]true[/code], the "space" character will have a visible representation.
var draw_spaces: bool;

#desc If [code]true[/code], the "tab" character will have a visible representation.
var draw_tabs: bool;

#desc If [code]false[/code], existing text cannot be modified and new text cannot be added.
var editable: bool;

var focus_mode: int;

#desc If [code]true[/code], all occurrences of the selected text will be highlighted.
var highlight_all_occurrences: bool;

#desc If [code]true[/code], the line containing the cursor is highlighted.
var highlight_current_line: bool;

#desc Language code used for line-breaking and text shaping algorithms, if left empty current locale is used instead.
var language: String;

#desc If [code]false[/code], using middle mouse button to paste clipboard will be disabled.
#desc [b]Note:[/b] This method is only implemented on Linux.
var middle_mouse_paste_enabled: bool;

#desc If [code]true[/code], a minimap is shown, providing an outline of your source code.
var minimap_draw: bool;

#desc The width, in pixels, of the minimap.
var minimap_width: int;

var mouse_default_cursor_shape: int;

#desc If [code]true[/code], custom [code]font_selected_color[/code] will be used for selected text.
var override_selected_font_color: bool;

#desc Text shown when the [TextEdit] is empty. It is [b]not[/b] the [TextEdit]'s default value (see [member text]).
var placeholder_text: String;

#desc If [code]true[/code], [TextEdit] will disable vertical scroll and fit minimum height to the number of visible lines.
var scroll_fit_content_height: bool;

#desc If there is a horizontal scrollbar, this determines the current horizontal scroll value in pixels.
var scroll_horizontal: int;

#desc Allow scrolling past the last line into "virtual" space.
var scroll_past_end_of_file: bool;

#desc Scroll smoothly over the text rather then jumping to the next location.
var scroll_smooth: bool;

#desc Sets the scroll speed with the minimap or when [member scroll_smooth] is enabled.
var scroll_v_scroll_speed: float;

#desc If there is a vertical scrollbar, this determines the current vertical scroll value in line numbers, starting at 0 for the top line.
var scroll_vertical: float;

#desc If [code]true[/code], text can be selected.
#desc If [code]false[/code], text can not be selected by the user or by the [method select] or [method select_all] methods.
var selecting_enabled: bool;

#desc If [code]true[/code], shortcut keys for context menu items are enabled, even if the context menu is disabled.
var shortcut_keys_enabled: bool;

#desc Set BiDi algorithm override for the structured text.
var structured_text_bidi_override: int;

#desc Set additional options for BiDi override.
var structured_text_bidi_override_options: Array;

#desc Sets the [SyntaxHighlighter] to use.
var syntax_highlighter: SyntaxHighlighter;

#desc String value of the [TextEdit].
var text: String;

#desc Base text writing direction.
var text_direction: int;

#desc If [code]true[/code], the native virtual keyboard is shown when focused on platforms that support it.
var virtual_keyboard_enabled: bool;

#desc Sets the line wrapping mode to use.
var wrap_mode: int;



#desc Override this method to define what happens when the user presses the backspace key.
virtual func _backspace() -> void:
	pass;

#desc Override this method to define what happens when the user performs a copy operation.
virtual func _copy() -> void:
	pass;

#desc Override this method to define what happens when the user performs a cut operation.
virtual func _cut() -> void:
	pass;

#desc Override this method to define what happens when the user types in the provided key [param unicode_char].
virtual func _handle_unicode_input() -> void:
	pass;

#desc Override this method to define what happens when the user performs a paste operation.
virtual func _paste() -> void:
	pass;

#desc Override this method to define what happens when the user performs a paste operation with middle mouse button.
#desc [b]Note:[/b] This method is only implemented on Linux.
virtual func _paste_primary_clipboard() -> void:
	pass;

#desc Register a new gutter to this [TextEdit]. Use [param at] to have a specific gutter order. A value of [code]-1[/code] appends the gutter to the right.
func add_gutter() -> void:
	pass;

#desc Adjust the viewport so the caret is visible.
func adjust_viewport_to_caret() -> void:
	pass;

#desc Called when the user presses the backspace key. Can be overridden with [method _backspace].
func backspace() -> void:
	pass;

#desc Starts a multipart edit. All edits will be treated as one action until [method end_complex_operation] is called.
func begin_complex_operation() -> void:
	pass;

#desc Centers the viewport on the line the editing caret is at. This also resets the [member scroll_horizontal] value to [code]0[/code].
func center_viewport_to_caret() -> void:
	pass;

#desc Performs a full reset of [TextEdit], including undo history.
func clear() -> void:
	pass;

#desc Clears the undo history.
func clear_undo_history() -> void:
	pass;

#desc Copies the current text selection. Can be overridden with [method _copy].
func copy() -> void:
	pass;

#desc Cut's the current selection. Can be overridden with [method _cut].
func cut() -> void:
	pass;

#desc Deletes the selected text.
func delete_selection() -> void:
	pass;

#desc Deselects the current selection.
func deselect() -> void:
	pass;

#desc Ends a multipart edit, started with [method begin_complex_operation]. If called outside a complex operation, the current operation is pushed onto the undo/redo stack.
func end_complex_operation() -> void:
	pass;

#desc Returns the column the editing caret is at.
func get_caret_column() -> int:
	pass;

#desc Returns the caret pixel draw position.
func get_caret_draw_pos() -> Vector2:
	pass;

#desc Returns the line the editing caret is on.
func get_caret_line() -> int:
	pass;

#desc Returns the wrap index the editing caret is on.
func get_caret_wrap_index() -> int:
	pass;

#desc Returns the first column containing a non-whitespace character.
func get_first_non_whitespace_column() -> int:
	pass;

#desc Returns the first visible line.
func get_first_visible_line() -> int:
	pass;

#desc Returns the number of gutters registered.
func get_gutter_count() -> int:
	pass;

#desc Returns the name of the gutter at the given index.
func get_gutter_name() -> String:
	pass;

#desc Returns the type of the gutter at the given index.
func get_gutter_type() -> int:
	pass;

#desc Returns the width of the gutter at the given index.
func get_gutter_width() -> int:
	pass;

#desc Returns the number of spaces and [code]tab * tab_size[/code] before the first char.
func get_indent_level() -> int:
	pass;

#desc Returns the last visible line. Use [method get_last_full_visible_line_wrap_index] for the wrap index.
func get_last_full_visible_line() -> int:
	pass;

#desc Returns the last visible wrap index of the last visible line.
func get_last_full_visible_line_wrap_index() -> int:
	pass;

#desc Returns the last unhidden line in the entire [TextEdit].
func get_last_unhidden_line() -> int:
	pass;

#desc Returns the text of a specific line.
func get_line() -> String:
	pass;

#desc Returns the current background color of the line. [code]Color(0, 0, 0, 0)[/code] is returned if no color is set.
func get_line_background_color() -> Color:
	pass;

#desc Returns the line and column at the given position. In the returned vector, [code]x[/code] is the column, [code]y[/code] is the line. If [param allow_out_of_bounds] is [code]false[/code] and the position is not over the text, both vector values will be set to [code]-1[/code].
func get_line_column_at_pos(position: Vector2i, allow_out_of_bounds: bool) -> Vector2i:
	pass;

#desc Returns the number of lines in the text.
func get_line_count() -> int:
	pass;

#desc Returns the icon currently in [param gutter] at [param line].
func get_line_gutter_icon(line: int, gutter: int) -> Texture2D:
	pass;

#desc Returns the color currently in [param gutter] at [param line].
func get_line_gutter_item_color(line: int, gutter: int) -> Color:
	pass;

#desc Returns the metadata currently in [param gutter] at [param line].
func get_line_gutter_metadata(line: int, gutter: int) -> Variant:
	pass;

#desc Returns the text currently in [param gutter] at [param line].
func get_line_gutter_text(line: int, gutter: int) -> String:
	pass;

#desc Returns the height of a largest line.
func get_line_height() -> int:
	pass;

#desc Returns the width in pixels of the [param wrap_index] on [param line].
func get_line_width(line: int, wrap_index: int) -> int:
	pass;

#desc Returns the number of times the given line is wrapped.
func get_line_wrap_count() -> int:
	pass;

#desc Returns the wrap index of the given line column.
func get_line_wrap_index_at_column(line: int, column: int) -> int:
	pass;

#desc Returns an array of [String]s representing each wrapped index.
func get_line_wrapped_text() -> PackedStringArray:
	pass;

#desc Returns the local mouse position adjusted for the text direction.
func get_local_mouse_pos() -> Vector2:
	pass;

#desc Returns the [PopupMenu] of this [TextEdit]. By default, this menu is displayed when right-clicking on the [TextEdit].
#desc [b]Warning:[/b] This is a required internal node, removing and freeing it may cause a crash. If you wish to hide it or any of its children, use their [member Window.visible] property.
func get_menu() -> PopupMenu:
	pass;

#desc Returns the equivalent minimap line at [param position]
func get_minimap_line_at_pos() -> int:
	pass;

#desc Returns the number of lines that may be drawn on the minimap.
func get_minimap_visible_lines() -> int:
	pass;

#desc Similar to [method get_next_visible_line_offset_from], but takes into account the line wrap indexes. In the returned vector, [code]x[/code] is the line, [code]y[/code] is the wrap index.
func get_next_visible_line_index_offset_from(line: int, wrap_index: int, visible_amount: int) -> Vector2i:
	pass;

#desc Returns the count to the next visible line from [param line] to [code]line + visible_amount[/code]. Can also count backwards. For example if a [TextEdit] has 5 lines with lines 2 and 3 hidden, calling this with [code]line = 1, visible_amount = 1[/code] would return 3.
func get_next_visible_line_offset_from(line: int, visible_amount: int) -> int:
	pass;

#desc Returns the local position for the given [param line] and [param column]. If [code]x[/code] or [code]y[/code] of the returned vector equal [code]-1[/code], the position is outside of the viewable area of the control.
#desc [b]Note:[/b] The Y position corresponds to the bottom side of the line. Use [method get_rect_at_line_column] to get the top side position.
func get_pos_at_line_column(line: int, column: int) -> Vector2i:
	pass;

#desc Returns the local position and size for the grapheme at the given [param line] and [param column]. If [code]x[/code] or [code]y[/code] position of the returned rect equal [code]-1[/code], the position is outside of the viewable area of the control.
#desc [b]Note:[/b] The Y position of the returned rect corresponds to the top side of the line, unlike [method get_pos_at_line_column] which returns the bottom side.
func get_rect_at_line_column(line: int, column: int) -> Rect2i:
	pass;

#desc Returns the last tagged saved version from [method tag_saved_version]
func get_saved_version() -> int:
	pass;

#desc Returns the scroll position for [param wrap_index] of [param line].
func get_scroll_pos_for_line(line: int, wrap_index: int) -> float:
	pass;

#desc Returns the text inside the selection.
func get_selected_text() -> String:
	pass;

#desc Returns the original start column of the selection.
func get_selection_column() -> int:
	pass;

#desc Returns the selection begin column.
func get_selection_from_column() -> int:
	pass;

#desc Returns the selection begin line.
func get_selection_from_line() -> int:
	pass;

#desc Returns the original start line of the selection.
func get_selection_line() -> int:
	pass;

#desc Returns the current selection mode.
func get_selection_mode() -> int:
	pass;

#desc Returns the selection end column.
func get_selection_to_column() -> int:
	pass;

#desc Returns the selection end line.
func get_selection_to_line() -> int:
	pass;

#desc Returns the [TextEdit]'s' tab size.
func get_tab_size() -> int:
	pass;

#desc Returns the total width of all gutters and internal padding.
func get_total_gutter_width() -> int:
	pass;

#desc Returns the number of lines that may be drawn.
func get_total_visible_line_count() -> int:
	pass;

#desc Returns the current version of the [TextEdit]. The version is a count of recorded operations by the undo/redo history.
func get_version() -> int:
	pass;

#desc Returns the number of visible lines, including wrapped text.
func get_visible_line_count() -> int:
	pass;

#desc Returns the total number of visible + wrapped lines between the two lines.
func get_visible_line_count_in_range(from_line: int, to_line: int) -> int:
	pass;

#desc Returns the word at [param position].
func get_word_at_pos() -> String:
	pass;

#desc Returns a [String] text with the word under the caret's location.
func get_word_under_caret() -> String:
	pass;

#desc Returns if the user has IME text.
func has_ime_text() -> bool:
	pass;

#desc Returns [code]true[/code] if a "redo" action is available.
func has_redo() -> bool:
	pass;

#desc Returns [code]true[/code] if the user has selected text.
func has_selection() -> bool:
	pass;

#desc Returns [code]true[/code] if an "undo" action is available.
func has_undo() -> bool:
	pass;

#desc Inserts a new line with [param text] at [param line].
func insert_line_at(line: int, text: String) -> void:
	pass;

#desc Insert the specified text at the caret position.
func insert_text_at_caret() -> void:
	pass;

#desc Returns [code]true[/code] if the caret is visible on the screen.
func is_caret_visible() -> bool:
	pass;

#desc Returns [code]true[/code] if the user is dragging their mouse for scrolling or selecting.
func is_dragging_cursor() -> bool:
	pass;

#desc Returns whether the gutter is clickable.
func is_gutter_clickable() -> bool:
	pass;

#desc Returns whether the gutter is currently drawn.
func is_gutter_drawn() -> bool:
	pass;

#desc Returns whether the gutter is overwritable.
func is_gutter_overwritable() -> bool:
	pass;

#desc Returns whether the gutter on the given line is clickable.
func is_line_gutter_clickable(line: int, gutter: int) -> bool:
	pass;

#desc Returns if the given line is wrapped.
func is_line_wrapped() -> bool:
	pass;

#desc Returns whether the menu is visible. Use this instead of [code]get_menu().visible[/code] to improve performance (so the creation of the menu is avoided).
func is_menu_visible() -> bool:
	pass;

#desc Returns whether the mouse is over selection. If [param edges] is [code]true[/code], the edges are considered part of the selection.
func is_mouse_over_selection() -> bool:
	pass;

#desc Returns whether the user is in overtype mode.
func is_overtype_mode_enabled() -> bool:
	pass;

#desc Triggers a right-click menu action by the specified index. See [enum MenuItems] for a list of available indexes.
func menu_option() -> void:
	pass;

#desc Merge the gutters from [param from_line] into [param to_line]. Only overwritable gutters will be copied.
func merge_gutters(from_line: int, to_line: int) -> void:
	pass;

#desc Paste at the current location. Can be overridden with [method _paste].
func paste() -> void:
	pass;

#desc Perform redo operation.
func redo() -> void:
	pass;

#desc Removes the gutter from this [TextEdit].
func remove_gutter() -> void:
	pass;

#desc Removes text between the given positions.
#desc [b]Note:[/b] This does not adjust the caret or selection, which as a result it can end up in an invalid position.
func remove_text(from_line: int, from_column: int, to_line: int, to_column: int) -> void:
	pass;

#desc Perform a search inside the text. Search flags can be specified in the [enum SearchFlags] enum.
#desc In the returned vector, [code]x[/code] is the column, [code]y[/code] is the line. If no results are found, both are equal to [code]-1[/code].
#desc [codeblocks]
#desc [gdscript]
#desc var result = search("print", SEARCH_WHOLE_WORDS, 0, 0)
#desc if  result.x != -1:
#desc # Result found.
#desc var line_number = result.y
#desc var column_number = result.x
#desc [/gdscript]
#desc [csharp]
#desc Vector2i result = Search("print", (uint)TextEdit.SearchFlags.WholeWords, 0, 0);
#desc if (result.Length > 0)
#desc {
#desc // Result found.
#desc int lineNumber = result.y;
#desc int columnNumber = result.x;
#desc }
#desc [/csharp]
#desc [/codeblocks]
func search(text: String, flags: int, from_line: int, from_colum: int) -> Vector2i:
	pass;

#desc Perform selection, from line/column to line/column.
#desc If [member selecting_enabled] is [code]false[/code], no selection will occur.
func select(from_line: int, from_column: int, to_line: int, to_column: int) -> void:
	pass;

#desc Select all the text.
#desc If [member selecting_enabled] is [code]false[/code], no selection will occur.
func select_all() -> void:
	pass;

#desc Selects the word under the caret.
func select_word_under_caret() -> void:
	pass;

#desc Moves the caret to the specified [param column] index.
#desc If [param adjust_viewport] is [code]true[/code], the viewport will center at the caret position after the move occurs.
func set_caret_column(column: int, adjust_viewport: bool) -> void:
	pass;

#desc Moves the caret to the specified [param line] index.
#desc If [param adjust_viewport] is [code]true[/code], the viewport will center at the caret position after the move occurs.
#desc If [param can_be_hidden] is [code]true[/code], the specified [code]line[/code] can be hidden.
func set_caret_line(line: int, adjust_viewport: bool, can_be_hidden: bool, wrap_index: int) -> void:
	pass;

#desc Sets the gutter as clickable. This will change the mouse cursor to a pointing hand when hovering over the gutter.
func set_gutter_clickable(gutter: int, clickable: bool) -> void:
	pass;

#desc Set a custom draw method for the gutter. The callback method must take the following args: [code]line: int, gutter: int, Area: Rect2[/code].
func set_gutter_custom_draw(column: int, draw_callback: Callable) -> void:
	pass;

#desc Sets whether the gutter should be drawn.
func set_gutter_draw(gutter: int, draw: bool) -> void:
	pass;

#desc Sets the name of the gutter.
func set_gutter_name(gutter: int, name: String) -> void:
	pass;

#desc Sets the gutter to overwritable. See [method merge_gutters].
func set_gutter_overwritable(gutter: int, overwritable: bool) -> void:
	pass;

#desc Sets the type of gutter.
func set_gutter_type(gutter: int, type: int) -> void:
	pass;

#desc Set the width of the gutter.
func set_gutter_width(gutter: int, width: int) -> void:
	pass;

#desc Sets the text for a specific line.
func set_line(line: int, new_text: String) -> void:
	pass;

#desc Positions the [param wrap_index] of [param line] at the center of the viewport.
func set_line_as_center_visible(line: int, wrap_index: int) -> void:
	pass;

#desc Positions the [param wrap_index] of [param line] at the top of the viewport.
func set_line_as_first_visible(line: int, wrap_index: int) -> void:
	pass;

#desc Positions the [param wrap_index] of [param line] at the bottom of the viewport.
func set_line_as_last_visible(line: int, wrap_index: int) -> void:
	pass;

#desc Sets the current background color of the line. Set to [code]Color(0, 0, 0, 0)[/code] for no color.
func set_line_background_color(line: int, color: Color) -> void:
	pass;

#desc If [param clickable] is [code]true[/code], makes the [param gutter] on [param line] clickable. See [signal gutter_clicked].
func set_line_gutter_clickable(line: int, gutter: int, clickable: bool) -> void:
	pass;

#desc Sets the icon for [param gutter] on [param line] to [param icon].
func set_line_gutter_icon(line: int, gutter: int, icon: Texture2D) -> void:
	pass;

#desc Sets the color for [param gutter] on [param line] to [param color].
func set_line_gutter_item_color(line: int, gutter: int, color: Color) -> void:
	pass;

#desc Sets the metadata for [param gutter] on [param line] to [param metadata].
func set_line_gutter_metadata(line: int, gutter: int, metadata: Variant) -> void:
	pass;

#desc Sets the text for [param gutter] on [param line] to [param text].
func set_line_gutter_text(line: int, gutter: int, text: String) -> void:
	pass;

#desc If [code]true[/code], sets the user into overtype mode. When the user types in this mode, it will override existing text.
func set_overtype_mode_enabled() -> void:
	pass;

#desc Sets the search [param flags]. This is used with [method set_search_text] to highlight occurrences of the searched text. Search flags can be specified from the [enum SearchFlags] enum.
func set_search_flags() -> void:
	pass;

#desc Sets the search text. See [method set_search_flags].
func set_search_text() -> void:
	pass;

#desc Sets the current selection mode.
func set_selection_mode(mode: int, line: int, column: int) -> void:
	pass;

#desc Sets the tab size for the [TextEdit] to use.
func set_tab_size() -> void:
	pass;

#desc Provide custom tooltip text. The callback method must take the following args: [code]hovered_word: String[/code]
func set_tooltip_request_func() -> void:
	pass;

#desc Swaps the two lines.
func swap_lines(from_line: int, to_line: int) -> void:
	pass;

#desc Tag the current version as saved.
func tag_saved_version() -> void:
	pass;

#desc Perform undo operation.
func undo() -> void:
	pass;


