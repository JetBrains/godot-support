#brief CPU-based 2D particle emitter.
#desc CPU-based 2D particle node used to create a variety of particle systems and effects.
#desc See also [GPUParticles2D], which provides the same functionality with hardware acceleration, but may not run on older devices.
class_name CPUParticles2D

#desc Particles are drawn in the order emitted.
const DRAW_ORDER_INDEX = 0;

#desc Particles are drawn in order of remaining lifetime.
const DRAW_ORDER_LIFETIME = 1;

#desc Use with [method set_param_min], [method set_param_max], and [method set_param_curve] to set initial velocity properties.
const PARAM_INITIAL_LINEAR_VELOCITY = 0;

#desc Use with [method set_param_min], [method set_param_max], and [method set_param_curve] to set angular velocity properties.
const PARAM_ANGULAR_VELOCITY = 1;

#desc Use with [method set_param_min], [method set_param_max], and [method set_param_curve] to set orbital velocity properties.
const PARAM_ORBIT_VELOCITY = 2;

#desc Use with [method set_param_min], [method set_param_max], and [method set_param_curve] to set linear acceleration properties.
const PARAM_LINEAR_ACCEL = 3;

#desc Use with [method set_param_min], [method set_param_max], and [method set_param_curve] to set radial acceleration properties.
const PARAM_RADIAL_ACCEL = 4;

#desc Use with [method set_param_min], [method set_param_max], and [method set_param_curve] to set tangential acceleration properties.
const PARAM_TANGENTIAL_ACCEL = 5;

#desc Use with [method set_param_min], [method set_param_max], and [method set_param_curve] to set damping properties.
const PARAM_DAMPING = 6;

#desc Use with [method set_param_min], [method set_param_max], and [method set_param_curve] to set angle properties.
const PARAM_ANGLE = 7;

#desc Use with [method set_param_min], [method set_param_max], and [method set_param_curve] to set scale properties.
const PARAM_SCALE = 8;

#desc Use with [method set_param_min], [method set_param_max], and [method set_param_curve] to set hue variation properties.
const PARAM_HUE_VARIATION = 9;

#desc Use with [method set_param_min], [method set_param_max], and [method set_param_curve] to set animation speed properties.
const PARAM_ANIM_SPEED = 10;

#desc Use with [method set_param_min], [method set_param_max], and [method set_param_curve] to set animation offset properties.
const PARAM_ANIM_OFFSET = 11;

#desc Represents the size of the [enum Parameter] enum.
const PARAM_MAX = 12;

#desc Use with [method set_particle_flag] to set [member particle_flag_align_y].
const PARTICLE_FLAG_ALIGN_Y_TO_VELOCITY = 0;

#desc Present for consistency with 3D particle nodes, not used in 2D.
const PARTICLE_FLAG_ROTATE_Y = 1;

#desc Present for consistency with 3D particle nodes, not used in 2D.
const PARTICLE_FLAG_DISABLE_Z = 2;

#desc Represents the size of the [enum ParticleFlags] enum.
const PARTICLE_FLAG_MAX = 3;

#desc All particles will be emitted from a single point.
const EMISSION_SHAPE_POINT = 0;

#desc Particles will be emitted in the volume of a sphere flattened to two dimensions.
const EMISSION_SHAPE_SPHERE = 1;

#desc Particles will be emitted on the surface of a sphere flattened to two dimensions.
const EMISSION_SHAPE_SPHERE_SURFACE = 2;

#desc Particles will be emitted in the area of a rectangle.
const EMISSION_SHAPE_RECTANGLE = 3;

#desc Particles will be emitted at a position chosen randomly among [member emission_points]. Particle color will be modulated by [member emission_colors].
const EMISSION_SHAPE_POINTS = 4;

#desc Particles will be emitted at a position chosen randomly among [member emission_points]. Particle velocity and rotation will be set based on [member emission_normals]. Particle color will be modulated by [member emission_colors].
const EMISSION_SHAPE_DIRECTED_POINTS = 5;

#desc Represents the size of the [enum EmissionShape] enum.
const EMISSION_SHAPE_MAX = 6;


#desc Number of particles emitted in one emission cycle.
var amount: int;

#desc Each particle's rotation will be animated along this [Curve].
var angle_curve: Curve;

var angle_max: float;

var angle_min: float;

#desc Each particle's angular velocity will vary along this [Curve].
var angular_velocity_curve: Curve;

var angular_velocity_max: float;

var angular_velocity_min: float;

#desc Each particle's animation offset will vary along this [Curve].
var anim_offset_curve: Curve;

var anim_offset_max: float;

var anim_offset_min: float;

#desc Each particle's animation speed will vary along this [Curve].
var anim_speed_curve: Curve;

var anim_speed_max: float;

var anim_speed_min: float;

#desc Each particle's initial color. If [member texture] is defined, it will be multiplied by this color.
var color: Color;

#desc Each particle's initial color will vary along this [GradientTexture1D] (multiplied with [member color]).
var color_initial_ramp: Gradient;

#desc Each particle's color will vary along this [Gradient] (multiplied with [member color]).
var color_ramp: Gradient;

#desc Damping will vary along this [Curve].
var damping_curve: Curve;

