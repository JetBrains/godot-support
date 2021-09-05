extends GeometryInstance3D
class_name MeshInstance3D


var mesh: Mesh setget set_mesh, get_mesh;
var skeleton: NodePath setget set_skeleton_path, get_skeleton_path;
var skin: Skin setget set_skin, get_skin;

func create_convex_collision(clean: bool, simplify: bool) -> void:
    pass;

func create_debug_tangents() -> void:
    pass;

func create_multiple_convex_collisions() -> void:
    pass;

func create_trimesh_collision() -> void:
    pass;

func get_active_material(surface: int) -> Material:
    pass;

func get_surface_override_material(surface: int) -> Material:
    pass;

func get_surface_override_material_count() -> int:
    pass;

func set_surface_override_material(surface: int, material: Material) -> void:
    pass;

