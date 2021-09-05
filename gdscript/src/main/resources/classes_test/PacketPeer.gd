extends RefCounted
class_name PacketPeer


var encode_buffer_max_size: int setget set_encode_buffer_max_size, get_encode_buffer_max_size;

func get_available_packet_count() -> int:
    pass;

func get_packet() -> PackedByteArray:
    pass;

func get_packet_error() -> int:
    pass;

func get_var(allow_objects: bool) -> Variant:
    pass;

func put_packet(buffer: PackedByteArray) -> int:
    pass;

func put_var(var: Variant, full_objects: bool) -> int:
    pass;

