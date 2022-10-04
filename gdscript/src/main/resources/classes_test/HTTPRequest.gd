extends Node
#brief A node with the ability to send HTTP(S) requests.
#desc A node with the ability to send HTTP requests. Uses [HTTPClient] internally.
#desc Can be used to make HTTP requests, i.e. download or upload files or web content via HTTP.
#desc [b]Warning:[/b] See the notes and warnings on [HTTPClient] for limitations, especially regarding TLS security.
#desc [b]Note:[/b] When exporting to Android, make sure to enable the [code]INTERNET[/code] permission in the Android export preset before exporting the project or using one-click deploy. Otherwise, network communication of any kind will be blocked by Android.
#desc [b]Example of contacting a REST API and printing one of its returned fields:[/b]
#desc [codeblocks]
#desc [gdscript]
#desc func _ready():
#desc # Create an HTTP request node and connect its completion signal.
#desc var http_request = HTTPRequest.new()
#desc add_child(http_request)
#desc http_request.request_completed.connect(self._http_request_completed)
#desc 
#desc # Perform a GET request. The URL below returns JSON as of writing.
#desc var error = http_request.request("https://httpbin.org/get")
#desc if error != OK:
#desc push_error("An error occurred in the HTTP request.")
#desc 
#desc # Perform a POST request. The URL below returns JSON as of writing.
#desc # Note: Don't make simultaneous requests using a single HTTPRequest node.
#desc # The snippet below is provided for reference only.
#desc var body = JSON.new().stringify({"name": "Godette"})
#desc error = http_request.request("https://httpbin.org/post", [], true, HTTPClient.METHOD_POST, body)
#desc if error != OK:
#desc push_error("An error occurred in the HTTP request.")
#desc 
#desc 
#desc # Called when the HTTP request is completed.
#desc func _http_request_completed(result, response_code, headers, body):
#desc var json = JSON.new()
#desc json.parse(body.get_string_from_utf8())
#desc var response = json.get_data()
#desc 
#desc # Will print the user agent string used by the HTTPRequest node (as recognized by httpbin.org).
#desc print(response.headers["User-Agent"])
#desc [/gdscript]
#desc [csharp]
#desc public override void _Ready()
#desc {
#desc // Create an HTTP request node and connect its completion signal.
#desc var httpRequest = new HTTPRequest();
#desc AddChild(httpRequest);
#desc httpRequest.RequestCompleted += HttpRequestCompleted;
#desc 
#desc // Perform a GET request. The URL below returns JSON as of writing.
#desc Error error = httpRequest.Request("https://httpbin.org/get");
#desc if (error != Error.Ok)
#desc {
#desc GD.PushError("An error occurred in the HTTP request.");
#desc }
#desc 
#desc // Perform a POST request. The URL below returns JSON as of writing.
#desc // Note: Don't make simultaneous requests using a single HTTPRequest node.
#desc // The snippet below is provided for reference only.
#desc string body = new JSON().Stringify(new Godot.Collections.Dictionary
#desc {
#desc { "name", "Godette" }
#desc });
#desc error = httpRequest.Request("https://httpbin.org/post", null, true, HTTPClient.Method.Post, body);
#desc if (error != Error.Ok)
#desc {
#desc GD.PushError("An error occurred in the HTTP request.");
#desc }
#desc }
#desc 
#desc // Called when the HTTP request is completed.
#desc private void HttpRequestCompleted(int result, int response_code, string[] headers, byte[] body)
#desc {
#desc var json = new JSON();
#desc json.Parse(body.GetStringFromUTF8());
#desc var response = json.GetData() as Godot.Collections.Dictionary;
#desc 
#desc // Will print the user agent string used by the HTTPRequest node (as recognized by httpbin.org).
#desc GD.Print((response["headers"] as Godot.Collections.Dictionary)["User-Agent"]);
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc [b]Example of loading and displaying an image using HTTPRequest:[/b]
#desc [codeblocks]
#desc [gdscript]
#desc func _ready():
#desc # Create an HTTP request node and connect its completion signal.
#desc var http_request = HTTPRequest.new()
#desc add_child(http_request)
#desc http_request.request_completed.connect(self._http_request_completed)
#desc 
#desc # Perform the HTTP request. The URL below returns a PNG image as of writing.
#desc var error = http_request.request("https://via.placeholder.com/512")
#desc if error != OK:
#desc push_error("An error occurred in the HTTP request.")
#desc 
#desc 
#desc # Called when the HTTP request is completed.
#desc func _http_request_completed(result, response_code, headers, body):
#desc if result != HTTPRequest.RESULT_SUCCESS:
#desc push_error("Image couldn't be downloaded. Try a different image.")
#desc 
#desc var image = Image.new()
#desc var error = image.load_png_from_buffer(body)
#desc if error != OK:
#desc push_error("Couldn't load the image.")
#desc 
#desc var texture = ImageTexture.create_from_image(image)
#desc 
#desc # Display the image in a TextureRect node.
#desc var texture_rect = TextureRect.new()
#desc add_child(texture_rect)
#desc texture_rect.texture = texture
#desc [/gdscript]
#desc [csharp]
#desc public override void _Ready()
#desc {
#desc // Create an HTTP request node and connect its completion signal.
#desc var httpRequest = new HTTPRequest();
#desc AddChild(httpRequest);
#desc httpRequest.RequestCompleted += HttpRequestCompleted;
#desc 
#desc // Perform the HTTP request. The URL below returns a PNG image as of writing.
#desc Error error = httpRequest.Request("https://via.placeholder.com/512");
#desc if (error != Error.Ok)
#desc {
#desc GD.PushError("An error occurred in the HTTP request.");
#desc }
#desc }
#desc 
#desc // Called when the HTTP request is completed.
#desc private void HttpRequestCompleted(int result, int response_code, string[] headers, byte[] body)
#desc {
#desc if (result != (int)HTTPRequest.Result.Success)
#desc {
#desc GD.PushError("Image couldn't be downloaded. Try a different image.");
#desc }
#desc var image = new Image();
#desc Error error = image.LoadPngFromBuffer(body);
#desc if (error != Error.Ok)
#desc {
#desc GD.PushError("Couldn't load the image.");
#desc }
#desc 
#desc var texture = new ImageTexture();
#desc texture.CreateFromImage(image);
#desc 
#desc // Display the image in a TextureRect node.
#desc var textureRect = new TextureRect();
#desc AddChild(textureRect);
#desc textureRect.Texture = texture;
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc [b]Gzipped response bodies[/b]: HTTPRequest will automatically handle decompression of response bodies. A [code]Accept-Encoding[/code] header will be automatically added to each of your requests, unless one is already specified. Any response with a [code]Content-Encoding: gzip[/code] header will automatically be decompressed and delivered to you as uncompressed bytes.
class_name HTTPRequest

