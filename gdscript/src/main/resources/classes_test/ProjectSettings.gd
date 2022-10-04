#brief Contains global variables accessible from everywhere.
#desc Contains global variables accessible from everywhere. Use [method get_setting], [method set_setting] or [method has_setting] to access them. Variables stored in [code]project.godot[/code] are also loaded into ProjectSettings, making this object very useful for reading custom game configuration options.
#desc When naming a Project Settings property, use the full path to the setting including the category. For example, [code]"application/config/name"[/code] for the project name. Category and property names can be viewed in the Project Settings dialog.
#desc [b]Feature tags:[/b] Project settings can be overridden for specific platforms and configurations (debug, release, ...) using [url=$DOCS_URL/tutorials/export/feature_tags.html]feature tags[/url].
#desc [b]Overriding:[/b] Any project setting can be overridden by creating a file named [code]override.cfg[/code] in the project's root directory. This can also be used in exported projects by placing this file in the same directory as the project binary. Overriding will still take the base project settings' [url=$DOCS_URL/tutorials/export/feature_tags.html]feature tags[/url] in account. Therefore, make sure to [i]also[/i] override the setting with the desired feature tags if you want them to override base project settings on all platforms and configurations.
class_name ProjectSettings


#desc Background color for the boot splash.
var application/boot_splash/bg_color: Color;

#desc If [code]true[/code], scale the boot splash image to the full window size (preserving the aspect ratio) when the engine starts. If [code]false[/code], the engine will leave it at the default pixel size.
var application/boot_splash/fullsize: bool;

#desc Path to an image used as the boot splash. If left empty, the default Godot Engine splash will be displayed instead.
#desc [b]Note:[/b] Only effective if [member application/boot_splash/show_image] is [code]true[/code].
var application/boot_splash/image: String;

#desc Minimum boot splash display time (in milliseconds). It is not recommended to set too high values for this setting.
var application/boot_splash/minimum_display_time: int;

#desc If [code]true[/code], displays the image specified in [member application/boot_splash/image] when the engine starts. If [code]false[/code], only displays the plain color specified in [member application/boot_splash/bg_color].
var application/boot_splash/show_image: bool;

#desc If [code]true[/code], applies linear filtering when scaling the image (recommended for high-resolution artwork). If [code]false[/code], uses nearest-neighbor interpolation (recommended for pixel art).
var application/boot_splash/use_filter: bool;

#desc This user directory is used for storing persistent data ([code]user://[/code] filesystem). If left empty, [code]user://[/code] resolves to a project-specific folder in Godot's own configuration folder (see [method OS.get_user_data_dir]). If a custom directory name is defined, this name will be used instead and appended to the system-specific user data directory (same parent folder as the Godot configuration folder documented in [method OS.get_user_data_dir]).
#desc The [member application/config/use_custom_user_dir] setting must be enabled for this to take effect.
var application/config/custom_user_dir_name: String;

#desc The project's description, displayed as a tooltip in the Project Manager when hovering the project.
var application/config/description: String;

#desc List of internal features associated with the project, like [code]Double Precision[/code] or [code]C#[/code]. Not to be confused with feature tags.
var application/config/features: PackedStringArray;

#desc Icon used for the project, set when project loads. Exporters will also use this icon when possible.
var application/config/icon: String;

#desc Icon set in [code].icns[/code] format used on macOS to set the game's icon. This is done automatically on start by calling [method DisplayServer.set_native_icon].
var application/config/macos_native_icon: String;

#desc The project's name. It is used both by the Project Manager and by exporters. The project name can be translated by translating its value in localization files. The window title will be set to match the project name automatically on startup.
#desc [b]Note:[/b] Changing this value will also change the user data folder's path if [member application/config/use_custom_user_dir] is [code]false[/code]. After renaming the project, you will no longer be able to access existing data in [code]user://[/code] unless you rename the old folder to match the new project name. See [url=$DOCS_URL/tutorials/io/data_paths.html]Data paths[/url] in the documentation for more information.
var application/config/name: String;

#desc Translations of the project's name. This setting is used by OS tools to translate application name on Android, iOS and macOS.
var application/config/name_localized: Dictionary;

#desc Specifies a file to override project settings. For example: [code]user://custom_settings.cfg[/code]. See "Overriding" in the [ProjectSettings] class description at the top for more information.
#desc [b]Note:[/b] Regardless of this setting's value, [code]res://override.cfg[/code] will still be read to override the project settings.
var application/config/project_settings_override: String;

#desc If [code]true[/code], the project will save user data to its own user directory (see [member application/config/custom_user_dir_name]). This setting is only effective on desktop platforms. A name must be set in the [member application/config/custom_user_dir_name] setting for this to take effect. If [code]false[/code], the project will save user data to [code](OS user data directory)/Godot/app_userdata/(project name)[/code].
var application/config/use_custom_user_dir: bool;

#desc If [code]true[/code], the project will use a hidden directory ([code].godot[/code]) for storing project-specific data (metadata, shader cache, etc.).
#desc If [code]false[/code], a non-hidden directory ([code]godot[/code]) will be used instead.
#desc [b]Note:[/b] Restart the application after changing this setting.
#desc [b]Note:[/b] Changing this value can help on platforms or with third-party tools where hidden directory patterns are disallowed. Only modify this setting if you know that your environment requires it, as changing the default can impact compatibility with some external tools or plugins which expect the default [code].godot[/code] folder.
var application/config/use_hidden_project_data_directory: bool;

#desc Icon set in [code].ico[/code] format used on Windows to set the game's icon. This is done automatically on start by calling [method DisplayServer.set_native_icon].
var application/config/windows_native_icon: String;

#desc If [code]true[/code], disables printing to standard error. If [code]true[/code], this also hides error and warning messages printed by [method @GlobalScope.push_error] and [method @GlobalScope.push_warning]. See also [member application/run/disable_stdout].
#desc Changes to this setting will only be applied upon restarting the application.
var application/run/disable_stderr: bool;

#desc If [code]true[/code], disables printing to standard output. This is equivalent to starting the editor or project with the [code]--quiet[/code] command line argument. See also [member application/run/disable_stderr].
#desc Changes to this setting will only be applied upon restarting the application.
var application/run/disable_stdout: bool;

#desc If [code]true[/code], flushes the standard output stream every time a line is printed. This affects both terminal logging and file logging.
#desc When running a project, this setting must be enabled if you want logs to be collected by service managers such as systemd/journalctl. This setting is disabled by default on release builds, since flushing on every printed line will negatively affect performance if lots of lines are printed in a rapid succession. Also, if this setting is enabled, logged files will still be written successfully if the application crashes or is otherwise killed by the user (without being closed "normally").
#desc [b]Note:[/b] Regardless of this setting, the standard error stream ([code]stderr[/code]) is always flushed when a line is printed to it.
#desc Changes to this setting will only be applied upon restarting the application.
var application/run/flush_stdout_on_print: bool;

#desc Debug build override for [member application/run/flush_stdout_on_print], as performance is less important during debugging.
#desc Changes to this setting will only be applied upon restarting the application.
var application/run/flush_stdout_on_print.debug: bool;

#desc Forces a delay between frames in the main loop (in milliseconds). This may be useful if you plan to disable vertical synchronization.
var application/run/frame_delay_msec: int;

#desc If [code]true[/code], enables low-processor usage mode. This setting only works on desktop platforms. The screen is not redrawn if nothing changes visually. This is meant for writing applications and editors, but is pretty useless (and can hurt performance) in most games.
var application/run/low_processor_mode: bool;

#desc Amount of sleeping between frames when the low-processor usage mode is enabled (in microseconds). Higher values will result in lower CPU usage.
var application/run/low_processor_mode_sleep_usec: int;

#desc Path to the main scene file that will be loaded when the project runs.
var application/run/main_scene: String;

#desc Maximum number of frames per second allowed. A value of [code]0[/code] means "no limit". The actual number of frames per second may still be below this value if the CPU or GPU cannot keep up with the project logic and rendering.
#desc Limiting the FPS can be useful to reduce system power consumption, which reduces heat and noise emissions (and improves battery life on mobile devices).
#desc If [member display/window/vsync/vsync_mode] is set to [code]Enabled[/code] or [code]Adaptive[/code], it takes precedence and the forced FPS number cannot exceed the monitor's refresh rate.
#desc If [member display/window/vsync/vsync_mode] is [code]Enabled[/code], on monitors with variable refresh rate enabled (G-Sync/FreeSync), using a FPS limit a few frames lower than the monitor's refresh rate will [url=https://blurbusters.com/howto-low-lag-vsync-on/]reduce input lag while avoiding tearing[/url].
#desc If [member display/window/vsync/vsync_mode] is [code]Disabled[/code], limiting the FPS to a high value that can be consistently reached on the system can reduce input lag compared to an uncapped framerate. Since this works by ensuring the GPU load is lower than 100%, this latency reduction is only effective in GPU-bottlenecked scenarios, not CPU-bottlenecked scenarios.
#desc See also [member physics/common/physics_ticks_per_second].
#desc [b]Note:[/b] This property is only read when the project starts. To change the rendering FPS cap at runtime, set [member Engine.max_fps] instead.
var application/run/max_fps: int;

#desc Audio buses will disable automatically when sound goes below a given dB threshold for a given time. This saves CPU as effects assigned to that bus will no longer do any processing.
var audio/buses/channel_disable_threshold_db: float;

#desc Audio buses will disable automatically when sound goes below a given dB threshold for a given time. This saves CPU as effects assigned to that bus will no longer do any processing.
var audio/buses/channel_disable_time: float;

#desc Default [AudioBusLayout] resource file to use in the project, unless overridden by the scene.
var audio/buses/default_bus_layout: String;

#desc Specifies the audio driver to use. This setting is platform-dependent as each platform supports different audio drivers. If left empty, the default audio driver will be used.
#desc The [code]Dummy[/code] audio driver disables all audio playback and recording, which is useful for non-game applications as it reduces CPU usage. It also prevents the engine from appearing as an application playing audio in the OS' audio mixer.
#desc [b]Note:[/b] The driver in use can be overridden at runtime via the [code]--audio-driver[/code] command line argument.
var audio/driver/driver: String;

#desc If [code]true[/code], microphone input will be allowed. This requires appropriate permissions to be set when exporting to Android or iOS.
#desc [b]Note:[/b] If the operating system blocks access to audio input devices (due to the user's privacy settings), audio capture will only return silence. On Windows 10 and later, make sure that apps are allowed to access the microphone in the OS' privacy settings.
var audio/driver/enable_input: bool;

#desc The mixing rate used for audio (in Hz). In general, it's better to not touch this and leave it to the host operating system.
var audio/driver/mix_rate: int;

#desc Safer override for [member audio/driver/mix_rate] in the Web platform. Here [code]0[/code] means "let the browser choose" (since some browsers do not like forcing the mix rate).
var audio/driver/mix_rate.web: int;

#desc Specifies the preferred output latency in milliseconds for audio. Lower values will result in lower audio latency at the cost of increased CPU usage. Low values may result in audible cracking on slower hardware.
#desc Audio output latency may be constrained by the host operating system and audio hardware drivers. If the host can not provide the specified audio output latency then Godot will attempt to use the nearest latency allowed by the host. As such you should always use [method AudioServer.get_output_latency] to determine the actual audio output latency.
#desc [b]Note:[/b] This setting is ignored on all versions of Windows prior to Windows 10.
var audio/driver/output_latency: int;

#desc Safer override for [member audio/driver/output_latency] in the Web platform, to avoid audio issues especially on mobile devices.
var audio/driver/output_latency.web: int;

#desc The base strength of the panning effect for all AudioStreamPlayer2D nodes. The panning strength can be further scaled on each Node using [member AudioStreamPlayer2D.panning_strength].
var audio/general/2d_panning_strength: float;

#desc The base strength of the panning effect for all AudioStreamPlayer3D nodes. The panning strength can be further scaled on each Node using [member AudioStreamPlayer3D.panning_strength].
var audio/general/3d_panning_strength: float;

#desc Setting to hardcode audio delay when playing video. Best to leave this untouched unless you know what you are doing.
var audio/video/video_delay_compensation_ms: int;

#desc The default compression level for gzip. Affects compressed scenes and resources. Higher levels result in smaller files at the cost of compression speed. Decompression speed is mostly unaffected by the compression level. [code]-1[/code] uses the default gzip compression level, which is identical to [code]6[/code] but could change in the future due to underlying zlib updates.
var compression/formats/gzip/compression_level: int;

#desc The default compression level for Zlib. Affects compressed scenes and resources. Higher levels result in smaller files at the cost of compression speed. Decompression speed is mostly unaffected by the compression level. [code]-1[/code] uses the default gzip compression level, which is identical to [code]6[/code] but could change in the future due to underlying zlib updates.
var compression/formats/zlib/compression_level: int;

#desc The default compression level for Zstandard. Affects compressed scenes and resources. Higher levels result in smaller files at the cost of compression speed. Decompression speed is mostly unaffected by the compression level.
var compression/formats/zstd/compression_level: int;

#desc Enables [url=https://github.com/facebook/zstd/releases/tag/v1.3.2]long-distance matching[/url] in Zstandard.
var compression/formats/zstd/long_distance_matching: bool;

#desc Largest size limit (in power of 2) allowed when compressing using long-distance matching with Zstandard. Higher values can result in better compression, but will require more memory when compressing and decompressing.
var compression/formats/zstd/window_log_size: int;

#desc If [code]true[/code], logs all output to files.
var debug/file_logging/enable_file_logging: bool;

#desc Desktop override for [member debug/file_logging/enable_file_logging], as log files are not readily accessible on mobile/Web platforms.
var debug/file_logging/enable_file_logging.pc: bool;

#desc Path at which to store log files for the project. Using a path under [code]user://[/code] is recommended.
var debug/file_logging/log_path: String;

#desc Specifies the maximum number of log files allowed (used for rotation).
var debug/file_logging/max_log_files: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when an [code]assert[/code] call always evaluates to false.
var debug/gdscript/warnings/assert_always_false: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when an [code]assert[/code] call always evaluates to true.
var debug/gdscript/warnings/assert_always_true: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when a constant is used as a function.
var debug/gdscript/warnings/constant_used_as_function: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when deprecated keywords are used.
var debug/gdscript/warnings/deprecated_keyword: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when an empty file is parsed.
var debug/gdscript/warnings/empty_file: int;

#desc If [code]true[/code], enables specific GDScript warnings (see [code]debug/gdscript/warnings/*[/code] settings). If [code]false[/code], disables all GDScript warnings.
var debug/gdscript/warnings/enable: bool;

#desc If [code]true[/code], scripts in the [code]res://addons[/code] folder will not generate warnings.
var debug/gdscript/warnings/exclude_addons: bool;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when using a function as if it is a property.
var debug/gdscript/warnings/function_used_as_property: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when a ternary operator may emit values with incompatible types.
var debug/gdscript/warnings/incompatible_ternary: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when trying to assign an integer to a variable that expects an enum value.
var debug/gdscript/warnings/int_assigned_to_enum: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when dividing an integer by another integer (the decimal part will be discarded).
var debug/gdscript/warnings/integer_division: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when passing a floating-point value to a function that expects an integer (it will be converted and lose precision).
var debug/gdscript/warnings/narrowing_conversion: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when using a property as if it is a function.
var debug/gdscript/warnings/property_used_as_function: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when a function that is not a coroutine is called with await.
var debug/gdscript/warnings/redundant_await: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when calling a function without using its return value (by assigning it to a variable or using it as a function argument). Such return values are sometimes used to denote possible errors using the [enum Error] enum.
var debug/gdscript/warnings/return_value_discarded: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when defining a local or member variable, signal, or enum that would have the same name as a built-in function or global class name, thus shadowing it.
var debug/gdscript/warnings/shadowed_global_identifier: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when defining a local or member variable that would shadow a member variable that the class defines.
var debug/gdscript/warnings/shadowed_variable: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when defining a local or subclass member variable that would shadow a variable that is inherited from a parent class.
var debug/gdscript/warnings/shadowed_variable_base_class: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when calling an expression that has no effect on the surrounding code, such as writing [code]2 + 2[/code] as a statement.
var debug/gdscript/warnings/standalone_expression: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when calling a ternary expression that has no effect on the surrounding code, such as writing [code]42 if active else 0[/code] as a statement.
var debug/gdscript/warnings/standalone_ternary: int;

