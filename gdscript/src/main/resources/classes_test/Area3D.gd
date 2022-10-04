extends CollisionObject3D
#brief 3D area for detection and physics and audio influence.
#desc 3D area that detects [CollisionObject3D] nodes overlapping, entering, or exiting. Can also alter or override local physics parameters (gravity, damping) and route audio to custom audio buses.
#desc To give the area its shape, add a [CollisionShape3D] or a [CollisionPolygon3D] node as a [i]direct[/i] child (or add multiple such nodes as direct children) of the area.
#desc [b]Warning:[/b] See [ConcavePolygonShape3D] (also called "trimesh") for a warning about possibly unexpected behavior when using that shape for an area.
class_name Area3D

#desc This area does not affect gravity/damping.
const SPACE_OVERRIDE_DISABLED = 0;

#desc This area adds its gravity/damping values to whatever has been calculated so far (in [member priority] order).
const SPACE_OVERRIDE_COMBINE = 1;

#desc This area adds its gravity/damping values to whatever has been calculated so far (in [member priority] order), ignoring any lower priority areas.
const SPACE_OVERRIDE_COMBINE_REPLACE = 2;

#desc This area replaces any gravity/damping, even the defaults, ignoring any lower priority areas.
const SPACE_OVERRIDE_REPLACE = 3;

#desc This area replaces any gravity/damping calculated so far (in [member priority] order), but keeps calculating the rest of the areas.
const SPACE_OVERRIDE_REPLACE_COMBINE = 4;


#desc The rate at which objects stop spinning in this area. Represents the angular velocity lost per second.
#desc See [member ProjectSettings.physics/3d/default_angular_damp] for more details about damping.
var angular_damp: float;

#desc Override mode for angular damping calculations within this area. See [enum SpaceOverride] for possible values.
var angular_damp_space_override: int;

#desc The name of the area's audio bus.
var audio_bus_name: StringName;

#desc If [code]true[/code], the area's audio bus overrides the default audio bus.
var audio_bus_override: bool;

#desc The area's gravity intensity (in meters per second squared). This value multiplies the gravity direction. This is useful to alter the force of gravity without altering its direction.
var gravity: float;

#desc The area's gravity vector (not normalized).
var gravity_direction: Vector3;

#desc If [code]true[/code], gravity is calculated from a point (set via [member gravity_point_center]). See also [member gravity_space_override].
var gravity_point: bool;

#desc If gravity is a point (see [member gravity_point]), this will be the point of attraction.
var gravity_point_center: Vector3;

#desc The falloff factor for point gravity. The greater the value, the faster gravity decreases with distance.
var gravity_point_distance_scale: float;

#desc Override mode for gravity calculations within this area. See [enum SpaceOverride] for possible values.
var gravity_space_override: int;

#desc The rate at which objects stop moving in this area. Represents the linear velocity lost per second.
#desc See [member ProjectSettings.physics/3d/default_linear_damp] for more details about damping.
var linear_damp: float;

#desc Override mode for linear damping calculations within this area. See [enum SpaceOverride] for possible values.
var linear_damp_space_override: int;

#desc If [code]true[/code], other monitoring areas can detect this area.
var monitorable: bool;

#desc If [code]true[/code], the area detects bodies or areas entering and exiting it.
var monitoring: bool;

#desc The area's priority. Higher priority areas are processed first.
var priority: float;

#desc The degree to which this area applies reverb to its associated audio. Ranges from [code]0[/code] to [code]1[/code] with [code]0.1[/code] precision.
var reverb_bus_amount: float;

#desc If [code]true[/code], the area applies reverb to its associated audio.
var reverb_bus_enable: bool;

#desc The reverb bus name to use for this area's associated audio.
var reverb_bus_name: StringName;

#desc The degree to which this area's reverb is a uniform effect. Ranges from [code]0[/code] to [code]1[/code] with [code]0.1[/code] precision.
var reverb_bus_uniformity: float;

#desc The exponential rate at which wind force decreases with distance from its origin.
var wind_attenuation_factor: float;

#desc The magnitude of area-specific wind force.
var wind_force_magnitude: float;

#desc The [Node3D] which is used to specify the the direction and origin of an area-specific wind force. The direction is opposite to the z-axis of the [Node3D]'s local transform, and its origin is the origin of the [Node3D]'s local transform.
var wind_source_path: NodePath;



#desc Returns a list of intersecting [Area3D]s. The overlapping area's [member CollisionObject3D.collision_layer] must be part of this area's [member CollisionObject3D.collision_mask] in order to be detected.
#desc For performance reasons (collisions are all processed at the same time) this list is modified once during the physics step, not immediately after objects are moved. Consider using signals instead.
func get_overlapping_areas() -> Area3D[]:
	pass;

#desc Returns a list of intersecting [PhysicsBody3D]s and [GridMap]s. The overlapping body's [member CollisionObject3D.collision_layer] must be part of this area's [member CollisionObject3D.collision_mask] in order to be detected.
#desc For performance reasons (collisions are all processed at the same time) this list is modified once during the physics step, not immediately after objects are moved. Consider using signals instead.
func get_overlapping_bodies() -> Node3D[]:
	pass;

#desc Returns [code]true[/code] if intersecting any [Area3D]s, otherwise returns [code]false[/code]. The overlapping area's [member CollisionObject3D.collision_layer] must be part of this area's [member CollisionObject3D.collision_mask] in order to be detected.
#desc For performance reasons (collisions are all processed at the same time) the list of overlapping areas is modified once during the physics step, not immediately after objects are moved. Consider using signals instead.
func has_overlapping_areas() -> bool:
	pass;

#desc Returns [code]true[/code] if intersecting any [PhysicsBody3D]s or [GridMap]s, otherwise returns [code]false[/code]. The overlapping body's [member CollisionObject3D.collision_layer] must be part of this area's [member CollisionObject3D.collision_mask] in order to be detected.
#desc For performance reasons (collisions are all processed at the same time) the list of overlapping bodies is modified once during the physics step, not immediately after objects are moved. Consider using signals instead.
func has_overlapping_bodies() -> bool:
	pass;

#desc Returns [code]true[/code] if the given [Area3D] intersects or overlaps this [Area3D], [code]false[/code] otherwise.
#desc [b]Note:[/b] The result of this test is not immediate after moving objects. For performance, list of overlaps is updated once per frame and before the physics step. Consider using signals instead.
func overlaps_area(area: Node) -> bool:
	pass;

#desc Returns [code]true[/code] if the given physics body intersects or overlaps this [Area3D], [code]false[/code] otherwise.
#desc [b]Note:[/b] The result of this test is not immediate after moving objects. For performance, list of overlaps is updated once per frame and before the physics step. Consider using signals instead.
#desc The [param body] argument can either be a [PhysicsBody3D] or a [GridMap] instance. While GridMaps are not physics body themselves, they register their tiles with collision shapes as a virtual physics body.
func overlaps_body(body: Node) -> bool:
	pass;


