extends Resource
class_name OccluderPolygon2D

const CULL_DISABLED = 0;
const CULL_CLOCKWISE = 1;
const CULL_COUNTER_CLOCKWISE = 2;

var closed: bool setget set_closed, is_closed;
var cull_mode: int setget set_cull_mode, get_cull_mode;
var polygon: PackedVector2Array setget set_polygon, get_polygon;

