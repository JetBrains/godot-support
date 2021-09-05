class_name PackedFloat64Array



func PackedFloat64Array() -> PackedFloat64Array:
    pass;

func PackedFloat64Array(from: PackedFloat64Array) -> PackedFloat64Array:
    pass;

func PackedFloat64Array(from: Array) -> PackedFloat64Array:
    pass;

func append(value: float) -> bool:
    pass;

func append_array(array: PackedFloat64Array) -> void:
    pass;

func duplicate() -> PackedFloat64Array:
    pass;

func fill(value: float) -> void:
    pass;

func has(value: float) -> bool:
    pass;

func insert(at_index: int, value: float) -> int:
    pass;

func is_empty() -> bool:
    pass;

func operator !=(right: PackedFloat64Array) -> bool:
    pass;

func operator +(right: PackedFloat64Array) -> PackedFloat64Array:
    pass;

func operator ==(right: PackedFloat64Array) -> bool:
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

func subarray(from: int, to: int) -> PackedFloat64Array:
    pass;

func to_byte_array() -> PackedByteArray:
    pass;

