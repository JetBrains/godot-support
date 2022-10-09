extends RefCounted
#brief Parameters to be sent to a 3D body motion test.
#desc This class contains parameters used in [method PhysicsServer3D.body_test_motion].
class_name PhysicsTestMotionParameters3D


#desc If set to [code]true[/code], shapes of type [constant PhysicsServer3D.SHAPE_SEPARATION_RAY] are used to detect collisions and can stop the motion. Can be useful when snapping to the ground.
#desc If set to [code]false[/code], shapes of type [constant PhysicsServer3D.SHAPE_SEPARATION_RAY] are only used for separation when overlapping with other bodies. That's the main use for separation ray shapes.
var collide_separation_ray: bool;

#desc Optional array of body [RID] to exclude from collision.
var exclude_bodies: Array;

#desc Optional array of object unique instance ID to exclude from collision. See [method Object.get_instance_id].
var exclude_objects: Array;

#desc Transform in global space where the motion should start. Usually set to [member Node3D.global_transform] for the current body's transform.
var from: Transform3D;

#desc Increases the size of the shapes involved in the collision detection.
var margin: float;

#desc Maximum number of returned collisions, between [code]1[/code] and [code]32[/code]. Always returns the deepest detected collisions.
var max_collisions: int;

#desc Motion vector to define the length and direction of the motion to test.
var motion: Vector3;

#desc If set to [code]true[/code], any depenetration from the recovery phase is reported as a collision; this is used e.g. by [CharacterBody3D] for improving floor detection during floor snapping.
#desc If set to [code]false[/code], only collisions resulting from the motion are reported, which is generally the desired behaviour.
var recovery_as_collision: bool;




