extends Shape2D
#brief Segment shape resource for 2D physics.
#desc 2D segment shape to be added as a [i]direct[/i] child of a [PhysicsBody2D] or [Area2D] using a [CollisionShape2D] node. Consists of two points, [code]a[/code] and [code]b[/code].
#desc [b]Performance:[/b] Being a primitive collision shape, [SegmentShape2D] is fast to check collisions against (though not as fast as [CircleShape2D]).
class_name SegmentShape2D


#desc The segment's first point position.
var a: Vector2;

#desc The segment's second point position.
var b: Vector2;




