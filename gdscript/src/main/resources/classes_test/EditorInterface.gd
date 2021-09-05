extends Node
class_name EditorInterface


var distraction_free_mode: bool setget set_distraction_free_mode, is_distraction_free_mode_enabled;

func edit_node(node: Node) -> void:
    pass;

func edit_resource(resource: Resource) -> void:
    pass;

func get_base_control() -> Control:
    pass;

func get_current_path() -> String:
    pass;

func get_edited_scene_root() -> Node:
    pass;

func get_editor_main_control() -> Control:
    pass;

func get_editor_paths() -> EditorPaths:
    pass;

func get_editor_scale() -> float:
    pass;

func get_editor_settings() -> EditorSettings:
    pass;

func get_file_system_dock() -> FileSystemDock:
    pass;

func get_inspector() -> EditorInspector:
    pass;

func get_open_scenes() -> Array:
    pass;

func get_playing_scene() -> String:
    pass;

func get_resource_filesystem() -> EditorFileSystem:
    pass;

func get_resource_previewer() -> EditorResourcePreview:
    pass;

func get_script_editor() -> ScriptEditor:
    pass;

func get_selected_path() -> String:
    pass;

func get_selection() -> EditorSelection:
    pass;

func inspect_object(object: Object, for_property: String, inspector_only: bool) -> void:
    pass;

func is_playing_scene() -> bool:
    pass;

func is_plugin_enabled(plugin: String) -> bool:
    pass;

func make_mesh_previews(meshes: Array, preview_size: int) -> Array:
    pass;

func open_scene_from_path(scene_filepath: String) -> void:
    pass;

func play_current_scene() -> void:
    pass;

func play_custom_scene(scene_filepath: String) -> void:
    pass;

func play_main_scene() -> void:
    pass;

func reload_scene_from_path(scene_filepath: String) -> void:
    pass;

func save_scene() -> int:
    pass;

func save_scene_as(path: String, with_preview: bool) -> void:
    pass;

func select_file(file: String) -> void:
    pass;

func set_main_screen_editor(name: String) -> void:
    pass;

func set_plugin_enabled(plugin: String, enabled: bool) -> void:
    pass;

func stop_playing_scene() -> void:
    pass;

