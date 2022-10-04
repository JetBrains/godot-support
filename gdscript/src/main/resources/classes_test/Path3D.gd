#brief Contains a [Curve3D] path for [PathFollow3D] nodes to follow.
#desc Can have [PathFollow3D] child nodes moving along the [Curve3D]. See [PathFollow3D] for more information on the usage.
#desc Note that the path is considered as relative to the moved nodes (children of [PathFollow3D]). As such, the curve should usually start with a zero vector [code](0, 0, 0)[/code].
class_name Path3D


#desc A [Curve3D] describing the path.
var curve: Curve3D;




