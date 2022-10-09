extends Material
#brief Particle properties for [GPUParticles3D] and [GPUParticles2D] nodes.
#desc ParticleProcessMaterial defines particle properties and behavior. It is used in the [code]process_material[/code] of [GPUParticles3D] and [GPUParticles2D] emitter nodes.
#desc Some of this material's properties are applied to each particle when emitted, while others can have a [CurveTexture] applied to vary values over the lifetime of the particle.
#desc Particle animation is available only in [GPUParticles2D]. To use it, attach a [CanvasItemMaterial], with [member CanvasItemMaterial.particles_animation] enabled, to the particles node.
class_name ParticleProcessMaterial

#desc Use with [method set_param_min], [method set_param_max], and [method set_param_texture] to set initial velocity properties.
const PARAM_INITIAL_LINEAR_VELOCITY = 0;

#desc Use with [method set_param_min], [method set_param_max], and [method set_param_texture] to set angular velocity properties.
const PARAM_ANGULAR_VELOCITY = 1;

#desc Use with [method set_param_min], [method set_param_max], and [method set_param_texture] to set orbital velocity properties.
const PARAM_ORBIT_VELOCITY = 2;

#desc Use with [method set_param_min], [method set_param_max], and [method set_param_texture] to set linear acceleration properties.
const PARAM_LINEAR_ACCEL = 3;

#desc Use with [method set_param_min], [method set_param_max], and [method set_param_texture] to set radial acceleration properties.
const PARAM_RADIAL_ACCEL = 4;

#desc Use with [method set_param_min], [method set_param_max], and [method set_param_texture] to set tangential acceleration properties.
const PARAM_TANGENTIAL_ACCEL = 5;

#desc Use with [method set_param_min], [method set_param_max], and [method set_param_texture] to set damping properties.
const PARAM_DAMPING = 6;

#desc Use with [method set_param_min], [method set_param_max], and [method set_param_texture] to set angle properties.
const PARAM_ANGLE = 7;

#desc Use with [method set_param_min], [method set_param_max], and [method set_param_texture] to set scale properties.
const PARAM_SCALE = 8;

#desc Use with [method set_param_min], [method set_param_max], and [method set_param_texture] to set hue variation properties.
const PARAM_HUE_VARIATION = 9;

#desc Use with [method set_param_min], [method set_param_max], and [method set_param_texture] to set animation speed properties.
const PARAM_ANIM_SPEED = 10;

#desc Use with [method set_param_min], [method set_param_max], and [method set_param_texture] to set animation offset properties.
const PARAM_ANIM_OFFSET = 11;

#desc Represents the size of the [enum Parameter] enum.
const PARAM_MAX = 15;

#desc Use with [method set_particle_flag] to set [member particle_flag_align_y].
const PARTICLE_FLAG_ALIGN_Y_TO_VELOCITY = 0;

#desc Use with [method set_particle_flag] to set [member particle_flag_rotate_y].
const PARTICLE_FLAG_ROTATE_Y = 1;

#desc Use with [method set_particle_flag] to set [member particle_flag_disable_z].
const PARTICLE_FLAG_DISABLE_Z = 2;

#desc Represents the size of the [enum ParticleFlags] enum.
const PARTICLE_FLAG_MAX = 3;

#desc All particles will be emitted from a single point.
const EMISSION_SHAPE_POINT = 0;

#desc Particles will be emitted in the volume of a sphere.
const EMISSION_SHAPE_SPHERE = 1;

#desc Particles will be emitted on the surface of a sphere.
const EMISSION_SHAPE_SPHERE_SURFACE = 2;

#desc Particles will be emitted in the volume of a box.
const EMISSION_SHAPE_BOX = 3;

#desc Particles will be emitted at a position determined by sampling a random point on the [member emission_point_texture]. Particle color will be modulated by [member emission_color_texture].
const EMISSION_SHAPE_POINTS = 4;

#desc Particles will be emitted at a position determined by sampling a random point on the [member emission_point_texture]. Particle velocity and rotation will be set based on [member emission_normal_texture]. Particle color will be modulated by [member emission_color_texture].
const EMISSION_SHAPE_DIRECTED_POINTS = 5;

