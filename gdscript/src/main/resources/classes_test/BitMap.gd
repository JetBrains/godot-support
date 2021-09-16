extends Resource
class_name BitMap


func create(size: Vector2) -> void:
    pass;
func create_from_image_alpha(image: Image, threshold: float) -> void:
    pass;
func get_bit(position: Vector2) -> bool:
    pass;
func get_size() -> Vector2:
    pass;
func get_true_bit_count() -> int:
    pass;
func grow_mask(pixels: int, rect: Rect2) -> void:
    pass;
func opaque_to_polygons(rect: Rect2, epsilon: float) -> Array:
    pass;
func set_bit(position: Vector2, bit: bool) -> void:
    pass;
func set_bit_rect(rect: Rect2, bit: bool) -> void:
    pass;
