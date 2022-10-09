extends Object
class_name DisplayServer

const FEATURE_GLOBAL_MENU = 0;

const FEATURE_SUBWINDOWS = 1;

const FEATURE_TOUCHSCREEN = 2;

const FEATURE_MOUSE = 3;

const FEATURE_MOUSE_WARP = 4;

const FEATURE_CLIPBOARD = 5;

const FEATURE_VIRTUAL_KEYBOARD = 6;

const FEATURE_CURSOR_SHAPE = 7;

const FEATURE_CUSTOM_CURSOR_SHAPE = 8;

const FEATURE_NATIVE_DIALOG = 9;

const FEATURE_IME = 10;

const FEATURE_WINDOW_TRANSPARENCY = 11;

const FEATURE_HIDPI = 12;

const FEATURE_ICON = 13;

const FEATURE_NATIVE_ICON = 14;

const FEATURE_ORIENTATION = 15;

const FEATURE_SWAP_BUFFERS = 16;

const FEATURE_CLIPBOARD_PRIMARY = 18;

#desc Display server supports text-to-speech. See [code]tts_*[/code] methods.
const FEATURE_TEXT_TO_SPEECH = 19;

#desc Display server supports expanding window content to the title. See [constant WINDOW_FLAG_EXTEND_TO_TITLE].
const FEATURE_EXTEND_TO_TITLE = 20;

#desc Makes the mouse cursor visible if it is hidden.
const MOUSE_MODE_VISIBLE = 0;

#desc Makes the mouse cursor hidden if it is visible.
const MOUSE_MODE_HIDDEN = 1;

#desc Captures the mouse. The mouse will be hidden and its position locked at the center of the screen.
#desc [b]Note:[/b] If you want to process the mouse's movement in this mode, you need to use [member InputEventMouseMotion.relative].
const MOUSE_MODE_CAPTURED = 2;

#desc Confines the mouse cursor to the game window, and make it visible.
const MOUSE_MODE_CONFINED = 3;

#desc Confines the mouse cursor to the game window, and make it hidden.
const MOUSE_MODE_CONFINED_HIDDEN = 4;

const SCREEN_OF_MAIN_WINDOW = -1;

const MAIN_WINDOW_ID = 0;

const INVALID_WINDOW_ID = -1;

const SCREEN_LANDSCAPE = 0;

const SCREEN_PORTRAIT = 1;

const SCREEN_REVERSE_LANDSCAPE = 2;

const SCREEN_REVERSE_PORTRAIT = 3;

const SCREEN_SENSOR_LANDSCAPE = 4;

const SCREEN_SENSOR_PORTRAIT = 5;

const SCREEN_SENSOR = 6;

#desc Default text virtual keyboard.
const KEYBOARD_TYPE_DEFAULT = 0;

#desc Multiline virtual keyboard.
const KEYBOARD_TYPE_MULTILINE = 1;

#desc Virtual number keypad, useful for PIN entry.
const KEYBOARD_TYPE_NUMBER = 2;

#desc Virtual number keypad, useful for entering fractional numbers.
const KEYBOARD_TYPE_NUMBER_DECIMAL = 3;

#desc Virtual phone number keypad.
const KEYBOARD_TYPE_PHONE = 4;

#desc Virtual keyboard with additional keys to assist with typing email addresses.
const KEYBOARD_TYPE_EMAIL_ADDRESS = 5;

#desc Virtual keyboard for entering a password. On most platforms, this should disable autocomplete and autocapitalization.
#desc [b]Note:[/b] This is not supported on Web. Instead, this behaves identically to [constant KEYBOARD_TYPE_DEFAULT].
const KEYBOARD_TYPE_PASSWORD = 6;

#desc Virtual keyboard with additional keys to assist with typing URLs.
const KEYBOARD_TYPE_URL = 7;

const CURSOR_ARROW = 0;

const CURSOR_IBEAM = 1;

const CURSOR_POINTING_HAND = 2;

const CURSOR_CROSS = 3;

const CURSOR_WAIT = 4;

const CURSOR_BUSY = 5;

const CURSOR_DRAG = 6;

const CURSOR_CAN_DROP = 7;

const CURSOR_FORBIDDEN = 8;

const CURSOR_VSIZE = 9;

const CURSOR_HSIZE = 10;

const CURSOR_BDIAGSIZE = 11;

const CURSOR_FDIAGSIZE = 12;

const CURSOR_MOVE = 13;

const CURSOR_VSPLIT = 14;

const CURSOR_HSPLIT = 15;

const CURSOR_HELP = 16;

const CURSOR_MAX = 17;

const WINDOW_MODE_WINDOWED = 0;

const WINDOW_MODE_MINIMIZED = 1;

const WINDOW_MODE_MAXIMIZED = 2;

#desc Fullscreen window mode. Note that this is not [i]exclusive[/i] fullscreen. On Windows and Linux, a borderless window is used to emulate fullscreen. On macOS, a new desktop is used to display the running project.
#desc Regardless of the platform, enabling fullscreen will change the window size to match the monitor's size. Therefore, make sure your project supports [url=$DOCS_URL/tutorials/rendering/multiple_resolutions.html]multiple resolutions[/url] when enabling fullscreen mode.
const WINDOW_MODE_FULLSCREEN = 3;