#desc Particles will be emitted in a ring or cylinder.
const EMISSION_SHAPE_RING = 6;

#desc Represents the size of the [enum EmissionShape] enum.
const EMISSION_SHAPE_MAX = 7;

#desc Use with [method set_param_min] and [method set_param_max] to set the turbulence minimum und maximum influence on each particles velocity.
const PARAM_TURB_VEL_INFLUENCE = 13;

#desc Use with [method set_param_min] and [method set_param_max] to set the turbulence minimum and maximum displacement of the particles spawn position.
const PARAM_TURB_INIT_DISPLACEMENT = 14;

#desc Use with [method set_param_texture] to set the turbulence influence over the particles life time.
const PARAM_TURB_INFLUENCE_OVER_LIFE = 12;

const SUB_EMITTER_DISABLED = 0;

const SUB_EMITTER_CONSTANT = 1;

const SUB_EMITTER_AT_END = 2;

const SUB_EMITTER_AT_COLLISION = 3;

#desc Represents the size of the [enum SubEmitterMode] enum.
const SUB_EMITTER_MAX = 4;

#desc No collision for particles. Particles will go through [GPUParticlesCollision3D] nodes.
const COLLISION_DISABLED = 0;

#desc [RigidBody3D]-style collision for particles using [GPUParticlesCollision3D] nodes.
const COLLISION_RIGID = 1;

#desc Hide particles instantly when colliding with a [GPUParticlesCollision3D] node. This can be combined with a subemitter that uses the [constant COLLISION_RIGID] collision mode to "replace" the parent particle with the subemitter on impact.
const COLLISION_HIDE_ON_CONTACT = 2;

#desc Represents the size of the [enum CollisionMode] enum.
const COLLISION_MAX = 3;


#desc Each particle's rotation will be animated along this [CurveTexture].
var angle_curve: Texture2D;

#desc Maximum initial rotation applied to each particle, in degrees.
#desc Only applied when [member particle_flag_disable_z] or [member particle_flag_rotate_y] are [code]true[/code] or the [BaseMaterial3D] being used to draw the particle is using [constant BaseMaterial3D.BILLBOARD_PARTICLES].
var angle_max: float;

#desc Minimum equivalent of [member angle_max].
var angle_min: float;

#desc Each particle's angular velocity (rotation speed) will vary along this [CurveTexture] over its lifetime.
var angular_velocity_curve: Texture2D;

#desc Maximum initial angular velocity (rotation speed) applied to each particle in [i]degrees[/i] per second.
#desc Only applied when [member particle_flag_disable_z] or [member particle_flag_rotate_y] are [code]true[/code] or the [BaseMaterial3D] being used to draw the particle is using [constant BaseMaterial3D.BILLBOARD_PARTICLES].
var angular_velocity_max: float;

#desc Minimum equivalent of [member angular_velocity_max].
var angular_velocity_min: float;

#desc Each particle's animation offset will vary along this [CurveTexture].
var anim_offset_curve: Texture2D;

#desc Maximum animation offset that corresponds to frame index in the texture. [code]0[/code] is the first frame, [code]1[/code] is the last one. See [member CanvasItemMaterial.particles_animation].
var anim_offset_max: float;

#desc Minimum equivalent of [member anim_offset_max].
var anim_offset_min: float;

#desc Each particle's animation speed will vary along this [CurveTexture].
var anim_speed_curve: Texture2D;

#desc Maximum particle animation speed. Animation speed of [code]1[/code] means that the particles will make full [code]0[/code] to [code]1[/code] offset cycle during lifetime, [code]2[/code] means [code]2[/code] cycles etc.
#desc With animation speed greater than [code]1[/code], remember to enable [member CanvasItemMaterial.particles_anim_loop] property if you want the animation to repeat.
var anim_speed_max: float;

#desc Minimum equivalent of [member anim_speed_max].
var anim_speed_min: float;

