extends Object
#brief Singleton for saving Godot-specific resource types.
#desc Singleton for saving Godot-specific resource types to the filesystem.
#desc It uses the many [ResourceFormatSaver] classes registered in the engine (either built-in or from a plugin) to save engine-specific resource data to text-based (e.g. [code].tres[/code] or [code].tscn[/code]) or binary files (e.g. [code].res[/code] or [code].scn[/code]).
class_name ResourceSaver

#desc No resource saving option.
const FLAG_NONE = 0;

#desc Save the resource with a path relative to the scene which uses it.
const FLAG_RELATIVE_PATHS = 1;

#desc Bundles external resources.
const FLAG_BUNDLE_RESOURCES = 2;

#desc Changes the [member Resource.resource_path] of the saved resource to match its new location.
const FLAG_CHANGE_PATH = 4;

#desc Do not save editor-specific metadata (identified by their [code]__editor[/code] prefix).
const FLAG_OMIT_EDITOR_PROPERTIES = 8;

#desc Save as big endian (see [member FileAccess.big_endian]).
const FLAG_SAVE_BIG_ENDIAN = 16;

#desc Compress the resource on save using [constant FileAccess.COMPRESSION_ZSTD]. Only available for binary resource types.
const FLAG_COMPRESS = 32;

#desc Take over the paths of the saved subresources (see [method Resource.take_over_path]).
const FLAG_REPLACE_SUBRESOURCE_PATHS = 64;




#desc Registers a new [ResourceFormatSaver]. The ResourceSaver will use the ResourceFormatSaver as described in [method save].
#desc This method is performed implicitly for ResourceFormatSavers written in GDScript (see [ResourceFormatSaver] for more information).
func add_resource_format_saver(format_saver: ResourceFormatSaver, at_front: bool) -> void:
	pass;

#desc Returns the list of extensions available for saving a resource of a given type.
func get_recognized_extensions(type: Resource) -> PackedStringArray:
	pass;

#desc Unregisters the given [ResourceFormatSaver].
func remove_resource_format_saver(format_saver: ResourceFormatSaver) -> void:
	pass;

#desc Saves a resource to disk to the given path, using a [ResourceFormatSaver] that recognizes the resource object. If [param path] is empty, [ResourceSaver] will try to use [member Resource.resource_path].
#desc The [param flags] bitmask can be specified to customize the save behavior using [enum SaverFlags] flags.
#desc Returns [constant OK] on success.
func save(resource: Resource, path: String, flags: int) -> int:
	pass;


