extends Node
#brief Used by the editor to extend its functionality.
#desc Plugins are used by the editor to extend functionality. The most common types of plugins are those which edit a given node or resource type, import plugins and export plugins. See also [EditorScript] to add functions to the editor.
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

const CONTAINER_INSPECTOR_BOTTOM = 9;

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

#desc Represents the size of the [enum DockSlot] enum.
const DOCK_SLOT_MAX = 8;

#desc Forwards the [InputEvent] to other EditorPlugins.
const AFTER_GUI_INPUT_PASS = 0;

#desc Prevents the [InputEvent] from reaching other Editor classes.
const AFTER_GUI_INPUT_STOP = 1;

#desc Pass the [InputEvent] to other editor plugins except the main [Node3D] one. This can be used to prevent node selection changes and work with sub-gizmos instead.
const AFTER_GUI_INPUT_CUSTOM = 2;




#desc This method is called when the editor is about to save the project, switch to another tab, etc. It asks the plugin to apply any pending state changes to ensure consistency.
#desc This is used, for example, in shader editors to let the plugin know that it must apply the shader code being written by the user to the object.
func _apply_changes() -> void:
	pass;

#desc This method is called when the editor is about to run the project. The plugin can then perform required operations before the project runs.
#desc This method must return a boolean. If this method returns [code]false[/code], the project will not run. The run is aborted immediately, so this also prevents all other plugins' [method _build] methods from running.
func _build() -> bool:
	pass;

#desc Clear all the state and reset the object being edited to zero. This ensures your plugin does not keep editing a currently existing node, or a node from the wrong scene.
func _clear() -> void:
	pass;

#desc Called by the engine when the user disables the [EditorPlugin] in the Plugin tab of the project settings window.
func _disable_plugin() -> void:
	pass;

#desc This function is used for plugins that edit specific object types (nodes or resources). It requests the editor to edit the given object.
func _edit(object: Variant) -> void:
	pass;

#desc Called by the engine when the user enables the [EditorPlugin] in the Plugin tab of the project settings window.
func _enable_plugin() -> void:
	pass;

#desc Called by the engine when the 3D editor's viewport is updated. Use the [code]overlay[/code] [Control] for drawing. You can update the viewport manually by calling [method update_overlays].
#desc [codeblocks]
#desc [gdscript]
#desc func _forward_3d_draw_over_viewport(overlay):
#desc # Draw a circle at cursor position.
#desc overlay.draw_circle(overlay.get_local_mouse_position(), 64)
#desc 
#desc func _forward_3d_gui_input(camera, event):
#desc if event is InputEventMouseMotion:
#desc # Redraw viewport when cursor is moved.
#desc update_overlays()
#desc return EditorPlugin.AFTER_GUI_INPUT_STOP
#desc return EditorPlugin.AFTER_GUI_INPUT_PASS
#desc [/gdscript]
#desc [csharp]
#desc public override void _Forward3dDrawOverViewport(Godot.Control overlay)
#desc {
#desc // Draw a circle at cursor position.
#desc overlay.DrawCircle(overlay.GetLocalMousePosition(), 64, Colors.White);
#desc }
#desc 
#desc public override EditorPlugin.AfterGUIInput _Forward3dGuiInput(Godot.Camera3D camera, InputEvent @event)
#desc {
#desc if (@event is InputEventMouseMotion)
#desc {
#desc // Redraw viewport when cursor is moved.
#desc UpdateOverlays();
#desc return EditorPlugin.AFTER_GUI_INPUT_STOP;
#desc }
#desc return EditorPlugin.AFTER_GUI_INPUT_PASS;
#desc [/csharp]
#desc [/codeblocks]
func _forward_3d_draw_over_viewport(viewport_control: Control) -> void:
	pass;

#desc This method is the same as [method _forward_3d_draw_over_viewport], except it draws on top of everything. Useful when you need an extra layer that shows over anything else.
#desc You need to enable calling of this method by using [method set_force_draw_over_forwarding_enabled].
func _forward_3d_force_draw_over_viewport(viewport_control: Control) -> void:
	pass;

