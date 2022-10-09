extends Container
#brief Tabbed container.
#desc Arranges [Control] children into a tabbed view, creating a tab for each one. The active tab's corresponding [Control] has its [code]visible[/code] property set to [code]true[/code], and all other children's to [code]false[/code].
#desc Ignores non-[Control] children.
#desc [b]Note:[/b] The drawing of the clickable tabs themselves is handled by this node. Adding [TabBar]s as children is not needed.
class_name TabContainer


#desc If [code]true[/code], all tabs are drawn in front of the panel. If [code]false[/code], inactive tabs are drawn behind the panel.
var all_tabs_in_front: bool;

#desc If [code]true[/code], tabs overflowing this node's width will be hidden, displaying two navigation buttons instead. Otherwise, this node's minimum size is updated so that all tabs are visible.
var clip_tabs: bool;

#desc The current tab index. When set, this index's [Control] node's [code]visible[/code] property is set to [code]true[/code] and all others are set to [code]false[/code].
var current_tab: int;

#desc If [code]true[/code], tabs can be rearranged with mouse drag.
var drag_to_rearrange_enabled: bool;

#desc Sets the position at which tabs will be placed. See [enum TabBar.AlignmentMode] for details.
var tab_alignment: int;

#desc [TabContainer]s with the same rearrange group ID will allow dragging the tabs between them. Enable drag with [member drag_to_rearrange_enabled].
#desc Setting this to [code]-1[/code] will disable rearranging between [TabContainer]s.
var tabs_rearrange_group: int;

#desc If [code]true[/code], tabs are visible. If [code]false[/code], tabs' content and titles are hidden.
var tabs_visible: bool;

#desc If [code]true[/code], children [Control] nodes that are hidden have their minimum size take into account in the total, instead of only the currently visible one.
var use_hidden_tabs_for_min_size: bool;



#desc Returns the child [Control] node located at the active tab index.
func get_current_tab_control() -> Control:
	pass;

#desc Returns the [Popup] node instance if one has been set already with [method set_popup].
#desc [b]Warning:[/b] This is a required internal node, removing and freeing it may cause a crash. If you wish to hide it or any of its children, use their [member Window.visible] property.
func get_popup() -> Popup:
	pass;

#desc Returns the previously active tab index.
func get_previous_tab() -> int:
	pass;

#desc Returns the button icon from the tab at index [param tab_idx].
func get_tab_button_icon(tab_idx: int) -> Texture2D:
	pass;

#desc Returns the [Control] node from the tab at index [param tab_idx].
func get_tab_control(tab_idx: int) -> Control:
	pass;

#desc Returns the number of tabs.
func get_tab_count() -> int:
	pass;

#desc Returns the [Texture2D] for the tab at index [param tab_idx] or [code]null[/code] if the tab has no [Texture2D].
func get_tab_icon(tab_idx: int) -> Texture2D:
	pass;

#desc Returns the index of the tab at local coordinates [param point]. Returns [code]-1[/code] if the point is outside the control boundaries or if there's no tab at the queried position.
func get_tab_idx_at_point(point: Vector2) -> int:
	pass;

#desc Returns the index of the tab tied to the given [param control]. The control must be a child of the [TabContainer].
func get_tab_idx_from_control(control: Control) -> int:
	pass;

#desc Returns the title of the tab at index [param tab_idx]. Tab titles default to the name of the indexed child node, but this can be overridden with [method set_tab_title].
func get_tab_title(tab_idx: int) -> String:
	pass;

#desc Returns [code]true[/code] if the tab at index [param tab_idx] is disabled.
func is_tab_disabled(tab_idx: int) -> bool:
	pass;

#desc Returns [code]true[/code] if the tab at index [param tab_idx] is hidden.
func is_tab_hidden(tab_idx: int) -> bool:
	pass;

#desc If set on a [Popup] node instance, a popup menu icon appears in the top-right corner of the [TabContainer] (setting it to [code]null[/code] will make it go away). Clicking it will expand the [Popup] node.
func set_popup(popup: Node) -> void:
	pass;

#desc Sets the button icon from the tab at index [param tab_idx].
func set_tab_button_icon(tab_idx: int, icon: Texture2D) -> void:
	pass;

#desc If [param disabled] is [code]true[/code], disables the tab at index [param tab_idx], making it non-interactable.
func set_tab_disabled(tab_idx: int, disabled: bool) -> void:
	pass;

#desc If [param hidden] is [code]true[/code], hides the tab at index [param tab_idx], making it disappear from the tab area.
func set_tab_hidden(tab_idx: int, hidden: bool) -> void:
	pass;

#desc Sets an icon for the tab at index [param tab_idx].
func set_tab_icon(tab_idx: int, icon: Texture2D) -> void:
	pass;

#desc Sets a custom title for the tab at index [param tab_idx] (tab titles default to the name of the indexed child node). Set it back to the child's name to make the tab default to it again.
func set_tab_title(tab_idx: int, title: String) -> void:
	pass;


