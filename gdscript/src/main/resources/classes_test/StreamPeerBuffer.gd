extends StreamPeer
class_name StreamPeerBuffer


var data_array: PackedByteArray setget set_data_array, get_data_array;

func clear() -> void:
    pass;

func duplicate() -> StreamPeerBuffer:
    pass;

func get_position() -> int:
    pass;

func get_size() -> int:
    pass;

func resize(size: int) -> void:
    pass;

func seek(position: int) -> void:
    pass;

