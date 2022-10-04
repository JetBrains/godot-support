extends Resource
#brief Object that holds the project-independent editor settings.
#desc Object that holds the project-independent editor settings. These settings are generally visible in the [b]Editor > Editor Settings[/b] menu.
#desc Property names use slash delimiters to distinguish sections. Setting values can be of any [Variant] type. It's recommended to use [code]snake_case[/code] for editor settings to be consistent with the Godot editor itself.
#desc Accessing the settings can be done using the following methods, such as:
#desc [codeblocks]
#desc [gdscript]
#desc var settings = EditorInterface.get_editor_settings()
#desc # `settings.set("some/property", 10)` also works as this class overrides `_set()` internally.
#desc settings.set_setting("some/property", 10)
#desc # `settings.get("some/property")` also works as this class overrides `_get()` internally.
#desc settings.get_setting("some/property")
#desc var list_of_settings = settings.get_property_list()
#desc [/gdscript]
#desc [csharp]
#desc EditorSettings settings = GetEditorInterface().GetEditorSettings();
#desc // `settings.set("some/property", value)` also works as this class overrides `_set()` internally.
#desc settings.SetSetting("some/property", Value);
#desc // `settings.get("some/property", value)` also works as this class overrides `_get()` internally.
#desc settings.GetSetting("some/property");
#desc Godot.Collections.Array listOfSettings = settings.GetPropertyList();
#desc [/csharp]
#desc [/codeblocks]
#desc [b]Note:[/b] This class shouldn't be instantiated directly. Instead, access the singleton using [method EditorInterface.get_editor_settings].
class_name EditorSettings

#desc Emitted after any editor setting has changed. It's used by various editor plugins to update their visuals on theme changes or logic on configuration changes.
const NOTIFICATION_EDITOR_SETTINGS_CHANGED = 10000;


#desc The size of the profiler's frame history. The default value (3600) allows seeing up to 60 seconds of profiling if the project renders at a constant 60 FPS. Higher values allow viewing longer periods of profiling in the graphs, especially when the project is running at high framerates.
var debugger/profiler_frame_history_size: int;

#desc If [code]true[/code], displays folders in the FileSystem dock's bottom pane when split mode is enabled. If [code]false[/code], only files will be displayed in the bottom pane. Split mode can be toggled by pressing the icon next to the [code]res://[/code] folder path.
#desc [b]Note:[/b] This setting has no effect when split mode is disabled (which is the default).
var docks/filesystem/always_show_folders: bool;

#desc List of file extensions to consider as editable text files in the FileSystem dock (by double-clicking on the files).
var docks/filesystem/textfile_extensions: String;

#desc The thumbnail size to use in the FileSystem dock (in pixels). See also [member filesystem/file_dialog/thumbnail_size].
var docks/filesystem/thumbnail_size: int;

#desc The refresh interval to use for the inspector dock's properties. The effect of this setting is mainly noticeable when adjusting gizmos in the 2D/3D editor and looking at the inspector at the same time. Lower values make the inspector more often, but take up more CPU time.
var docks/property_editor/auto_refresh_interval: float;

#desc The tint intensity to use for the subresources background in the inspector dock. The tint is used to distinguish between different subresources in the inspector. Higher values result in a more noticeable background color difference.
var docks/property_editor/subresource_hue_tint: float;

#desc If [code]true[/code], the scene tree dock will automatically unfold nodes when a node that has folded parents is selected.
var docks/scene_tree/auto_expand_to_selected: bool;

#desc If [code]true[/code], the Create dialog (Create New Node/Create New Resource) will start with all its sections expanded. Otherwise, sections will be collapsed until the user starts searching (which will automatically expand sections as needed).
var docks/scene_tree/start_create_dialog_fully_expanded: bool;

#desc The "start" stop of the color gradient to use for bones in the 2D skeleton editor.
var editors/2d/bone_color1: Color;

#desc The "end" stop of the color gradient to use for bones in the 2D skeleton editor.
var editors/2d/bone_color2: Color;

#desc The color to use for inverse kinematics-enabled bones in the 2D skeleton editor.
var editors/2d/bone_ik_color: Color;

#desc The outline color to use for non-selected bones in the 2D skeleton editor. See also [member editors/2d/bone_selected_color].
var editors/2d/bone_outline_color: Color;

#desc The outline size in the 2D skeleton editor (in pixels). See also [member editors/2d/bone_width].
var editors/2d/bone_outline_size: int;

#desc The color to use for selected bones in the 2D skeleton editor. See also [member editors/2d/bone_outline_color].
var editors/2d/bone_selected_color: Color;

#desc The bone width in the 2D skeleton editor (in pixels). See also [member editors/2d/bone_outline_size].
var editors/2d/bone_width: int;

#desc If [code]true[/code], prevents the 2D editor viewport from leaving the scene. Limits are calculated dynamically based on nodes present in the current scene. If [code]false[/code], the 2D editor viewport will be able to move freely, but you risk getting lost when zooming out too far. You can refocus on the scene by selecting a node then pressing [kbd]F[/kbd].
var editors/2d/constrain_editor_view: bool;

#desc The grid color to use in the 2D editor.
var editors/2d/grid_color: Color;

#desc The guides color to use in the 2D editor. Guides can be created by dragging the mouse cursor from the rulers.
var editors/2d/guides_color: Color;

#desc The color to use when drawing smart snapping lines in the 2D editor. The smart snapping lines will automatically display when moving 2D nodes if smart snapping is enabled in the Snapping Options menu at the top of the 2D editor viewport.
var editors/2d/smart_snapping_line_color: Color;

#desc The color of the viewport border in the 2D editor. This border represents the viewport's size at the base resolution defined in the Project Settings. Objects placed outside this border will not be visible unless a [Camera2D] node is used, or unless the window is resized and the stretch mode is set to [code]disabled[/code].
var editors/2d/viewport_border_color: Color;

#desc The default camera field of view to use in the 3D editor (in degrees). The camera field of view can be adjusted on a per-scene basis using the [b]View[/b] menu at the top of the 3D editor. If a scene had its camera field of view adjusted using the [b]View[/b] menu, this setting is ignored in the scene in question. This setting is also ignored while a Camera3D node is being previewed in the editor.
var editors/3d/default_fov: float;

#desc The default camera far clip distance to use in the 3D editor (in degrees). Higher values make it possible to view objects placed further away from the camera, at the cost of lower precision in the depth buffer (which can result in visible Z-fighting in the distance). The camera far clip distance can be adjusted on a per-scene basis using the [b]View[/b] menu at the top of the 3D editor. If a scene had its camera far clip distance adjusted using the [b]View[/b] menu, this setting is ignored in the scene in question. This setting is also ignored while a Camera3D node is being previewed in the editor.
var editors/3d/default_z_far: float;

#desc The default camera near clip distance to use in the 3D editor (in degrees). Lower values make it possible to view objects placed closer to the camera, at the cost of lower precision in the depth buffer (which can result in visible Z-fighting in the distance). The camera near clip distance can be adjusted on a per-scene basis using the [b]View[/b] menu at the top of the 3D editor. If a scene had its camera near clip distance adjusted using the [b]View[/b] menu, this setting is ignored in the scene in question. This setting is also ignored while a Camera3D node is being previewed in the editor.
var editors/3d/default_z_near: float;

