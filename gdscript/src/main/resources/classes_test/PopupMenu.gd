#brief PopupMenu displays a list of options.
#desc [PopupMenu] is a modal window used to display a list of options. They are popular in toolbars or context menus.
#desc The size of a [PopupMenu] can be limited by using [member Window.max_size]. If the height of the list of items is larger than the maximum height of the [PopupMenu], a [ScrollContainer] within the popup will allow the user to scroll the contents.
#desc If no maximum size is set, or if it is set to 0, the [PopupMenu] height will be limited by its parent rect.
#desc All [code]set_*[/code] methods allow negative item index, which makes the item accessed from the last one.
#desc [b]Incremental search:[/b] Like [ItemList] and [Tree], [PopupMenu] supports searching within the list while the control is focused. Press a key that matches the first letter of an item's name to select the first item starting with the given letter. After that point, there are two ways to perform incremental search: 1) Press the same key again before the timeout duration to select the next item starting with the same letter. 2) Press letter keys that match the rest of the word before the timeout duration to match to select the item in question directly. Both of these actions will be reset to the beginning of the list if the timeout duration has passed since the last keystroke was registered. You can adjust the timeout duration by changing [member ProjectSettings.gui/timers/incremental_search_max_interval_msec].
class_name PopupMenu


#desc If [code]true[/code], allows navigating [PopupMenu] with letter keys.
var allow_search: bool;

#desc If [code]true[/code], hides the [PopupMenu] when a checkbox or radio button is selected.
var hide_on_checkable_item_selection: bool;

#desc If [code]true[/code], hides the [PopupMenu] when an item is selected.
var hide_on_item_selection: bool;

#desc If [code]true[/code], hides the [PopupMenu] when a state item is selected.
var hide_on_state_item_selection: bool;

#desc The number of items currently in the list.
var item_count: int;

#desc Sets the delay time in seconds for the submenu item to popup on mouse hovering. If the popup menu is added as a child of another (acting as a submenu), it will inherit the delay time of the parent menu item.
var submenu_popup_delay: float;



#desc Adds a new checkable item with text [param label].
#desc An [param id] can optionally be provided, as well as an accelerator ([param accel]). If no [param id] is provided, one will be created from the index. If no [param accel] is provided then the default [code]0[/code] will be assigned to it. See [method get_item_accelerator] for more info on accelerators.
#desc [b]Note:[/b] Checkable items just display a checkmark, but don't have any built-in checking behavior and must be checked/unchecked manually. See [method set_item_checked] for more info on how to control it.
func add_check_item(label: String, id: int, accel: int) -> void:
	pass;

#desc Adds a new checkable item and assigns the specified [Shortcut] to it. Sets the label of the checkbox to the [Shortcut]'s name.
#desc An [param id] can optionally be provided. If no [param id] is provided, one will be created from the index.
#desc [b]Note:[/b] Checkable items just display a checkmark, but don't have any built-in checking behavior and must be checked/unchecked manually. See [method set_item_checked] for more info on how to control it.
func add_check_shortcut(shortcut: Shortcut, id: int, global: bool) -> void:
	pass;

#desc Adds a new checkable item with text [param label] and icon [param texture].
#desc An [param id] can optionally be provided, as well as an accelerator ([param accel]). If no [param id] is provided, one will be created from the index. If no [param accel] is provided then the default [code]0[/code] will be assigned to it. See [method get_item_accelerator] for more info on accelerators.
#desc [b]Note:[/b] Checkable items just display a checkmark, but don't have any built-in checking behavior and must be checked/unchecked manually. See [method set_item_checked] for more info on how to control it.
func add_icon_check_item(texture: Texture2D, label: String, id: int, accel: int) -> void:
	pass;

#desc Adds a new checkable item and assigns the specified [Shortcut] and icon [param texture] to it. Sets the label of the checkbox to the [Shortcut]'s name.
#desc An [param id] can optionally be provided. If no [param id] is provided, one will be created from the index.
#desc [b]Note:[/b] Checkable items just display a checkmark, but don't have any built-in checking behavior and must be checked/unchecked manually. See [method set_item_checked] for more info on how to control it.
func add_icon_check_shortcut(texture: Texture2D, shortcut: Shortcut, id: int, global: bool) -> void:
	pass;

#desc Adds a new item with text [param label] and icon [param texture].
#desc An [param id] can optionally be provided, as well as an accelerator ([param accel]). If no [param id] is provided, one will be created from the index. If no [param accel] is provided then the default [code]0[/code] will be assigned to it. See [method get_item_accelerator] for more info on accelerators.
func add_icon_item(texture: Texture2D, label: String, id: int, accel: int) -> void:
	pass;

#desc Same as [method add_icon_check_item], but uses a radio check button.
func add_icon_radio_check_item(texture: Texture2D, label: String, id: int, accel: int) -> void:
	pass;

#desc Same as [method add_icon_check_shortcut], but uses a radio check button.
func add_icon_radio_check_shortcut(texture: Texture2D, shortcut: Shortcut, id: int, global: bool) -> void:
	pass;

