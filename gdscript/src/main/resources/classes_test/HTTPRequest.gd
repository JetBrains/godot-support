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

var accept_gzip: bool setget set_accept_gzip, is_accepting_gzip;
var body_size_limit: int setget set_body_size_limit, get_body_size_limit;
var download_chunk_size: int setget set_download_chunk_size, get_download_chunk_size;
var download_file: String setget set_download_file, get_download_file;
var max_redirects: int setget set_max_redirects, get_max_redirects;
var timeout: int setget set_timeout, get_timeout;
var use_threads: bool setget set_use_threads, is_using_threads;

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

