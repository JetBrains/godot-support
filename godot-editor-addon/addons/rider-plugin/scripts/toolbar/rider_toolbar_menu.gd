@tool
extends MenuButton
class_name RiderToolbarMenu # do not rename - it is used from godot-rd

enum MenuItemId {
	START_RIDER,
	RIDER_INSTALLATION,
	TOGGLE_PRESET,
	EXTERNAL_EDITOR_SETTINGS,
}

const EDITOR_SETTINGS_SHORTCUT := "editor/editor_settings"
const INSTALLATION_SUBMENU_NAME := "RiderInstallationMenu"
# Installation items use their index as id, so the manual selection action uses an id outside that range.
# It must be non-negative: PopupMenu reports the item index instead of a negative id in id_pressed.
const MANUAL_SELECTION_ITEM_ID := 1 << 20

var _editor_settings: EditorSettings
var _plugin_cfg_path: String
var _preset_applier: PresetApplier
var _locator_service: RiderLocatorService
var _rd_loader: RdLoader
var _is_active: bool
var _popup_menu: PopupMenu
var _installation_menu: PopupMenu

func setup(plugin_dir: String, plugin_cfg_path: String, preset_applier: PresetApplier, is_active: bool,
		locator_service: RiderLocatorService, rd_loader: RdLoader) -> void:
	_editor_settings = EditorInterface.get_editor_settings()
	_plugin_cfg_path = plugin_cfg_path
	_preset_applier = preset_applier
	_locator_service = locator_service
	_rd_loader = rd_loader
	_is_active = is_active

	# DPITexture processes the SVG source for the current editor scale, the same way the built-in editor icons do.
	icon = DPITexture.create_from_string(FileAccess.get_file_as_string(plugin_dir.path_join("icons/RiderRobot.svg")), EditorInterface.get_editor_scale())
	add_theme_color_override("icon_normal_color", Color.WHITE)
	add_theme_color_override("icon_hover_color", Color.WHITE)
	add_theme_color_override("icon_pressed_color", Color.WHITE)
	add_theme_color_override("icon_hover_pressed_color", Color.WHITE)
	flat = false
	theme_type_variation = "RunBarButton"
	size_flags_horizontal = Control.SIZE_SHRINK_CENTER
	size_flags_vertical = Control.SIZE_SHRINK_CENTER
	icon_alignment = HORIZONTAL_ALIGNMENT_CENTER
	focus_mode = Control.FOCUS_NONE
	mouse_entered.connect(_update_button_tooltip)

	_popup_menu = get_popup()
	_popup_menu.add_icon_item(icon, "Start Rider", MenuItemId.START_RIDER)
	_installation_menu = PopupMenu.new()
	_installation_menu.name = INSTALLATION_SUBMENU_NAME
	_popup_menu.add_child(_installation_menu)
	_popup_menu.add_submenu_item("Rider Installation", INSTALLATION_SUBMENU_NAME, MenuItemId.RIDER_INSTALLATION)
	_update_installation_item()
	_popup_menu.add_icon_item(_get_toggle_icon(_is_active),
		"Use recommended editor settings preset", MenuItemId.TOGGLE_PRESET)
	# The toggle icon is wider than the other menu icons, so keep it within the regular editor icon width.
	_popup_menu.set_item_icon_max_width(_popup_menu.get_item_index(MenuItemId.TOGGLE_PRESET),
		_get_editor_icon("Tools").get_width())
	_popup_menu.set_item_tooltip(_popup_menu.get_item_index(MenuItemId.TOGGLE_PRESET),
		"Applies recommended editor settings, including Use External Editor. It does not change the selected Rider.")
	_popup_menu.add_icon_item(_get_editor_icon("Tools"),
		"External Editor Settings…", MenuItemId.EXTERNAL_EDITOR_SETTINGS)
	_popup_menu.id_pressed.connect(_on_menu_id_pressed)
	_popup_menu.about_to_popup.connect(_on_menu_about_to_popup)
	_installation_menu.about_to_popup.connect(_populate_installations_menu)
	_installation_menu.id_pressed.connect(_on_installation_id_pressed)
	_locator_service.installations_changed.connect(_on_installations_changed)
	EditorSettingsUtil.get_rider_install_change_signal.connect(_on_rider_install_changed)
	_update_button_tooltip()

# Placing the control before the run bar keeps it visible as long as the run bar is.
# It also matches the position of the Godot icon in Rider.
func move_rider_icon_before_run_bar() -> void:
	var title_bar := get_parent()
	if title_bar == null:
		return
	for child in title_bar.get_children():
		if child.get_class() == "EditorRunBar":
			title_bar.move_child(self, child.get_index())
			return
	# The run bar is not found (e.g. another Godot version), so keep the default position at the end.

func _get_editor_icon(icon_name: StringName) -> Texture2D:
	return EditorInterface.get_editor_theme().get_icon(icon_name, "EditorIcons")

