#brief A modification that holds and executes a [SkeletonModificationStack2D].
#desc This [SkeletonModification2D] holds a reference to a [SkeletonModificationStack2D], allowing you to use multiple modification stacks on a single [Skeleton2D].
#desc [b]Note:[/b] The modifications in the held [SkeletonModificationStack2D] will only be executed if their execution mode matches the execution mode of the SkeletonModification2DStackHolder.
class_name SkeletonModification2DStackHolder




#desc Returns the [SkeletonModificationStack2D] that this modification is holding.
func get_held_modification_stack() -> SkeletonModificationStack2D:
	pass;

#desc Sets the [SkeletonModificationStack2D] that this modification is holding. This modification stack will then be executed when this modification is executed.
func set_held_modification_stack(held_modification_stack: SkeletonModificationStack2D) -> void:
	pass;


