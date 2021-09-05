extends PacketPeer
class_name MultiplayerPeer

const TRANSFER_MODE_UNRELIABLE = 0;
const TRANSFER_MODE_UNRELIABLE_ORDERED = 1;
const TRANSFER_MODE_RELIABLE = 2;
const CONNECTION_DISCONNECTED = 0;
const CONNECTION_CONNECTING = 1;
const CONNECTION_CONNECTED = 2;
const TARGET_PEER_BROADCAST = 0;
const TARGET_PEER_SERVER = 1;

var refuse_new_connections: bool setget set_refuse_new_connections, is_refusing_new_connections;
var transfer_mode: int setget set_transfer_mode, get_transfer_mode;

func get_connection_status() -> int:
    pass;

func get_packet_peer() -> int:
    pass;

func get_unique_id() -> int:
    pass;

func poll() -> void:
    pass;

func set_target_peer(id: int) -> void:
    pass;

