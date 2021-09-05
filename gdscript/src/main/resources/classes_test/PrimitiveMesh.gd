extends Mesh
class_name PrimitiveMesh


var custom_aabb: AABB setget set_custom_aabb, get_custom_aabb;
var flip_faces: bool setget set_flip_faces, get_flip_faces;
var material: Material setget set_material, get_material;

func get_mesh_arrays() -> Array:
    pass;

