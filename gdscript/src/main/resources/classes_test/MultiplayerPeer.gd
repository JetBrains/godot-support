#brief A high-level network interface to simplify multiplayer interactions.
#desc Manages the connection to multiplayer peers. Assigns unique IDs to each client connected to the server. See also [MultiplayerAPI].
#desc [b]Note:[/b] The high-level multiplayer API protocol is an implementation detail and isn't meant to be used by non-Godot servers. It may change without notice.
#desc [b]Note:[/b] When exporting to Android, make sure to enable the [code]INTERNET[/code] permission in the Android export preset before exporting the project or using one-click deploy. Otherwise, network communication of any kind will be blocked by Android.
class_name MultiplayerPeer

#desc The ongoing connection disconnected.
const CONNECTION_DISCONNECTED = 0;

#desc A connection attempt is ongoing.
const CONNECTION_CONNECTING = 1;

#desc The connection attempt succeeded.
const CONNECTION_CONNECTED = 2;

#desc Packets are sent to the server and then redistributed to other peers.
const TARGET_PEER_BROADCAST = 0;

#desc Packets are sent to the server alone.
const TARGET_PEER_SERVER = 1;

#desc Packets are not acknowledged, no resend attempts are made for lost packets. Packets may arrive in any order. Potentially faster than [constant TRANSFER_MODE_UNRELIABLE_ORDERED]. Use for non-critical data, and always consider whether the order matters.
const TRANSFER_MODE_UNRELIABLE = 0;

#desc Packets are not acknowledged, no resend attempts are made for lost packets. Packets are received in the order they were sent in. Potentially faster than [constant TRANSFER_MODE_RELIABLE]. Use for non-critical data or data that would be outdated if received late due to resend attempt(s) anyway, for example movement and positional data.
const TRANSFER_MODE_UNRELIABLE_ORDERED = 1;

#desc Packets must be received and resend attempts should be made until the packets are acknowledged. Packets must be received in the order they were sent in. Most reliable transfer mode, but potentially the slowest due to the overhead. Use for critical data that must be transmitted and arrive in order, for example an ability being triggered or a chat message. Consider carefully if the information really is critical, and use sparingly.
const TRANSFER_MODE_RELIABLE = 2;


#desc If [code]true[/code], this [MultiplayerPeer] refuses new connections.
var refuse_new_connections: bool;

#desc The channel to use to send packets. Many network APIs such as ENet and WebRTC allow the creation of multiple independent channels which behaves, in a way, like separate connections. This means that reliable data will only block delivery of other packets on that channel, and ordering will only be in respect to the channel the packet is being sent on. Using different channels to send [b]different and independent[/b] state updates is a common way to optimize network usage and decrease latency in fast-paced games.
#desc [b]Note:[/b] The default channel ([code]0[/code]) actually works as 3 separate channels (one for each [enum TransferMode]) so that [constant TRANSFER_MODE_RELIABLE] and [constant TRANSFER_MODE_UNRELIABLE_ORDERED] does not interact with each other by default. Refer to the specific network API documentation (e.g. ENet or WebRTC) to learn how to set up channels correctly.
var transfer_channel: int;

#desc The manner in which to send packets to the [code]target_peer[/code]. See [enum TransferMode].
var transfer_mode: int;



#desc Returns a randomly generated integer that can be used as a network unique ID.
func generate_unique_id() -> int:
	pass;

#desc Returns the current state of the connection. See [enum ConnectionStatus].
func get_connection_status() -> int:
	pass;

#desc Returns the ID of the [MultiplayerPeer] who sent the most recent packet.
func get_packet_peer() -> int:
	pass;

#desc Returns the ID of this [MultiplayerPeer].
func get_unique_id() -> int:
	pass;

#desc Waits up to 1 second to receive a new network event.
func poll() -> void:
	pass;

#desc Sets the peer to which packets will be sent.
#desc The [param id] can be one of: [constant TARGET_PEER_BROADCAST] to send to all connected peers, [constant TARGET_PEER_SERVER] to send to the peer acting as server, a valid peer ID to send to that specific peer, a negative peer ID to send to all peers except that one. By default, the target peer is [constant TARGET_PEER_BROADCAST].
func set_target_peer(id: int) -> void:
	pass;


