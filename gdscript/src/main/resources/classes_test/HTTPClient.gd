#brief Low-level hyper-text transfer protocol client.
#desc Hyper-text transfer protocol client (sometimes called "User Agent"). Used to make HTTP requests to download web content, upload files and other data or to communicate with various services, among other use cases.
#desc See the [HTTPRequest] node for a higher-level alternative.
#desc [b]Note:[/b] This client only needs to connect to a host once (see [method connect_to_host]) to send multiple requests. Because of this, methods that take URLs usually take just the part after the host instead of the full URL, as the client is already connected to a host. See [method request] for a full example and to get started.
#desc A [HTTPClient] should be reused between multiple requests or to connect to different hosts instead of creating one client per request. Supports Transport Layer Security (TLS), including server certificate verification. HTTP status codes in the 2xx range indicate success, 3xx redirection (i.e. "try again, but over here"), 4xx something was wrong with the request, and 5xx something went wrong on the server's side.
#desc For more information on HTTP, see https://developer.mozilla.org/en-US/docs/Web/HTTP (or read RFC 2616 to get it straight from the source: https://tools.ietf.org/html/rfc2616).
#desc [b]Note:[/b] When exporting to Android, make sure to enable the [code]INTERNET[/code] permission in the Android export preset before exporting the project or using one-click deploy. Otherwise, network communication of any kind will be blocked by Android.
#desc [b]Note:[/b] It's recommended to use transport encryption (TLS) and to avoid sending sensitive information (such as login credentials) in HTTP GET URL parameters. Consider using HTTP POST requests or HTTP headers for such information instead.
#desc [b]Note:[/b] When performing HTTP requests from a project exported to Web, keep in mind the remote server may not allow requests from foreign origins due to [url=https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS]CORS[/url]. If you host the server in question, you should modify its backend to allow requests from foreign origins by adding the [code]Access-Control-Allow-Origin: *[/code] HTTP header.
#desc [b]Note:[/b] TLS support is currently limited to TLS 1.0, TLS 1.1, and TLS 1.2. Attempting to connect to a TLS 1.3-only server will return an error.
#desc [b]Warning:[/b] TLS certificate revocation and certificate pinning are currently not supported. Revoked certificates are accepted as long as they are otherwise valid. If this is a concern, you may want to use automatically managed certificates with a short validity period.
class_name HTTPClient

#desc HTTP GET method. The GET method requests a representation of the specified resource. Requests using GET should only retrieve data.
const METHOD_GET = 0;

#desc HTTP HEAD method. The HEAD method asks for a response identical to that of a GET request, but without the response body. This is useful to request metadata like HTTP headers or to check if a resource exists.
const METHOD_HEAD = 1;

#desc HTTP POST method. The POST method is used to submit an entity to the specified resource, often causing a change in state or side effects on the server. This is often used for forms and submitting data or uploading files.
const METHOD_POST = 2;

#desc HTTP PUT method. The PUT method asks to replace all current representations of the target resource with the request payload. (You can think of POST as "create or update" and PUT as "update", although many services tend to not make a clear distinction or change their meaning).
const METHOD_PUT = 3;

#desc HTTP DELETE method. The DELETE method requests to delete the specified resource.
const METHOD_DELETE = 4;

#desc HTTP OPTIONS method. The OPTIONS method asks for a description of the communication options for the target resource. Rarely used.
const METHOD_OPTIONS = 5;

#desc HTTP TRACE method. The TRACE method performs a message loop-back test along the path to the target resource. Returns the entire HTTP request received in the response body. Rarely used.
const METHOD_TRACE = 6;

#desc HTTP CONNECT method. The CONNECT method establishes a tunnel to the server identified by the target resource. Rarely used.
const METHOD_CONNECT = 7;

#desc HTTP PATCH method. The PATCH method is used to apply partial modifications to a resource.
const METHOD_PATCH = 8;

#desc Represents the size of the [enum Method] enum.
const METHOD_MAX = 9;

#desc Status: Disconnected from the server.
const STATUS_DISCONNECTED = 0;

#desc Status: Currently resolving the hostname for the given URL into an IP.
const STATUS_RESOLVING = 1;

#desc Status: DNS failure: Can't resolve the hostname for the given URL.
const STATUS_CANT_RESOLVE = 2;

#desc Status: Currently connecting to server.
const STATUS_CONNECTING = 3;

