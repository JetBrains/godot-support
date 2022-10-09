extends VisualInstance3D
#brief Abstract class for 3D particle collision shapes affecting [GPUParticles3D] nodes.
#desc Particle collision shapes can be used to make particles stop or bounce against them.
#desc Particle collision shapes in real-time and can be moved, rotated and scaled during gameplay. Unlike attractors, non-uniform scaling of collision shapes is [i]not[/i] supported.
#desc Particle collision shapes can be temporarily disabled by hiding them.
#desc [b]Note:[/b] [member ParticleProcessMaterial.collision_mode] must be [constant ParticleProcessMaterial.COLLISION_RIGID] or [constant ParticleProcessMaterial.COLLISION_HIDE_ON_CONTACT] on the [GPUParticles3D]'s process material for collision to work.
#desc [b]Note:[/b] Particle collision only affects [GPUParticles3D], not [CPUParticles3D].
#desc [b]Note:[/b] Particles pushed by a collider that is being moved will not be interpolated, which can result in visible stuttering. This can be alleviated by setting [member GPUParticles3D.fixed_fps] to [code]0[/code] or a value that matches or exceeds the target framerate.
class_name GPUParticlesCollision3D


#desc The particle rendering layers ([member VisualInstance3D.layers]) that will be affected by the collision shape. By default, all particles that have [member ParticleProcessMaterial.collision_mode] set to [constant ParticleProcessMaterial.COLLISION_RIGID] or [constant ParticleProcessMaterial.COLLISION_HIDE_ON_CONTACT] will be affected by a collision shape.
#desc After configuring particle nodes accordingly, specific layers can be unchecked to prevent certain particles from being affected by attractors. For example, this can be used if you're using an attractor as part of a spell effect but don't want the attractor to affect unrelated weather particles at the same position.
#desc Particle attraction can also be disabled on a per-process material basis by setting [member ParticleProcessMaterial.attractor_interaction_enabled] on the [GPUParticles3D] node.
var cull_mask: int;




