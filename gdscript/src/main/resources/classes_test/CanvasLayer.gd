extends Node
class_name CanvasLayer

var custom_viewport: Node;
var follow_viewport_enable: bool;
var follow_viewport_scale: float;
var layer: int;
var offset: Vector2;
var rotation: float;
var scale: Vector2;
var transform: Transform2D;

func get_canvas() -> RID:
    pass;
