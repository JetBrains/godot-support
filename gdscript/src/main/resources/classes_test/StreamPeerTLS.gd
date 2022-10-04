extends StreamPeer
#brief TLS stream peer.
#desc TLS stream peer. This object can be used to connect to an TLS server or accept a single TLS client connection.
#desc [b]Note:[/b] When exporting to Android, make sure to enable the [code]INTERNET[/code] permission in the Android export preset before exporting the project or using one-click deploy. Otherwise, network communication of any kind will be blocked by Android.
class_name StreamPeerTLS

#desc A status representing a [StreamPeerTLS] that is disconnected.
const STATUS_DISCONNECTED = 0;

#desc A status representing a [StreamPeerTLS] during handshaking.
const STATUS_HANDSHAKING = 1;

#desc A status representing a [StreamPeerTLS] that is connected to a host.
const STATUS_CONNECTED = 2;

#desc A status representing a [StreamPeerTLS] in error state.
const STATUS_ERROR = 3;

#desc An error status that shows a mismatch in the TLS certificate domain presented by the host and the domain requested for validation.
const STATUS_ERROR_HOSTNAME_MISMATCH = 4;


var blocking_handshake: bool;



#desc Accepts a peer connection as a server using the given [param private_key] and providing the given [param certificate] to the client. You can pass the optional [param chain] parameter to provide additional CA chain information along with the certificate.
func accept_stream(stream: StreamPeer, private_key: CryptoKey, certificate: X509Certificate, chain: X509Certificate) -> int:
	pass;

#desc Connects to a peer using an underlying [StreamPeer] [param stream]. If [param validate_certs] is [code]true[/code], [StreamPeerTLS] will validate that the certificate presented by the peer matches the [param for_hostname].
#desc [b]Note:[/b] Specifying a custom [param valid_certificate] is not supported in Web exports due to browsers restrictions.
func connect_to_stream(stream: StreamPeer, validate_certs: bool, for_hostname: String, valid_certificate: X509Certificate) -> int:
	pass;

#desc Disconnects from host.
func disconnect_from_stream() -> void:
	pass;

#desc Returns the status of the connection. See [enum Status] for values.
func get_status() -> int:
	pass;

#desc Returns the underlying [StreamPeer] connection, used in [method accept_stream] or [method connect_to_stream].
func get_stream() -> StreamPeer:
	pass;

#desc Poll the connection to check for incoming bytes. Call this right before [method StreamPeer.get_available_bytes] for it to work properly.
func poll() -> void:
	pass;


