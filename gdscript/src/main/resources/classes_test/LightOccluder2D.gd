extends Node2D
#brief Occludes light cast by a Light2D, casting shadows.
#desc Occludes light cast by a Light2D, casting shadows. The LightOccluder2D must be provided with an [OccluderPolygon2D] in order for the shadow to be computed.
class_name LightOccluder2D


#desc The [OccluderPolygon2D] used to compute the shadow.
var occluder: OccluderPolygon2D;

#desc The LightOccluder2D's occluder light mask. The LightOccluder2D will cast shadows only from Light2D(s) that have the same light mask(s).
var occluder_light_mask: int;

var sdf_collision: bool;




