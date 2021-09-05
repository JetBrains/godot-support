extends Node
class_name CanvasLayer


var custom_viewport: Node setget set_custom_viewport, get_custom_viewport;
var follow_viewport_enable: bool setget set_follow_viewport, is_following_viewport;
var follow_viewport_scale: float setget set_follow_viewport_scale, get_follow_viewport_scale;
var layer: int setget set_layer, get_layer;
var offset: Vector2 setget set_offset, get_offset;
var rotation: float setget set_rotation, get_rotation;
var scale: Vector2 setget set_scale, get_scale;
var transform: Transform2D setget set_transform, get_transform;

func get_canvas() -> RID:
    pass;