#desc If [code]true[/code], all warnings will be reported as if they are errors.
var debug/gdscript/warnings/treat_warnings_as_errors: bool;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when using a variable that wasn't previously assigned.
var debug/gdscript/warnings/unassigned_variable: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when assigning a variable using an assignment operator like [code]+=[/code] if the variable wasn't previously assigned.
var debug/gdscript/warnings/unassigned_variable_op_assign: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when unreachable code is detected (such as after a [code]return[/code] statement that will always be executed).
var debug/gdscript/warnings/unreachable_code: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when an unreachable [code]match[/code] pattern is detected.
var debug/gdscript/warnings/unreachable_pattern: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when using an expression whose type may not be compatible with the function parameter expected.
var debug/gdscript/warnings/unsafe_call_argument: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when performing an unsafe cast.
var debug/gdscript/warnings/unsafe_cast: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when calling a method whose presence is not guaranteed at compile-time in the class.
var debug/gdscript/warnings/unsafe_method_access: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when accessing a property whose presence is not guaranteed at compile-time in the class.
var debug/gdscript/warnings/unsafe_property_access: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when a local constant is never used.
var debug/gdscript/warnings/unused_local_constant: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when a function parameter is never used.
var debug/gdscript/warnings/unused_parameter: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when a private member variable is never used.
var debug/gdscript/warnings/unused_private_class_variable: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when a signal is declared but never emitted.
var debug/gdscript/warnings/unused_signal: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when a local variable is unused.
var debug/gdscript/warnings/unused_variable: int;

#desc When set to [code]warn[/code] or [code]error[/code], produces a warning or an error respectively when assigning the result of a function that returns [code]void[/code] to a variable.
var debug/gdscript/warnings/void_assignment: int;

#desc Message to be displayed before the backtrace when the engine crashes. By default, this message is only used in exported projects due to the editor-only override applied to this setting.
var debug/settings/crash_handler/message: String;

#desc Editor-only override for [member debug/settings/crash_handler/message]. Does not affect exported projects in debug or release mode.
var debug/settings/crash_handler/message.editor: String;

#desc Maximum call stack allowed for debugging GDScript.
var debug/settings/gdscript/max_call_stack: int;

#desc Maximum number of functions per frame allowed when profiling.
var debug/settings/profiler/max_functions: int;

#desc Print frames per second to standard output every second.
var debug/settings/stdout/print_fps: bool;

#desc Print GPU profile information to standard output every second. This includes how long each frame takes the GPU to render on average, broken down into different steps of the render pipeline, such as CanvasItems, shadows, glow, etc.
var debug/settings/stdout/print_gpu_profile: bool;

#desc Print more information to standard output when running. It displays information such as memory leaks, which scenes and resources are being loaded, etc. This can also be enabled using the [code]--verbose[/code] or [code]-v[/code] command line argument, even on an exported project. See also [method OS.is_stdout_verbose] and [method @GlobalScope.print_verbose].
var debug/settings/stdout/verbose_stdout: bool;

#desc Color of the contact points between collision shapes, visible when "Visible Collision Shapes" is enabled in the Debug menu.
var debug/shapes/collision/contact_color: Color;

#desc Sets whether 2D physics will display collision outlines in game when "Visible Collision Shapes" is enabled in the Debug menu.
var debug/shapes/collision/draw_2d_outlines: bool;

#desc Maximum number of contact points between collision shapes to display when "Visible Collision Shapes" is enabled in the Debug menu.
var debug/shapes/collision/max_contacts_displayed: int;

#desc Color of the collision shapes, visible when "Visible Collision Shapes" is enabled in the Debug menu.
var debug/shapes/collision/shape_color: Color;

#desc Color to display edge connections between navigation regions, visible when "Visible Navigation" is enabled in the Debug menu.
var debug/shapes/navigation/edge_connection_color: Color;

#desc If enabled, displays edge connections between navigation regions when "Visible Navigation" is enabled in the Debug menu.
var debug/shapes/navigation/enable_edge_connections: bool;

#desc If enabled, displays edge connections between navigation regions through geometry when "Visible Navigation" is enabled in the Debug menu.
var debug/shapes/navigation/enable_edge_connections_xray: bool;

#desc If enabled, displays navigation mesh polygon edges when "Visible Navigation" is enabled in the Debug menu.
var debug/shapes/navigation/enable_edge_lines: bool;

#desc If enabled, displays navigation mesh polygon edges through geometry when "Visible Navigation" is enabled in the Debug menu.
var debug/shapes/navigation/enable_edge_lines_xray: bool;

#desc If enabled, colorizes each navigation mesh polygon face with a random color when "Visible Navigation" is enabled in the Debug menu.
var debug/shapes/navigation/enable_geometry_face_random_color: bool;

#desc If enabled, displays navigation link connections when "Visible Navigation" is enabled in the Debug menu.
var debug/shapes/navigation/enable_link_connections: bool;

#desc If enabled, displays navigation link connections through geometry when "Visible Navigation" is enabled in the Debug menu.
var debug/shapes/navigation/enable_link_connections_xray: bool;

#desc Color to display enabled navigation mesh polygon edges, visible when "Visible Navigation" is enabled in the Debug menu.
var debug/shapes/navigation/geometry_edge_color: Color;

#desc Color to display disabled navigation mesh polygon edges, visible when "Visible Navigation" is enabled in the Debug menu.
var debug/shapes/navigation/geometry_edge_disabled_color: Color;

#desc Color to display enabled navigation mesh polygon faces, visible when "Visible Navigation" is enabled in the Debug menu.
var debug/shapes/navigation/geometry_face_color: Color;

#desc Color to display disabled navigation mesh polygon faces, visible when "Visible Navigation" is enabled in the Debug menu.
var debug/shapes/navigation/geometry_face_disabled_color: Color;

#desc Color to use to display navigation link connections, visible when "Visible Navigation" is enabled in the Debug menu.
var debug/shapes/navigation/link_connection_color: Color;

#desc Color to use to display disabled navigation link connections, visible when "Visible Navigation" is enabled in the Debug menu.
var debug/shapes/navigation/link_connection_disabled_color: Color;

#desc Color of the curve path geometry, visible when "Visible Paths" is enabled in the Debug menu.
var debug/shapes/paths/geometry_color: Color;

#desc Line width of the curve path geometry, visible when "Visible Paths" is enabled in the Debug menu.
var debug/shapes/paths/geometry_width: float;

#desc Custom image for the mouse cursor (limited to 256×256).
var display/mouse_cursor/custom_image: String;

#desc Hotspot for the custom mouse cursor image.
var display/mouse_cursor/custom_image_hotspot: Vector2;

#desc Position offset for tooltips, relative to the mouse cursor's hotspot.
var display/mouse_cursor/tooltip_position_offset: Vector2;

#desc If [code]true[/code], allows HiDPI display on Windows, macOS, Android, iOS and Web. If [code]false[/code], the platform's low-DPI fallback will be used on HiDPI displays, which causes the window to be displayed in a blurry or pixelated manner (and can cause various window management bugs). Therefore, it is recommended to make your project scale to [url=$DOCS_URL/tutorials/viewports/multiple_resolutions.html]multiple resolutions[/url] instead of disabling this setting.
#desc [b]Note:[/b] This setting has no effect on Linux as DPI-awareness fallbacks are not supported there.
var display/window/dpi/allow_hidpi: bool;

#desc If [code]true[/code], keeps the screen on (even in case of inactivity), so the screensaver does not take over. Works on desktop and mobile platforms.
var display/window/energy_saving/keep_screen_on: bool;

#desc Editor-only override for [member display/window/energy_saving/keep_screen_on]. Does not affect exported projects in debug or release mode.
var display/window/energy_saving/keep_screen_on.editor: bool;

#desc The default screen orientation to use on mobile devices. See [enum DisplayServer.ScreenOrientation] for possible values.
#desc [b]Note:[/b] When set to a portrait orientation, this project setting does not flip the project resolution's width and height automatically. Instead, you have to set [member display/window/size/viewport_width] and [member display/window/size/viewport_height] accordingly.
var display/window/handheld/orientation: int;

#desc If [code]true[/code], the home indicator is hidden automatically. This only affects iOS devices without a physical home button.
var display/window/ios/hide_home_indicator: bool;

#desc If [code]true[/code], allows per-pixel transparency for the window background. This affects performance, so leave it on [code]false[/code] unless you need it.
var display/window/per_pixel_transparency/allowed: bool;

#desc Forces the main window to be always on top.
#desc [b]Note:[/b] This setting is ignored on iOS, Android, and Web.
var display/window/size/always_on_top: bool;

#desc Forces the main window to be borderless.
#desc [b]Note:[/b] This setting is ignored on iOS, Android, and Web.
var display/window/size/borderless: bool;

#desc Main window content is expanded to the full size of the window. Unlike a borderless window, the frame is left intact and can be used to resize the window, and the title bar is transparent, but has minimize/maximize/close buttons.
#desc [b]Note:[/b] This setting is implemented only on macOS.
var display/window/size/extend_to_title: bool;

#desc Main window mode. See [enum DisplayServer.WindowMode] for possible values and how each mode behaves.
var display/window/size/mode: int;

#desc Main window can't be focused. No-focus window will ignore all input, except mouse clicks.
var display/window/size/no_focus: bool;

#desc Allows the window to be resizable by default.
#desc [b]Note:[/b] This setting is ignored on iOS.
var display/window/size/resizable: bool;

#desc Main window background can be transparent.
#desc [b]Note:[/b] To use transparent splash screen, set [member application/boot_splash/bg_color] to [code]Color(0, 0, 0, 0)[/code].
#desc [b]Note:[/b] This setting has no effect if [member display/window/per_pixel_transparency/allowed] is set to [code]false[/code].
var display/window/size/transparent: bool;

#desc Sets the game's main viewport height. On desktop platforms, this is also the initial window height, represented by an indigo-colored rectangle in the 2D editor. Stretch mode settings also use this as a reference when using the [code]canvas_items[/code] or [code]viewport[/code] stretch modes. See also [member display/window/size/viewport_width], [member display/window/size/window_width_override] and [member display/window/size/window_height_override].
var display/window/size/viewport_height: int;

#desc Sets the game's main viewport width. On desktop platforms, this is also the initial window width, represented by an indigo-colored rectangle in the 2D editor. Stretch mode settings also use this as a reference when using the [code]canvas_items[/code] or [code]viewport[/code] stretch modes. See also [member display/window/size/viewport_height], [member display/window/size/window_width_override] and [member display/window/size/window_height_override].
var display/window/size/viewport_width: int;

#desc On desktop platforms, overrides the game's initial window height. See also [member display/window/size/window_width_override], [member display/window/size/viewport_width] and [member display/window/size/viewport_height].
#desc [b]Note:[/b] By default, or when set to [code]0[/code], the initial window height is the [member display/window/size/viewport_height]. This setting is ignored on iOS, Android, and Web.
var display/window/size/window_height_override: int;

#desc On desktop platforms, overrides the game's initial window width. See also [member display/window/size/window_height_override], [member display/window/size/viewport_width] and [member display/window/size/viewport_height].
#desc [b]Note:[/b] By default, or when set to [code]0[/code], the initial window width is the viewport [member display/window/size/viewport_width]. This setting is ignored on iOS, Android, and Web.
var display/window/size/window_width_override: int;

#desc Sets the V-Sync mode for the main game window.
#desc See [enum DisplayServer.VSyncMode] for possible values and how they affect the behavior of your application.
#desc Depending on the platform and used renderer, the engine will fall back to [code]Enabled[/code], if the desired mode is not supported.
var display/window/vsync/vsync_mode: int;

#desc If [code]true[/code], requests V-Sync to be disabled when writing a movie (similar to setting [member display/window/vsync/vsync_mode] to [b]Disabled[/b]). This can speed up video writing if the hardware is fast enough to render, encode and save the video at a framerate higher than the monitor's refresh rate.
#desc [b]Note:[/b] [member editor/movie_writer/disable_vsync] has no effect if the operating system or graphics driver forces V-Sync with no way for applications to disable it.
var editor/movie_writer/disable_vsync: bool;

#desc The number of frames per second to record in the video when writing a movie. Simulation speed will adjust to always match the specified framerate, which means the engine will appear to run slower at higher [member editor/movie_writer/fps] values. Certain FPS values will require you to adjust [member editor/movie_writer/mix_rate] to prevent audio from desynchronizing over time.
#desc This can be specified manually on the command line using the [code]--fixed-fps <fps>[/code] command line argument.
var editor/movie_writer/fps: int;

#desc The audio mix rate to use in the recorded audio when writing a movie (in Hz). This can be different from [member audio/driver/mix_rate], but this value must be divisible by [member editor/movie_writer/fps] to prevent audio from desynchronizing over time.
var editor/movie_writer/mix_rate: int;

#desc The JPEG quality to use when writing a video to an AVI file, between [code]0.01[/code] and [code]1.0[/code] (inclusive). Higher [code]quality[/code] values result in better-looking output at the cost of larger file sizes. Recommended [code]quality[/code] values are between [code]0.75[/code] and [code]0.9[/code]. Even at quality [code]1.0[/code], JPEG compression remains lossy.
#desc [b]Note:[/b] This does not affect the audio quality or writing PNG image sequences.
var editor/movie_writer/mjpeg_quality: float;

#desc The output path for the movie. The file extension determines the [MovieWriter] that will be used.
#desc Godot has 2 built-in [MovieWriter]s:
#desc - AVI container with MJPEG for video and uncompressed audio ([code].avi[/code] file extension). Lossy compression, medium file sizes, fast encoding. The lossy compression quality can be adjusted by changing [member ProjectSettings.editor/movie_writer/mjpeg_quality]. The resulting file can be viewed in most video players, but it must be converted to another format for viewing on the web or by Godot with [VideoStreamPlayer]. MJPEG does not support transparency. AVI output is currently limited to a file of 4 GB in size at most.
#desc - PNG image sequence for video and WAV for audio ([code].png[/code] file extension). Lossless compression, large file sizes, slow encoding. Designed to be encoded to a video file with another tool such as [url=https://ffmpeg.org/]FFmpeg[/url] after recording. Transparency is currently not supported, even if the root viewport is set to be transparent.
#desc If you need to encode to a different format or pipe a stream through third-party software, you can extend this [MovieWriter] class to create your own movie writers.
#desc When using PNG output, the frame number will be appended at the end of the file name. It starts from 0 and is padded with 8 digits to ensure correct sorting and easier processing. For example, if the output path is [code]/tmp/hello.png[/code], the first two frames will be [code]/tmp/hello00000000.png[/code] and [code]/tmp/hello00000001.png[/code]. The audio will be saved at [code]/tmp/hello.wav[/code].
var editor/movie_writer/movie_file: String;

#desc The speaker mode to use in the recorded audio when writing a movie. See [enum AudioServer.SpeakerMode] for possible values.
var editor/movie_writer/speaker_mode: int;

#desc When creating node names automatically, set the type of casing in this project. This is mostly an editor setting.
var editor/node_naming/name_casing: int;

#desc What to use to separate node name from number. This is mostly an editor setting.
var editor/node_naming/name_num_separator: int;

#desc The command-line arguments to append to Godot's own command line when running the project. This doesn't affect the editor itself.
#desc It is possible to make another executable run Godot by using the [code]%command%[/code] placeholder. The placeholder will be replaced with Godot's own command line. Program-specific arguments should be placed [i]before[/i] the placeholder, whereas Godot-specific arguments should be placed [i]after[/i] the placeholder.
#desc For example, this can be used to force the project to run on the dedicated GPU in a NVIDIA Optimus system on Linux:
#desc [codeblock]
#desc prime-run %command%
#desc [/codeblock]
var editor/run/main_run_args: String;

#desc Text-based file extensions to include in the script editor's "Find in Files" feature. You can add e.g. [code]tscn[/code] if you wish to also parse your scene files, especially if you use built-in scripts which are serialized in the scene files.
var editor/script/search_in_file_extensions: PackedStringArray;

#desc Search path for project-specific script templates. Godot will search for script templates both in the editor-specific path and in this project-specific path.
var editor/script/templates_search_path: String;

#desc If [code]true[/code], Blender 3D scene files with the [code].blend[/code] extension will be imported by converting them to glTF 2.0.
#desc This requires configuring a path to a Blender executable in the editor settings at [code]filesystem/import/blender/blender3_path[/code]. Blender 3.0 or later is required.
var filesystem/import/blender/enabled: bool;

#desc Override for [member filesystem/import/blender/enabled] on Android where Blender can't easily be accessed from Godot.
var filesystem/import/blender/enabled.android: bool;

#desc Override for [member filesystem/import/blender/enabled] on the Web where Blender can't easily be accessed from Godot.
var filesystem/import/blender/enabled.web: bool;

#desc If [code]true[/code], Autodesk FBX 3D scene files with the [code].fbx[/code] extension will be imported by converting them to glTF 2.0.
#desc This requires configuring a path to a FBX2glTF executable in the editor settings at [code]filesystem/import/fbx/fbx2gltf_path[/code].
var filesystem/import/fbx/enabled: bool;

