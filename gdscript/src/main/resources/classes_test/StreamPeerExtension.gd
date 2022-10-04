class_name StreamPeerExtension




virtual const func _get_available_bytes() -> int:
	pass;

virtual func _get_data(r_buffer: uint8_t*, r_bytes: int, r_received: int32_t*) -> int:
	pass;

virtual func _get_partial_data(r_buffer: uint8_t*, r_bytes: int, r_received: int32_t*) -> int:
	pass;

virtual func _put_data(p_data: const uint8_t*, p_bytes: int, r_sent: int32_t*) -> int:
	pass;

virtual func _put_partial_data(p_data: const uint8_t*, p_bytes: int, r_sent: int32_t*) -> int:
	pass;


