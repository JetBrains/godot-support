extends RefCounted
class_name PCKPacker



func add_file(pck_path: String, source_path: String, encrypt: bool) -> int:
    pass;

func flush(verbose: bool) -> int:
    pass;

func pck_start(pck_name: String, alignment: int, key: String, encrypt_directory: bool) -> int:
    pass;

