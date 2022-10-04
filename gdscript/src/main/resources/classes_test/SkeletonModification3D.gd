#brief A resource that operates on bones in a [Skeleton3D].
#desc This resource provides an interface that can be expanded so code that operates on bones in a [Skeleton3D] can be mixed and matched together to create complex interactions.
#desc This is used to provide Godot with a flexible and powerful Inverse Kinematics solution that can be adapted for many different uses.
class_name SkeletonModification3D


#desc When true, the modification's [method _execute] function will be called by the [SkeletonModificationStack3D].
var enabled: bool;

#desc The execution mode for the modification. This tells the modification stack when to execute the modification. Some modifications have settings that are only available in certain execution modes.
var execution_mode: int;



#desc Executes the given modification. This is where the modification performs whatever function it is designed to do.
virtual func _execute(delta: float) -> void:
	pass;

#desc Sets up the modification so it can be executed. This function should be called automatically by the [SkeletonModificationStack3D] containing this modification.
#desc If you need to initialize a modification before use, this is the place to do it!
virtual func _setup_modification(modification_stack: SkeletonModificationStack3D) -> void:
	pass;

#desc Takes a angle and clamps it so it is within the passed-in [param min] and [param max] range. [param invert] will inversely clamp the angle, clamping it to the range outside of the given bounds.
func clamp_angle(angle: float, min: float, max: float, invert: bool) -> float:
	pass;

#desc Returns whether this modification has been successfully setup or not.
func get_is_setup() -> bool:
	pass;

#desc Returns the [SkeletonModificationStack3D] that this modification is bound to. Through the modification stack, you can access the Skeleton3D the modification is operating on.
func get_modification_stack() -> SkeletonModificationStack3D:
	pass;

#desc Manually allows you to set the setup state of the modification. This function should only rarely be used, as the [SkeletonModificationStack3D] the modification is bound to should handle setting the modification up.
func set_is_setup(is_setup: bool) -> void:
	pass;


