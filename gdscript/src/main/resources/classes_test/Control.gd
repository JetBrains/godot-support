#brief All user interface nodes inherit from Control. A control's anchors and offsets adapt its position and size relative to its parent.
#desc Base class for all UI-related nodes. [Control] features a bounding rectangle that defines its extents, an anchor position relative to its parent control or the current viewport, and offsets relative to the anchor. The offsets update automatically when the node, any of its parents, or the screen size change.
#desc For more information on Godot's UI system, anchors, offsets, and containers, see the related tutorials in the manual. To build flexible UIs, you'll need a mix of UI elements that inherit from [Control] and [Container] nodes.
#desc [b]User Interface nodes and input[/b]
#desc Godot sends input events to the scene's root node first, by calling [method Node._input]. [method Node._input] forwards the event down the node tree to the nodes under the mouse cursor, or on keyboard focus. To do so, it calls [code]MainLoop._input_event[/code].
#desc [b]FIXME:[/b] No longer valid after DisplayServer split and Input refactoring.
#desc Call [method accept_event] so no other node receives the event. Once you accept an input, it becomes handled so [method Node._unhandled_input] will not process it.
#desc Only one [Control] node can be in keyboard focus. Only the node in focus will receive keyboard events. To get the focus, call [method grab_focus]. [Control] nodes lose focus when another node grabs it, or if you hide the node in focus.
#desc Sets [member mouse_filter] to [constant MOUSE_FILTER_IGNORE] to tell a [Control] node to ignore mouse or touch events. You'll need it if you place an icon on top of a button.
#desc [Theme] resources change the Control's appearance. If you change the [Theme] on a [Control] node, it affects all of its children. To override some of the theme's parameters, call one of the [code]add_theme_*_override[/code] methods, like [method add_theme_font_override]. You can override the theme with the inspector.
#desc [b]Note:[/b] Theme items are [i]not[/i] [Object] properties. This means you can't access their values using [method Object.get] and [method Object.set]. Instead, use the [code]get_theme_*[/code] and [code]add_theme_*_override[/code] methods provided by this class.
class_name Control

#desc The node cannot grab focus. Use with [member focus_mode].
const FOCUS_NONE = 0;

#desc The node can only grab focus on mouse clicks. Use with [member focus_mode].
const FOCUS_CLICK = 1;

#desc The node can grab focus on mouse click or using the arrows and the Tab keys on the keyboard. Use with [member focus_mode].
const FOCUS_ALL = 2;

#desc Sent when the node changes size. Use [member size] to get the new size.
const NOTIFICATION_RESIZED = 40;

#desc Sent when the mouse pointer enters the node.
const NOTIFICATION_MOUSE_ENTER = 41;

#desc Sent when the mouse pointer exits the node.
const NOTIFICATION_MOUSE_EXIT = 42;

#desc Sent when the node grabs focus.
const NOTIFICATION_FOCUS_ENTER = 43;

#desc Sent when the node loses focus.
const NOTIFICATION_FOCUS_EXIT = 44;

#desc Sent when the node needs to refresh its theme items. This happens in one of the following cases:
#desc - The [member theme] property is changed on this node or any of its ancestors.
#desc - The [member theme_type_variation] property is changed on this node.
#desc - One of the node's theme property overrides is changed.
#desc - The node enters the scene tree.
#desc [b]Note:[/b] As an optimization, this notification won't be sent from changes that occur while this node is outside of the scene tree. Instead, all of the theme item updates can be applied at once when the node enters the scene tree.
const NOTIFICATION_THEME_CHANGED = 45;

#desc Sent when this node is inside a [ScrollContainer] which has begun being scrolled.
const NOTIFICATION_SCROLL_BEGIN = 47;

#desc Sent when this node is inside a [ScrollContainer] which has stopped being scrolled.
const NOTIFICATION_SCROLL_END = 48;

#desc Sent when control layout direction is changed.
const NOTIFICATION_LAYOUT_DIRECTION_CHANGED = 49;

#desc Show the system's arrow mouse cursor when the user hovers the node. Use with [member mouse_default_cursor_shape].
const CURSOR_ARROW = 0;

#desc Show the system's I-beam mouse cursor when the user hovers the node. The I-beam pointer has a shape similar to "I". It tells the user they can highlight or insert text.
const CURSOR_IBEAM = 1;

#desc Show the system's pointing hand mouse cursor when the user hovers the node.
const CURSOR_POINTING_HAND = 2;

#desc Show the system's cross mouse cursor when the user hovers the node.
const CURSOR_CROSS = 3;

#desc Show the system's wait mouse cursor when the user hovers the node. Often an hourglass.
const CURSOR_WAIT = 4;

#desc Show the system's busy mouse cursor when the user hovers the node. Often an arrow with a small hourglass.
const CURSOR_BUSY = 5;

#desc Show the system's drag mouse cursor, often a closed fist or a cross symbol, when the user hovers the node. It tells the user they're currently dragging an item, like a node in the Scene dock.
const CURSOR_DRAG = 6;

#desc Show the system's drop mouse cursor when the user hovers the node. It can be an open hand. It tells the user they can drop an item they're currently grabbing, like a node in the Scene dock.
const CURSOR_CAN_DROP = 7;

#desc Show the system's forbidden mouse cursor when the user hovers the node. Often a crossed circle.
const CURSOR_FORBIDDEN = 8;

#desc Show the system's vertical resize mouse cursor when the user hovers the node. A double-headed vertical arrow. It tells the user they can resize the window or the panel vertically.
const CURSOR_VSIZE = 9;

#desc Show the system's horizontal resize mouse cursor when the user hovers the node. A double-headed horizontal arrow. It tells the user they can resize the window or the panel horizontally.
const CURSOR_HSIZE = 10;

#desc Show the system's window resize mouse cursor when the user hovers the node. The cursor is a double-headed arrow that goes from the bottom left to the top right. It tells the user they can resize the window or the panel both horizontally and vertically.
const CURSOR_BDIAGSIZE = 11;

#desc Show the system's window resize mouse cursor when the user hovers the node. The cursor is a double-headed arrow that goes from the top left to the bottom right, the opposite of [constant CURSOR_BDIAGSIZE]. It tells the user they can resize the window or the panel both horizontally and vertically.
const CURSOR_FDIAGSIZE = 12;

#desc Show the system's move mouse cursor when the user hovers the node. It shows 2 double-headed arrows at a 90 degree angle. It tells the user they can move a UI element freely.
const CURSOR_MOVE = 13;

#desc Show the system's vertical split mouse cursor when the user hovers the node. On Windows, it's the same as [constant CURSOR_VSIZE].
const CURSOR_VSPLIT = 14;

#desc Show the system's horizontal split mouse cursor when the user hovers the node. On Windows, it's the same as [constant CURSOR_HSIZE].
const CURSOR_HSPLIT = 15;

#desc Show the system's help mouse cursor when the user hovers the node, a question mark.
const CURSOR_HELP = 16;

#desc Snap all 4 anchors to the top-left of the parent control's bounds. Use with [method set_anchors_preset].
const PRESET_TOP_LEFT = 0;

#desc Snap all 4 anchors to the top-right of the parent control's bounds. Use with [method set_anchors_preset].
const PRESET_TOP_RIGHT = 1;

