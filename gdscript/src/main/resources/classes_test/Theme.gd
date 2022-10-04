#brief Theme resource for styling/skinning [Control]s and [Window]s.
#desc A theme resource is used for styling/skinning [Control] and [Window] nodes. While individual controls can be styled using their local theme overrides (see [method Control.add_theme_color_override]), theme resources allow you to store and apply the same settings between all controls sharing the same type (e.g. style all [Button]s the same). One theme resource can be used for the entire project, but you can also set a separate theme resource to a branch of control nodes. A theme resources assigned to a control node applies to the control itself, as well as all of its direct and indirect children (as long as a chain of controls is uninterrupted).
#desc Use [member ProjectSettings.gui/theme/custom] to set up a project-scope theme that will be available to every control in your project.
#desc Use [member Control.theme] of any control node to set up a theme that will be available to that control and all of its direct and indirect children.
class_name Theme

#desc Theme's [Color] item type.
const DATA_TYPE_COLOR = 0;

#desc Theme's constant item type.
const DATA_TYPE_CONSTANT = 1;

#desc Theme's [Font] item type.
const DATA_TYPE_FONT = 2;

#desc Theme's font size item type.
const DATA_TYPE_FONT_SIZE = 3;

#desc Theme's icon [Texture2D] item type.
const DATA_TYPE_ICON = 4;

#desc Theme's [StyleBox] item type.
const DATA_TYPE_STYLEBOX = 5;

#desc Maximum value for the DataType enum.
const DATA_TYPE_MAX = 6;


#desc The default base scale factor of this theme resource. Used by some controls to scale their visual properties based on the global scale factor. If this value is set to [code]0.0[/code], the global scale factor is used (see [member ThemeDB.fallback_base_scale]).
#desc Use [method has_default_base_scale] to check if this value is valid.
var default_base_scale: float;

#desc The default font of this theme resource. Used as the default value when trying to fetch a font resource that doesn't exist in this theme or is in invalid state. If the default font is also missing or invalid, the engine fallback value is used (see [member ThemeDB.fallback_font]).
#desc Use [method has_default_font] to check if this value is valid.
var default_font: Font;

#desc The default font size of this theme resource. Used as the default value when trying to fetch a font size value that doesn't exist in this theme or is in invalid state. If the default font size is also missing or invalid, the engine fallback value is used (see [member ThemeDB.fallback_font_size]).
#desc Values below [code]0[/code] are invalid and can be used to unset the property. Use [method has_default_font_size] to check if this value is valid.
var default_font_size: int;



#desc Adds an empty theme type for every valid data type.
#desc [b]Note:[/b] Empty types are not saved with the theme. This method only exists to perform in-memory changes to the resource. Use available [code]set_*[/code] methods to add theme items.
func add_type() -> void:
	pass;

#desc Removes all the theme properties defined on the theme resource.
func clear() -> void:
	pass;

#desc Removes the [Color] property defined by [param name] and [param theme_type], if it exists.
#desc Fails if it doesn't exist. Use [method has_color] to check for existence.
func clear_color(name: StringName, theme_type: StringName) -> void:
	pass;

#desc Removes the constant property defined by [param name] and [param theme_type], if it exists.
#desc Fails if it doesn't exist. Use [method has_constant] to check for existence.
func clear_constant(name: StringName, theme_type: StringName) -> void:
	pass;

#desc Removes the [Font] property defined by [param name] and [param theme_type], if it exists.
#desc Fails if it doesn't exist. Use [method has_font] to check for existence.
func clear_font(name: StringName, theme_type: StringName) -> void:
	pass;

#desc Removes the font size property defined by [param name] and [param theme_type], if it exists.
#desc Fails if it doesn't exist. Use [method has_font_size] to check for existence.
func clear_font_size(name: StringName, theme_type: StringName) -> void:
	pass;

#desc Removes the icon property defined by [param name] and [param theme_type], if it exists.
#desc Fails if it doesn't exist. Use [method has_icon] to check for existence.
func clear_icon(name: StringName, theme_type: StringName) -> void:
	pass;

#desc Removes the [StyleBox] property defined by [param name] and [param theme_type], if it exists.
#desc Fails if it doesn't exist. Use [method has_stylebox] to check for existence.
func clear_stylebox(name: StringName, theme_type: StringName) -> void:
	pass;

#desc Removes the theme property of [param data_type] defined by [param name] and [param theme_type], if it exists.
#desc Fails if it doesn't exist. Use [method has_theme_item] to check for existence.
#desc [b]Note:[/b] This method is analogous to calling the corresponding data type specific method, but can be used for more generalized logic.
func clear_theme_item(data_type: int, name: StringName, theme_type: StringName) -> void:
	pass;