#desc Called when there is a root node in the current edited scene, [method _handles] is implemented, and an [InputEvent] happens in the 3D viewport. The return value decides whether the [InputEvent] is consumed or forwarded to other [EditorPlugin]s. See [enum AfterGUIInput] for options. Example:
#desc [codeblocks]
#desc [gdscript]
#desc # Prevents the InputEvent from reaching other Editor classes.
#desc func _forward_3d_gui_input(camera, event):
#desc return EditorPlugin.AFTER_GUI_INPUT_STOP
#desc [/gdscript]
#desc [csharp]
#desc // Prevents the InputEvent from reaching other Editor classes.
#desc public override EditorPlugin.AfterGUIInput _Forward3dGuiInput(Camera3D camera, InputEvent @event)
#desc {
#desc return EditorPlugin.AFTER_GUI_INPUT_STOP;
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc Must [code]return EditorPlugin.AFTER_GUI_INPUT_PASS[/code] in order to forward the [InputEvent] to other Editor classes. Example:
#desc [codeblocks]
#desc [gdscript]
#desc # Consumes InputEventMouseMotion and forwards other InputEvent types.
#desc func _forward_3d_gui_input(camera, event):
#desc return EditorPlugin.AFTER_GUI_INPUT_STOP if event is InputEventMouseMotion else EditorPlugin.AFTER_GUI_INPUT_PASS
#desc [/gdscript]
#desc [csharp]
#desc // Consumes InputEventMouseMotion and forwards other InputEvent types.
#desc public override EditorPlugin.AfterGUIInput _Forward3dGuiInput(Camera3D camera, InputEvent @event)
#desc {
#desc return @event is InputEventMouseMotion ? EditorPlugin.AFTER_GUI_INPUT_STOP : EditorPlugin.AFTER_GUI_INPUT_PASS;
#desc }
#desc [/csharp]
#desc [/codeblocks]
func _forward_3d_gui_input(viewport_camera: Camera3D, event: InputEvent) -> int:
	pass;

#desc Called by the engine when the 2D editor's viewport is updated. Use the [code]overlay[/code] [Control] for drawing. You can update the viewport manually by calling [method update_overlays].
#desc [codeblocks]
#desc [gdscript]
#desc func _forward_canvas_draw_over_viewport(overlay):
#desc # Draw a circle at cursor position.
#desc overlay.draw_circle(overlay.get_local_mouse_position(), 64, Color.white)
#desc 
#desc func _forward_canvas_gui_input(event):
#desc if event is InputEventMouseMotion:
#desc # Redraw viewport when cursor is moved.
#desc update_overlays()
#desc return true
#desc return false
#desc [/gdscript]
#desc [csharp]
#desc public override void ForwardCanvasDrawOverViewport(Godot.Control overlay)
#desc {
#desc // Draw a circle at cursor position.
#desc overlay.DrawCircle(overlay.GetLocalMousePosition(), 64, Colors.White);
#desc }
#desc 
#desc public override bool ForwardCanvasGuiInput(InputEvent @event)
#desc {
#desc if (@event is InputEventMouseMotion)
#desc {
#desc // Redraw viewport when cursor is moved.
#desc UpdateOverlays();
#desc return true;
#desc }
#desc return false;
#desc [/csharp]
#desc [/codeblocks]
func _forward_canvas_draw_over_viewport(viewport_control: Control) -> void:
	pass;

#desc This method is the same as [method _forward_canvas_draw_over_viewport], except it draws on top of everything. Useful when you need an extra layer that shows over anything else.
#desc You need to enable calling of this method by using [method set_force_draw_over_forwarding_enabled].
func _forward_canvas_force_draw_over_viewport(viewport_control: Control) -> void:
	pass;

#desc Called when there is a root node in the current edited scene, [method _handles] is implemented and an [InputEvent] happens in the 2D viewport. Intercepts the [InputEvent], if [code]return true[/code] [EditorPlugin] consumes the [param event], otherwise forwards [param event] to other Editor classes. Example:
#desc [codeblocks]
#desc [gdscript]
#desc # Prevents the InputEvent from reaching other Editor classes.
#desc func _forward_canvas_gui_input(event):
#desc return true
#desc [/gdscript]
#desc [csharp]
#desc // Prevents the InputEvent from reaching other Editor classes.
#desc public override bool ForwardCanvasGuiInput(InputEvent @event)
#desc {
#desc return true;
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc Must [code]return false[/code] in order to forward the [InputEvent] to other Editor classes. Example:
#desc [codeblocks]
#desc [gdscript]
#desc # Consumes InputEventMouseMotion and forwards other InputEvent types.
#desc func _forward_canvas_gui_input(event):
#desc if (event is InputEventMouseMotion):
#desc return true
#desc return false
#desc [/gdscript]
#desc [csharp]
#desc // Consumes InputEventMouseMotion and forwards other InputEvent types.
#desc public override bool ForwardCanvasGuiInput(InputEvent @event)
#desc {
#desc if (@event is InputEventMouseMotion) {
#desc return true;
#desc }
#desc return false
#desc }
#desc [/csharp]
#desc [/codeblocks]
func _forward_canvas_gui_input(event: InputEvent) -> bool:
	pass;

