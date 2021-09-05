extends Node3D
class_name Joint3D


var collision/exclude_nodes: bool setget set_exclude_nodes_from_collision, get_exclude_nodes_from_collision;
var nodes/node_a: NodePath setget set_node_a, get_node_a;
var nodes/node_b: NodePath setget set_node_b, get_node_b;
var solver/priority: int setget set_solver_priority, get_solver_priority;

