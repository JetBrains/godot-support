#brief Operating System functions.
#desc Operating System functions. OS wraps the most common functionality to communicate with the host operating system, such as the clipboard, video driver, date and time, timers, environment variables, execution of binaries, command line, etc.
class_name OS

#desc The Vulkan rendering backend. It requires Vulkan 1.0 support and automatically uses features from Vulkan 1.1 and 1.2 if available.
const VIDEO_DRIVER_VULKAN = 0;

#desc The OpenGL 3 rendering backend. It uses OpenGL 3.3 Core Profile on desktop platforms, OpenGL ES 3.0 on mobile devices, and WebGL 2.0 on Web.
const VIDEO_DRIVER_OPENGL_3 = 1;

#desc Sunday.
const DAY_SUNDAY = 0;

#desc Monday.
const DAY_MONDAY = 1;

#desc Tuesday.
const DAY_TUESDAY = 2;

#desc Wednesday.
const DAY_WEDNESDAY = 3;

#desc Thursday.
const DAY_THURSDAY = 4;

#desc Friday.
const DAY_FRIDAY = 5;

#desc Saturday.
const DAY_SATURDAY = 6;

#desc January.
const MONTH_JANUARY = 1;

#desc February.
const MONTH_FEBRUARY = 2;

#desc March.
const MONTH_MARCH = 3;

#desc April.
const MONTH_APRIL = 4;

#desc May.
const MONTH_MAY = 5;

#desc June.
const MONTH_JUNE = 6;

#desc July.
const MONTH_JULY = 7;

#desc August.
const MONTH_AUGUST = 8;

#desc September.
const MONTH_SEPTEMBER = 9;

#desc October.
const MONTH_OCTOBER = 10;

#desc November.
const MONTH_NOVEMBER = 11;

#desc December.
const MONTH_DECEMBER = 12;

#desc Desktop directory path.
const SYSTEM_DIR_DESKTOP = 0;

#desc DCIM (Digital Camera Images) directory path.
const SYSTEM_DIR_DCIM = 1;

#desc Documents directory path.
const SYSTEM_DIR_DOCUMENTS = 2;

#desc Downloads directory path.
const SYSTEM_DIR_DOWNLOADS = 3;

#desc Movies directory path.
const SYSTEM_DIR_MOVIES = 4;

#desc Music directory path.
const SYSTEM_DIR_MUSIC = 5;

#desc Pictures directory path.
const SYSTEM_DIR_PICTURES = 6;

#desc Ringtones directory path.
const SYSTEM_DIR_RINGTONES = 7;


#desc If [code]true[/code], the engine optimizes for low processor usage by only refreshing the screen if needed. Can improve battery consumption on mobile.
var low_processor_usage_mode: bool;

#desc The amount of sleeping between frames when the low-processor usage mode is enabled (in microseconds). Higher values will result in lower CPU usage.
var low_processor_usage_mode_sleep_usec: int;



#desc Displays a modal dialog box using the host OS' facilities. Execution is blocked until the dialog is closed.
func alert(text: String, title: String) -> void:
	pass;

#desc Shuts down system MIDI driver.
#desc [b]Note:[/b] This method is implemented on Linux, macOS and Windows.
func close_midi_inputs() -> void:
	pass;

#desc Crashes the engine (or the editor if called within a [code]@tool[/code] script). This should [i]only[/i] be used for testing the system's crash handler, not for any other purpose. For general error reporting, use (in order of preference) [method @GDScript.assert], [method @GlobalScope.push_error] or [method alert]. See also [method kill].
func crash() -> void:
	pass;

#desc Creates a new instance of Godot that runs independently. The [param arguments] are used in the given order and separated by a space.
#desc If the process creation succeeds, the method will return the new process ID, which you can use to monitor the process (and potentially terminate it with [method kill]). If the process creation fails, the method will return [code]-1[/code].
#desc [b]Note:[/b] This method is implemented on Android, iOS, Linux, macOS and Windows.
func create_instance() -> int:
	pass;

