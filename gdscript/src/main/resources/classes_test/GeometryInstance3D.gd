#brief Base node for geometry-based visual instances.
#desc Base node for geometry-based visual instances. Shares some common functionality like visibility and custom materials.
class_name GeometryInstance3D

#desc Will not cast any shadows. Use this to improve performance for small geometry that is unlikely to cast noticeable shadows (such as debris).
const SHADOW_CASTING_SETTING_OFF = 0;

#desc Will cast shadows from all visible faces in the GeometryInstance3D.
#desc Will take culling into account, so faces not being rendered will not be taken into account when shadow casting.
const SHADOW_CASTING_SETTING_ON = 1;

#desc Will cast shadows from all visible faces in the GeometryInstance3D.
#desc Will not take culling into account, so all faces will be taken into account when shadow casting.
const SHADOW_CASTING_SETTING_DOUBLE_SIDED = 2;

#desc Will only show the shadows casted from this object.
#desc In other words, the actual mesh will not be visible, only the shadows casted from the mesh will be.
const SHADOW_CASTING_SETTING_SHADOWS_ONLY = 3;

#desc Disabled global illumination mode. Use for dynamic objects that do not contribute to global illumination (such as characters). When using [VoxelGI] and SDFGI, the geometry will [i]receive[/i] indirect lighting and reflections but the geometry will not be considered in GI baking. When using [LightmapGI], the object will receive indirect lighting using lightmap probes instead of using the baked lightmap texture.
const GI_MODE_DISABLED = 0;

#desc Baked global illumination mode. Use for static objects that contribute to global illumination (such as level geometry). This GI mode is effective when using [VoxelGI], SDFGI and [LightmapGI].
const GI_MODE_STATIC = 1;

#desc Dynamic global illumination mode. Use for dynamic objects that contribute to global illumination. This GI mode is only effective when using [VoxelGI], but it has a higher performance impact than [constant GI_MODE_STATIC]. When using other GI methods, this will act the same as [constant GI_MODE_DISABLED].
const GI_MODE_DYNAMIC = 2;

#desc The standard texel density for lightmapping with [LightmapGI].
const LIGHTMAP_SCALE_1X = 0;

#desc Multiplies texel density by 2× for lightmapping with [LightmapGI]. To ensure consistency in texel density, use this when scaling a mesh by a factor between 1.5 and 3.0.
const LIGHTMAP_SCALE_2X = 1;

#desc Multiplies texel density by 4× for lightmapping with [LightmapGI]. To ensure consistency in texel density, use this when scaling a mesh by a factor between 3.0 and 6.0.
const LIGHTMAP_SCALE_4X = 2;

#desc Multiplies texel density by 8× for lightmapping with [LightmapGI]. To ensure consistency in texel density, use this when scaling a mesh by a factor greater than 6.0.
const LIGHTMAP_SCALE_8X = 3;

#desc Represents the size of the [enum LightmapScale] enum.
const LIGHTMAP_SCALE_MAX = 4;

#desc Will not fade itself nor its visibility dependencies, hysteresis will be used instead. This is the fastest approach to manual LOD, but it can result in noticeable LOD transitions depending on how the LOD meshes are authored. See [member visibility_range_begin] and [member Node3D.visibility_parent] for more information.
const VISIBILITY_RANGE_FADE_DISABLED = 0;

#desc Will fade-out itself when reaching the limits of its own visibility range. This is slower than [constant VISIBILITY_RANGE_FADE_DISABLED], but it can provide smoother transitions. The fading range is determined by [member visibility_range_begin_margin] and [member visibility_range_end_margin].
const VISIBILITY_RANGE_FADE_SELF = 1;

#desc Will fade-in its visibility dependencies (see [member Node3D.visibility_parent]) when reaching the limits of its own visibility range. This is slower than [constant VISIBILITY_RANGE_FADE_DISABLED], but it can provide smoother transitions. The fading range is determined by [member visibility_range_begin_margin] and [member visibility_range_end_margin].
const VISIBILITY_RANGE_FADE_DEPENDENCIES = 2;


#desc The selected shadow casting flag. See [enum ShadowCastingSetting] for possible values.
var cast_shadow: int;