#desc Adds a new item and assigns the specified [Shortcut] and icon [param texture] to it. Sets the label of the checkbox to the [Shortcut]'s name.
#desc An [param id] can optionally be provided. If no [param id] is provided, one will be created from the index.
func add_icon_shortcut(texture: Texture2D, shortcut: Shortcut, id: int, global: bool) -> void:
	pass;

#desc Adds a new item with text [param label].
#desc An [param id] can optionally be provided, as well as an accelerator ([param accel]). If no [param id] is provided, one will be created from the index. If no [param accel] is provided then the default [code]0[/code] will be assigned to it. See [method get_item_accelerator] for more info on accelerators.
#desc [b]Note:[/b] The provided [param id] is used only in [signal id_pressed] and [signal id_focused] signals. It's not related to the [code]index[/code] arguments in e.g. [method set_item_checked].
func add_item(label: String, id: int, accel: int) -> void:
	pass;

#desc Adds a new multistate item with text [param label].
#desc Contrarily to normal binary items, multistate items can have more than two states, as defined by [param max_states]. Each press or activate of the item will increase the state by one. The default value is defined by [param default_state].
#desc An [param id] can optionally be provided, as well as an accelerator ([param accel]). If no [param id] is provided, one will be created from the index. If no [param accel] is provided then the default [code]0[/code] will be assigned to it. See [method get_item_accelerator] for more info on accelerators.
func add_multistate_item(label: String, max_states: int, default_state: int, id: int, accel: int) -> void:
	pass;

#desc Adds a new radio check button with text [param label].
#desc An [param id] can optionally be provided, as well as an accelerator ([param accel]). If no [param id] is provided, one will be created from the index. If no [param accel] is provided then the default [code]0[/code] will be assigned to it. See [method get_item_accelerator] for more info on accelerators.
#desc [b]Note:[/b] Checkable items just display a checkmark, but don't have any built-in checking behavior and must be checked/unchecked manually. See [method set_item_checked] for more info on how to control it.
func add_radio_check_item(label: String, id: int, accel: int) -> void:
	pass;

#desc Adds a new radio check button and assigns a [Shortcut] to it. Sets the label of the checkbox to the [Shortcut]'s name.
#desc An [param id] can optionally be provided. If no [param id] is provided, one will be created from the index.
#desc [b]Note:[/b] Checkable items just display a checkmark, but don't have any built-in checking behavior and must be checked/unchecked manually. See [method set_item_checked] for more info on how to control it.
func add_radio_check_shortcut(shortcut: Shortcut, id: int, global: bool) -> void:
	pass;

#desc Adds a separator between items. Separators also occupy an index, which you can set by using the [param id] parameter.
#desc A [param label] can optionally be provided, which will appear at the center of the separator.
func add_separator(label: String, id: int) -> void:
	pass;

#desc Adds a [Shortcut].
#desc An [param id] can optionally be provided. If no [param id] is provided, one will be created from the index.
func add_shortcut(shortcut: Shortcut, id: int, global: bool) -> void:
	pass;

#desc Adds an item that will act as a submenu of the parent [PopupMenu] node when clicked. The [param submenu] argument is the name of the child [PopupMenu] node that will be shown when the item is clicked.
#desc An [param id] can optionally be provided. If no [param id] is provided, one will be created from the index.
func add_submenu_item(label: String, submenu: String, id: int) -> void:
	pass;

#desc Removes all items from the [PopupMenu].
func clear() -> void:
	pass;

#desc Returns the index of the currently focused item. Returns [code]-1[/code] if no item is focused.
func get_focused_item() -> int:
	pass;

#desc Returns the accelerator of the item at the given [param index]. Accelerators are special combinations of keys that activate the item, no matter which control is focused.
func get_item_accelerator() -> int:
	pass;

#desc Returns the icon of the item at the given [param index].
func get_item_icon() -> Texture2D:
	pass;

#desc Returns the id of the item at the given [param index]. [code]id[/code] can be manually assigned, while index can not.
func get_item_id() -> int:
	pass;

#desc Returns the horizontal offset of the item at the given [param index].
func get_item_indent() -> int:
	pass;

#desc Returns the index of the item containing the specified [param id]. Index is automatically assigned to each item by the engine and can not be set manually.
func get_item_index() -> int:
	pass;

#desc Returns item's text language code.
func get_item_language() -> String:
	pass;

#desc Returns the metadata of the specified item, which might be of any type. You can set it with [method set_item_metadata], which provides a simple way of assigning context data to items.
func get_item_metadata() -> Variant:
	pass;

#desc Returns the [Shortcut] associated with the item at the given [param index].
func get_item_shortcut() -> Shortcut:
	pass;

#desc Returns the submenu name of the item at the given [param index]. See [method add_submenu_item] for more info on how to add a submenu.
func get_item_submenu() -> String:
	pass;

#desc Returns the text of the item at the given [param index].
func get_item_text() -> String:
	pass;

#desc Returns item's text base writing direction.
func get_item_text_direction() -> int:
	pass;

#desc Returns the tooltip associated with the item at the given [param index].
func get_item_tooltip() -> String:
	pass;