#desc Status: Can't connect to the server.
const STATUS_CANT_CONNECT = 4;

#desc Status: Connection established.
const STATUS_CONNECTED = 5;

#desc Status: Currently sending request.
const STATUS_REQUESTING = 6;

#desc Status: HTTP body received.
const STATUS_BODY = 7;

#desc Status: Error in HTTP connection.
const STATUS_CONNECTION_ERROR = 8;

#desc Status: Error in TLS handshake.
const STATUS_TLS_HANDSHAKE_ERROR = 9;

#desc HTTP status code [code]100 Continue[/code]. Interim response that indicates everything so far is OK and that the client should continue with the request (or ignore this status if already finished).
const RESPONSE_CONTINUE = 100;

#desc HTTP status code [code]101 Switching Protocol[/code]. Sent in response to an [code]Upgrade[/code] request header by the client. Indicates the protocol the server is switching to.
const RESPONSE_SWITCHING_PROTOCOLS = 101;

#desc HTTP status code [code]102 Processing[/code] (WebDAV). Indicates that the server has received and is processing the request, but no response is available yet.
const RESPONSE_PROCESSING = 102;

#desc HTTP status code [code]200 OK[/code]. The request has succeeded. Default response for successful requests. Meaning varies depending on the request. GET: The resource has been fetched and is transmitted in the message body. HEAD: The entity headers are in the message body. POST: The resource describing the result of the action is transmitted in the message body. TRACE: The message body contains the request message as received by the server.
const RESPONSE_OK = 200;

#desc HTTP status code [code]201 Created[/code]. The request has succeeded and a new resource has been created as a result of it. This is typically the response sent after a PUT request.
const RESPONSE_CREATED = 201;

#desc HTTP status code [code]202 Accepted[/code]. The request has been received but not yet acted upon. It is non-committal, meaning that there is no way in HTTP to later send an asynchronous response indicating the outcome of processing the request. It is intended for cases where another process or server handles the request, or for batch processing.
const RESPONSE_ACCEPTED = 202;

#desc HTTP status code [code]203 Non-Authoritative Information[/code]. This response code means returned meta-information set is not exact set as available from the origin server, but collected from a local or a third party copy. Except this condition, 200 OK response should be preferred instead of this response.
const RESPONSE_NON_AUTHORITATIVE_INFORMATION = 203;

#desc HTTP status code [code]204 No Content[/code]. There is no content to send for this request, but the headers may be useful. The user-agent may update its cached headers for this resource with the new ones.
const RESPONSE_NO_CONTENT = 204;

#desc HTTP status code [code]205 Reset Content[/code]. The server has fulfilled the request and desires that the client resets the "document view" that caused the request to be sent to its original state as received from the origin server.
const RESPONSE_RESET_CONTENT = 205;

#desc HTTP status code [code]206 Partial Content[/code]. This response code is used because of a range header sent by the client to separate download into multiple streams.
const RESPONSE_PARTIAL_CONTENT = 206;

#desc HTTP status code [code]207 Multi-Status[/code] (WebDAV). A Multi-Status response conveys information about multiple resources in situations where multiple status codes might be appropriate.
const RESPONSE_MULTI_STATUS = 207;

#desc HTTP status code [code]208 Already Reported[/code] (WebDAV). Used inside a DAV: propstat response element to avoid enumerating the internal members of multiple bindings to the same collection repeatedly.
const RESPONSE_ALREADY_REPORTED = 208;

#desc HTTP status code [code]226 IM Used[/code] (WebDAV). The server has fulfilled a GET request for the resource, and the response is a representation of the result of one or more instance-manipulations applied to the current instance.
const RESPONSE_IM_USED = 226;

#desc HTTP status code [code]300 Multiple Choice[/code]. The request has more than one possible responses and there is no standardized way to choose one of the responses. User-agent or user should choose one of them.
const RESPONSE_MULTIPLE_CHOICES = 300;

#desc HTTP status code [code]301 Moved Permanently[/code]. Redirection. This response code means the URI of requested resource has been changed. The new URI is usually included in the response.
const RESPONSE_MOVED_PERMANENTLY = 301;

#desc HTTP status code [code]302 Found[/code]. Temporary redirection. This response code means the URI of requested resource has been changed temporarily. New changes in the URI might be made in the future. Therefore, this same URI should be used by the client in future requests.
const RESPONSE_FOUND = 302;