#desc Unmarks [param theme_type] as being a variation of another theme type. See [method set_type_variation].
func clear_type_variation() -> void:
	pass;

#desc Returns the [Color] property defined by [param name] and [param theme_type], if it exists.
#desc Returns the default color value if the property doesn't exist. Use [method has_color] to check for existence.
func get_color(name: StringName, theme_type: StringName) -> Color:
	pass;

#desc Returns a list of names for [Color] properties defined with [param theme_type]. Use [method get_color_type_list] to get a list of possible theme type names.
func get_color_list() -> PackedStringArray:
	pass;

#desc Returns a list of all unique theme type names for [Color] properties. Use [method get_type_list] to get a list of all unique theme types.
func get_color_type_list() -> PackedStringArray:
	pass;

#desc Returns the constant property defined by [param name] and [param theme_type], if it exists.
#desc Returns [code]0[/code] if the property doesn't exist. Use [method has_constant] to check for existence.
func get_constant(name: StringName, theme_type: StringName) -> int:
	pass;

#desc Returns a list of names for constant properties defined with [param theme_type]. Use [method get_constant_type_list] to get a list of possible theme type names.
func get_constant_list() -> PackedStringArray:
	pass;

#desc Returns a list of all unique theme type names for constant properties. Use [method get_type_list] to get a list of all unique theme types.
func get_constant_type_list() -> PackedStringArray:
	pass;

#desc Returns the [Font] property defined by [param name] and [param theme_type], if it exists.
#desc Returns the default theme font if the property doesn't exist and the default theme font is set up (see [member default_font]). Use [method has_font] to check for existence of the property and [method has_default_font] to check for existence of the default theme font.
#desc Returns the engine fallback font value, if neither exist (see [member ThemeDB.fallback_font]).
func get_font(name: StringName, theme_type: StringName) -> Font:
	pass;

#desc Returns a list of names for [Font] properties defined with [param theme_type]. Use [method get_font_type_list] to get a list of possible theme type names.
func get_font_list() -> PackedStringArray:
	pass;

#desc Returns the font size property defined by [param name] and [param theme_type], if it exists.
#desc Returns the default theme font size if the property doesn't exist and the default theme font size is set up (see [member default_font_size]). Use [method has_font_size] to check for existence of the property and [method has_default_font_size] to check for existence of the default theme font.
#desc Returns the engine fallback font size value, if neither exist (see [member ThemeDB.fallback_font_size]).
func get_font_size(name: StringName, theme_type: StringName) -> int:
	pass;

#desc Returns a list of names for font size properties defined with [param theme_type]. Use [method get_font_size_type_list] to get a list of possible theme type names.
func get_font_size_list() -> PackedStringArray:
	pass;

#desc Returns a list of all unique theme type names for font size properties. Use [method get_type_list] to get a list of all unique theme types.
func get_font_size_type_list() -> PackedStringArray:
	pass;

#desc Returns a list of all unique theme type names for [Font] properties. Use [method get_type_list] to get a list of all unique theme types.
func get_font_type_list() -> PackedStringArray:
	pass;

#desc Returns the icon property defined by [param name] and [param theme_type], if it exists.
#desc Returns the engine fallback icon value if the property doesn't exist (see [member ThemeDB.fallback_icon]). Use [method has_icon] to check for existence.
func get_icon(name: StringName, theme_type: StringName) -> Texture2D:
	pass;

#desc Returns a list of names for icon properties defined with [param theme_type]. Use [method get_icon_type_list] to get a list of possible theme type names.
func get_icon_list() -> PackedStringArray:
	pass;

#desc Returns a list of all unique theme type names for icon properties. Use [method get_type_list] to get a list of all unique theme types.
func get_icon_type_list() -> PackedStringArray:
	pass;

#desc Returns the [StyleBox] property defined by [param name] and [param theme_type], if it exists.
#desc Returns the engine fallback stylebox value if the property doesn't exist (see [member ThemeDB.fallback_stylebox]). Use [method has_stylebox] to check for existence.
func get_stylebox(name: StringName, theme_type: StringName) -> StyleBox:
	pass;

#desc Returns a list of names for [StyleBox] properties defined with [param theme_type]. Use [method get_stylebox_type_list] to get a list of possible theme type names.
func get_stylebox_list() -> PackedStringArray:
	pass;

#desc Returns a list of all unique theme type names for [StyleBox] properties. Use [method get_type_list] to get a list of all unique theme types.
func get_stylebox_type_list() -> PackedStringArray:
	pass;