#desc Exclusive fullscreen window mode. This mode is implemented on Windows only. On other platforms, it is equivalent to [constant WINDOW_MODE_FULLSCREEN].
#desc Only one window in exclusive fullscreen mode can be visible on a given screen at a time. If multiple windows are in exclusive fullscreen mode for the same screen, the last one being set to this mode takes precedence.
#desc Regardless of the platform, enabling fullscreen will change the window size to match the monitor's size. Therefore, make sure your project supports [url=$DOCS_URL/tutorials/rendering/multiple_resolutions.html]multiple resolutions[/url] when enabling fullscreen mode.
const WINDOW_MODE_EXCLUSIVE_FULLSCREEN = 4;

#desc Window can't be resizing by dragging its resize grip. It's still possible to resize the window using [method window_set_size]. This flag is ignored for full screen windows.
const WINDOW_FLAG_RESIZE_DISABLED = 0;

#desc Window do not have native title bar and other decorations. This flag is ignored for full-screen windows.
const WINDOW_FLAG_BORDERLESS = 1;

#desc Window is floating above other regular windows. This flag is ignored for full-screen windows.
const WINDOW_FLAG_ALWAYS_ON_TOP = 2;

#desc Window background can be transparent.
#desc [b]Note:[/b] This flag has no effect if [member ProjectSettings.display/window/per_pixel_transparency/allowed] is set to [code]false[/code].
const WINDOW_FLAG_TRANSPARENT = 3;

#desc Window can't be focused. No-focus window will ignore all input, except mouse clicks.
const WINDOW_FLAG_NO_FOCUS = 4;

#desc Window is part of menu or [OptionButton] dropdown. This flag can't be changed when window is visible. An active popup window will exclusively receive all input, without stealing focus from its parent. Popup windows are automatically closed when uses click outside it, or when an application is switched. Popup window must have [constant WINDOW_FLAG_TRANSPARENT] set.
const WINDOW_FLAG_POPUP = 5;

#desc Window content is expanded to the full size of the window. Unlike borderless window, the frame is left intact and can be used to resize the window, title bar is transparent, but have minimize/maximize/close buttons.
#desc [b]Note:[/b] This flag is implemented on macOS.
const WINDOW_FLAG_EXTEND_TO_TITLE = 6;

const WINDOW_FLAG_MAX = 7;

const WINDOW_EVENT_MOUSE_ENTER = 0;

const WINDOW_EVENT_MOUSE_EXIT = 1;

const WINDOW_EVENT_FOCUS_IN = 2;

const WINDOW_EVENT_FOCUS_OUT = 3;

const WINDOW_EVENT_CLOSE_REQUEST = 4;

const WINDOW_EVENT_GO_BACK_REQUEST = 5;

const WINDOW_EVENT_DPI_CHANGE = 6;

const WINDOW_EVENT_TITLEBAR_CHANGE = 7;

#desc No vertical synchronization, which means the engine will display frames as fast as possible (tearing may be visible).
const VSYNC_DISABLED = 0;

#desc Default vertical synchronization mode, the image is displayed only on vertical blanking intervals (no tearing is visible).
const VSYNC_ENABLED = 1;

#desc Behaves like [constant VSYNC_DISABLED] when the framerate drops below the screen's refresh rate to reduce stuttering (tearing may be visible), otherwise vertical synchronization is enabled to avoid tearing.
const VSYNC_ADAPTIVE = 2;

#desc Displays the most recent image in the queue on vertical blanking intervals, while rendering to the other images (no tearing is visible).
#desc Although not guaranteed, the images can be rendered as fast as possible, which may reduce input lag.
const VSYNC_MAILBOX = 3;

#desc Display handle:
#desc - Linux: [code]X11::Display*[/code] for the display.
const DISPLAY_HANDLE = 0;

#desc Window handle:
#desc - Windows: [code]HWND[/code] for the window.
#desc - Linux: [code]X11::Window*[/code] for the window.
#desc - macOS: [code]NSWindow*[/code] for the window.
#desc - iOS: [code]UIViewController*[/code] for the view controller.
#desc - Android: [code]jObject[/code] for the activity.
const WINDOW_HANDLE = 1;

#desc Window view:
#desc - macOS: [code]NSView*[/code] for the window main view.
#desc - iOS: [code]UIView*[/code] for the window main view.
const WINDOW_VIEW = 2;

#desc Utterance has begun to be spoken.
const TTS_UTTERANCE_STARTED = 0;

#desc Utterance was successfully finished.
const TTS_UTTERANCE_ENDED = 1;

#desc Utterance was canceled, or TTS service was unable to process it.
const TTS_UTTERANCE_CANCELED = 2;

#desc Utterance reached a word or sentence boundary.
const TTS_UTTERANCE_BOUNDARY = 3;




#desc Returns the user's clipboard as a string if possible.
func clipboard_get() -> String:
	pass;

#desc Returns the user's primary clipboard as a string if possible.
#desc [b]Note:[/b] This method is only implemented on Linux.
func clipboard_get_primary() -> String:
	pass;

#desc Returns [code]true[/code] if there is content on the user's clipboard.
func clipboard_has() -> bool:
	pass;

#desc Sets the user's clipboard content to the given string.
func clipboard_set(clipboard: String) -> void:
	pass;

#desc Sets the user's primary clipboard content to the given string.
#desc [b]Note:[/b] This method is only implemented on Linux.
func clipboard_set_primary(clipboard_primary: String) -> void:
	pass;

func create_sub_window(mode: int, vsync_mode: int, flags: int, rect: Rect2i) -> int:
	pass;

func cursor_get_shape() -> int:
	pass;

func cursor_set_custom_image(cursor: Resource, shape: int, hotspot: Vector2) -> void:
	pass;

func cursor_set_shape(shape: int) -> void:
	pass;

func delete_sub_window(window_id: int) -> void:
	pass;

