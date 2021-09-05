class_name PackedVector3Array



func PackedVector3Array() -> PackedVector3Array:
    pass;

func PackedVector3Array(from: PackedVector3Array) -> PackedVector3Array:
    pass;

func PackedVector3Array(from: Array) -> PackedVector3Array:
    pass;

func append(value: Vector3) -> bool:
    pass;

func append_array(array: PackedVector3Array) -> void:
    pass;

func duplicate() -> PackedVector3Array:
    pass;

func fill(value: Vector3) -> void:
    pass;

func has(value: Vector3) -> bool:
    pass;

func insert(at_index: int, value: Vector3) -> int:
    pass;

func is_empty() -> bool:
    pass;

func operator !=(right: PackedVector3Array) -> bool:
    pass;

func operator *(right: Transform3D) -> PackedVector3Array:
    pass;

func operator +(right: PackedVector3Array) -> PackedVector3Array:
    pass;

func operator ==(right: PackedVector3Array) -> bool:
    pass;

func operator [](index: int) -> Vector3:
    pass;

func push_back(value: Vector3) -> bool:
    pass;

func remove(index: int) -> void:
    pass;

func resize(new_size: int) -> int:
    pass;

func reverse() -> void:
    pass;

func set(index: int, value: Vector3) -> void:
    pass;

func size() -> int:
    pass;

func sort() -> void:
    pass;

func subarray(from: int, to: int) -> PackedVector3Array:
    pass;

func to_byte_array() -> PackedByteArray:
    pass;