#desc HTTP status code [code]303 See Other[/code]. The server is redirecting the user agent to a different resource, as indicated by a URI in the Location header field, which is intended to provide an indirect response to the original request.
const RESPONSE_SEE_OTHER = 303;

#desc HTTP status code [code]304 Not Modified[/code]. A conditional GET or HEAD request has been received and would have resulted in a 200 OK response if it were not for the fact that the condition evaluated to [code]false[/code].
const RESPONSE_NOT_MODIFIED = 304;

#desc HTTP status code [code]305 Use Proxy[/code]. [i]Deprecated. Do not use.[/i]
const RESPONSE_USE_PROXY = 305;

#desc HTTP status code [code]306 Switch Proxy[/code]. [i]Deprecated. Do not use.[/i]
const RESPONSE_SWITCH_PROXY = 306;

#desc HTTP status code [code]307 Temporary Redirect[/code]. The target resource resides temporarily under a different URI and the user agent MUST NOT change the request method if it performs an automatic redirection to that URI.
const RESPONSE_TEMPORARY_REDIRECT = 307;

#desc HTTP status code [code]308 Permanent Redirect[/code]. The target resource has been assigned a new permanent URI and any future references to this resource ought to use one of the enclosed URIs.
const RESPONSE_PERMANENT_REDIRECT = 308;

#desc HTTP status code [code]400 Bad Request[/code]. The request was invalid. The server cannot or will not process the request due to something that is perceived to be a client error (e.g., malformed request syntax, invalid request message framing, invalid request contents, or deceptive request routing).
const RESPONSE_BAD_REQUEST = 400;

#desc HTTP status code [code]401 Unauthorized[/code]. Credentials required. The request has not been applied because it lacks valid authentication credentials for the target resource.
const RESPONSE_UNAUTHORIZED = 401;

#desc HTTP status code [code]402 Payment Required[/code]. This response code is reserved for future use. Initial aim for creating this code was using it for digital payment systems, however this is not currently used.
const RESPONSE_PAYMENT_REQUIRED = 402;

#desc HTTP status code [code]403 Forbidden[/code]. The client does not have access rights to the content, i.e. they are unauthorized, so server is rejecting to give proper response. Unlike [code]401[/code], the client's identity is known to the server.
const RESPONSE_FORBIDDEN = 403;

#desc HTTP status code [code]404 Not Found[/code]. The server can not find requested resource. Either the URL is not recognized or the endpoint is valid but the resource itself does not exist. May also be sent instead of 403 to hide existence of a resource if the client is not authorized.
const RESPONSE_NOT_FOUND = 404;

#desc HTTP status code [code]405 Method Not Allowed[/code]. The request's HTTP method is known by the server but has been disabled and cannot be used. For example, an API may forbid DELETE-ing a resource. The two mandatory methods, GET and HEAD, must never be disabled and should not return this error code.
const RESPONSE_METHOD_NOT_ALLOWED = 405;

#desc HTTP status code [code]406 Not Acceptable[/code]. The target resource does not have a current representation that would be acceptable to the user agent, according to the proactive negotiation header fields received in the request. Used when negotiation content.
const RESPONSE_NOT_ACCEPTABLE = 406;

#desc HTTP status code [code]407 Proxy Authentication Required[/code]. Similar to 401 Unauthorized, but it indicates that the client needs to authenticate itself in order to use a proxy.
const RESPONSE_PROXY_AUTHENTICATION_REQUIRED = 407;

#desc HTTP status code [code]408 Request Timeout[/code]. The server did not receive a complete request message within the time that it was prepared to wait.
const RESPONSE_REQUEST_TIMEOUT = 408;

#desc HTTP status code [code]409 Conflict[/code]. The request could not be completed due to a conflict with the current state of the target resource. This code is used in situations where the user might be able to resolve the conflict and resubmit the request.
const RESPONSE_CONFLICT = 409;

#desc HTTP status code [code]410 Gone[/code]. The target resource is no longer available at the origin server and this condition is likely permanent.
const RESPONSE_GONE = 410;

#desc HTTP status code [code]411 Length Required[/code]. The server refuses to accept the request without a defined Content-Length header.
const RESPONSE_LENGTH_REQUIRED = 411;

#desc HTTP status code [code]412 Precondition Failed[/code]. One or more conditions given in the request header fields evaluated to [code]false[/code] when tested on the server.
const RESPONSE_PRECONDITION_FAILED = 412;

