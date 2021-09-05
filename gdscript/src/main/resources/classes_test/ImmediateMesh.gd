extends Mesh
class_name ImmediateMesh



func clear_surfaces() -> void:
    pass;

func surface_add_vertex(vertex: Vector3) -> void:
    pass;

func surface_add_vertex_2d(vertex: Vector2) -> void:
    pass;

func surface_begin(primitive: int, material: Material) -> void:
    pass;

func surface_end() -> void:
    pass;

func surface_set_color(color: Color) -> void:
    pass;

func surface_set_normal(normal: Vector3) -> void:
    pass;

func surface_set_tangent(tangent: Plane) -> void:
    pass;

func surface_set_uv(uv: Vector2) -> void:
    pass;

func surface_set_uv2(uv2: Vector2) -> void:
    pass;

