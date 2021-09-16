extends Resource
class_name CryptoKey


func is_public_only() -> bool:
    pass;
func load(path: String, public_only: bool) -> int:
    pass;
func load_from_string(string_key: String, public_only: bool) -> int:
    pass;
func save(path: String, public_only: bool) -> int:
    pass;
func save_to_string(public_only: bool) -> String:
    pass;