#desc This is for editors that edit script-based objects. You can return a list of breakpoints in the format ([code]script:line[/code]), for example: [code]res://path_to_script.gd:25[/code].
func _get_breakpoints() -> PackedStringArray:
	pass;

#desc Override this method in your plugin to return a [Texture2D] in order to give it an icon.
#desc For main screen plugins, this appears at the top of the screen, to the right of the "2D", "3D", "Script", and "AssetLib" buttons.
#desc Ideally, the plugin icon should be white with a transparent background and 16x16 pixels in size.
#desc [codeblocks]
#desc [gdscript]
#desc func _get_plugin_icon():
#desc # You can use a custom icon:
#desc return preload("res://addons/my_plugin/my_plugin_icon.svg")
#desc # Or use a built-in icon:
#desc return get_editor_interface().get_base_control().get_theme_icon("Node", "EditorIcons")
#desc [/gdscript]
#desc [csharp]
#desc public override Texture2D GetPluginIcon()
#desc {
#desc // You can use a custom icon:
#desc return ResourceLoader.Load<Texture2D>("res://addons/my_plugin/my_plugin_icon.svg");
#desc // Or use a built-in icon:
#desc return GetEditorInterface().GetBaseControl().GetThemeIcon("Node", "EditorIcons");
#desc }
#desc [/csharp]
#desc [/codeblocks]
func _get_plugin_icon() -> Texture2D:
	pass;

#desc Override this method in your plugin to provide the name of the plugin when displayed in the Godot editor.
#desc For main screen plugins, this appears at the top of the screen, to the right of the "2D", "3D", "Script", and "AssetLib" buttons.
func _get_plugin_name() -> String:
	pass;

#desc Override this method to provide a state data you want to be saved, like view position, grid settings, folding, etc. This is used when saving the scene (so state is kept when opening it again) and for switching tabs (so state can be restored when the tab returns). This data is automatically saved for each scene in an [code]editstate[/code] file in the editor metadata folder. If you want to store global (scene-independent) editor data for your plugin, you can use [method _get_window_layout] instead.
#desc Use [method _set_state] to restore your saved state.
#desc [b]Note:[/b] This method should not be used to save important settings that should persist with the project.
#desc [b]Note:[/b] You must implement [method _get_plugin_name] for the state to be stored and restored correctly.
#desc [codeblock]
#desc func _get_state():
#desc var state = {"zoom": zoom, "preferred_color": my_color}
#desc return state
#desc [/codeblock]
func _get_state() -> Dictionary:
	pass;

#desc Override this method to provide the GUI layout of the plugin or any other data you want to be stored. This is used to save the project's editor layout when [method queue_save_layout] is called or the editor layout was changed (for example changing the position of a dock). The data is stored in the [code]editor_layout.cfg[/code] file in the editor metadata directory.
#desc Use [method _set_window_layout] to restore your saved layout.
#desc [codeblock]
#desc func _get_window_layout(configuration):
#desc configuration.set_value("MyPlugin", "window_position", $Window.position)
#desc configuration.set_value("MyPlugin", "icon_color", $Icon.modulate)
#desc [/codeblock]
func _get_window_layout(configuration: ConfigFile) -> void:
	pass;

#desc Implement this function if your plugin edits a specific type of object (Resource or Node). If you return [code]true[/code], then you will get the functions [method _edit] and [method _make_visible] called when the editor requests them. If you have declared the methods [method _forward_canvas_gui_input] and [method _forward_3d_gui_input] these will be called too.
func _handles(object: Variant) -> bool:
	pass;

