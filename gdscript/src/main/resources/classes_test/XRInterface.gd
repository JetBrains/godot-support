extends RefCounted
class_name XRInterface

const XR_NONE = 0;
const XR_MONO = 1;
const XR_STEREO = 2;
const XR_AR = 4;
const XR_EXTERNAL = 8;
const EYE_MONO = 0;
const EYE_LEFT = 1;
const EYE_RIGHT = 2;
const XR_NORMAL_TRACKING = 0;
const XR_EXCESSIVE_MOTION = 1;
const XR_INSUFFICIENT_FEATURES = 2;
const XR_UNKNOWN_TRACKING = 3;
const XR_NOT_TRACKING = 4;

var ar_is_anchor_detection_enabled: bool setget set_anchor_detection_is_enabled, get_anchor_detection_is_enabled;
var interface_is_initialized: bool setget set_is_initialized, is_initialized;
var interface_is_primary: bool setget set_is_primary, is_primary;

func get_camera_feed_id() -> int:
    pass;

func get_capabilities() -> int:
    pass;

func get_name() -> StringName:
    pass;

func get_render_targetsize() -> Vector2:
    pass;

func get_tracking_status() -> int:
    pass;

func get_view_count() -> int:
    pass;

func initialize() -> bool:
    pass;

func uninitialize() -> void:
    pass;

