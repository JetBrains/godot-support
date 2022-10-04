#brief Dialog for selecting files or directories in the filesystem.
#desc FileDialog is a preset dialog used to choose files and directories in the filesystem. It supports filter masks. The FileDialog automatically sets its window title according to the [member file_mode]. If you want to use a custom title, disable this by setting [member mode_overrides_title] to [code]false[/code].
class_name FileDialog

#desc The dialog allows selecting one, and only one file.
const FILE_MODE_OPEN_FILE = 0;

#desc The dialog allows selecting multiple files.
const FILE_MODE_OPEN_FILES = 1;

#desc The dialog only allows selecting a directory, disallowing the selection of any file.
const FILE_MODE_OPEN_DIR = 2;

#desc The dialog allows selecting one file or directory.
const FILE_MODE_OPEN_ANY = 3;

#desc The dialog will warn when a file exists.
const FILE_MODE_SAVE_FILE = 4;

#desc The dialog only allows accessing files under the [Resource] path ([code]res://[/code]).
const ACCESS_RESOURCES = 0;

#desc The dialog only allows accessing files under user data path ([code]user://[/code]).
const ACCESS_USERDATA = 1;

#desc The dialog allows accessing files on the whole file system.
const ACCESS_FILESYSTEM = 2;


#desc The file system access scope. See enum [code]Access[/code] constants.
#desc [b]Warning:[/b] Currently, in sandboxed environments such as Web builds or sandboxed macOS apps, FileDialog cannot access the host file system. See [url=https://github.com/godotengine/godot-proposals/issues/1123]godot-proposals#1123[/url].
var access: int;

#desc The current working directory of the file dialog.
var current_dir: String;

#desc The currently selected file of the file dialog.
var current_file: String;

#desc The currently selected file path of the file dialog.
var current_path: String;

var dialog_hide_on_ok: bool;

#desc The dialog's open or save mode, which affects the selection behavior. See [enum FileMode].
var file_mode: int;

#desc The available file type filters. For example, this shows only [code].png[/code] and [code].gd[/code] files: [code]set_filters(PackedStringArray(["*.png ; PNG Images","*.gd ; GDScript Files"]))[/code]. Multiple file types can also be specified in a single filter. [code]"*.png, *.jpg, *.jpeg ; Supported Images"[/code] will show both PNG and JPEG files when selected.
var filters: PackedStringArray;

#desc If [code]true[/code], changing the [code]Mode[/code] property will set the window title accordingly (e.g. setting mode to [constant FILE_MODE_OPEN_FILE] will change the window title to "Open a File").
var mode_overrides_title: bool;

#desc If non-empty, the given sub-folder will be "root" of this [FileDialog], i.e. user won't be able to go to its parent directory.
var root_subfolder: String;

#desc If [code]true[/code], the dialog will show hidden files.
var show_hidden_files: bool;

var title: String;



#desc Adds a comma-delimited file name [param filter] option to the [FileDialog] with an optional [param description], which restricts what files can be picked.
#desc A [param filter] should be of the form [code]"filename.extension"[/code], where filename and extension can be [code]*[/code] to match any string. Filters starting with [code].[/code] (i.e. empty filenames) are not allowed.
#desc For example, a [param filter] of [code]"*.png, *.jpg"[/code] and a [param description] of [code]"Images"[/code] results in filter text "Images (*.png, *.jpg)".
func add_filter(filter: String, description: String) -> void:
	pass;

#desc Clear all the added filters in the dialog.
func clear_filters() -> void:
	pass;

#desc Clear all currently selected items in the dialog.
func deselect_all() -> void:
	pass;

#desc Returns the LineEdit for the selected file.
#desc [b]Warning:[/b] This is a required internal node, removing and freeing it may cause a crash. If you wish to hide it or any of its children, use their [member CanvasItem.visible] property.
func get_line_edit() -> LineEdit:
	pass;

#desc Returns the vertical box container of the dialog, custom controls can be added to it.
#desc [b]Warning:[/b] This is a required internal node, removing and freeing it may cause a crash. If you wish to hide it or any of its children, use their [member CanvasItem.visible] property.
func get_vbox() -> VBoxContainer:
	pass;

#desc Invalidate and update the current dialog content list.
func invalidate() -> void:
	pass;


