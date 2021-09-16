extends Object
class_name OS
const VIDEO_DRIVER_GLES2 = 0;
const VIDEO_DRIVER_VULKAN = 1;
const DAY_SUNDAY = 0;
const DAY_MONDAY = 1;
const DAY_TUESDAY = 2;
const DAY_WEDNESDAY = 3;
const DAY_THURSDAY = 4;
const DAY_FRIDAY = 5;
const DAY_SATURDAY = 6;
const MONTH_JANUARY = 1;
const MONTH_FEBRUARY = 2;
const MONTH_MARCH = 3;
const MONTH_APRIL = 4;
const MONTH_MAY = 5;
const MONTH_JUNE = 6;
const MONTH_JULY = 7;
const MONTH_AUGUST = 8;
const MONTH_SEPTEMBER = 9;
const MONTH_OCTOBER = 10;
const MONTH_NOVEMBER = 11;
const MONTH_DECEMBER = 12;
const SYSTEM_DIR_DESKTOP = 0;
const SYSTEM_DIR_DCIM = 1;
const SYSTEM_DIR_DOCUMENTS = 2;
const SYSTEM_DIR_DOWNLOADS = 3;
const SYSTEM_DIR_MOVIES = 4;
const SYSTEM_DIR_MUSIC = 5;
const SYSTEM_DIR_PICTURES = 6;
const SYSTEM_DIR_RINGTONES = 7;

var low_processor_usage_mode: bool;
var low_processor_usage_mode_sleep_usec: int;

func can_use_threads() -> bool:
    pass;
func close_midi_inputs() -> void:
    pass;
func create_process(path: String, arguments: PackedStringArray) -> int:
    pass;
func delay_msec(msec: int) -> void:
    pass;
func delay_usec(usec: int) -> void:
    pass;
func dump_memory_to_file(file: String) -> void:
    pass;
func dump_resources_to_file(file: String) -> void:
    pass;
func execute(path: String, arguments: PackedStringArray, output: Array, read_stderr: bool) -> int:
    pass;
func find_keycode_from_string(string: String) -> int:
    pass;
func get_cache_dir() -> String:
    pass;
func get_cmdline_args() -> PackedStringArray:
    pass;
func get_config_dir() -> String:
    pass;
func get_connected_midi_inputs() -> PackedStringArray:
    pass;
func get_data_dir() -> String:
    pass;
func get_environment(variable: String) -> String:
    pass;
func get_executable_path() -> String:
    pass;
func get_external_data_dir() -> String:
    pass;
func get_granted_permissions() -> PackedStringArray:
    pass;
func get_keycode_string(code: int) -> String:
    pass;
func get_locale() -> String:
    pass;
func get_model_name() -> String:
    pass;
func get_name() -> String:
    pass;
func get_process_id() -> int:
    pass;
func get_processor_count() -> int:
    pass;
func get_static_memory_peak_usage() -> int:
    pass;
func get_static_memory_usage() -> int:
    pass;
func get_system_dir(dir: int) -> String:
    pass;
func get_thread_caller_id() -> int:
    pass;
func get_unique_id() -> String:
    pass;
func get_user_data_dir() -> String:
    pass;
func has_environment(variable: String) -> bool:
    pass;
func has_feature(tag_name: String) -> bool:
    pass;
func is_debug_build() -> bool:
    pass;
func is_keycode_unicode(code: int) -> bool:
    pass;
func is_stdout_verbose() -> bool:
    pass;
func is_userfs_persistent() -> bool:
    pass;
func kill(pid: int) -> int:
    pass;
func open_midi_inputs() -> void:
    pass;
func print_all_resources(tofile: String) -> void:
    pass;
func print_all_textures_by_size() -> void:
    pass;
func print_resources_by_type(types: PackedStringArray) -> void:
    pass;
func print_resources_in_use(short: bool) -> void:
    pass;
func request_permission(name: String) -> bool:
    pass;
func request_permissions() -> bool:
    pass;
func set_environment(variable: String, value: String) -> bool:
    pass;
func set_thread_name(name: String) -> int:
    pass;
func set_use_file_access_save_and_swap(enabled: bool) -> void:
    pass;
func shell_open(uri: String) -> int:
    pass;
