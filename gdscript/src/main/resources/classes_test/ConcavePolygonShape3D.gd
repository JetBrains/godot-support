#brief Concave polygon shape resource (also called "trimesh") for 3D physics.
#desc 3D concave polygon shape resource (also called "trimesh") to be added as a [i]direct[/i] child of a [PhysicsBody3D] or [Area3D] using a [CollisionShape3D] node. This shape is created by feeding a list of triangles. Despite its name, [ConcavePolygonShape3D] can also store convex polygon shapes. However, unlike [ConvexPolygonShape3D], [ConcavePolygonShape3D] is [i]not[/i] limited to storing convex shapes exclusively.
#desc [b]Note:[/b] When used for collision, [ConcavePolygonShape3D] is intended to work with static [PhysicsBody3D] nodes like [StaticBody3D] and will not work with [CharacterBody3D] or [RigidBody3D] with a mode other than Static.
#desc [b]Performance:[/b] Due to its complexity, [ConcavePolygonShape3D] is the slowest collision shape to check collisions against. Its use should generally be limited to level geometry. For convex geometry, using [ConvexPolygonShape3D] will perform better. For dynamic physics bodies that need concave collision, several [ConvexPolygonShape3D]s can be used to represent its collision by using convex decomposition; see [ConvexPolygonShape3D]'s documentation for instructions. However, consider using primitive collision shapes such as [SphereShape3D] or [BoxShape3D] first.
#desc [b]Warning:[/b] Using this shape for an [Area3D] (via a [CollisionShape3D] node, created e.g. by using the [i]Create Trimesh Collision Sibling[/i] option in the [i]Mesh[/i] menu that appears when selecting a [MeshInstance3D] node) may give unexpected results: the area will only detect collisions with the triangle faces in the [ConcavePolygonShape3D] (and not with any "inside" of the shape, for example); moreover it will only detect all such collisions if [member backface_collision] is [code]true[/code].
class_name ConcavePolygonShape3D


#desc If set to [code]true[/code], collisions occur on both sides of the concave shape faces. Otherwise they occur only along the face normals.
var backface_collision: bool;



#desc Returns the faces (an array of triangles).
func get_faces() -> PackedVector3Array:
	pass;

#desc Sets the faces (an array of triangles).
func set_faces(faces: PackedVector3Array) -> void:
	pass;