#desc Request successful.
const RESULT_SUCCESS = 0;

const RESULT_CHUNKED_BODY_SIZE_MISMATCH = 1;

#desc Request failed while connecting.
const RESULT_CANT_CONNECT = 2;

#desc Request failed while resolving.
const RESULT_CANT_RESOLVE = 3;

#desc Request failed due to connection (read/write) error.
const RESULT_CONNECTION_ERROR = 4;

#desc Request failed on TLS handshake.
const RESULT_TLS_HANDSHAKE_ERROR = 5;

#desc Request does not have a response (yet).
const RESULT_NO_RESPONSE = 6;

#desc Request exceeded its maximum size limit, see [member body_size_limit].
const RESULT_BODY_SIZE_LIMIT_EXCEEDED = 7;

const RESULT_BODY_DECOMPRESS_FAILED = 8;

#desc Request failed (currently unused).
const RESULT_REQUEST_FAILED = 9;

#desc HTTPRequest couldn't open the download file.
const RESULT_DOWNLOAD_FILE_CANT_OPEN = 10;

#desc HTTPRequest couldn't write to the download file.
const RESULT_DOWNLOAD_FILE_WRITE_ERROR = 11;

#desc Request reached its maximum redirect limit, see [member max_redirects].
const RESULT_REDIRECT_LIMIT_REACHED = 12;

const RESULT_TIMEOUT = 13;


#desc If [code]true[/code], this header will be added to each request: [code]Accept-Encoding: gzip, deflate[/code] telling servers that it's okay to compress response bodies.
#desc Any Response body declaring a [code]Content-Encoding[/code] of either [code]gzip[/code] or [code]deflate[/code] will then be automatically decompressed, and the uncompressed bytes will be delivered via [code]request_completed[/code].
#desc If the user has specified their own [code]Accept-Encoding[/code] header, then no header will be added regardless of [code]accept_gzip[/code].
#desc If [code]false[/code] no header will be added, and no decompression will be performed on response bodies. The raw bytes of the response body will be returned via [code]request_completed[/code].
var accept_gzip: bool;