#desc Snap all 4 anchors to the bottom-left of the parent control's bounds. Use with [method set_anchors_preset].
const PRESET_BOTTOM_LEFT = 2;

#desc Snap all 4 anchors to the bottom-right of the parent control's bounds. Use with [method set_anchors_preset].
const PRESET_BOTTOM_RIGHT = 3;

#desc Snap all 4 anchors to the center of the left edge of the parent control's bounds. Use with [method set_anchors_preset].
const PRESET_CENTER_LEFT = 4;

#desc Snap all 4 anchors to the center of the top edge of the parent control's bounds. Use with [method set_anchors_preset].
const PRESET_CENTER_TOP = 5;

#desc Snap all 4 anchors to the center of the right edge of the parent control's bounds. Use with [method set_anchors_preset].
const PRESET_CENTER_RIGHT = 6;

#desc Snap all 4 anchors to the center of the bottom edge of the parent control's bounds. Use with [method set_anchors_preset].
const PRESET_CENTER_BOTTOM = 7;

#desc Snap all 4 anchors to the center of the parent control's bounds. Use with [method set_anchors_preset].
const PRESET_CENTER = 8;

#desc Snap all 4 anchors to the left edge of the parent control. The left offset becomes relative to the left edge and the top offset relative to the top left corner of the node's parent. Use with [method set_anchors_preset].
const PRESET_LEFT_WIDE = 9;

#desc Snap all 4 anchors to the top edge of the parent control. The left offset becomes relative to the top left corner, the top offset relative to the top edge, and the right offset relative to the top right corner of the node's parent. Use with [method set_anchors_preset].
const PRESET_TOP_WIDE = 10;

#desc Snap all 4 anchors to the right edge of the parent control. The right offset becomes relative to the right edge and the top offset relative to the top right corner of the node's parent. Use with [method set_anchors_preset].
const PRESET_RIGHT_WIDE = 11;

#desc Snap all 4 anchors to the bottom edge of the parent control. The left offset becomes relative to the bottom left corner, the bottom offset relative to the bottom edge, and the right offset relative to the bottom right corner of the node's parent. Use with [method set_anchors_preset].
const PRESET_BOTTOM_WIDE = 12;

#desc Snap all 4 anchors to a vertical line that cuts the parent control in half. Use with [method set_anchors_preset].
const PRESET_VCENTER_WIDE = 13;

#desc Snap all 4 anchors to a horizontal line that cuts the parent control in half. Use with [method set_anchors_preset].
const PRESET_HCENTER_WIDE = 14;

#desc Snap all 4 anchors to the respective corners of the parent control. Set all 4 offsets to 0 after you applied this preset and the [Control] will fit its parent control. Use with [method set_anchors_preset].
const PRESET_FULL_RECT = 15;

#desc The control will be resized to its minimum size.
const PRESET_MODE_MINSIZE = 0;

#desc The control's width will not change.
const PRESET_MODE_KEEP_WIDTH = 1;

#desc The control's height will not change.
const PRESET_MODE_KEEP_HEIGHT = 2;

#desc The control's size will not change.
const PRESET_MODE_KEEP_SIZE = 3;

#desc Tells the parent [Container] to align the node with its start, either the top or the left edge. It is mutually exclusive with [constant SIZE_FILL] and other shrink size flags, but can be used with [constant SIZE_EXPAND] in some containers. Use with [member size_flags_horizontal] and [member size_flags_vertical].
#desc [b]Note:[/b] Setting this flag is equal to not having any size flags.
const SIZE_SHRINK_BEGIN = 0;

#desc Tells the parent [Container] to expand the bounds of this node to fill all the available space without pushing any other node. It is mutually exclusive with shrink size flags. Use with [member size_flags_horizontal] and [member size_flags_vertical].
const SIZE_FILL = 1;

#desc Tells the parent [Container] to let this node take all the available space on the axis you flag. If multiple neighboring nodes are set to expand, they'll share the space based on their stretch ratio. See [member size_flags_stretch_ratio]. Use with [member size_flags_horizontal] and [member size_flags_vertical].
const SIZE_EXPAND = 2;

#desc Sets the node's size flags to both fill and expand. See [constant SIZE_FILL] and [constant SIZE_EXPAND] for more information.
const SIZE_EXPAND_FILL = 3;

#desc Tells the parent [Container] to center the node in the available space. It is mutually exclusive with [constant SIZE_FILL] and other shrink size flags, but can be used with [constant SIZE_EXPAND] in some containers. Use with [member size_flags_horizontal] and [member size_flags_vertical].
const SIZE_SHRINK_CENTER = 4;

#desc Tells the parent [Container] to align the node with its end, either the bottom or the right edge. It is mutually exclusive with [constant SIZE_FILL] and other shrink size flags, but can be used with [constant SIZE_EXPAND] in some containers. Use with [member size_flags_horizontal] and [member size_flags_vertical].
const SIZE_SHRINK_END = 8;

#desc The control will receive mouse button input events through [method _gui_input] if clicked on. And the control will receive the [signal mouse_entered] and [signal mouse_exited] signals. These events are automatically marked as handled, and they will not propagate further to other controls. This also results in blocking signals in other controls.
const MOUSE_FILTER_STOP = 0;

#desc The control will receive mouse button input events through [method _gui_input] if clicked on. And the control will receive the [signal mouse_entered] and [signal mouse_exited] signals. If this control does not handle the event, the parent control (if any) will be considered, and so on until there is no more parent control to potentially handle it. This also allows signals to fire in other controls. If no control handled it, the event will be passed to `_unhandled_input` for further processing.
const MOUSE_FILTER_PASS = 1;

#desc The control will not receive mouse button input events through [method _gui_input]. The control will also not receive the [signal mouse_entered] nor [signal mouse_exited] signals. This will not block other controls from receiving these events or firing the signals. Ignored events will not be handled automatically.
const MOUSE_FILTER_IGNORE = 2;

#desc The control will grow to the left or top to make up if its minimum size is changed to be greater than its current size on the respective axis.
const GROW_DIRECTION_BEGIN = 0;

#desc The control will grow to the right or bottom to make up if its minimum size is changed to be greater than its current size on the respective axis.
const GROW_DIRECTION_END = 1;

#desc The control will grow in both directions equally to make up if its minimum size is changed to be greater than its current size.
const GROW_DIRECTION_BOTH = 2;

#desc Snaps one of the 4 anchor's sides to the origin of the node's [code]Rect[/code], in the top left. Use it with one of the [code]anchor_*[/code] member variables, like [member anchor_left]. To change all 4 anchors at once, use [method set_anchors_preset].
const ANCHOR_BEGIN = 0;

#desc Snaps one of the 4 anchor's sides to the end of the node's [code]Rect[/code], in the bottom right. Use it with one of the [code]anchor_*[/code] member variables, like [member anchor_left]. To change all 4 anchors at once, use [method set_anchors_preset].
const ANCHOR_END = 1;

#desc Automatic layout direction, determined from the parent control layout direction.
const LAYOUT_DIRECTION_INHERITED = 0;

#desc Automatic layout direction, determined from the current locale.
const LAYOUT_DIRECTION_LOCALE = 1;

#desc Left-to-right layout direction.
const LAYOUT_DIRECTION_LTR = 2;