#desc The modifier key to use to enable freelook in the 3D editor (on top of pressing the right mouse button).
#desc [b]Note:[/b] Regardless of this setting, the freelook toggle keyboard shortcut ([kbd]Shift + F[/kbd] by default) is always available.
#desc [b]Note:[/b] On certain window managers on Linux, the [kbd]Alt[/kbd] key will be intercepted by the window manager when clicking a mouse button at the same time. This means Godot will not see the modifier key as being pressed.
var editors/3d/freelook/freelook_activation_modifier: int;

#desc The base 3D freelook speed in units per second. This can be adjusted by using the mouse wheel while in freelook mode, or by holding down the "fast" or "slow" modifier keys ([kbd]Shift[/kbd] and [kbd]Alt[/kbd] by default, respectively).
var editors/3d/freelook/freelook_base_speed: float;

#desc The inertia of the 3D freelook camera. Higher values make the camera start and stop slower, which looks smoother but adds latency.
var editors/3d/freelook/freelook_inertia: float;

#desc The navigation scheme to use when freelook is enabled in the 3D editor. Some of the navigation schemes below may be more convenient when designing specific levels in the 3D editor.
#desc - [b]Default:[/b] The "Freelook Forward", "Freelook Backward", "Freelook Up" and "Freelook Down" keys will move relative to the camera, taking its pitch angle into account for the movement.
#desc - [b]Partially Axis-Locked:[/b] The "Freelook Forward" and "Freelook Backward" keys will move relative to the camera, taking its pitch angle into account for the movement. The "Freelook Up" and "Freelook Down" keys will move in an "absolute" manner, [i]not[/i] taking the camera's pitch angle into account for the movement.
#desc - [b]Fully Axis-Locked:[/b] The "Freelook Forward", "Freelook Backward", "Freelook Up" and "Freelook Down" keys will move in an "absolute" manner, [i]not[/i] taking the camera's pitch angle into account for the movement.
#desc See also [member editors/3d/navigation/navigation_scheme].
var editors/3d/freelook/freelook_navigation_scheme: int;

#desc The mouse sensitivity to use while freelook mode is active in the 3D editor. See also [member editors/3d/navigation_feel/orbit_sensitivity].
var editors/3d/freelook/freelook_sensitivity: float;

#desc If [code]true[/code], freelook speed is linked to the zoom value used in the camera orbit mode in the 3D editor.
var editors/3d/freelook/freelook_speed_zoom_link: bool;

#desc The grid division bias to use in the 3D editor. Negative values will cause small grid divisions to appear earlier, whereas positive values will cause small grid divisions to appear later.
var editors/3d/grid_division_level_bias: float;

#desc The smallest grid division to use in the 3D editor, specified as a power of 2. The grid will not be able to get larger than [code]1 ^ grid_division_level_max[/code] units. By default, this means grid divisions cannot get smaller than 100 units each, no matter how far away the camera is from the grid.
var editors/3d/grid_division_level_max: int;

#desc The smallest grid division to use in the 3D editor, specified as a power of 2. The grid will not be able to get smaller than [code]1 ^ grid_division_level_min[/code] units. By default, this means grid divisions cannot get smaller than 1 unit each, no matter how close the camera is from the grid.
var editors/3d/grid_division_level_min: int;

#desc The grid size in units. Higher values prevent the grid from appearing "cut off" at certain angles, but make the grid more demanding to render. Depending on the camera's position, the grid may not be fully visible since a shader is used to fade it progressively.
var editors/3d/grid_size: int;

#desc If [code]true[/code], render the grid on an XY plane. This can be useful for 3D side-scrolling games.
var editors/3d/grid_xy_plane: bool;

#desc If [code]true[/code], render the grid on an XZ plane.
var editors/3d/grid_xz_plane: bool;

#desc If [code]true[/code], render the grid on an YZ plane. This can be useful for 3D side-scrolling games.
var editors/3d/grid_yz_plane: bool;

#desc If [code]true[/code], enables 3-button mouse emulation mode. This is useful on laptops when using a trackpad.
#desc When 3-button mouse emulation mode is enabled, the pan, zoom and orbit modifiers can always be used in the 3D editor viewport, even when not holding down any mouse button.
#desc [b]Note:[/b] No matter the orbit modifier configured in [member editors/3d/navigation/orbit_modifier], [kbd]Alt[/kbd] will always remain usable for orbiting in this mode to improve usability with graphics tablets.
var editors/3d/navigation/emulate_3_button_mouse: bool;

#desc If [code]true[/code], allows using the top row [kbd]0[/kbd]-[kbd]9[/kbd] keys to function as their equivalent numpad keys for 3D editor navigation. This should be enabled on keyboards that have no numeric keypad available.
var editors/3d/navigation/emulate_numpad: bool;

#desc If [code]true[/code], invert the horizontal mouse axis when panning or orbiting in the 3D editor. This setting does [i]not[/i] apply to freelook mode.
var editors/3d/navigation/invert_x_axis: bool;

#desc If [code]true[/code], invert the vertical mouse axis when panning, orbiting, or using freelook mode in the 3D editor.
var editors/3d/navigation/invert_y_axis: bool;

#desc The navigation scheme to use in the 3D editor. Changing this setting will affect the mouse buttons that must be held down to perform certain operations in the 3D editor viewport.
#desc - [b]Godot[/b] Middle mouse button to orbit, [kbd]Shift + Middle mouse button[/kbd] to pan. [kbd]Mouse wheel[/kbd] to zoom.
#desc - [b]Maya:[/b] [kbd]Alt + Left mouse button[/kbd] to orbit. [kbd]Middle mouse button[/kbd] to pan, [kbd]Shift + Middle mouse button[/kbd] to pan 10 times faster. [kbd]Mouse wheel[/kbd] to zoom.
#desc - [b]Modo:[/b] [kbd]Alt + Left mouse button[/kbd] to orbit. [kbd]Alt + Shift + Left mouse button[/kbd] to pan. [kbd]Ctrl + Alt + Left mouse button[/kbd] to zoom.
#desc See also [member editors/3d/freelook/freelook_navigation_scheme].
#desc [b]Note:[/b] On certain window managers on Linux, the [kbd]Alt[/kbd] key will be intercepted by the window manager when clicking a mouse button at the same time. This means Godot will not see the modifier key as being pressed.
var editors/3d/navigation/navigation_scheme: int;

#desc The modifier key that must be held to orbit in the 3D editor.
#desc [b]Note:[/b] If [member editors/3d/navigation/emulate_3_button_mouse] is [code]true[/code], [kbd]Alt[/kbd] will always remain usable for orbiting to improve usability with graphics tablets.
#desc [b]Note:[/b] On certain window managers on Linux, the [kbd]Alt[/kbd] key will be intercepted by the window manager when clicking a mouse button at the same time. This means Godot will not see the modifier key as being pressed.
var editors/3d/navigation/orbit_modifier: int;

#desc The modifier key that must be held to pan in the 3D editor.
#desc [b]Note:[/b] On certain window managers on Linux, the [kbd]Alt[/kbd] key will be intercepted by the window manager when clicking a mouse button at the same time. This means Godot will not see the modifier key as being pressed.
var editors/3d/navigation/pan_modifier: int;

