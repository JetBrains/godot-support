#brief A packed array of bytes.
#desc An array specifically designed to hold bytes. Packs data tightly, so it saves memory for large array sizes.
#desc [PackedByteArray] also provides methods to encode/decode various types to/from bytes. The way values are encoded is an implementation detail and shouldn't be relied upon when interacting with external apps.
class_name PackedByteArray



#desc Constructs an empty [PackedByteArray].
func PackedByteArray() -> PackedByteArray:
	pass;

#desc Constructs a [PackedByteArray] as a copy of the given [PackedByteArray].
func PackedByteArray() -> PackedByteArray:
	pass;

#desc Constructs a new [PackedByteArray]. Optionally, you can pass in a generic [Array] that will be converted.
func PackedByteArray() -> PackedByteArray:
	pass;


#desc Appends an element at the end of the array (alias of [method push_back]).
func append() -> bool:
	pass;

#desc Appends a [PackedByteArray] at the end of this array.
func append_array() -> void:
	pass;

#desc Finds the index of an existing value (or the insertion index that maintains sorting order, if the value is not yet present in the array) using binary search. Optionally, a [param before] specifier can be passed. If [code]false[/code], the returned index comes after all existing entries of the value in the array.
#desc [b]Note:[/b] Calling [method bsearch] on an unsorted array results in unexpected behavior.
func bsearch(value: int, before: bool) -> int:
	pass;

#desc Clears the array. This is equivalent to using [method resize] with a size of [code]0[/code].
func clear() -> void:
	pass;

#desc Returns a new [PackedByteArray] with the data compressed. Set the compression mode using one of [enum FileAccess.CompressionMode]'s constants.
func compress() -> PackedByteArray:
	pass;

#desc Returns the number of times an element is in the array.
func count() -> int:
	pass;

#desc Decodes a 64-bit floating point number from the bytes starting at [param byte_offset]. Fails if the byte count is insufficient. Returns [code]0.0[/code] if a valid number can't be decoded.
func decode_double() -> float:
	pass;

#desc Decodes a 32-bit floating point number from the bytes starting at [param byte_offset]. Fails if the byte count is insufficient. Returns [code]0.0[/code] if a valid number can't be decoded.
func decode_float() -> float:
	pass;

#desc Decodes a 16-bit floating point number from the bytes starting at [param byte_offset]. Fails if the byte count is insufficient. Returns [code]0.0[/code] if a valid number can't be decoded.
func decode_half() -> float:
	pass;

#desc Decodes a 16-bit signed integer number from the bytes starting at [param byte_offset]. Fails if the byte count is insufficient. Returns [code]0[/code] if a valid number can't be decoded.
func decode_s16() -> int:
	pass;

#desc Decodes a 32-bit signed integer number from the bytes starting at [param byte_offset]. Fails if the byte count is insufficient. Returns [code]0[/code] if a valid number can't be decoded.
func decode_s32() -> int:
	pass;

#desc Decodes a 64-bit signed integer number from the bytes starting at [param byte_offset]. Fails if the byte count is insufficient. Returns [code]0[/code] if a valid number can't be decoded.
func decode_s64() -> int:
	pass;

#desc Decodes a 8-bit signed integer number from the bytes starting at [param byte_offset]. Fails if the byte count is insufficient. Returns [code]0[/code] if a valid number can't be decoded.
func decode_s8() -> int:
	pass;

#desc Decodes a 16-bit unsigned integer number from the bytes starting at [param byte_offset]. Fails if the byte count is insufficient. Returns [code]0[/code] if a valid number can't be decoded.
func decode_u16() -> int:
	pass;

#desc Decodes a 32-bit unsigned integer number from the bytes starting at [param byte_offset]. Fails if the byte count is insufficient. Returns [code]0[/code] if a valid number can't be decoded.
func decode_u32() -> int:
	pass;

#desc Decodes a 64-bit unsigned integer number from the bytes starting at [param byte_offset]. Fails if the byte count is insufficient. Returns [code]0[/code] if a valid number can't be decoded.
func decode_u64() -> int:
	pass;

#desc Decodes a 8-bit unsigned integer number from the bytes starting at [param byte_offset]. Fails if the byte count is insufficient. Returns [code]0[/code] if a valid number can't be decoded.
func decode_u8() -> int:
	pass;

#desc Decodes a [Variant] from the bytes starting at [param byte_offset]. Returns [code]null[/code] if a valid variant can't be decoded or the value is [Object]-derived and [param allow_objects] is [code]false[/code].
func decode_var(byte_offset: int, allow_objects: bool) -> Variant:
	pass;

#desc Decodes a size of a [Variant] from the bytes starting at [param byte_offset]. Requires at least 4 bytes of data starting at the offset, otherwise fails.
func decode_var_size(byte_offset: int, allow_objects: bool) -> int:
	pass;

