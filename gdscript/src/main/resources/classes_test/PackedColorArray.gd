class_name PackedColorArray



func PackedColorArray() -> PackedColorArray:
    pass;

func PackedColorArray(from: PackedColorArray) -> PackedColorArray:
    pass;

func PackedColorArray(from: Array) -> PackedColorArray:
    pass;

func append(value: Color) -> bool:
    pass;

func append_array(array: PackedColorArray) -> void:
    pass;

func duplicate() -> PackedColorArray:
    pass;

func fill(value: Color) -> void:
    pass;

func has(value: Color) -> bool:
    pass;

func insert(at_index: int, value: Color) -> int:
    pass;

func is_empty() -> bool:
    pass;

func operator !=(right: PackedColorArray) -> bool:
    pass;

func operator +(right: PackedColorArray) -> PackedColorArray:
    pass;

func operator ==(right: PackedColorArray) -> bool:
    pass;

func operator [](index: int) -> Color:
    pass;

func push_back(value: Color) -> bool:
    pass;

func remove(index: int) -> void:
    pass;

func resize(new_size: int) -> int:
    pass;

func reverse() -> void:
    pass;

func set(index: int, value: Color) -> void:
    pass;

func size() -> int:
    pass;

func sort() -> void:
    pass;

func subarray(from: int, to: int) -> PackedColorArray:
    pass;

func to_byte_array() -> PackedByteArray:
    pass;