#desc If [code]true[/code], warps the mouse around the 3D viewport while panning in the 3D editor. This makes it possible to pan over a large area without having to exit panning then mouse the mouse back constantly.
var editors/3d/navigation/warped_mouse_panning: bool;

#desc The modifier key that must be held to zoom in the 3D editor.
#desc [b]Note:[/b] On certain window managers on Linux, the [kbd]Alt[/kbd] key will be intercepted by the window manager when clicking a mouse button at the same time. This means Godot will not see the modifier key as being pressed.
var editors/3d/navigation/zoom_modifier: int;

#desc The mouse cursor movement direction to use when zooming by moving the mouse. This does not affect zooming with the mouse wheel.
var editors/3d/navigation/zoom_style: int;

#desc The inertia to use when orbiting in the 3D editor. Higher values make the camera start and stop slower, which looks smoother but adds latency.
var editors/3d/navigation_feel/orbit_inertia: float;

#desc The mouse sensitivity to use when orbiting in the 3D editor. See also [member editors/3d/freelook/freelook_sensitivity].
var editors/3d/navigation_feel/orbit_sensitivity: float;

#desc The inertia to use when panning in the 3D editor. Higher values make the camera start and stop slower, which looks smoother but adds latency.
var editors/3d/navigation_feel/translation_inertia: float;

#desc The inertia to use when zooming in the 3D editor. Higher values make the camera start and stop slower, which looks smoother but adds latency.
var editors/3d/navigation_feel/zoom_inertia: float;

#desc The color to use for the primary 3D grid. The color's alpha channel affects the grid's opacity.
var editors/3d/primary_grid_color: Color;

#desc If set above 0, where a primary grid line should be drawn. By default, primary lines are configured to be more visible than secondary lines. This helps with measurements in the 3D editor. See also [member editors/3d/primary_grid_color] and [member editors/3d/secondary_grid_color].
var editors/3d/primary_grid_steps: int;

#desc The color to use for the secondary 3D grid. This is generally a less visible color than [member editors/3d/primary_grid_color]. The color's alpha channel affects the grid's opacity.
var editors/3d/secondary_grid_color: Color;

#desc The color to use for the selection box that surrounds selected nodes in the 3D editor viewport. The color's alpha channel influences the selection box's opacity.
var editors/3d/selection_box_color: Color;

#desc The color override to use for 3D editor gizmos if the [Node3D] in question is part of an instanced scene file (from the perspective of the current scene).
var editors/3d_gizmos/gizmo_colors/instantiated: Color;

#desc The 3D editor gizmo color for [Joint3D]s and [PhysicalBone3D]s.
var editors/3d_gizmos/gizmo_colors/joint: Color;

#desc The 3D editor gizmo color for [CollisionShape3D]s, [VehicleWheel3D]s, [RayCast3D]s and [SpringArm3D]s.
var editors/3d_gizmos/gizmo_colors/shape: Color;

#desc If [code]true[/code], automatically updates animation tracks' target paths when renaming or reparenting nodes in the Scene tree dock.
var editors/animation/autorename_animation_tracks: bool;

#desc If [code]true[/code], display a confirmation dialog when adding a new track to an animation by pressing the "key" icon next to a property.
var editors/animation/confirm_insert_track: bool;

#desc If [code]true[/code], create a Bezier track instead of a standard track when pressing the "key" icon next to a property. Bezier tracks provide more control over animation curves, but are more difficult to adjust quickly.
var editors/animation/default_create_bezier_tracks: bool;

#desc If [code]true[/code], create a [code]RESET[/code] track when creating a new animation track. This track can be used to restore the animation to a "default" state.
var editors/animation/default_create_reset_tracks: bool;

#desc The modulate color to use for "future" frames displayed in the animation editor's onion skinning feature.
var editors/animation/onion_layers_future_color: Color;

#desc The modulate color to use for "past" frames displayed in the animation editor's onion skinning feature.
var editors/animation/onion_layers_past_color: Color;

#desc The maximum distance at which tiles can be placed on a GridMap, relative to the camera position (in 3D units).
var editors/grid_map/pick_distance: float;

#desc The panning speed when using the mouse wheel or touchscreen events in the 2D editor. This setting does not apply to panning by holding down the middle or right mouse buttons.
var editors/panning/2d_editor_pan_speed: int;

#desc Controls whether the mouse wheel scroll zooms or pans in the 2D editor. See also [member editors/panning/sub_editors_panning_scheme] and [member editors/panning/animation_editors_panning_scheme].
var editors/panning/2d_editor_panning_scheme: int;

#desc Controls whether the mouse wheel scroll zooms or pans in the animation track and Bezier editors. See also [member editors/panning/2d_editor_panning_scheme] and [member editors/panning/sub_editors_panning_scheme] (which controls the animation blend tree editor's pan behavior).
var editors/panning/animation_editors_panning_scheme: int;

#desc If [code]true[/code], allows panning by holding down [kbd]Space[/kbd] in the 2D editor viewport (in addition to panning with the middle or right mouse buttons). If [code]false[/code], the left mouse button must be held down while holding down [kbd]Space[/kbd] to pan in the 2D editor viewport.
var editors/panning/simple_panning: bool;

#desc Controls whether the mouse wheel scroll zooms or pans in subeditors. The list of affected subeditors is: animation blend tree editor, [Polygon2D] editor, tileset editor, texture region editor and visual shader editor. See also [member editors/panning/2d_editor_panning_scheme] and [member editors/panning/animation_editors_panning_scheme].
var editors/panning/sub_editors_panning_scheme: int;

#desc If [code]true[/code], warps the mouse around the 2D viewport while panning in the 2D editor. This makes it possible to pan over a large area without having to exit panning then mouse the mouse back constantly.
var editors/panning/warped_mouse_panning: bool;

#desc The radius in which points can be selected in the [Polygon2D] and [CollisionPolygon2D] editors (in pixels). Higher values make it easier to select points quickly, but can make it more difficult to select the expected point when several points are located close to each other.
var editors/polygon_editor/point_grab_radius: int;

#desc If [code]true[/code], displays the polygon's previous shape in the 2D polygon editors with an opaque gray outline. This outline is displayed while dragging a point until the left mouse button is released.
var editors/polygon_editor/show_previous_outline: bool;

#desc If [code]true[/code], displays a grid while the TileMap editor is active. See also [member editors/tiles_editor/grid_color].
var editors/tiles_editor/display_grid: bool;

#desc The color to use for the TileMap editor's grid.
#desc [b]Note:[/b] Only effective if [member editors/tiles_editor/display_grid] is [code]true[/code].
var editors/tiles_editor/grid_color: Color;

#desc The curvature to use for connection lines in the visual shader editor. Higher values will make connection lines appear more curved, with values above [code]0.5[/code] resulting in more "angular" turns in the middle of connection lines.
var editors/visual_editors/lines_curvature: float;

#desc The opacity of the minimap displayed in the bottom-right corner of the visual shader editor.
var editors/visual_editors/minimap_opacity: float;

#desc The size to use for port previews in the visual shader uniforms (toggled by clicking the "eye" icon next to an output). The value is defined in pixels at 100% zoom, and will scale with zoom automatically.
var editors/visual_editors/visual_shader/port_preview_size: int;