#desc True if the interaction with particle attractors is enabled.
var attractor_interaction_enabled: bool;

#desc The particles' bounciness. Values range from [code]0[/code] (no bounce) to [code]1[/code] (full bounciness). Only effective if [member collision_mode] is [constant COLLISION_RIGID].
var collision_bounce: float;

#desc The particles' friction. Values range from [code]0[/code] (frictionless) to [code]1[/code] (maximum friction). Only effective if [member collision_mode] is [constant COLLISION_RIGID].
var collision_friction: float;

#desc The particles' collision mode.
#desc [b]Note:[/b] Particles can only collide with [GPUParticlesCollision3D] nodes, not [PhysicsBody3D] nodes. To make particles collide with various objects, you can add [GPUParticlesCollision3D] nodes as children of [PhysicsBody3D] nodes.
var collision_mode: int;

#desc Should collision take scale into account.
var collision_use_scale: bool;

#desc Each particle's initial color. If the [GPUParticles2D]'s [code]texture[/code] is defined, it will be multiplied by this color.
#desc [b]Note:[/b] [member color] multiplies the particle mesh's vertex colors. To have a visible effect on a [BaseMaterial3D], [member BaseMaterial3D.vertex_color_use_as_albedo] [i]must[/i] be [code]true[/code]. For a [ShaderMaterial], [code]ALBEDO *= COLOR.rgb;[/code] must be inserted in the shader's [code]fragment()[/code] function. Otherwise, [member color] will have no visible effect.
var color: Color;

#desc Each particle's initial color will vary along this [GradientTexture1D] (multiplied with [member color]).
#desc [b]Note:[/b] [member color_initial_ramp] multiplies the particle mesh's vertex colors. To have a visible effect on a [BaseMaterial3D], [member BaseMaterial3D.vertex_color_use_as_albedo] [i]must[/i] be [code]true[/code]. For a [ShaderMaterial], [code]ALBEDO *= COLOR.rgb;[/code] must be inserted in the shader's [code]fragment()[/code] function. Otherwise, [member color_initial_ramp] will have no visible effect.
var color_initial_ramp: Texture2D;

#desc Each particle's color will vary along this [GradientTexture1D] over its lifetime (multiplied with [member color]).
#desc [b]Note:[/b] [member color_ramp] multiplies the particle mesh's vertex colors. To have a visible effect on a [BaseMaterial3D], [member BaseMaterial3D.vertex_color_use_as_albedo] [i]must[/i] be [code]true[/code]. For a [ShaderMaterial], [code]ALBEDO *= COLOR.rgb;[/code] must be inserted in the shader's [code]fragment()[/code] function. Otherwise, [member color_ramp] will have no visible effect.
var color_ramp: Texture2D;

#desc Damping will vary along this [CurveTexture].
var damping_curve: Texture2D;

#desc The maximum rate at which particles lose velocity. For example value of [code]100[/code] means that the particle will go from [code]100[/code] velocity to [code]0[/code] in [code]1[/code] second.
var damping_max: float;

#desc Minimum equivalent of [member damping_max].
var damping_min: float;

#desc Unit vector specifying the particles' emission direction.
var direction: Vector3;

#desc The box's extents if [code]emission_shape[/code] is set to [constant EMISSION_SHAPE_BOX].
var emission_box_extents: Vector3;

#desc Particle color will be modulated by color determined by sampling this texture at the same point as the [member emission_point_texture].
#desc [b]Note:[/b] [member emission_color_texture] multiplies the particle mesh's vertex colors. To have a visible effect on a [BaseMaterial3D], [member BaseMaterial3D.vertex_color_use_as_albedo] [i]must[/i] be [code]true[/code]. For a [ShaderMaterial], [code]ALBEDO *= COLOR.rgb;[/code] must be inserted in the shader's [code]fragment()[/code] function. Otherwise, [member emission_color_texture] will have no visible effect.
var emission_color_texture: Texture2D;

