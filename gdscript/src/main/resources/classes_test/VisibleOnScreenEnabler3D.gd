#brief Enables certain nodes only when approximately visible.
#desc The VisibleOnScreenEnabler3D will disable [RigidBody3D] and [AnimationPlayer] nodes when they are not visible. It will only affect other nodes within the same scene as the VisibleOnScreenEnabler3D itself.
#desc If you just want to receive notifications, use [VisibleOnScreenNotifier3D] instead.
#desc [b]Note:[/b] VisibleOnScreenEnabler3D uses an approximate heuristic for performance reasons. It doesn't take walls and other occlusion into account. The heuristic is an implementation detail and may change in future versions. If you need precise visibility checking, use another method such as adding an [Area3D] node as a child of a [Camera3D] node and/or [method Vector3.dot].
#desc [b]Note:[/b] VisibleOnScreenEnabler3D will not affect nodes added after scene initialization.
class_name VisibleOnScreenEnabler3D

const ENABLE_MODE_INHERIT = 0;

const ENABLE_MODE_ALWAYS = 1;

const ENABLE_MODE_WHEN_PAUSED = 2;


var enable_mode: int;

var enable_node_path: NodePath;




