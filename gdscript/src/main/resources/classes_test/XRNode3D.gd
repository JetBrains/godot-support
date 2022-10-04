#brief A spatial node that has its position automatically updated by the [XRServer].
#desc This node can be bound to a specific pose of a [XRPositionalTracker] and will automatically have its [member Node3D.transform] updated by the [XRServer]. Nodes of this type must be added as children of the [XROrigin3D] node.
class_name XRNode3D


#desc The name of the pose we're bound to. Which poses a tracker supports is not known during design time.
#desc Godot defines number of standard pose names such as [code]aim[/code] and [code]grip[/code] but other may be configured within a given [XRInterface].
var pose: StringName;

#desc The name of the tracker we're bound to. Which trackers are available is not known during design time.
#desc Godot defines a number of standard trackers such as [code]left_hand[/code] and [code]right_hand[/code] but others may be configured within a given [XRInterface].
var tracker: StringName;



#desc Returns [code]true[/code] if the [member tracker] has current tracking data for the [member pose] being tracked.
func get_has_tracking_data() -> bool:
	pass;

#desc Returns [code]true[/code] if the [member tracker] has been registered and the [member pose] is being tracked.
func get_is_active() -> bool:
	pass;

#desc Returns the [XRPose] containing the current state of the pose being tracked. This gives access to additional properties of this pose.
func get_pose() -> XRPose:
	pass;

#desc Triggers a haptic pulse on a device associated with this interface.
#desc [param action_name] is the name of the action for this pulse.
func trigger_haptic_pulse(action_name: String, frequency: float, amplitude: float, duration_sec: float, delay_sec: float) -> void:
	pass;


