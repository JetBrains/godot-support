class_name Array



func Array() -> Array:
    pass;

func Array(from: Array) -> Array:
    pass;

func Array(from: PackedByteArray) -> Array:
    pass;

func Array(from: PackedColorArray) -> Array:
    pass;

func Array(from: PackedFloat32Array) -> Array:
    pass;

func Array(from: PackedFloat64Array) -> Array:
    pass;

func Array(from: PackedInt32Array) -> Array:
    pass;

func Array(from: PackedInt64Array) -> Array:
    pass;

func Array(from: PackedStringArray) -> Array:
    pass;

func Array(from: PackedVector2Array) -> Array:
    pass;

func Array(from: PackedVector3Array) -> Array:
    pass;

func append(value: Variant) -> void:
    pass;

func append_array(array: Array) -> void:
    pass;

func back() -> Variant:
    pass;

func bsearch(value: Variant, before: bool) -> int:
    pass;

func bsearch_custom(value: Variant, func: Callable, before: bool) -> int:
    pass;

func clear() -> void:
    pass;

func count(value: Variant) -> int:
    pass;

func duplicate(deep: bool) -> Array:
    pass;

func erase(value: Variant) -> void:
    pass;

func fill(value: Variant) -> void:
    pass;

func filter(method: Callable) -> Array:
    pass;

func find(what: Variant, from: int) -> int:
    pass;

func find_last(value: Variant) -> int:
    pass;

func front() -> Variant:
    pass;

func has(value: Variant) -> bool:
    pass;

func hash() -> int:
    pass;

func insert(position: int, value: Variant) -> void:
    pass;

func is_empty() -> bool:
    pass;

func map(method: Callable) -> Array:
    pass;

func max() -> Variant:
    pass;

func min() -> Variant:
    pass;

func operator !=(right: Array) -> bool:
    pass;

func operator +(right: Array) -> Array:
    pass;

func operator <(right: Array) -> bool:
    pass;

func operator <=(right: Array) -> bool:
    pass;

func operator ==(right: Array) -> bool:
    pass;

func operator >(right: Array) -> bool:
    pass;

func operator >=(right: Array) -> bool:
    pass;

func operator [](index: int) -> void:
    pass;

func pop_back() -> Variant:
    pass;

func pop_front() -> Variant:
    pass;

func push_back(value: Variant) -> void:
    pass;

func push_front(value: Variant) -> void:
    pass;

func reduce(method: Callable, accum: Variant) -> Variant:
    pass;

func remove(position: int) -> void:
    pass;

func resize(size: int) -> int:
    pass;

func reverse() -> void:
    pass;

func rfind(what: Variant, from: int) -> int:
    pass;

func shuffle() -> void:
    pass;

func size() -> int:
    pass;

func slice(begin: int, end: int, step: int, deep: bool) -> Array:
    pass;

func sort() -> void:
    pass;

func sort_custom(func: Callable) -> void:
    pass;

