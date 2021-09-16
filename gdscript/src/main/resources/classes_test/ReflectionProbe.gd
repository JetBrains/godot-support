extends VisualInstance3D
class_name ReflectionProbe
const UPDATE_ONCE = 0;
const UPDATE_ALWAYS = 1;
const AMBIENT_DISABLED = 0;
const AMBIENT_ENVIRONMENT = 1;
const AMBIENT_COLOR = 2;

var ambient_color: Color;
var ambient_color_energy: float;
var ambient_mode: int;
var box_projection: bool;
var cull_mask: int;
var enable_shadows: bool;
var extents: Vector3;
var intensity: float;
var interior: bool;
var lod_threshold: float;
var max_distance: float;
var origin_offset: Vector3;
var update_mode: int;

