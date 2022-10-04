extends VisualInstance3D
#brief Node that projects a texture onto a [MeshInstance3D].
#desc [Decal]s are used to project a texture onto a [Mesh] in the scene. Use Decals to add detail to a scene without affecting the underlying [Mesh]. They are often used to add weathering to building, add dirt or mud to the ground, or add variety to props. Decals can be moved at any time, making them suitable for things like blob shadows or laser sight dots.
#desc They are made of an [AABB] and a group of [Texture2D]s specifying [Color], normal, ORM (ambient occlusion, roughness, metallic), and emission. Decals are projected within their [AABB] so altering the orientation of the Decal affects the direction in which they are projected. By default, Decals are projected down (i.e. from positive Y to negative Y).
#desc The [Texture2D]s associated with the Decal are automatically stored in a texture atlas which is used for drawing the decals so all decals can be drawn at once. Godot uses clustered decals, meaning they are stored in cluster data and drawn when the mesh is drawn, they are not drawn as a post-processing effect after.
#desc [b]Note:[/b] Decals cannot affect an underlying material's transparency, regardless of its transparency mode (alpha blend, alpha scissor, alpha hash, opaque pre-pass). This means translucent or transparent areas of a material will remain translucent or transparent even if an opaque decal is applied on them.
class_name Decal

#desc [Texture2D] corresponding to [member texture_albedo].
const TEXTURE_ALBEDO = 0;

#desc [Texture2D] corresponding to [member texture_normal].
const TEXTURE_NORMAL = 1;

#desc [Texture2D] corresponding to [member texture_orm].
const TEXTURE_ORM = 2;

#desc [Texture2D] corresponding to [member texture_emission].
const TEXTURE_EMISSION = 3;

#desc Max size of [enum DecalTexture] enum.
const TEXTURE_MAX = 4;


#desc Blends the albedo [Color] of the decal with albedo [Color] of the underlying mesh. This can be set to [code]0.0[/code] to create a decal that only affects normal or ORM. In this case, an albedo texture is still required as its alpha channel will determine where the normal and ORM will be overridden. See also [member modulate].
var albedo_mix: float;

#desc Specifies which [member VisualInstance3D.layers] this decal will project on. By default, Decals affect all layers. This is used so you can specify which types of objects receive the Decal and which do not. This is especially useful so you can ensure that dynamic objects don't accidentally receive a Decal intended for the terrain under them.
var cull_mask: int;

#desc The distance from the camera at which the Decal begins to fade away (in 3D units).
var distance_fade_begin: float;

#desc If [code]true[/code], decals will smoothly fade away when far from the active [Camera3D] starting at [member distance_fade_begin]. The Decal will fade out over [member distance_fade_begin] + [member distance_fade_length], after which it will be culled and not sent to the shader at all. Use this to reduce the number of active Decals in a scene and thus improve performance.
var distance_fade_enabled: bool;

#desc The distance over which the Decal fades (in 3D units). The Decal becomes slowly more transparent over this distance and is completely invisible at the end. Higher values result in a smoother fade-out transition, which is more suited when the camera moves fast.
var distance_fade_length: float;

#desc Energy multiplier for the emission texture. This will make the decal emit light at a higher or lower intensity, independently of the albedo color. See also [member modulate].
var emission_energy: float;

#desc Sets the size of the [AABB] used by the decal. The AABB goes from [code]-extents[/code] to [code]extents[/code].
var extents: Vector3;

#desc Sets the curve over which the decal will fade as the surface gets further from the center of the [AABB]. Only positive values are valid (negative values will be clamped to [code]0.0[/code]). See also [member upper_fade].
var lower_fade: float;

#desc Changes the [Color] of the Decal by multiplying the albedo and emission colors with this value. The alpha component is only taken into account when multiplying the albedo color, not the emission color. See also [member emission_energy] and [member albedo_mix] to change the emission and albedo intensity independently of each other.
var modulate: Color;

#desc Fades the Decal if the angle between the Decal's [AABB] and the target surface becomes too large. A value of [code]0[/code] projects the Decal regardless of angle, a value of [code]1[/code] limits the Decal to surfaces that are nearly perpendicular.
#desc [b]Note:[/b] Setting [member normal_fade] to a value greater than [code]0.0[/code] has a small performance cost due to the added normal angle computations.
var normal_fade: float;