#desc Right-to-left layout direction.
const LAYOUT_DIRECTION_RTL = 3;

#desc Text writing direction is the same as layout direction.
const TEXT_DIRECTION_INHERITED = 3;

#desc Automatic text writing direction, determined from the current locale and text content.
const TEXT_DIRECTION_AUTO = 0;

#desc Left-to-right text writing direction.
const TEXT_DIRECTION_LTR = 1;

#desc Right-to-left text writing direction.
const TEXT_DIRECTION_RTL = 2;


#desc Anchors the bottom edge of the node to the origin, the center, or the end of its parent control. It changes how the bottom offset updates when the node moves or changes size. You can use one of the [enum Anchor] constants for convenience.
var anchor_bottom: float;

#desc Anchors the left edge of the node to the origin, the center or the end of its parent control. It changes how the left offset updates when the node moves or changes size. You can use one of the [enum Anchor] constants for convenience.
var anchor_left: float;

#desc Anchors the right edge of the node to the origin, the center or the end of its parent control. It changes how the right offset updates when the node moves or changes size. You can use one of the [enum Anchor] constants for convenience.
var anchor_right: float;

#desc Anchors the top edge of the node to the origin, the center or the end of its parent control. It changes how the top offset updates when the node moves or changes size. You can use one of the [enum Anchor] constants for convenience.
var anchor_top: float;

#desc Toggles if any text should automatically change to its translated version depending on the current locale. Note that this will not affect any internal nodes (e.g. the popup of a [MenuButton]).
#desc Also decides if the node's strings should be parsed for POT generation.
var auto_translate: bool;

#desc Enables whether rendering of [CanvasItem] based children should be clipped to this control's rectangle. If [code]true[/code], parts of a child which would be visibly outside of this control's rectangle will not be rendered and won't receive input.
var clip_contents: bool;

#desc The minimum size of the node's bounding rectangle. If you set it to a value greater than (0, 0), the node's bounding rectangle will always have at least this size, even if its content is smaller. If it's set to (0, 0), the node sizes automatically to fit its content, be it a texture or child nodes.
var custom_minimum_size: Vector2i;

#desc The focus access mode for the control (None, Click or All). Only one Control can be focused at the same time, and it will receive keyboard signals.
var focus_mode: int;

#desc Tells Godot which node it should give keyboard focus to if the user presses the down arrow on the keyboard or down on a gamepad by default. You can change the key by editing the [code]ui_down[/code] input action. The node must be a [Control]. If this property is not set, Godot will give focus to the closest [Control] to the bottom of this one.
var focus_neighbor_bottom: NodePath;

#desc Tells Godot which node it should give keyboard focus to if the user presses the left arrow on the keyboard or left on a gamepad by default. You can change the key by editing the [code]ui_left[/code] input action. The node must be a [Control]. If this property is not set, Godot will give focus to the closest [Control] to the left of this one.
var focus_neighbor_left: NodePath;

#desc Tells Godot which node it should give keyboard focus to if the user presses the right arrow on the keyboard or right on a gamepad by default. You can change the key by editing the [code]ui_right[/code] input action. The node must be a [Control]. If this property is not set, Godot will give focus to the closest [Control] to the bottom of this one.
var focus_neighbor_right: NodePath;

#desc Tells Godot which node it should give keyboard focus to if the user presses the top arrow on the keyboard or top on a gamepad by default. You can change the key by editing the [code]ui_top[/code] input action. The node must be a [Control]. If this property is not set, Godot will give focus to the closest [Control] to the bottom of this one.
var focus_neighbor_top: NodePath;

#desc Tells Godot which node it should give keyboard focus to if the user presses [kbd]Tab[/kbd] on a keyboard by default. You can change the key by editing the [code]ui_focus_next[/code] input action.
#desc If this property is not set, Godot will select a "best guess" based on surrounding nodes in the scene tree.
var focus_next: NodePath;

#desc Tells Godot which node it should give keyboard focus to if the user presses [kbd]Shift + Tab[/kbd] on a keyboard by default. You can change the key by editing the [code]ui_focus_prev[/code] input action.
#desc If this property is not set, Godot will select a "best guess" based on surrounding nodes in the scene tree.
var focus_previous: NodePath;

#desc The node's global position, relative to the world (usually to the top-left corner of the window).
var global_position: Vector2;

#desc Controls the direction on the horizontal axis in which the control should grow if its horizontal minimum size is changed to be greater than its current size, as the control always has to be at least the minimum size.
var grow_horizontal: int;

#desc Controls the direction on the vertical axis in which the control should grow if its vertical minimum size is changed to be greater than its current size, as the control always has to be at least the minimum size.
var grow_vertical: int;

#desc Controls layout direction and text writing direction. Right-to-left layouts are necessary for certain languages (e.g. Arabic and Hebrew).
var layout_direction: int;

#desc The default cursor shape for this control. Useful for Godot plugins and applications or games that use the system's mouse cursors.
#desc [b]Note:[/b] On Linux, shapes may vary depending on the cursor theme of the system.
var mouse_default_cursor_shape: int;

#desc Controls whether the control will be able to receive mouse button input events through [method _gui_input] and how these events should be handled. Also controls whether the control can receive the [signal mouse_entered], and [signal mouse_exited] signals. See the constants to learn what each does.
var mouse_filter: int;

#desc When enabled, scroll wheel events processed by [method _gui_input] will be passed to the parent control even if [member mouse_filter] is set to [constant MOUSE_FILTER_STOP]. As it defaults to true, this allows nested scrollable containers to work out of the box.
#desc You should disable it on the root of your UI if you do not want scroll events to go to the [code]_unhandled_input[/code] processing.
var mouse_force_pass_scroll_events: bool;

#desc Distance between the node's bottom edge and its parent control, based on [member anchor_bottom].
#desc Offsets are often controlled by one or multiple parent [Container] nodes, so you should not modify them manually if your node is a direct child of a [Container]. Offsets update automatically when you move or resize the node.
var offset_bottom: float;

#desc Distance between the node's left edge and its parent control, based on [member anchor_left].
#desc Offsets are often controlled by one or multiple parent [Container] nodes, so you should not modify them manually if your node is a direct child of a [Container]. Offsets update automatically when you move or resize the node.
var offset_left: float;

#desc Distance between the node's right edge and its parent control, based on [member anchor_right].
#desc Offsets are often controlled by one or multiple parent [Container] nodes, so you should not modify them manually if your node is a direct child of a [Container]. Offsets update automatically when you move or resize the node.
var offset_right: float;

#desc Distance between the node's top edge and its parent control, based on [member anchor_top].
#desc Offsets are often controlled by one or multiple parent [Container] nodes, so you should not modify them manually if your node is a direct child of a [Container]. Offsets update automatically when you move or resize the node.
var offset_top: float;

#desc By default, the node's pivot is its top-left corner. When you change its [member rotation] or [member scale], it will rotate or scale around this pivot. Set this property to [member size] / 2 to pivot around the Control's center.
var pivot_offset: Vector2;

#desc The node's position, relative to its parent. It corresponds to the rectangle's top-left corner. The property is not affected by [member pivot_offset].
var position: Vector2;

#desc The node's rotation around its pivot, in radians. See [member pivot_offset] to change the pivot's position.
var rotation: float;

