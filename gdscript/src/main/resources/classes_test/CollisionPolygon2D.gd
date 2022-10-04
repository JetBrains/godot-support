#brief Defines a 2D collision polygon.
#desc Provides a concave or convex 2D collision polygon to a [CollisionObject2D] parent. Polygons can be drawn in the editor or specified by a list of vertices. See also [ConvexPolygonShape2D].
#desc In the editor, a [CollisionPolygon2D] can be generated from a [Sprite2D]'s outline by selecting a [Sprite2D] node, going to the [b]Sprite2D[/b] menu at the top of the 2D editor viewport then choosing [b]Create CollisionPolygon2D Sibling[/b].
class_name CollisionPolygon2D

#desc Collisions will include the polygon and its contained area.
const BUILD_SOLIDS = 0;

#desc Collisions will only include the polygon edges.
const BUILD_SEGMENTS = 1;


#desc Collision build mode. Use one of the [enum BuildMode] constants.
var build_mode: int;

#desc If [code]true[/code], no collisions will be detected.
var disabled: bool;

#desc If [code]true[/code], only edges that face up, relative to [CollisionPolygon2D]'s rotation, will collide with other objects.
#desc [b]Note:[/b] This property has no effect if this [CollisionPolygon2D] is a child of an [Area2D] node.
var one_way_collision: bool;

#desc The margin used for one-way collision (in pixels). Higher values will make the shape thicker, and work better for colliders that enter the polygon at a high velocity.
var one_way_collision_margin: float;

#desc The polygon's list of vertices. The final point will be connected to the first. The returned value is a clone of the [PackedVector2Array], not a reference.
var polygon: PackedVector2Array;




