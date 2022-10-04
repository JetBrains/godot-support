#brief Type used to handle the filesystem.
#desc Directory type. It is used to manage directories and their content (not restricted to the project folder).
#desc [DirAccess] can't be instantiated directly. Instead it is created with a static method that takes a path for which it will be opened.
#desc Most of the methods have a static alternative that can be used without creating a [DirAccess]. Static methods only support absolute paths (including [code]res://[/code] and [code]user://[/code]).
#desc [codeblock]
#desc # Standard
#desc var dir = DirAccess.open("user://levels")
#desc dir.make_dir("world1")
#desc # Static
#desc DirAccess.make_dir_absolute("user://levels/world1")
#desc [/codeblock]
#desc [b]Note:[/b] Many resources types are imported (e.g. textures or sound files), and their source asset will not be included in the exported game, as only the imported version is used. Use [ResourceLoader] to access imported resources.
#desc Here is an example on how to iterate through the files of a directory:
#desc [codeblocks]
#desc [gdscript]
#desc func dir_contents(path):
#desc var dir = DirAccess.open(path)
#desc if dir:
#desc dir.list_dir_begin()
#desc var file_name = dir.get_next()
#desc while file_name != "":
#desc if dir.current_is_dir():
#desc print("Found directory: " + file_name)
#desc else:
#desc print("Found file: " + file_name)
#desc file_name = dir.get_next()
#desc else:
#desc print("An error occurred when trying to access the path.")
#desc [/gdscript]
#desc [csharp]
#desc public void DirContents(string path)
#desc {
#desc using var dir = DirAccess.Open(path);
#desc if (dir != null)
#desc {
#desc dir.ListDirBegin();
#desc string fileName = dir.GetNext();
#desc while (fileName != "")
#desc {
#desc if (dir.CurrentIsDir())
#desc {
#desc GD.Print("Found directory: " + fileName);
#desc }
#desc else
#desc {
#desc GD.Print("Found file: " + fileName);
#desc }
#desc fileName = dir.GetNext();
#desc }
#desc }
#desc else
#desc {
#desc GD.Print("An error occurred when trying to access the path.");
#desc }
#desc }
#desc [/csharp]
#desc [/codeblocks]
class_name DirAccess


#desc If [code]true[/code], hidden files are included when the navigating directory.
#desc Affects [method list_dir_begin], [method get_directories] and [method get_files].
var include_hidden: bool;

#desc If [code]true[/code], [code].[/code] and [code]..[/code] are included when navigating the directory.
#desc Affects [method list_dir_begin] and [method get_directories].
var include_navigational: bool;



#desc Changes the currently opened directory to the one passed as an argument. The argument can be relative to the current directory (e.g. [code]newdir[/code] or [code]../newdir[/code]), or an absolute path (e.g. [code]/tmp/newdir[/code] or [code]res://somedir/newdir[/code]).
#desc Returns one of the [enum Error] code constants ([code]OK[/code] on success).
func change_dir(to_dir: String) -> int:
	pass;

#desc Copies the [param from] file to the [param to] destination. Both arguments should be paths to files, either relative or absolute. If the destination file exists and is not access-protected, it will be overwritten.
#desc If [param chmod_flags] is different than [code]-1[/code], the Unix permissions for the destination path will be set to the provided value, if available on the current operating system.
#desc Returns one of the [enum Error] code constants ([code]OK[/code] on success).
func copy(from: String, to: String, chmod_flags: int) -> int:
	pass;

#desc Static version of [method copy]. Supports only absolute paths.
static func copy_absolute(from: String, to: String, chmod_flags: int) -> int:
	pass;

#desc Returns whether the current item processed with the last [method get_next] call is a directory ([code].[/code] and [code]..[/code] are considered directories).
func current_is_dir() -> bool:
	pass;

#desc Returns whether the target directory exists. The argument can be relative to the current directory, or an absolute path.
func dir_exists(path: String) -> bool:
	pass;

#desc Static version of [method dir_exists]. Supports only absolute paths.
static func dir_exists_absolute(path: String) -> bool:
	pass;

#desc Returns whether the target file exists. The argument can be relative to the current directory, or an absolute path.
#desc For a static equivalent, use [method FileAccess.file_exists].
func file_exists(path: String) -> bool:
	pass;

#desc Returns the absolute path to the currently opened directory (e.g. [code]res://folder[/code] or [code]C:\tmp\folder[/code]).
func get_current_dir(include_drive: bool) -> String:
	pass;

#desc Returns the currently opened directory's drive index. See [method get_drive_name] to convert returned index to the name of the drive.
func get_current_drive() -> int:
	pass;

#desc Returns a [PackedStringArray] containing filenames of the directory contents, excluding files. The array is sorted alphabetically.
#desc Affected by [member include_hidden] and [member include_navigational].
func get_directories() -> PackedStringArray:
	pass;

