extends Button
class_name OptionButton


var action_mode: int setget set_action_mode, get_action_mode;
var align: int setget set_text_align, get_text_align;
var selected: int setget _select_int, get_selected;
var toggle_mode: bool setget set_toggle_mode, is_toggle_mode;

func add_icon_item(texture: Texture2D, label: String, id: int) -> void:
    pass;

func add_item(label: String, id: int) -> void:
    pass;

func add_separator() -> void:
    pass;

func clear() -> void:
    pass;

func get_item_count() -> int:
    pass;

func get_item_icon(idx: int) -> Texture2D:
    pass;

func get_item_id(idx: int) -> int:
    pass;

func get_item_index(id: int) -> int:
    pass;

func get_item_metadata(idx: int) -> Variant:
    pass;

func get_item_text(idx: int) -> String:
    pass;

func get_popup() -> PopupMenu:
    pass;

func get_selected_id() -> int:
    pass;

func get_selected_metadata() -> Variant:
    pass;

func is_item_disabled(idx: int) -> bool:
    pass;

func remove_item(idx: int) -> void:
    pass;

func select(idx: int) -> void:
    pass;

func set_item_disabled(idx: int, disabled: bool) -> void:
    pass;

func set_item_icon(idx: int, texture: Texture2D) -> void:
    pass;

func set_item_id(idx: int, id: int) -> void:
    pass;

func set_item_metadata(idx: int, metadata: Variant) -> void:
    pass;

func set_item_text(idx: int, text: String) -> void:
    pass;

