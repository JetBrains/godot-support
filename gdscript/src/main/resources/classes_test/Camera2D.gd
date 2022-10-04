#brief Camera node for 2D scenes.
#desc Camera node for 2D scenes. It forces the screen (current layer) to scroll following this node. This makes it easier (and faster) to program scrollable scenes than manually changing the position of [CanvasItem]-based nodes.
#desc Cameras register themselves in the nearest [Viewport] node (when ascending the tree). Only one camera can be active per viewport. If no viewport is available ascending the tree, the camera will register in the global viewport.
#desc This node is intended to be a simple helper to get things going quickly, but more functionality may be desired to change how the camera works. To make your own custom camera node, inherit it from [Node2D] and change the transform of the canvas by setting [member Viewport.canvas_transform] in [Viewport] (you can obtain the current [Viewport] by using [method Node.get_viewport]).
#desc Note that the [Camera2D] node's [code]position[/code] doesn't represent the actual position of the screen, which may differ due to applied smoothing or limits. You can use [method get_screen_center_position] to get the real position.
class_name Camera2D

#desc The camera's position is fixed so that the top-left corner is always at the origin.
const ANCHOR_MODE_FIXED_TOP_LEFT = 0;

#desc The camera's position takes into account vertical/horizontal offsets and the screen size.
const ANCHOR_MODE_DRAG_CENTER = 1;

#desc The camera updates with the [code]_physics_process[/code] callback.
const CAMERA2D_PROCESS_PHYSICS = 0;

#desc The camera updates with the [code]_process[/code] callback.
const CAMERA2D_PROCESS_IDLE = 1;


#desc The Camera2D's anchor point. See [enum AnchorMode] constants.
var anchor_mode: int;

#desc If [code]true[/code], the camera acts as the active camera for its [Viewport] ancestor. Only one camera can be current in a given viewport, so setting a different camera in the same viewport [code]current[/code] will disable whatever camera was already active in that viewport.
var current: bool;

#desc The custom [Viewport] node attached to the [Camera2D]. If [code]null[/code] or not a [Viewport], uses the default viewport instead.
var custom_viewport: Node;

#desc Bottom margin needed to drag the camera. A value of [code]1[/code] makes the camera move only when reaching the bottom edge of the screen.
var drag_bottom_margin: float;

#desc If [code]true[/code], the camera only moves when reaching the horizontal (left and right) drag margins. If [code]false[/code], the camera moves horizontally regardless of margins.
var drag_horizontal_enabled: bool;

#desc The relative horizontal drag offset of the camera between the right ([code]-1[/code]) and left ([code]1[/code]) drag margins.
#desc [b]Note:[/b] Used to set the initial horizontal drag offset; determine the current offset; or force the current offset. It's not automatically updated when [member drag_horizontal_enabled] is [code]true[/code] or the drag margins are changed.
var drag_horizontal_offset: float;

#desc Left margin needed to drag the camera. A value of [code]1[/code] makes the camera move only when reaching the left edge of the screen.
var drag_left_margin: float;

#desc Right margin needed to drag the camera. A value of [code]1[/code] makes the camera move only when reaching the right edge of the screen.
var drag_right_margin: float;

#desc Top margin needed to drag the camera. A value of [code]1[/code] makes the camera move only when reaching the top edge of the screen.
var drag_top_margin: float;

#desc If [code]true[/code], the camera only moves when reaching the vertical (top and bottom) drag margins. If [code]false[/code], the camera moves vertically regardless of the drag margins.
var drag_vertical_enabled: bool;

#desc The relative vertical drag offset of the camera between the bottom ([code]-1[/code]) and top ([code]1[/code]) drag margins.
#desc [b]Note:[/b] Used to set the initial vertical drag offset; determine the current offset; or force the current offset. It's not automatically updated when [member drag_vertical_enabled] is [code]true[/code] or the drag margins are changed.
var drag_vertical_offset: float;

