#brief A TCP server.
#desc A TCP server. Listens to connections on a port and returns a [StreamPeerTCP] when it gets an incoming connection.
#desc [b]Note:[/b] When exporting to Android, make sure to enable the [code]INTERNET[/code] permission in the Android export preset before exporting the project or using one-click deploy. Otherwise, network communication of any kind will be blocked by Android.
class_name TCPServer




#desc Returns the local port this server is listening to.
func get_local_port() -> int:
	pass;

#desc Returns [code]true[/code] if a connection is available for taking.
func is_connection_available() -> bool:
	pass;

#desc Returns [code]true[/code] if the server is currently listening for connections.
func is_listening() -> bool:
	pass;

#desc Listen on the [param port] binding to [param bind_address].
#desc If [param bind_address] is set as [code]"*"[/code] (default), the server will listen on all available addresses (both IPv4 and IPv6).
#desc If [param bind_address] is set as [code]"0.0.0.0"[/code] (for IPv4) or [code]"::"[/code] (for IPv6), the server will listen on all available addresses matching that IP type.
#desc If [param bind_address] is set to any valid address (e.g. [code]"192.168.1.101"[/code], [code]"::1"[/code], etc), the server will only listen on the interface with that addresses (or fail if no interface with the given address exists).
func listen(port: int, bind_address: String) -> int:
	pass;

#desc Stops listening.
func stop() -> void:
	pass;

#desc If a connection is available, returns a StreamPeerTCP with the connection.
func take_connection() -> StreamPeerTCP:
	pass;


