@tool
extends EditorPlugin

var toolbar_menu: RiderToolbarMenu
var _locator_service: RiderLocatorService
var _rd_loader: RdLoader

func _enter_tree() -> void:
	var editor_settings := EditorInterface.get_editor_settings()
	var script_path := (get_script() as Script).resource_path
	var plugin_dir := script_path.get_base_dir()
	var plugin_cfg_path := plugin_dir + "/plugin.cfg"

	_write_godot_editor_cfg()

	var cfg := ConfigFile.new()
	var err := cfg.load(plugin_cfg_path)
	var is_active := true
	var presets_json_path := plugin_dir + "/presets.json"
	if err == OK:
		is_active = str(cfg.get_value("presets", "active", "on")) == "on"
		var presets_rel_path := str(cfg.get_value("presets", "presets", "presets.json"))
		presets_json_path = plugin_dir + "/" + presets_rel_path
	else:
		push_warning("Failed to load plugin.cfg: %s" % [err])

	# Build toolbar control
	var preset_applier := PresetApplier.new(presets_json_path)
	toolbar_menu = RiderToolbarMenu.new()
	toolbar_menu.setup(plugin_dir, plugin_cfg_path, preset_applier, is_active)
	add_control_to_container(EditorPlugin.CONTAINER_TOOLBAR, toolbar_menu)

	_locator_service = RiderLocatorService.new()
	_rd_loader = RdLoader.new()
	
	# add_child to get access to _enter/exit_tree
	add_child(_rd_loader)
	_locator_service.start_search()

	# Ensure settings reflect current state on startup
	preset_applier.apply_preset(editor_settings, is_active)

func _exit_tree() -> void:
	EditorSettingsUtil.erase_rider_external_editor_setting()
	if toolbar_menu != null:
		remove_control_from_container(EditorPlugin.CONTAINER_TOOLBAR, toolbar_menu)
		toolbar_menu.queue_free()

	var args := OS.get_cmdline_args()
	if "--rider-addon-tests" in args:
		print("==== rider-addon-tests finished ====")

func _write_godot_editor_cfg() -> void:
	var settings_dir := EditorInterface.get_editor_paths().get_project_settings_dir()
	var cfg_path := settings_dir.path_join("rider_addon_godot_editor.cfg")
	var cfg := ConfigFile.new()
	cfg.set_value("editor_metadata", "process_id", OS.get_process_id())
	cfg.set_value("editor_metadata", "executable_path", OS.get_executable_path())
	var err := cfg.save(cfg_path)
	if err != OK:
		push_warning("Failed to save rider_addon_godot_editor.cfg: %s" % [err])