#desc Returns a new [PackedByteArray] with the data decompressed. Set [param buffer_size] to the size of the uncompressed data. Set the compression mode using one of [enum FileAccess.CompressionMode]'s constants.
func decompress(buffer_size: int, compression_mode: int) -> PackedByteArray:
	pass;

#desc Returns a new [PackedByteArray] with the data decompressed. Set the compression mode using one of [enum FileAccess.CompressionMode]'s constants. [b]This method only accepts gzip and deflate compression modes.[/b]
#desc This method is potentially slower than [code]decompress[/code], as it may have to re-allocate its output buffer multiple times while decompressing, whereas [code]decompress[/code] knows it's output buffer size from the beginning.
#desc GZIP has a maximal compression ratio of 1032:1, meaning it's very possible for a small compressed payload to decompress to a potentially very large output. To guard against this, you may provide a maximum size this function is allowed to allocate in bytes via [param max_output_size]. Passing -1 will allow for unbounded output. If any positive value is passed, and the decompression exceeds that amount in bytes, then an error will be returned.
func decompress_dynamic(max_output_size: int, compression_mode: int) -> PackedByteArray:
	pass;

#desc Creates a copy of the array, and returns it.
func duplicate() -> PackedByteArray:
	pass;

#desc Encodes a 64-bit floating point number as bytes at the index of [param byte_offset] bytes. The array must have at least 8 bytes of allocated space, starting at the offset.
func encode_double(byte_offset: int, value: float) -> void:
	pass;

#desc Encodes a 32-bit floating point number as bytes at the index of [param byte_offset] bytes. The array must have at least 4 bytes of space, starting at the offset.
func encode_float(byte_offset: int, value: float) -> void:
	pass;

#desc Encodes a 16-bit floating point number as bytes at the index of [param byte_offset] bytes. The array must have at least 2 bytes of space, starting at the offset.
func encode_half(byte_offset: int, value: float) -> void:
	pass;

#desc Encodes a 16-bit signed integer number as bytes at the index of [param byte_offset] bytes. The array must have at least 2 bytes of space, starting at the offset.
func encode_s16(byte_offset: int, value: int) -> void:
	pass;

#desc Encodes a 32-bit signed integer number as bytes at the index of [param byte_offset] bytes. The array must have at least 2 bytes of space, starting at the offset.
func encode_s32(byte_offset: int, value: int) -> void:
	pass;

#desc Encodes a 64-bit signed integer number as bytes at the index of [param byte_offset] bytes. The array must have at least 2 bytes of space, starting at the offset.
func encode_s64(byte_offset: int, value: int) -> void:
	pass;

#desc Encodes a 8-bit signed integer number (signed byte) at the index of [param byte_offset] bytes. The array must have at least 1 byte of space, starting at the offset.
func encode_s8(byte_offset: int, value: int) -> void:
	pass;

#desc Encodes a 16-bit unsigned integer number as bytes at the index of [param byte_offset] bytes. The array must have at least 2 bytes of space, starting at the offset.
func encode_u16(byte_offset: int, value: int) -> void:
	pass;

#desc Encodes a 32-bit unsigned integer number as bytes at the index of [param byte_offset] bytes. The array must have at least 4 bytes of space, starting at the offset.
func encode_u32(byte_offset: int, value: int) -> void:
	pass;

#desc Encodes a 64-bit unsigned integer number as bytes at the index of [param byte_offset] bytes. The array must have at least 8 bytes of space, starting at the offset.
func encode_u64(byte_offset: int, value: int) -> void:
	pass;

#desc Encodes a 8-bit unsigned integer number (byte) at the index of [param byte_offset] bytes. The array must have at least 1 byte of space, starting at the offset.
func encode_u8(byte_offset: int, value: int) -> void:
	pass;

#desc Encodes a [Variant] at the index of [param byte_offset] bytes. A sufficient space must be allocated, depending on the encoded variant's size. If [param allow_objects] is [code]false[/code], [Object]-derived values are not permitted and will instead be serialized as ID-only.
func encode_var(byte_offset: int, value: Variant, allow_objects: bool) -> int:
	pass;

#desc Assigns the given value to all elements in the array. This can typically be used together with [method resize] to create an array with a given size and initialized elements.
func fill() -> void:
	pass;

#desc Searches the array for a value and returns its index or [code]-1[/code] if not found. Optionally, the initial search index can be passed.
func find(value: int, from: int) -> int:
	pass;

#desc Converts ASCII/Latin-1 encoded array to [String]. Fast alternative to [method get_string_from_utf8] if the content is ASCII/Latin-1 only. Unlike the UTF-8 function this function maps every byte to a character in the array. Multibyte sequences will not be interpreted correctly. For parsing user input always use [method get_string_from_utf8].
func get_string_from_ascii() -> String:
	pass;

#desc Converts UTF-16 encoded array to [String]. If the BOM is missing, system endianness is assumed. Returns empty string if source array is not valid UTF-16 string.
func get_string_from_utf16() -> String:
	pass;

