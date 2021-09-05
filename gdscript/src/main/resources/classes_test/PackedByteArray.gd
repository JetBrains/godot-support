class_name PackedByteArray



func PackedByteArray() -> PackedByteArray:
    pass;

func PackedByteArray(from: PackedByteArray) -> PackedByteArray:
    pass;

func PackedByteArray(from: Array) -> PackedByteArray:
    pass;

func append(value: int) -> bool:
    pass;

func append_array(array: PackedByteArray) -> void:
    pass;

func compress(compression_mode: int) -> PackedByteArray:
    pass;

func decode_double(byte_offset: int) -> float:
    pass;

func decode_float(byte_offset: int) -> float:
    pass;

func decode_half(byte_offset: int) -> float:
    pass;

func decode_s16(byte_offset: int) -> int:
    pass;

func decode_s32(byte_offset: int) -> int:
    pass;

func decode_s64(byte_offset: int) -> int:
    pass;

func decode_s8(byte_offset: int) -> int:
    pass;

func decode_u16(byte_offset: int) -> int:
    pass;

func decode_u32(byte_offset: int) -> int:
    pass;

func decode_u64(byte_offset: int) -> int:
    pass;

func decode_u8(byte_offset: int) -> int:
    pass;

func decode_var(byte_offset: int, allow_objects: bool) -> Variant:
    pass;

func decode_var_size(byte_offset: int, allow_objects: bool) -> int:
    pass;

func decompress(buffer_size: int, compression_mode: int) -> PackedByteArray:
    pass;

func decompress_dynamic(max_output_size: int, compression_mode: int) -> PackedByteArray:
    pass;

func duplicate() -> PackedByteArray:
    pass;

func encode_double(byte_offset: int, value: float) -> void:
    pass;

func encode_float(byte_offset: int, value: float) -> void:
    pass;

func encode_half(byte_offset: int, value: float) -> void:
    pass;

func encode_s16(byte_offset: int, value: int) -> void:
    pass;

func encode_s32(byte_offset: int, value: int) -> void:
    pass;

func encode_s64(byte_offset: int, value: int) -> void:
    pass;

func encode_s8(byte_offset: int, value: int) -> void:
    pass;

func encode_u16(byte_offset: int, value: int) -> void:
    pass;

func encode_u32(byte_offset: int, value: int) -> void:
    pass;

func encode_u64(byte_offset: int, value: int) -> void:
    pass;

func encode_u8(byte_offset: int, value: int) -> void:
    pass;

func encode_var(byte_offset: int, value: Variant, allow_objects: bool) -> int:
    pass;

func fill(value: int) -> void:
    pass;

func get_string_from_ascii() -> String:
    pass;

func get_string_from_utf16() -> String:
    pass;

func get_string_from_utf32() -> String:
    pass;

func get_string_from_utf8() -> String:
    pass;

func has(value: int) -> bool:
    pass;

func has_encoded_var(byte_offset: int, allow_objects: bool) -> bool:
    pass;

func hex_encode() -> String:
    pass;

func insert(at_index: int, value: int) -> int:
    pass;

func is_empty() -> bool:
    pass;

func operator !=(right: PackedByteArray) -> bool:
    pass;

func operator +(right: PackedByteArray) -> PackedByteArray:
    pass;

func operator ==(right: PackedByteArray) -> bool:
    pass;

func operator [](index: int) -> int:
    pass;

func push_back(value: int) -> bool:
    pass;

func remove(index: int) -> void:
    pass;

func resize(new_size: int) -> int:
    pass;

func reverse() -> void:
    pass;

func set(index: int, value: int) -> void:
    pass;

func size() -> int:
    pass;

func sort() -> void:
    pass;

func subarray(from: int, to: int) -> PackedByteArray:
    pass;

