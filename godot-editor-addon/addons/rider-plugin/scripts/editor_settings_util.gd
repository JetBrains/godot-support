@tool
class_name EditorSettingsUtil

# this is rider own setting, not a part of Godot settings
const EXTERNAL_EDITOR_SETTING: String = "text_editor/external/editor"
# this is Godot setting
const EXEC_PATH_SETTING: String = "text_editor/external/exec_path"


static func set_external_editor_path(path: String) -> void:
	var editor_settings := EditorInterface.get_editor_settings()
	editor_settings.set_setting(EXEC_PATH_SETTING, path)

static func get_external_editor_path() -> String:
	var editor_settings := EditorInterface.get_editor_settings()
	if editor_settings.has_setting(EXEC_PATH_SETTING):
		return editor_settings.get_setting(EXEC_PATH_SETTING)
	else:
		return ""

# Returns the current dropdown index, 0 if the setting doesn't exist.
static func get_rider_selector_index() -> int:
	var editor_settings := EditorInterface.get_editor_settings()
	if not editor_settings.has_setting(EXTERNAL_EDITOR_SETTING):
		return 0
	return editor_settings.get_setting(EXTERNAL_EDITOR_SETTING)

static func erase_rider_external_editor_setting() -> void:
	var editor_settings := EditorInterface.get_editor_settings()
	if editor_settings.has_setting(EXTERNAL_EDITOR_SETTING):
		editor_settings.erase(EXTERNAL_EDITOR_SETTING)

# Register a single-entry enum so the persisted int doesn't render as a raw
# number while async detection is in progress. register_rider_installations
# replaces this once detection finishes.
static func show_rider_searching_state() -> void:
	# Order matters: `set` must come before `add_property_info` — the latter
	# errors with `!props.has(pinfo.name)` if the property isn't in `props` yet.
	var editor_settings := EditorInterface.get_editor_settings()
	editor_settings.set(EXTERNAL_EDITOR_SETTING, 0)
	editor_settings.add_property_info({
		"name": EXTERNAL_EDITOR_SETTING,
		"type": TYPE_INT,
		"hint": PROPERTY_HINT_ENUM,
		"hint_string": "Searching — reopen Settings to see the update",
	})

# Register the dropdown with one entry per detected installation (plus a "-"
# placeholder at index 0) and select the given index.
static func register_rider_installations(installations: Array, selected_index: int) -> void:
	var editor_settings := EditorInterface.get_editor_settings()
	var entries: Array = ["-"]
	for element in installations:
		var display_name: String = element.get("display", "")
		entries.append(_escape_enum_label(display_name))
	editor_settings.set(EXTERNAL_EDITOR_SETTING, selected_index)
	editor_settings.add_property_info({
		"name": EXTERNAL_EDITOR_SETTING,
		"type": TYPE_INT,
		"hint": PROPERTY_HINT_ENUM,
		"hint_string": ",".join(entries),
	})

# Comma is the enum delimiter, colon introduces explicit value assignment.
static func _escape_enum_label(s: String) -> String:
	return s.replace(",", " •").replace(":", " -")