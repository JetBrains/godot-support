#brief Box-shaped 3D particle attractor with strength varying within the box, affecting [GPUParticles3D] nodes.
#desc Box-shaped 3D particle attractor with strength varying within the box, affecting [GPUParticles3D] nodes.
#desc Unlike [GPUParticlesAttractorBox3D], [GPUParticlesAttractorVectorField3D] uses a [member texture] to affect attraction strength within the box. This can be used to create complex attraction scenarios where particles travel in different directions depending on their location. This can be useful for weather effects such as sandstorms.
#desc [b]Note:[/b] Particle attractors only affect [GPUParticles3D], not [CPUParticles3D].
class_name GPUParticlesAttractorVectorField3D


#desc The extents of the vector field box in 3D units.
var extents: Vector3;

#desc The 3D texture to be used. Values are linearly interpolated between the texture's pixels.
#desc [b]Note:[/b] To get better performance, the 3D texture's resolution should reflect the [member extents] of the attractor. Since particle attraction is usually low-frequency data, the texture can be kept at a low resolution such as 64×64×64.
var texture: Texture3D;




