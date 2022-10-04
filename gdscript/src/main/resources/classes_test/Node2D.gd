#brief A 2D game object, inherited by all 2D-related nodes. Has a position, rotation, scale, and Z index.
#desc A 2D game object, with a transform (position, rotation, and scale). All 2D nodes, including physics objects and sprites, inherit from Node2D. Use Node2D as a parent node to move, scale and rotate children in a 2D project. Also gives control of the node's render order.
class_name Node2D


#desc Global position.
var global_position: Vector2;

#desc Global rotation in radians.
var global_rotation: float;

#desc Global scale.
var global_scale: Vector2;

#desc Global skew in radians.
var global_skew: float;

#desc Global [Transform2D].
var global_transform: Transform2D;

#desc Position, relative to the node's parent.
var position: Vector2;

#desc Rotation in radians, relative to the node's parent.
var rotation: float;

#desc The node's scale. Unscaled value: [code](1, 1)[/code].
#desc [b]Note:[/b] Negative X scales in 2D are not decomposable from the transformation matrix. Due to the way scale is represented with transformation matrices in Godot, negative scales on the X axis will be changed to negative scales on the Y axis and a rotation of 180 degrees when decomposed.
var scale: Vector2;

var skew: float;

#desc Local [Transform2D].
var transform: Transform2D;

#desc If [code]true[/code], child nodes with the lowest Y position are drawn before those with a higher Y position. If [code]false[/code], Y-sorting is disabled. Y-sorting only affects children that inherit from [CanvasItem].
#desc You can nest nodes with Y-sorting. Child Y-sorted nodes are sorted in the same space as the parent Y-sort. This feature allows you to organize a scene better or divide it into multiple ones without changing your scene tree.
var y_sort_enabled: bool;

#desc If [code]true[/code], the node's Z index is relative to its parent's Z index. If this node's Z index is 2 and its parent's effective Z index is 3, then this node's effective Z index will be 2 + 3 = 5.
var z_as_relative: bool;

#desc Z index. Controls the order in which the nodes render. A node with a higher Z index will display in front of others. Must be between [constant RenderingServer.CANVAS_ITEM_Z_MIN] and [constant RenderingServer.CANVAS_ITEM_Z_MAX] (inclusive).
var z_index: int;



#desc Multiplies the current scale by the [param ratio] vector.
func apply_scale(ratio: Vector2) -> void:
	pass;

#desc Returns the angle between the node and the [param point] in radians.
#desc [url=https://raw.githubusercontent.com/godotengine/godot-docs/master/img/node2d_get_angle_to.png]Illustration of the returned angle.[/url]
func get_angle_to(point: Vector2) -> float:
	pass;

#desc Returns the [Transform2D] relative to this node's parent.
func get_relative_transform_to_parent(parent: Node) -> Transform2D:
	pass;

#desc Adds the [param offset] vector to the node's global position.
func global_translate(offset: Vector2) -> void:
	pass;

#desc Rotates the node so it points towards the [param point], which is expected to use global coordinates.
func look_at(point: Vector2) -> void:
	pass;

#desc Applies a local translation on the node's X axis based on the [method Node._process]'s [param delta]. If [param scaled] is [code]false[/code], normalizes the movement.
func move_local_x(delta: float, scaled: bool) -> void:
	pass;

#desc Applies a local translation on the node's Y axis based on the [method Node._process]'s [param delta]. If [param scaled] is [code]false[/code], normalizes the movement.
func move_local_y(delta: float, scaled: bool) -> void:
	pass;

#desc Applies a rotation to the node, in radians, starting from its current rotation.
func rotate(radians: float) -> void:
	pass;

#desc Transforms the provided local position into a position in global coordinate space. The input is expected to be local relative to the [Node2D] it is called on. e.g. Applying this method to the positions of child nodes will correctly transform their positions into the global coordinate space, but applying it to a node's own position will give an incorrect result, as it will incorporate the node's own transformation into its global position.
func to_global(local_point: Vector2) -> Vector2:
	pass;

#desc Transforms the provided global position into a position in local coordinate space. The output will be local relative to the [Node2D] it is called on. e.g. It is appropriate for determining the positions of child nodes, but it is not appropriate for determining its own position relative to its parent.
func to_local(global_point: Vector2) -> Vector2:
	pass;

#desc Translates the node by the given [param offset] in local coordinates.
func translate(offset: Vector2) -> void:
	pass;


