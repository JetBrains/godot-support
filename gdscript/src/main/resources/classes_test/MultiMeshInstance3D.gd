#brief Node that instances a [MultiMesh].
#desc [MultiMeshInstance3D] is a specialized node to instance [GeometryInstance3D]s based on a [MultiMesh] resource.
#desc This is useful to optimize the rendering of a high number of instances of a given mesh (for example trees in a forest or grass strands).
class_name MultiMeshInstance3D


#desc The [MultiMesh] resource that will be used and shared among all instances of the [MultiMeshInstance3D].
var multimesh: MultiMesh;




