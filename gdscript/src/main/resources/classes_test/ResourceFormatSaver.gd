extends RefCounted
class_name ResourceFormatSaver



func _get_recognized_extensions(resource: Resource) -> PackedStringArray:
    pass;

func _recognize(resource: Resource) -> bool:
    pass;

func _save(path: String, resource: Resource, flags: int) -> int:
    pass;

