extends RefCounted
class_name File
const READ = 1;
const WRITE = 2;
const READ_WRITE = 3;
const WRITE_READ = 7;
const COMPRESSION_FASTLZ = 0;
const COMPRESSION_DEFLATE = 1;
const COMPRESSION_ZSTD = 2;
const COMPRESSION_GZIP = 3;

var big_endian: bool;

func close() -> void:
    pass;
func eof_reached() -> bool:
    pass;
func file_exists(path: String) -> bool:
    pass;
func flush() -> void:
    pass;
func get_16() -> int:
    pass;
func get_32() -> int:
    pass;
func get_64() -> int:
    pass;
func get_8() -> int:
    pass;
func get_as_text() -> String:
    pass;
func get_buffer(length: int) -> PackedByteArray:
    pass;
func get_csv_line(delim: String) -> PackedStringArray:
    pass;
func get_double() -> float:
    pass;
func get_error() -> int:
    pass;
func get_float() -> float:
    pass;
func get_length() -> int:
    pass;
func get_line() -> String:
    pass;
func get_md5(path: String) -> String:
    pass;
func get_modified_time(file: String) -> int:
    pass;
func get_pascal_string() -> String:
    pass;
func get_path() -> String:
    pass;
func get_path_absolute() -> String:
    pass;
func get_position() -> int:
    pass;
func get_real() -> float:
    pass;
func get_sha256(path: String) -> String:
    pass;
func get_var(allow_objects: bool) -> Variant:
    pass;
func is_open() -> bool:
    pass;
func open(path: String, flags: int) -> int:
    pass;
func open_compressed(path: String, mode_flags: int, compression_mode: int) -> int:
    pass;
func open_encrypted(path: String, mode_flags: int, key: PackedByteArray) -> int:
    pass;
func open_encrypted_with_pass(path: String, mode_flags: int, pass: String) -> int:
    pass;
func seek(position: int) -> void:
    pass;
func seek_end(position: int) -> void:
    pass;
func store_16(value: int) -> void:
    pass;
func store_32(value: int) -> void:
    pass;
func store_64(value: int) -> void:
    pass;
func store_8(value: int) -> void:
    pass;
func store_buffer(buffer: PackedByteArray) -> void:
    pass;
func store_csv_line(values: PackedStringArray, delim: String) -> void:
    pass;
func store_double(value: float) -> void:
    pass;
func store_float(value: float) -> void:
    pass;
func store_line(line: String) -> void:
    pass;
func store_pascal_string(string: String) -> void:
    pass;
func store_real(value: float) -> void:
    pass;
func store_string(string: String) -> void:
    pass;
func store_var(value: Variant, full_objects: bool) -> void:
    pass;
