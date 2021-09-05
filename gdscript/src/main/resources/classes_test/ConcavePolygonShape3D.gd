extends Shape3D
class_name ConcavePolygonShape3D


var backface_collision: bool setget set_backface_collision_enabled, is_backface_collision_enabled;

func get_faces() -> PackedVector3Array:
    pass;

func set_faces(faces: PackedVector3Array) -> void:
    pass;

