#brief A directory for the resource filesystem.
#desc A more generalized, low-level variation of the directory concept.
class_name EditorFileSystemDirectory




#desc Returns the index of the directory with name [param name] or [code]-1[/code] if not found.
func find_dir_index() -> int:
	pass;

#desc Returns the index of the file with name [param name] or [code]-1[/code] if not found.
func find_file_index() -> int:
	pass;

#desc Returns the name of the file at index [param idx].
func get_file() -> String:
	pass;

#desc Returns the number of files in this directory.
func get_file_count() -> int:
	pass;

#desc Returns [code]true[/code] if the file at index [param idx] imported properly.
func get_file_import_is_valid() -> bool:
	pass;

#desc Returns the path to the file at index [param idx].
func get_file_path() -> String:
	pass;

#desc Returns the base class of the script class defined in the file at index [param idx]. If the file doesn't define a script class using the [code]class_name[/code] syntax, this will return an empty string.
func get_file_script_class_extends() -> String:
	pass;

#desc Returns the name of the script class defined in the file at index [param idx]. If the file doesn't define a script class using the [code]class_name[/code] syntax, this will return an empty string.
func get_file_script_class_name() -> String:
	pass;

#desc Returns the resource type of the file at index [param idx]. This returns a string such as [code]"Resource"[/code] or [code]"GDScript"[/code], [i]not[/i] a file extension such as [code]".gd"[/code].
func get_file_type() -> StringName:
	pass;

#desc Returns the name of this directory.
func get_name() -> String:
	pass;

#desc Returns the parent directory for this directory or [code]null[/code] if called on a directory at [code]res://[/code] or [code]user://[/code].
func get_parent() -> EditorFileSystemDirectory:
	pass;

#desc Returns the path to this directory.
func get_path() -> String:
	pass;

#desc Returns the subdirectory at index [param idx].
func get_subdir() -> EditorFileSystemDirectory:
	pass;

#desc Returns the number of subdirectories in this directory.
func get_subdir_count() -> int:
	pass;