#desc Returns [code]true[/code] if this is a main screen editor plugin (it goes in the workspace selector together with [b]2D[/b], [b]3D[/b], [b]Script[/b] and [b]AssetLib[/b]).
#desc When the plugin's workspace is selected, other main screen plugins will be hidden, but your plugin will not appear automatically. It needs to be added as a child of [method EditorInterface.get_base_control] and made visible inside [method _make_visible].
#desc Use [method _get_plugin_name] and [method _get_plugin_icon] to customize the plugin button's appearance.
#desc [codeblock]
#desc var plugin_control
#desc 
#desc func _enter_tree():
#desc plugin_control = preload("my_plugin_control.tscn").instantiate()
#desc get_editor_interface().get_editor_main_screen().add_child(plugin_control)
#desc plugin_control.hide()
#desc 
#desc func _has_main_screen():
#desc return true
#desc 
#desc func _make_visible(visible):
#desc plugin_control.visible = visible
#desc 
#desc func _get_plugin_name():
#desc return "My Super Cool Plugin 3000"
#desc 
#desc func _get_plugin_icon():
#desc return get_editor_interface().get_base_control().get_theme_icon("Node", "EditorIcons")
#desc [/codeblock]
func _has_main_screen() -> bool:
	pass;

#desc This function will be called when the editor is requested to become visible. It is used for plugins that edit a specific object type.
#desc Remember that you have to manage the visibility of all your editor controls manually.
func _make_visible(visible: bool) -> void:
	pass;

#desc This method is called after the editor saves the project or when it's closed. It asks the plugin to save edited external scenes/resources.
func _save_external_data() -> void:
	pass;

#desc Restore the state saved by [method _get_state]. This method is called when the current scene tab is changed in the editor.
#desc [b]Note:[/b] Your plugin must implement [method _get_plugin_name], otherwise it will not be recognized and this method will not be called.
#desc [codeblock]
#desc func _set_state(data):
#desc zoom = data.get("zoom", 1.0)
#desc preferred_color = data.get("my_color", Color.white)
#desc [/codeblock]
func _set_state(state: Dictionary) -> void:
	pass;

#desc Restore the plugin GUI layout and data saved by [method _get_window_layout]. This method is called for every plugin on editor startup. Use the provided [param configuration] file to read your saved data.
#desc [codeblock]
#desc func _set_window_layout(configuration):
#desc $Window.position = configuration.get_value("MyPlugin", "window_position", Vector2())
#desc $Icon.modulate = configuration.get_value("MyPlugin", "icon_color", Color.white)
#desc [/codeblock]
func _set_window_layout(configuration: ConfigFile) -> void:
	pass;

#desc Adds a script at [param path] to the Autoload list as [param name].
func add_autoload_singleton(name: String, path: String) -> void:
	pass;

#desc Adds a control to the bottom panel (together with Output, Debug, Animation, etc). Returns a reference to the button added. It's up to you to hide/show the button when needed. When your plugin is deactivated, make sure to remove your custom control with [method remove_control_from_bottom_panel] and free it with [method Node.queue_free].
func add_control_to_bottom_panel(control: Control, title: String) -> Button:
	pass;

#desc Adds a custom control to a container (see [enum CustomControlContainer]). There are many locations where custom controls can be added in the editor UI.
#desc Please remember that you have to manage the visibility of your custom controls yourself (and likely hide it after adding it).
#desc When your plugin is deactivated, make sure to remove your custom control with [method remove_control_from_container] and free it with [method Node.queue_free].
func add_control_to_container(container: int, control: Control) -> void:
	pass;

#desc Adds the control to a specific dock slot (see [enum DockSlot] for options).
#desc If the dock is repositioned and as long as the plugin is active, the editor will save the dock position on further sessions.
#desc When your plugin is deactivated, make sure to remove your custom control with [method remove_control_from_docks] and free it with [method Node.queue_free].
func add_control_to_dock(slot: int, control: Control) -> void:
	pass;

#desc Adds a custom type, which will appear in the list of nodes or resources. An icon can be optionally passed.
#desc When a given node or resource is selected, the base type will be instantiated (e.g. "Node3D", "Control", "Resource"), then the script will be loaded and set to this object.
#desc You can use the virtual method [method _handles] to check if your custom object is being edited by checking the script or using the [code]is[/code] keyword.
#desc During run-time, this will be a simple object with a script so this function does not need to be called then.
#desc [b]Note:[/b] Custom types added this way are not true classes. They are just a helper to create a node with specific script.
func add_custom_type(type: String, base: String, script: Script, icon: Texture2D) -> void:
	pass;