#desc The node's scale, relative to its [member size]. Change this property to scale the node around its [member pivot_offset]. The Control's [member tooltip_text] will also scale according to this value.
#desc [b]Note:[/b] This property is mainly intended to be used for animation purposes. Text inside the Control will look pixelated or blurry when the Control is scaled. To support multiple resolutions in your project, use an appropriate viewport stretch mode as described in the [url=$DOCS_URL/tutorials/viewports/multiple_resolutions.html]documentation[/url] instead of scaling Controls individually.
#desc [b]Note:[/b] If the Control node is a child of a [Container] node, the scale will be reset to [code]Vector2(1, 1)[/code] when the scene is instantiated. To set the Control's scale when it's instantiated, wait for one frame using [code]await get_tree().process_frame[/code] then set its [member scale] property.
var scale: Vector2;

#desc The size of the node's bounding rectangle, in pixels. [Container] nodes update this property automatically.
var size: Vector2;

#desc Tells the parent [Container] nodes how they should resize and place the node on the X axis. Use one of the [enum SizeFlags] constants to change the flags. See the constants to learn what each does.
var size_flags_horizontal: int;

#desc If the node and at least one of its neighbors uses the [constant SIZE_EXPAND] size flag, the parent [Container] will let it take more or less space depending on this property. If this node has a stretch ratio of 2 and its neighbor a ratio of 1, this node will take two thirds of the available space.
var size_flags_stretch_ratio: float;

#desc Tells the parent [Container] nodes how they should resize and place the node on the Y axis. Use one of the [enum SizeFlags] constants to change the flags. See the constants to learn what each does.
var size_flags_vertical: int;

#desc The [Theme] resource this node and all its [Control] children use. If a child node has its own [Theme] resource set, theme items are merged with child's definitions having higher priority.
var theme: Theme;

#desc The name of a theme type variation used by this [Control] to look up its own theme items. When empty, the class name of the node is used (e.g. [code]Button[/code] for the [Button] control), as well as the class names of all parent classes (in order of inheritance).
#desc When set, this property gives the highest priority to the type of the specified name. This type can in turn extend another type, forming a dependency chain. See [method Theme.set_type_variation]. If the theme item cannot be found using this type or its base types, lookup falls back on the class names.
#desc [b]Note:[/b] To look up [Control]'s own items use various [code]get_theme_*[/code] methods without specifying [code]theme_type[/code].
#desc [b]Note:[/b] Theme items are looked for in the tree order, from branch to root, where each [Control] node is checked for its [member theme] property. The earliest match against any type/class name is returned. The project-level Theme and the default Theme are checked last.
var theme_type_variation: StringName;

#desc The default tooltip text. The tooltip appears when the user's mouse cursor stays idle over this control for a few moments, provided that the [member mouse_filter] property is not [constant MOUSE_FILTER_IGNORE]. The time required for the tooltip to appear can be changed with the [code]gui/timers/tooltip_delay_sec[/code] option in Project Settings. See also [method get_tooltip].
#desc The tooltip popup will use either a default implementation, or a custom one that you can provide by overriding [method _make_custom_tooltip]. The default tooltip includes a [PopupPanel] and [Label] whose theme properties can be customized using [Theme] methods with the [code]"TooltipPanel"[/code] and [code]"TooltipLabel"[/code] respectively. For example:
#desc [codeblocks]
#desc [gdscript]
#desc var style_box = StyleBoxFlat.new()
#desc style_box.set_bg_color(Color(1, 1, 0))
#desc style_box.set_border_width_all(2)
#desc # We assume here that the `theme` property has been assigned a custom Theme beforehand.
#desc theme.set_stylebox("panel", "TooltipPanel", style_box)
#desc theme.set_color("font_color", "TooltipLabel", Color(0, 1, 1))
#desc [/gdscript]
#desc [csharp]
#desc var styleBox = new StyleBoxFlat();
#desc styleBox.SetBgColor(new Color(1, 1, 0));
#desc styleBox.SetBorderWidthAll(2);
#desc // We assume here that the `Theme` property has been assigned a custom Theme beforehand.
#desc Theme.SetStyleBox("panel", "TooltipPanel", styleBox);
#desc Theme.SetColor("font_color", "TooltipLabel", new Color(0, 1, 1));
#desc [/csharp]
#desc [/codeblocks]
var tooltip_text: String;



#desc Godot calls this method to test if [param data] from a control's [method _get_drag_data] can be dropped at [param at_position]. [param at_position] is local to this control.
#desc This method should only be used to test the data. Process the data in [method _drop_data].
#desc [codeblocks]
#desc [gdscript]
#desc func _can_drop_data(position, data):
#desc # Check position if it is relevant to you
#desc # Otherwise, just check data
#desc return typeof(data) == TYPE_DICTIONARY and data.has("expected")
#desc [/gdscript]
#desc [csharp]
#desc public override bool CanDropData(Vector2 position, object data)
#desc {
#desc // Check position if it is relevant to you
#desc // Otherwise, just check data
#desc return data is Godot.Collections.Dictionary && (data as Godot.Collections.Dictionary).Contains("expected");
#desc }
#desc [/csharp]
#desc [/codeblocks]
virtual const func _can_drop_data(at_position: Vector2, data: Variant) -> bool:
	pass;

#desc Godot calls this method to pass you the [param data] from a control's [method _get_drag_data] result. Godot first calls [method _can_drop_data] to test if [param data] is allowed to drop at [param at_position] where [param at_position] is local to this control.
#desc [codeblocks]
#desc [gdscript]
#desc func _can_drop_data(position, data):
#desc return typeof(data) == TYPE_DICTIONARY and data.has("color")
#desc func _drop_data(position, data):
#desc var color = data["color"]
#desc [/gdscript]
#desc [csharp]
#desc public override bool CanDropData(Vector2 position, object data)
#desc {
#desc return data is Godot.Collections.Dictionary && (data as Godot.Collections.Dictionary).Contains("color");
#desc }
#desc public override void DropData(Vector2 position, object data)
#desc {
#desc Color color = (Color)(data as Godot.Collections.Dictionary)["color"];
#desc }
#desc [/csharp]
#desc [/codeblocks]
virtual func _drop_data(at_position: Vector2, data: Variant) -> void:
	pass;

#desc Godot calls this method to get data that can be dragged and dropped onto controls that expect drop data. Returns [code]null[/code] if there is no data to drag. Controls that want to receive drop data should implement [method _can_drop_data] and [method _drop_data]. [param at_position] is local to this control. Drag may be forced with [method force_drag].
#desc A preview that will follow the mouse that should represent the data can be set with [method set_drag_preview]. A good time to set the preview is in this method.
#desc [codeblocks]
#desc [gdscript]
#desc func _get_drag_data(position):
#desc var mydata = make_data() # This is your custom method generating the drag data.
#desc set_drag_preview(make_preview(mydata)) # This is your custom method generating the preview of the drag data.
#desc return mydata
#desc [/gdscript]
#desc [csharp]
#desc public override object GetDragData(Vector2 position)
#desc {
#desc object mydata = MakeData(); // This is your custom method generating the drag data.
#desc SetDragPreview(MakePreview(mydata)); // This is your custom method generating the preview of the drag data.
#desc return mydata;
#desc }
#desc [/csharp]
#desc [/codeblocks]
virtual const func _get_drag_data() -> Variant:
	pass;

