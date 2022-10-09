extends Node2D
#brief Joint used with [Skeleton2D] to control and animate other nodes.
#desc Use a hierarchy of [code]Bone2D[/code] bound to a [Skeleton2D] to control, and animate other [Node2D] nodes.
#desc You can use [code]Bone2D[/code] and [code]Skeleton2D[/code] nodes to animate 2D meshes created with the Polygon 2D UV editor.
#desc Each bone has a [member rest] transform that you can reset to with [method apply_rest]. These rest poses are relative to the bone's parent.
#desc If in the editor, you can set the rest pose of an entire skeleton using a menu option, from the code, you need to iterate over the bones to set their individual rest poses.
class_name Bone2D


#desc Rest transform of the bone. You can reset the node's transforms to this value using [method apply_rest].
var rest: Transform2D;



#desc Stores the node's current transforms in [member rest].
func apply_rest() -> void:
	pass;

#desc Returns whether this [code]Bone2D[/code] node is going to autocalculate its length and bone angle using its first [code]Bone2D[/code] child node, if one exists. If there are no [code]Bone2D[/code] children, then it cannot autocalculate these values and will print a warning.
func get_autocalculate_length_and_angle() -> bool:
	pass;

#desc Returns the angle of the bone in the [code]Bone2D[/code] node.
#desc [b]Note:[/b] This is different from the [code]Bone2D[/code]'s rotation. The bone angle is the rotation of the bone shown by the [code]Bone2D[/code] gizmo, and because [code]Bone2D[/code] bones are based on positions, this can vary from the actual rotation of the [code]Bone2D[/code] node.
func get_bone_angle() -> float:
	pass;

#desc Deprecated. Please use  [code]get_length[/code] instead.
func get_default_length() -> float:
	pass;

#desc Returns the node's index as part of the entire skeleton. See [Skeleton2D].
func get_index_in_skeleton() -> int:
	pass;

#desc Returns the length of the bone in the [code]Bone2D[/code] node.
func get_length() -> float:
	pass;

#desc Returns the node's [member rest] [code]Transform2D[/code] if it doesn't have a parent, or its rest pose relative to its parent.
func get_skeleton_rest() -> Transform2D:
	pass;

#desc When set to [code]true[/code], the [code]Bone2D[/code] node will attempt to automatically calculate the bone angle and length using the first child [code]Bone2D[/code] node, if one exists. If none exist, the [code]Bone2D[/code] cannot automatically calculate these values and will print a warning.
func set_autocalculate_length_and_angle(auto_calculate: bool) -> void:
	pass;

#desc Sets the bone angle for the [code]Bone2D[/code] node. This is typically set to the rotation from the [code]Bone2D[/code] node to a child [code]Bone2D[/code] node.
#desc [b]Note:[/b] This is different from the [code]Bone2D[/code]'s rotation. The bone angle is the rotation of the bone shown by the [code]Bone2D[/code] gizmo, and because [code]Bone2D[/code] bones are based on positions, this can vary from the actual rotation of the [code]Bone2D[/code] node.
func set_bone_angle(angle: float) -> void:
	pass;

#desc Deprecated. Please use [code]set_length[/code] instead.
func set_default_length(default_length: float) -> void:
	pass;

#desc Sets the length of the bone in the [code]Bone2D[/code] node.
func set_length(length: float) -> void:
	pass;