#desc Creates a new process that runs independently of Godot. It will not terminate if Godot terminates. The path specified in [param path] must exist and be executable file or macOS .app bundle. Platform path resolution will be used. The [param arguments] are used in the given order and separated by a space.
#desc On Windows, if [param open_console] is [code]true[/code] and the process is a console app, a new terminal window will be opened. This is ignored on other platforms.
#desc If the process creation succeeds, the method will return the new process ID, which you can use to monitor the process (and potentially terminate it with [method kill]). If the process creation fails, the method will return [code]-1[/code].
#desc For example, running another instance of the project:
#desc [codeblocks]
#desc [gdscript]
#desc var pid = OS.create_process(OS.get_executable_path(), [])
#desc [/gdscript]
#desc [csharp]
#desc var pid = OS.CreateProcess(OS.GetExecutablePath(), new string[] {});
#desc [/csharp]
#desc [/codeblocks]
#desc See [method execute] if you wish to run an external command and retrieve the results.
#desc [b]Note:[/b] This method is implemented on Android, iOS, Linux, macOS and Windows.
#desc [b]Note:[/b] On macOS, sandboxed applications are limited to run only embedded helper executables, specified during export or system .app bundle, system .app bundles will ignore arguments.
func create_process(path: String, arguments: PackedStringArray, open_console: bool) -> int:
	pass;

#desc Delays execution of the current thread by [param msec] milliseconds. [param msec] must be greater than or equal to [code]0[/code]. Otherwise, [method delay_msec] will do nothing and will print an error message.
#desc [b]Note:[/b] [method delay_msec] is a [i]blocking[/i] way to delay code execution. To delay code execution in a non-blocking way, see [method SceneTree.create_timer]. Awaiting with [method SceneTree.create_timer] will delay the execution of code placed below the [code]await[/code] without affecting the rest of the project (or editor, for [EditorPlugin]s and [EditorScript]s).
#desc [b]Note:[/b] When [method delay_msec] is called on the main thread, it will freeze the project and will prevent it from redrawing and registering input until the delay has passed. When using [method delay_msec] as part of an [EditorPlugin] or [EditorScript], it will freeze the editor but won't freeze the project if it is currently running (since the project is an independent child process).
func delay_msec() -> void:
	pass;

#desc Delays execution of the current thread by [param usec] microseconds. [param usec] must be greater than or equal to [code]0[/code]. Otherwise, [method delay_usec] will do nothing and will print an error message.
#desc [b]Note:[/b] [method delay_usec] is a [i]blocking[/i] way to delay code execution. To delay code execution in a non-blocking way, see [method SceneTree.create_timer]. Awaiting with [method SceneTree.create_timer] will delay the execution of code placed below the [code]await[/code] without affecting the rest of the project (or editor, for [EditorPlugin]s and [EditorScript]s).
#desc [b]Note:[/b] When [method delay_usec] is called on the main thread, it will freeze the project and will prevent it from redrawing and registering input until the delay has passed. When using [method delay_usec] as part of an [EditorPlugin] or [EditorScript], it will freeze the editor but won't freeze the project if it is currently running (since the project is an independent child process).
func delay_usec() -> void:
	pass;

