extends Object
#brief Control for a single item inside a [Tree].
#desc Control for a single item inside a [Tree]. May have child [TreeItem]s and be styled as well as contain buttons.
#desc You can remove a [TreeItem] by using [method Object.free].
class_name TreeItem

#desc Cell contains a string.
const CELL_MODE_STRING = 0;

#desc Cell contains a checkbox.
const CELL_MODE_CHECK = 1;

#desc Cell contains a range.
const CELL_MODE_RANGE = 2;

#desc Cell contains an icon.
const CELL_MODE_ICON = 3;

const CELL_MODE_CUSTOM = 4;


#desc If [code]true[/code], the TreeItem is collapsed.
var collapsed: bool;

#desc The custom minimum height.
var custom_minimum_height: int;

#desc If [code]true[/code], folding is disabled for this TreeItem.
var disable_folding: bool;

#desc If [code]true[/code], the [TreeItem] is visible (default).
#desc Note that if a [TreeItem] is set to not be visible, none of its children will be visible either.
var visible: bool;



#desc Adds a button with [Texture2D] [param button] at column [param column]. The [param id] is used to identify the button. If not specified, the next available index is used, which may be retrieved by calling [method get_button_count] immediately before this method. Optionally, the button can be [param disabled] and have a [param tooltip_text].
func add_button(column: int, button: Texture2D, id: int, disabled: bool, tooltip_text: String) -> void:
	pass;

#desc Calls the [param method] on the actual TreeItem and its children recursively. Pass parameters as a comma separated list.
vararg func call_recursive(method: StringName) -> void:
	pass;

#desc Resets the background color for the given column to default.
func clear_custom_bg_color(column: int) -> void:
	pass;

#desc Resets the color for the given column to default.
func clear_custom_color(column: int) -> void:
	pass;

#desc Creates an item and adds it as a child.
#desc The new item will be inserted as position [param idx] (the default value [code]-1[/code] means the last position), or it will be the last child if [param idx] is higher than the child count.
func create_child(idx: int) -> TreeItem:
	pass;

#desc Deselects the given column.
func deselect(column: int) -> void:
	pass;

#desc Removes the button at index [param button_idx] in column [param column].
func erase_button(column: int, button_idx: int) -> void:
	pass;

#desc Returns the [Texture2D] of the button at index [param button_idx] in column [param column].
func get_button(column: int, button_idx: int) -> Texture2D:
	pass;

#desc Returns the button index if there is a button with id [param id] in column [param column], otherwise returns -1.
func get_button_by_id(column: int, id: int) -> int:
	pass;

#desc Returns the number of buttons in column [param column].
func get_button_count(column: int) -> int:
	pass;

#desc Returns the id for the button at index [param button_idx] in column [param column].
func get_button_id(column: int, button_idx: int) -> int:
	pass;

#desc Returns the tooltip text for the button at index [param button_idx] in column [param column].
func get_button_tooltip_text(column: int, button_idx: int) -> String:
	pass;

#desc Returns the column's cell mode.
func get_cell_mode(column: int) -> int:
	pass;

#desc Returns a child item by its index (see [method get_child_count]). This method is often used for iterating all children of an item.
#desc Negative indices access the children from the last one.
func get_child(idx: int) -> TreeItem:
	pass;

#desc Returns the number of child items.
func get_child_count() -> int:
	pass;

#desc Returns an array of references to the item's children.
func get_children() -> Array[TreeItem]:
	pass;

#desc Returns the custom background color of column [param column].
func get_custom_bg_color(column: int) -> Color:
	pass;

#desc Returns the custom color of column [param column].
func get_custom_color(column: int) -> Color:
	pass;

#desc Returns custom font used to draw text in the column [param column].
func get_custom_font(column: int) -> Font:
	pass;

#desc Returns custom font size used to draw text in the column [param column].
func get_custom_font_size(column: int) -> int:
	pass;

#desc Returns [code]true[/code] if [code]expand_right[/code] is set.
func get_expand_right(column: int) -> bool:
	pass;

#desc Returns the TreeItem's first child.
func get_first_child() -> TreeItem:
	pass;

#desc Returns the given column's icon [Texture2D]. Error if no icon is set.
func get_icon(column: int) -> Texture2D:
	pass;

#desc Returns the column's icon's maximum width.
func get_icon_max_width(column: int) -> int:
	pass;

#desc Returns the [Color] modulating the column's icon.
func get_icon_modulate(column: int) -> Color:
	pass;

#desc Returns the icon [Texture2D] region as [Rect2].
func get_icon_region(column: int) -> Rect2:
	pass;

