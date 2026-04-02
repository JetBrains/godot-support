@tool
extends RefCounted
class_name RiderLocatorService

var _installations_found: Array = []
var _thread: Thread = null

func get_installations() -> Array:
	var result: Array = RiderLocator.new().get_installations() # from the gdextension
	return result

# todo
func fix_external_editor_if_supplied_in_commandline(_settings_service: EditorSettingsService, editor_settings: EditorSettings) -> bool:
	# When Godot is started from Rider (or vice versa), we may receive the Rider path
	# via command-line so we can keep Godot's external editor setting in sync.
	# Supported form (only this one):
	#   --my_rider_path="/absolute/path/to/rider with possible spaces"
	var args : Array = OS.get_cmdline_args()
	var provided_rider_path := ""
	for a_raw in args:
		var a: String = str(a_raw)
		if a.begins_with("--my_rider_path="):
			provided_rider_path = a.substr("--my_rider_path=".length())
			break

	if provided_rider_path.is_empty():
		return false

	provided_rider_path = trim_quotes(provided_rider_path)

	# Validate existence (file or dir)
	var looks_existing := FileAccess.file_exists(provided_rider_path) || DirAccess.dir_exists_absolute(provided_rider_path)
	if looks_existing:
		_settings_service.set_external_editor_path(editor_settings, provided_rider_path)
		print("Rider path provided via CLI (my_rider_path): ", provided_rider_path)
		return true
	else:
		push_warning("my_rider_path was provided but does not exist: %s" % [provided_rider_path])
	return false

func trim_quotes(s: String) -> String:
	if s.begins_with('"') and s.ends_with('"') and s.length() >= 2:
		return s.substr(1, s.length() - 2)
	return s


func add_selector_in_editor_interface(_settings_service: EditorSettingsService):
	_update_selector(_installations_found)

	if _installations_found.is_empty() and _thread == null:
		_thread = Thread.new()
		_thread.start(_load_installations)

func _load_installations() -> void:
	var array: Array = get_installations()
	call_deferred("_on_installations_loaded", array)

func _on_installations_loaded(array: Array):
	_installations_found = array
	if _thread:
		_thread.wait_to_finish()
		_thread = null
	_update_selector(_installations_found)

func _notification(what: int) -> void:
	if what == NOTIFICATION_PREDELETE and _thread != null:
		_thread.wait_to_finish()

func _update_selector(array: Array):
	var name := "text_editor/external/editor"
	var settings := EditorInterface.get_editor_settings()

	if !(settings.has_setting(name)):
		settings.set(name, 0)

	var installations: Array = ["Custom"]
	for element in array:
		var display_name: String = element.get("display", "")
		# Replace special characters that break PROPERTY_HINT_ENUM format
		# Comma is the enum delimiter, colon is used for explicit value assignment
		display_name = display_name.replace(",", " •").replace(":", " -")
		installations.append(display_name)
	var options :String = ",".join(installations)

	settings.add_property_info({
		"name": name,
		"type":TYPE_INT,
		"hint":PROPERTY_HINT_ENUM,
		"hint_string": options
	})

	# Connect to settings changes to update external editor path when selection changes
	if not settings.settings_changed.is_connected(_on_selection_changed):
		settings.settings_changed.connect(_on_selection_changed.bind())

func _on_selection_changed() -> void:
	var name := "text_editor/external/editor"
	var settings := EditorInterface.get_editor_settings()
	var selected_index: int = settings.get_setting(name)

	# Index 0 is "Custom", so user manages the path manually
	if selected_index == 0:
		return

	# Map to actual installation (offset by 1 because of "Custom" at index 0)
	var installation_index := selected_index - 1
	var installations_array = _installations_found
	if installation_index >= 0 and installation_index < installations_array.size():
		var installation = installations_array[installation_index]
		var new_path: String = installation.get("path", "")
		if not new_path.is_empty():
			EditorSettingsService.new().set_external_editor_path(settings, new_path)
