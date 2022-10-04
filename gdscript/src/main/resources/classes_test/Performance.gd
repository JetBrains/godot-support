#brief Exposes performance-related data.
#desc This class provides access to a number of different monitors related to performance, such as memory usage, draw calls, and FPS. These are the same as the values displayed in the [b]Monitor[/b] tab in the editor's [b]Debugger[/b] panel. By using the [method get_monitor] method of this class, you can access this data from your code.
#desc You can add custom monitors using the [method add_custom_monitor] method. Custom monitors are available in [b]Monitor[/b] tab in the editor's [b]Debugger[/b] panel together with built-in monitors.
#desc [b]Note:[/b] Some of the built-in monitors are only available in debug mode and will always return [code]0[/code] when used in a project exported in release mode.
#desc [b]Note:[/b] Some of the built-in monitors are not updated in real-time for performance reasons, so there may be a delay of up to 1 second between changes.
#desc [b]Note:[/b] Custom monitors do not support negative values. Negative values are clamped to 0.
class_name Performance

#desc The number of frames rendered in the last second. This metric is only updated once per second, even if queried more often. [i]Higher is better.[/i]
const TIME_FPS = 0;

#desc Time it took to complete one frame, in seconds. [i]Lower is better.[/i]
const TIME_PROCESS = 1;

#desc Time it took to complete one physics frame, in seconds. [i]Lower is better.[/i]
const TIME_PHYSICS_PROCESS = 2;

#desc Static memory currently used, in bytes. Not available in release builds. [i]Lower is better.[/i]
const MEMORY_STATIC = 3;

#desc Available static memory. Not available in release builds. [i]Lower is better.[/i]
const MEMORY_STATIC_MAX = 4;

#desc Largest amount of memory the message queue buffer has used, in bytes. The message queue is used for deferred functions calls and notifications. [i]Lower is better.[/i]
const MEMORY_MESSAGE_BUFFER_MAX = 5;

#desc Number of objects currently instantiated (including nodes). [i]Lower is better.[/i]
const OBJECT_COUNT = 6;

#desc Number of resources currently used. [i]Lower is better.[/i]
const OBJECT_RESOURCE_COUNT = 7;

#desc Number of nodes currently instantiated in the scene tree. This also includes the root node. [i]Lower is better.[/i]
const OBJECT_NODE_COUNT = 8;

#desc Number of orphan nodes, i.e. nodes which are not parented to a node of the scene tree. [i]Lower is better.[/i]
const OBJECT_ORPHAN_NODE_COUNT = 9;

#desc The total number of objects in the last rendered frame. This metric doesn't include culled objects (either via hiding nodes, frustum culling or occlusion culling). [i]Lower is better.[/i]
const RENDER_TOTAL_OBJECTS_IN_FRAME = 10;

#desc The total number of vertices or indices rendered in the last rendered frame. This metric doesn't include primitives from culled objects (either via hiding nodes, frustum culling or occlusion culling). Due to the depth prepass and shadow passes, the number of primitives is always higher than the actual number of vertices in the scene (typically double or triple the original vertex count). [i]Lower is better.[/i]
const RENDER_TOTAL_PRIMITIVES_IN_FRAME = 11;

#desc The total number of draw calls performed in the last rendered frame. This metric doesn't include culled objects (either via hiding nodes, frustum culling or occlusion culling), since they do not result in draw calls. [i]Lower is better.[/i]
const RENDER_TOTAL_DRAW_CALLS_IN_FRAME = 12;

#desc The amount of video memory used (texture and vertex memory combined, in bytes). Since this metric also includes miscellaneous allocations, this value is always greater than the sum of [constant RENDER_TEXTURE_MEM_USED] and [constant RENDER_BUFFER_MEM_USED]. [i]Lower is better.[/i]
const RENDER_VIDEO_MEM_USED = 13;

#desc The amount of texture memory used (in bytes). [i]Lower is better.[/i]
const RENDER_TEXTURE_MEM_USED = 14;

#desc The amount of render buffer memory used (in bytes). [i]Lower is better.[/i]
const RENDER_BUFFER_MEM_USED = 15;

#desc Number of active [RigidBody2D] nodes in the game. [i]Lower is better.[/i]
const PHYSICS_2D_ACTIVE_OBJECTS = 16;

#desc Number of collision pairs in the 2D physics engine. [i]Lower is better.[/i]
const PHYSICS_2D_COLLISION_PAIRS = 17;

#desc Number of islands in the 2D physics engine. [i]Lower is better.[/i]
const PHYSICS_2D_ISLAND_COUNT = 18;

#desc Number of active [RigidBody3D] and [VehicleBody3D] nodes in the game. [i]Lower is better.[/i]
const PHYSICS_3D_ACTIVE_OBJECTS = 19;

#desc Number of collision pairs in the 3D physics engine. [i]Lower is better.[/i]
const PHYSICS_3D_COLLISION_PAIRS = 20;

