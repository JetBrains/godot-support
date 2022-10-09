extends PacketPeer
class_name PacketPeerExtension




func _get_available_packet_count() -> int:
	pass;

func _get_max_packet_size() -> int:
	pass;

func _get_packet(r_buffer: const uint8_t **, r_buffer_size: int32_t*) -> int:
	pass;

func _put_packet(p_buffer: const uint8_t*, p_buffer_size: int) -> int:
	pass;


