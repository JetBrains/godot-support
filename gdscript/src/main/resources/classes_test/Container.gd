extends Control
#brief Base node for containers.
#desc Base node for containers. A [Container] contains other controls and automatically arranges them in a certain way.
#desc A Control can inherit this to create custom container classes.
class_name Container

#desc Notification just before children are going to be sorted, in case there's something to process beforehand.
const NOTIFICATION_PRE_SORT_CHILDREN = 50;

#desc Notification for when sorting the children, it must be obeyed immediately.
const NOTIFICATION_SORT_CHILDREN = 51;


var mouse_filter: int;



#desc Implement to return a list of allowed horizontal [enum Control.SizeFlags] for child nodes. This doesn't technically prevent the usages of any other size flags, if your implementation requires that. This only limits the options available to the user in the inspector dock.
#desc [b]Note:[/b] Having no size flags is equal to having [constant Control.SIZE_SHRINK_BEGIN]. As such, this value is always implicitly allowed.
func _get_allowed_size_flags_horizontal() -> PackedInt32Array:
	pass;

#desc Implement to return a list of allowed vertical [enum Control.SizeFlags] for child nodes. This doesn't technically prevent the usages of any other size flags, if your implementation requires that. This only limits the options available to the user in the inspector dock.
#desc [b]Note:[/b] Having no size flags is equal to having [constant Control.SIZE_SHRINK_BEGIN]. As such, this value is always implicitly allowed.
func _get_allowed_size_flags_vertical() -> PackedInt32Array:
	pass;

#desc Fit a child control in a given rect. This is mainly a helper for creating custom container classes.
func fit_child_in_rect(child: Control, rect: Rect2) -> void:
	pass;

#desc Queue resort of the contained children. This is called automatically anyway, but can be called upon request.
func queue_sort() -> void:
	pass;