#desc HTTP status code [code]413 Entity Too Large[/code]. The server is refusing to process a request because the request payload is larger than the server is willing or able to process.
const RESPONSE_REQUEST_ENTITY_TOO_LARGE = 413;

#desc HTTP status code [code]414 Request-URI Too Long[/code]. The server is refusing to service the request because the request-target is longer than the server is willing to interpret.
const RESPONSE_REQUEST_URI_TOO_LONG = 414;

#desc HTTP status code [code]415 Unsupported Media Type[/code]. The origin server is refusing to service the request because the payload is in a format not supported by this method on the target resource.
const RESPONSE_UNSUPPORTED_MEDIA_TYPE = 415;

#desc HTTP status code [code]416 Requested Range Not Satisfiable[/code]. None of the ranges in the request's Range header field overlap the current extent of the selected resource or the set of ranges requested has been rejected due to invalid ranges or an excessive request of small or overlapping ranges.
const RESPONSE_REQUESTED_RANGE_NOT_SATISFIABLE = 416;

#desc HTTP status code [code]417 Expectation Failed[/code]. The expectation given in the request's Expect header field could not be met by at least one of the inbound servers.
const RESPONSE_EXPECTATION_FAILED = 417;

#desc HTTP status code [code]418 I'm A Teapot[/code]. Any attempt to brew coffee with a teapot should result in the error code "418 I'm a teapot". The resulting entity body MAY be short and stout.
const RESPONSE_IM_A_TEAPOT = 418;

#desc HTTP status code [code]421 Misdirected Request[/code]. The request was directed at a server that is not able to produce a response. This can be sent by a server that is not configured to produce responses for the combination of scheme and authority that are included in the request URI.
const RESPONSE_MISDIRECTED_REQUEST = 421;

#desc HTTP status code [code]422 Unprocessable Entity[/code] (WebDAV). The server understands the content type of the request entity (hence a 415 Unsupported Media Type status code is inappropriate), and the syntax of the request entity is correct (thus a 400 Bad Request status code is inappropriate) but was unable to process the contained instructions.
const RESPONSE_UNPROCESSABLE_ENTITY = 422;

#desc HTTP status code [code]423 Locked[/code] (WebDAV). The source or destination resource of a method is locked.
const RESPONSE_LOCKED = 423;

#desc HTTP status code [code]424 Failed Dependency[/code] (WebDAV). The method could not be performed on the resource because the requested action depended on another action and that action failed.
const RESPONSE_FAILED_DEPENDENCY = 424;

#desc HTTP status code [code]426 Upgrade Required[/code]. The server refuses to perform the request using the current protocol but might be willing to do so after the client upgrades to a different protocol.
const RESPONSE_UPGRADE_REQUIRED = 426;

#desc HTTP status code [code]428 Precondition Required[/code]. The origin server requires the request to be conditional.
const RESPONSE_PRECONDITION_REQUIRED = 428;

#desc HTTP status code [code]429 Too Many Requests[/code]. The user has sent too many requests in a given amount of time (see "rate limiting"). Back off and increase time between requests or try again later.
const RESPONSE_TOO_MANY_REQUESTS = 429;

#desc HTTP status code [code]431 Request Header Fields Too Large[/code]. The server is unwilling to process the request because its header fields are too large. The request MAY be resubmitted after reducing the size of the request header fields.
const RESPONSE_REQUEST_HEADER_FIELDS_TOO_LARGE = 431;

#desc HTTP status code [code]451 Response Unavailable For Legal Reasons[/code]. The server is denying access to the resource as a consequence of a legal demand.
const RESPONSE_UNAVAILABLE_FOR_LEGAL_REASONS = 451;

#desc HTTP status code [code]500 Internal Server Error[/code]. The server encountered an unexpected condition that prevented it from fulfilling the request.
const RESPONSE_INTERNAL_SERVER_ERROR = 500;

#desc HTTP status code [code]501 Not Implemented[/code]. The server does not support the functionality required to fulfill the request.
const RESPONSE_NOT_IMPLEMENTED = 501;

#desc HTTP status code [code]502 Bad Gateway[/code]. The server, while acting as a gateway or proxy, received an invalid response from an inbound server it accessed while attempting to fulfill the request. Usually returned by load balancers or proxies.
const RESPONSE_BAD_GATEWAY = 502;

