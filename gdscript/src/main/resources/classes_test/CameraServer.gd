extends Object
class_name CameraServer
const FEED_RGBA_IMAGE = 0;
const FEED_YCBCR_IMAGE = 0;
const FEED_Y_IMAGE = 0;
const FEED_CBCR_IMAGE = 1;


func add_feed(feed: CameraFeed) -> void:
    pass;
func feeds() -> Array:
    pass;
func get_feed(index: int) -> CameraFeed:
    pass;
func get_feed_count() -> int:
    pass;
func remove_feed(feed: CameraFeed) -> void:
    pass;
