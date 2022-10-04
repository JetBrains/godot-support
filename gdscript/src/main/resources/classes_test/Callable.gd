#brief An object representing a method in a certain object that can be called.
#desc [Callable] is a first class object which can be held in variables and passed to functions. It represents a given method in an [Object], and is typically used for signal callbacks.
#desc [b]Example:[/b]
#desc [codeblocks]
#desc [gdscript]
#desc func print_args(arg1, arg2, arg3 = ""):
#desc prints(arg1, arg2, arg3)
#desc 
#desc func test():
#desc var callable = Callable(self, "print_args")
#desc callable.call("hello", "world")  # Prints "hello world ".
#desc callable.call(Vector2.UP, 42, callable)  # Prints "(0, -1) 42 Node(Node.gd)::print_args".
#desc callable.call("invalid")  # Invalid call, should have at least 2 arguments.
#desc [/gdscript]
#desc [csharp]
#desc public void PrintArgs(object arg1, object arg2, object arg3 = null)
#desc {
#desc GD.PrintS(arg1, arg2, arg3);
#desc }
#desc 
#desc public void Test()
#desc {
#desc Callable callable = new Callable(this, nameof(PrintArgs));
#desc callable.Call("hello", "world"); // Prints "hello world null".
#desc callable.Call(Vector2.Up, 42, callable); // Prints "(0, -1) 42 Node(Node.cs)::PrintArgs".
#desc callable.Call("invalid"); // Invalid call, should have at least 2 arguments.
#desc }
#desc [/csharp]
#desc [/codeblocks]
class_name Callable



#desc Constructs a null [Callable] with no object nor method bound.
func Callable() -> Callable:
	pass;

#desc Constructs a [Callable] as a copy of the given [Callable].
func Callable(from: Callable) -> Callable:
	pass;

#desc Creates a new [Callable] for the method called [param method] in the specified [param object].
func Callable(object: Object, method: StringName) -> Callable:
	pass;


#desc Returns a copy of this [Callable] with the arguments bound. Bound arguments are passed after the arguments supplied by [method call].
func bind() -> Callable:
	pass;

#desc Calls the method represented by this [Callable]. Arguments can be passed and should match the method's signature.
func call() -> Variant:
	pass;

#desc Calls the method represented by this [Callable] in deferred mode, i.e. during the idle frame. Arguments can be passed and should match the method's signature.
#desc [codeblock]
#desc func _ready():
#desc grab_focus.call_deferred()
#desc [/codeblock]
func call_deferred() -> void:
	pass;

#desc Calls the method represented by this [Callable]. Contrary to [method call], this method does not take a variable number of arguments but expects all arguments to be passed via a single [Array].
func callv(arguments: Array) -> Variant:
	pass;

#desc Returns the name of the method represented by this [Callable].
func get_method() -> StringName:
	pass;

#desc Returns the object on which this [Callable] is called.
func get_object() -> Object:
	pass;

#desc Returns the ID of this [Callable]'s object (see [method Object.get_instance_id]).
func get_object_id() -> int:
	pass;

#desc Returns the 32-bit hash value of this [Callable]'s object.
#desc [b]Note:[/b] [Callable]s with equal content will always produce identical hash values. However, the reverse is not true. Returning identical hash values does [i]not[/i] imply the callables are equal, because different callables can have identical hash values due to hash collisions. The engine uses a 32-bit hash algorithm for [method hash].
func hash() -> int:
	pass;

#desc Returns [code]true[/code] if this [Callable] is a custom callable whose behavior differs based on implementation details. Custom callables are used in the engine for various reasons. If [code]true[/code], you can't use [method get_method].
func is_custom() -> bool:
	pass;

#desc Returns [code]true[/code] if this [Callable] has no target to call the method on.
func is_null() -> bool:
	pass;

#desc Returns [code]true[/code] if this [Callable] is a standard callable, referencing an object and a method using a [StringName].
func is_standard() -> bool:
	pass;

#desc Returns [code]true[/code] if the object exists and has a valid function assigned, or is a custom callable.
func is_valid() -> bool:
	pass;

#desc Perform an RPC (Remote Procedure Call). This is used for multiplayer and is normally not available unless the function being called has been marked as [i]RPC[/i]. Calling it on unsupported functions will result in an error.
func rpc() -> void:
	pass;

#desc Perform an RPC (Remote Procedure Call) on a specific peer ID (see multiplayer documentation for reference). This is used for multiplayer and is normally not available unless the function being called has been marked as [i]RPC[/i]. Calling it on unsupported functions will result in an error.
func rpc_id(peer_id: int) -> void:
	pass;

#desc Returns a copy of this [Callable] with the arguments unbound. Calling the returned [Callable] will call the method without the extra arguments that are supplied in the [Callable] on which you are calling this method.
func unbind(argcount: int) -> Callable:
	pass;


