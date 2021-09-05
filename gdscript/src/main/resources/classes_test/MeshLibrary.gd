extends Resource
class_name MeshLibrary



func clear() -> void:
    pass;

func create_item(id: int) -> void:
    pass;

func find_item_by_name(name: String) -> int:
    pass;

func get_item_list() -> PackedInt32Array:
    pass;

func get_item_mesh(id: int) -> Mesh:
    pass;

func get_item_name(id: int) -> String:
    pass;

func get_item_navmesh(id: int) -> NavigationMesh:
    pass;

func get_item_navmesh_transform(id: int) -> Transform3D:
    pass;

func get_item_preview(id: int) -> Texture2D:
    pass;

func get_item_shapes(id: int) -> Array:
    pass;

func get_last_unused_item_id() -> int:
    pass;

func remove_item(id: int) -> void:
    pass;

func set_item_mesh(id: int, mesh: Mesh) -> void:
    pass;

func set_item_name(id: int, name: String) -> void:
    pass;

func set_item_navmesh(id: int, navmesh: NavigationMesh) -> void:
    pass;

func set_item_navmesh_transform(id: int, navmesh: Transform3D) -> void:
    pass;

func set_item_preview(id: int, texture: Texture2D) -> void:
    pass;

func set_item_shapes(id: int, shapes: Array) -> void:
    pass;

