#brief Special button that brings up a [PopupMenu] when clicked.
#desc Special button that brings up a [PopupMenu] when clicked.
#desc New items can be created inside this [PopupMenu] using [code]get_popup().add_item("My Item Name")[/code]. You can also create them directly from the editor. To do so, select the [MenuButton] node, then in the toolbar at the top of the 2D editor, click [b]Items[/b] then click [b]Add[/b] in the popup. You will be able to give each item new properties.
#desc See also [BaseButton] which contains common properties and methods associated with this node.
class_name MenuButton


var action_mode: int;

var flat: bool;

var focus_mode: int;

#desc The number of items currently in the list.
var item_count: int;

#desc If [code]true[/code], when the cursor hovers above another [MenuButton] within the same parent which also has [code]switch_on_hover[/code] enabled, it will close the current [MenuButton] and open the other one.
var switch_on_hover: bool;

var toggle_mode: bool;



#desc Returns the [PopupMenu] contained in this button.
#desc [b]Warning:[/b] This is a required internal node, removing and freeing it may cause a crash. If you wish to hide it or any of its children, use their [member Window.visible] property.
func get_popup() -> PopupMenu:
	pass;

#desc If [code]true[/code], shortcuts are disabled and cannot be used to trigger the button.
func set_disable_shortcuts(disabled: bool) -> void:
	pass;

#desc Adjusts popup position and sizing for the [MenuButton], then shows the [PopupMenu]. Prefer this over using [code]get_popup().popup()[/code].
func show_popup() -> void:
	pass;


