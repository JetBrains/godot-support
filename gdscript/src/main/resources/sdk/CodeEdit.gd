extends TextEdit
#brief Multiline text control intended for editing code.
#desc CodeEdit is a specialised [TextEdit] designed for editing plain text code files. It contains a bunch of features commonly found in code editors such as line numbers, line folding, code completion, indent management and string / comment management.
#desc [b]Note:[/b] By default [CodeEdit] always use left-to-right text direction to correctly display source code.
class_name CodeEdit

#desc Marks the option as a class.
const KIND_CLASS = 0;

#desc Marks the option as a function.
const KIND_FUNCTION = 1;

#desc Marks the option as a Godot signal.
const KIND_SIGNAL = 2;

#desc Marks the option as a variable.
const KIND_VARIABLE = 3;

#desc Marks the option as a member.
const KIND_MEMBER = 4;

#desc Marks the option as a enum entry.
const KIND_ENUM = 5;

#desc Marks the option as a constant.
const KIND_CONSTANT = 6;

#desc Marks the option as a Godot node path.
const KIND_NODE_PATH = 7;

#desc Marks the option as a file path.
const KIND_FILE_PATH = 8;

#desc Marks the option as unclassified or plain text.
const KIND_PLAIN_TEXT = 9;


#desc Sets whether brace pairs should be autocompleted.
var auto_brace_completion_enabled: bool;

#desc Highlight mismatching brace pairs.
var auto_brace_completion_highlight_matching: bool;

#desc Sets the brace pairs to be autocompleted.
var auto_brace_completion_pairs: Dictionary;

#desc Sets whether code completion is allowed.
var code_completion_enabled: bool;

#desc Sets prefixes that will trigger code completion.
var code_completion_prefixes: String[];

#desc Sets the comment delimiters. All existing comment delimiters will be removed.
var delimiter_comments: String[];

#desc Sets the string delimiters. All existing string delimiters will be removed.
var delimiter_strings: String[];

#desc Sets if bookmarked should be drawn in the gutter. This gutter is shared with breakpoints and executing lines.
var gutters_draw_bookmarks: bool;

#desc Sets if breakpoints should be drawn in the gutter. This gutter is shared with bookmarks and executing lines.
var gutters_draw_breakpoints_gutter: bool;

#desc Sets if executing lines should be marked in the gutter. This gutter is shared with breakpoints and bookmarks lines.
var gutters_draw_executing_lines: bool;

#desc Sets if foldable lines icons should be drawn in the gutter.
var gutters_draw_fold_gutter: bool;

#desc Sets if line numbers should be drawn in the gutter.
var gutters_draw_line_numbers: bool;

#desc Sets if line numbers drawn in the gutter are zero padded.
var gutters_zero_pad_line_numbers: bool;

#desc Sets whether automatic indent are enabled, this will add an extra indent if a prefix or brace is found.
var indent_automatic: bool;

#desc Prefixes to trigger an automatic indent.
var indent_automatic_prefixes: String[];

#desc Size of tabs, if [code]indent_use_spaces[/code] is enabled the number of spaces to use.
var indent_size: int;

#desc Use spaces instead of tabs for indentation.
var indent_use_spaces: bool;

var layout_direction: int;

#desc Sets whether line folding is allowed.
var line_folding: bool;

#desc Draws vertical lines at the provided columns. The first entry is considered a main hard guideline and is draw more prominently
var line_length_guidelines: int[];

#desc Set when a validated word from [signal symbol_validate] is clicked, the [signal symbol_lookup] should be emitted.
var symbol_lookup_on_click: bool;

var text_direction: int;



#desc Override this method to define how the selected entry should be inserted. If [param replace] is true, any existing text should be replaced.
func _confirm_code_completion(replace: bool) -> void:
	pass;

#desc Override this method to define what items in [param candidates] should be displayed.
#desc Both [param candidates] and the return is a [Array] of [Dictionary], see [method get_code_completion_option] for [Dictionary] content.
func _filter_code_completion_candidates(candidates: Dictionary[]) -> Array[Dictionary]:
	pass;

#desc Override this method to define what happens when the user requests code completion. If [param force] is true, any checks should be bypassed.
func _request_code_completion(force: bool) -> void:
	pass;