func dialog_input_text(title: String, description: String, existing_text: String, callback: Callable) -> int:
	pass;

func dialog_show(title: String, description: String, buttons: PackedStringArray, callback: Callable) -> int:
	pass;

func enable_for_stealing_focus(process_id: int) -> void:
	pass;

func force_process_and_drop_events() -> void:
	pass;

#desc Returns OS theme accent color. Returns [code]Color(0, 0, 0, 0)[/code], if accent color is unknown.
#desc [b]Note:[/b] This method is implemented on macOS and Windows.
func get_accent_color() -> Color:
	pass;

#desc Returns an [Array] of [Rect2], each of which is the bounding rectangle for a display cutout or notch. These are non-functional areas on edge-to-edge screens used by cameras and sensors. Returns an empty array if the device does not have cutouts. See also [method get_display_safe_area].
#desc [b]Note:[/b] Currently only implemented on Android. Other platforms will return an empty array even if they do have display cutouts or notches.
func get_display_cutouts() -> Array[Rect2]:
	pass;

#desc Returns the unobscured area of the display where interactive controls should be rendered. See also [method get_display_cutouts].
func get_display_safe_area() -> Rect2i:
	pass;

func get_name() -> String:
	pass;

func get_screen_count() -> int:
	pass;

func get_swap_cancel_ok() -> bool:
	pass;

func get_window_at_screen_position(position: Vector2i) -> int:
	pass;

func get_window_list() -> PackedInt32Array:
	pass;

#desc Adds a new checkable item with text [param label] to the global menu with ID [param menu_root].
#desc Returns index of the inserted item, it's not guaranteed to be the same as [param index] value.
#desc [b]Note:[/b] This method is implemented on macOS.
#desc [b]Supported system menu IDs:[/b]
#desc [codeblock]
#desc "_main" - Main menu (macOS).
#desc "_dock" - Dock popup menu (macOS).
#desc [/codeblock]
func global_menu_add_check_item(menu_root: String, label: String, callback: Callable, key_callback: Callable, tag: Variant, accelerator: int, index: int) -> int:
	pass;

#desc Adds a new checkable item with text [param label] and icon [param icon] to the global menu with ID [param menu_root].
#desc Returns index of the inserted item, it's not guaranteed to be the same as [param index] value.
#desc [b]Note:[/b] This method is implemented on macOS.
#desc [b]Supported system menu IDs:[/b]
#desc [codeblock]
#desc "_main" - Main menu (macOS).
#desc "_dock" - Dock popup menu (macOS).
#desc [/codeblock]
func global_menu_add_icon_check_item(menu_root: String, icon: Texture2D, label: String, callback: Callable, key_callback: Callable, tag: Variant, accelerator: int, index: int) -> int:
	pass;

#desc Adds a new item with text [param label] and icon [param icon] to the global menu with ID [param menu_root].
#desc Returns index of the inserted item, it's not guaranteed to be the same as [param index] value.
#desc [b]Note:[/b] This method is implemented on macOS.
#desc [b]Supported system menu IDs:[/b]
#desc [codeblock]
#desc "_main" - Main menu (macOS).
#desc "_dock" - Dock popup menu (macOS).
#desc [/codeblock]
func global_menu_add_icon_item(menu_root: String, icon: Texture2D, label: String, callback: Callable, key_callback: Callable, tag: Variant, accelerator: int, index: int) -> int:
	pass;

#desc Adds a new radio-checkable item with text [param label] and icon [param icon] to the global menu with ID [param menu_root].
#desc Returns index of the inserted item, it's not guaranteed to be the same as [param index] value.
#desc [b]Note:[/b] Radio-checkable items just display a checkmark, but don't have any built-in checking behavior and must be checked/unchecked manually. See [method global_menu_set_item_checked] for more info on how to control it.
#desc [b]Note:[/b] This method is implemented on macOS.
#desc [b]Supported system menu IDs:[/b]
#desc [codeblock]
#desc "_main" - Main menu (macOS).
#desc "_dock" - Dock popup menu (macOS).
#desc [/codeblock]
func global_menu_add_icon_radio_check_item(menu_root: String, icon: Texture2D, label: String, callback: Callable, key_callback: Callable, tag: Variant, accelerator: int, index: int) -> int:
	pass;

#desc Adds a new item with text [param label] to the global menu with ID [param menu_root].
#desc Returns index of the inserted item, it's not guaranteed to be the same as [param index] value.
#desc [b]Note:[/b] This method is implemented on macOS.
#desc [b]Supported system menu IDs:[/b]
#desc [codeblock]
#desc "_main" - Main menu (macOS).
#desc "_dock" - Dock popup menu (macOS).
#desc [/codeblock]
func global_menu_add_item(menu_root: String, label: String, callback: Callable, key_callback: Callable, tag: Variant, accelerator: int, index: int) -> int:
	pass;

#desc Adds a new item with text [param labe] to the global menu with ID [param menu_root].
#desc Contrarily to normal binary items, multistate items can have more than two states, as defined by [param max_states]. Each press or activate of the item will increase the state by one. The default value is defined by [param default_state].
#desc Returns index of the inserted item, it's not guaranteed to be the same as [param index] value.
#desc [b]Note:[/b] By default, there's no indication of the current item state, it should be changed manually.
#desc [b]Note:[/b] This method is implemented on macOS.
#desc [b]Supported system menu IDs:[/b]
#desc [codeblock]
#desc "_main" - Main menu (macOS).
#desc "_dock" - Dock popup menu (macOS).
#desc [/codeblock]
func global_menu_add_multistate_item(menu_root: String, labe: String, max_states: int, default_state: int, callback: Callable, key_callback: Callable, tag: Variant, accelerator: int, index: int) -> int:
	pass;