#desc Override for [member filesystem/import/fbx/enabled] on Android where FBX2glTF can't easily be accessed from Godot.
var filesystem/import/fbx/enabled.android: bool;

#desc Override for [member filesystem/import/fbx/enabled] on the Web where FBX2glTF can't easily be accessed from Godot.
var filesystem/import/fbx/enabled.web: bool;

#desc Default value for [member ScrollContainer.scroll_deadzone], which will be used for all [ScrollContainer]s unless overridden.
var gui/common/default_scroll_deadzone: int;

#desc If [code]true[/code], swaps Cancel and OK buttons in dialogs on Windows and UWP to follow interface conventions.
var gui/common/swap_cancel_ok: bool;

#desc Maximum undo/redo history size for [TextEdit] fields.
var gui/common/text_edit_undo_stack_max_size: int;

#desc Path to a custom [Theme] resource file to use for the project ([code].theme[/code] or generic [code].tres[/code]/[code].res[/code] extension).
var gui/theme/custom: String;

#desc Path to a custom [Font] resource to use as default for all GUI elements of the project.
var gui/theme/custom_font: String;

#desc Font anti-aliasing mode. See [member FontFile.antialiasing],
var gui/theme/default_font_antialiasing: int;

#desc If set to [code]true[/code], the default font will have mipmaps generated. This prevents text from looking grainy when a [Control] is scaled down, or when a [Label3D] is viewed from a long distance (if [member Label3D.texture_filter] is set to a mode that displays mipmaps).
#desc Enabling [member gui/theme/default_font_generate_mipmaps] increases font generation time and memory usage. Only enable this setting if you actually need it.
#desc [b]Note:[/b] This setting does not affect custom [Font]s used within the project.
var gui/theme/default_font_generate_mipmaps: bool;

#desc Default font hinting mode. See [member FontFile.hinting].
var gui/theme/default_font_hinting: int;

#desc If set to [code]true[/code], the default font will use multichannel signed distance field (MSDF) for crisp rendering at any size. Since this approach does not rely on rasterizing the font every time its size changes, this allows for resizing the font in real-time without any performance penalty. Text will also not look grainy for [Control]s that are scaled down (or for [Label3D]s viewed from a long distance).
#desc MSDF font rendering can be combined with [member gui/theme/default_font_generate_mipmaps] to further improve font rendering quality when scaled down.
#desc [b]Note:[/b] This setting does not affect custom [Font]s used within the project.
var gui/theme/default_font_multichannel_signed_distance_field: bool;

#desc Default font glyph sub-pixel positioning mode. See [member FontFile.subpixel_positioning].
var gui/theme/default_font_subpixel_positioning: int;

#desc The default scale factor for [Control]s, when not overriden by a [Theme].
#desc [b]Note:[/b] This property is only read when the project starts. To change the default scale at runtime, set [member ThemeDB.fallback_base_scale] instead.
var gui/theme/default_theme_scale: float;

#desc LCD sub-pixel layout used for font anti-aliasing. See [enum TextServer.FontLCDSubpixelLayout].
var gui/theme/lcd_subpixel_layout: int;

#desc Timer setting for incremental search in [Tree], [ItemList], etc. controls (in milliseconds).
var gui/timers/incremental_search_max_interval_msec: int;

#desc Timer for detecting idle in [TextEdit] (in seconds).
var gui/timers/text_edit_idle_detect_sec: float;

#desc Default delay for tooltips (in seconds).
var gui/timers/tooltip_delay_sec: float;

#desc Default [InputEventAction] to confirm a focused button, menu or list item, or validate input.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_accept: Dictionary;

#desc Default [InputEventAction] to discard a modal or pending input.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_cancel: Dictionary;

#desc Default [InputEventAction] to copy a selection to the clipboard.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_copy: Dictionary;

#desc Default [InputEventAction] to cut a selection to the clipboard.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_cut: Dictionary;

#desc Default [InputEventAction] to move down in the UI.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_down: Dictionary;

#desc Default [InputEventAction] to go to the end position of a [Control] (e.g. last item in an [ItemList] or a [Tree]), matching the behavior of [constant KEY_END] on typical desktop UI systems.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_end: Dictionary;

#desc Default [InputEventAction] to refresh the contents of the current directory of a [FileDialog].
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_filedialog_refresh: Dictionary;

#desc Default [InputEventAction] to toggle showing hidden files and directories in a [FileDialog].
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_filedialog_show_hidden: Dictionary;

#desc Default [InputEventAction] to go up one directory in a [FileDialog].
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_filedialog_up_one_level: Dictionary;

#desc Default [InputEventAction] to focus the next [Control] in the scene. The focus behavior can be configured via [member Control.focus_next].
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_focus_next: Dictionary;

#desc Default [InputEventAction] to focus the previous [Control] in the scene. The focus behavior can be configured via [member Control.focus_previous].
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_focus_prev: Dictionary;

#desc Default [InputEventAction] to delete a [GraphNode] in a [GraphEdit].
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_graph_delete: Dictionary;

#desc Default [InputEventAction] to duplicate a [GraphNode] in a [GraphEdit].
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_graph_duplicate: Dictionary;

#desc Default [InputEventAction] to go to the start position of a [Control] (e.g. first item in an [ItemList] or a [Tree]), matching the behavior of [constant KEY_HOME] on typical desktop UI systems.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_home: Dictionary;

#desc Default [InputEventAction] to move left in the UI.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_left: Dictionary;

#desc Default [InputEventAction] to open a context menu in a text field.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_menu: Dictionary;

#desc Default [InputEventAction] to go down a page in a [Control] (e.g. in an [ItemList] or a [Tree]), matching the behavior of [constant KEY_PAGEDOWN] on typical desktop UI systems.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_page_down: Dictionary;

#desc Default [InputEventAction] to go up a page in a [Control] (e.g. in an [ItemList] or a [Tree]), matching the behavior of [constant KEY_PAGEUP] on typical desktop UI systems.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_page_up: Dictionary;

#desc Default [InputEventAction] to paste from the clipboard.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_paste: Dictionary;

#desc Default [InputEventAction] to redo an undone action.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_redo: Dictionary;

#desc Default [InputEventAction] to move right in the UI.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_right: Dictionary;

#desc Default [InputEventAction] to select an item in a [Control] (e.g. in an [ItemList] or a [Tree]).
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_select: Dictionary;

var input/ui_swap_input_direction: Dictionary;

#desc Default [InputEventAction] to delete the character before the text cursor.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_backspace: Dictionary;

#desc Default [InputEventAction] to delete [b]all[/b] text before the text cursor.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_backspace_all_to_left: Dictionary;

#desc macOS specific override for the shortcut to delete all text before the text cursor.
var input/ui_text_backspace_all_to_left.macos: Dictionary;

#desc Default [InputEventAction] to delete all characters before the cursor up until a whitespace or punctuation character.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_backspace_word: Dictionary;

#desc macOS specific override for the shortcut to delete a word.
var input/ui_text_backspace_word.macos: Dictionary;

#desc Default [InputEventAction] to move the text cursor the the end of the text.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_caret_document_end: Dictionary;

#desc macOS specific override for the shortcut to move the text cursor to the end of the text.
var input/ui_text_caret_document_end.macos: Dictionary;

#desc Default [InputEventAction] to move the text cursor to the start of the text.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_caret_document_start: Dictionary;

#desc macOS specific override for the shortcut to move the text cursor to the start of the text.
var input/ui_text_caret_document_start.macos: Dictionary;

#desc Default [InputEventAction] to move the text cursor down.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_caret_down: Dictionary;

#desc Default [InputEventAction] to move the text cursor left.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_caret_left: Dictionary;

#desc Default [InputEventAction] to move the text cursor to the end of the line.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_caret_line_end: Dictionary;

#desc macOS specific override for the shortcut to move the text cursor to the end of the line.
var input/ui_text_caret_line_end.macos: Dictionary;

#desc Default [InputEventAction] to move the text cursor to the start of the line.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_caret_line_start: Dictionary;

#desc macOS specific override for the shortcut to move the text cursor to the start of the line.
var input/ui_text_caret_line_start.macos: Dictionary;

#desc Default [InputEventAction] to move the text cursor down one page.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_caret_page_down: Dictionary;

#desc Default [InputEventAction] to move the text cursor up one page.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_caret_page_up: Dictionary;

#desc Default [InputEventAction] to move the text cursor right.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_caret_right: Dictionary;

#desc Default [InputEventAction] to move the text cursor up.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_caret_up: Dictionary;

#desc Default [InputEventAction] to move the text cursor left to the next whitespace or punctuation.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_caret_word_left: Dictionary;

#desc macOS specific override for the shortcut to move the text cursor back one word.
var input/ui_text_caret_word_left.macos: Dictionary;

#desc Default [InputEventAction] to move the text cursor right to the next whitespace or punctuation.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_caret_word_right: Dictionary;

#desc macOS specific override for the shortcut to move the text cursor forward one word.
var input/ui_text_caret_word_right.macos: Dictionary;

#desc Default [InputEventAction] to accept an autocompetion hint.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_completion_accept: Dictionary;

#desc Default [InputEventAction] to request autocompetion.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_completion_query: Dictionary;

#desc Default [InputEventAction] to accept an autocompetion hint, replacing existing text.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_completion_replace: Dictionary;

#desc Default [InputEventAction] to unindent text.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_dedent: Dictionary;

#desc Default [InputEventAction] to delete the character after the text cursor.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_delete: Dictionary;

#desc Default [InputEventAction] to delete [b]all[/b] text after the text cursor.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_delete_all_to_right: Dictionary;

#desc macOS specific override for the shortcut to delete all text after the text cursor.
var input/ui_text_delete_all_to_right.macos: Dictionary;

#desc Default [InputEventAction] to delete all characters after the cursor up until a whitespace or punctuation character.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_delete_word: Dictionary;

#desc macOS specific override for the shortcut to delete a word after the text cursor.
var input/ui_text_delete_word.macos: Dictionary;

#desc Default [InputEventAction] to indent the current line.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_indent: Dictionary;

#desc Default [InputEventAction] to insert a new line at the position of the text cursor.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_newline: Dictionary;

#desc Default [InputEventAction] to insert a new line before the current one.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_newline_above: Dictionary;

#desc Default [InputEventAction] to insert a new line after the current one.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_newline_blank: Dictionary;

#desc Default [InputEventAction] to scroll down one line of text.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_scroll_down: Dictionary;

#desc macOS specific override for the shortcut to scroll down one line.
var input/ui_text_scroll_down.macos: Dictionary;

#desc Default [InputEventAction] to scroll up one line of text.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_scroll_up: Dictionary;

#desc macOS specific override for the shortcut to scroll up one line.
var input/ui_text_scroll_up.macos: Dictionary;

#desc Default [InputEventAction] to select all text.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_select_all: Dictionary;

#desc If no selection is currently active, selects the word currently under the caret in text fields. If a selection is currently active, deselects the current selection.
#desc [b]Note:[/b] Currently, this is only implemented in [TextEdit], not [LineEdit].
var input/ui_text_select_word_under_caret: Dictionary;

#desc Default [InputEventAction] to submit a text field.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_submit: Dictionary;

#desc Default [InputEventAction] to toggle [i]instert mode[/i] in a text field. While in insert mode, inserting new text overrides the character after the cursor, unless the next character is a new line.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_text_toggle_insert_mode: Dictionary;

#desc Default [InputEventAction] to undo the most recent action.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_undo: Dictionary;

#desc Default [InputEventAction] to move up in the UI.
#desc [b]Note:[/b] Default [code]ui_*[/code] actions cannot be removed as they are necessary for the internal logic of several [Control]s. The events assigned to the action can however be modified.
var input/ui_up: Dictionary;

#desc If [code]true[/code], key/touch/joystick events will be flushed just before every idle and physics frame.
#desc If [code]false[/code], such events will be flushed only once per process frame, between iterations of the engine.
#desc Enabling this can greatly improve the responsiveness to input, specially in devices that need to run multiple physics frames per visible (process) frame, because they can't run at the target frame rate.
#desc [b]Note:[/b] Currently implemented only on Android.
var input_devices/buffering/agile_event_flushing: bool;

#desc Specifies the tablet driver to use. If left empty, the default driver will be used.
#desc [b]Note:[/b] The driver in use can be overridden at runtime via the [code]--tablet-driver[/code] command line argument.
var input_devices/pen_tablet/driver: String;

#desc Override for [member input_devices/pen_tablet/driver] on Windows.
var input_devices/pen_tablet/driver.windows: String;

#desc If [code]true[/code], sends mouse input events when tapping or swiping on the touchscreen.
var input_devices/pointing/emulate_mouse_from_touch: bool;

#desc If [code]true[/code], sends touch input events when clicking or dragging the mouse.
var input_devices/pointing/emulate_touch_from_mouse: bool;

#desc Default delay for touch events. This only affects iOS devices.
var input_devices/pointing/ios/touch_delay: float;

#desc The locale to fall back to if a translation isn't available in a given language. If left empty, [code]en[/code] (English) will be used.
var internationalization/locale/fallback: String;

#desc If [code]true[/code], text server break iteration rule sets, dictionaries and other optional data are included in the exported project.
#desc [b]Note:[/b] "ICU / HarfBuzz / Graphite" text server data includes dictionaries for Burmese, Chinese, Japanese, Khmer, Lao and Thai as well as Unicode Standard Annex #29 and Unicode Standard Annex #14 word and line breaking rules. Data is about 4 MB large.
#desc [b]Note:[/b] "Fallback" text server does not use additional data.
var internationalization/locale/include_text_server_data: bool;

#desc If non-empty, this locale will be used when running the project from the editor.
var internationalization/locale/test: String;

#desc Locale-dependent resource remaps. Edit them in the "Localization" tab of Project Settings editor.
var internationalization/locale/translation_remaps: PackedStringArray;

#desc List of translation files available in the project. Edit them in the "Localization" tab of Project Settings editor.
var internationalization/locale/translations: PackedStringArray;

#desc Double vowels in strings during pseudolocalization to simulate the lengthening of text due to localization.
var internationalization/pseudolocalization/double_vowels: bool;

#desc The expansion ratio to use during pseudolocalization. A value of [code]0.3[/code] is sufficient for most practical purposes, and will increase the length of each string by 30%.
var internationalization/pseudolocalization/expansion_ratio: float;

#desc If [code]true[/code], emulate bidirectional (right-to-left) text when pseudolocalization is enabled. This can be used to spot issues with RTL layout and UI mirroring that will crop up if the project is localized to RTL languages such as Arabic or Hebrew.
var internationalization/pseudolocalization/fake_bidi: bool;

#desc Replace all characters in the string with [code]*[/code]. Useful for finding non-localizable strings.
var internationalization/pseudolocalization/override: bool;

#desc Prefix that will be prepended to the pseudolocalized string.
var internationalization/pseudolocalization/prefix: String;

#desc Replace all characters with their accented variants during pseudolocalization.
var internationalization/pseudolocalization/replace_with_accents: bool;

#desc Skip placeholders for string formatting like [code]%s[/code] or [code]%f[/code] during pseudolocalization. Useful to identify strings which need additional control characters to display correctly.
var internationalization/pseudolocalization/skip_placeholders: bool;

#desc Suffix that will be appended to the pseudolocalized string.
var internationalization/pseudolocalization/suffix: String;

#desc If [code]true[/code], enables pseudolocalization for the project. This can be used to spot untranslatable strings or layout issues that may occur once the project is localized to languages that have longer strings than the source language.
#desc [b]Note:[/b] This property is only read when the project starts. To toggle pseudolocalization at run-time, use [member TranslationServer.pseudolocalization_enabled] instead.
var internationalization/pseudolocalization/use_pseudolocalization: bool;

#desc Force layout direction and text writing direction to RTL for all locales.
var internationalization/rendering/force_right_to_left_layout_direction: bool;

#desc Specifies the [TextServer] to use. If left empty, the default will be used.
#desc "ICU / HarfBuzz / Graphite" is the most advanced text driver, supporting right-to-left typesetting and complex scripts (for languages like Arabic, Hebrew, etc). The "Fallback" text driver does not support right-to-left typesetting and complex scripts.
#desc [b]Note:[/b] The driver in use can be overridden at runtime via the [code]--text-driver[/code] command line argument.
#desc [b]Note:[/b] There is an additional [code]Dummy[/code] text driver available, which disables all text rendering and font-related functionality. This driver is not listed in the project settings, but it can be enabled when running the editor or project using the [code]--text-driver Dummy[/code] command line argument.
var internationalization/rendering/text_driver: String;

