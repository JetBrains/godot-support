extends RefCounted
#brief A tracked object.
#desc An instance of this object represents a device that is tracked, such as a controller or anchor point. HMDs aren't represented here as they are handled internally.
#desc As controllers are turned on and the [XRInterface] detects them, instances of this object are automatically added to this list of active tracking objects accessible through the [XRServer].
#desc The [XRController3D] and [XRAnchor3D] both consume objects of this type and should be used in your project. The positional trackers are just under-the-hood objects that make this all work. These are mostly exposed so that GDExtension-based interfaces can interact with them.
class_name XRPositionalTracker

#desc The hand this tracker is held in is unknown or not applicable.
const TRACKER_HAND_UNKNOWN = 0;

#desc This tracker is the left hand controller.
const TRACKER_HAND_LEFT = 1;

#desc This tracker is the right hand controller.
const TRACKER_HAND_RIGHT = 2;


#desc The description of this tracker.
var description: String;

#desc Defines which hand this tracker relates to.
var hand: int;

#desc The unique name of this tracker. The trackers that are available differ between various XR runtimes and can often be configured by the user. Godot maintains a number of reserved names that it expects the [XRInterface] to implement if applicable:
#desc - [code]left_hand[/code] identifies the controller held in the players left hand
#desc - [code]right_hand[/code] identifies the controller held in the players right hand
var name: StringName;

#desc The profile associated with this tracker, interface dependent but will indicate the type of controller being tracked.
var profile: String;

#desc The type of tracker.
var type: int;



#desc Returns an input for this tracker. It can return a boolean, float or [Vector2] value depending on whether the input is a button, trigger or thumbstick/thumbpad.
func get_input(name: StringName) -> Variant:
	pass;

#desc Returns the current [XRPose] state object for the bound [param name] pose.
func get_pose(name: StringName) -> XRPose:
	pass;

#desc Returns [code]true[/code] if the tracker is available and is currently tracking the bound [param name] pose.
func has_pose(name: StringName) -> bool:
	pass;

#desc Marks this pose as invalid, we don't clear the last reported state but it allows users to decide if trackers need to be hidden if we loose tracking or just remain at their last known position.
func invalidate_pose(name: StringName) -> void:
	pass;

#desc Changes the value for the given input. This method is called by a [XRInterface] implementation and should not be used directly.
func set_input(name: StringName, value: Variant) -> void:
	pass;

#desc Sets the transform, linear velocity, angular velocity and tracking confidence for the given pose. This method is called by a [XRInterface] implementation and should not be used directly.
func set_pose(name: StringName, transform: Transform3D, linear_velocity: Vector3, angular_velocity: Vector3, tracking_confidence: int) -> void:
	pass;


