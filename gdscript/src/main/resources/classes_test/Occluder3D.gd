#brief Occluder shape resource for use with occlusion culling in [OccluderInstance3D].
#desc [Occluder3D] stores an occluder shape that can be used by the engine's occlusion culling system.
#desc See [OccluderInstance3D]'s documentation for instructions on setting up occlusion culling.
class_name Occluder3D




#desc Returns the occluder shape's vertex indices.
func get_indices() -> PackedInt32Array:
	pass;

#desc Returns the occluder shape's vertex positions.
func get_vertices() -> PackedVector3Array:
	pass;


