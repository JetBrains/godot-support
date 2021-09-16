extends TextEdit
class_name CodeEdit
const KIND_CLASS = 0;
const KIND_FUNCTION = 1;
const KIND_SIGNAL = 2;
const KIND_VARIABLE = 3;
const KIND_MEMBER = 4;
const KIND_ENUM = 5;
const KIND_CONSTANT = 6;
const KIND_NODE_PATH = 7;
const KIND_FILE_PATH = 8;
const KIND_PLAIN_TEXT = 9;

var code_completion_enabled: bool;
var code_completion_prefixes: Array[String];
var delimiter_comments: Array[String];
var delimiter_strings: Array[String];
var draw_bookmarks: bool;
var draw_breakpoints_gutter: bool;
var draw_executing_lines: bool;
var draw_fold_gutter: bool;
var draw_line_numbers: bool;
var indent_automatic: bool;
var indent_automatic_prefixes: Array[String];
var indent_size: int;
var indent_use_spaces: bool;
var layout_direction: int;
var line_folding: bool;
var structured_text_bidi_override_options: Array;
var text_direction: int;
var zero_pad_line_numbers: bool;

func _confirm_code_completion(replace: bool) -> void:
    pass;
func _filter_code_completion_candidates(candidates: Array) -> Array:
    pass;
func _request_code_completion(force: bool) -> void:
    pass;
func add_code_completion_option(type: int, display_text: String, insert_text: String, text_color: Color, icon: Resource, value: Variant) -> void:
    pass;
func add_comment_delimiter(start_key: String, end_key: String, line_only: bool) -> void:
    pass;
func add_string_delimiter(start_key: String, end_key: String, line_only: bool) -> void:
    pass;
func can_fold_line(line: int) -> bool:
    pass;
func cancel_code_completion() -> void:
    pass;
func clear_bookmarked_lines() -> void:
    pass;
func clear_breakpointed_lines() -> void:
    pass;
func clear_comment_delimiters() -> void:
    pass;
func clear_executing_lines() -> void:
    pass;
func clear_string_delimiters() -> void:
    pass;
func confirm_code_completion(replace: bool) -> void:
    pass;
func do_indent() -> void:
    pass;
func do_unindent() -> void:
    pass;
func fold_all_lines() -> void:
    pass;
func fold_line(line: int) -> void:
    pass;
func get_bookmarked_lines() -> Array:
    pass;
func get_breakpointed_lines() -> Array:
    pass;
func get_code_completion_option(index: int) -> Dictionary:
    pass;
func get_code_completion_options() -> Array[Dictionary]:
    pass;
func get_code_completion_selected_index() -> int:
    pass;
func get_delimiter_end_key(delimiter_index: int) -> String:
    pass;
func get_delimiter_end_position(line: int, column: int) -> Vector2:
    pass;
func get_delimiter_start_key(delimiter_index: int) -> String:
    pass;
func get_delimiter_start_position(line: int, column: int) -> Vector2:
    pass;
func get_executing_lines() -> Array:
    pass;
func get_folded_lines() -> Array[int]:
    pass;
func get_text_for_code_completion() -> String:
    pass;
func has_comment_delimiter(start_key: String) -> bool:
    pass;
func has_string_delimiter(start_key: String) -> bool:
    pass;
func indent_lines() -> void:
    pass;
func is_in_comment(line: int, column: int) -> int:
    pass;
func is_in_string(line: int, column: int) -> int:
    pass;
func is_line_bookmarked(line: int) -> bool:
    pass;
func is_line_breakpointed(line: int) -> bool:
    pass;
func is_line_executing(line: int) -> bool:
    pass;
func is_line_folded(line: int) -> bool:
    pass;
func remove_comment_delimiter(start_key: String) -> void:
    pass;
func remove_string_delimiter(start_key: String) -> void:
    pass;
func request_code_completion(force: bool) -> void:
    pass;
func set_code_completion_selected_index(index: int) -> void:
    pass;
func set_code_hint(code_hint: String) -> void:
    pass;
func set_code_hint_draw_below(draw_below: bool) -> void:
    pass;
func set_line_as_bookmarked(line: int, bookmarked: bool) -> void:
    pass;
func set_line_as_breakpoint(line: int, breakpointed: bool) -> void:
    pass;
func set_line_as_executing(line: int, executing: bool) -> void:
    pass;
func toggle_foldable_line(line: int) -> void:
    pass;
func unfold_all_lines() -> void:
    pass;
func unfold_line(line: int) -> void:
    pass;
func unindent_lines() -> void:
    pass;
func update_code_completion_options(force: bool) -> void:
    pass;
