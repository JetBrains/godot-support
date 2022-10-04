extends ConfirmationDialog
#brief A modified version of [FileDialog] used by the editor.
class_name EditorFileDialog

#desc The [EditorFileDialog] can select only one file. Accepting the window will open the file.
const FILE_MODE_OPEN_FILE = 0;

#desc The [EditorFileDialog] can select multiple files. Accepting the window will open all files.
const FILE_MODE_OPEN_FILES = 1;

#desc The [EditorFileDialog] can select only one directory. Accepting the window will open the directory.
const FILE_MODE_OPEN_DIR = 2;

#desc The [EditorFileDialog] can select a file or directory. Accepting the window will open it.
const FILE_MODE_OPEN_ANY = 3;

#desc The [EditorFileDialog] can select only one file. Accepting the window will save the file.
const FILE_MODE_SAVE_FILE = 4;

#desc The [EditorFileDialog] can only view [code]res://[/code] directory contents.
const ACCESS_RESOURCES = 0;

#desc The [EditorFileDialog] can only view [code]user://[/code] directory contents.
const ACCESS_USERDATA = 1;

#desc The [EditorFileDialog] can view the entire local file system.
const ACCESS_FILESYSTEM = 2;

#desc The [EditorFileDialog] displays resources as thumbnails.
const DISPLAY_THUMBNAILS = 0;

#desc The [EditorFileDialog] displays resources as a list of filenames.
const DISPLAY_LIST = 1;


#desc The location from which the user may select a file, including [code]res://[/code], [code]user://[/code], and the local file system.
var access: int;

#desc The currently occupied directory.
var current_dir: String;

#desc The currently selected file.
var current_file: String;

#desc The file system path in the address bar.
var current_path: String;

var dialog_hide_on_ok: bool;

#desc If [code]true[/code], the [EditorFileDialog] will not warn the user before overwriting files.
var disable_overwrite_warning: bool;

#desc The view format in which the [EditorFileDialog] displays resources to the user.
var display_mode: int;

#desc The dialog's open or save mode, which affects the selection behavior. See [enum FileMode]
var file_mode: int;

#desc If [code]true[/code], hidden files and directories will be visible in the [EditorFileDialog].
var show_hidden_files: bool;

var title: String;



#desc Adds a comma-delimited file name [param filter] option to the [EditorFileDialog] with an optional [param description], which restricts what files can be picked.
#desc A [param filter] should be of the form [code]"filename.extension"[/code], where filename and extension can be [code]*[/code] to match any string. Filters starting with [code].[/code] (i.e. empty filenames) are not allowed.
#desc For example, a [param filter] of [code]"*.tscn, *.scn"[/code] and a [param description] of [code]"Scenes"[/code] results in filter text "Scenes (*.tscn, *.scn)".
func add_filter(filter: String, description: String) -> void:
	pass;

#desc Removes all filters except for "All Files (*)".
func clear_filters() -> void:
	pass;

#desc Returns the [code]VBoxContainer[/code] used to display the file system.
#desc [b]Warning:[/b] This is a required internal node, removing and freeing it may cause a crash. If you wish to hide it or any of its children, use their [member CanvasItem.visible] property.
func get_vbox() -> VBoxContainer:
	pass;

#desc Notify the [EditorFileDialog] that its view of the data is no longer accurate. Updates the view contents on next view update.
func invalidate() -> void:
	pass;