#desc Adds a brace pair.
#desc Both the start and end keys must be symbols. Only the start key has to be unique.
func add_auto_brace_completion_pair(start_key: String, end_key: String) -> void:
	pass;

#desc Submits an item to the queue of potential candidates for the autocomplete menu. Call [method update_code_completion_options] to update the list.
#desc [b]Note:[/b] This list will replace all current candidates.
func add_code_completion_option(type: int, display_text: String, insert_text: String, text_color: Color, icon: Resource, value: Variant) -> void:
	pass;

#desc Adds a comment delimiter.
#desc Both the start and end keys must be symbols. Only the start key has to be unique.
#desc Line only denotes if the region should continue until the end of the line or carry over on to the next line. If the end key is blank this is automatically set to [code]true[/code].
func add_comment_delimiter(start_key: String, end_key: String, line_only: bool) -> void:
	pass;

#desc Adds a string delimiter.
#desc Both the start and end keys must be symbols. Only the start key has to be unique.
#desc Line only denotes if the region should continue until the end of the line or carry over on to the next line. If the end key is blank this is automatically set to [code]true[/code].
func add_string_delimiter(start_key: String, end_key: String, line_only: bool) -> void:
	pass;

#desc Returns if the given line is foldable, that is, it has indented lines right below it or a comment / string block.
func can_fold_line(line: int) -> bool:
	pass;

#desc Cancels the autocomplete menu.
func cancel_code_completion() -> void:
	pass;

#desc Clears all bookmarked lines.
func clear_bookmarked_lines() -> void:
	pass;

#desc Clears all breakpointed lines.
func clear_breakpointed_lines() -> void:
	pass;

#desc Removes all comment delimiters.
func clear_comment_delimiters() -> void:
	pass;

#desc Clears all executed lines.
func clear_executing_lines() -> void:
	pass;

#desc Removes all string delimiters.
func clear_string_delimiters() -> void:
	pass;

#desc Inserts the selected entry into the text. If [param replace] is true, any existing text is replaced rather then merged.
func confirm_code_completion(replace: bool) -> void:
	pass;

#desc Perform an indent as if the user activated the "ui_text_indent" action.
func do_indent() -> void:
	pass;

#desc Perform an unindent as if the user activated the "ui_text_unindent" action.
func do_unindent() -> void:
	pass;

#desc Folds all lines that are possible to be folded (see [method can_fold_line]).
func fold_all_lines() -> void:
	pass;

#desc Folds the given line, if possible (see [method can_fold_line]).
func fold_line(line: int) -> void:
	pass;

#desc Gets the matching auto brace close key for [param open_key].
func get_auto_brace_completion_close_key(open_key: String) -> String:
	pass;

#desc Gets all bookmarked lines.
func get_bookmarked_lines() -> PackedInt32Array:
	pass;

#desc Gets all breakpointed lines.
func get_breakpointed_lines() -> PackedInt32Array:
	pass;

#desc Gets the completion option at [param index]. The return [Dictionary] has the following key-values:
#desc [code]kind[/code]: [enum CodeCompletionKind]
#desc [code]display_text[/code]: Text that is shown on the autocomplete menu.
#desc [code]insert_text[/code]: Text that is to be inserted when this item is selected.
#desc [code]font_color[/code]: Color of the text on the autocomplete menu.
#desc [code]icon[/code]: Icon to draw on the autocomplete menu.
#desc [code]default_value[/code]: Value of the symbol.
func get_code_completion_option(index: int) -> Dictionary:
	pass;

#desc Gets all completion options, see [method get_code_completion_option] for return content.
func get_code_completion_options() -> Array[Dictionary]:
	pass;

#desc Gets the index of the current selected completion option.
func get_code_completion_selected_index() -> int:
	pass;

#desc Gets the end key for a string or comment region index.
func get_delimiter_end_key(delimiter_index: int) -> String:
	pass;

#desc If [param line] [param column] is in a string or comment, returns the end position of the region. If not or no end could be found, both [Vector2] values will be [code]-1[/code].
func get_delimiter_end_position(line: int, column: int) -> Vector2:
	pass;

#desc Gets the start key for a string or comment region index.
func get_delimiter_start_key(delimiter_index: int) -> String:
	pass;

#desc If [param line] [param column] is in a string or comment, returns the start position of the region. If not or no start could be found, both [Vector2] values will be [code]-1[/code].
func get_delimiter_start_position(line: int, column: int) -> Vector2:
	pass;

