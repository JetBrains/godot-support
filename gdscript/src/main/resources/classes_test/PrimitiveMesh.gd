extends Mesh
class_name PrimitiveMesh

var custom_aabb: AABB;
var flip_faces: bool;
var material: Material;

func get_mesh_arrays() -> Array:
    pass;