#desc If [code]true[/code], draws the camera's drag margin rectangle in the editor.
var editor_draw_drag_margin: bool;

#desc If [code]true[/code], draws the camera's limits rectangle in the editor.
var editor_draw_limits: bool;

#desc If [code]true[/code], draws the camera's screen rectangle in the editor.
var editor_draw_screen: bool;

#desc If [code]true[/code], the camera's rendered view is not affected by its [member Node2D.rotation] and [member Node2D.global_rotation].
var ignore_rotation: bool;

#desc Bottom scroll limit in pixels. The camera stops moving when reaching this value, but [member offset] can push the view past the limit.
var limit_bottom: int;

#desc Left scroll limit in pixels. The camera stops moving when reaching this value, but [member offset] can push the view past the limit.
var limit_left: int;

#desc Right scroll limit in pixels. The camera stops moving when reaching this value, but [member offset] can push the view past the limit.
var limit_right: int;

#desc If [code]true[/code], the camera smoothly stops when reaches its limits.
#desc This property has no effect if [member smoothing_enabled] is [code]false[/code].
#desc [b]Note:[/b] To immediately update the camera's position to be within limits without smoothing, even with this setting enabled, invoke [method reset_smoothing].
var limit_smoothed: bool;

#desc Top scroll limit in pixels. The camera stops moving when reaching this value, but [member offset] can push the view past the limit.
var limit_top: int;

#desc The camera's relative offset. Useful for looking around or camera shake animations. The offsetted camera can go past the limits defined in [member limit_top], [member limit_bottom], [member limit_left] and [member limit_right].
var offset: Vector2;

#desc The camera's process callback. See [enum Camera2DProcessCallback].
var process_callback: int;

#desc If [code]true[/code], the camera smoothly moves towards the target at [member smoothing_speed].
var smoothing_enabled: bool;

#desc Speed in pixels per second of the camera's smoothing effect when [member smoothing_enabled] is [code]true[/code].
var smoothing_speed: float;

#desc The camera's zoom. A zoom of [code]Vector(2, 2)[/code] doubles the size seen in the viewport. A zoom of [code]Vector(0.5, 0.5)[/code] halves the size seen in the viewport.
var zoom: Vector2;



#desc Aligns the camera to the tracked node.
func align() -> void:
	pass;

#desc Forces the camera to update scroll immediately.
func force_update_scroll() -> void:
	pass;

#desc Returns the specified [enum Side]'s margin. See also [member drag_bottom_margin], [member drag_top_margin], [member drag_left_margin], and [member drag_right_margin].
func get_drag_margin() -> float:
	pass;

#desc Returns the camera limit for the specified [enum Side]. See also [member limit_bottom], [member limit_top], [member limit_left], and [member limit_right].
func get_limit() -> int:
	pass;

#desc Returns the center of the screen from this camera's point of view, in global coordinates.
#desc [b]Note:[/b] The exact targeted position of the camera may be different. See [method get_target_position].
func get_screen_center_position() -> Vector2:
	pass;

#desc Returns this camera's target position, in global coordinates.
#desc [b]Note:[/b] The returned value is not the same as [member Node2D.global_position], as it is affected by the drag properties. It is also not the same as the current position if [member smoothing_enabled] is [code]true[/code] (see [method get_screen_center_position]).
func get_target_position() -> Vector2:
	pass;

#desc Sets the camera's position immediately to its current smoothing destination.
#desc This method has no effect if [member smoothing_enabled] is [code]false[/code].
func reset_smoothing() -> void:
	pass;

#desc Sets the specified [enum Side]'s margin. See also [member drag_bottom_margin], [member drag_top_margin], [member drag_left_margin], and [member drag_right_margin].
func set_drag_margin(margin: int, drag_margin: float) -> void:
	pass;

#desc Sets the camera limit for the specified [enum Side]. See also [member limit_bottom], [member limit_top], [member limit_left], and [member limit_right].
func set_limit(margin: int, limit: int) -> void:
	pass;


