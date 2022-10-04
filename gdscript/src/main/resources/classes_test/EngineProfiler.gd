#brief Base class for creating custom profilers.
#desc This class can be used to implement custom profilers that are able to interact with the engine and editor debugger.
#desc See [EngineDebugger] and [EditorDebuggerPlugin] for more information.
class_name EngineProfiler




#desc Called when data is added to profiler using [method EngineDebugger.profiler_add_frame_data].
virtual func _add_frame() -> void:
	pass;

#desc Called once every engine iteration when the profiler is active with information about the current frame. All time values are in seconds. Lower values represent faster processing times and are therefore considered better.
virtual func _tick(frame_time: float, process_time: float, physics_time: float, physics_frame_time: float) -> void:
	pass;

#desc Called when the profiler is enabled/disabled, along with a set of [param options].
virtual func _toggle(enable: bool, options: Array) -> void:
	pass;


