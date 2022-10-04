#brief Loads a specific resource type from a file.
#desc Godot loads resources in the editor or in exported games using ResourceFormatLoaders. They are queried automatically via the [ResourceLoader] singleton, or when a resource with internal dependencies is loaded. Each file type may load as a different resource type, so multiple ResourceFormatLoaders are registered in the engine.
#desc Extending this class allows you to define your own loader. Be sure to respect the documented return types and values. You should give it a global class name with [code]class_name[/code] for it to be registered. Like built-in ResourceFormatLoaders, it will be called automatically when loading resources of its handled type(s). You may also implement a [ResourceFormatSaver].
#desc [b]Note:[/b] You can also extend [EditorImportPlugin] if the resource type you need exists but Godot is unable to load its format. Choosing one way over another depends on if the format is suitable or not for the final exported game. For example, it's better to import [code].png[/code] textures as [code].ctex[/code] ([CompressedTexture2D]) first, so they can be loaded with better efficiency on the graphics card.
class_name ResourceFormatLoader

const CACHE_MODE_IGNORE = 0;

const CACHE_MODE_REUSE = 1;

const CACHE_MODE_REPLACE = 2;




virtual const func _exists(path: String) -> bool:
	pass;

virtual const func _get_classes_used(path: String) -> PackedStringArray:
	pass;

#desc If implemented, gets the dependencies of a given resource. If [param add_types] is [code]true[/code], paths should be appended [code]::TypeName[/code], where [code]TypeName[/code] is the class name of the dependency.
#desc [b]Note:[/b] Custom resource types defined by scripts aren't known by the [ClassDB], so you might just return [code]"Resource"[/code] for them.
virtual const func _get_dependencies(path: String, add_types: bool) -> PackedStringArray:
	pass;

#desc Gets the list of extensions for files this loader is able to read.
virtual const func _get_recognized_extensions() -> PackedStringArray:
	pass;

#desc Gets the class name of the resource associated with the given path. If the loader cannot handle it, it should return [code]""[/code].
#desc [b]Note:[/b] Custom resource types defined by scripts aren't known by the [ClassDB], so you might just return [code]"Resource"[/code] for them.
virtual const func _get_resource_type(path: String) -> String:
	pass;

virtual const func _get_resource_uid(path: String) -> int:
	pass;

#desc Tells which resource class this loader can load.
#desc [b]Note:[/b] Custom resource types defined by scripts aren't known by the [ClassDB], so you might just handle [code]"Resource"[/code] for them.
virtual const func _handles_type(type: StringName) -> bool:
	pass;

#desc Loads a resource when the engine finds this loader to be compatible. If the loaded resource is the result of an import, [param original_path] will target the source file. Returns a [Resource] object on success, or an [enum Error] constant in case of failure.
#desc The [param cache_mode] property defines whether and how the cache should be used or updated when loading the resource. See [enum CacheMode] for details.
virtual const func _load(path: String, original_path: String, use_sub_threads: bool, cache_mode: int) -> Variant:
	pass;

#desc If implemented, renames dependencies within the given resource and saves it. [param renames] is a dictionary [code]{ String => String }[/code] mapping old dependency paths to new paths.
#desc Returns [constant OK] on success, or an [enum Error] constant in case of failure.
virtual const func _rename_dependencies(path: String, renames: Dictionary) -> int:
	pass;


