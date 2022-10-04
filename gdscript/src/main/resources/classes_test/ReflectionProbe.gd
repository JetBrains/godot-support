#brief Captures its surroundings to create fast, accurate reflections from a given point.
#desc Captures its surroundings as a cubemap, and stores versions of it with increasing levels of blur to simulate different material roughnesses.
#desc The [ReflectionProbe] is used to create high-quality reflections at a low performance cost (when [member update_mode] is [constant UPDATE_ONCE]). [ReflectionProbe]s can be blended together and with the rest of the scene smoothly. [ReflectionProbe]s can also be combined with [VoxelGI], SDFGI ([member Environment.sdfgi_enabled]) and screen-space reflections ([member Environment.ssr_enabled]) to get more accurate reflections in specific areas. [ReflectionProbe]s render all objects within their [member cull_mask], so updating them can be quite expensive. It is best to update them once with the important static objects and then leave them as-is.
#desc [b]Note:[/b] Unlike [VoxelGI] and SDFGI, [ReflectionProbe]s only source their environment from a [WorldEnvironment] node. If you specify an [Environment] resource within a [Camera3D] node, it will be ignored by the [ReflectionProbe]. This can lead to incorrect lighting within the [ReflectionProbe].
class_name ReflectionProbe

#desc Update the probe once on the next frame (recommended for most objects). The corresponding radiance map will be generated over the following six frames. This takes more time to update than [constant UPDATE_ALWAYS], but it has a lower performance cost and can result in higher-quality reflections. The ReflectionProbe is updated when its transform changes, but not when nearby geometry changes. You can force a [ReflectionProbe] update by moving the [ReflectionProbe] slightly in any direction.
const UPDATE_ONCE = 0;

#desc Update the probe every frame. This provides better results for fast-moving dynamic objects (such as cars). However, it has a significant performance cost. Due to the cost, it's recommended to only use one ReflectionProbe with [constant UPDATE_ALWAYS] at most per scene. For all other use cases, use [constant UPDATE_ONCE].
const UPDATE_ALWAYS = 1;

#desc Do not apply any ambient lighting inside the [ReflectionProbe]'s [member extents].
const AMBIENT_DISABLED = 0;

#desc Apply automatically-sourced environment lighting inside the [ReflectionProbe]'s [member extents].
const AMBIENT_ENVIRONMENT = 1;

#desc Apply custom ambient lighting inside the [ReflectionProbe]'s [member extents]. See [member ambient_color] and [member ambient_color_energy].
const AMBIENT_COLOR = 2;


#desc The custom ambient color to use within the [ReflectionProbe]'s [member extents]. Only effective if [member ambient_mode] is [constant AMBIENT_COLOR].
var ambient_color: Color;

#desc The custom ambient color energy to use within the [ReflectionProbe]'s [member extents]. Only effective if [member ambient_mode] is [constant AMBIENT_COLOR].
var ambient_color_energy: float;

#desc The ambient color to use within the [ReflectionProbe]'s [member extents]. The ambient color will smoothly blend with other [ReflectionProbe]s and the rest of the scene (outside the [ReflectionProbe]'s [member extents]).
var ambient_mode: int;

#desc If [code]true[/code], enables box projection. This makes reflections look more correct in rectangle-shaped rooms by offsetting the reflection center depending on the camera's location.
#desc [b]Note:[/b] To better fit rectangle-shaped rooms that are not aligned to the grid, you can rotate the [ReflectionProbe] node.
var box_projection: bool;

#desc Sets the cull mask which determines what objects are drawn by this probe. Every [VisualInstance3D] with a layer included in this cull mask will be rendered by the probe. To improve performance, it is best to only include large objects which are likely to take up a lot of space in the reflection.
var cull_mask: int;

#desc If [code]true[/code], computes shadows in the reflection probe. This makes the reflection probe slower to render; you may want to disable this if using the [constant UPDATE_ALWAYS] [member update_mode].
var enable_shadows: bool;

#desc The size of the reflection probe. The larger the extents, the more space covered by the probe, which will lower the perceived resolution. It is best to keep the extents only as large as you need them.
#desc [b]Note:[/b] To better fit areas that are not aligned to the grid, you can rotate the [ReflectionProbe] node.
var extents: Vector3;

#desc Defines the reflection intensity. Intensity modulates the strength of the reflection.
var intensity: float;

#desc If [code]true[/code], reflections will ignore sky contribution.
var interior: bool;

#desc The maximum distance away from the [ReflectionProbe] an object can be before it is culled. Decrease this to improve performance, especially when using the [constant UPDATE_ALWAYS] [member update_mode].
#desc [b]Note:[/b] The maximum reflection distance is always at least equal to the [member extents]. This means that decreasing [member max_distance] will not always cull objects from reflections, especially if the reflection probe's [member extents] are already large.
var max_distance: float;

#desc The automatic LOD bias to use for meshes rendered within the [ReflectionProbe] (this is analog to [member Viewport.mesh_lod_threshold]). Higher values will use less detailed versions of meshes that have LOD variations generated. If set to [code]0.0[/code], automatic LOD is disabled. Increase [member mesh_lod_threshold] to improve performance at the cost of geometry detail, especially when using the [constant UPDATE_ALWAYS] [member update_mode].
#desc [b]Note:[/b] [member mesh_lod_threshold] does not affect [GeometryInstance3D] visibility ranges (also known as "manual" LOD or hierarchical LOD).
var mesh_lod_threshold: float;

#desc Sets the origin offset to be used when this [ReflectionProbe] is in [member box_projection] mode. This can be set to a non-zero value to ensure a reflection fits a rectangle-shaped room, while reducing the number of objects that "get in the way" of the reflection.
var origin_offset: Vector3;

#desc Sets how frequently the [ReflectionProbe] is updated. Can be [constant UPDATE_ONCE] or [constant UPDATE_ALWAYS].
var update_mode: int;