#desc [Texture2D] with the base [Color] of the Decal. Either this or the [member texture_emission] must be set for the Decal to be visible. Use the alpha channel like a mask to smoothly blend the edges of the decal with the underlying object.
#desc [b]Note:[/b] Unlike [BaseMaterial3D] whose filter mode can be adjusted on a per-material basis, the filter mode for [Decal] textures is set globally with [member ProjectSettings.rendering/textures/decals/filter].
var texture_albedo: Texture2D;

#desc [Texture2D] with the emission [Color] of the Decal. Either this or the [member texture_emission] must be set for the Decal to be visible. Use the alpha channel like a mask to smoothly blend the edges of the decal with the underlying object.
#desc [b]Note:[/b] Unlike [BaseMaterial3D] whose filter mode can be adjusted on a per-material basis, the filter mode for [Decal] textures is set globally with [member ProjectSettings.rendering/textures/decals/filter].
var texture_emission: Texture2D;

#desc [Texture2D] with the per-pixel normal map for the decal. Use this to add extra detail to decals.
#desc [b]Note:[/b] Unlike [BaseMaterial3D] whose filter mode can be adjusted on a per-material basis, the filter mode for [Decal] textures is set globally with [member ProjectSettings.rendering/textures/decals/filter].
#desc [b]Note:[/b] Setting this texture alone will not result in a visible decal, as [member texture_albedo] must also be set. To create a normal-only decal, load an albedo texture into [member texture_albedo] and set [member albedo_mix] to [code]0.0[/code]. The albedo texture's alpha channel will be used to determine where the underlying surface's normal map should be overridden (and its intensity).
var texture_normal: Texture2D;

#desc [Texture2D] storing ambient occlusion, roughness, and metallic for the decal. Use this to add extra detail to decals.
#desc [b]Note:[/b] Unlike [BaseMaterial3D] whose filter mode can be adjusted on a per-material basis, the filter mode for [Decal] textures is set globally with [member ProjectSettings.rendering/textures/decals/filter].
#desc [b]Note:[/b] Setting this texture alone will not result in a visible decal, as [member texture_albedo] must also be set. To create a ORM-only decal, load an albedo texture into [member texture_albedo] and set [member albedo_mix] to [code]0.0[/code]. The albedo texture's alpha channel will be used to determine where the underlying surface's ORM map should be overridden (and its intensity).
var texture_orm: Texture2D;

#desc Sets the curve over which the decal will fade as the surface gets further from the center of the [AABB]. Only positive values are valid (negative values will be clamped to [code]0.0[/code]). See also [member lower_fade].
var upper_fade: float;



#desc Returns the [Texture2D] associated with the specified [enum DecalTexture]. This is a convenience method, in most cases you should access the texture directly.
#desc For example, instead of [code]albedo_tex = $Decal.get_texture(Decal.TEXTURE_ALBEDO)[/code], use [code]albedo_tex = $Decal.texture_albedo[/code].
#desc One case where this is better than accessing the texture directly is when you want to copy one Decal's textures to another. For example:
#desc [codeblocks]
#desc [gdscript]
#desc for i in Decal.TEXTURE_MAX:
#desc $NewDecal.set_texture(i, $OldDecal.get_texture(i))
#desc [/gdscript]
#desc [csharp]
#desc for (int i = 0; i < (int)Decal.DecalTexture.Max; i++)
#desc {
#desc GetNode<Decal>("NewDecal").SetTexture(i, GetNode<Decal>("OldDecal").GetTexture(i));
#desc }
#desc [/csharp]
#desc [/codeblocks]
func get_texture(type: int) -> Texture2D:
	pass;

#desc Sets the [Texture2D] associated with the specified [enum DecalTexture]. This is a convenience method, in most cases you should access the texture directly.
#desc For example, instead of [code]$Decal.set_texture(Decal.TEXTURE_ALBEDO, albedo_tex)[/code], use [code]$Decal.texture_albedo = albedo_tex[/code].
#desc One case where this is better than accessing the texture directly is when you want to copy one Decal's textures to another. For example:
#desc [codeblocks]
#desc [gdscript]
#desc for i in Decal.TEXTURE_MAX:
#desc $NewDecal.set_texture(i, $OldDecal.get_texture(i))
#desc [/gdscript]
#desc [csharp]
#desc for (int i = 0; i < (int)Decal.DecalTexture.Max; i++)
#desc {
#desc GetNode<Decal>("NewDecal").SetTexture(i, GetNode<Decal>("OldDecal").GetTexture(i));
#desc }
#desc [/csharp]
#desc [/codeblocks]
func set_texture(type: int, texture: Texture2D) -> void:
	pass;