#desc Adds a new radio-checkable item with text [param label] to the global menu with ID [param menu_root].
#desc Returns index of the inserted item, it's not guaranteed to be the same as [param index] value.
#desc [b]Note:[/b] Radio-checkable items just display a checkmark, but don't have any built-in checking behavior and must be checked/unchecked manually. See [method global_menu_set_item_checked] for more info on how to control it.
#desc [b]Note:[/b] This method is implemented on macOS.
#desc [b]Supported system menu IDs:[/b]
#desc [codeblock]
#desc "_main" - Main menu (macOS).
#desc "_dock" - Dock popup menu (macOS).
#desc [/codeblock]
func global_menu_add_radio_check_item(menu_root: String, label: String, callback: Callable, key_callback: Callable, tag: Variant, accelerator: int, index: int) -> int:
	pass;

#desc Adds a separator between items to the global menu with ID [param menu_root]. Separators also occupy an index.
#desc Returns index of the inserted item, it's not guaranteed to be the same as [param index] value.
#desc [b]Note:[/b] This method is implemented on macOS.
#desc [b]Supported system menu IDs:[/b]
#desc [codeblock]
#desc "_main" - Main menu (macOS).
#desc "_dock" - Dock popup menu (macOS).
#desc [/codeblock]
func global_menu_add_separator(menu_root: String, index: int) -> int:
	pass;

#desc Adds an item that will act as a submenu of the global menu [param menu_root]. The [param submenu] argument is the ID of the global menu root that will be shown when the item is clicked.
#desc Returns index of the inserted item, it's not guaranteed to be the same as [param index] value.
#desc [b]Note:[/b] This method is implemented on macOS.
#desc [b]Supported system menu IDs:[/b]
#desc [codeblock]
#desc "_main" - Main menu (macOS).
#desc "_dock" - Dock popup menu (macOS).
#desc [/codeblock]
func global_menu_add_submenu_item(menu_root: String, label: String, submenu: String, index: int) -> int:
	pass;

#desc Removes all items from the global menu with ID [param menu_root].
#desc [b]Note:[/b] This method is implemented on macOS.
#desc [b]Supported system menu IDs:[/b]
#desc [codeblock]
#desc "_main" - Main menu (macOS).
#desc "_dock" - Dock popup menu (macOS).
#desc [/codeblock]
func global_menu_clear(menu_root: String) -> void:
	pass;

#desc Returns the accelerator of the item at index [param idx]. Accelerators are special combinations of keys that activate the item, no matter which control is focused.
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_get_item_accelerator(menu_root: String, idx: int) -> int:
	pass;

#desc Returns the callback of the item at index [param idx].
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_get_item_callback(menu_root: String, idx: int) -> Callable:
	pass;

#desc Returns the icon of the item at index [param idx].
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_get_item_icon(menu_root: String, idx: int) -> Texture2D:
	pass;

#desc Returns the horizontal offset of the item at the given [param idx].
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_get_item_indentation_level(menu_root: String, idx: int) -> int:
	pass;

#desc Returns the index of the item with the specified [param tag]. Index is automatically assigned to each item by the engine. Index can not be set manually.
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_get_item_index_from_tag(menu_root: String, tag: Variant) -> int:
	pass;

#desc Returns the index of the item with the specified [param text]. Index is automatically assigned to each item by the engine. Index can not be set manually.
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_get_item_index_from_text(menu_root: String, text: String) -> int:
	pass;

#desc Returns the callback of the item accelerator at index [param idx].
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_get_item_key_callback(menu_root: String, idx: int) -> Callable:
	pass;

#desc Returns number of states of an multistate item. See [method global_menu_add_multistate_item] for details.
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_get_item_max_states(menu_root: String, idx: int) -> int:
	pass;

#desc Returns the state of an multistate item. See [method global_menu_add_multistate_item] for details.
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_get_item_state(menu_root: String, idx: int) -> int:
	pass;

#desc Returns the submenu ID of the item at index [param idx]. See [method global_menu_add_submenu_item] for more info on how to add a submenu.
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_get_item_submenu(menu_root: String, idx: int) -> String:
	pass;

#desc Returns the metadata of the specified item, which might be of any type. You can set it with [method global_menu_set_item_tag], which provides a simple way of assigning context data to items.
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_get_item_tag(menu_root: String, idx: int) -> Variant:
	pass;

#desc Returns the text of the item at index [param idx].
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_get_item_text(menu_root: String, idx: int) -> String:
	pass;

#desc Returns the tooltip associated with the specified index index [param idx].
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_get_item_tooltip(menu_root: String, idx: int) -> String:
	pass;

#desc Returns [code]true[/code] if the item at index [param idx] is checkable in some way, i.e. if it has a checkbox or radio button.
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_is_item_checkable(menu_root: String, idx: int) -> bool:
	pass;

#desc Returns [code]true[/code] if the item at index [param idx] is checked.
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_is_item_checked(menu_root: String, idx: int) -> bool:
	pass;

#desc Returns [code]true[/code] if the item at index [param idx] is disabled. When it is disabled it can't be selected, or its action invoked.
#desc See [method global_menu_set_item_disabled] for more info on how to disable an item.
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_is_item_disabled(menu_root: String, idx: int) -> bool:
	pass;

#desc Returns [code]true[/code] if the item at index [param idx] has radio button-style checkability.
#desc [b]Note:[/b] This is purely cosmetic; you must add the logic for checking/unchecking items in radio groups.
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_is_item_radio_checkable(menu_root: String, idx: int) -> bool:
	pass;

