extends RefCounted
class_name UDPServer


var max_pending_connections: int setget set_max_pending_connections, get_max_pending_connections;

func get_local_port() -> int:
    pass;

func is_connection_available() -> bool:
    pass;

func is_listening() -> bool:
    pass;

func listen(port: int, bind_address: String) -> int:
    pass;

func poll() -> int:
    pass;

func stop() -> void:
    pass;

func take_connection() -> PacketPeerUDP:
    pass;

