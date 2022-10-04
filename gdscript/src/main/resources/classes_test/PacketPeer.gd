#brief Abstraction and base class for packet-based protocols.
#desc PacketPeer is an abstraction and base class for packet-based protocols (such as UDP). It provides an API for sending and receiving packets both as raw data or variables. This makes it easy to transfer data over a protocol, without having to encode data as low-level bytes or having to worry about network ordering.
#desc [b]Note:[/b] When exporting to Android, make sure to enable the [code]INTERNET[/code] permission in the Android export preset before exporting the project or using one-click deploy. Otherwise, network communication of any kind will be blocked by Android.
class_name PacketPeer


#desc Maximum buffer size allowed when encoding [Variant]s. Raise this value to support heavier memory allocations.
#desc The [method put_var] method allocates memory on the stack, and the buffer used will grow automatically to the closest power of two to match the size of the [Variant]. If the [Variant] is bigger than [code]encode_buffer_max_size[/code], the method will error out with [constant ERR_OUT_OF_MEMORY].
var encode_buffer_max_size: int;



#desc Returns the number of packets currently available in the ring-buffer.
func get_available_packet_count() -> int:
	pass;

#desc Gets a raw packet.
func get_packet() -> PackedByteArray:
	pass;

#desc Returns the error state of the last packet received (via [method get_packet] and [method get_var]).
func get_packet_error() -> int:
	pass;

#desc Gets a Variant. If [param allow_objects] is [code]true[/code], decoding objects is allowed.
#desc [b]Warning:[/b] Deserialized objects can contain code which gets executed. Do not use this option if the serialized object comes from untrusted sources to avoid potential security threats such as remote code execution.
func get_var(allow_objects: bool) -> Variant:
	pass;

#desc Sends a raw packet.
func put_packet(buffer: PackedByteArray) -> int:
	pass;

#desc Sends a [Variant] as a packet. If [param full_objects] is [code]true[/code], encoding objects is allowed (and can potentially include code).
func put_var(var: Variant, full_objects: bool) -> int:
	pass;