#desc Converts UTF-32 encoded array to [String]. System endianness is assumed. Returns empty string if source array is not valid UTF-32 string.
func get_string_from_utf32() -> String:
	pass;

#desc Converts UTF-8 encoded array to [String]. Slower than [method get_string_from_ascii] but supports UTF-8 encoded data. Use this function if you are unsure about the source of the data. For user input this function should always be preferred. Returns empty string if source array is not valid UTF-8 string.
func get_string_from_utf8() -> String:
	pass;

#desc Returns [code]true[/code] if the array contains [param value].
func has() -> bool:
	pass;

#desc Returns [code]true[/code] if a valid [Variant] value can be decoded at the [param byte_offset]. Returns [code]false[/code] othewrise or when the value is [Object]-derived and [param allow_objects] is [code]false[/code].
func has_encoded_var(byte_offset: int, allow_objects: bool) -> bool:
	pass;

#desc Returns a hexadecimal representation of this array as a [String].
#desc [codeblocks]
#desc [gdscript]
#desc var array = PackedByteArray([11, 46, 255])
#desc print(array.hex_encode()) # Prints: 0b2eff
#desc [/gdscript]
#desc [csharp]
#desc var array = new byte[] {11, 46, 255};
#desc GD.Print(array.HexEncode()); // Prints: 0b2eff
#desc [/csharp]
#desc [/codeblocks]
func hex_encode() -> String:
	pass;

#desc Inserts a new element at a given position in the array. The position must be valid, or at the end of the array ([code]idx == size()[/code]).
func insert(at_index: int, value: int) -> int:
	pass;

#desc Returns [code]true[/code] if the array is empty.
func is_empty() -> bool:
	pass;

#desc Appends an element at the end of the array.
func push_back() -> bool:
	pass;

#desc Removes an element from the array by index.
func remove_at() -> void:
	pass;

#desc Sets the size of the array. If the array is grown, reserves elements at the end of the array. If the array is shrunk, truncates the array to the new size.
func resize() -> int:
	pass;

#desc Reverses the order of the elements in the array.
func reverse() -> void:
	pass;

#desc Searches the array in reverse order. Optionally, a start search index can be passed. If negative, the start index is considered relative to the end of the array.
func rfind(value: int, from: int) -> int:
	pass;

#desc Changes the byte at the given index.
func set(index: int, value: int) -> void:
	pass;

#desc Returns the number of elements in the array.
func size() -> int:
	pass;

#desc Returns the slice of the [PackedByteArray], from [param begin] (inclusive) to [param end] (exclusive), as a new [PackedByteArray].
#desc The absolute value of [param begin] and [param end] will be clamped to the array size, so the default value for [param end] makes it slice to the size of the array by default (i.e. [code]arr.slice(1)[/code] is a shorthand for [code]arr.slice(1, arr.size())[/code]).
#desc If either [param begin] or [param end] are negative, they will be relative to the end of the array (i.e. [code]arr.slice(0, -2)[/code] is a shorthand for [code]arr.slice(0, arr.size() - 2)[/code]).
func slice(begin: int, end: int) -> PackedByteArray:
	pass;

#desc Sorts the elements of the array in ascending order.
func sort() -> void:
	pass;

#desc Returns a copy of the data converted to a [PackedFloat32Array], where each block of 4 bytes has been converted to a 32-bit float (C++ [code]float[/code]).
#desc The size of the input array must be a multiple of 4 (size of 32-bit float). The size of the new array will be [code]byte_array.size() / 4[/code].
#desc If the original data can't be converted to 32-bit floats, the resulting data is undefined.
func to_float32_array() -> PackedFloat32Array:
	pass;

#desc Returns a copy of the data converted to a [PackedFloat64Array], where each block of 8 bytes has been converted to a 64-bit float (C++ [code]double[/code], Godot [float]).
#desc The size of the input array must be a multiple of 8 (size of 64-bit double). The size of the new array will be [code]byte_array.size() / 8[/code].
#desc If the original data can't be converted to 64-bit floats, the resulting data is undefined.
func to_float64_array() -> PackedFloat64Array:
	pass;

#desc Returns a copy of the data converted to a [PackedInt32Array], where each block of 4 bytes has been converted to a signed 32-bit integer (C++ [code]int32_t[/code]).
#desc The size of the input array must be a multiple of 4 (size of 32-bit integer). The size of the new array will be [code]byte_array.size() / 4[/code].
#desc If the original data can't be converted to signed 32-bit integers, the resulting data is undefined.
func to_int32_array() -> PackedInt32Array:
	pass;

#desc Returns a copy of the data converted to a [PackedInt64Array], where each block of 8 bytes has been converted to a signed 64-bit integer (C++ [code]int64_t[/code], Godot [int]).
#desc The size of the input array must be a multiple of 8 (size of 64-bit integer). The size of the new array will be [code]byte_array.size() / 8[/code].
#desc If the original data can't be converted to signed 64-bit integers, the resulting data is undefined.
func to_int64_array() -> PackedInt64Array:
	pass;


