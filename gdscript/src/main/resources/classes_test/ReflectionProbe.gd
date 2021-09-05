extends VisualInstance3D
class_name ReflectionProbe

const UPDATE_ONCE = 0;
const UPDATE_ALWAYS = 1;
const AMBIENT_DISABLED = 0;
const AMBIENT_ENVIRONMENT = 1;
const AMBIENT_COLOR = 2;

var ambient_color: Color setget set_ambient_color, get_ambient_color;
var ambient_color_energy: float setget set_ambient_color_energy, get_ambient_color_energy;
var ambient_mode: int setget set_ambient_mode, get_ambient_mode;
var box_projection: bool setget set_enable_box_projection, is_box_projection_enabled;
var cull_mask: int setget set_cull_mask, get_cull_mask;
var enable_shadows: bool setget set_enable_shadows, are_shadows_enabled;
var extents: Vector3 setget set_extents, get_extents;
var intensity: float setget set_intensity, get_intensity;
var interior: bool setget set_as_interior, is_set_as_interior;
var lod_threshold: float setget set_lod_threshold, get_lod_threshold;
var max_distance: float setget set_max_distance, get_max_distance;
var origin_offset: Vector3 setget set_origin_offset, get_origin_offset;
var update_mode: int setget set_update_mode, get_update_mode;

