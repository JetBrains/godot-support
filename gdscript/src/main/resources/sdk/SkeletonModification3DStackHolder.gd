extends SkeletonModification3D
#brief A modification that holds and executes a [SkeletonModificationStack3D].
#desc This [SkeletonModification3D] holds a reference to a [SkeletonModificationStack3D], allowing you to use multiple modification stacks on a single [Skeleton3D].
#desc [b]Note:[/b] The modifications in the held [SkeletonModificationStack3D] will only be executed if their execution mode matches the execution mode of the SkeletonModification3DStackHolder.
class_name SkeletonModification3DStackHolder




#desc Returns the [SkeletonModificationStack3D] that this modification is holding.
func get_held_modification_stack() -> SkeletonModificationStack3D:
	pass;

#desc Sets the [SkeletonModificationStack3D] that this modification is holding. This modification stack will then be executed when this modification is executed.
func set_held_modification_stack(held_modification_stack: SkeletonModificationStack3D) -> void:
	pass;


