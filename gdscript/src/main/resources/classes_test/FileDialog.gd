extends ConfirmationDialog
class_name FileDialog

const FILE_MODE_OPEN_FILE = 0;
const FILE_MODE_OPEN_FILES = 1;
const FILE_MODE_OPEN_DIR = 2;
const FILE_MODE_OPEN_ANY = 3;
const FILE_MODE_SAVE_FILE = 4;
const ACCESS_RESOURCES = 0;
const ACCESS_USERDATA = 1;
const ACCESS_FILESYSTEM = 2;

var access: int setget set_access, get_access;
var current_dir: String setget set_current_dir, get_current_dir;
var current_file: String setget set_current_file, get_current_file;
var current_path: String setget set_current_path, get_current_path;
var dialog_hide_on_ok: bool setget set_hide_on_ok, get_hide_on_ok;
var file_mode: int setget set_file_mode, get_file_mode;
var filters: PackedStringArray setget set_filters, get_filters;
var mode_overrides_title: bool setget set_mode_overrides_title, is_mode_overriding_title;
var show_hidden_files: bool setget set_show_hidden_files, is_showing_hidden_files;
var title: String setget set_title, get_title;

func add_filter(filter: String) -> void:
    pass;

func clear_filters() -> void:
    pass;

func deselect_all() -> void:
    pass;

func get_line_edit() -> LineEdit:
    pass;

func get_vbox() -> VBoxContainer:
    pass;

func invalidate() -> void:
    pass;

