extends Control
class_name Tabs

const ALIGN_LEFT = 0;
const ALIGN_CENTER = 1;
const ALIGN_RIGHT = 2;
const ALIGN_MAX = 3;
const CLOSE_BUTTON_SHOW_NEVER = 0;
const CLOSE_BUTTON_SHOW_ACTIVE_ONLY = 1;
const CLOSE_BUTTON_SHOW_ALWAYS = 2;
const CLOSE_BUTTON_MAX = 3;

var clip_tabs: bool setget set_clip_tabs, get_clip_tabs;
var current_tab: int setget set_current_tab, get_current_tab;
var drag_to_rearrange_enabled: bool setget set_drag_to_rearrange_enabled, get_drag_to_rearrange_enabled;
var scrolling_enabled: bool setget set_scrolling_enabled, get_scrolling_enabled;
var tab_align: int setget set_tab_align, get_tab_align;
var tab_close_display_policy: int setget set_tab_close_display_policy, get_tab_close_display_policy;

func add_tab(title: String, icon: Texture2D) -> void:
    pass;

func clear_tab_opentype_features(tab_idx: int) -> void:
    pass;

func ensure_tab_visible(idx: int) -> void:
    pass;

func get_offset_buttons_visible() -> bool:
    pass;

func get_previous_tab() -> int:
    pass;

func get_select_with_rmb() -> bool:
    pass;

func get_tab_count() -> int:
    pass;

func get_tab_disabled(tab_idx: int) -> bool:
    pass;

func get_tab_icon(tab_idx: int) -> Texture2D:
    pass;

func get_tab_language(tab_idx: int) -> String:
    pass;

func get_tab_offset() -> int:
    pass;

func get_tab_opentype_feature(tab_idx: int, tag: String) -> int:
    pass;

func get_tab_rect(tab_idx: int) -> Rect2:
    pass;

func get_tab_text_direction(tab_idx: int) -> int:
    pass;

func get_tab_title(tab_idx: int) -> String:
    pass;

func get_tabs_rearrange_group() -> int:
    pass;

func move_tab(from: int, to: int) -> void:
    pass;

func remove_tab(tab_idx: int) -> void:
    pass;

func set_select_with_rmb(enabled: bool) -> void:
    pass;

func set_tab_disabled(tab_idx: int, disabled: bool) -> void:
    pass;

func set_tab_icon(tab_idx: int, icon: Texture2D) -> void:
    pass;

func set_tab_language(tab_idx: int, language: String) -> void:
    pass;

func set_tab_opentype_feature(tab_idx: int, tag: String, values: int) -> void:
    pass;

func set_tab_text_direction(tab_idx: int, direction: int) -> void:
    pass;

func set_tab_title(tab_idx: int, title: String) -> void:
    pass;

func set_tabs_rearrange_group(group_id: int) -> void:
    pass;