#desc Executes a command. The file specified in [param path] must exist and be executable. Platform path resolution will be used. The [param arguments] are used in the given order and separated by a space. If an [param output] [Array] is provided, the complete shell output of the process will be appended as a single [String] element in [param output]. If [param read_stderr] is [code]true[/code], the output to the standard error stream will be included too.
#desc On Windows, if [param open_console] is [code]true[/code] and the process is a console app, a new terminal window will be opened. This is ignored on other platforms.
#desc If the command is successfully executed, the method will return the exit code of the command, or [code]-1[/code] if it fails.
#desc [b]Note:[/b] The Godot thread will pause its execution until the executed command terminates. Use [Thread] to create a separate thread that will not pause the Godot thread, or use [method create_process] to create a completely independent process.
#desc For example, to retrieve a list of the working directory's contents:
#desc [codeblocks]
#desc [gdscript]
#desc var output = []
#desc var exit_code = OS.execute("ls", ["-l", "/tmp"], output)
#desc [/gdscript]
#desc [csharp]
#desc var output = new Godot.Collections.Array();
#desc int exitCode = OS.Execute("ls", new string[] {"-l", "/tmp"}, output);
#desc [/csharp]
#desc [/codeblocks]
#desc If you wish to access a shell built-in or execute a composite command, a platform-specific shell can be invoked. For example:
#desc [codeblocks]
#desc [gdscript]
#desc var output = []
#desc OS.execute("CMD.exe", ["/C", "cd %TEMP% && dir"], output)
#desc [/gdscript]
#desc [csharp]
#desc var output = new Godot.Collections.Array();
#desc OS.Execute("CMD.exe", new string[] {"/C", "cd %TEMP% && dir"}, output);
#desc [/csharp]
#desc [/codeblocks]
#desc [b]Note:[/b] This method is implemented on Android, iOS, Linux, macOS and Windows.
#desc [b]Note:[/b] To execute a Windows command interpreter built-in command, specify [code]cmd.exe[/code] in [param path], [code]/c[/code] as the first argument, and the desired command as the second argument.
#desc [b]Note:[/b] To execute a PowerShell built-in command, specify [code]powershell.exe[/code] in [param path], [code]-Command[/code] as the first argument, and the desired command as the second argument.
#desc [b]Note:[/b] To execute a Unix shell built-in command, specify shell executable name in [param path], [code]-c[/code] as the first argument, and the desired command as the second argument.
#desc [b]Note:[/b] On macOS, sandboxed applications are limited to run only embedded helper executables, specified during export.
func execute(path: String, arguments: PackedStringArray, output: Array, read_stderr: bool, open_console: bool) -> int:
	pass;

#desc Returns the keycode of the given string (e.g. "Escape").
func find_keycode_from_string() -> int:
	pass;

#desc Returns the [i]global[/i] cache data directory according to the operating system's standards. On desktop platforms, this path can be overridden by setting the [code]XDG_CACHE_HOME[/code] environment variable before starting the project. See [url=$DOCS_URL/tutorials/io/data_paths.html]File paths in Godot projects[/url] in the documentation for more information. See also [method get_config_dir] and [method get_data_dir].
#desc Not to be confused with [method get_user_data_dir], which returns the [i]project-specific[/i] user data path.
func get_cache_dir() -> String:
	pass;

#desc Returns the command-line arguments passed to the engine.
#desc Command-line arguments can be written in any form, including both [code]--key value[/code] and [code]--key=value[/code] forms so they can be properly parsed, as long as custom command-line arguments do not conflict with engine arguments.
#desc You can also incorporate environment variables using the [method get_environment] method.
#desc You can set [member ProjectSettings.editor/run/main_run_args] to define command-line arguments to be passed by the editor when running the project.
#desc Here's a minimal example on how to parse command-line arguments into a dictionary using the [code]--key=value[/code] form for arguments:
#desc [codeblocks]
#desc [gdscript]
#desc var arguments = {}
#desc for argument in OS.get_cmdline_args():
#desc if argument.find("=") > -1:
#desc var key_value = argument.split("=")
#desc arguments[key_value[0].lstrip("--")] = key_value[1]
#desc else:
#desc # Options without an argument will be present in the dictionary,
#desc # with the value set to an empty string.
#desc arguments[argument.lstrip("--")] = ""
#desc [/gdscript]
#desc [csharp]
#desc var arguments = new Godot.Collections.Dictionary();
#desc foreach (var argument in OS.GetCmdlineArgs())
#desc {
#desc if (argument.Find("=") > -1)
#desc {
#desc string[] keyValue = argument.Split("=");
#desc arguments[keyValue[0].LStrip("--")] = keyValue[1];
#desc }
#desc else
#desc {
#desc // Options without an argument will be present in the dictionary,
#desc // with the value set to an empty string.
#desc arguments[keyValue[0].LStrip("--")] = "";
#desc }
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc [b]Note:[/b] Passing custom user arguments directly is not recommended, as the engine may discard or modify them. Instead, the best way is to use the standard UNIX double dash ([code]--[/code]) and then pass custom arguments, which the engine itself will ignore. These can be read via [method get_cmdline_user_args].
func get_cmdline_args() -> PackedStringArray:
	pass;