#desc Returns the node's order in the tree. For example, if called on the first child item the position is [code]0[/code].
func get_index() -> int:
	pass;

#desc Returns item's text language code.
func get_language(column: int) -> String:
	pass;

#desc Returns the metadata value that was set for the given column using [method set_metadata].
func get_metadata(column: int) -> Variant:
	pass;

#desc Returns the next sibling TreeItem in the tree or a null object if there is none.
func get_next() -> TreeItem:
	pass;

#desc Returns the next visible sibling TreeItem in the tree or a null object if there is none.
#desc If [param wrap] is enabled, the method will wrap around to the first visible element in the tree when called on the last visible element, otherwise it returns [code]null[/code].
func get_next_visible(wrap: bool) -> TreeItem:
	pass;

#desc Returns the parent TreeItem or a null object if there is none.
func get_parent() -> TreeItem:
	pass;

#desc Returns the previous sibling TreeItem in the tree or a null object if there is none.
func get_prev() -> TreeItem:
	pass;

#desc Returns the previous visible sibling TreeItem in the tree or a null object if there is none.
#desc If [param wrap] is enabled, the method will wrap around to the last visible element in the tree when called on the first visible element, otherwise it returns [code]null[/code].
func get_prev_visible(wrap: bool) -> TreeItem:
	pass;

#desc Returns the value of a [constant CELL_MODE_RANGE] column.
func get_range(column: int) -> float:
	pass;

#desc Returns a dictionary containing the range parameters for a given column. The keys are "min", "max", "step", and "expr".
func get_range_config(column: int) -> Dictionary:
	pass;

func get_structured_text_bidi_override(column: int) -> int:
	pass;

func get_structured_text_bidi_override_options(column: int) -> Array:
	pass;

#desc Gets the suffix string shown after the column value.
func get_suffix(column: int) -> String:
	pass;

#desc Returns the given column's text.
func get_text(column: int) -> String:
	pass;

#desc Returns the given column's text alignment.
func get_text_alignment(column: int) -> int:
	pass;

#desc Returns item's text base writing direction.
func get_text_direction(column: int) -> int:
	pass;

#desc Returns the given column's tooltip text.
func get_tooltip_text(column: int) -> String:
	pass;

#desc Returns the [Tree] that owns this TreeItem.
func get_tree() -> Tree:
	pass;

#desc Returns [code]true[/code] if this [TreeItem], or any of its descendants, is collapsed.
#desc If [param only_visible] is [code]true[/code] it ignores non-visible [TreeItem]s.
func is_any_collapsed(only_visible: bool) -> bool:
	pass;

#desc Returns [code]true[/code] if the button at index [param button_idx] for the given [param column] is disabled.
func is_button_disabled(column: int, button_idx: int) -> bool:
	pass;

#desc Returns [code]true[/code] if the given [param column] is checked.
func is_checked(column: int) -> bool:
	pass;

func is_custom_set_as_button(column: int) -> bool:
	pass;

#desc Returns [code]true[/code] if the given [param column] is editable.
func is_editable(column: int) -> bool:
	pass;

#desc Returns [code]true[/code] if the given [param column] is indeterminate.
func is_indeterminate(column: int) -> bool:
	pass;

#desc Returns [code]true[/code] if the given [param column] is selectable.
func is_selectable(column: int) -> bool:
	pass;

#desc Returns [code]true[/code] if the given [param column] is selected.
func is_selected(column: int) -> bool:
	pass;

#desc Moves this TreeItem right after the given [param item].
#desc [b]Note:[/b] You can't move to the root or move the root.
func move_after(item: TreeItem) -> void:
	pass;

#desc Moves this TreeItem right before the given [param item].
#desc [b]Note:[/b] You can't move to the root or move the root.
func move_before(item: TreeItem) -> void:
	pass;

#desc Propagates this item's checked status to its children and parents for the given [param column]. It is possible to process the items affected by this method call by connecting to [signal Tree.check_propagated_to_item]. The order that the items affected will be processed is as follows: the item invoking this method, children of that item, and finally parents of that item. If [param emit_signal] is [code]false[/code], then [signal Tree.check_propagated_to_item] will not be emitted.
func propagate_check(column: int, emit_signal: bool) -> void:
	pass;

#desc Removes the given child [TreeItem] and all its children from the [Tree]. Note that it doesn't free the item from memory, so it can be reused later. To completely remove a [TreeItem] use [method Object.free].
func remove_child(child: TreeItem) -> void:
	pass;

