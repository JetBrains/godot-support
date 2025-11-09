@tool
extends Object

class_name EditorSettingsService

func set_external_editor_path(editor_settings: EditorSettings, path: String) -> void:
	editor_settings.set_setting("text_editor/external/exec_path", path)
	
func has_valid_external_editor_path(editor_settings: EditorSettings) -> bool:
	var has_setting: bool = editor_settings.has_setting("text_editor/external/exec_path")
	if not has_setting: 
		return false
	var path : String = editor_settings.get_setting("text_editor/external/exec_path")
	var exists := not path.is_empty() && (FileAccess.file_exists(path) || DirAccess.dir_exists_absolute(path))
	return exists

func set_use_external_editor(editor_settings: EditorSettings, enabled: bool) -> void:
	editor_settings.set_setting("text_editor/external/use_external_editor", enabled)
