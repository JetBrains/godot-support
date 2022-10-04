#brief Node that instances meshes into a scenario.
#desc MeshInstance3D is a node that takes a [Mesh] resource and adds it to the current scenario by creating an instance of it. This is the class most often used render 3D geometry and can be used to instance a single [Mesh] in many places. This allows reusing geometry, which can save on resources. When a [Mesh] has to be instantiated more than thousands of times at close proximity, consider using a [MultiMesh] in a [MultiMeshInstance3D] instead.
class_name MeshInstance3D


#desc The [Mesh] resource for the instance.
var mesh: Mesh;

#desc [NodePath] to the [Skeleton3D] associated with the instance.
var skeleton: NodePath;

#desc Sets the skin to be used by this instance.
var skin: Skin;



#desc This helper creates a [StaticBody3D] child node with a [ConvexPolygonShape3D] collision shape calculated from the mesh geometry. It's mainly used for testing.
#desc If [param clean] is [code]true[/code] (default), duplicate and interior vertices are removed automatically. You can set it to [code]false[/code] to make the process faster if not needed.
#desc If [param simplify] is [code]true[/code], the geometry can be further simplified to reduce the number of vertices. Disabled by default.
func create_convex_collision(clean: bool, simplify: bool) -> void:
	pass;

#desc This helper creates a [MeshInstance3D] child node with gizmos at every vertex calculated from the mesh geometry. It's mainly used for testing.
func create_debug_tangents() -> void:
	pass;

#desc This helper creates a [StaticBody3D] child node with multiple [ConvexPolygonShape3D] collision shapes calculated from the mesh geometry via convex decomposition. It's mainly used for testing.
func create_multiple_convex_collisions() -> void:
	pass;

#desc This helper creates a [StaticBody3D] child node with a [ConcavePolygonShape3D] collision shape calculated from the mesh geometry. It's mainly used for testing.
func create_trimesh_collision() -> void:
	pass;

func find_blend_shape_by_name(name: StringName) -> int:
	pass;

#desc Returns the [Material] that will be used by the [Mesh] when drawing. This can return the [member GeometryInstance3D.material_override], the surface override [Material] defined in this [MeshInstance3D], or the surface [Material] defined in the [Mesh]. For example, if [member GeometryInstance3D.material_override] is used, all surfaces will return the override material.
func get_active_material(surface: int) -> Material:
	pass;

func get_blend_shape_count() -> int:
	pass;

func get_blend_shape_value(blend_shape_idx: int) -> float:
	pass;

#desc Returns the override [Material] for the specified surface of the [Mesh] resource.
func get_surface_override_material(surface: int) -> Material:
	pass;

#desc Returns the number of surface override materials. This is equivalent to [method Mesh.get_surface_count].
func get_surface_override_material_count() -> int:
	pass;

func set_blend_shape_value(blend_shape_idx: int, value: float) -> void:
	pass;

#desc Sets the override [Material] for the specified surface of the [Mesh] resource. This material is associated with this [MeshInstance3D] rather than with the [Mesh] resource.
func set_surface_override_material(surface: int, material: Material) -> void:
	pass;


