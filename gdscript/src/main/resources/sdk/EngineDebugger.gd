extends Object
#brief Exposes the internal debugger.
#desc [EngineDebugger] handles the communication between the editor and the running game. It is active in the running game. Messages can be sent/received through it. It also manages the profilers.
class_name EngineDebugger




#desc Returns [code]true[/code] if a capture with the given name is present otherwise [code]false[/code].
func has_capture(name: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if a profiler with the given name is present otherwise [code]false[/code].
func has_profiler(name: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if the debugger is active otherwise [code]false[/code].
func is_active() -> bool:
	pass;

#desc Returns [code]true[/code] if a profiler with the given name is present and active otherwise [code]false[/code].
func is_profiling(name: StringName) -> bool:
	pass;

#desc Calls the [code]add[/code] callable of the profiler with given [param name] and [param data].
func profiler_add_frame_data(name: StringName, data: Array) -> void:
	pass;

#desc Calls the [code]toggle[/code] callable of the profiler with given [param name] and [param arguments]. Enables/Disables the same profiler depending on [code]enable[/code] argument.
func profiler_enable(name: StringName, enable: bool, arguments: Array) -> void:
	pass;

#desc Registers a message capture with given [param name]. If [param name] is "my_message" then messages starting with "my_message:" will be called with the given callable.
#desc Callable must accept a message string and a data array as argument. If the message and data are valid then callable must return [code]true[/code] otherwise [code]false[/code].
func register_message_capture(name: StringName, callable: Callable) -> void:
	pass;

#desc Registers a profiler with the given [param name]. See [EngineProfiler] for more information.
func register_profiler(name: StringName, profiler: EngineProfiler) -> void:
	pass;

#desc Sends a message with given [param message] and [param data] array.
func send_message(message: String, data: Array) -> void:
	pass;

#desc Unregisters the message capture with given [param name].
func unregister_message_capture(name: StringName) -> void:
	pass;

#desc Unregisters a profiler with given [param name].
func unregister_profiler(name: StringName) -> void:
	pass;


