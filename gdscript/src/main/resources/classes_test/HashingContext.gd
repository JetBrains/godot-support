extends RefCounted
class_name HashingContext

const HASH_MD5 = 0;
const HASH_SHA1 = 1;
const HASH_SHA256 = 2;


func finish() -> PackedByteArray:
    pass;

func start(type: int) -> int:
    pass;

func update(chunk: PackedByteArray) -> int:
    pass;

