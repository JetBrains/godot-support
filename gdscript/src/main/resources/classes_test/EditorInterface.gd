#brief Godot editor's interface.
#desc EditorInterface gives you control over Godot editor's window. It allows customizing the window, saving and (re-)loading scenes, rendering mesh previews, inspecting and editing resources and objects, and provides access to [EditorSettings], [EditorFileSystem], [EditorResourcePreview], [ScriptEditor], the editor viewport, and information about scenes.
#desc [b]Note:[/b] This class shouldn't be instantiated directly. Instead, access the singleton using [method EditorPlugin.get_editor_interface].
class_name EditorInterface


#desc If [code]true[/code], enables distraction-free mode which hides side docks to increase the space available for the main view.
var distraction_free_mode: bool;



#desc Edits the given [Node]. The node will be also selected if it's inside the scene tree.
func edit_node() -> void:
	pass;

#desc Edits the given [Resource]. If the resource is a [Script] you can also edit it with [method edit_script] to specify the line and column position.
func edit_resource() -> void:
	pass;

#desc Edits the given [Script]. The line and column on which to open the script can also be specified. The script will be open with the user-configured editor for the script's language which may be an external editor.
func edit_script(script: Script, line: int, column: int, grab_focus: bool) -> void:
	pass;

#desc Returns the main container of Godot editor's window. For example, you can use it to retrieve the size of the container and place your controls accordingly.
#desc [b]Warning:[/b] Removing and freeing this node will render the editor useless and may cause a crash.
func get_base_control() -> Control:
	pass;

#desc Returns the editor's [EditorCommandPalette] instance.
#desc [b]Warning:[/b] Removing and freeing this node will render a part of the editor useless and may cause a crash.
func get_command_palette() -> EditorCommandPalette:
	pass;

#desc Returns the current path being viewed in the [FileSystemDock].
func get_current_path() -> String:
	pass;

#desc Returns the edited (current) scene's root [Node].
func get_edited_scene_root() -> Node:
	pass;

#desc Returns the editor control responsible for main screen plugins and tools. Use it with plugins that implement [method EditorPlugin._has_main_screen].
#desc [b]Warning:[/b] Removing and freeing this node will render a part of the editor useless and may cause a crash.
func get_editor_main_screen() -> VBoxContainer:
	pass;

func get_editor_paths() -> EditorPaths:
	pass;

#desc Returns the actual scale of the editor UI ([code]1.0[/code] being 100% scale). This can be used to adjust position and dimensions of the UI added by plugins.
#desc [b]Note:[/b] This value is set via the [code]interface/editor/display_scale[/code] and [code]interface/editor/custom_display_scale[/code] editor settings. Editor must be restarted for changes to be properly applied.
func get_editor_scale() -> float:
	pass;

#desc Returns the editor's [EditorSettings] instance.
func get_editor_settings() -> EditorSettings:
	pass;

#desc Returns the editor's [FileSystemDock] instance.
#desc [b]Warning:[/b] Removing and freeing this node will render a part of the editor useless and may cause a crash.
func get_file_system_dock() -> FileSystemDock:
	pass;

#desc Returns the editor's [EditorInspector] instance.
#desc [b]Warning:[/b] Removing and freeing this node will render a part of the editor useless and may cause a crash.
func get_inspector() -> EditorInspector:
	pass;

#desc Returns an [Array] with the file paths of the currently opened scenes.
func get_open_scenes() -> PackedStringArray:
	pass;

#desc Returns the name of the scene that is being played. If no scene is currently being played, returns an empty string.
func get_playing_scene() -> String:
	pass;

#desc Returns the editor's [EditorFileSystem] instance.
func get_resource_filesystem() -> EditorFileSystem:
	pass;

#desc Returns the editor's [EditorResourcePreview] instance.
func get_resource_previewer() -> EditorResourcePreview:
	pass;

#desc Returns the editor's [ScriptEditor] instance.
#desc [b]Warning:[/b] Removing and freeing this node will render a part of the editor useless and may cause a crash.
func get_script_editor() -> ScriptEditor:
	pass;

#desc Returns the path of the directory currently selected in the [FileSystemDock]. If a file is selected, its base directory will be returned using [method String.get_base_dir] instead.
func get_selected_path() -> String:
	pass;

#desc Returns the editor's [EditorSelection] instance.
func get_selection() -> EditorSelection:
	pass;

#desc Shows the given property on the given [param object] in the editor's Inspector dock. If [param inspector_only] is [code]true[/code], plugins will not attempt to edit [param object].
func inspect_object(object: Object, for_property: String, inspector_only: bool) -> void:
	pass;

#desc Returns [code]true[/code] if a scene is currently being played, [code]false[/code] otherwise. Paused scenes are considered as being played.
func is_playing_scene() -> bool:
	pass;

#desc Returns [code]true[/code] if the specified [param plugin] is enabled. The plugin name is the same as its directory name.
func is_plugin_enabled() -> bool:
	pass;

#desc Returns mesh previews rendered at the given size as an [Array] of [Texture2D]s.
func make_mesh_previews(meshes: Mesh[], preview_size: int) -> Texture2D[]:
	pass;

#desc Opens the scene at the given path.
func open_scene_from_path() -> void:
	pass;

#desc Plays the currently active scene.
func play_current_scene() -> void:
	pass;

#desc Plays the scene specified by its filepath.
func play_custom_scene() -> void:
	pass;

#desc Plays the main scene.
func play_main_scene() -> void:
	pass;

#desc Reloads the scene at the given path.
func reload_scene_from_path() -> void:
	pass;

#desc Restarts the editor. This closes the editor and then opens the same project. If [param save] is [code]true[/code], the project will be saved before restarting.
func restart_editor() -> void:
	pass;

#desc Saves the scene. Returns either [code]OK[/code] or [code]ERR_CANT_CREATE[/code] (see [@GlobalScope] constants).
func save_scene() -> int:
	pass;

#desc Saves the scene as a file at [param path].
func save_scene_as(path: String, with_preview: bool) -> void:
	pass;

#desc Selects the file, with the path provided by [param file], in the FileSystem dock.
func select_file() -> void:
	pass;

#desc Sets the editor's current main screen to the one specified in [param name]. [param name] must match the text of the tab in question exactly ([code]2D[/code], [code]3D[/code], [code]Script[/code], [code]AssetLib[/code]).
func set_main_screen_editor() -> void:
	pass;

#desc Sets the enabled status of a plugin. The plugin name is the same as its directory name.
func set_plugin_enabled(plugin: String, enabled: bool) -> void:
	pass;

#desc Stops the scene that is currently playing.
func stop_playing_scene() -> void:
	pass;