#desc Similar to [method get_cmdline_args], but this returns the user arguments (any argument passed after the double dash [code]--[/code] argument). These are left untouched by Godot for the user.
#desc For example, in the command line below, [code]--fullscreen[/code] will not be returned in [method get_cmdline_user_args] and [code]--level 1[/code] will only be returned in [method get_cmdline_user_args]:
#desc [codeblock]
#desc godot --fullscreen -- --level 1
#desc [/codeblock]
func get_cmdline_user_args() -> PackedStringArray:
	pass;

#desc Returns the [i]global[/i] user configuration directory according to the operating system's standards. On desktop platforms, this path can be overridden by setting the [code]XDG_CONFIG_HOME[/code] environment variable before starting the project. See [url=$DOCS_URL/tutorials/io/data_paths.html]File paths in Godot projects[/url] in the documentation for more information. See also [method get_cache_dir] and [method get_data_dir].
#desc Not to be confused with [method get_user_data_dir], which returns the [i]project-specific[/i] user data path.
func get_config_dir() -> String:
	pass;

#desc Returns an array of MIDI device names.
#desc The returned array will be empty if the system MIDI driver has not previously been initialised with [method open_midi_inputs].
#desc [b]Note:[/b] This method is implemented on Linux, macOS and Windows.
func get_connected_midi_inputs() -> PackedStringArray:
	pass;

#desc Returns the [i]global[/i] user data directory according to the operating system's standards. On desktop platforms, this path can be overridden by setting the [code]XDG_DATA_HOME[/code] environment variable before starting the project. See [url=$DOCS_URL/tutorials/io/data_paths.html]File paths in Godot projects[/url] in the documentation for more information. See also [method get_cache_dir] and [method get_config_dir].
#desc Not to be confused with [method get_user_data_dir], which returns the [i]project-specific[/i] user data path.
func get_data_dir() -> String:
	pass;

#desc Returns the name of the distribution for Linux and BSD platforms (e.g. Ubuntu, Manjaro, OpenBSD, etc.).
#desc Returns the same value as [method get_name] for stock Android ROMs, but attempts to return the custom ROM name for popular Android derivatives such as LineageOS.
#desc Returns the same value as [method get_name] for other platforms.
#desc [b]Note:[/b] This method is not supported on the web platform. It returns an empty string.
func get_distribution_name() -> String:
	pass;

#desc Returns the value of an environment variable. Returns an empty string if the environment variable doesn't exist.
#desc [b]Note:[/b] Double-check the casing of [param variable]. Environment variable names are case-sensitive on all platforms except Windows.
func get_environment() -> String:
	pass;

#desc Returns the path to the current engine executable.
#desc [b]Note:[/b] On macOS, always use [method create_instance] instead of relying on executable path.
func get_executable_path() -> String:
	pass;

#desc With this function, you can get the list of dangerous permissions that have been granted to the Android application.
#desc [b]Note:[/b] This method is implemented on Android.
func get_granted_permissions() -> PackedStringArray:
	pass;

#desc Returns the given keycode as a string (e.g. Return values: [code]"Escape"[/code], [code]"Shift+Escape"[/code]).
#desc See also [member InputEventKey.keycode] and [method InputEventKey.get_keycode_with_modifiers].
func get_keycode_string() -> String:
	pass;

