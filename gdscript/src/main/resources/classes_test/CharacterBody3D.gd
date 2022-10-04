#brief Specialized 3D physics body node for characters moved by script.
#desc Character bodies are special types of bodies that are meant to be user-controlled. They are not affected by physics at all; to other types of bodies, such as a rigid body, these are the same as a [AnimatableBody3D]. However, they have two main uses:
#desc [b]Kinematic characters:[/b] Character bodies have an API for moving objects with walls and slopes detection ([method move_and_slide] method), in addition to collision detection (also done with [method PhysicsBody3D.move_and_collide]). This makes them really useful to implement characters that move in specific ways and collide with the world, but don't require advanced physics.
#desc [b]Kinematic motion:[/b] Character bodies can also be used for kinematic motion (same functionality as [AnimatableBody3D]), which allows them to be moved by code and push other bodies on their path.
class_name CharacterBody3D

#desc Apply when notions of walls, ceiling and floor are relevant. In this mode the body motion will react to slopes (acceleration/slowdown). This mode is suitable for grounded games like platformers.
const MOTION_MODE_GROUNDED = 0;

#desc Apply when there is no notion of floor or ceiling. All collisions will be reported as [code]on_wall[/code]. In this mode, when you slide, the speed will always be constant. This mode is suitable for games without ground like space games.
const MOTION_MODE_FLOATING = 1;

#desc Add the last platform velocity to the [member velocity] when you leave a moving platform.
const PLATFORM_ON_LEAVE_ADD_VELOCITY = 0;

#desc Add the last platform velocity to the [member velocity] when you leave a moving platform, but any downward motion is ignored. It's useful to keep full jump height even when the platform is moving down.
const PLATFORM_ON_LEAVE_ADD_UPWARD_VELOCITY = 1;

#desc Do nothing when leaving a platform.
const PLATFORM_ON_LEAVE_DO_NOTHING = 2;


#desc If [code]true[/code], the body will be able to move on the floor only. This option avoids to be able to walk on walls, it will however allow to slide down along them.
var floor_block_on_wall: bool;

#desc If [code]false[/code] (by default), the body will move faster on downward slopes and slower on upward slopes.
#desc If [code]true[/code], the body will always move at the same speed on the ground no matter the slope. Note that you need to use [member floor_snap_length] to stick along a downward slope at constant speed.
var floor_constant_speed: bool;

#desc Maximum angle (in radians) where a slope is still considered a floor (or a ceiling), rather than a wall, when calling [method move_and_slide]. The default value equals 45 degrees.
var floor_max_angle: float;

#desc Sets a snapping distance. When set to a value different from [code]0.0[/code], the body is kept attached to slopes when calling [method move_and_slide]. The snapping vector is determined by the given distance along the opposite direction of the [member up_direction].
#desc As long as the snapping vector is in contact with the ground and the body moves against `up_direction`, the body will remain attached to the surface. Snapping is not applied if the body moves along `up_direction`, so it will be able to detach from the ground when jumping.
var floor_snap_length: float;

#desc If [code]true[/code], the body will not slide on slopes when calling [method move_and_slide] when the body is standing still.
#desc If [code]false[/code], the body will slide on floor's slopes when [member velocity] applies a downward force.
var floor_stop_on_slope: bool;

#desc Maximum number of times the body can change direction before it stops when calling [method move_and_slide].
var max_slides: int;

#desc Sets the motion mode which defines the behavior of [method move_and_slide]. See [enum MotionMode] constants for available modes.
var motion_mode: int;

#desc Collision layers that will be included for detecting floor bodies that will act as moving platforms to be followed by the [CharacterBody3D]. By default, all floor bodies are detected and propagate their velocity.
var platform_floor_layers: int;

#desc Sets the behavior to apply when you leave a moving platform. By default, to be physically accurate, when you leave the last platform velocity is applied. See [enum PlatformOnLeave] constants for available behavior.
var platform_on_leave: int;

#desc Collision layers that will be included for detecting wall bodies that will act as moving platforms to be followed by the [CharacterBody3D]. By default, all wall bodies are ignored.
var platform_wall_layers: int;

#desc Extra margin used for collision recovery when calling [method move_and_slide].
#desc If the body is at least this close to another body, it will consider them to be colliding and will be pushed away before performing the actual motion.
#desc A higher value means it's more flexible for detecting collision, which helps with consistently detecting walls and floors.
#desc A lower value forces the collision algorithm to use more exact detection, so it can be used in cases that specifically require precision, e.g at very low scale to avoid visible jittering, or for stability with a stack of character bodies.
var safe_margin: float;

#desc If [code]true[/code], during a jump against the ceiling, the body will slide, if [code]false[/code] it will be stopped and will fall vertically.
var slide_on_ceiling: bool;

#desc Vector pointing upwards, used to determine what is a wall and what is a floor (or a ceiling) when calling [method move_and_slide]. Defaults to [code]Vector3.UP[/code]. As the vector will be normalized it can't be equal to [constant Vector3.ZERO], if you want all collisions to be reported as walls, consider using [constant MOTION_MODE_FLOATING] as [member motion_mode].
var up_direction: Vector3;

