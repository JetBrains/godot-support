#brief Editor-only helper for setting up root motion in [AnimationTree].
#desc [i]Root motion[/i] refers to an animation technique where a mesh's skeleton is used to give impulse to a character. When working with 3D animations, a popular technique is for animators to use the root skeleton bone to give motion to the rest of the skeleton. This allows animating characters in a way where steps actually match the floor below. It also allows precise interaction with objects during cinematics. See also [AnimationTree].
#desc [b]Note:[/b] [RootMotionView] is only visible in the editor. It will be hidden automatically in the running project.
class_name RootMotionView


#desc Path to an [AnimationTree] node to use as a basis for root motion.
var animation_path: NodePath;

#desc The grid's cell size in 3D units.
var cell_size: float;

#desc The grid's color.
var color: Color;

#desc The grid's radius in 3D units. The grid's opacity will fade gradually as the distance from the origin increases until this [member radius] is reached.
var radius: float;

#desc If [code]true[/code], the grid's points will all be on the same Y coordinate ([i]local[/i] Y = 0). If [code]false[/code], the points' original Y coordinate is preserved.
var zero_y: bool;