#desc Returns the host OS locale as a string of the form [code]language_Script_COUNTRY_VARIANT@extra[/code]. If you want only the language code and not the fully specified locale from the OS, you can use [method get_locale_language].
#desc [code]language[/code] - 2 or 3-letter [url=https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes]language code[/url], in lower case.
#desc [code]Script[/code] - optional, 4-letter [url=https://en.wikipedia.org/wiki/ISO_15924]script code[/url], in title case.
#desc [code]COUNTRY[/code] - optional, 2 or 3-letter [url=https://en.wikipedia.org/wiki/ISO_3166-1]country code[/url], in upper case.
#desc [code]VARIANT[/code] - optional, language variant, region and sort order. Variant can have any number of underscored keywords.
#desc [code]extra[/code] - optional, semicolon separated list of additional key words. Currency, calendar, sort order and numbering system information.
func get_locale() -> String:
	pass;

#desc Returns the host OS locale's 2 or 3-letter [url=https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes]language code[/url] as a string which should be consistent on all platforms. This is equivalent to extracting the [code]language[/code] part of the [method get_locale] string.
#desc This can be used to narrow down fully specified locale strings to only the "common" language code, when you don't need the additional information about country code or variants. For example, for a French Canadian user with [code]fr_CA[/code] locale, this would return [code]fr[/code].
func get_locale_language() -> String:
	pass;

#desc Returns the ID of the main thread. See [method get_thread_caller_id].
#desc [b]Note:[/b] Thread IDs are not deterministic and may be reused across application restarts.
func get_main_thread_id() -> int:
	pass;

#desc Returns the model name of the current device.
#desc [b]Note:[/b] This method is implemented on Android and iOS. Returns [code]"GenericDevice"[/code] on unsupported platforms.
func get_model_name() -> String:
	pass;

#desc Returns the name of the host OS.
#desc On Windows, this is [code]"Windows"[/code] or [code]"UWP"[/code] if exported on Universal Windows Platform.
#desc On macOS, this is [code]"macOS"[/code].
#desc On Linux-based operating systems, this is [code]"Linux"[/code].
#desc On BSD-based operating systems, this is [code]"FreeBSD"[/code], [code]"NetBSD"[/code], [code]"OpenBSD"[/code], or [code]"BSD"[/code] as a fallback.
#desc On Android, this is [code]"Android"[/code].
#desc On iOS, this is [code]"iOS"[/code].
#desc On the web, this is [code]"Web"[/code].
#desc [b]Note:[/b] Custom builds of the engine may support additional platforms, such as consoles, yielding other return values.
#desc [codeblocks]
#desc [gdscript]
#desc match OS.get_name():
#desc "Windows", "UWP":
#desc print("Windows")
#desc "macOS":
#desc print("macOS")
#desc "Linux", "FreeBSD", "NetBSD", "OpenBSD", "BSD":
#desc print("Linux/BSD")
#desc "Android":
#desc print("Android")
#desc "iOS":
#desc print("iOS")
#desc "Web":
#desc print("Web")
#desc [/gdscript]
#desc [csharp]
#desc switch (OS.GetName())
#desc {
#desc case "Windows":
#desc case "UWP":
#desc GD.Print("Windows");
#desc break;
#desc case "macOS":
#desc GD.Print("macOS");
#desc break;
#desc case "Linux":
#desc case "FreeBSD":
#desc case "NetBSD":
#desc case "OpenBSD"
#desc case "BSD":
#desc GD.Print("Linux/BSD");
#desc break;
#desc case "Android":
#desc GD.Print("Android");
#desc break;
#desc case "iOS":
#desc GD.Print("iOS");
#desc break;
#desc case "Web":
#desc GD.Print("Web");
#desc break;
#desc }
#desc [/csharp]
#desc [/codeblocks]
func get_name() -> String:
	pass;

#desc Returns the project's process ID.
#desc [b]Note:[/b] This method is implemented on Android, iOS, Linux, macOS and Windows.
func get_process_id() -> int:
	pass;

