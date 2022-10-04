#brief UDP packet peer.
#desc UDP packet peer. Can be used to send raw UDP packets as well as [Variant]s.
#desc [b]Note:[/b] When exporting to Android, make sure to enable the [code]INTERNET[/code] permission in the Android export preset before exporting the project or using one-click deploy. Otherwise, network communication of any kind will be blocked by Android.
class_name PacketPeerUDP




#desc Binds this [PacketPeerUDP] to the specified [param port] and [param bind_address] with a buffer size [param recv_buf_size], allowing it to receive incoming packets.
#desc If [param bind_address] is set to [code]"*"[/code] (default), the peer will be bound on all available addresses (both IPv4 and IPv6).
#desc If [param bind_address] is set to [code]"0.0.0.0"[/code] (for IPv4) or [code]"::"[/code] (for IPv6), the peer will be bound to all available addresses matching that IP type.
#desc If [param bind_address] is set to any valid address (e.g. [code]"192.168.1.101"[/code], [code]"::1"[/code], etc), the peer will only be bound to the interface with that addresses (or fail if no interface with the given address exists).
func bind(port: int, bind_address: String, recv_buf_size: int) -> int:
	pass;

#desc Closes the [PacketPeerUDP]'s underlying UDP socket.
func close() -> void:
	pass;

#desc Calling this method connects this UDP peer to the given [param host]/[param port] pair. UDP is in reality connectionless, so this option only means that incoming packets from different addresses are automatically discarded, and that outgoing packets are always sent to the connected address (future calls to [method set_dest_address] are not allowed). This method does not send any data to the remote peer, to do that, use [method PacketPeer.put_var] or [method PacketPeer.put_packet] as usual. See also [UDPServer].
#desc [b]Note:[/b] Connecting to the remote peer does not help to protect from malicious attacks like IP spoofing, etc. Think about using an encryption technique like TLS or DTLS if you feel like your application is transferring sensitive information.
func connect_to_host(host: String, port: int) -> int:
	pass;

#desc Returns the local port to which this peer is bound.
func get_local_port() -> int:
	pass;

#desc Returns the IP of the remote peer that sent the last packet(that was received with [method PacketPeer.get_packet] or [method PacketPeer.get_var]).
func get_packet_ip() -> String:
	pass;

#desc Returns the port of the remote peer that sent the last packet(that was received with [method PacketPeer.get_packet] or [method PacketPeer.get_var]).
func get_packet_port() -> int:
	pass;

#desc Returns whether this [PacketPeerUDP] is bound to an address and can receive packets.
func is_bound() -> bool:
	pass;

#desc Returns [code]true[/code] if the UDP socket is open and has been connected to a remote address. See [method connect_to_host].
func is_socket_connected() -> bool:
	pass;

#desc Joins the multicast group specified by [param multicast_address] using the interface identified by [param interface_name].
#desc You can join the same multicast group with multiple interfaces. Use [method IP.get_local_interfaces] to know which are available.
#desc [b]Note:[/b] Some Android devices might require the [code]CHANGE_WIFI_MULTICAST_STATE[/code] permission for multicast to work.
func join_multicast_group(multicast_address: String, interface_name: String) -> int:
	pass;

#desc Removes the interface identified by [param interface_name] from the multicast group specified by [param multicast_address].
func leave_multicast_group(multicast_address: String, interface_name: String) -> int:
	pass;

#desc Enable or disable sending of broadcast packets (e.g. [code]set_dest_address("255.255.255.255", 4343)[/code]. This option is disabled by default.
#desc [b]Note:[/b] Some Android devices might require the [code]CHANGE_WIFI_MULTICAST_STATE[/code] permission and this option to be enabled to receive broadcast packets too.
func set_broadcast_enabled() -> void:
	pass;

#desc Sets the destination address and port for sending packets and variables. A hostname will be resolved using DNS if needed.
#desc [b]Note:[/b] [method set_broadcast_enabled] must be enabled before sending packets to a broadcast address (e.g. [code]255.255.255.255[/code]).
func set_dest_address(host: String, port: int) -> int:
	pass;

#desc Waits for a packet to arrive on the bound address. See [method bind].
#desc [b]Note:[/b] [method wait] can't be interrupted once it has been called. This can be worked around by allowing the other party to send a specific "death pill" packet like this:
#desc [codeblocks]
#desc [gdscript]
#desc socket = PacketPeerUDP.new()
#desc # Server
#desc socket.set_dest_address("127.0.0.1", 789)
#desc socket.put_packet("Time to stop".to_ascii())
#desc 
#desc # Client
#desc while socket.wait() == OK:
#desc var data = socket.get_packet().get_string_from_ascii()
#desc if data == "Time to stop":
#desc return
#desc [/gdscript]
#desc [csharp]
#desc var socket = new PacketPeerUDP();
#desc // Server
#desc socket.SetDestAddress("127.0.0.1", 789);
#desc socket.PutPacket("Time to stop".ToAscii());
#desc 
#desc // Client
#desc while (socket.Wait() == OK)
#desc {
#desc string data = socket.GetPacket().GetStringFromASCII();
#desc if (data == "Time to stop")
#desc {
#desc return;
#desc }
#desc }
#desc [/csharp]
#desc [/codeblocks]
func wait() -> int:
	pass;


