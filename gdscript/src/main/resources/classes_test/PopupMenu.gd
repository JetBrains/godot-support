extends Popup
class_name PopupMenu

var allow_search: bool;
var hide_on_checkable_item_selection: bool;
var hide_on_item_selection: bool;
var hide_on_state_item_selection: bool;
var submenu_popup_delay: float;

func add_check_item(label: String, id: int, accel: int) -> void:
    pass;
func add_check_shortcut(shortcut: Shortcut, id: int, global: bool) -> void:
    pass;
func add_icon_check_item(texture: Texture2D, label: String, id: int, accel: int) -> void:
    pass;
func add_icon_check_shortcut(texture: Texture2D, shortcut: Shortcut, id: int, global: bool) -> void:
    pass;
func add_icon_item(texture: Texture2D, label: String, id: int, accel: int) -> void:
    pass;
func add_icon_radio_check_item(texture: Texture2D, label: String, id: int, accel: int) -> void:
    pass;
func add_icon_radio_check_shortcut(texture: Texture2D, shortcut: Shortcut, id: int, global: bool) -> void:
    pass;
func add_icon_shortcut(texture: Texture2D, shortcut: Shortcut, id: int, global: bool) -> void:
    pass;
func add_item(label: String, id: int, accel: int) -> void:
    pass;
func add_multistate_item(label: String, max_states: int, default_state: int, id: int, accel: int) -> void:
    pass;
func add_radio_check_item(label: String, id: int, accel: int) -> void:
    pass;
func add_radio_check_shortcut(shortcut: Shortcut, id: int, global: bool) -> void:
    pass;
func add_separator(label: String, id: int) -> void:
    pass;
func add_shortcut(shortcut: Shortcut, id: int, global: bool) -> void:
    pass;
func add_submenu_item(label: String, submenu: String, id: int) -> void:
    pass;
func clear() -> void:
    pass;
func clear_item_opentype_features(idx: int) -> void:
    pass;
func get_current_index() -> int:
    pass;
func get_item_accelerator(idx: int) -> int:
    pass;
func get_item_count() -> int:
    pass;
func get_item_icon(idx: int) -> Texture2D:
    pass;
func get_item_id(idx: int) -> int:
    pass;
func get_item_index(id: int) -> int:
    pass;
func get_item_language(idx: int) -> String:
    pass;
func get_item_metadata(idx: int) -> Variant:
    pass;
func get_item_opentype_feature(idx: int, tag: String) -> int:
    pass;
func get_item_shortcut(idx: int) -> Shortcut:
    pass;
func get_item_submenu(idx: int) -> String:
    pass;
func get_item_text(idx: int) -> String:
    pass;
func get_item_text_direction(idx: int) -> int:
    pass;
func get_item_tooltip(idx: int) -> String:
    pass;
func is_item_checkable(idx: int) -> bool:
    pass;
func is_item_checked(idx: int) -> bool:
    pass;
func is_item_disabled(idx: int) -> bool:
    pass;
func is_item_radio_checkable(idx: int) -> bool:
    pass;
func is_item_separator(idx: int) -> bool:
    pass;
func is_item_shortcut_disabled(idx: int) -> bool:
    pass;
func remove_item(idx: int) -> void:
    pass;
func set_item_accelerator(idx: int, accel: int) -> void:
    pass;
func set_item_as_checkable(idx: int, enable: bool) -> void:
    pass;
func set_item_as_radio_checkable(idx: int, enable: bool) -> void:
    pass;
func set_item_as_separator(idx: int, enable: bool) -> void:
    pass;
func set_item_checked(idx: int, checked: bool) -> void:
    pass;
func set_item_disabled(idx: int, disabled: bool) -> void:
    pass;
func set_item_icon(idx: int, icon: Texture2D) -> void:
    pass;
func set_item_id(idx: int, id: int) -> void:
    pass;
func set_item_language(idx: int, language: String) -> void:
    pass;
func set_item_metadata(idx: int, metadata: Variant) -> void:
    pass;
func set_item_multistate(idx: int, state: int) -> void:
    pass;
func set_item_opentype_feature(idx: int, tag: String, value: int) -> void:
    pass;
func set_item_shortcut(idx: int, shortcut: Shortcut, global: bool) -> void:
    pass;
func set_item_shortcut_disabled(idx: int, disabled: bool) -> void:
    pass;
func set_item_submenu(idx: int, submenu: String) -> void:
    pass;
func set_item_text(idx: int, text: String) -> void:
    pass;
func set_item_text_direction(idx: int, direction: int) -> void:
    pass;
func set_item_tooltip(idx: int, tooltip: String) -> void:
    pass;
func toggle_item_checked(idx: int) -> void:
    pass;
func toggle_item_multistate(idx: int) -> void:
    pass;
