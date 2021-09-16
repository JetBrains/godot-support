extends Resource
class_name StyleBox

var content_margin_bottom: float;
var content_margin_left: float;
var content_margin_right: float;
var content_margin_top: float;

func draw(canvas_item: RID, rect: Rect2) -> void:
    pass;
func get_center_size() -> Vector2:
    pass;
func get_current_item_drawn() -> CanvasItem:
    pass;
func get_default_margin(margin: int) -> float:
    pass;
func get_margin(margin: int) -> float:
    pass;
func get_minimum_size() -> Vector2:
    pass;
func get_offset() -> Vector2:
    pass;
func set_default_margin(margin: int, offset: float) -> void:
    pass;
func test_mask(point: Vector2, rect: Rect2) -> bool:
    pass;
