extends VisualInstance3D
#brief Abstract class for 3D particle attractors affecting [GPUParticles3D] nodes.
#desc Particle attractors can be used to attract particles towards the attractor's origin, or to push them away from the attractor's origin.
#desc Particle attractors work in real-time and can be moved, rotated and scaled during gameplay. Unlike collision shapes, non-uniform scaling of attractors is also supported.
#desc Attractors can be temporarily disabled by hiding them, or by setting their [member strength] to [code]0.0[/code].
#desc [b]Note:[/b] Particle attractors only affect [GPUParticles3D], not [CPUParticles3D].
class_name GPUParticlesAttractor3D


#desc The particle attractor's attenuation. Higher values result in more gradual pushing of particles as they come closer to the attractor's origin. Zero or negative values will cause particles to be pushed very fast as soon as the touch the attractor's edges.
var attenuation: float;

#desc The particle rendering layers ([member VisualInstance3D.layers]) that will be affected by the attractor. By default, all particles are affected by an attractor.
#desc After configuring particle nodes accordingly, specific layers can be unchecked to prevent certain particles from being affected by attractors. For example, this can be used if you're using an attractor as part of a spell effect but don't want the attractor to affect unrelated weather particles at the same position.
#desc Particle attraction can also be disabled on a per-process material basis by setting [member ParticleProcessMaterial.attractor_interaction_enabled] on the [GPUParticles3D] node.
var cull_mask: int;

#desc Adjusts how directional the attractor is. At [code]0.0[/code], the attractor is not directional at all: it will attract particles towards its center. At [code]1.0[/code], the attractor is fully directional: particles will always be pushed towards local -Z (or +Z if [member strength] is negative).
#desc [b]Note:[/b] If [member directionality] is greater than [code]0.0[/code], the direction in which particles are pushed can be changed by rotating the [GPUParticlesAttractor3D] node.
var directionality: float;

#desc If [member strength] is negative, particles will be pushed in the reverse direction. Particles will be pushed [i]away[/i] from the attractor's  origin if [member directionality] is [code]0.0[/code], or towards local +Z if [member directionality] is greater than [code]0.0[/code].
var strength: float;




