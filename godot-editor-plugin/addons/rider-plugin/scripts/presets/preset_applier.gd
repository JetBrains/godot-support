@tool
extends Object

class_name PresetApplier

var presets_path: String

func _init(p_path: String) -> void:
	presets_path = p_path

func apply_preset(editor_settings: EditorSettings, active: String) -> void:
	var data: Dictionary = JsonUtils.load_dict_from_file(presets_path)
	if data.is_empty():
		push_warning("Failed to load presets: %s" % presets_path)
		return
	if not data.has(active):
		push_warning("Preset '%s' not found in presets.json" % active)
		return
	var preset := data[active] as Dictionary
	for key in preset:
		editor_settings.set_setting(str(key), preset[key])
