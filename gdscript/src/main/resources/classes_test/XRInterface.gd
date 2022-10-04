#brief Base class for an XR interface implementation.
#desc This class needs to be implemented to make an AR or VR platform available to Godot and these should be implemented as C++ modules or GDExtension modules. Part of the interface is exposed to GDScript so you can detect, enable and configure an AR or VR platform.
#desc Interfaces should be written in such a way that simply enabling them will give us a working setup. You can query the available interfaces through [XRServer].
class_name XRInterface

#desc No XR capabilities.
const XR_NONE = 0;

#desc This interface can work with normal rendering output (non-HMD based AR).
const XR_MONO = 1;

#desc This interface supports stereoscopic rendering.
const XR_STEREO = 2;

#desc This interface supports quad rendering (not yet supported by Godot).
const XR_QUAD = 4;

#desc this interface supports VR.
const XR_VR = 8;

#desc This interface supports AR (video background and real world tracking).
const XR_AR = 16;

#desc This interface outputs to an external device. If the main viewport is used, the on screen output is an unmodified buffer of either the left or right eye (stretched if the viewport size is not changed to the same aspect ratio of [method get_render_target_size]). Using a separate viewport node frees up the main viewport for other purposes.
const XR_EXTERNAL = 32;

#desc Tracking is behaving as expected.
const XR_NORMAL_TRACKING = 0;

#desc Tracking is hindered by excessive motion (the player is moving faster than tracking can keep up).
const XR_EXCESSIVE_MOTION = 1;

#desc Tracking is hindered by insufficient features, it's too dark (for camera-based tracking), player is blocked, etc.
const XR_INSUFFICIENT_FEATURES = 2;

#desc We don't know the status of the tracking or this interface does not provide feedback.
const XR_UNKNOWN_TRACKING = 3;

#desc Tracking is not functional (camera not plugged in or obscured, lighthouses turned off, etc.).
const XR_NOT_TRACKING = 4;

#desc Play area mode not set or not available.
const XR_PLAY_AREA_UNKNOWN = 0;

#desc Play area only supports orientation tracking, no positional tracking, area will center around player.
const XR_PLAY_AREA_3DOF = 1;

#desc Player is in seated position, limited positional tracking, fixed guardian around player.
const XR_PLAY_AREA_SITTING = 2;

#desc Player is free to move around, full positional tracking.
const XR_PLAY_AREA_ROOMSCALE = 3;

#desc Same as roomscale but origin point is fixed to the center of the physical space, XRServer.center_on_hmd disabled.
const XR_PLAY_AREA_STAGE = 4;


#desc On an AR interface, [code]true[/code] if anchor detection is enabled.
var ar_is_anchor_detection_enabled: bool;

#desc [code]true[/code] if this is the primary interface.
var interface_is_primary: bool;

#desc The play area mode for this interface.
var xr_play_area_mode: int;



#desc If this is an AR interface that requires displaying a camera feed as the background, this method returns the feed ID in the [CameraServer] for this interface.
func get_camera_feed_id() -> int:
	pass;

#desc Returns a combination of [enum Capabilities] flags providing information about the capabilities of this interface.
func get_capabilities() -> int:
	pass;

#desc Returns the name of this interface (OpenXR, OpenVR, OpenHMD, ARKit, etc).
func get_name() -> StringName:
	pass;

#desc Returns an array of vectors that denotes the physical play area mapped to the virtual space around the [XROrigin3D] point. The points form a convex polygon that can be used to react to or visualise the play area. This returns an empty array if this feature is not supported or if the information is not yet available.
func get_play_area() -> PackedVector3Array:
	pass;

#desc Returns the resolution at which we should render our intermediate results before things like lens distortion are applied by the VR platform.
func get_render_target_size() -> Vector2:
	pass;

#desc If supported, returns the status of our tracking. This will allow you to provide feedback to the user whether there are issues with positional tracking.
func get_tracking_status() -> int:
	pass;

#desc Returns the number of views that need to be rendered for this device. 1 for Monoscopic, 2 for Stereoscopic.
func get_view_count() -> int:
	pass;

#desc Call this to initialize this interface. The first interface that is initialized is identified as the primary interface and it will be used for rendering output.
#desc After initializing the interface you want to use you then need to enable the AR/VR mode of a viewport and rendering should commence.
#desc [b]Note:[/b] You must enable the XR mode on the main viewport for any device that uses the main output of Godot, such as for mobile VR.
#desc If you do this for a platform that handles its own output (such as OpenVR) Godot will show just one eye without distortion on screen. Alternatively, you can add a separate viewport node to your scene and enable AR/VR on that viewport. It will be used to output to the HMD, leaving you free to do anything you like in the main window, such as using a separate camera as a spectator camera or rendering something completely different.
#desc While currently not used, you can activate additional interfaces. You may wish to do this if you want to track controllers from other platforms. However, at this point in time only one interface can render to an HMD.
func initialize() -> bool:
	pass;

#desc Is [code]true[/code] if this interface has been initialised.
func is_initialized() -> bool:
	pass;

#desc Is [code]true[/code] if passthrough is enabled.
func is_passthrough_enabled() -> bool:
	pass;

#desc Is [code]true[/code] if this interface supports passthrough.
func is_passthrough_supported() -> bool:
	pass;

#desc Sets the active play area mode, will return [code]false[/code] if the mode can't be used with this interface.
func set_play_area_mode(mode: int) -> bool:
	pass;

#desc Starts passthrough, will return [code]false[/code] if passthrough couldn't be started.
#desc [b]Note:[/b] The viewport used for XR must have a transparent background, otherwise passthrough may not properly render.
func start_passthrough() -> bool:
	pass;

#desc Stops passthrough.
func stop_passthrough() -> void:
	pass;

#desc Call this to find out if a given play area mode is supported by this interface.
func supports_play_area_mode(mode: int) -> bool:
	pass;

#desc Triggers a haptic pulse on a device associated with this interface.
#desc [param action_name] is the name of the action for this pulse.
#desc [param tracker_name] is optional and can be used to direct the pulse to a specific device provided that device is bound to this haptic.
func trigger_haptic_pulse(action_name: String, tracker_name: StringName, frequency: float, amplitude: float, duration_sec: float, delay_sec: float) -> void:
	pass;

#desc Turns the interface off.
func uninitialize() -> void:
	pass;


