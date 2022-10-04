#brief 2D particle emitter.
#desc 2D particle node used to create a variety of particle systems and effects. [GPUParticles2D] features an emitter that generates some number of particles at a given rate.
#desc Use the [code]process_material[/code] property to add a [ParticleProcessMaterial] to configure particle appearance and behavior. Alternatively, you can add a [ShaderMaterial] which will be applied to all particles.
class_name GPUParticles2D

#desc Particles are drawn in the order emitted.
const DRAW_ORDER_INDEX = 0;

#desc Particles are drawn in order of remaining lifetime.
const DRAW_ORDER_LIFETIME = 1;

const DRAW_ORDER_REVERSE_LIFETIME = 2;

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


#desc Number of particles emitted in one emission cycle.
var amount: int;

var collision_base_size: float;

#desc Particle draw order. Uses [enum DrawOrder] values.
var draw_order: int;

#desc If [code]true[/code], particles are being emitted.
var emitting: bool;

#desc How rapidly particles in an emission cycle are emitted. If greater than [code]0[/code], there will be a gap in emissions before the next cycle begins.
var explosiveness: float;

#desc The particle system's frame rate is fixed to a value. For instance, changing the value to 2 will make the particles render at 2 frames per second. Note this does not slow down the simulation of the particle system itself.
var fixed_fps: int;

#desc If [code]true[/code], results in fractional delta calculation which has a smoother particles display effect.
var fract_delta: bool;

#desc Enables particle interpolation, which makes the particle movement smoother when their [member fixed_fps] is lower than the screen refresh rate.
var interpolate: bool;

#desc Amount of time each particle will exist.
var lifetime: float;

#desc If [code]true[/code], particles use the parent node's coordinate space (known as local coordinates). This will cause particles to move and rotate along the [GPUParticles2D] node (and its parents) when it is moved or rotated. If [code]false[/code], particles use global coordinates; they will not move or rotate along the [GPUParticles2D] node (and its parents) when it is moved or rotated.
var local_coords: bool;

#desc If [code]true[/code], only one emission cycle occurs. If set [code]true[/code] during a cycle, emission will stop at the cycle's end.
var one_shot: bool;

#desc Particle system starts as if it had already run for this many seconds.
var preprocess: float;

#desc [Material] for processing particles. Can be a [ParticleProcessMaterial] or a [ShaderMaterial].
var process_material: Material;

#desc Emission lifetime randomness ratio.
var randomness: float;

#desc Particle system's running speed scaling ratio. A value of [code]0[/code] can be used to pause the particles.
var speed_scale: float;

#desc The [NodePath] to the [GPUParticles2D] used for sub-emissions.
var sub_emitter: NodePath;

#desc Particle texture. If [code]null[/code], particles will be squares.
var texture: Texture2D;

var trail_enabled: bool;

var trail_length_secs: float;

var trail_section_subdivisions: int;

var trail_sections: int;

#desc The [Rect2] that determines the node's region which needs to be visible on screen for the particle system to be active.
#desc Grow the rect if particles suddenly appear/disappear when the node enters/exits the screen. The [Rect2] can be grown via code or with the [b]Particles → Generate Visibility Rect[/b] editor tool.
var visibility_rect: Rect2;



#desc Returns a rectangle containing the positions of all existing particles.
func capture_rect() -> Rect2:
	pass;

#desc Emits a single particle. Whether [param xform], [param velocity], [param color] and [param custom] are applied depends on the value of [param flags]. See [enum EmitFlags].
func emit_particle(xform: Transform2D, velocity: Vector2, color: Color, custom: Color, flags: int) -> void:
	pass;

#desc Restarts all the existing particles.
func restart() -> void:
	pass;