#desc The folder where projects should be scanned for (recursively), in a way similar to the project manager's [b]Scan[/b]button. This can be set to the same value as [member filesystem/directories/default_project_path] for convenience.
#desc [b]Note:[/b] Setting this path to a folder with very large amounts of files/folders can slow down the project manager startup significantly. To keep the project manager quick to start up, it is recommended to set this value to a folder as "specific" as possible.
var filesystem/directories/autoscan_project_path: String;

#desc The folder where new projects should be created by default when clicking the project manager's [b]New Project[/b] button. This can be set to the same value as [member filesystem/directories/autoscan_project_path] for convenience.
var filesystem/directories/default_project_path: String;

#desc The display mode to use in the editor's file dialogs.
#desc - [b]Thumbnails[/b] takes more space, but displays dynamic resource thumbnails, making resources easier to preview without having to open them.
#desc - [b]List[/b] is more compact but doesn't display dynamic resource thumbnails. Instead, it displays static icons based on the file extension.
var filesystem/file_dialog/display_mode: int;

#desc If [code]true[/code], display hidden files in the editor's file dialogs. Files that have names starting with [code].[/code] are considered hidden (e.g. [code].hidden_file[/code]).
var filesystem/file_dialog/show_hidden_files: bool;

#desc The thumbnail size to use in the editor's file dialogs (in pixels). See also [member docks/filesystem/thumbnail_size].
var filesystem/file_dialog/thumbnail_size: int;

#desc If [code]true[/code], uses lossless compression for binary resources.
var filesystem/on_save/compress_binary_resources: bool;

#desc If [code]true[/code], when saving a file, the editor will rename the old file to a different name, save a new file, then only remove the old file once the new file has been saved. This makes loss of data less likely to happen if the editor or operating system exits unexpectedly while saving (e.g. due to a crash or power outage).
#desc [b]Note:[/b] On Windows, this feature can interact negatively with certain antivirus programs. In this case, you may have to set this to [code]false[/code] to prevent file locking issues.
var filesystem/on_save/safe_save_on_backup_then_rename: bool;

#desc If [code]true[/code], automatically opens screenshots with the default program associated to [code].png[/code] files after a screenshot is taken using the [b]Editor > Take Screenshot[/b] action.
var interface/editor/automatically_open_screenshots: bool;

#desc The font to use for the script editor. Must be a resource of a [Font] type such as a [code].ttf[/code] or [code].otf[/code] font file.
var interface/editor/code_font: String;

#desc The font ligatures to enable for the currently configured code font. Not all fonts include support for ligatures.
#desc [b]Note:[/b] The default editor code font ([url=https://www.jetbrains.com/lp/mono/]JetBrains Mono[/url]) has contextual ligatures in its font file.
var interface/editor/code_font_contextual_ligatures: int;

#desc List of custom OpenType features to use, if supported by the currently configured code font. Not all fonts include support for custom OpenType features. The string should follow the OpenType specification.
#desc [b]Note:[/b] The default editor code font ([url=https://www.jetbrains.com/lp/mono/]JetBrains Mono[/url]) has custom OpenType features in its font file, but there is no documented list yet.
var interface/editor/code_font_custom_opentype_features: String;

#desc List of alternative characters to use, if supported by the currently configured code font. Not all fonts include support for custom variations. The string should follow the OpenType specification.
#desc [b]Note:[/b] The default editor code font ([url=https://www.jetbrains.com/lp/mono/]JetBrains Mono[/url]) has alternate characters in its font file, but there is no documented list yet.
var interface/editor/code_font_custom_variations: String;

#desc The size of the font in the script editor. This setting does not impact the font size of the Output panel (see [member run/output/font_size]).
var interface/editor/code_font_size: int;

#desc The custom editor scale factor to use. This can be used for displays with very high DPI where a scale factor of 200% is not sufficient.
#desc [b]Note:[/b] Only effective if [member interface/editor/display_scale] is set to [b]Custom[/b].
var interface/editor/custom_display_scale: float;

#desc If [code]true[/code], lengthens the editor's localizable strings and replaces their characters with accented variants. This allows spotting non-localizable strings easily, while also ensuring the UI layout doesn't break when strings are made longer (as many languages require strings to be longer).
#desc This is a debugging feature and should only be enabled when working on the editor itself.
var interface/editor/debug/enable_pseudolocalization: bool;

#desc The display scale factor to use for the editor interface. Higher values are more suited to hiDPI/Retina displays.
#desc If set to [b]Auto[/b], the editor scale is automatically determined based on the screen resolution and reported display DPI. This heuristic is not always ideal, which means you can get better results by setting the editor scale manually.
#desc If set to [b]Custom[/b], the scaling value in [member interface/editor/custom_display_scale] will be used.
var interface/editor/display_scale: int;

#desc The language to use for the editor interface.
#desc Translations are provided by the community. If you spot a mistake, [url=https://docs.godotengine.org/en/latest/community/contributing/editor_and_docs_localization.html]contribute to editor translations on Weblate![/url]
var interface/editor/editor_language: String;

#desc Expanding main editor window content to the title, if supported by [DisplayServer]. See [constant DisplayServer.WINDOW_FLAG_EXTEND_TO_TITLE].
#desc Specific to the macOS platform.
var interface/editor/expand_to_title: bool;

#desc FreeType's font anti-aliasing mode used to render the editor fonts. Most fonts are not designed to look good with anti-aliasing disabled, so it's recommended to leave this enabled unless you're using a pixel art font.
var interface/editor/font_antialiasing: int;

#desc The font hinting mode to use for the editor fonts. FreeType supports the following font hinting modes:
#desc - [b]None:[/b] Don't use font hinting when rasterizing the font. This results in a smooth font, but it can look blurry.
#desc - [b]Light:[/b] Use hinting on the X axis only. This is a compromise between font sharpness and smoothness.
#desc - [b]Normal:[/b] Use hinting on both X and Y axes. This results in a sharp font, but it doesn't look very smooth.
#desc If set to [b]Auto[/b], the font hinting mode will be set to match the current operating system in use. This means the [b]Light[/b] hinting mode will be used on Windows and Linux, and the [b]None[/b] hinting mode will be used on macOS.
var interface/editor/font_hinting: int;

#desc The subpixel positioning mode to use when rendering editor font glyphs. This affects both the main and code fonts. [b]Disabled[/b] is the fastest to render and uses the least memory. [b]Auto[/b] only uses subpixel positioning for small font sizes (where the benefit is the most noticeable). [b]One half of a pixel[/b] and [b]One quarter of a pixel[/b] force the same subpixel positioning mode for all editor fonts, regardless of their size (with [b]One quarter of a pixel[/b] being the highest-quality option).
var interface/editor/font_subpixel_positioning: int;

#desc The amount of sleeping between frames when the low-processor usage mode is enabled (in microseconds). Higher values will result in lower CPU/GPU usage, which can improve battery life on laptops. However, higher values will result in a less responsive editor. The default value is set to allow for maximum smoothness on monitors up to 144 Hz. See also [member interface/editor/unfocused_low_processor_mode_sleep_usec].
var interface/editor/low_processor_mode_sleep_usec: float;

#desc The font to use for the editor interface. Must be a resource of a [Font] type such as a [code].ttf[/code] or [code].otf[/code] font file.
var interface/editor/main_font: String;

#desc The font to use for bold text in the editor interface. Must be a resource of a [Font] type such as a [code].ttf[/code] or [code].otf[/code] font file.
var interface/editor/main_font_bold: String;

