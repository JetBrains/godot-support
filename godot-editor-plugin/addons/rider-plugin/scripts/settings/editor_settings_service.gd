@tool
extends Object

class_name EditorSettingsService

func set_external_editor_path(editor_settings: EditorSettings, path: String) -> void:
	editor_settings.set_setting("text_editor/external/exec_path", path)

func set_use_external_editor(editor_settings: EditorSettings, enabled: bool) -> void:
	editor_settings.set_setting("text_editor/external/use_external_editor", enabled)