#desc Returns the theme property of [param data_type] defined by [param name] and [param theme_type], if it exists.
#desc Returns the engine fallback icon value if the property doesn't exist (see [ThemeDB]). Use [method has_theme_item] to check for existence.
#desc [b]Note:[/b] This method is analogous to calling the corresponding data type specific method, but can be used for more generalized logic.
func get_theme_item(data_type: int, name: StringName, theme_type: StringName) -> Variant:
	pass;

#desc Returns a list of names for properties of [param data_type] defined with [param theme_type]. Use [method get_theme_item_type_list] to get a list of possible theme type names.
#desc [b]Note:[/b] This method is analogous to calling the corresponding data type specific method, but can be used for more generalized logic.
func get_theme_item_list(data_type: int, theme_type: String) -> PackedStringArray:
	pass;

#desc Returns a list of all unique theme type names for [param data_type] properties. Use [method get_type_list] to get a list of all unique theme types.
#desc [b]Note:[/b] This method is analogous to calling the corresponding data type specific method, but can be used for more generalized logic.
func get_theme_item_type_list() -> PackedStringArray:
	pass;

#desc Returns a list of all unique theme type names. Use the appropriate [code]get_*_type_list[/code] method to get a list of unique theme types for a single data type.
func get_type_list() -> PackedStringArray:
	pass;

#desc Returns the name of the base theme type if [param theme_type] is a valid variation type. Returns an empty string otherwise.
func get_type_variation_base() -> StringName:
	pass;

#desc Returns a list of all type variations for the given [param base_type].
func get_type_variation_list() -> PackedStringArray:
	pass;