#desc Virtual method to be implemented by the user. Returns the minimum size for this control. Alternative to [member custom_minimum_size] for controlling minimum size via code. The actual minimum size will be the max value of these two (in each axis separately).
#desc If not overridden, defaults to [constant Vector2.ZERO].
#desc [b]Note:[/b] This method will not be called when the script is attached to a [Control] node that already overrides its minimum size (e.g. [Label], [Button], [PanelContainer] etc.). It can only be used with most basic GUI nodes, like [Control], [Container], [Panel] etc.
virtual const func _get_minimum_size() -> Vector2:
	pass;

#desc Virtual method to be implemented by the user. Use this method to process and accept inputs on UI elements. See [method accept_event].
#desc Example: clicking a control.
#desc [codeblocks]
#desc [gdscript]
#desc func _gui_input(event):
#desc if event is InputEventMouseButton:
#desc if event.button_index == MOUSE_BUTTON_LEFT and event.pressed:
#desc print("I've been clicked D:")
#desc [/gdscript]
#desc [csharp]
#desc public override void _GuiInput(InputEvent @event)
#desc {
#desc if (@event is InputEventMouseButton)
#desc {
#desc var mb = @event as InputEventMouseButton;
#desc if (mb.ButtonIndex == (int)ButtonList.Left && mb.Pressed)
#desc {
#desc GD.Print("I've been clicked D:");
#desc }
#desc }
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc The event won't trigger if:
#desc * clicking outside the control (see [method _has_point]);
#desc * control has [member mouse_filter] set to [constant MOUSE_FILTER_IGNORE];
#desc * control is obstructed by another [Control] on top of it, which doesn't have [member mouse_filter] set to [constant MOUSE_FILTER_IGNORE];
#desc * control's parent has [member mouse_filter] set to [constant MOUSE_FILTER_STOP] or has accepted the event;
#desc * it happens outside the parent's rectangle and the parent has either [member clip_contents] enabled.
#desc [b]Note:[/b] Event position is relative to the control origin.
virtual func _gui_input() -> void:
	pass;

#desc Virtual method to be implemented by the user. Returns whether the given [param position] is inside this control.
#desc If not overridden, default behavior is checking if the point is within control's Rect.
#desc [b]Note:[/b] If you want to check if a point is inside the control, you can use [code]get_rect().has_point(point)[/code].
virtual const func _has_point() -> bool:
	pass;

#desc Virtual method to be implemented by the user. Returns a [Control] node that should be used as a tooltip instead of the default one. The [param for_text] includes the contents of the [member tooltip_text] property.
#desc The returned node must be of type [Control] or Control-derived. It can have child nodes of any type. It is freed when the tooltip disappears, so make sure you always provide a new instance (if you want to use a pre-existing node from your scene tree, you can duplicate it and pass the duplicated instance). When [code]null[/code] or a non-Control node is returned, the default tooltip will be used instead.
#desc The returned node will be added as child to a [PopupPanel], so you should only provide the contents of that panel. That [PopupPanel] can be themed using [method Theme.set_stylebox] for the type [code]"TooltipPanel"[/code] (see [member tooltip_text] for an example).
#desc [b]Note:[/b] The tooltip is shrunk to minimal size. If you want to ensure it's fully visible, you might want to set its [member custom_minimum_size] to some non-zero value.
#desc [b]Note:[/b] The node (and any relevant children) should be [member CanvasItem.visible] when returned, otherwise, the viewport that instantiates it will not be able to calculate its minimum size reliably.
#desc Example of usage with a custom-constructed node:
#desc [codeblocks]
#desc [gdscript]
#desc func _make_custom_tooltip(for_text):
#desc var label = Label.new()
#desc label.text = for_text
#desc return label
#desc [/gdscript]
#desc [csharp]
#desc public override Godot.Control _MakeCustomTooltip(String forText)
#desc {
#desc var label = new Label();
#desc label.Text = forText;
#desc return label;
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc Example of usage with a custom scene instance:
#desc [codeblocks]
#desc [gdscript]
#desc func _make_custom_tooltip(for_text):
#desc var tooltip = preload("res://SomeTooltipScene.tscn").instantiate()
#desc tooltip.get_node("Label").text = for_text
#desc return tooltip
#desc [/gdscript]
#desc [csharp]
#desc public override Godot.Control _MakeCustomTooltip(String forText)
#desc {
#desc Node tooltip = ResourceLoader.Load<PackedScene>("res://SomeTooltipScene.tscn").Instantiate();
#desc tooltip.GetNode<Label>("Label").Text = forText;
#desc return tooltip;
#desc }
#desc [/csharp]
#desc [/codeblocks]
virtual const func _make_custom_tooltip() -> Object:
	pass;

#desc User defined BiDi algorithm override function.
#desc Returns [code]Array[/code] of [code]Vector2i[/code] text ranges, in the left-to-right order. Ranges should cover full source [param text] without overlaps. BiDi algorithm will be used on each range separately.
virtual const func _structured_text_parser(args: Array, text: String) -> Vector2i[]:
	pass;

#desc Marks an input event as handled. Once you accept an input event, it stops propagating, even to nodes listening to [method Node._unhandled_input] or [method Node._unhandled_key_input].
func accept_event() -> void:
	pass;

#desc Creates a local override for a theme [Color] with the specified [param name]. Local overrides always take precedence when fetching theme items for the control. An override can be removed with [method remove_theme_color_override].
#desc See also [method get_theme_color].
#desc [b]Example of overriding a label's color and resetting it later:[/b]
#desc [codeblocks]
#desc [gdscript]
#desc # Given the child Label node "MyLabel", override its font color with a custom value.
#desc $MyLabel.add_theme_color_override("font_color", Color(1, 0.5, 0))
#desc # Reset the font color of the child label.
#desc $MyLabel.remove_theme_color_override("font_color")
#desc # Alternatively it can be overridden with the default value from the Label type.
#desc $MyLabel.add_theme_color_override("font_color", get_theme_color("font_color", "Label"))
#desc [/gdscript]
#desc [csharp]
#desc // Given the child Label node "MyLabel", override its font color with a custom value.
#desc GetNode<Label>("MyLabel").AddThemeColorOverride("font_color", new Color(1, 0.5f, 0))
#desc // Reset the font color of the child label.
#desc GetNode<Label>("MyLabel").RemoveThemeColorOverride("font_color")
#desc // Alternatively it can be overridden with the default value from the Label type.
#desc GetNode<Label>("MyLabel").AddThemeColorOverride("font_color", GetThemeColor("font_color", "Label"))
#desc [/csharp]
#desc [/codeblocks]
func add_theme_color_override(name: StringName, color: Color) -> void:
	pass;

#desc Creates a local override for a theme constant with the specified [param name]. Local overrides always take precedence when fetching theme items for the control. An override can be removed with [method remove_theme_constant_override].
#desc See also [method get_theme_constant].
func add_theme_constant_override(name: StringName, constant: int) -> void:
	pass;

#desc Creates a local override for a theme [Font] with the specified [param name]. Local overrides always take precedence when fetching theme items for the control. An override can be removed with [method remove_theme_font_override].
#desc See also [method get_theme_font].
func add_theme_font_override(name: StringName, font: Font) -> void:
	pass;

