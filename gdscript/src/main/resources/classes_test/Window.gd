#brief Base class for all windows.
#desc A node that creates a window. The window can either be a native system window or embedded inside another [Window] (see [member Viewport.gui_embed_subwindows]).
#desc At runtime, [Window]s will not close automatically when requested. You need to handle it manually using [signal close_requested] (this applies both to clicking close button and clicking outside popup).
class_name Window

#desc Emitted when [Window]'s visibility changes, right before [signal visibility_changed].
const NOTIFICATION_VISIBILITY_CHANGED = 30;

#desc Sent when the node needs to refresh its theme items. This happens in one of the following cases:
#desc - The [member theme] property is changed on this node or any of its ancestors.
#desc - The [member theme_type_variation] property is changed on this node.
#desc - The node enters the scene tree.
#desc [b]Note:[/b] As an optimization, this notification won't be sent from changes that occur while this node is outside of the scene tree. Instead, all of the theme item updates can be applied at once when the node enters the scene tree.
const NOTIFICATION_THEME_CHANGED = 32;

#desc Windowed mode, i.e. [Window] doesn't occupy whole screen (unless set to the size of the screen).
const MODE_WINDOWED = 0;

#desc Minimized window mode, i.e. [Window] is not visible and available on window manager's window list. Normally happens when the minimize button is presesd.
const MODE_MINIMIZED = 1;

#desc Maximized window mode, i.e. [Window] will occupy whole screen area except task bar and still display its borders. Normally happens when the minimize button is presesd.
const MODE_MAXIMIZED = 2;

#desc Fullscreen window mode. Note that this is not [i]exclusive[/i] fullscreen. On Windows and Linux, a borderless window is used to emulate fullscreen. On macOS, a new desktop is used to display the running project.
#desc Regardless of the platform, enabling fullscreen will change the window size to match the monitor's size. Therefore, make sure your project supports [url=$DOCS_URL/tutorials/rendering/multiple_resolutions.html]multiple resolutions[/url] when enabling fullscreen mode.
const MODE_FULLSCREEN = 3;

#desc Exclusive fullscreen window mode. This mode is implemented on Windows only. On other platforms, it is equivalent to [constant MODE_FULLSCREEN].
#desc Only one window in exclusive fullscreen mode can be visible on a given screen at a time. If multiple windows are in exclusive fullscreen mode for the same screen, the last one being set to this mode takes precedence.
#desc Regardless of the platform, enabling fullscreen will change the window size to match the monitor's size. Therefore, make sure your project supports [url=$DOCS_URL/tutorials/rendering/multiple_resolutions.html]multiple resolutions[/url] when enabling fullscreen mode.
const MODE_EXCLUSIVE_FULLSCREEN = 4;

#desc The window's ability to be resized. Set with [member unresizable].
const FLAG_RESIZE_DISABLED = 0;

#desc Borderless window. Set with [member borderless].
const FLAG_BORDERLESS = 1;

#desc Flag for making the window always on top of all other windows. Set with [member always_on_top].
const FLAG_ALWAYS_ON_TOP = 2;

#desc Flag for per-pixel transparency. Set with [member transparent].
const FLAG_TRANSPARENT = 3;

#desc The window's ability to gain focus. Set with [member unfocusable].
const FLAG_NO_FOCUS = 4;

#desc Whether the window is popup or a regular window. Set with [member popup_window].
const FLAG_POPUP = 5;

#desc Window contents is expanded to the full size of the window, window title bar is transparent.
const FLAG_EXTEND_TO_TITLE = 6;

#desc Max value of the [enum Flags].
const FLAG_MAX = 7;

#desc The content will not be scaled to match the [Window]'s size.
const CONTENT_SCALE_MODE_DISABLED = 0;

#desc The content will be rendered at the target size. This is more performance-expensive than [constant CONTENT_SCALE_MODE_VIEWPORT], but provides better results.
const CONTENT_SCALE_MODE_CANVAS_ITEMS = 1;

