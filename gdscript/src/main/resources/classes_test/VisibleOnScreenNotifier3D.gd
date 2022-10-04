#brief Detects approximately when the node is visible on screen.
#desc The VisibleOnScreenNotifier3D detects when it is visible on the screen. It also notifies when its bounding rectangle enters or exits the screen or a [Camera3D]'s view.
#desc If you want nodes to be disabled automatically when they exit the screen, use [VisibleOnScreenEnabler3D] instead.
#desc [b]Note:[/b] VisibleOnScreenNotifier3D uses an approximate heuristic for performance reasons. It doesn't take walls and other occlusion into account. The heuristic is an implementation detail and may change in future versions. If you need precise visibility checking, use another method such as adding an [Area3D] node as a child of a [Camera3D] node and/or [method Vector3.dot].
class_name VisibleOnScreenNotifier3D


#desc The VisibleOnScreenNotifier3D's bounding box.
var aabb: AABB;



#desc If [code]true[/code], the bounding box is on the screen.
#desc [b]Note:[/b] It takes one frame for the node's visibility to be assessed once added to the scene tree, so this method will return [code]false[/code] right after it is instantiated, even if it will be on screen in the draw pass.
func is_on_screen() -> bool:
	pass;