#desc Optional name for the 2D navigation layer 1. If left empty, the layer will display as "Layer 1".
var layer_names/2d_navigation/layer_1: String;

#desc Optional name for the 2D navigation layer 10. If left empty, the layer will display as "Layer 10".
var layer_names/2d_navigation/layer_10: String;

#desc Optional name for the 2D navigation layer 11. If left empty, the layer will display as "Layer 11".
var layer_names/2d_navigation/layer_11: String;

#desc Optional name for the 2D navigation layer 12. If left empty, the layer will display as "Layer 12".
var layer_names/2d_navigation/layer_12: String;

#desc Optional name for the 2D navigation layer 13. If left empty, the layer will display as "Layer 13".
var layer_names/2d_navigation/layer_13: String;

#desc Optional name for the 2D navigation layer 14. If left empty, the layer will display as "Layer 14".
var layer_names/2d_navigation/layer_14: String;

#desc Optional name for the 2D navigation layer 15. If left empty, the layer will display as "Layer 15".
var layer_names/2d_navigation/layer_15: String;

#desc Optional name for the 2D navigation layer 16. If left empty, the layer will display as "Layer 16".
var layer_names/2d_navigation/layer_16: String;

#desc Optional name for the 2D navigation layer 17. If left empty, the layer will display as "Layer 17".
var layer_names/2d_navigation/layer_17: String;

#desc Optional name for the 2D navigation layer 18. If left empty, the layer will display as "Layer 18".
var layer_names/2d_navigation/layer_18: String;

#desc Optional name for the 2D navigation layer 19. If left empty, the layer will display as "Layer 19".
var layer_names/2d_navigation/layer_19: String;

#desc Optional name for the 2D navigation layer 2. If left empty, the layer will display as "Layer 2".
var layer_names/2d_navigation/layer_2: String;

#desc Optional name for the 2D navigation layer 20. If left empty, the layer will display as "Layer 20".
var layer_names/2d_navigation/layer_20: String;

#desc Optional name for the 2D navigation layer 21. If left empty, the layer will display as "Layer 21".
var layer_names/2d_navigation/layer_21: String;

#desc Optional name for the 2D navigation layer 22. If left empty, the layer will display as "Layer 22".
var layer_names/2d_navigation/layer_22: String;

#desc Optional name for the 2D navigation layer 23. If left empty, the layer will display as "Layer 23".
var layer_names/2d_navigation/layer_23: String;

#desc Optional name for the 2D navigation layer 24. If left empty, the layer will display as "Layer 24".
var layer_names/2d_navigation/layer_24: String;

#desc Optional name for the 2D navigation layer 25. If left empty, the layer will display as "Layer 25".
var layer_names/2d_navigation/layer_25: String;

#desc Optional name for the 2D navigation layer 26. If left empty, the layer will display as "Layer 26".
var layer_names/2d_navigation/layer_26: String;

#desc Optional name for the 2D navigation layer 27. If left empty, the layer will display as "Layer 27".
var layer_names/2d_navigation/layer_27: String;

#desc Optional name for the 2D navigation layer 28. If left empty, the layer will display as "Layer 28".
var layer_names/2d_navigation/layer_28: String;

#desc Optional name for the 2D navigation layer 29. If left empty, the layer will display as "Layer 29".
var layer_names/2d_navigation/layer_29: String;

#desc Optional name for the 2D navigation layer 3. If left empty, the layer will display as "Layer 3".
var layer_names/2d_navigation/layer_3: String;

#desc Optional name for the 2D navigation layer 30. If left empty, the layer will display as "Layer 30".
var layer_names/2d_navigation/layer_30: String;

#desc Optional name for the 2D navigation layer 31. If left empty, the layer will display as "Layer 31".
var layer_names/2d_navigation/layer_31: String;

#desc Optional name for the 2D navigation layer 32. If left empty, the layer will display as "Layer 32".
var layer_names/2d_navigation/layer_32: String;

#desc Optional name for the 2D navigation layer 4. If left empty, the layer will display as "Layer 4".
var layer_names/2d_navigation/layer_4: String;

#desc Optional name for the 2D navigation layer 5. If left empty, the layer will display as "Layer 5".
var layer_names/2d_navigation/layer_5: String;

#desc Optional name for the 2D navigation layer 6. If left empty, the layer will display as "Layer 6".
var layer_names/2d_navigation/layer_6: String;

#desc Optional name for the 2D navigation layer 7. If left empty, the layer will display as "Layer 7".
var layer_names/2d_navigation/layer_7: String;

#desc Optional name for the 2D navigation layer 8. If left empty, the layer will display as "Layer 8".
var layer_names/2d_navigation/layer_8: String;

#desc Optional name for the 2D navigation layer 9. If left empty, the layer will display as "Layer 9".
var layer_names/2d_navigation/layer_9: String;

#desc Optional name for the 2D physics layer 1. If left empty, the layer will display as "Layer 1".
var layer_names/2d_physics/layer_1: String;

#desc Optional name for the 2D physics layer 10. If left empty, the layer will display as "Layer 10".
var layer_names/2d_physics/layer_10: String;

#desc Optional name for the 2D physics layer 11. If left empty, the layer will display as "Layer 11".
var layer_names/2d_physics/layer_11: String;

#desc Optional name for the 2D physics layer 12. If left empty, the layer will display as "Layer 12".
var layer_names/2d_physics/layer_12: String;

#desc Optional name for the 2D physics layer 13. If left empty, the layer will display as "Layer 13".
var layer_names/2d_physics/layer_13: String;

#desc Optional name for the 2D physics layer 14. If left empty, the layer will display as "Layer 14".
var layer_names/2d_physics/layer_14: String;

#desc Optional name for the 2D physics layer 15. If left empty, the layer will display as "Layer 15".
var layer_names/2d_physics/layer_15: String;

#desc Optional name for the 2D physics layer 16. If left empty, the layer will display as "Layer 16".
var layer_names/2d_physics/layer_16: String;

#desc Optional name for the 2D physics layer 17. If left empty, the layer will display as "Layer 17".
var layer_names/2d_physics/layer_17: String;

#desc Optional name for the 2D physics layer 18. If left empty, the layer will display as "Layer 18".
var layer_names/2d_physics/layer_18: String;

#desc Optional name for the 2D physics layer 19. If left empty, the layer will display as "Layer 19".
var layer_names/2d_physics/layer_19: String;

#desc Optional name for the 2D physics layer 2. If left empty, the layer will display as "Layer 2".
var layer_names/2d_physics/layer_2: String;

#desc Optional name for the 2D physics layer 20. If left empty, the layer will display as "Layer 20".
var layer_names/2d_physics/layer_20: String;

#desc Optional name for the 2D physics layer 21. If left empty, the layer will display as "Layer 21".
var layer_names/2d_physics/layer_21: String;

#desc Optional name for the 2D physics layer 22. If left empty, the layer will display as "Layer 22".
var layer_names/2d_physics/layer_22: String;

#desc Optional name for the 2D physics layer 23. If left empty, the layer will display as "Layer 23".
var layer_names/2d_physics/layer_23: String;

#desc Optional name for the 2D physics layer 24. If left empty, the layer will display as "Layer 24".
var layer_names/2d_physics/layer_24: String;

#desc Optional name for the 2D physics layer 25. If left empty, the layer will display as "Layer 25".
var layer_names/2d_physics/layer_25: String;

#desc Optional name for the 2D physics layer 26. If left empty, the layer will display as "Layer 26".
var layer_names/2d_physics/layer_26: String;

#desc Optional name for the 2D physics layer 27. If left empty, the layer will display as "Layer 27".
var layer_names/2d_physics/layer_27: String;

#desc Optional name for the 2D physics layer 28. If left empty, the layer will display as "Layer 28".
var layer_names/2d_physics/layer_28: String;

#desc Optional name for the 2D physics layer 29. If left empty, the layer will display as "Layer 29".
var layer_names/2d_physics/layer_29: String;

#desc Optional name for the 2D physics layer 3. If left empty, the layer will display as "Layer 3".
var layer_names/2d_physics/layer_3: String;

#desc Optional name for the 2D physics layer 30. If left empty, the layer will display as "Layer 30".
var layer_names/2d_physics/layer_30: String;

#desc Optional name for the 2D physics layer 31. If left empty, the layer will display as "Layer 31".
var layer_names/2d_physics/layer_31: String;

#desc Optional name for the 2D physics layer 32. If left empty, the layer will display as "Layer 32".
var layer_names/2d_physics/layer_32: String;

#desc Optional name for the 2D physics layer 4. If left empty, the layer will display as "Layer 4".
var layer_names/2d_physics/layer_4: String;

#desc Optional name for the 2D physics layer 5. If left empty, the layer will display as "Layer 5".
var layer_names/2d_physics/layer_5: String;

#desc Optional name for the 2D physics layer 6. If left empty, the layer will display as "Layer 6".
var layer_names/2d_physics/layer_6: String;

#desc Optional name for the 2D physics layer 7. If left empty, the layer will display as "Layer 7".
var layer_names/2d_physics/layer_7: String;

#desc Optional name for the 2D physics layer 8. If left empty, the layer will display as "Layer 8".
var layer_names/2d_physics/layer_8: String;

#desc Optional name for the 2D physics layer 9. If left empty, the layer will display as "Layer 9".
var layer_names/2d_physics/layer_9: String;

#desc Optional name for the 2D render layer 1. If left empty, the layer will display as "Layer 1".
var layer_names/2d_render/layer_1: String;

#desc Optional name for the 2D render layer 10. If left empty, the layer will display as "Layer 10".
var layer_names/2d_render/layer_10: String;

#desc Optional name for the 2D render layer 11. If left empty, the layer will display as "Layer 11".
var layer_names/2d_render/layer_11: String;

#desc Optional name for the 2D render layer 12. If left empty, the layer will display as "Layer 12".
var layer_names/2d_render/layer_12: String;

#desc Optional name for the 2D render layer 13. If left empty, the layer will display as "Layer 13".
var layer_names/2d_render/layer_13: String;

#desc Optional name for the 2D render layer 14. If left empty, the layer will display as "Layer 14".
var layer_names/2d_render/layer_14: String;

#desc Optional name for the 2D render layer 15. If left empty, the layer will display as "Layer 15".
var layer_names/2d_render/layer_15: String;

#desc Optional name for the 2D render layer 16. If left empty, the layer will display as "Layer 16".
var layer_names/2d_render/layer_16: String;

#desc Optional name for the 2D render layer 17. If left empty, the layer will display as "Layer 17".
var layer_names/2d_render/layer_17: String;

#desc Optional name for the 2D render layer 18. If left empty, the layer will display as "Layer 18".
var layer_names/2d_render/layer_18: String;

#desc Optional name for the 2D render layer 19. If left empty, the layer will display as "Layer 19".
var layer_names/2d_render/layer_19: String;

#desc Optional name for the 2D render layer 2. If left empty, the layer will display as "Layer 2".
var layer_names/2d_render/layer_2: String;

#desc Optional name for the 2D render layer 20. If left empty, the layer will display as "Layer 20".
var layer_names/2d_render/layer_20: String;

#desc Optional name for the 2D render layer 3. If left empty, the layer will display as "Layer 3".
var layer_names/2d_render/layer_3: String;

#desc Optional name for the 2D render layer 4. If left empty, the layer will display as "Layer 4".
var layer_names/2d_render/layer_4: String;

#desc Optional name for the 2D render layer 5. If left empty, the layer will display as "Layer 5".
var layer_names/2d_render/layer_5: String;

#desc Optional name for the 2D render layer 6. If left empty, the layer will display as "Layer 6".
var layer_names/2d_render/layer_6: String;

#desc Optional name for the 2D render layer 7. If left empty, the layer will display as "Layer 7".
var layer_names/2d_render/layer_7: String;

#desc Optional name for the 2D render layer 8. If left empty, the layer will display as "Layer 8".
var layer_names/2d_render/layer_8: String;

#desc Optional name for the 2D render layer 9. If left empty, the layer will display as "Layer 9".
var layer_names/2d_render/layer_9: String;

#desc Optional name for the 3D navigation layer 1. If left empty, the layer will display as "Layer 1".
var layer_names/3d_navigation/layer_1: String;

#desc Optional name for the 3D navigation layer 10. If left empty, the layer will display as "Layer 10".
var layer_names/3d_navigation/layer_10: String;

#desc Optional name for the 3D navigation layer 11. If left empty, the layer will display as "Layer 11".
var layer_names/3d_navigation/layer_11: String;

#desc Optional name for the 3D navigation layer 12. If left empty, the layer will display as "Layer 12".
var layer_names/3d_navigation/layer_12: String;

#desc Optional name for the 3D navigation layer 13. If left empty, the layer will display as "Layer 13".
var layer_names/3d_navigation/layer_13: String;

#desc Optional name for the 3D navigation layer 14. If left empty, the layer will display as "Layer 14".
var layer_names/3d_navigation/layer_14: String;

#desc Optional name for the 3D navigation layer 15. If left empty, the layer will display as "Layer 15".
var layer_names/3d_navigation/layer_15: String;

#desc Optional name for the 3D navigation layer 16. If left empty, the layer will display as "Layer 16".
var layer_names/3d_navigation/layer_16: String;

#desc Optional name for the 3D navigation layer 17. If left empty, the layer will display as "Layer 17".
var layer_names/3d_navigation/layer_17: String;

#desc Optional name for the 3D navigation layer 18. If left empty, the layer will display as "Layer 18".
var layer_names/3d_navigation/layer_18: String;

#desc Optional name for the 3D navigation layer 19. If left empty, the layer will display as "Layer 19".
var layer_names/3d_navigation/layer_19: String;

#desc Optional name for the 3D navigation layer 2. If left empty, the layer will display as "Layer 2".
var layer_names/3d_navigation/layer_2: String;

#desc Optional name for the 3D navigation layer 20. If left empty, the layer will display as "Layer 20".
var layer_names/3d_navigation/layer_20: String;

#desc Optional name for the 3D navigation layer 21. If left empty, the layer will display as "Layer 21".
var layer_names/3d_navigation/layer_21: String;

#desc Optional name for the 3D navigation layer 22. If left empty, the layer will display as "Layer 22".
var layer_names/3d_navigation/layer_22: String;

#desc Optional name for the 3D navigation layer 23. If left empty, the layer will display as "Layer 23".
var layer_names/3d_navigation/layer_23: String;

#desc Optional name for the 3D navigation layer 24. If left empty, the layer will display as "Layer 24".
var layer_names/3d_navigation/layer_24: String;

#desc Optional name for the 3D navigation layer 25. If left empty, the layer will display as "Layer 25".
var layer_names/3d_navigation/layer_25: String;

#desc Optional name for the 3D navigation layer 26. If left empty, the layer will display as "Layer 26".
var layer_names/3d_navigation/layer_26: String;

#desc Optional name for the 3D navigation layer 27. If left empty, the layer will display as "Layer 27".
var layer_names/3d_navigation/layer_27: String;

#desc Optional name for the 3D navigation layer 28. If left empty, the layer will display as "Layer 28".
var layer_names/3d_navigation/layer_28: String;

#desc Optional name for the 3D navigation layer 29. If left empty, the layer will display as "Layer 29".
var layer_names/3d_navigation/layer_29: String;

#desc Optional name for the 3D navigation layer 3. If left empty, the layer will display as "Layer 3".
var layer_names/3d_navigation/layer_3: String;

#desc Optional name for the 3D navigation layer 30. If left empty, the layer will display as "Layer 30".
var layer_names/3d_navigation/layer_30: String;

#desc Optional name for the 3D navigation layer 31. If left empty, the layer will display as "Layer 31".
var layer_names/3d_navigation/layer_31: String;

#desc Optional name for the 3D navigation layer 32. If left empty, the layer will display as "Layer 32".
var layer_names/3d_navigation/layer_32: String;

#desc Optional name for the 3D navigation layer 4. If left empty, the layer will display as "Layer 4".
var layer_names/3d_navigation/layer_4: String;

#desc Optional name for the 3D navigation layer 5. If left empty, the layer will display as "Layer 5".
var layer_names/3d_navigation/layer_5: String;

#desc Optional name for the 3D navigation layer 6. If left empty, the layer will display as "Layer 6".
var layer_names/3d_navigation/layer_6: String;

#desc Optional name for the 3D navigation layer 7. If left empty, the layer will display as "Layer 7".
var layer_names/3d_navigation/layer_7: String;

#desc Optional name for the 3D navigation layer 8. If left empty, the layer will display as "Layer 8".
var layer_names/3d_navigation/layer_8: String;

#desc Optional name for the 3D navigation layer 9. If left empty, the layer will display as "Layer 9".
var layer_names/3d_navigation/layer_9: String;