#desc Creates a local override for a theme font size with the specified [param name]. Local overrides always take precedence when fetching theme items for the control. An override can be removed with [method remove_theme_font_size_override].
#desc See also [method get_theme_font_size].
func add_theme_font_size_override(name: StringName, font_size: int) -> void:
	pass;

#desc Creates a local override for a theme icon with the specified [param name]. Local overrides always take precedence when fetching theme items for the control. An override can be removed with [method remove_theme_icon_override].
#desc See also [method get_theme_icon].
func add_theme_icon_override(name: StringName, texture: Texture2D) -> void:
	pass;

#desc Creates a local override for a theme [StyleBox] with the specified [param name]. Local overrides always take precedence when fetching theme items for the control. An override can be removed with [method remove_theme_stylebox_override].
#desc See also [method get_theme_stylebox].
#desc [b]Example of modifying a property in a StyleBox by duplicating it:[/b]
#desc [codeblocks]
#desc [gdscript]
#desc # The snippet below assumes the child node MyButton has a StyleBoxFlat assigned.
#desc # Resources are shared across instances, so we need to duplicate it
#desc # to avoid modifying the appearance of all other buttons.
#desc var new_stylebox_normal = $MyButton.get_theme_stylebox("normal").duplicate()
#desc new_stylebox_normal.border_width_top = 3
#desc new_stylebox_normal.border_color = Color(0, 1, 0.5)
#desc $MyButton.add_theme_stylebox_override("normal", new_stylebox_normal)
#desc # Remove the stylebox override.
#desc $MyButton.remove_theme_stylebox_override("normal")
#desc [/gdscript]
#desc [csharp]
#desc // The snippet below assumes the child node MyButton has a StyleBoxFlat assigned.
#desc // Resources are shared across instances, so we need to duplicate it
#desc // to avoid modifying the appearance of all other buttons.
#desc StyleBoxFlat newStyleboxNormal = GetNode<Button>("MyButton").GetThemeStylebox("normal").Duplicate() as StyleBoxFlat;
#desc newStyleboxNormal.BorderWidthTop = 3;
#desc newStyleboxNormal.BorderColor = new Color(0, 1, 0.5f);
#desc GetNode<Button>("MyButton").AddThemeStyleboxOverride("normal", newStyleboxNormal);
#desc // Remove the stylebox override.
#desc GetNode<Button>("MyButton").RemoveThemeStyleboxOverride("normal");
#desc [/csharp]
#desc [/codeblocks]
func add_theme_stylebox_override(name: StringName, stylebox: StyleBox) -> void:
	pass;

#desc Prevents [code]*_theme_*_override[/code] methods from emitting [constant NOTIFICATION_THEME_CHANGED] until [method end_bulk_theme_override] is called.
func begin_bulk_theme_override() -> void:
	pass;

#desc Ends a bulk theme override update. See [method begin_bulk_theme_override].
func end_bulk_theme_override() -> void:
	pass;

#desc Finds the next (below in the tree) [Control] that can receive the focus.
func find_next_valid_focus() -> Control:
	pass;

#desc Finds the previous (above in the tree) [Control] that can receive the focus.
func find_prev_valid_focus() -> Control:
	pass;

#desc Forces drag and bypasses [method _get_drag_data] and [method set_drag_preview] by passing [param data] and [param preview]. Drag will start even if the mouse is neither over nor pressed on this control.
#desc The methods [method _can_drop_data] and [method _drop_data] must be implemented on controls that want to receive drop data.
func force_drag(data: Variant, preview: Control) -> void:
	pass;

#desc Returns the anchor for the specified [enum Side]. A getter method for [member anchor_bottom], [member anchor_left], [member anchor_right] and [member anchor_top].
func get_anchor() -> float:
	pass;

#desc Returns [member offset_left] and [member offset_top]. See also [member position].
func get_begin() -> Vector2:
	pass;

#desc Returns combined minimum size from [member custom_minimum_size] and [method get_minimum_size].
func get_combined_minimum_size() -> Vector2:
	pass;

#desc Returns the mouse cursor shape the control displays on mouse hover. See [enum CursorShape].
func get_cursor_shape() -> int:
	pass;

#desc Returns [member offset_right] and [member offset_bottom].
func get_end() -> Vector2:
	pass;

#desc Returns the focus neighbor for the specified [enum Side]. A getter method for [member focus_neighbor_bottom], [member focus_neighbor_left], [member focus_neighbor_right] and [member focus_neighbor_top].
func get_focus_neighbor() -> NodePath:
	pass;

#desc Returns the position and size of the control relative to the top-left corner of the screen. See [member position] and [member size].
func get_global_rect() -> Rect2:
	pass;

#desc Returns the minimum size for this control. See [member custom_minimum_size].
func get_minimum_size() -> Vector2:
	pass;

#desc Returns the anchor for the specified [enum Side]. A getter method for [member offset_bottom], [member offset_left], [member offset_right] and [member offset_top].
func get_offset() -> float:
	pass;

#desc Returns the width/height occupied in the parent control.
func get_parent_area_size() -> Vector2:
	pass;

#desc Returns the parent control node.
func get_parent_control() -> Control:
	pass;

#desc Returns the position and size of the control relative to the top-left corner of the parent Control. See [member position] and [member size].
func get_rect() -> Rect2:
	pass;

#desc Returns the position of this [Control] in global screen coordinates (i.e. taking window position into account). Mostly useful for editor plugins.
#desc Equals to [member global_position] if the window is embedded (see [member Viewport.gui_embed_subwindows]).
#desc Example usage for showing a popup:
#desc [codeblock]
#desc popup_menu.position = get_screen_position() + get_local_mouse_position()
#desc popup_menu.reset_size()
#desc popup_menu.popup()
#desc [/codeblock]
func get_screen_position() -> Vector2:
	pass;

#desc Returns a [Color] from the first matching [Theme] in the tree if that [Theme] has a color item with the specified [param name] and [param theme_type]. If [param theme_type] is omitted the class name of the current control is used as the type, or [member theme_type_variation] if it is defined. If the type is a class name its parent classes are also checked, in order of inheritance. If the type is a variation its base types are checked, in order of dependency, then the control's class name and its parent classes are checked.
#desc For the current control its local overrides are considered first (see [method add_theme_color_override]), then its assigned [member theme]. After the current control, each parent control and its assigned [member theme] are considered; controls without a [member theme] assigned are skipped. If no matching [Theme] is found in the tree, the custom project [Theme] (see [member ProjectSettings.gui/theme/custom]) and the default [Theme] are used (see [ThemeDB]).
#desc [codeblocks]
#desc [gdscript]
#desc func _ready():
#desc # Get the font color defined for the current Control's class, if it exists.
#desc modulate = get_theme_color("font_color")
#desc # Get the font color defined for the Button class.
#desc modulate = get_theme_color("font_color", "Button")
#desc [/gdscript]
#desc [csharp]
#desc public override void _Ready()
#desc {
#desc // Get the font color defined for the current Control's class, if it exists.
#desc Modulate = GetThemeColor("font_color");
#desc // Get the font color defined for the Button class.
#desc Modulate = GetThemeColor("font_color", "Button");
#desc }
#desc [/csharp]
#desc [/codeblocks]
func get_theme_color(name: StringName, theme_type: StringName) -> Color:
	pass;

