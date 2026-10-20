@tool
extends MenuButton
class_name RiderToolbarMenu

enum MenuItemId {
	START_RIDER,
	TOGGLE_PRESET,
	EXTERNAL_EDITOR_SETTINGS,
}

const EDITOR_SETTINGS_SHORTCUT := "editor/editor_settings"

var _editor_settings: EditorSettings
var _plugin_cfg_path: String
var _preset_applier: PresetApplier
var _is_active: bool
var _popup_menu: PopupMenu

func setup(plugin_dir: String, plugin_cfg_path: String, preset_applier: PresetApplier, is_active: bool) -> void:
	_editor_settings = EditorInterface.get_editor_settings()
	_plugin_cfg_path = plugin_cfg_path
	_preset_applier = preset_applier
	_is_active = is_active

	# DPITexture processes the SVG source for the current editor scale, the same way the built-in editor icons do.
	icon = DPITexture.create_from_string(FileAccess.get_file_as_string(plugin_dir.path_join("icons/RiderRobot.svg")), EditorInterface.get_editor_scale())
	tooltip_text = "Expand Menu (Rider addon)"
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

	_popup_menu = get_popup()
	_popup_menu.add_icon_item(icon, "Start Rider", MenuItemId.START_RIDER)
	_popup_menu.set_item_tooltip(_popup_menu.get_item_index(MenuItemId.START_RIDER),
		"Starts Rider when the external editor path points to Rider.")
	_popup_menu.add_icon_item(_get_toggle_icon(_is_active),
		"Use external editor preset", MenuItemId.TOGGLE_PRESET)
	# The toggle icon is wider than the other menu icons, so keep it within the regular editor icon width.
	_popup_menu.set_item_icon_max_width(_popup_menu.get_item_index(MenuItemId.TOGGLE_PRESET),
		_get_editor_icon("Tools").get_width())
	_popup_menu.set_item_tooltip(_popup_menu.get_item_index(MenuItemId.TOGGLE_PRESET),
		"Switches between internal and external editor presets; this does not enable or disable the Rider plugin.")
	_popup_menu.add_icon_item(_get_editor_icon("Tools"),
		"External Editor Settings…", MenuItemId.EXTERNAL_EDITOR_SETTINGS)
	_popup_menu.id_pressed.connect(_on_menu_id_pressed)
	_popup_menu.about_to_popup.connect(_on_menu_about_to_popup)

func _get_editor_icon(icon_name: StringName) -> Texture2D:
	return EditorInterface.get_editor_theme().get_icon(icon_name, "EditorIcons")

# The CheckButton icons of the editor theme follow the current theme (dark/light) and the accent color.
func _get_toggle_icon(is_on: bool) -> Texture2D:
	return EditorInterface.get_editor_theme().get_icon("checked" if is_on else "unchecked", "CheckButton")

func _on_menu_about_to_popup() -> void:
	_popup_menu.set_item_disabled(_popup_menu.get_item_index(MenuItemId.START_RIDER),
		not EditorSettingsUtil.is_external_editor_rider())
	_popup_menu.set_item_icon(_popup_menu.get_item_index(MenuItemId.TOGGLE_PRESET), _get_toggle_icon(_is_active))
	# MenuButton focuses the first item only when opened from the keyboard, so focus it after the popup is shown.
	_focus_first_enabled_item.call_deferred()

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

func _start_rider() -> void:
	var external_editor_path := EditorSettingsUtil.get_external_editor_path()
	if not EditorSettingsUtil.is_rider_path(external_editor_path):
		return
	var project_path := ProjectSettings.globalize_path("res://")
	if OS.create_process(external_editor_path, PackedStringArray([project_path])) < 0:
		push_warning("Failed to start Rider using external editor path: %s" % external_editor_path)

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
