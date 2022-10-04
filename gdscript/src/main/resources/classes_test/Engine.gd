extends Object
#brief Access to engine properties.
#desc The [Engine] singleton allows you to query and modify the project's run-time parameters, such as frames per second, time scale, and others.
class_name Engine


#desc The maximum number of frames per second that can be rendered. A value of [code]0[/code] means "no limit". The actual number of frames per second may still be below this value if the CPU or GPU cannot keep up with the project logic and rendering.
#desc Limiting the FPS can be useful to reduce system power consumption, which reduces heat and noise emissions (and improves battery life on mobile devices).
#desc If [member ProjectSettings.display/window/vsync/vsync_mode] is [code]Enabled[/code] or [code]Adaptive[/code], it takes precedence and the forced FPS number cannot exceed the monitor's refresh rate.
#desc If [member ProjectSettings.display/window/vsync/vsync_mode] is [code]Enabled[/code], on monitors with variable refresh rate enabled (G-Sync/FreeSync), using a FPS limit a few frames lower than the monitor's refresh rate will [url=https://blurbusters.com/howto-low-lag-vsync-on/]reduce input lag while avoiding tearing[/url].
#desc If [member ProjectSettings.display/window/vsync/vsync_mode] is [code]Disabled[/code], limiting the FPS to a high value that can be consistently reached on the system can reduce input lag compared to an uncapped framerate. Since this works by ensuring the GPU load is lower than 100%, this latency reduction is only effective in GPU-bottlenecked scenarios, not CPU-bottlenecked scenarios.
#desc See also [member physics_ticks_per_second] and [member ProjectSettings.application/run/max_fps].
var max_fps: int;

#desc Controls how much physics ticks are synchronized with real time. For 0 or less, the ticks are synchronized. Such values are recommended for network games, where clock synchronization matters. Higher values cause higher deviation of the in-game clock and real clock but smooth out framerate jitters. The default value of 0.5 should be fine for most; values above 2 could cause the game to react to dropped frames with a noticeable delay and are not recommended.
#desc [b]Note:[/b] For best results, when using a custom physics interpolation solution, the physics jitter fix should be disabled by setting [member physics_jitter_fix] to [code]0[/code].
var physics_jitter_fix: float;

#desc The number of fixed iterations per second. This controls how often physics simulation and [method Node._physics_process] methods are run. This value should generally always be set to [code]60[/code] or above, as Godot doesn't interpolate the physics step. As a result, values lower than [code]60[/code] will look stuttery. This value can be increased to make input more reactive or work around collision tunneling issues, but keep in mind doing so will increase CPU usage. See also [member max_fps] and [member ProjectSettings.physics/common/physics_ticks_per_second].
#desc [b]Note:[/b] Only 8 physics ticks may be simulated per rendered frame at most. If more than 8 physics ticks have to be simulated per rendered frame to keep up with rendering, the game will appear to slow down (even if [code]delta[/code] is used consistently in physics calculations). Therefore, it is recommended not to increase [member physics_ticks_per_second] above 240. Otherwise, the game will slow down when the rendering framerate goes below 30 FPS.
var physics_ticks_per_second: int;

#desc If [code]false[/code], stops printing error and warning messages to the console and editor Output log. This can be used to hide error and warning messages during unit test suite runs. This property is equivalent to the [member ProjectSettings.application/run/disable_stderr] project setting.
#desc [b]Warning:[/b] If you set this to [code]false[/code] anywhere in the project, important error messages may be hidden even if they are emitted from other scripts. If this is set to [code]false[/code] in a [code]@tool[/code] script, this will also impact the editor itself. Do [i]not[/i] report bugs before ensuring error messages are enabled (as they are by default).
#desc [b]Note:[/b] This property does not impact the editor's Errors tab when running a project from the editor.
var print_error_messages: bool;

#desc Controls how fast or slow the in-game clock ticks versus the real life one. It defaults to 1.0. A value of 2.0 means the game moves twice as fast as real life, whilst a value of 0.5 means the game moves at half the regular speed.
var time_scale: float;



