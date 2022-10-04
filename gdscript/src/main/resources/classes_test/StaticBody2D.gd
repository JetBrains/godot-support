extends PhysicsBody2D
#brief Physics body for 2D physics which is static or moves only by script. Useful for floor and walls.
#desc Static body for 2D physics.
#desc A static body is a simple body that can't be moved by external forces or contacts. It is ideal for implementing objects in the environment, such as walls or platforms. In contrast to [RigidBody2D], it doesn't consume any CPU resources as long as they don't move.
#desc They have extra functionalities to move and affect other bodies:
#desc [b]Static transform change:[/b] Static bodies can be moved by animation or script. In this case, they are just teleported and don't affect other bodies on their path.
#desc [b]Constant velocity:[/b] When [member constant_linear_velocity] or [member constant_angular_velocity] is set, static bodies don't move themselves but affect touching bodies as if they were moving. This is useful for simulating conveyor belts or conveyor wheels.
class_name StaticBody2D


#desc The body's constant angular velocity. This does not rotate the body, but affects touching bodies, as if it were rotating.
var constant_angular_velocity: float;

#desc The body's constant linear velocity. This does not move the body, but affects touching bodies, as if it were moving.
var constant_linear_velocity: Vector2;

#desc The physics material override for the body.
#desc If a material is assigned to this property, it will be used instead of any other physics material, such as an inherited one.
var physics_material_override: PhysicsMaterial;