#desc Removes the item at index [param idx] from the global menu [param menu_root].
#desc [b]Note:[/b] The indices of items after the removed item will be shifted by one.
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_remove_item(menu_root: String, idx: int) -> void:
	pass;

#desc Sets the accelerator of the item at index [param idx].
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_set_item_accelerator(menu_root: String, idx: int, keycode: int) -> void:
	pass;

#desc Sets the callback of the item at index [param idx]. Callback is emitted when an item is pressed.
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_set_item_callback(menu_root: String, idx: int, callback: Callable) -> void:
	pass;

#desc Sets whether the item at index [param idx] has a checkbox. If [code]false[/code], sets the type of the item to plain text.
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_set_item_checkable(menu_root: String, idx: int, checkable: bool) -> void:
	pass;

#desc Sets the checkstate status of the item at index [param idx].
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_set_item_checked(menu_root: String, idx: int, checked: bool) -> void:
	pass;

#desc Enables/disables the item at index [param idx]. When it is disabled, it can't be selected and its action can't be invoked.
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_set_item_disabled(menu_root: String, idx: int, disabled: bool) -> void:
	pass;

#desc Replaces the [Texture2D] icon of the specified [param idx].
#desc [b]Note:[/b] This method is implemented on macOS.
#desc [b]Note:[/b] This method is not supported by macOS "_dock" menu items.
func global_menu_set_item_icon(menu_root: String, idx: int, icon: Texture2D) -> void:
	pass;

#desc Sets the horizontal offset of the item at the given [param idx].
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_set_item_indentation_level(menu_root: String, idx: int, level: int) -> void:
	pass;

#desc Sets the callback of the item at index [param idx]. Callback is emitted when its accelerator is activated.
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_set_item_key_callback(menu_root: String, idx: int, key_callback: Callable) -> void:
	pass;

#desc Sets number of state of an multistate item. See [method global_menu_add_multistate_item] for details.
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_set_item_max_states(menu_root: String, idx: int, max_states: int) -> void:
	pass;

#desc Sets the type of the item at the specified index [param idx] to radio button. If [code]false[/code], sets the type of the item to plain text
#desc [b]Note:[/b] This is purely cosmetic; you must add the logic for checking/unchecking items in radio groups.
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_set_item_radio_checkable(menu_root: String, idx: int, checkable: bool) -> void:
	pass;

#desc Sets the state of an multistate item. See [method global_menu_add_multistate_item] for details.
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_set_item_state(menu_root: String, idx: int, state: int) -> void:
	pass;

#desc Sets the submenu of the item at index [param idx]. The submenu is the ID of a global menu root that would be shown when the item is clicked.
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_set_item_submenu(menu_root: String, idx: int, submenu: String) -> void:
	pass;

#desc Sets the metadata of an item, which may be of any type. You can later get it with [method global_menu_get_item_tag], which provides a simple way of assigning context data to items.
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_set_item_tag(menu_root: String, idx: int, tag: Variant) -> void:
	pass;

#desc Sets the text of the item at index [param idx].
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_set_item_text(menu_root: String, idx: int, text: String) -> void:
	pass;

#desc Sets the [String] tooltip of the item at the specified index [param idx].
#desc [b]Note:[/b] This method is implemented on macOS.
func global_menu_set_item_tooltip(menu_root: String, idx: int, tooltip: String) -> void:
	pass;

func has_feature(feature: int) -> bool:
	pass;

func ime_get_selection() -> Vector2i:
	pass;

func ime_get_text() -> String:
	pass;

#desc Returns [code]true[/code] if OS is using dark mode.
#desc [b]Note:[/b] This method is implemented on macOS, Windows and Linux.
func is_dark_mode() -> bool:
	pass;

#desc Returns [code]true[/code] if OS supports dark mode.
#desc [b]Note:[/b] This method is implemented on macOS, Windows and Linux.
func is_dark_mode_supported() -> bool:
	pass;

#desc Returns active keyboard layout index.
#desc [b]Note:[/b] This method is implemented on Linux, macOS and Windows.
func keyboard_get_current_layout() -> int:
	pass;

#desc Converts a physical (US QWERTY) [param keycode] to one in the active keyboard layout.
#desc [b]Note:[/b] This method is implemented on Linux, macOS and Windows.
func keyboard_get_keycode_from_physical(keycode: int) -> int:
	pass;

#desc Returns the number of keyboard layouts.
#desc [b]Note:[/b] This method is implemented on Linux, macOS and Windows.
func keyboard_get_layout_count() -> int:
	pass;

#desc Returns the ISO-639/BCP-47 language code of the keyboard layout at position [param index].
#desc [b]Note:[/b] This method is implemented on Linux, macOS and Windows.
func keyboard_get_layout_language(index: int) -> String:
	pass;

#desc Returns the localized name of the keyboard layout at position [param index].
#desc [b]Note:[/b] This method is implemented on Linux, macOS and Windows.
func keyboard_get_layout_name(index: int) -> String:
	pass;

#desc Sets active keyboard layout.
#desc [b]Note:[/b] This method is implemented on Linux, macOS and Windows.
func keyboard_set_current_layout(index: int) -> void:
	pass;

func mouse_get_button_state() -> int:
	pass;

func mouse_get_mode() -> int:
	pass;

#desc Returns the mouse cursor's current position.
func mouse_get_position() -> Vector2i:
	pass;

func mouse_set_mode(mouse_mode: int) -> void:
	pass;

