#brief Canvas drawing layer.
#desc Canvas drawing layer. [CanvasItem] nodes that are direct or indirect children of a [CanvasLayer] will be drawn in that layer. The layer is a numeric index that defines the draw order. The default 2D scene renders with index 0, so a [CanvasLayer] with index -1 will be drawn below, and one with index 1 will be drawn above. This is very useful for HUDs (in layer 1+ or above), or backgrounds (in layer -1 or below).
class_name CanvasLayer


#desc The custom [Viewport] node assigned to the [CanvasLayer]. If [code]null[/code], uses the default viewport instead.
var custom_viewport: Node;

#desc If enabled, the [CanvasLayer] will use the viewport's transform, so it will move when camera moves instead of being anchored in a fixed position on the screen.
#desc Together with [member follow_viewport_scale] it can be used for a pseudo 3D effect.
var follow_viewport_enabled: bool;

#desc Scales the layer when using [member follow_viewport_enabled]. Layers moving into the foreground should have increasing scales, while layers moving into the background should have decreasing scales.
var follow_viewport_scale: float;

#desc Layer index for draw order. Lower values are drawn first.
var layer: int;

#desc The layer's base offset.
var offset: Vector2;

#desc The layer's rotation in radians.
var rotation: float;

#desc The layer's scale.
var scale: Vector2;

#desc The layer's transform.
var transform: Transform2D;

#desc If [code]false[/code], any [CanvasItem] under this [CanvasLayer] will be hidden.
#desc Unlike [member CanvasItem.visible], visibility of a [CanvasLayer] isn't propagated to underlying layers.
var visible: bool;



#desc Returns the RID of the canvas used by this layer.
func get_canvas() -> RID:
	pass;

#desc Hides any [CanvasItem] under this [CanvasLayer]. This is equivalent to setting [member visible] to [code]false[/code].
func hide() -> void:
	pass;

#desc Shows any [CanvasItem] under this [CanvasLayer]. This is equivalent to setting [member visible] to [code]true[/code].
func show() -> void:
	pass;


