#brief Convex polygon shape resource for 2D physics.
#desc 2D convex polygon shape to be added as a [i]direct[/i] child of a [PhysicsBody2D] or [Area2D] using a [CollisionShape2D] node. A convex polygon, whatever its shape, is internally decomposed into as many convex polygons as needed to ensure all collision checks against it are always done on convex polygons (which are faster to check). See also [CollisionPolygon2D].
#desc The main difference between a [ConvexPolygonShape2D] and a [ConcavePolygonShape2D] is that a concave polygon assumes it is concave and uses a more complex method of collision detection, and a convex one forces itself to be convex to speed up collision detection.
#desc [b]Performance:[/b] [ConvexPolygonShape2D] is faster to check collisions against compared to [ConcavePolygonShape2D], but it is slower than primitive collision shapes such as [CircleShape2D] or [RectangleShape2D]. Its use should generally be limited to medium-sized objects that cannot have their collision accurately represented by a primitive shape.
class_name ConvexPolygonShape2D


#desc The polygon's list of vertices. Can be in either clockwise or counterclockwise order. Only set this property with convex hull points, use [method set_point_cloud] to generate a convex hull shape from concave shape points.
var points: PackedVector2Array;



#desc Based on the set of points provided, this creates and assigns the [member points] property using the convex hull algorithm. Removing all unneeded points. See [method Geometry2D.convex_hull] for details.
func set_point_cloud(point_cloud: PackedVector2Array) -> void:
	pass;