#desc Optional name for the 3D physics layer 1. If left empty, the layer will display as "Layer 1".
var layer_names/3d_physics/layer_1: String;

#desc Optional name for the 3D physics layer 10. If left empty, the layer will display as "Layer 10".
var layer_names/3d_physics/layer_10: String;

#desc Optional name for the 3D physics layer 11. If left empty, the layer will display as "Layer 11".
var layer_names/3d_physics/layer_11: String;

#desc Optional name for the 3D physics layer 12. If left empty, the layer will display as "Layer 12".
var layer_names/3d_physics/layer_12: String;

#desc Optional name for the 3D physics layer 13. If left empty, the layer will display as "Layer 13".
var layer_names/3d_physics/layer_13: String;

#desc Optional name for the 3D physics layer 14. If left empty, the layer will display as "Layer 14".
var layer_names/3d_physics/layer_14: String;

#desc Optional name for the 3D physics layer 15. If left empty, the layer will display as "Layer 15".
var layer_names/3d_physics/layer_15: String;

#desc Optional name for the 3D physics layer 16. If left empty, the layer will display as "Layer 16".
var layer_names/3d_physics/layer_16: String;

#desc Optional name for the 3D physics layer 17. If left empty, the layer will display as "Layer 17".
var layer_names/3d_physics/layer_17: String;

#desc Optional name for the 3D physics layer 18. If left empty, the layer will display as "Layer 18".
var layer_names/3d_physics/layer_18: String;

#desc Optional name for the 3D physics layer 19. If left empty, the layer will display as "Layer 19".
var layer_names/3d_physics/layer_19: String;

#desc Optional name for the 3D physics layer 2. If left empty, the layer will display as "Layer 2".
var layer_names/3d_physics/layer_2: String;

#desc Optional name for the 3D physics layer 20. If left empty, the layer will display as "Layer 20".
var layer_names/3d_physics/layer_20: String;

#desc Optional name for the 3D physics layer 21. If left empty, the layer will display as "Layer 21".
var layer_names/3d_physics/layer_21: String;

#desc Optional name for the 3D physics layer 22. If left empty, the layer will display as "Layer 22".
var layer_names/3d_physics/layer_22: String;

#desc Optional name for the 3D physics layer 23. If left empty, the layer will display as "Layer 23".
var layer_names/3d_physics/layer_23: String;

#desc Optional name for the 3D physics layer 24. If left empty, the layer will display as "Layer 24".
var layer_names/3d_physics/layer_24: String;

#desc Optional name for the 3D physics layer 25. If left empty, the layer will display as "Layer 25".
var layer_names/3d_physics/layer_25: String;

#desc Optional name for the 3D physics layer 26. If left empty, the layer will display as "Layer 26".
var layer_names/3d_physics/layer_26: String;

#desc Optional name for the 3D physics layer 27. If left empty, the layer will display as "Layer 27".
var layer_names/3d_physics/layer_27: String;

#desc Optional name for the 3D physics layer 28. If left empty, the layer will display as "Layer 28".
var layer_names/3d_physics/layer_28: String;

#desc Optional name for the 3D physics layer 29. If left empty, the layer will display as "Layer 29".
var layer_names/3d_physics/layer_29: String;

#desc Optional name for the 3D physics layer 3. If left empty, the layer will display as "Layer 3".
var layer_names/3d_physics/layer_3: String;

#desc Optional name for the 3D physics layer 30. If left empty, the layer will display as "Layer 30".
var layer_names/3d_physics/layer_30: String;

#desc Optional name for the 3D physics layer 31. If left empty, the layer will display as "Layer 31".
var layer_names/3d_physics/layer_31: String;

#desc Optional name for the 3D physics layer 32. If left empty, the layer will display as "Layer 32".
var layer_names/3d_physics/layer_32: String;

#desc Optional name for the 3D physics layer 4. If left empty, the layer will display as "Layer 4".
var layer_names/3d_physics/layer_4: String;

#desc Optional name for the 3D physics layer 5. If left empty, the layer will display as "Layer 5".
var layer_names/3d_physics/layer_5: String;

#desc Optional name for the 3D physics layer 6. If left empty, the layer will display as "Layer 6".
var layer_names/3d_physics/layer_6: String;

#desc Optional name for the 3D physics layer 7. If left empty, the layer will display as "Layer 7".
var layer_names/3d_physics/layer_7: String;

#desc Optional name for the 3D physics layer 8. If left empty, the layer will display as "Layer 8".
var layer_names/3d_physics/layer_8: String;

#desc Optional name for the 3D physics layer 9. If left empty, the layer will display as "Layer 9".
var layer_names/3d_physics/layer_9: String;

#desc Optional name for the 3D render layer 1. If left empty, the layer will display as "Layer 1".
var layer_names/3d_render/layer_1: String;

#desc Optional name for the 3D render layer 10. If left empty, the layer will display as "Layer 10".
var layer_names/3d_render/layer_10: String;

#desc Optional name for the 3D render layer 11. If left empty, the layer will display as "Layer 11".
var layer_names/3d_render/layer_11: String;

#desc Optional name for the 3D render layer 12. If left empty, the layer will display as "Layer 12".
var layer_names/3d_render/layer_12: String;

#desc Optional name for the 3D render layer 13. If left empty, the layer will display as "Layer 13".
var layer_names/3d_render/layer_13: String;

#desc Optional name for the 3D render layer 14. If left empty, the layer will display as "Layer 14"
var layer_names/3d_render/layer_14: String;

#desc Optional name for the 3D render layer 15. If left empty, the layer will display as "Layer 15".
var layer_names/3d_render/layer_15: String;

#desc Optional name for the 3D render layer 16. If left empty, the layer will display as "Layer 16".
var layer_names/3d_render/layer_16: String;

#desc Optional name for the 3D render layer 17. If left empty, the layer will display as "Layer 17".
var layer_names/3d_render/layer_17: String;

#desc Optional name for the 3D render layer 18. If left empty, the layer will display as "Layer 18".
var layer_names/3d_render/layer_18: String;

#desc Optional name for the 3D render layer 19. If left empty, the layer will display as "Layer 19".
var layer_names/3d_render/layer_19: String;

#desc Optional name for the 3D render layer 2. If left empty, the layer will display as "Layer 2".
var layer_names/3d_render/layer_2: String;

#desc Optional name for the 3D render layer 20. If left empty, the layer will display as "Layer 20".
var layer_names/3d_render/layer_20: String;

#desc Optional name for the 3D render layer 3. If left empty, the layer will display as "Layer 3".
var layer_names/3d_render/layer_3: String;

#desc Optional name for the 3D render layer 4. If left empty, the layer will display as "Layer 4".
var layer_names/3d_render/layer_4: String;

#desc Optional name for the 3D render layer 5. If left empty, the layer will display as "Layer 5".
var layer_names/3d_render/layer_5: String;

#desc Optional name for the 3D render layer 6. If left empty, the layer will display as "Layer 6".
var layer_names/3d_render/layer_6: String;

#desc Optional name for the 3D render layer 7. If left empty, the layer will display as "Layer 7".
var layer_names/3d_render/layer_7: String;

#desc Optional name for the 3D render layer 8. If left empty, the layer will display as "Layer 8".
var layer_names/3d_render/layer_8: String;

#desc Optional name for the 3D render layer 9. If left empty, the layer will display as "Layer 9".
var layer_names/3d_render/layer_9: String;

#desc Godot uses a message queue to defer some function calls. If you run out of space on it (you will see an error), you can increase the size here.
var memory/limits/message_queue/max_size_kb: int;

#desc This is used by servers when used in multi-threading mode (servers and visual). RIDs are preallocated to avoid stalling the server requesting them on threads. If servers get stalled too often when loading resources in a thread, increase this number.
var memory/limits/multithreaded_server/rid_pool_prealloc: int;

#desc Default cell size for 2D navigation maps. See [method NavigationServer2D.map_set_cell_size].
var navigation/2d/default_cell_size: int;

#desc Default edge connection margin for 2D navigation maps. See [method NavigationServer2D.map_set_edge_connection_margin].
var navigation/2d/default_edge_connection_margin: int;

#desc Default link connection radius for 2D navigation maps. See [method NavigationServer2D.map_set_link_connection_radius].
var navigation/2d/default_link_connection_radius: int;

#desc Default cell size for 3D navigation maps. See [method NavigationServer3D.map_set_cell_size].
var navigation/3d/default_cell_size: float;

#desc Default edge connection margin for 3D navigation maps. See [method NavigationServer3D.map_set_edge_connection_margin].
var navigation/3d/default_edge_connection_margin: float;

#desc Default link connection radius for 3D navigation maps. See [method NavigationServer3D.map_set_link_connection_radius].
var navigation/3d/default_link_connection_radius: float;

#desc Maximum number of characters allowed to send as output from the debugger. Over this value, content is dropped. This helps not to stall the debugger connection.
var network/limits/debugger/max_chars_per_second: int;

#desc Maximum number of errors allowed to be sent from the debugger. Over this value, content is dropped. This helps not to stall the debugger connection.
var network/limits/debugger/max_errors_per_second: int;

#desc Maximum number of messages in the debugger queue. Over this value, content is dropped. This helps to limit the debugger memory usage.
var network/limits/debugger/max_queued_messages: int;

#desc Maximum number of warnings allowed to be sent from the debugger. Over this value, content is dropped. This helps not to stall the debugger connection.
var network/limits/debugger/max_warnings_per_second: int;

#desc Default size of packet peer stream for deserializing Godot data (in bytes, specified as a power of two). The default value [code]16[/code] is equal to 65,536 bytes. Over this size, data is dropped.
var network/limits/packet_peer_stream/max_buffer_po2: int;

#desc Timeout (in seconds) for connection attempts using TCP.
var network/limits/tcp/connect_timeout_seconds: int;

#desc Maximum size (in kiB) for the [WebRTCDataChannel] input buffer.
var network/limits/webrtc/max_channel_in_buffer_kb: int;

#desc Amount of read ahead used by remote filesystem. Higher values decrease the effects of latency at the cost of higher bandwidth usage.
var network/remote_fs/page_read_ahead: int;

#desc Page size used by remote filesystem (in bytes).
var network/remote_fs/page_size: int;

#desc The CA certificates bundle to use for TLS connections. If this is set to a non-empty value, this will [i]override[/i] Godot's default [url=https://github.com/godotengine/godot/blob/master/thirdparty/certs/ca-certificates.crt]Mozilla certificate bundle[/url]. If left empty, the default certificate bundle will be used.
#desc If in doubt, leave this setting empty.
var network/tls/certificate_bundle_override: String;

#desc The default angular damp in 2D.
#desc [b]Note:[/b] Good values are in the range [code]0[/code] to [code]1[/code]. At value [code]0[/code] objects will keep moving with the same velocity. Values greater than [code]1[/code] will aim to reduce the velocity to [code]0[/code] in less than a second e.g. a value of [code]2[/code] will aim to reduce the velocity to [code]0[/code] in half a second. A value equal to or greater than the physics frame rate ([member ProjectSettings.physics/common/physics_ticks_per_second], [code]60[/code] by default) will bring the object to a stop in one iteration.
var physics/2d/default_angular_damp: float;

#desc The default gravity strength in 2D (in pixels per second squared).
#desc [b]Note:[/b] This property is only read when the project starts. To change the default gravity at runtime, use the following code sample:
#desc [codeblocks]
#desc [gdscript]
#desc # Set the default gravity strength to 980.
#desc PhysicsServer2D.area_set_param(get_viewport().find_world_2d().space, PhysicsServer2D.AREA_PARAM_GRAVITY, 980)
#desc [/gdscript]
#desc [csharp]
#desc // Set the default gravity strength to 980.
#desc PhysicsServer2D.AreaSetParam(GetViewport().FindWorld2d().Space, PhysicsServer2D.AreaParameter.Gravity, 980);
#desc [/csharp]
#desc [/codeblocks]
var physics/2d/default_gravity: float;

#desc The default gravity direction in 2D.
#desc [b]Note:[/b] This property is only read when the project starts. To change the default gravity vector at runtime, use the following code sample:
#desc [codeblocks]
#desc [gdscript]
#desc # Set the default gravity direction to `Vector2(0, 1)`.
#desc PhysicsServer2D.area_set_param(get_viewport().find_world_2d().space, PhysicsServer2D.AREA_PARAM_GRAVITY_VECTOR, Vector2.DOWN)
#desc [/gdscript]
#desc [csharp]
#desc // Set the default gravity direction to `Vector2(0, 1)`.
#desc PhysicsServer2D.AreaSetParam(GetViewport().FindWorld2d().Space, PhysicsServer2D.AreaParameter.GravityVector, Vector2.Down)
#desc [/csharp]
#desc [/codeblocks]
var physics/2d/default_gravity_vector: Vector2;

#desc The default linear damp in 2D.
#desc [b]Note:[/b] Good values are in the range [code]0[/code] to [code]1[/code]. At value [code]0[/code] objects will keep moving with the same velocity. Values greater than [code]1[/code] will aim to reduce the velocity to [code]0[/code] in less than a second e.g. a value of [code]2[/code] will aim to reduce the velocity to [code]0[/code] in half a second. A value equal to or greater than the physics frame rate ([member ProjectSettings.physics/common/physics_ticks_per_second], [code]60[/code] by default) will bring the object to a stop in one iteration.
var physics/2d/default_linear_damp: float;

#desc Sets which physics engine to use for 2D physics.
#desc "DEFAULT" and "GodotPhysics2D" are the same, as there is currently no alternative 2D physics server implemented.
var physics/2d/physics_engine: String;

#desc If [code]true[/code], the 2D physics server runs on a separate thread, making better use of multi-core CPUs. If [code]false[/code], the 2D physics server runs on the main thread. Running the physics server on a separate thread can increase performance, but restricts API access to only physics process.
var physics/2d/run_on_separate_thread: bool;

#desc Threshold angular velocity under which a 2D physics body will be considered inactive. See [constant PhysicsServer2D.SPACE_PARAM_BODY_ANGULAR_VELOCITY_SLEEP_THRESHOLD].
var physics/2d/sleep_threshold_angular: float;

#desc Threshold linear velocity under which a 2D physics body will be considered inactive. See [constant PhysicsServer2D.SPACE_PARAM_BODY_LINEAR_VELOCITY_SLEEP_THRESHOLD].
var physics/2d/sleep_threshold_linear: float;

#desc Maximum distance a shape can penetrate another shape before it is considered a collision. See [constant PhysicsServer2D.SPACE_PARAM_CONTACT_MAX_ALLOWED_PENETRATION].
var physics/2d/solver/contact_max_allowed_penetration: float;

#desc Maximum distance a shape can be from another before they are considered separated and the contact is discarded. See [constant PhysicsServer2D.SPACE_PARAM_CONTACT_MAX_SEPARATION].
var physics/2d/solver/contact_max_separation: float;

#desc Maximum distance a pair of bodies has to move before their collision status has to be recalculated. See [constant PhysicsServer2D.SPACE_PARAM_CONTACT_RECYCLE_RADIUS].
var physics/2d/solver/contact_recycle_radius: float;

#desc Default solver bias for all physics constraints. Defines how much bodies react to enforce constraints. See [constant PhysicsServer2D.SPACE_PARAM_CONSTRAINT_DEFAULT_BIAS].
#desc Individual constraints can have a specific bias value (see [member Joint2D.bias]).
var physics/2d/solver/default_constraint_bias: float;

#desc Default solver bias for all physics contacts. Defines how much bodies react to enforce contact separation. See [constant PhysicsServer2D.SPACE_PARAM_CONTACT_DEFAULT_BIAS].
#desc Individual shapes can have a specific bias value (see [member Shape2D.custom_solver_bias]).
var physics/2d/solver/default_contact_bias: float;

#desc Number of solver iterations for all contacts and constraints. The greater the number of iterations, the more accurate the collisions will be. However, a greater number of iterations requires more CPU power, which can decrease performance. See [constant PhysicsServer2D.SPACE_PARAM_SOLVER_ITERATIONS].
var physics/2d/solver/solver_iterations: int;

#desc Time (in seconds) of inactivity before which a 2D physics body will put to sleep. See [constant PhysicsServer2D.SPACE_PARAM_BODY_TIME_TO_SLEEP].
var physics/2d/time_before_sleep: float;

#desc The default angular damp in 3D.
#desc [b]Note:[/b] Good values are in the range [code]0[/code] to [code]1[/code]. At value [code]0[/code] objects will keep moving with the same velocity. Values greater than [code]1[/code] will aim to reduce the velocity to [code]0[/code] in less than a second e.g. a value of [code]2[/code] will aim to reduce the velocity to [code]0[/code] in half a second. A value equal to or greater than the physics frame rate ([member ProjectSettings.physics/common/physics_ticks_per_second], [code]60[/code] by default) will bring the object to a stop in one iteration.
var physics/3d/default_angular_damp: float;

