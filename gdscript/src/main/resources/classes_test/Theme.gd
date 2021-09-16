extends Resource
class_name Theme
const DATA_TYPE_COLOR = 0;
const DATA_TYPE_CONSTANT = 1;
const DATA_TYPE_FONT = 2;
const DATA_TYPE_FONT_SIZE = 3;
const DATA_TYPE_ICON = 4;
const DATA_TYPE_STYLEBOX = 5;
const DATA_TYPE_MAX = 6;

var default_font: Font;
var default_font_size: int;

func clear() -> void:
    pass;
func clear_color(name: StringName, theme_type: StringName) -> void:
    pass;
func clear_constant(name: StringName, theme_type: StringName) -> void:
    pass;
func clear_font(name: StringName, theme_type: StringName) -> void:
    pass;
func clear_font_size(name: StringName, theme_type: StringName) -> void:
    pass;
func clear_icon(name: StringName, theme_type: StringName) -> void:
    pass;
func clear_stylebox(name: StringName, theme_type: StringName) -> void:
    pass;
func clear_theme_item(data_type: int, name: StringName, theme_type: StringName) -> void:
    pass;
func clear_type_variation(theme_type: StringName) -> void:
    pass;
func copy_default_theme() -> void:
    pass;
func copy_theme(other: Theme) -> void:
    pass;
func get_color(name: StringName, theme_type: StringName) -> Color:
    pass;
func get_color_list(theme_type: String) -> PackedStringArray:
    pass;
func get_color_type_list() -> PackedStringArray:
    pass;
func get_constant(name: StringName, theme_type: StringName) -> int:
    pass;
func get_constant_list(theme_type: String) -> PackedStringArray:
    pass;
func get_constant_type_list() -> PackedStringArray:
    pass;
func get_font(name: StringName, theme_type: StringName) -> Font:
    pass;
func get_font_list(theme_type: String) -> PackedStringArray:
    pass;
func get_font_size(name: StringName, theme_type: StringName) -> int:
    pass;
func get_font_size_list(theme_type: String) -> PackedStringArray:
    pass;
func get_font_size_type_list() -> PackedStringArray:
    pass;
func get_font_type_list() -> PackedStringArray:
    pass;
func get_icon(name: StringName, theme_type: StringName) -> Texture2D:
    pass;
func get_icon_list(theme_type: String) -> PackedStringArray:
    pass;
func get_icon_type_list() -> PackedStringArray:
    pass;
func get_stylebox(name: StringName, theme_type: StringName) -> StyleBox:
    pass;
func get_stylebox_list(theme_type: String) -> PackedStringArray:
    pass;
func get_stylebox_type_list() -> PackedStringArray:
    pass;
func get_theme_item(data_type: int, name: StringName, theme_type: StringName) -> Variant:
    pass;
func get_theme_item_list(data_type: int, theme_type: String) -> PackedStringArray:
    pass;
func get_theme_item_type_list(data_type: int) -> PackedStringArray:
    pass;
func get_type_list() -> PackedStringArray:
    pass;
func get_type_variation_base(theme_type: StringName) -> StringName:
    pass;
func get_type_variation_list(base_type: StringName) -> PackedStringArray:
    pass;
func has_color(name: StringName, theme_type: StringName) -> bool:
    pass;
func has_constant(name: StringName, theme_type: StringName) -> bool:
    pass;
func has_font(name: StringName, theme_type: StringName) -> bool:
    pass;
func has_font_size(name: StringName, theme_type: StringName) -> bool:
    pass;
func has_icon(name: StringName, theme_type: StringName) -> bool:
    pass;
func has_stylebox(name: StringName, theme_type: StringName) -> bool:
    pass;
func has_theme_item(data_type: int, name: StringName, theme_type: StringName) -> bool:
    pass;
func is_type_variation(theme_type: StringName, base_type: StringName) -> bool:
    pass;
func rename_color(old_name: StringName, name: StringName, theme_type: StringName) -> void:
    pass;
func rename_constant(old_name: StringName, name: StringName, theme_type: StringName) -> void:
    pass;
func rename_font(old_name: StringName, name: StringName, theme_type: StringName) -> void:
    pass;
func rename_font_size(old_name: StringName, name: StringName, theme_type: StringName) -> void:
    pass;
func rename_icon(old_name: StringName, name: StringName, theme_type: StringName) -> void:
    pass;
func rename_stylebox(old_name: StringName, name: StringName, theme_type: StringName) -> void:
    pass;
func rename_theme_item(data_type: int, old_name: StringName, name: StringName, theme_type: StringName) -> void:
    pass;
func set_color(name: StringName, theme_type: StringName, color: Color) -> void:
    pass;
func set_constant(name: StringName, theme_type: StringName, constant: int) -> void:
    pass;
func set_font(name: StringName, theme_type: StringName, font: Font) -> void:
    pass;
func set_font_size(name: StringName, theme_type: StringName, font_size: int) -> void:
    pass;
func set_icon(name: StringName, theme_type: StringName, texture: Texture2D) -> void:
    pass;
func set_stylebox(name: StringName, theme_type: StringName, texture: StyleBox) -> void:
    pass;
func set_theme_item(data_type: int, name: StringName, theme_type: StringName, value: Variant) -> void:
    pass;
func set_type_variation(theme_type: StringName, base_type: StringName) -> void:
    pass;
