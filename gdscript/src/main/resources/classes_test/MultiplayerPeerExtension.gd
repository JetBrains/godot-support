#brief Class that can be inherited to implement custom multiplayer API networking layers via GDExtension.
#desc This class is designed to be inherited from a GDExtension plugin to implement custom networking layers for the multiplayer API (such as WebRTC). All the methods below [b]must[/b] be implemented to have a working custom multiplayer implementation. See also [MultiplayerAPI].
class_name MultiplayerPeerExtension




#desc Called when the available packet count is internally requested by the [MultiplayerAPI].
virtual const func _get_available_packet_count() -> int:
	pass;

#desc Called when the connection status is requested on the [MultiplayerPeer] (see [method MultiplayerPeer.get_connection_status]).
virtual const func _get_connection_status() -> int:
	pass;

#desc Called when the maximum allowed packet size (in bytes) is requested by the [MultiplayerAPI].
virtual const func _get_max_packet_size() -> int:
	pass;

#desc Called when a packet needs to be received by the [MultiplayerAPI], with [param r_buffer_size] being the size of the binary [param r_buffer] in bytes.
virtual func _get_packet(r_buffer: const uint8_t **, r_buffer_size: int32_t*) -> int:
	pass;

#desc Called when the ID of the [MultiplayerPeer] who sent the most recent packet is requested (see [method MultiplayerPeer.get_packet_peer]).
virtual const func _get_packet_peer() -> int:
	pass;

#desc Called when a packet needs to be received by the [MultiplayerAPI], if [method _get_packet] isn't implemented. Use this when extending this class via GDScript.
virtual func _get_packet_script() -> PackedByteArray:
	pass;

#desc Called when the transfer channel to use is read on this [MultiplayerPeer] (see [member MultiplayerPeer.transfer_channel]).
virtual const func _get_transfer_channel() -> int:
	pass;

#desc Called when the transfer mode to use is read on this [MultiplayerPeer] (see [member MultiplayerPeer.transfer_mode]).
virtual const func _get_transfer_mode() -> int:
	pass;

#desc Called when the unique ID of this [MultiplayerPeer] is requested (see [method MultiplayerPeer.get_unique_id]).
virtual const func _get_unique_id() -> int:
	pass;

#desc Called when the "refuse new connections" status is requested on this [MultiplayerPeer] (see [member MultiplayerPeer.refuse_new_connections]).
virtual const func _is_refusing_new_connections() -> bool:
	pass;

#desc Called when the "is server" status is requested on the [MultiplayerAPI]. See [method MultiplayerAPI.is_server].
virtual const func _is_server() -> bool:
	pass;

#desc Called when the [MultiplayerAPI] is polled. See [method MultiplayerAPI.poll].
virtual func _poll() -> void:
	pass;

#desc Called when a packet needs to be sent by the [MultiplayerAPI], with [param p_buffer_size] being the size of the binary [param p_buffer] in bytes.
virtual func _put_packet(p_buffer: const uint8_t*, p_buffer_size: int) -> int:
	pass;

#desc Called when a packet needs to be sent by the [MultiplayerAPI], if [method _put_packet] isn't implemented. Use this when extending this class via GDScript.
virtual func _put_packet_script() -> int:
	pass;

#desc Called when the "refuse new connections" status is set on this [MultiplayerPeer] (see [member MultiplayerPeer.refuse_new_connections]).
virtual func _set_refuse_new_connections() -> void:
	pass;

#desc Called when the target peer to use is set for this [MultiplayerPeer] (see [method MultiplayerPeer.set_target_peer]).
virtual func _set_target_peer() -> void:
	pass;

#desc Called when the channel to use is set for this [MultiplayerPeer] (see [member MultiplayerPeer.transfer_channel]).
virtual func _set_transfer_channel() -> void:
	pass;

#desc Called when the transfer mode is set on this [MultiplayerPeer] (see [member MultiplayerPeer.transfer_mode]).
virtual func _set_transfer_mode() -> void:
	pass;


