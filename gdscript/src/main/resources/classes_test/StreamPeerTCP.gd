extends StreamPeer
class_name StreamPeerTCP

const STATUS_NONE = 0;
const STATUS_CONNECTING = 1;
const STATUS_CONNECTED = 2;
const STATUS_ERROR = 3;


func bind(port: int, host: String) -> int:
    pass;

func connect_to_host(host: String, port: int) -> int:
    pass;

func disconnect_from_host() -> void:
    pass;

func get_connected_host() -> String:
    pass;

func get_connected_port() -> int:
    pass;

func get_local_port() -> int:
    pass;

func get_status() -> int:
    pass;

func is_connected_to_host() -> bool:
    pass;

func set_no_delay(enabled: bool) -> void:
    pass;

