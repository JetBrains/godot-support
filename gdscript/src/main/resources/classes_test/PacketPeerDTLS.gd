extends PacketPeer
class_name PacketPeerDTLS

const STATUS_DISCONNECTED = 0;
const STATUS_HANDSHAKING = 1;
const STATUS_CONNECTED = 2;
const STATUS_ERROR = 3;
const STATUS_ERROR_HOSTNAME_MISMATCH = 4;


func connect_to_peer(packet_peer: PacketPeerUDP, validate_certs: bool, for_hostname: String, valid_certificate: X509Certificate) -> int:
    pass;

func disconnect_from_peer() -> void:
    pass;

func get_status() -> int:
    pass;

func poll() -> void:
    pass;

