@tool
extends Node
class_name RdLoader

var _loaded_rd_path: String = ""
var _needs_restart: bool = false
var _restart_dialog: ConfirmationDialog = null

func _enter_tree() -> void:
	var editor_path := EditorSettingsUtil.get_external_editor_path()
	# external editor doesn't have to be Rider 
	if editor_path != "" && editor_path.to_lower().contains("rider"):
		# This is done in order to not have to wait on the rider search, which can take a while.
		load_rd_extension(editor_path)
	EditorSettingsUtil.get_rider_install_change_signal.connect(load_rd_extension)

func _exit_tree() -> void:
	EditorSettingsUtil.get_rider_install_change_signal.disconnect(load_rd_extension)
	if _restart_dialog != null:
		_restart_dialog.queue_free()
		_restart_dialog = null

func _install_path_to_rd_path(rider_path: String) -> String:
	var godot_rd_base: String = rider_path.get_base_dir().get_base_dir().path_join("plugins").path_join("rider-godot").path_join("godot-rd")
	var gdextension_path: String = godot_rd_base.path_join("godot-rd.gdextension")
	return gdextension_path

func _show_restart_dialog() -> void:
	_needs_restart = true
	if _restart_dialog != null:
		return
	_restart_dialog = ConfirmationDialog.new()
	_restart_dialog.title = "Restart Godot"
	# TODO: Perhaps better wording?
	_restart_dialog.dialog_text = "Features such as 'Open in Rider' or 'Open in Godot' can only be used after restart."
	_restart_dialog.get_ok_button().text = "Restart"
	_restart_dialog.confirmed.connect(_restart_editor)
	_restart_dialog.visibility_changed.connect(_on_restart_dialog_visibility_changed)
	_restart_dialog.popup_exclusive_centered(self)

func _on_restart_dialog_visibility_changed() -> void:
	if _restart_dialog != null && !_restart_dialog.visible:
		_restart_dialog.queue_free()
		_restart_dialog = null

func _restart_editor() -> void:
	EditorInterface.restart_editor()
	
class VersionCheckResult:
	var minimum_version: String 
	var met_requirement: bool
	
	func _init(minimum_version: String, met_requirement: bool) -> void:
		self.minimum_version = minimum_version
		self.met_requirement = met_requirement
	
func check_gdextension_version(gdextension_path: String) -> VersionCheckResult:
	var config := ConfigFile.new()
	var error := config.load(gdextension_path)
	if error != OK:
		print_verbose("[RIDER RD] failed to load gdextension file as config to check its version.")
		return VersionCheckResult.new("",false)

	var minimum := str(config.get_value("configuration", "compatibility_minimum", ""))	
	
	if minimum.is_empty():
		print_verbose("[RIDER RD] failed to check minimum version, because it is missing.")
		return VersionCheckResult.new("", false)
	
	return VersionCheckResult.new(minimum, engine_meets_minimum(minimum))

func engine_meets_minimum(required: String) -> bool:
	var parts := required.split(".")
	var required_version := [
		int(parts[0]) if parts.size() > 0 else 0,
		int(parts[1]) if parts.size() > 1 else 0,
		int(parts[2]) if parts.size() > 2 else 0,
	]

	var current := Engine.get_version_info()
	var current_version := [
		int(current.major),
		int(current.minor),
		int(current.patch),
	]

	for i in 3:
		if current_version[i] != required_version[i]:
			return current_version[i] > required_version[i]

	return true


func load_rd_extension(rider_path: String) -> void :
	if !FileAccess.file_exists(rider_path):
		print_verbose("[RIDER RD] load rd extension called on non existing path " + rider_path + ", perhaps the rider installation got deleted?")
		return
	var gdextension_path: String = _install_path_to_rd_path(rider_path)
	if !FileAccess.file_exists(gdextension_path):
		return
	if _loaded_rd_path != "":
		if _loaded_rd_path != gdextension_path || _needs_restart:
			# loading multiple RD extensions would make it so the port file
			# for which Rider listens, would get written to by both extensions.
			# Meaning it would
			_show_restart_dialog()
		return
	var version_check := check_gdextension_version(gdextension_path)
	if !version_check.met_requirement:
		if version_check.minimum_version.is_empty():
			push_warning("[RIDER RD] Failed to initialize communication between Godot and Rider, some features will not be available.")
		else: 
			var current := Engine.get_version_info()
			var current_str := "%s.%s.%s" % [current.major, current.minor, current.patch]
			push_warning("[RIDER RD] Failed to initialize communication between Godot and Rider. " +
				"The Rider RD extension is compatible with Godot %s or later, but the current version is %s." % [version_check.minimum_version, current_str]  )
		return
	# loading the same extension twice would fails with `GDExtensionManager.LOAD_STATUS_ALREADY_LOADED`
	if (!GDExtensionManager.is_extension_loaded(gdextension_path)
			&& GDExtensionManager.load_extension(gdextension_path) != GDExtensionManager.LOAD_STATUS_OK):
		push_warning("[RIDER RD] Failed to initialize communication between Godot and Rider, some features will not be available.")
	else:
		_loaded_rd_path = gdextension_path