#desc Returns a constant from the first matching [Theme] in the tree if that [Theme] has a constant item with the specified [param name] and [param theme_type].
#desc See [method get_theme_color] for details.
func get_theme_constant(name: StringName, theme_type: StringName) -> int:
	pass;

#desc Returns the default base scale value from the first matching [Theme] in the tree if that [Theme] has a valid [member Theme.default_base_scale] value.
#desc See [method get_theme_color] for details.
func get_theme_default_base_scale() -> float:
	pass;

#desc Returns the default font from the first matching [Theme] in the tree if that [Theme] has a valid [member Theme.default_font] value.
#desc See [method get_theme_color] for details.
func get_theme_default_font() -> Font:
	pass;

#desc Returns the default font size value from the first matching [Theme] in the tree if that [Theme] has a valid [member Theme.default_font_size] value.
#desc See [method get_theme_color] for details.
func get_theme_default_font_size() -> int:
	pass;

#desc Returns a [Font] from the first matching [Theme] in the tree if that [Theme] has a font item with the specified [param name] and [param theme_type].
#desc See [method get_theme_color] for details.
func get_theme_font(name: StringName, theme_type: StringName) -> Font:
	pass;

#desc Returns a font size from the first matching [Theme] in the tree if that [Theme] has a font size item with the specified [param name] and [param theme_type].
#desc See [method get_theme_color] for details.
func get_theme_font_size(name: StringName, theme_type: StringName) -> int:
	pass;

#desc Returns an icon from the first matching [Theme] in the tree if that [Theme] has an icon item with the specified [param name] and [param theme_type].
#desc See [method get_theme_color] for details.
func get_theme_icon(name: StringName, theme_type: StringName) -> Texture2D:
	pass;

#desc Returns a [StyleBox] from the first matching [Theme] in the tree if that [Theme] has a stylebox item with the specified [param name] and [param theme_type].
#desc See [method get_theme_color] for details.
func get_theme_stylebox(name: StringName, theme_type: StringName) -> StyleBox:
	pass;

#desc Returns the tooltip text [param at_position] in local coordinates, which will typically appear when the cursor is resting over this control. By default, it returns [member tooltip_text].
#desc [b]Note:[/b] This method can be overridden to customise its behaviour. If this method returns an empty [String], no tooltip is displayed.
func get_tooltip() -> String:
	pass;

#desc Creates an [InputEventMouseButton] that attempts to click the control. If the event is received, the control acquires focus.
#desc [codeblocks]
#desc [gdscript]
#desc func _process(delta):
#desc grab_click_focus() #when clicking another Control node, this node will be clicked instead
#desc [/gdscript]
#desc [csharp]
#desc public override void _Process(float delta)
#desc {
#desc GrabClickFocus(); //when clicking another Control node, this node will be clicked instead
#desc }
#desc [/csharp]
#desc [/codeblocks]
func grab_click_focus() -> void:
	pass;

#desc Steal the focus from another control and become the focused control (see [member focus_mode]).
#desc [b]Note[/b]: Using this method together with [method Callable.call_deferred] makes it more reliable, especially when called inside [method Node._ready].
func grab_focus() -> void:
	pass;

#desc Returns [code]true[/code] if this is the current focused control. See [member focus_mode].
func has_focus() -> bool:
	pass;

