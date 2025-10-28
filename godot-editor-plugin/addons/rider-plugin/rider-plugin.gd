@tool
extends EditorPlugin

var editor_settings: EditorSettings
var checkbutton: CheckButton
var rider_button: Button
var _preset_applier: PresetApplier
var _settings_service: EditorSettingsService
var _locator_service: RiderLocatorService
var _plugin_cfg_path: String
var _presets_json_path: String

func _enter_tree() -> void:
	editor_settings = EditorInterface.get_editor_settings()
	var script_path := (get_script() as Script).resource_path
	var plugin_dir := script_path.get_base_dir()
	_plugin_cfg_path = plugin_dir + "/plugin.cfg"
	var cfg := ConfigFile.new()
	var err := cfg.load(_plugin_cfg_path)
	if err != OK:
		push_warning("Failed to load plugin.cfg: %s" % [err])
		return
	var active := str(cfg.get_value("presets", "active", "on"))
	var presets_rel_path := str(cfg.get_value("presets", "presets", "presets.json"))
	_presets_json_path = plugin_dir + "/" + presets_rel_path

	# Build UI
	checkbutton = CheckButton.new()
	checkbutton.text = "Use Rider"
	checkbutton.tooltip_text = "Shortcut for setting recommended settings"
	checkbutton.button_pressed = active == "on"
	checkbutton.pressed.connect(_on_checkbutton_pressed)
	add_control_to_container(EditorPlugin.CONTAINER_TOOLBAR, checkbutton)

	# Initialize services and panel
	_settings_service = EditorSettingsService.new()
	_locator_service = RiderLocatorService.new()
	_preset_applier = PresetApplier.new(_presets_json_path)

	_locator_service.add_selector_in_editor_interface(_settings_service)

	# Ensure settings reflect current state on startup
	_apply_preset(active)

func _on_checkbutton_pressed() -> void:
	var cfg := ConfigFile.new()
	if cfg.load(_plugin_cfg_path) != OK:
		push_warning("Failed to load plugin.cfg to update state")
		return
	var new_active: String = "off"
	if checkbutton.button_pressed:
		new_active = "on"
	cfg.set_value("presets", "active", new_active)
	var save_err := cfg.save(_plugin_cfg_path)
	if save_err != OK:
		push_warning("Failed to save plugin.cfg: %s" % [save_err])
	# Apply selected preset to editor settings
	_apply_preset(new_active)

func _apply_preset(active: String) -> void:
	_preset_applier.apply_preset(editor_settings, active)

func _exit_tree() -> void:
	remove_control_from_container(EditorPlugin.CONTAINER_TOOLBAR, checkbutton)
	remove_control_from_container(EditorPlugin.CONTAINER_TOOLBAR, rider_button)
	checkbutton.free()
	rider_button.free()