#desc The content will be rendered at the base size and then scaled to the target size. More performant than [constant CONTENT_SCALE_MODE_CANVAS_ITEMS], but results in pixelated image.
const CONTENT_SCALE_MODE_VIEWPORT = 2;

#desc The aspect will be ignored. Scaling will simply stretch the content to fit the target size.
const CONTENT_SCALE_ASPECT_IGNORE = 0;

#desc The content's aspect will be preserved. If the target size has different aspect from the base one, the image will be centered and black bars will appear on left and right sides.
const CONTENT_SCALE_ASPECT_KEEP = 1;

#desc The content can be expanded vertically. Scaling horizontally will result in keeping the width ratio and then black bars on left and right sides.
const CONTENT_SCALE_ASPECT_KEEP_WIDTH = 2;

#desc The content can be expanded horizontally. Scaling vertically will result in keeping the height ratio and then black bars on top and bottom sides.
const CONTENT_SCALE_ASPECT_KEEP_HEIGHT = 3;

#desc The content's aspect will be preserved. If the target size has different aspect from the base one, the content will stay in the to-left corner and add an extra visible area in the stretched space.
const CONTENT_SCALE_ASPECT_EXPAND = 4;

#desc Automatic layout direction, determined from the parent window layout direction.
const LAYOUT_DIRECTION_INHERITED = 0;

#desc Automatic layout direction, determined from the current locale.
const LAYOUT_DIRECTION_LOCALE = 1;

#desc Left-to-right layout direction.
const LAYOUT_DIRECTION_LTR = 2;

#desc Right-to-left layout direction.
const LAYOUT_DIRECTION_RTL = 3;


#desc If [code]true[/code], the window will be on top of all other windows. Does not work if [member transient] is enabled.
var always_on_top: bool;

#desc Toggles if any text should automatically change to its translated version depending on the current locale.
var auto_translate: bool;

#desc If [code]true[/code], the window will have no borders.
var borderless: bool;

#desc Specifies how the content's aspect behaves when the [Window] is resized. The base aspect is determined by [member content_scale_size].
var content_scale_aspect: int;

#desc Specifies the base scale of [Window]'s content when its [member size] is equal to [member content_scale_size].
var content_scale_factor: float;

#desc Specifies how the content is scaled when the [Window] is resized.
var content_scale_mode: int;

#desc Base size of the content (i.e. nodes that are drawn inside the window). If non-zero, [Window]'s content will be scaled when the window is resized to a different size.
var content_scale_size: Vector2i;

#desc The screen the window is currently on.
var current_screen: int;

#desc If [code]true[/code], the [Window] will be in exclusive mode. Exclusive windows are always on top of their parent and will block all input going to the parent [Window].
#desc Needs [member transient] enabled to work.
var exclusive: bool;

#desc If [code]true[/code], the [Window] contents is expanded to the full size of the window, window title bar is transparent.
var extend_to_title: bool;

#desc If non-zero, the [Window] can't be resized to be bigger than this size.
#desc [b]Note:[/b] This property will be ignored if the value is lower than [member min_size].
var max_size: Vector2i;

#desc If non-zero, the [Window] can't be resized to be smaller than this size.
#desc [b]Note:[/b] This property will be ignored in favor of [method get_contents_minimum_size] if [member wrap_controls] is enabled and if its size is bigger.
var min_size: Vector2i;

#desc Set's the window's current mode.
#desc [b]Note:[/b] Fullscreen mode is not exclusive fullscreen on Windows and Linux.
var mode: int;

#desc If [code]true[/code], the [Window] will be considered a popup. Popups are sub-windows that don't show as separate windows in system's window manager's window list and will send close request when anything is clicked outside of them (unless [member exclusive] is enabled).
var popup_window: bool;

#desc The window's position in pixels.
var position: Vector2i;

#desc The window's size in pixels.
var size: Vector2i;

