#brief Defines a 2D polygon for LightOccluder2D.
#desc Editor facility that helps you draw a 2D polygon used as resource for [LightOccluder2D].
class_name OccluderPolygon2D

#desc Culling is disabled. See [member cull_mode].
const CULL_DISABLED = 0;

#desc Culling is performed in the clockwise direction. See [member cull_mode].
const CULL_CLOCKWISE = 1;

#desc Culling is performed in the counterclockwise direction. See [member cull_mode].
const CULL_COUNTER_CLOCKWISE = 2;


#desc If [code]true[/code], closes the polygon. A closed OccluderPolygon2D occludes the light coming from any direction. An opened OccluderPolygon2D occludes the light only at its outline's direction.
var closed: bool;

#desc The culling mode to use.
var cull_mode: int;

#desc A [Vector2] array with the index for polygon's vertices positions.
#desc [b]Note:[/b] The returned value is a copy of the underlying array, rather than a reference.
var polygon: PackedVector2Array;




