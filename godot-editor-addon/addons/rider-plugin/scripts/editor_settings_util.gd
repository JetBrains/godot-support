@tool
class_name EditorSettingsUtil

# this is rider own setting, not a part of Godot settings
const EXTERNAL_EDITOR_SETTING: String = "text_editor/external/editor"
# this is Godot setting
const EXEC_PATH_SETTING: String = "text_editor/external/exec_path"
const DOTNET_EXTERNAL_EDITOR_SETTING: String = "dotnet/editor/external_editor"
const DOTNET_EDITOR_PATH_SETTING: String = "dotnet/editor/editor_path_optional"
const DOTNET_RIDER_EDITOR_LABEL: String = "JetBrains Rider"

# Godot doesn't provide a  nice way to define a static signal, 
# so this is a workaround
class RiderInstallSignalEmitter extends RefCounted:
	signal rider_install_changed(rider_path: String)

static var _signal_emitter := RiderInstallSignalEmitter.new()
static var _dotnet_rider_editor_id: int = -1

static var get_rider_install_change_signal: Signal:
	get():
		return _signal_emitter.rider_install_changed


static func set_external_editor_path(path: String) -> void:
	var editor_settings := EditorInterface.get_editor_settings()
	editor_settings.set_setting(EXEC_PATH_SETTING, path)
	notify_rider_install_changed(path)

static func notify_rider_install_changed(path: String) -> void:
	_signal_emitter.rider_install_changed.emit(path)

static func get_external_editor_path() -> String:
	var editor_settings := EditorInterface.get_editor_settings()
	if editor_settings.has_setting(EXEC_PATH_SETTING):
		return editor_settings.get_setting(EXEC_PATH_SETTING)
	else:
		return ""

# The external editor doesn't have to be Rider; this is a name-based heuristic.
static func is_rider_path(path: String) -> bool:
	return path != "" && path.to_lower().contains("rider")

static func resolve_rider_executable(path: String) -> String:
	var normalized_path := path.trim_suffix("/")
	if normalized_path.to_lower().ends_with(".app"):
		return normalized_path.path_join("Contents").path_join("MacOS").path_join("rider")
	return path

static func has_dotnet_external_editor() -> bool:
	var editor_settings := EditorInterface.get_editor_settings()
	return (editor_settings.has_setting(DOTNET_EXTERNAL_EDITOR_SETTING)
		and editor_settings.has_setting(DOTNET_EDITOR_PATH_SETTING)
		and _get_dotnet_rider_editor_id() >= 0)

static func _get_dotnet_rider_editor_id() -> int:
	if _dotnet_rider_editor_id >= 0:
		return _dotnet_rider_editor_id
	var editor_settings := EditorInterface.get_editor_settings()
	for property in editor_settings.get_property_list():
		if (property.get("name") != DOTNET_EXTERNAL_EDITOR_SETTING
			or property.get("hint") != PROPERTY_HINT_ENUM):
			continue
		for enum_value in property.get("hint_string", "").split(",", false):
			var separator: int = enum_value.rfind(":")
			if separator < 0:
				continue
			var label: String = enum_value.substr(0, separator)
			var value: String = enum_value.substr(separator + 1)
			if label == DOTNET_RIDER_EDITOR_LABEL and value.is_valid_int():
				_dotnet_rider_editor_id = value.to_int()
				return _dotnet_rider_editor_id
	return -1

static func is_dotnet_external_editor_rider(rider_path: String) -> bool:
	var editor_settings := EditorInterface.get_editor_settings()
	return (is_rider_path(rider_path)
		and has_dotnet_external_editor()
		and editor_settings.get_setting(DOTNET_EXTERNAL_EDITOR_SETTING) == _get_dotnet_rider_editor_id()
		and editor_settings.get_setting(DOTNET_EDITOR_PATH_SETTING) == resolve_rider_executable(rider_path))

static func set_dotnet_external_editor_rider(rider_path: String, is_enabled: bool) -> void:
	var editor_settings := EditorInterface.get_editor_settings()
	if not has_dotnet_external_editor():
		return
	if is_enabled:
		if rider_path.is_empty():
			return
		editor_settings.set_setting(DOTNET_EXTERNAL_EDITOR_SETTING, _get_dotnet_rider_editor_id())
		editor_settings.set_setting(DOTNET_EDITOR_PATH_SETTING, resolve_rider_executable(rider_path))
	else:
		editor_settings.set_setting(DOTNET_EXTERNAL_EDITOR_SETTING,
			editor_settings.property_get_revert(DOTNET_EXTERNAL_EDITOR_SETTING))
		editor_settings.set_setting(DOTNET_EDITOR_PATH_SETTING,
			editor_settings.property_get_revert(DOTNET_EDITOR_PATH_SETTING))

static func apply_rider_to_editors(rider_path: String) -> void:
	if rider_path.is_empty():
		return
	set_external_editor_path(rider_path)
	set_dotnet_external_editor_rider(rider_path, true)

static func clear_rider_from_editors() -> void:
	var editor_settings := EditorInterface.get_editor_settings()
	var rider_path := get_external_editor_path()
	if is_rider_path(rider_path):
		set_external_editor_path("")
	if has_dotnet_external_editor():
		var dotnet_editor_path: String = editor_settings.get_setting(DOTNET_EDITOR_PATH_SETTING)
		if is_dotnet_external_editor_rider(dotnet_editor_path):
			set_dotnet_external_editor_rider(dotnet_editor_path, false)

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

# Register the dropdown with one entry per detected installation (plus a
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
