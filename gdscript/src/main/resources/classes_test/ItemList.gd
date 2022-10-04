#brief Control that provides a list of selectable items (and/or icons) in a single column, or optionally in multiple columns.
#desc This control provides a selectable list of items that may be in a single (or multiple columns) with option of text, icons, or both text and icon. Tooltips are supported and may be different for every item in the list.
#desc Selectable items in the list may be selected or deselected and multiple selection may be enabled. Selection with right mouse button may also be enabled to allow use of popup context menus. Items may also be "activated" by double-clicking them or by pressing [kbd]Enter[/kbd].
#desc Item text only supports single-line strings, newline characters (e.g. [code]\n[/code]) in the string won't produce a newline. Text wrapping is enabled in [constant ICON_MODE_TOP] mode, but column's width is adjusted to fully fit its content by default. You need to set [member fixed_column_width] greater than zero to wrap the text.
#desc All [code]set_*[/code] methods allow negative item index, which makes the item accessed from the last one.
#desc [b]Incremental search:[/b] Like [PopupMenu] and [Tree], [ItemList] supports searching within the list while the control is focused. Press a key that matches the first letter of an item's name to select the first item starting with the given letter. After that point, there are two ways to perform incremental search: 1) Press the same key again before the timeout duration to select the next item starting with the same letter. 2) Press letter keys that match the rest of the word before the timeout duration to match to select the item in question directly. Both of these actions will be reset to the beginning of the list if the timeout duration has passed since the last keystroke was registered. You can adjust the timeout duration by changing [member ProjectSettings.gui/timers/incremental_search_max_interval_msec].
class_name ItemList

#desc Icon is drawn above the text.
const ICON_MODE_TOP = 0;

#desc Icon is drawn to the left of the text.
const ICON_MODE_LEFT = 1;

#desc Only allow selecting a single item.
const SELECT_SINGLE = 0;

#desc Allows selecting multiple items by holding [kbd]Ctrl[/kbd] or [kbd]Shift[/kbd].
const SELECT_MULTI = 1;


#desc If [code]true[/code], the currently selected item can be selected again.
var allow_reselect: bool;

#desc If [code]true[/code], right mouse button click can select items.
var allow_rmb_select: bool;

#desc If [code]true[/code], the control will automatically resize the height to fit its content.
var auto_height: bool;

var clip_contents: bool;

#desc The width all columns will be adjusted to.
#desc A value of zero disables the adjustment, each item will have a width equal to the width of its content and the columns will have an uneven width.
var fixed_column_width: int;

#desc The size all icons will be adjusted to.
#desc If either X or Y component is not greater than zero, icon size won't be affected.
var fixed_icon_size: Vector2i;

var focus_mode: int;

#desc The icon position, whether above or to the left of the text. See the [enum IconMode] constants.
var icon_mode: int;

#desc The scale of icon applied after [member fixed_icon_size] and transposing takes effect.
var icon_scale: float;

#desc The number of items currently in the list.
var item_count: int;

#desc Maximum columns the list will have.
#desc If greater than zero, the content will be split among the specified columns.
#desc A value of zero means unlimited columns, i.e. all items will be put in the same row.
var max_columns: int;

#desc Maximum lines of text allowed in each item. Space will be reserved even when there is not enough lines of text to display.
#desc [b]Note:[/b] This property takes effect only when [member icon_mode] is [constant ICON_MODE_TOP]. To make the text wrap, [member fixed_column_width] should be greater than zero.
var max_text_lines: int;

#desc Whether all columns will have the same width.
#desc If [code]true[/code], the width is equal to the largest column width of all columns.
var same_column_width: bool;

#desc Allows single or multiple item selection. See the [enum SelectMode] constants.
var select_mode: int;

#desc Sets the clipping behavior when the text exceeds an item's bounding rectangle. See [enum TextServer.OverrunBehavior] for a description of all modes.
var text_overrun_behavior: int;



#desc Adds an item to the item list with no text, only an icon. Returns the index of an added item.
func add_icon_item(icon: Texture2D, selectable: bool) -> int:
	pass;

#desc Adds an item to the item list with specified text. Returns the index of an added item.
#desc Specify an [param icon], or use [code]null[/code] as the [param icon] for a list item with no icon.
#desc If selectable is [code]true[/code], the list item will be selectable.
func add_item(text: String, icon: Texture2D, selectable: bool) -> int:
	pass;

#desc Removes all items from the list.
func clear() -> void:
	pass;

#desc Ensures the item associated with the specified index is not selected.
func deselect() -> void:
	pass;

#desc Ensures there are no items selected.
func deselect_all() -> void:
	pass;

#desc Ensure current selection is visible, adjusting the scroll position as necessary.
func ensure_current_is_visible() -> void:
	pass;

#desc Returns the item index at the given [param position].
#desc When there is no item at that point, -1 will be returned if [param exact] is [code]true[/code], and the closest item index will be returned otherwise.
func get_item_at_position(position: Vector2, exact: bool) -> int:
	pass;

