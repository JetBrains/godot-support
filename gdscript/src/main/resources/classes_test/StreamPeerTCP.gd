#brief TCP stream peer.
#desc TCP stream peer. This object can be used to connect to TCP servers, or also is returned by a TCP server.
#desc [b]Note:[/b] When exporting to Android, make sure to enable the [code]INTERNET[/code] permission in the Android export preset before exporting the project or using one-click deploy. Otherwise, network communication of any kind will be blocked by Android.
class_name StreamPeerTCP

#desc The initial status of the [StreamPeerTCP]. This is also the status after disconnecting.
const STATUS_NONE = 0;

#desc A status representing a [StreamPeerTCP] that is connecting to a host.
const STATUS_CONNECTING = 1;

#desc A status representing a [StreamPeerTCP] that is connected to a host.
const STATUS_CONNECTED = 2;

#desc A status representing a [StreamPeerTCP] in error state.
const STATUS_ERROR = 3;




#desc Opens the TCP socket, and binds it to the specified local address.
#desc This method is generally not needed, and only used to force the subsequent call to [method connect_to_host] to use the specified [param host] and [param port] as source address. This can be desired in some NAT punchthrough techniques, or when forcing the source network interface.
func bind(port: int, host: String) -> int:
	pass;

#desc Connects to the specified [code]host:port[/code] pair. A hostname will be resolved if valid. Returns [constant OK] on success.
func connect_to_host(host: String, port: int) -> int:
	pass;

#desc Disconnects from host.
func disconnect_from_host() -> void:
	pass;

#desc Returns the IP of this peer.
func get_connected_host() -> String:
	pass;

#desc Returns the port of this peer.
func get_connected_port() -> int:
	pass;

#desc Returns the local port to which this peer is bound.
func get_local_port() -> int:
	pass;

#desc Returns the status of the connection, see [enum Status].
func get_status() -> int:
	pass;

#desc Poll the socket, updating its state. See [method get_status].
func poll() -> int:
	pass;

#desc If [param enabled] is [code]true[/code], packets will be sent immediately. If [param enabled] is [code]false[/code] (the default), packet transfers will be delayed and combined using [url=https://en.wikipedia.org/wiki/Nagle%27s_algorithm]Nagle's algorithm[/url].
#desc [b]Note:[/b] It's recommended to leave this disabled for applications that send large packets or need to transfer a lot of data, as enabling this can decrease the total available bandwidth.
func set_no_delay(enabled: bool) -> void:
	pass;


