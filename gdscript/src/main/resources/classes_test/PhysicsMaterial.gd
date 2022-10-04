#brief A material for physics properties.
#desc Provides a means of modifying the collision properties of a [PhysicsBody3D].
class_name PhysicsMaterial


#desc If [code]true[/code], subtracts the bounciness from the colliding object's bounciness instead of adding it.
var absorbent: bool;

#desc The body's bounciness. Values range from [code]0[/code] (no bounce) to [code]1[/code] (full bounciness).
var bounce: float;

#desc The body's friction. Values range from [code]0[/code] (frictionless) to [code]1[/code] (maximum friction).
var friction: float;

#desc If [code]true[/code], the physics engine will use the friction of the object marked as "rough" when two objects collide. If [code]false[/code], the physics engine will use the lowest friction of all colliding objects instead. If [code]true[/code] for both colliding objects, the physics engine will use the highest friction.
var rough: bool;




