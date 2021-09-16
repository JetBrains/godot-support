extends Object
class_name JSONRPC
const PARSE_ERROR = -32700;
const INVALID_REQUEST = -32600;
const METHOD_NOT_FOUND = -32601;
const INVALID_PARAMS = -32602;
const INTERNAL_ERROR = -32603;


func make_notification(method: String, params: Variant) -> Dictionary:
    pass;
func make_request(method: String, params: Variant, id: Variant) -> Dictionary:
    pass;
func make_response(result: Variant, id: Variant) -> Dictionary:
    pass;
func make_response_error(code: int, message: String, id: Variant) -> Dictionary:
    pass;
func process_action(action: Variant, recurse: bool) -> Variant:
    pass;
func process_string(action: String) -> String:
    pass;
func set_scope(scope: String, target: Object) -> void:
    pass;
