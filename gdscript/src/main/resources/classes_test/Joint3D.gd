extends Node3D
class_name Joint3D

var collision/exclude_nodes: bool;
var nodes/node_a: NodePath;
var nodes/node_b: NodePath;
var solver/priority: int;

