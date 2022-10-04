#brief Server keeping track of different cameras accessible in Godot.
#desc The [CameraServer] keeps track of different cameras accessible in Godot. These are external cameras such as webcams or the cameras on your phone.
#desc It is notably used to provide AR modules with a video feed from the camera.
#desc [b]Note:[/b] This class is currently only implemented on macOS and iOS. On other platforms, no [CameraFeed]s will be available.
class_name CameraServer

#desc The RGBA camera image.
const FEED_RGBA_IMAGE = 0;

#desc The [url=https://en.wikipedia.org/wiki/YCbCr]YCbCr[/url] camera image.
const FEED_YCBCR_IMAGE = 0;

#desc The Y component camera image.
const FEED_Y_IMAGE = 0;

#desc The CbCr component camera image.
const FEED_CBCR_IMAGE = 1;




#desc Adds the camera [param feed] to the camera server.
func add_feed() -> void:
	pass;

#desc Returns an array of [CameraFeed]s.
func feeds() -> CameraFeed[]:
	pass;

#desc Returns the [CameraFeed] corresponding to the camera with the given [param index].
func get_feed() -> CameraFeed:
	pass;

#desc Returns the number of [CameraFeed]s registered.
func get_feed_count() -> int:
	pass;

#desc Removes the specified camera [param feed].
func remove_feed() -> void:
	pass;