#desc Returns [code]true[/code] if there is a matching [Theme] in the tree that has a color item with the specified [param name] and [param theme_type].
#desc See [method get_theme_color] for details.
func has_theme_color(name: StringName, theme_type: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if there is a local override for a theme [Color] with the specified [param name] in this [Control] node.
#desc See [method add_theme_color_override].
func has_theme_color_override() -> bool:
	pass;

#desc Returns [code]true[/code] if there is a matching [Theme] in the tree that has a constant item with the specified [param name] and [param theme_type].
#desc See [method get_theme_color] for details.
func has_theme_constant(name: StringName, theme_type: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if there is a local override for a theme constant with the specified [param name] in this [Control] node.
#desc See [method add_theme_constant_override].
func has_theme_constant_override() -> bool:
	pass;

#desc Returns [code]true[/code] if there is a matching [Theme] in the tree that has a font item with the specified [param name] and [param theme_type].
#desc See [method get_theme_color] for details.
func has_theme_font(name: StringName, theme_type: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if there is a local override for a theme [Font] with the specified [param name] in this [Control] node.
#desc See [method add_theme_font_override].
func has_theme_font_override() -> bool:
	pass;

#desc Returns [code]true[/code] if there is a matching [Theme] in the tree that has a font size item with the specified [param name] and [param theme_type].
#desc See [method get_theme_color] for details.
func has_theme_font_size(name: StringName, theme_type: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if there is a local override for a theme font size with the specified [param name] in this [Control] node.
#desc See [method add_theme_font_size_override].
func has_theme_font_size_override() -> bool:
	pass;

#desc Returns [code]true[/code] if there is a matching [Theme] in the tree that has an icon item with the specified [param name] and [param theme_type].
#desc See [method get_theme_color] for details.
func has_theme_icon(name: StringName, theme_type: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if there is a local override for a theme icon with the specified [param name] in this [Control] node.
#desc See [method add_theme_icon_override].
func has_theme_icon_override() -> bool:
	pass;

#desc Returns [code]true[/code] if there is a matching [Theme] in the tree that has a stylebox item with the specified [param name] and [param theme_type].
#desc See [method get_theme_color] for details.
func has_theme_stylebox(name: StringName, theme_type: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if there is a local override for a theme [StyleBox] with the specified [param name] in this [Control] node.
#desc See [method add_theme_stylebox_override].
func has_theme_stylebox_override() -> bool:
	pass;

#desc Returns [code]true[/code] if a drag operation is successful. Alternative to [method Viewport.gui_is_drag_successful].
#desc Best used with [constant Node.NOTIFICATION_DRAG_END].
func is_drag_successful() -> bool:
	pass;

#desc Returns [code]true[/code] if layout is right-to-left.
func is_layout_rtl() -> bool:
	pass;

#desc Give up the focus. No other control will be able to receive keyboard input.
func release_focus() -> void:
	pass;

#desc Removes a local override for a theme [Color] with the specified [param name] previously added by [method add_theme_color_override] or via the Inspector dock.
func remove_theme_color_override() -> void:
	pass;

#desc Removes a local override for a theme constant with the specified [param name] previously added by [method add_theme_constant_override] or via the Inspector dock.
func remove_theme_constant_override() -> void:
	pass;

#desc Removes a local override for a theme [Font] with the specified [param name] previously added by [method add_theme_font_override] or via the Inspector dock.
func remove_theme_font_override() -> void:
	pass;

#desc Removes a local override for a theme font size with the specified [param name] previously added by [method add_theme_font_size_override] or via the Inspector dock.
func remove_theme_font_size_override() -> void:
	pass;

#desc Removes a local override for a theme icon with the specified [param name] previously added by [method add_theme_icon_override] or via the Inspector dock.
func remove_theme_icon_override() -> void:
	pass;

#desc Removes a local override for a theme [StyleBox] with the specified [param name] previously added by [method add_theme_stylebox_override] or via the Inspector dock.
func remove_theme_stylebox_override() -> void:
	pass;

#desc Resets the size to [method get_combined_minimum_size]. This is equivalent to calling [code]set_size(Vector2())[/code] (or any size below the minimum).
func reset_size() -> void:
	pass;

#desc Sets the anchor for the specified [enum Side] to [param anchor]. A setter method for [member anchor_bottom], [member anchor_left], [member anchor_right] and [member anchor_top].
#desc If [param keep_offset] is [code]true[/code], offsets aren't updated after this operation.
#desc If [param push_opposite_anchor] is [code]true[/code] and the opposite anchor overlaps this anchor, the opposite one will have its value overridden. For example, when setting left anchor to 1 and the right anchor has value of 0.5, the right anchor will also get value of 1. If [param push_opposite_anchor] was [code]false[/code], the left anchor would get value 0.5.
func set_anchor(side: int, anchor: float, keep_offset: bool, push_opposite_anchor: bool) -> void:
	pass;

#desc Works the same as [method set_anchor], but instead of [code]keep_offset[/code] argument and automatic update of offset, it allows to set the offset yourself (see [method set_offset]).
func set_anchor_and_offset(side: int, anchor: float, offset: float, push_opposite_anchor: bool) -> void:
	pass;

#desc Sets both anchor preset and offset preset. See [method set_anchors_preset] and [method set_offsets_preset].
func set_anchors_and_offsets_preset(preset: int, resize_mode: int, margin: int) -> void:
	pass;

#desc Sets the anchors to a [param preset] from [enum Control.LayoutPreset] enum. This is the code equivalent to using the Layout menu in the 2D editor.
#desc If [param keep_offsets] is [code]true[/code], control's position will also be updated.
func set_anchors_preset(preset: int, keep_offsets: bool) -> void:
	pass;

#desc Sets [member offset_left] and [member offset_top] at the same time. Equivalent of changing [member position].
func set_begin() -> void:
	pass;

#desc Forwards the handling of this control's drag and drop to [param target] object.
#desc Forwarding can be implemented in the target object similar to the methods [method _get_drag_data], [method _can_drop_data], and [method _drop_data] but with two differences:
#desc 1. The function name must be suffixed with [b]_fw[/b]
#desc 2. The function must take an extra argument that is the control doing the forwarding
#desc [codeblocks]
#desc [gdscript]
#desc # ThisControl.gd
#desc extends Control
#desc export(Control) var target_control
#desc 
#desc func _ready():
#desc set_drag_forwarding(target_control)
#desc 
#desc # TargetControl.gd
#desc extends Control
#desc 
#desc func _can_drop_data_fw(position, data, from_control):
#desc return true
#desc 
#desc func _drop_data_fw(position, data, from_control):
#desc my_handle_data(data) # Your handler method.
#desc 
#desc func _get_drag_data_fw(position, from_control):
#desc set_drag_preview(my_preview)
#desc return my_data()
#desc [/gdscript]
#desc [csharp]
#desc // ThisControl.cs
#desc public class ThisControl : Control
#desc {
#desc [Export]
#desc public Control TargetControl { get; set; }
#desc public override void _Ready()
#desc {
#desc SetDragForwarding(TargetControl);
#desc }
#desc }
#desc 
#desc // TargetControl.cs
#desc public class TargetControl : Control
#desc {
#desc public void CanDropDataFw(Vector2 position, object data, Control fromControl)
#desc {
#desc return true;
#desc }
#desc 
#desc public void DropDataFw(Vector2 position, object data, Control fromControl)
#desc {
#desc MyHandleData(data); // Your handler method.
#desc }
#desc 
#desc public void GetDragDataFw(Vector2 position, Control fromControl)
#desc {
#desc SetDragPreview(MyPreview);
#desc return MyData();
#desc }
#desc }
#desc [/csharp]
#desc [/codeblocks]
func set_drag_forwarding() -> void:
	pass;

#desc Shows the given control at the mouse pointer. A good time to call this method is in [method _get_drag_data]. The control must not be in the scene tree. You should not free the control, and you should not keep a reference to the control beyond the duration of the drag. It will be deleted automatically after the drag has ended.
#desc [codeblocks]
#desc [gdscript]
#desc export (Color, RGBA) var color = Color(1, 0, 0, 1)
#desc 
#desc func _get_drag_data(position):
#desc # Use a control that is not in the tree
#desc var cpb = ColorPickerButton.new()
#desc cpb.color = color
#desc cpb.size = Vector2(50, 50)
#desc set_drag_preview(cpb)
#desc return color
#desc [/gdscript]
#desc [csharp]
#desc [Export]
#desc public Color Color = new Color(1, 0, 0, 1);
#desc 
#desc public override object GetDragData(Vector2 position)
#desc {
#desc // Use a control that is not in the tree
#desc var cpb = new ColorPickerButton();
#desc cpb.Color = Color;
#desc cpb.RectSize = new Vector2(50, 50);
#desc SetDragPreview(cpb);
#desc return Color;
#desc }
#desc [/csharp]
#desc [/codeblocks]
func set_drag_preview() -> void:
	pass;

#desc Sets [member offset_right] and [member offset_bottom] at the same time.
func set_end() -> void:
	pass;

#desc Sets the anchor for the specified [enum Side] to the [Control] at [param neighbor] node path. A setter method for [member focus_neighbor_bottom], [member focus_neighbor_left], [member focus_neighbor_right] and [member focus_neighbor_top].
func set_focus_neighbor(side: int, neighbor: NodePath) -> void:
	pass;

#desc Sets the [member global_position] to given [param position].
#desc If [param keep_offsets] is [code]true[/code], control's anchors will be updated instead of offsets.
func set_global_position(position: Vector2, keep_offsets: bool) -> void:
	pass;

#desc Sets the offset for the specified [enum Side] to [param offset]. A setter method for [member offset_bottom], [member offset_left], [member offset_right] and [member offset_top].
func set_offset(side: int, offset: float) -> void:
	pass;

#desc Sets the offsets to a [param preset] from [enum Control.LayoutPreset] enum. This is the code equivalent to using the Layout menu in the 2D editor.
#desc Use parameter [param resize_mode] with constants from [enum Control.LayoutPresetMode] to better determine the resulting size of the [Control]. Constant size will be ignored if used with presets that change size, e.g. [code]PRESET_LEFT_WIDE[/code].
#desc Use parameter [param margin] to determine the gap between the [Control] and the edges.
func set_offsets_preset(preset: int, resize_mode: int, margin: int) -> void:
	pass;

#desc Sets the [member position] to given [param position].
#desc If [param keep_offsets] is [code]true[/code], control's anchors will be updated instead of offsets.
func set_position(position: Vector2, keep_offsets: bool) -> void:
	pass;

#desc Sets the size (see [member size]).
#desc If [param keep_offsets] is [code]true[/code], control's anchors will be updated instead of offsets.
func set_size(size: Vector2, keep_offsets: bool) -> void:
	pass;

#desc Invalidates the size cache in this node and in parent nodes up to top level. Intended to be used with [method get_minimum_size] when the return value is changed. Setting [member custom_minimum_size] directly calls this method automatically.
func update_minimum_size() -> void:
	pass;

#desc Moves the mouse cursor to [param position], relative to [member position] of this [Control].
func warp_mouse() -> void:
	pass;