var damping_max: float;

var damping_min: float;

#desc Unit vector specifying the particles' emission direction.
var direction: Vector2;

#desc Particle draw order. Uses [enum DrawOrder] values.
var draw_order: int;

#desc Sets the [Color]s to modulate particles by when using [constant EMISSION_SHAPE_POINTS] or [constant EMISSION_SHAPE_DIRECTED_POINTS].
var emission_colors: PackedColorArray;

#desc Sets the direction the particles will be emitted in when using [constant EMISSION_SHAPE_DIRECTED_POINTS].
var emission_normals: PackedVector2Array;

#desc Sets the initial positions to spawn particles when using [constant EMISSION_SHAPE_POINTS] or [constant EMISSION_SHAPE_DIRECTED_POINTS].
var emission_points: PackedVector2Array;

#desc The rectangle's extents if [member emission_shape] is set to [constant EMISSION_SHAPE_RECTANGLE].
var emission_rect_extents: Vector2;

#desc Particles will be emitted inside this region. See [enum EmissionShape] for possible values.
var emission_shape: int;

#desc The sphere's radius if [member emission_shape] is set to [constant EMISSION_SHAPE_SPHERE].
var emission_sphere_radius: float;

#desc If [code]true[/code], particles are being emitted.
var emitting: bool;

#desc How rapidly particles in an emission cycle are emitted. If greater than [code]0[/code], there will be a gap in emissions before the next cycle begins.
var explosiveness: float;

#desc The particle system's frame rate is fixed to a value. For instance, changing the value to 2 will make the particles render at 2 frames per second. Note this does not slow down the simulation of the particle system itself.
var fixed_fps: int;

#desc If [code]true[/code], results in fractional delta calculation which has a smoother particles display effect.
var fract_delta: bool;

#desc Gravity applied to every particle.
var gravity: Vector2;

#desc Each particle's hue will vary along this [Curve].
var hue_variation_curve: Curve;

var hue_variation_max: float;

var hue_variation_min: float;

var initial_velocity_max: float;

var initial_velocity_min: float;

#desc Amount of time each particle will exist.
var lifetime: float;

#desc Particle lifetime randomness ratio.
var lifetime_randomness: float;

#desc Each particle's linear acceleration will vary along this [Curve].
var linear_accel_curve: Curve;

var linear_accel_max: float;

var linear_accel_min: float;

#desc If [code]true[/code], particles use the parent node's coordinate space (known as local coordinates). This will cause particles to move and rotate along the [CPUParticles2D] node (and its parents) when it is moved or rotated. If [code]false[/code], particles use global coordinates; they will not move or rotate along the [CPUParticles2D] node (and its parents) when it is moved or rotated.
var local_coords: bool;

#desc If [code]true[/code], only one emission cycle occurs. If set [code]true[/code] during a cycle, emission will stop at the cycle's end.
var one_shot: bool;

#desc Each particle's orbital velocity will vary along this [Curve].
var orbit_velocity_curve: Curve;

var orbit_velocity_max: float;

var orbit_velocity_min: float;

#desc Align Y axis of particle with the direction of its velocity.
var particle_flag_align_y: bool;

#desc Particle system starts as if it had already run for this many seconds.
var preprocess: float;

#desc Each particle's radial acceleration will vary along this [Curve].
var radial_accel_curve: Curve;

var radial_accel_max: float;

var radial_accel_min: float;

#desc Emission lifetime randomness ratio.
var randomness: float;

#desc Each particle's scale will vary along this [Curve].
var scale_amount_curve: Curve;

var scale_amount_max: float;

var scale_amount_min: float;

var scale_curve_x: Curve;

var scale_curve_y: Curve;

#desc Particle system's running speed scaling ratio. A value of [code]0[/code] can be used to pause the particles.
var speed_scale: float;

var split_scale: bool;

#desc Each particle's initial direction range from [code]+spread[/code] to [code]-spread[/code] degrees.
var spread: float;

#desc Each particle's tangential acceleration will vary along this [Curve].
var tangential_accel_curve: Curve;

var tangential_accel_max: float;

var tangential_accel_min: float;

#desc Particle texture. If [code]null[/code], particles will be squares.
var texture: Texture2D;



#desc Sets this node's properties to match a given [GPUParticles2D] node with an assigned [ParticleProcessMaterial].
func convert_from_particles() -> void:
	pass;

#desc Returns the [Curve] of the parameter specified by [enum Parameter].
func get_param_curve() -> Curve:
	pass;

func get_param_max() -> float:
	pass;

func get_param_min() -> float:
	pass;

#desc Returns the enabled state of the given flag (see [enum ParticleFlags] for options).
func get_particle_flag() -> bool:
	pass;

#desc Restarts the particle emitter.
func restart() -> void:
	pass;

#desc Sets the [Curve] of the parameter specified by [enum Parameter].
func set_param_curve(param: int, curve: Curve) -> void:
	pass;

func set_param_max(param: int, value: float) -> void:
	pass;

func set_param_min(param: int, value: float) -> void:
	pass;

#desc Enables or disables the given flag (see [enum ParticleFlags] for options).
func set_particle_flag(particle_flag: int, enable: bool) -> void:
	pass;