#desc The size of the font in the editor interface.
var interface/editor/main_font_size: int;

#desc If [code]true[/code], the mouse's additional side buttons will be usable to navigate in the script editor's file history. Set this to [code]false[/code] if you're using the side buttons for other purposes (such as a push-to-talk button in a VoIP program).
var interface/editor/mouse_extra_buttons_navigate_history: bool;

#desc If [code]true[/code], the editor will save all scenes when confirming the [b]Save[/b] action when quitting the editor or quitting to the project list. If [code]false[/code], the editor will ask to save each scene individually.
var interface/editor/save_each_scene_on_quit: bool;

#desc If [code]true[/code], the editor's Script tab will have a separate distraction mode setting from the 2D/3D/AssetLib tabs. If [code]false[/code], the distraction-free mode toggle is shared between all tabs.
var interface/editor/separate_distraction_mode: bool;

#desc If enabled, displays internal engine errors in toast notifications (toggleable by clicking the "bell" icon at the bottom of the editor). No matter the value of this setting, non-internal engine errors will always be visible in toast notifications.
#desc The default [b]Auto[/b] value will only enable this if the editor was compiled with the [code]dev=yes[/code] option (the default is [code]dev=no[/code]).
var interface/editor/show_internal_errors_in_toast_notifications: int;

#desc If [code]true[/code], embed modal windows such as docks inside the main editor window. When single-window mode is enabled, tooltips will also be embedded inside the main editor window, which means they can't be displayed outside of the editor window.
var interface/editor/single_window_mode: bool;

#desc When the editor window is unfocused, the amount of sleeping between frames when the low-processor usage mode is enabled (in microseconds). Higher values will result in lower CPU/GPU usage, which can improve battery life on laptops (in addition to improving the running project's performance if the editor has to redraw continuously). However, higher values will result in a less responsive editor. The default value is set to limit the editor to 20 FPS when the editor window is unfocused. See also [member interface/editor/low_processor_mode_sleep_usec].
var interface/editor/unfocused_low_processor_mode_sleep_usec: float;

#desc If [code]true[/code], editor main menu is using embedded [MenuBar] instead of system global menu.
#desc Specific to the macOS platform.
var interface/editor/use_embedded_menu: bool;

#desc The number of [Array] or [Dictionary] items to display on each "page" in the inspector. Higher values allow viewing more values per page, but take more time to load. This increased load time is noticeable when selecting nodes that have array or dictionary properties in the editor.
var interface/inspector/max_array_dictionary_items_per_page: int;

#desc If [code]true[/code], display OpenType features marked as [code]hidden[/code] by the font file in the [Font] editor.
var interface/inspector/show_low_level_opentype_features: bool;

#desc Controls when the Close (X) button is displayed on scene tabs at the top of the editor.
var interface/scene_tabs/display_close_button: int;

#desc The maximum width of each scene tab at the top editor (in pixels).
var interface/scene_tabs/maximum_width: int;

#desc If [code]true[/code], show a button next to each scene tab that opens the scene's "dominant" script when clicked. The "dominant" script is the one that is at the highest level in the scene's hierarchy.
var interface/scene_tabs/show_script_button: bool;

#desc If [code]true[/code], display an automatically-generated thumbnail when hovering scene tabs with the mouse. Scene thumbnails are generated when saving the scene.
var interface/scene_tabs/show_thumbnail_on_hover: bool;

#desc The color to use for "highlighted" user interface elements in the editor (pressed and hovered items).
var interface/theme/accent_color: Color;

#desc The spacing to add for buttons and list items in the editor (in pixels). Increasing this value is useful to improve usability on touch screens, at the cost of reducing the amount of usable screen real estate.
var interface/theme/additional_spacing: float;

#desc The base color to use for user interface elements in the editor. Secondary colors (such as darker/lighter variants) are derived from this color.
var interface/theme/base_color: Color;

#desc The border size to use for interface elements (in pixels).
var interface/theme/border_size: int;

#desc The contrast factor to use when deriving the editor theme's base color (see [member interface/theme/base_color]). When using a positive values, the derived colors will be [i]darker[/i] than the base color. This contrast factor can be set to a negative value, which will make the derived colors [i]brighter[/i] than the base color. Negative contrast rates often look better for light themes.
var interface/theme/contrast: float;

#desc The corner radius to use for interface elements (in pixels). [code]0[/code] is square.
var interface/theme/corner_radius: int;

#desc The custom theme resource to use for the editor. Must be a Godot theme resource in [code].tres[/code] or [code].res[/code] format.
var interface/theme/custom_theme: String;

#desc The icon and font color scheme to use in the editor.
#desc - [b]Auto[/b] determines the color scheme to use automatically based on [member interface/theme/base_color].
#desc - [b]Dark[/b] makes fonts and icons light (suitable for dark themes).
#desc - [b]Light[/b] makes fonts and icons dark (suitable for light themes). Icon colors are automatically converted by the editor following [url=https://github.com/godotengine/godot/blob/master/editor/editor_themes.cpp#L135]this set of rules[/url].
var interface/theme/icon_and_font_color: int;

#desc The saturation to use for editor icons. Higher values result in more vibrant colors.
#desc [b]Note:[/b] The default editor icon saturation was increased by 30% in Godot 4.0 and later. To get Godot 3.x's icon saturation back, set [member interface/theme/icon_saturation] to [code]0.77[/code].
var interface/theme/icon_saturation: float;

#desc The editor theme preset to use.
var interface/theme/preset: String;

#desc The opacity to use when drawing relationship lines in the editor's [Tree]-based GUIs (such as the Scene tree dock).
var interface/theme/relationship_line_opacity: float;

#desc The address to listen to when starting the remote debugger. This can be set to [code]0.0.0.0[/code] to allow external clients to connect to the remote debugger (instead of restricting the remote debugger to connections from [code]localhost[/code]).
var network/debug/remote_host: String;

#desc The port to listen to when starting the remote debugger. Godot will try to use port numbers above the configured number if the configured number is already taken by another application.
var network/debug/remote_port: int;

#desc The host to use to contact the HTTP and HTTPS proxy in the editor (for the asset library and export template downloads). See also [member network/http_proxy/port].
#desc [b]Note:[/b] Godot currently doesn't automatically use system proxy settings, so you have to enter them manually here if needed.
var network/http_proxy/host: String;

#desc The port number to use to contact the HTTP and HTTPS proxy in the editor (for the asset library and export template downloads). See also [member network/http_proxy/host].
#desc [b]Note:[/b] Godot currently doesn't automatically use system proxy settings, so you have to enter them manually here if needed.
var network/http_proxy/port: int;

#desc The TLS certificate bundle to use for HTTP requests made within the editor (e.g. from the AssetLib tab). If left empty, the [url=https://github.com/godotengine/godot/blob/master/thirdparty/certs/ca-certificates.crt]included Mozilla certificate bundle[/url] will be used.
var network/tls/editor_tls_certificates: String;

#desc The sorting order to use in the project manager. When changing the sorting order in the project manager, this setting is set permanently in the editor settings.
var project_manager/sorting_order: int;

#desc If [code]true[/code], saves all scenes and scripts automatically before running the project. Setting this to [code]false[/code] prevents the editor from saving if there are no changes which can speed up the project startup slightly, but it makes it possible to run a project that has unsaved changes. (Unsaved changes will not be visible in the running project.)
var run/auto_save/save_before_running: bool;

