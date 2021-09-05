extends SyntaxHighlighter
class_name CodeHighlighter


var color_regions: Dictionary setget set_color_regions, get_color_regions;
var function_color: Color setget set_function_color, get_function_color;
var keyword_colors: Dictionary setget set_keyword_colors, get_keyword_colors;
var member_keyword_colors: Dictionary setget set_member_keyword_colors, get_member_keyword_colors;
var member_variable_color: Color setget set_member_variable_color, get_member_variable_color;
var number_color: Color setget set_number_color, get_number_color;
var symbol_color: Color setget set_symbol_color, get_symbol_color;

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

