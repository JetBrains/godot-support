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
const FEATURE_CONSOLE_WINDOW = 10;
const FEATURE_IME = 11;
const FEATURE_WINDOW_TRANSPARENCY = 12;
const FEATURE_HIDPI = 13;
const FEATURE_ICON = 14;
const FEATURE_NATIVE_ICON = 15;
const FEATURE_ORIENTATION = 16;
const FEATURE_SWAP_BUFFERS = 17;
const MOUSE_MODE_VISIBLE = 0;
const MOUSE_MODE_HIDDEN = 1;
const MOUSE_MODE_CAPTURED = 2;
const MOUSE_MODE_CONFINED = 3;
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
const WINDOW_MODE_FULLSCREEN = 3;
const WINDOW_FLAG_RESIZE_DISABLED = 0;
const WINDOW_FLAG_BORDERLESS = 1;
const WINDOW_FLAG_ALWAYS_ON_TOP = 2;
const WINDOW_FLAG_TRANSPARENT = 3;
const WINDOW_FLAG_NO_FOCUS = 4;
const WINDOW_FLAG_MAX = 5;
const WINDOW_EVENT_MOUSE_ENTER = 0;
const WINDOW_EVENT_MOUSE_EXIT = 1;
const WINDOW_EVENT_FOCUS_IN = 2;
const WINDOW_EVENT_FOCUS_OUT = 3;
const WINDOW_EVENT_CLOSE_REQUEST = 4;
const WINDOW_EVENT_GO_BACK_REQUEST = 5;
const WINDOW_EVENT_DPI_CHANGE = 6;
const VSYNC_DISABLED = 0;
const VSYNC_ENABLED = 1;
const VSYNC_ADAPTIVE = 2;
const VSYNC_MAILBOX = 3;


func alert(text: String, title: String) -> void:
    pass;

func clipboard_get() -> String:
    pass;

func clipboard_set(clipboard: String) -> void:
    pass;

func console_set_visible(console_visible: bool) -> void:
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

func global_menu_add_check_item(menu_root: String, label: String, callback: Callable, tag: Variant) -> void:
    pass;

func global_menu_add_item(menu_root: String, label: String, callback: Callable, tag: Variant) -> void:
    pass;

func global_menu_add_separator(menu_root: String) -> void:
    pass;

func global_menu_add_submenu_item(menu_root: String, label: String, submenu: String) -> void:
    pass;

func global_menu_clear(menu_root: String) -> void:
    pass;

func global_menu_get_item_callback(menu_root: String, idx: int) -> Callable:
    pass;

func global_menu_get_item_submenu(menu_root: String, idx: int) -> String:
    pass;

func global_menu_get_item_tag(menu_root: String, idx: int) -> Variant:
    pass;

func global_menu_get_item_text(menu_root: String, idx: int) -> String:
    pass;

func global_menu_is_item_checkable(menu_root: String, idx: int) -> bool:
    pass;

func global_menu_is_item_checked(menu_root: String, idx: int) -> bool:
    pass;

func global_menu_remove_item(menu_root: String, idx: int) -> void:
    pass;

func global_menu_set_item_callback(menu_root: String, idx: int, callback: Callable) -> void:
    pass;

func global_menu_set_item_checkable(menu_root: String, idx: int, checkable: bool) -> void:
    pass;

func global_menu_set_item_checked(menu_root: String, idx: int, checked: bool) -> void:
    pass;

func global_menu_set_item_submenu(menu_root: String, idx: int, submenu: String) -> void:
    pass;

func global_menu_set_item_tag(menu_root: String, idx: int, tag: Variant) -> void:
    pass;

func global_menu_set_item_text(menu_root: String, idx: int, text: String) -> void:
    pass;

func has_feature(feature: int) -> bool:
    pass;

func ime_get_selection() -> Vector2i:
    pass;

func ime_get_text() -> String:
    pass;

func is_console_visible() -> bool:
    pass;

func keyboard_get_current_layout() -> int:
    pass;

func keyboard_get_layout_count() -> int:
    pass;

func keyboard_get_layout_language(index: int) -> String:
    pass;

func keyboard_get_layout_name(index: int) -> String:
    pass;

func keyboard_set_current_layout(index: int) -> void:
    pass;

func mouse_get_absolute_position() -> Vector2i:
    pass;

func mouse_get_button_state() -> int:
    pass;

func mouse_get_mode() -> int:
    pass;

func mouse_get_position() -> Vector2i:
    pass;

func mouse_set_mode(mouse_mode: int) -> void:
    pass;

func mouse_warp_to_position(position: Vector2i) -> void:
    pass;

func process_events() -> void:
    pass;

func screen_get_dpi(screen: int) -> int:
    pass;

func screen_get_max_scale() -> float:
    pass;

func screen_get_orientation(screen: int) -> int:
    pass;

func screen_get_position(screen: int) -> Vector2i:
    pass;

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

func tablet_get_current_driver() -> String:
    pass;

func tablet_get_driver_count() -> int:
    pass;

func tablet_get_driver_name(idx: int) -> String:
    pass;

func tablet_set_current_driver(name: String) -> void:
    pass;

func virtual_keyboard_get_height() -> int:
    pass;

func virtual_keyboard_hide() -> void:
    pass;

func virtual_keyboard_show(existing_text: String, position: Rect2, multiline: bool, max_length: int, cursor_start: int, cursor_end: int) -> void:
    pass;

func window_attach_instance_id(instance_id: int, window_id: int) -> void:
    pass;

func window_can_draw(window_id: int) -> bool:
    pass;

func window_get_attached_instance_id(window_id: int) -> int:
    pass;

func window_get_current_screen(window_id: int) -> int:
    pass;

func window_get_flag(flag: int, window_id: int) -> bool:
    pass;

func window_get_max_size(window_id: int) -> Vector2i:
    pass;

func window_get_min_size(window_id: int) -> Vector2i:
    pass;

func window_get_mode(window_id: int) -> int:
    pass;

func window_get_position(window_id: int) -> Vector2i:
    pass;

func window_get_real_size(window_id: int) -> Vector2i:
    pass;

func window_get_size(window_id: int) -> Vector2i:
    pass;

func window_get_vsync_mode(window_id: int) -> int:
    pass;

func window_move_to_foreground(window_id: int) -> void:
    pass;

func window_request_attention(window_id: int) -> void:
    pass;

func window_set_current_screen(screen: int, window_id: int) -> void:
    pass;

func window_set_drop_files_callback(callback: Callable, window_id: int) -> void:
    pass;

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

func window_set_min_size(min_size: Vector2i, window_id: int) -> void:
    pass;

func window_set_mode(mode: int, window_id: int) -> void:
    pass;

func window_set_mouse_passthrough(region: PackedVector2Array, window_id: int) -> void:
    pass;

func window_set_position(position: Vector2i, window_id: int) -> void:
    pass;

func window_set_rect_changed_callback(callback: Callable, window_id: int) -> void:
    pass;

func window_set_size(size: Vector2i, window_id: int) -> void:
    pass;

func window_set_title(title: String, window_id: int) -> void:
    pass;

func window_set_transient(window_id: int, parent_window_id: int) -> void:
    pass;

func window_set_vsync_mode(vsync_mode: int, window_id: int) -> void:
    pass;

func window_set_window_event_callback(callback: Callable, window_id: int) -> void:
    pass;

