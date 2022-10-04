extends Control
#brief A base class to implement debugger plugins.
#desc [EditorDebuggerPlugin] provides functions related to the editor side of the debugger.
#desc You don't need to instantiate this class; that is automatically handled by the debugger. [Control] nodes can be added as child nodes to provide a GUI for the plugin.
#desc Do not free or reparent this node, otherwise it becomes unusable.
#desc To use [EditorDebuggerPlugin], register it using the [method EditorPlugin.add_debugger_plugin] method first.
class_name EditorDebuggerPlugin




#desc Returns [code]true[/code] if a message capture with given name is present otherwise [code]false[/code].
func has_capture(name: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if the game is in break state otherwise [code]false[/code].
func is_breaked() -> bool:
	pass;

#desc Returns [code]true[/code] if the game can be debugged otherwise [code]false[/code].
func is_debuggable() -> bool:
	pass;

#desc Returns [code]true[/code] if there is an instance of the game running with the attached debugger otherwise [code]false[/code].
func is_session_active() -> bool:
	pass;

#desc Registers a message capture with given [param name]. If [param name] is "my_message" then messages starting with "my_message:" will be called with the given callable.
#desc Callable must accept a message string and a data array as argument. If the message and data are valid then callable must return [code]true[/code] otherwise [code]false[/code].
func register_message_capture(name: StringName, callable: Callable) -> void:
	pass;

#desc Sends a message with given [param message] and [param data] array.
func send_message(message: String, data: Array) -> void:
	pass;

#desc Unregisters the message capture with given name.
func unregister_message_capture(name: StringName) -> void:
	pass;


