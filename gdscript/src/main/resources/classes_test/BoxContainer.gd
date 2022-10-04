#brief Base class for box containers.
#desc Arranges child [Control] nodes vertically or horizontally, and rearranges them automatically when their minimum size changes.
class_name BoxContainer

#desc The child controls will be arranged at the beginning of the container, i.e. top if orientation is vertical, left if orientation is horizontal (right for RTL layout).
const ALIGNMENT_BEGIN = 0;

#desc The child controls will be centered in the container.
const ALIGNMENT_CENTER = 1;

#desc The child controls will be arranged at the end of the container, i.e. bottom if orientation is vertical, right if orientation is horizontal (left for RTL layout).
const ALIGNMENT_END = 2;


#desc The alignment of the container's children (must be one of [constant ALIGNMENT_BEGIN], [constant ALIGNMENT_CENTER], or [constant ALIGNMENT_END]).
var alignment: int;

#desc If [code]true[/code], the [BoxContainer] will arrange its children vertically, rather than horizontally.
#desc Can't be changed when using [HBoxContainer] and [VBoxContainer].
var vertical: bool;



#desc Adds a [Control] node to the box as a spacer. If [param begin] is [code]true[/code], it will insert the [Control] node in front of all other children.
func add_spacer() -> Control:
	pass;


