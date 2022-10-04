#brief Singleton used to load resource files.
#desc Singleton used to load resource files from the filesystem.
#desc It uses the many [ResourceFormatLoader] classes registered in the engine (either built-in or from a plugin) to load files into memory and convert them to a format that can be used by the engine.
#desc [b]Note:[/b] You have to import the files into the engine first to load them using [method load]. If you want to load [Image]s at run-time, you may use [method Image.load]. If you want to import audio files, you can use the snippet described in [member AudioStreamMP3.data].
class_name ResourceLoader

#desc The resource is invalid, or has not been loaded with [method load_threaded_request].
const THREAD_LOAD_INVALID_RESOURCE = 0;

#desc The resource is still being loaded.
const THREAD_LOAD_IN_PROGRESS = 1;

#desc Some error occurred during loading and it failed.
const THREAD_LOAD_FAILED = 2;

#desc The resource was loaded successfully and can be accessed via [method load_threaded_get].
const THREAD_LOAD_LOADED = 3;

const CACHE_MODE_IGNORE = 0;

const CACHE_MODE_REUSE = 1;

const CACHE_MODE_REPLACE = 2;




#desc Registers a new [ResourceFormatLoader]. The ResourceLoader will use the ResourceFormatLoader as described in [method load].
#desc This method is performed implicitly for ResourceFormatLoaders written in GDScript (see [ResourceFormatLoader] for more information).
func add_resource_format_loader(format_loader: ResourceFormatLoader, at_front: bool) -> void:
	pass;

#desc Returns whether a recognized resource exists for the given [param path].
#desc An optional [param type_hint] can be used to further specify the [Resource] type that should be handled by the [ResourceFormatLoader]. Anything that inherits from [Resource] can be used as a type hint, for example [Image].
func exists(path: String, type_hint: String) -> bool:
	pass;

#desc Returns the dependencies for the resource at the given [param path].
func get_dependencies() -> PackedStringArray:
	pass;

#desc Returns the list of recognized extensions for a resource type.
func get_recognized_extensions_for_type() -> PackedStringArray:
	pass;

#desc Returns the ID associated with a given resource path, or [code]-1[/code] when no such ID exists.
func get_resource_uid() -> int:
	pass;

#desc Returns whether a cached resource is available for the given [param path].
#desc Once a resource has been loaded by the engine, it is cached in memory for faster access, and future calls to the [method load] method will use the cached version. The cached resource can be overridden by using [method Resource.take_over_path] on a new resource for that same path.
func has_cached() -> bool:
	pass;

#desc Loads a resource at the given [param path], caching the result for further access.
#desc The registered [ResourceFormatLoader]s are queried sequentially to find the first one which can handle the file's extension, and then attempt loading. If loading fails, the remaining ResourceFormatLoaders are also attempted.
#desc An optional [param type_hint] can be used to further specify the [Resource] type that should be handled by the [ResourceFormatLoader]. Anything that inherits from [Resource] can be used as a type hint, for example [Image].
#desc The [param cache_mode] property defines whether and how the cache should be used or updated when loading the resource. See [enum CacheMode] for details.
#desc Returns an empty resource if no [ResourceFormatLoader] could handle the file.
#desc GDScript has a simplified [method @GDScript.load] built-in method which can be used in most situations, leaving the use of [ResourceLoader] for more advanced scenarios.
func load(path: String, type_hint: String, cache_mode: int) -> Resource:
	pass;

#desc Returns the resource loaded by [method load_threaded_request].
#desc If this is called before the loading thread is done (i.e. [method load_threaded_get_status] is not [constant THREAD_LOAD_LOADED]), the calling thread will be blocked until the resource has finished loading.
func load_threaded_get() -> Resource:
	pass;

#desc Returns the status of a threaded loading operation started with [method load_threaded_request] for the resource at [param path]. See [enum ThreadLoadStatus] for possible return values.
#desc An array variable can optionally be passed via [param progress], and will return a one-element array containing the percentage of completion of the threaded loading.
func load_threaded_get_status(path: String, progress: Array) -> int:
	pass;

#desc Loads the resource using threads. If [param use_sub_threads] is [code]true[/code], multiple threads will be used to load the resource, which makes loading faster, but may affect the main thread (and thus cause game slowdowns).
#desc The [param cache_mode] property defines whether and how the cache should be used or updated when loading the resource. See [enum CacheMode] for details.
func load_threaded_request(path: String, type_hint: String, use_sub_threads: bool, cache_mode: int) -> int:
	pass;

#desc Unregisters the given [ResourceFormatLoader].
func remove_resource_format_loader() -> void:
	pass;

#desc Changes the behavior on missing sub-resources. The default behavior is to abort loading.
func set_abort_on_missing_resources() -> void:
	pass;


