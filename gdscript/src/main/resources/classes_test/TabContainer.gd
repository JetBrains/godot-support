extends Container
class_name TabContainer

const ALIGN_LEFT = 0;
const ALIGN_CENTER = 1;
const ALIGN_RIGHT = 2;

var all_tabs_in_front: bool setget set_all_tabs_in_front, is_all_tabs_in_front;
var current_tab: int setget set_current_tab, get_current_tab;
var drag_to_rearrange_enabled: bool setget set_drag_to_rearrange_enabled, get_drag_to_rearrange_enabled;
var tab_align: int setget set_tab_align, get_tab_align;
var tabs_visible: bool setget set_tabs_visible, are_tabs_visible;
var use_hidden_tabs_for_min_size: bool setget set_use_hidden_tabs_for_min_size, get_use_hidden_tabs_for_min_size;

func get_current_tab_control() -> Control:
    pass;

func get_popup() -> Popup:
    pass;

func get_previous_tab() -> int:
    pass;

func get_tab_control(tab_idx: int) -> Control:
    pass;

func get_tab_count() -> int:
    pass;

func get_tab_disabled(tab_idx: int) -> bool:
    pass;

func get_tab_icon(tab_idx: int) -> Texture2D:
    pass;

func get_tab_title(tab_idx: int) -> String:
    pass;

func get_tabs_rearrange_group() -> int:
    pass;

func set_popup(popup: Node) -> void:
    pass;

func set_tab_disabled(tab_idx: int, disabled: bool) -> void:
    pass;

func set_tab_icon(tab_idx: int, icon: Texture2D) -> void:
    pass;

func set_tab_title(tab_idx: int, title: String) -> void:
    pass;

func set_tabs_rearrange_group(group_id: int) -> void:
    pass;