# The CheckButton icons of the editor theme follow the current theme (dark/light) and the accent color.
func _get_toggle_icon(is_on: bool) -> Texture2D:
	return EditorInterface.get_editor_theme().get_icon("checked" if is_on else "unchecked", "CheckButton")

func _get_checkbox_icon(is_checked: bool) -> Texture2D:
	return EditorInterface.get_editor_theme().get_icon("checked" if is_checked else "unchecked", "CheckBox")

func _update_installation_item() -> void:
	var has_rider := EditorSettingsUtil.is_rider_path(EditorSettingsUtil.get_external_editor_path())
	var item_index := _popup_menu.get_item_index(MenuItemId.RIDER_INSTALLATION)
	_popup_menu.set_item_icon(item_index, _get_checkbox_icon(has_rider))
	_popup_menu.set_item_tooltip(item_index,
		"A Rider executable is selected." if has_rider else "No Rider executable is selected.")

func _on_menu_about_to_popup() -> void:
	_update_button_tooltip()
	var current_path := EditorSettingsUtil.get_external_editor_path()
	var start_rider_index := _popup_menu.get_item_index(MenuItemId.START_RIDER)
	var has_rider := EditorSettingsUtil.is_rider_path(current_path)
	_popup_menu.set_item_disabled(start_rider_index, not has_rider)
	_popup_menu.set_item_tooltip(start_rider_index,
		"Start the selected Rider." if has_rider else "Select a Rider installation to enable this action.")
	_update_installation_item()
	_popup_menu.set_item_icon(_popup_menu.get_item_index(MenuItemId.TOGGLE_PRESET), _get_toggle_icon(_is_active))
	# MenuButton focuses the first item only when opened from the keyboard, so focus it after the popup is shown.
	_focus_first_enabled_item.call_deferred()

func _populate_installations_menu() -> void:
	_installation_menu.clear()
	if _locator_service.is_searching():
		_add_installation_status("Searching...")
		return

	var installations := _locator_service.get_installations()
	var selected_installation_index := _locator_service.find_installation(
		EditorSettingsUtil.get_external_editor_path())
	for index in installations.size():
		var installation = installations[index]
		var path: String = installation.get("path", "")
		var display := _get_installation_label(installation.get("display", "Rider"), path)
		var has_rd := RdLoader.has_rd_extension(path)
		if not has_rd:
			display += " (godot-rd missing)"
		_installation_menu.add_radio_check_item(display, index)
		var item_index := _installation_menu.get_item_count() - 1
		_installation_menu.set_item_checked(item_index, index == selected_installation_index)
		var tooltip := path
		if not has_rd:
			tooltip += "\nInstall the Godot Support plugin in Rider to enable its Godot connection."
		_installation_menu.set_item_tooltip(item_index, tooltip)

	var current_path := EditorSettingsUtil.get_external_editor_path()
	if EditorSettingsUtil.is_rider_path(current_path) and selected_installation_index < 0:
		var custom_name := current_path.get_file()
		if custom_name.is_empty():
			custom_name = current_path
		_installation_menu.add_radio_check_item("Custom: " + custom_name, -1)
		var custom_item_index := _installation_menu.get_item_count() - 1
		_installation_menu.set_item_checked(custom_item_index, true)
		_installation_menu.set_item_disabled(custom_item_index, true)
		_installation_menu.set_item_tooltip(custom_item_index, current_path)

	if installations.is_empty() and not EditorSettingsUtil.is_rider_path(current_path):
		_add_manual_selection_item("Set `Exec Path` Manually…")

# The locator gives "Rider <version> <path>". The menu shows the path only in the tooltip.
func _get_installation_label(display: String, path: String) -> String:
	var path_suffix := " " + path
	if not path.is_empty() and display.ends_with(path_suffix):
		return display.substr(0, display.length() - path_suffix.length())
	return display

func _add_installation_status(text: String) -> void:
	_installation_menu.add_item(text)
	_installation_menu.set_item_disabled(_installation_menu.get_item_count() - 1, true)

func _add_manual_selection_item(text: String) -> void:
	_installation_menu.add_icon_item(_get_editor_icon("Tools"), text, MANUAL_SELECTION_ITEM_ID)
	_installation_menu.set_item_tooltip(_installation_menu.get_item_count() - 1,
		"No Rider installation was detected.\nOpens Editor Settings > Text Editor > External to set the Rider executable path.")

func _on_installation_id_pressed(id: int) -> void:
	if id == MANUAL_SELECTION_ITEM_ID:
		_open_external_editor_settings()
		return
	_locator_service.select_installation(id)

func _on_installations_changed() -> void:
	if _installation_menu.visible:
		_populate_installations_menu()
	_update_button_tooltip()

func _on_rider_install_changed(_rider_path: String) -> void:
	_update_button_tooltip()

