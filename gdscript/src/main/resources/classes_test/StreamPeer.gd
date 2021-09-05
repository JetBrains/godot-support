extends RefCounted
class_name StreamPeer


var big_endian: bool setget set_big_endian, is_big_endian_enabled;

func get_16() -> int:
    pass;

func get_32() -> int:
    pass;

func get_64() -> int:
    pass;

func get_8() -> int:
    pass;

func get_available_bytes() -> int:
    pass;

func get_data(bytes: int) -> Array:
    pass;

func get_double() -> float:
    pass;

func get_float() -> float:
    pass;

func get_partial_data(bytes: int) -> Array:
    pass;

func get_string(bytes: int) -> String:
    pass;

func get_u16() -> int:
    pass;

func get_u32() -> int:
    pass;

func get_u64() -> int:
    pass;

func get_u8() -> int:
    pass;

func get_utf8_string(bytes: int) -> String:
    pass;

func get_var(allow_objects: bool) -> Variant:
    pass;

func put_16(value: int) -> void:
    pass;

func put_32(value: int) -> void:
    pass;

func put_64(value: int) -> void:
    pass;

func put_8(value: int) -> void:
    pass;

func put_data(data: PackedByteArray) -> int:
    pass;

func put_double(value: float) -> void:
    pass;

func put_float(value: float) -> void:
    pass;

func put_partial_data(data: PackedByteArray) -> Array:
    pass;

func put_string(value: String) -> void:
    pass;

func put_u16(value: int) -> void:
    pass;

func put_u32(value: int) -> void:
    pass;

func put_u64(value: int) -> void:
    pass;

func put_u8(value: int) -> void:
    pass;

func put_utf8_string(value: String) -> void:
    pass;

func put_var(value: Variant, full_objects: bool) -> void:
    pass;

