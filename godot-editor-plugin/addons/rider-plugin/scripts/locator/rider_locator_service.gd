@tool
class_name RiderLocatorService

func get_installations() -> Array:
	var result: Array = RiderLocator.new().get_installations() # from the gdextension
	return result

	# we are just getting last one
	# todo: make it friendly configurable, there might be more then one Rider version around
	# Godot C# has a separate setting for external editor, maybe need an option to sync with it too
func fix_external_editor_if_needed(_settings_service: EditorSettingsService, editor_settings: EditorSettings):
	if not _settings_service.has_valid_external_editor_path(editor_settings):
		var array: Array = get_installations()
		var index: int = array.size() - 1
		if index >= 0:
			var riderPath = array[index]
			var new_path : String = riderPath.get("path", "")
			if not new_path.is_empty():
				_settings_service.set_external_editor_path(editor_settings, new_path)
				print("set exec_path: ", new_path)

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
