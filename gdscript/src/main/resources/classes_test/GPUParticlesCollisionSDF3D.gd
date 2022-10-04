extends GPUParticlesCollision3D
#brief Baked signed distance field 3D particle attractor affecting [GPUParticles3D] nodes.
#desc Baked signed distance field 3D particle attractor affecting [GPUParticles3D] nodes.
#desc Signed distance fields (SDF) allow for efficiently representing approximate collision shapes for convex and concave objects of any shape. This is more flexible than [GPUParticlesCollisionHeightField3D], but it requires a baking step.
#desc [b]Baking:[/b] The signed distance field texture can be baked by selecting the [GPUParticlesCollisionSDF3D] node in the editor, then clicking [b]Bake SDF[/b] at the top of the 3D viewport. Any [i]visible[/i] [MeshInstance3D]s touching the [member extents] will be taken into account for baking, regardless of their [member GeometryInstance3D.gi_mode].
#desc [b]Note:[/b] Baking a [GPUParticlesCollisionSDF3D]'s [member texture] is only possible within the editor, as there is no bake method exposed for use in exported projects. However, it's still possible to load pre-baked [Texture3D]s into its [member texture] property in an exported project.
#desc [b]Note:[/b] [member ParticleProcessMaterial.collision_mode] must be [constant ParticleProcessMaterial.COLLISION_RIGID] or [constant ParticleProcessMaterial.COLLISION_HIDE_ON_CONTACT] on the [GPUParticles3D]'s process material for collision to work.
#desc [b]Note:[/b] Particle collision only affects [GPUParticles3D], not [CPUParticles3D].
class_name GPUParticlesCollisionSDF3D

#desc Bake a 16×16×16 signed distance field. This is the fastest option, but also the least precise.
const RESOLUTION_16 = 0;

#desc Bake a 32×32×32 signed distance field.
const RESOLUTION_32 = 1;

#desc Bake a 64×64×64 signed distance field.
const RESOLUTION_64 = 2;

#desc Bake a 128×128×128 signed distance field.
const RESOLUTION_128 = 3;

#desc Bake a 256×256×256 signed distance field.
const RESOLUTION_256 = 4;

#desc Bake a 512×512×512 signed distance field. This is the slowest option, but also the most precise.
const RESOLUTION_512 = 5;

#desc Represents the size of the [enum Resolution] enum.
const RESOLUTION_MAX = 6;


#desc The visual layers to account for when baking the particle collision SDF. Only [MeshInstance3D]s whose [member VisualInstance3D.layers] match with this [member bake_mask] will be included in the generated particle collision SDF. By default, all objects are taken into account for the particle collision SDF baking.
var bake_mask: int;

#desc The collision SDF's extents in 3D units. To improve SDF quality, the [member extents] should be set as small as possible while covering the parts of the scene you need.
var extents: Vector3;

#desc The bake resolution to use for the signed distance field [member texture]. The texture must be baked again for changes to the [member resolution] property to be effective. Higher resolutions have a greater performance cost and take more time to bake. Higher resolutions also result in larger baked textures, leading to increased VRAM and storage space requirements. To improve performance and reduce bake times, use the lowest resolution possible for the object you're representing the collision of.
var resolution: int;

#desc The 3D texture representing the signed distance field.
var texture: Texture3D;

#desc The collision shape's thickness. Unlike other particle colliders, [GPUParticlesCollisionSDF3D] is actually hollow on the inside. [member thickness] can be increased to prevent particles from tunneling through the collision shape at high speeds, or when the [GPUParticlesCollisionSDF3D] is moved.
var thickness: float;



#desc Returns whether or not the specified layer of the [member bake_mask] is enabled, given a [param layer_number] between 1 and 32.
func get_bake_mask_value(layer_number: int) -> bool:
	pass;

#desc Based on [param value], enables or disables the specified layer in the [member bake_mask], given a [param layer_number] between 1 and 32.
func set_bake_mask_value(layer_number: int, value: bool) -> void:
	pass;