#desc Returns the number of [i]logical[/i] CPU cores available on the host machine. On CPUs with HyperThreading enabled, this number will be greater than the number of [i]physical[/i] CPU cores.
func get_processor_count() -> int:
	pass;

#desc Returns the name of the CPU model on the host machine (e.g. "Intel(R) Core(TM) i7-6700K CPU @ 4.00GHz").
#desc [b]Note:[/b] This method is only implemented on Windows, macOS, Linux and iOS. On Android, Web and UWP, [method get_processor_name] returns an empty string.
func get_processor_name() -> String:
	pass;

#desc Returns the list of command line arguments that will be used when the project automatically restarts using [method set_restart_on_exit]. See also [method is_restart_on_exit_set].
func get_restart_on_exit_arguments() -> PackedStringArray:
	pass;

#desc Returns the maximum amount of static memory used (only works in debug).
func get_static_memory_peak_usage() -> int:
	pass;

#desc Returns the amount of static memory being used by the program in bytes (only works in debug).
func get_static_memory_usage() -> int:
	pass;

#desc Returns the actual path to commonly used folders across different platforms. Available locations are specified in [enum SystemDir].
#desc [b]Note:[/b] This method is implemented on Android, Linux, macOS and Windows.
#desc [b]Note:[/b] Shared storage is implemented on Android and allows to differentiate between app specific and shared directories. Shared directories have additional restrictions on Android.
func get_system_dir(dir: int, shared_storage: bool) -> String:
	pass;

#desc Returns path to the system font file with [param font_name] and style. Return empty string if no matching fonts found.
#desc [b]Note:[/b] This method is implemented on iOS, Linux, macOS and Windows.
func get_system_font_path(font_name: String, bold: bool, italic: bool) -> String:
	pass;

#desc Returns list of font family names available.
#desc [b]Note:[/b] This method is implemented on iOS, Linux, macOS and Windows.
func get_system_fonts() -> PackedStringArray:
	pass;

#desc Returns the ID of the current thread. This can be used in logs to ease debugging of multi-threaded applications.
#desc [b]Note:[/b] Thread IDs are not deterministic and may be reused across application restarts.
func get_thread_caller_id() -> int:
	pass;

#desc Returns a string that is unique to the device.
#desc [b]Note:[/b] This string may change without notice if the user reinstalls/upgrades their operating system or changes their hardware. This means it should generally not be used to encrypt persistent data as the data saved before an unexpected ID change would become inaccessible. The returned string may also be falsified using external programs, so do not rely on the string returned by [method get_unique_id] for security purposes.
#desc [b]Note:[/b] Returns an empty string on Web and UWP, as this method isn't implemented on those platforms yet.
func get_unique_id() -> String:
	pass;

#desc Returns the absolute directory path where user data is written ([code]user://[/code]).
#desc On Windows, this is [code]%AppData%\Godot\app_userdata\[project_name][/code], or [code]%AppData%\[custom_name][/code] if [code]use_custom_user_dir[/code] is set. [code]%AppData%[/code] expands to [code]%UserProfile%\AppData\Roaming[/code].
#desc On macOS, this is [code]~/Library/Application Support/Godot/app_userdata/[project_name][/code], or [code]~/Library/Application Support/[custom_name][/code] if [code]use_custom_user_dir[/code] is set.
#desc On Linux and BSD, this is [code]~/.local/share/godot/app_userdata/[project_name][/code], or [code]~/.local/share/[custom_name][/code] if [code]use_custom_user_dir[/code] is set.
#desc On Android and iOS, this is a sandboxed directory in either internal or external storage, depending on the user's configuration.
#desc On the web, this is a virtual directory managed by the browser.
#desc If the project name is empty, [code][project_name][/code] falls back to [code][unnamed project][/code].
#desc Not to be confused with [method get_data_dir], which returns the [i]global[/i] (non-project-specific) user home directory.
func get_user_data_dir() -> String:
	pass;