#desc HTTP status code [code]503 Service Unavailable[/code]. The server is currently unable to handle the request due to a temporary overload or scheduled maintenance, which will likely be alleviated after some delay. Try again later.
const RESPONSE_SERVICE_UNAVAILABLE = 503;

#desc HTTP status code [code]504 Gateway Timeout[/code]. The server, while acting as a gateway or proxy, did not receive a timely response from an upstream server it needed to access in order to complete the request. Usually returned by load balancers or proxies.
const RESPONSE_GATEWAY_TIMEOUT = 504;

#desc HTTP status code [code]505 HTTP Version Not Supported[/code]. The server does not support, or refuses to support, the major version of HTTP that was used in the request message.
const RESPONSE_HTTP_VERSION_NOT_SUPPORTED = 505;

#desc HTTP status code [code]506 Variant Also Negotiates[/code]. The server has an internal configuration error: the chosen variant resource is configured to engage in transparent content negotiation itself, and is therefore not a proper end point in the negotiation process.
const RESPONSE_VARIANT_ALSO_NEGOTIATES = 506;

#desc HTTP status code [code]507 Insufficient Storage[/code]. The method could not be performed on the resource because the server is unable to store the representation needed to successfully complete the request.
const RESPONSE_INSUFFICIENT_STORAGE = 507;

#desc HTTP status code [code]508 Loop Detected[/code]. The server terminated an operation because it encountered an infinite loop while processing a request with "Depth: infinity". This status indicates that the entire operation failed.
const RESPONSE_LOOP_DETECTED = 508;

#desc HTTP status code [code]510 Not Extended[/code]. The policy for accessing the resource has not been met in the request. The server should send back all the information necessary for the client to issue an extended request.
const RESPONSE_NOT_EXTENDED = 510;

#desc HTTP status code [code]511 Network Authentication Required[/code]. The client needs to authenticate to gain network access.
const RESPONSE_NETWORK_AUTH_REQUIRED = 511;


#desc If [code]true[/code], execution will block until all data is read from the response.
var blocking_mode_enabled: bool;

#desc The connection to use for this client.
var connection: StreamPeer;

#desc The size of the buffer used and maximum bytes to read per iteration. See [method read_response_body_chunk].
var read_chunk_size: int;



#desc Closes the current connection, allowing reuse of this [HTTPClient].
func close() -> void:
	pass;

#desc Connects to a host. This needs to be done before any requests are sent.
#desc The host should not have http:// prepended but will strip the protocol identifier if provided.
#desc If no [param port] is specified (or [code]-1[/code] is used), it is automatically set to 80 for HTTP and 443 for HTTPS (if [param use_tls] is enabled).
#desc [param verify_host] will check the TLS identity of the host if set to [code]true[/code].
func connect_to_host(host: String, port: int, use_tls: bool, verify_host: bool) -> int:
	pass;

#desc Returns the response's body length.
#desc [b]Note:[/b] Some Web servers may not send a body length. In this case, the value returned will be [code]-1[/code]. If using chunked transfer encoding, the body length will also be [code]-1[/code].
func get_response_body_length() -> int:
	pass;

#desc Returns the response's HTTP status code.
func get_response_code() -> int:
	pass;

#desc Returns the response headers.
func get_response_headers() -> PackedStringArray:
	pass;

#desc Returns all response headers as a Dictionary of structure [code]{ "key": "value1; value2" }[/code] where the case-sensitivity of the keys and values is kept like the server delivers it. A value is a simple String, this string can have more than one value where "; " is used as separator.
#desc [b]Example:[/b]
#desc [codeblock]
#desc {
#desc "content-length": 12,
#desc "Content-Type": "application/json; charset=UTF-8",
#desc }
#desc [/codeblock]
func get_response_headers_as_dictionary() -> Dictionary:
	pass;

#desc Returns a [enum Status] constant. Need to call [method poll] in order to get status updates.
func get_status() -> int:
	pass;

#desc If [code]true[/code], this [HTTPClient] has a response available.
func has_response() -> bool:
	pass;

#desc If [code]true[/code], this [HTTPClient] has a response that is chunked.
func is_response_chunked() -> bool:
	pass;

#desc This needs to be called in order to have any request processed. Check results with [method get_status].
func poll() -> int:
	pass;