#desc Number of islands in the 3D physics engine. [i]Lower is better.[/i]
const PHYSICS_3D_ISLAND_COUNT = 21;

#desc Output latency of the [AudioServer]. [i]Lower is better.[/i]
const AUDIO_OUTPUT_LATENCY = 22;

#desc Represents the size of the [enum Monitor] enum.
const MONITOR_MAX = 23;




#desc Adds a custom monitor with the name [param id]. You can specify the category of the monitor using slash delimiters in [param id] (for example: [code]"Game/NumberOfNPCs"[/code]). If there is more than one slash delimiter, then the default category is used. The default category is [code]"Custom"[/code]. Prints an error if given [param id] is already present.
#desc [codeblocks]
#desc [gdscript]
#desc func _ready():
#desc var monitor_value = Callable(self, "get_monitor_value")
#desc 
#desc # Adds monitor with name "MyName" to category "MyCategory".
#desc Performance.add_custom_monitor("MyCategory/MyMonitor", monitor_value)
#desc 
#desc # Adds monitor with name "MyName" to category "Custom".
#desc # Note: "MyCategory/MyMonitor" and "MyMonitor" have same name but different IDs, so the code is valid.
#desc Performance.add_custom_monitor("MyMonitor", monitor_value)
#desc 
#desc # Adds monitor with name "MyName" to category "Custom".
#desc # Note: "MyMonitor" and "Custom/MyMonitor" have same name and same category but different IDs, so the code is valid.
#desc Performance.add_custom_monitor("Custom/MyMonitor", monitor_value)
#desc 
#desc # Adds monitor with name "MyCategoryOne/MyCategoryTwo/MyMonitor" to category "Custom".
#desc Performance.add_custom_monitor("MyCategoryOne/MyCategoryTwo/MyMonitor", monitor_value)
#desc 
#desc func get_monitor_value():
#desc return randi() % 25
#desc [/gdscript]
#desc [csharp]
#desc public override void _Ready()
#desc {
#desc var monitorValue = new Callable(this, nameof(GetMonitorValue));
#desc 
#desc // Adds monitor with name "MyName" to category "MyCategory".
#desc Performance.AddCustomMonitor("MyCategory/MyMonitor", monitorValue);
#desc // Adds monitor with name "MyName" to category "Custom".
#desc // Note: "MyCategory/MyMonitor" and "MyMonitor" have same name but different ids so the code is valid.
#desc Performance.AddCustomMonitor("MyMonitor", monitorValue);
#desc 
#desc // Adds monitor with name "MyName" to category "Custom".
#desc // Note: "MyMonitor" and "Custom/MyMonitor" have same name and same category but different ids so the code is valid.
#desc Performance.AddCustomMonitor("Custom/MyMonitor", monitorValue);
#desc 
#desc // Adds monitor with name "MyCategoryOne/MyCategoryTwo/MyMonitor" to category "Custom".
#desc Performance.AddCustomMonitor("MyCategoryOne/MyCategoryTwo/MyMonitor", monitorValue);
#desc }
#desc 
#desc public int GetMonitorValue()
#desc {
#desc return GD.Randi() % 25;
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc The debugger calls the callable to get the value of custom monitor. The callable must return a zero or positive integer or floating-point number.
#desc Callables are called with arguments supplied in argument array.
func add_custom_monitor(id: StringName, callable: Callable, arguments: Array) -> void:
	pass;

#desc Returns the value of custom monitor with given [param id]. The callable is called to get the value of custom monitor. See also [method has_custom_monitor]. Prints an error if the given [param id] is absent.
func get_custom_monitor(id: StringName) -> Variant:
	pass;

#desc Returns the names of active custom monitors in an [Array].
func get_custom_monitor_names() -> StringName[]:
	pass;

#desc Returns the value of one of the available built-in monitors. You should provide one of the [enum Monitor] constants as the argument, like this:
#desc [codeblocks]
#desc [gdscript]
#desc print(Performance.get_monitor(Performance.TIME_FPS)) # Prints the FPS to the console.
#desc [/gdscript]
#desc [csharp]
#desc GD.Print(Performance.GetMonitor(Performance.Monitor.TimeFps)); // Prints the FPS to the console.
#desc [/csharp]
#desc [/codeblocks]
#desc See [method get_custom_monitor] to query custom performance monitors' values.
func get_monitor(monitor: int) -> float:
	pass;

#desc Returns the last tick in which custom monitor was added/removed (in microseconds since the engine started). This is set to [method Time.get_ticks_usec] when the monitor is updated.
func get_monitor_modification_time() -> int:
	pass;

#desc Returns [code]true[/code] if custom monitor with the given [param id] is present, [code]false[/code] otherwise.
func has_custom_monitor(id: StringName) -> bool:
	pass;

#desc Removes the custom monitor with given [param id]. Prints an error if the given [param id] is already absent.
func remove_custom_monitor(id: StringName) -> void:
	pass;


