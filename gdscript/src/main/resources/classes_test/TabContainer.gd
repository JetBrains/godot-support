extends Container
class_name TabContainer
const ALIGN_LEFT = 0;
const ALIGN_CENTER = 1;
const ALIGN_RIGHT = 2;

var all_tabs_in_front: bool;
var current_tab: int;
var drag_to_rearrange_enabled: bool;
var tab_align: int;
var tabs_visible: bool;
var use_hidden_tabs_for_min_size: bool;

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