func process_events() -> void:
	pass;

#desc Returns the dots per inch density of the specified screen. If [param screen] is [constant SCREEN_OF_MAIN_WINDOW] (the default value), a screen with the main window will be used.
#desc [b]Note:[/b] On macOS, returned value is inaccurate if fractional display scaling mode is used.
#desc [b]Note:[/b] On Android devices, the actual screen densities are grouped into six generalized densities:
#desc [codeblock]
#desc ldpi - 120 dpi
#desc mdpi - 160 dpi
#desc hdpi - 240 dpi
#desc xhdpi - 320 dpi
#desc xxhdpi - 480 dpi
#desc xxxhdpi - 640 dpi
#desc [/codeblock]
#desc [b]Note:[/b] This method is implemented on Android, Linux, macOS and Windows. Returns [code]72[/code] on unsupported platforms.
func screen_get_dpi(screen: int) -> int:
	pass;

#desc Returns the greatest scale factor of all screens.
#desc [b]Note:[/b] On macOS returned value is [code]2.0[/code] if there is at least one hiDPI (Retina) screen in the system, and [code]1.0[/code] in all other cases.
#desc [b]Note:[/b] This method is implemented on macOS.
func screen_get_max_scale() -> float:
	pass;

func screen_get_orientation(screen: int) -> int:
	pass;

func screen_get_position(screen: int) -> Vector2i:
	pass;

#desc Returns the current refresh rate of the specified screen. If [param screen] is [constant SCREEN_OF_MAIN_WINDOW] (the default value), a screen with the main window will be used.
#desc [b]Note:[/b] Returns [code]-1.0[/code] if the DisplayServer fails to find the refresh rate for the specified screen. On Web, [method screen_get_refresh_rate] will always return [code]-1.0[/code] as there is no way to retrieve the refresh rate on that platform.
#desc To fallback to a default refresh rate if the method fails, try:
#desc [codeblock]
#desc var refresh_rate = DisplayServer.screen_get_refresh_rate()
#desc if refresh_rate < 0:
#desc refresh_rate = 60.0
#desc [/codeblock]
func screen_get_refresh_rate(screen: int) -> float:
	pass;

#desc Returns the scale factor of the specified screen by index.
#desc [b]Note:[/b] On macOS returned value is [code]2.0[/code] for hiDPI (Retina) screen, and [code]1.0[/code] for all other cases.
#desc [b]Note:[/b] This method is implemented on macOS.
func screen_get_scale(screen: int) -> float:
	pass;

func screen_get_size(screen: int) -> Vector2i:
	pass;

func screen_get_usable_rect(screen: int) -> Rect2i:
	pass;

func screen_is_kept_on() -> bool:
	pass;

func screen_is_touchscreen(screen: int) -> bool:
	pass;

func screen_set_keep_on(enable: bool) -> void:
	pass;

func screen_set_orientation(orientation: int, screen: int) -> void:
	pass;

func set_icon(image: Image) -> void:
	pass;

func set_native_icon(filename: String) -> void:
	pass;

#desc Returns current active tablet driver name.
#desc [b]Note:[/b] This method is implemented on Windows.
func tablet_get_current_driver() -> String:
	pass;

#desc Returns the total number of available tablet drivers.
#desc [b]Note:[/b] This method is implemented on Windows.
func tablet_get_driver_count() -> int:
	pass;

#desc Returns the tablet driver name for the given index.
#desc [b]Note:[/b] This method is implemented on Windows.
func tablet_get_driver_name(idx: int) -> String:
	pass;

#desc Set active tablet driver name.
#desc [b]Note:[/b] This method is implemented on Windows.
func tablet_set_current_driver(name: String) -> void:
	pass;

#desc Returns an [Array] of voice information dictionaries.
#desc Each [Dictionary] contains two [String] entries:
#desc - [code]name[/code] is voice name.
#desc - [code]id[/code] is voice identifier.
#desc - [code]language[/code] is language code in [code]lang_Variant[/code] format. [code]lang[/code] part is a 2 or 3-letter code based on the ISO-639 standard, in lowercase. And [code]Variant[/code] part is an engine dependent string describing country, region or/and dialect.
#desc [b]Note:[/b] This method is implemented on Android, iOS, Web, Linux, macOS, and Windows.
func tts_get_voices() -> Array[Dictionary]:
	pass;

#desc Returns an [PackedStringArray] of voice identifiers for the [param language].
#desc [b]Note:[/b] This method is implemented on Android, iOS, Web, Linux, macOS, and Windows.
func tts_get_voices_for_language(language: String) -> PackedStringArray:
	pass;

#desc Returns [code]true[/code] if the synthesizer is in a paused state.
#desc [b]Note:[/b] This method is implemented on Android, iOS, Web, Linux, macOS, and Windows.
func tts_is_paused() -> bool:
	pass;

#desc Returns [code]true[/code] if the synthesizer is generating speech, or have utterance waiting in the queue.
#desc [b]Note:[/b] This method is implemented on Android, iOS, Web, Linux, macOS, and Windows.
func tts_is_speaking() -> bool:
	pass;

#desc Puts the synthesizer into a paused state.
#desc [b]Note:[/b] This method is implemented on Android, iOS, Web, Linux, macOS, and Windows.
func tts_pause() -> void:
	pass;

#desc Resumes the synthesizer if it was paused.
#desc [b]Note:[/b] This method is implemented on Android, iOS, Web, Linux, macOS, and Windows.
func tts_resume() -> void:
	pass;

