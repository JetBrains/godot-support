#brief Base node for all joint constraints in 2D physics.
#desc Base node for all joint constraints in 2D physics. Joints take 2 bodies and apply a custom constraint.
class_name Joint2D


#desc When [member node_a] and [member node_b] move in different directions the [code]bias[/code] controls how fast the joint pulls them back to their original position. The lower the [code]bias[/code] the more the two bodies can pull on the joint.
#desc When set to [code]0[/code], the default value from [member ProjectSettings.physics/2d/solver/default_constraint_bias] is used.
var bias: float;

#desc If [code]true[/code], [member node_a] and [member node_b] can not collide.
var disable_collision: bool;

#desc The first body attached to the joint. Must derive from [PhysicsBody2D].
var node_a: NodePath;

#desc The second body attached to the joint. Must derive from [PhysicsBody2D].
var node_b: NodePath;




