@tool
extends Object

class_name PresetApplier

var presets_path: String

func _init(p_path: String) -> void:
	presets_path = p_path

func apply_preset(editor_settings: EditorSettings, is_active: bool) -> void:
	var data: Dictionary = JsonUtils.load_dict_from_file(presets_path)
	if data.is_empty():
		push_warning("Failed to load presets: %s" % presets_path)
		return

	var new_preset_key := "on" if is_active else "off"
	var previous_preset_key := "on" if not is_active else "off"

	if not data.has(new_preset_key):
		push_warning("Preset '%s' not found in presets.json" % new_preset_key)
		return

	# Reset keys from previous preset that are missing in the new preset
	if data.has(previous_preset_key):
		var previous_preset := data[previous_preset_key] as Dictionary
		var new_preset := data[new_preset_key] as Dictionary
		for key in previous_preset:
			if not new_preset.has(key):
				editor_settings.set_setting(str(key), editor_settings.property_get_revert(str(key)))
	# Apply the new preset
	var preset := data[new_preset_key] as Dictionary
	for key in preset:
		editor_settings.set_setting(str(key), preset[key])
