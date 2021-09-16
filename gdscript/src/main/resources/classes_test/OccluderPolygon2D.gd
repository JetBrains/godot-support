extends Resource
class_name OccluderPolygon2D
const CULL_DISABLED = 0;
const CULL_CLOCKWISE = 1;
const CULL_COUNTER_CLOCKWISE = 2;

var closed: bool;
var cull_mode: int;
var polygon: PackedVector2Array;

