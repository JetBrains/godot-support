extends GPUParticlesCollision3D
#brief Sphere-shaped 3D particle collision shape affecting [GPUParticles3D] nodes.
#desc Sphere-shaped 3D particle collision shape affecting [GPUParticles3D] nodes.
#desc [b]Note:[/b] [member ParticleProcessMaterial.collision_mode] must be [constant ParticleProcessMaterial.COLLISION_RIGID] or [constant ParticleProcessMaterial.COLLISION_HIDE_ON_CONTACT] on the [GPUParticles3D]'s process material for collision to work.
#desc [b]Note:[/b] Particle collision only affects [GPUParticles3D], not [CPUParticles3D].
class_name GPUParticlesCollisionSphere3D


#desc The collision sphere's radius in 3D units.
var radius: float;




