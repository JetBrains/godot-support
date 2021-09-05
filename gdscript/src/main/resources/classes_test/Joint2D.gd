extends Node2D
class_name Joint2D


var bias: float setget set_bias, get_bias;
var disable_collision: bool setget set_exclude_nodes_from_collision, get_exclude_nodes_from_collision;
var node_a: NodePath setget set_node_a, get_node_a;
var node_b: NodePath setget set_node_b, get_node_b;