#desc The default gravity strength in 3D (in meters per second squared).
#desc [b]Note:[/b] This property is only read when the project starts. To change the default gravity at runtime, use the following code sample:
#desc [codeblocks]
#desc [gdscript]
#desc # Set the default gravity strength to 9.8.
#desc PhysicsServer3D.area_set_param(get_viewport().find_world().space, PhysicsServer3D.AREA_PARAM_GRAVITY, 9.8)
#desc [/gdscript]
#desc [csharp]
#desc // Set the default gravity strength to 9.8.
#desc PhysicsServer3D.AreaSetParam(GetViewport().FindWorld().Space, PhysicsServer3D.AreaParameter.Gravity, 9.8);
#desc [/csharp]
#desc [/codeblocks]
var physics/3d/default_gravity: float;

#desc The default gravity direction in 3D.
#desc [b]Note:[/b] This property is only read when the project starts. To change the default gravity vector at runtime, use the following code sample:
#desc [codeblocks]
#desc [gdscript]
#desc # Set the default gravity direction to `Vector3(0, -1, 0)`.
#desc PhysicsServer3D.area_set_param(get_viewport().find_world().get_space(), PhysicsServer3D.AREA_PARAM_GRAVITY_VECTOR, Vector3.DOWN)
#desc [/gdscript]
#desc [csharp]
#desc // Set the default gravity direction to `Vector3(0, -1, 0)`.
#desc PhysicsServer3D.AreaSetParam(GetViewport().FindWorld().Space, PhysicsServer3D.AreaParameter.GravityVector, Vector3.Down)
#desc [/csharp]
#desc [/codeblocks]
var physics/3d/default_gravity_vector: Vector3;

#desc The default linear damp in 3D.
#desc [b]Note:[/b] Good values are in the range [code]0[/code] to [code]1[/code]. At value [code]0[/code] objects will keep moving with the same velocity. Values greater than [code]1[/code] will aim to reduce the velocity to [code]0[/code] in less than a second e.g. a value of [code]2[/code] will aim to reduce the velocity to [code]0[/code] in half a second. A value equal to or greater than the physics frame rate ([member ProjectSettings.physics/common/physics_ticks_per_second], [code]60[/code] by default) will bring the object to a stop in one iteration.
var physics/3d/default_linear_damp: float;

#desc Sets which physics engine to use for 3D physics.
#desc "DEFAULT" and "GodotPhysics3D" are the same, as there is currently no alternative 3D physics server implemented.
var physics/3d/physics_engine: String;

#desc If [code]true[/code], the 3D physics server runs on a separate thread, making better use of multi-core CPUs. If [code]false[/code], the 3D physics server runs on the main thread. Running the physics server on a separate thread can increase performance, but restricts API access to only physics process.
var physics/3d/run_on_separate_thread: bool;

#desc Threshold angular velocity under which a 3D physics body will be considered inactive. See [constant PhysicsServer3D.SPACE_PARAM_BODY_ANGULAR_VELOCITY_SLEEP_THRESHOLD].
var physics/3d/sleep_threshold_angular: float;

#desc Threshold linear velocity under which a 3D physics body will be considered inactive. See [constant PhysicsServer3D.SPACE_PARAM_BODY_LINEAR_VELOCITY_SLEEP_THRESHOLD].
var physics/3d/sleep_threshold_linear: float;

#desc Maximum distance a shape can penetrate another shape before it is considered a collision. See [constant PhysicsServer3D.SPACE_PARAM_CONTACT_MAX_ALLOWED_PENETRATION].
var physics/3d/solver/contact_max_allowed_penetration: float;

#desc Maximum distance a shape can be from another before they are considered separated and the contact is discarded. See [constant PhysicsServer3D.SPACE_PARAM_CONTACT_MAX_SEPARATION].
var physics/3d/solver/contact_max_separation: float;

#desc Maximum distance a pair of bodies has to move before their collision status has to be recalculated. See [constant PhysicsServer3D.SPACE_PARAM_CONTACT_RECYCLE_RADIUS].
var physics/3d/solver/contact_recycle_radius: float;

#desc Default solver bias for all physics contacts. Defines how much bodies react to enforce contact separation. See [constant PhysicsServer3D.SPACE_PARAM_CONTACT_DEFAULT_BIAS].
#desc Individual shapes can have a specific bias value (see [member Shape3D.custom_solver_bias]).
var physics/3d/solver/default_contact_bias: float;

#desc Number of solver iterations for all contacts and constraints. The greater the number of iterations, the more accurate the collisions will be. However, a greater number of iterations requires more CPU power, which can decrease performance. See [constant PhysicsServer3D.SPACE_PARAM_SOLVER_ITERATIONS].
var physics/3d/solver/solver_iterations: int;

#desc Time (in seconds) of inactivity before which a 3D physics body will put to sleep. See [constant PhysicsServer3D.SPACE_PARAM_BODY_TIME_TO_SLEEP].
var physics/3d/time_before_sleep: float;

#desc Enables [member Viewport.physics_object_picking] on the root viewport.
var physics/common/enable_object_picking: bool;

#desc Controls how much physics ticks are synchronized with real time. For 0 or less, the ticks are synchronized. Such values are recommended for network games, where clock synchronization matters. Higher values cause higher deviation of in-game clock and real clock, but allows smoothing out framerate jitters. The default value of 0.5 should be fine for most; values above 2 could cause the game to react to dropped frames with a noticeable delay and are not recommended.
#desc [b]Note:[/b] For best results, when using a custom physics interpolation solution, the physics jitter fix should be disabled by setting [member physics/common/physics_jitter_fix] to [code]0[/code].
#desc [b]Note:[/b] This property is only read when the project starts. To change the physics FPS at runtime, set [member Engine.physics_jitter_fix] instead.
var physics/common/physics_jitter_fix: float;

#desc The number of fixed iterations per second. This controls how often physics simulation and [method Node._physics_process] methods are run. See also [member application/run/max_fps].
#desc [b]Note:[/b] This property is only read when the project starts. To change the physics FPS at runtime, set [member Engine.physics_ticks_per_second] instead.
#desc [b]Note:[/b] Only 8 physics ticks may be simulated per rendered frame at most. If more than 8 physics ticks have to be simulated per rendered frame to keep up with rendering, the game will appear to slow down (even if [code]delta[/code] is used consistently in physics calculations). Therefore, it is recommended not to increase [member physics/common/physics_ticks_per_second] above 240. Otherwise, the game will slow down when the rendering framerate goes below 30 FPS.
var physics/common/physics_ticks_per_second: int;

var rendering/2d/sdf/oversize: int;

var rendering/2d/sdf/scale: int;

var rendering/2d/shadow_atlas/size: int;

var rendering/2d/snap/snap_2d_transforms_to_pixel: bool;

var rendering/2d/snap/snap_2d_vertices_to_pixel: bool;

#desc Sets the number of MSAA samples to use for 2D/Canvas rendering (as a power of two). MSAA is used to reduce aliasing around the edges of polygons. A higher MSAA value results in smoother edges but can be significantly slower on some hardware. This has no effect on shader-induced aliasing or texture aliasing.
var rendering/anti_aliasing/quality/msaa_2d: int;

#desc Sets the number of MSAA samples to use for 3D rendering (as a power of two). MSAA is used to reduce aliasing around the edges of polygons. A higher MSAA value results in smoother edges but can be significantly slower on some hardware. See also bilinear scaling 3d [member rendering/scaling_3d/mode] for supersampling, which provides higher quality but is much more expensive. This has no effect on shader-induced aliasing or texture aliasing.
var rendering/anti_aliasing/quality/msaa_3d: int;

#desc Sets the screen-space antialiasing mode for the default screen [Viewport]. Screen-space antialiasing works by selectively blurring edges in a post-process shader. It differs from MSAA which takes multiple coverage samples while rendering objects. Screen-space AA methods are typically faster than MSAA and will smooth out specular aliasing, but tend to make scenes appear blurry. The blurriness is partially counteracted by automatically using a negative mipmap LOD bias (see [member rendering/textures/default_filters/texture_mipmap_bias]).
#desc Another way to combat specular aliasing is to enable [member rendering/anti_aliasing/screen_space_roughness_limiter/enabled].
var rendering/anti_aliasing/quality/screen_space_aa: int;

#desc If [code]true[/code], uses a fast post-processing dithering filter on the default screen [Viewport] to make banding significantly less visible. In some cases, the dithering pattern may be slightly noticable. Note that this will make losslessly compressed (PNG etc.) screenshots larger.
var rendering/anti_aliasing/quality/use_debanding: bool;

#desc Enables Temporal Anti-Aliasing for the default screen [Viewport]. TAA works by jittering the camera and accumulating the images of the last rendered frames, motion vector rendering is used to account for camera and object motion. Enabling TAA can make the image blurrier, which is partially counteracted by automatically using a negative mipmap LOD bias (see [member rendering/textures/default_filters/texture_mipmap_bias]).
#desc [b]Note:[/b] The implementation is not complete yet, some visual instances such as particles and skinned meshes may show artifacts.
var rendering/anti_aliasing/quality/use_taa: bool;

var rendering/anti_aliasing/screen_space_roughness_limiter/amount: float;

var rendering/anti_aliasing/screen_space_roughness_limiter/enabled: bool;

var rendering/anti_aliasing/screen_space_roughness_limiter/limit: float;

#desc Sets the quality of the depth of field effect. Higher quality takes more samples, which is slower but looks smoother.
var rendering/camera/depth_of_field/depth_of_field_bokeh_quality: int;

#desc Sets the depth of field shape. Can be Box, Hexagon, or Circle. Box is the fastest. Circle is the most realistic, but also the most expensive to compute.
var rendering/camera/depth_of_field/depth_of_field_bokeh_shape: int;

#desc If [code]true[/code], jitters DOF samples to make effect slightly blurrier and hide lines created from low sample rates. This can result in a slightly grainy appearance when used with a low number of samples.
var rendering/camera/depth_of_field/depth_of_field_use_jitter: bool;

#desc Disables [member rendering/driver/depth_prepass/enable] conditionally for certain vendors. By default, disables the depth prepass for mobile devices as mobile devices do not benefit from the depth prepass due to their unique architecture.
var rendering/driver/depth_prepass/disable_for_vendors: String;

#desc If [code]true[/code], performs a previous depth pass before rendering 3D materials. This increases performance significantly in scenes with high overdraw, when complex materials and lighting are used. However, in scenes with few occluded surfaces, the depth prepass may reduce performance. If your game is viewed from a fixed angle that makes it easy to avoid overdraw (such as top-down or side-scrolling perspective), consider disabling the depth prepass to improve performance. This setting can be changed at run-time to optimize performance depending on the scene currently being viewed.
#desc [b]Note:[/b] Only supported when using the Vulkan Clustered backend or the OpenGL backend. When using Vulkan Mobile there is no depth prepass performed.
var rendering/driver/depth_prepass/enable: bool;

#desc Thread model for rendering. Rendering on a thread can vastly improve performance, but synchronizing to the main thread can cause a bit more jitter.
var rendering/driver/threads/thread_model: int;

#desc Default background clear color. Overridable per [Viewport] using its [Environment]. See [member Environment.background_mode] and [member Environment.background_color] in particular. To change this default color programmatically, use [method RenderingServer.set_default_clear_color].
var rendering/environment/defaults/default_clear_color: Color;

#desc [Environment] that will be used as a fallback environment in case a scene does not specify its own environment. The default environment is loaded in at scene load time regardless of whether you have set an environment or not. If you do not rely on the fallback environment, you do not need to set this property.
var rendering/environment/defaults/default_environment: String;

#desc Sets how the glow effect is upscaled before being copied onto the screen. Linear is faster, but looks blocky. Bicubic is slower but looks smooth.
var rendering/environment/glow/upscale_mode: int;

#desc Lower-end override for [member rendering/environment/glow/upscale_mode] on mobile devices, due to performance concerns or driver support.
var rendering/environment/glow/upscale_mode.mobile: int;

#desc Takes more samples during downsample pass of glow. This ensures that single pixels are captured by glow which makes the glow look smoother and more stable during movement. However, it is very expensive and makes the glow post process take twice as long.
var rendering/environment/glow/use_high_quality: bool;

#desc Sets the quality for rough screen-space reflections. Turning off will make all screen space reflections sharp, while higher values make rough reflections look better.
var rendering/environment/screen_space_reflection/roughness_quality: int;

#desc Quality target to use when [member rendering/environment/ssao/quality] is set to [code]ULTRA[/code]. A value of [code]0.0[/code] provides a quality and speed similar to [code]MEDIUM[/code] while a value of [code]1.0[/code] provides much higher quality than any of the other settings at the cost of performance.
var rendering/environment/ssao/adaptive_target: float;

#desc Number of blur passes to use when computing screen-space ambient occlusion. A higher number will result in a smoother look, but will be slower to compute and will have less high-frequency detail.
var rendering/environment/ssao/blur_passes: int;

#desc Distance at which the screen-space ambient occlusion effect starts to fade out. Use this hide ambient occlusion at great distances.
var rendering/environment/ssao/fadeout_from: float;

#desc Distance at which the screen-space ambient occlusion is fully faded out. Use this hide ambient occlusion at great distances.
var rendering/environment/ssao/fadeout_to: float;

#desc If [code]true[/code], screen-space ambient occlusion will be rendered at half size and then upscaled before being added to the scene. This is significantly faster but may miss small details. If [code]false[/code], screen-space ambient occlusion will be rendered at full size.
var rendering/environment/ssao/half_size: bool;

#desc Sets the quality of the screen-space ambient occlusion effect. Higher values take more samples and so will result in better quality, at the cost of performance. Setting to [code]ULTRA[/code] will use the [member rendering/environment/ssao/adaptive_target] setting.
var rendering/environment/ssao/quality: int;

#desc Quality target to use when [member rendering/environment/ssil/quality] is set to [code]ULTRA[/code]. A value of [code]0.0[/code] provides a quality and speed similar to [code]MEDIUM[/code] while a value of [code]1.0[/code] provides much higher quality than any of the other settings at the cost of performance. When using the adaptive target, the performance cost scales with the complexity of the scene.
var rendering/environment/ssil/adaptive_target: float;

#desc Number of blur passes to use when computing screen-space indirect lighting. A higher number will result in a smoother look, but will be slower to compute and will have less high-frequency detail.
var rendering/environment/ssil/blur_passes: int;

#desc Distance at which the screen-space indirect lighting effect starts to fade out. Use this hide screen-space indirect lighting at great distances.
var rendering/environment/ssil/fadeout_from: float;

#desc Distance at which the screen-space indirect lighting is fully faded out. Use this hide screen-space indirect lighting at great distances.
var rendering/environment/ssil/fadeout_to: float;

#desc If [code]true[/code], screen-space indirect lighting will be rendered at half size and then upscaled before being added to the scene. This is significantly faster but may miss small details and may result in some objects appearing to glow at their edges.
var rendering/environment/ssil/half_size: bool;

#desc Sets the quality of the screen-space indirect lighting effect. Higher values take more samples and so will result in better quality, at the cost of performance. Setting to [code]ULTRA[/code] will use the [member rendering/environment/ssil/adaptive_target] setting.
var rendering/environment/ssil/quality: int;

#desc Scales the depth over which the subsurface scattering effect is applied. A high value may allow light to scatter into a part of the mesh or another mesh that is close in screen space but far in depth.
var rendering/environment/subsurface_scattering/subsurface_scattering_depth_scale: float;

#desc Sets the quality of the subsurface scattering effect. Higher values are slower but look nicer.
var rendering/environment/subsurface_scattering/subsurface_scattering_quality: int;

#desc Scales the distance over which samples are taken for subsurface scattering effect. Changing this does not impact performance, but higher values will result in significant artifacts as the samples will become obviously spread out. A lower value results in a smaller spread of scattered light.
var rendering/environment/subsurface_scattering/subsurface_scattering_scale: float;

#desc Enables filtering of the volumetric fog effect prior to integration. This substantially blurs the fog which reduces fine details but also smooths out harsh edges and aliasing artifacts. Disable when more detail is required.
var rendering/environment/volumetric_fog/use_filter: int;

#desc Number of slices to use along the depth of the froxel buffer for volumetric fog. A lower number will be more efficient but may result in artifacts appearing during camera movement. See also [member Environment.volumetric_fog_length].
var rendering/environment/volumetric_fog/volume_depth: int;