#desc Adds a [Script] as debugger plugin to the Debugger. The script must extend [EditorDebuggerPlugin].
func add_debugger_plugin(script: Script) -> void:
	pass;

#desc Registers a new [EditorExportPlugin]. Export plugins are used to perform tasks when the project is being exported.
#desc See [method add_inspector_plugin] for an example of how to register a plugin.
func add_export_plugin(plugin: EditorExportPlugin) -> void:
	pass;

#desc Registers a new [EditorImportPlugin]. Import plugins are used to import custom and unsupported assets as a custom [Resource] type.
#desc If [param first_priority] is [code]true[/code], the new import plugin is inserted first in the list and takes precedence over pre-existing plugins.
#desc [b]Note:[/b] If you want to import custom 3D asset formats use [method add_scene_format_importer_plugin] instead.
#desc See [method add_inspector_plugin] for an example of how to register a plugin.
func add_import_plugin(importer: EditorImportPlugin, first_priority: bool) -> void:
	pass;

#desc Registers a new [EditorInspectorPlugin]. Inspector plugins are used to extend [EditorInspector] and provide custom configuration tools for your object's properties.
#desc [b]Note:[/b] Always use [method remove_inspector_plugin] to remove the registered [EditorInspectorPlugin] when your [EditorPlugin] is disabled to prevent leaks and an unexpected behavior.
#desc [codeblocks]
#desc [gdscript]
#desc const MyInspectorPlugin = preload("res://addons/your_addon/path/to/your/script.gd")
#desc var inspector_plugin = MyInspectorPlugin.new()
#desc 
#desc func _enter_tree():
#desc add_inspector_plugin(inspector_plugin)
#desc 
#desc func _exit_tree():
#desc remove_inspector_plugin(inspector_plugin)
#desc [/gdscript]
#desc [/codeblocks]
func add_inspector_plugin(plugin: EditorInspectorPlugin) -> void:
	pass;

#desc Registers a new [EditorNode3DGizmoPlugin]. Gizmo plugins are used to add custom gizmos to the 3D preview viewport for a [Node3D].
#desc See [method add_inspector_plugin] for an example of how to register a plugin.
func add_node_3d_gizmo_plugin(plugin: EditorNode3DGizmoPlugin) -> void:
	pass;

#desc Registers a new [EditorSceneFormatImporter]. Scene importers are used to import custom 3D asset formats as scenes.
#desc If [param first_priority] is [code]true[/code], the new import plugin is inserted first in the list and takes precedence over pre-existing plugins.
func add_scene_format_importer_plugin(scene_format_importer: EditorSceneFormatImporter, first_priority: bool) -> void:
	pass;

#desc Add a [EditorScenePostImportPlugin]. These plugins allow customizing the import process of 3D assets by adding new options to the import dialogs.
#desc If [param first_priority] is [code]true[/code], the new import plugin is inserted first in the list and takes precedence over pre-existing plugins.
func add_scene_post_import_plugin(scene_import_plugin: EditorScenePostImportPlugin, first_priority: bool) -> void:
	pass;

#desc Adds a custom menu item to [b]Project > Tools[/b] named [param name]. When clicked, the provided [param callable] will be called.
func add_tool_menu_item(name: String, callable: Callable) -> void:
	pass;

#desc Adds a custom [PopupMenu] submenu under [b]Project > Tools >[/b] [param name]. Use [code]remove_tool_menu_item(name)[/code] on plugin clean up to remove the menu.
func add_tool_submenu_item(name: String, submenu: PopupMenu) -> void:
	pass;

#desc Registers a custom translation parser plugin for extracting translatable strings from custom files.
func add_translation_parser_plugin(parser: EditorTranslationParserPlugin) -> void:
	pass;

#desc Hooks a callback into the undo/redo action creation when a property is modified in the inspector. This allows, for example, to save other properties that may be lost when a given property is modified.
#desc The callback should have 4 arguments: [Object] [code]undo_redo[/code], [Object] [code]modified_object[/code], [String] [code]property[/code] and [Variant] [code]new_value[/code]. They are, respectively, the [UndoRedo] object used by the inspector, the currently modified object, the name of the modified property and the new value the property is about to take.
func add_undo_redo_inspector_hook_callback(callable: Callable) -> void:
	pass;