#desc Gets all executing lines.
func get_executing_lines() -> PackedInt32Array:
	pass;

#desc Returns all lines that are current folded.
func get_folded_lines() -> Array[int]:
	pass;

#desc Returns the full text with char [code]0xFFFF[/code] at the caret location.
func get_text_for_code_completion() -> String:
	pass;

#desc Returns the full text with char [code]0xFFFF[/code] at the cursor location.
func get_text_for_symbol_lookup() -> String:
	pass;

#desc Returns [code]true[/code] if close key [param close_key] exists.
func has_auto_brace_completion_close_key(close_key: String) -> bool:
	pass;

#desc Returns [code]true[/code] if open key [param open_key] exists.
func has_auto_brace_completion_open_key(open_key: String) -> bool:
	pass;

#desc Returns [code]true[/code] if comment [param start_key] exists.
func has_comment_delimiter(start_key: String) -> bool:
	pass;

#desc Returns [code]true[/code] if string [param start_key] exists.
func has_string_delimiter(start_key: String) -> bool:
	pass;

#desc Indents selected lines, or in the case of no selection the caret line by one.
func indent_lines() -> void:
	pass;

#desc Returns delimiter index if [param line] [param column] is in a comment. If [param column] is not provided, will return delimiter index if the entire [param line] is a comment. Otherwise [code]-1[/code].
func is_in_comment(line: int, column: int) -> int:
	pass;

#desc Returns the delimiter index if [param line] [param column] is in a string. If [param column] is not provided, will return the delimiter index if the entire [param line] is a string. Otherwise [code]-1[/code].
func is_in_string(line: int, column: int) -> int:
	pass;

#desc Returns whether the line at the specified index is bookmarked or not.
func is_line_bookmarked(line: int) -> bool:
	pass;

#desc Returns whether the line at the specified index is breakpointed or not.
func is_line_breakpointed(line: int) -> bool:
	pass;

#desc Returns whether the line at the specified index is marked as executing or not.
func is_line_executing(line: int) -> bool:
	pass;

#desc Returns whether the line at the specified index is folded or not.
func is_line_folded(line: int) -> bool:
	pass;

#desc Removes the comment delimiter with [param start_key].
func remove_comment_delimiter(start_key: String) -> void:
	pass;

#desc Removes the string delimiter with [param start_key].
func remove_string_delimiter(start_key: String) -> void:
	pass;

#desc Emits [signal code_completion_requested], if [param force] is true will bypass all checks. Otherwise will check that the caret is in a word or in front of a prefix. Will ignore the request if all current options are of type file path, node path or signal.
func request_code_completion(force: bool) -> void:
	pass;

#desc Sets the current selected completion option.
func set_code_completion_selected_index(index: int) -> void:
	pass;

#desc Sets the code hint text. Pass an empty string to clear.
func set_code_hint(code_hint: String) -> void:
	pass;

#desc Sets if the code hint should draw below the text.
func set_code_hint_draw_below(draw_below: bool) -> void:
	pass;

#desc Sets the line as bookmarked.
func set_line_as_bookmarked(line: int, bookmarked: bool) -> void:
	pass;

#desc Sets the line as breakpointed.
func set_line_as_breakpoint(line: int, breakpointed: bool) -> void:
	pass;

#desc Sets the line as executing.
func set_line_as_executing(line: int, executing: bool) -> void:
	pass;

#desc Sets the symbol emitted by [signal symbol_validate] as a valid lookup.
func set_symbol_lookup_word_as_valid(valid: bool) -> void:
	pass;

#desc Toggle the folding of the code block at the given line.
func toggle_foldable_line(line: int) -> void:
	pass;

#desc Unfolds all lines, folded or not.
func unfold_all_lines() -> void:
	pass;

#desc Unfolds all lines that were previously folded.
func unfold_line(line: int) -> void:
	pass;

#desc Unindents selected lines, or in the case of no selection the caret line by one.
func unindent_lines() -> void:
	pass;

#desc Submits all completion options added with [method add_code_completion_option]. Will try to force the autoccomplete menu to popup, if [param force] is [code]true[/code].
#desc [b]Note:[/b] This will replace all current candidates.
func update_code_completion_options(force: bool) -> void:
	pass;