#desc Base size used to determine size of froxel buffer in the camera X-axis and Y-axis. The final size is scaled by the aspect ratio of the screen, so actual values may differ from what is set. Set a larger size for more detailed fog, set a smaller size for better performance.
var rendering/environment/volumetric_fog/volume_size: int;

#desc Sets the driver to be used by the renderer when using the Compatibility renderer. This property can not be edited directly, instead, set the driver using the platform-specific overrides.
var rendering/gl_compatibility/driver: String;

#desc Android override for [member rendering/gl_compatibility/driver].
var rendering/gl_compatibility/driver.android: String;

#desc iOS override for [member rendering/gl_compatibility/driver].
var rendering/gl_compatibility/driver.ios: String;

#desc LinuxBSD override for [member rendering/gl_compatibility/driver].
var rendering/gl_compatibility/driver.linuxbsd: String;

#desc macOS override for [member rendering/gl_compatibility/driver].
var rendering/gl_compatibility/driver.macos: String;

#desc Web override for [member rendering/gl_compatibility/driver].
var rendering/gl_compatibility/driver.web: String;

#desc Windows override for [member rendering/gl_compatibility/driver].
var rendering/gl_compatibility/driver.windows: String;

#desc If [code]true[/code], renders [VoxelGI] and SDFGI ([member Environment.sdfgi_enabled]) buffers at halved resolution (e.g. 960×540 when the viewport size is 1920×1080). This improves performance significantly when VoxelGI or SDFGI is enabled, at the cost of artifacts that may be visible on polygon edges. The loss in quality becomes less noticeable as the viewport resolution increases. [LightmapGI] rendering is not affected by this setting.
#desc [b]Note:[/b] This property is only read when the project starts. To set half-resolution GI at run-time, call [method RenderingServer.gi_set_use_half_resolution] instead.
var rendering/global_illumination/gi/use_half_resolution: bool;

var rendering/global_illumination/sdfgi/frames_to_converge: int;

var rendering/global_illumination/sdfgi/frames_to_update_lights: int;

var rendering/global_illumination/sdfgi/probe_ray_count: int;

var rendering/global_illumination/voxel_gi/quality: int;

#desc The maximum number of rays that can be thrown per pass when baking lightmaps with [LightmapGI]. Depending on the scene, adjusting this value may result in higher GPU utilization when baking lightmaps, leading to faster bake times.
var rendering/lightmapping/bake_performance/max_rays_per_pass: int;

#desc The maximum number of rays that can be thrown per pass when baking dynamic object lighting in [LightmapProbe]s with [LightmapGI]. Depending on the scene, adjusting this value may result in higher GPU utilization when baking lightmaps, leading to faster bake times.
var rendering/lightmapping/bake_performance/max_rays_per_probe_pass: int;

#desc The region size to use when baking lightmaps with [LightmapGI].
var rendering/lightmapping/bake_performance/region_size: int;

#desc The number of rays to use for baking dynamic object lighting in [LightmapProbe]s when [member LightmapGI.quality] is [constant LightmapGI.BAKE_QUALITY_HIGH].
var rendering/lightmapping/bake_quality/high_quality_probe_ray_count: int;

#desc The number of rays to use for baking lightmaps with [LightmapGI] when [member LightmapGI.quality] is [constant LightmapGI.BAKE_QUALITY_HIGH].
var rendering/lightmapping/bake_quality/high_quality_ray_count: int;

#desc The number of rays to use for baking dynamic object lighting in [LightmapProbe]s when [member LightmapGI.quality] is [constant LightmapGI.BAKE_QUALITY_LOW].
var rendering/lightmapping/bake_quality/low_quality_probe_ray_count: int;

#desc The number of rays to use for baking lightmaps with [LightmapGI] when [member LightmapGI.quality] is [constant LightmapGI.BAKE_QUALITY_LOW].
var rendering/lightmapping/bake_quality/low_quality_ray_count: int;

#desc The number of rays to use for baking dynamic object lighting in [LightmapProbe]s when [member LightmapGI.quality] is [constant LightmapGI.BAKE_QUALITY_MEDIUM].
var rendering/lightmapping/bake_quality/medium_quality_probe_ray_count: int;

#desc The number of rays to use for baking lightmaps with [LightmapGI] when [member LightmapGI.quality] is [constant LightmapGI.BAKE_QUALITY_MEDIUM].
var rendering/lightmapping/bake_quality/medium_quality_ray_count: int;

#desc The number of rays to use for baking dynamic object lighting in [LightmapProbe]s when [member LightmapGI.quality] is [constant LightmapGI.BAKE_QUALITY_ULTRA].
var rendering/lightmapping/bake_quality/ultra_quality_probe_ray_count: int;

#desc The number of rays to use for baking lightmaps with [LightmapGI] when [member LightmapGI.quality] is [constant LightmapGI.BAKE_QUALITY_ULTRA].
var rendering/lightmapping/bake_quality/ultra_quality_ray_count: int;

#desc The framerate-independent update speed when representing dynamic object lighting from [LightmapProbe]s. Higher values make dynamic object lighting update faster. Higher values can prevent fast-moving objects from having "outdated" indirect lighting displayed on them, at the cost of possible flickering when an object moves from a bright area to a shaded area.
var rendering/lightmapping/probe_capture/update_speed: float;

#desc Use 16 bits for shadow depth map. Enabling this results in shadows having less precision and may result in shadow acne, but can lead to performance improvements on some devices.
var rendering/lights_and_shadows/directional_shadow/16_bits: bool;

#desc The directional shadow's size in pixels. Higher values will result in sharper shadows, at the cost of performance. The value will be rounded up to the nearest power of 2.
var rendering/lights_and_shadows/directional_shadow/size: int;

#desc Lower-end override for [member rendering/lights_and_shadows/directional_shadow/size] on mobile devices, due to performance concerns or driver support.
var rendering/lights_and_shadows/directional_shadow/size.mobile: int;

#desc Quality setting for shadows cast by [DirectionalLight3D]s. Higher quality settings use more samples when reading from shadow maps and are thus slower. Low quality settings may result in shadows looking grainy.
#desc [b]Note:[/b] The Soft Very Low setting will automatically multiply [i]constant[/i] shadow blur by 0.75x to reduce the amount of noise visible. This automatic blur change only affects the constant blur factor defined in [member Light3D.shadow_blur], not the variable blur performed by [DirectionalLight3D]s' [member Light3D.light_angular_distance].
#desc [b]Note:[/b] The Soft High and Soft Ultra settings will automatically multiply [i]constant[/i] shadow blur by 1.5× and 2× respectively to make better use of the increased sample count. This increased blur also improves stability of dynamic object shadows.
var rendering/lights_and_shadows/directional_shadow/soft_shadow_filter_quality: int;

#desc Lower-end override for [member rendering/lights_and_shadows/directional_shadow/soft_shadow_filter_quality] on mobile devices, due to performance concerns or driver support.
var rendering/lights_and_shadows/directional_shadow/soft_shadow_filter_quality.mobile: int;

#desc Use 16 bits for shadow depth map. Enabling this results in shadows having less precision and may result in shadow acne, but can lead to performance improvements on some devices.
var rendering/lights_and_shadows/positional_shadow/atlas_16_bits: bool;

#desc Subdivision quadrant size for shadow mapping. See shadow mapping documentation.
var rendering/lights_and_shadows/positional_shadow/atlas_quadrant_0_subdiv: int;

#desc Subdivision quadrant size for shadow mapping. See shadow mapping documentation.
var rendering/lights_and_shadows/positional_shadow/atlas_quadrant_1_subdiv: int;

#desc Subdivision quadrant size for shadow mapping. See shadow mapping documentation.
var rendering/lights_and_shadows/positional_shadow/atlas_quadrant_2_subdiv: int;

#desc Subdivision quadrant size for shadow mapping. See shadow mapping documentation.
var rendering/lights_and_shadows/positional_shadow/atlas_quadrant_3_subdiv: int;

#desc Size for shadow atlas (used for OmniLights and SpotLights). See documentation.
var rendering/lights_and_shadows/positional_shadow/atlas_size: int;

#desc Lower-end override for [member rendering/lights_and_shadows/positional_shadow/atlas_size] on mobile devices, due to performance concerns or driver support.
var rendering/lights_and_shadows/positional_shadow/atlas_size.mobile: int;

#desc Quality setting for shadows cast by [OmniLight3D]s and [SpotLight3D]s. Higher quality settings use more samples when reading from shadow maps and are thus slower. Low quality settings may result in shadows looking grainy.
#desc [b]Note:[/b] The Soft Very Low setting will automatically multiply [i]constant[/i] shadow blur by 0.75x to reduce the amount of noise visible. This automatic blur change only affects the constant blur factor defined in [member Light3D.shadow_blur], not the variable blur performed by [DirectionalLight3D]s' [member Light3D.light_angular_distance].
#desc [b]Note:[/b] The Soft High and Soft Ultra settings will automatically multiply shadow blur by 1.5× and 2× respectively to make better use of the increased sample count. This increased blur also improves stability of dynamic object shadows.
var rendering/lights_and_shadows/positional_shadow/soft_shadow_filter_quality: int;

#desc Lower-end override for [member rendering/lights_and_shadows/positional_shadow/soft_shadow_filter_quality] on mobile devices, due to performance concerns or driver support.
var rendering/lights_and_shadows/positional_shadow/soft_shadow_filter_quality.mobile: int;

#desc Enables the use of physically based units for light sources. Physically based units tend to be much larger than the arbitrary units used by Godot, but they can be used to match lighting within Godot to real-world lighting. Due to the large dynamic range of lighting conditions present in nature, Godot bakes exposure into the various lighting quantities before rendering. Most light sources bake exposure automatically at run time based on the active [CameraAttributes] resource, but [LightmapGI] and [VoxelGI] require a [CameraAttributes] resource to be set at bake time to reduce the dynamic range. At run time, Godot will automatically reconcile the baked exposure with the active exposure to ensure lighting remains consistent.
var rendering/lights_and_shadows/use_physical_light_units: bool;

var rendering/limits/cluster_builder/max_clustered_elements: float;

var rendering/limits/forward_renderer/threaded_render_minimum_instances: int;

var rendering/limits/global_shader_variables/buffer_size: int;

#desc Max number of omnilights and spotlights renderable per object. At the default value of 8, this means that each surface can be affected by up to 8 omnilights and 8 spotlights. This is further limited by hardware support and [member rendering/limits/opengl/max_renderable_lights]. Setting this low will slightly reduce memory usage, may decrease shader compile times, and may result in faster rendering on low-end, mobile, or web devices.
var rendering/limits/opengl/max_lights_per_object: int;

#desc Max number of elements renderable in a frame. If more elements than this are visible per frame, they will not be drawn. Keep in mind elements refer to mesh surfaces and not meshes themselves. Setting this low will slightly reduce memory usage and may decrease shader compile times, particularly on web. For most uses, the default value is suitable, but consider lowering as much as possible on web export.
var rendering/limits/opengl/max_renderable_elements: int;

#desc Max number of positional lights renderable in a frame. If more lights than this number are used, they will be ignored. Setting this low will slightly reduce memory usage and may decrease shader compile times, particularly on web. For most uses, the default value is suitable, but consider lowering as much as possible on web export.
var rendering/limits/opengl/max_renderable_lights: int;

var rendering/limits/spatial_indexer/threaded_cull_minimum_instances: int;

var rendering/limits/spatial_indexer/update_iterations_per_frame: int;

var rendering/limits/time/time_rollover_secs: float;

#desc The automatic LOD bias to use for meshes rendered within the [ReflectionProbe]. Higher values will use less detailed versions of meshes that have LOD variations generated. If set to [code]0.0[/code], automatic LOD is disabled. Increase [member rendering/mesh_lod/lod_change/threshold_pixels] to improve performance at the cost of geometry detail.
#desc [b]Note:[/b] [member rendering/mesh_lod/lod_change/threshold_pixels] does not affect [GeometryInstance3D] visibility ranges (also known as "manual" LOD or hierarchical LOD).
#desc [b]Note:[/b] This property is only read when the project starts. To adjust the automatic LOD threshold at runtime, set [member Viewport.mesh_lod_threshold] on the root [Viewport].
var rendering/mesh_lod/lod_change/threshold_pixels: float;

#desc The [url=https://en.wikipedia.org/wiki/Bounding_volume_hierarchy]BVH[/url] quality to use when rendering the occlusion culling buffer. Higher values will result in more accurate occlusion culling, at the cost of higher CPU usage.
var rendering/occlusion_culling/bvh_build_quality: int;

#desc Higher values will result in more accurate occlusion culling, at the cost of higher CPU usage. The occlusion culling buffer's pixel count is roughly equal to [code]occlusion_rays_per_thread * number_of_logical_cpu_cores[/code], so it will depend on the system's CPU. Therefore, CPUs with fewer cores will use a lower resolution to attempt keeping performance costs even across devices.
var rendering/occlusion_culling/occlusion_rays_per_thread: int;

#desc If [code]true[/code], [OccluderInstance3D] nodes will be usable for occlusion culling in 3D in the root viewport. In custom viewports, [member Viewport.use_occlusion_culling] must be set to [code]true[/code] instead.
#desc [b]Note:[/b] Enabling occlusion culling has a cost on the CPU. Only enable occlusion culling if you actually plan to use it. Large open scenes with few or no objects blocking the view will generally not benefit much from occlusion culling. Large open scenes generally benefit more from mesh LOD and visibility ranges ([member GeometryInstance3D.visibility_range_begin] and [member GeometryInstance3D.visibility_range_end]) compared to occlusion culling.
var rendering/occlusion_culling/use_occlusion_culling: bool;

#desc Number of cubemaps to store in the reflection atlas. The number of [ReflectionProbe]s in a scene will be limited by this amount. A higher number requires more VRAM.
var rendering/reflections/reflection_atlas/reflection_count: int;

#desc Size of cubemap faces for [ReflectionProbe]s. A higher number requires more VRAM and may make reflection probe updating slower.
var rendering/reflections/reflection_atlas/reflection_size: int;

#desc Lower-end override for [member rendering/reflections/reflection_atlas/reflection_size] on mobile devices, due to performance concerns or driver support.
var rendering/reflections/reflection_atlas/reflection_size.mobile: int;

#desc Use a higher quality variant of the fast filtering algorithm. Significantly slower than using default quality, but results in smoother reflections. Should only be used when the scene is especially detailed.
var rendering/reflections/sky_reflections/fast_filter_high_quality: bool;

#desc Sets the number of samples to take when using importance sampling for [Sky]s and [ReflectionProbe]s. A higher value will result in smoother, higher quality reflections, but increases time to calculate radiance maps. In general, fewer samples are needed for simpler, low dynamic range environments while more samples are needed for HDR environments and environments with a high level of detail.
var rendering/reflections/sky_reflections/ggx_samples: int;

#desc Lower-end override for [member rendering/reflections/sky_reflections/ggx_samples] on mobile devices, due to performance concerns or driver support.
var rendering/reflections/sky_reflections/ggx_samples.mobile: int;

#desc Limits the number of layers to use in radiance maps when using importance sampling. A lower number will be slightly faster and take up less VRAM.
var rendering/reflections/sky_reflections/roughness_layers: int;

#desc If [code]true[/code], uses texture arrays instead of mipmaps for reflection probes and panorama backgrounds (sky). This reduces jitter noise and upscaling artifacts on reflections, but is significantly slower to compute and uses [member rendering/reflections/sky_reflections/roughness_layers] times more memory.
var rendering/reflections/sky_reflections/texture_array_reflections: bool;

#desc Lower-end override for [member rendering/reflections/sky_reflections/texture_array_reflections] on mobile devices, due to performance concerns or driver support.
var rendering/reflections/sky_reflections/texture_array_reflections.mobile: bool;

#desc Sets the renderer that will be used by the project. Options are:
#desc [b]Forward Plus[/b]: High-end renderer designed for Desktop devices. Has a higher base overhead, but scales well with complex scenes. Not suitable for older devices or mobile.
#desc [b]Mobile[/b]: Modern renderer designed for mobile devices. Has a lower base overhead than Forward Plus, but does not scale as well to large scenes with many elements.
#desc [b]GL Compatibility[/b]: Low-end renderer designed for older devices. Based on the limitations of the OpenGL 3.3/ OpenGL ES 3.0 / WebGL 2 APIs.
var rendering/renderer/rendering_method: String;

#desc Override for [member rendering/renderer/rendering_method] on mobile devices.
var rendering/renderer/rendering_method.mobile: String;

#desc Override for [member rendering/renderer/rendering_method] on web.
var rendering/renderer/rendering_method.web: String;

var rendering/rendering_device/descriptor_pools/max_descriptors_per_pool: int;