#desc Adds a callback, which is called when the utterance has started, finished, canceled or reached a text boundary.
#desc - [code]TTS_UTTERANCE_STARTED[/code], [code]TTS_UTTERANCE_ENDED[/code], and [code]TTS_UTTERANCE_CANCELED[/code] callable's method should take one [int] parameter, the utterance id.
#desc - [code]TTS_UTTERANCE_BOUNDARY[/code] callable's method should take two [int] parameters, the index of the character and the utterance id.
#desc [b]Note:[/b] The granularity of the boundary callbacks is engine dependent.
#desc [b]Note:[/b] This method is implemented on Android, iOS, Web, Linux, macOS, and Windows.
func tts_set_utterance_callback(event: int, callable: Callable) -> void:
	pass;

#desc Adds an utterance to the queue. If [param interrupt] is [code]true[/code], the queue is cleared first.
#desc - [param voice] identifier is one of the [code]"id"[/code] values returned by [method tts_get_voices] or one of the values returned by [method tts_get_voices_for_language].
#desc - [param volume] ranges from [code]0[/code] (lowest) to [code]100[/code] (highest).
#desc - [param pitch] ranges from [code]0.0[/code] (lowest) to [code]2.0[/code] (highest), [code]1.0[/code] is default pitch for the current voice.
#desc - [param rate] ranges from [code]0.1[/code] (lowest) to [code]10.0[/code] (highest), [code]1.0[/code] is a normal speaking rate. Other values act as a percentage relative.
#desc - [param utterance_id] is passed as a parameter to the callback functions.
#desc [b]Note:[/b] On Windows and Linux, utterance [param text] can use SSML markup. SSML support is engine and voice dependent. If the engine does not support SSML, you should strip out all XML markup before calling [method tts_speak].
#desc [b]Note:[/b] The granularity of pitch, rate, and volume is engine and voice dependent. Values may be truncated.
#desc [b]Note:[/b] This method is implemented on Android, iOS, Web, Linux, macOS, and Windows.
func tts_speak(text: String, voice: String, volume: int, pitch: float, rate: float, utterance_id: int, interrupt: bool) -> void:
	pass;

#desc Stops synthesis in progress and removes all utterances from the queue.
#desc [b]Note:[/b] This method is implemented on Android, iOS, Web, Linux, macOS, and Windows.
func tts_stop() -> void:
	pass;

#desc Returns the on-screen keyboard's height in pixels. Returns 0 if there is no keyboard or if it is currently hidden.
func virtual_keyboard_get_height() -> int:
	pass;

#desc Hides the virtual keyboard if it is shown, does nothing otherwise.
func virtual_keyboard_hide() -> void:
	pass;

#desc Shows the virtual keyboard if the platform has one.
#desc [param existing_text] parameter is useful for implementing your own [LineEdit] or [TextEdit], as it tells the virtual keyboard what text has already been typed (the virtual keyboard uses it for auto-correct and predictions).
#desc [param position] parameter is the screen space [Rect2] of the edited text.
#desc [param type] parameter allows configuring which type of virtual keyboard to show.
#desc [param max_length] limits the number of characters that can be entered if different from [code]-1[/code].
#desc [param cursor_start] can optionally define the current text cursor position if [param cursor_end] is not set.
#desc [param cursor_start] and [param cursor_end] can optionally define the current text selection.
#desc [b]Note:[/b] This method is implemented on Android, iOS and Web.
func virtual_keyboard_show(existing_text: String, position: Rect2, type: int, max_length: int, cursor_start: int, cursor_end: int) -> void:
	pass;

#desc Sets the mouse cursor position to the given [param position] relative to an origin at the upper left corner of the currently focused game Window Manager window.
func warp_mouse(position: Vector2i) -> void:
	pass;

func window_attach_instance_id(instance_id: int, window_id: int) -> void:
	pass;

func window_can_draw(window_id: int) -> bool:
	pass;

#desc Returns ID of the active popup window, or [constant INVALID_WINDOW_ID] if there is none.
func window_get_active_popup() -> int:
	pass;

func window_get_attached_instance_id(window_id: int) -> int:
	pass;

func window_get_current_screen(window_id: int) -> int:
	pass;

#desc Returns the current value of the given window's [param flag].
func window_get_flag(flag: int, window_id: int) -> bool:
	pass;

func window_get_max_size(window_id: int) -> Vector2i:
	pass;

func window_get_min_size(window_id: int) -> Vector2i:
	pass;

#desc Returns the mode of the given window.
func window_get_mode(window_id: int) -> int:
	pass;

#desc Returns internal structure pointers for use in plugins.
#desc [b]Note:[/b] This method is implemented on Android, Linux, macOS and Windows.
func window_get_native_handle(handle_type: int, window_id: int) -> int:
	pass;

#desc Returns the bounding box of control, or menu item that was used to open the popup window, in the screen coordinate system.
func window_get_popup_safe_rect(window: int) -> Rect2i:
	pass;

#desc Returns the position of the given window to on the screen.
func window_get_position(window_id: int) -> Vector2i:
	pass;

func window_get_real_size(window_id: int) -> Vector2i:
	pass;

#desc Returns left and right margins of the title that are safe to use (contains no buttons or other elements) when [constant WINDOW_FLAG_EXTEND_TO_TITLE] flag is set.
func window_get_safe_title_margins(window_id: int) -> Vector2i:
	pass;

func window_get_size(window_id: int) -> Vector2i:
	pass;

#desc Returns the V-Sync mode of the given window.
func window_get_vsync_mode(window_id: int) -> int:
	pass;

