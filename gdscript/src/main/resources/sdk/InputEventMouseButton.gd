extends InputEventMouse
#brief Input event type for mouse button events.
#desc Contains mouse click information. See [method Node._input].
class_name InputEventMouseButton


#desc The mouse button identifier, one of the [enum MouseButton] button or button wheel constants.
var button_index: int;

#desc If [code]true[/code], the mouse button's state is a double-click.
var double_click: bool;

#desc The amount (or delta) of the event. When used for high-precision scroll events, this indicates the scroll amount (vertical or horizontal). This is only supported on some platforms; the reported sensitivity varies depending on the platform. May be [code]0[/code] if not supported.
var factor: float;

#desc If [code]true[/code], the mouse button's state is pressed. If [code]false[/code], the mouse button's state is released.
var pressed: bool;




