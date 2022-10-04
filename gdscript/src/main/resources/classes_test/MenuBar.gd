#brief A horizontal menu bar, which displays [PopupMenu]s or system global menu.
#desc New items can be created by adding [PopupMenu] nodes to his node.
class_name MenuBar


#desc Flat [MenuBar] don't display item decoration.
var flat: bool;

#desc Language code used for line-breaking and text shaping algorithms, if left empty current locale is used instead.
var language: String;

#desc If [code]true[/code], [MenuBar] will use system global menu when supported.
var prefer_global_menu: bool;

#desc The [Node] which must be a parent of the focused GUI [Control] for the shortcut to be activated. If [code]null[/code], the shortcut can be activated when any control is focused (a global shortcut). This allows shortcuts to be accepted only when the user has a certain area of the GUI focused.
var shortcut_context: Node;

#desc Position in the global menu to insert first [MenuBar] item at.
var start_index: int;

#desc If [code]true[/code], when the cursor hovers above menu item, it will close the current [PopupMenu] and open the other one.
var switch_on_hover: bool;

#desc Base text writing direction.
var text_direction: int;



#desc Returns number of menu items.
func get_menu_count() -> int:
	pass;

#desc Returns [PopupMenu] associated with menu item.
func get_menu_popup() -> PopupMenu:
	pass;

#desc Returns menu item title.
func get_menu_title() -> String:
	pass;

#desc Returns menu item tooltip.
func get_menu_tooltip() -> String:
	pass;

#desc Returns [code]true[/code], if menu item is disabled.
func is_menu_disabled() -> bool:
	pass;

#desc Returns [code]true[/code], if menu item is hidden.
func is_menu_hidden() -> bool:
	pass;

#desc Returns [code]true[/code], if system global menu is supported and used by this [MenuBar].
func is_native_menu() -> bool:
	pass;

#desc If [code]true[/code], shortcuts are disabled and cannot be used to trigger the button.
func set_disable_shortcuts() -> void:
	pass;

#desc If [code]true[/code], menu item is disabled.
func set_menu_disabled(menu: int, disabled: bool) -> void:
	pass;

#desc If [code]true[/code], menu item is hidden.
func set_menu_hidden(menu: int, hidden: bool) -> void:
	pass;

#desc Sets menu item title.
func set_menu_title(menu: int, title: String) -> void:
	pass;

#desc Sets menu item tooltip.
func set_menu_tooltip(menu: int, tooltip: String) -> void:
	pass;