#desc The [Theme] resource that determines the style of the underlying [Control] nodes.
#desc [Window] styles will have no effect unless the window is embedded.
var theme: Theme;

#desc The name of a theme type variation used by this [Window] to look up its own theme items. See [member Control.theme_type_variation] for more details.
var theme_type_variation: StringName;

#desc The window's title. If the [Window] is non-embedded, title styles set in [Theme] will have no effect.
var title: String;

#desc If [code]true[/code], the [Window] is transient, i.e. it's considered a child of another [Window]. Transient windows can't be in fullscreen mode and will return focus to their parent when closed.
#desc Note that behavior might be different depending on the platform.
var transient: bool;

#desc If [code]true[/code], the [Window]'s background can be transparent. This is best used with embedded windows.
#desc [b]Note:[/b] This flag has no effect if [member ProjectSettings.display/window/per_pixel_transparency/allowed] is set to [code]false[/code].
var transparent: bool;

#desc If [code]true[/code], the [Window] can't be focused nor interacted with. It can still be visible.
var unfocusable: bool;

#desc If [code]true[/code], the window can't be resized. Minimize and maximize buttons are disabled.
var unresizable: bool;

#desc If [code]true[/code], the window is visible.
var visible: bool;

#desc If [code]true[/code], the window's size will automatically update when a child node is added or removed, ignoring [member min_size] if the new size is bigger.
#desc If [code]false[/code], you need to call [method child_controls_changed] manually.
var wrap_controls: bool;



#desc Returns whether the window is being drawn to the screen.
func can_draw() -> bool:
	pass;

#desc Requests an update of the [Window] size to fit underlying [Control] nodes.
func child_controls_changed() -> void:
	pass;

#desc Returns the combined minimum size from the child [Control] nodes of the window. Use [method child_controls_changed] to update it when children nodes have changed.
func get_contents_minimum_size() -> Vector2:
	pass;

#desc Returns [code]true[/code] if the [param flag] is set.
func get_flag() -> bool:
	pass;

#desc Returns layout direction and text writing direction.
func get_layout_direction() -> int:
	pass;

#desc Returns the window's size including its border.
func get_real_size() -> Vector2i:
	pass;

#desc Returns the [Color] at [param name] if the theme has [param theme_type].
#desc See [method Control.get_theme_color] for more details.
func get_theme_color(name: StringName, theme_type: StringName) -> Color:
	pass;

#desc Returns the constant at [param name] if the theme has [param theme_type].
#desc See [method Control.get_theme_color] for more details.
func get_theme_constant(name: StringName, theme_type: StringName) -> int:
	pass;

#desc Returns the default base scale defined in the attached [Theme].
#desc See [member Theme.default_base_scale] for more details.
func get_theme_default_base_scale() -> float:
	pass;

#desc Returns the default [Font] defined in the attached [Theme].
#desc See [member Theme.default_font] for more details.
func get_theme_default_font() -> Font:
	pass;

#desc Returns the default font size defined in the attached [Theme].
#desc See [member Theme.default_font_size] for more details.
func get_theme_default_font_size() -> int:
	pass;

#desc Returns the [Font] at [param name] if the theme has [param theme_type].
#desc See [method Control.get_theme_color] for more details.
func get_theme_font(name: StringName, theme_type: StringName) -> Font:
	pass;

#desc Returns the font size at [param name] if the theme has [param theme_type].
#desc See [method Control.get_theme_color] for more details.
func get_theme_font_size(name: StringName, theme_type: StringName) -> int:
	pass;

#desc Returns the icon at [param name] if the theme has [param theme_type].
#desc See [method Control.get_theme_color] for more details.
func get_theme_icon(name: StringName, theme_type: StringName) -> Texture2D:
	pass;

#desc Returns the [StyleBox] at [param name] if the theme has [param theme_type].
#desc See [method Control.get_theme_color] for more details.
func get_theme_stylebox(name: StringName, theme_type: StringName) -> StyleBox:
	pass;

