extends Object
#brief Singleton that connects the engine with the browser's JavaScript context in Web export.
#desc The JavaScriptBridge singleton is implemented only in the Web export. It's used to access the browser's JavaScript context. This allows interaction with embedding pages or calling third-party JavaScript APIs.
#desc [b]Note:[/b] This singleton can be disabled at build-time to improve security. By default, the JavaScriptBridge singleton is enabled. Official export templates also have the JavaScriptBridge singleton enabled. See [url=$DOCS_URL/development/compiling/compiling_for_web.html]Compiling for the Web[/url] in the documentation for more information.
class_name JavaScriptBridge




#desc Creates a reference to a [Callable] that can be used as a callback by JavaScript. The reference must be kept until the callback happens, or it won't be called at all. See [JavaScriptObject] for usage.
func create_callback(callable: Callable) -> JavaScriptObject:
	pass;

#desc Creates a new JavaScript object using the [code]new[/code] constructor. The [param object] must a valid property of the JavaScript [code]window[/code]. See [JavaScriptObject] for usage.
vararg func create_object(object: String) -> Variant:
	pass;

#desc Prompts the user to download a file containing the specified [param buffer]. The file will have the given [param name] and [param mime] type.
#desc [b]Note:[/b] The browser may override the [url=https://en.wikipedia.org/wiki/Media_type]MIME type[/url] provided based on the file [param name]'s extension.
#desc [b]Note:[/b] Browsers might block the download if [method download_buffer] is not being called from a user interaction (e.g. button click).
#desc [b]Note:[/b] Browsers might ask the user for permission or block the download if multiple download requests are made in a quick succession.
func download_buffer(buffer: PackedByteArray, name: String, mime: String) -> void:
	pass;

#desc Execute the string [param code] as JavaScript code within the browser window. This is a call to the actual global JavaScript function [code]eval()[/code].
#desc If [param use_global_execution_context] is [code]true[/code], the code will be evaluated in the global execution context. Otherwise, it is evaluated in the execution context of a function within the engine's runtime environment.
func eval(code: String, use_global_execution_context: bool) -> Variant:
	pass;

#desc Returns an interface to a JavaScript object that can be used by scripts. The [param interface] must be a valid property of the JavaScript [code]window[/code]. The callback must accept a single [Array] argument, which will contain the JavaScript [code]arguments[/code]. See [JavaScriptObject] for usage.
func get_interface(interface: String) -> JavaScriptObject:
	pass;

#desc Returns [code]true[/code] if a new version of the progressive web app is waiting to be activated.
#desc [b]Note:[/b] Only relevant when exported as a Progressive Web App.
func pwa_needs_update() -> bool:
	pass;

#desc Performs the live update of the progressive web app. Forcing the new version to be installed and the page to be reloaded.
#desc [b]Note:[/b] Your application will be [b]reloaded in all browser tabs[/b].
#desc [b]Note:[/b] Only relevant when exported as a Progressive Web App and [method pwa_needs_update] returns [code]true[/code].
func pwa_update() -> int:
	pass;


