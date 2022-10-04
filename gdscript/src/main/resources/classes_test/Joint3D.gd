#brief Base class for all 3D joints.
#desc Joints are used to bind together two physics bodies. They have a solver priority and can define if the bodies of the two attached nodes should be able to collide with each other. See also [Generic6DOFJoint3D].
class_name Joint3D


#desc If [code]true[/code], the two bodies of the nodes are not able to collide with each other.
var exclude_nodes_from_collision: bool;

#desc The node attached to the first side (A) of the joint.
var node_a: NodePath;

#desc The node attached to the second side (B) of the joint.
var node_b: NodePath;

#desc The priority used to define which solver is executed first for multiple joints. The lower the value, the higher the priority.
var solver_priority: int;




