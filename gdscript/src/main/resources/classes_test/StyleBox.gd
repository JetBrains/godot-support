extends Resource
#brief Base class for drawing stylized boxes for the UI.
#desc StyleBox is [Resource] that provides an abstract base class for drawing stylized boxes for the UI. StyleBoxes are used for drawing the styles of buttons, line edit backgrounds, tree backgrounds, etc. and also for testing a transparency mask for pointer signals. If mask test fails on a StyleBox assigned as mask to a control, clicks and motion signals will go through it to the one below.
#desc [b]Note:[/b] For children of [Control] that have [i]Theme Properties[/i], the [code]focus[/code] [StyleBox] is displayed over the [code]normal[/code], [code]hover[/code] or [code]pressed[/code] [StyleBox]. This makes the [code]focus[/code] [StyleBox] more reusable across different nodes.
class_name StyleBox


#desc The bottom margin for the contents of this style box. Increasing this value reduces the space available to the contents from the bottom.
#desc If this value is negative, it is ignored and a child-specific margin is used instead. For example for [StyleBoxFlat] the border thickness (if any) is used instead.
#desc It is up to the code using this style box to decide what these contents are: for example, a [Button] respects this content margin for the textual contents of the button.
#desc [method get_margin] should be used to fetch this value as consumer instead of reading these properties directly. This is because it correctly respects negative values and the fallback mentioned above.
var content_margin_bottom: float;

#desc The left margin for the contents of this style box.	Increasing this value reduces the space available to the contents from the left.
#desc Refer to [member content_margin_bottom] for extra considerations.
var content_margin_left: float;

#desc The right margin for the contents of this style box. Increasing this value reduces the space available to the contents from the right.
#desc Refer to [member content_margin_bottom] for extra considerations.
var content_margin_right: float;

#desc The top margin for the contents of this style box. Increasing this value reduces the space available to the contents from the top.
#desc Refer to [member content_margin_bottom] for extra considerations.
var content_margin_top: float;



func _draw(to_canvas_item: RID, rect: Rect2) -> void:
	pass;

func _get_center_size() -> Vector2:
	pass;

func _get_draw_rect(rect: Rect2) -> Rect2:
	pass;

func _get_style_margin(side: int) -> float:
	pass;

func _test_mask(point: Vector2, rect: Rect2) -> bool:
	pass;

#desc Draws this stylebox using a canvas item identified by the given [RID].
#desc The [RID] value can either be the result of [method CanvasItem.get_canvas_item] called on an existing [CanvasItem]-derived node, or directly from creating a canvas item in the [RenderingServer] with [method RenderingServer.canvas_item_create].
func draw(canvas_item: RID, rect: Rect2) -> void:
	pass;

#desc Returns the size of this [StyleBox] without the margins.
func get_center_size() -> Vector2:
	pass;

#desc Returns the [CanvasItem] that handles its [constant CanvasItem.NOTIFICATION_DRAW] or [method CanvasItem._draw] callback at this moment.
func get_current_item_drawn() -> CanvasItem:
	pass;

#desc Returns the default margin of the specified [enum Side].
func get_default_margin(margin: int) -> float:
	pass;

#desc Returns the content margin offset for the specified [enum Side].
#desc Positive values reduce size inwards, unlike [Control]'s margin values.
func get_margin(margin: int) -> float:
	pass;

#desc Returns the minimum size that this stylebox can be shrunk to.
func get_minimum_size() -> Vector2:
	pass;

#desc Returns the "offset" of a stylebox. This helper function returns a value equivalent to [code]Vector2(style.get_margin(MARGIN_LEFT), style.get_margin(MARGIN_TOP))[/code].
func get_offset() -> Vector2:
	pass;

#desc Sets the default value of the specified [enum Side] to [param offset] pixels.
func set_default_margin(margin: int, offset: float) -> void:
	pass;

#desc Sets the default margin to [param offset] pixels for all sides.
func set_default_margin_all(offset: float) -> void:
	pass;

#desc Test a position in a rectangle, return whether it passes the mask test.
func test_mask(point: Vector2, rect: Rect2) -> bool:
	pass;


