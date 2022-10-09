extends Node
#brief Resource filesystem, as the editor sees it.
#desc This object holds information of all resources in the filesystem, their types, etc.
#desc [b]Note:[/b] This class shouldn't be instantiated directly. Instead, access the singleton using [method EditorInterface.get_resource_filesystem].
class_name EditorFileSystem




#desc Returns the resource type of the file, given the full path. This returns a string such as [code]"Resource"[/code] or [code]"GDScript"[/code], [i]not[/i] a file extension such as [code]".gd"[/code].
func get_file_type(path: String) -> String:
	pass;

#desc Gets the root directory object.
func get_filesystem() -> EditorFileSystemDirectory:
	pass;

#desc Returns a view into the filesystem at [param path].
func get_filesystem_path(path: String) -> EditorFileSystemDirectory:
	pass;

#desc Returns the scan progress for 0 to 1 if the FS is being scanned.
func get_scanning_progress() -> float:
	pass;

#desc Returns [code]true[/code] if the filesystem is being scanned.
func is_scanning() -> bool:
	pass;

#desc Reimports a set of files. Call this if these files or their [code].import[/code] files were directly edited by script or an external program.
#desc If the file type changed or the file was newly created, use [method update_file] or [method scan].
#desc [b]Note:[/b] This function blocks until the import is finished. However, the main loop iteration, including timers and [method Node._process], will occur during the import process due to progress bar updates. Avoid calls to [method reimport_files] or [method scan] while an import is in progress.
func reimport_files(files: PackedStringArray) -> void:
	pass;

#desc Scan the filesystem for changes.
func scan() -> void:
	pass;

#desc Check if the source of any imported resource changed.
func scan_sources() -> void:
	pass;

#desc Add a file in an existing directory, or schedule file information to be updated on editor restart. Can be used to update text files saved by an external program.
#desc This will not import the file. To reimport, call [method reimport_files] or [method scan] methods.
func update_file(path: String) -> void:
	pass;

#desc Scans the script files and updates the list of custom class names.
func update_script_classes() -> void:
	pass;


