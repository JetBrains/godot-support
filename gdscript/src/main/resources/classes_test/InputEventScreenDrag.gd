#brief Input event type for screen drag events. Only available on mobile devices.
#desc Contains screen drag information. See [method Node._input].
class_name InputEventScreenDrag


#desc The drag event index in the case of a multi-drag event.
var index: int;

#desc The drag position.
var position: Vector2;

#desc The drag position relative to the previous position (position at the last frame).
var relative: Vector2;

#desc The drag velocity.
var velocity: Vector2;




