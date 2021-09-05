class_name PackedFloat32Array



func PackedFloat32Array() -> PackedFloat32Array:
    pass;

func PackedFloat32Array(from: PackedFloat32Array) -> PackedFloat32Array:
    pass;

func PackedFloat32Array(from: Array) -> PackedFloat32Array:
    pass;

func append(value: float) -> bool:
    pass;

func append_array(array: PackedFloat32Array) -> void:
    pass;

func duplicate() -> PackedFloat32Array:
    pass;

func fill(value: float) -> void:
    pass;

func has(value: float) -> bool:
    pass;

func insert(at_index: int, value: float) -> int:
    pass;

func is_empty() -> bool:
    pass;

func operator !=(right: PackedFloat32Array) -> bool:
    pass;

func operator +(right: PackedFloat32Array) -> PackedFloat32Array:
    pass;

func operator ==(right: PackedFloat32Array) -> bool:
    pass;

func operator [](index: int) -> float:
    pass;

func push_back(value: float) -> bool:
    pass;

func remove(index: int) -> void:
    pass;

func resize(new_size: int) -> int:
    pass;

func reverse() -> void:
    pass;

func set(index: int, value: float) -> void:
    pass;

func size() -> int:
    pass;

func sort() -> void:
    pass;

func subarray(from: int, to: int) -> PackedFloat32Array:
    pass;

func to_byte_array() -> PackedByteArray:
    pass;

