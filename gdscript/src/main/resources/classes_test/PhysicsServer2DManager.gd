extends Object
#brief Manager for 2D physics server implementations.
#desc [PhysicsServer2DManager] is the API for registering [PhysicsServer2D] implementations, and for setting the default implementation.
#desc [b]Note:[/b] It is not possible to switch physics servers at runtime. This class is only used on startup at the server initialization level, by Godot itself and possibly by GDExtensions.
class_name PhysicsServer2DManager




#desc Register a [PhysicsServer2D] implementation by passing a [param name] and a [Callable] that returns a [PhysicsServer2D] object.
func register_server(name: String, create_callback: Callable) -> void:
	pass;

#desc Set the default [PhysicsServer2D] implementation to the one identified by [param name], if [param priority] is greater than the priority of the current default implementation.
func set_default_server(name: String, priority: int) -> void:
	pass;


