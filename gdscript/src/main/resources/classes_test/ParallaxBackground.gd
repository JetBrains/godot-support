#brief A node used to create a parallax scrolling background.
#desc A ParallaxBackground uses one or more [ParallaxLayer] child nodes to create a parallax effect. Each [ParallaxLayer] can move at a different speed using [member ParallaxLayer.motion_offset]. This creates an illusion of depth in a 2D game. If not used with a [Camera2D], you must manually calculate the [member scroll_offset].
class_name ParallaxBackground


var layer: int;

#desc The base position offset for all [ParallaxLayer] children.
var scroll_base_offset: Vector2;

#desc The base motion scale for all [ParallaxLayer] children.
var scroll_base_scale: Vector2;

#desc If [code]true[/code], elements in [ParallaxLayer] child aren't affected by the zoom level of the camera.
var scroll_ignore_camera_zoom: bool;

#desc Top-left limits for scrolling to begin. If the camera is outside of this limit, the background will stop scrolling. Must be lower than [member scroll_limit_end] to work.
var scroll_limit_begin: Vector2;

#desc Bottom-right limits for scrolling to end. If the camera is outside of this limit, the background will stop scrolling. Must be higher than [member scroll_limit_begin] to work.
var scroll_limit_end: Vector2;

#desc The ParallaxBackground's scroll value. Calculated automatically when using a [Camera2D], but can be used to manually manage scrolling when no camera is present.
var scroll_offset: Vector2;




