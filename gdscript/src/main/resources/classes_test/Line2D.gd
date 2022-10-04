#brief A 2D line.
#desc A line through several points in 2D space.
class_name Line2D

#desc The line's joints will be pointy. If [code]sharp_limit[/code] is greater than the rotation of a joint, it becomes a bevel joint instead.
const LINE_JOINT_SHARP = 0;

#desc The line's joints will be bevelled/chamfered.
const LINE_JOINT_BEVEL = 1;

#desc The line's joints will be rounded.
const LINE_JOINT_ROUND = 2;

#desc Don't draw a line cap.
const LINE_CAP_NONE = 0;

#desc Draws the line cap as a box.
const LINE_CAP_BOX = 1;

#desc Draws the line cap as a circle.
const LINE_CAP_ROUND = 2;

#desc Takes the left pixels of the texture and renders it over the whole line.
const LINE_TEXTURE_NONE = 0;

#desc Tiles the texture over the line. [member CanvasItem.texture_repeat] of the [Line2D] node must be [constant CanvasItem.TEXTURE_REPEAT_ENABLED] or [constant CanvasItem.TEXTURE_REPEAT_MIRROR] for it to work properly.
const LINE_TEXTURE_TILE = 1;

#desc Stretches the texture across the line. [member CanvasItem.texture_repeat] of the [Line2D] node must be [constant CanvasItem.TEXTURE_REPEAT_DISABLED] for best results.
const LINE_TEXTURE_STRETCH = 2;


#desc If [code]true[/code], the line's border will be anti-aliased.
#desc [b]Note:[/b] Line2D is not accelerated by batching when being anti-aliased.
var antialiased: bool;

#desc Controls the style of the line's first point. Use [enum LineCapMode] constants.
var begin_cap_mode: int;

#desc The line's color. Will not be used if a gradient is set.
var default_color: Color;

#desc Controls the style of the line's last point. Use [enum LineCapMode] constants.
var end_cap_mode: int;

#desc The gradient is drawn through the whole line from start to finish. The default color will not be used if a gradient is set.
var gradient: Gradient;

#desc The style for the points between the start and the end.
var joint_mode: int;

#desc The points that form the lines. The line is drawn between every point set in this array. Points are interpreted as local vectors.
var points: PackedVector2Array;

#desc The smoothness of the rounded joints and caps. Higher values result in smoother corners, but are more demanding to render and update. This is only used if a cap or joint is set as round.
#desc [b]Note:[/b] The default value is tuned for lines with the default [member width]. For thin lines, this value should be reduced to a number between [code]2[/code] and [code]4[/code] to improve performance.
var round_precision: int;

#desc The direction difference in radians between vector points. This value is only used if [member joint_mode] is set to [constant LINE_JOINT_SHARP].
var sharp_limit: float;

#desc The texture used for the line's texture. Uses [code]texture_mode[/code] for drawing style.
var texture: Texture2D;

#desc The style to render the [code]texture[/code] on the line. Use [enum LineTextureMode] constants.
var texture_mode: int;

#desc The line's width.
var width: float;

#desc The line's width varies with the curve. The original width is simply multiply by the value of the Curve.
var width_curve: Curve;



#desc Adds a point with the specified [param position] relative to the line's own position. Appends the new point at the end of the point list.
#desc If [param index] is given, the new point is inserted before the existing point identified by index [param index]. Every existing point starting from [param index] is shifted further down the list of points. The index must be greater than or equal to [code]0[/code] and must not exceed the number of existing points in the line. See [method get_point_count].
func add_point(position: Vector2, index: int) -> void:
	pass;

#desc Removes all points from the line.
func clear_points() -> void:
	pass;

#desc Returns the number of points in the line.
func get_point_count() -> int:
	pass;

#desc Returns the position of the point at index [param index].
func get_point_position(index: int) -> Vector2:
	pass;

#desc Removes the point at index [param index] from the line.
func remove_point(index: int) -> void:
	pass;

#desc Overwrites the position of the point at index [param index] with the supplied [param position].
func set_point_position(index: int, position: Vector2) -> void:
	pass;


