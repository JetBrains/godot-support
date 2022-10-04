#brief Base input event type for mouse events.
#desc Stores general mouse events information.
class_name InputEventMouse


#desc The mouse button mask identifier, one of or a bitwise combination of the [enum MouseButton] button masks.
var button_mask: int;

#desc When received in [method Node._input] or [method Node._unhandled_input], returns the mouse's position in the root [Viewport] using the coordinate system of the root [Viewport].
#desc When received in [method Control._gui_input], returns the mouse's position in the [CanvasLayer] that the [Control] is in using the coordinate system of the [CanvasLayer].
var global_position: Vector2;

#desc When received in [method Node._input] or [method Node._unhandled_input], returns the mouse's position in the [Viewport] this [Node] is in using the coordinate system of this [Viewport].
#desc When received in [method Control._gui_input], returns the mouse's position in the [Control] using the local coordinate system of the [Control].
var position: Vector2;




