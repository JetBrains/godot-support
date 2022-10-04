#brief A helper node for displaying scrollable elements such as lists.
#desc A ScrollContainer node meant to contain a [Control] child.
#desc ScrollContainers will automatically create a scrollbar child ([HScrollBar], [VScrollBar], or both) when needed and will only draw the Control within the ScrollContainer area. Scrollbars will automatically be drawn at the right (for vertical) or bottom (for horizontal) and will enable dragging to move the viewable Control (and its children) within the ScrollContainer. Scrollbars will also automatically resize the grabber based on the [member Control.custom_minimum_size] of the Control relative to the ScrollContainer.
#desc Works great with a [Panel] control. You can set [code]EXPAND[/code] on the children's size flags, so they will upscale to the ScrollContainer's size if it's larger (scroll is invisible for the chosen dimension).
class_name ScrollContainer

#desc Scrolling disabled, scrollbar will be invisible.
const SCROLL_MODE_DISABLED = 0;

#desc Scrolling enabled, scrollbar will be visible only if necessary, i.e. container's content is bigger than the container.
const SCROLL_MODE_AUTO = 1;

#desc Scrolling enabled, scrollbar will be always visible.
const SCROLL_MODE_SHOW_ALWAYS = 2;

#desc Scrolling enabled, scrollbar will be hidden.
const SCROLL_MODE_SHOW_NEVER = 3;


var clip_contents: bool;

#desc If [code]true[/code], the ScrollContainer will automatically scroll to focused children (including indirect children) to make sure they are fully visible.
var follow_focus: bool;

#desc Controls whether horizontal scrollbar can be used and when it should be visible. See [enum ScrollMode] for options.
var horizontal_scroll_mode: int;

var scroll_deadzone: int;

#desc The current horizontal scroll value.
var scroll_horizontal: int;

#desc The current vertical scroll value.
var scroll_vertical: int;

#desc Controls whether vertical scrollbar can be used and when it should be visible. See [enum ScrollMode] for options.
var vertical_scroll_mode: int;



#desc Ensures the given [param control] is visible (must be a direct or indirect child of the ScrollContainer). Used by [member follow_focus].
#desc [b]Note:[/b] This will not work on a node that was just added during the same frame. If you want to scroll to a newly added child, you must wait until the next frame using [signal SceneTree.process_frame]:
#desc [codeblock]
#desc add_child(child_node)
#desc await get_tree().process_frame
#desc ensure_control_visible(child_node)
#desc [/codeblock]
func ensure_control_visible() -> void:
	pass;

#desc Returns the horizontal scrollbar [HScrollBar] of this [ScrollContainer].
#desc [b]Warning:[/b] This is a required internal node, removing and freeing it may cause a crash. If you wish to disable or hide a scrollbar, you can use [member horizontal_scroll_mode].
func get_h_scroll_bar() -> HScrollBar:
	pass;

#desc Returns the vertical scrollbar [VScrollBar] of this [ScrollContainer].
#desc [b]Warning:[/b] This is a required internal node, removing and freeing it may cause a crash. If you wish to disable or hide a scrollbar, you can use [member vertical_scroll_mode].
func get_v_scroll_bar() -> VScrollBar:
	pass;