#desc Returns a [PackedStringArray] containing filenames of the directory contents, excluding files, at the given [param path]. The array is sorted alphabetically.
#desc Use [method get_directories] if you want more control of what gets included.
static func get_directories_at(path: String) -> PackedStringArray:
	pass;

#desc On Windows, returns the number of drives (partitions) mounted on the current filesystem.
#desc On macOS, returns the number of mounted volumes.
#desc On Linux, returns the number of mounted volumes and GTK 3 bookmarks.
#desc On other platforms, the method returns 0.
static func get_drive_count() -> int:
	pass;

#desc On Windows, returns the name of the drive (partition) passed as an argument (e.g. [code]C:[/code]).
#desc On macOS, returns the path to the mounted volume passed as an argument.
#desc On Linux, returns the path to the mounted volume or GTK 3 bookmark passed as an argument.
#desc On other platforms, or if the requested drive does not exist, the method returns an empty String.
static func get_drive_name(idx: int) -> String:
	pass;

#desc Returns a [PackedStringArray] containing filenames of the directory contents, excluding directories. The array is sorted alphabetically.
#desc Affected by [member include_hidden].
func get_files() -> PackedStringArray:
	pass;

#desc Returns a [PackedStringArray] containing filenames of the directory contents, excluding directories, at the given [param path]. The array is sorted alphabetically.
#desc Use [method get_files] if you want more control of what gets included.
static func get_files_at(path: String) -> PackedStringArray:
	pass;

#desc Returns the next element (file or directory) in the current directory.
#desc The name of the file or directory is returned (and not its full path). Once the stream has been fully processed, the method returns an empty [String] and closes the stream automatically (i.e. [method list_dir_end] would not be mandatory in such a case).
func get_next() -> String:
	pass;

#desc Returns the result of the last [method open] call in the current thread.
static func get_open_error() -> int:
	pass;

#desc Returns the available space on the current directory's disk, in bytes. Returns [code]0[/code] if the platform-specific method to query the available space fails.
func get_space_left() -> int:
	pass;

#desc Initializes the stream used to list all files and directories using the [method get_next] function, closing the currently opened stream if needed. Once the stream has been processed, it should typically be closed with [method list_dir_end].
#desc Affected by [member include_hidden] and [member include_navigational].
#desc [b]Note:[/b] The order of files and directories returned by this method is not deterministic, and can vary between operating systems. If you want a list of all files or folders sorted alphabetically, use [method get_files] or [method get_directories].
func list_dir_begin() -> int:
	pass;

#desc Closes the current stream opened with [method list_dir_begin] (whether it has been fully processed with [method get_next] does not matter).
func list_dir_end() -> void:
	pass;

#desc Creates a directory. The argument can be relative to the current directory, or an absolute path. The target directory should be placed in an already existing directory (to create the full path recursively, see [method make_dir_recursive]).
#desc Returns one of the [enum Error] code constants ([code]OK[/code] on success).
func make_dir(path: String) -> int:
	pass;

#desc Static version of [method make_dir]. Supports only absolute paths.
static func make_dir_absolute(path: String) -> int:
	pass;

#desc Creates a target directory and all necessary intermediate directories in its path, by calling [method make_dir] recursively. The argument can be relative to the current directory, or an absolute path.
#desc Returns one of the [enum Error] code constants ([code]OK[/code] on success).
func make_dir_recursive(path: String) -> int:
	pass;

#desc Static version of [method make_dir_recursive]. Supports only absolute paths.
static func make_dir_recursive_absolute(path: String) -> int:
	pass;

#desc Creates a new [DirAccess] object and opens an existing directory of the filesystem. The [param path] argument can be within the project tree ([code]res://folder[/code]), the user directory ([code]user://folder[/code]) or an absolute path of the user filesystem (e.g. [code]/tmp/folder[/code] or [code]C:\tmp\folder[/code]).
#desc Returns [code]null[/code] if opening the directory failed. You can use [method get_open_error] to check the error that occurred.
static func open(path: String) -> DirAccess:
	pass;

#desc Permanently deletes the target file or an empty directory. The argument can be relative to the current directory, or an absolute path. If the target directory is not empty, the operation will fail.
#desc If you don't want to delete the file/directory permanently, use [method OS.move_to_trash] instead.
#desc Returns one of the [enum Error] code constants ([code]OK[/code] on success).
func remove(path: String) -> int:
	pass;

#desc Static version of [method remove]. Supports only absolute paths.
static func remove_absolute(path: String) -> int:
	pass;

#desc Renames (move) the [param from] file or directory to the [param to] destination. Both arguments should be paths to files or directories, either relative or absolute. If the destination file or directory exists and is not access-protected, it will be overwritten.
#desc Returns one of the [enum Error] code constants ([code]OK[/code] on success).
func rename(from: String, to: String) -> int:
	pass;

#desc Static version of [method rename]. Supports only absolute paths.
static func rename_absolute(from: String, to: String) -> int:
	pass;