#desc Returns the name of the CPU architecture the Godot binary was built for. Possible return values are [code]x86_64[/code], [code]x86_32[/code], [code]arm64[/code], [code]armv7[/code], [code]rv64[/code], [code]riscv[/code], [code]ppc64[/code], [code]ppc[/code], [code]wasm64[/code] and [code]wasm32[/code].
#desc To detect whether the current CPU architecture is 64-bit, you can use the fact that all 64-bit architecture names have [code]64[/code] in their name:
#desc [codeblocks]
#desc [gdscript]
#desc if "64" in Engine.get_architecture_name():
#desc print("Running on 64-bit CPU.")
#desc else:
#desc print("Running on 32-bit CPU.")
#desc [/gdscript]
#desc [csharp]
#desc if (Engine.GetArchitectureName().Contains("64"))
#desc GD.Print("Running on 64-bit CPU.");
#desc else
#desc GD.Print("Running on 32-bit CPU.");
#desc [/csharp]
#desc [/codeblocks]
#desc [b]Note:[/b] [method get_architecture_name] does [i]not[/i] return the name of the host CPU architecture. For example, if running an x86_32 Godot binary on a x86_64 system, the returned value will be [code]x86_32[/code].
func get_architecture_name() -> String:
	pass;

#desc Returns engine author information in a Dictionary.
#desc [code]lead_developers[/code]    - Array of Strings, lead developer names
#desc [code]founders[/code]           - Array of Strings, founder names
#desc [code]project_managers[/code]   - Array of Strings, project manager names
#desc [code]developers[/code]         - Array of Strings, developer names
func get_author_info() -> Dictionary:
	pass;

#desc Returns an Array of copyright information Dictionaries.
#desc [code]name[/code]    - String, component name
#desc [code]parts[/code]   - Array of Dictionaries {[code]files[/code], [code]copyright[/code], [code]license[/code]} describing subsections of the component
func get_copyright_info() -> Dictionary[]:
	pass;

#desc Returns a Dictionary of Arrays of donor names.
#desc {[code]platinum_sponsors[/code], [code]gold_sponsors[/code], [code]silver_sponsors[/code], [code]bronze_sponsors[/code], [code]mini_sponsors[/code], [code]gold_donors[/code], [code]silver_donors[/code], [code]bronze_donors[/code]}
func get_donor_info() -> Dictionary:
	pass;

#desc Returns the total number of frames drawn. On headless platforms, or if the render loop is disabled with [code]--disable-render-loop[/code] via command line, [method get_frames_drawn] always returns [code]0[/code]. See [method get_process_frames].
func get_frames_drawn() -> int:
	pass;

#desc Returns the frames per second of the running game.
func get_frames_per_second() -> float:
	pass;

#desc Returns Dictionary of licenses used by Godot and included third party components.
func get_license_info() -> Dictionary:
	pass;

#desc Returns Godot license text.
func get_license_text() -> String:
	pass;

#desc Returns the main loop object (see [MainLoop] and [SceneTree]).
func get_main_loop() -> MainLoop:
	pass;

#desc Returns the total number of frames passed since engine initialization which is advanced on each [b]physics frame[/b]. See also [method get_process_frames].
#desc [method get_physics_frames] can be used to run expensive logic less often without relying on a [Timer]:
#desc [codeblocks]
#desc [gdscript]
#desc func _physics_process(_delta):
#desc if Engine.get_physics_frames() % 2 == 0:
#desc pass  # Run expensive logic only once every 2 physics frames here.
#desc [/gdscript]
#desc [csharp]
#desc public override void _PhysicsProcess(double delta)
#desc {
#desc base._PhysicsProcess(delta);
#desc 
#desc if (Engine.GetPhysicsFrames() % 2 == 0)
#desc {
#desc // Run expensive logic only once every 2 physics frames here.
#desc }
#desc }
#desc [/csharp]
#desc [/codeblocks]
func get_physics_frames() -> int:
	pass;

#desc Returns the fraction through the current physics tick we are at the time of rendering the frame. This can be used to implement fixed timestep interpolation.
func get_physics_interpolation_fraction() -> float:
	pass;

#desc Returns the total number of frames passed since engine initialization which is advanced on each [b]process frame[/b], regardless of whether the render loop is enabled. See also [method get_frames_drawn] and [method get_physics_frames].
#desc [method get_process_frames] can be used to run expensive logic less often without relying on a [Timer]:
#desc [codeblocks]
#desc [gdscript]
#desc func _process(_delta):
#desc if Engine.get_process_frames() % 2 == 0:
#desc pass  # Run expensive logic only once every 2 process (render) frames here.
#desc [/gdscript]
#desc [csharp]
#desc public override void _Process(double delta)
#desc {
#desc base._Process(delta);
#desc 
#desc if (Engine.GetProcessFrames() % 2 == 0)
#desc {
#desc // Run expensive logic only once every 2 physics frames here.
#desc }
#desc }
#desc [/csharp]
#desc [/codeblocks]
func get_process_frames() -> int:
	pass;

