extends RefCounted
class_name HMACContext



func finish() -> PackedByteArray:
    pass;

func start(hash_type: int, key: PackedByteArray) -> int:
    pass;

func update(data: PackedByteArray) -> int:
    pass;

