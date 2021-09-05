extends Node
class_name EditorPlugin

const CONTAINER_TOOLBAR = 0;
const CONTAINER_SPATIAL_EDITOR_MENU = 1;
const CONTAINER_SPATIAL_EDITOR_SIDE_LEFT = 2;
const CONTAINER_SPATIAL_EDITOR_SIDE_RIGHT = 3;
const CONTAINER_SPATIAL_EDITOR_BOTTOM = 4;
const CONTAINER_CANVAS_EDITOR_MENU = 5;
const CONTAINER_CANVAS_EDITOR_SIDE_LEFT = 6;
const CONTAINER_CANVAS_EDITOR_SIDE_RIGHT = 7;
const CONTAINER_CANVAS_EDITOR_BOTTOM = 8;
const CONTAINER_PROPERTY_EDITOR_BOTTOM = 9;
const CONTAINER_PROJECT_SETTING_TAB_LEFT = 10;
const CONTAINER_PROJECT_SETTING_TAB_RIGHT = 11;
const DOCK_SLOT_LEFT_UL = 0;
const DOCK_SLOT_LEFT_BL = 1;
const DOCK_SLOT_LEFT_UR = 2;
const DOCK_SLOT_LEFT_BR = 3;
const DOCK_SLOT_RIGHT_UL = 4;
const DOCK_SLOT_RIGHT_BL = 5;
const DOCK_SLOT_RIGHT_UR = 6;
const DOCK_SLOT_RIGHT_BR = 7;
const DOCK_SLOT_MAX = 8;


func _apply_changes() -> void:
    pass;

func _build() -> bool:
    pass;

func _clear() -> void:
    pass;

func _disable_plugin() -> void:
    pass;

func _edit(object: Object) -> void:
    pass;

func _enable_plugin() -> void:
    pass;

func _forward_canvas_draw_over_viewport(overlay: Control) -> void:
    pass;

func _forward_canvas_force_draw_over_viewport(overlay: Control) -> void:
    pass;

func _forward_canvas_gui_input(event: InputEvent) -> bool:
    pass;

func _forward_spatial_draw_over_viewport(overlay: Control) -> void:
    pass;

func _forward_spatial_force_draw_over_viewport(overlay: Control) -> void:
    pass;

func _forward_spatial_gui_input(camera: Camera3D, event: InputEvent) -> bool:
    pass;

func _get_breakpoints() -> PackedStringArray:
    pass;

func _get_plugin_icon() -> Texture2D:
    pass;

func _get_plugin_name() -> String:
    pass;

func _get_state() -> Dictionary:
    pass;

func _get_window_layout(layout: ConfigFile) -> void:
    pass;

func _handles(object: Object) -> bool:
    pass;

func _has_main_screen() -> bool:
    pass;

func _make_visible(visible: bool) -> void:
    pass;

func _save_external_data() -> void:
    pass;

func _set_state(state: Dictionary) -> void:
    pass;

func _set_window_layout(layout: ConfigFile) -> void:
    pass;

func add_autoload_singleton(name: String, path: String) -> void:
    pass;

func add_control_to_bottom_panel(control: Control, title: String) -> Button:
    pass;

func add_control_to_container(container: int, control: Control) -> void:
    pass;

func add_control_to_dock(slot: int, control: Control) -> void:
    pass;

func add_custom_type(type: String, base: String, script: Script, icon: Texture2D) -> void:
    pass;

func add_debugger_plugin(script: Script) -> void:
    pass;

func add_export_plugin(plugin: EditorExportPlugin) -> void:
    pass;

func add_import_plugin(importer: EditorImportPlugin) -> void:
    pass;

func add_inspector_plugin(plugin: EditorInspectorPlugin) -> void:
    pass;

func add_scene_import_plugin(scene_importer: EditorSceneImporter) -> void:
    pass;

func add_spatial_gizmo_plugin(plugin: EditorNode3DGizmoPlugin) -> void:
    pass;

func add_tool_menu_item(name: String, callable: Callable) -> void:
    pass;

func add_tool_submenu_item(name: String, submenu: Object) -> void:
    pass;

func add_translation_parser_plugin(parser: EditorTranslationParserPlugin) -> void:
    pass;

func add_undo_redo_inspector_hook_callback(callable: Callable) -> void:
    pass;

func get_editor_interface() -> EditorInterface:
    pass;

func get_script_create_dialog() -> ScriptCreateDialog:
    pass;

func get_undo_redo() -> UndoRedo:
    pass;

func hide_bottom_panel() -> void:
    pass;

func make_bottom_panel_item_visible(item: Control) -> void:
    pass;

func queue_save_layout() -> void:
    pass;

func remove_autoload_singleton(name: String) -> void:
    pass;

func remove_control_from_bottom_panel(control: Control) -> void:
    pass;

func remove_control_from_container(container: int, control: Control) -> void:
    pass;

func remove_control_from_docks(control: Control) -> void:
    pass;

func remove_custom_type(type: String) -> void:
    pass;

func remove_debugger_plugin(script: Script) -> void:
    pass;

func remove_export_plugin(plugin: EditorExportPlugin) -> void:
    pass;

func remove_import_plugin(importer: EditorImportPlugin) -> void:
    pass;

func remove_inspector_plugin(plugin: EditorInspectorPlugin) -> void:
    pass;

func remove_scene_import_plugin(scene_importer: EditorSceneImporter) -> void:
    pass;

func remove_spatial_gizmo_plugin(plugin: EditorNode3DGizmoPlugin) -> void:
    pass;

func remove_tool_menu_item(name: String) -> void:
    pass;

func remove_translation_parser_plugin(parser: EditorTranslationParserPlugin) -> void:
    pass;

func remove_undo_redo_inspector_hook_callback(callable: Callable) -> void:
    pass;

func set_force_draw_over_forwarding_enabled() -> void:
    pass;

func set_input_event_forwarding_always_enabled() -> void:
    pass;

func update_overlays() -> int:
    pass;