func get_script_language(index: int) -> ScriptLanguage:
	pass;

func get_script_language_count() -> int:
	pass;

#desc Returns a global singleton with given [param name]. Often used for plugins, e.g. GodotPayments.
func get_singleton(name: StringName) -> Object:
	pass;

func get_singleton_list() -> PackedStringArray:
	pass;

#desc Returns the current engine version information in a Dictionary.
#desc [code]major[/code]    - Holds the major version number as an int
#desc [code]minor[/code]    - Holds the minor version number as an int
#desc [code]patch[/code]    - Holds the patch version number as an int
#desc [code]hex[/code]      - Holds the full version number encoded as a hexadecimal int with one byte (2 places) per number (see example below)
#desc [code]status[/code]   - Holds the status (e.g. "beta", "rc1", "rc2", ... "stable") as a String
#desc [code]build[/code]    - Holds the build name (e.g. "custom_build") as a String
#desc [code]hash[/code]     - Holds the full Git commit hash as a String
#desc [code]year[/code]     - Holds the year the version was released in as an int
#desc [code]string[/code]   - [code]major[/code] + [code]minor[/code] + [code]patch[/code] + [code]status[/code] + [code]build[/code] in a single String
#desc The [code]hex[/code] value is encoded as follows, from left to right: one byte for the major, one byte for the minor, one byte for the patch version. For example, "3.1.12" would be [code]0x03010C[/code]. [b]Note:[/b] It's still an int internally, and printing it will give you its decimal representation, which is not particularly meaningful. Use hexadecimal literals for easy version comparisons from code:
#desc [codeblocks]
#desc [gdscript]
#desc if Engine.get_version_info().hex >= 0x030200:
#desc # Do things specific to version 3.2 or later
#desc else:
#desc # Do things specific to versions before 3.2
#desc [/gdscript]
#desc [csharp]
#desc if ((int)Engine.GetVersionInfo()["hex"] >= 0x030200)
#desc {
#desc // Do things specific to version 3.2 or later
#desc }
#desc else
#desc {
#desc // Do things specific to versions before 3.2
#desc }
#desc [/csharp]
#desc [/codeblocks]
func get_version_info() -> Dictionary:
	pass;

#desc Returns the path to the [MovieWriter]'s output file, or an empty string if the engine wasn't started in Movie Maker mode. This path can be absolute or relative depending on how the user specified it.
func get_write_movie_path() -> String:
	pass;

#desc Returns [code]true[/code] if a singleton with given [param name] exists in global scope.
func has_singleton(name: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if the script is currently running inside the editor, [code]false[/code] otherwise. This is useful for [code]@tool[/code] scripts to conditionally draw editor helpers, or prevent accidentally running "game" code that would affect the scene state while in the editor:
#desc [codeblocks]
#desc [gdscript]
#desc if Engine.is_editor_hint():
#desc draw_gizmos()
#desc else:
#desc simulate_physics()
#desc [/gdscript]
#desc [csharp]
#desc if (Engine.IsEditorHint())
#desc DrawGizmos();
#desc else
#desc SimulatePhysics();
#desc [/csharp]
#desc [/codeblocks]
#desc See [url=$DOCS_URL/tutorials/plugins/running_code_in_the_editor.html]Running code in the editor[/url] in the documentation for more information.
#desc [b]Note:[/b] To detect whether the script is run from an editor [i]build[/i] (e.g. when pressing [kbd]F5[/kbd]), use [method OS.has_feature] with the [code]"editor"[/code] argument instead. [code]OS.has_feature("editor")[/code] will evaluate to [code]true[/code] both when the code is running in the editor and when running the project from the editor, but it will evaluate to [code]false[/code] when the code is run from an exported project.
func is_editor_hint() -> bool:
	pass;

#desc Returns [code]true[/code] if the game is inside the fixed process and physics phase of the game loop.
func is_in_physics_frame() -> bool:
	pass;

func register_script_language(language: ScriptLanguage) -> void:
	pass;

func register_singleton(name: StringName, instance: Object) -> void:
	pass;

func unregister_singleton(name: StringName) -> void:
	pass;