#desc If [code]true[/code], the editor will clear the Output panel when running the project.
var run/output/always_clear_output_on_play: bool;

#desc If [code]true[/code], the editor will collapse the Output panel when stopping the project.
var run/output/always_close_output_on_stop: bool;

#desc If [code]true[/code], the editor will expand the Output panel when running the project.
var run/output/always_open_output_on_play: bool;

#desc The size of the font in the [b]Output[/b] panel at the bottom of the editor. This setting does not impact the font size of the script editor (see [member interface/editor/code_font_size]).
var run/output/font_size: int;

#desc The window mode to use to display the project when starting the project from the editor.
var run/window_placement/rect: int;

#desc The custom position to use when starting the project from the editor (in pixels from the top-left corner). Only effective if [member run/window_placement/rect] is set to [b]Custom Position[/b].
var run/window_placement/rect_custom_position: Vector2;

#desc The monitor to display the project on when starting the project from the editor.
var run/window_placement/screen: int;

#desc If [code]true[/code], makes the caret blink according to [member text_editor/appearance/caret/caret_blink_interval]. Disabling this setting can improve battery life on laptops if you spend long amounts of time in the script editor, since it will reduce the frequency at which the editor needs to be redrawn.
var text_editor/appearance/caret/caret_blink: bool;

#desc The interval at which to blink the caret (in seconds). See also [member text_editor/appearance/caret/caret_blink].
var text_editor/appearance/caret/caret_blink_interval: float;

#desc If [code]true[/code], highlights all occurrences of the currently selected text in the script editor. See also [member text_editor/theme/highlighting/word_highlighted_color].
var text_editor/appearance/caret/highlight_all_occurrences: bool;

#desc If [code]true[/code], colors the background of the line the caret is currently on with [member text_editor/theme/highlighting/current_line_color].
var text_editor/appearance/caret/highlight_current_line: bool;

#desc The shape of the caret to use in the script editor. [b]Line[/b] displays a vertical line to the left of the current character, whereas [b]Block[/b] displays a outline over the current character.
var text_editor/appearance/caret/type: int;

#desc The column at which to display a subtle line as a line length guideline for scripts. This should generally be greater than [member text_editor/appearance/guidelines/line_length_guideline_soft_column].
var text_editor/appearance/guidelines/line_length_guideline_hard_column: int;

#desc The column at which to display a [i]very[/i] subtle line as a line length guideline for scripts. This should generally be lower than [member text_editor/appearance/guidelines/line_length_guideline_hard_column].
var text_editor/appearance/guidelines/line_length_guideline_soft_column: int;

#desc If [code]true[/code], displays line length guidelines to help you keep line lengths in check. See also [member text_editor/appearance/guidelines/line_length_guideline_soft_column] and [member text_editor/appearance/guidelines/line_length_guideline_hard_column].
var text_editor/appearance/guidelines/show_line_length_guidelines: bool;

#desc If [code]true[/code], highlights type-safe lines by displaying their line number color with [member text_editor/theme/highlighting/safe_line_number_color] instead of [member text_editor/theme/highlighting/line_number_color]. Type-safe lines are lines of code where the type of all variables is known at compile-time. These type-safe lines will run faster in Godot 4.0 and later thanks to typed instructions.
var text_editor/appearance/gutters/highlight_type_safe_lines: bool;

#desc If [code]true[/code], displays line numbers with zero padding (e.g. [code]007[/code] instead of [code]7[/code]).
var text_editor/appearance/gutters/line_numbers_zero_padded: bool;

#desc If [code]true[/code], displays a gutter at the left containing icons for bookmarks.
var text_editor/appearance/gutters/show_bookmark_gutter: bool;

#desc If [code]true[/code], displays a gutter at the left containing icons for methods with signal connections.
var text_editor/appearance/gutters/show_info_gutter: bool;

#desc If [code]true[/code], displays line numbers in the gutter at the left.
var text_editor/appearance/gutters/show_line_numbers: bool;

#desc If [code]true[/code], displays the folding arrows next to indented code sections and allows code folding. If [code]false[/code], hides the folding arrows next to indented code sections and disallows code folding.
var text_editor/appearance/lines/code_folding: bool;

#desc If [code]true[/code], wraps long lines over multiple lines to avoid horizontal scrolling. This is a display-only feature; it does not actually insert line breaks in your scripts.
var text_editor/appearance/lines/word_wrap: int;

#desc The width of the minimap in the script editor (in pixels).
var text_editor/appearance/minimap/minimap_width: int;

#desc If [code]true[/code], draws an overview of the script near the scroll bar. The minimap can be left-clicked to scroll directly to a location in an "absolute" manner.
var text_editor/appearance/minimap/show_minimap: bool;

#desc If [code]true[/code], draws space characters as centered points.
var text_editor/appearance/whitespace/draw_spaces: bool;

#desc If [code]true[/code], draws tab characters as chevrons.
var text_editor/appearance/whitespace/draw_tabs: bool;

#desc The space to add between lines (in pixels). Greater line spacing can help improve readability at the cost of displaying fewer lines on screen.
var text_editor/appearance/whitespace/line_spacing: int;

#desc If [code]true[/code], automatically reloads scripts in the editor when they have been modified and saved by external editors.
var text_editor/behavior/files/auto_reload_scripts_on_external_change: bool;

#desc If set to a value greater than [code]0[/code], automatically saves the current script following the specified interval (in seconds). This can be used to prevent data loss if the editor crashes.
var text_editor/behavior/files/autosave_interval_secs: int;

#desc If [code]true[/code], converts indentation to match the script editor's indentation settings when saving a script. See also [member text_editor/behavior/indent/type].
var text_editor/behavior/files/convert_indent_on_save: bool;

#desc If [code]true[/code], reopens scripts that were opened in the last session when the editor is reopened on a given project.
var text_editor/behavior/files/restore_scripts_on_load: bool;

#desc If [code]true[/code], trims trailing whitespace when saving a script. Trailing whitespace refers to tab and space characters placed at the end of lines. Since these serve no practical purpose, they can and should be removed to make version control diffs less noisy.
var text_editor/behavior/files/trim_trailing_whitespace_on_save: bool;

#desc If [code]true[/code], automatically indents code when pressing the [kbd]Enter[/kbd] key based on blocks above the new line.
var text_editor/behavior/indent/auto_indent: bool;

#desc When using tab indentation, determines the length of each tab. When using space indentation, determines how many spaces are inserted when pressing [kbd]Tab[/kbd] and when automatic indentation is performed.
var text_editor/behavior/indent/size: int;

#desc The indentation style to use (tabs or spaces).
#desc [b]Note:[/b] The [url=https://docs.godotengine.org/en/latest/getting_started/scripting/gdscript/gdscript_styleguide.html]GDScript style guide[/url] recommends using tabs for indentation. It is advised to change this setting only if you need to work on a project that currently uses spaces for indentation.
var text_editor/behavior/indent/type: int;

#desc If [code]true[/code], allows drag-and-dropping text in the script editor to move text. Disable this if you find yourself accidentally drag-and-dropping text in the script editor.
var text_editor/behavior/navigation/drag_and_drop_selection: bool;

