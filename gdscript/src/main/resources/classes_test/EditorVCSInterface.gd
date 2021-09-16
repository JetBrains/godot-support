extends Object
class_name EditorVCSInterface


func commit(msg: String) -> void:
    pass;
func get_file_diff(file_path: String) -> Array:
    pass;
func get_modified_files_data() -> Dictionary:
    pass;
func get_project_name() -> String:
    pass;
func get_vcs_name() -> String:
    pass;
func initialize(project_root_path: String) -> bool:
    pass;
func is_addon_ready() -> bool:
    pass;
func is_vcs_initialized() -> bool:
    pass;
func shut_down() -> bool:
    pass;
func stage_file(file_path: String) -> void:
    pass;
func unstage_file(file_path: String) -> void:
    pass;
