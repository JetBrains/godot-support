#brief Input event type for screen touch events.
#brief (only available on mobile devices)
#desc Stores multi-touch press/release information. Supports touch press, touch release and [member index] for multi-touch count and order.
class_name InputEventScreenTouch


#desc The touch index in the case of a multi-touch event. One index = one finger.
var index: int;

#desc The touch position, in screen (global) coordinates.
var position: Vector2;

#desc If [code]true[/code], the touch's state is pressed. If [code]false[/code], the touch's state is released.
var pressed: bool;




