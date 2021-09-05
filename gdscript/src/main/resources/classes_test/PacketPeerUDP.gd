extends PacketPeer
class_name PacketPeerUDP



func bind(port: int, bind_address: String, recv_buf_size: int) -> int:
    pass;

func close() -> void:
    pass;

func connect_to_host(host: String, port: int) -> int:
    pass;

func get_local_port() -> int:
    pass;

func get_packet_ip() -> String:
    pass;

func get_packet_port() -> int:
    pass;

func is_bound() -> bool:
    pass;

func is_connected_to_host() -> bool:
    pass;

func join_multicast_group(multicast_address: String, interface_name: String) -> int:
    pass;

func leave_multicast_group(multicast_address: String, interface_name: String) -> int:
    pass;

func set_broadcast_enabled(enabled: bool) -> void:
    pass;

func set_dest_address(host: String, port: int) -> int:
    pass;

func wait() -> int:
    pass;

