extends PacketPeer
#brief DTLS packet peer.
#desc This class represents a DTLS peer connection. It can be used to connect to a DTLS server, and is returned by [method DTLSServer.take_connection].
#desc [b]Note:[/b] When exporting to Android, make sure to enable the [code]INTERNET[/code] permission in the Android export preset before exporting the project or using one-click deploy. Otherwise, network communication of any kind will be blocked by Android.
#desc [b]Warning:[/b] TLS certificate revocation and certificate pinning are currently not supported. Revoked certificates are accepted as long as they are otherwise valid. If this is a concern, you may want to use automatically managed certificates with a short validity period.
class_name PacketPeerDTLS

#desc A status representing a [PacketPeerDTLS] that is disconnected.
const STATUS_DISCONNECTED = 0;

#desc A status representing a [PacketPeerDTLS] that is currently performing the handshake with a remote peer.
const STATUS_HANDSHAKING = 1;

#desc A status representing a [PacketPeerDTLS] that is connected to a remote peer.
const STATUS_CONNECTED = 2;

#desc A status representing a [PacketPeerDTLS] in a generic error state.
const STATUS_ERROR = 3;

#desc An error status that shows a mismatch in the DTLS certificate domain presented by the host and the domain requested for validation.
const STATUS_ERROR_HOSTNAME_MISMATCH = 4;




#desc Connects a [param packet_peer] beginning the DTLS handshake using the underlying [PacketPeerUDP] which must be connected (see [method PacketPeerUDP.connect_to_host]). If [param validate_certs] is [code]true[/code], [PacketPeerDTLS] will validate that the certificate presented by the remote peer and match it with the [param for_hostname] argument. You can specify a custom [X509Certificate] to use for validation via the [param valid_certificate] argument.
func connect_to_peer(packet_peer: PacketPeerUDP, validate_certs: bool, for_hostname: String, valid_certificate: X509Certificate) -> int:
	pass;

#desc Disconnects this peer, terminating the DTLS session.
func disconnect_from_peer() -> void:
	pass;

#desc Returns the status of the connection. See [enum Status] for values.
func get_status() -> int:
	pass;

#desc Poll the connection to check for incoming packets. Call this frequently to update the status and keep the connection working.
func poll() -> void:
	pass;