#desc Current velocity vector (typically meters per second), used and modified during calls to [method move_and_slide].
var velocity: Vector3;

#desc Minimum angle (in radians) where the body is allowed to slide when it encounters a slope. The default value equals 15 degrees. When [member motion_mode] is [constant MOTION_MODE_GROUNDED], it only affects movement if [member floor_block_on_wall] is [code]true[/code].
var wall_min_slide_angle: float;



#desc Returns the floor's collision angle at the last collision point according to [param up_direction], which is [code]Vector3.UP[/code] by default. This value is always positive and only valid after calling [method move_and_slide] and when [method is_on_floor] returns [code]true[/code].
func get_floor_angle() -> float:
	pass;

#desc Returns the surface normal of the floor at the last collision point. Only valid after calling [method move_and_slide] and when [method is_on_floor] returns [code]true[/code].
func get_floor_normal() -> Vector3:
	pass;

#desc Returns the last motion applied to the [CharacterBody3D] during the last call to [method move_and_slide]. The movement can be split into multiple motions when sliding occurs, and this method return the last one, which is useful to retrieve the current direction of the movement.
func get_last_motion() -> Vector3:
	pass;

#desc Returns a [KinematicCollision3D], which contains information about the latest collision that occurred during the last call to [method move_and_slide].
func get_last_slide_collision() -> KinematicCollision3D:
	pass;

#desc Returns the linear velocity of the floor at the last collision point. Only valid after calling [method move_and_slide] and when [method is_on_floor] returns [code]true[/code].
func get_platform_velocity() -> Vector3:
	pass;

#desc Returns the travel (position delta) that occurred during the last call to [method move_and_slide].
func get_position_delta() -> Vector3:
	pass;

#desc Returns the current real velocity since the last call to [method move_and_slide]. For example, when you climb a slope, you will move diagonally even though the velocity is horizontal. This method returns the diagonal movement, as opposed to [member velocity] which returns the requested velocity.
func get_real_velocity() -> Vector3:
	pass;

#desc Returns a [KinematicCollision3D], which contains information about a collision that occurred during the last call to [method move_and_slide]. Since the body can collide several times in a single call to [method move_and_slide], you must specify the index of the collision in the range 0 to ([method get_slide_collision_count] - 1).
func get_slide_collision() -> KinematicCollision3D:
	pass;

#desc Returns the number of times the body collided and changed direction during the last call to [method move_and_slide].
func get_slide_collision_count() -> int:
	pass;

#desc Returns the surface normal of the wall at the last collision point. Only valid after calling [method move_and_slide] and when [method is_on_wall] returns [code]true[/code].
func get_wall_normal() -> Vector3:
	pass;

#desc Returns [code]true[/code] if the body collided with the ceiling on the last call of [method move_and_slide]. Otherwise, returns [code]false[/code]. The [member up_direction] and [member floor_max_angle] are used to determine whether a surface is "ceiling" or not.
func is_on_ceiling() -> bool:
	pass;

#desc Returns [code]true[/code] if the body collided only with the ceiling on the last call of [method move_and_slide]. Otherwise, returns [code]false[/code]. The [member up_direction] and [member floor_max_angle] are used to determine whether a surface is "ceiling" or not.
func is_on_ceiling_only() -> bool:
	pass;

#desc Returns [code]true[/code] if the body collided with the floor on the last call of [method move_and_slide]. Otherwise, returns [code]false[/code]. The [member up_direction] and [member floor_max_angle] are used to determine whether a surface is "floor" or not.
func is_on_floor() -> bool:
	pass;

#desc Returns [code]true[/code] if the body collided only with the floor on the last call of [method move_and_slide]. Otherwise, returns [code]false[/code]. The [member up_direction] and [member floor_max_angle] are used to determine whether a surface is "floor" or not.
func is_on_floor_only() -> bool:
	pass;

#desc Returns [code]true[/code] if the body collided with a wall on the last call of [method move_and_slide]. Otherwise, returns [code]false[/code]. The [member up_direction] and [member floor_max_angle] are used to determine whether a surface is "wall" or not.
func is_on_wall() -> bool:
	pass;

#desc Returns [code]true[/code] if the body collided only with a wall on the last call of [method move_and_slide]. Otherwise, returns [code]false[/code]. The [member up_direction] and [member floor_max_angle] are used to determine whether a surface is "wall" or not.
func is_on_wall_only() -> bool:
	pass;

#desc Moves the body based on [member velocity]. If the body collides with another, it will slide along the other body rather than stop immediately. If the other body is a [CharacterBody3D] or [RigidBody3D], it will also be affected by the motion of the other body. You can use this to make moving and rotating platforms, or to make nodes push other nodes.
#desc Modifies [member velocity] if a slide collision occurred. To get the latest collision call [method get_last_slide_collision], for more detailed information about collisions that occurred, use [method get_slide_collision].
#desc When the body touches a moving platform, the platform's velocity is automatically added to the body motion. If a collision occurs due to the platform's motion, it will always be first in the slide collisions.
#desc Returns [code]true[/code] if the body collided, otherwise, returns [code]false[/code].
func move_and_slide() -> bool:
	pass;