#desc Returns [code]true[/code], if double-click on a window title should maximize it.
#desc [b]Note:[/b] This method is implemented on macOS.
func window_maximize_on_title_dbl_click() -> bool:
	pass;

#desc Returns [code]true[/code], if double-click on a window title should minimize it.
#desc [b]Note:[/b] This method is implemented on macOS.
func window_minimize_on_title_dbl_click() -> bool:
	pass;

func window_move_to_foreground(window_id: int) -> void:
	pass;

func window_request_attention(window_id: int) -> void:
	pass;

func window_set_current_screen(screen: int, window_id: int) -> void:
	pass;

func window_set_drop_files_callback(callback: Callable, window_id: int) -> void:
	pass;

#desc If set to [code]true[/code], this window will always stay on top of its parent window, parent window will ignore input while this window is opened.
#desc [b]Note:[/b] On macOS, exclusive windows are confined to the same space (virtual desktop or screen) as the parent window.
#desc [b]Note:[/b] This method is implemented on macOS and Windows.
func window_set_exclusive(window_id: int, exclusive: bool) -> void:
	pass;

#desc Enables or disables the given window's given [param flag]. See [enum WindowFlags] for possible values and their behavior.
func window_set_flag(flag: int, enabled: bool, window_id: int) -> void:
	pass;

func window_set_ime_active(active: bool, window_id: int) -> void:
	pass;

func window_set_ime_position(position: Vector2i, window_id: int) -> void:
	pass;

func window_set_input_event_callback(callback: Callable, window_id: int) -> void:
	pass;

func window_set_input_text_callback(callback: Callable, window_id: int) -> void:
	pass;

func window_set_max_size(max_size: Vector2i, window_id: int) -> void:
	pass;

#desc Sets the minimum size for the given window to [param min_size] (in pixels).
#desc [b]Note:[/b] By default, the main window has a minimum size of [code]Vector2i(64, 64)[/code]. This prevents issues that can arise when the window is resized to a near-zero size.
func window_set_min_size(min_size: Vector2i, window_id: int) -> void:
	pass;

#desc Sets window mode for the given window to [param mode]. See [enum WindowMode] for possible values and how each mode behaves.
#desc [b]Note:[/b] Setting the window to fullscreen forcibly sets the borderless flag to [code]true[/code], so make sure to set it back to [code]false[/code] when not wanted.
func window_set_mode(mode: int, window_id: int) -> void:
	pass;

#desc Sets a polygonal region of the window which accepts mouse events. Mouse events outside the region will be passed through.
#desc Passing an empty array will disable passthrough support (all mouse events will be intercepted by the window, which is the default behavior).
#desc [codeblocks]
#desc [gdscript]
#desc # Set region, using Path2D node.
#desc DisplayServer.window_set_mouse_passthrough($Path2D.curve.get_baked_points())
#desc 
#desc # Set region, using Polygon2D node.
#desc DisplayServer.window_set_mouse_passthrough($Polygon2D.polygon)
#desc 
#desc # Reset region to default.
#desc DisplayServer.window_set_mouse_passthrough([])
#desc [/gdscript]
#desc [csharp]
#desc // Set region, using Path2D node.
#desc DisplayServer.WindowSetMousePassthrough(GetNode<Path2D>("Path2D").Curve.GetBakedPoints());
#desc 
#desc // Set region, using Polygon2D node.
#desc DisplayServer.WindowSetMousePassthrough(GetNode<Polygon2D>("Polygon2D").Polygon);
#desc 
#desc // Reset region to default.
#desc DisplayServer.WindowSetMousePassthrough(new Vector2[] {});
#desc [/csharp]
#desc [/codeblocks]
#desc [b]Note:[/b] On Windows, the portion of a window that lies outside the region is not drawn, while on Linux and macOS it is.
#desc [b]Note:[/b] This method is implemented on Linux, macOS and Windows.
func window_set_mouse_passthrough(region: PackedVector2Array, window_id: int) -> void:
	pass;

#desc Sets the bounding box of control, or menu item that was used to open the popup window, in the screen coordinate system. Clicking this area will not auto-close this popup.
func window_set_popup_safe_rect(window: int, rect: Rect2i) -> void:
	pass;

#desc Sets the position of the given window to [param position].
func window_set_position(position: Vector2i, window_id: int) -> void:
	pass;

func window_set_rect_changed_callback(callback: Callable, window_id: int) -> void:
	pass;

#desc Sets the size of the given window to [param size].
func window_set_size(size: Vector2i, window_id: int) -> void:
	pass;

#desc Sets the title of the given window to [param title].
func window_set_title(title: String, window_id: int) -> void:
	pass;

#desc Sets window transient parent. Transient window is will be destroyed with its transient parent and displayed on top of non-exclusive full-screen parent window. Transient windows can't enter full-screen mode.
func window_set_transient(window_id: int, parent_window_id: int) -> void:
	pass;

#desc Sets the V-Sync mode of the given window.
#desc See [enum DisplayServer.VSyncMode] for possible values and how they affect the behavior of your application.
#desc Depending on the platform and used renderer, the engine will fall back to [constant VSYNC_ENABLED], if the desired mode is not supported.
func window_set_vsync_mode(vsync_mode: int, window_id: int) -> void:
	pass;

#desc When [constant WINDOW_FLAG_EXTEND_TO_TITLE] flag is set, set offset to the center of the first titlebar button.
#desc [b]Note:[/b] This flag is implemented on macOS.
func window_set_window_buttons_offset(offset: Vector2i, window_id: int) -> void:
	pass;

func window_set_window_event_callback(callback: Callable, window_id: int) -> void:
	pass;


