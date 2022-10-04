#brief A helper to handle dictionaries which look like JSONRPC documents.
#desc [url=https://www.jsonrpc.org/]JSON-RPC[/url] is a standard which wraps a method call in a [JSON] object. The object has a particular structure and identifies which method is called, the parameters to that function, and carries an ID to keep track of responses. This class implements that standard on top of [Dictionary]; you will have to convert between a [Dictionary] and [JSON] with other functions.
class_name JSONRPC

const PARSE_ERROR = -32700;

const INVALID_REQUEST = -32600;

#desc A method call was requested but no function of that name existed in the JSONRPC subclass.
const METHOD_NOT_FOUND = -32601;

const INVALID_PARAMS = -32602;

const INTERNAL_ERROR = -32603;




#desc Returns a dictionary in the form of a JSON-RPC notification. Notifications are one-shot messages which do not expect a response.
#desc - [param method]: Name of the method being called.
#desc - [param params]: An array or dictionary of parameters being passed to the method.
func make_notification(method: String, params: Variant) -> Dictionary:
	pass;

#desc Returns a dictionary in the form of a JSON-RPC request. Requests are sent to a server with the expectation of a response. The ID field is used for the server to specify which exact request it is responding to.
#desc - [param method]: Name of the method being called.
#desc - [param params]: An array or dictionary of parameters being passed to the method.
#desc - [param id]: Uniquely identifies this request. The server is expected to send a response with the same ID.
func make_request(method: String, params: Variant, id: Variant) -> Dictionary:
	pass;

#desc When a server has received and processed a request, it is expected to send a response. If you did not want a response then you need to have sent a Notification instead.
#desc - [param result]: The return value of the function which was called.
#desc - [param id]: The ID of the request this response is targeted to.
func make_response(result: Variant, id: Variant) -> Dictionary:
	pass;

#desc Creates a response which indicates a previous reply has failed in some way.
#desc - [param code]: The error code corresponding to what kind of error this is. See the [enum ErrorCode] constants.
#desc - [param message]: A custom message about this error.
#desc - [param id]: The request this error is a response to.
func make_response_error(code: int, message: String, id: Variant) -> Dictionary:
	pass;

#desc Given a Dictionary which takes the form of a JSON-RPC request: unpack the request and run it. Methods are resolved by looking at the field called "method" and looking for an equivalently named function in the JSONRPC object. If one is found that method is called.
#desc To add new supported methods extend the JSONRPC class and call [method process_action] on your subclass.
#desc [param action]: The action to be run, as a Dictionary in the form of a JSON-RPC request or notification.
func process_action(action: Variant, recurse: bool) -> Variant:
	pass;

func process_string() -> String:
	pass;

func set_scope(scope: String, target: Object) -> void:
	pass;