#desc If [code]true[/code], the caret will be moved when right-clicking somewhere in the script editor (like when left-clicking or middle-clicking). If [code]false[/code], the caret will only be moved when left-clicking or middle-clicking somewhere.
var text_editor/behavior/navigation/move_caret_on_right_click: bool;

#desc If [code]true[/code], allows scrolling past the end of the file.
var text_editor/behavior/navigation/scroll_past_end_of_file: bool;

#desc If [code]true[/code], allows scrolling in sub-line intervals and enables a smooth scrolling animation when using the mouse wheel to scroll.
#desc [b]Note:[/b] [member text_editor/behavior/navigation/smooth_scrolling] currently behaves poorly in projects where [member ProjectSettings.physics/common/physics_ticks_per_second] has been increased significantly from its default value ([code]60[/code]). In this case, it is recommended to disable this setting.
var text_editor/behavior/navigation/smooth_scrolling: bool;

#desc If [code]true[/code], prevents automatically switching between the Script and 2D/3D screens when selecting a node in the Scene tree dock.
var text_editor/behavior/navigation/stay_in_script_editor_on_node_selected: bool;

#desc The number of pixels to scroll with every mouse wheel increment. Higher values make the script scroll by faster when using the mouse wheel.
#desc [b]Note:[/b] You can hold down [kbd]Alt[/kbd] while using the mouse wheel to temporarily scroll 5 times faster.
var text_editor/behavior/navigation/v_scroll_speed: int;

#desc If [code]true[/code], adds static typing hints such as [code]-> void[/code] and [code]: int[/code] when performing method definition autocompletion.
var text_editor/completion/add_type_hints: bool;

#desc If [code]true[/code], automatically completes braces when making use of code completion.
var text_editor/completion/auto_brace_complete: bool;

#desc The delay in seconds after which autocompletion suggestions should be displayed when the user stops typing.
var text_editor/completion/code_complete_delay: float;

#desc If [code]true[/code], provides autocompletion suggestions for file paths in methods such as [code]load()[/code] and [code]preload()[/code].
var text_editor/completion/complete_file_paths: bool;

#desc The delay in seconds after which the script editor should check for errors when the user stops typing.
var text_editor/completion/idle_parse_delay: float;

#desc If [code]true[/code], the code completion tooltip will appear below the current line unless there is no space on screen below the current line. If [code]false[/code], the code completion tooltip will appear above the current line.
var text_editor/completion/put_callhint_tooltip_below_current_line: bool;

#desc If [code]true[/code], performs string autocompletion with single quotes. If [code]false[/code], performs string autocompletion with double quotes (which matches the [url=https://docs.godotengine.org/en/latest/tutorials/scripting/gdscript/gdscript_styleguide.html]GDScript style guide[/url]).
var text_editor/completion/use_single_quotes: bool;

#desc Controls which multi-line code blocks should be displayed in the editor help. This setting does not affect single-line code literals in the editor help.
var text_editor/help/class_reference_examples: int;

#desc The font size to use for the editor help (built-in class reference).
var text_editor/help/help_font_size: int;

#desc The font size to use for code samples in the editor help (built-in class reference).
var text_editor/help/help_source_font_size: int;

#desc The font size to use for headings in the editor help (built-in class reference).
var text_editor/help/help_title_font_size: int;

#desc If [code]true[/code], displays a table of contents at the left of the editor help (at the location where the members overview would appear when editing a script).
var text_editor/help/show_help_index: bool;

#desc If [code]true[/code], displays an overview of the current script's member variables and functions at the left of the script editor. See also [member text_editor/script_list/sort_members_outline_alphabetically].
var text_editor/script_list/show_members_overview: bool;

#desc If [code]true[/code], sorts the members outline (located at the left of the script editor) using alphabetical order. If [code]false[/code], sorts the members outline depending on the order in which members are found in the script.
#desc [b]Note:[/b] Only effective if [member text_editor/script_list/show_members_overview] is [code]true[/code].
var text_editor/script_list/sort_members_outline_alphabetically: bool;

#desc The syntax theme to use in the script editor.
#desc You can save your own syntax theme from your current settings by using [b]File > Theme > Save As...[/b] at the top of the script editor. The syntax theme will then be available locally in the list of color themes.
#desc You can find additional syntax themes to install in the [url=https://github.com/godotengine/godot-syntax-themes]godot-syntax-themes[/url] repository.
var text_editor/theme/color_theme: String;

#desc The script editor's background color. If set to a translucent color, the editor theme's base color will be visible behind.
var text_editor/theme/highlighting/background_color: Color;

#desc The script editor's base type color (used for types like [Vector2], [Vector3], ...).
var text_editor/theme/highlighting/base_type_color: Color;

#desc The script editor's bookmark icon color (displayed in the gutter).
var text_editor/theme/highlighting/bookmark_color: Color;

#desc The script editor's brace mismatch color. Used when the caret is currently on a mismatched brace, parenthesis or bracket character.
var text_editor/theme/highlighting/brace_mismatch_color: Color;

#desc The script editor's breakpoint icon color (displayed in the gutter).
var text_editor/theme/highlighting/breakpoint_color: Color;

#desc The script editor's caret background color.
#desc [b]Note:[/b] This setting has no effect as it's currently unused.
var text_editor/theme/highlighting/caret_background_color: Color;

#desc The script editor's caret color.
var text_editor/theme/highlighting/caret_color: Color;

#desc The script editor's color for the code folding icon (displayed in the gutter).
var text_editor/theme/highlighting/code_folding_color: Color;

#desc The script editor's comment color.
#desc [b]Note:[/b] In GDScript, unlike Python, multiline strings are not considered to be comments, and will use the string highlighting color instead.
var text_editor/theme/highlighting/comment_color: Color;

#desc The script editor's autocompletion box background color.
var text_editor/theme/highlighting/completion_background_color: Color;

#desc The script editor's autocompletion box background color to highlight existing characters in the completion results. This should be a translucent color so that [member text_editor/theme/highlighting/completion_selected_color] can be seen behind.
var text_editor/theme/highlighting/completion_existing_color: Color;

#desc The script editor's autocompletion box text color.
var text_editor/theme/highlighting/completion_font_color: Color;

#desc The script editor's autocompletion box scroll bar color.
var text_editor/theme/highlighting/completion_scroll_color: Color;

#desc The script editor's autocompletion box scroll bar color when hovered or pressed with the mouse.
var text_editor/theme/highlighting/completion_scroll_hovered_color: Color;

#desc The script editor's autocompletion box background color for the currently selected line.
var text_editor/theme/highlighting/completion_selected_color: Color;

#desc The script editor's control flow keyword color (used for keywords like [code]if[/code], [code]for[/code], [code]return[/code], ...).
var text_editor/theme/highlighting/control_flow_keyword_color: Color;

#desc The script editor's background color for the line the caret is currently on. This should be set to a translucent color so that it can display on top of other line color modifiers such as [member text_editor/theme/highlighting/mark_color].
var text_editor/theme/highlighting/current_line_color: Color;

#desc The script editor's engine type color ([Vector2], [Vector3], [Color], ...).
var text_editor/theme/highlighting/engine_type_color: Color;

#desc The script editor's color for the debugger's executing line icon (displayed in the gutter).
var text_editor/theme/highlighting/executing_line_color: Color;

