class_name PropertyOwner

var _value: int = 0

var my_property: int:
    get = get_my_property,
    set = set_my_property

func get_my_property() -> int:
    return _value

func set_my_property(value: int) -> void:
    _value = value
