@tool
extends RefCounted
class_name RiderLocatorService

signal installations_changed

var _installations_found: Array = []
# we can keep a reference, it will be freed automatically, when the editor is closed
var _rider_locator: RefCounted = null
var _is_searching: bool = false

# Last value we wrote to / observed for the Rider selector dropdown
var _last_selected_index: int = 0
var _last_exec_path: String = ""

func start_search() -> void:
	var editor_settings := EditorInterface.get_editor_settings()
	_last_exec_path = EditorSettingsUtil.get_external_editor_path()
	var settings_changed := editor_settings.settings_changed
	if not settings_changed.is_connected(_on_selection_changed):
		settings_changed.connect(_on_selection_changed)

	# use ClassDB to somehow still work without GDExtension part (in older Godot versions)
	if not ClassDB.class_exists("RiderLocator"):
		return

	if _rider_locator == null:
		_rider_locator = ClassDB.instantiate("RiderLocator")
		if _rider_locator == null:
			return
		_rider_locator.connect("installations_loaded", _on_installations_loaded)
	EditorSettingsUtil.show_rider_searching_state()
	_is_searching = true
	_rider_locator.start_search()

func _on_installations_loaded(array: Array) -> void:
	_installations_found = array
	_is_searching = false
	_replace_missing_rider_installation()
	_update_selector()
	installations_changed.emit()

func _replace_missing_rider_installation() -> void:
	var exec_path := EditorSettingsUtil.get_external_editor_path()
	if not EditorSettingsUtil.is_rider_path(exec_path):
		return
	if FileAccess.file_exists(EditorSettingsUtil.resolve_rider_executable(exec_path)):
		return
	if _installations_found.is_empty():
		return

	_apply_installation(_installations_found.size() - 1)

func get_installations() -> Array:
	return _installations_found.duplicate()

func is_searching() -> bool:
	return _is_searching

func select_installation(index: int) -> void:
	if not _apply_installation(index):
		return
	_last_selected_index = index + 1
	EditorInterface.get_editor_settings().set(EditorSettingsUtil.EXTERNAL_EDITOR_SETTING, _last_selected_index)

func _apply_installation(index: int) -> bool:
	if index < 0 or index >= _installations_found.size():
		return false
	var new_path: String = _installations_found[index].get("path", "")
	if new_path.is_empty():
		return false
	_last_exec_path = new_path
	EditorSettingsUtil.apply_rider_to_editors(new_path)
	return true

func find_installation(path: String) -> int:
	for index in _installations_found.size():
		if _installations_found[index].get("path", "") == path:
			return index
	return -1

func _update_selector() -> void:
	if _installations_found.is_empty():
		_last_selected_index = 0
		EditorSettingsUtil.erase_rider_external_editor_setting()
		return

	var index := _index_for_exec_path()
	# Set _last_selected_index BEFORE register_rider_installations so the
	# synchronous settings_changed handler sees a match and skips its work.
	_last_selected_index = index
	EditorSettingsUtil.register_rider_installations(_installations_found, index)

# Selector index: 0 means no Rider, otherwise the installation index + 1.
func _index_for_exec_path() -> int:
	return find_installation(EditorSettingsUtil.get_external_editor_path()) + 1

func _on_selection_changed() -> void:
	var exec_path := EditorSettingsUtil.get_external_editor_path()
	if exec_path != _last_exec_path:
		_on_external_editor_path_changed(exec_path)

	var selected_index := EditorSettingsUtil.get_rider_selector_index()

	# settings_changed fires for every editor setting, this helps to avoid updating on every other changes
	if selected_index == _last_selected_index:
		return
	_last_selected_index = selected_index

	# Index 0 means no Rider is selected. Keep another external editor path unchanged.
	if selected_index == 0:
		_last_exec_path = "" if EditorSettingsUtil.is_rider_path(exec_path) else exec_path
		EditorSettingsUtil.clear_rider_from_editors()
		return

	_apply_installation(selected_index - 1)

func _on_external_editor_path_changed(path: String) -> void:
	var previous_path := _last_exec_path
	_last_exec_path = path
	if EditorInterface.get_editor_settings().has_setting(EditorSettingsUtil.EXTERNAL_EDITOR_SETTING):
		var selected_index := _index_for_exec_path()
		_last_selected_index = selected_index
		EditorInterface.get_editor_settings().set(EditorSettingsUtil.EXTERNAL_EDITOR_SETTING, selected_index)

	EditorSettingsUtil.notify_rider_install_changed(path)
	if EditorSettingsUtil.is_rider_path(path):
		EditorSettingsUtil.set_dotnet_external_editor_rider(path, true)
	elif EditorSettingsUtil.is_dotnet_external_editor_rider(previous_path):
		EditorSettingsUtil.set_dotnet_external_editor_rider(previous_path, false)
