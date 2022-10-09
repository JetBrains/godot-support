extends Container
#brief Base class for flow containers.
#desc Arranges child [Control] nodes vertically or horizontally in a left-to-right or top-to-bottom flow.
#desc A line is filled with [Control] nodes until no more fit on the same line, similar to text in an autowrapped label.
class_name FlowContainer


#desc If [code]true[/code], the [FlowContainer] will arrange its children vertically, rather than horizontally.
#desc Can't be changed when using [HFlowContainer] and [VFlowContainer].
var vertical: bool;



#desc Returns the current line count.
func get_line_count() -> int:
	pass;


