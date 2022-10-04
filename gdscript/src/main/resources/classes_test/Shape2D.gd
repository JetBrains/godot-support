#brief Base class for all 2D shapes.
#desc Base class for all 2D shapes. All 2D shape types inherit from this.
class_name Shape2D


#desc The shape's custom solver bias. Defines how much bodies react to enforce contact separation when this shape is involved.
#desc When set to [code]0[/code], the default value from [member ProjectSettings.physics/2d/solver/default_contact_bias] is used.
var custom_solver_bias: float;



#desc Returns [code]true[/code] if this shape is colliding with another.
#desc This method needs the transformation matrix for this shape ([param local_xform]), the shape to check collisions with ([param with_shape]), and the transformation matrix of that shape ([param shape_xform]).
func collide(local_xform: Transform2D, with_shape: Shape2D, shape_xform: Transform2D) -> bool:
	pass;

#desc Returns a list of contact point pairs where this shape touches another.
#desc If there are no collisions, the returned list is empty. Otherwise, the returned list contains contact points arranged in pairs, with entries alternating between points on the boundary of this shape and points on the boundary of [param with_shape].
#desc A collision pair A, B can be used to calculate the collision normal with [code](B - A).normalized()[/code], and the collision depth with [code](B - A).length()[/code]. This information is typically used to separate shapes, particularly in collision solvers.
#desc This method needs the transformation matrix for this shape ([param local_xform]), the shape to check collisions with ([param with_shape]), and the transformation matrix of that shape ([param shape_xform]).
func collide_and_get_contacts(local_xform: Transform2D, with_shape: Shape2D, shape_xform: Transform2D) -> PackedVector2Array:
	pass;

#desc Returns whether this shape would collide with another, if a given movement was applied.
#desc This method needs the transformation matrix for this shape ([param local_xform]), the movement to test on this shape ([param local_motion]), the shape to check collisions with ([param with_shape]), the transformation matrix of that shape ([param shape_xform]), and the movement to test onto the other object ([param shape_motion]).
func collide_with_motion(local_xform: Transform2D, local_motion: Vector2, with_shape: Shape2D, shape_xform: Transform2D, shape_motion: Vector2) -> bool:
	pass;

#desc Returns a list of contact point pairs where this shape would touch another, if a given movement was applied.
#desc If there would be no collisions, the returned list is empty. Otherwise, the returned list contains contact points arranged in pairs, with entries alternating between points on the boundary of this shape and points on the boundary of [param with_shape].
#desc A collision pair A, B can be used to calculate the collision normal with [code](B - A).normalized()[/code], and the collision depth with [code](B - A).length()[/code]. This information is typically used to separate shapes, particularly in collision solvers.
#desc This method needs the transformation matrix for this shape ([param local_xform]), the movement to test on this shape ([param local_motion]), the shape to check collisions with ([param with_shape]), the transformation matrix of that shape ([param shape_xform]), and the movement to test onto the other object ([param shape_motion]).
func collide_with_motion_and_get_contacts(local_xform: Transform2D, local_motion: Vector2, with_shape: Shape2D, shape_xform: Transform2D, shape_motion: Vector2) -> PackedVector2Array:
	pass;

#desc Draws a solid shape onto a [CanvasItem] with the [RenderingServer] API filled with the specified [param color]. The exact drawing method is specific for each shape and cannot be configured.
func draw(canvas_item: RID, color: Color) -> void:
	pass;