#desc Returns the exact production and build version of the operating system. This is different from the branded version used in marketing. This helps to distinguish between different releases of operating systems, including minor versions, and insider and custom builds.
#desc For Windows, the major and minor version are returned, as well as the build number. For example, the returned string can look like [code]10.0.9926[/code] for a build of Windows 10, and it can look like [code]6.1.7601[/code] for a build of Windows 7 SP1.
#desc For rolling distributions, such as Arch Linux, an empty string is returned.
#desc For macOS and iOS, the major and minor version are returned, as well as the patch number.
#desc For UWP, the device family version is returned.
#desc For Android, the SDK version and the incremental build number are returned. If it's a custom ROM, it attempts to return its version instead.
#desc [b]Note:[/b] This method is not supported on the web platform. It returns an empty string.
func get_version() -> String:
	pass;

#desc Returns [code]true[/code] if the environment variable with the name [param variable] exists.
#desc [b]Note:[/b] Double-check the casing of [param variable]. Environment variable names are case-sensitive on all platforms except Windows.
func has_environment() -> bool:
	pass;

#desc Returns [code]true[/code] if the feature for the given feature tag is supported in the currently running instance, depending on the platform, build, etc. Can be used to check whether you're currently running a debug build, on a certain platform or arch, etc. Refer to the [url=$DOCS_URL/getting_started/workflow/export/feature_tags.html]Feature Tags[/url] documentation for more details.
#desc [b]Note:[/b] Tag names are case-sensitive.
func has_feature() -> bool:
	pass;

#desc Returns [code]true[/code] if the Godot binary used to run the project is a [i]debug[/i] export template, or when running in the editor.
#desc Returns [code]false[/code] if the Godot binary used to run the project is a [i]release[/i] export template.
#desc To check whether the Godot binary used to run the project is an export template (debug or release), use [code]OS.has_feature("standalone")[/code] instead.
func is_debug_build() -> bool:
	pass;

#desc Returns [code]true[/code] if the input keycode corresponds to a Unicode character.
func is_keycode_unicode() -> bool:
	pass;

#desc Returns [code]true[/code] if the child process ID ([param pid]) is still running or [code]false[/code] if it has terminated.
#desc Must be a valid ID generated from [method create_process].
#desc [b]Note:[/b] This method is implemented on Android, iOS, Linux, macOS and Windows.
func is_process_running() -> bool:
	pass;

#desc Returns [code]true[/code] if the project will automatically restart when it exits for any reason, [code]false[/code] otherwise. See also [method set_restart_on_exit] and [method get_restart_on_exit_arguments].
func is_restart_on_exit_set() -> bool:
	pass;

#desc Returns [code]true[/code] if the engine was executed with the [code]--verbose[/code] or [code]-v[/code] command line argument, or if [member ProjectSettings.debug/settings/stdout/verbose_stdout] is [code]true[/code]. See also [method @GlobalScope.print_verbose].
func is_stdout_verbose() -> bool:
	pass;

#desc If [code]true[/code], the [code]user://[/code] file system is persistent, so that its state is the same after a player quits and starts the game again. Relevant to the Web platform, where this persistence may be unavailable.
func is_userfs_persistent() -> bool:
	pass;

#desc Kill (terminate) the process identified by the given process ID ([param pid]), e.g. the one returned by [method execute] in non-blocking mode. See also [method crash].
#desc [b]Note:[/b] This method can also be used to kill processes that were not spawned by the game.
#desc [b]Note:[/b] This method is implemented on Android, iOS, Linux, macOS and Windows.
func kill() -> int:
	pass;

