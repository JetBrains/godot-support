extends Texture2D
#brief Texture provided by a [CameraFeed].
#desc This texture gives access to the camera texture provided by a [CameraFeed].
#desc [b]Note:[/b] Many cameras supply YCbCr images which need to be converted in a shader.
class_name CameraTexture


#desc The ID of the [CameraFeed] for which we want to display the image.
var camera_feed_id: int;

#desc Convenience property that gives access to the active property of the [CameraFeed].
var camera_is_active: bool;

#desc Which image within the [CameraFeed] we want access to, important if the camera image is split in a Y and CbCr component.
var which_feed: int;




