extends Node
class_name HTTPRequest
const RESULT_SUCCESS = 0;
const RESULT_CHUNKED_BODY_SIZE_MISMATCH = 1;
const RESULT_CANT_CONNECT = 2;
const RESULT_CANT_RESOLVE = 3;
const RESULT_CONNECTION_ERROR = 4;
const RESULT_SSL_HANDSHAKE_ERROR = 5;
const RESULT_NO_RESPONSE = 6;
const RESULT_BODY_SIZE_LIMIT_EXCEEDED = 7;
const RESULT_BODY_DECOMPRESS_FAILED = 8;
const RESULT_REQUEST_FAILED = 9;
const RESULT_DOWNLOAD_FILE_CANT_OPEN = 10;
const RESULT_DOWNLOAD_FILE_WRITE_ERROR = 11;
const RESULT_REDIRECT_LIMIT_REACHED = 12;
const RESULT_TIMEOUT = 13;

var accept_gzip: bool;
var body_size_limit: int;
var download_chunk_size: int;
var download_file: String;
var max_redirects: int;
var timeout: int;
var use_threads: bool;

func cancel_request() -> void:
    pass;
func get_body_size() -> int:
    pass;
func get_downloaded_bytes() -> int:
    pass;
func get_http_client_status() -> int:
    pass;
func request(url: String, custom_headers: PackedStringArray, ssl_validate_domain: bool, method: int, request_data: String) -> int:
    pass;
func request_raw(url: String, custom_headers: PackedStringArray, ssl_validate_domain: bool, method: int, request_data_raw: PackedByteArray) -> int:
    pass;
