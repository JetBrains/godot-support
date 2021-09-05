extends RefCounted
class_name MultiplayerAPI

const RPC_MODE_DISABLED = 0;
const RPC_MODE_REMOTE = 1;
const RPC_MODE_MASTER = 2;
const RPC_MODE_PUPPET = 3;
const RPC_MODE_REMOTESYNC = 4;
const RPC_MODE_MASTERSYNC = 5;
const RPC_MODE_PUPPETSYNC = 6;

var allow_object_decoding: bool setget set_allow_object_decoding, is_object_decoding_allowed;
var network_peer: MultiplayerPeer setget set_network_peer, get_network_peer;
var refuse_new_network_connections: bool setget set_refuse_new_network_connections, is_refusing_new_network_connections;
var root_node: Node setget set_root_node, get_root_node;

func clear() -> void:
    pass;

func get_network_connected_peers() -> PackedInt32Array:
    pass;

func get_network_unique_id() -> int:
    pass;

func get_rpc_sender_id() -> int:
    pass;

func has_network_peer() -> bool:
    pass;

func is_network_server() -> bool:
    pass;

func poll() -> void:
    pass;

func send_bytes(bytes: PackedByteArray, id: int, mode: int) -> int:
    pass;

