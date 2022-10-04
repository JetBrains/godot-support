#brief Contains a [Curve2D] path for [PathFollow2D] nodes to follow.
#desc Can have [PathFollow2D] child nodes moving along the [Curve2D]. See [PathFollow2D] for more information on usage.
#desc [b]Note:[/b] The path is considered as relative to the moved nodes (children of [PathFollow2D]). As such, the curve should usually start with a zero vector ([code](0, 0)[/code]).
class_name Path2D


#desc A [Curve2D] describing the path.
var curve: Curve2D;




