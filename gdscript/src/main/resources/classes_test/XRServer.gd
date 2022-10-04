#brief Server for AR and VR features.
#desc The AR/VR server is the heart of our Advanced and Virtual Reality solution and handles all the processing.
class_name XRServer

#desc The tracker tracks the location of the players head. This is usually a location centered between the players eyes. Note that for handheld AR devices this can be the current location of the device.
const TRACKER_HEAD = 1;

#desc The tracker tracks the location of a controller.
const TRACKER_CONTROLLER = 2;

#desc The tracker tracks the location of a base station.
const TRACKER_BASESTATION = 4;

#desc The tracker tracks the location and size of an AR anchor.
const TRACKER_ANCHOR = 8;

#desc Used internally to filter trackers of any known type.
const TRACKER_ANY_KNOWN = 127;

#desc Used internally if we haven't set the tracker type yet.
const TRACKER_UNKNOWN = 128;

#desc Used internally to select all trackers.
const TRACKER_ANY = 255;

#desc Fully reset the orientation of the HMD. Regardless of what direction the user is looking to in the real world. The user will look dead ahead in the virtual world.
const RESET_FULL_ROTATION = 0;

#desc Resets the orientation but keeps the tilt of the device. So if we're looking down, we keep looking down but heading will be reset.
const RESET_BUT_KEEP_TILT = 1;

#desc Does not reset the orientation of the HMD, only the position of the player gets centered.
const DONT_RESET_ROTATION = 2;


#desc The primary [XRInterface] currently bound to the [XRServer].
var primary_interface: XRInterface;

#desc Allows you to adjust the scale to your game's units. Most AR/VR platforms assume a scale of 1 game world unit = 1 real world meter.
var world_scale: float;



#desc Registers an [XRInterface] object.
func add_interface() -> void:
	pass;

#desc Registers a new [XRPositionalTracker] that tracks a spatial location in real space.
func add_tracker() -> void:
	pass;

#desc This is an important function to understand correctly. AR and VR platforms all handle positioning slightly differently.
#desc For platforms that do not offer spatial tracking, our origin point (0,0,0) is the location of our HMD, but you have little control over the direction the player is facing in the real world.
#desc For platforms that do offer spatial tracking, our origin point depends very much on the system. For OpenVR, our origin point is usually the center of the tracking space, on the ground. For other platforms, it's often the location of the tracking camera.
#desc This method allows you to center your tracker on the location of the HMD. It will take the current location of the HMD and use that to adjust all your tracking data; in essence, realigning the real world to your player's current position in the game world.
#desc For this method to produce usable results, tracking information must be available. This often takes a few frames after starting your game.
#desc You should call this method after a few seconds have passed. For instance, when the user requests a realignment of the display holding a designated button on a controller for a short period of time, or when implementing a teleport mechanism.
func center_on_hmd(rotation_mode: int, keep_height: bool) -> void:
	pass;

#desc Finds an interface by its [param name]. For instance, if your project uses capabilities of an AR/VR platform, you can find the interface for that platform by name and initialize it.
func find_interface() -> XRInterface:
	pass;

#desc Returns the primary interface's transformation.
func get_hmd_transform() -> Transform3D:
	pass;

#desc Returns the interface registered at the given [param idx] index in the list of interfaces.
func get_interface() -> XRInterface:
	pass;

#desc Returns the number of interfaces currently registered with the AR/VR server. If your project supports multiple AR/VR platforms, you can look through the available interface, and either present the user with a selection or simply try to initialize each interface and use the first one that returns [code]true[/code].
func get_interface_count() -> int:
	pass;

#desc Returns a list of available interfaces the ID and name of each interface.
func get_interfaces() -> Dictionary[]:
	pass;

#desc Returns the reference frame transform. Mostly used internally and exposed for GDExtension build interfaces.
func get_reference_frame() -> Transform3D:
	pass;

#desc Returns the positional tracker with the given [param tracker_name].
func get_tracker() -> XRPositionalTracker:
	pass;

#desc Returns a dictionary of trackers for [param tracker_types].
func get_trackers() -> Dictionary:
	pass;

#desc Removes this [param interface].
func remove_interface() -> void:
	pass;

#desc Removes this positional [param tracker].
func remove_tracker() -> void:
	pass;