#desc Causes the window to grab focus, allowing it to receive user input.
func grab_focus() -> void:
	pass;

#desc Returns [code]true[/code] if the window is focused.
func has_focus() -> bool:
	pass;

#desc Returns [code]true[/code] if [Color] with [param name] is in [param theme_type].
func has_theme_color(name: StringName, theme_type: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if constant with [param name] is in [param theme_type].
func has_theme_constant(name: StringName, theme_type: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if [Font] with [param name] is in [param theme_type].
func has_theme_font(name: StringName, theme_type: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if font size with [param name] is in [param theme_type].
func has_theme_font_size(name: StringName, theme_type: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if icon with [param name] is in [param theme_type].
func has_theme_icon(name: StringName, theme_type: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if [StyleBox] with [param name] is in [param theme_type].
func has_theme_stylebox(name: StringName, theme_type: StringName) -> bool:
	pass;

#desc Hides the window. This is not the same as minimized state. Hidden window can't be interacted with and needs to be made visible with [method show].
func hide() -> void:
	pass;

#desc Returns [code]true[/code] if the window is currently embedded in another window.
func is_embedded() -> bool:
	pass;

#desc Returns [code]true[/code] if layout is right-to-left.
func is_layout_rtl() -> bool:
	pass;

#desc Returns [code]true[/code] if the window can be maximized (the maximize button is enabled).
func is_maximize_allowed() -> bool:
	pass;

#desc Returns [code]true[/code] if font oversampling is enabled. See [method set_use_font_oversampling].
func is_using_font_oversampling() -> bool:
	pass;

#desc Moves the [Window] on top of other windows and focuses it.
func move_to_foreground() -> void:
	pass;

#desc Shows the [Window] and makes it transient (see [member transient]). If [param rect] is provided, it will be set as the [Window]'s size.
#desc Fails if called on the main window.
func popup() -> void:
	pass;

#desc Popups the [Window] at the center of the current screen, with optionally given minimum size.
#desc If the [Window] is embedded, it will be centered in the parent [Viewport] instead.
func popup_centered() -> void:
	pass;

#desc Popups the [Window] centered inside its parent [Window].
#desc [code]fallback_ratio[/code] determines the maximum size of the [Window], in relation to its parent.
func popup_centered_clamped(minsize: Vector2i, fallback_ratio: float) -> void:
	pass;

#desc Popups the [Window] centered inside its parent [Window] and sets its size as a [param ratio] of parent's size.
func popup_centered_ratio() -> void:
	pass;

#desc Popups the [Window] with a position shifted by parent [Window]'s position.
#desc If the [Window] is embedded, has the same effect as [method popup].
func popup_on_parent() -> void:
	pass;

#desc Tells the OS that the [Window] needs an attention. This makes the window stand out in some way depending on the system, e.g. it might blink on the task bar.
func request_attention() -> void:
	pass;

#desc Resets the size to the minimum size, which is the max of [member min_size] and (if [member wrap_controls] is enabled) [method get_contents_minimum_size]. This is equivalent to calling [code]set_size(Vector2i())[/code] (or any size below the minimum).
func reset_size() -> void:
	pass;

#desc Sets a specified window flag.
func set_flag(flag: int, enabled: bool) -> void:
	pass;

#desc If [param active] is [code]true[/code], enables system's native IME (Input Method Editor).
func set_ime_active() -> void:
	pass;

#desc Moves IME to the given position.
func set_ime_position() -> void:
	pass;

#desc Sets layout direction and text writing direction. Right-to-left layouts are necessary for certain languages (e.g. Arabic and Hebrew).
func set_layout_direction() -> void:
	pass;

#desc Enables font oversampling. This makes fonts look better when they are scaled up.
func set_use_font_oversampling() -> void:
	pass;

#desc Makes the [Window] appear. This enables interactions with the [Window] and doesn't change any of its property other than visibility (unlike e.g. [method popup]).
func show() -> void:
	pass;