#desc Selects the given [param column].
func select(column: int) -> void:
	pass;

#desc Sets the given column's button [Texture2D] at index [param button_idx] to [param button].
func set_button(column: int, button_idx: int, button: Texture2D) -> void:
	pass;

#desc If [code]true[/code], disables the button at index [param button_idx] in the given [param column].
func set_button_disabled(column: int, button_idx: int, disabled: bool) -> void:
	pass;

#desc Sets the given column's cell mode to [param mode]. See [enum TreeCellMode] constants.
func set_cell_mode(column: int, mode: int) -> void:
	pass;

#desc If [code]true[/code], the given [param column] is checked. Clears column's indeterminate status.
func set_checked(column: int, checked: bool) -> void:
	pass;

#desc Collapses or uncollapses this [TreeItem] and all the descendants of this item.
func set_collapsed_recursive(enable: bool) -> void:
	pass;

func set_custom_as_button(column: int, enable: bool) -> void:
	pass;

#desc Sets the given column's custom background color and whether to just use it as an outline.
func set_custom_bg_color(column: int, color: Color, just_outline: bool) -> void:
	pass;

#desc Sets the given column's custom color.
func set_custom_color(column: int, color: Color) -> void:
	pass;

#desc Sets the given column's custom draw callback to [param callback] method on [param object].
#desc The [param callback] should accept two arguments: the [TreeItem] that is drawn and its position and size as a [Rect2].
func set_custom_draw(column: int, object: Object, callback: StringName) -> void:
	pass;

#desc Sets custom font used to draw text in the given [param column].
func set_custom_font(column: int, font: Font) -> void:
	pass;

#desc Sets custom font size used to draw text in the given [param column].
func set_custom_font_size(column: int, font_size: int) -> void:
	pass;

#desc If [code]true[/code], the given [param column] is editable.
func set_editable(column: int, enabled: bool) -> void:
	pass;

#desc If [code]true[/code], the given [param column] is expanded to the right.
func set_expand_right(column: int, enable: bool) -> void:
	pass;

#desc Sets the given column's icon [Texture2D].
func set_icon(column: int, texture: Texture2D) -> void:
	pass;

#desc Sets the given column's icon's maximum width.
func set_icon_max_width(column: int, width: int) -> void:
	pass;

#desc Modulates the given column's icon with [param modulate].
func set_icon_modulate(column: int, modulate: Color) -> void:
	pass;

#desc Sets the given column's icon's texture region.
func set_icon_region(column: int, region: Rect2) -> void:
	pass;

#desc If [code]true[/code], the given [param column] is marked [param indeterminate].
#desc [b]Note:[/b] If set [code]true[/code] from [code]false[/code], then column is cleared of checked status.
func set_indeterminate(column: int, indeterminate: bool) -> void:
	pass;

#desc Sets language code of item's text used for line-breaking and text shaping algorithms, if left empty current locale is used instead.
func set_language(column: int, language: String) -> void:
	pass;

#desc Sets the metadata value for the given column, which can be retrieved later using [method get_metadata]. This can be used, for example, to store a reference to the original data.
func set_metadata(column: int, meta: Variant) -> void:
	pass;

#desc Sets the value of a [constant CELL_MODE_RANGE] column.
func set_range(column: int, value: float) -> void:
	pass;

#desc Sets the range of accepted values for a column. The column must be in the [constant CELL_MODE_RANGE] mode.
#desc If [param expr] is [code]true[/code], the edit mode slider will use an exponential scale as with [member Range.exp_edit].
func set_range_config(column: int, min: float, max: float, step: float, expr: bool) -> void:
	pass;

#desc If [code]true[/code], the given column is selectable.
func set_selectable(column: int, selectable: bool) -> void:
	pass;

func set_structured_text_bidi_override(column: int, parser: int) -> void:
	pass;

func set_structured_text_bidi_override_options(column: int, args: Array) -> void:
	pass;

#desc Sets a string to be shown after a column's value (for example, a unit abbreviation).
func set_suffix(column: int, text: String) -> void:
	pass;

#desc Sets the given column's text value.
func set_text(column: int, text: String) -> void:
	pass;

#desc Sets the given column's text alignment. See [enum HorizontalAlignment] for possible values.
func set_text_alignment(column: int, text_alignment: int) -> void:
	pass;

#desc Sets item's text base writing direction.
func set_text_direction(column: int, direction: int) -> void:
	pass;

#desc Sets the given column's tooltip text.
func set_tooltip_text(column: int, tooltip: String) -> void:
	pass;

func uncollapse_tree() -> void:
	pass;