#desc Generates a GET/POST application/x-www-form-urlencoded style query string from a provided dictionary, e.g.:
#desc [codeblocks]
#desc [gdscript]
#desc var fields = {"username": "user", "password": "pass"}
#desc var query_string = http_client.query_string_from_dict(fields)
#desc # Returns "username=user&password=pass"
#desc [/gdscript]
#desc [csharp]
#desc var fields = new Godot.Collections.Dictionary { { "username", "user" }, { "password", "pass" } };
#desc string queryString = new HTTPClient().QueryStringFromDict(fields);
#desc // Returns "username=user&password=pass"
#desc [/csharp]
#desc [/codeblocks]
#desc Furthermore, if a key has a [code]null[/code] value, only the key itself is added, without equal sign and value. If the value is an array, for each value in it a pair with the same key is added.
#desc [codeblocks]
#desc [gdscript]
#desc var fields = {"single": 123, "not_valued": null, "multiple": [22, 33, 44]}
#desc var query_string = http_client.query_string_from_dict(fields)
#desc # Returns "single=123&not_valued&multiple=22&multiple=33&multiple=44"
#desc [/gdscript]
#desc [csharp]
#desc var fields = new Godot.Collections.Dictionary{{"single", 123}, {"notValued", null}, {"multiple", new Godot.Collections.Array{22, 33, 44}}};
#desc string queryString = new HTTPClient().QueryStringFromDict(fields);
#desc // Returns "single=123&not_valued&multiple=22&multiple=33&multiple=44"
#desc [/csharp]
#desc [/codeblocks]
func query_string_from_dict() -> String:
	pass;

#desc Reads one chunk from the response.
func read_response_body_chunk() -> PackedByteArray:
	pass;

#desc Sends a request to the connected host.
#desc The URL parameter is usually just the part after the host, so for [code]https://somehost.com/index.php[/code], it is [code]/index.php[/code]. When sending requests to an HTTP proxy server, it should be an absolute URL. For [constant HTTPClient.METHOD_OPTIONS] requests, [code]*[/code] is also allowed. For [constant HTTPClient.METHOD_CONNECT] requests, it should be the authority component ([code]host:port[/code]).
#desc Headers are HTTP request headers. For available HTTP methods, see [enum Method].
#desc To create a POST request with query strings to push to the server, do:
#desc [codeblocks]
#desc [gdscript]
#desc var fields = {"username" : "user", "password" : "pass"}
#desc var query_string = http_client.query_string_from_dict(fields)
#desc var headers = ["Content-Type: application/x-www-form-urlencoded", "Content-Length: " + str(query_string.length())]
#desc var result = http_client.request(http_client.METHOD_POST, "/index.php", headers, query_string)
#desc [/gdscript]
#desc [csharp]
#desc var fields = new Godot.Collections.Dictionary { { "username", "user" }, { "password", "pass" } };
#desc string queryString = new HTTPClient().QueryStringFromDict(fields);
#desc string[] headers = {"Content-Type: application/x-www-form-urlencoded", "Content-Length: " + queryString.Length};
#desc var result = new HTTPClient().Request(HTTPClient.Method.Post, "index.php", headers, queryString);
#desc [/csharp]
#desc [/codeblocks]
#desc [b]Note:[/b] The [param body] parameter is ignored if [param method] is [constant HTTPClient.METHOD_GET]. This is because GET methods can't contain request data. As a workaround, you can pass request data as a query string in the URL. See [method String.uri_encode] for an example.
func request(method: int, url: String, headers: PackedStringArray, body: String) -> int:
	pass;

#desc Sends a raw request to the connected host.
#desc The URL parameter is usually just the part after the host, so for [code]https://somehost.com/index.php[/code], it is [code]/index.php[/code]. When sending requests to an HTTP proxy server, it should be an absolute URL. For [constant HTTPClient.METHOD_OPTIONS] requests, [code]*[/code] is also allowed. For [constant HTTPClient.METHOD_CONNECT] requests, it should be the authority component ([code]host:port[/code]).
#desc Headers are HTTP request headers. For available HTTP methods, see [enum Method].
#desc Sends the body data raw, as a byte array and does not encode it in any way.
func request_raw(method: int, url: String, headers: PackedStringArray, body: PackedByteArray) -> int:
	pass;

#desc Sets the proxy server for HTTP requests.
#desc The proxy server is unset if [param host] is empty or [param port] is -1.
func set_http_proxy(host: String, port: int) -> void:
	pass;

#desc Sets the proxy server for HTTPS requests.
#desc The proxy server is unset if [param host] is empty or [param port] is -1.
func set_https_proxy(host: String, port: int) -> void:
	pass;


