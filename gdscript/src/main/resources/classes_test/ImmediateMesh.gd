#brief Mesh optimized for creating geometry manually.
#desc Mesh optimized for creating geometry manually, similar to OpenGL1.x immediate mode.
class_name ImmediateMesh




#desc Clear all surfaces.
func clear_surfaces() -> void:
	pass;

#desc Add a 3D vertex using the current attributes previously set.
func surface_add_vertex() -> void:
	pass;

#desc Add a 2D vertex using the current attributes previously set.
func surface_add_vertex_2d() -> void:
	pass;

#desc Begin a new surface.
func surface_begin(primitive: int, material: Material) -> void:
	pass;

#desc End and commit current surface. Note that surface being created will not be visible until this function is called.
func surface_end() -> void:
	pass;

#desc Set the color attribute that will be pushed with the next vertex.
func surface_set_color() -> void:
	pass;

#desc Set the normal attribute that will be pushed with the next vertex.
func surface_set_normal() -> void:
	pass;

#desc Set the tangent attribute that will be pushed with the next vertex.
func surface_set_tangent() -> void:
	pass;

#desc Set the UV attribute that will be pushed with the next vertex.
func surface_set_uv() -> void:
	pass;

#desc Set the UV2 attribute that will be pushed with the next vertex.
func surface_set_uv2() -> void:
	pass;


