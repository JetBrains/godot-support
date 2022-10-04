#brief Concave polygon shape resource for 2D physics.
#desc 2D concave polygon shape to be added as a [i]direct[/i] child of a [PhysicsBody2D] or [Area2D] using a [CollisionShape2D] node. It is made out of segments and is optimal for complex polygonal concave collisions. However, it is not advised to use for [RigidBody2D] nodes. A CollisionPolygon2D in convex decomposition mode (solids) or several convex objects are advised for that instead. Otherwise, a concave polygon 2D shape is better for static collisions.
#desc The main difference between a [ConvexPolygonShape2D] and a [ConcavePolygonShape2D] is that a concave polygon assumes it is concave and uses a more complex method of collision detection, and a convex one forces itself to be convex to speed up collision detection.
#desc [b]Performance:[/b] Due to its complexity, [ConcavePolygonShape2D] is the slowest collision shape to check collisions against. Its use should generally be limited to level geometry. For convex geometry, using [ConvexPolygonShape2D] will perform better. For dynamic physics bodies that need concave collision, several [ConvexPolygonShape2D]s can be used to represent its collision by using convex decomposition; see [ConvexPolygonShape2D]'s documentation for instructions. However, consider using primitive collision shapes such as [CircleShape2D] or [RectangleShape2D] first.
#desc [b]Warning:[/b] Using this shape for an [Area2D] (via a [CollisionShape2D] node) may give unexpected results: the area will only detect collisions with the segments in the [ConcavePolygonShape2D] (and not with any "inside" of the shape, for example).
class_name ConcavePolygonShape2D


#desc The array of points that make up the [ConcavePolygonShape2D]'s line segments.
var segments: PackedVector2Array;




