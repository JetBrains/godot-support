extends RefCounted
class_name AESContext

const MODE_ECB_ENCRYPT = 0;
const MODE_ECB_DECRYPT = 1;
const MODE_CBC_ENCRYPT = 2;
const MODE_CBC_DECRYPT = 3;
const MODE_MAX = 4;


func finish() -> void:
    pass;

func get_iv_state() -> PackedByteArray:
    pass;

func start(mode: int, key: PackedByteArray, iv: PackedByteArray) -> int:
    pass;

func update(src: PackedByteArray) -> PackedByteArray:
    pass;