#desc Returns the custom background color of the item specified by [param idx] index.
func get_item_custom_bg_color() -> Color:
	pass;

#desc Returns the custom foreground color of the item specified by [param idx] index.
func get_item_custom_fg_color() -> Color:
	pass;

#desc Returns the icon associated with the specified index.
func get_item_icon() -> Texture2D:
	pass;

#desc Returns a [Color] modulating item's icon at the specified index.
func get_item_icon_modulate() -> Color:
	pass;

#desc Returns the region of item's icon used. The whole icon will be used if the region has no area.
func get_item_icon_region() -> Rect2:
	pass;

#desc Returns item's text language code.
func get_item_language() -> String:
	pass;

#desc Returns the metadata value of the specified index.
func get_item_metadata() -> Variant:
	pass;

#desc Returns the text associated with the specified index.
func get_item_text() -> String:
	pass;

#desc Returns item's text base writing direction.
func get_item_text_direction() -> int:
	pass;

#desc Returns the tooltip hint associated with the specified index.
func get_item_tooltip() -> String:
	pass;

#desc Returns an array with the indexes of the selected items.
func get_selected_items() -> PackedInt32Array:
	pass;

#desc Returns the vertical scrollbar.
#desc [b]Warning:[/b] This is a required internal node, removing and freeing it may cause a crash. If you wish to hide it or any of its children, use their [member CanvasItem.visible] property.
func get_v_scroll_bar() -> VScrollBar:
	pass;

#desc Returns [code]true[/code] if one or more items are selected.
func is_anything_selected() -> bool:
	pass;

#desc Returns [code]true[/code] if the item at the specified index is disabled.
func is_item_disabled() -> bool:
	pass;

#desc Returns [code]true[/code] if the item icon will be drawn transposed, i.e. the X and Y axes are swapped.
func is_item_icon_transposed() -> bool:
	pass;

#desc Returns [code]true[/code] if the item at the specified index is selectable.
func is_item_selectable() -> bool:
	pass;

#desc Returns [code]true[/code] if the tooltip is enabled for specified item index.
func is_item_tooltip_enabled() -> bool:
	pass;

#desc Returns [code]true[/code] if the item at the specified index is currently selected.
func is_selected() -> bool:
	pass;

#desc Moves item from index [param from_idx] to [param to_idx].
func move_item(from_idx: int, to_idx: int) -> void:
	pass;

#desc Removes the item specified by [param idx] index from the list.
func remove_item() -> void:
	pass;

#desc Select the item at the specified index.
#desc [b]Note:[/b] This method does not trigger the item selection signal.
func select(idx: int, single: bool) -> void:
	pass;

#desc Sets the background color of the item specified by [param idx] index to the specified [Color].
func set_item_custom_bg_color(idx: int, custom_bg_color: Color) -> void:
	pass;

#desc Sets the foreground color of the item specified by [param idx] index to the specified [Color].
func set_item_custom_fg_color(idx: int, custom_fg_color: Color) -> void:
	pass;

#desc Disables (or enables) the item at the specified index.
#desc Disabled items cannot be selected and do not trigger activation signals (when double-clicking or pressing [kbd]Enter[/kbd]).
func set_item_disabled(idx: int, disabled: bool) -> void:
	pass;

#desc Sets (or replaces) the icon's [Texture2D] associated with the specified index.
func set_item_icon(idx: int, icon: Texture2D) -> void:
	pass;

#desc Sets a modulating [Color] of the item associated with the specified index.
func set_item_icon_modulate(idx: int, modulate: Color) -> void:
	pass;

#desc Sets the region of item's icon used. The whole icon will be used if the region has no area.
func set_item_icon_region(idx: int, rect: Rect2) -> void:
	pass;

#desc Sets whether the item icon will be drawn transposed.
func set_item_icon_transposed(idx: int, transposed: bool) -> void:
	pass;

#desc Sets language code of item's text used for line-breaking and text shaping algorithms, if left empty current locale is used instead.
func set_item_language(idx: int, language: String) -> void:
	pass;

#desc Sets a value (of any type) to be stored with the item associated with the specified index.
func set_item_metadata(idx: int, metadata: Variant) -> void:
	pass;

#desc Allows or disallows selection of the item associated with the specified index.
func set_item_selectable(idx: int, selectable: bool) -> void:
	pass;

#desc Sets text of the item associated with the specified index.
func set_item_text(idx: int, text: String) -> void:
	pass;

#desc Sets item's text base writing direction.
func set_item_text_direction(idx: int, direction: int) -> void:
	pass;

#desc Sets the tooltip hint for the item associated with the specified index.
func set_item_tooltip(idx: int, tooltip: String) -> void:
	pass;

#desc Sets whether the tooltip hint is enabled for specified item index.
func set_item_tooltip_enabled(idx: int, enable: bool) -> void:
	pass;

#desc Sorts items in the list by their text.
func sort_items_by_text() -> void:
	pass;