#desc Moves the file or directory to the system's recycle bin. See also [method DirAccess.remove].
#desc The method takes only global paths, so you may need to use [method ProjectSettings.globalize_path]. Do not use it for files in [code]res://[/code] as it will not work in exported projects.
#desc [b]Note:[/b] If the user has disabled the recycle bin on their system, the file will be permanently deleted instead.
#desc [codeblocks]
#desc [gdscript]
#desc var file_to_remove = "user://slot1.sav"
#desc OS.move_to_trash(ProjectSettings.globalize_path(file_to_remove))
#desc [/gdscript]
#desc [csharp]
#desc var fileToRemove = "user://slot1.sav";
#desc OS.MoveToTrash(ProjectSettings.GlobalizePath(fileToRemove));
#desc [/csharp]
#desc [/codeblocks]
func move_to_trash() -> int:
	pass;

#desc Initialises the singleton for the system MIDI driver.
#desc [b]Note:[/b] This method is implemented on Linux, macOS and Windows.
func open_midi_inputs() -> void:
	pass;

#desc Reads a user input string from the standard input (usually the terminal).
#desc [b]Note:[/b] This method is implemented on Linux, macOS and Windows. Non-blocking reads are not currently supported on any platform.
func read_string_from_stdin() -> String:
	pass;

#desc At the moment this function is only used by [code]AudioDriverOpenSL[/code] to request permission for [code]RECORD_AUDIO[/code] on Android.
func request_permission() -> bool:
	pass;

#desc With this function, you can request dangerous permissions since normal permissions are automatically granted at install time in Android applications.
#desc [b]Note:[/b] This method is implemented on Android.
func request_permissions() -> bool:
	pass;

#desc Sets the value of the environment variable [param variable] to [param value]. The environment variable will be set for the Godot process and any process executed with [method execute] after running [method set_environment]. The environment variable will [i]not[/i] persist to processes run after the Godot process was terminated.
#desc [b]Note:[/b] Double-check the casing of [param variable]. Environment variable names are case-sensitive on all platforms except Windows.
func set_environment(variable: String, value: String) -> bool:
	pass;

#desc If [param restart] is [code]true[/code], restarts the project automatically when it is exited with [method SceneTree.quit] or [constant Node.NOTIFICATION_WM_CLOSE_REQUEST]. Command line [param arguments] can be supplied. To restart the project with the same command line arguments as originally used to run the project, pass [method get_cmdline_args] as the value for [param arguments].
#desc [method set_restart_on_exit] can be used to apply setting changes that require a restart. See also [method is_restart_on_exit_set] and [method get_restart_on_exit_arguments].
#desc [b]Note:[/b] This method is only effective on desktop platforms, and only when the project isn't started from the editor. It will have no effect on mobile and Web platforms, or when the project is started from the editor.
#desc [b]Note:[/b] If the project process crashes or is [i]killed[/i] by the user (by sending [code]SIGKILL[/code] instead of the usual [code]SIGTERM[/code]), the project won't restart automatically.
func set_restart_on_exit(restart: bool, arguments: PackedStringArray) -> void:
	pass;

#desc Sets the name of the current thread.
func set_thread_name() -> int:
	pass;

#desc Enables backup saves if [param enabled] is [code]true[/code].
func set_use_file_access_save_and_swap() -> void:
	pass;

#desc Requests the OS to open a resource with the most appropriate program. For example:
#desc - [code]OS.shell_open("C:\\Users\name\Downloads")[/code] on Windows opens the file explorer at the user's Downloads folder.
#desc - [code]OS.shell_open("https://godotengine.org")[/code] opens the default web browser on the official Godot website.
#desc - [code]OS.shell_open("mailto:example@example.com")[/code] opens the default email client with the "To" field set to [code]example@example.com[/code]. See [url=https://datatracker.ietf.org/doc/html/rfc2368]RFC 2368 - The [code]mailto[/code] URL scheme[/url] for a list of fields that can be added.
#desc Use [method ProjectSettings.globalize_path] to convert a [code]res://[/code] or [code]user://[/code] path into a system path for use with this method.
#desc [b]Note:[/b] This method is implemented on Android, iOS, Web, Linux, macOS and Windows.
func shell_open() -> int:
	pass;


