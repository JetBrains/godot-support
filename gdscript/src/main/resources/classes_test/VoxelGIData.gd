#brief Contains baked voxel global illumination data for use in a [VoxelGI] node.
#desc [VoxelGIData] contains baked voxel global illumination for use in a [VoxelGI] node. [VoxelGIData] also offers several properties to adjust the final appearance of the global illumination. These properties can be adjusted at run-time without having to bake the [VoxelGI] node again.
#desc [b]Note:[/b] To prevent text-based scene files ([code].tscn[/code]) from growing too much and becoming slow to load and save, always save [VoxelGIData] to an external binary resource file ([code].res[/code]) instead of embedding it within the scene. This can be done by clicking the dropdown arrow next to the [VoxelGIData] resource, choosing [b]Edit[/b], clicking the floppy disk icon at the top of the inspector then choosing [b]Save As...[/b].
class_name VoxelGIData


#desc The normal bias to use for indirect lighting and reflections. Higher values reduce self-reflections visible in non-rough materials, at the cost of more visible light leaking and flatter-looking indirect lighting. To prioritize hiding self-reflections over lighting quality, set [member bias] to [code]0.0[/code] and [member normal_bias] to a value between [code]1.0[/code] and [code]2.0[/code].
var bias: float;

#desc The dynamic range to use ([code]1.0[/code] represents a low dynamic range scene brightness). Higher values can be used to provide brighter indirect lighting, at the cost of more visible color banding in dark areas (both in indirect lighting and reflections). To avoid color banding, it's recommended to use the lowest value that does not result in visible light clipping.
var dynamic_range: float;

#desc The energy of the indirect lighting and reflections produced by the [VoxelGI] node. Higher values result in brighter indirect lighting. If indirect lighting looks too flat, try decreasing [member propagation] while increasing [member energy] at the same time. See also [member use_two_bounces] which influences the indirect lighting's effective brightness.
var energy: float;

#desc If [code]true[/code], [Environment] lighting is ignored by the [VoxelGI] node. If [code]false[/code], [Environment] lighting is taken into account by the [VoxelGI] node. [Environment] lighting updates in real-time, which means it can be changed without having to bake the [VoxelGI] node again.
var interior: bool;

#desc The normal bias to use for indirect lighting and reflections. Higher values reduce self-reflections visible in non-rough materials, at the cost of more visible light leaking and flatter-looking indirect lighting. See also [member bias]. To prioritize hiding self-reflections over lighting quality, set [member bias] to [code]0.0[/code] and [member normal_bias] to a value between [code]1.0[/code] and [code]2.0[/code].
var normal_bias: float;

#desc The multiplier to use when light bounces off a surface. Higher values result in brighter indirect lighting. If indirect lighting looks too flat, try decreasing [member propagation] while increasing [member energy] at the same time. See also [member use_two_bounces] which influences the indirect lighting's effective brightness.
var propagation: float;

#desc If [code]true[/code], performs two bounces of indirect lighting instead of one. This makes indirect lighting look more natural and brighter at a small performance cost. The second bounce is also visible in reflections. If the scene appears too bright after enabling [member use_two_bounces], adjust [member propagation] and [member energy].
var use_two_bounces: bool;



func allocate(to_cell_xform: Transform3D, aabb: AABB, octree_size: Vector3, octree_cells: PackedByteArray, data_cells: PackedByteArray, distance_field: PackedByteArray, level_counts: PackedInt32Array) -> void:
	pass;

#desc Returns the bounds of the baked voxel data as an [AABB], which should match [member VoxelGI.extents] after being baked (which only contains the size as a [Vector3]).
#desc [b]Note:[/b] If the extents were modified without baking the VoxelGI data, then the value of [method get_bounds] and [member VoxelGI.extents] will not match.
func get_bounds() -> AABB:
	pass;

func get_data_cells() -> PackedByteArray:
	pass;

func get_level_counts() -> PackedInt32Array:
	pass;

func get_octree_cells() -> PackedByteArray:
	pass;

func get_octree_size() -> Vector3:
	pass;

func get_to_cell_xform() -> Transform3D:
	pass;