#desc Returns the [EditorInterface] object that gives you control over Godot editor's window and its functionalities.
func get_editor_interface() -> EditorInterface:
	pass;

#desc Returns the [PopupMenu] under [b]Scene > Export As...[/b].
func get_export_as_menu() -> PopupMenu:
	pass;

#desc Gets the Editor's dialog used for making scripts.
#desc [b]Note:[/b] Users can configure it before use.
#desc [b]Warning:[/b] Removing and freeing this node will render a part of the editor useless and may cause a crash.
func get_script_create_dialog() -> ScriptCreateDialog:
	pass;

#desc Gets the undo/redo object. Most actions in the editor can be undoable, so use this object to make sure this happens when it's worth it.
func get_undo_redo() -> EditorUndoRedoManager:
	pass;

#desc Minimizes the bottom panel.
func hide_bottom_panel() -> void:
	pass;

#desc Makes a specific item in the bottom panel visible.
func make_bottom_panel_item_visible(item: Control) -> void:
	pass;

#desc Queue save the project's editor layout.
func queue_save_layout() -> void:
	pass;

#desc Removes an Autoload [param name] from the list.
func remove_autoload_singleton(name: String) -> void:
	pass;

#desc Removes the control from the bottom panel. You have to manually [method Node.queue_free] the control.
func remove_control_from_bottom_panel(control: Control) -> void:
	pass;

#desc Removes the control from the specified container. You have to manually [method Node.queue_free] the control.
func remove_control_from_container(container: int, control: Control) -> void:
	pass;

#desc Removes the control from the dock. You have to manually [method Node.queue_free] the control.
func remove_control_from_docks(control: Control) -> void:
	pass;

#desc Removes a custom type added by [method add_custom_type].
func remove_custom_type(type: String) -> void:
	pass;

#desc Removes the debugger plugin with given script from the Debugger.
func remove_debugger_plugin(script: Script) -> void:
	pass;

#desc Removes an export plugin registered by [method add_export_plugin].
func remove_export_plugin(plugin: EditorExportPlugin) -> void:
	pass;

#desc Removes an import plugin registered by [method add_import_plugin].
func remove_import_plugin(importer: EditorImportPlugin) -> void:
	pass;

#desc Removes an inspector plugin registered by [method add_import_plugin]
func remove_inspector_plugin(plugin: EditorInspectorPlugin) -> void:
	pass;

#desc Removes a gizmo plugin registered by [method add_node_3d_gizmo_plugin].
func remove_node_3d_gizmo_plugin(plugin: EditorNode3DGizmoPlugin) -> void:
	pass;

#desc Removes a scene format importer registered by [method add_scene_format_importer_plugin].
func remove_scene_format_importer_plugin(scene_format_importer: EditorSceneFormatImporter) -> void:
	pass;

#desc Remove the [EditorScenePostImportPlugin], added with [method add_scene_post_import_plugin].
func remove_scene_post_import_plugin(scene_import_plugin: EditorScenePostImportPlugin) -> void:
	pass;

#desc Removes a menu [param name] from [b]Project > Tools[/b].
func remove_tool_menu_item(name: String) -> void:
	pass;

#desc Removes a custom translation parser plugin registered by [method add_translation_parser_plugin].
func remove_translation_parser_plugin(parser: EditorTranslationParserPlugin) -> void:
	pass;

#desc Removes a callback previsously added by [method add_undo_redo_inspector_hook_callback].
func remove_undo_redo_inspector_hook_callback(callable: Callable) -> void:
	pass;

#desc Enables calling of [method _forward_canvas_force_draw_over_viewport] for the 2D editor and [method _forward_3d_force_draw_over_viewport] for the 3D editor when their viewports are updated. You need to call this method only once and it will work permanently for this plugin.
func set_force_draw_over_forwarding_enabled() -> void:
	pass;

#desc Use this method if you always want to receive inputs from 3D view screen inside [method _forward_3d_gui_input]. It might be especially usable if your plugin will want to use raycast in the scene.
func set_input_event_forwarding_always_enabled() -> void:
	pass;

#desc Updates the overlays of the 2D and 3D editor viewport. Causes methods [method _forward_canvas_draw_over_viewport], [method _forward_canvas_force_draw_over_viewport], [method _forward_3d_draw_over_viewport] and [method _forward_3d_force_draw_over_viewport] to be called.
func update_overlays() -> int:
	pass;


