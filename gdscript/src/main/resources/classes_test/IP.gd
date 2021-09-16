extends Object
class_name IP
const RESOLVER_STATUS_NONE = 0;
const RESOLVER_STATUS_WAITING = 1;
const RESOLVER_STATUS_DONE = 2;
const RESOLVER_STATUS_ERROR = 3;
const RESOLVER_MAX_QUERIES = 32;
const RESOLVER_INVALID_ID = -1;
const TYPE_NONE = 0;
const TYPE_IPV4 = 1;
const TYPE_IPV6 = 2;
const TYPE_ANY = 3;


func clear_cache(hostname: String) -> void:
    pass;
func erase_resolve_item(id: int) -> void:
    pass;
func get_local_addresses() -> Array:
    pass;
func get_local_interfaces() -> Array:
    pass;
func get_resolve_item_address(id: int) -> String:
    pass;
func get_resolve_item_addresses(id: int) -> Array:
    pass;
func get_resolve_item_status(id: int) -> int:
    pass;
func resolve_hostname(host: String, ip_type: int) -> String:
    pass;
func resolve_hostname_addresses(host: String, ip_type: int) -> Array:
    pass;
func resolve_hostname_queue_item(host: String, ip_type: int) -> int:
    pass;