#desc Particle velocity and rotation will be set by sampling this texture at the same point as the [member emission_point_texture]. Used only in [constant EMISSION_SHAPE_DIRECTED_POINTS]. Can be created automatically from mesh or node by selecting "Create Emission Points from Mesh/Node" under the "Particles" tool in the toolbar.
var emission_normal_texture: Texture2D;

#desc The number of emission points if [code]emission_shape[/code] is set to [constant EMISSION_SHAPE_POINTS] or [constant EMISSION_SHAPE_DIRECTED_POINTS].
var emission_point_count: int;

#desc Particles will be emitted at positions determined by sampling this texture at a random position. Used with [constant EMISSION_SHAPE_POINTS] and [constant EMISSION_SHAPE_DIRECTED_POINTS]. Can be created automatically from mesh or node by selecting "Create Emission Points from Mesh/Node" under the "Particles" tool in the toolbar.
var emission_point_texture: Texture2D;

#desc The axis of the ring when using the emitter [constant EMISSION_SHAPE_RING].
var emission_ring_axis: Vector3;

#desc The height of the ring when using the emitter [constant EMISSION_SHAPE_RING].
var emission_ring_height: float;

#desc The inner radius of the ring when using the emitter [constant EMISSION_SHAPE_RING].
var emission_ring_inner_radius: float;

#desc The radius of the ring when using the emitter [constant EMISSION_SHAPE_RING].
var emission_ring_radius: float;

#desc Particles will be emitted inside this region. Use [enum EmissionShape] constants for values.
var emission_shape: int;

#desc The sphere's radius if [code]emission_shape[/code] is set to [constant EMISSION_SHAPE_SPHERE].
var emission_sphere_radius: float;

#desc Amount of [member spread] along the Y axis.
var flatness: float;

#desc Gravity applied to every particle.
var gravity: Vector3;

#desc Each particle's hue will vary along this [CurveTexture].
var hue_variation_curve: Texture2D;

#desc Maximum initial hue variation applied to each particle. It will shift the particle color's hue.
var hue_variation_max: float;

#desc Minimum equivalent of [member hue_variation_max].
var hue_variation_min: float;

#desc Maximum initial velocity magnitude for each particle. Direction comes from [member direction] and [member spread].
var initial_velocity_max: float;

#desc Minimum equivalent of [member initial_velocity_max].
var initial_velocity_min: float;

#desc Particle lifetime randomness ratio. The lifetime will be multiplied by a value interpolated between [code]1.0[/code] and a random number less than one. For example a random ratio of [code]0.4[/code] would scale the original lifetime between [code]0.4-1.0[/code] of its original value.
var lifetime_randomness: float;

#desc Each particle's linear acceleration will vary along this [CurveTexture].
var linear_accel_curve: Texture2D;

#desc Maximum linear acceleration applied to each particle in the direction of motion.
var linear_accel_max: float;

#desc Minimum equivalent of [member linear_accel_min].
var linear_accel_min: float;

#desc Each particle's orbital velocity will vary along this [CurveTexture].
var orbit_velocity_curve: Texture2D;

#desc Maximum orbital velocity applied to each particle. Makes the particles circle around origin. Specified in number of full rotations around origin per second.
#desc Only available when [member particle_flag_disable_z] is [code]true[/code].
var orbit_velocity_max: float;

#desc Minimum equivalent of [member orbit_velocity_max].
var orbit_velocity_min: float;

#desc Align Y axis of particle with the direction of its velocity.
var particle_flag_align_y: bool;

#desc If [code]true[/code], particles will not move on the z axis.
var particle_flag_disable_z: bool;

#desc If [code]true[/code], particles rotate around Y axis by [member angle_min].
var particle_flag_rotate_y: bool;

#desc Each particle's radial acceleration will vary along this [CurveTexture].
var radial_accel_curve: Texture2D;

#desc Maximum radial acceleration applied to each particle. Makes particle accelerate away from the origin or towards it if negative.
var radial_accel_max: float;

#desc Minimum equivalent of [member radial_accel_max].
var radial_accel_min: float;

#desc Each particle's scale will vary along this [CurveTexture]. If a [CurveXYZTexture] is supplied instead, the scale will be separated per-axis.
var scale_curve: Texture2D;

