extends Shape2D
#brief Rectangle shape resource for 2D physics.
#desc 2D rectangle shape to be added as a [i]direct[/i] child of a [PhysicsBody2D] or [Area2D] using a [CollisionShape2D] node. This shape is useful for modeling box-like 2D objects.
#desc [b]Performance:[/b] Being a primitive collision shape, [RectangleShape2D] is fast to check collisions against (though not as fast as [CircleShape2D]).
class_name RectangleShape2D


#desc The rectangle's width and height.
var size: Vector2;




