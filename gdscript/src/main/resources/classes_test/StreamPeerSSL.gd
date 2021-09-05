extends StreamPeer
class_name StreamPeerSSL

const STATUS_DISCONNECTED = 0;
const STATUS_HANDSHAKING = 1;
const STATUS_CONNECTED = 2;
const STATUS_ERROR = 3;
const STATUS_ERROR_HOSTNAME_MISMATCH = 4;

var blocking_handshake: bool setget set_blocking_handshake_enabled, is_blocking_handshake_enabled;

func accept_stream(stream: StreamPeer, private_key: CryptoKey, certificate: X509Certificate, chain: X509Certificate) -> int:
    pass;

func connect_to_stream(stream: StreamPeer, validate_certs: bool, for_hostname: String, valid_certificate: X509Certificate) -> int:
    pass;

func disconnect_from_stream() -> void:
    pass;

func get_status() -> int:
    pass;

func poll() -> void:
    pass;