#desc The script editor's function call color.
#desc [b]Note:[/b] When using the GDScript syntax highlighter, this is replaced by the function definition color configured in the syntax theme for function definitions (e.g. [code]func _ready():[/code]).
var text_editor/theme/highlighting/function_color: Color;

#desc The script editor's non-control flow keyword color (used for keywords like [code]var[/code], [code]func[/code], some built-in methods, ...).
var text_editor/theme/highlighting/keyword_color: Color;

#desc The script editor's color for the line length guideline. The "hard" line length guideline will be drawn with this color, whereas the "soft" line length guideline will be drawn with an opacity twice as low.
var text_editor/theme/highlighting/line_length_guideline_color: Color;

#desc The script editor's color for line numbers. See also [member text_editor/theme/highlighting/safe_line_number_color].
var text_editor/theme/highlighting/line_number_color: Color;

#desc The script editor's background color for lines with errors. This should be set to a translucent color so that it can display on top of other line color modifiers such as [member text_editor/theme/highlighting/current_line_color].
var text_editor/theme/highlighting/mark_color: Color;

#desc The script editor's color for member variables on objects (e.g. [code]self.some_property[/code]).
#desc [b]Note:[/b] This color is not used for local variable declaration and access.
var text_editor/theme/highlighting/member_variable_color: Color;

#desc The script editor's color for numbers (integer and floating-point).
var text_editor/theme/highlighting/number_color: Color;

#desc The script editor's color for type-safe line numbers. See also [member text_editor/theme/highlighting/line_number_color].
#desc [b]Note:[/b] Only displayed if [member text_editor/appearance/gutters/highlight_type_safe_lines] is [code]true[/code].
var text_editor/theme/highlighting/safe_line_number_color: Color;

#desc The script editor's color for the border of search results. This border helps bring further attention to the search result. Set this color's opacity to 0 to disable the border.
var text_editor/theme/highlighting/search_result_border_color: Color;

#desc The script editor's background color for search results.
var text_editor/theme/highlighting/search_result_color: Color;

#desc The script editor's background color for the currently selected text.
var text_editor/theme/highlighting/selection_color: Color;

#desc The script editor's color for strings (single-line and multi-line).
var text_editor/theme/highlighting/string_color: Color;

#desc The script editor's color for operators ([code]( ) [ ] { } + - * /[/code], ...).
var text_editor/theme/highlighting/symbol_color: Color;

#desc The script editor's color for text not highlighted by any syntax highlighting rule.
var text_editor/theme/highlighting/text_color: Color;

#desc The script editor's background color for text. This should be set to a translucent color so that it can display on top of other line color modifiers such as [member text_editor/theme/highlighting/current_line_color].
var text_editor/theme/highlighting/text_selected_color: Color;

#desc The script editor's color for user-defined types (using [code]@class_name[/code]).
var text_editor/theme/highlighting/user_type_color: Color;

#desc The script editor's color for words highlighted by selecting them. Only visible if [member text_editor/appearance/caret/highlight_all_occurrences] is [code]true[/code].
var text_editor/theme/highlighting/word_highlighted_color: Color;



#desc Adds a custom property info to a property. The dictionary must contain:
#desc - [code]name[/code]: [String] (the name of the property)
#desc - [code]type[/code]: [int] (see [enum Variant.Type])
#desc - optionally [code]hint[/code]: [int] (see [enum PropertyHint]) and [code]hint_string[/code]: [String]
#desc [b]Example:[/b]
#desc [codeblocks]
#desc [gdscript]
#desc var settings = EditorInterface.get_editor_settings()
#desc settings.set("category/property_name", 0)
#desc 
#desc var property_info = {
#desc "name": "category/property_name",
#desc "type": TYPE_INT,
#desc "hint": PROPERTY_HINT_ENUM,
#desc "hint_string": "one,two,three"
#desc }
#desc 
#desc settings.add_property_info(property_info)
#desc [/gdscript]
#desc [csharp]
#desc var settings = GetEditorInterface().GetEditorSettings();
#desc settings.Set("category/property_name", 0);
#desc 
#desc var propertyInfo = new Godot.Collections.Dictionary
#desc {
#desc {"name", "category/propertyName"},
#desc {"type", Variant.Type.Int},
#desc {"hint", PropertyHint.Enum},
#desc {"hint_string", "one,two,three"}
#desc };
#desc 
#desc settings.AddPropertyInfo(propertyInfo);
#desc [/csharp]
#desc [/codeblocks]
func add_property_info(info: Dictionary) -> void:
	pass;

#desc Checks if any settings with the prefix [param setting_prefix] exist in the set of changed settings. See also [method get_changed_settings].
func check_changed_settings_in_group(setting_prefix: String) -> bool:
	pass;

#desc Erases the setting whose name is specified by [param property].
func erase(property: String) -> void:
	pass;

#desc Gets an array of the settings which have been changed since the last save. Note that internally [code]changed_settings[/code] is cleared after a successful save, so generally the most appropriate place to use this method is when processing [constant NOTIFICATION_EDITOR_SETTINGS_CHANGED]
func get_changed_settings() -> PackedStringArray:
	pass;

#desc Returns the list of favorite files and directories for this project.
func get_favorites() -> PackedStringArray:
	pass;

#desc Returns project-specific metadata for the [param section] and [param key] specified. If the metadata doesn't exist, [param default] will be returned instead. See also [method set_project_metadata].
func get_project_metadata(section: String, key: String, default: Variant) -> Variant:
	pass;

#desc Returns the list of recently visited folders in the file dialog for this project.
func get_recent_dirs() -> PackedStringArray:
	pass;

#desc Returns the value of the setting specified by [param name]. This is equivalent to using [method Object.get] on the EditorSettings instance.
func get_setting(name: String) -> Variant:
	pass;

#desc Returns [code]true[/code] if the setting specified by [param name] exists, [code]false[/code] otherwise.
func has_setting(name: String) -> bool:
	pass;

#desc Marks the passed editor setting as being changed, see [method get_changed_settings]. Only settings which exist (see [method has_setting]) will be accepted.
func mark_setting_changed(setting: String) -> void:
	pass;

#desc Overrides the built-in editor action [param name] with the input actions defined in [param actions_list].
func set_builtin_action_override(name: String, actions_list: InputEvent[]) -> void:
	pass;

#desc Sets the list of favorite files and directories for this project.
func set_favorites(dirs: PackedStringArray) -> void:
	pass;

#desc Sets the initial value of the setting specified by [param name] to [param value]. This is used to provide a value for the Revert button in the Editor Settings. If [param update_current] is true, the current value of the setting will be set to [param value] as well.
func set_initial_value(name: StringName, value: Variant, update_current: bool) -> void:
	pass;

#desc Sets project-specific metadata with the [param section], [param key] and [param data] specified. This metadata is stored outside the project folder and therefore won't be checked into version control. See also [method get_project_metadata].
func set_project_metadata(section: String, key: String, data: Variant) -> void:
	pass;

#desc Sets the list of recently visited folders in the file dialog for this project.
func set_recent_dirs(dirs: PackedStringArray) -> void:
	pass;

#desc Sets the [param value] of the setting specified by [param name]. This is equivalent to using [method Object.set] on the EditorSettings instance.
func set_setting(name: String, value: Variant) -> void:
	pass;


