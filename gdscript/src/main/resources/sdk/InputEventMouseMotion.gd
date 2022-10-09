extends InputEventMouse
#brief Input event type for mouse motion events.
#desc Contains mouse and pen motion information. Supports relative, absolute positions and velocity. See [method Node._input].
#desc [b]Note:[/b] By default, this event is only emitted once per frame rendered at most. If you need more precise input reporting, set [member Input.use_accumulated_input] to [code]false[/code] to make events emitted as often as possible. If you use InputEventMouseMotion to draw lines, consider implementing [url=https://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm]Bresenham's line algorithm[/url] as well to avoid visible gaps in lines if the user is moving the mouse quickly.
class_name InputEventMouseMotion


#desc Returns [code]true[/code] when using the eraser end of a stylus pen.
#desc [b]Note:[/b] This property is implemented on Linux, macOS and Windows.
var pen_inverted: bool;

#desc Represents the pressure the user puts on the pen. Ranges from [code]0.0[/code] to [code]1.0[/code].
var pressure: float;

#desc The mouse position relative to the previous position (position at the last frame).
#desc [b]Note:[/b] Since [InputEventMouseMotion] is only emitted when the mouse moves, the last event won't have a relative position of [code]Vector2(0, 0)[/code] when the user stops moving the mouse.
var relative: Vector2;

#desc Represents the angles of tilt of the pen. Positive X-coordinate value indicates a tilt to the right. Positive Y-coordinate value indicates a tilt toward the user. Ranges from [code]-1.0[/code] to [code]1.0[/code] for both axes.
var tilt: Vector2;

#desc The mouse velocity in pixels per second.
var velocity: Vector2;




