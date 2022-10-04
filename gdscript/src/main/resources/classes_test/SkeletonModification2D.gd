#brief A resource that operates on [Bone2D] nodes in a [Skeleton2D].
#desc This resource provides an interface that can be expanded so code that operates on [Bone2D] nodes in a [Skeleton2D] can be mixed and matched together to create complex interactions.
#desc This is used to provide Godot with a flexible and powerful Inverse Kinematics solution that can be adapted for many different uses.
class_name SkeletonModification2D


#desc If [code]true[/code], the modification's [method _execute] function will be called by the [SkeletonModificationStack2D].
var enabled: bool;

#desc The execution mode for the modification. This tells the modification stack when to execute the modification. Some modifications have settings that are only available in certain execution modes.
var execution_mode: int;



#desc Used for drawing [b]editor-only[/b] modification gizmos. This function will only be called in the Godot editor and can be overridden to draw custom gizmos.
#desc [b]Note:[/b] You will need to use the Skeleton2D from [method SkeletonModificationStack2D.get_skeleton] and it's draw functions, as the [SkeletonModification2D] resource cannot draw on its own.
virtual func _draw_editor_gizmo() -> void:
	pass;

#desc Executes the given modification. This is where the modification performs whatever function it is designed to do.
virtual func _execute() -> void:
	pass;

#desc Called when the modification is setup. This is where the modification performs initialization.
virtual func _setup_modification() -> void:
	pass;

#desc Takes a angle and clamps it so it is within the passed-in [param min] and [param max] range. [param invert] will inversely clamp the angle, clamping it to the range outside of the given bounds.
func clamp_angle(angle: float, min: float, max: float, invert: bool) -> float:
	pass;

#desc Returns whether this modification will call [method _draw_editor_gizmo] in the Godot editor to draw modification-specific gizmos.
func get_editor_draw_gizmo() -> bool:
	pass;

#desc Returns whether this modification has been successfully setup or not.
func get_is_setup() -> bool:
	pass;

#desc Returns the [SkeletonModificationStack2D] that this modification is bound to. Through the modification stack, you can access the Skeleton3D the modification is operating on.
func get_modification_stack() -> SkeletonModificationStack2D:
	pass;

#desc Sets whether this modification will call [method _draw_editor_gizmo] in the Godot editor to draw modification-specific gizmos.
func set_editor_draw_gizmo() -> void:
	pass;

#desc Manually allows you to set the setup state of the modification. This function should only rarely be used, as the [SkeletonModificationStack2D] the modification is bound to should handle setting the modification up.
func set_is_setup() -> void:
	pass;