#desc Returns [code]true[/code] if the [Color] property defined by [param name] and [param theme_type] exists.
#desc Returns [code]false[/code] if it doesn't exist. Use [method set_color] to define it.
func has_color(name: StringName, theme_type: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if the constant property defined by [param name] and [param theme_type] exists.
#desc Returns [code]false[/code] if it doesn't exist. Use [method set_constant] to define it.
func has_constant(name: StringName, theme_type: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if [member default_base_scale] has a valid value.
#desc Returns [code]false[/code] if it doesn't. The value must be greater than [code]0.0[/code] to be considered valid.
func has_default_base_scale() -> bool:
	pass;

#desc Returns [code]true[/code] if [member default_font] has a valid value.
#desc Returns [code]false[/code] if it doesn't.
func has_default_font() -> bool:
	pass;

#desc Returns [code]true[/code] if [member default_font_size] has a valid value.
#desc Returns [code]false[/code] if it doesn't. The value must be greater than [code]0[/code] to be considered valid.
func has_default_font_size() -> bool:
	pass;

#desc Returns [code]true[/code] if the [Font] property defined by [param name] and [param theme_type] exists, or if the default theme font is set up (see [method has_default_font]).
#desc Returns [code]false[/code] if neither exist. Use [method set_font] to define the property.
func has_font(name: StringName, theme_type: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if the font size property defined by [param name] and [param theme_type] exists, or if the default theme font size is set up (see [method has_default_font_size]).
#desc Returns [code]false[/code] if neither exist. Use [method set_font_size] to define the property.
func has_font_size(name: StringName, theme_type: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if the icon property defined by [param name] and [param theme_type] exists.
#desc Returns [code]false[/code] if it doesn't exist. Use [method set_icon] to define it.
func has_icon(name: StringName, theme_type: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if the [StyleBox] property defined by [param name] and [param theme_type] exists.
#desc Returns [code]false[/code] if it doesn't exist. Use [method set_stylebox] to define it.
func has_stylebox(name: StringName, theme_type: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if the theme property of [param data_type] defined by [param name] and [param theme_type] exists.
#desc Returns [code]false[/code] if it doesn't exist. Use [method set_theme_item] to define it.
#desc [b]Note:[/b] This method is analogous to calling the corresponding data type specific method, but can be used for more generalized logic.
func has_theme_item(data_type: int, name: StringName, theme_type: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if [param theme_type] is marked as a variation of [param base_type].
func is_type_variation(theme_type: StringName, base_type: StringName) -> bool:
	pass;

#desc Adds missing and overrides existing definitions with values from the [param other] theme resource.
#desc [b]Note:[/b] This modifies the current theme. If you want to merge two themes together without modifying either one, create a new empty theme and merge the other two into it one after another.
func merge_with() -> void:
	pass;

#desc Removes the theme type, gracefully discarding defined theme items. If the type is a variation, this information is also erased. If the type is a base for type variations, those variations lose their base.
func remove_type() -> void:
	pass;

#desc Renames the [Color] property defined by [param old_name] and [param theme_type] to [param name], if it exists.
#desc Fails if it doesn't exist, or if a similar property with the new name already exists. Use [method has_color] to check for existence, and [method clear_color] to remove the existing property.
func rename_color(old_name: StringName, name: StringName, theme_type: StringName) -> void:
	pass;

#desc Renames the constant property defined by [param old_name] and [param theme_type] to [param name], if it exists.
#desc Fails if it doesn't exist, or if a similar property with the new name already exists. Use [method has_constant] to check for existence, and [method clear_constant] to remove the existing property.
func rename_constant(old_name: StringName, name: StringName, theme_type: StringName) -> void:
	pass;

#desc Renames the [Font] property defined by [param old_name] and [param theme_type] to [param name], if it exists.
#desc Fails if it doesn't exist, or if a similar property with the new name already exists. Use [method has_font] to check for existence, and [method clear_font] to remove the existing property.
func rename_font(old_name: StringName, name: StringName, theme_type: StringName) -> void:
	pass;

#desc Renames the font size property defined by [param old_name] and [param theme_type] to [param name], if it exists.
#desc Fails if it doesn't exist, or if a similar property with the new name already exists. Use [method has_font_size] to check for existence, and [method clear_font_size] to remove the existing property.
func rename_font_size(old_name: StringName, name: StringName, theme_type: StringName) -> void:
	pass;

#desc Renames the icon property defined by [param old_name] and [param theme_type] to [param name], if it exists.
#desc Fails if it doesn't exist, or if a similar property with the new name already exists. Use [method has_icon] to check for existence, and [method clear_icon] to remove the existing property.
func rename_icon(old_name: StringName, name: StringName, theme_type: StringName) -> void:
	pass;

#desc Renames the [StyleBox] property defined by [param old_name] and [param theme_type] to [param name], if it exists.
#desc Fails if it doesn't exist, or if a similar property with the new name already exists. Use [method has_stylebox] to check for existence, and [method clear_stylebox] to remove the existing property.
func rename_stylebox(old_name: StringName, name: StringName, theme_type: StringName) -> void:
	pass;

#desc Renames the theme property of [param data_type] defined by [param old_name] and [param theme_type] to [param name], if it exists.
#desc Fails if it doesn't exist, or if a similar property with the new name already exists. Use [method has_theme_item] to check for existence, and [method clear_theme_item] to remove the existing property.
#desc [b]Note:[/b] This method is analogous to calling the corresponding data type specific method, but can be used for more generalized logic.
func rename_theme_item(data_type: int, old_name: StringName, name: StringName, theme_type: StringName) -> void:
	pass;

#desc Creates or changes the value of the [Color] property defined by [param name] and [param theme_type]. Use [method clear_color] to remove the property.
func set_color(name: StringName, theme_type: StringName, color: Color) -> void:
	pass;

#desc Creates or changes the value of the constant property defined by [param name] and [param theme_type]. Use [method clear_constant] to remove the property.
func set_constant(name: StringName, theme_type: StringName, constant: int) -> void:
	pass;

#desc Creates or changes the value of the [Font] property defined by [param name] and [param theme_type]. Use [method clear_font] to remove the property.
func set_font(name: StringName, theme_type: StringName, font: Font) -> void:
	pass;

#desc Creates or changes the value of the font size property defined by [param name] and [param theme_type]. Use [method clear_font_size] to remove the property.
func set_font_size(name: StringName, theme_type: StringName, font_size: int) -> void:
	pass;

#desc Creates or changes the value of the icon property defined by [param name] and [param theme_type]. Use [method clear_icon] to remove the property.
func set_icon(name: StringName, theme_type: StringName, texture: Texture2D) -> void:
	pass;

#desc Creates or changes the value of the [StyleBox] property defined by [param name] and [param theme_type]. Use [method clear_stylebox] to remove the property.
func set_stylebox(name: StringName, theme_type: StringName, texture: StyleBox) -> void:
	pass;

#desc Creates or changes the value of the theme property of [param data_type] defined by [param name] and [param theme_type]. Use [method clear_theme_item] to remove the property.
#desc Fails if the [param value] type is not accepted by [param data_type].
#desc [b]Note:[/b] This method is analogous to calling the corresponding data type specific method, but can be used for more generalized logic.
func set_theme_item(data_type: int, name: StringName, theme_type: StringName, value: Variant) -> void:
	pass;

#desc Marks [param theme_type] as a variation of [param base_type].
#desc This adds [param theme_type] as a suggested option for [member Control.theme_type_variation] on a [Control] that is of the [param base_type] class.
#desc Variations can also be nested, i.e. [param base_type] can be another variation. If a chain of variations ends with a [param base_type] matching the class of the [Control], the whole chain is going to be suggested as options.
#desc [b]Note:[/b] Suggestions only show up if this theme resource is set as the project default theme. See [member ProjectSettings.gui/theme/custom].
func set_type_variation(theme_type: StringName, base_type: StringName) -> void:
	pass;


