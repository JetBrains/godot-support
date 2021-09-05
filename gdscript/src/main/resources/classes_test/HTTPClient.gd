extends RefCounted
class_name HTTPClient

const METHOD_GET = 0;
const METHOD_HEAD = 1;
const METHOD_POST = 2;
const METHOD_PUT = 3;
const METHOD_DELETE = 4;
const METHOD_OPTIONS = 5;
const METHOD_TRACE = 6;
const METHOD_CONNECT = 7;
const METHOD_PATCH = 8;
const METHOD_MAX = 9;
const STATUS_DISCONNECTED = 0;
const STATUS_RESOLVING = 1;
const STATUS_CANT_RESOLVE = 2;
const STATUS_CONNECTING = 3;
const STATUS_CANT_CONNECT = 4;
const STATUS_CONNECTED = 5;
const STATUS_REQUESTING = 6;
const STATUS_BODY = 7;
const STATUS_CONNECTION_ERROR = 8;
const STATUS_SSL_HANDSHAKE_ERROR = 9;
const RESPONSE_CONTINUE = 100;
const RESPONSE_SWITCHING_PROTOCOLS = 101;
const RESPONSE_PROCESSING = 102;
const RESPONSE_OK = 200;
const RESPONSE_CREATED = 201;
const RESPONSE_ACCEPTED = 202;
const RESPONSE_NON_AUTHORITATIVE_INFORMATION = 203;
const RESPONSE_NO_CONTENT = 204;
const RESPONSE_RESET_CONTENT = 205;
const RESPONSE_PARTIAL_CONTENT = 206;
const RESPONSE_MULTI_STATUS = 207;
const RESPONSE_ALREADY_REPORTED = 208;
const RESPONSE_IM_USED = 226;
const RESPONSE_MULTIPLE_CHOICES = 300;
const RESPONSE_MOVED_PERMANENTLY = 301;
const RESPONSE_FOUND = 302;
const RESPONSE_SEE_OTHER = 303;
const RESPONSE_NOT_MODIFIED = 304;
const RESPONSE_USE_PROXY = 305;
const RESPONSE_SWITCH_PROXY = 306;
const RESPONSE_TEMPORARY_REDIRECT = 307;
const RESPONSE_PERMANENT_REDIRECT = 308;
const RESPONSE_BAD_REQUEST = 400;
const RESPONSE_UNAUTHORIZED = 401;
const RESPONSE_PAYMENT_REQUIRED = 402;
const RESPONSE_FORBIDDEN = 403;
const RESPONSE_NOT_FOUND = 404;
const RESPONSE_METHOD_NOT_ALLOWED = 405;
const RESPONSE_NOT_ACCEPTABLE = 406;
const RESPONSE_PROXY_AUTHENTICATION_REQUIRED = 407;
const RESPONSE_REQUEST_TIMEOUT = 408;
const RESPONSE_CONFLICT = 409;
const RESPONSE_GONE = 410;
const RESPONSE_LENGTH_REQUIRED = 411;
const RESPONSE_PRECONDITION_FAILED = 412;
const RESPONSE_REQUEST_ENTITY_TOO_LARGE = 413;
const RESPONSE_REQUEST_URI_TOO_LONG = 414;
const RESPONSE_UNSUPPORTED_MEDIA_TYPE = 415;
const RESPONSE_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
const RESPONSE_EXPECTATION_FAILED = 417;
const RESPONSE_IM_A_TEAPOT = 418;
const RESPONSE_MISDIRECTED_REQUEST = 421;
const RESPONSE_UNPROCESSABLE_ENTITY = 422;
const RESPONSE_LOCKED = 423;
const RESPONSE_FAILED_DEPENDENCY = 424;
const RESPONSE_UPGRADE_REQUIRED = 426;
const RESPONSE_PRECONDITION_REQUIRED = 428;
const RESPONSE_TOO_MANY_REQUESTS = 429;
const RESPONSE_REQUEST_HEADER_FIELDS_TOO_LARGE = 431;
const RESPONSE_UNAVAILABLE_FOR_LEGAL_REASONS = 451;
const RESPONSE_INTERNAL_SERVER_ERROR = 500;
const RESPONSE_NOT_IMPLEMENTED = 501;
const RESPONSE_BAD_GATEWAY = 502;
const RESPONSE_SERVICE_UNAVAILABLE = 503;
const RESPONSE_GATEWAY_TIMEOUT = 504;
const RESPONSE_HTTP_VERSION_NOT_SUPPORTED = 505;
const RESPONSE_VARIANT_ALSO_NEGOTIATES = 506;
const RESPONSE_INSUFFICIENT_STORAGE = 507;
const RESPONSE_LOOP_DETECTED = 508;
const RESPONSE_NOT_EXTENDED = 510;
const RESPONSE_NETWORK_AUTH_REQUIRED = 511;

var blocking_mode_enabled: bool setget set_blocking_mode, is_blocking_mode_enabled;
var connection: StreamPeer setget set_connection, get_connection;
var read_chunk_size: int setget set_read_chunk_size, get_read_chunk_size;

func close() -> void:
    pass;

func connect_to_host(host: String, port: int, use_ssl: bool, verify_host: bool) -> int:
    pass;

func get_response_body_length() -> int:
    pass;

func get_response_code() -> int:
    pass;

func get_response_headers() -> PackedStringArray:
    pass;

func get_response_headers_as_dictionary() -> Dictionary:
    pass;

func get_status() -> int:
    pass;

func has_response() -> bool:
    pass;

func is_response_chunked() -> bool:
    pass;

func poll() -> int:
    pass;

func query_string_from_dict(fields: Dictionary) -> String:
    pass;

func read_response_body_chunk() -> PackedByteArray:
    pass;

func request(method: int, url: String, headers: PackedStringArray, body: String) -> int:
    pass;

func request_raw(method: int, url: String, headers: PackedStringArray, body: PackedByteArray) -> int:
    pass;