func _focus_first_enabled_item() -> void:
	if not _popup_menu.visible:
		return
	for item_index in range(_popup_menu.item_count):
		if not _popup_menu.is_item_disabled(item_index) and not _popup_menu.is_item_separator(item_index):
			_popup_menu.set_focused_item(item_index)
			return

func _on_menu_id_pressed(id: int) -> void:
	match id:
		MenuItemId.START_RIDER:
			_start_rider()
		MenuItemId.TOGGLE_PRESET:
			_set_preset_active(not _is_active)
		MenuItemId.EXTERNAL_EDITOR_SETTINGS:
			_open_external_editor_settings()

func _get_connection_status() -> String:
	var rider_path := EditorSettingsUtil.get_external_editor_path()
	if _rd_loader.needs_restart():
		return "Rider bridge: Restart Godot"
	if rider_path.is_empty():
		return "Rider bridge: No Rider selected"
	if not EditorSettingsUtil.is_rider_path(rider_path):
		return "Rider bridge: External editor is not Rider"
	if not RdLoader.has_rd_extension(rider_path):
		return "Rider bridge: godot-rd is missing"
	if _rd_loader.is_loaded_for(rider_path):
		return "Rider bridge: godot-rd loaded"
	return "Rider bridge: Not loaded"

func _update_button_tooltip() -> void:
	tooltip_text = "Rider addon\n%s" % _get_connection_status()

func _start_rider() -> void:
	var external_editor_path := EditorSettingsUtil.get_external_editor_path()
	if not EditorSettingsUtil.is_rider_path(external_editor_path):
		return
	var rider_executable_path := EditorSettingsUtil.resolve_rider_executable(external_editor_path)
	var project_path := ProjectSettings.globalize_path("res://")
	if OS.create_process(rider_executable_path, PackedStringArray([project_path])) < 0:
		push_warning("Failed to start Rider using external editor path: %s" % rider_executable_path)

func _set_preset_active(is_active: bool) -> void:
	_is_active = is_active
	_preset_applier.apply_preset(_editor_settings, is_active)

	var cfg := ConfigFile.new()
	var load_err := cfg.load(_plugin_cfg_path)
	if load_err != OK:
		push_warning("Failed to load plugin.cfg, the preset state will not be saved: %s" % [load_err])
		return
	cfg.set_value("presets", "active", _preset_applier.get_preset_key(is_active))
	var save_err := cfg.save(_plugin_cfg_path)
	if save_err != OK:
		push_warning("Failed to save plugin.cfg: %s" % [save_err])

func _open_external_editor_settings() -> void:
	if not _open_editor_settings():
		push_warning("Could not find the Editor Settings menu item")
		return

	await get_tree().process_frame
	# The plugin may have been disabled during the awaited frame.
	if not is_inside_tree():
		return
	var settings_dialog := _find_node_by_class(EditorInterface.get_base_control(), "EditorSettingsDialog")
	if settings_dialog == null:
		push_warning("Could not find the Editor Settings dialog")
		return
	if not _select_external_editor_section(settings_dialog):
		push_warning("Could not find the Text Editor > External section in Editor Settings")

# The editor adds the "Editor Settings..." item with the shared "editor/editor_settings" shortcut.
# The match uses the identity of that Shortcut object, so it does not depend on the item text or the editor language.
func _open_editor_settings() -> bool:
	if not _editor_settings.has_shortcut(EDITOR_SETTINGS_SHORTCUT):
		return false
	var shortcut := _editor_settings.get_shortcut(EDITOR_SETTINGS_SHORTCUT)
	if shortcut == null:
		return false
	return _activate_shortcut_item(EditorInterface.get_base_control(), shortcut)

func _activate_shortcut_item(node: Node, shortcut: Shortcut) -> bool:
	var popup := node as PopupMenu
	if popup != null and popup != _popup_menu:
		for item_index in range(popup.item_count):
			if popup.get_item_shortcut(item_index) == shortcut:
				popup.id_pressed.emit(popup.get_item_id(item_index))
				return true
	for child in node.get_children():
		if _activate_shortcut_item(child, shortcut):
			return true
	return false

func _find_node_by_class(node: Node, target_class_name: String) -> Node:
	if node.get_class() == target_class_name:
		return node
	for child in node.get_children():
		var result := _find_node_by_class(child, target_class_name)
		if result != null:
			return result
	return null

func _select_external_editor_section(node: Node) -> bool:
	if node is Tree:
		var tree := node as Tree
		var root_item := tree.get_root()
		if root_item != null and _select_external_editor_tree_item(tree, root_item):
			return true
	for child in node.get_children():
		if _select_external_editor_section(child):
			return true
	return false

func _select_external_editor_tree_item(tree: Tree, item: TreeItem) -> bool:
	if str(item.get_metadata(0)) == "text_editor/external":
		item.select(0)
		tree.item_selected.emit()
		return true
	var child := item.get_first_child()
	while child != null:
		if _select_external_editor_tree_item(tree, child):
			return true
		child = child.get_next()
	return false
