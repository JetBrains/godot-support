extends Material
#brief A material for [CanvasItem]s.
#desc [CanvasItemMaterial]s provide a means of modifying the textures associated with a CanvasItem. They specialize in describing blend and lighting behaviors for textures. Use a [ShaderMaterial] to more fully customize a material's interactions with a [CanvasItem].
class_name CanvasItemMaterial

#desc Mix blending mode. Colors are assumed to be independent of the alpha (opacity) value.
const BLEND_MODE_MIX = 0;

#desc Additive blending mode.
const BLEND_MODE_ADD = 1;

#desc Subtractive blending mode.
const BLEND_MODE_SUB = 2;

#desc Multiplicative blending mode.
const BLEND_MODE_MUL = 3;

#desc Mix blending mode. Colors are assumed to be premultiplied by the alpha (opacity) value.
const BLEND_MODE_PREMULT_ALPHA = 4;

#desc Render the material using both light and non-light sensitive material properties.
const LIGHT_MODE_NORMAL = 0;

#desc Render the material as if there were no light.
const LIGHT_MODE_UNSHADED = 1;

#desc Render the material as if there were only light.
const LIGHT_MODE_LIGHT_ONLY = 2;


#desc The manner in which a material's rendering is applied to underlying textures.
var blend_mode: int;

#desc The manner in which material reacts to lighting.
var light_mode: int;

#desc The number of columns in the spritesheet assigned as [Texture2D] for a [GPUParticles2D] or [CPUParticles2D].
#desc [b]Note:[/b] This property is only used and visible in the editor if [member particles_animation] is [code]true[/code].
var particles_anim_h_frames: int;

#desc If [code]true[/code], the particles animation will loop.
#desc [b]Note:[/b] This property is only used and visible in the editor if [member particles_animation] is [code]true[/code].
var particles_anim_loop: bool;

#desc The number of rows in the spritesheet assigned as [Texture2D] for a [GPUParticles2D] or [CPUParticles2D].
#desc [b]Note:[/b] This property is only used and visible in the editor if [member particles_animation] is [code]true[/code].
var particles_anim_v_frames: int;

#desc If [code]true[/code], enable spritesheet-based animation features when assigned to [GPUParticles2D] and [CPUParticles2D] nodes. The [member ParticleProcessMaterial.anim_speed_max] or [member CPUParticles2D.anim_speed_max] should also be set to a positive value for the animation to play.
#desc This property (and other [code]particles_anim_*[/code] properties that depend on it) has no effect on other types of nodes.
var particles_animation: bool;