#desc Maximum initial scale applied to each particle.
var scale_max: float;

#desc Minimum equivalent of [member scale_max].
var scale_min: float;

#desc Each particle's initial direction range from [code]+spread[/code] to [code]-spread[/code] degrees.
var spread: float;

var sub_emitter_amount_at_end: int;

var sub_emitter_frequency: float;

var sub_emitter_keep_velocity: bool;

var sub_emitter_mode: int;

#desc Each particle's tangential acceleration will vary along this [CurveTexture].
var tangential_accel_curve: Texture2D;

#desc Maximum tangential acceleration applied to each particle. Tangential acceleration is perpendicular to the particle's velocity giving the particles a swirling motion.
var tangential_accel_max: float;

#desc Minimum equivalent of [member tangential_accel_max].
var tangential_accel_min: float;

#desc Enables and disables Turbulence for the particle system.
var turbulence_enabled: bool;

#desc Minimum turbulence influence on each particle.
#desc The actual amount of turbulence influence on each particle is calculated as a random value between [member turbulence_influence_min] and [member turbulence_influence_max] and multiplied by the amount of turbulence influence from [member turbulence_influence_over_life].
var turbulence_influence_max: float;

#desc Maximum turbulence influence on each particle.
#desc The actual amount of turbulence influence on each particle is calculated as a random value between [member turbulence_influence_min] and [member turbulence_influence_max] and multiplied by the amount of turbulence influence from [member turbulence_influence_over_life].
var turbulence_influence_min: float;

#desc Each particle's amount of turbulence will be influenced along this [CurveTexture] over its life time.
var turbulence_influence_over_life: Texture2D;

#desc Maximum displacement of each particles spawn position by the turbulence.
#desc The actual amount of displacement will be a factor of the underlying turbulence multiplied by a random value between [member turbulence_initial_displacement_min] and [member turbulence_initial_displacement_max].
var turbulence_initial_displacement_max: float;

#desc Minimum displacement of each particles spawn position by the turbulence.
#desc The actual amount of displacement will be a factor of the underlying turbulence multiplied by a random value between [member turbulence_initial_displacement_min] and [member turbulence_initial_displacement_max].
var turbulence_initial_displacement_min: float;

#desc This value controls the overall scale/frequency of the turbulence noise pattern.
#desc A small scale will result in smaller features with more detail while a high scale will result in smoother noise with larger features.
var turbulence_noise_scale: float;

#desc The movement speed of the turbulence pattern. This changes how quickly the noise changes over time.
#desc A value of [code]Vector3(0.0, 0.0, 0.0)[/code] will freeze the turbulence pattern in place.
var turbulence_noise_speed: Vector3;

#desc Use to influence the noise speed in a random pattern. This helps to break up visible movement patterns.
var turbulence_noise_speed_random: float;

#desc The turbulence noise strength. Increasing this will result in a stronger, more contrasting, noise pattern.
var turbulence_noise_strength: float;



#desc Returns the maximum value range for the given parameter.
func get_param_max(param: int) -> float:
	pass;

#desc Returns the minimum value range for the given parameter.
func get_param_min(param: int) -> float:
	pass;

#desc Returns the [Texture2D] used by the specified parameter.
func get_param_texture(param: int) -> Texture2D:
	pass;

#desc Returns [code]true[/code] if the specified particle flag is enabled. See [enum ParticleFlags] for options.
func get_particle_flag(particle_flag: int) -> bool:
	pass;

#desc Sets the maximum value range for the given parameter.
func set_param_max(param: int, value: float) -> void:
	pass;

#desc Sets the minimum value range for the given parameter.
func set_param_min(param: int, value: float) -> void:
	pass;

#desc Sets the [Texture2D] for the specified [enum Parameter].
func set_param_texture(param: int, texture: Texture2D) -> void:
	pass;

#desc If [code]true[/code], enables the specified particle flag. See [enum ParticleFlags] for options.
func set_particle_flag(particle_flag: int, enable: bool) -> void:
	pass;