#desc Returns [code]true[/code] if the item at the given [param index] is checkable in some way, i.e. if it has a checkbox or radio button.
#desc [b]Note:[/b] Checkable items just display a checkmark or radio button, but don't have any built-in checking behavior and must be checked/unchecked manually.
func is_item_checkable() -> bool:
	pass;

#desc Returns [code]true[/code] if the item at the given [param index] is checked.
func is_item_checked() -> bool:
	pass;

#desc Returns [code]true[/code] if the item at the given [param index] is disabled. When it is disabled it can't be selected, or its action invoked.
#desc See [method set_item_disabled] for more info on how to disable an item.
func is_item_disabled() -> bool:
	pass;

#desc Returns [code]true[/code] if the item at the given [param index] has radio button-style checkability.
#desc [b]Note:[/b] This is purely cosmetic; you must add the logic for checking/unchecking items in radio groups.
func is_item_radio_checkable() -> bool:
	pass;

#desc Returns [code]true[/code] if the item is a separator. If it is, it will be displayed as a line. See [method add_separator] for more info on how to add a separator.
func is_item_separator() -> bool:
	pass;

#desc Returns [code]true[/code] if the specified item's shortcut is disabled.
func is_item_shortcut_disabled() -> bool:
	pass;

#desc Removes the item at the given [param index] from the menu.
#desc [b]Note:[/b] The indices of items after the removed item will be shifted by one.
func remove_item() -> void:
	pass;

#desc Moves the scroll view to make the item at the given [param index] visible.
func scroll_to_item() -> void:
	pass;

#desc Sets the currently focused item as the given [param index].
#desc Passing [code]-1[/code] as the index makes so that no item is focused.
func set_focused_item() -> void:
	pass;

#desc Sets the accelerator of the item at the given [param index]. Accelerators are special combinations of keys that activate the item, no matter which control is focused.
func set_item_accelerator(index: int, accel: int) -> void:
	pass;

#desc Sets whether the item at the given [param index] has a checkbox. If [code]false[/code], sets the type of the item to plain text.
#desc [b]Note:[/b] Checkable items just display a checkmark, but don't have any built-in checking behavior and must be checked/unchecked manually.
func set_item_as_checkable(index: int, enable: bool) -> void:
	pass;

#desc Sets the type of the item at the given [param index] to radio button. If [code]false[/code], sets the type of the item to plain text.
func set_item_as_radio_checkable(index: int, enable: bool) -> void:
	pass;

#desc Mark the item at the given [param index] as a separator, which means that it would be displayed as a line. If [code]false[/code], sets the type of the item to plain text.
func set_item_as_separator(index: int, enable: bool) -> void:
	pass;

#desc Sets the checkstate status of the item at the given [param index].
func set_item_checked(index: int, checked: bool) -> void:
	pass;

#desc Enables/disables the item at the given [param index]. When it is disabled, it can't be selected and its action can't be invoked.
func set_item_disabled(index: int, disabled: bool) -> void:
	pass;

#desc Replaces the [Texture2D] icon of the item at the given [param index].
func set_item_icon(index: int, icon: Texture2D) -> void:
	pass;

#desc Sets the [param id] of the item at the given [param index].
#desc The [param id] is used in [signal id_pressed] and [signal id_focused] signals.
func set_item_id(index: int, id: int) -> void:
	pass;

#desc Sets the horizontal offset of the item at the given [param index].
func set_item_indent(index: int, indent: int) -> void:
	pass;

#desc Sets language code of item's text used for line-breaking and text shaping algorithms, if left empty current locale is used instead.
func set_item_language(index: int, language: String) -> void:
	pass;

#desc Sets the metadata of an item, which may be of any type. You can later get it with [method get_item_metadata], which provides a simple way of assigning context data to items.
func set_item_metadata(index: int, metadata: Variant) -> void:
	pass;

#desc Sets the state of a multistate item. See [method add_multistate_item] for details.
func set_item_multistate(index: int, state: int) -> void:
	pass;

#desc Sets a [Shortcut] for the item at the given [param index].
func set_item_shortcut(index: int, shortcut: Shortcut, global: bool) -> void:
	pass;

#desc Disables the [Shortcut] of the item at the given [param index].
func set_item_shortcut_disabled(index: int, disabled: bool) -> void:
	pass;

#desc Sets the submenu of the item at the given [param index]. The submenu is the name of a child [PopupMenu] node that would be shown when the item is clicked.
func set_item_submenu(index: int, submenu: String) -> void:
	pass;

#desc Sets the text of the item at the given [param index].
func set_item_text(index: int, text: String) -> void:
	pass;

#desc Sets item's text base writing direction.
func set_item_text_direction(index: int, direction: int) -> void:
	pass;

#desc Sets the [String] tooltip of the item at the given [param index].
func set_item_tooltip(index: int, tooltip: String) -> void:
	pass;

#desc Toggles the check state of the item at the given [param index].
func toggle_item_checked() -> void:
	pass;

#desc Cycle to the next state of a multistate item. See [method add_multistate_item] for details.
func toggle_item_multistate() -> void:
	pass;


