extends CollisionObject3D
class_name Area3D
const SPACE_OVERRIDE_DISABLED = 0;
const SPACE_OVERRIDE_COMBINE = 1;
const SPACE_OVERRIDE_COMBINE_REPLACE = 2;
const SPACE_OVERRIDE_REPLACE = 3;
const SPACE_OVERRIDE_REPLACE_COMBINE = 4;

var angular_damp: float;
var audio_bus_name: StringName;
var audio_bus_override: bool;
var gravity: float;
var gravity_distance_scale: float;
var gravity_point: bool;
var gravity_vec: Vector3;
var linear_damp: float;
var monitorable: bool;
var monitoring: bool;
var priority: float;
var reverb_bus_amount: float;
var reverb_bus_enable: bool;
var reverb_bus_name: StringName;
var reverb_bus_uniformity: float;
var space_override: int;

func get_overlapping_areas() -> Array[Area3D]:
    pass;
func get_overlapping_bodies() -> Array[Node3D]:
    pass;
func overlaps_area(area: Node) -> bool:
    pass;
func overlaps_body(body: Node) -> bool:
    pass;
