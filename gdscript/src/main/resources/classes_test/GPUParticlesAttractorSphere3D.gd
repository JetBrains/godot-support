extends GPUParticlesAttractor3D
#brief Ellipse-shaped 3D particle attractor affecting [GPUParticles3D] nodes.
#desc Ellipse-shaped 3D particle attractor affecting [GPUParticles3D] nodes.
#desc [b]Note:[/b] Particle attractors only affect [GPUParticles3D], not [CPUParticles3D].
class_name GPUParticlesAttractorSphere3D


#desc The attractor sphere's radius in 3D units.
#desc [b]Note:[/b] Stretched ellipses can be obtained by using non-uniform scaling on the [GPUParticlesAttractorSphere3D] node.
var radius: float;




