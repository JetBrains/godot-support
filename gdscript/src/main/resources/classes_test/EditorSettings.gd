extends Resource
class_name EditorSettings
const NOTIFICATION_EDITOR_SETTINGS_CHANGED = 10000;


func add_property_info(info: Dictionary) -> void:
    pass;
func erase(property: String) -> void:
    pass;
func get_favorites() -> PackedStringArray:
    pass;
func get_project_metadata(section: String, key: String, default: Variant) -> Variant:
    pass;
func get_project_settings_dir() -> String:
    pass;
func get_recent_dirs() -> PackedStringArray:
    pass;
func get_setting(name: String) -> Variant:
    pass;
func has_setting(name: String) -> bool:
    pass;
func property_can_revert(name: String) -> bool:
    pass;
func property_get_revert(name: String) -> Variant:
    pass;
func set_builtin_action_override(name: String, actions_list: Array) -> void:
    pass;
func set_favorites(dirs: PackedStringArray) -> void:
    pass;
func set_initial_value(name: StringName, value: Variant, update_current: bool) -> void:
    pass;
func set_project_metadata(section: String, key: String, data: Variant) -> void:
    pass;
func set_recent_dirs(dirs: PackedStringArray) -> void:
    pass;
func set_setting(name: String, value: Variant) -> void:
    pass;
