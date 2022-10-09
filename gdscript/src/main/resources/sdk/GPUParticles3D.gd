extends GeometryInstance3D
#brief 3D particle emitter.
#desc 3D particle node used to create a variety of particle systems and effects. [GPUParticles3D] features an emitter that generates some number of particles at a given rate.
#desc Use the [code]process_material[/code] property to add a [ParticleProcessMaterial] to configure particle appearance and behavior. Alternatively, you can add a [ShaderMaterial] which will be applied to all particles.
class_name GPUParticles3D

#desc Particles are drawn in the order emitted.
const DRAW_ORDER_INDEX = 0;

#desc Particles are drawn in order of remaining lifetime.
const DRAW_ORDER_LIFETIME = 1;

const DRAW_ORDER_REVERSE_LIFETIME = 2;

#desc Particles are drawn in order of depth.
const DRAW_ORDER_VIEW_DEPTH = 3;

#desc Particle starts at the specified position.
const EMIT_FLAG_POSITION = 1;

#desc Particle starts with specified rotation and scale.
const EMIT_FLAG_ROTATION_SCALE = 2;

#desc Particle starts with the specified velocity vector, which defines the emission direction and speed.
const EMIT_FLAG_VELOCITY = 4;

#desc Particle starts with specified color.
const EMIT_FLAG_COLOR = 8;

#desc Particle starts with specified [code]CUSTOM[/code] data.
const EMIT_FLAG_CUSTOM = 16;

#desc Maximum number of draw passes supported.
const MAX_DRAW_PASSES = 4;

const TRANSFORM_ALIGN_DISABLED = 0;

const TRANSFORM_ALIGN_Z_BILLBOARD = 1;

const TRANSFORM_ALIGN_Y_TO_VELOCITY = 2;

const TRANSFORM_ALIGN_Z_BILLBOARD_Y_TO_VELOCITY = 3;


#desc Number of particles to emit.
var amount: int;

var collision_base_size: float;

#desc Particle draw order. Uses [enum DrawOrder] values.
var draw_order: int;

#desc [Mesh] that is drawn for the first draw pass.
var draw_pass_1: Mesh;

#desc [Mesh] that is drawn for the second draw pass.
var draw_pass_2: Mesh;

#desc [Mesh] that is drawn for the third draw pass.
var draw_pass_3: Mesh;

#desc [Mesh] that is drawn for the fourth draw pass.
var draw_pass_4: Mesh;

#desc The number of draw passes when rendering particles.
var draw_passes: int;

var draw_skin: Skin;

#desc If [code]true[/code], particles are being emitted.
var emitting: bool;

#desc Time ratio between each emission. If [code]0[/code], particles are emitted continuously. If [code]1[/code], all particles are emitted simultaneously.
var explosiveness: float;

#desc The particle system's frame rate is fixed to a value. For instance, changing the value to 2 will make the particles render at 2 frames per second. Note this does not slow down the simulation of the particle system itself.
var fixed_fps: int;

#desc If [code]true[/code], results in fractional delta calculation which has a smoother particles display effect.
var fract_delta: bool;

#desc Enables particle interpolation, which makes the particle movement smoother when their [member fixed_fps] is lower than the screen refresh rate.
var interpolate: bool;

#desc Amount of time each particle will exist.
var lifetime: float;

#desc If [code]true[/code], particles use the parent node's coordinate space (known as local coordinates). This will cause particles to move and rotate along the [GPUParticles3D] node (and its parents) when it is moved or rotated. If [code]false[/code], particles use global coordinates; they will not move or rotate along the [GPUParticles3D] node (and its parents) when it is moved or rotated.
var local_coords: bool;

#desc If [code]true[/code], only [code]amount[/code] particles will be emitted.
var one_shot: bool;

#desc Amount of time to preprocess the particles before animation starts. Lets you start the animation some time after particles have started emitting.
var preprocess: float;

#desc [Material] for processing particles. Can be a [ParticleProcessMaterial] or a [ShaderMaterial].
var process_material: Material;

#desc Emission randomness ratio.
var randomness: float;

#desc Speed scaling ratio. A value of [code]0[/code] can be used to pause the particles.
var speed_scale: float;

var sub_emitter: NodePath;

var trail_enabled: bool;

var trail_length_secs: float;

var transform_align: int;

#desc The [AABB] that determines the node's region which needs to be visible on screen for the particle system to be active.
#desc Grow the box if particles suddenly appear/disappear when the node enters/exits the screen. The [AABB] can be grown via code or with the [b]Particles → Generate AABB[/b] editor tool.
var visibility_aabb: AABB;



#desc Returns the axis-aligned bounding box that contains all the particles that are active in the current frame.
func capture_aabb() -> AABB:
	pass;

#desc Emits a single particle. Whether [param xform], [param velocity], [param color] and [param custom] are applied depends on the value of [param flags]. See [enum EmitFlags].
func emit_particle(xform: Transform3D, velocity: Vector3, color: Color, custom: Color, flags: int) -> void:
	pass;

#desc Returns the [Mesh] that is drawn at index [param pass].
func get_draw_pass_mesh(pass: int) -> Mesh:
	pass;

#desc Restarts the particle emission, clearing existing particles.
func restart() -> void:
	pass;

#desc Sets the [Mesh] that is drawn at index [param pass].
func set_draw_pass_mesh(pass: int, mesh: Mesh) -> void:
	pass;


