extends RefCounted
#brief Used to query and configure import format support.
#desc This class is used to query and configure a certain import format. It is used in conjunction with asset format import plugins.
class_name EditorFileSystemImportFormatSupportQuery




#desc Return the file extensions supported.
func _get_file_extensions() -> PackedStringArray:
	pass;

#desc Return whether this importer is active.
func _is_active() -> bool:
	pass;

#desc Query support. Return false if import must not continue.
func _query() -> bool:
	pass;


