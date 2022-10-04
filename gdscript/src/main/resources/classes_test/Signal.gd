#brief Class representing a signal defined in an object.
class_name Signal



#desc Constructs a null [Signal] with no object nor signal name bound.
func Signal() -> Signal:
	pass;

#desc Constructs a [Signal] as a copy of the given [Signal].
func Signal(from: Signal) -> Signal:
	pass;

#desc Creates a new [Signal] with the name [param signal] in the specified [param object].
func Signal(object: Object, signal: StringName) -> Signal:
	pass;


#desc Connects this signal to the specified [Callable], optionally providing connection flags. You can provide additional arguments to the connected method call by using [method Callable.bind].
#desc [codeblock]
#desc for button in $Buttons.get_children():
#desc button.pressed.connect(on_pressed.bind(button))
#desc 
#desc func on_pressed(button):
#desc print(button.name, " was pressed")
#desc [/codeblock]
func connect(callable: Callable, flags: int) -> int:
	pass;

#desc Disconnects this signal from the specified [Callable].
func disconnect(callable: Callable) -> void:
	pass;

#desc Emits this signal to all connected objects.
func emit() -> void:
	pass;

#desc Returns the list of [Callable]s connected to this signal.
func get_connections() -> Array:
	pass;

#desc Returns the name of this signal.
func get_name() -> StringName:
	pass;

#desc Returns the object emitting this signal.
func get_object() -> Object:
	pass;

#desc Returns the ID of the object emitting this signal (see [method Object.get_instance_id]).
func get_object_id() -> int:
	pass;

#desc Returns [code]true[/code] if the specified [Callable] is connected to this signal.
func is_connected(callable: Callable) -> bool:
	pass;

func is_null() -> bool:
	pass;


