#brief Convex polygon shape resource for 3D physics.
#desc 3D convex polygon shape resource to be added as a [i]direct[/i] child of a [PhysicsBody3D] or [Area3D] using a [CollisionShape3D] node. Unlike [ConcavePolygonShape3D], [ConvexPolygonShape3D] cannot store concave polygon shapes. [ConvexPolygonShape2D]s can be manually drawn in the editor using the [CollisionPolygon3D] node.
#desc [b]Convex decomposition:[/b] Concave objects' collisions can be represented accurately using [i]several[/i] [ConvexPolygonShape3D]s. This allows dynamic physics bodies to have complex concave collisions (at a performance cost). This is available in the editor by selecting the [MeshInstance3D], going to the [b]Mesh[/b] menu and choosing [b]Create Multiple Convex Collision Siblings[/b]. Alternatively, [method MeshInstance3D.create_multiple_convex_collisions] can be called in a script to perform this decomposition at run-time.
#desc [b]Performance:[/b] [ConvexPolygonShape3D] is faster to check collisions against compared to [ConcavePolygonShape3D], but it is slower than primitive collision shapes such as [SphereShape3D] or [BoxShape3D]. Its use should generally be limited to medium-sized objects that cannot have their collision accurately represented by a primitive shape.
class_name ConvexPolygonShape3D


#desc The list of 3D points forming the convex polygon shape.
var points: PackedVector3Array;