#desc Maximum allowed size for response bodies. If the response body is compressed, this will be used as the maximum allowed size for the decompressed body.
var body_size_limit: int;

#desc The size of the buffer used and maximum bytes to read per iteration. See [member HTTPClient.read_chunk_size].
#desc Set this to a lower value (e.g. 4096 for 4 KiB) when downloading small files to decrease memory usage at the cost of download speeds.
var download_chunk_size: int;

#desc The file to download into. Will output any received file into it.
var download_file: String;

#desc Maximum number of allowed redirects.
var max_redirects: int;

#desc If set to a value greater than [code]0.0[/code] before the request starts, the HTTP request will time out after [code]timeout[/code] seconds have passed and the request is not [i]completed[/i] yet. For small HTTP requests such as REST API usage, set [member timeout] to a value between [code]10.0[/code] and [code]30.0[/code] to prevent the application from getting stuck if the request fails to get a response in a timely manner. For file downloads, leave this to [code]0.0[/code] to prevent the download from failing if it takes too much time.
var timeout: float;

#desc If [code]true[/code], multithreading is used to improve performance.
var use_threads: bool;



#desc Cancels the current request.
func cancel_request() -> void:
	pass;

#desc Returns the response body length.
#desc [b]Note:[/b] Some Web servers may not send a body length. In this case, the value returned will be [code]-1[/code]. If using chunked transfer encoding, the body length will also be [code]-1[/code].
func get_body_size() -> int:
	pass;

#desc Returns the number of bytes this HTTPRequest downloaded.
func get_downloaded_bytes() -> int:
	pass;

#desc Returns the current status of the underlying [HTTPClient]. See [enum HTTPClient.Status].
func get_http_client_status() -> int:
	pass;

#desc Creates request on the underlying [HTTPClient]. If there is no configuration errors, it tries to connect using [method HTTPClient.connect_to_host] and passes parameters onto [method HTTPClient.request].
#desc Returns [constant OK] if request is successfully created. (Does not imply that the server has responded), [constant ERR_UNCONFIGURED] if not in the tree, [constant ERR_BUSY] if still processing previous request, [constant ERR_INVALID_PARAMETER] if given string is not a valid URL format, or [constant ERR_CANT_CONNECT] if not using thread and the [HTTPClient] cannot connect to host.
#desc [b]Note:[/b] When [param method] is [constant HTTPClient.METHOD_GET], the payload sent via [param request_data] might be ignored by the server or even cause the server to reject the request (check [url=https://datatracker.ietf.org/doc/html/rfc7231#section-4.3.1]RFC 7231 section 4.3.1[/url] for more details). As a workaround, you can send data as a query string in the URL (see [method String.uri_encode] for an example).
#desc [b]Note:[/b] It's recommended to use transport encryption (TLS) and to avoid sending sensitive information (such as login credentials) in HTTP GET URL parameters. Consider using HTTP POST requests or HTTP headers for such information instead.
func request(url: String, custom_headers: PackedStringArray, tls_validate_domain: bool, method: int, request_data: String) -> int:
	pass;

#desc Creates request on the underlying [HTTPClient] using a raw array of bytes for the request body. If there is no configuration errors, it tries to connect using [method HTTPClient.connect_to_host] and passes parameters onto [method HTTPClient.request].
#desc Returns [constant OK] if request is successfully created. (Does not imply that the server has responded), [constant ERR_UNCONFIGURED] if not in the tree, [constant ERR_BUSY] if still processing previous request, [constant ERR_INVALID_PARAMETER] if given string is not a valid URL format, or [constant ERR_CANT_CONNECT] if not using thread and the [HTTPClient] cannot connect to host.
func request_raw(url: String, custom_headers: PackedStringArray, tls_validate_domain: bool, method: int, request_data_raw: PackedByteArray) -> int:
	pass;

#desc Sets the proxy server for HTTP requests.
#desc The proxy server is unset if [param host] is empty or [param port] is -1.
func set_http_proxy(host: String, port: int) -> void:
	pass;

#desc Sets the proxy server for HTTPS requests.
#desc The proxy server is unset if [param host] is empty or [param port] is -1.
func set_https_proxy(host: String, port: int) -> void:
	pass;


