#brief This object contains all data related to a pose on a tracked object.
#desc XR runtimes often identify multiple locations on devices such as controllers that are spatially tracked.
#desc Orientation, location, linear velocity and angular velocity are all provided for each pose by the XR runtime. This object contains this state of a pose.
class_name XRPose

#desc No tracking information is available for this pose.
const XR_TRACKING_CONFIDENCE_NONE = 0;

#desc Tracking information may be inaccurate or estimated. For instance with inside out tracking this would indicate a controller may be (partially) obscured.
const XR_TRACKING_CONFIDENCE_LOW = 1;

#desc Tracking information is deemed accurate and up to date.
const XR_TRACKING_CONFIDENCE_HIGH = 2;


#desc The angular velocity for this pose.
var angular_velocity: Vector3;

#desc If [code]true[/code] our tracking data is up to date. If [code]false[/code] we're no longer receiving new tracking data and our state is whatever that last valid state was.
var has_tracking_data: bool;

#desc The linear velocity of this pose.
var linear_velocity: Vector3;

#desc The name of this pose. Pose names are often driven by an action map setup by the user. Godot does suggest a number of pose names that it expects [XRInterface]s to implement:
#desc - [code]root[/code] defines a root location, often used for tracked objects that do not have further nodes.
#desc - [code]aim[/code] defines the tip of a controller with the orientation pointing outwards, for instance: add your raycasts to this.
#desc - [code]grip[/code] defines the location where the user grips the controller
#desc - [code]skeleton[/code] defines the root location a hand mesh should be placed when using hand tracking and the animated skeleton supplied by the XR runtime.
var name: StringName;

#desc The tracking confidence for this pose, provides insight on how accurate the spatial positioning of this record is.
var tracking_confidence: int;

#desc The transform containing the original and transform as reported by the XR runtime.
var transform: Transform3D;



#desc Returns the [member transform] with world scale and our reference frame applied. This is the transform used to position [XRNode3D] objects.
func get_adjusted_transform() -> Transform3D:
	pass;


