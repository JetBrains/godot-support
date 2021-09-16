extends SyntaxHighlighter
class_name CodeHighlighter

var color_regions: Dictionary;
var function_color: Color;
var keyword_colors: Dictionary;
var member_keyword_colors: Dictionary;
var member_variable_color: Color;
var number_color: Color;
var symbol_color: Color;

func add_color_region(start_key: String, end_key: String, color: Color, line_only: bool) -> void:
    pass;
func add_keyword_color(keyword: String, color: Color) -> void:
    pass;
func add_member_keyword_color(member_keyword: String, color: Color) -> void:
    pass;
func clear_color_regions() -> void:
    pass;
func clear_keyword_colors() -> void:
    pass;
func clear_member_keyword_colors() -> void:
    pass;
func get_keyword_color(keyword: String) -> Color:
    pass;
func get_member_keyword_color(member_keyword: String) -> Color:
    pass;
func has_color_region(start_key: String) -> bool:
    pass;
func has_keyword_color(keyword: String) -> bool:
    pass;
func has_member_keyword_color(member_keyword: String) -> bool:
    pass;
func remove_color_region(start_key: String) -> void:
    pass;
func remove_keyword_color(keyword: String) -> void:
    pass;
func remove_member_keyword_color(member_keyword: String) -> void:
    pass;
