extends Occluder3D
#brief 3D polygon shape for use with occlusion culling in [OccluderInstance3D].
#desc [ArrayOccluder3D] stores an arbitrary 3D polygon shape that can be used by the engine's occlusion culling system. This is analogous to [ArrayMesh], but for occluders.
#desc See [OccluderInstance3D]'s documentation for instructions on setting up occlusion culling.
class_name ArrayOccluder3D


var indices: PackedInt32Array;

var vertices: PackedVector3Array;



func set_arrays(vertices: PackedVector3Array, indices: PackedInt32Array) -> void:
	pass;


