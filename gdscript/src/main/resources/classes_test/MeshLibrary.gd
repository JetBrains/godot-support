extends Resource
#brief Library of meshes.
#desc A library of meshes. Contains a list of [Mesh] resources, each with a name and ID. Each item can also include collision and navigation shapes. This resource is used in [GridMap].
class_name MeshLibrary




#desc Clears the library.
func clear() -> void:
	pass;

#desc Creates a new item in the library with the given ID.
#desc You can get an unused ID from [method get_last_unused_item_id].
func create_item(id: int) -> void:
	pass;

#desc Returns the first item with the given name.
func find_item_by_name(name: String) -> int:
	pass;

#desc Returns the list of item IDs in use.
func get_item_list() -> PackedInt32Array:
	pass;

#desc Returns the item's mesh.
func get_item_mesh(id: int) -> Mesh:
	pass;

#desc Returns the transform applied to the item's mesh.
func get_item_mesh_transform(id: int) -> Transform3D:
	pass;

#desc Returns the item's name.
func get_item_name(id: int) -> String:
	pass;

#desc Returns the item's navigation mesh.
func get_item_navmesh(id: int) -> NavigationMesh:
	pass;

#desc Returns the transform applied to the item's navigation mesh.
func get_item_navmesh_transform(id: int) -> Transform3D:
	pass;

#desc When running in the editor, returns a generated item preview (a 3D rendering in isometric perspective). When used in a running project, returns the manually-defined item preview which can be set using [method set_item_preview]. Returns an empty [Texture2D] if no preview was manually set in a running project.
func get_item_preview(id: int) -> Texture2D:
	pass;

#desc Returns an item's collision shapes.
#desc The array consists of each [Shape3D] followed by its [Transform3D].
func get_item_shapes(id: int) -> Array:
	pass;

#desc Gets an unused ID for a new item.
func get_last_unused_item_id() -> int:
	pass;

#desc Removes the item.
func remove_item(id: int) -> void:
	pass;

#desc Sets the item's mesh.
func set_item_mesh(id: int, mesh: Mesh) -> void:
	pass;

#desc Sets the transform to apply to the item's mesh.
func set_item_mesh_transform(id: int, mesh_transform: Transform3D) -> void:
	pass;

#desc Sets the item's name.
#desc This name is shown in the editor. It can also be used to look up the item later using [method find_item_by_name].
func set_item_name(id: int, name: String) -> void:
	pass;

#desc Sets the item's navigation mesh.
func set_item_navmesh(id: int, navmesh: NavigationMesh) -> void:
	pass;

#desc Sets the transform to apply to the item's navigation mesh.
func set_item_navmesh_transform(id: int, navmesh: Transform3D) -> void:
	pass;

#desc Sets a texture to use as the item's preview icon in the editor.
func set_item_preview(id: int, texture: Texture2D) -> void:
	pass;

#desc Sets an item's collision shapes.
#desc The array should consist of [Shape3D] objects, each followed by a [Transform3D] that will be applied to it. For shapes that should not have a transform, use [constant Transform3D.IDENTITY].
func set_item_shapes(id: int, shapes: Array) -> void:
	pass;