#desc Sets the driver to be used by the renderer when using a RenderingDevice-based renderer like the clustered renderer or the mobile renderer. This property can not be edited directly, instead, set the driver using the platform-specific overrides.
var rendering/rendering_device/driver: String;

#desc Android override for [member rendering/rendering_device/driver].
var rendering/rendering_device/driver.android: String;

#desc iOS override for [member rendering/rendering_device/driver].
var rendering/rendering_device/driver.ios: String;

#desc LinuxBSD override for [member rendering/rendering_device/driver].
var rendering/rendering_device/driver.linuxbsd: String;

#desc macOS override for [member rendering/rendering_device/driver].
var rendering/rendering_device/driver.macos: String;

#desc Windows override for [member rendering/rendering_device/driver].
var rendering/rendering_device/driver.windows: String;

var rendering/rendering_device/staging_buffer/block_size_kb: int;

var rendering/rendering_device/staging_buffer/max_size_mb: int;

var rendering/rendering_device/staging_buffer/texture_upload_region_size_px: int;

#desc Determines how sharp the upscaled image will be when using the FSR upscaling mode. Sharpness halves with every whole number. Values go from 0.0 (sharpest) to 2.0. Values above 2.0 won't make a visible difference.
var rendering/scaling_3d/fsr_sharpness: float;

#desc Sets the scaling 3D mode. Bilinear scaling renders at different resolution to either undersample or supersample the viewport. FidelityFX Super Resolution 1.0, abbreviated to FSR, is an upscaling technology that produces high quality images at fast framerates by using a spatially aware upscaling algorithm. FSR is slightly more expensive than bilinear, but it produces significantly higher image quality. FSR should be used where possible.
var rendering/scaling_3d/mode: int;

#desc Scales the 3D render buffer based on the viewport size uses an image filter specified in [member rendering/scaling_3d/mode] to scale the output image to the full viewport size. Values lower than [code]1.0[/code] can be used to speed up 3D rendering at the cost of quality (undersampling). Values greater than [code]1.0[/code] are only valid for bilinear mode and can be used to improve 3D rendering quality at a high performance cost (supersampling). See also [member rendering/anti_aliasing/quality/msaa_3d] for multi-sample antialiasing, which is significantly cheaper but only smooths the edges of polygons.
var rendering/scaling_3d/scale: float;

var rendering/shader_compiler/shader_cache/compress: bool;

#desc Enable the shader cache, which stores compiled shaders to disk to prevent stuttering from shader compilation the next time the shader is needed.
var rendering/shader_compiler/shader_cache/enabled: bool;

var rendering/shader_compiler/shader_cache/strip_debug: bool;

var rendering/shader_compiler/shader_cache/strip_debug.release: bool;

var rendering/shader_compiler/shader_cache/use_zstd_compression: bool;

#desc If [code]true[/code], uses faster but lower-quality Lambert material lighting model instead of Burley.
var rendering/shading/overrides/force_lambert_over_burley: bool;

#desc Lower-end override for [member rendering/shading/overrides/force_lambert_over_burley] on mobile devices, due to performance concerns or driver support.
var rendering/shading/overrides/force_lambert_over_burley.mobile: bool;

#desc If [code]true[/code], forces vertex shading for all rendering. This can increase performance a lot, but also reduces quality immensely. Can be used to optimize performance on low-end mobile devices.
var rendering/shading/overrides/force_vertex_shading: bool;

#desc Lower-end override for [member rendering/shading/overrides/force_vertex_shading] on mobile devices, due to performance concerns or driver support.
var rendering/shading/overrides/force_vertex_shading.mobile: bool;

#desc The filtering quality to use for [Decal] nodes. When using one of the anisotropic filtering modes, the anisotropic filtering level is controlled by [member rendering/textures/default_filters/anisotropic_filtering_level].
var rendering/textures/decals/filter: int;

#desc Sets the maximum number of samples to take when using anisotropic filtering on textures (as a power of two). A higher sample count will result in sharper textures at oblique angles, but is more expensive to compute. A value of [code]0[/code] forcibly disables anisotropic filtering, even on materials where it is enabled.
#desc The anisotropic filtering level also affects decals and light projectors if they are configured to use anisotropic filtering. See [member rendering/textures/decals/filter] and [member rendering/textures/light_projectors/filter].
#desc [b]Note:[/b] For performance reasons, anisotropic filtering [i]is not enabled by default[/i] on 2D and 3D materials. For this setting to have an effect in 3D, set [member BaseMaterial3D.texture_filter] to [constant BaseMaterial3D.TEXTURE_FILTER_LINEAR_WITH_MIPMAPS_ANISOTROPIC] or [constant BaseMaterial3D.TEXTURE_FILTER_NEAREST_WITH_MIPMAPS_ANISOTROPIC] on materials. For this setting to have an effect in 2D, set [member CanvasItem.texture_filter] to [constant CanvasItem.TEXTURE_FILTER_LINEAR_WITH_MIPMAPS_ANISOTROPIC] or [constant CanvasItem.TEXTURE_FILTER_NEAREST_WITH_MIPMAPS_ANISOTROPIC] on the [CanvasItem] node displaying the texture (or in [CanvasTexture]). However, anisotropic filtering is rarely useful in 2D, so only enable it for textures in 2D if it makes a meaningful visual difference.
#desc [b]Note:[/b] This property is only read when the project starts. There is currently no way to change this setting at run-time.
var rendering/textures/default_filters/anisotropic_filtering_level: int;

#desc Affects the final texture sharpness by reading from a lower or higher mipmap (also called "texture LOD bias"). Negative values make mipmapped textures sharper but grainier when viewed at a distance, while positive values make mipmapped textures blurrier (even when up close).
#desc Enabling temporal antialiasing ([member rendering/anti_aliasing/quality/use_taa]) will automatically apply a [code]-0.5[/code] offset to this value, while enabling FXAA ([member rendering/anti_aliasing/quality/screen_space_aa]) will automatically apply a [code]-0.25[/code] offset to this value. If both TAA and FXAA are enbled at the same time, an offset of [code]-0.75[/code] is applied to this value.
#desc [b]Note:[/b] If [member rendering/scaling_3d/scale] is lower than [code]1.0[/code] (exclusive), [member rendering/textures/default_filters/texture_mipmap_bias] is used to adjust the automatic mipmap bias which is calculated internally based on the scale factor. The formula for this is [code]log2(scaling_3d_scale) + mipmap_bias[/code].
#desc [b]Note:[/b] This property is only read when the project starts. To change the mipmap LOD bias at run-time, set [member Viewport.texture_mipmap_bias] instead.
var rendering/textures/default_filters/texture_mipmap_bias: float;

#desc If [code]true[/code], uses nearest-neighbor mipmap filtering when using mipmaps (also called "bilinear filtering"), which will result in visible seams appearing between mipmap stages. This may increase performance in mobile as less memory bandwidth is used. If [code]false[/code], linear mipmap filtering (also called "trilinear filtering") is used.
#desc [b]Note:[/b] This property is only read when the project starts. There is currently no way to change this setting at run-time.
var rendering/textures/default_filters/use_nearest_mipmap_filter: bool;

#desc The filtering quality to use for [OmniLight3D] and [SpotLight3D] projectors. When using one of the anisotropic filtering modes, the anisotropic filtering level is controlled by [member rendering/textures/default_filters/anisotropic_filtering_level].
var rendering/textures/light_projectors/filter: int;

#desc If [code]true[/code], the texture importer will import lossless textures using the PNG format. Otherwise, it will default to using WebP.
var rendering/textures/lossless_compression/force_png: bool;

#desc The default compression level for lossless WebP. Higher levels result in smaller files at the cost of compression speed. Decompression speed is mostly unaffected by the compression level. Supported values are 0 to 9. Note that compression levels above 6 are very slow and offer very little savings.
var rendering/textures/lossless_compression/webp_compression_level: int;

#desc If [code]true[/code], the texture importer will import VRAM-compressed textures using the BPTC algorithm. This texture compression algorithm is only supported on desktop platforms, and only when using the Vulkan renderer.
#desc [b]Note:[/b] Changing this setting does [i]not[/i] impact textures that were already imported before. To make this setting apply to textures that were already imported, exit the editor, remove the [code].godot/imported/[/code] folder located inside the project folder then restart the editor (see [member application/config/use_hidden_project_data_directory]).
var rendering/textures/vram_compression/import_bptc: bool;

#desc If [code]true[/code], the texture importer will import VRAM-compressed textures using the Ericsson Texture Compression algorithm. This algorithm doesn't support alpha channels in textures.
#desc [b]Note:[/b] Changing this setting does [i]not[/i] impact textures that were already imported before. To make this setting apply to textures that were already imported, exit the editor, remove the [code].godot/imported/[/code] folder located inside the project folder then restart the editor (see [member application/config/use_hidden_project_data_directory]).
var rendering/textures/vram_compression/import_etc: bool;

#desc If [code]true[/code], the texture importer will import VRAM-compressed textures using the Ericsson Texture Compression 2 algorithm. This texture compression algorithm is only supported when using the Vulkan renderer.
#desc [b]Note:[/b] Changing this setting does [i]not[/i] impact textures that were already imported before. To make this setting apply to textures that were already imported, exit the editor, remove the [code].godot/imported/[/code] folder located inside the project folder then restart the editor (see [member application/config/use_hidden_project_data_directory]).
var rendering/textures/vram_compression/import_etc2: bool;

#desc If [code]true[/code], the texture importer will import VRAM-compressed textures using the S3 Texture Compression algorithm. This algorithm is only supported on desktop platforms and consoles.
#desc [b]Note:[/b] Changing this setting does [i]not[/i] impact textures that were already imported before. To make this setting apply to textures that were already imported, exit the editor, remove the [code].godot/imported/[/code] folder located inside the project folder then restart the editor (see [member application/config/use_hidden_project_data_directory]).
var rendering/textures/vram_compression/import_s3tc: bool;

#desc Set the default Variable Rate Shading (VRS) mode for the main viewport. See [member Viewport.vrs_mode] to change this at runtime, and [enum Viewport.VRSMode] for possible values.
var rendering/vrs/mode: int;

#desc If [member rendering/vrs/mode] is set to texture, this is the path to default texture loaded as the VRS image.
var rendering/vrs/texture: String;

var threading/worker_pool/low_priority_thread_ratio: float;

var threading/worker_pool/max_threads: int;

var threading/worker_pool/use_system_threads_for_low_priority_tasks: bool;

#desc Action map configuration to load by default.
var xr/openxr/default_action_map: String;

#desc If [code]true[/code] Godot will setup and initialise OpenXR on startup.
var xr/openxr/enabled: bool;

#desc Specify whether OpenXR should be configured for an HMD or a hand held device.
var xr/openxr/form_factor: int;

#desc Specify the default reference space.
var xr/openxr/reference_space: int;

#desc Specify the view configuration with which to configure OpenXR setting up either Mono or Stereo rendering.
var xr/openxr/view_configuration: int;

#desc If [code]true[/code], Godot will compile shaders required for XR.
var xr/shaders/enabled: bool;



#desc Adds a custom property info to a property. The dictionary must contain:
#desc - [code]name[/code]: [String] (the property's name)
#desc - [code]type[/code]: [int] (see [enum Variant.Type])
#desc - optionally [code]hint[/code]: [int] (see [enum PropertyHint]) and [code]hint_string[/code]: [String]
#desc [b]Example:[/b]
#desc [codeblocks]
#desc [gdscript]
#desc ProjectSettings.set("category/property_name", 0)
#desc 
#desc var property_info = {
#desc "name": "category/property_name",
#desc "type": TYPE_INT,
#desc "hint": PROPERTY_HINT_ENUM,
#desc "hint_string": "one,two,three"
#desc }
#desc 
#desc ProjectSettings.add_property_info(property_info)
#desc [/gdscript]
#desc [csharp]
#desc ProjectSettings.Singleton.Set("category/property_name", 0);
#desc 
#desc var propertyInfo = new Godot.Collections.Dictionary
#desc {
#desc {"name", "category/propertyName"},
#desc {"type", Variant.Type.Int},
#desc {"hint", PropertyHint.Enum},
#desc {"hint_string", "one,two,three"},
#desc };
#desc 
#desc ProjectSettings.AddPropertyInfo(propertyInfo);
#desc [/csharp]
#desc [/codeblocks]
func add_property_info(hint: Dictionary) -> void:
	pass;

#desc Clears the whole configuration (not recommended, may break things).
func clear(name: String) -> void:
	pass;

#desc Returns the order of a configuration value (influences when saved to the config file).
func get_order(name: String) -> int:
	pass;

#desc Returns the value of a setting.
#desc [b]Example:[/b]
#desc [codeblocks]
#desc [gdscript]
#desc print(ProjectSettings.get_setting("application/config/name"))
#desc [/gdscript]
#desc [csharp]
#desc GD.Print(ProjectSettings.GetSetting("application/config/name"));
#desc [/csharp]
#desc [/codeblocks]
func get_setting(name: String) -> Variant:
	pass;

#desc Returns the absolute, native OS path corresponding to the localized [param path] (starting with [code]res://[/code] or [code]user://[/code]). The returned path will vary depending on the operating system and user preferences. See [url=$DOCS_URL/tutorials/io/data_paths.html]File paths in Godot projects[/url] to see what those paths convert to. See also [method localize_path].
#desc [b]Note:[/b] [method globalize_path] with [code]res://[/code] will not work in an exported project. Instead, prepend the executable's base directory to the path when running from an exported project:
#desc [codeblock]
#desc var path = ""
#desc if OS.has_feature("editor"):
#desc # Running from an editor binary.
#desc # `path` will contain the absolute path to `hello.txt` located in the project root.
#desc path = ProjectSettings.globalize_path("res://hello.txt")
#desc else:
#desc # Running from an exported project.
#desc # `path` will contain the absolute path to `hello.txt` next to the executable.
#desc # This is *not* identical to using `ProjectSettings.globalize_path()` with a `res://` path,
#desc # but is close enough in spirit.
#desc path = OS.get_executable_path().get_base_dir().path_join("hello.txt")
#desc [/codeblock]
func globalize_path(path: String) -> String:
	pass;

#desc Returns [code]true[/code] if a configuration value is present.
func has_setting(name: String) -> bool:
	pass;

#desc Loads the contents of the .pck or .zip file specified by [param pack] into the resource filesystem ([code]res://[/code]). Returns [code]true[/code] on success.
#desc [b]Note:[/b] If a file from [param pack] shares the same path as a file already in the resource filesystem, any attempts to load that file will use the file from [param pack] unless [param replace_files] is set to [code]false[/code].
#desc [b]Note:[/b] The optional [param offset] parameter can be used to specify the offset in bytes to the start of the resource pack. This is only supported for .pck files.
func load_resource_pack(pack: String, replace_files: bool, offset: int) -> bool:
	pass;

#desc Returns the localized path (starting with [code]res://[/code]) corresponding to the absolute, native OS [param path]. See also [method globalize_path].
func localize_path(path: String) -> String:
	pass;

#desc Saves the configuration to the [code]project.godot[/code] file.
#desc [b]Note:[/b] This method is intended to be used by editor plugins, as modified [ProjectSettings] can't be loaded back in the running app. If you want to change project settings in exported projects, use [method save_custom] to save [code]override.cfg[/code] file.
func save() -> int:
	pass;

#desc Saves the configuration to a custom file. The file extension must be [code].godot[/code] (to save in text-based [ConfigFile] format) or [code].binary[/code] (to save in binary format). You can also save [code]override.cfg[/code] file, which is also text, but can be used in exported projects unlike other formats.
func save_custom(file: String) -> int:
	pass;

#desc Sets the specified property's initial value. This is the value the property reverts to.
func set_initial_value(name: String, value: Variant) -> void:
	pass;

#desc Sets the order of a configuration value (influences when saved to the config file).
func set_order(name: String, position: int) -> void:
	pass;

#desc Sets whether a setting requires restarting the editor to properly take effect.
#desc [b]Note:[/b] This is just a hint to display to the user that the editor must be restarted for changes to take effect. Enabling [method set_restart_if_changed] does [i]not[/i] delay the setting being set when changed.
func set_restart_if_changed(name: String, restart: bool) -> void:
	pass;

#desc Sets the value of a setting.
#desc [b]Example:[/b]
#desc [codeblocks]
#desc [gdscript]
#desc ProjectSettings.set_setting("application/config/name", "Example")
#desc [/gdscript]
#desc [csharp]
#desc ProjectSettings.SetSetting("application/config/name", "Example");
#desc [/csharp]
#desc [/codeblocks]
#desc This can also be used to erase custom project settings. To do this change the setting value to [code]null[/code].
func set_setting(name: String, value: Variant) -> void:
	pass;


