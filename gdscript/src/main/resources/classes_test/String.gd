class_name String


func String() -> String:
    pass;
func String(from: String) -> String:
    pass;
func String(from: NodePath) -> String:
    pass;
func String(from: StringName) -> String:
    pass;
func begins_with(text: String) -> bool:
    pass;
func bigrams() -> PackedStringArray:
    pass;
func bin_to_int() -> int:
    pass;
func c_escape() -> String:
    pass;
func c_unescape() -> String:
    pass;
func capitalize() -> String:
    pass;
func casecmp_to(to: String) -> int:
    pass;
func chr(char: int) -> String:
    pass;
func count(what: String, from: int, to: int) -> int:
    pass;
func countn(what: String, from: int, to: int) -> int:
    pass;
func dedent() -> String:
    pass;
func ends_with(text: String) -> bool:
    pass;
func find(what: String, from: int) -> int:
    pass;
func findn(what: String, from: int) -> int:
    pass;
func format(values: Variant, placeholder: String) -> String:
    pass;
func get_base_dir() -> String:
    pass;
func get_basename() -> String:
    pass;
func get_extension() -> String:
    pass;
func get_file() -> String:
    pass;
func hash() -> int:
    pass;
func hex_to_int() -> int:
    pass;
func humanize_size(size: int) -> String:
    pass;
func insert(position: int, what: String) -> String:
    pass;
func is_absolute_path() -> bool:
    pass;
func is_empty() -> bool:
    pass;
func is_rel_path() -> bool:
    pass;
func is_subsequence_of(text: String) -> bool:
    pass;
func is_subsequence_ofi(text: String) -> bool:
    pass;
func is_valid_filename() -> bool:
    pass;
func is_valid_float() -> bool:
    pass;
func is_valid_hex_number(with_prefix: bool) -> bool:
    pass;
func is_valid_html_color() -> bool:
    pass;
func is_valid_identifier() -> bool:
    pass;
func is_valid_int() -> bool:
    pass;
func is_valid_ip_address() -> bool:
    pass;
func join(parts: PackedStringArray) -> String:
    pass;
func json_escape() -> String:
    pass;
func left(position: int) -> String:
    pass;
func length() -> int:
    pass;
func lpad(min_length: int, character: String) -> String:
    pass;
func lstrip(chars: String) -> String:
    pass;
func match(expr: String) -> bool:
    pass;
func matchn(expr: String) -> bool:
    pass;
func md5_buffer() -> PackedByteArray:
    pass;
func md5_text() -> String:
    pass;
func naturalnocasecmp_to(to: String) -> int:
    pass;
func nocasecmp_to(to: String) -> int:
    pass;
func num(number: float, decimals: int) -> String:
    pass;
func num_scientific(number: float) -> String:
    pass;
func pad_decimals(digits: int) -> String:
    pass;
func pad_zeros(digits: int) -> String:
    pass;
func plus_file(file: String) -> String:
    pass;
func repeat(count: int) -> String:
    pass;
func replace(what: String, forwhat: String) -> String:
    pass;
func replacen(what: String, forwhat: String) -> String:
    pass;
func rfind(what: String, from: int) -> int:
    pass;
func rfindn(what: String, from: int) -> int:
    pass;
func right(position: int) -> String:
    pass;
func rpad(min_length: int, character: String) -> String:
    pass;
func rsplit(delimiter: String, allow_empty: bool, maxsplit: int) -> PackedStringArray:
    pass;
func rstrip(chars: String) -> String:
    pass;
func sha1_buffer() -> PackedByteArray:
    pass;
func sha1_text() -> String:
    pass;
func sha256_buffer() -> PackedByteArray:
    pass;
func sha256_text() -> String:
    pass;
func similarity(text: String) -> float:
    pass;
func split(delimiter: String, allow_empty: bool, maxsplit: int) -> PackedStringArray:
    pass;
func split_floats(delimiter: String, allow_empty: bool) -> PackedFloat32Array:
    pass;
func strip_edges(left: bool, right: bool) -> String:
    pass;
func strip_escapes() -> String:
    pass;
func substr(from: int, len: int) -> String:
    pass;
func to_ascii_buffer() -> PackedByteArray:
    pass;
func to_float() -> float:
    pass;
func to_int() -> int:
    pass;
func to_lower() -> String:
    pass;
func to_upper() -> String:
    pass;
func to_utf16_buffer() -> PackedByteArray:
    pass;
func to_utf32_buffer() -> PackedByteArray:
    pass;
func to_utf8_buffer() -> PackedByteArray:
    pass;
func trim_prefix(prefix: String) -> String:
    pass;
func trim_suffix(suffix: String) -> String:
    pass;
func unicode_at(at: int) -> int:
    pass;
func uri_decode() -> String:
    pass;
func uri_encode() -> String:
    pass;
func validate_node_name() -> String:
    pass;
func xml_escape(escape_quotes: bool) -> String:
    pass;
func xml_unescape() -> String:
    pass;