#desc The extra distance added to the GeometryInstance3D's bounding box ([AABB]) to increase its cull box.
var extra_cull_margin: float;

#desc The texel density to use for lightmapping in [LightmapGI]. Greater scale values provide higher resolution in the lightmap, which can result in sharper shadows for lights that have both direct and indirect light baked. However, greater scale values will also increase the space taken by the mesh in the lightmap texture, which increases the memory, storage, and bake time requirements. When using a single mesh at different scales, consider adjusting this value to keep the lightmap texel density consistent across meshes.
var gi_lightmap_scale: int;

#desc The global illumination mode to use for the whole geometry. To avoid inconsistent results, use a mode that matches the purpose of the mesh during gameplay (static/dynamic).
#desc [b]Note:[/b] Lights' bake mode will also affect the global illumination rendering. See [member Light3D.light_bake_mode].
var gi_mode: int;

var ignore_occlusion_culling: bool;

var lod_bias: float;

#desc The material overlay for the whole geometry.
#desc If a material is assigned to this property, it will be rendered on top of any other active material for all the surfaces.
var material_overlay: Material;

#desc The material override for the whole geometry.
#desc If a material is assigned to this property, it will be used instead of any material set in any material slot of the mesh.
var material_override: Material;

#desc The transparency applied to the whole geometry (as a multiplier of the materials' existing transparency). [code]0.0[/code] is fully opaque, while [code]1.0[/code] is fully transparent. Values greater than [code]0.0[/code] (exclusive) will force the geometry's materials to go through the transparent pipeline, which is slower to render and can exhibit rendering issues due to incorrect transparency sorting. However, unlike using a transparent material, setting [member transparency] to a value greater than [code]0.0[/code] (exclusive) will [i]not[/i] disable shadow rendering.
#desc In spatial shaders, [code]1.0 - transparency[/code] is set as the default value of the [code]ALPHA[/code] built-in.
#desc [b]Note:[/b] [member transparency] is clamped between [code]0.0[/code] and [code]1.0[/code], so this property cannot be used to make transparent materials more opaque than they originally are.
var transparency: float;

#desc Starting distance from which the GeometryInstance3D will be visible, taking [member visibility_range_begin_margin] into account as well. The default value of 0 is used to disable the range check.
var visibility_range_begin: float;

#desc Margin for the [member visibility_range_begin] threshold. The GeometryInstance3D will only change its visibility state when it goes over or under the [member visibility_range_begin] threshold by this amount.
#desc If [member visibility_range_fade_mode] is [constant VISIBILITY_RANGE_FADE_DISABLED], this acts as an hysteresis distance. If [member visibility_range_fade_mode] is [constant VISIBILITY_RANGE_FADE_SELF] or [constant VISIBILITY_RANGE_FADE_DEPENDENCIES], this acts as a fade transition distance and must be set to a value greater than [code]0.0[/code] for the effect to be noticeable.
var visibility_range_begin_margin: float;

#desc Distance from which the GeometryInstance3D will be hidden, taking [member visibility_range_end_margin] into account as well. The default value of 0 is used to disable the range check.
var visibility_range_end: float;

#desc Margin for the [member visibility_range_end] threshold. The GeometryInstance3D will only change its visibility state when it goes over or under the [member visibility_range_end] threshold by this amount.
#desc If [member visibility_range_fade_mode] is [constant VISIBILITY_RANGE_FADE_DISABLED], this acts as an hysteresis distance. If [member visibility_range_fade_mode] is [constant VISIBILITY_RANGE_FADE_SELF] or [constant VISIBILITY_RANGE_FADE_DEPENDENCIES], this acts as a fade transition distance and must be set to a value greater than [code]0.0[/code] for the effect to be noticeable.
var visibility_range_end_margin: float;

#desc Controls which instances will be faded when approaching the limits of the visibility range. See [enum VisibilityRangeFadeMode] for possible values.
var visibility_range_fade_mode: int;



func get_instance_shader_parameter() -> Variant:
	pass;

#desc Overrides the bounding box of this node with a custom one. To remove it, set an [AABB] with all fields set to zero.
func set_custom_aabb() -> void:
	pass;

func set_instance_shader_parameter(name: StringName, value: Variant) -> void:
	pass;


