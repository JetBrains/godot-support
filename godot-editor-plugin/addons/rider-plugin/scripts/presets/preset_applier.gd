@tool
extends Object

class_name PresetApplier

var presets_path: String

func _init(p_path: String) -> void:
	presets_path = p_path

func get_preset_key(is_active: bool) -> String:
	return "on" if is_active else "off"

func apply_preset(editor_settings: EditorSettings, is_active: bool) -> void:
	var data: Dictionary = JsonUtils.load_dict_from_file(presets_path)
	if data.is_empty():
		push_warning("Failed to load presets: %s" % presets_path)
		return

	var new_preset_key := get_preset_key(is_active)
	var previous_preset_key := get_preset_key(not is_active)

	if not data.has(new_preset_key):
		push_warning("Preset '%s' not found in presets.json" % new_preset_key)
		return

	var new_preset := data[new_preset_key] as Dictionary
	# Reset keys from previous preset that are missing in the new preset
	if data.has(previous_preset_key):
		var previous_preset := data[previous_preset_key] as Dictionary
		for key in previous_preset:
			if not new_preset.has(key):
				editor_settings.set_setting(str(key), editor_settings.property_get_revert(str(key)))
	# Apply the new preset
	for key in new_preset:
		editor_settings.set_setting(str(key), new_preset[key])
